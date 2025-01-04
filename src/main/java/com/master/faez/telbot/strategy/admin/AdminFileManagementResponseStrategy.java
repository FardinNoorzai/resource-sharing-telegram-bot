package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.File;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.BookService;
import com.master.faez.telbot.services.ResourceService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminFileManagementResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    BookService bookService;
    @Autowired
    ResourceService resourceService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Delete Resource")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Yes","Back"),null,List.of("Are you sure that you want to delete the resource?","This action can't be back!"),userSession));
            stateMachine.sendEvent(USER_EVENTS.DELETE_RESOURCE);
        }else if(text.equalsIgnoreCase("Create new Resource type")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("send me the name of Resource","Use the keyboard when you are done"),userSession));
            stateMachine.sendEvent(USER_EVENTS.CREATE_RESOURCE);
        }else if (text.equalsIgnoreCase("List Resources")){
            Book book = (Book) stateMachine.getExtendedState().getVariables().get("book");
            List<String> resourceNames = resourceService.findAllByBook(book);
            resourceNames.add("Back");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,resourceNames,null,List.of("You can select one of the resources"),userSession));
            stateMachine.sendEvent(USER_EVENTS.SELECT_RESOURCE);
        }else if(text.equalsIgnoreCase("Add files")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("send me or forward me files that you wanna add","Use the keyboard when you are done"),userSession));
            stateMachine.sendEvent(USER_EVENTS.ADD_FILE);
        } else if (text.equalsIgnoreCase("List files")){
            Resource resource = (Resource) stateMachine.getExtendedState().getVariables().get("resource");
            resource = resourceService.findById(resource.getId());
            stateMachine.sendEvent(USER_EVENTS.LIST_FILES);
            List<String> files = new ArrayList<>();
            resource.getFiles().forEach((file -> {
                files.add(file.getFileName());
            }));
            files.add("Back");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,files,null,List.of("You can select one of the files"),userSession));
            stateMachine.sendEvent(USER_EVENTS.LIST_FILES);
        }else{
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_FILE_MANAGEMENT,null,List.of("select one of keys to proceed"),userSession));
        }
    }
}
