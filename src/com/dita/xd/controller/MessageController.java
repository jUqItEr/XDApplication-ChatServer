package com.dita.xd.controller;

import com.dita.xd.model.ChatMessageBean;
import com.dita.xd.service.implementation.MessageServiceImpl;

import java.sql.Timestamp;
import java.util.Vector;

public class MessageController {
    private final MessageServiceImpl svc;

    public MessageController() {
        svc = new MessageServiceImpl();
    }
    public boolean appendMessage(ChatMessageBean bean) {
        return svc.appendMessage(bean);
    }

    public ChatMessageBean getMessage(int recordId) {
        return svc.getMessage(recordId);
    }

    public Vector<ChatMessageBean> getMessages(int chatroomId) {
        return svc.getMessages(chatroomId);
    }

    public Vector<ChatMessageBean> getMessages(int chatroomId, String userId) {
        return svc.getMessages(chatroomId, userId);
    }

    public Vector<ChatMessageBean> getMessages(int chatroomId, String userId, String targetAt) {
        return svc.getMessages(chatroomId, userId, targetAt);
    }
}
