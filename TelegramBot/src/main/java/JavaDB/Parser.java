package JavaDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

	private Connection c;

	private static Parser p;

	private Parser() {
		try {
			this.c = DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Parser getParser() {
		if (p == null) {
			p = new Parser();
		}
		return p;
	}

	public void setConnection() {
		try {
			this.c = DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
		} catch (Exception e) {

		}
	}

	public int executeUpdate(String query) throws Exception {
		int result = 0;
		PreparedStatement st = c.prepareStatement(query);
		result = st.executeUpdate();
		return result;
	}

	public int executeUpdate(String query, Object[] params) throws Exception {
		int result = 0;
		int numberOfParams = params.length;
		PreparedStatement st = c.prepareStatement(query);
		for (int i = 1; i <= numberOfParams; i++) {
			st.setObject(i, params[i - 1]);
		}
		result = st.executeUpdate();
		return result;
	}

	public ResultSet executeQuery(String query) throws Exception {
		ResultSet result = null;
		PreparedStatement st = c.prepareStatement(query);
		ResultSet rs = st.executeQuery();
		result = rs;
		return result;
	}

	public ResultSet executeQuery(String query, Object[] params) throws Exception {
		ResultSet result = null;
		PreparedStatement st = c.prepareStatement(query);
		int numberOfParams = params.length;
		for (int i = 1; i <= numberOfParams; i++) {
			st.setObject(i, params[i - 1]);
		}
		ResultSet rs = st.executeQuery();
		result = rs;
		return result;
	}

}
