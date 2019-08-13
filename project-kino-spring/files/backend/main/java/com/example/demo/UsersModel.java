package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class UsersModel {
	@Autowired
	private JdbcTemplate connection;
	boolean res;
	public UsersModel() {
		
	}
	public boolean register(String username, String email, String password) {
		
		String query = "INSERT INTO "+Tables.usersTable()+"(name, email,password) VALUES(?,?,?)";
		connection.update(query, new Object[] {username,email,password});
		return true;	
	}
	public boolean login(String username, String password) {
		String query = "SELECT password FROM "+Tables.usersTable()+" WHERE name=?";
		String dbpass = "";
		try {
		dbpass = connection.query(query, new Object[] {username}, (rs,rowNum)->rs.getString("password")).get(0);
		}catch(IndexOutOfBoundsException e) {
			return false;
		}
		return dbpass.equals(password);
	}
	
	
}
