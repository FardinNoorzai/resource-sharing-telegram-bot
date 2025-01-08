package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.core.NewAdminEvent;
import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.UserService;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class AdminAddAdminResponseStrategy implements ResponseStrategy {
    @Autowired
    UserService userService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        System.out.println("add admin class");
        try{
            Long id = Long.valueOf(update.getMessage().getText());
            User user = userService.findUserById(id);
            System.out.println(user.getUserRole());
            if(user != null) {
                if(!user.getUserRole().equals(USER_ROLE.ADMIN)){
                    applicationEventPublisher.publishEvent(new NewAdminEvent(this,user.getId()));
                    applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("User "+ user.getName() + " was promoted to admin in the bot"),userSession));
                }else{
                    applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("This user is already admin in the bot"),userSession));
                }
            }else{
                applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("This user has not started the bot yet"),userSession));
            }
        }catch (Exception e){
            applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("Sorry there was an error"),userSession));

        }
    }
}
