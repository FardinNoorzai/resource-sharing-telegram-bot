package com.master.faez.telbot.core;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageProcessor {
    public void processMessage(Update update);
}
