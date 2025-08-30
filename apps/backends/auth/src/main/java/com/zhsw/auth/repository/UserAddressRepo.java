package com.zhsw.auth.repository;

import com.zhsw.auth.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepo extends JpaRepository<UserAddress, Long> {
}
