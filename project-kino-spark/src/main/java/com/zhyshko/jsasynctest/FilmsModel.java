package com.zhyshko.jsasynctest;

import java.sql.*;
import java.util.HashMap;
import java.util.List;


public class FilmsModel {
	private static String tableName;
	private static Connection connection;
	public FilmsModel(Connection c, String tableName) {
		FilmsModel.connection = c;
		FilmsModel.tableName = tableName;
	}
	public static HashMap<Integer, String> getFilms() {
		HashMap<Integer, String> result = new HashMap<>();
		try {
			String query = "SELECT * FROM "+tableName;
			Statement st = connection.prepareStatement(query);
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				result.put(rs.getInt("id"), rs.getString("name"));
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
