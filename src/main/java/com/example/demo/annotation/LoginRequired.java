package com.example.demo.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shengyi
 * @create 2022/9/15 - 21:55
 */
@Target(ElementType.METHOD)     // 此注解描述方法
@Retention(RetentionPolicy.RUNTIME)     // 运行时有效
public @interface LoginRequired {

}
