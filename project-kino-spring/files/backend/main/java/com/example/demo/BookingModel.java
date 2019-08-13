package com.example.demo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingModel {
	@Autowired
	private JdbcTemplate connection;

	public BookingModel() {

	}

	public void buy(String user, String data, String title, String date, String time, String hall) {

		String[] places = data.split(" ");

		for (String place : places) {
			int row = Integer.parseInt(place.split("-")[0]);
			int col = Integer.parseInt(place.split("-")[1]);
			String query = "UPDATE " + Tables.bookingTable() + " SET status=2 WHERE seansid = (SELECT id FROM "
					+ Tables.seancesTable() + " WHERE time=? AND date=? AND filmid=(SELECT id FROM "
					+ Tables.filmsTable() + " WHERE name=?) AND hallid=(SELECT id FROM " + Tables.hallsTable()
					+ " WHERE name=?)) AND line=? AND col=?";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			String newDate = LocalDate.parse(date, formatter).format(DateTimeFormatter.ISO_LOCAL_DATE);
			connection.update(query, new Object[] { time, newDate, title, hall, row, col });

		}

	}

	public boolean book(String user, String data, int seanse) {

		int cleaned = connection.query("SELECT cleaned FROM " + Tables.seancesTable() + " WHERE id=?",
				new Object[] { seanse }, (rs, rowNum) -> rs.getInt(1)).get(0);

		String timedate = connection.query("SELECT time,date FROM " + Tables.seancesTable() + " WHERE id=?",
				new Object[] { seanse }, (rs, rowNum) -> rs.getString(1) + "," + rs.getString(2)).get(0);

		LocalDate date = LocalDate.parse(timedate.split(",")[1]);
		LocalTime time = LocalTime.parse(timedate.split(",")[0]);

		if (cleaned == 0) {
			if (LocalTime.now().isAfter(time.minus(30, ChronoUnit.MINUTES)) && date.equals(LocalDate.now())) {
				connection.update("UPDATE " + Tables.seancesTable() + " SET cleaned=1 WHERE id=?",
						new Object[] { seanse });
			}
		}

		String[] places = data.split(",");

		int userid = -1;
		String query = "SELECT id FROM " + Tables.usersTable() + " WHERE name=?";
		userid = connection.query(query, new Object[] { user }, (rs, rowNum) -> rs.getInt("id")).get(0);

		for (String place : places) {
			int row = Integer.parseInt(place.split("-")[0]);
			int col = Integer.parseInt(place.split("-")[1]);

			int status = 0;
			try {
				status = connection
						.query("SELECT status FROM " + Tables.bookingTable() + " WHERE line=? AND col=? AND seansid=?",
								new Object[] { row, col, seanse }, (rs, rowNum) -> rs.getInt(1))
						.get(0);
			} catch (Exception e) {
				status = 0;
			}

			if (status != 0) {
				return false;
			} else {
				query = "INSERT INTO " + Tables.bookingTable() + "(seansid, line, col, status) VALUES(?,?,?,?)";
				connection.update(query, new Object[] { seanse, row, col, 1 });
				String query2 = "SELECT MAX(id) FROM " + Tables.bookingTable();
				int bookingid = -1;
				bookingid = connection.query(query2, new Object[] {}, (rs, rowNum) -> rs.getInt(1)).get(0);
				String query3 = "INSERT INTO " + Tables.bookingsTable() + " VALUES(?,?)";
				connection.update(query3, userid, bookingid);
			}
		}
		return true;

	}

}
