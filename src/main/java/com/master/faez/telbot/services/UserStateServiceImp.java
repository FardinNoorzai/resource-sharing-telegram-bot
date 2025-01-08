package com.master.faez.telbot.services;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.DeleteAdminEvent;
import com.master.faez.telbot.core.NewAdminEvent;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.state.StateMachineConfig;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
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
    BookService bookService;

    @Autowired
    StateMachineService stateMachineService;

    @Autowired
    StateMachineConfig stateMachineConfig;

    @Autowired
    StateMachinePersister<USER_STATES,USER_EVENTS,String> persist;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public UserSession getCurrentSession(Update update) {
        String id = update.getMessage().getFrom().getId().toString();
        UserSession session = getSessionFromContext(update.getMessage().getFrom().getId());
        if(session != null){
            session.setUpdate(update);
            return session;
        }
        if(ifStateMachineExist(id)){
            User user = loadUserFromDatabase(update.getMessage().getFrom().getId());

            //creating state machine based on the user role
            StateMachine<USER_STATES,USER_EVENTS> statemachine = null;
            if(user != null){
                if(user.getUserRole() == USER_ROLE.ADMIN){
                    statemachine = stateMachineConfig.newAdminStateMachine(id);
                }else{
                    statemachine = stateMachineConfig.newUserStateMachine(id);
                }
            }


            try {
                StateMachine<USER_STATES,USER_EVENTS> machine = persist.restore(statemachine,id);
                machine.start();
                return getUserSession(update, statemachine, user, machine);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            User user = createUser(update);
            StateMachine<USER_STATES,USER_EVENTS> statemachine = null;

            //creating state machine based on the user role
            if(user != null){
                if(user.getUserRole() == USER_ROLE.ADMIN){
                    statemachine = stateMachineConfig.newAdminStateMachine(id);
                }else{
                    statemachine = stateMachineConfig.newUserStateMachine(id);
                }
            }


            statemachine.start();
            try {
                persist.persist(statemachine,id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return getUserSession(update, statemachine, user, statemachine);

        }

    }

    private UserSession getUserSession(Update update, StateMachine<USER_STATES, USER_EVENTS> statemachine, User user, StateMachine<USER_STATES, USER_EVENTS> machine) {
        UserSession userSession = UserSession.builder()
                .user(user)
                .stateMachine(machine)
                .update(update)
                .states(new Stack<>())
                .build();
        StateMachineListener stateMachineListener = new StateMachineListener();
        stateMachineListener.setUserSession(userSession);
        stateMachineListener.setPersist(persist);
        statemachine.addStateListener(stateMachineListener);
        sessions.add(userSession);
        return userSession;
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
                .userRole(USER_ROLE.USER)
                .build();
        if(update.getMessage().getFrom().getId().toString().equals("485898907")){
            user.setUserRole(USER_ROLE.ADMIN);
        }
        log.warn("create and saving the user to the database");
        return userService.saveUser(user);
    }

    public boolean ifStateMachineExist(String id){
        return stateMachineService.exist(id);
    }
    @EventListener(NewAdminEvent.class)
    private void promoteToAdmin(NewAdminEvent event){
        resetState(USER_ROLE.ADMIN, event.getId());
    }


    @EventListener(DeleteAdminEvent.class)
    private void deleteAdmin(DeleteAdminEvent event){
        resetState(USER_ROLE.USER,event.getId());
    }

    public void resetState(USER_ROLE role,Long userId) {
        UserSession session = getSessionFromContext(userId);
        User user = null;
        String text = "";
        if(session != null){
            sessions.remove(session);
            user = updateUserRole(userId,role);
            stateMachineService.deleteById(userId);
        }else{
            user = updateUserRole(userId,role);
            stateMachineService.deleteById(userId);
        }
        StateMachine<USER_STATES,USER_EVENTS> statemachine = null;
        if(role == USER_ROLE.ADMIN){
            statemachine = stateMachineConfig.newAdminStateMachine(userId.toString());
            text = "You were promoted to admin!\nNow you can use the keyboard to navigate into different sections";
        }else{
            statemachine = stateMachineConfig.newUserStateMachine(userId.toString());
            text = "You were removed from being admin\nNow you can use the keyboard to navigate into different sections";
        }
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setStates(new Stack<>());
        userSession.setStateMachine(statemachine);
        StateMachineListener stateMachineListener = new StateMachineListener();
        stateMachineListener.setUserSession(userSession);
        stateMachineListener.setPersist(persist);
        statemachine.addStateListener(stateMachineListener);
        statemachine.start();
        sessions.add(userSession);
        if(role == USER_ROLE.USER){
            List<String> bookNames = bookService.findAllBooksNames();
            bookNames.add("About Us");
            eventPublisher.publishEvent(new ProcessedMessage(this, bookNames,null,List.of(text),userSession));

        }else{
            eventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_HOME,null,List.of(text),userSession));
        }
    }
    public User updateUserRole(Long id,USER_ROLE role){
        User user = userService.findUserById(id);
        user.setUserRole(role);
        return userService.saveUser(user);
    }

}
