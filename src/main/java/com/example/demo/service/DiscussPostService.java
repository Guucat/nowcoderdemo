package com.example.demo.service;

import com.example.demo.entity.DiscussPost;

import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/2 - 17:38
 */
public interface DiscussPostService {
    List<DiscussPost> findDiscussPosts(int userId, int offset, int limit);
    int findDiscussPostRows(int userId);
    int addDiscussPost(DiscussPost discussPost);
    DiscussPost findDiscussPostById(int id);

}
