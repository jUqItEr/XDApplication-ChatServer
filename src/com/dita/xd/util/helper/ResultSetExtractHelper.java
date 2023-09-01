package com.dita.xd.util.helper;

import com.dita.xd.model.ChatMessageBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ResultSetExtractHelper {
    public static ChatMessageBean extractChatMessageBean(ResultSet rs)
            throws SQLException {
        Timestamp beanCreatedAt = rs.getTimestamp("cmt.created_at");
        String beanContent = rs.getString("cmt.content");
        String beanReadState = rs.getString("cmt.read_state");
        String beanUserId = rs.getString("ut.id");
        String beanUserNickname = rs.getString("ut.nickname");
        String beanProfileImage = rs.getString("ut.profile_image");
        int beanId = rs.getInt("cmt.id");
        int beanChatRoomId = rs.getInt("cmt.chatroom_tbl_id");

        return new ChatMessageBean(beanId, beanContent, beanChatRoomId, beanUserId,
                beanUserNickname, beanProfileImage, beanCreatedAt, beanReadState);
    }
}
