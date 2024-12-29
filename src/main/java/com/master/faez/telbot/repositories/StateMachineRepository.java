package com.master.faez.telbot.repositories;

import com.master.faez.telbot.models.StateMachineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface StateMachineRepository extends JpaRepository<StateMachineEntity,String> {
}
