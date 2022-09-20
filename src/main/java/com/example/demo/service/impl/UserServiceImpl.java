package com.example.demo.service.impl;

import com.example.demo.dao.LoginTicketMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.LoginTicket;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.DemoConstant;
import com.example.demo.util.DemoUtil;
import com.example.demo.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author shengyi
 * @create 2022/9/2 - 17:28
 */
@Service
public class UserServiceImpl implements UserService, DemoConstant {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${nowcoderdemo.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public User findUserById(int userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为null");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "用户名不能为空");
            return map;
        }
        // 验证账号
        User DbUser = userMapper.selectByName(user.getUsername());
        if (DbUser != null) {
            map.put("usernameMsg", "用户名已被注册");
            return map;
        }
        // 验证邮箱
        DbUser = userMapper.selectByEmail(user.getEmail());
        if (DbUser != null) {
            map.put("emailMsg", "邮箱已被注册");
            return map;
        }
        // 注册用户
        user.setSalt(DemoUtil.generateUUID().substring(0, 5));
        user.setPassword(DemoUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(DemoUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        // 发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation" + "/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendEmail(user.getEmail(), "账号激活", content);
        return map;
    }

    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    @Override
    public Map<String, Object> login(String username, String password, int expired){
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(username)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        // 合法性验证
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "账号不存在");
            return map;
        }
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "账号未激活");
            return map;
        }
        password = DemoUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password) ) {
            map.put("passwordMsg", "密码不正确");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setTicket(DemoUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expired * 1000L));
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    @Override
    public LoginTicket findLoginTicket(String name) {
        return loginTicketMapper.selectByTicket(name);
    }

    @Override
    public int updateHeader(int userId, String headUrl) {
         return userMapper.updateHeader(userId, headUrl);
    }

    @Override
    public Integer findIdByName(String username) {
        return userMapper.selectIdByName(username);
    }

}
