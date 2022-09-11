package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.Date;

/**
 * @author shengyi
 * @create 2022/9/9 - 0:11
 */
@Getter
@Setter
@ToString
public class LoginTicket {
    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired; // 过期时间
}
