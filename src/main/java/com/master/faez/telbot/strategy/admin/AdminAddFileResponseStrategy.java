package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.File;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.FileService;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class AdminAddFileResponseStrategy implements ResponseStrategy {
    @Autowired
    FileService fileService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void response(UserSession userSession) {
        Update update = userSession.getUpdate();
        StateMachine<USER_STATES, USER_EVENTS> machine = userSession.getStateMachine();
        File file = new File();
        Resource resource = (Resource) machine.getExtendedState().getVariables().get("resource");

        if(update.hasMessage() && update.getMessage().hasVideo()) {

            file.setFileType(update.getMessage().getVideo().getMimeType());
            if(update.getMessage().getCaption() != null) {
                file.setFileName(update.getMessage().getCaption());
            }else{
                file.setFileName(update.getMessage().getVideo().getFileId());
            }
            double size = (double) update.getMessage().getVideo().getFileSize() / (1024 * 1024);
            file.setSize(size);
            file.setFileId(update.getMessage().getVideo().getFileId());
        }
        else if(update.hasMessage() && update.getMessage().hasDocument()) {
            System.out.println("has document has been executed");
            file.setFileType(update.getMessage().getDocument().getMimeType());
            file.setFileName(update.getMessage().getDocument().getFileName());
            double size = (double) update.getMessage().getDocument().getFileSize() / (1024 * 1024);
            file.setSize(size);
            file.setFileId(update.getMessage().getDocument().getFileId());
        }
        else if(update.hasMessage() && update.getMessage().hasPhoto()) {

            file.setFileType("Image/jpg");
            update.getMessage().getPhoto().forEach((photoSize -> {
                if(update.getMessage().getCaption() != null) {
                    file.setFileName(update.getMessage().getCaption());
                }else {
                    file.setFileName(photoSize.getFileId());
                }
                double size = (double) photoSize.getFileSize() / (1024 * 1024);
                file.setSize(size);
                file.setFileId(photoSize.getFileId());

            }));
        }else if(update.hasMessage() && update.getMessage().hasAudio()) {
            file.setFileType(update.getMessage().getAudio().getMimeType());
            file.setFileName(update.getMessage().getAudio().getFileName());
            double size = (double) update.getMessage().getAudio().getFileSize() / (1024 * 1024);
            file.setSize(size);
            file.setFileId(update.getMessage().getAudio().getFileId());
        }else if(update.hasMessage() && update.getMessage().hasVoice()) {
            file.setFileType(update.getMessage().getVoice().getMimeType());
            file.setFileName(update.getMessage().getVoice().getFileId());
            double size = (double) update.getMessage().getVoice().getFileSize() / (1024 * 1024);
            file.setSize(size);
            file.setFileId(update.getMessage().getVoice().getFileId());
        }else{
            applicationEventPublisher.publishEvent(new ProcessedMessage(this, null,null,List.of("Sorry this file type is not supported!"),userSession));
            return;
        }
        file.setResource(resource);
        fileService.save(file);
        applicationEventPublisher.publishEvent(new ProcessedMessage(this, null,null,List.of("Your file was saved! you can send another one of press the back"),userSession));
    }
}
