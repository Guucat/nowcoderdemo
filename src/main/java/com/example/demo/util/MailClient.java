package com.example.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author shengyi
 * @create 2022/9/2 - 22:01
 */
@Component
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")     //注入配置文件中的某个值
    private String from;

    /**
     *
     * @param to        邮件接收人
     * @param subject   邮件主题
     * @param content   邮件内容
     */
    public void sendEmail(String to, String subject, String content) {
        try {
            // 构建邮件发送对象
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);  // 支持html格式
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("邮件发送失败" + e.getMessage());
        }
    }
}
