package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.BookService;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminCreateBookResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    BookService bookService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        Book book = Book.builder().name(text).build();
        book = bookService.save(book);
        applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Back"),null,List.of("Book with the id of "+ book.getId() + " was saved!"),userSession));
    }
}
