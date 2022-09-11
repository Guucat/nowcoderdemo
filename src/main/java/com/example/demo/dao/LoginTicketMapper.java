package com.example.demo.dao;

import com.example.demo.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author shengyi
 * @create 2022/9/9 - 0:13
 */
@Mapper
public interface LoginTicketMapper {
    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})",
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select user_id, ticket, status, expired from login_ticket ",
            "where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "update login_ticket set status = #{status} ",
            "where ticket = #{ticket}"
    })
    int updateStatus(String ticket, int status);
}

