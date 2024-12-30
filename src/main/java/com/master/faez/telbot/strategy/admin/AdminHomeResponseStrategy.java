package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@Service
public class AdminHomeResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        String text = update.getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Books")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_BOOK_MANAGEMENT, null,List.of("select one of keys to proceed"),userSession));
            stateMachine.sendEvent(USER_EVENTS.SELECT_BOOK);
        } else if (text.equalsIgnoreCase("Broadcast Message")) {

        } else {
            ProcessedMessage processedMessage = new ProcessedMessage(this, CONSTANTS.KEYBOARD_HOME, null, List.of("you can user keyboard to navigated into different sections"), userSession);
            applicationEventPublisher.publishEvent(processedMessage);
        }
    }
}
