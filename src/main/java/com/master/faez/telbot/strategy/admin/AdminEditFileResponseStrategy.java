package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.File;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.FileService;
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
public class AdminEditFileResponseStrategy implements ResponseStrategy {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    FileService fileService;

    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        File file = (File) stateMachine.getExtendedState().getVariables().get("file");
        if(file != null) {
            file.setFileName(update.getMessage().getText());
            fileService.save(file);
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("The file name was updated use the keyboard to go back!"),userSession));
        }
    }
}
