package com.master.faez.telbot.services;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.state.StateMachineConfig;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
@Service
public class UserStateServiceImp implements UserStateService {
    List<UserSession> sessions = new ArrayList<>();
    @Autowired
    UserService userService;
    @Autowired
    StateMachineConfig stateMachineConfig;

    @Override
    public UserSession getCurrentSession(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        UserSession session = getSessionFromContext(userId);
        if(session == null) {
            return createSession(update);
        }
        session.setUpdate(update);
        return session;
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
    public StateMachine<USER_STATES, USER_EVENTS> createAdminStateMachine() {
        return stateMachineConfig.newAdminStateMachine();
    }




    public User loadUser(Long id){
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
                .USER_STATE(USER_STATES.START)
                .build();
        log.warn("create and saving the user to the database");
        return userService.saveUser(user);
    }
    public UserSession createSession(Update update){
        UserSession userSession = new UserSession();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = createAdminStateMachine();
        User user = loadUser(update.getMessage().getFrom().getId());
        if(user == null) {
            user = createUser(update);
            userSession.setUser(user);
            userSession.setStateMachine(stateMachine);
        }else{
            userSession.setUser(user);
            stateMachine.sendEvent(USER_EVENTS.USER_EXIST);
            userSession.setStateMachine(stateMachine);
        }
        stateMachine.start();
        sessions.add(userSession);
        return userSession;
    }
    public void previousStep(UserSession userSession){
        Stack<USER_STATES> states = userSession.getStates();
        StateMachine<USER_STATES,USER_EVENTS> machine = null;
        if(states.isEmpty()){
            machine = createAdminStateMachine();
            machine.start();
            userSession.setStateMachine(machine);
        }else{
            machine = stateMachineConfig.newCustomStepAdminStateMachine(states.pop());
            machine.start();
            userSession.setStateMachine(machine);
        }
    }

}
