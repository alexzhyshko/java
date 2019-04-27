import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

public class Main {
	public static Connection con;
	public static Statement stmt;
	public static ResultSet rs;
	public static void main(String[] args) {
		
			Controller controller = new Controller();
			File file = new File("C:\\soft\\javaDb\\dblist.txt"); 
			  
			  BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
			  String st; 
			  while ((st = br.readLine()) != null) { 
			    String[] line = st.split(" ");
			    System.out.println(line);
			    controller.addDB(line[0], line[1], Integer.parseInt(line[2]), line[3], line[4]);
			  }
			  br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//controller.addDB("mydb", "localhost", 3306, "root", "root");
			//controller.addDB("newdb", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test1", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test2", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test3", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test4", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test5", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test6", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test7", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test8", "192.168.88.139", 3306, "alex", "1234");
			//controller.addDB("test9", "192.168.88.139", 3306, "alex", "1234");
			
			FormControl app = new FormControl(controller);
			app.update();	

		

	}

}
