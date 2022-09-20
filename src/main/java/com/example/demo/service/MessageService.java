package com.example.demo.service;

import com.example.demo.entity.Message;

import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/18 - 16:31
 */
public interface MessageService {
    List<Message> findConversations(int userId, int offset, int limit);
    int findConversationCount(int userId);
    List<Message> findLetters(String conversationId, int offset, int limit);
    int findLetterCount(String conversationId);
    int findLetterUnreadCount(int userId, String conversationId);
    int addMessage(Message message);
    int readMessage(List<Integer> ids);
    List<Integer> getUnreadLetterIds(List<Message> letterList);
}
