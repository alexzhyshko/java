import java.util.List;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class AdminPanel extends Application {

	Controller controller;
	Stage window;
	Scene main;
	Scene add;
	Scene retur;
	Scene showReturn;
	Scene addUser;
	Scene changeUser;
	Scene deleteUser;
	Scene users;
	Scene sendMessage;
	Scene sendToAll;
	

	public static void main(String[] args) {

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		controller = new Controller();
		window = primaryStage;
		window.setResizable(false);
		window.setTitle("Admin Panel");

		
		
		
		
		//scene send to all
		
		
		TextField message1 = new TextField();
		VBox allLayout = new VBox();
		allLayout.getChildren().add(message1);
		Button sbmt4 = new Button("Submit");
		Button cncl4 = new Button("Cancel");
		allLayout.getChildren().add(sbmt4);
		allLayout.getChildren().add(cncl4);
		
		
		sbmt4.setOnAction(e->{
			if(controller.sendToAll(message1.getText().trim()))window.setScene(this.main);
		});
		cncl4.setOnAction(e->{
			window.setScene(this.main);
		});
		
		
		this.sendToAll = new Scene(allLayout);
		
		
		
		
		
		
		//scene send message
		
		TextField username4 = new TextField("Username");
		TextField message = new TextField();
		VBox messageLayout = new VBox();
		messageLayout.getChildren().add(username4);
		messageLayout.getChildren().add(message);
		Button sbmt3 = new Button("Submit");
		Button cncl3 = new Button("Cancel");
		messageLayout.getChildren().add(sbmt3);
		messageLayout.getChildren().add(cncl3);
		
		
		sbmt3.setOnAction(e->{
			if(controller.sendMessage(username4.getText().trim(), message.getText().trim()))window.setScene(this.main);
		});
		cncl3.setOnAction(e->{
			window.setScene(this.main);
		});
		
		
		this.sendMessage = new Scene(messageLayout);
		
		
		
		
		//scene delete user 
				TextField username2 = new TextField("Username");
				PasswordField password2 = new PasswordField();
				VBox userLayout2 = new VBox();
				userLayout2.getChildren().add(username2);
				userLayout2.getChildren().add(password2);
				Button sbmt2 = new Button("Submit");
				Button cncl2 = new Button("Cancel");
				userLayout2.getChildren().add(sbmt2);
				userLayout2.getChildren().add(cncl2);
				
				
				sbmt2.setOnAction(e->{
					if(controller.deleteUser(username2.getText().trim(), password2.getText().trim()))window.setScene(this.main);
				});
				cncl2.setOnAction(e->{
					window.setScene(this.main);
				});
				
				
				this.deleteUser = new Scene(userLayout2);
		
		//scene change user 
		TextField username1 = new TextField("Username");
		PasswordField password1 = new PasswordField();
		VBox userLayout1 = new VBox();
		userLayout1.getChildren().add(username1);
		userLayout1.getChildren().add(password1);
		Button sbmt1 = new Button("Submit");
		Button cncl1 = new Button("Cancel");
		userLayout1.getChildren().add(sbmt1);
		userLayout1.getChildren().add(cncl1);
		
		
		sbmt1.setOnAction(e->{
			if(controller.changeUser(username1.getText().trim(), password1.getText().trim()))window.setScene(this.main);
		});
		cncl1.setOnAction(e->{
			window.setScene(this.main);
		});
		
		
		this.changeUser = new Scene(userLayout1);
		
		
		
		
		
		
		
		//scene add user
		
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField();
		VBox userLayout = new VBox();
		userLayout.getChildren().add(username);
		userLayout.getChildren().add(password);
		Button sbmt = new Button("Submit");
		Button cncl = new Button("Cancel");
		userLayout.getChildren().add(sbmt);
		userLayout.getChildren().add(cncl);
		
		
		sbmt.setOnAction(e->{
			if(controller.addUser(username.getText().trim(), password.getText().trim()))window.setScene(this.main);
		});
		cncl.setOnAction(e->{
			window.setScene(this.main);
		});
		
		
		this.addUser = new Scene(userLayout);
		
		
		
		
		
		
		
		
		
		
		
		
		// scene main
		VBox layout = new VBox();

		main = new Scene(layout, 850, 400);

		MenuBar bar = new MenuBar();
		Menu edit = new Menu("Manage");
		MenuItem home = new MenuItem("Home");
		MenuItem users = new MenuItem("Users");
		MenuItem add_book = new MenuItem("Add book");
		MenuItem add_user = new MenuItem("Add user");
		MenuItem delete_user = new MenuItem("Delete user");
		MenuItem change_user = new MenuItem("Change user password");
		MenuItem send = new MenuItem("Send message");
		MenuItem sendToAll = new MenuItem("Send message to all");
		MenuItem ret = new MenuItem("Return book");
		MenuItem req = new MenuItem("Return requests");

		ret.setOnAction(e->window.setScene(retur));
		
		edit.getItems().add(home);
		edit.getItems().add(users);
		edit.getItems().add(add_book);
		edit.getItems().add(add_user);
		edit.getItems().add(delete_user);
		edit.getItems().add(change_user);
		edit.getItems().add(send);
		edit.getItems().add(sendToAll);
		edit.getItems().add(ret);
		edit.getItems().add(req);
		bar.getMenus().add(edit);
		
		add_book.setOnAction(e -> window.setScene(add));
		add_user.setOnAction(e -> window.setScene(addUser));
		change_user.setOnAction(e->window.setScene(this.changeUser));
		delete_user.setOnAction(e->window.setScene(this.deleteUser));
		send.setOnAction(e->window.setScene(this.sendMessage));
		sendToAll.setOnAction(e->window.setScene(this.sendToAll));
		
		layout.getChildren().add(bar);

		
		TableView<MyDataType> listOfBooks = new TableView<>();
		listOfBooks.setLayoutX(10);
		listOfBooks.setLayoutY(45);
		listOfBooks.setMinHeight(400);
		listOfBooks.setMaxHeight(400);
		listOfBooks.setMinWidth(850);
		listOfBooks.setMaxWidth(850);
		
		TableColumn<MyDataType, String> column1 = new TableColumn<>("Book, author");
		TableColumn<MyDataType, String> column2 = new TableColumn<>("Owner");
		TableColumn<MyDataType, String> column3 = new TableColumn<>("Next");
		TableColumn<MyDataType, String> column4 = new TableColumn<>("Taken");
		TableColumn<MyDataType, String> column5 = new TableColumn<>("Expires");
		TableColumn<MyDataType, String> column6 = new TableColumn<>("Status");
		
		
		column1.setCellValueFactory(new PropertyValueFactory<>("NameAuthor"));
		column1.setMinWidth(320);
		column1.setMaxWidth(320);
		
		column2.setCellValueFactory(new PropertyValueFactory<>("Owner"));
		column2.setMinWidth(60);
		column2.setMaxWidth(60);
		
		column3.setCellValueFactory(new PropertyValueFactory<>("Next"));
		column3.setMinWidth(60);
		column3.setMaxWidth(60);
		
		column4.setCellValueFactory(new PropertyValueFactory<>("Taken"));
		column4.setMinWidth(80);
		column4.setMaxWidth(80);
		
		column5.setCellValueFactory(new PropertyValueFactory<>("Expires"));
		column5.setMinWidth(80);
		column5.setMaxWidth(80);
		
		column6.setCellValueFactory(new PropertyValueFactory<>("Status"));
		column6.setMinWidth(250);
		column6.setMaxWidth(250);
		
		listOfBooks.getColumns().add(column1);
		listOfBooks.getColumns().add(column2);
		listOfBooks.getColumns().add(column3);
		listOfBooks.getColumns().add(column4);
		listOfBooks.getColumns().add(column5);
		listOfBooks.getColumns().add(column6);
		
		
		
		layout.getChildren().add(listOfBooks);
		

		
		for (String string : controller.updateTable()) {

			String[] credentials = string.split(",");
			listOfBooks.getItems().add(new MyDataType(credentials[0], credentials[1], credentials[3], credentials[2], credentials[4], credentials[5]));

		}

		
		ret.setOnAction(e-> window.setScene(retur));
		
		
		
		//scene users
		
		MenuBar bar2 = new MenuBar();
		Menu edit2 = new Menu("Manage");
		MenuItem home2 = new MenuItem("Home");
		MenuItem users2 = new MenuItem("Users");
		MenuItem add2_book = new MenuItem("Add book");
		MenuItem add2_user = new MenuItem("Add user");
		MenuItem change2_user = new MenuItem("Change user password");
		MenuItem delete2_user = new MenuItem("Delete user");
		MenuItem send2 = new MenuItem("Send message");
		MenuItem sendToAll2 = new MenuItem("Send message to all");
		MenuItem ret2 = new MenuItem("Return book");
		MenuItem req2 = new MenuItem("Return requests");

		ret2.setOnAction(e->window.setScene(retur));
		
		edit2.getItems().add(home2);
		edit2.getItems().add(users2);
		edit2.getItems().add(add2_book);
		edit2.getItems().add(add2_user);
		edit2.getItems().add(delete2_user);
		edit2.getItems().add(change2_user);
		edit2.getItems().add(send2);
		edit2.getItems().add(sendToAll2);
		edit2.getItems().add(ret2);
		edit2.getItems().add(req2);
		bar2.getMenus().add(edit2);
		
		VBox usersLayout = new VBox();
		
		usersLayout.getChildren().add(bar2);
		
		
		add2_book.setOnAction(e->{
			window.setScene(this.add);
		});
		add2_user.setOnAction(e->{
			window.setScene(this.addUser);
		});
		change2_user.setOnAction(e->{
			window.setScene(this.changeUser);
		});
		delete2_user.setOnAction(e->{
			window.setScene(this.deleteUser);
		});
		send2.setOnAction(e->window.setScene(this.sendMessage));
		sendToAll2.setOnAction(e->window.setScene(this.sendToAll));
		
		
		
		TableView<UserType> listOfUsers = new TableView<>();
		listOfUsers.setLayoutX(10);
		listOfUsers.setLayoutY(45);
		listOfUsers.setMinHeight(400);
		listOfUsers.setMaxHeight(400);
		listOfUsers.setMinWidth(850);
		listOfUsers.setMaxWidth(850);
		
		TableColumn<UserType, String> username_table = new TableColumn<>("User");
		
		username_table.setCellValueFactory(new PropertyValueFactory<>("Name"));
		username_table.setMinWidth(150);
		username_table.setMaxWidth(80);
		
		
		TableColumn<UserType, String> username_book1 = new TableColumn<>("Book 1");
		
		username_book1.setCellValueFactory(new PropertyValueFactory<>("Book1"));
		username_book1.setMinWidth(350);
		username_book1.setMaxWidth(350);
		
		
		TableColumn<UserType, String> username_book2 = new TableColumn<>("Book 2");
		
		username_book2.setCellValueFactory(new PropertyValueFactory<>("Book2"));
		username_book2.setMinWidth(350);
		username_book2.setMaxWidth(350);
		
		
		
		
		listOfUsers.getColumns().add(username_table);
		listOfUsers.getColumns().add(username_book1);
		listOfUsers.getColumns().add(username_book2);
		
		usersLayout.getChildren().add(listOfUsers);
		
		listOfUsers.getItems().clear();
		for(String user : controller.getUsers()) {
			List<String> userbooks = controller.getBooks(user);
			listOfUsers.getItems().add(new UserType(user, userbooks.get(0), userbooks.get(1)));
		}
		
		this.users = new Scene(usersLayout, 850, 400);
		
		
		//scene show return
		
		VBox returners = new VBox();
		
		
		showReturn = new Scene(returners, 500, 400);
		
		MenuBar bar1 = new MenuBar();
		Menu edit1 = new Menu("Manage");
		MenuItem home1 = new MenuItem("Home");
		MenuItem users1 = new MenuItem("Users");
		MenuItem add1_book = new MenuItem("Add book");
		MenuItem add1_user = new MenuItem("Add user");
		MenuItem change1_user = new MenuItem("Change user password");
		MenuItem delete1_user = new MenuItem("Delete user");
		MenuItem send1 = new MenuItem("Send message");
		MenuItem sendToAll1 = new MenuItem("Send message to all");
		MenuItem ret1 = new MenuItem("Return book");
		MenuItem req1 = new MenuItem("Return requests");

		ret1.setOnAction(e->window.setScene(retur));
		
		edit1.getItems().add(home1);
		edit1.getItems().add(users1);
		edit1.getItems().add(add1_book);
		edit1.getItems().add(add1_user);
		edit1.getItems().add(delete1_user);
		edit1.getItems().add(change1_user);
		edit1.getItems().add(send1);
		edit1.getItems().add(sendToAll1);
		edit1.getItems().add(ret1);
		edit1.getItems().add(req1);
		bar1.getMenus().add(edit1);
		
		
		add1_book.setOnAction(e->{
			window.setScene(this.add);
		});
		add1_user.setOnAction(e->{
			window.setScene(this.addUser);
		});
		change1_user.setOnAction(e->{
			window.setScene(this.changeUser);
		});
		delete1_user.setOnAction(e->{
			window.setScene(this.deleteUser);
		});
		send1.setOnAction(e->window.setScene(this.sendMessage));
		sendToAll1.setOnAction(e->window.setScene(this.sendToAll));
		
		returners.getChildren().add(bar1);
		Text info = new Text("");
		returners.getChildren().add(info);
		for (String string : controller.getReturners()) {

			Button returner = new Button(string);
			returner.setOnAction(e->{
				String book = returner.getText().trim().split(",")[0];
				String user = returner.getText().trim().split(",")[1];
				controller.returnBook(user, book);
			});
			returners.getChildren().add(returner);
			
			

		}
		
		
		
		
		
		
		
		
		
		// scene add
		VBox addLayout = new VBox();

		

		Label name = new Label("Book name");
		Label author = new Label("Book author");
		TextField nm = new TextField();
		TextField au = new TextField();

		Button submit = new Button("Add");
		submit.setOnAction(e -> {
			
			if (controller.addBook(nm.getText().trim(), au.getText().trim())) {
				window.setScene(main);
			}
			
			
		});

		Button cancel = new Button("Cancel");
		cancel.setOnAction(e -> {
			window.setScene(main);
		});

		addLayout.getChildren().add(name);
		addLayout.getChildren().add(nm);
		addLayout.getChildren().add(author);
		addLayout.getChildren().add(au);
		addLayout.getChildren().add(submit);
		addLayout.getChildren().add(cancel);

		add = new Scene(addLayout, 200, 150);
		
		
		//scene return
		TextField user = new TextField("user");
		TextField book = new TextField("book");
		Button retu = new Button("Return");
		Button cancel1 = new Button("Cancel");
		cancel.setOnAction(e -> {
			window.setScene(main);
		});
		
		VBox retLayout = new VBox();
		
		retLayout.getChildren().add(user);
		retLayout.getChildren().add(book);
		retLayout.getChildren().add(retu);
		retLayout.getChildren().add(cancel1);
		
		cancel1.setOnAction(e->window.setScene(main));
		retu.setOnAction(e->{
			
			if(controller.returnBook(user.getText().trim(), book.getText().trim())) window.setScene(main);

		});
		
		this.retur = new Scene(retLayout);
		
		
		
		
		window.setScene(main);
		window.show();
		
		
		
		req.setOnAction(e->{
			info.setText("");
			returners.getChildren().clear();
			returners.getChildren().add(bar1);
			returners.getChildren().add(info);
			for (String string : controller.getReturners()) {

				Button returner = new Button(string);
				returner.setOnAction(e1->{
					String book1 = returner.getText().trim().split(",")[0];
					String user1 = returner.getText().trim().split(",")[1];
					controller.returnBook(user1, book1);
					info.setText("Returned "+book1+" from "+user1);
				});
				returners.getChildren().add(returner);
				
				

			}
			window.setScene(showReturn);	
		});
		req1.setOnAction(e->{
			info.setText("");
			returners.getChildren().clear();
			returners.getChildren().add(bar1);
			returners.getChildren().add(info);
			
			for (String string : controller.getReturners()) {

				Button returner = new Button(string);
				returner.setOnAction(e2->{
					String book2 = returner.getText().trim().split(",")[0];
					String user2 = returner.getText().trim().split(",")[1];
					
					controller.returnBook(user2, book2);
					
					
					
					info.setText("Returned "+book2+" from "+user2);
				});
				returners.getChildren().add(returner);
				
				

			}
			window.setScene(showReturn);	
		});
		
		home.setOnAction(e->{
			layout.getChildren().clear();
			layout.getChildren().add(bar);
			layout.getChildren().add(listOfBooks);
			
			listOfBooks.getItems().clear();
			for (String string : controller.updateTable()) {

				String[] credentials = string.split(",");
				listOfBooks.getItems().add(new MyDataType(credentials[0], credentials[1], credentials[3], credentials[2], credentials[4], credentials[5]));

			}
			window.setScene(this.main);
		});
		
		home1.setOnAction(e->{
			
			layout.getChildren().clear();
			layout.getChildren().add(bar);
			layout.getChildren().add(listOfBooks);
			listOfBooks.getItems().clear();
			for (String string : controller.updateTable()) {

				String[] credentials = string.split(",");
				listOfBooks.getItems().add(new MyDataType(credentials[0], credentials[1], credentials[3], credentials[2], credentials[4], credentials[5]));

			}
			window.setScene(this.main);
			
		});
		home2.setOnAction(e->{
			
			layout.getChildren().clear();
			layout.getChildren().add(bar);
			layout.getChildren().add(listOfBooks);
			listOfBooks.getItems().clear();
			for (String string : controller.updateTable()) {

				String[] credentials = string.split(",");
				listOfBooks.getItems().add(new MyDataType(credentials[0], credentials[1], credentials[3], credentials[2], credentials[4], credentials[5]));

			}
			window.setScene(this.main);
			
		});
		
		users.setOnAction(e->{
			usersLayout.getChildren().clear();
			usersLayout.getChildren().add(bar2);
			usersLayout.getChildren().add(listOfUsers);
			
			listOfUsers.getItems().clear();
			for(String user0 : controller.getUsers()) {
				List<String> userbooks = controller.getBooks(user0);
				listOfUsers.getItems().add(new UserType(user0, userbooks.get(0), userbooks.get(1)));
			}
			window.setScene(this.users);
		});
		
		users1.setOnAction(e->{
			usersLayout.getChildren().clear();
			usersLayout.getChildren().add(bar2);
			usersLayout.getChildren().add(listOfUsers);
			
			listOfUsers.getItems().clear();
			for(String user1 : controller.getUsers()) {
				List<String> userbooks = controller.getBooks(user1);
				listOfUsers.getItems().add(new UserType(user1, userbooks.get(0), userbooks.get(1)));
			}
			window.setScene(this.users);
		});
		
		users2.setOnAction(e->{
			usersLayout.getChildren().clear();
			usersLayout.getChildren().add(bar2);
			usersLayout.getChildren().add(listOfUsers);
			
			listOfUsers.getItems().clear();
			for(String user2 : controller.getUsers()) {
				List<String> userbooks = controller.getBooks(user2);
				listOfUsers.getItems().add(new UserType(user2, userbooks.get(0), userbooks.get(1)));
			}
			window.setScene(this.users);
		});

	}


	

}
