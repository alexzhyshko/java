package pkg;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin_controller")
public class admin_controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Model model;

	public admin_controller() {
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
		if (this.model.checkStatus()) {
			String name = request.getParameter("goodname");
			int price = Integer.parseInt(request.getParameter("goodprice"));
			String characteristics = request.getParameter("goodcharacteristics");
			String category = request.getParameter("goodcategory");
			if (!name.trim().equals("") && !characteristics.trim().equals("") && !category.trim().equals("")
					&& price > 0) {
				model.addGood(name, price, characteristics, category);
			}

		}
		try {
			response.sendRedirect("admin.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
