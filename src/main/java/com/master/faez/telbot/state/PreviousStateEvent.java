package com.master.faez.telbot.state;

import com.master.faez.telbot.core.UserSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class PreviousStateEvent extends ApplicationEvent {
    UserSession userSession;

    public PreviousStateEvent(Object source, UserSession userSession) {
        super(source);
        this.userSession = userSession;
    }
}
