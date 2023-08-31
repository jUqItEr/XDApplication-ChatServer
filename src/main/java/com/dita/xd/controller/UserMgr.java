package com.dita.xd.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dita.xd.model.UserBean;
import com.dita.xd.service.implementation.DBConnectionMgr1;


public class UserMgr {

	private DBConnectionMgr1 pool;
	public UserMgr() {
		pool = DBConnectionMgr1.getInstance();
	}
	
	public boolean insertUser(UserBean bean) { 
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = null;
		boolean flag = false;
		try {
			con = pool.getConnection(); 
			sql = "insert user_tbl values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(sql);                  
			pstmt.setString(1,bean.getUserId());    
			pstmt.setString(2,bean.getPassword());    
			pstmt.setString(3,bean.getEmail()); 
			pstmt.setString(4,bean.getNickname());    
			pstmt.setString(5,bean.getProfileImage());
			pstmt.setString(6,bean.getHeaderImage());
			pstmt.setString(7,bean.getAddress());
			pstmt.setString(8,String.valueOf(bean.getGender()));
			pstmt.setString(9,bean.getWebsite());
			pstmt.setDate(10,bean.getBirthday());
			pstmt.setString(11,bean.getIntroduce());
			pstmt.setTimestamp(12,bean.getCreatedAt());
			int cnt = pstmt.executeUpdate(); 
			if(cnt==1) flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return flag;                                                            
	}
	
	public UserBean getUser(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		UserBean bean = new UserBean();
		try {
			con = pool.getConnection();
			sql = "select * from user_tbl where id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();                       
			if(rs.next()) {
				bean.setUserId(rs.getString("id")); 
				bean.setPassword(rs.getString("password"));
				bean.setEmail(rs.getString("email"));
				bean.setNickname(rs.getString("nickname"));
				bean.setProfileImage(rs.getString("profile_image"));
				bean.setHeaderImage(rs.getString("header_image"));
				bean.setAddress(rs.getString("address"));
				bean.setGender((rs.getString("gender")).charAt(0));
				bean.setWebsite(rs.getString("website"));
				bean.setBirthday(rs.getDate("birthday"));
				bean.setIntroduce(rs.getString("introduce"));
				bean.setCreatedAt(rs.getTimestamp("created_at"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		System.out.println("userid : "+ bean.getUserId());
		System.out.println("userEmail : "+ bean.getEmail());
		System.out.println("userPassword : "+ bean.getPassword());
		return bean;                                                 
	}
}
