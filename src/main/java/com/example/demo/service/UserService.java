package com.example.demo.service;

import com.example.demo.entity.LoginTicket;
import com.example.demo.entity.User;

import java.util.Map;


/**
 * @author shengyi
 * @create 2022/9/2 - 17:27
 */

public interface UserService {
     User findUserById(int userId);
     Map<String, Object> register(User user);
     int activation(int userId, String code);
     Map<String, Object> login(String username, String password, int expired);
     void logout(String ticket);
     LoginTicket findLoginTicket(String name);
     int updateHeader(int userId, String headUrl);
     Integer findIdByName(String username);


}
