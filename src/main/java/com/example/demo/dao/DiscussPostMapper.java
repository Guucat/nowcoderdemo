package com.example.demo.dao;

import com.example.demo.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/2 - 0:39
 */
@Mapper
public interface DiscussPostMapper {
    /**
     *
     * @param userId
     * @param offset 起始行的行号, 从0开始
     * @param limit  查询数据的条数
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
    /**
     * @Param 可以给参数取别名
     * 当需要用到动态条件拼接sql, 且方法中只有一个参数时, 须加上此注解
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

}
