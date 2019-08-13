package com.zhyshko.jsasynctest;

import java.sql.*;

public class UsersModel {
	private String tableName;
	private Connection connection;
	public UsersModel(Connection c, String tableName) {
		this.connection = c;
		this.tableName = tableName;
	}
	public boolean register(String username, String email, String password) {
		
		String query = "INSERT INTO "+tableName+"(name, email,password) VALUES(?,?,?)";
		try {
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1, username);
			st.setString(2, email);
			st.setString(3, password);
			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;	
	}
	public boolean login(String username, String password) {
		String query = "SELECT password FROM "+tableName+" WHERE name=?";
		try {
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			String dbpass = "";
			while(rs.next()) {
				dbpass = rs.getString("password");
			}
			if(dbpass.equals(password)) {
				return true;
			}
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}
