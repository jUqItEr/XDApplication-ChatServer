package com.dita.xd.model;

public class ChatUserBean {
    private int chatroomId;
    private String userId;

    public ChatUserBean() {
    }

    public ChatUserBean(int chatroomId, String userId) {
        this.chatroomId = chatroomId;
        this.userId = userId;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
