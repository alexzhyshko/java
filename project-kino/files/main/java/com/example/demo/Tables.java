package com.example.demo;

public class Tables {
	String bookingTable;
	String bookingsTable;
	String filmsTable;
	String hallsTable;
	String kinoTable;
	String seancesTable;
	String soonTable;
	String usersTable;
	
	public Tables() {
		configure();
	}
	
	//set params for every table
	public void configure() {
		
		
		this.bookingTable = "booking";
		this.bookingsTable = "bookings";
		this.filmsTable = "films";
		this.hallsTable = "halls";
		this.kinoTable = "kino";
		this.seancesTable = "seances";
		this.soonTable = "soon";
		this.usersTable = "users";
		
		
	}
	
	public static String bookingTable() {
		try {
			return Tables.class.getDeclaredField("bookingTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String bookingsTable() {
		try {
			return Tables.class.getDeclaredField("bookingsTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String filmsTable() {
		try {
			return Tables.class.getDeclaredField("filmsTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String hallsTable() {
		try {
			return Tables.class.getDeclaredField("hallsTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String kinoTable() {
		try {
			return Tables.class.getDeclaredField("kinoTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String seancesTable() {
		try {
			return Tables.class.getDeclaredField("seancesTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String soonTable() {
		try {
			return Tables.class.getDeclaredField("soonTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String usersTable() {
		try {
			return Tables.class.getDeclaredField("usersTable").get(Tables.class.newInstance()).toString();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
