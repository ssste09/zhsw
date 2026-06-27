package com.zhsw.profile.service;

import com.zhsw.profile.entity.Profile;
import com.zhsw.profile.repository.ProfileRepo;
import io.vavr.control.Try;
import org.openapitools.model.ProfileType;
import org.openapitools.model.ProfileUserRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProfileService {

    private final ProfileRepo profileRepo;

    public ProfileService(ProfileRepo profileRepo) {
        this.profileRepo = profileRepo;
    }

    public Try<Profile> createRandomProfile(Long userId, ProfileUserRequest profileUserRequest) {
        return Try.of(() -> {
            var existingProfile = profileRepo.findByUserId(userId);

            var profile = existingProfile.orElseGet(Profile::new);

            profile.setUserId(userId);
            profile.setProfileType(getRandomProfileType());

            return profileRepo.save(profile);
        });
    }

    private ProfileType getRandomProfileType() {
        ProfileType[] values = ProfileType.values();
        int randomIndex = new Random().nextInt(values.length);
        return values[randomIndex];
    }
}
