package com.zhyshko.jsasynctest;

import java.sql.Connection;

public class KinoModel {
	private String tableName;
	private Connection connection;
	public KinoModel(Connection c, String tableName) {
		this.connection = c;
		this.tableName = tableName;
	}
}
