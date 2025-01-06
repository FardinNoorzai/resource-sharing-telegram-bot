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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Stack;

@Service
public class AdminDeleteResourceResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    ResourceService resourceService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> machine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Yes")){
            Resource resource = (Resource) machine.getExtendedState().getVariables().get("resource");
            if(resource == null){
                System.out.println(" resource is null");

            }else{
                System.out.println(resource.getName());
            }
            resourceService.deleteById(resource.getId());
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null,List.of("Resource with name: "+ resource.getName()+" was deleted","you are returned back to Resource selection menu"),userSession));
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_RESOURCE_MANAGEMENT, null, List.of("select one of keys to proceed"),userSession));
            machine.sendEvent(USER_EVENTS.DELETE_RESOURCE);
            Stack<USER_STATES> stateStack = userSession.getStates();

            while (!stateStack.empty()) {
                if(stateStack.peek() != USER_STATES.RESOURCE_MANAGEMENT){
                    stateStack.pop();
                }
                if(stateStack.peek() == USER_STATES.RESOURCE_MANAGEMENT){
                    stateStack.pop();
                    break;
                }
            }
        }
    }
}
