package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.BookService;
import com.master.faez.telbot.state.PreviousStateEvent;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminBookResponseStrategy implements ResponseStrategy {

    @Autowired
    BookService bookService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void response(UserSession userSession) {
        if(userSession.getUpdate().getMessage().getText().trim().equals(CONSTANTS.CREATE_NEW_BOOK)){
            ProcessedMessage processedMessage = new ProcessedMessage(this,CONSTANTS.KEYBOARD_CANCEL,null, List.of("Now you can send me name of the books which you wanna create\nPress Cancel to go back"),userSession);
            applicationEventPublisher.publishEvent(processedMessage);
            StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
            stateMachine.sendEvent(USER_EVENTS.CREATE_BOOK_SELECTED_FROM_BOOK_KEYBOARD);
        }else if(userSession.getUpdate().getMessage().getText().trim().equals(CONSTANTS.LIST_BOOKS)){
            ProcessedMessage processedMessage = new ProcessedMessage(this,bookService.findAllBooksNames(),null, List.of("Select one of the books"),userSession);
            applicationEventPublisher.publishEvent(processedMessage);
            StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
            stateMachine.sendEvent(USER_EVENTS.LIST_BOOKS_SELECTED_FROM_BOOK_KEYBOARD);
        }else if(userSession.getUpdate().getMessage().getText().trim().equals("Back")){
            applicationEventPublisher.publishEvent(new PreviousStateEvent(this,userSession));
        }
    }
}
