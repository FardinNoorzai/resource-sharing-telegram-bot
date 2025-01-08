package com.master.faez.telbot.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class DeleteAdminEvent extends ApplicationEvent {
    Long id;
    public DeleteAdminEvent(Object source, Long chatId) {
        super(source);
        this.id = chatId;
    }
}
