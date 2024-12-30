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
public class AdminDeleteBookResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    BookService bookService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> machine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Yes")){
            Book book = (Book) machine.getExtendedState().getVariables().get("book");
            bookService.deleteById(book.getId());
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null,List.of("Book with name: "+ book.getName()+" was deleted","you are returned back to book selection menu"),userSession));
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_BOOK_MANAGEMENT, null, List.of("select one of keys to proceed"),userSession));
            machine.sendEvent(USER_EVENTS.DELETE_BOOK);
        }
    }
}
