package users;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import main.Connector;

/**
 * Servlet implementation class Login
 */


@WebServlet("/Settings") 
@MultipartConfig(maxFileSize = 134217728)
public class Settings extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Settings() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter(); 
		int userID = (int) session.getAttribute("userID");
		
		String action = request.getParameter("action");
		if (action.equalsIgnoreCase("verifyOldPass")) {
			response.setContentType("text/plain");
			String password = request.getParameter("pass");
			pw.write( passwordIsSame(userID, password) ? "true" : "false" );
		}
		else if (action.equalsIgnoreCase("updatePassword")) {
			response.setContentType("text/html");
			String newPass = request.getParameter("inpPass");
			updatePassword(userID, newPass);
			pw.print( getPart1(userID, "changePassword") );
			pw.print(getPartPasswordUpdated());
			pw.print(getPart2(session, "changePassword"));
		}
		else if (action.equalsIgnoreCase("changePassword")) {
			response.setContentType("text/html");

			pw.print(getPart1(userID, "changePassword"));
			pw.print(getPartPassword());
			pw.print(getPart2(session, "changePassword"));
		}
		else if (action.equalsIgnoreCase("verifyOldEmail")) {
			response.setContentType("text/plain");
			String email = request.getParameter("email");
			pw.write( emailIsSame(userID, email) ? "true" : "false" );
		}
		else if (action.equalsIgnoreCase("updateEmail")) {
			response.setContentType("text/html");
			String email = request.getParameter("inpNewEmail");
			updateEmail(userID, email);
			
			pw.print(getPart1(userID, "changeMail"));
			pw.print(getPartEmailUpdated());
			pw.print(getPart2(session, "changeMail"));
		}
		else if (action.equalsIgnoreCase("changeEmail")) {
			response.setContentType("text/html");
			pw.print(getPart1(userID, "changeMail"));
			pw.print(getPartEmail());
			pw.print(getPart2(session, "changeMail"));
		}
		else if (action.equalsIgnoreCase("changeAvatar")) {
			response.setContentType("text/html");
			pw.print(getPart1(userID, "changeAvatar"));
			pw.print(getPartAvatar());
			pw.print(getPart2(session, "changeAvatar"));
		}
		else if (action.equalsIgnoreCase("changeBack")) {
			response.setContentType("text/html");
			pw.print(getPart1(userID, "changeBack"));
			pw.print(getPartBack());
			pw.print(getPart2(session, "changeBack"));
		}
		else if (action.equalsIgnoreCase("updateBack")) {
			response.setContentType("text/html");
			
			Part selection = request.getPart("selectionBackground");
			Part battle = request.getPart("battleBackground");
			if (updateBack(
					(selection.getSize()>0) ? selection.getInputStream() : null, (battle.getSize()>0) ? battle.getInputStream() : null, userID
				)) {
				pw.print(getPart1(userID, "changeBack"));
				pw.print(getPartBackSuccess());
				pw.print(getPart2(session, "changeBack"));
			}
			else {
				pw.print(getPart1(userID, "changeBack"));
				pw.print(getPartBackError());
				pw.print(getPart2(session, "changeBack"));
			}	

			
		}
		else if (action.equalsIgnoreCase("updateAvatar")) {
			response.setContentType("text/html");

			Part ilustracao = request.getPart("avatar");
			InputStream input = null;
			if (ilustracao != null && ilustracao.getSize() != 0) {
				input = ilustracao.getInputStream();
				if (updateAvatar(input, userID)) {
					pw.print(getPart1(userID, "changeAvatar"));
					pw.print(getPartAvatarSuccess());
					pw.print(getPart2(session, "changeAvatar"));
				}
				else {
					pw.print(getPart1(userID, "changeAvatar"));
					pw.print(getPartAvatarError());
					pw.print(getPart2(session, "changeAvatar"));
				}
			}
	
		}
	}
	
	private boolean updateBack(InputStream selectionBackground, InputStream battleBackground, int userID) {
		try {
			Connection con = Connector.getConnection();
			
			if (selectionBackground!=null) {
				PreparedStatement pstmt = con.prepareStatement("UPDATE USERS SET selectionBackground =? WHERE userID=?"); 
	            pstmt.setBinaryStream(1, selectionBackground);
	            pstmt.setInt(2, userID);
	            pstmt.executeUpdate();  
	            selectionBackground.close();
			}
			if (battleBackground!=null) {
				PreparedStatement pstmt = con.prepareStatement("UPDATE USERS SET battleBackground =? WHERE userID=?"); 
	            pstmt.setBinaryStream(1, battleBackground);
	            pstmt.setInt(2, userID);
	            pstmt.executeUpdate();  
	            battleBackground.close();
			}
			
			return true;
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}	
		
		return false;
	
	}

	
	private boolean updateAvatar(InputStream input, int userID) {
		try {
			Connection con = Connector.getConnection();
			PreparedStatement pstmt = con.prepareStatement("UPDATE USERS SET avatar =? WHERE userID=?"); 
            pstmt.setBinaryStream(1, input);
            pstmt.setInt(2, userID);
	
            pstmt.executeUpdate();  
			input.close();
			return true;
			
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}	
		return false;
	}

	private boolean emailIsSame(int userID, String email) {

		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String query = "select * from USERS where userID="+userID;
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
		
			if (rs.next()) {
				return email.equalsIgnoreCase( rs.getString("email") );
			}
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
		
		return false;
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
	
	private void updateEmail(int userID, String email) {
		try {
			Connection con = Connector.getConnection();

			PreparedStatement pstmt = con.prepareStatement("UPDATE USERS SET email =? WHERE userID=?"); 
		             pstmt.setString(1, email);
		             pstmt.setInt(2, userID);
			
		     pstmt.executeUpdate();  
	
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private String getPartEmailUpdated() {
		
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" + 
				"    		<div id=\"mailUpdated\" class=\"welcome-title\">\r\n" + 
				"    			E-mail Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\">\r\n" + 
				"		  				<input id=\"inpEmail\" type=\"email\" name=\"inpEmail\" placeholder=\"Current e-mail address...\" required><br>" + 
				"		  				<input id=\"inpNewEmail\" type=\"email\" name=\"inpNewEmail\" placeholder=\"New e-mail address...\" required><br>" + 
				"			  			<input type=\"hidden\" name=\"action\" value=\"updateEmail\">\r\n" + 
				"						<div id=\"errorEmailIncorrect\"><span>Current e-mail is not correct</span></div>\r\n" + 
				"						<div id=\"patternTitleEmail\"><span>E-mail is not valid</span></div>\r\n" + 
				"						<!-- <input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> -->\r\n" + 
				"						<a onclick=\"correctEmail()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
		
		
	}
	private String getPartPasswordUpdated() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" + 
				"    		<div id=\"passwordUpdated\" class=\"welcome-title\">\r\n" + 
				"    			Password Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\">\r\n" + 
				"		  				<input id=\"inpOld\" type=\"password\" name=\"inpOld\" placeholder=\"Current password...\" required><br>\r\n" + 
				"		  				<input id=\"inpPass\" type=\"password\" name=\"inpPass\" placeholder=\"New password...\" \r\n" + 
				"			  				pattern=\"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\" \r\n" + 
				"			  				title=\"Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters\"\r\n" + 
				"			  				required><br>\r\n" + 
				"						<input id=\"inpConfirmPass\" type=\"password\" name=\"inpConfirmPass\" placeholder=\"Confirm new password...\"\r\n" + 
				"							pattern=\"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\" \r\n" + 
				"			  				title=\"Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters\"\r\n" + 
				"			  				required><br>\r\n" + 
				"			  			<input type=\"hidden\" name=\"action\" value=\"updatePassword\">\r\n" + 
				"						<div id=\"errorPassIncorrect\"><span>Current password is not correct</span></div>\r\n" + 
				"						<div id=\"errorPassNotMatch\"><span>Passwords do not match</span></div>\r\n" + 
				"						<!-- <input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> -->\r\n" + 
				"						<a onclick=\"correctPassword()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPartAvatarSuccess() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" +
				"    		<div id=\"avatarUploaded\" class=\"welcome-title\">\r\n" + 
				"    			Avatar Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\" enctype='multipart/form-data' method=\"POST\">\r\n" + 
				"						<label for=\"avatar\">Change avatar:</label><br>\r\n" + 
				"					<input type=\"file\" accept=\"image/*\" name=\"avatar\" required /><br>"+
				"  						<!--<input type=\"submit\">-->"+
				"			  			<input type=\"hidden\" name=\"action\" value=\"updateAvatar\">\r\n" + 
				"						<input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> \r\n" + 
				"						<!--<a onclick=\"correctEmail()\"><img src=\"img/login_register_icons/register.png\"></a><br><br>-->\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPartAvatarError() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" +
				"    		<div id=\"avatarUploaded\" style=\"display:none;\" class=\"welcome-title\">\r\n" + 
				"    			Avatar Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\" enctype='multipart/form-data' method=\"POST\">\r\n" + 
				"						<label for=\"avatar\">Avatar:</label><br>\r\n" + 
				"					<input type=\"file\" accept=\"image/*\" name=\"avatar\" required /><br>"+
				"  						<!--<input type=\"submit\">-->"+
				"			  			<input type=\"hidden\" name=\"action\" value=\"updateAvatar\">\r\n" + 
				"						<div id=\"errorAvatar\"><span>Error uploading avatar</span></div>\r\n" + 
				"						<input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> \r\n" + 
				"						<!--<a onclick=\"correctEmail()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>-->\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPartAvatar() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" +
				"    		<div id=\"avatarUploaded\" style=\"display:none;\" class=\"welcome-title\">\r\n" + 
				"    			Avatar Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\" enctype='multipart/form-data' method=\"POST\">\r\n" + 
				"						<label for=\"avatar\">Avatar:</label><br>\r\n" + 
				"					<input type=\"file\" accept=\"image/*\" name=\"avatar\" required />"+
				"  						<!--<input type=\"submit\">-->"+
				"			  			<input type=\"hidden\" name=\"action\" value=\"updateAvatar\"><br>\r\n" + 
				"						<div id=\"errorAvatar\" style=\"display:none;\"><span>Error uploading avatar</span></div>\r\n" + 
				"						<input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> \r\n" + 
				"						<!--<a onclick=\"correctEmail()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>-->\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPartBackSuccess() {
		
		return "	<section id=\"settings\">\r\n" + 
		"		<div class=\"text-center\">\r\n" + 
		"    	\r\n" +
		"    		<div id=\"backUpdated\" class=\"welcome-title\">\r\n" + 
		"    			Background Updated!\r\n" + 
		"    		</div>\r\n" + 
		"	    	\r\n" + 
		"	    	\r\n" + 
		"	    	<div class=\"col\">\r\n" + 
		"	    		<div class=\"group-form\">\r\n" + 
		"	    			<div class=\"blackBackground\">.</div>\r\n" + 
		"	    			<form  id=\"form\" action=\"Settings\" enctype='multipart/form-data' method=\"POST\">\r\n" + 
		"					<label for=\"selectionBackground\">Selection background:</label><br>\r\n" + 
		"					<input type=\"file\" accept=\"image/*\" name=\"selectionBackground\" /><br><br>"+
		"					<label for=\"battleBackground\">Battle background:</label><br>\r\n" + 
		"					<input type=\"file\" accept=\"image/*\" name=\"battleBackground\" />"+
		"  						<!--<input type=\"submit\">-->"+
		"			  			<input type=\"hidden\" name=\"action\" value=\"updateBack\"><br>\r\n" + 
		"						<div id=\"errorAvatar\" style=\"display:none;\"><span>Error uploading image</span></div>\r\n" + 
		"						<input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> \r\n" + 
		"						<!--<a onclick=\"correctEmail()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>-->\r\n" + 
		"					</form>\r\n" + 
		"					\r\n" + 
		"				</div>\r\n" + 
		"    		</div>\r\n" + 
		"    	</div>\r\n" + 
		"	</section>";
	}
	
	private String getPartBack() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" +
				"    		<div id=\"backUpdated\" style=\"display:none;\" class=\"welcome-title\">\r\n" + 
				"    			Background Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\" enctype='multipart/form-data' method=\"POST\">\r\n" + 
				"					<label for=\"selectionBackground\">Change selection background:</label><br>\r\n" + 
				"					<input type=\"file\" accept=\"image/*\" name=\"selectionBackground\" /><br><br>"+
				"					<label for=\"battleBackground\">Change battle background:</label><br>\r\n" + 
				"					<input type=\"file\" accept=\"image/*\" name=\"battleBackground\" />"+
				"  						<!--<input type=\"submit\">-->"+
				"			  			<input type=\"hidden\" name=\"action\" value=\"updateBack\"><br>\r\n" + 
				"						<div id=\"errorAvatar\" style=\"display:none;\"><span>Error uploading image</span></div>\r\n" + 
				"						<input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> \r\n" + 
				"						<!--<a onclick=\"correctEmail()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>-->\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPartBackError() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" +
				"    		<div id=\"backUpdated\" style=\"display:none;\" class=\"welcome-title\">\r\n" + 
				"    			Background Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\" enctype='multipart/form-data' method=\"POST\">\r\n" + 
				"					<label for=\"selectionBackground\">Change selection background:</label><br>\r\n" + 
				"					<input type=\"file\" accept=\"image/*\" name=\"selectionBackground\" /><br><br>"+
				"					<label for=\"battleBackground\">Change battle background:</label><br>\r\n" + 
				"					<input type=\"file\" accept=\"image/*\" name=\"battleBackground\" />"+
				"  						<!--<input type=\"submit\">-->"+
				"			  			<input type=\"hidden\" name=\"action\" value=\"updateBack\"><br>\r\n" + 
				"						<div id=\"errorAvatar\"><span>Error uploading image</span></div>\r\n" + 
				"						<input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> \r\n" + 
				"						<!--<a onclick=\"correctEmail()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>-->\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPartEmail() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" +
				"    		<div id=\"mailUpdated\" style=\"display:none;\" class=\"welcome-title\">\r\n" + 
				"    			E-mail Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\">\r\n" + 
				"		  				<input id=\"inpEmail\" type=\"email\" name=\"inpEmail\" placeholder=\"Current e-mail address...\" required><br>" + 
				"		  				<input id=\"inpNewEmail\" type=\"email\" name=\"inpNewEmail\" placeholder=\"New e-mail address...\" required><br>" + 
				"			  			<input type=\"hidden\" name=\"action\" value=\"updateEmail\">\r\n" + 
				"						<div id=\"errorEmailIncorrect\"><span>Current e-mail is not correct</span></div>\r\n" + 
				"						<div id=\"patternTitleEmail\"><span>E-mail is not valid</span></div>\r\n" + 
				"						<!-- <input type=\"image\" alt=\"submit\" src=\"img/login_register_icons/register.png\"><br><br> -->\r\n" + 
				"						<a onclick=\"correctEmail()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPart2(HttpSession session, String page) {

		String script = "";
		
		if (session.getAttribute("userID")!=null) {
			script += "<script>\r\n" + 
					"window.onload = function() {\r\n" + 
					"	\r\n" + 
					"	  languageOnLoad("+session.getAttribute("currentTheme")+", \""+page+"\");\r\n" + 
					"	  \r\n" + 
					"};\r\n" + 
					"</script>";
		}
		
		return "<section id=\"footer\">\r\n" + 
				"\r\n" + 
				"        <ul class=\"list-inline\">\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\"><img src=\"img/Buttons/Paypal.png\"></a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\"><img src=\"img/Buttons/Discord.png\"></a></li>\r\n" + 
				"        </ul>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"        <ul class=\"list-inline aboveFooter\">\r\n" + 
				"            <li class=\"list-inline-item\"><a id=\"footerPrivacy\" href=\"#\">PRIVACY NOTICE</a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a id=\"footerTerms\" href=\"#\">TERMS OF SERVICE</a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a id=\"footerCopyright\" href=\"#\">COPYRIGHT</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"\r\n" + 
				"        <ul class=\"list-inline belowFooter\">\r\n" + 
				"            <li class=\"list-inline-item\"><a href=\"#\">Anime-Arena 2022</a></li>\r\n" + 
				"            <li class=\"list-inline-item\"><a id=\"footerReserved\" href=\"#\">All rights Reserved</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"       \r\n" + 
				"    </section>\r\n" + 
				"\r\n" + 
				"<script type=\"text/javascript\">\r\n" + 
				"function displayUsers(tipoUser) {\r\n" + 
				"	console.log(tipoUser);\r\n" + 
				"    if (tipoUser==\"administrador\") {\r\n" + 
				"       /* document.getElementById(\"players\").style.display=\"block\";*/\r\n" + 
				"        document.getElementById(\"admin\").style.display=\"block\";\r\n" + 
				"    }\r\n" + 
				"    else if (tipoUser==\"player\") {\r\n" + 
				"        /*document.getElementById(\"players\").style.display=\"block\";*/\r\n" + 
				"        document.getElementById(\"admin\").style.display=\"none\";\r\n" + 
				"    }\r\n" + 
				"    else {\r\n" + 
				"      /*  document.getElementById(\"players\").style.display=\"none\";*/\r\n" + 
				"        document.getElementById(\"admin\").style.display=\"none\";\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"</script>\r\n" +
				"<script>\r\n" + 
				"	var tipo = \""+session.getAttribute("tipoUser")+"\";\r\n" + 
				"	\r\n" + 
				"	if (tipo!=null) {\r\n" + 
				"		displayUsers( tipo );\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	\r\n" + 
				"</script>\r\n" + 
				script+
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
	
	private String getPartPassword() {
		return "	<section id=\"settings\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"    	\r\n" + 
				"    		<div id=\"passwordUpdated\" class=\"welcome-title\" style=\"display:none;\">\r\n" + 
				"    			Password Updated!\r\n" + 
				"    		</div>\r\n" + 
				"	    	\r\n" + 
				"	    	\r\n" + 
				"	    	<div class=\"col\">\r\n" + 
				"	    		<div class=\"group-form\">\r\n" + 
				"	    			<div class=\"blackBackground\">.</div>\r\n" + 
				"	    			<form  id=\"form\" action=\"Settings\">\r\n" + 
				"		  				<input id=\"inpOld\" type=\"password\" name=\"inpOld\" placeholder=\"Current password...\" required><br>\r\n" + 
				"		  				<input id=\"inpPass\" type=\"password\" name=\"inpPass\" placeholder=\"New password...\" \r\n" + 
				"			  				pattern=\"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\" \r\n" + 
				"			  				title=\"Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters\"\r\n" + 
				"			  				required><br>\r\n" + 
				"						<input id=\"inpConfirmPass\" type=\"password\" name=\"inpConfirmPass\" placeholder=\"Confirm new password...\"\r\n" + 
				"							pattern=\"(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}\" \r\n" + 
				"			  				title=\"Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters\"\r\n" + 
				"			  				required><br>\r\n" + 
				"			  			<input type=\"hidden\" name=\"action\" value=\"updatePassword\">\r\n" + 
				"						<div id=\"errorPassIncorrect\"><span>Current password is not correct</span></div>\r\n" + 
				"						<div id=\"errorPassNotMatch\"><span>Passwords do not match</span></div>\r\n" + 
				"						<div id=\"patternTitle\"><span>Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters</span></div>\r\n" + 
				""+
				
				"						<!-- <input type=\"image\" alt=\"submit\" src=\"img/Buttons/confirm_button.png\"><br><br> -->\r\n" + 
				"						<a onclick=\"correctPassword()\"><img src=\"img/Buttons/confirm_button.png\"></a><br><br>\r\n" + 
				"					</form>\r\n" + 
				"					\r\n" + 
				"				</div>\r\n" + 
				"    		</div>\r\n" + 
				"    	</div>\r\n" + 
				"	</section>";
	}
	
	private String getPart1(int userID, String page) {
		
		String part1 = "<html lang=\"en\">\r\n" + 
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
				"    <link href=\"css/register.css\" rel=\"stylesheet\">\r\n" + 
				"    <script type=\"text/javascript\" src=\"js/settings.js\"></script>\r\n" +
				"	 <script type=\"text/javascript\" src=\"js/language.js\"></script>" +
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
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navAnn\" href=\"index.jsp\">ANNOUNCEMENTS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navRanks\" href=\"leaderboards.jsp\">LEADERBOARDS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navMissions\" href=\"missions.jsp\">MISSIONS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navHelp\" href=\"help.jsp\">GAME INFO</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\" id=\"admin\" style=\"display:none;\">\r\n" + 
				"                    <a class=\"nav-link navBarItem dropdown-toggle\" id=\"adminDropdown\" role=\"button\"\r\n" + 
				"                        data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\r\n" + 
				"                        ADMIN\r\n" + 
				"                    </a>\r\n" + 
				"                    <!-- Dropdown - User Information -->\r\n" + 
				"                    <div class=\"dropdown-menu animated--grow-in \"\r\n" + 
				"                        aria-labelledby=\"adminDropdown\">\r\n" + 
				"                        <a id=\"navCchar\" class=\"dropdown-item\" href=\"create.jsp\">\r\n" + 
				"                            Create Character\r\n" + 
				"                        </a>\r\n" + 
				"  						 <a id=\"navCann\" class=\"dropdown-item\" href=\"createAnnouncement.jsp\">\r\n" + 
				"                            Create Announcement\r\n" + 
				"                        </a>\r\n" + 
				"                        <a id=\"navCtheme\" class=\"dropdown-item\" href=\"createTheme.jsp\">\r\n" + 
				"                            Create Theme\r\n" + 
				"                        </a>" +
				"                    </div>\r\n" + 
				"                </li>\r\n" + 
				"            </ul>\r\n" + 
				"			<ul class=\"nav navbar-nav ml-auto justify-content-end\"  id=\"isLog\">";
		
		String username = UserInfo.getPlayerUsername(userID);
		
		part1 += "<li class=\"navbar-nav ml-auto\">\r\n" + 
				"                    <a class=\"nav-link dropdown-toggle\" id=\"userDropdown\" role=\"button\"\r\n" + 
				"                        data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\r\n" + 
				"                        <span id=\"navWelcome\" class=\"welcome\">Welcome, "+username+" </span>\r\n" + 
						"                        <img class=\"img-profile rounded-circle online\" style=\"width: 50px; height: 50px;\" src=\"ViewAvatar?id="+userID+"\">\r\n" + 
						"                    </a>\r\n" + 
						"                    <!-- Dropdown - User Information -->\r\n" + 
						"                    <div class=\"dropdown-menu dropdown-menu-right shadow animated--grow-in \"\r\n" + 
						"                        aria-labelledby=\"userDropdown\">\r\n" + 
						"                        <a id=\"navProfile\" class=\"dropdown-item\" href=\"ViewProfile?username="+username+"\">\r\n" + 
								"                            Profile\r\n" + 
								"                        </a>\r\n" + 
								"                        <div class=\"dropdown-divider\"></div>\r\n" + 
								"                        <a id=\"navAvatar\" class=\"dropdown-item\" href=\"Settings?action=changeAvatar\">\r\n" + 
								"                            Change avatar\r\n" + 
								"                        </a>\r\n" + 
								"    					 <a id=\"navBack\" class=\"dropdown-item\" href=\"Settings?action=changeBack\">\r\n" + 
								"                            Change backgrounds\r\n" + 
								"                        </a>" +
								"                        <a id=\"navMail\" class=\"dropdown-item\" href=\"Settings?action=changeEmail\">\r\n" + 
								"                            Change e-mail\r\n" + 
								"                        </a>\r\n" + 
								"                        <a id=\"navPass\" class=\"dropdown-item\" href=\"Settings?action=changePassword\">\r\n" + 
								"                            Change password\r\n" + 
								"                        </a>\r\n" + 
								"						 <div class=\"dropdown-divider\"></div>\r\n" + 
								"                        <a class=\"dropdown-item\" onclick=\"setLanguage('english', '"+page+"')\">\r\n" + 
								"                            English\r\n" + 
								"                        </a>\r\n" + 
								"                        <a class=\"dropdown-item\" onclick=\"setLanguage('portuguese', '"+page+"')\">\r\n" + 
								"                            Português\r\n" + 
								"                        </a>" +
								"                         <div class=\"dropdown-divider\"></div>\r\n" + 
								"                        <a id=\"navLogout\" class=\"dropdown-item\" href=\"logout.jsp\">\r\n" + 
								"                            Logout\r\n" + 
								"                        </a>\r\n" + 
								"                    </div>\r\n" + 
								"              ";
		
		part1 += "</ul>\r\n" + 
				"        </div>\r\n" + 
				"    </nav>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    <section id=\"play\">\r\n" + 
				"        <div class=\"text-center\">\r\n" + 
				"\r\n" + 
				"                <a href=\"selection.jsp\"><img src=\"img/Buttons/play button.png\"></a>\r\n" + 
				"                <br>\r\n" + 
				"                <img src=\"img/logo_full.png\">\r\n" + 
				"                <br>\r\n" + 
				"                <p id=\"banner\">YOUR #1 ONLINE ANIME STRATEGIC GAME</p>\r\n" + 
				"        </div>\r\n" + 
				"    </section>";
		
		return part1;

	}
	
	private void updatePassword(int userID, String newPass) {
		try {
			Connection con = Connector.getConnection();

			PreparedStatement pstmt =con.prepareStatement("UPDATE USERS SET pass =? WHERE userID=?"); 
		             pstmt.setString(1, Connector.hashFromPass(newPass));
		             pstmt.setInt(2, userID);
			
		     pstmt.executeUpdate();  
	
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	
	}
	
	private boolean passwordIsSame(int id, String pass) {
		
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String query = "select * from USERS where userID="+id;
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			//int id = 0;
			if (rs.next()) {
				//id = Integer.parseInt(rs_id.getString("userID"))+1;
				return Connector.hashFromPass(pass).equalsIgnoreCase( rs.getString("pass") );
			}
			
			rs.close();
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