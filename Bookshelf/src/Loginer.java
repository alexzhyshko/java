import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class Loginer {
	private String DBname;
	private String DBtable;
	private String DBuser;
	private String DBpassword;
	private String DBpath;
	private int DBport;
	private String connectionURL;
	private String filename = "C:\\Library\\logdata.txt";	//
	
	
	//
	private Connection connection;
	
	public Loginer() {
		
		try(Scanner sc = new Scanner(new FileReader(filename));) {
			
			List<String> logdata = new ArrayList<>();
			while(sc.hasNextLine()) {
				logdata.add(sc.nextLine());
			}

			//System.out.println(logdata.get(6).trim());
			//System.out.println(logdata.get(7).trim());
			//System.out.println(logdata.get(8).trim());
			//System.out.println(logdata.get(9).trim());
			//System.out.println(logdata.get(10).trim());
			//System.out.println(logdata.get(11).trim());
			
			if(new File(filename).exists()) {
				this.DBname = logdata.get(6).trim();
				this.DBtable = logdata.get(7).trim();
				this.DBuser = logdata.get(8).trim();
				this.DBpassword = logdata.get(9).trim();
				this.DBpath = logdata.get(10).trim();
				this.DBport = Integer.parseInt(logdata.get(11).trim());
			}
			this.connectionURL = "jdbc:mysql://" + DBpath + ":" + DBport + "/" + DBname + "?user=" + DBuser + "&password="
					+ DBpassword + "&serverTimezone=UTC";
			try {
				this.connection = DriverManager.getConnection(connectionURL);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public boolean checkStatus() {
		try (Connection temp = DriverManager.getConnection(this.connectionURL);) {

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	
	public boolean login(String user, String password) {
		
		String query = "SELECT password FROM "+DBtable+" WHERE username=?;";
		try(PreparedStatement ps = connection.prepareStatement(query);){
			ps.setString(1, user.trim().toLowerCase());
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					if(rs.getString(1).trim().equals(password.trim())) {
						String query2 = "UPDATE "+DBtable+" SET logged_in='true' WHERE username=?;";
						PreparedStatement st2 = connection.prepareStatement(query2);
						st2.setString(1, user.trim());
						st2.executeUpdate();
						return true;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	
		
		return false;
	}
	
	
	public boolean addUser(String username, String password) {
		String query = "INSERT INTO "+DBtable+"(username, password, admin_message, logged_in) VALUES(?,?,'', 'false');";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, username.trim());
			st.setString(2, password.trim());
			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean changeUser(String username, String password) {
		String query = "UPDATE " + DBtable + " SET password = ? WHERE username = ?;";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(2, username.trim());
			st.setString(1, password.trim());
			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteUser(String username, String password) {
		String query = "DELETE FROM " + DBtable + " WHERE username = ?;";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, username.trim());
			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public List<String> getUsers(){
		List<String> result = new ArrayList<>();
		String query = "SELECT username FROM "+DBtable+";";
		try(Statement st = connection.prepareStatement(query);){
			try(ResultSet rs = st.executeQuery(query);){
				while(rs.next()) {
					result.add(rs.getString(1));
				}
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	
	public String hasMessage(String user) {
		String query = "SELECT admin_message FROM "+DBtable+" WHERE username=?;";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, user.trim());
			try(ResultSet rs = st.executeQuery()){
				while(rs.next()) {
					if(!rs.getString(1).trim().isEmpty()) {
						return rs.getString(1).trim();
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	
	public boolean sendMessage(String user, String message) {
		String existingMessage="";
		String query = "SELECT admin_message FROM "+DBtable+" WHERE username=?;";
		String query1 = "UPDATE "+DBtable+" SET admin_message=? WHERE username=?;";
		try {
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1, user.trim());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				if(!rs.getString(1).trim().isEmpty()) {
					existingMessage = rs.getString(1);
				}
			}
			String messageToSend = existingMessage.trim().isEmpty()?message:existingMessage+"\n"+message;
			PreparedStatement st1 = connection.prepareStatement(query1);
			st1.setString(1, messageToSend);
			st1.setString(2, user.trim());
			st1.executeUpdate();
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public boolean sendToAll(String message) {
		String query = "SELECT username FROM "+DBtable+";";
		
		try {
			Statement st = connection.prepareStatement(query);
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				if(!sendMessage(rs.getString(1).trim(), message.trim()))return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	public boolean messageRead(String user) {
		String query = "UPDATE "+DBtable+" SET admin_message='' WHERE username=?;";
		try {
			PreparedStatement st = connection.prepareStatement(query);
			st.setString(1, user.trim());
			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
}
