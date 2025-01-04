package com.master.faez.telbot.strategy.admin;

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
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class AdminBookEditResponseStrategy implements ResponseStrategy {

    @Autowired
    BookService bookService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        Book book = (Book) stateMachine.getExtendedState().getVariables().get("book");
        if(book != null) {
            book.setName(update.getMessage().getText());
            bookService.save(book);
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("The book name was updated use the keyboard to go back!"),userSession));
        }
    }
}
