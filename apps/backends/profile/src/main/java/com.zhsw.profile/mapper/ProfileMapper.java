package com.zhsw.profile.mapper;

import lombok.Data;
import org.openapitools.model.Profile;
import org.springframework.stereotype.Component;

@Component
@Data
public class ProfileMapper {
    public Profile mapToProfileResponse(com.zhsw.profile.entity.Profile profile) {
        return new Profile(profile.getUserId(), profile.getProfileType());
    }
}
