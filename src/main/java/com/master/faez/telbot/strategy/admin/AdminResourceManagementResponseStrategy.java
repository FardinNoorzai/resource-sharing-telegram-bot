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

import java.util.List;

@Service
public class AdminResourceManagementResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    BookService bookService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Delete book")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Yes","Back"),null,List.of("Are you sure that you want to delete the book?","This action can't be back!"),userSession));
            stateMachine.sendEvent(USER_EVENTS.DELETE_BOOK);
        }else{
            Book book = (Book)stateMachine.getExtendedState().getVariables().get("book");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_RESOURCE_MANAGEMENT,null,List.of("You selected book with id: "+book.getId() +" Name :"+ book.getName(),"Use the Keyboard to proceed."),userSession));

        }
    }
}
