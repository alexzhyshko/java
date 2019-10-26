package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.postgresql.util.PSQLException;

import JavaDB.Parser;
import model.DBUser;

public class ModelController {

	private static ModelController controller;
	private static Parser parser;

	private ModelController() {
		if (parser == null) {
			parser = Parser.getParser();
		}
	}

	private void establishConnection() {
		parser.setConnection();
	}

	public static ModelController get() {
		if (controller == null) {
			controller = new ModelController();
		}
		return controller;
	}

	public synchronized boolean usedPromo(String id) {
		boolean result = false;
		try {

			String query = "SELECT usedpromo FROM users WHERE id=?";
			ResultSet rs = parser.executeQuery(query, new Object[] { id });
			while (rs.next()) {
				if (rs.getString(1).equals("t")) {
					result = true;
				}
			}

		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}

	public synchronized boolean hasOrders(String id) {
		boolean result = false;
		try {

			String query = "SELECT EXISTS(SELECT * FROM orders WHERE userId=?)";
			ResultSet rs = parser.executeQuery(query, new Object[] { id });
			while (rs.next()) {
				if (rs.getString(1).equals("t")) {
					result = true;
				}
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}

	public synchronized boolean userExists(String id) {
		boolean result = false;
		try {
			ResultSet rs = parser.executeQuery("SELECT EXISTS(SELECT * FROM users WHERE id=?)", new Object[] { id });
			while (rs.next()) {
				if (rs.getString(1).equals("t")) {
					result = true;
				}
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (SQLIntegrityConstraintViolationException e) {
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	public synchronized boolean hasContact(String id) {
		boolean result = true;
		try {
			ResultSet rs = parser.executeQuery("SELECT number FROM users WHERE id=?", new Object[] { id });
			while (rs.next()) {
				if (rs.getString(1).trim().isEmpty()) {
					result = false;
				}
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (SQLIntegrityConstraintViolationException e) {
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

	public synchronized boolean saveUser(String id, String username, String firstName, String lastName,
			int lastchoice) {
		try {
			int res = 0;
			ResultSet rs = parser.executeQuery("SELECT usedpromo FROM users WHERE id=?", new Object[] { id });
			while (rs.next()) {
				res = rs.getInt(1);
			}
			int result = parser.executeUpdate(
					"INSERT INTO users(id, username, firstname, lastname, number, lastchoice, usedpromo) VALUES(?,?,?,?,'',?,?)",
					new Object[] { id, username, firstName, lastName, lastchoice, res });
			return result != 0 ? true : false;
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (SQLIntegrityConstraintViolationException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized boolean updateUser(String id, String firstName, String lastName, String contact,
			int lastchoice) {
		try {
			int result = parser.executeUpdate(
					"UPDATE users SET firstname=?, lastname=?, number=?, lastchoice=? WHERE id=?",
					new Object[] { firstName, lastName, contact, lastchoice, id });
			return result != 0 ? true : false;
		} catch (SQLIntegrityConstraintViolationException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized int getUserState(String id) {
		int res = 0;
		try {
			ResultSet rs = parser.executeQuery("SELECT state FROM users WHERE id=?", new Object[] { id });
			while (rs.next()) {
				res = Integer.parseInt(rs.getString(1));
			}
			return res;
		} catch (PSQLException connExc) {
			establishConnection();
			return res;

		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}
	}

	public synchronized void setUserState(String id, int state) {
		try {

			parser.executeUpdate("UPDATE users SET state=? WHERE id=?", new Object[] { state, id });

		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public synchronized void incrementUserState(String id) {
		try {
			ResultSet rs = parser.executeQuery("SELECT state FROM users WHERE id=?", new Object[] { id });
			int state = 0;
			while (rs.next()) {
				state = rs.getInt(1);
			}
			state++;
			if (state == 4) {
				state = 0;
			}
			parser.executeUpdate("UPDATE users SET state=? WHERE id=?", new Object[] { state, id });
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public synchronized DBUser getUser(String id) {
		DBUser res = null;
		try {

			ResultSet rs = parser.executeQuery("SELECT * FROM users WHERE id=?", new Object[] { id });
			while (rs.next()) {
				res = new DBUser(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						Integer.parseInt(rs.getString(6)));
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}
		return res;
	}

	public synchronized boolean saveOrder(String userId, int goodId) {
		try {
			ResultSet rs = parser.executeQuery("SELECT MAX(id) FROM orders");
			int id = -1;
			while (rs.next()) {
				id = rs.getInt(1);
			}
			id++;
			parser.executeUpdate("INSERT INTO orders(id,userId, goodId) VALUES(?,?,?)",
					new Object[] { id, userId, goodId });
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public synchronized int getGoodId(String goodName, int goodPrice) {
		try {
			ResultSet rs = parser.executeQuery("SELECT id FROM goods WHERE name=? AND price=?",
					new Object[] { goodName, goodPrice });
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (SQLIntegrityConstraintViolationException e) {
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	public synchronized void setLastChoice(int productId, String userId) {
		try {
			parser.executeUpdate("UPDATE users SET lastchoice=? WHERE id=?", new Object[] { productId, userId });
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (SQLIntegrityConstraintViolationException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized int getLastChoice(String userId) {
		try {
			ResultSet rs = parser.executeQuery("SELECT lastchoice FROM users WHERE id=?", new Object[] { userId });
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (SQLIntegrityConstraintViolationException e) {
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	public synchronized List<Product> getAllGoods() {
		List<Product> res = new ArrayList<>();
		try {
			ResultSet rs = parser.executeQuery("SELECT * FROM goods");
			while (rs.next()) {
				res.add(new Product(rs.getInt(1), rs.getString(2), rs.getInt(3)));
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	public synchronized String getAdminChatId() {
		String res = "";
		try {
			ResultSet rs = parser.executeQuery("SELECT chatid FROM admindata;");
			while (rs.next()) {
				res = rs.getString(1);
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	public List<Product> getUserOrders(String id) {
		List<Product> res = new ArrayList<>();
		try {
			String query = "select goods.id, goods.name, goods.price from orders inner join goods ON orders.goodid = goods.id where orders.userid = ?;";
			ResultSet rs = parser.executeQuery(query, new Object[] { id });
			while (rs.next()) {
				res.add(new Product(rs.getInt(1), rs.getString(2), rs.getInt(3)));
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public String generatePromoForUser(String id) {
		String result = null;
		try {
			ResultSet rs = parser.executeQuery("SELECT EXISTS(SELECT * FROM promo WHERE ownerid=?)",
					new Object[] { id });
			while (rs.next()) {
				if (rs.getString(1).equals("t")) {
					return null;
				}
				String uuid = UUID.randomUUID().toString();
				uuid = uuid.replace("-", "");
				uuid = uuid.substring(uuid.length() - 8, uuid.length() - 1);
				uuid += id;
				parser.executeUpdate("INSERT INTO promo(value, ownerid, used) values(?,?,?);",
						new Object[] { uuid, id, 0 });
				result = uuid;
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public String getUserPromo(String userid) {
		String result = null;
		try {
			ResultSet rs = parser.executeQuery("SELECT EXISTS(SELECT * FROM promo WHERE ownerid=?)",
					new Object[] { userid });
			while (rs.next()) {
				if (rs.getString(1).equals("f")) {
					return null;
				}
				rs = parser.executeQuery("SELECT value FROM promo WHERE ownerid=?", new Object[] { userid });
				while (rs.next()) {
					return rs.getString(1);
				}
			}

		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;

	}

	public boolean usePromo(String code) {
		try {
			ResultSet rs = parser.executeQuery("SELECT EXISTS(SELECT * FROM promo WHERE value=?)",
					new Object[] { code });
			while (rs.next()) {
				if (rs.getString(1).equals("f")) {
					return false;
				}
				rs = parser.executeQuery("SELECT used FROM promo WHERE value=?", new Object[] { code });
				while (rs.next()) {
					if (rs.getString(1).equals("t")) {
						return false;
					}
					parser.executeUpdate("UPDATE promo SET used=1 WHERE value=? ", new Object[] { code });
					return true;
				}
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getPromoOwner(String code) {
		try {
			ResultSet rs = parser.executeQuery("SELECT EXISTS(SELECT ownerid FROM promo WHERE value=?)",
					new Object[] { code });
			while (rs.next()) {
				if (rs.getString(1).equals("f")) {
					return null;
				}
				rs = parser.executeQuery("SELECT ownerid FROM promo WHERE value=?", new Object[] { code });
				while (rs.next()) {
					return rs.getString(1);
				}
			}
		} catch (PSQLException connExc) {
			establishConnection();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public String getAdminUsername() {
		String result = "stekloKPI"; // backup name, if no admin registered
		try {
			ResultSet rs = parser.executeQuery("SELECT username FROM admindata");
			while (rs.next()) {
				result = rs.getString(1);
				break; // gets only the first admin username(main admin)
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;

	}

}
