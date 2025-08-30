package com.zhsw.auth.controller;


import org.openapitools.api.AuthApi;
import org.openapitools.model.ListUsersGET200Response;
import org.openapitools.model.TrySpecGenerationGET200Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthApplicationController implements AuthApi {
    @Override
    public ResponseEntity<TrySpecGenerationGET200Response> trySpecGenerationGET() {
        return new ResponseEntity(new TrySpecGenerationGET200Response().key("ciao2333"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListUsersGET200Response> listUsersGET(String companyId) {
        return AuthApi.super.listUsersGET(companyId);
    }
}
