package com.example.demo;

import com.example.demo.dao.DiscussPostMapper;
import com.example.demo.dao.LoginTicketMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.LoginTicket;
import com.example.demo.service.DiscussPostService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.Ticket;

import java.util.Date;

/**
 * @author shengyi
 * @create 2022/9/9 - 0:30
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    LoginTicketMapper loginTicketMapper;
    @Test
    public void testLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setTicket("Test");
        loginTicket.setExpired(new Date());
        loginTicket.setUserId(520);
        loginTicket.setStatus(0);
        loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(loginTicketMapper.selectByTicket("Test"));
        loginTicketMapper.updateStatus("Test", 1);
        System.out.println(loginTicketMapper.selectByTicket("Test"));
    }

    @Autowired
    DiscussPostService discussPostService;
    @Test
    public void DiscussPostTest() {
        DiscussPost post = new DiscussPost();
        post.setCreateTime(new Date());
        post.setTitle("a");
        post.setContent("b");
        discussPostService.addDiscussPost(post);
    }

    @Autowired
    UserService userService;
    @Test
    public void findIdByNameTest() {
        int in = userService.findIdByName("yyy");
        System.out.println(in);
        Integer empty = userService.findIdByName("");
        System.out.println(empty);
        Integer no = userService.findIdByName("adxx56ts");
        System.out.println(no);
    }

}
