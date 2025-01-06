package com.master.faez.telbot.response;

import com.master.faez.telbot.core.UserSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ProcessedMessage extends ApplicationEvent {
    List<String> buttons;
    Map<String,String> files;
    List<String> messages;
    UserSession userSession;

    public ProcessedMessage(Object source, List<String> buttons, Map<String,String> files, List<String> messages, UserSession userSession) {
        super(source);
        this.buttons = buttons;
        this.files = files;
        this.messages = messages;
        this.userSession = userSession;
    }
}
