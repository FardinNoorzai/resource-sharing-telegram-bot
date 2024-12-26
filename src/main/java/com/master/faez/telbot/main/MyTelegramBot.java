package com.master.faez.telbot.main;

import com.master.faez.telbot.core.MessageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    String token;
    @Value("${telegram.bot.username}")
    String username;
    @Autowired
    MessageProcessor messageProcessor;

    @Override
    public void onUpdateReceived(Update update) {
        messageProcessor.processMessage(update);
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return username;
    }
}
