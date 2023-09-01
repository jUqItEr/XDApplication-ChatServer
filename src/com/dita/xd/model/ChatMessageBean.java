package com.dita.xd.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ChatMessageBean {
    private int id;
    private String content;
    private int chatroomId;
    private String userId;
    private String userNickname;
    private String userProfileImage;
    private Timestamp createdAt;
    private String readState;

    public ChatMessageBean() {
    }

    public ChatMessageBean(int id, String content, int chatroomId, String userId, String userNickname,
                           String userProfileImage, Timestamp createdAt, String readState) {
        this.id = id;
        this.content = content;
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileImage = userProfileImage;
        this.createdAt = createdAt;
        this.readState = readState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getReadState() {
        return readState;
    }

    public void setReadState(String readState) {
        this.readState = readState;
    }
}
