package com.master.faez.telbot.repositories;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findAllByUserRole(USER_ROLE role);
}
