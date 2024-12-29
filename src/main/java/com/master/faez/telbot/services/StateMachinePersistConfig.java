package com.master.faez.telbot.services;

import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

@Configuration
public class StateMachinePersistConfig {
    @Autowired
    JpaStateMachinePersist jpaStateMachinePersist;
    @Bean
    public StateMachinePersister<USER_STATES, USER_EVENTS,String> persist() {
        return new DefaultStateMachinePersister<>(jpaStateMachinePersist);
    }

}
