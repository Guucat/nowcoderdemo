package com.example.demo.service.impl;

import com.example.demo.dao.MessageMapper;
import com.example.demo.entity.Message;
import com.example.demo.service.MessageService;
import com.example.demo.util.HostHolder;
import com.example.demo.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shengyi
 * @create 2022/9/18 - 16:34
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Autowired
    HostHolder hostHolder;

    @Override
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    @Override
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    @Override
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    @Override
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    @Override
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    @Override
    public List<Integer> getUnreadLetterIds(List<Message> letterList) {
        List<Integer> list = new ArrayList<>();
        if (letterList != null &&!letterList.isEmpty()) {
            for (Message letter : letterList) {
                if (hostHolder.getUser().getId() == letter.getToId() && letter.getStatus() == 0) {
                    list.add(letter.getId());
                }
            }
        }
        return list;
    }
}
