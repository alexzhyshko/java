package com.zhyshko.jsasynctest;

import static spark.Spark.get;
import static spark.Spark.post;

import java.time.LocalDate;
import java.util.List;

public class App {
	static Model model = new Model("jdbc:mysql://localhost:3306/kino", "root", "root");

	public static void main(String[] args) {
		get("/loadhall", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			// hall seat status in array:(0 - free, 1 - booked, 2 - bought)
			String txt = req.session().attribute("seanse").toString();
			int[][] response = model.getHallStatus(Integer.parseInt(txt));
			String responseStr = "";
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
		});
		get("/loadindex", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
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
			return response;
		});

		get("/loadtoday", (req, res) -> {
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
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
			

		});
		post("/loadseanse", (req, res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			String param = req.body();
			res.status(300);
			req.session().raw().setAttribute("seanse", ""+model.getSeanseIdForToday(param.split(",")[0],LocalDate.now().toString(), param.split(",")[1], param.split(",")[2])+"");
			Object txt = req.session().raw().getAttribute("seanse");
			req.session().raw().setAttribute("title", param.split(",")[0]+" ("+param.split(",")[1]+", \""+param.split(",")[2]+"\" Hall)");
			return "hall.html";
		});
		
		get("/gettitle", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			return req.session().attribute("title");
		});
		post("/bui", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			if(req.session().raw().getAttribute("username")==null) {
				res.status(300);
				return "login.html";
			}
			String body = req.body();
			res.status(300);
			
			model.buy(req.session().raw().getAttribute("username").toString(), body, Integer.parseInt(req.session().raw().getAttribute("seanse").toString()));
			return "index.html";
		});
		
		post("/login", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			String body = req.body();
			String username = body.split(",")[0];
			String password = body.split(",")[1];
			boolean bol = model.login(username,password);
			res.status(bol?200:400);
			String result=bol?"user.html":"Incorrect login or password";
			if(bol) {
				req.session().raw().setAttribute("username", username);
			}
			return result;
		});
		post("/register", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			String body = req.body();
			String username = body.split(",")[0];
			String email = body.split(",")[1];
			String password = body.split(",")[2];
			boolean bol = model.register(username, email,password);
			res.status(bol?200:400);
			String result=bol?"login.html":"User already exists";
			return result;
		});
		get("/logout", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			req.session().raw().setAttribute("username", null);
			return "";
		});
		get("/loaduser", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			if(req.session().raw().getAttribute("username")==null) {
				res.status(400);
				return "login.html";
			}else {
				res.status(200);
				List<String> tickets = model.getTickets(req.session().raw().getAttribute("username").toString());
				String response = req.session().raw().getAttribute("username").toString()+";";
				for(String str : tickets) {
					response+=str+"_";
				}
				response = response.substring(0,response.length()-1);
				return response;
				
			}
			
		});
		get("/loadfilms", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			String result = "";
			List<String> list = model.getAllFilms();
			if(!list.isEmpty()) {
				for(String str:list) {
				result+=str+";";
				}
			result = result.substring(0,result.length()-1);
			}
			return result;
		});
		post("/loadhallfilms", (req,res)->{
			res.header("Access-Control-Allow-Origin", "http://127.0.0.1:3000");
			res.header("Access-Control-Allow-Credentials", "true");
			String body = req.body();
			
			String title = body.split(",")[0];
			String date = body.split(",")[1];
			String time = body.split(",")[2];
			String hall = body.split(",")[3];
			
			
			req.session().attribute("seanse", "");
			req.session().attribute("seanse", ""+model.getSeanseIdForFilms(title,date,time, hall)+"");
			Object txt = req.session().attribute("seanse");
			req.session().attribute("title", title+" ("+time+", \""+hall+"\" Hall)");
			
			return "hall.html";
		});
		
		
	}
}
