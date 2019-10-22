package model;

public class DBUser {

	private String id;
	private String name;
	private String surname;
	private String number;
	private int state;
	private String username;
	
	public DBUser(String id, String username, String name, String surname, String number, int state) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.number = number;
		this.state = state;
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getNumber() {
		return number;
	}

	public String getUsername() {
		return username;
	}

	
	
	
	
}
