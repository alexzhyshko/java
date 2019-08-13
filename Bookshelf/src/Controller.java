import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JOptionPane;

public class Controller {
	private Model model;
	private Loginer loginer;


	public Controller() {
		this.model = new Model("", "", "", "", 0);
		this.loginer = new Loginer();
	}
	

	public List<String> getUsers(){
		return loginer.getUsers();
	}
	
	public void cancelBooking(String user, String book) {
		if(user==null||book==null) return;
		if(user.trim().isEmpty()||book.trim().isEmpty()) return;
		model.cancelBooking(user, book);
	}
	
	public boolean updateDetails(String path, String DBname, String DBtable, String logtable, String DBuser, String DBpassword, String DBpath, String DBport) {
		String pathToFile=path;
		
	
		if(pathToFile.trim().isEmpty()) return false;
		if(DBname==null||DBtable==null||DBuser==null||DBpassword==null||DBpath==null||DBport==null) return false;
		if(!DBname.trim().isEmpty()) this.model.setName(DBname.trim());
		if(!DBtable.trim().isEmpty()) this.model.setTable(DBtable.trim());
		if(!DBuser.trim().isEmpty()) this.model.setUser(DBuser.trim());
		if(!DBpassword.trim().isEmpty()) this.model.setPassword(DBpassword.trim());
		if(!DBpath.trim().isEmpty()) this.model.setPath(DBpath.trim());
		if(!DBport.trim().isEmpty()) this.model.setPort(DBport.trim());
		if(!pathToFile.trim().isEmpty()) this.model.setFilename(pathToFile);
		
		
		model.updateDetails(pathToFile, DBname, DBtable, logtable, DBuser, DBpassword, DBpath, DBport);
		
		return !pathToFile.trim().isEmpty()&&!DBname.trim().isEmpty()&&!DBtable.trim().isEmpty()&&!DBuser.trim().isEmpty()&&!DBpassword.trim().isEmpty()&&!DBpath.trim().isEmpty()&&!DBport.trim().isEmpty();
		
	}
	
	public String getName() {
		return model.getName();
	}
	public String getTable() {
		return model.getTable();
	}
	public String getUser() {
		return model.getUser();
	}
	public String getPath() {
		return model.getPath();
	}
	public String getPort() {
		return Integer.toString(model.getPort());
	}
	
	
	public boolean sendToAll(String message) {
		if(message==null)return false;
		if(message.trim().isEmpty())return false;
		return loginer.sendToAll(message.trim());
	}
	
	
	
	public boolean checkStatus() {
		return loginer.checkStatus();
	}
	public boolean checkBookStatus() {
		return model.checkStatus();
	}
	
	public boolean messageRead(String user) {
		if(user==null) return false;
		if(user.trim().isEmpty()) return false;
		return loginer.messageRead(user.trim());
	} 
	
	
	public boolean sendMessage(String user, String message) {
		if(user==null||message==null) return false;
		if(user.trim().isEmpty()||message.trim().isEmpty()) return false;
		return loginer.sendMessage(user.trim(), message.trim());
	}
	
	public String getMessage(String user) {
		if(user==null) return null;
		if(user.trim().isEmpty()) return null;
		return loginer.hasMessage(user.trim());
	}
	
	public boolean deleteUser(String username, String password) {
		if(username==null||password==null) return false;
		if(username.trim().isEmpty()||password.trim().isEmpty()) return false;
		return loginer.deleteUser(username, password);
	}
	
	public boolean changeUser(String username, String password) {
		if(username==null||password==null) return false;
		if(username.trim().isEmpty()||password.trim().isEmpty()) return false;
		return loginer.changeUser(username, password);
	}
	
	public boolean addUser(String username, String password) {
		if(username==null||password==null) return false;
		if(username.trim().isEmpty()||password.trim().isEmpty()) return false;
		return loginer.addUser(username, password);
	}
	
	
	public void sendReturn(String user, String book) {
		if(user==null||book==null) return;
		if(user.trim().isEmpty()||book.trim().isEmpty()) return;
		model.sendReturn(user.trim(), book.trim());
	}
	
	public boolean returnBook(String user, String book) {
		if(user==null||book==null) return false;
		if(user.trim().isEmpty()||book.trim().isEmpty()) return false;
		boolean result = model.returnBook(user.trim(), book.trim());
		if(result)sendMessage(user, "Your book "+book+" has been returned");
		else sendMessage(user, "Your book "+book+" hasn't been returned");
		return result;
		
	}
	
	
	public List<String> getAllBooks(){
		return model.getAllBooks();
	}
	 
	
	public List<String> getReturners() {
		return model.getReturners();
	}
	
	public String getReturn(String book) {
		if(book==null) return null;
		if(book.trim().isEmpty()) return null;
		return model.getReturn(book);
	}
	
	public List<String> getAllData(String book){
		if(book==null) return null;
		if(book.trim().isEmpty()) return null;
		return model.getAllData(book);
	}
	
	public boolean login(String user, String password) {
		if(user==null||password==null) return false;
		if(user.trim().isEmpty()||password.trim().isEmpty()) return false;
		return loginer.login(user.trim(), password.trim());
	}
	

	
	public List<String> getBooks(String user) {
		if(user==null) return null;
		if(user.trim().isEmpty()) return null; 
		return model.getBooks(user);
		
	}
	
	public List<String> getData(String book){
		if(book==null) return null;
		if(book.trim().isEmpty()) return null;
		return model.getData(book);
	}
	
	public boolean addBook(String bookName, String bookAuthor) {
		if (bookName == null || bookAuthor == null) {
			System.out.println("Controller> AddBook()> Null parameters");
			return false;
		}
		if (!bookName.trim().isEmpty() && !bookAuthor.trim().isEmpty()) {
			if (model.checkStatus()) {
				
				boolean result = model.addBook(bookName, bookAuthor);
				if(result)sendToAll("New book has been added: "+bookName+" by "+bookAuthor+". Check it out!");
				return result;
			} else
				return false;
		} else {
			System.out.println("Controller> AddBook()> Empty parameters");
			return false;
		}

	}

	public List<Integer> getBooks() {
		if (model.checkStatus()) {
			return model.getBooks();
		} else {
			return null;
		}
	}

	public List<String> updateTable() {
		if (model.checkStatus()) {
			return model.updateTable();
		}
		return null;
	}
	
	
	
	
	public String reserveBook(String personName, String bookName) {
		
		if(personName==null||bookName==null) {
			return "Null parameters";
		}
		if(personName.trim().isEmpty()||bookName.trim().isEmpty()) {
			return "Empty parameters";
		}
		if(!model.checkStatus()) {
			return "Server unreachable";
		}
			return model.reserveBook(personName, bookName);
		
	}
	public String getStatus(String bookname) {
		if(model.checkStatus()&&!bookname.trim().isEmpty()) {
			return model.getStatus(bookname);
		}else {
			return null;
		}
	}
}
