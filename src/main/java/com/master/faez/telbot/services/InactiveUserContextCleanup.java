package com.master.faez.telbot.services;

import com.master.faez.telbot.core.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InactiveUserContextCleanup {
    @Autowired
    UserStateServiceImp userStateService;
    @Scheduled(fixedDelay = 43200000)
    public void cleanUp() {
        System.out.println("Cleaning up the context");
        Long inactivityThreshold = 1000l * 60l * 60l * 24l;
        Long now = System.currentTimeMillis();
        Map<String, UserSession> context = userStateService.getContext();
        for (Map.Entry<String, UserSession> entry : context.entrySet()) {
            UserSession userSession = entry.getValue();
            if(now - userSession.getLastActiveTime() > inactivityThreshold) {
                context.remove(entry.getKey());
                System.out.println("removing " + entry.getKey() + " from the context");
            }
        }
    }
}
