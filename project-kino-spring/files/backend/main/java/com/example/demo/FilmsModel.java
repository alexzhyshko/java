package com.example.demo;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class FilmsModel {
	@Autowired
	private JdbcTemplate connection;
	public FilmsModel() {
	
	}
	public HashMap<Integer, String> getFilms() {
		HashMap<Integer, String> result = new HashMap<>();
		String query = "SELECT * FROM "+Tables.filmsTable();
		List<String> temp = connection.query(query, (rs,rowNum)->rs.getInt("id")+","+ rs.getString("name"));
		for(String str : temp) {
			result.put(Integer.parseInt(str.split(",")[0]), str.split(",")[1]);
		}
		
		return result;
		
	}
	
}
