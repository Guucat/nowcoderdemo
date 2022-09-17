package com.example.demo.dao;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/18 - 0:46
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);
}
