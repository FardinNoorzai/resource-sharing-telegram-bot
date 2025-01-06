package com.master.faez.telbot.strategy.user;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.repositories.ResourceRepository;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.FileService;
import com.master.faez.telbot.services.ResourceService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserResourceListResponseStrategy implements ResponseStrategy {
    @Autowired
    ResourceService resourceService;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Override
    public void response(UserSession userSession) {
        if(userSession.getUpdate().hasMessage()){
            String message = userSession.getUpdate().getMessage().getText();
            StateMachine<USER_STATES,USER_EVENTS> machine = userSession.getStateMachine();
            Book book = (Book) machine.getExtendedState().getVariables().get("book");
            Resource resource = resourceService.findByBookAndName(message,book);
            if(resource != null){
                Map<String,String> files = new HashMap<>();
                resource.getFiles().forEach(file -> {
                    files.put(file.getFileName(),file.getFileId());
                });
                String text = "";
                if(files.isEmpty()){
                    text = "Sorry no file is added to this section :(";
                }else{
                    text = "Sending files...";
                }
                eventPublisher.publishEvent(new ProcessedMessage(this,null,files,List.of(text),userSession));
            }
        }
    }
}
