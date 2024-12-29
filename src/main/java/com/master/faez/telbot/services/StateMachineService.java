package com.master.faez.telbot.services;

import com.master.faez.telbot.repositories.StateMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateMachineService {
    @Autowired
    private StateMachineRepository stateMachineRepository;

    public boolean exist(String stateMachineId) {
        return stateMachineRepository.existsById(stateMachineId);
    }
}
