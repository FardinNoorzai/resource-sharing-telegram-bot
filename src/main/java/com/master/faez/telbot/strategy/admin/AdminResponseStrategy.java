package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminResponseStrategy implements ResponseStrategy {
    AdminStartResponseStrategy adminStartResponseStrategy;


    @Override
    public void response(UserSession userSession) {
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        USER_STATES state = stateMachine.getState().getId();
        switch (state){
            case START -> adminStartResponseStrategy.response(userSession);
            case HOME -> adminStartResponseStrategy.response(userSession);
            case BOOK_MANAGEMENT -> adminStartResponseStrategy.response(userSession);
        }
    }
}
