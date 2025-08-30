package com.zhsw.auth.repository;

import com.zhsw.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
