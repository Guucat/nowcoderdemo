package com.example.demo.controller;

import com.example.demo.entity.Comment;
import com.example.demo.entity.DiscussPost;
import com.example.demo.entity.Page;
import com.example.demo.entity.User;
import com.example.demo.service.CommentService;
import com.example.demo.service.DiscussPostService;
import com.example.demo.service.UserService;
import com.example.demo.util.DemoConstant;
import com.example.demo.util.DemoUtil;
import com.example.demo.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.*;

/**
 * @author shengyi
 * @create 2022/9/17 - 20:41
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements DemoConstant {

    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return DemoUtil.getJsonString(403, "你还没有登录");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        //统一处理异常
        System.out.println("?????????");
        return DemoUtil.getJsonString(0, "发布成功");
    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 此处可用Redis缓存User数据
        // 查询帖子作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());

        // 评论：给帖子的评论
        // 回复：给评论的评论
        // 评论列表
        List<Comment> comments = commentService.findCommentsByEntity(ENTITY_TYPE_POST, post.getId(),
                page.getOffset(), page.getLimit());
        // 评论Vo列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if (comments != null) {
            for (Comment comment : comments) {
                // 一个评论的Vo
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 评论人
                commentVo.put("user", userService.findUserById(comment.getUserId()));
                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(),
                        0, Integer.MAX_VALUE);
                // 回复Vo列表
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply: replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        // 回复
                        replyVo.put("reply", reply);
                        // 评论人
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        if (target != null) {
                            System.out.println(target.toString());
                        }
                        replyVo.put("target", target);

                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                // 回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount", replyCount);

                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        return "site/discuss-detail";
    }



}
