package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.state.StateMachineConfig;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AdminResponseStrategy implements ResponseStrategy {
    @Autowired
    AdminStartResponseStrategy adminStartResponseStrategy;
    @Autowired
    AdminHomeResponseStrategy adminHomeResponseStrategy;
    @Autowired
    AdminBookManagementResponseStrategy adminBookManagementResponseStrategy;
    @Autowired
    AdminBookListResponseStrategy adminBookListResponseStrategy;
    @Autowired
    AdminCreateBookResponseStrategy adminCreateBookResponseStrategy;
    @Autowired
    AdminResourceManagementResponseStrategy adminResourceManagementResponseStrategy;
    @Autowired
    AdminDeleteBookResponseStrategy adminDeleteBookResponseStrategy;
    @Autowired
    AdminCreateNewResourceTypeResponseStrategy adminCreateNewResourceTypeResponseStrategy;
    @Autowired
    AdminResourceListResponseStrategy adminResourceListResponseStrategy;
    @Autowired
    AdminFileManagementResponseStrategy adminFileManagementResponseStrategy;
    @Autowired
    AdminDeleteResourceResponseStrategy adminDeleteResourceResponseStrategy;


    HashMap<USER_STATES, ResponseStrategy> responseStrategies;
    @Override
    public void response(UserSession userSession) {
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        USER_STATES state = stateMachine.getState().getId();
        ResponseStrategy responseStrategy = responseStrategies.get(state);
        if(responseStrategy != null) {
            responseStrategy.response(userSession);
        }
    }


    @PostConstruct
    public void init() {
        responseStrategies = new HashMap<>();
        responseStrategies.put(USER_STATES.START, adminStartResponseStrategy);
        responseStrategies.put(USER_STATES.HOME, adminHomeResponseStrategy);
        responseStrategies.put(USER_STATES.BOOK_MANAGEMENT, adminBookManagementResponseStrategy);
        responseStrategies.put(USER_STATES.BOOK_LIST, adminBookListResponseStrategy);
        responseStrategies.put(USER_STATES.BOOK_CREATE,adminCreateBookResponseStrategy);
        responseStrategies.put(USER_STATES.RESOURCE_MANAGEMENT, adminResourceManagementResponseStrategy);
        responseStrategies.put(USER_STATES.BOOK_DELETE, adminDeleteBookResponseStrategy);
        responseStrategies.put(USER_STATES.RESOURCE_CREATE,adminCreateNewResourceTypeResponseStrategy);
        responseStrategies.put(USER_STATES.RESOURCE_LIST, adminResourceListResponseStrategy);
        responseStrategies.put(USER_STATES.FILE_MANAGEMENT, adminFileManagementResponseStrategy);
        responseStrategies.put(USER_STATES.RESOURCE_DELETE, adminDeleteResourceResponseStrategy);

    }
}
