package com.master.faez.telbot.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StateMachineEntity {
    @Id
    private String machineId;
    private String state;
    private String context;
}
