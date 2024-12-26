package com.master.faez.telbot.response;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardGenerator {
    public ReplyKeyboardMarkup getReplyKeyboardMarkup(ProcessedMessage processedMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<String> buttons = processedMessage.getButtons();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        int i = 0;
        KeyboardRow keyboardRow = new KeyboardRow();

        for (String text : buttons) {
            keyboardRow.add(text);
            i++;
            if (i == 2) {
                keyboardRows.add(keyboardRow);
                keyboardRow = new KeyboardRow();
                i = 0;
            }
        }
        if (!keyboardRow.isEmpty()) {
            keyboardRows.add(keyboardRow);
        }
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

}
