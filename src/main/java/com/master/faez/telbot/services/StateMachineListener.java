package com.master.faez.telbot.services;

import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class StateMachineListener extends StateMachineListenerAdapter<USER_STATES, USER_EVENTS> {
    @Override
    public void stateChanged(State<USER_STATES, USER_EVENTS> from, State<USER_STATES, USER_EVENTS> to) {
        super.stateChanged(from, to);
    }

    @Override
    public void stateMachineStarted(StateMachine<USER_STATES, USER_EVENTS> stateMachine) {
        super.stateMachineStarted(stateMachine);
    }

}
