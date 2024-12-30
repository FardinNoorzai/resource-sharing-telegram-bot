package com.master.faez.telbot.state;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;

@Configuration
@EnableStateMachine
public class StateMachineConfig {

    public StateMachine<USER_STATES, USER_EVENTS> newAdminStateMachine(String id){
        StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder = StateMachineBuilder.builder();
        try {
                builder.configureStates()
                    .withStates()
                    .initial(USER_STATES.START)
                    .state(USER_STATES.HOME)
                    .state(USER_STATES.BOOK_MANAGEMENT)
                        .state(USER_STATES.BOOK_LIST)
                        .state(USER_STATES.RESOURCE_MANAGEMENT)
                        .state(USER_STATES.BOOK_CREATE)
                        .state(USER_STATES.BOOK_DELETE);
            adminTransition(builder);
            builder.configureConfiguration().withConfiguration().machineId(id);
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
                    .state(USER_STATES.HOME)
                    .state(USER_STATES.BOOK_MANAGEMENT)
                    .state(USER_STATES.BOOK_LIST)
                    .state(USER_STATES.RESOURCE_MANAGEMENT)
                    .state(USER_STATES.BOOK_CREATE)
                    .state(USER_STATES.BOOK_DELETE);
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
                .target(USER_STATES.HOME)
                .event(USER_EVENTS.USER_EXISTS_OR_CREATED)
                .and()
                .withExternal()
                .source(USER_STATES.HOME)
                .target(USER_STATES.BOOK_MANAGEMENT)
                .event(USER_EVENTS.SELECT_BOOK)
                .and()
                .withExternal()
                .source(USER_STATES.BOOK_MANAGEMENT)
                .target(USER_STATES.BOOK_LIST)
                .event(USER_EVENTS.BOOK_LIST_SELECTED)
                .and()
                .withExternal()
                .source(USER_STATES.BOOK_LIST)
                .target(USER_STATES.RESOURCE_MANAGEMENT)
                .event(USER_EVENTS.BOOK_SELECTED)
                .and()
                .withExternal()
                .source(USER_STATES.BOOK_MANAGEMENT)
                .target(USER_STATES.BOOK_CREATE)
                .event(USER_EVENTS.CREATE_BOOK)
                .and()
                .withExternal()
                .source(USER_STATES.RESOURCE_MANAGEMENT)
                .target(USER_STATES.BOOK_DELETE)
                .event(USER_EVENTS.DELETE_BOOK)
                .and()
                .withExternal()
                .source(USER_STATES.BOOK_DELETE)
                .target(USER_STATES.BOOK_MANAGEMENT)
                .event(USER_EVENTS.DELETE_BOOK);
    }
}
