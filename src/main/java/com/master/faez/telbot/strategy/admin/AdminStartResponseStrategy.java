package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminStartResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void response(UserSession userSession) {
        ProcessedMessage processedMessage = new ProcessedMessage(this, CONSTANTS.KEYBOARD_HOME,null, List.of("you can user keyboard to navigated into different sections"),userSession);
        applicationEventPublisher.publishEvent(processedMessage);
        userSession.getStateMachine().sendEvent(USER_EVENTS.START_WAS_RECEIVED);
    }
}
