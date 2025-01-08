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
                        .state(USER_STATES.BOOK_DELETE)
                        .state(USER_STATES.BOOK_EDIT)
                        .state(USER_STATES.RESOURCE_CREATE)
                        .state(USER_STATES.RESOURCE_LIST)
                        .state(USER_STATES.FILE_MANAGEMENT)
                        .state(USER_STATES.RESOURCE_DELETE)
                        .state(USER_STATES.FILE_ADD)
                        .state(USER_STATES.FILE_LIST)
                        .state(USER_STATES.FILE_SELECT)
                        .state(USER_STATES.FILE_EDIT)
                        .state(USER_STATES.FILE_DELETE)
                        .state(USER_STATES.BROADCAST)
                        .state(USER_STATES.ABOUT_US)
                        .state(USER_STATES.ADD_ADMIN)
                        .state(USER_STATES.DELETE_ADMIN);

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
                    .state(USER_STATES.BOOK_DELETE)
                    .state(USER_STATES.BOOK_EDIT)
                    .state(USER_STATES.RESOURCE_CREATE)
                    .state(USER_STATES.RESOURCE_LIST)
                    .state(USER_STATES.FILE_MANAGEMENT)
                    .state(USER_STATES.RESOURCE_DELETE)
                    .state(USER_STATES.FILE_LIST)
                    .state(USER_STATES.FILE_ADD)
                    .state(USER_STATES.FILE_SELECT)
                    .state(USER_STATES.FILE_EDIT)
                    .state(USER_STATES.FILE_DELETE)
                    .state(USER_STATES.BROADCAST)
                    .state(USER_STATES.ABOUT_US)
                    .state(USER_STATES.ADD_ADMIN)
                    .state(USER_STATES.DELETE_ADMIN);


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
                .event(USER_EVENTS.DELETE_BOOK)
                .and()
                .withExternal()
                .source(USER_STATES.RESOURCE_MANAGEMENT)
                .target(USER_STATES.BOOK_EDIT)
                .event(USER_EVENTS.EDIT_BOOK)
                .and()
                .withExternal()
                .source(USER_STATES.RESOURCE_MANAGEMENT)
                .target(USER_STATES.RESOURCE_CREATE)
                .event(USER_EVENTS.CREATE_RESOURCE)
                .and()
                .withExternal()
                .source(USER_STATES.RESOURCE_MANAGEMENT)
                .target(USER_STATES.RESOURCE_LIST)
                .event(USER_EVENTS.SELECT_RESOURCE)
                .and()
                .withExternal()
                .source(USER_STATES.RESOURCE_LIST)
                .target(USER_STATES.FILE_MANAGEMENT)
                .event(USER_EVENTS.RESOURCE_SELECTED)
                .and()
                .withExternal()
                .source(USER_STATES.FILE_MANAGEMENT)
                .target(USER_STATES.RESOURCE_DELETE)
                .event(USER_EVENTS.DELETE_RESOURCE)
                .and()
                .withExternal()
                .source(USER_STATES.RESOURCE_DELETE)
                .target(USER_STATES.RESOURCE_MANAGEMENT)
                .event(USER_EVENTS.DELETE_RESOURCE)
                .and()
                .withExternal()
                .source(USER_STATES.FILE_MANAGEMENT)
                .target(USER_STATES.FILE_ADD)
                .event(USER_EVENTS.ADD_FILE)
                .and()
                .withExternal()
                .source(USER_STATES.FILE_MANAGEMENT)
                .target(USER_STATES.FILE_LIST)
                .event(USER_EVENTS.LIST_FILES)
                .and()
                .withExternal()
                .source(USER_STATES.FILE_LIST)
                .target(USER_STATES.FILE_SELECT)
                .event(USER_EVENTS.FILE_SELECTED)
                .and()
                .withExternal()
                .source(USER_STATES.FILE_SELECT)
                .target(USER_STATES.FILE_EDIT)
                .event(USER_EVENTS.EDIT_FILE)
                .and()
                .withExternal()
                .source(USER_STATES.FILE_SELECT)
                .target(USER_STATES.FILE_DELETE)
                .event(USER_EVENTS.DELETE_FILE)
                .and()
                .withExternal()
                .source(USER_STATES.FILE_DELETE)
                .target(USER_STATES.FILE_LIST)
                .event(USER_EVENTS.DELETE_FILE)
                .and()
                .withExternal()
                .source(USER_STATES.HOME)
                .target(USER_STATES.BROADCAST)
                .event(USER_EVENTS.BROADCAST)
                .and()
                .withExternal()
                .source(USER_STATES.HOME)
                .target(USER_STATES.ABOUT_US)
                .event(USER_EVENTS.ABOUT_US)
                .and()
                .withExternal()
                .source(USER_STATES.HOME)
                .target(USER_STATES.ADD_ADMIN)
                .event(USER_EVENTS.ADD_ADMIN)
                .and()
                .withExternal()
                .source(USER_STATES.HOME)
                .target(USER_STATES.DELETE_ADMIN)
                .event(USER_EVENTS.DELETE_ADMIN);
    }


    public StateMachine<USER_STATES, USER_EVENTS> newCustomStepUserStateMachine(USER_STATES state){
        StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(state)
                    .state(USER_STATES.HOME)
                    .state(USER_STATES.BOOK_LIST)
                    .state(USER_STATES.RESOURCE_LIST);

            UserTransition(builder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    }



    public StateMachine<USER_STATES, USER_EVENTS> newUserStateMachine(String id){
        StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(USER_STATES.START)
                    .state(USER_STATES.BOOK_LIST)
                    .state(USER_STATES.RESOURCE_LIST);

            UserTransition(builder);
            builder.configureConfiguration().withConfiguration().machineId(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return builder.build();
    }


    private void UserTransition(StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder) throws Exception {
        builder.configureTransitions()
                .withExternal()
                .source(USER_STATES.START)
                .target(USER_STATES.BOOK_LIST)
                .event(USER_EVENTS.USER_EXISTS_OR_CREATED)
                .and()
                .withExternal()
                .source(USER_STATES.BOOK_LIST)
                .target(USER_STATES.RESOURCE_LIST)
                .event(USER_EVENTS.SELECT_RESOURCE);
    }
}
