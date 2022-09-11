package users;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DB.InsertIntoBD;
import main.Connector;

/**
 * Servlet implementation class Register
 */
@WebServlet("/UpdateProfile")
public class UpdateProfile extends HttpServlet {
	private static final long serialVersionUID = 7215979604673189309L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateProfile() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		//response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		
		String action = (String) request.getParameter("action");
		//int userID = (int) session.getAttribute("userID");
		if (action.equalsIgnoreCase("changeUser")) {
			String siteRank = request.getParameter("siteRank");
			String state = request.getParameter("state");
			String username = request.getParameter("user");
			
			updateSiteRank(username, siteRank);
			if (state.length()>0) {
				updateState(username, state);
			}
			response.sendRedirect("ViewProfile?username="+username);
		}
	
		
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void updateState(String username, String state) {

		System.out.println(state);
		SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");  
		Date date = null;
		try {
			date = formatter1.parse(state);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		System.out.println(date.toString());
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		updateState(sqlDate, username);
		
		
	}
	
	private void updateState(java.sql.Date sqlDate, String username) {
		try {
			Connection con = Connector.getConnection();

			PreparedStatement pstmt = con.prepareStatement("UPDATE USERS SET estado =? WHERE username=?"); 
		             pstmt.setDate(1, sqlDate);
		             pstmt.setString(2, username);
			
		     pstmt.executeUpdate(); 
			 con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void updateSiteRank(String username, String siteRank) {
		int userID = UserInfo.getPlayerID(username);
		if (siteRank.equalsIgnoreCase("admin") && !UserInfo.isAdmin(userID)) {
			insertAdmin(userID);
		}
		else if (siteRank.equalsIgnoreCase("player") && UserInfo.isAdmin(userID)) {
			removeAdmin(userID);
		}
	}
	
	
	private void removeAdmin(int userID) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			String query = "delete from ADMINISTRATOR where administratorID = ?";
		    PreparedStatement preparedStmt = conn.prepareStatement(query);
		    preparedStmt.setInt(1, userID);
		    preparedStmt.execute();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}
	
	private void insertAdmin(int userID) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			String query = "insert into ADMINISTRATOR ("
					+ "administratorID) "
					+ "values ((?))";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, userID);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}


	
}
