package pkg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model {
	private final String DBname = "mydb";
	private final String DBuser = "root";
	private final String DBpassword = "root";
	private final String DBpath = "localhost";
	private final int DBport = 3306;
	private final String connectionURL = "jdbc:mysql://localhost:3306/mydb?user=" + DBuser + "&password=" + DBpassword
			+ "&serverTimezone=UTC";;
	private Connection connection;
//	private LinkedList listOfGoods = new LinkedList();
	private BinaryTree listOfGoods = new BinaryTree();

	// constructor
	// establishes connection with Database
	public Model() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
		}
		// checks if database is alive
		try (Connection connection = DriverManager.getConnection(this.connectionURL);) {
		} catch (Exception e) {
			System.out.println("Connection to Database '" + this.DBname + "' on " + this.DBpath + ":" + this.DBport
					+ " is unsuccessful");
			e.printStackTrace();
			return;
		}
		// if alive types to console and establishes connection
		try {
			this.connection = DriverManager.getConnection(this.connectionURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connection to Database '" + this.DBname + "' on " + this.DBpath + ":" + this.DBport
				+ " is successful");

		// filling linked list with existing Database values
		String query = "SELECT * FROM shop;";
		try (Statement statement = connection.prepareStatement(query);) {
			try (ResultSet result = statement.executeQuery(query);) {
				while (result.next()) {
					this.listOfGoods.add(result.getString(2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// checks if Database is alive
	protected boolean checkStatus() {
		try (Connection connection = DriverManager.getConnection(this.connectionURL);) {
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected void printContents() {
		String query = "SELECT * FROM shop";
		try {
			Statement stmt = connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void addGood(String goodName, int goodPrice, String goodCharacteristics, String category) {

		// add new good page to the filesystem
		File newPage = new File("C:/Users/sanja/workspace/TAProject/WebContent/" + goodName + ".html");
		try (OutputStream writer = new FileOutputStream(newPage);) {
			newPage.createNewFile();
			String goodCPU = goodCharacteristics.split("\n")[0];
			String goodGPU = goodCharacteristics.split("\n")[1];
			String goodRAM = goodCharacteristics.split("\n")[2];
			String goodDrive = goodCharacteristics.split("\n")[3];
			String text = "<!DOCTYPE html>\r\n" + 
					"<html>\r\n" + 
					"<head>\r\n" + 
					"  <meta charset=\"utf-8\">\r\n" + 
					"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
					"  <title>Сайт</title>\r\n" + 
					"  <link rel=\"stylesheet\" href=\"css/bootstrap.css\">\r\n" + 
					"  <link rel=\"stylesheet\" href=\"css/main.css\">\r\n" + 
					"  <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.8.2/css/all.css\" integrity=\"sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay\" crossorigin=\"anonymous\">\r\n" + 
					"  <link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\r\n" + 
					"</head>\r\n" + 
					"<body>\r\n" + 
					"  <body>\r\n" + 
					"	<div class=\"w3-sidebar w3-bar-block w3-border-right\" style=\"display: none\" id=\"mySidebar\">\r\n" + 
					"	<button onclick=\"w3_close()\" class=\"w3-bar-item w3-large\">Close&times;</button>\r\n" + 
					"	<form action=\"Controller\" method=\"post\">\r\n" +
					"   <button class=\"w3-bar-item w3-button\"><a href=\"index.html\" >Home</a></button>\r\n"+
					"		<input type=\"submit\" value=\"Laptop\" name=\"category\" class=\"w3-bar-item w3-button\">\r\n" + 
					"		<input type=\"submit\" value=\"Phone\" name=\"category\" class=\"w3-bar-item w3-button\">\r\n" + 
					"		<input type=\"submit\" value=\"Food\" name=\"category\" class=\"w3-bar-item w3-button\">\r\n" + 
					"		</form>\r\n" + 
					"	</div>\r\n" + 
					"	<div class=\"w3-teal\">\r\n" + 
					"		<button class=\"w3-button w3-xlarge\" onclick=\"w3_open()\" >☰  Navigate</button>\r\n" + 
					"		\r\n" + 
					"	</div>\r\n" + 
					"	<div id=\"headerwrap\">\r\n" + 
					"		<div class=\"container\">\r\n" + 
					"			<div class=\"row centered\">\r\n" + 
					"				<div class=\"col-lg-8 col-lg-offset-2\">\r\n" + 
					"					<h1><font color=\"black\">Internet-catalog</font></h1>\r\n" + 
					"					<h1><font color=\"black\">Baklashka</font></h1>\r\n" + 
					"				</div>\r\n" + 
					"			</div>\r\n" + 
					"		</div>\r\n" + 
					"	</div>" + 
					"	<div id=\"lg\">\r\n" + 
					"		<div class=\"container\">\r\n" + 
					"			<div class=\"row centered\">\r\n" + 
					"				<div class=\"container\">\r\n" + 
					"					<form role=\"form\" action=\"Controller\" method=\"post\">\r\n" + 
					"						<div class=\"form-group\">\r\n" + 
					"							<input type=\"text\" name=\"search_query\" class=\"form-control\"\r\n" + 
					"								id=\"search\" placeholder=\"Search...\">\r\n" + 
					"						</div>\r\n" + 
					"						<div class=\"checkbox\">\r\n" + 
					"							<input type=\"submit\" class=\"btn btn-success\" value=\"Search\" name=\"search_button\">\r\n" + 
					"						</div>\r\n" + 
					"\r\n" + 
					"					</form>\r\n" + 
					"				</div>\r\n" + 
					"			</div>\r\n" + 
					"		</div>\r\n" + 
					"  <div class=\"container w\">\r\n" + 
					"			<div class=\"row centered\">\r\n" + 
					"				<br> <br>\r\n" + 
					"				<div class=\"col-lg-4\">\r\n" + 
					"\r\n" + 
					"					<p>\r\n" + 
					"					<h2>"+goodName+"</h2>\r\n" + 
					"					</p>\r\n" + 
					"				</div>\r\n" + 
					"				<div class=\"col-lg-4\">\r\n" + 
					"\r\n" + 
					"					<h1>"+goodPrice+"</h1>\r\n" + 
					"					</br>\r\n" + 
					"				</div>\r\n" + 
					"			</div>\r\n" + 
					"			<br> <br>\r\n" + 
					"		</div>\r\n" + 
					"		<div>\r\n" + 
					"<center>\r\n"+
					"			<h3>"+goodCPU+"</h3>\r\n" + 
					"			<h3>"+goodGPU+"</h3>\r\n" + 
					"			<h3>"+goodRAM+"</h3>\r\n" + 
					"			<h3>"+goodDrive+"</h3>\r\n" + 
					"</center>\r\n"+
					"		</div>"+
					"  <div class=\"container wb\">\r\n" + 
					"    <div class=\"row centered\">\r\n" + 
					"      <br><br>\r\n" + 
					"      <div class=\"col-lg-2\"></div>\r\n" + 
					"      <div class=\"col-lg-10 col-lg-offset-1\">\r\n" + 
					"        <img src=\"img/munter.png\" alt=\"\" class=\"img-responsive\">\r\n" + 
					"      </div>\r\n" + 
					"    </div>\r\n" + 
					"  </div>\r\n" + 
					"\r\n" + 
					"    <div id=\"r\">\r\n" + 
					"    <div class=\"container\">\r\n" + 
					"      <div class=\"row centered\">\r\n" + 
					"        <div class=\"col-lg-8 col-lg-offset-2\">\r\n" + 
					"          <h4>National University of Ukraine</h4>\r\n" + 
					"          <h3><font color=\"white\">Igor Sikorsky Kyiv Polytechnic Institute</font></h3>\r\n" + 
					"          <h2><font color=\"white\">Department of automatics and control in technical systems</font></h2>\r\n" + 
					"          <h2><font color=\"white\">FICT, IA-83</font></h2>\r\n" + 
					"        </div>\r\n" + 
					"      </div>\r\n" + 
					"    </div>\r\n" + 
					"  </div>\r\n" + 
					"  <div id=\"f\">\r\n" + 
					"    <div class=\"container\">\r\n" + 
					"      <div class=\"row centered\">\r\n" + 
					"        <a href=\"https://instagram.com/alex_zhyshko?igshid=1xbjjktt0xngt\">Саша<i class=\"fab fa-instagram\"></i></a>\r\n" + 
					"        <a href=\"https://www.instagram.com/vlad.s_217/\">Влад<i class=\"fab fa-instagram\"></i></a>\r\n" + 
					"        <a href=\"https://www.instagram.com/dima_nechitaylo/\">Дима<i class=\"fab fa-instagram\"></i></a>\r\n" + 
					"        <a href=\"https://www.instagram.com/vkrasiy/\">Виталь<i class=\"fab fa-instagram\"></i></a>\r\n" + 
					"        <a href=\"https://www.instagram.com/pustoye__mesto/\">Марта<i class=\"fab fa-instagram\"></i></a>\r\n" + 
					"      </div>\r\n" + 
					"    </div>\r\n" + 
					"  </div>"+
					"\r\n" + 
					"  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js\"></script>\r\n" + 
					"  <script src=\"js/bootstrap.min.js\"></script>\r\n" + 
					"  <script>\r\n" + 
					"			function w3_open() {\r\n" + 
					"				document.getElementById(\"mySidebar\").style.display = \"block\";\r\n" + 
					"			}\r\n" + 
					"\r\n" + 
					"			function w3_close() {\r\n" + 
					"				document.getElementById(\"mySidebar\").style.display = \"none\";\r\n" + 
					"			}\r\n" + 
					"		</script>\r\n" + 
					"</body>\r\n" + 
					"</html>";
			writer.write(text.getBytes("UTF-8"));
			// add new good to the Database
			String query = "INSERT INTO shop (good_name, good_price, good_characteristics, good_category, good_page_url) VALUES (?, ?, ?, ?, ?);";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query);) {
				preparedStatement.setString(1, goodName);
				preparedStatement.setInt(2, goodPrice);
				preparedStatement.setString(3, goodCharacteristics);
				preparedStatement.setString(4, category);
				preparedStatement.setString(5, goodName + ".html");
				try {
					preparedStatement.executeUpdate();
					System.out.println("Successfully added to the DB");

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Couldn't add to the DB");
					return;
				}

			} catch (SQLSyntaxErrorException e) {
				e.printStackTrace();
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// updating Tree of goods with new data added
			this.listOfGoods.add(goodName);
	}

	// kills connection
	protected void killDB() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Couldn't close the connection with " + this.DBname + " on " + this.connectionURL);
			return;
		}
		System.out.println("Successfully closed connection with " + this.DBname + " on " + this.connectionURL);
	}

	protected List<String> getCategory(String selectedCategory) {
		String query = "SELECT * FROM shop;";

		List<String> listOfCategories = new ArrayList<>();
		try (Statement stmnt = connection.prepareStatement(query);) {
			try (ResultSet rs = stmnt.executeQuery(query);) {
				while (rs.next()) {
					if (rs.getString(5).equals(selectedCategory)) {
						listOfCategories.add(rs.getString(2));
					}
				}
				mergeSort(listOfCategories);
				return listOfCategories;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Can't find goods of specified category");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Can't find goods of specified category");
			return null;
		}

	}

	protected List<String> searchByWord(String word) {
		List<String> result = this.listOfGoods.search(word);
		try {
			quickSort(result);	
			
		}catch(IllegalArgumentException e) {
			e.printStackTrace();
			
		}
		
		return result;

	}

	private static void quickSort(List<String> list)  throws IllegalArgumentException{
		sort(list, 0, list.size() - 1);
	}

	private static void sort(List<String> list, int from, int to) {
		if (from < to) {
			int pivot = from;
			int left = from + 1;
			int right = to;
			String pivotValue = list.get(pivot);
			while (left <= right) {
				while (left <= to && list.get(left).compareTo(pivotValue) <= 0) {
					left++;
				}
				while (right > from && list.get(right).compareTo(pivotValue) > 0) {
					right--;
				}
				if (left < right) {
					Collections.swap(list, left, right);
				}
			}
			Collections.swap(list, pivot, left - 1);
			sort(list, from, right - 1);
			sort(list, right + 1, to);
		}
	}

	private List<String> mergeSort(List<String> whole) throws IllegalArgumentException{
		List<String> left = new ArrayList<String>();
		List<String> right = new ArrayList<String>();
		int center;

		if (whole.size() == 1) {
			return whole;
		} else {
			center = whole.size() / 2;
			for (int i = 0; i < center; i++) {
				left.add(whole.get(i));
			}
			for (int i = center; i < whole.size(); i++) {
				right.add(whole.get(i));
			}
			left = mergeSort(left);
			right = mergeSort(right);
			merge(left, right, whole);
		}
		return whole;
	}

	private void merge(List<String> left, List<String> right, List<String> whole) {
		int leftIndex = 0;
		int rightIndex = 0;
		int wholeIndex = 0;
		while (leftIndex < left.size() && rightIndex < right.size()) {
			if ((left.get(leftIndex).compareTo(right.get(rightIndex))) > 0) {
				whole.set(wholeIndex, left.get(leftIndex));
				leftIndex++;
			} else {
				whole.set(wholeIndex, right.get(rightIndex));
				rightIndex++;
			}
			wholeIndex++;
		}

		List<String> rest;
		int restIndex;
		if (leftIndex >= left.size()) {
			rest = right;
			restIndex = rightIndex;
		} else {
			rest = left;
			restIndex = leftIndex;
		}
		for (int i = restIndex; i < rest.size(); i++) {
			whole.set(wholeIndex, rest.get(i));
			wholeIndex++;
		}
	}
}
