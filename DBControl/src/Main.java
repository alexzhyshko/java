import java.sql.*;

public class Main {
	public static Connection con;
	public static Statement stmt;
	public static ResultSet rs;
	public static void main(String[] args) {
		
			Controller controller = new Controller();
			FormControl app = new FormControl(controller);
			app.update();	

		

	}

}
