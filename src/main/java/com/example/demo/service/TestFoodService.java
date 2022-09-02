package com.example.demo.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author shengyi
 * @create 2022/8/1 - 15:31
 */
@Service
public class TestFoodService {
    public TestFoodService() {
        System.out.println("创建对象");
    }

    @PostConstruct  // 创建对象后调用
    public void init() {
        System.out.println("初始化对象");
    }

    @PreDestroy // 销毁对象前调用
    public void destroy() {
        System.out.println("销毁了对象");
    }
}
