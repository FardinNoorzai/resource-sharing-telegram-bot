package com.master.faez.telbot.services;

import com.master.faez.telbot.models.StateMachineEntity;
import com.master.faez.telbot.repositories.StateMachineRepository;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaStateMachinePersist implements StateMachinePersist<USER_STATES, USER_EVENTS,String> {
    @Autowired
    StateMachineRepository stateMachineRepository;
    @Override
    public void write(StateMachineContext<USER_STATES, USER_EVENTS> context, String machineId) throws Exception {
        StateMachineEntity entity = new StateMachineEntity();
        entity.setMachineId(machineId);
        entity.setState(context.getState().toString());
        entity.setContext(context.getExtendedState().toString());
        stateMachineRepository.save(entity);
    }

    @Override
    public StateMachineContext<USER_STATES, USER_EVENTS> read(String machineId) throws Exception {
        Optional<StateMachineEntity> entity = stateMachineRepository.findById(machineId);
        if (entity.isPresent()) {
            StateMachineEntity smEntity = entity.get();
            return new DefaultStateMachineContext<>(
                    USER_STATES.valueOf(smEntity.getState()),
                    null,
                    null,
                    null
            );
        }
        return null;
    }
}
