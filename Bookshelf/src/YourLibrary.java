import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.text.*;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import javafx.animation.*;
import javafx.util.Duration;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;


public class YourLibrary extends Application {

	Controller controller;
	Stage window;
	Scene log;
	Scene home;
	Scene catalog;
	Scene settings;
	String username;
	Scene config;
	int font_size = 20;
	String color1 = "white";
	String color2 = "black";
	double alpha = 0.65;
	private String pathToFile = "C:\\Library\\logdata.txt";
	public static void main(String[] args) {
		
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		

		
		
		
		username = "";
		controller = new Controller();

		window = primaryStage;
		window.setResizable(false);
		window.setTitle("Library!");
		
		window.getIcons().add(new Image(YourLibrary.class.getClassLoader().getResourceAsStream("img/icon.png")));

		// home page
		
		Rectangle notification = new Rectangle();
		notification.setWidth(40);
		notification.setHeight(28);
		notification.setX(600);
		notification.setY(0);
		notification.setFill(Paint.valueOf("#0061ff"));
		
		Text alarm_not = new Text("!");
		alarm_not.setX(615);
		alarm_not.setY(18);
		alarm_not.setFill(Paint.valueOf("white"));
		alarm_not.setFont(Font.font("Consolas", 16));
		
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Admin message");
        alert.setHeaderText("Message");
        
        
        Stage dialog = (Stage)alert.getDialogPane().getScene().getWindow();
        dialog.getIcons().add(new Image(YourLibrary.class.getClassLoader().getResourceAsStream("img/icon.png")));
        
		
		Group notification_group = new Group();
		
		notification_group.getChildren().addAll(notification, alarm_not);
		
		
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(0, 0, 0, 0));
		grid.setVgap(10);
		grid.setHgap(10);
		
		
		
		BackgroundImage myBI= new BackgroundImage(new Image(YourLibrary.class.getClassLoader().getResourceAsStream("img/bg.jpg")),
		       BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		          BackgroundSize.DEFAULT);
		
		
		
		grid.setBackground(new Background(myBI));

		Rectangle rec = new Rectangle();
		rec.setX(0);
		rec.setY(0);
		rec.setWidth(800);
		rec.setHeight(40);
		rec.toBack();
		rec.setFill(Color.web(color1,alpha));

		Rectangle rectangle = new Rectangle();
		rectangle.setX(5);
		rectangle.setY(7);
		rectangle.setWidth(75);
		rectangle.setHeight(26.5);
		rectangle.setFill(Color.web(color1,alpha+0.2));

		Text home = new Text("Home");
		home.setX(26);
		home.setY(25);
		home.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Rectangle rectangle1 = new Rectangle();
		rectangle1.setX(90);
		rectangle1.setY(7);
		rectangle1.setWidth(75);
		rectangle1.setHeight(26.5);
		rectangle1.setFill(Color.web(color1,alpha+0.2));

		Text catalog = new Text("Catalog");
		catalog.setX(102);
		catalog.setY(25);
		catalog.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Rectangle rectangle2 = new Rectangle();
		rectangle2.setX(175);
		rectangle2.setY(7);
		rectangle2.setWidth(75);
		rectangle2.setHeight(26.5);
		rectangle2.setFill(Color.web(color1,alpha+0.2));

		Text settings = new Text("Settings");
		settings.setX(185);
		settings.setY(25);

		settings.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Text dateTime = new Text();
		dateTime.setX(375);
		dateTime.setY(25);
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			dateTime.setText(LocalDateTime.now().format(formatter));
		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
		dateTime.setFont(Font.font("Consolas", FontPosture.ITALIC, 18));
		dateTime.setFill(Color.valueOf(color2));

		Text day = new Text();
		day.setX(340);
		day.setY(25);
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		day.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
		day.setFont(Font.font("Consolas", FontPosture.ITALIC, 18));
		day.setFill(Paint.valueOf(color2));

		Text date1 = new Text();
		date1.setX(445);
		date1.setY(25);
		Calendar cal = Calendar.getInstance();
		date1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
		date1.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		date1.setFill(Paint.valueOf(color2));

		Text dateDay = new Text();
		dateDay.setX(470);
		dateDay.setY(25);
		dateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
		dateDay.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		dateDay.setFill(Paint.valueOf(color2));

		Text year = new Text();
		year.setX(490);
		year.setY(25);
		year.setText(Integer.toString(cal.get(Calendar.YEAR)));
		year.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		year.setFill(Paint.valueOf(color2));

		Text user = new Text();
		user.setX(660);
		user.setY(25);
		user.setText(this.username);
		user.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Button logout = new Button();
		logout.setLayoutX(735);
		logout.setLayoutY(7);
		logout.setText("Exit");
		
		logout.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");

		Group gr = new Group();
		gr.getChildren().add(rec);

		gr.getChildren().add(rectangle);
		gr.getChildren().add(home);

		gr.getChildren().add(rectangle1);
		gr.getChildren().add(catalog);

		gr.getChildren().add(rectangle2);
		gr.getChildren().add(settings);

		gr.getChildren().add(dateTime);
		gr.getChildren().add(day);

		gr.getChildren().add(date1);
		gr.getChildren().add(dateDay);
		gr.getChildren().add(year);

		
		
		gr.getChildren().add(user);
		gr.getChildren().add(logout);
		
		
		
		
		
		
	        
	        
		
		
		

		Group top = new Group();

		Text booksLabel = new Text("Your books: ");
		booksLabel.setFont(Font.font("Tw Cen MT Condensed", font_size));
		booksLabel.setFill(Paint.valueOf("white"));
		Button update = new Button("Refresh");
		update.setLayoutX(0);
		update.setLayoutY(5);
		update.setPrefSize(80, 10);
		
		update.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");

		top.getChildren().addAll(booksLabel, update);
		GridPane.setConstraints(top, 0, 2);

		Group book1 = new Group();

		Rectangle back = new Rectangle();
		back.setX(15);
		back.setY(15);
		back.setWidth(750);
		back.setHeight(120);
		back.setFill(Color.web(color1, alpha));

		Rectangle rec_left_f = new Rectangle();
		rec_left_f.setX(30);
		rec_left_f.setY(70);
		rec_left_f.setWidth(230);
		rec_left_f.setHeight(20);
		rec_left_f.setFill(Color.web(color1,alpha+0.2));

		Rectangle rec_taken_f = new Rectangle();
		rec_taken_f.setX(30);
		rec_taken_f.setY(96);
		rec_taken_f.setWidth(80);
		rec_taken_f.setHeight(20);
		rec_taken_f.setFill(Color.web(color1,alpha+0.2));

		Rectangle rec_exp_f = new Rectangle();
		rec_exp_f.setX(180);
		rec_exp_f.setY(96);
		rec_exp_f.setWidth(80);
		rec_exp_f.setHeight(20);
		rec_exp_f.setFill(Color.web(color1,alpha+0.2));

		Rectangle rec_next_f = new Rectangle();
		rec_next_f.setX(447);
		rec_next_f.setY(75);
		rec_next_f.setWidth(80);
		rec_next_f.setHeight(30);
		rec_next_f.setFill(Color.web(color1,alpha+0.2));

		Text book_f = new Text("No book");
		Text book1left = new Text("");
		Text author_f = new Text("");
		Text taken_f = new Text("");
		Text exp_f = new Text("");
		Text next_f = new Text("");
		Text next = new Text("Next:");
		Text request = new Text("");
		
		request.setX(550);
		request.setY(94);
		request.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		
		book1left.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Button return1 = new Button("Return");
		return1.setLayoutX(290);
		return1.setLayoutY(80);
		return1.setMinHeight(20);
		return1.setMinWidth(100);
		return1.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");
		book_f.setY(55);
		book_f.setX(30);
		book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));

		author_f.setX(400);
		author_f.setY(55);
		author_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));

		taken_f.setY(112);
		taken_f.setX(32);
		taken_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		exp_f.setY(112);
		exp_f.setX(182);
		exp_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		next_f.setX(450);
		next_f.setY(95);
		next_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		book1left.setX(30);
		book1left.setY(85);

		next.setX(408);
		next.setY(95);
		next.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		return1.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		book1.getChildren().addAll(back, rec_left_f, rec_taken_f, rec_exp_f, rec_next_f, book_f, book1left, author_f,
				next, taken_f, exp_f, next_f, return1, request);

		Group book2 = new Group();
		int num = 14;

		Rectangle back1 = new Rectangle();
		back1.setX(15);
		back1.setY(0);
		back1.setWidth(750);
		back1.setHeight(120);
		back1.setFill(Color.web(color1, alpha));

		Rectangle rec_left_s = new Rectangle();
		rec_left_s.setX(30);
		rec_left_s.setY(70 - num);
		rec_left_s.setWidth(230);
		rec_left_s.setHeight(20);
		rec_left_s.setFill(Color.web(color1, alpha+0.2));

		Rectangle rec_taken_s = new Rectangle();
		rec_taken_s.setX(30);
		rec_taken_s.setY(96 - num);
		rec_taken_s.setWidth(80);
		rec_taken_s.setHeight(20);
		rec_taken_s.setFill(Color.web(color1,alpha+0.2));

		Rectangle rec_exp_s = new Rectangle();
		rec_exp_s.setX(180);
		rec_exp_s.setY(96 - num);
		rec_exp_s.setWidth(80);
		rec_exp_s.setHeight(20);
		rec_exp_s.setFill(Color.web(color1,alpha+0.2));

		Rectangle rec_next_s = new Rectangle();
		rec_next_s.setX(447);
		rec_next_s.setY(75 - num);
		rec_next_s.setWidth(80);
		rec_next_s.setHeight(30);
		rec_next_s.setFill(Color.web(color1,alpha+0.2));

		Text book_s = new Text("No book");
		Text book2left = new Text("");
		Text author_s = new Text("");
		Text taken_s = new Text("");
		Text exp_s = new Text("");
		Text next_s = new Text("");
		Text next1 = new Text("Next:");
		Text request2 = new Text("");
		
		request2.setX(550);
		request2.setY(80);
		request2.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		book2left.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Button return2 = new Button("Return");
		return2.setLayoutX(292);
		return2.setLayoutY(80 - num);
		return2.setMinHeight(20);
		return2.setMinWidth(100);
		return2.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");

		book_s.setY(55 - num);
		book_s.setX(30);
		book_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));

		author_s.setX(450);
		author_s.setY(55 - num);
		author_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));

		taken_s.setY(112 - num);
		taken_s.setX(32);
		taken_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		exp_s.setY(112 - num);
		exp_s.setX(182);
		exp_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		next_s.setX(450);
		next_s.setY(95 - num);
		next_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		book2left.setX(30);
		book2left.setY(85 - num);

		next1.setX(408);
		next1.setY(95 - num);
		next1.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		return2.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		book2.getChildren().addAll(back1, rec_exp_s, rec_taken_s, rec_left_s, rec_next_s, book_s, author_s, book2left,
				taken_s, exp_s, next_s, return2, next1, request2);

		GridPane.setConstraints(gr, 0, 0);
		GridPane.setConstraints(book1, 0, 3);
		GridPane.setConstraints(book2, 0, 4);

		grid.getChildren().addAll(gr);
		grid.getChildren().addAll(top, book1, book2);

		

		this.home = new Scene(grid, 800, 400);
		

		//scene config
		
		VBox configGrid = new VBox();
		configGrid.setBackground(new Background(myBI));
		
		
		Text DBname1_label = new Text("DB name");
		TextField DBname1 = new TextField();
		Text DBtable1_label = new Text("Book table name");
		TextField DBtable1 = new TextField();
		Text logtable1_label = new Text("Logdata table name");
		TextField logtable1 = new TextField();
		Text DBuser1_label = new Text("DB user");
		TextField DBuser1 = new TextField();
		Text DBpassword1_label = new Text("DB password");
		PasswordField DBpassword1 = new PasswordField();
		Text DBpath1_label = new Text("DB path(e.g. 192.168.1.1)");
		TextField DBpath1 = new TextField();
		Text DBport1_label = new Text("DB port(e.g. 3306)");
		TextField DBport1 = new TextField();
		
		
		DBname1_label.setFill(Paint.valueOf("white"));
		DBtable1_label.setFill(Paint.valueOf("white"));
		logtable1_label.setFill(Paint.valueOf("white"));
		DBuser1_label.setFill(Paint.valueOf("white"));
		DBpassword1_label.setFill(Paint.valueOf("white"));
		DBpath1_label.setFill(Paint.valueOf("white"));
		DBport1_label.setFill(Paint.valueOf("white"));
		
		
		Button sendDetails1 = new Button("Apply");
		
		
		
		Group h1 = new Group();
		Group h2 = new Group();
		Group h3 = new Group();
		Group h4 = new Group();
		Group h5 = new Group();
		Group h6 = new Group();
		Group h7 = new Group();
		
		
		
		h1.getChildren().addAll(DBname1_label, DBname1);
		h2.getChildren().addAll(DBtable1_label, DBtable1);
		h3.getChildren().addAll(DBuser1_label, DBuser1);
		h4.getChildren().addAll(DBpassword1_label, DBpassword1);
		h5.getChildren().addAll(DBpath1_label, DBpath1);
		h6.getChildren().addAll(DBport1_label, DBport1);
		h7.getChildren().addAll(logtable1_label, logtable1);


		

		configGrid.getChildren().addAll(h1, h2, h7, h3, h4, h5, h6, sendDetails1);
		
		
		
		this.config = new Scene(configGrid, 800,400);
		
		
		
		
		
		
		
		
		
		
		// login page
		GridPane logging = new GridPane();
		
		logging.setBackground(new Background(myBI));
		logging.setPadding(new Insets(0, 0, 0, 0));
		logging.setVgap(10);
		logging.setHgap(10);

		
		
		Image img = new Image("img/img.png", 20,20,false,false);
		ImageView image = new ImageView(img);
		
		image.setOnMouseClicked(e->{
			window.setScene(this.config);
		});
		
		
		
		
		TextField input_un = new TextField("Username");
		PasswordField input_ps = new PasswordField();
		Text info = new Text();
		info.setStyle("");
		info.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		info.setStyle("-fx-font-weight: bold");
		Button submit = new Button("Log in");
		submit.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");

		info.setFill(Color.valueOf("#000000"));
		

		GridPane.setConstraints(input_un, 0, 0);
		GridPane.setConstraints(input_ps, 0, 1);
		GridPane.setConstraints(submit, 0, 2);
		GridPane.setConstraints(image, 1,0);
		
		GridPane.setConstraints(info, 0, 5);
		logging.setAlignment(Pos.CENTER);

		logging.getChildren().addAll(input_un, input_ps, submit, info);
		if(!new File(pathToFile).exists()) {
			logging.getChildren().add(image);
		}
		log = new Scene(logging, 800, 400);

		
		window.show();

		// catalogPage

		GridPane catalogGrid = new GridPane();
		
		
		catalogGrid.setBackground(new Background(myBI));
		catalogGrid.setHgap(25);
		catalogGrid.setVgap(15);
		
		
		
		catalogGrid.setPadding(new Insets(0, 0, 0, 0));
		catalogGrid.setVgap(10);
		catalogGrid.setHgap(10);

		Rectangle catalogrec = new Rectangle();
		catalogrec.setX(0);
		catalogrec.setY(0);
		catalogrec.setWidth(800);
		catalogrec.setHeight(40);
		catalogrec.toBack();
		catalogrec.setFill(Color.web(color1,alpha));

		Rectangle catalogrectangle = new Rectangle();
		catalogrectangle.setX(5);
		catalogrectangle.setY(7);
		catalogrectangle.setWidth(75);
		catalogrectangle.setHeight(26.5);
		catalogrectangle.setFill(Color.web(color1,alpha+0.2));

		Text cataloghome = new Text("Home");
		cataloghome.setX(26);
		cataloghome.setY(25);
		cataloghome.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Rectangle catalogrectangle1 = new Rectangle();
		catalogrectangle1.setX(90);
		catalogrectangle1.setY(7);
		catalogrectangle1.setWidth(75);
		catalogrectangle1.setHeight(26.5);
		catalogrectangle1.setFill(Color.web(color1,alpha+0.2));

		Text catalogcatalog = new Text("Catalog");
		catalogcatalog.setX(102);
		catalogcatalog.setY(25);
		catalogcatalog.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Rectangle catalogrectangle2 = new Rectangle();
		catalogrectangle2.setX(175);
		catalogrectangle2.setY(7);
		catalogrectangle2.setWidth(75);
		catalogrectangle2.setHeight(26.5);
		catalogrectangle2.setFill(Color.web(color1,alpha+0.2));

		Text catalogsettings = new Text("Settings");
		catalogsettings.setX(185);
		catalogsettings.setY(25);

		catalogsettings.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Text catalogdateTime = new Text();
		catalogdateTime.setX(375);
		catalogdateTime.setY(25);
		Timeline catalogclock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			catalogdateTime.setText(LocalDateTime.now().format(formatter));
		}), new KeyFrame(Duration.seconds(1)));
		catalogclock.setCycleCount(Animation.INDEFINITE);
		catalogclock.play();
		catalogdateTime.setFont(Font.font("Consolas", FontPosture.ITALIC, 18));
		catalogdateTime.setFill(Color.web(color2));

		Text catalogday = new Text();
		catalogday.setX(340);
		catalogday.setY(25);
		catalogday.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
		catalogday.setFont(Font.font("Consolas", FontPosture.ITALIC, 18));
		catalogday.setFill(Color.web(color2));

		Text catalogdate1 = new Text();
		catalogdate1.setX(445);
		catalogdate1.setY(25);
		catalogdate1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
		catalogdate1.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		catalogdate1.setFill(Color.web(color2));

		Text catalogdateDay = new Text();
		catalogdateDay.setX(470);
		catalogdateDay.setY(25);
		catalogdateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
		catalogdateDay.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		catalogdateDay.setFill(Color.web(color2));

		Text catalogyear = new Text();
		catalogyear.setX(490);
		catalogyear.setY(25);
		catalogyear.setText(Integer.toString(cal.get(Calendar.YEAR)));
		catalogyear.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		catalogyear.setFill(Color.web(color2));

		Text cataloguser = new Text();
		cataloguser.setX(660);
		cataloguser.setY(25);
		cataloguser.setText(this.username);
		cataloguser.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Button cataloglogout = new Button();
		cataloglogout.setLayoutX(735);
		cataloglogout.setLayoutY(7);
		cataloglogout.setText("Exit");
		
		cataloglogout.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");

		Group gr1 = new Group();

		gr1.getChildren().addAll(catalogrec, catalogrectangle, cataloghome, catalogrectangle1, catalogcatalog,
				catalogrectangle2, catalogsettings, catalogdateTime, catalogday, catalogdate1, catalogdateDay,
				catalogyear, cataloguser, cataloglogout);

		GridPane.setConstraints(gr1, 0, 0);

		catalogGrid.getChildren().add(gr1);

		

		
		
		Group list = new Group();
		
		Rectangle listBack = new Rectangle();
		listBack.setFill(Color.web(color1,alpha));
		listBack.setWidth(460);
		listBack.setHeight(300);
		listBack.setY(15);
		
		Rectangle listFront = new Rectangle();
		listFront.setFill(Color.web(color1,alpha+0.2));
		listFront.setWidth(440);
		listFront.setHeight(260);
		listFront.setY(45);
		listFront.setX(10);
		
		Text listLabel = new Text("Books: ");
		listLabel.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		listLabel.setX(10);
		listLabel.setY(35);
		
		
		TableView<String> listOfBooks = new TableView<>();
		listOfBooks.setLayoutX(10);
		listOfBooks.setLayoutY(45);
		listOfBooks.setMinHeight(260);
		listOfBooks.setMaxHeight(260);
		listOfBooks.setMinWidth(440);
		listOfBooks.setMaxWidth(440);
		
		TableColumn<String, String> column1 = new TableColumn<>("Book name");
		column1.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
		column1.setMinWidth(440);
		column1.setMaxWidth(440);
		
		listOfBooks.getColumns().add(column1);
		
		Text detailsAuthor = new Text("Author: ");
		detailsAuthor.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		detailsAuthor.setX(500);
		detailsAuthor.setY(35);
		
		
		Rectangle recAuthor = new Rectangle();
		recAuthor.setX(500);
		recAuthor.setY(40);
		recAuthor.setWidth(280);
		recAuthor.setHeight(27);
		recAuthor.setFill(Color.web(color1,alpha+0.2));
		
		
		Text detAuthor = new Text("");
		detAuthor.setY(57);
		detAuthor.setX(510);
		detAuthor.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		
		
		Text detailsStatus = new Text("Status:");
		detailsStatus.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		detailsStatus.setX(500);
		detailsStatus.setY(110);
		
		Rectangle recStatus = new Rectangle();
		recStatus.setX(500);
		recStatus.setY(115);
		recStatus.setWidth(280);
		recStatus.setHeight(27);
		recStatus.setFill(Color.web(color1,alpha+0.2));
		
		
		Text detStatus = new Text("");
		detStatus.setY(133);
		detStatus.setX(510);
		detStatus.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		
		
		Rectangle recNext = new Rectangle();
		recNext.setX(500);
		recNext.setY(185);
		recNext.setWidth(280);
		recNext.setHeight(27);
		recNext.setFill(Color.web(color1,alpha+0.2));
		
		Text detNext = new Text("");
		detNext.setY(202);
		detNext.setX(510);
		detNext.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		
		
		
		Button take = new Button("TAKE");
		take.setLayoutX(500);
		take.setLayoutY(240);
		take.setMinSize(70, 20);
		take.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");
		
		
		Rectangle rec_takeResult = new Rectangle();
		rec_takeResult.setX(500);
		rec_takeResult.setY(273);
		rec_takeResult.setWidth(280);
		rec_takeResult.setHeight(27);
		rec_takeResult.setFill(Color.web(color1,alpha+0.2));
		
		Text takeResult = new Text();
		takeResult.setX(510);
		takeResult.setY(290);
		detNext.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		
		
		list.getChildren().addAll(listBack, listFront,listOfBooks, recAuthor, recStatus, recNext, listLabel, detailsAuthor, detailsStatus, detNext, detStatus, detAuthor, take,rec_takeResult, takeResult);
		GridPane.setConstraints(list, 0, 1);
		
		
		catalogGrid.getChildren().add(list);
		
		
		this.catalog = new Scene(catalogGrid, 800, 400);

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//scene settings
		
		
		GridPane settingsGrid = new GridPane();
		settingsGrid.setHgap(25);
		settingsGrid.setVgap(15);
		
		settingsGrid.setBackground(new Background(myBI));
		
		settingsGrid.setPadding(new Insets(0, 0, 0, 0));
		settingsGrid.setVgap(10);
		settingsGrid.setHgap(10);

		Rectangle settingsrec = new Rectangle();
		settingsrec.setX(0);
		settingsrec.setY(0);
		settingsrec.setWidth(800);
		settingsrec.setHeight(40);
		settingsrec.toBack();
		settingsrec.setFill(Color.web(color1,alpha));

		Rectangle settingsrectangle = new Rectangle();
		settingsrectangle.setX(5);
		settingsrectangle.setY(7);
		settingsrectangle.setWidth(75);
		settingsrectangle.setHeight(26.5);
		settingsrectangle.setFill(Color.web(color1,alpha+0.2));

		Text settingshome = new Text("Home");
		settingshome.setX(26);
		settingshome.setY(25);
		settingshome.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		
		

		Rectangle settingsrectangle1 = new Rectangle();
		settingsrectangle1.setX(90);
		settingsrectangle1.setY(7);
		settingsrectangle1.setWidth(75);
		settingsrectangle1.setHeight(26.5);
		settingsrectangle1.setFill(Color.web(color1,alpha+0.2));

		Text settingscatalog = new Text("Catalog");
		settingscatalog.setX(102);
		settingscatalog.setY(25);
		settingscatalog.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Rectangle settingsrectangle2 = new Rectangle();
		settingsrectangle2.setX(175);
		settingsrectangle2.setY(7);
		settingsrectangle2.setWidth(75);
		settingsrectangle2.setHeight(26.5);
		settingsrectangle2.setFill(Color.web(color1,alpha+0.2));

		Text settingssettings = new Text("Settings");
		settingssettings.setX(185);
		settingssettings.setY(25);

		settingssettings.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Text settingsdateTime = new Text();
		settingsdateTime.setX(375);
		settingsdateTime.setY(25);
		Timeline settingsclock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
			settingsdateTime.setText(LocalDateTime.now().format(formatter));
		}), new KeyFrame(Duration.seconds(1)));
		settingsclock.setCycleCount(Animation.INDEFINITE);
		settingsclock.play();
		settingsdateTime.setFont(Font.font("Consolas", FontPosture.ITALIC, 18));
		settingsdateTime.setFill(Color.valueOf("black"));

		Text settingsday = new Text();
		settingsday.setX(340);
		settingsday.setY(25);
		settingsday.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
		settingsday.setFont(Font.font("Consolas", FontPosture.ITALIC, 18));
		settingsday.setFill(Paint.valueOf("black"));

		Text settingsdate1 = new Text();
		settingsdate1.setX(445);
		settingsdate1.setY(25);
		settingsdate1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
		settingsdate1.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		settingsdate1.setFill(Paint.valueOf("black"));

		Text settingsdateDay = new Text();
		settingsdateDay.setX(470);
		settingsdateDay.setY(25);
		settingsdateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
		settingsdateDay.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		settingsdateDay.setFill(Paint.valueOf("black"));

		Text settingsyear = new Text();
		settingsyear.setX(490);
		settingsyear.setY(25);
		settingsyear.setText(Integer.toString(cal.get(Calendar.YEAR)));
		settingsyear.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));
		settingsyear.setFill(Paint.valueOf("black"));

		Text settingsuser = new Text();
		settingsuser.setX(660);
		settingsuser.setY(25);
		settingsuser.setText(this.username);
		settingsuser.setFont(Font.font("Consolas", FontPosture.ITALIC, 14));

		Button settingslogout = new Button();
		settingslogout.setLayoutX(735);
		settingslogout.setLayoutY(7);
		settingslogout.setText("Exit");
		
		settingslogout.setStyle("-fx-background-color: "+color1+"; -fx-border-width: 0px;");

		Group gr3 = new Group();

		gr3.getChildren().addAll(settingsrec, settingsrectangle, settingshome, settingsrectangle1, settingscatalog,
				settingsrectangle2, settingssettings, settingsdateTime, settingsday, settingsdate1, settingsdateDay,
				settingsyear, settingsuser, settingslogout);

		GridPane.setConstraints(gr3, 0, 0);

		settingsGrid.getChildren().add(gr3);
		
		
		
		Text DBname_label = new Text("DB name");
		TextField DBname = new TextField();
		Text DBtable_label = new Text("Book table name");
		TextField DBtable = new TextField();
		Text logtable_label = new Text("Logdata table name");
		TextField logtable = new TextField();
		Text DBuser_label = new Text("DB user");
		TextField DBuser = new TextField();
		Text DBpassword_label = new Text("DB password");
		PasswordField DBpassword = new PasswordField();
		Text DBpath_label = new Text("DB path(e.g. 192.168.1.1)");
		TextField DBpath = new TextField();
		Text DBport_label = new Text("DB port(e.g. 3306)");
		TextField DBport = new TextField();
		
		DBname_label.setFill(Paint.valueOf("white"));
		DBtable_label.setFill(Paint.valueOf("white"));
		logtable_label.setFill(Paint.valueOf("white"));
		DBuser_label.setFill(Paint.valueOf("white"));
		DBpassword_label.setFill(Paint.valueOf("white"));
		DBpath_label.setFill(Paint.valueOf("white"));
		DBport_label.setFill(Paint.valueOf("white"));
		
		
		
		Button sendDetails = new Button("Apply");
		
		sendDetails.setOnAction(e->{	
		if(controller.updateDetails(pathToFile, DBname.getText().trim(), DBtable.getText().trim(),logtable.getText().trim(), DBuser.getText().trim(), DBpassword.getText().trim(), DBpath.getText().trim(), DBport.getText().trim())) {	
			window.setScene(this.home);
		}
		});
		
		Group hb1 = new Group();
		Group hb2 = new Group();
		Group hb3 = new Group();
		Group hb4 = new Group();
		Group hb5 = new Group();
		Group hb6 = new Group();
		Group hb7 = new Group();
		
		
		
		hb1.getChildren().addAll(DBname_label, DBname);
		hb2.getChildren().addAll(DBtable_label, DBtable);
		hb3.getChildren().addAll(DBuser_label, DBuser);
		hb4.getChildren().addAll(DBpassword_label, DBpassword);
		hb5.getChildren().addAll(DBpath_label, DBpath);
		hb6.getChildren().addAll(DBport_label, DBport);
		hb7.getChildren().addAll(logtable_label, logtable);

		
		GridPane.setConstraints(hb1, 0, 1);
		GridPane.setConstraints(hb2, 0, 2);
		GridPane.setConstraints(hb7, 0, 3);
		GridPane.setConstraints(hb3, 0, 4);
		GridPane.setConstraints(hb4, 0, 5);
		GridPane.setConstraints(hb5, 0, 6);
		GridPane.setConstraints(hb6, 0, 7);
		GridPane.setConstraints(sendDetails, 0, 8);
		

		settingsGrid.getChildren().addAll(hb1, hb2, hb7, hb3, hb4, hb5, hb6, sendDetails);
		
		
		this.settings = new Scene(settingsGrid, 800, 400);
		
		

		
		
		
		
		
		
		
		
		
		
		
		
		
		home.setOnMouseClicked(e -> {
			
			day.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
			date1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
			dateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
			year.setText(Integer.toString(cal.get(Calendar.YEAR)));
			List<String> books = controller.getBooks(this.username);
			String firstBook = books.get(0) == null ? "No book" : books.get(0).trim().isEmpty()?"No book":books.get(0);
			String secondBook = books.get(1) == null ? "No book" : books.get(1).trim().isEmpty()?"No book":books.get(1);

			if(!controller.checkStatus()) if(!gr.getChildren().contains(notification_group))gr.getChildren().add(notification_group);
			if(controller.getMessage(this.username)!=null) {
				if(!gr.getChildren().contains(notification_group)) {
					if(!gr.getChildren().contains(notification_group)) {
						if(gr3.getChildren().contains(notification_group))gr3.getChildren().remove(notification_group);
						if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
						gr.getChildren().add(notification_group);
					}
				}
			}
			else gr.getChildren().remove(notification_group);

			
			List<String> book_first_data = controller.getData(firstBook);
			List<String> book_second_data = controller.getData(secondBook);
			book_f.setText(firstBook.trim().equals("No book")?"No book":firstBook+", "+book_first_data.get(0));
			book_s.setText(secondBook.trim().equals("No book")?"No book":secondBook+", "+book_second_data.get(0));
			
			//author_f.setText(book_first_data.get(0));
			//author_s.setText(book_second_data.get(0));
			
			if(book_s.getText().length()>34) {
				book_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}
			
			if(book_f.getText().length()>34) {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}

			book1left.setText(book_first_data.get(1));
			book2left.setText(book_second_data.get(1));

			taken_f.setText(book_first_data.get(2));
			taken_s.setText(book_second_data.get(2));

			exp_f.setText(book_first_data.get(3));
			exp_s.setText(book_second_data.get(3));

			next_f.setText(book_first_data.get(4));
			next_s.setText(book_second_data.get(4));

			request.setText(controller.getReturn(firstBook));
			request2.setText(controller.getReturn(secondBook));
			
			window.setScene(this.home);

		});

		update.setOnAction(e -> {
			day.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
			date1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
			dateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
			year.setText(Integer.toString(cal.get(Calendar.YEAR)));
			List<String> books = controller.getBooks(this.username);
			String firstBook = books.get(0) == null ? "No book" : books.get(0).trim().isEmpty()?"No book":books.get(0);
			String secondBook = books.get(1) == null ? "No book" : books.get(1).trim().isEmpty()?"No book":books.get(1);

			
			if(!controller.checkStatus()) if(!gr.getChildren().contains(notification_group))gr.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
				System.out.println("User: "+this.username);
				System.out.println("Message: "+controller.getMessage(this.username));
				
				if(!gr.getChildren().contains(notification_group)) {
					if(!gr.getChildren().contains(notification_group)) {
						if(gr3.getChildren().contains(notification_group))gr3.getChildren().remove(notification_group);
						if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
						gr.getChildren().add(notification_group);
					}
				}
			}
			else gr.getChildren().remove(notification_group);
			
			List<String> book_first_data = controller.getData(firstBook);
			List<String> book_second_data = controller.getData(secondBook);
			book_f.setText(firstBook.trim().equals("No book")?"No book":firstBook+", "+book_first_data.get(0));
			book_s.setText(secondBook.trim().equals("No book")?"No book":secondBook+", "+book_second_data.get(0));
			
			if(book_s.getText().length()>34) {
				book_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}
			
			if(book_f.getText().length()>34) {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}
			
			//author_f.setText(book_first_data.get(0));
			//author_s.setText(book_second_data.get(0));

			book1left.setText(book_first_data.get(1));
			book2left.setText(book_second_data.get(1));

			taken_f.setText(book_first_data.get(2));
			taken_s.setText(book_second_data.get(2));

			exp_f.setText(book_first_data.get(3));
			exp_s.setText(book_second_data.get(3));

			next_f.setText(book_first_data.get(4));
			next_s.setText(book_second_data.get(4));
			
			request.setText(controller.getReturn(firstBook));
			request2.setText(controller.getReturn(secondBook));
		});
		
		
		submit.setOnAction(e -> {
			if (controller.login(input_un.getText(), input_ps.getText())) {

				this.username = input_un.getText().trim();
				user.setText(this.username);
				input_un.setText("");
				input_ps.setText("");
				info.setText("");
				day.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
				date1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
				dateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
				year.setText(Integer.toString(cal.get(Calendar.YEAR)));
				List<String> books = controller.getBooks(this.username);
				String firstBook = books.get(0) == null ? "No book" : books.get(0).trim().isEmpty()?"No book":books.get(0);
				String secondBook = books.get(1) == null ? "No book" : books.get(1).trim().isEmpty()?"No book":books.get(1);


				if(!controller.checkStatus()) if(!gr.getChildren().contains(notification_group))gr.getChildren().add(notification_group);

				if(controller.getMessage(this.username)!=null) {
					if(!gr.getChildren().contains(notification_group)) {
						if(!gr.getChildren().contains(notification_group)) {
							if(gr3.getChildren().contains(notification_group))gr3.getChildren().remove(notification_group);
							if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
							gr.getChildren().add(notification_group);
						}
					}
				}
				else gr.getChildren().remove(notification_group);
				
				List<String> book_first_data = controller.getData(firstBook);
				List<String> book_second_data = controller.getData(secondBook);

				
				book_f.setText(firstBook.trim().equals("No book")?"No book":firstBook+", "+book_first_data.get(0));
				book_s.setText(secondBook.trim().equals("No book")?"No book":secondBook+", "+book_second_data.get(0));
				
				//author_f.setText(book_first_data.get(0));
				//author_s.setText(book_second_data.get(0));
				
				if(book_s.getText().length()>34) {
					book_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
				}else {
					book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
				}
				
				if(book_f.getText().length()>34) {
					book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
				}else {
					book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
				}

				book1left.setText(book_first_data.get(1));
				book2left.setText(book_second_data.get(1));

				taken_f.setText(book_first_data.get(2));
				taken_s.setText(book_second_data.get(2));

				exp_f.setText(book_first_data.get(3));
				exp_s.setText(book_second_data.get(3));

				next_f.setText(book_first_data.get(4));
				next_s.setText(book_second_data.get(4));

				
				request.setText(controller.getReturn(firstBook));
				request2.setText(controller.getReturn(secondBook));
				
				window.setScene(this.home);
			} else {
				info.setText(controller.checkStatus()?"Incorrect login or password, \nmaybe you are already logged in":"No connection to server. \nAsk admin for help");
				input_ps.setText("");
			}
		});
		
		
		catalogsettings.setOnMouseClicked(e->{
			window.setScene(this.settings);
			window.setScene(this.settings);
			DBname.setText(controller.getName());
			DBtable.setText(controller.getTable());
			DBuser.setText(controller.getUser());
			DBpassword.setText("root");
			DBpath.setText(controller.getPath());
			DBport.setText(controller.getPort());
			settingsuser.setText(this.username);
			
			if(!controller.checkStatus()) if(!settingsGrid.getChildren().contains(notification_group))settingsGrid.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
				if(!gr3.getChildren().contains(notification_group)) {
					if(gr.getChildren().contains(notification_group))gr.getChildren().remove(notification_group);
					if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
					gr3.getChildren().add(notification_group);
				}
			}
			else gr3.getChildren().remove(notification_group);
			
			
			
		});
		
		settings.setOnMouseClicked(e->{
			window.setScene(this.settings);
			DBname.setText(controller.getName());
			DBtable.setText(controller.getTable());
			DBuser.setText(controller.getUser());
			DBpassword.setText("root");
			DBpath.setText(controller.getPath());
			DBport.setText(controller.getPort());
			if(!controller.checkStatus()) if(!settingsGrid.getChildren().contains(notification_group))settingsGrid.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
				if(!gr3.getChildren().contains(notification_group)) {
					if(gr.getChildren().contains(notification_group))gr.getChildren().remove(notification_group);
					if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
					gr3.getChildren().add(notification_group);
				}
			}
			else gr3.getChildren().remove(notification_group);
			settingsuser.setText(this.username);
			
		});
		
		cataloghome.setOnMouseClicked(e -> {

			day.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
			date1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
			dateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
			year.setText(Integer.toString(cal.get(Calendar.YEAR)));
			List<String> books = controller.getBooks(this.username);
			String firstBook = books.get(0) == null ? "No book" : books.get(0).trim().isEmpty()?"No book":books.get(0);
			String secondBook = books.get(1) == null ? "No book" : books.get(1).trim().isEmpty()?"No book":books.get(1);


			
			
			List<String> book_first_data = controller.getData(firstBook);
			List<String> book_second_data = controller.getData(secondBook);
			book_f.setText(firstBook.trim().equals("No book")?"No book":firstBook+", "+book_first_data.get(0));
			book_s.setText(secondBook.trim().equals("No book")?"No book":secondBook+", "+book_second_data.get(0));
			

			
			if(book_s.getText().length()>34) {
				book_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}
			
			if(book_f.getText().length()>34) {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}

			book1left.setText(book_first_data.get(1));
			book2left.setText(book_second_data.get(1));

			taken_f.setText(book_first_data.get(2));
			taken_s.setText(book_second_data.get(2));

			exp_f.setText(book_first_data.get(3));
			exp_s.setText(book_second_data.get(3));

			next_f.setText(book_first_data.get(4));
			next_s.setText(book_second_data.get(4));

			if(!controller.checkStatus()) if(!gr.getChildren().contains(notification_group))gr.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
				if(!gr.getChildren().contains(notification_group)) {
					if(!gr.getChildren().contains(notification_group)) {
						if(gr3.getChildren().contains(notification_group))gr3.getChildren().remove(notification_group);
						if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
						gr.getChildren().add(notification_group);
					}
				}
			}
			else gr.getChildren().remove(notification_group);
			
			request.setText(controller.getReturn(firstBook));
			request2.setText(controller.getReturn(secondBook));
			
			window.setScene(this.home);

		});
		
		
		settingshome.setOnMouseClicked(e->{
			day.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
			date1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
			dateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
			year.setText(Integer.toString(cal.get(Calendar.YEAR)));
			List<String> books = controller.getBooks(this.username);
			String firstBook = books.get(0) == null ? "No book" : books.get(0).trim().isEmpty()?"No book":books.get(0);
			String secondBook = books.get(1) == null ? "No book" : books.get(1).trim().isEmpty()?"No book":books.get(1);

			if(!controller.checkStatus()) if(!gr.getChildren().contains(notification_group))gr.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
					if(!gr.getChildren().contains(notification_group)) {
						if(gr3.getChildren().contains(notification_group))gr3.getChildren().remove(notification_group);
						if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
						gr.getChildren().add(notification_group);
					}			
				}
			else gr.getChildren().remove(notification_group);
			
			List<String> book_first_data = controller.getData(firstBook);
			List<String> book_second_data = controller.getData(secondBook);
			book_f.setText(firstBook.trim().equals("No book")?"No book":firstBook+", "+book_first_data.get(0));
			book_s.setText(secondBook.trim().equals("No book")?"No book":secondBook+", "+book_second_data.get(0));
			
			
			if(book_s.getText().length()>34) {
				book_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}
			
			if(book_f.getText().length()>34) {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
			}else {
				book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
			}

			book1left.setText(book_first_data.get(1));
			book2left.setText(book_second_data.get(1));

			taken_f.setText(book_first_data.get(2));
			taken_s.setText(book_second_data.get(2));

			exp_f.setText(book_first_data.get(3));
			exp_s.setText(book_second_data.get(3));

			next_f.setText(book_first_data.get(4));
			next_s.setText(book_second_data.get(4));

			
			request.setText(controller.getReturn(firstBook));
			request2.setText(controller.getReturn(secondBook));
			
			window.setScene(this.home);
			
		});
		
		
		catalog.setOnMouseClicked(e -> {
			cataloguser.setText(this.username);
			listOfBooks.getItems().clear();
			for(String str : controller.getAllBooks()) {
				listOfBooks.getItems().add(str);
			}
			
			if(!controller.checkStatus()) if(!catalogGrid.getChildren().contains(notification_group))catalogGrid.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
				if(!gr1.getChildren().contains(notification_group)) {
					if(gr.getChildren().contains(notification_group)) gr.getChildren().remove(notification_group);
					if(gr3.getChildren().contains(notification_group)) gr3.getChildren().remove(notification_group);
					gr1.getChildren().add(notification_group);
				}
			}
			else gr1.getChildren().remove(notification_group);
			
			window.setScene(this.catalog);
			takeResult.setText("");
		});
		
		listOfBooks.setOnMouseClicked(e->{
			String str = listOfBooks.getSelectionModel().getSelectedItem();
			List<String> bookData = controller.getAllData(str.trim());
			detAuthor.setText(bookData.get(2));
			detStatus.setText(bookData.get(8));
			detNext.setText(bookData.get(3).trim().isEmpty()?"Not in use":"In use by "+bookData.get(3).trim()+" till "+bookData.get(6));
		});
		
		catalogcatalog.setOnMouseClicked(e->{
			listOfBooks.getItems().clear();
			for(String str : controller.getAllBooks()) {
				listOfBooks.getItems().add(str);
			}
			takeResult.setText("");
			if(!controller.checkStatus()) if(!catalogGrid.getChildren().contains(notification_group))catalogGrid.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
				if(controller.getMessage(this.username)!=null) {
					if(!gr1.getChildren().contains(notification_group)) {
						if(gr.getChildren().contains(notification_group)) gr.getChildren().remove(notification_group);
						if(gr3.getChildren().contains(notification_group)) gr3.getChildren().remove(notification_group);
						gr1.getChildren().add(notification_group);
					}
				}		
				}
			else gr1.getChildren().remove(notification_group);
			
		});
		
		
		settingscatalog.setOnMouseClicked(e->{
			listOfBooks.getItems().clear();
			for(String str : controller.getAllBooks()) {
				listOfBooks.getItems().add(str);
			}
			takeResult.setText("");
			if(!controller.checkStatus()) if(!catalogGrid.getChildren().contains(notification_group))catalogGrid.getChildren().add(notification_group);

			if(controller.getMessage(this.username)!=null) {
				if(controller.getMessage(this.username)!=null) {
					if(!gr1.getChildren().contains(notification_group)) {
						if(gr.getChildren().contains(notification_group)) gr.getChildren().remove(notification_group);
						if(gr3.getChildren().contains(notification_group)) gr3.getChildren().remove(notification_group);
						gr1.getChildren().add(notification_group);
					}
				}			}
			else gr1.getChildren().remove(notification_group);
			window.setScene(this.catalog);
			
		});
		
		
		take.setOnAction(e->{
			
			if(listOfBooks.getSelectionModel().getSelectedItem()==null) {
				takeResult.setText("Choose book first");
				return;
			}
			
			
			
			
			List<String> books = controller.getBooks(this.username);
			int quantity = 0;
			for(String str : books) {
				if(!str.trim().equals("No book")) {
					quantity++;
				}
				
			}
			if(quantity==2) {
				takeResult.setText("You already have two books to read");
				return;
			}
		
		
			takeResult.setText(controller.reserveBook(this.username, listOfBooks.getSelectionModel().getSelectedItem().trim()));	
			
		});
		
		
		return1.setOnAction(e->{
			
			if(!book_f.getText().trim().contains("(you are next)")) {
				controller.sendReturn(this.username, book_f.getText().trim().split(",")[0]);
				request.setText(book_f.getText().contains("No book")?"":"Return requested, \nwait for confirmation");
			}else {
				controller.cancelBooking(this.username, book_f.getText().trim().split(",")[0]);
			}
			
			
			
			
			
		});
		
		return2.setOnAction(e->{
			
		
			
			if(!book_s.getText().trim().contains("(you are next)")) {
				controller.sendReturn(this.username, book_s.getText().trim().split(",")[0]);
				request2.setText(book_s.getText().contains("No book")?"":"Return requested, \nwait for confirmation");
			}else {
				controller.cancelBooking(this.username, book_s.getText().trim().split(",")[0]);
			}
			
			
		});
		
		// window.setScene(this.home);
		//window.setScene(this.catalog);
		//window.setScene(this.settings);
		window.setScene(this.log);
		
		
		
		window.setOnCloseRequest(e->{
				this.username = "";
				window.close();
		});
		
		
		
		logout.setOnMouseClicked(e -> {
			
				this.username = "";
				logging.getChildren().clear();
				logging.getChildren().addAll(input_un, input_ps, submit, info);
				if(!new File(pathToFile).exists()) {
					logging.getChildren().add(image);
				}
				window.setScene(log);
			
		});
		
		cataloglogout.setOnMouseClicked(e -> {
			
			this.username = "";
			logging.getChildren().clear();
			logging.getChildren().addAll(input_un, input_ps, submit, info);
			if(!new File(pathToFile).exists()) {
				logging.getChildren().add(image);
			}
			window.setScene(log);
			
		});
		
		settingslogout.setOnMouseClicked(e -> {
				this.username = "";
				logging.getChildren().clear();
				logging.getChildren().addAll(input_un, input_ps, submit, info);
				if(!new File(pathToFile).exists()) {
					logging.getChildren().add(image);
				}
				window.setScene(log);
		});
		
		sendDetails1.setOnAction(e->{	
			
			if(controller.updateDetails(pathToFile, DBname1.getText().trim(), DBtable1.getText().trim(),logtable1.getText().trim(), DBuser1.getText().trim(), DBpassword1.getText().trim(), DBpath1.getText().trim(), DBport1.getText().trim())) {
				logging.getChildren().clear();
				logging.getChildren().addAll(input_un, input_ps, submit, info);
				if(!new File(pathToFile).exists()) {
					logging.getChildren().add(image);
				}
				window.setScene(this.log);
				info.setText("");
				this.controller = new Controller();
			}
			});
		
		 notification.setOnMouseClicked(e->{	
			 String message = controller.getMessage(this.username);
	        	gr.getChildren().remove(notification_group);
	        	gr1.getChildren().remove(notification_group);
	        	gr3.getChildren().remove(notification_group);
	        	System.out.println("Username: "+this.username);
	        	System.out.println("Message: "+message);
	        	controller.messageRead(this.username);
				alert.setContentText(controller.checkStatus()?message:"No connection to the host");
	            alert.showAndWait();
	        });
		 alarm_not.setOnMouseClicked(e->{	
			 String message = controller.getMessage(this.username);
	        	gr.getChildren().remove(notification_group);
	        	gr1.getChildren().remove(notification_group);
	        	gr3.getChildren().remove(notification_group);
	        	System.out.println("Username: "+this.username);
	        	System.out.println("Message: "+message);
	        	controller.messageRead(this.username);
				alert.setContentText(controller.checkStatus()?message:"No connection to the host");
	            alert.showAndWait();
	            
	        });
		 log.setOnKeyPressed(e->{
			 if(e.getCode().toString().toUpperCase().equals("ENTER")) {
				 if (controller.login(input_un.getText(), input_ps.getText())) {

						this.username = input_un.getText().trim();
						user.setText(this.username);
						input_un.setText("");
						input_ps.setText("");
						info.setText("");
						day.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
						date1.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime()));
						dateDay.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
						year.setText(Integer.toString(cal.get(Calendar.YEAR)));
						List<String> books = controller.getBooks(this.username);
						String firstBook = books.get(0) == null ? "No book" : books.get(0).trim().isEmpty()?"No book":books.get(0);
						String secondBook = books.get(1) == null ? "No book" : books.get(1).trim().isEmpty()?"No book":books.get(1);


						if(!controller.checkStatus()) if(!gr.getChildren().contains(notification_group))gr.getChildren().add(notification_group);

						if(controller.getMessage(this.username)!=null) {
							if(!gr.getChildren().contains(notification_group)) {
								if(!gr.getChildren().contains(notification_group)) {
									if(gr3.getChildren().contains(notification_group))gr3.getChildren().remove(notification_group);
									if(gr1.getChildren().contains(notification_group))gr1.getChildren().remove(notification_group);
									gr.getChildren().add(notification_group);
								}
							}
						}
						else gr.getChildren().remove(notification_group);
						
						List<String> book_first_data = controller.getData(firstBook);
						List<String> book_second_data = controller.getData(secondBook);

						
						book_f.setText(firstBook.trim().equals("No book")?"No book":firstBook+", "+book_first_data.get(0));
						book_s.setText(secondBook.trim().equals("No book")?"No book":secondBook+", "+book_second_data.get(0));
						
						//author_f.setText(book_first_data.get(0));
						//author_s.setText(book_second_data.get(0));
						
						if(book_s.getText().length()>34) {
							book_s.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
						}else {
							book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
						}
						
						if(book_f.getText().length()>34) {
							book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 22));
						}else {
							book_f.setFont(Font.font("Consolas", FontPosture.ITALIC, 30));
						}

						book1left.setText(book_first_data.get(1));
						book2left.setText(book_second_data.get(1));

						taken_f.setText(book_first_data.get(2));
						taken_s.setText(book_second_data.get(2));

						exp_f.setText(book_first_data.get(3));
						exp_s.setText(book_second_data.get(3));

						next_f.setText(book_first_data.get(4));
						next_s.setText(book_second_data.get(4));

						
						request.setText(controller.getReturn(firstBook));
						request2.setText(controller.getReturn(secondBook));
						
						window.setScene(this.home);
					} else {
						info.setText(controller.checkStatus()?"Incorrect login or password, \nmaybe you are already logged in":"No connection to server. \nAsk admin for help");
						input_ps.setText("");
					}
			 }
		 });
		
	}

}
