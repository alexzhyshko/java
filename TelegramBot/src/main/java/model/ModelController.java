package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.DBUser;

public class ModelController {
	
	private static Connection c;

	private static ModelController controller;

	private ModelController() {
		try {
			String dbUrl = System.getenv("JDBC_DATABASE_URL");
			c = DriverManager.getConnection(dbUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			PreparedStatement st = c.prepareStatement(query);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equals("t")) {
					result = true;
				}
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}
	
	public synchronized boolean hasOrders(String id) {
		boolean result = false;
		try {
			
			
			String query = "SELECT EXISTS(SELECT * FROM orders WHERE userId=?)";
			PreparedStatement st = c.prepareStatement(query);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equals("t")) {
					result = true;
				}
				
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return result;
	}
	
	
	
	
	
	public synchronized boolean userExists(String id) {
		boolean result = false;
		try {
			PreparedStatement st = c.prepareStatement("SELECT EXISTS(SELECT * FROM users WHERE id=?)");
			st.setString(1, id);

			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).equals("t")) {
					result = true;
				}
			}
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
			PreparedStatement st = c.prepareStatement("SELECT number FROM users WHERE id=?");
			st.setString(1, id);

			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if (rs.getString(1).trim().isEmpty()) {
					result = false;
				}
				
			}
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
			PreparedStatement st0 = c.prepareStatement(
					"SELECT usedpromo FROM users WHERE id=?");
			st0.setString(1, id);
			ResultSet rs = st0.executeQuery();
			while(rs.next()) {
				res = rs.getInt(1);
			}
			PreparedStatement st = c.prepareStatement(
					"INSERT INTO users(id, username, firstname, lastname, number, lastchoice, usedpromo) VALUES(?,?,?,?,'',?,?)");
			st.setString(1, id);
			st.setString(2, username);
			st.setString(3, firstName);
			st.setString(4, lastName);
			st.setInt(5, lastchoice);
			st.setInt(6, res);
			return st.executeUpdate() != 0 ? true : false;
		} catch (SQLIntegrityConstraintViolationException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized boolean updateUser(String id, String firstName, String lastName, String contact,
			int lastchoice) {
		try {
			PreparedStatement st = c
					.prepareStatement("UPDATE users SET firstname=?, lastname=?, number=?, lastchoice=? WHERE id=?");

			st.setString(1, firstName);
			st.setString(2, lastName);
			st.setString(3, contact);
			st.setInt(4, lastchoice);
			st.setString(5, id);
			return st.executeUpdate() != 0 ? true : false;
		} catch (SQLIntegrityConstraintViolationException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 0->1 - start state, open catalog
	// 1->2 - catalog, open goods
	// 2->3 - choose good, open details request
	// 3->0 - send details, return to first state
	public synchronized int getUserState(String id) {
		int res = 0;
		try {
			PreparedStatement st = c.prepareStatement("SELECT state FROM users WHERE id=?");
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				res = Integer.parseInt(rs.getString(1));
			}
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}
	}

	public synchronized void setUserState(String id, int state) {
		try {

			PreparedStatement st = c.prepareStatement("UPDATE users SET state=? WHERE id=?");
			st.setInt(1, state);
			st.setString(2, id);
			st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public synchronized void incrementUserState(String id) {
		try {
			PreparedStatement st = c.prepareStatement("SELECT state FROM users WHERE id=?");
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			int state = 0;
			while (rs.next()) {
				state = rs.getInt(1);
			}
			state++;
			if (state == 4) {
				state = 0;
			}
			st = c.prepareStatement("UPDATE users SET state=? WHERE id=?");
			st.setInt(1, state);
			st.setString(2, id);
			st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public synchronized DBUser getUser(String id) {
		DBUser res = null;
		try {
			PreparedStatement st = c.prepareStatement("SELECT * FROM users WHERE id=?");
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				res = new DBUser(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						Integer.parseInt(rs.getString(6)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return res;
		}
		return res;
	}

	public synchronized boolean saveOrder(String userId, int goodId) {
		try {
			PreparedStatement st0 = c.prepareStatement("SELECT MAX(id) FROM orders");
			ResultSet rs = st0.executeQuery();
			int id=-1;
			while(rs.next()) {
				id = rs.getInt(1);
			}
			id++;
			PreparedStatement st = c.prepareStatement("INSERT INTO orders(id,userId, goodId) VALUES(?,?,?)");
			st.setInt(1, id);
			st.setString(2, userId);
			st.setInt(3, goodId);
			st.execute();

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
			PreparedStatement st = c.prepareStatement("SELECT id FROM goods WHERE name=? AND price=?");
			st.setString(1, goodName);
			st.setInt(2, goodPrice);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
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
			PreparedStatement st = c.prepareStatement("UPDATE users SET lastchoice=? WHERE id=?");
			st.setInt(1, productId);
			st.setString(2, userId);
			st.executeUpdate();

		} catch (SQLIntegrityConstraintViolationException e) {

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public synchronized int getLastChoice(String userId) {
		try {
			PreparedStatement st = c.prepareStatement("SELECT lastchoice FROM users WHERE id=?");
			st.setString(1, userId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
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
			PreparedStatement st = c.prepareStatement("SELECT * FROM goods");
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				res.add(new Product(rs.getInt(1), rs.getString(2), rs.getInt(3)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	public synchronized String getAdminChatId() {
		String res = "";
		try {
			PreparedStatement st = c.prepareStatement("SELECT chatid FROM admindata;");
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				res = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	public List<Product> getUserOrders(String id) {
		List<Product> res = new ArrayList<>();
		
		try {
			PreparedStatement st = c.prepareStatement("select goods.id, goods.name, goods.price from orders inner join goods ON orders.goodid = goods.id where orders.userid = ?;");
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				res.add(new Product(rs.getInt(1), rs.getString(2), rs.getInt(3)));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	
	public String generatePromoForUser(String id) {
		String result = null;
		
		try {
			String query = "SELECT EXISTS(SELECT * FROM promo WHERE ownerid=?)";
			PreparedStatement st = c.prepareStatement(query);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equals("t")) {
					return null;
				}
				String uuid = UUID.randomUUID().toString();
				uuid = uuid.replace("-", "");
				uuid = uuid.substring(uuid.length()-8, uuid.length()-1);
				uuid += id;
				query = "INSERT INTO promo(value, ownerid, used) values(?,?,?);";
				st = c.prepareStatement(query);
				st.setString(1, uuid);
				st.setString(2, id);
				st.setInt(3, 0);
				st.executeUpdate();
				result = uuid;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	
	public String getUserPromo(String userid) {
		
		String result = null;
		
		try {
			String query = "SELECT EXISTS(SELECT * FROM promo WHERE ownerid=?)";
			PreparedStatement st = c.prepareStatement(query);
			st.setString(1, userid);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equals("f")) {
					return null;
				}
				query = "SELECT value FROM promo WHERE ownerid=?";
				st = c.prepareStatement(query);
				st.setString(1, userid);
				rs = st.executeQuery();
				while(rs.next()) {
					return rs.getString(1);
				}
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
		
	}
	
	
	public boolean usePromo(String code) {
		try {
			String query = "SELECT EXISTS(SELECT * FROM promo WHERE value=?)";
			PreparedStatement st = c.prepareStatement(query);
			st.setString(1, code);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equals("f")) {
					return false;
				}
				query = "SELECT used FROM promo WHERE value=?";
				st = c.prepareStatement(query);
				st.setString(1, code);
				rs = st.executeQuery();
				while(rs.next()) {
					if(rs.getString(1).equals("t")) {
						return false;
					}
					query = "UPDATE promo SET used=1 WHERE value=? ";
					st = c.prepareStatement(query);
					st.setString(1, code);
					st.executeUpdate();
					return true;
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public String getPromoOwner(String code) {
		try {
			String query = "SELECT EXISTS(SELECT ownerid FROM promo WHERE value=?)";
			PreparedStatement st = c.prepareStatement(query);
			st.setString(1, code);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equals("f")) {
					return null;
				}
				query = "SELECT ownerid FROM promo WHERE value=?";
				st = c.prepareStatement(query);
				st.setString(1, code);
				rs = st.executeQuery();
				while(rs.next()) {
					return rs.getString(1);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

}
