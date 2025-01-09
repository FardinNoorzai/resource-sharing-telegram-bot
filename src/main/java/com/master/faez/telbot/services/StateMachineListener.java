package com.master.faez.telbot.services;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import lombok.Setter;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Setter
public class StateMachineListener extends StateMachineListenerAdapter<USER_STATES, USER_EVENTS> {
    UserSession userSession;
    StateMachinePersister<USER_STATES, USER_EVENTS, String> persist;

    @Override
    public void stateChanged(State<USER_STATES, USER_EVENTS> from, State<USER_STATES, USER_EVENTS> to) {
        Stack<USER_STATES> states = userSession.getStates();
        if(from != null){
            states.push(from.getId());
            try {
                persist.persist(userSession.getStateMachine(),userSession.getUser().getId().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        super.stateChanged(from, to);
    }

    @Override
    public void stateMachineStarted(StateMachine<USER_STATES, USER_EVENTS> stateMachine) {
        try {
            persist.persist(userSession.getStateMachine(),userSession.getUser().getId().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.stateMachineStarted(stateMachine);
    }

}
