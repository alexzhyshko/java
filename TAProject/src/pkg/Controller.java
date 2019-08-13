package pkg;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Model model;

	public Controller() {
		this.model = new Model();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) {
		}

		String searchQuery = request.getParameter("search_query");
		String category = request.getParameter("category");
		String button = request.getParameter("search_button");
		System.out.println("Category: "+category);
		System.out.println("Search word: "+searchQuery);
		if(category==null) {
			if(!searchQuery.trim().equals("")) {
				if(model.checkStatus()) {
					System.out.println("DB alive");
					try {
						List<String> resultList = model.searchByWord(searchQuery);
						request.getSession().setAttribute("result", resultList);
						}catch(Error e) {
							e.printStackTrace();
							try {
								response.sendRedirect("index.html");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							return;
							
						}
						
						try {
							response.sendRedirect("resultPage.jsp");

						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("Couldn't send result list to result page");

						}
				}
				
				
			}else {
				try {
					response.sendRedirect("index.html");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		 else {
				try {
				List<String> resultList = model.getCategory(category);
				request.getSession().setAttribute("result", resultList);
				}catch(Error e) {
					e.printStackTrace();
					try {
						response.sendRedirect("index.html");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				try {
					response.sendRedirect("resultPage.jsp");

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Couldn't send result list to result page");

				}
				
			}

	}
}
