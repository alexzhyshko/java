package com.zhyshko.jsasynctest;

import java.sql.Connection;

public class BookingsModel {
	private String tableName;
	private Connection connection;
	public BookingsModel(Connection c, String tableName) {
		this.connection = c;
		this.tableName = tableName;
	}
}
