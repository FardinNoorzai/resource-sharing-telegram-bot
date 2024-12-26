package com.master.faez.telbot.strategy;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.strategy.admin.AdminResponseStrategy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RoleBasedResponseStrategy implements ResponseStrategy{
    @Autowired
    AdminResponseStrategy adminResponseStrategy;
    Map<USER_ROLE,ResponseStrategy> responseStrategies = new HashMap<>();
    @Override
    public void response(UserSession userSession) {
        ResponseStrategy responseStrategy = responseStrategies.get(userSession.getUser().getUSER_ROLE());
        if(responseStrategy != null) {
            log.warn("state {} before response", userSession.getStateMachine().getState().getId());
            responseStrategy.response(userSession);
            log.warn("state {} after response", userSession.getStateMachine().getState().getId());
        }else{
            System.out.println("response strategy not found");
        }

    }
    @PostConstruct
    public void init() {
        responseStrategies.put(USER_ROLE.ADMIN,adminResponseStrategy);
    }
}
