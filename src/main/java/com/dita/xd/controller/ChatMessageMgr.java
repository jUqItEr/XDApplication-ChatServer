package com.dita.xd.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;

import com.dita.xd.model.ChatMessageBean;

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
		try {// Ǯ�����
			con = pool.getConnection(); // ���ؼǰ�ü�� ������.
			sql = "select count(id) from user_tbl where id = ? and password = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);                    // sql 1��° ����ǥ�� id��
			//pstmt.setString(2, pwd);                // sql 2��° ����ǥ�� pwd��
			if(id.equals("aaa")) {
				pstmt.setString(2, otp_a);                //  �ӽ÷� otp ������ �α��� �Ǵ��� Ȯ���ϱ����� ����
			}
			else if(id.equals("bbb")) {
				pstmt.setString(2, otp_b);                //  �ӽ÷� otp ������ �α��� �Ǵ��� Ȯ���ϱ����� ����
			}
			
			
			rs= pstmt.executeQuery();             // sql ������ ������ rs�� ��������
			
			if(rs.next() && rs.getInt(1) == 1) {// rs�� �����Ͱ� �����ϴ� ���
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt,rs);
		}
		return flag;
	}
	
	//����
	public boolean insertChatMessage(ChatMessageBean bean) { // chat_message_tbl �� ������ �����ϱ�
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {// Ǯ�����
			con = pool.getConnection(); // ���ؼǰ�ü�� ������.
			sql = "insert chat_message_tbl values(null, ?, ?, ?, now(), ?)"; // chat_message_tbl�� ������ ������
			pstmt = con.prepareStatement(sql);                  
			pstmt.setString(1,bean.getContent());      //bean�� ����� Content�� 2��° ?�� ����
			pstmt.setInt(2,bean.getChatroomId());      //bean�� ����� ChatroomId�� 3��° ?�� ����
			pstmt.setString(3,bean.getUserId());       //bean�� ����� UserId�� 4��° ?�� ����
			//pstmt.setTimestamp(4,bean.getCreatedAt()); //bean�� ����� CreatedAt�� 5��° ?�� ����
			pstmt.setString(4,String.valueOf(bean.getReadState())); //bean�� ����� ReadState�� 5��° ?�� ����
			//System.out.println("pstmt : "+pstmt);
			int cnt = pstmt.executeUpdate(); // SQL�� ����
			if(cnt==1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con/*��ü �ݳ�*/, pstmt);
		}
		return flag;                                                             // db�� ���������� ����Ǿ��ٸ� true �ƴϸ� false
	}
	
	public ChatMessageBean getChatMessage(int id) { // ä�ù� id(pk)�� �Է��ϸ� 
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
			rs = pstmt.executeQuery();                       // rs�� num��°�� ����� ��ü�� ������.
			if(rs.next()) {
				bean.setId(rs.getInt("id"));          // rs�� ������ ���� bean���ٰ� �Ѱ���.
				bean.setContent(rs.getString("content"));         // �޼�������
				bean.setChatroomId(rs.getInt("chatroom_tbl_id")); // ���� id
				bean.setUserId(rs.getString("user_tbl_id"));      //�ۼ��� id
				bean.setCreatedAt(rs.getTimestamp("created_at")); //�ۼ��ð�
				bean.setReadState(rs.getString("read_state").charAt(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		System.out.println("id : "+ bean.getId());            //�޼��� id
		System.out.println("content : "+ bean.getContent());  //�޼��� ����
		System.out.println("user_tbl_id : "+ bean.getUserId()); // �ۼ��� id
		return bean;                                                 // bean�� num ��°�� ��ü�� ������ ���� ä�� return
	}

	public Vector<ChatMessageBean> getMsgList(int chatroomid, String userid){
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		ResultSet rs = null;
		Vector<ChatMessageBean> vlist = new Vector<ChatMessageBean>(); // �޼��� ���� �������� �����.
		try {// Ǯ�����
			con = pool.getConnection(); // ���ؼǰ�ü�� ������.
			sql = "select * from chat_message_tbl where chatroom_tbl_id = ? or user_tbl_id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, chatroomid);          //
			pstmt.setString(2, userid);
			rs= pstmt.executeQuery(); // rs �� �����ͺ��̽����� �޾� �� ������
			while (rs.next()) {
				ChatMessageBean bean = new ChatMessageBean();
				bean.setId(rs.getInt(1));                // chat_message�� pk��
				bean.setContent(rs.getString(2));           // chat_message�� ����
				bean.setChatroomId(rs.getInt(3));           // ä�ù��� ���� ���̵�
				bean.setUserId(rs.getString(4));          // ä�ù濡�� ä�� �ۼ���id
				bean.setCreatedAt(Timestamp.valueOf(rs.getString(5)));      // chat_message�� ���� �����
				bean.setReadState(rs.getString(6).charAt(0));
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
		ChatMessageMgr mgr =  new ChatMessageMgr();
		//UserBean bean = new UserBean();
		//bean.setName("��ȣ��2");
		//bean.setPhone("010-5555-2323");
		//bean.setAddress("�λ�� ������");
		//bean.setTeam("���Ǵ�");
		//boolean result = mgr.insertMember(bean);
		//System.out.println(result);
		//mgr.getMemberlist();
		//mgr.insertUser(bean);
		//mgr.getChatMessage(1); // 1��°�� �ִ� �޼�������, �ۼ���id, �ۼ� �ð�� ����
	}
}
