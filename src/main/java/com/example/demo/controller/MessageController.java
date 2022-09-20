package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.entity.Page;
import com.example.demo.entity.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import com.example.demo.util.DemoUtil;
import com.example.demo.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author shengyi
 * @create 2022/9/18 - 16:46
 */
@Controller
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;

    // 私信列表
    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        // 会话列表
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        // 查询未读消息数
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        return "/site/letter";
    }

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));

        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();

        boolean get = false;
        int targetId = hostHolder.getUser().getId();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map= new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
                if (!get) {
                    targetId = targetId == message.getFromId() ? message.getToId() : message.getFromId();
                    get =true;
                }
            }
        }

        model.addAttribute("letters", letters);
        // 私信目标
        model.addAttribute("target", userService.findUserById(targetId));
        List<Integer> unreadIds = messageService.getUnreadLetterIds(letterList);
        if (!unreadIds.isEmpty()) {
            messageService.readMessage(unreadIds);
        }
        return "site/letter-detail";
    }

    @PostMapping("/letter/send")
    @ResponseBody
    public String sendLetter(String toName, String content) {
        Integer toId = userService.findIdByName(toName);
        if (toId == null) {
            return DemoUtil.getJsonString(1, "用户不存在");
        }
        Message message = new Message();
        message.setCreateTime(new Date());
        message.setStatus(0);
        message.setContent(content);
        message.setToId(toId);
        int fromId = hostHolder.getUser().getId();
        message.setFromId(fromId);
        message.setConversationId(Math.min(fromId,toId) + "_" + Math.max(fromId, toId));
        messageService.addMessage(message);
        return DemoUtil.getJsonString(0);
    }
//
//    @PostMapping("letter/update")
//    public
}
