import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author OLEKSANDR ZHYSHKO
 *
 */

public class Model {
	private String DBname;
	private String tableName = "bookshelf"; 
	private String DBuser;
	private String DBpassword;
	private String DBpath;
	private int DBport;
	private String filename = "C:\\Library\\logdata.txt";	//
	private String connectionURL;
	//
	Connection connection;

	
	public Model(String name, String user, String password, String path, int port) {
		

		//
		
		//
		
	
		
		try(Scanner sc = new Scanner(new FileReader(filename));) {
		
		List<String> logdata = new ArrayList<>();
		while(sc.hasNextLine()) {
			logdata.add(sc.nextLine());
		}

		
		if(new File(filename).exists()) {
			this.DBname = logdata.get(0).trim();
			this.tableName = logdata.get(1).trim();
			this.DBuser = logdata.get(2).trim();
			this.DBpassword = logdata.get(3).trim();
			this.DBpath = logdata.get(4).trim();
			this.DBport = Integer.parseInt(logdata.get(5).trim());
		}else {
			this.DBname = name;
			this.DBuser = user;
			this.DBpassword = password;
			this.DBpath = path;
			this.DBport = port;
		}
		
		this.connectionURL = "jdbc:mysql://" + DBpath + ":" + DBport + "/" + DBname + "?user=" + DBuser + "&password="
				+ DBpassword + "&serverTimezone=UTC";
		
		//System.out.println(
				// ? "Model> Constructor> Connection successful" : "Model> Connection unsuccessful");
		
		setConnection();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		

	}

	// getters and setters
	
	public void setFilename(String text) {
		this.filename = text;
	}
	public void setName(String name) {
		this.DBname = name;
	}
	public void setTable(String table) {
		this.tableName = table;
	}
	public void setUser(String user) {
		this.DBuser = user;
	}
	public void setPassword(String password) {
		this.DBpassword = password;
	}
	public void setPath(String path) {
		this.DBpath = path;
	}
	public void setPort(String port) {
		this.DBport = Integer.parseInt(port);
	}
	
	
	public String getName() {
		return this.DBname;
	}

	public String getUser() {
		return this.DBuser;
	}

	public String getPath() {
		return this.DBpath;
	}

	public int getPort() {
		return this.DBport;
	}

	public String getTable() {
		return this.tableName;
	}

	// set up connection
	public boolean setConnection() {
		try {
			this.connection = DriverManager.getConnection(this.connectionURL);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// check connection
	public boolean checkStatus() {
		try (Connection temp = DriverManager.getConnection(this.connectionURL);) {

		} catch (SQLException e) {
			e.printStackTrace();
			//System.out.println("Model> CheckStatus()> DB dead");
			return false;
		}
		return true;
	}

	
	public List<String> getAllBooks(){
		List<String> result = new ArrayList<>();
		String query = "SELECT book_name FROM "+tableName;
		try(Statement st = connection.prepareStatement(query);){
			try(ResultSet rs = st.executeQuery(query);){
				while(rs.next()) {
					result.add(rs.getString(1));
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	// add new book
	public boolean addBook(String bookName, String bookAuthor) {
		String query = "INSERT INTO "
				+ "bookshelf(book_name, book_author, book_first_owner, book_second_owner, book_taken, book_expires, book_status, book_request) "
				+ "VALUES(?,?,' ',' ',' ',' ',' ','');";

		if (alreadyAdded(bookName, bookAuthor)) {
			return false;
		}

		try (PreparedStatement ps = connection.prepareStatement(query);) {
			ps.setString(1, bookName);
			ps.setString(2, bookAuthor);
			try {
				int rs = ps.executeUpdate();

			} catch (Exception e) {
				//System.out
						//.println("Model> AddBook()> Couldn't add a new book: " + bookName + ", author: " + bookAuthor);
				e.printStackTrace();
				return false;

			}

		} catch (Exception e) {
			//System.out.println("Model> AddBook()> Couldn't add a new book: " + bookName + ", author: " + bookAuthor);
			e.printStackTrace();
			return false;
		}
		//System.out.println("Model> AddBook()> Added a new book: " + bookName + ", author: " + bookAuthor);
		return true;

	}

	private boolean alreadyAdded(String bookName, String author) {
		String query = "SELECT * FROM " + tableName;
		try (Statement st = connection.prepareStatement(query);) {
			try (ResultSet rs = st.executeQuery(query);) {
				while (rs.next()) {
					if (rs.getString(2).trim().toLowerCase().equals(bookName.trim().toLowerCase())
							&& rs.getString(3).trim().toLowerCase().equals(author.trim().toLowerCase())) {
						return true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// returns ID's of all books in DB for logic
	public List<Integer> getBooks() {
		List<Integer> result = new ArrayList<>();

		String query = "SELECT * FROM " + tableName + ";";
		try (Statement st = connection.prepareStatement(query);) {
			try (ResultSet rs = st.executeQuery(query);) {
				while (rs.next()) {
					result.add(rs.getInt(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
				//System.out.println("Model> GetBooks()> Couldn't get books");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Model> GetBooks()> Couldn't get books");
			return null;
		}

		return result;

	}

	// returns list of names and authors of books for UI
	public List<String> updateTable() {
		List<Integer> ids = new ArrayList<>();
		ids = getBooks();
		List<String> result = new ArrayList<>();
		for (int id : ids) {
			String query = "SELECT * FROM " + tableName + " WHERE id=?;";
			try {
				PreparedStatement st = connection.prepareStatement(query);
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					result.add(rs.getString(2) + " by " + rs.getString(3)
							+","+(rs.getString(4).trim().isEmpty() ? "" :rs.getString(4))
							+","+(rs.getString(5).trim().isEmpty() ? "" :rs.getString(5))
							+","+(rs.getString(4).trim().isEmpty() ? "" :LocalDate.parse(rs.getString(6)))
							+","+(rs.getString(4).trim().isEmpty() ? "" :LocalDate.parse(rs.getString(7)))
							+ ", Status: " + getStatus(rs.getString(2)));
				}
			} catch (Exception e) {
				e.printStackTrace();
				//System.out.println("Model> UpdateTable()> Couldn't process request");
				return null;
			}

		}
		return result;
	}

	// to reserve a book(for client part)
	public String reserveBook(String name, String bookName) {
		String query = "SELECT (book_first_owner) FROM " + tableName + " WHERE book_name=?;";
		String query2 = "SELECT (book_second_owner) FROM " + tableName + " WHERE book_name=?;";
		try (PreparedStatement st = connection.prepareStatement(query);) {
			st.setString(1, bookName);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					if(rs.getString(1).trim().equals(name.trim())) {
						return "You already have this book";
					}
					if (rs.getString(1).trim().isEmpty()) {
						addFirstPerson(name, bookName);
						//System.out.println("Model> ReserveBook()> Successfully added a first owner to the book");
						return "Successfully added a first owner to the book";
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Model> ReserveBook()> Couldn't operate result set");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Model> ReserveBook()> Error preparing statement");
			return null;

		}

		try (PreparedStatement st = connection.prepareStatement(query2);) {
			st.setString(1, bookName);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					if(rs.getString(1).trim().equals(name.trim())) {
						return "You have already booked this book";
					}
					if (rs.getString(1).trim().isEmpty()) {
						addSecondPerson(name, bookName);
						//System.out.println("Model> ReserveBook()> Successfully added a second owner to the book");
						return "Successfully added a second owner to the book";
					} else {
						return "All slots reserved, wait";
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				//System.out.println("Model> ReserveBook()> Couldn't operate result set");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Model> ReserveBook()> Error preparing statement");
			return null;

		}

		return "";

	}

	private void addFirstPerson(String person, String book) {
		String query = "UPDATE " + tableName + " SET book_first_owner = ? WHERE book_name = ?;";
		String query2 = "UPDATE " + tableName + " SET book_taken = ? WHERE book_name = ?;";
		String query3 = "UPDATE " + tableName + " SET book_expires = ? WHERE book_name = ?;";
		try (PreparedStatement st = connection.prepareStatement(query);) {
			st.setString(1, person);
			st.setString(2, book);
			st.executeUpdate();
			PreparedStatement st1 = connection.prepareStatement(query2);
			st1.setString(1, LocalDate.now().toString());
			st1.setString(2, book);
			st1.executeUpdate();
			PreparedStatement st2 = connection.prepareStatement(query3);
			st2.setString(1, LocalDate.now().plus(14, ChronoUnit.DAYS).toString());
			st2.setString(2, book);
			st2.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Model> (private)AddFirstPerson()> Couldn't create statement");
			return;
		}

	}

	private void addSecondPerson(String person, String book) {
		String query = "UPDATE " + tableName + " SET book_second_owner = ? WHERE book_name = ?;";
		try (PreparedStatement st = connection.prepareStatement(query);) {
			st.setString(1, person);
			st.setString(2, book);
			st.executeUpdate();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("Model> (private)AddFirstPerson()> Couldn't create statement");
			return;
		}
	}

	public String getStatus(String bookName) {
		String query = "SELECT * FROM " + tableName + " WHERE book_name=?";
		String status = "";
		try (PreparedStatement st = connection.prepareStatement(query)) {
			st.setString(1, bookName);
			try (ResultSet rs = st.executeQuery()) {
				while (rs.next()) {
					if (rs.getString(4).trim().isEmpty()) {

						status = "Book is free";
					} else if (!rs.getString(4).trim().isEmpty() && rs.getString(5).trim().isEmpty()) {
						status = "Vacant slot to book in advance";
					} else if (!rs.getString(4).trim().isEmpty() && !rs.getString(5).trim().isEmpty()) {
						status = "Book is reserved";
					} else if (LocalDate.parse(rs.getString(7)).isAfter(LocalDate.now())) {
						status = "Book return time expired";
					}

				}
				return status;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getBooks(String user) {
		List<String> result = new ArrayList<>();
		String query = "SELECT * FROM " + tableName + ";";
		try (Statement st = connection.prepareStatement(query);) {
			try (ResultSet rs = st.executeQuery(query);) {
				while (rs.next()) {
					if (rs.getString(4).toLowerCase().equals(user.toLowerCase())||rs.getString(5).toLowerCase().equals(user.toLowerCase())) {
						result.add(rs.getString(4).toLowerCase().equals(user.toLowerCase())?rs.getString(2):rs.getString(2)+"(you are next)");
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.add("No book");
		result.add("No book");
		return result;
	}

	public List<String> getData(String book) {
		List<String> result = new ArrayList<>();
		String query = "SELECT * FROM "+tableName+" WHERE book_name=?";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, book.trim());
			try(ResultSet rs = st.executeQuery();){
				while(rs.next()) {
					result.add(!rs.getString(3).trim().isEmpty()?rs.getString(3).trim():" ");
					result.add(!rs.getString(7).trim().isEmpty()?Duration.between(LocalDate.now().atStartOfDay(), LocalDate.parse(rs.getString(7)).atStartOfDay()).toDays()!=0?
							Duration.between(LocalDate.now().atStartOfDay(), LocalDate.parse(rs.getString(7)).atStartOfDay()).toDays()+" days left":
								"Expired":" ");
					result.add(!rs.getString(6).trim().isEmpty()?rs.getString(6):" ");
					result.add(!rs.getString(7).trim().isEmpty()?rs.getString(7):" ");
					result.add(!rs.getString(5).trim().isEmpty()?rs.getString(5):" ");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		result.add(" ");
		result.add(" ");
		result.add(" ");
		result.add(" ");
		result.add(" ");
		return result;
		
	}

	
	public List<String> getAllData(String book){
		List<String> result = new ArrayList<>();
		String status = "";
		String query = "SELECT * FROM "+tableName+" WHERE book_name=?";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, book.trim());
			try(ResultSet rs = st.executeQuery();){
				while(rs.next()) {
					for(int i=1; i<9; i++) {
						result.add(rs.getString(i));
					}
					if (rs.getString(4).trim().isEmpty()) {

						status = "Book is free";
					} else if (!rs.getString(4).trim().isEmpty() && rs.getString(5).trim().isEmpty()) {
						status = "Vacant slot to book in advance";
					} else if (!rs.getString(4).trim().isEmpty() && !rs.getString(5).trim().isEmpty()) {
						status = "Book is reserved";
					} else if (LocalDate.parse(rs.getString(7)).isAfter(LocalDate.now())) {
						status = "Book return time expired";
					}

				}
				
				result.add(status);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	public boolean returnBook(String user, String book) {
		
		String query1 = "SELECT * FROM "+tableName+" WHERE book_name=?;";
		try(PreparedStatement st = connection.prepareStatement(query1);){
			st.setString(1, book.trim());
			try(ResultSet rs = st.executeQuery();){
				while(rs.next()) {
					String query9 = "UPDATE "+tableName+" SET book_request ='' WHERE book_name = ?;";
					String query3 = "UPDATE " + tableName + " SET book_taken = '' WHERE book_name = ?;";
					String query4 = "UPDATE " + tableName + " SET book_expires = '' WHERE book_name = ?;";
					
					PreparedStatement st9 = connection.prepareStatement(query9);
					PreparedStatement st2 = connection.prepareStatement(query3);
					PreparedStatement st3 = connection.prepareStatement(query4);
					st9.setString(1, book.trim());
					st2.setString(1, book.trim());
					st3.setString(1, book.trim());
					st9.executeUpdate();
					st2.executeUpdate();
					st3.executeUpdate();
					if(rs.getString(5).trim().isEmpty()) {
						
						//System.out.println("User: "+user);
						String query2 = "UPDATE " + tableName + " SET book_first_owner = '' WHERE book_name = ?;";
						PreparedStatement st1 = connection.prepareStatement(query2);
						st1.setString(1, book.trim());
						st1.executeUpdate();
					}else {
						user = rs.getString(5).trim();
						
						String query5 = "UPDATE "+tableName+" SET book_second_owner='' WHERE book_name=?;";
						String query6 = "UPDATE "+tableName+" SET book_first_owner=? WHERE book_name=?;";
						String query7 = "UPDATE " + tableName + " SET book_taken = ? WHERE book_name = ?;";
						String query8 = "UPDATE " + tableName + " SET book_expires = ? WHERE book_name = ?;";
						PreparedStatement st4 = connection.prepareStatement(query5);
						PreparedStatement st5 = connection.prepareStatement(query6);
						st4.setString(1, book.trim());
						st5.setString(1, user.trim());
						st5.setString(2, book.trim());
						st4.executeUpdate();
						st5.executeUpdate();
						PreparedStatement st6 = connection.prepareStatement(query7);
						st6.setString(1, LocalDate.now().toString());
						st6.setString(2, book.trim());
						st6.executeUpdate();
						PreparedStatement st7 = connection.prepareStatement(query8);
						st7.setString(1, LocalDate.now().plus(14, ChronoUnit.DAYS).toString());
						st7.setString(2, book.trim());
						st7.executeUpdate();
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
		
		return true;
	}
	
	
	public void sendReturn(String user, String book) {
		String query0 = "SELECT book_first_owner FROM "+tableName+" WHERE book_name=?;";
		boolean realOwner = true;
		try(PreparedStatement st0 = connection.prepareStatement(query0);){
			st0.setString(1, book.trim());
			ResultSet rs0 = st0.executeQuery();
			while(rs0.next()) {
				if(!rs0.getString(1).trim().equals(user.trim())) {
					realOwner = false;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		if(realOwner) {
		String query = "UPDATE "+tableName+" SET book_request =? WHERE book_name = ?;";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, "Return requested by: "+user.trim());
			st.setString(2, book.trim());
			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
		}
	}
	
	
	public String getReturn(String book) {
		String query="SELECT book_request FROM "+tableName+" WHERE book_name=?;";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, book.trim());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				return rs.getString(1);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	public List<String> getReturners(){
		List<String> result = new ArrayList<>();
		
		String query = "SELECT * FROM "+tableName;
		try {
			Statement st = connection.prepareStatement(query);
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				if(!rs.getString(9).trim().isEmpty()) {
					result.add(rs.getString(2)+", "+rs.getString(4));	
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public boolean updateDetails(String pathToFile, String DBname, String DBtable, String logtable, String DBuser, String DBpassword, String DBpath, String DBport) {
		
		new File("C:/Library").mkdir();
		
		if(!new File(pathToFile).exists()) {
			try {
				new File(pathToFile).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathToFile));){
			
	    writer.write(DBname);
	    writer.write("\n"+DBtable);
	    writer.write("\n"+DBuser);
	    writer.write("\n"+DBpassword);
	    writer.write("\n"+DBpath);
	    writer.write("\n"+DBport);
	    
	    writer.write("\n"+DBname);
	    writer.write("\n"+logtable);
	    writer.write("\n"+DBuser);
	    writer.write("\n"+DBpassword);
	    writer.write("\n"+DBpath);
	    writer.write("\n"+DBport);
	    
	    
	    	
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
		
		
	}
	
	
	
	public void cancelBooking(String user, String book) {
		String query = "UPDATE "+tableName+" SET book_second_owner = '' WHERE book_name = ?;";
		try(PreparedStatement st = connection.prepareStatement(query);){
			st.setString(1, book.trim().contains("(you are next)")?book.trim().substring(0, book.length()-14):book.trim());

			st.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
