package com.example.demo.service;

import com.example.demo.entity.Comment;

import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/18 - 0:53
 */
public interface CommentService {
    List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit);
    int findCommentCount(int entityType, int entityId);
    int addComment(Comment comment);
}
