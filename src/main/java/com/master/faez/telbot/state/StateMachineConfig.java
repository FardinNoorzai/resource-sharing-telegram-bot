package com.master.faez.telbot.state;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig {


    public StateMachine<USER_STATES, USER_EVENTS> newAdminStateMachine(){
        StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(USER_STATES.START)
                    .states(EnumSet.allOf(USER_STATES.class));
            adminTransition(builder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    }
    public StateMachine<USER_STATES, USER_EVENTS> newCustomStepAdminStateMachine(USER_STATES state){
        StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(state)
                    .states(EnumSet.allOf(USER_STATES.class));
            adminTransition(builder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    }

    private void adminTransition(StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder) throws Exception {
        builder.configureTransitions()
                .withExternal()
                .source(USER_STATES.START)
                .target(USER_STATES.WAITING_FOR_HOME_KEYBOARD_RESPONSE)
                .event(USER_EVENTS.START_WAS_RECEIVED)
                .and()
                .withExternal()
                .source(USER_STATES.WAITING_FOR_HOME_KEYBOARD_RESPONSE)
                .target(USER_STATES.WAITING_FOR_BOOK_KEYBOARD_RESPONSE)
                .event(USER_EVENTS.BOOK_SELECTED_FROM_HOME_KEYBOARD)
                .and()
                .withExternal()
                .source(USER_STATES.WAITING_FOR_BOOK_KEYBOARD_RESPONSE)
                .target(USER_STATES.WAITING_FOR_NEW_BOOK_NAME)
                .event(USER_EVENTS.CREATE_BOOK_SELECTED_FROM_BOOK_KEYBOARD)
                .and()
                .withExternal()
                .source(USER_STATES.WAITING_FOR_NEW_BOOK_NAME)
                .target(USER_STATES.WAITING_FOR_BOOK_KEYBOARD_RESPONSE)
                .event(USER_EVENTS.BOOK_CREATED)
                .and()
                .withExternal()
                .source(USER_STATES.WAITING_FOR_BOOK_KEYBOARD_RESPONSE)
                .target(USER_STATES.WAITING_FOR_BOOK_SELECTION)
                .event(USER_EVENTS.LIST_BOOKS_SELECTED_FROM_BOOK_KEYBOARD)
                .and()
                .withExternal()
                .source(USER_STATES.WAITING_FOR_BOOK_SELECTION)
                .target(USER_STATES.WAITING_FOR_SELECTED_BOOK_KEYBOARD_RESPONSE)
                .event(USER_EVENTS.BOOK_SELECTED_FROM_BOOK_LIST)
                .and()
                .withExternal()
                .source(USER_STATES.START)
                .target(USER_STATES.WAITING_FOR_HOME_KEYBOARD_RESPONSE)
                .event(USER_EVENTS.USER_EXIST);
    }


}
