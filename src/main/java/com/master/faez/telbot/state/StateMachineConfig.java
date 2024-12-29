package com.master.faez.telbot.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig {

    public StateMachine<USER_STATES, USER_EVENTS> newAdminStateMachine(String id){
        StateMachineBuilder.Builder<USER_STATES, USER_EVENTS> builder = StateMachineBuilder.builder();
        try {
            builder.configureStates()
                    .withStates()
                    .initial(USER_STATES.START)
                    .states(EnumSet.allOf(USER_STATES.class));
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
                .target(USER_STATES.BOOK_CREATE)
                .event(USER_EVENTS.CREATE_BOOK);
    }
}
