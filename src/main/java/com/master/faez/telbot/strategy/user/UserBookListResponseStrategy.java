package com.master.faez.telbot.strategy.user;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.AboutService;
import com.master.faez.telbot.services.BookService;
import com.master.faez.telbot.services.ResourceService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBookListResponseStrategy implements ResponseStrategy {

    @Autowired
    BookService bookService;
    @Autowired
    ResourceService resourceService;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    AboutService aboutService;
    @Override
    public void response(UserSession userSession) {
        if(userSession.getUpdate().hasMessage()){
            String message = userSession.getUpdate().getMessage().getText();
           if(message.equalsIgnoreCase("About us")){
               try{
                   String text = aboutService.getAllAbouts().get(0).getText();
                   eventPublisher.publishEvent(new ProcessedMessage(this,null,null,List.of(text),userSession));
               }catch (IndexOutOfBoundsException e){
                   eventPublisher.publishEvent(new ProcessedMessage(this,null,null,List.of("Sorry the information about us is not set yet try again this later"),userSession));
               }
                return;
            }
            Book book = bookService.findByName(message);
            if(book != null){
                List<String> resources = resourceService.findAllByBook(book);
                resources.add("Back");
                eventPublisher.publishEvent(new ProcessedMessage(this,resources,null,List.of("Please use the keyboard"),userSession));
                userSession.getStateMachine().getExtendedState().getVariables().put("book",book);
                userSession.getStateMachine().sendEvent(USER_EVENTS.SELECT_RESOURCE);
            }else{
                List<String> books = bookService.findAllBooksNames();
                String text = "";
                if(books.isEmpty()){
                    text = "Sorry, But no book was found :(";
                }else{
                    text = "Use the keyboard to navigate into different sections.";
                }
                books.add("About us");
                eventPublisher.publishEvent(new ProcessedMessage(this,books,null,List.of(text),userSession));}
        }

    }
}
