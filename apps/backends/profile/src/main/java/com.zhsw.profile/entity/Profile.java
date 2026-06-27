package com.zhsw.profile.entity;

import jakarta.persistence.*;
import org.openapitools.model.ProfileType;

@Entity
@Table(name = "profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type")
    private ProfileType profileType;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }
}
