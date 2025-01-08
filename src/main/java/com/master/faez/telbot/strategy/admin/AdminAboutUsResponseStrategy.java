package com.master.faez.telbot.strategy.admin;

import com.master.faez.telbot.core.UserSession;
import com.master.faez.telbot.models.About;
import com.master.faez.telbot.response.ProcessedMessage;
import com.master.faez.telbot.services.AboutService;
import com.master.faez.telbot.strategy.ResponseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminAboutUsResponseStrategy implements ResponseStrategy {
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    AboutService aboutService;
    @Override
    public void response(UserSession userSession) {
        String text = userSession.getUpdate().getMessage().getText();
        About about = new About();
        about.setText(text + "\n\n\nDeveloped By @Fardin_Noorzai" );
        aboutService.deleteAllAbout();
        aboutService.saveAbout(about);
        applicationEventPublisher.publishEvent(new ProcessedMessage(this,null,null, List.of("Text was updated to:\n" + text + "\n\n\nDeveloped By @Fardin_Noorzai"),userSession));
    }
}
