package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
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

import java.util.List;

@Service
public class AdminResourceManagementResponseStrategy implements ResponseStrategy {
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
        if(text.equalsIgnoreCase("Delete book")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Yes","Back"),null,List.of("Are you sure that you want to delete the book?","This action can't be back!"),userSession));
            stateMachine.sendEvent(USER_EVENTS.DELETE_BOOK);
        }else if(text.equalsIgnoreCase("Create new Resource type")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("send me the name of Resource","Use the keyboard when you are done"),userSession));
            stateMachine.sendEvent(USER_EVENTS.CREATE_RESOURCE);
        }else if (text.equalsIgnoreCase("List Resources")){
            Book book = (Book) stateMachine.getExtendedState().getVariables().get("book");
            List<String> resourceNames = resourceService.findAllByBook(book);
            resourceNames.add("Back");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,resourceNames,null,List.of("You can select one of the resources"),userSession));
            stateMachine.sendEvent(USER_EVENTS.SELECT_RESOURCE);
        }else if (text.equalsIgnoreCase("Edit Book")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("Send me the new name for this book"),userSession));
            stateMachine.sendEvent(USER_EVENTS.EDIT_BOOK);
        }else{
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_RESOURCE_MANAGEMENT,null,List.of("select one of keys to proceed"),userSession));
        }
    }
}
