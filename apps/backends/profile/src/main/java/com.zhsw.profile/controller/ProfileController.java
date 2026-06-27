package com.zhsw.profile.controller;

import com.zhsw.model.LoginUser;
import com.zhsw.profile.mapper.ProfileMapper;
import com.zhsw.profile.service.ProfileService;
import org.openapitools.api.ProfileApi;
import org.openapitools.model.Profile;
import org.openapitools.model.ProfileUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController implements ProfileApi {

    @Autowired
    private LoginUser loginUser;

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @Override
    public ResponseEntity<Profile> createProfile(ProfileUserRequest request) {

        return profileService
                .createRandomProfile(loginUser.getUserId(), request)
                .map(profileMapper::mapToProfileResponse)
                .map(ResponseEntity::ok)
                .get();
    }
}
