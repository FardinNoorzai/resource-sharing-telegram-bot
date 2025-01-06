package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
public class AdminDeleteFileResponseStrategy implements ResponseStrategy {

    @Autowired
    FileService fileService;
    @Autowired
    ResourceService resourceService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> machine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Yes")){
            File file = (File) machine.getExtendedState().getVariables().get("file");
            System.out.println(file.getId());
            fileService.delete(file);
            Resource resource = (Resource) machine.getExtendedState().getVariables().get("resource");
            resource = resourceService.findById(resource.getId());
            List<String> files = new ArrayList<>();
            resource.getFiles().forEach((f -> {
                files.add(f.getFileName());
            }));
            files.add("back");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("file with name: "+ file.getFileName()+" was deleted","you are returned back to file selection menu"),userSession));
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,files,null, List.of("Use the keyboard to proceed"),userSession));
            machine.sendEvent(USER_EVENTS.DELETE_FILE);
            Stack<USER_STATES> stateStack = userSession.getStates();
            while (!stateStack.empty()) {
                if(stateStack.peek() != USER_STATES.FILE_LIST){
                    stateStack.pop();
                }
                if(stateStack.peek() == USER_STATES.FILE_LIST && !stateStack.empty()){
                    stateStack.pop();
                    break;
                }
            }
    }

    }

}
