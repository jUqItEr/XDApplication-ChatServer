package com.dita.xd.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;

import com.dita.xd.model.ChatUserBean;

public class ChatListMgr {
	private DBConnectionMgr1 pool;
	public ChatListMgr() {
		pool = DBConnectionMgr1.getInstance();
	}
	
	public Vector<ChatUserBean> getMsgList(String userid){ 
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector<ChatUserBean> vlist = new Vector<ChatUserBean>(); // �޼��� ���� �������� �����.
		try {// Ǯ�����
			con = pool.getConnection(); // ���ؼǰ�ü�� ������.
			sql = "select * from chat_user_tbl where user_tbl_id = ?"; //user_tbl_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs= pstmt.executeQuery(); // rs �� �����ͺ��̽����� �޾� �� ������
			while (rs.next()) {
				ChatUserBean bean = new ChatUserBean();
				bean.setChatroomId(rs.getInt(1));                // chat_message�� pk��
				bean.setUserId(rs.getString(2));           // chat_message�� ����
				vlist.addElement(bean);                  // vlist�� ����
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt,rs);   // ??
		}
		return vlist;
	}
	
	public static void main(String[] args) {
		ChatListMgr mgr = new ChatListMgr();
	}
}
	