package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.ResourceService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminCreateNewResourceTypeResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    ResourceService resourceService;

    @Override
    public void response(UserSession userSession) {
        StateMachine<USER_STATES, USER_EVENTS> machine = userSession.getStateMachine();
        Book book = (Book) machine.getExtendedState().getVariables().get("book");
        String text = userSession.getUpdate().getMessage().getText();
        Resource resource = Resource.builder().name(text).book(book).build();
        resourceService.save(resource);
        applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Back"),null,List.of("Resource with the id of "+ resource.getId() + " and name of "+resource.getName()+" was saved!"),userSession));
    }
}
