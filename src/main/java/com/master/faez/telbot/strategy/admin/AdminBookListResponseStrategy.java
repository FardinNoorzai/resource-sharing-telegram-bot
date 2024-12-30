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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class AdminBookListResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    BookService bookService;

    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> machine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Back")){
            List<String> bookNames = bookService.findAllBooksNames();
            bookNames.add("Back");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,bookNames,null,List.of("You can select one of the books"),userSession));
        }else{
            Book book = bookService.findByName(text);
            if(book == null){
                applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null,List.of("Use the keyboard!"),userSession));
            }else{
                applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_RESOURCE_MANAGEMENT,null,List.of("You selected book with id: "+book.getId() +" Name :"+ book.getName(),"Use the Keyboard to proceed."),userSession));
                machine.getExtendedState().getVariables().put("book",book);
                machine.sendEvent(USER_EVENTS.BOOK_SELECTED);
            }

        }

    }
}
