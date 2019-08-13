package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
@Configurable
public class SoonModel {
	@Autowired
	private JdbcTemplate connection;

	public List<String> getFilms(){
		List<String> result = new ArrayList<>();
		String query = "SELECT name FROM "+Tables.soonTable();
		result = connection.query(query, (rs,rowNum)->rs.getString("name"));
		return result;
	}
	
	
	
}
