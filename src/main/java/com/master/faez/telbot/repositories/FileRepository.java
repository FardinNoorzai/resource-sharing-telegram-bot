package com.master.faez.telbot.repositories;

import com.master.faez.telbot.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Integer> {
    public Optional<File> findByFileName(String name);
}
