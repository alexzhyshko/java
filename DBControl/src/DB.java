import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB implements DBInterface {
	String name;
	private String path;
	private String user;
	private String password;
	String connectionURL;
	int capacity = 1000;
	int size;
	boolean status = false;
	Connection connection;

	public DB(String name, String path, int port, String user, String password) {
		this.connectionURL = "jdbc:mysql://" + path + ":3306/" + name + "?user=" + user + "&password=" + password
				+ "&serverTimezone=UTC";
		try {
			this.connection = DriverManager.getConnection(this.connectionURL);
			this.path = path;
			this.password = password;
			this.user = user;
			
			String query = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
			try (PreparedStatement preparedStmt = connection.prepareStatement(query);) {
				preparedStmt.setString(1, name);
				try(ResultSet rs = preparedStmt.executeQuery();){
					
				}catch(Exception e) {
					e.printStackTrace();
					return;
				}
				
			} catch (SQLSyntaxErrorException e) {
				e.printStackTrace();
				return;
			}
			this.name = name;
			this.size = updateSize();
			this.status = true;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean accessCheck() {
		String query = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
		try (PreparedStatement preparedStmt = connection.prepareStatement(query);){
			preparedStmt.setString(1, name);
			try(ResultSet rs = preparedStmt.executeQuery();){
				
			}catch(Exception e) {
				e.printStackTrace();
				this.status = false;
				this.capacity = 0;
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.status = false;
			this.capacity = 0;
			return false;
		}
		this.capacity = 1000;
		return true;

	}

	public int updateSize() {
		int result = 0;

		try (PreparedStatement preparedStmt = connection.prepareStatement(
				"select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;");) {
			preparedStmt.setString(1, name);
			try (ResultSet rs = preparedStmt.executeQuery();) {

				while (rs.next()) {
					int temp = 0;
					String query2 = "select count(*) from DBnm.TBnm";
					query2 = query2.replaceFirst("DBnm", this.name);
					query2 = query2.replaceFirst("TBnm", rs.getString(2));
					try (Statement statement = connection.prepareStatement(query2);
							ResultSet rs2 = statement.executeQuery(query2);) {

						while (rs2.next()) {
							temp = rs2.getInt(1);
						}
						result += temp;
					} catch (Exception ex) {

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public ArrayList<Object[]> getContents() {
		try {
			return this.contents;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
