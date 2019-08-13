
public class Model implements Runnable, ModelInterface {
	DB fixedDB;
	Boolean actualDBstatus = false;
	String DBname;
	String DBpath;
	private String DBuser;
	private String DBpassword;
	int DBport;
	String deadDate = "";
	int DBsize;
	int DBcapacity;

	public Model(String name, String path, int port, String user, String password) {
		this.fixedDB = new DB(name, path, port, user, password);
		this.DBpassword = password;
		this.DBpath = path;
		this.DBuser = user;
		this.DBport = port;
		this.actualDBstatus = checkStatus();
		DBname = name;
	}

	public String getPath() {
		return this.DBpath;
	}

	public String getUser() {
		return this.DBuser;
	}

	public int getPort() {
		return this.DBport;
	}

	@Override
	public void run() {

		actualDBstatus = fixedDB.accessCheck();
		if (actualDBstatus) {
			DBname = fixedDB.name;
			DBsize = fixedDB.updateSize();
			DBcapacity = fixedDB.capacity;
		} else {
			DBname = fixedDB.name;
			DBcapacity = fixedDB.capacity;
			actualDBstatus = fixedDB.accessCheck();
		}

	}

	@Override
	public boolean checkStatus() {
		try {
			return fixedDB.accessCheck();
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

}
