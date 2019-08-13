package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;


@Service
@Configurable
public class Model {


	
	@Autowired
	private SeancesModel seancesModel;
	
	@Autowired
	private UsersModel usersModel;
	
	@Autowired
	private BookingModel bookingModel;
	
	@Autowired
	private SoonModel soonModel;
	
	
	

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
	public void buy(String user, String data, String title, String date, String time, String hall) {
		bookingModel.buy(user, data, title, date, time, hall);
	}
	public boolean book(String username, String data, int seanse) {
		return bookingModel.book(username,data, seanse);
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
	public int getPrice(int seanseId) {
		return seancesModel.getPrice(seanseId);
	}
	
	
}
