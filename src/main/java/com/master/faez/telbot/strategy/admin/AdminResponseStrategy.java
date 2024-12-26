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
    AdminHomeResponseStrategy adminHomeResponseStrategy;
    AdminStartResponseStrategy adminStartResponseStrategy;
    AdminBookResponseStrategy adminBookResponseStrategy;
    AdminNewBookResponseStrategy adminNewBookResponseStrategy;


    @Override
    public void response(UserSession userSession) {
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        USER_STATES state = stateMachine.getState().getId();
        switch (state){
            case START -> adminStartResponseStrategy.response(userSession);
            case WAITING_FOR_HOME_KEYBOARD_RESPONSE -> adminHomeResponseStrategy.response(userSession);
            case WAITING_FOR_BOOK_KEYBOARD_RESPONSE -> adminBookResponseStrategy.response(userSession);
            case WAITING_FOR_NEW_BOOK_NAME -> adminNewBookResponseStrategy.response(userSession);
        }
    }
}
