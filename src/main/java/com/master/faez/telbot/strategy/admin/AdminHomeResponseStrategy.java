package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.CONSTANTS;
import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.UserService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@Service
public class AdminHomeResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    UserService userService;
    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        String text = update.getMessage().getText();
        StateMachine<USER_STATES, USER_EVENTS> stateMachine = userSession.getStateMachine();
        if(text.equalsIgnoreCase("Books")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, CONSTANTS.KEYBOARD_BOOK_MANAGEMENT, null,List.of("select one of keys to proceed"),userSession));
            stateMachine.sendEvent(USER_EVENTS.SELECT_BOOK);
        } else if (text.equalsIgnoreCase("Broadcast Message")) {
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, List.of("Back"), null,List.of("Send the message that you wanna broadcast\nThe message will be send to all the users including admins press back to cancel"),userSession));
            stateMachine.sendEvent(USER_EVENTS.BROADCAST);
        }else if(text.equalsIgnoreCase("About us")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("Send the message that you use for about us button"),userSession));
            stateMachine.sendEvent(USER_EVENTS.ABOUT_US);
        }else if(text.equalsIgnoreCase("Add new Admin")){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("Now you can add a new admin to the bot\nForward a message of the user that you wanna promote"),userSession));
            stateMachine.sendEvent(USER_EVENTS.ADD_ADMIN);
        }else if (text.equalsIgnoreCase("Delete an Admin")){
            List<User> users = userService.findAllByRole(USER_ROLE.ADMIN);
            if(users.size() <= 1){
                applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of("since there are only one admins you can not remove anyone"),userSession));
            }else{
                String response = "You can send the id of the user to removed them from being admin";
                for(User user : users){
                    response += "\n"+user.getName() + " ID "+ user.getId();
                }
                applicationEventPublisher.publishEvent(new ProcessedMessage(this,List.of("Back"),null,List.of(response),userSession));
                stateMachine.sendEvent(USER_EVENTS.DELETE_ADMIN);
            }
        }else {
            ProcessedMessage processedMessage = new ProcessedMessage(this, CONSTANTS.KEYBOARD_HOME, null, List.of("you can user keyboard to navigated into different sections"), userSession);
            applicationEventPublisher.publishEvent(processedMessage);
        }
    }
}
