package com.example.demo.util;

/**
 * @author shengyi
 * @create 2022/9/11 - 20:39
 */

import com.example.demo.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，使用LocalHost跨组件传递对象信息 (代替session对象
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
