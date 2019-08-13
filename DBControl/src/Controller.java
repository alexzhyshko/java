import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Controller {
	ArrayList<Model> models = new ArrayList<>();
	ArrayList<Model> DBpool = new ArrayList<>();

	public String addDB(String name, String path, int port, String user, String password) {
		String connectionURL = "jdbc:mysql://" + path + "/" + name + "?user=" + user + "&password=" + password
				+ "&serverTimezone=UTC";
		try (Connection conn = DriverManager.getConnection(connectionURL);
			PreparedStatement preparedStmt = createPreparedStatement(conn,name);
			ResultSet rs = preparedStmt.executeQuery();){
			
		} catch (Exception e) {
			e.printStackTrace();
			return "DB doen't exist";
		}
		for (Model model : DBpool) {
			if (model.getPath().equals(path) && model.DBname.equals(name) && model.getUser().equals(user)) {
				return "DB already added";
			}
		}
		Model newModel = new Model(name, path, port, user, password);
		models.add(newModel);
		DBpool.add(newModel);
		return "Success";
	}
	private PreparedStatement createPreparedStatement(Connection con, String name) throws SQLException {
	    String sql = "select table_schema as database_name,table_name from information_schema.tables where table_type = 'BASE TABLE' and table_schema = ? order by database_name, table_name;";
	    PreparedStatement ps = con.prepareStatement(sql);
	    ps.setString(1, name);
	    return ps;
	}
	private void updateStatus() {
		if (models.size() != 0) {
			ExecutorService service = Executors.newFixedThreadPool(models.size());
			for (Model entry : models) {
				Model task = entry;
				service.submit(task);
			}
			service.shutdown();
			try {
				
				service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("InterruptedException in line:21 in Controller.java");
			}
		}
	}

	public ArrayList<Model> getAll() {
		updateStatus();
		return models;
	}

	public ArrayList<Model> getModels() {
		return models;
	}

	public int close() {
		if(models.size()!=0) {
			for(Model model : models) {
				model.fixedDB.closeConnection();
			}
		}
		return 1;
	}
}
