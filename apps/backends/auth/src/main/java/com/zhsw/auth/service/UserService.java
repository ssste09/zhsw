package com.zhsw.auth.service;

import com.zhsw.auth.entity.User;
import com.zhsw.auth.mapper.UserMapper;
import com.zhsw.auth.repository.UserRepository;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.SignUpUserRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public Try<User> registerUser(SignUpUserRequest signUpUserRequest) {
        return Try.of(() -> {
                    if (isUserRegistered(signUpUserRequest.getEmail())) {
                        throw new IllegalArgumentException("Email is already registered");
                    }
                    return userMapper.mapToSignUpRequestToUser(signUpUserRequest);
                })
                .map(userRepository::save);
    }

    private boolean isUserRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User login(String email, String password) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email not registered"));

        if (!user.getPassword().matches(password)) {
            throw new IllegalArgumentException("Incorrect password");
        }

        return user;
    }
}
