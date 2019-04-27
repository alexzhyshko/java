import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller {
	private ArrayList<Model> models = new ArrayList<>();
	private ArrayList<Model> DBpool = new ArrayList<>();
	public String addDB(String name, String path,int port, String user, String password) {
		try {
			String connectionURL="jdbc:mysql://"+path+"/"+name+"?user="+user+"&password="+password+"&serverTimezone=UTC";
			Connection conn = DriverManager.getConnection(connectionURL); 
			String query = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, name);
			ResultSet rs = preparedStmt.executeQuery();
			}catch(Exception e) {
				
				return "DB doen't exist";
			}
		for(Model model : DBpool) {
			if(model.getPath().equals(path)&&model.DBname.equals(name)&&model.getUser().equals(user)) {
				return "DB already added";
			}
		}
		Model newModel = new Model(name, path, port, user, password);
		models.add(newModel);
		DBpool.add(newModel);
		return "Success";
	}


	private void updateStatus() {
		if(models.size()!=0) {
		ExecutorService service = Executors.newFixedThreadPool(models.size());
		for (Model entry : models) {
			Model task = entry;
			service.submit(task);
		}
		service.shutdown();
		try {
			service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			System.out.println("InterruptedException in line:21 in Controller.java");
		}
		}
	}

	public ArrayList<Model> getAll() {
		updateStatus();
		return models;
	}
}
