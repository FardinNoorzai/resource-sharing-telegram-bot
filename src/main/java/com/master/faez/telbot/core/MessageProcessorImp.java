package com.master.faez.telbot.core;

import com.master.faez.telbot.services.UserStateService;
import com.master.faez.telbot.strategy.RoleBasedResponseStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageProcessorImp implements MessageProcessor {
    @Autowired
    UserStateService userStateService;
    @Autowired
    RoleBasedResponseStrategy responseStrategy;

    @Override
    public void processMessage(Update update) {
        UserSession userSession  = userStateService.getCurrentSession(update);
        responseStrategy.response(userSession);
    }
}
