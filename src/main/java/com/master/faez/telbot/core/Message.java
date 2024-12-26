package com.master.faez.telbot.core;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Message {
    Update update;
    UserSession userSession;
}
