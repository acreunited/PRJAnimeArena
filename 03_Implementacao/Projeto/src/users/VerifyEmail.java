package users;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import main.Connector;

/**
 * Servlet implementation class Login
 */
@WebServlet("/VerifyEmail")
public class VerifyEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VerifyEmail() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter(); 
		
		String action = request.getParameter("action");
		
		
		if (action.equalsIgnoreCase("validateEmail")) {
			String username = request.getParameter("username");
			pw.print( getPart1() );
			if (validate(username)) {
				
				pw.print("Your account is verified, you can start playing!");
				
				//session.setAttribute("email", "valid");
				//response.sendRedirect("email.jsp");
				
				insertDefaultCharacters(username);
			}
			else {
				pw.print("Your account is already verified!");
			}
			pw.print( getPart2() );
		}
		else if (action.equalsIgnoreCase("emailSent")) {
			
			String username = request.getParameter("username");
					
			pw.print( getPart1() );
			pw.print("Thank you for registering "+username+"! Only 1 step left!<br>\r\n" + 
					"A verification email was sent to you. Please validade it, "
					+ "in order to start playing.");
			pw.print( getPart2() );
		}
		//http://localhost:8080/AnimeArena/VerifyEmail?action=validateEmail&username=ranktest

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	
	private void insertDefaultCharacters(String username) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String lastID_query = "select * from USERS where username='"+username+"';";
			PreparedStatement stmt_id = conn.prepareStatement(lastID_query);
			ResultSet rs_id = stmt_id.executeQuery();
			int id = -1;
			if (rs_id.next()) {
				id = Integer.parseInt(rs_id.getString("userID"));
			}
			rs_id.close();
			
			if (id>=0) {
			
				//SELECT * 
				//FROM   CHARACTERS 
				//WHERE characterID NOT IN (SELECT characterID FROM MISSION);
				String getChars = "SELECT * FROM CHARACTERS WHERE characterID NOT IN (SELECT characterID FROM MISSION)";
				PreparedStatement stmt = conn.prepareStatement(getChars);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					int charID = rs.getInt("characterID");
					String insertChar = "insert into USER_CHARACTER ("
							+ "userID, characterID) "
							+ "values ((?),(?))";
					
					PreparedStatement stmt_char = conn.prepareStatement(insertChar);
					stmt_char.setInt(1, id);
					stmt_char.setInt(2, charID);
					stmt_char.executeUpdate();
				}
				rs.close();
				
				//USER_MISSION
				String getMissions = "SELECT * FROM MISSION";
				PreparedStatement stmt_mission = conn.prepareStatement(getMissions);
				ResultSet rs_mission = stmt_mission.executeQuery();
				while (rs_mission.next()) {
					int missionID = rs_mission.getInt("missionID");
					String insertMi = "insert into USER_MISSION ("
							+ "userID, missionID, completed) "
							+ "values ((?),(?),(?))";
					
					PreparedStatement stmt_insert = conn.prepareStatement(insertMi);
					stmt_insert.setInt(1, id);
					stmt_insert.setInt(2, missionID);
					stmt_insert.setBoolean(3, false);
					stmt_insert.executeUpdate();
				}
				rs_mission.close();
				
				
				//criar ficheiro xml missoes
				createMissionsXML(id);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}
	
	private void createMissionsXML(int id) {
		File source = new File("D:\\GitHub\\Projeto\\missions.xml");
		File dest = new File("D:\\GitHub\\Projeto\\missions"+id+".xml");

		  try {
			Files.copy(source.toPath(), dest.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getPart1() {
		return "<!DOCTYPE html>\r\n" + 
				"<html lang=\"en\">\r\n" + 
				"\r\n" + 
				"<head>\r\n" + 
				"\r\n" + 
				"    <meta charset=\"utf-8\">\r\n" + 
				"    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n" + 
				"    <meta name=\"description\" content=\"Anime-Arena\">\r\n" + 
				"\r\n" + 
				"    <title>Anime-Arena</title>\r\n" + 
				"    \r\n" + 
				"    <link href=\"extras/fontawesome-free/css/all.min.css\" rel=\"stylesheet\" type=\"text/css\">\r\n" + 
				"    <link href=\"https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i\" rel=\"stylesheet\">\r\n" + 
				"\r\n" + 
				"    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx\" crossorigin=\"anonymous\">\r\n" + 
				"    <link href=\"css/site.css\" rel=\"stylesheet\">\r\n" + 
				"    <link href=\"css/announcements.css\" rel=\"stylesheet\">\r\n" + 
				"    \r\n" + 
				"\r\n" + 
				"\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"\r\n" + 
				"    <nav class=\"navbar navbar-expand-lg navbar-dark bg-faded\">\r\n" + 
				"        <a class=\"navbar-brand \" href=\"#\"><img id=\"adjustLeft\" src=\"img/logo_small.png\" style=\"width:50%;\"></a>\r\n" + 
				"        <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarSupportedContent\" aria-controls=\"navbarSupportedContent\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\r\n" + 
				"            <span class=\"navbar-toggler-icon\"></span>\r\n" + 
				"        </button>\r\n" + 
				"\r\n" + 
				"        <div class=\"collapse navbar-collapse\" id=\"navbarSupportedContent\">\r\n" + 
				"            <ul class=\"navbar-nav mr-auto w-100 justify-content-center\">\r\n" + 
				"                <li class=\"nav-item active\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" href=\"index.jsp\">ANNOUNCEMENTS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" href=\"#\">LEADERBOARDS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" href=\"missions.jsp\">MISSIONS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" href=\"#\">GAME INFO</a>\r\n" + 
				"                </li>\r\n" + 
				"                \r\n" + 
				"            </ul>\r\n" + 
				"            <ul class=\"navbar-nav justify-content-end\" id=\"login\">\r\n" + 
				"				<li class=\"nav-item\" id=\"login\">\r\n" + 
				"					<a class=\"nav-link navBarItem\" href=\"login.jsp\">LOGIN</a>\r\n" + 
				"				</li>\r\n" + 
				"			</ul>\r\n" + 
				"			<ul class=\"navbar-nav justify-content-end\" id=\"register\">\r\n" + 
				"				<li class=\"nav-item\" id=\"register\">\r\n" + 
				"					<a class=\"nav-link navBarItem\" href=\"register.jsp\">REGISTER</a>\r\n" + 
				"				</li>\r\n" + 
				"			</ul>\r\n" + 
				"			\r\n" + 
				"			\r\n" + 
				"        </div>\r\n" + 
				"    </nav>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    <section id=\"play\">\r\n" + 
				"        <div class=\"text-center\">\r\n" + 
				"	        <a href=\"selection.jsp\"><img src=\"img/Buttons/play button.png\"></a>\r\n" + 
				"	        <br>\r\n" + 
				"	        <img src=\"img/logo_full.png\">\r\n" + 
				"	        <br>\r\n" + 
				"	        <p>YOUR #1 ONLINE ANIME STRATEGIC GAME</p>\r\n" + 
				"        </div>\r\n" + 
				"    </section>\r\n" + 
				"\r\n" + 
				"   <section id=\"state\">\r\n" + 
				"   \r\n" + 
				"		<div class=\"text-center\">";
	}
	
	private String getPart2() {
		return "</div>\r\n" + 
				"		\r\n" + 
				"   </section>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    <section id=\"footerFix\">\r\n" + 
				"\r\n" + 
				"        <ul class=\"list-inline\">\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\"><img src=\"img/Buttons/Paypal.png\"></a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\"><img src=\"img/Buttons/Discord.png\"></a></li>\r\n" + 
				"        </ul>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"        <ul class=\"list-inline aboveFooter\">\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\">PRIVACY NOTICE</a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\">TERMS OF SERVICE</a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\">COPYRIGHT</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"\r\n" + 
				"        <ul class=\"list-inline belowFooter\">\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\">Anime-Arena 2022</a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\">All rights Reserved</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"       \r\n" + 
				"    </section>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				" <!-- Bootstrap core JavaScript-->\r\n" + 
				"<script src=\"js/jquery.js\"></script>\r\n" + 
				"<script src=\"extras/bootstrap/js/bootstrap.bundle.min.js\"></script>\r\n" + 
				"<script src=\"js/jquery.easing.js\"></script>\r\n" + 
				"<script src=\"js/interface.js\"></script>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"</body>\r\n" + 
				"\r\n" + 
				"</html>";
	}
	
	private boolean validate(String username) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String names = "select userID, username from USERS;";
			PreparedStatement stmt_names = conn.prepareStatement(names);
			ResultSet rs_names = stmt_names.executeQuery();

			while (rs_names.next()) {
				if (rs_names.getString("username").equalsIgnoreCase(username)) {
					int userID = rs_names.getInt("userID");
					
					String query = "UPDATE USERS SET isActive=true where userID="+userID;
					PreparedStatement stmt = conn.prepareStatement(query);
					stmt.executeUpdate();
					return true;
				}
			}
			rs_names.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
		
		return false;
	}


}