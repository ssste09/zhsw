package com.zhsw.auth.service;

import com.zhsw.auth.entity.User;
import com.zhsw.auth.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


}
