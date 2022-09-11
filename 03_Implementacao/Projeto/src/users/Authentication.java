package users;

import java.io.IOException;
import java.io.PrintWriter;
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

import main.Connector;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Authentication")
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Authentication() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter(); 

		String action = request.getParameter("action");
		if (action.equalsIgnoreCase("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			login(session, response, pw, username, password);
		}
		else if (action.equalsIgnoreCase("verifyUsername")) {
			String username = request.getParameter("username");
			pw.write( userExists(pw, username) ? "true" : "false" );
		}
		else if (action.equalsIgnoreCase("register")) {
			
			String username = request.getParameter("inpUsername");
			String password = request.getParameter("inpPass");
			//String confirmPassword = request.getParameter("confirm");
			String email = request.getParameter("inpEmail");
			//if (password.equalsIgnoreCase(confirmPassword)) {
			register(pw, response, username, password, email);
			//}
			/*else {
				pw.write("passNotMatch");
				
			}*/
			String link = "http://localhost:8080/AnimeArena/VerifyEmail?action=validateEmail&username="+username;
			sendEmail(email, username, link);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private boolean sendEmail(String RECIPIENT, String username, String link) {
		 String from = "noreply.animearena";
		 String pass = "nmltdatpdxpzcvuh";
		 String[] to = {
		  RECIPIENT
		 }; // list of recipient email addresses
		 String subject = "Anime-Arena Verification";
		 Properties props = System.getProperties();
		 String host = "smtp.gmail.com";
		 props.put("mail.smtp.starttls.enable", "true");
		 props.put("mail.smtp.host", host);
		 props.put("mail.smtp.user", from);
		 props.put("mail.smtp.password", pass);
		 props.put("mail.smtp.port", "587");
		 props.put("mail.smtp.auth", "true");

		 Session session = Session.getDefaultInstance(props);
		 MimeMessage message = new MimeMessage(session);

		 try {
		  message.setFrom(new InternetAddress(from));
		  InternetAddress[] toAddress = new InternetAddress[to.length];

		  // To get the array of addresses
		  for (int i = 0; i < to.length; i++) {
		   toAddress[i] = new InternetAddress(to[i]);
		  }

		  for (int i = 0; i < toAddress.length; i++) {
		   message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		  }

		  message.setSubject(subject);
		  BodyPart messageBodyPart = new MimeBodyPart();
//		  String htmlText = "<div style=\" background-color: white;width: 25vw;height:auto;border: 20px solid grey;padding: 50px;margin:100 auto;\">\n" +
//		   "<h1 style=\"text-align: center;font-size:1.5vw\">" + title + "</h1>\n" + "<div align=\"center\">" +
//		   "<h2 style=\"text-align: center;font-size:1.0vw\">" + body + "</h2>" +
//
//		   "<h3 style=\"text-align: center;text-decoration: underline;text-decoration-color: red;font-size:0.9vw\">" +
//		   under_line_text + "</h3><br><h4 style=\"text-align: center;font-size:0.7vw\">" + end_text +
//		   " </h4></div>";
//		  messageBodyPart.setContent(htmlText, "text/html");
		  
		  String text = "Welcome "+username+"!\n";
		  text += "\nPlease click on the link below to activate your account:\n"+link;
		  text += "\n\n";
		  messageBodyPart.setContent(text, "text/plain");

		  Multipart multipart = new MimeMultipart();

		  // Set text message part
		  multipart.addBodyPart(messageBodyPart);
		  message.setContent(multipart);

		  Transport transport = session.getTransport("smtp");
		  transport.connect(host, from, pass);
		  transport.sendMessage(message, message.getAllRecipients());
		  transport.close();
		 } catch (AddressException ae) {
		  ae.printStackTrace();
		 } catch (MessagingException me) {
		  me.printStackTrace();
		 }
		 return true;
		}
	
	private boolean userExists(PrintWriter pw, String username) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String names = "select username from USERS;";
			PreparedStatement stmt_names = conn.prepareStatement(names);
			ResultSet rs_names = stmt_names.executeQuery();
			
			//boolean exists = false;
			while (rs_names.next()) {
				if (rs_names.getString("username").equalsIgnoreCase(username)) {
					pw.write("userExists");
					return true;
				}
			}
			rs_names.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void register(PrintWriter pw, HttpServletResponse response, String username, String password, String email) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String lastID_query = "select userID from USERS order by userID DESC LIMIT 1;";
			PreparedStatement stmt_id = conn.prepareStatement(lastID_query);
			ResultSet rs_id = stmt_id.executeQuery();
			int id = 0;
			if (rs_id.next()) {
				id = Integer.parseInt(rs_id.getString("userID"))+1;
			}
			
			rs_id.close();
		
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			Blob avatar = null;
			
			String query = "insert into USERS ("
					+ "userID, username, pass, registerDate, xp, avatar, email, nWins, nLosses, "
					+ "selectionBackground, battleBackground, streak, highestStreak, highestLevel, estado, isActive) "
					+ "values ((?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?),(?))";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			stmt.setString(2, username);
			stmt.setString(3, Connector.hashFromPass(password));
			stmt.setDate(4, sqlDate);
			stmt.setInt(5, 0);
			stmt.setBlob(6, avatar);
			stmt.setString(7, email);
			stmt.setInt(8, 0);
			stmt.setInt(9, 0);
			stmt.setBlob(10, avatar);
			stmt.setBlob(11, avatar);
			stmt.setInt(12, 0);
			stmt.setInt(13, 0);
			stmt.setInt(14, 1);
			stmt.setDate(15, sqlDate);
			stmt.setBoolean(16, false);
			stmt.executeUpdate();
			
			
			String insertMember = "insert into MEMBERS ("
					+ "membersID) "
					+ "values ((?))";
			
			PreparedStatement stmt_member = conn.prepareStatement(insertMember);
			stmt_member.setInt(1,  id);
			stmt_member.executeUpdate();

			//pw.write("success");
			try {
				response.sendRedirect("VerifyEmail?action=emailSent&username="+username);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			stmt.close();
			stmt_member.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}
	
	private void login(
			HttpSession session, HttpServletResponse response, PrintWriter pw,
			String username, String password) {
		
		PreparedStatement stmt = null;

		session.setAttribute("loggedIn", false);
		session.setAttribute("tipoUser", "guest");
		
		try {
			Class.forName(Connector.drv);

			Connection conn = Connector.getConnection();

			String query = String.format("select * FROM USERS where username='%s'", username); 
			
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				if (!rs.getBoolean("isActive")) {
					pw.write("notActive");
				}
				else if (isBanned(rs.getDate("estado"))) {
					pw.write("banned");
				}
				else if (Connector.hashFromPass(password).equals(rs.getString("pass"))) {
					session.setAttribute("username", username);
					session.setAttribute("loggedIn", true);

					int id = rs.getInt("userID");
					session.setAttribute("userID", id);
					
					session.setAttribute("currentTheme", rs.getInt("currentTheme"));

					query = String.format("select * from ADMINISTRATOR where administratorID='%d'", id);
					rs = conn.prepareStatement(query).executeQuery();
					if (rs.next()) { // id único, só pode ter 1
						session.setAttribute("tipoUser", "administrador");
						pw.write("valid");
					} else {
						pw.write("valid");
						session.setAttribute("tipoUser", "player");
					}

				} 
				else {
					pw.write("notMatch");
				}
				
				
			} else {
				for (int i = 0; i < 5; i++) {
					System.out.printf("USERNAME %s nao foi encontrada\n", username);
				}
				session.setAttribute("tipoUser", "guest");
				//response.sendRedirect("login.jsp");
				pw.write("notMatch");
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}
	
	private boolean isBanned(Date estado) {
		
		Date currentDate = new Date();
		return estado.after(currentDate);
	}

}