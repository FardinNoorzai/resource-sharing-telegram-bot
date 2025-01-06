package com.master.faez.telbot.strategy.user;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserResponseStrategy implements ResponseStrategy {
    Map<USER_STATES,ResponseStrategy> responseStrategies = new HashMap<>();
    @Autowired
    UserStartResponseStrategy userStartResponseStrategy;
    @Autowired
    UserBookListResponseStrategy userBookListResponseStrategy;
    @Autowired
    UserResourceListResponseStrategy userResourceListResponseStrategy;
    @Override
    public void response(UserSession userSession) {
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        USER_STATES state = stateMachine.getState().getId();
        ResponseStrategy responseStrategy = responseStrategies.get(state);
        System.out.println(userSession.getUpdate());
        if(responseStrategy != null) {
            responseStrategy.response(userSession);
        }
    }

    @PostConstruct
    public void init() {
        responseStrategies.put(USER_STATES.START,userStartResponseStrategy);
        responseStrategies.put(USER_STATES.BOOK_LIST,userBookListResponseStrategy);
        responseStrategies.put(USER_STATES.RESOURCE_LIST,userResourceListResponseStrategy);
    }
}
