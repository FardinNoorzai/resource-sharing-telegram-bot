package com.master.faez.telbot.response;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ResponseGenerator {
    KeyboardGenerator keyboardGenerator;
    TelegramLongPollingBot telegramLongPollingBot;

    @EventListener(ProcessedMessage.class)
    public void processedMessage(ProcessedMessage processedMessage) {
        SendMessage sendMessage = new SendMessage();
        if(processedMessage.getButtons() != null){
            ReplyKeyboardMarkup replyKeyboardMarkup = keyboardGenerator.getReplyKeyboardMarkup(processedMessage);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        sendMessage.setChatId(processedMessage.getUserSession().getUser().getId());
        String text = generatedText(processedMessage.getMessages());
        sendMessage.setText(text);
        try {
            telegramLongPollingBot.execute(sendMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        sendFiles(processedMessage.getFiles(), processedMessage.getUserSession().getUser().getId());
    }


    public String generatedText(List<String> message){
        if(message != null && message.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for(String s : message){
                stringBuilder.append(s);
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        }
        return null;
    }


    public void sendFiles(Map<String,String> files, Long chatId){
        if(files != null){
            for (Map.Entry<String, String> entry : files.entrySet()) {

                String fileName = entry.getKey();
                String fileId = entry.getValue();
                SendDocument document = new SendDocument();
                document.setChatId(chatId);
                document.setDocument(new InputFile(fileId));
                document.setCaption(fileName);
                try {
                    telegramLongPollingBot.execute(document);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
