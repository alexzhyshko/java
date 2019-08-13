package com.example.demo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;


@RestController
public class App {
	
	@Autowired
	JdbcTemplate connection;
	
	
	@Autowired
	Model model;

	List<SeanceObject> seanceCache = new ArrayList<>();
	boolean daemonExists = false;
			
	@GetMapping("loadhall")
	public String loadhall(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		// hall seat status in array:(0 - free, 1 - booked, 2 - bought)
		
		String txt = req.getSession().getAttribute("seanse").toString();
		
		
	
		int[][] response = model.getHallStatus(Integer.parseInt(txt));
		
		
		
		String responseStr = model.getPrice(Integer.parseInt(txt))+"|";
		for (int[] row : response) {
			responseStr += "";
			for (int num : row) {
				responseStr += num + ",";
			}
			responseStr = responseStr.substring(0, responseStr.length() - 1);
			responseStr += ";";
		}
		responseStr = responseStr.substring(0, responseStr.length() - 1);
		responseStr += "";
		return responseStr;
	}
	
	
	@RequestMapping(path="/loadindex", method=RequestMethod.GET)
	public String loadindex(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		String response = "";
		List<String> soon = model.getSoonFilms();
		for (String str : soon) {
			response += str + ",";
		}
		response = response.substring(0, response.length() - 1);
		response += ";";
		List<String> offers = model.getOffers();
		for (String str : offers) {
			response += str + ",";
		}
		response = response.substring(0, response.length() - 1);
		response += ";";
		
		
		
		if(!daemonExists) {
		Thread cleaner = new Thread(()->{
			daemonExists = true;
			while(true) {
			seanceCache = connection.query("SELECT * FROM "+Tables.seancesTable()+" WHERE cleaned=0", (rs, rowNum)->new SeanceObject(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
			for(SeanceObject seance : seanceCache) {
				if(LocalDate.parse(seance.date).equals(LocalDate.now())&&LocalTime.parse(seance.time).isAfter(LocalTime.now())&&LocalTime.parse(seance.time).isBefore(LocalTime.now().plus(30,ChronoUnit.MINUTES))) {
					List<Integer> ids = connection.query("SELECT id FROM "+Tables.bookingTable()+" WHERE status=1 AND seansid=?", new Object[] {seance.id}, (rs,rowNum)->rs.getInt(1));
					connection.update("UPDATE "+Tables.bookingTable()+" SET status=0 WHERE seansid=? AND status=1", new Object[] {seance.id});
					for(int id : ids) {
						connection.update("DELETE FROM "+Tables.bookingsTable()+" WHERE bookingid=?", new Object[] {id});
					}
					connection.update("UPDATE "+Tables.seancesTable()+" SET cleaned=1 WHERE id=?", new Object[] {seance.id});
				}
			}
			try {
			Thread.sleep(1000*60*1);//1 minutes
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			}
		});
		cleaner.setDaemon(true);
		cleaner.start();
		}
		
		return response;
	}
	@GetMapping("loadtoday")
	public String loadtoday(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		String response = "";
		List<String> today = model.getToday();
		if(today.isEmpty()) {
			return "";
		}else {
			for (String str : today) {
				response += str + ";";
			}
			response = response.substring(0, response.length() - 1);
		}
		return response;
	}
	@GetMapping("gettitle")
	public String getTitle(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		return req.getSession().getAttribute("title").toString();
	}
	@GetMapping("logout")
	public String logout(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		req.getSession().setAttribute("username", null);
		return "";
	}
	@GetMapping("loaduser")
	public String loaduser(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		if(req.getSession().getAttribute("username")==null) {
			res.setStatus(400);
			return "login.html";
		}else {
			res.setStatus(200);
			List<String> tickets = model.getTickets(req.getSession().getAttribute("username").toString());
			String response = req.getSession().getAttribute("username").toString()+";";
			for(String str : tickets) {
				response+=str+"_";
			}
			response = response.substring(0,response.length()-1);
			return response;
			
		}
	}
	@GetMapping("loadfilms")
	public String loadfilms(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		String result = "";
		List<String> list = model.getAllFilms();
		if(!list.isEmpty()) {
			for(String str:list) {
			result+=str+";";
			}
		result = result.substring(0,result.length()-1);
		}
		return result;
	}
	
	@PostMapping("loadseanse")
	public String loadseanse(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		try {
		String param = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		res.setStatus(300);
		req.getSession().setAttribute("seanse", ""+model.getSeanseIdForToday(param.split(",")[0],LocalDate.now().toString(), param.split(",")[1], param.split(",")[2])+"");
		req.getSession().setAttribute("title", param.split(",")[0]+" ("+param.split(",")[1]+", \""+param.split(",")[2]+"\" Hall)");
		return "hall.html";
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	@PostMapping("bui")
	public String bui(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		if(req.getSession().getAttribute("username")==null) {
			res.setStatus(300);
			return "login.html";
		}
		try {
		String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		res.setStatus(300);
		
		
		String places = body.split(";")[0];
		String data = body.split(";")[1];
		
		
		Arrays.asList(data).forEach(System.out::println);
		String title = data.split(",")[0];
		String time = data.split(",")[2];
		String date = data.split(",")[1];
		String hall = data.split(",")[3];
		
		
		date+="."+LocalDate.now().getYear();
		model.buy(req.getSession().getAttribute("username").toString(), places, title, date, time, hall);
		return "index.html";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("book")
	public String book(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		boolean result=false;
		try {
			String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			result = model.book(req.getSession().getAttribute("username").toString(), body, Integer.parseInt(req.getSession().getAttribute("seanse").toString()));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		res.setStatus(result?300:400);
		return result?"user.html":"hall.html";
	}
	
	
	@PostMapping("login")
	public String login(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		try {
		String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		String username = body.split(",")[0];
		String password = body.split(",")[1];
		boolean bol = model.login(username,password);
		res.setStatus(bol?200:400);
		String result=bol?"user.html":"Incorrect login or password";
		if(bol) {
			req.getSession().setAttribute("username", username);
		}
		return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("register")
	public String register(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		try {
		String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		String username = body.split(",")[0];
		String email = body.split(",")[1];
		String password = body.split(",")[2];
		boolean bol = model.register(username, email,password);
		res.setStatus(bol?200:400);
		String result=bol?"login.html":"User already exists";
		return result;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("loadhallfilms")
	public String loadhallfilms(HttpServletRequest req, HttpServletResponse res) {
		res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		try {
		String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		String title = body.split(",")[0];
		String date = body.split(",")[1];
		String time = body.split(",")[2];
		String hall = body.split(",")[3];
		
		
		req.getSession().setAttribute("seanse", "");
		req.getSession().setAttribute("seanse", ""+model.getSeanseIdForFilms(title,date,time, hall)+"");
		req.getSession().setAttribute("title", title+" ("+time+", \""+hall+"\" Hall)");
		
		return "hall.html";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

//	
//	
//	public static void main(String[] args) {
//		
//		
		
//		get("/loadhall", (req, res) -> {
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			// hall seat status in array:(0 - free, 1 - booked, 2 - bought)
//			String txt = req.session().attribute("seanse").toString();
//			int[][] response = model.getHallStatus(Integer.parseInt(txt));
//			String responseStr = "";
//			for (int[] row : response) {
//				responseStr += "";
//				for (int num : row) {
//					responseStr += num + ",";
//				}
//				responseStr = responseStr.substring(0, responseStr.length() - 1);
//				responseStr += ";";
//			}
//			responseStr = responseStr.substring(0, responseStr.length() - 1);
//			responseStr += "";
//			return responseStr;
//		});
//		get("/loadindex", (req, res) -> {
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			String response = "";
//			List<String> soon = model.getSoonFilms();
//			for (String str : soon) {
//				response += str + ",";
//			}
//			response = response.substring(0, response.length() - 1);
//			response += ";";
//			List<String> offers = model.getOffers();
//			for (String str : offers) {
//				response += str + ",";
//			}
//			response = response.substring(0, response.length() - 1);
//			response += ";";
//			return response;
//		});

//		get("/loadtoday", (req, res) -> {
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			String response = "";
//			List<String> today = model.getToday();
//			if(today.isEmpty()) {
//				return "";
//			}else {
//				for (String str : today) {
//					response += str + ";";
//				}
//				response = response.substring(0, response.length() - 1);
//			}
//			return response;
//			
//
//		});
//		post("/loadseanse", (req, res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			String param = req.body();
//			res.status(300);
//			req.session().raw().setAttribute("seanse", ""+model.getSeanseIdForToday(param.split(",")[0],LocalDate.now().toString(), param.split(",")[1], param.split(",")[2])+"");
//			Object txt = req.session().raw().getAttribute("seanse");
//			req.session().raw().setAttribute("title", param.split(",")[0]+" ("+param.split(",")[1]+", \""+param.split(",")[2]+"\" Hall)");
//			return "hall.html";
//		});
		
//		get("/gettitle", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			return req.session().attribute("title");
//		});
//		post("/bui", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			if(req.session().raw().getAttribute("username")==null) {
//				res.status(300);
//				return "login.html";
//			}
//			String body = req.body();
//			res.status(300);
//			
//			model.buy(req.session().raw().getAttribute("username").toString(), body, Integer.parseInt(req.session().raw().getAttribute("seanse").toString()));
//			return "index.html";
//		});
		
//		post("/login", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			String body = req.body();
//			String username = body.split(",")[0];
//			String password = body.split(",")[1];
//			boolean bol = model.login(username,password);
//			res.status(bol?200:400);
//			String result=bol?"user.html":"Incorrect login or password";
//			if(bol) {
//				req.session().raw().setAttribute("username", username);
//			}
//			return result;
//		});
//		post("/register", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			String body = req.body();
//			String username = body.split(",")[0];
//			String email = body.split(",")[1];
//			String password = body.split(",")[2];
//			boolean bol = model.register(username, email,password);
//			res.status(bol?200:400);
//			String result=bol?"login.html":"User already exists";
//			return result;
//		});
//		get("/logout", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			req.session().raw().setAttribute("username", null);
//			return "";
//		});
//		get("/loaduser", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			if(req.session().raw().getAttribute("username")==null) {
//				res.status(400);
//				return "login.html";
//			}else {
//				res.status(200);
//				List<String> tickets = model.getTickets(req.session().raw().getAttribute("username").toString());
//				String response = req.session().raw().getAttribute("username").toString()+";";
//				for(String str : tickets) {
//					response+=str+"_";
//				}
//				response = response.substring(0,response.length()-1);
//				return response;
//				
//			}
//			
//		});
//		get("/loadfilms", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			String result = "";
//			List<String> list = model.getAllFilms();
//			if(!list.isEmpty()) {
//				for(String str:list) {
//				result+=str+";";
//				}
//			result = result.substring(0,result.length()-1);
//			}
//			return result;
//		});
//		post("/loadhallfilms", (req,res)->{
//			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
//			res.header("Access-Control-Allow-Credentials", "true");
//			String body = req.body();
//			
//			String title = body.split(",")[0];
//			String date = body.split(",")[1];
//			String time = body.split(",")[2];
//			String hall = body.split(",")[3];
//			
//			
//			req.session().attribute("seanse", "");
//			req.session().attribute("seanse", ""+model.getSeanseIdForFilms(title,date,time, hall)+"");
//			Object txt = req.session().attribute("seanse");
//			req.session().attribute("title", title+" ("+time+", \""+hall+"\" Hall)");
//			
//			return "hall.html";
//		});
		
		
	//}
}
