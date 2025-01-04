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
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class AdminFileSelectedResponseStrategy implements ResponseStrategy {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        StateMachine<USER_STATES,USER_EVENTS> machine = userSession.getStateMachine();
        if(update.getMessage().getText().equalsIgnoreCase("Edit")){
            machine.sendEvent(USER_EVENTS.EDIT_FILE);
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Back"),null,List.of("Send me the new name use keyboard when you are done!"),userSession));
        }else if(update.getMessage().getText().equalsIgnoreCase("Delete")){
            machine.sendEvent(USER_EVENTS.DELETE_BOOK);
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Yes","Back"),null,List.of("Are you sure you want to delete the file?"),userSession));

        }else{
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_FILE_SELECTED,null,List.of("use the keyboard to proceed"),userSession));

        }
    }
}
