package com.example.demo.config;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.controller.interceptor.LoginRequiredInterceptor;
import com.example.demo.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shengyi
 * @create 2022/9/11 - 20:52
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    LoginTicketInterceptor interceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");  // 不处理对静态资源的访问
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");  // 不处理对静态资源的访问

    }

}
