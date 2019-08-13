package com.zhyshko.jsasynctest;

import java.sql.*;
import java.util.List;

public class Model {

	private String URL;
	private String user;
	private String password;
	private Connection connection;
	private String kinoTable = "kino";
	private String seancesTable = "seances";
	private String usersTable = "users";
	private String bookingTable = "booking";
	private String bookingsTable = "bookings";
	private String hallsTable = "halls";
	private String filmsTable = "films";
	private String soonTable = "soon";
	
	private KinoModel kinoModel;
	private SeancesModel seancesModel;
	private UsersModel usersModel;
	private BookingModel bookingModel;
	private BookingsModel bookingsModel;
	private HallsModel hallsModel;
	private FilmsModel filmsModel;
	private SoonModel soonModel;
	
	
	public Model(String url, String user, String password) {
		this.URL = url;
		this.user = user;
		this.password = password;
		
		//connect to DB
		connect();
		
		
		//create model for every table
		this.kinoModel = new KinoModel(this.connection, this.kinoTable);
		this.seancesModel = new SeancesModel(this.connection, this.seancesTable);
		this.usersModel = new UsersModel(this.connection, this.usersTable);
		this.bookingModel = new BookingModel(this.connection, this.bookingTable);
		this.bookingsModel = new BookingsModel(this.connection, this.bookingsTable);
		this.hallsModel = new HallsModel(this.connection, this.hallsTable);
		this.filmsModel = new FilmsModel(this.connection, this.filmsTable);
		this.soonModel = new SoonModel(this.connection, this.soonTable);
		
		
	}
	
	private boolean connect() {
		try {
			this.connection = DriverManager.getConnection(this.URL, user, password);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public List<String> getSoonFilms(){
		return soonModel.getFilms();	
	}
	public List<String> getOffers(){
		return seancesModel.getSoonFilms();
	}
	public List<String> getToday(){
		return seancesModel.getToday();
	}
	public long getSeanseIdForToday(String title,String date, String time, String hall) {
		return seancesModel.getSeanseIdForToday(title, date, time, hall);
	}
	public long getSeanseIdForFilms(String title,String date, String time, String hall) {
		return seancesModel.getSeanseIdForFilms(title, date, time, hall);
	}
	public int[][] getHallStatus(int seanseId){
		return seancesModel.getHallStatus(seanseId);
	}
	public void buy(String username, String data, int seanse) {
		bookingModel.buy(username,data, seanse);
	}
	public boolean register(String username, String email, String password) {
		return usersModel.register(username, email, password);
	}
	public boolean login(String username, String password) {
		return usersModel.login(username, password);
	}
	public List<String> getTickets(String username){
		return seancesModel.getTickets(username);
	}
	public List<String> getAllFilms(){
		return seancesModel.getAllFilms();
	}
	
	
}
