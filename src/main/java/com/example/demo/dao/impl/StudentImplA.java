package com.example.demo.dao.impl;

import com.example.demo.dao.Student;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * @author shengyi
 * @create 2022/8/1 - 15:13
 */
@Repository("stu")
@Scope("prototype")
public class StudentImplA implements Student {
    @Override
    public String hi() {
        return "hi A";
    }
}
