package com.master.faez.telbot.repositories;

import com.master.faez.telbot.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
}
