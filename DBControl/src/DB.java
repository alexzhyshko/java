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
	final static int capacity = 1000;
	int size;
	boolean status = false;

	public DB(String name, String path,int port, String user, String password) {
		try {
			this.path = path;
			this.password = password;
			this.user = user;
			try {
			this.connectionURL="jdbc:mysql://"+path+":3306/"+name+"?user="+user+"&password="+password+"&serverTimezone=UTC";
			Connection conn = DriverManager.getConnection(this.connectionURL); 
			String query = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, name);
			ResultSet rs = preparedStmt.executeQuery();
			}catch(SQLSyntaxErrorException e) {
				
				return;
			}
			this.name = name;
			this.connectionURL  = "jdbc:mysql://"+path+":"+port+"/"+name+"?&serverTimezone=UTC";
			Connection conn = DriverManager.getConnection(this.connectionURL, user, password); 
			String query = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, name);
			ResultSet rs = preparedStmt.executeQuery();
			this.size = updateSize();
			this.status = true;
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean accessCheck() {
			try {
				Connection conn = DriverManager.getConnection(this.connectionURL, user, password); 
				String query = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, name);
				ResultSet rs = preparedStmt.executeQuery();
				
			}catch(Exception e) {
				this.status = false;
				return false;
			}
			
			return true;
			
		
	}
	
	public int updateSize() {
		int result = 0;
		
		try {
			Connection conn = DriverManager.getConnection(this.connectionURL, user,password);
			String query = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, name);
			ResultSet rs = preparedStmt.executeQuery();
			
			while(rs.next()) {
				int temp=0;
				String query2 = "select count(*) from DBnm.TBnm";
				query2 = query2.replaceFirst("DBnm", this.name);
				query2 = query2.replaceFirst("TBnm", rs.getString(2));
				Statement statement = conn.prepareStatement(query2);
				ResultSet rs2 = statement.executeQuery(query2);
				while(rs2.next()) {
				temp=rs2.getInt(1);
				}
				result+=temp;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
	@Override
	public ArrayList<Object[]> getContents() {
		try {
			return this.contents;
		} catch (Throwable e) {
			return null;
		}
	}
	

}
