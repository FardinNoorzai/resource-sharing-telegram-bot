package com.master.faez.telbot.repositories;

import com.master.faez.telbot.models.Book;
import com.master.faez.telbot.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
    public Optional<Resource> findByName(String name);

}
