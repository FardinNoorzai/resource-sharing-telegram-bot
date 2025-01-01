package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.BookService;
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
public class AdminBookManagementResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    BookService bookService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("List Books")){
            List<String> bookNames = bookService.findAllBooksNames();
            bookNames.add("Back");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,bookNames,null,List.of("You can select one of the books"),userSession));
            stateMachine.sendEvent(USER_EVENTS.BOOK_LIST_SELECTED);
        }else if(text.equalsIgnoreCase("Create New Book")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("send me the name of book","Use the keyboard when you are done"),userSession));
            stateMachine.sendEvent(USER_EVENTS.CREATE_BOOK);
        }else{
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_BOOK_MANAGEMENT, null,List.of("select one of keys to proceed"),userSession));
        }
    }

}
