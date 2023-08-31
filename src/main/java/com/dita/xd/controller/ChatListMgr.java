package com.dita.xd.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import com.dita.xd.model.ChatUserBean;
import com.dita.xd.service.implementation.DBConnectionMgr1;

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
		Vector<ChatUserBean> vlist = new Vector<ChatUserBean>(); // 메세지 빈은 동적으로 만든디.
		try {// 풀링기법
			con = pool.getConnection(); // 컨넥션객체를 빌려옴.
			sql = "select * from chat_user_tbl where user_tbl_id = ?"; //user_tbl_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userid);
			rs= pstmt.executeQuery(); // rs 는 데이터베이스에서 받아 온 데이터
			while (rs.next()) {
				ChatUserBean bean = new ChatUserBean();
				bean.setChatroomId(rs.getInt(1));                // chat_message의 pk값
				bean.setUserId(rs.getString(2));           // chat_message의 내용
				vlist.addElement(bean);                  // vlist에 저장
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt,rs);   // ??
		}
		return vlist;
	}
}
