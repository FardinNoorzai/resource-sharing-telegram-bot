package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.File;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.FileService;
import com.master.faez.telbot.services.ResourceService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminFileSelectionResponseStrategy implements ResponseStrategy {
    @Autowired
    FileService fileService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    ResourceService resourceService;
    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        StateMachine<USER_STATES,USER_EVENTS> stateMachine = userSession.getStateMachine();
        if(update.hasMessage()){
            Resource resource = (Resource) stateMachine.getExtendedState().getVariables().get("resource");
            resource = resourceService.findById(resource.getId());
            stateMachine.sendEvent(USER_EVENTS.LIST_FILES);
            List<String> files = new ArrayList<>();
            resource.getFiles().forEach((file -> {
                files.add(file.getFileName());
            }));
            files.add("Back");
            if(update.getMessage().hasText() && !update.getMessage().getText().equalsIgnoreCase("Back")){
                File file = fileService.findByName(update.getMessage().getText());
                if(file != null){
                    stateMachine.getExtendedState().getVariables().put("file", file);
                    applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_FILE_SELECTED,null,List.of("You selected " + file.getFileName() + " use the keyboard to proceed"),userSession));
                    stateMachine.sendEvent(USER_EVENTS.FILE_SELECTED);
                }else{
                    applicationEventPublisher.publishEvent(new ProcessedMessage(this, files,null,List.of("Use the keyboard to proceed"),userSession));
                }
            }else{
                applicationEventPublisher.publishEvent(new ProcessedMessage(this,files,null, List.of("Use the keyboard to proceed"),userSession));
            }
        }
    }
}
