package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.BookService;
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
public class AdminResourceListResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    ResourceService resourceService;

    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> machine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Back")){
            List<String> bookNames = resourceService.findAllBooksNames();
            bookNames.add("Back");
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,bookNames,null,List.of("You can select one of the Resources"),userSession));
        }else{
            Resource resource = resourceService.findByName(text);
            if(resource == null){
                System.out.println("resource was not found");
                applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null,List.of("Use the keyboard!"),userSession));
            }else{
                System.out.println("resource name is "+ resource.getName());
                applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_FILE_MANAGEMENT,null,List.of("You selected Resource with id: "+resource.getId() +" Name :"+ resource.getName(),"Use the Keyboard to proceed."),userSession));
                machine.getExtendedState().getVariables().put("resource",resource);
                machine.sendEvent(USER_EVENTS.RESOURCE_SELECTED);
            }

        }

    }
}
