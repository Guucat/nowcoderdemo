package com.example.demo.dao;

import com.example.demo.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/18 - 14:23
 */
@Mapper
public interface MessageMapper {
    // 查询当前用户会话列表，每个会话只返回最新的一条消息
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户会话数量, 用于分页
    int selectConversationCount(int Id);

    // 查询某个会话的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增消息
    int insertMessage(Message message);

    // 修改消息的状态
    int updateStatus(List<Integer> ids, int status);
}
