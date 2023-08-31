package com.dita.xd.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;

import com.dita.xd.model.ChatMessageBean;
import com.dita.xd.service.implementation.DBConnectionMgr1;

public class ChatMessageMgr {
	private DBConnectionMgr1 pool;
	public ChatMessageMgr() {
		pool = DBConnectionMgr1.getInstance();
	}

	public boolean loginChk(String id, String pwd) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		String otp_a = "ZjdlZjBjMjFhYmQ3MWZlNTk4OWU1NjNlNGI2ODkzY2VhZWY1MTkyZDdiMTBjYTRmNzdhNzJmY2IzMDk2ODgwMTk1YjI0N2IzMGM5MDFiN2E5NDA3MzViOGNkYTcxNWRmZGJjYTY0YjI=";
		String otp_b = "YjE2ZjAzODA1MjM0YzQ3NmI0YjQ4NjI5ZGFhNWFhM2EwN2ZjYmQyYjRjMDIwOTQ3MjEyZjJlOWVmOWVhOTg1YTgyMWJhNGY5NzYzMzBiNDRlOGVlODdlZmU5NzMzZmYyNzU5ZjhiMzE=";
		boolean flag = false;
		try {// 풀링기법
			con = pool.getConnection(); // 컨넥션객체를 빌려옴.
			sql = "select count(id) from user_tbl where id = ? and password = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);                    // sql 1번째 물음표를 id로
			//pstmt.setString(2, pwd);                // sql 2번째 물음표를 pwd로
			if(id.equals("aaa")) {
				pstmt.setString(2, otp_a);                //  임시로 otp 지정후 로그인 되는지 확인하기위해 만듬
			}
			else if(id.equals("bbb")) {
				pstmt.setString(2, otp_b);                //  임시로 otp 지정후 로그인 되는지 확인하기위해 만듬
			}


			rs= pstmt.executeQuery();             // sql 실행후 데이터 rs에 가져오기

			if(rs.next() && rs.getInt(1) == 1) {// rs에 데이터가 존재하는 경우
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt,rs);
		}
		return flag;
	}

	//저장
	public boolean insertChatMessage(ChatMessageBean bean) { // chat_message_tbl 에 데이터 저장하기
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {// 풀링기법
			con = pool.getConnection(); // 컨넥션객체를 빌려옴.
			sql = "insert chat_message_tbl values(null, ?, ?, ?, now(), ?)"; // chat_message_tbl에 저장할 데이터
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,bean.getContent());      //bean에 저장된 Content을 2번째 ?에 지정
			pstmt.setInt(2,bean.getChatroomId());      //bean에 저장된 ChatroomId을 3번째 ?에 지정
			pstmt.setString(3,bean.getUserId());       //bean에 저장된 UserId을 4번째 ?에 지정
			//pstmt.setTimestamp(4,bean.getCreatedAt()); //bean에 저장된 CreatedAt을 5번째 ?에 지정
			pstmt.setString(4,String.valueOf(bean.getReadState())); //bean에 저장된 ReadState을 5번째 ?에 지정
			//System.out.println("pstmt : "+pstmt);
			int cnt = pstmt.executeUpdate(); // SQL문 실행
			if(cnt==1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con/*객체 반납*/, pstmt);
		}
		return flag;                                                             // db에 정상적으로 저장되었다면 true 아니면 false
	}

	public ChatMessageBean getChatMessage(int id) { // 채팅방 id(pk)를 입력하면
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		ChatMessageBean bean = new ChatMessageBean();
		try {
			con = pool.getConnection();
			sql = "select * from chat_message_tbl where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();                       // rs에 num번째의 저장된 개체를 가져옴.
			if(rs.next()) {
				bean.setId(rs.getInt("id"));          // rs에 가져온 값을 bean에다가 넘겨줌.
				bean.setContent(rs.getString("content"));         // 메세지내용
				bean.setChatroomId(rs.getInt("chatroom_tbl_id")); // 세션 id
				bean.setUserId(rs.getString("user_tbl_id"));      //작성자 id
				bean.setCreatedAt(rs.getTimestamp("created_at")); //작성시각
				bean.setReadState(rs.getString("read_state").charAt(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		System.out.println("id : "+ bean.getId());            //메세지 id
		System.out.println("content : "+ bean.getContent());  //메세지 내용
		System.out.println("user_tbl_id : "+ bean.getUserId()); // 작성자 id
		return bean;                                                 // bean은 num 번째의 개체의 정보를 가진 채로 return
	}

	public Vector<ChatMessageBean> getMsgList(int chatroomid, String userid){
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector<ChatMessageBean> vlist = new Vector<ChatMessageBean>(); // 메세지 빈은 동적으로 만든디.
		try {// 풀링기법
			con = pool.getConnection(); // 컨넥션객체를 빌려옴.
			sql = "select * from chat_message_tbl where chatroom_tbl_id = ? or user_tbl_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, chatroomid);          //
			pstmt.setString(2, userid);
			rs= pstmt.executeQuery(); // rs 는 데이터베이스에서 받아 온 데이터
			while (rs.next()) {
				ChatMessageBean bean = new ChatMessageBean();
				bean.setId(rs.getInt(1));                // chat_message의 pk값
				bean.setContent(rs.getString(2));           // chat_message의 내용
				bean.setChatroomId(rs.getInt(3));           // 채팅방의 세션 아이디
				bean.setUserId(rs.getString(4));          // 채팅방에서 채팅 작성자id
				bean.setCreatedAt(Timestamp.valueOf(rs.getString(5)));      // chat_message의 언제 썼는지
				bean.setReadState(rs.getString(6).charAt(0));
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
