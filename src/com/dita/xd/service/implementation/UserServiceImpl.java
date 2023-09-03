package com.dita.xd.service.implementation;

import com.dita.xd.model.UserBean;
import com.dita.xd.service.UserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class UserServiceImpl implements UserService {
    private final DBConnectionServiceImpl pool;

    public UserServiceImpl() {
        pool = DBConnectionServiceImpl.getInstance();
    }

    @Override
    public boolean dismissUser(int chatroomId, String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = null;
        boolean flag = false;

        try {
            conn = pool.getConnection();
            sql = "delete from chat_user_tbl where chatroom_tbl_id = ? and user_tbl_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            pstmt.setString(2, userId);

            flag = pstmt.executeUpdate() == 1;

            if (flag) {
                Vector<UserBean> users = getUsers(chatroomId);

                if (users.size() == 0) {
                    sql = "delete from chatroom_tbl where id = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, chatroomId);

                    flag = pstmt.executeUpdate() == 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt);
        }
        return flag;
    }

    @Override
    public Vector<UserBean> getUsers(int chatroomId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from chat_user_tbl where chatroom_tbl_id = ?";
        Vector<UserBean> beans = new Vector<>();

        try {
            conn = pool.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, chatroomId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                UserBean bean = new UserBean();
                String userId = rs.getString("user_tbl_id");
                bean.setUserId(userId);
                beans.addElement(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(conn, pstmt, rs);
        }
        return beans;
    }
}
