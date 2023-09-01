package com.dita.xd.service;

import com.dita.xd.model.ChatMessageBean;

import java.sql.Timestamp;
import java.util.Vector;

public interface MessageService extends Service {
    // 메시지를 DB로 전송함.
    boolean appendMessage(ChatMessageBean bean);
    // 채팅 기록 중 id에 해당하는 메시지 하나를 불러옴.
    ChatMessageBean getMessage(int recordId);
    // 채팅방의 모든 내용을 불러옴.
    Vector<ChatMessageBean> getMessages(int chatroomId);
    // 채팅방에서 해당 사용자가 말한 내용을 불러옴.
    Vector<ChatMessageBean> getMessages(int chatroomId, String userId);
    Vector<ChatMessageBean> getMessages(int chatroomId, String userId, Timestamp targetAt);
}
