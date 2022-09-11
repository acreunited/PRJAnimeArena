package game;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/Timer")
public class Timer extends HttpServlet {
	
	private static final long serialVersionUID = 7215979604673189309L;
	
	public Timer() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		//setting the content type  
		PrintWriter pw = response.getWriter(); 

		HttpSession session = request.getSession();
		
		String action = request.getParameter("action");
		
		if (action.equalsIgnoreCase("reduceTimer")) {
			
			if (session.getAttribute("turnTime")!=null) {
				session.setAttribute("turnTime", (int)session.getAttribute("turnTime")-3);
				pw.write("<div class='mc_bar_fill' id='timeBar' style='width: "+session.getAttribute("turnTime")+"px;'></div>");
				
			}
			
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void writeNewTime(PrintWriter pw, float currentTime) {
		pw.write("<div class='mc_bar_fill' id='timeBar' style='width: "+(currentTime-3.18)+"px;'></div>");
	}
	
}
