package com.zhsw.auth.controller;

import com.zhsw.auth.mapper.UserMapper;
import com.zhsw.auth.service.UserService;
import org.openapitools.api.AuthApi;
import org.openapitools.model.LoginUserRequest;
import org.openapitools.model.LoginUserResponse;
import org.openapitools.model.SignUpUserRequest;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthApplicationController implements AuthApi {

    private final UserService userService;
    private final UserMapper userMapper;

    public AuthApplicationController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<User> userGET(Integer userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<User> signup(SignUpUserRequest signUpUserRequest) {
        return userService
                .registerUser(signUpUserRequest)
                .map(userMapper::mapToUserResponse)
                .map(ResponseEntity::ok)
                .get();
    }

    @Override
    public ResponseEntity<LoginUserResponse> login(LoginUserRequest loginUserRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
