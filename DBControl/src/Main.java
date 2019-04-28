import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.*;

public class Main {
	public static Connection con;
	public static Statement stmt;
	public static ResultSet rs;
	public static void main(String[] args) {
		
			Controller controller = new Controller();
			FormControl app = new FormControl(controller);
			app.update();	
			File file = new File("C:\\soft\\javaDb\\dblist.txt"); 
			  
			  BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
			  String st; 
			  while ((st = br.readLine()) != null) { 
			    String[] line = st.split(" ");
			    String result = controller.addDB(line[0], line[1], Integer.parseInt(line[2]), line[3], line[4]);
			    try {
	    			    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\soft\\javaDb\\log.txt", true));
	    			    writer.newLine();
	    			    if(!result.equals("Success")) {
	    			    	writer.write("Failed to add DB '"+line[0]+"'(remove dead database from auto-add list)");
	    			    }
	    			    else {
	    			    	writer.write("Successfully added DB '"+line[0]+"'");
	    			    }
	    			    writer.close();
	    			    
	    				}catch(Exception ex) {
	    					
	    				}
			    
			  }
			  br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			

		

	}

}
