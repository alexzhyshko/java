package com.zhyshko.jsasynctest;

import java.sql.*;

public class BookingModel {
	private String tableName;
	private Connection connection;
	public BookingModel(Connection c, String tableName) {
		this.connection = c;
		this.tableName = tableName;
	}
	public void buy(String user, String data, int seanse) {

		String[] places = data.split(",");
		
		int userid = -1;
		try {
			String query = "SELECT id FROM users WHERE name=?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1,user);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				userid = rs.getInt("id");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		for(String place : places) {
			int row = Integer.parseInt(place.split("-")[0]);
			int col = Integer.parseInt(place.split("-")[1]);
			String query = "INSERT INTO "+tableName+"(seansid, line, col, status) VALUES(?,?,?,?)";
			String query2 = "SELECT MAX(id) FROM "+tableName;
			
			try {
				PreparedStatement st = connection.prepareStatement(query);
				st.setInt(1, seanse);
				st.setInt(2, row);
				st.setInt(3, col);
				st.setInt(4, 2);
				st.executeUpdate();
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				PreparedStatement st = connection.prepareStatement(query2);
				int bookingid=-1;
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					bookingid = rs.getInt(1);
				}
				String query3 = "INSERT INTO bookings VALUES(?,?)";
				PreparedStatement st2 = connection.prepareStatement(query3);
				st2.setInt(1, userid);
				st2.setInt(2, bookingid);
				st2.executeUpdate();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
