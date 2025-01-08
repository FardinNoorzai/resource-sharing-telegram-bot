package com.master.faez.telbot.services;

import com.master.faez.telbot.constants.USER_ROLE;
import com.master.faez.telbot.models.User;
import com.master.faez.telbot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public List<User> findAllByRole(USER_ROLE userRole) {
        return userRepository.findAllByUserRole(userRole);
    }
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
