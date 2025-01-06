package com.master.faez.telbot.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.File;
import com.master.faez.telbot.models.Resource;
import com.master.faez.telbot.models.StateMachineEntity;
import com.master.faez.telbot.repositories.StateMachineRepository;
import com.master.faez.telbot.state.USER_EVENTS;
import com.master.faez.telbot.state.USER_STATES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class JpaStateMachinePersist implements StateMachinePersist<USER_STATES, USER_EVENTS, String> {

    @Autowired
    private StateMachineRepository stateMachineRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void write(StateMachineContext<USER_STATES, USER_EVENTS> context, String machineId) throws Exception {
        StateMachineEntity entity = new StateMachineEntity();
        entity.setMachineId(machineId);
        entity.setState(context.getState().toString());

        String extendedStateJson = objectMapper.writeValueAsString(context.getExtendedState().getVariables());
        entity.setContext(extendedStateJson);

        stateMachineRepository.save(entity);
    }

    @Override
    public StateMachineContext<USER_STATES, USER_EVENTS> read(String machineId) throws Exception {
        Optional<StateMachineEntity> entity = stateMachineRepository.findById(machineId);
        if (entity.isPresent()) {
            StateMachineEntity smEntity = entity.get();

            // Deserialize the JSON back to a map
            Map<Object, Object> extendedStateVariables = objectMapper.readValue(
                    smEntity.getContext(),
                    new TypeReference<Map<Object, Object>>() {}
            );

            if (extendedStateVariables.containsKey("book")) {
                Object bookObject = extendedStateVariables.get("book");
                if (bookObject instanceof Map) {
                    Book book = objectMapper.convertValue(bookObject, Book.class);
                    extendedStateVariables.put("book", book);
                } else if (bookObject instanceof String) {
                    Book book = objectMapper.readValue(bookObject.toString(), Book.class);
                    extendedStateVariables.put("book", book);
                }
            }
            
            if (extendedStateVariables.containsKey("resource")) {
                Object resourceObject = extendedStateVariables.get("resource");
                if (resourceObject instanceof Map) {
                    Resource resource = objectMapper.convertValue(resourceObject, Resource.class);
                    extendedStateVariables.put("resource", resource);
                } else if (resourceObject instanceof String) {
                    Resource resource = objectMapper.readValue(resourceObject.toString(), Resource.class);
                    extendedStateVariables.put("resource", resource);
                }
            }

            if (extendedStateVariables.containsKey("file")) {
                Object resourceObject = extendedStateVariables.get("file");
                if (resourceObject instanceof Map) {
                    File file = objectMapper.convertValue(resourceObject, File.class);
                    extendedStateVariables.put("file", file);
                } else if (resourceObject instanceof String) {
                    File file = objectMapper.readValue(resourceObject.toString(), File.class);
                    extendedStateVariables.put("file", file);
                }
            }

            ExtendedState extendedState = new DefaultExtendedState();
            extendedState.getVariables().putAll(extendedStateVariables);
            return new DefaultStateMachineContext<>(
                    USER_STATES.valueOf(smEntity.getState()),
                    null,
                    null,
                    extendedState
            );
        }
        return null;
    }


    public Book getBookFromExtendedState(StateMachineContext<USER_STATES, USER_EVENTS> context) {
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        Object bookObject = variables.get("book");
        if (bookObject instanceof Map) {
            return objectMapper.convertValue(bookObject, Book.class);  // Deserialize to Book object
        }
        return null;
    }
}
