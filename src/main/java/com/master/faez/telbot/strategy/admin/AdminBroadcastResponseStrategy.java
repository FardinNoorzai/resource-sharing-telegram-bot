package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.response.BroadcastMessage;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.UserService;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminBroadcastResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    UserService userService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        List<BroadcastMessage> messages = new ArrayList<>();
        List<User> users = userService.findAll();
        users.forEach(user -> {
            if(!user.getId().equals(userSession.getUser().getId())) {
                BroadcastMessage broadcastMessage = new BroadcastMessage();
                if(containsFile(userSession.getUpdate())){
                    String fileId = getFileId(userSession.getUpdate());
                    if(!fileId.isEmpty() && !fileId.isBlank()){
                        broadcastMessage.setFile(fileId);
                        broadcastMessage.setMessage("");
                    }else{
                        broadcastMessage.setFile(null);
                    }
                }else{
                    broadcastMessage.setMessage(text);
                }
                broadcastMessage.setChatId(user.getId());
                messages.add(broadcastMessage);
            }
        });
        ProcessedMessage processedMessage = new ProcessedMessage(this,null,null,List.of("Your message will be send to all of the users\nIf you wanna broadcast another text or file just send or press back"),userSession);
        processedMessage.setBroadcastMessages(messages);
        applicationEventPublisher.publishEvent(processedMessage);
    }

    private boolean containsFile(Update update){
        return update.getMessage().hasDocument() || update.getMessage().hasPhoto() || update.getMessage().hasVoice() || update.getMessage().hasAudio() || update.getMessage().hasVideo();
    }
    private String getFileId(Update update){
        if(update.getMessage().hasDocument()){
            return update.getMessage().getDocument().getFileId();
        }else if(update.getMessage().hasPhoto()){
            List<PhotoSize> photoSize = update.getMessage().getPhoto();
            return photoSize.isEmpty() ? null : photoSize.get(0).getFileId();
        }else if(update.getMessage().hasVoice()){
            return update.getMessage().getVoice().getFileId();
        }else if(update.getMessage().hasAudio()){
            return update.getMessage().getAudio().getFileId();
        }else if(update.getMessage().hasVideo()){
            return update.getMessage().getVideo().getFileId();
        }
        return null;
    }
}
