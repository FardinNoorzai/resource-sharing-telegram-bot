package com.master.faez.telbot.repositories;

import com.master.faez.telbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
