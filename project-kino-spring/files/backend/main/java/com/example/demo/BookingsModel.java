package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class BookingsModel {
	@Autowired
	private JdbcTemplate connection;
	public BookingsModel() {

	}
}
