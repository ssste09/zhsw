package com.zhsw.auth.controller;


import org.openapitools.api.AuthApi;
import org.openapitools.model.LoginUserRequest;
import org.openapitools.model.LoginUserResponse;
import org.openapitools.model.SignUpUserRequest;
import org.openapitools.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthApplicationController implements AuthApi {

    @Override
    public ResponseEntity<User> userGET(Integer userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<User> signup(SignUpUserRequest signUpUserRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<LoginUserResponse> login(LoginUserRequest loginUserRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
