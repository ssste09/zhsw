package com.zhsw.profile.config;

import com.zhsw.model.LoginUser;
import com.zhsw.security.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@Configuration
public class Config extends SecurityConfig {
    @Bean
    public Map<HttpMethod, String[]> protectedRoutes() {
        return Map.of(HttpMethod.POST, new String[] {"/profile", "/profile/**"});
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public LoginUser loginUser() {
        return (LoginUser)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
