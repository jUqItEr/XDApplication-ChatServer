package com.dita.xd.service.implementation;

import com.dita.xd.model.ChatMessageBean;
import com.dita.xd.service.MessageService;
import com.dita.xd.util.helper.ResultSetExtractHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;

import static com.dita.xd.util.helper.ResultSetExtractHelper.extractChatMessageBean;

public class MessageServiceImpl implements MessageService {
    private final DBConnectionServiceImpl pool;

    public MessageServiceImpl() {
        pool = DBConnectionServiceImpl.getInstance();
    }

    @Override
    public boolean appendMessage(ChatMessageBean bean) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "insert into chat_message_tbl values (null, ?, ?, ?, now(), ?)";
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
        String sql = "select * from chat_logs_view where id = ?";
        ChatMessageBean bean = null;

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, recordId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = extractChatMessageBean(rs);
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
        String sql = "select * from chat_logs_view where chatroom_id = ?";
        Vector<ChatMessageBean> beans = new Vector<>();

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                beans.addElement(extractChatMessageBean(rs));
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
        String sql = "select * from chat_logs_view where chatroom_id = ? and user_id = ?";
        Vector<ChatMessageBean> beans = new Vector<>();

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                beans.addElement(extractChatMessageBean(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt, rs);
        }
        return beans;
    }

    @Override
    public Vector<ChatMessageBean> getMessages(int chatroomId, String userId, String targetAt) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from chat_logs_view where chatroom_id = ? and user_id = ? " +
                "and date(created_at) = date(?)";
        Vector<ChatMessageBean> beans = new Vector<>();

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            pstmt.setString(2, userId);
            pstmt.setString(3, targetAt);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                beans.addElement(extractChatMessageBean(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt, rs);
        }
        return beans;
    }
}
