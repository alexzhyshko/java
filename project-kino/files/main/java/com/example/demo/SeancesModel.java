package com.example.demo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class SeancesModel {
	@Autowired
	private JdbcTemplate connection;
	
	@Autowired
	private FilmsModel filmsModel;
	
	
	@Autowired
	private HallsModel hallsModel;

	public List<String> getSoonFilms(){
		String query  = "SELECT TIME_FORMAT(time, \"%H:%i\"),date,filmid,hallid FROM "+Tables.seancesTable()+" WHERE date=? AND time>? AND time<?";
		List<String> result = new ArrayList<>();
		HashMap<Integer, String> films = filmsModel.getFilms();
		result = connection.query(query,new Object[] {LocalDate.now().toString(),LocalTime.now().toString(),LocalTime.now().plus(2,ChronoUnit.HOURS).toString()}, (rs,rowNum)->films.get(rs.getInt("filmid"))+" "+rs.getString(1));
		if(result.isEmpty()) {
			result.add("Currently no offers");
		}
		return result;
	}
	public List<String> getToday(){
		List<String> result = new ArrayList<>();
		HashMap<Integer, String> halls = hallsModel.getHalls();
		HashMap<Integer, String> films = filmsModel.getFilms();
		String query = "SELECT TIME_FORMAT(time, \"%H:%i\"),date,filmid,hallid FROM "+Tables.seancesTable()+" WHERE date=? AND time>?";
		result = connection.query(query,new Object[] {LocalDate.now().toString(),LocalTime.now().toString()}, (rs,rowNum)->films.get(rs.getInt(3))+","+rs.getString(1)+","+halls.get(rs.getInt(4)));
		result.sort(Comparator.comparing(e->e.toString()));
		return result;
		
	}
	public long getSeanseIdForToday(String title,String date, String time, String hall) {
		long result=-1;
		HashMap<Integer, String> halls = hallsModel.getHalls();
		HashMap<Integer, String> films = filmsModel.getFilms();
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
		String query = "SELECT id FROM "+Tables.seancesTable()+" WHERE filmid=? AND time=? AND hallid=? AND date=?";
		result = connection.query(query, new Object[] {filmid,time,hallid,date},(rs,rowNum)->rs.getInt(1)).get(0);
		return result;
	}
	
	
	public long getSeanseIdForFilms(String title,String date, String time, String hall) {
		long result=-1;
		HashMap<Integer, String> halls = hallsModel.getHalls();
		HashMap<Integer, String> films = filmsModel.getFilms();
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
		String query = "SELECT id FROM "+Tables.seancesTable()+" WHERE filmid=? AND time=? AND hallid=? AND date=STR_TO_DATE(?, '%d.%m-%Y')";
		result = connection.query(query, new Object[] {filmid,time,hallid,date},(rs,rowNum)->rs.getInt(1)).get(0);
		return result;
	}
	
	public int[][] getHallStatus(int seanseId){
		String query = "SELECT line, colmns FROM "+Tables.hallsTable()+" WHERE id=(SELECT hallid FROM seances WHERE id=?)";
		int rows = 0;
		int columns = 0;
		String complex = connection.query(query, new Object[] {seanseId},(rs,rowNum)->rs.getInt("line")+","+rs.getInt("colmns")).get(0);
		
		rows = Integer.parseInt(complex.split(",")[0]);
		columns = Integer.parseInt(complex.split(",")[1]);
		
		
		int[][] result = new int[rows][columns];
		
		for(int i=0; i<rows;i++) {
			for(int j=0; j<columns; j++) {
				result[i][j] = 0;
			}
		}
		
		List<String> temp = new ArrayList<>();
		query = "SELECT line, col, status FROM "+Tables.bookingTable()+" WHERE seansid=?";
		temp = connection.query(query, new Object[] {seanseId},(rs,rowNum)->rs.getInt("line")+" "+rs.getInt("col")+" "+rs.getInt("status"));
		
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
		String query = "SELECT id FROM "+Tables.usersTable()+" WHERE name=?";
		userid = connection.query(query, new Object[] {username}, (rs,rowNum)->rs.getInt(1)).get(0);
		
		
		//get all booking id's from 'bookings' table
		query = "SELECT bookingid FROM "+Tables.bookingsTable()+" WHERE userid=?";
		List<Integer> bookingids = new ArrayList<>();
		bookingids = connection.query(query, new Object[] {userid}, (rs,rowNum)->rs.getInt(1));
		
		
		HashMap<String, String> temp = new HashMap<>();
		
		
		//add to final result string:
		//"Title (time, date, hall): [places]"
		
		List<String> str = new ArrayList<>();
		
		String status = "";
		String price = "";
		String key="";
		for(int id : bookingids) {
				String title = "";
				query = "select name from "+Tables.filmsTable()+" where id=(select filmid from "+Tables.seancesTable()+" where id=(SELECT seansid FROM "+Tables.bookingTable()+" WHERE id=?))";
				title = connection.query(query,new Object[] {id}, (rs,rowNum)->rs.getString(1)).get(0);
				String time="";
				String date="";
				String hall = "";
				query = "select time,date, (select name from "+Tables.hallsTable()+" where id="+Tables.seancesTable()+".hallid) from "+Tables.seancesTable()+" where id=(SELECT seansid FROM "+Tables.bookingTable()+" WHERE id=?)";
				String complex = connection.query(query, new Object[] {id},(rs,rowNum)->rs.getString(1)+","+rs.getString(2)+","+rs.getString(3)).get(0);
				time = complex.split(",")[0];
				date = complex.split(",")[1];
				hall = complex.split(",")[2];

				
				
				
				query = "select line,col,status,(SELECT price FROM "+Tables.filmsTable()+" WHERE name=?) from "+Tables.bookingTable()+" where seansid=(select id from "+Tables.seancesTable()+" where time=? AND date=? AND filmid=(select id from "+Tables.filmsTable()+" where name=?)) AND id=?";
				if(LocalDate.parse(date).isBefore(LocalDate.now()) && !LocalDate.parse(date).equals(LocalDate.now())) {
					key="";
					continue;
				}else if(LocalDate.parse(date).equals(LocalDate.now())&&LocalTime.parse(time).isBefore(LocalTime.now())&&!LocalTime.parse(time).equals(LocalTime.now())){
					
						key="";
						continue;
				
				}else{
					str.add(connection.query(query, new Object[] {title,time,date,title,id}, (rs,rowNum)->rs.getInt(1)+"-"+rs.getInt(2)+"_"+Status.status(rs.getInt(3))+"_"+rs.getInt(4)).get(0));
					
					for(String st : str) {
						status = st.split("_")[1];
						price = st.split("_")[2];
						key = title+", \""+hall+"\" Hall, "+status+", "+price+"+("+time+", "+date.substring(5)+")";
						String tmp = temp.get(key);
						String newst = st.split("_")[0]+", ";
						tmp+=newst;
						temp.put(key, tmp);
					}
					
				}
				str.clear();
		}
		
	
		
		
		
		
		List<String> res = new ArrayList<>();
		for(String newkey : temp.keySet()) {
			String val = temp.get(newkey);
			if(!val.isEmpty()) {
				val = val.substring(4);
				res.add(newkey+"/"+val);
			}
			
		}
		
		
		
		
		return res;
		
		
		
	}
	public List<String> getAllFilms(){
		List<Integer> allFilms = new ArrayList<>();
		String query = "SELECT DISTINCT filmid FROM "+Tables.seancesTable();
		allFilms = connection.query(query, (rs,rowNum)->rs.getInt(1));
		
		HashMap<String, String> tempDates = new HashMap<>();
		HashMap<String, String> tempTitles = new HashMap<>();
		
		
		try {
		for(int filmid : allFilms) {
			String title="";
			String query2 = "SELECT "+Tables.filmsTable()+".name, DATE_FORMAT("+Tables.seancesTable()+".date, \"%d.%m\"), TIME_FORMAT("+Tables.seancesTable()+".time, \"%H:%i\"),(SELECT name FROM "+Tables.hallsTable()+" WHERE id="+Tables.seancesTable()+".hallid) FROM "+Tables.seancesTable()+" INNER JOIN "+Tables.filmsTable()+" ON "+Tables.filmsTable()+".id="+Tables.seancesTable()+".filmid WHERE filmid=? AND date>=?";
			List<FilmInfo> temp = connection.query(query2, new Object[] {filmid,LocalDate.now().toString()},(rs1,rowNum)->new FilmInfo(rs1.getString(1), rs1.getString(2), rs1.getString(3), rs1.getString(4)));
			for(FilmInfo film : temp){
				if(LocalTime.parse(film.getTime()).isAfter(LocalTime.now())||LocalDate.parse(film.getFormattedDate()).isAfter(LocalDate.now())) {
				String tmp = tempDates.get(film.getDate());
				tmp+=film.getTime()+" "+film.getHall()+"_";
				tempDates.put(film.getDate(), tmp);
				}
				title = film.getFilmname();
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
	
	public int getPrice(int seanseId) {
		int price=0;
		String query = "SELECT price FROM "+Tables.filmsTable()+" WHERE id = (SELECT filmid FROM "+Tables.seancesTable()+" WHERE id=?)";
		price = connection.query(query, new Object[] {seanseId},(rs, rowNum)->rs.getInt(1)).get(0);
		return price;
	}
	
}
