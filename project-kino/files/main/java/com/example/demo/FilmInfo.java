package com.example.demo;

import java.time.LocalDate;

public class FilmInfo {
	private String filmname;
	private String date;
	private String time;
	private String hall;
	
	private String formattedDate;
	
	public FilmInfo(String str1, String str2, String str3, String str4) {
		this.filmname = str1;
		this.date = str2;
		this.time = str3;
		this.hall = str4;
		System.out.println();
		this.formattedDate = LocalDate.now().getYear()+"-"+this.date.substring(3,5)+"-"+this.date.substring(0,2);
	}
	public String getFilmname() {
		return filmname;
	}
	public void setFilmname(String filmname) {
		this.filmname = filmname;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getHall() {
		return hall;
	}
	public void setHall(String hall) {
		this.hall = hall;
	}
	public String getFormattedDate() {
		return formattedDate;
	}
	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
	
	
	
}
