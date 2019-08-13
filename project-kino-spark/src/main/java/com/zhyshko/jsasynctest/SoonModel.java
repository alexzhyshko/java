package com.zhyshko.jsasynctest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SoonModel {
	private String tableName;
	private Connection connection;
	public SoonModel(Connection c, String tableName) {
		this.connection = c;
		this.tableName = tableName;
	}
	public List<String> getFilms(){
		List<String> result = new ArrayList<>();
		String query = "SELECT name FROM "+tableName;
		try {
			Statement st = connection.prepareStatement(query);
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				String temp = rs.getString("name");
				result.add(temp);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
}
