package com.example.demo.controller;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.DemoUtil;
import com.example.demo.util.HostHolder;
import com.mysql.cj.log.LogFactory;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author shengyi
 * @create 2022/9/12 - 10:52
 */

@RequestMapping("/user")
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${nowcoderdemo.path.upload}")
    private String uploadPath;

    @Value("${nowcoderdemo.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    /**
     *  储存用户上传的图片
     *  会抛出上传失败异常，交由统一异常处理
     */
    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "您还没有上传图片");
            return "/site/setting";
        }
        String fileName = headerImage.getOriginalFilename();
        if (fileName == null) {
            model.addAttribute("error", "文件名为空");
            return "/site/setting";
        }
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "上传文件格式不对");
            return "/site/setting";
        }
        // 生成新文件名
        fileName = DemoUtil.generateUUID() + suffix;
        // 确定文件存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传头像文件失败" + e);
            // 抛出异常，交给统一异常处理
            throw new RuntimeException("上传文件失败, 服务器发生异常:" + e);
        }
        // 更新用户头像路径
        // http://localhost:8080/demo/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    /**
     *
     * @param fileName 文件名
     * @param response  http响应器
     *                 通过图片名获取头像
     */
    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放图片路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应文件的图片类型
        response.setContentType("image/" + suffix);
        try (FileInputStream fis = new FileInputStream(fileName);
             OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像图失败:" + e);
        }
    }




}
