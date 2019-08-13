package com.zhyshko.jsasynctest;

import java.sql.*;
import java.util.HashMap;


public class HallsModel {
	private static String tableName;
	private static Connection connection;
	public HallsModel(Connection c, String tableName) {
		HallsModel.connection = c;
		HallsModel.tableName = tableName;
	}
	public int[][] getHallDimensions(String seance){
		;
		String query = "SELECT * FROM "+tableName+" WHERE id=(SELECT hallid FROM seances WHERE id=?)";
		try {
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, Integer.parseInt(seance));
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				return new int[rs.getInt("line")][rs.getInt("colmns")];
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		return null;
	}
	
	public static HashMap<Integer, String> getHalls() {
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
