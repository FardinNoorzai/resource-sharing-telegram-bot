package com.master.faez.telbot.core;

import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import com.master.faez.telbot.models.User;
import lombok.*;
import org.springframework.statemachine.StateMachine;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Stack;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserSession {
    User user;
    Update update;
    StateMachine<USER_STATES, USER_EVENTS> stateMachine;
    Stack<USER_STATES> states = new Stack<>();
}
