package com.master.faez.telbot.models;

import jakarta.persistence.Column;
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
    @Column(length = 65535)
    private String context;
}
