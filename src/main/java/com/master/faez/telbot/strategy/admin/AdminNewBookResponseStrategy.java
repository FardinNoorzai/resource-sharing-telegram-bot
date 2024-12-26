package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.repositories.BookRepository;
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
public class AdminNewBookResponseStrategy implements ResponseStrategy {
    @Autowired
    BookService bookService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        userSession.getStates().push(userSession.getStateMachine().getState().getId());
        if(text.equals(CONSTANTS.CANCEL)){
            System.out.println(userSession.getStates().size());
            System.out.println(userSession.getStates().peek());
            applicationEventPublisher.publishEvent(new PreviousStateEvent(this, userSession));
        }else{
            Book book = new Book();
            book.setName(text);
            bookService.save(book);
            ProcessedMessage processedMessage = new ProcessedMessage(this,CONSTANTS.KEYBOARD_CANCEL,null, List.of("Enter the next book name or press cancel to go back"),userSession);
            applicationEventPublisher.publishEvent(processedMessage);
        }
    }
}
