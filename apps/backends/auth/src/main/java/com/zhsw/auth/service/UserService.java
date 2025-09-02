package com.zhsw.auth.service;

import com.zhsw.auth.entity.User;
import com.zhsw.auth.mapper.UserMapper;
import com.zhsw.auth.repository.UserRepository;
import com.zhsw.auth.utils.JwtService;
import io.vavr.control.Try;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.LoginUserRequest;
import org.openapitools.model.SignUpUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public Try<User> registerUser(SignUpUserRequest signUpUserRequest) {
        return Try.of(() -> {
                    if (isUserRegistered(signUpUserRequest.getEmail())) {
                        throw new IllegalArgumentException("Email is already registered");
                    }
                    User user = userMapper.mapSignUpRequestToUser(signUpUserRequest);
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    return user;
                })
                .map(userRepository::save);
    }

    private boolean isUserRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public Try<String> login(LoginUserRequest loginUserRequest) {
        return Try.of(() -> {
            User user = userRepository
                    .findByEmail(loginUserRequest.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Email not registered"));

            if (!passwordEncoder.matches(loginUserRequest.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Incorrect password");
            }

            return jwtService.generateToken(user.getUserId(), user.getEmail());
        });
    }
}
