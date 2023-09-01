package com.dita.xd.service.implementation;

import com.dita.xd.model.ChatMessageBean;
import com.dita.xd.service.MessageService;
import com.dita.xd.util.helper.ResultSetExtractHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;

public class MessageServiceImpl implements MessageService {
    private final DBConnectionServiceImpl pool;

    public MessageServiceImpl() {
        pool = DBConnectionServiceImpl.getInstance();
    }

    @Override
    public boolean appendMessage(ChatMessageBean bean) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO chat_message_tbl VALUES (NULL, ?, ?, ?, NOW(), ?)";
        boolean flag = false;

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bean.getContent());
            pstmt.setInt(2, bean.getChatroomId());
            pstmt.setString(3, bean.getUserId());
            pstmt.setString(4, String.valueOf(bean.getReadState()));

            flag = pstmt.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt);
        }
        return flag;
    }

    @Override
    public ChatMessageBean getMessage(int recordId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT cmt.id, cmt.content, cmt.chatroom_tbl_id, ut.id, " +
                "ut.nickname, ut.profile_image, cmt.created_at, cmt.read_state " +
                "FROM user_tbl ut JOIN chat_message_tbl cmt " +
                "ON cmt.user_tbl_id = ut.id WHERE cmt.user_tbl_id=?";
        ChatMessageBean bean = null;

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recordId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = ResultSetExtractHelper.extractChatMessageBean(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt, rs);
        }
        return bean;
    }

    @Override
    public Vector<ChatMessageBean> getMessages(int chatroomId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT cmt.id, cmt.content, cmt.chatroom_tbl_id, ut.id, " +
                "ut.nickname, ut.profile_image, cmt.created_at, cmt.read_state " +
                "FROM user_tbl ut JOIN chat_message_tbl cmt " +
                "ON cmt.user_tbl_id = ut.id WHERE cmt.chatroom_tbl_id=? ORDER BY cmt.id DESC";
        Vector<ChatMessageBean> beans = new Vector<>();

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                beans.addElement(
                        ResultSetExtractHelper.extractChatMessageBean(rs)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt, rs);
        }
        return beans;
    }

    @Override
    public Vector<ChatMessageBean> getMessages(int chatroomId, String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT cmt.id, cmt.content, cmt.chatroom_tbl_id, ut.id, " +
                "ut.nickname, ut.profile_image, cmt.created_at, cmt.read_state " +
                "FROM user_tbl ut JOIN chat_message_tbl cmt " +
                "ON cmt.user_tbl_id = ut.id WHERE cmt.chatroom_tbl_id=? AND cmt.user_tbl_id=? ORDER BY cmt.id DESC";
        Vector<ChatMessageBean> beans = new Vector<>();

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                beans.addElement(
                        ResultSetExtractHelper.extractChatMessageBean(rs)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt, rs);
        }
        return beans;
    }

    @Override
    public Vector<ChatMessageBean> getMessages(int chatroomId, String userId, Timestamp targetAt) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT cmt.id, cmt.content, cmt.chatroom_tbl_id, ut.id, " +
                "ut.nickname, ut.profile_image, cmt.created_at, cmt.read_state " +
                "FROM user_tbl ut JOIN chat_message_tbl cmt " +
                "ON cmt.user_tbl_id = ut.id WHERE cmt.chatroom_tbl_id=? AND cmt.user_tbl_id=? AND " +
                "DATE(cmt.created_at)=DATE(?) ORDER BY cmt.id DESC";
        Vector<ChatMessageBean> beans = new Vector<>();

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            pstmt.setString(2, userId);
            pstmt.setTimestamp(3, targetAt);
            rs = pstmt.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt, rs);
        }
        return beans;
    }
}
