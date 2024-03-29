package com.example.demo.util;

import org.springframework.http.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author shengyi
 * @create 2022/9/11 - 20:27
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request, String name) {
        if (request == null ||name == null) {
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
