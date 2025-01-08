package com.master.faez.telbot.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class NewAdminEvent extends ApplicationEvent {
    Long id;

    public NewAdminEvent(Object source, Long id) {
        super(source);
        this.id = id;

    }
}
