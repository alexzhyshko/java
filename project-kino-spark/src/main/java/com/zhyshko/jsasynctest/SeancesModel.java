package com.zhyshko.jsasynctest;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;



public class SeancesModel {
	private String tableName;
	private Connection connection;
	public SeancesModel(Connection c, String tableName) {
		this.connection = c;
		this.tableName = tableName;
	}
	public List<String> getSoonFilms(){
		String query  = "SELECT TIME_FORMAT(time, \"%H:%i\"),date,filmid,hallid FROM "+tableName+" WHERE date=? AND time>? AND time<?";
		List<String> result = new ArrayList<>();
		try {
			HashMap<Integer, String> films = FilmsModel.getFilms();
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1, LocalDate.now().toString());
			st.setString(2, LocalTime.now().toString());
			st.setString(3, LocalTime.now().plus(2,ChronoUnit.HOURS).toString());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				result.add(films.get(rs.getInt("filmid"))+" "+rs.getString(1));
			}
			if(result.isEmpty()) {
				result.add("Currently no offers");
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	public List<String> getToday(){
		List<String> result = new ArrayList<>();
		HashMap<Integer, String> halls = HallsModel.getHalls();
		HashMap<Integer, String> films = FilmsModel.getFilms();
		String query = "SELECT TIME_FORMAT(time, \"%H:%i\"),date,filmid,hallid FROM seances WHERE date=? AND time>?";
		try {
			
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1, LocalDate.now().toString());
			st.setString(2, LocalTime.now().toString());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				result.add(films.get(rs.getInt(3))+","+rs.getString(1)+","+halls.get(rs.getInt(4)));
			}
			
			result.sort(Comparator.comparing(e->e.toString()));
			
			return result;
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public long getSeanseIdForToday(String title,String date, String time, String hall) {
		long result=-1;
		HashMap<Integer, String> halls = HallsModel.getHalls();
		HashMap<Integer, String> films = FilmsModel.getFilms();
		int hallid = -1;
		int filmid = -1;
		for(int key : halls.keySet()) {
			if(halls.get(key).equals(hall)) {
				hallid = key;
				break;
			}
		}
		for(int key : films.keySet()) {
			if(films.get(key).equals(title)) {
				filmid = key;
				break;
			}
		}
		String query = "SELECT id FROM "+tableName+" WHERE filmid=? AND time=? AND hallid=? AND date=?";
		try {
		PreparedStatement st = connection.prepareStatement(query);
		
		st.setInt(1, filmid);
		st.setString(2, time);
		st.setInt(3,hallid);
		st.setString(4, date);
		
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			result = rs.getInt(1);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public long getSeanseIdForFilms(String title,String date, String time, String hall) {
		long result=-1;
		HashMap<Integer, String> halls = HallsModel.getHalls();
		HashMap<Integer, String> films = FilmsModel.getFilms();
		int hallid = -1;
		int filmid = -1;
		for(int key : halls.keySet()) {
			if(halls.get(key).equals(hall)) {
				hallid = key;
				break;
			}
		}
		for(int key : films.keySet()) {
			if(films.get(key).equals(title)) {
				filmid = key;
				break;
			}
		}
		String query = "SELECT id FROM "+tableName+" WHERE filmid=? AND time=? AND hallid=? AND date=STR_TO_DATE(?, '%d.%m-%Y')";
		try {
		PreparedStatement st = connection.prepareStatement(query);
		
		st.setInt(1, filmid);
		st.setString(2, time);
		st.setInt(3,hallid);
		st.setString(4, date);
		
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			result = rs.getInt(1);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int[][] getHallStatus(int seanseId){
		String query = "SELECT line, colmns FROM halls WHERE id=(SELECT hallid FROM seances WHERE id=?)";
		int rows = 0;
		int columns = 0;
		try {
			
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, seanseId);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				rows = rs.getInt("line");
				columns = rs.getInt("colmns");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		int[][] result = new int[rows][columns];
		
		for(int i=0; i<rows;i++) {
			for(int j=0; j<columns; j++) {
				result[i][j] = 0;
			}
		}
		
		List<String> temp = new ArrayList<>();
		query = "SELECT line, col, status FROM booking WHERE seansid=?";
		
		try {
		PreparedStatement st = connection.prepareStatement(query);
		st.setInt(1, seanseId);
		ResultSet rs = st.executeQuery();
		while(rs.next()) {
			temp.add(rs.getInt("line")+" "+rs.getInt("col")+" "+rs.getInt("status"));
		}
		}catch(Exception e) {
			e.printStackTrace();
		}

		
		for(String position : temp) {
			int row = Integer.parseInt(position.split(" ")[0]);
			int col = Integer.parseInt(position.split(" ")[1]);
			int status = Integer.parseInt(position.split(" ")[2]);
			result[row-1][col-1] = status;
		}
		
		return result;
		
	}
	public List<String> getTickets(String username){
		
		//get userid
		int userid = -1;
		
		try {
			String query = "SELECT id FROM users WHERE name=?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1, username);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				userid = rs.getInt(1);
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		//get all booking id's from 'bookings' table
		
		List<Integer> bookingids = new ArrayList<>();
		try {
			String query = "SELECT bookingid FROM bookings WHERE userid=?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, userid);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				bookingids.add(rs.getInt(1));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		HashMap<String, String> temp = new HashMap<>();
		
		
		//add to final result string:
		//"Title (time, date, hall): [places]"
		
		for(int id : bookingids) {
			try {
				String title = "";
				String query = "select name from films where id=(select filmid from seances where id=(SELECT seansid FROM booking WHERE id=?))";
				PreparedStatement st = connection.prepareStatement(query);
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				while(rs.next()) {
					title = rs.getString(1);
				}
				String time="";
				String date="";
				query = "select time,date from seances where id=(SELECT seansid FROM booking WHERE id=?)";
				st = connection.prepareStatement(query);
				st.setInt(1, id);
				rs = st.executeQuery();
				while(rs.next()) {
					
					time = rs.getString(1);
					date = rs.getString(2);
					
				}
				String key = title+"+("+time+", "+date.substring(5)+")";
				
				query = "select line,col from booking where seansid=(select id from seances where time=? AND date=? AND filmid=(select id from films where name=?)) AND id=?";
				st = connection.prepareStatement(query);
				st.setString(1, time);
				st.setString(2, date);
				st.setString(3, title);
				st.setInt(4, id);
				rs = st.executeQuery();
				
				if(LocalDate.parse(date).isBefore(LocalDate.now()) && !LocalDate.parse(date).equals(LocalDate.now())) {
					
				}else if(LocalTime.parse(time).isBefore(LocalTime.now())&&!LocalTime.parse(time).equals(LocalTime.now())){
				
				}else {
				while(rs.next()) {
					String tmp = "";
					tmp = temp.get(key);
					tmp+=rs.getInt(1)+"-"+rs.getInt(2)+", ";
					temp.put(key, tmp);
				}
				}
				
				
				
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		
		
		List<String> res = new ArrayList<>();
		for(String key : temp.keySet()) {
			String val = temp.get(key);
			val = val.substring(4,val.length()-2);
			res.add(key+"/"+val);
		}
		return res;
		
		
		
	}
	public List<String> getAllFilms(){
		List<Integer> allFilms = new ArrayList<>();
		try {
			String query = "SELECT DISTINCT filmid FROM "+tableName;
			Statement st = connection.prepareStatement(query);
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				allFilms.add(rs.getInt(1));
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		HashMap<String, String> tempDates = new HashMap<>();
		HashMap<String, String> tempTitles = new HashMap<>();
		
		
		try {
		for(int filmid : allFilms) {
			String title="";
			String query = "SELECT films.name, DATE_FORMAT(seances.date, \"%d.%m\"), TIME_FORMAT(seances.time, \"%H:%i\"),(SELECT name FROM halls WHERE id=seances.hallid) FROM seances INNER JOIN films ON films.id=seances.filmid WHERE filmid=? AND date>=?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, filmid);
			st.setString(2, LocalDate.now().toString());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(LocalTime.parse(rs.getString(3)).isAfter(LocalTime.now())) {
				String tmp = tempDates.get(rs.getString(2));
				tmp+=rs.getString(3)+" "+rs.getString(4)+"_";
				tempDates.put(rs.getString(2), tmp);
				}
				title = rs.getString(1);
				String val="";
				for(String key : tempDates.keySet()) {
					String value = tempDates.get(key);
					value = value.substring(4,value.length()-1);
					val += key+"-"+value+"/"; 
				}
				if(!val.isEmpty()) {
				val = val.substring(0,val.length()-1);
				tempTitles.put(title, val);
				}
				
			}
			
			
			tempDates.clear();
		}
		
		
		
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	
		
		
		List<String> result = new ArrayList<>();
		
		for(String key : tempTitles.keySet()) {
			result.add(key+"+"+tempTitles.get(key));
		}
		
		
		
		
		return result;
	}
}
