package com.zhsw.auth.service;

import com.zhsw.auth.entity.User;
import com.zhsw.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User registerUser(User user) {

        if(isUserRegistered(user.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }
        return userRepository.save(user);
    }

    private boolean isUserRegistered(String email) {

        try {
            Optional<User> user = userRepository.findByEmail(email);
            return user.isPresent();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
