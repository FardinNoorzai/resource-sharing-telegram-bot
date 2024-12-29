package com.master.faez.telbot.services;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.state.StateMachineConfig;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserStateServiceImp implements UserStateService {
    List<UserSession> sessions = new ArrayList<>();
    @Autowired
    UserService userService;

    @Autowired
    StateMachineService stateMachineService;

    @Autowired
    StateMachineConfig stateMachineConfig;

    @Autowired
    StateMachinePersister<USER_STATES,USER_EVENTS,String> persist;

    @Override
    public UserSession getCurrentSession(Update update) {
        String id = update.getMessage().getFrom().getId().toString();
        System.out.println(" id " + id);
        UserSession session = getSessionFromContext(update.getMessage().getFrom().getId());
        if(session != null){
            return session;
        }
        if(ifStateMachineExist(id)){
            StateMachine<USER_STATES,USER_EVENTS> statemachine = stateMachineConfig.newAdminStateMachine(id);
            User user = loadUserFromDatabase(update.getMessage().getFrom().getId());
            try {
                StateMachine<USER_STATES,USER_EVENTS> machine = persist.restore(statemachine,id);
                addListener(machine,id);
                machine.start();
                UserSession userSession = UserSession.builder()
                        .user(user)
                        .stateMachine(machine)
                        .update(update)
                        .build();
                sessions.add(userSession);
                return userSession;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            User user = createUser(update);
            StateMachine<USER_STATES,USER_EVENTS> statemachine = stateMachineConfig.newAdminStateMachine(id);
            addListener(statemachine,id);
            statemachine.start();
            try {
                persist.persist(statemachine,id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            UserSession userSession = UserSession.builder()
                    .user(user)
                    .stateMachine(statemachine)
                    .update(update)
                    .build();
            sessions.add(userSession);
            return userSession;

        }

    }

    public UserSession getSessionFromContext(Long userId) {
        for(UserSession userSession : sessions) {
            if(userSession.getUser().getId().equals(userId)) {
                log.warn("session was returned from the context");
                return userSession;
            }
        }
        return null;
    }
    public User loadUserFromDatabase(Long id){
        log.warn("loading user from database");
        return userService.findUserById(id);
    }
    public User createUser(Update update){
        Long userId = update.getMessage().getFrom().getId();
        String name = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        String username = update.getMessage().getFrom().getUserName();
        User user = User.builder()
                .name(name)
                .lastName(lastName)
                .username(username)
                .id(userId)
                .USER_ROLE(USER_ROLE.ADMIN)
                .build();
        log.warn("create and saving the user to the database");
        return userService.saveUser(user);
    }

    public boolean ifStateMachineExist(String id){
        return stateMachineService.exist(id);
    }

    public void addListener(StateMachine<USER_STATES,USER_EVENTS> machine,String id){
        machine.addStateListener(new StateMachineListener(){
            @Override
            public void stateChanged(State<USER_STATES, USER_EVENTS> from, State<USER_STATES, USER_EVENTS> to) {
                log.warn("machine with id {} has changed from {} to {}",id,from.getId().name(),to.getId().name());
                super.stateChanged(from, to);
            }

            @Override
            public void stateMachineStarted(StateMachine<USER_STATES, USER_EVENTS> stateMachine) {
                log.warn("machine with id {} has started", id);
                super.stateMachineStarted(stateMachine);
            }
        });
    }

}
