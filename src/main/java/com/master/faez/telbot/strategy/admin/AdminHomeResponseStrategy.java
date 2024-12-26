package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.strategy.ResponseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminHomeResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void response(UserSession userSession) {
        String message = userSession.getUpdate().getMessage().getText();
        userSession.getStates().push(userSession.getStateMachine().getState().getId());
        if(message.trim().equals(CONSTANTS.BOOKS)){
            ProcessedMessage processedMessage = new ProcessedMessage(this,CONSTANTS.KEYBOARD_BOOK,null, List.of("Chose an option from the keyboard"),userSession);
            applicationEventPublisher.publishEvent(processedMessage);
            userSession.getStateMachine().sendEvent(USER_EVENTS.BOOK_SELECTED_FROM_HOME_KEYBOARD);
        }else{
            ProcessedMessage processedMessage = new ProcessedMessage(this,CONSTANTS.KEYBOARD_HOME,null,CONSTANTS.ERROR_INVALID_KEYBOARD_KEY,userSession);
            applicationEventPublisher.publishEvent(processedMessage);
        }
    }
}
