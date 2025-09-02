package com.zhsw.auth.service;

import com.zhsw.auth.entity.User;
import com.zhsw.auth.mapper.UserMapper;
import com.zhsw.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.SignUpUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Tag("integration")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowWhenUserIsAlreadyPresent() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(User.builder().email("test@email.com").build()));

        var request = new SignUpUserRequest();
        request.setEmail("test@email.com");

        var result = userService.registerUser(request);

        assertTrue(result.isFailure(), "Expected the Try to be a failure");
        assertTrue(result.getCause() instanceof IllegalArgumentException);
        assertEquals("Email is already registered", result.getCause().getMessage());
    }

    @Test
    void shouldSaveAndReturnUser() {
        var request = new SignUpUserRequest();
        request.setEmail("test@email.com");
        request.setPassword("plainPassword123");

        User mappedUser = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userMapper.mapToSignUpRequestToUser(any(SignUpUserRequest.class))).thenReturn(mappedUser);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword123");
        when(userRepository.save(any()))
                .thenReturn(User.builder().email("test@email.com").build());

        var result = userService.registerUser(request);

        assertTrue(result.isSuccess());
        assertEquals("test@email.com", result.get().getEmail());
        assertNotEquals("plainPassword123", result.get().getPassword());
    }
}
