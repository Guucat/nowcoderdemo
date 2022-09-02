package com.example.demo.service;

import com.example.demo.entity.User;


/**
 * @author shengyi
 * @create 2022/9/2 - 17:27
 */

public interface UserService {
    User findUserById(int userId);
}
