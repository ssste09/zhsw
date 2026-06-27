package com.zhsw.model;

public class LoginUser {
    private String email;
    private Long userId;

    public LoginUser(String email, Long userId) {
        this.email = email;
        this.userId = userId;
    }

    public LoginUser() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
