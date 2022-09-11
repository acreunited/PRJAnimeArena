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
@WebServlet("/Language")
public class Language extends HttpServlet {
	private static final long serialVersionUID = 7215979604673189309L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Language() {
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
		int userID = (int) session.getAttribute("userID");
		if (action.equalsIgnoreCase("portuguese")) {
			setCurrentTheme(userID, 2, session);
		}
		else if (action.equalsIgnoreCase("english")) {
			setCurrentTheme(userID, 1, session);
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
	
	private void setCurrentTheme(int userID, int themeID, HttpSession session) {
		try {
			Connection con = Connector.getConnection();

			PreparedStatement pstmt =con.prepareStatement("UPDATE USERS SET currentTheme =? WHERE userID=?"); 
		             pstmt.setInt(1, themeID);
		             pstmt.setInt(2, userID);
			
		     pstmt.executeUpdate();  
	
			con.close();
			
			session.setAttribute("currentTheme", themeID);
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	
}
