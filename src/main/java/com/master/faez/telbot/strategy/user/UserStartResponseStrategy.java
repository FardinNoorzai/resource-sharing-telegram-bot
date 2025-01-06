package com.master.faez.telbot.strategy.user;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.BookService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserStartResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    BookService bookService;

    @Override
    public void response(UserSession userSession) {
        List<String> bookNames = bookService.findAllBooksNames();
        String text = "";
        if(bookNames.isEmpty()){
            text = "Sorry, But no book was found :(";
        }else if (userSession.getUpdate().getMessage().getText().equalsIgnoreCase("/Start")){
            text = "You were successfully registered to our database "+userSession.getUser().getName()+"!\nNow you can use keyboard to navigated into different sections";
        }else{
            text = "Use the keyboard to navigate into different sections.";
        }
        ProcessedMessage processedMessage = new ProcessedMessage(this, bookNames,null, List.of(text),userSession);
        applicationEventPublisher.publishEvent(processedMessage);
        userSession.getStateMachine().sendEvent(USER_EVENTS.USER_EXISTS_OR_CREATED);
    }
}
