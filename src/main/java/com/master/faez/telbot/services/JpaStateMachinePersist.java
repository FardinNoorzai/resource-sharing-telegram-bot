package com.master.faez.telbot.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.faez.telbot.models.Book;
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

        // Serialize the extended state to JSON
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
                    new TypeReference<Map<Object, Object>>() {} // Preserve type safety
            );

            // Handle specific keys that need conversion
            if (extendedStateVariables.containsKey("book")) {
                Object bookObject = extendedStateVariables.get("book");
                if (bookObject instanceof Map) {
                    // Convert LinkedHashMap to Book
                    Book book = objectMapper.convertValue(bookObject, Book.class);
                    extendedStateVariables.put("book", book);
                } else if (bookObject instanceof String) {
                    // Handle case where it's a JSON string
                    Book book = objectMapper.readValue(bookObject.toString(), Book.class);
                    extendedStateVariables.put("book", book);
                }
            }

            // Rebuild extended state
            ExtendedState extendedState = new DefaultExtendedState();
            extendedState.getVariables().putAll(extendedStateVariables);

            // Return the state machine context
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
        // Convert the LinkedHashMap to a Book object
        Map<Object, Object> variables = context.getExtendedState().getVariables();
        Object bookObject = variables.get("book");

        // Ensure it's a LinkedHashMap before converting
        if (bookObject instanceof Map) {
            return objectMapper.convertValue(bookObject, Book.class);  // Deserialize to Book object
        }
        return null;
    }
}
