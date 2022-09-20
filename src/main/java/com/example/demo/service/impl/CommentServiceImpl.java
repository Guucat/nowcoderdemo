package com.example.demo.service.impl;

import com.example.demo.dao.CommentMapper;
import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import com.example.demo.service.DiscussPostService;
import com.example.demo.util.DemoConstant;
import com.example.demo.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/18 - 0:54
 */
@Service
public class CommentServiceImpl implements CommentService, DemoConstant {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    SensitiveFilter filter;
    @Autowired
    DiscussPostService discussPostService;

    @Override
    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType,entityId, offset, limit);
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    /**
     *  增加一条评论，原子的增加评论数
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new RuntimeException("参数不能为空");
        }
        // 添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent((filter.filter(comment.getContent())));
        // 添加回复数量,
        // 当评论的是帖子时，要更新评论数      // ？当评论的是评论时，要更新回复数，数据库没有单独存储回复的数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(),
                    comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count + 1);
        }
        return commentMapper.insertComment(comment);
    }
}
