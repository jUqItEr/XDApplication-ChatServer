package com.dita.xd.util.helper;

import com.dita.xd.model.ChatMessageBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ResultSetExtractHelper {
    public static ChatMessageBean extractChatMessageBean(ResultSet rs)
            throws SQLException {
        Timestamp beanCreatedAt = rs.getTimestamp("created_at");
        String beanContent = rs.getString("content");
        String beanReadState = rs.getString("read_state");
        String beanUserId = rs.getString("user_id");
        String beanUserNickname = rs.getString("nickname");
        String beanProfileImage = rs.getString("profile_image");
        int beanId = rs.getInt("id");
        int beanChatRoomId = rs.getInt("chatroom_id");

        return new ChatMessageBean(beanId, beanContent, beanChatRoomId, beanUserId,
                beanUserNickname, beanProfileImage, beanCreatedAt, beanReadState);
    }
}
