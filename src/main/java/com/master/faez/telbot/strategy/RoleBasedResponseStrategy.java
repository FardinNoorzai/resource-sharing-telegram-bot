package com.master.faez.telbot.strategy;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.services.StateMachineListener;
import com.master.faez.telbot.state.StateMachineConfig;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.admin.AdminResponseStrategy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RoleBasedResponseStrategy implements ResponseStrategy{
    @Autowired
    AdminResponseStrategy adminResponseStrategy;
    Map<USER_ROLE,ResponseStrategy> responseStrategies = new HashMap<>();

    @Autowired
    StateMachinePersister<USER_STATES, USER_EVENTS,String> persist;
    @Autowired
    StateMachineConfig stateMachineConfig;

    @Override
    public void response(UserSession userSession) {
        if(userSession.getUpdate().getMessage().getText().equalsIgnoreCase("Back")){
            goBack(userSession);
        }
        ResponseStrategy responseStrategy = responseStrategies.get(userSession.getUser().getUSER_ROLE());
        if(responseStrategy != null) {
            log.warn("state {} before response", userSession.getStateMachine().getState().getId());
            responseStrategy.response(userSession);
            log.warn("state {} after response", userSession.getStateMachine().getState().getId());
        }else{
            System.out.println("response strategy not found");
        }
    }

    private void goBack(UserSession userSession) {
        StateMachine<USER_STATES, USER_EVENTS> stateMachine;
        if(userSession.getStates().isEmpty()){
            System.out.println("state stack is empty");
            stateMachine = stateMachineConfig.newAdminStateMachine(userSession.getUser().getId().toString());
        }else{
            System.out.println("state stack size is"+ " " + userSession.getStates().size());
            System.out.println("state stack is not empty and user is returning to the " + userSession.getStates().peek());

            stateMachine = stateMachineConfig.newCustomStepAdminStateMachine(userSession.getStates().pop());
        }
        StateMachineListener stateMachineListener = new StateMachineListener();
        stateMachineListener.setPersist(persist);
        stateMachineListener.setUserSession(userSession);
        stateMachine.addStateListener(stateMachineListener);
        stateMachine.getExtendedState().getVariables().putAll(userSession.getStateMachine().getExtendedState().getVariables());
        userSession.getStateMachine().stop();
        stateMachine.start();
        userSession.setStateMachine(stateMachine);
    }


    @PostConstruct
    public void init() {
        responseStrategies.put(USER_ROLE.ADMIN,adminResponseStrategy);
    }
}
