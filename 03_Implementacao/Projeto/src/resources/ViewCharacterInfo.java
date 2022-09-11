package resources;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.Connector;
import users.UserInfo;

@WebServlet("/ViewCharacterInfo")
public class ViewCharacterInfo extends HttpServlet {
	
	private static final long serialVersionUID = 7215979604673189309L;
	
	public ViewCharacterInfo() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter();
				
		int characterID = Integer.parseInt(request.getParameter("id"));
		
		int userID = -1;
		if (session.getAttribute("userID")!=null) {
			userID = (int) session.getAttribute("userID");
		}
		
		pw.print( getPart1(session, userID) );
		pw.print( getBigSmallScreen(characterID, userID) );
		pw.print( getPart2(session, userID) );

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	private String getBigSmallScreen(int characterID, int userID) {
		
		String charName = null;
		String charDesc = null;
		int[] cooldowns = new int[4];
		String[] desciptions = new String[4];
		int[] abilitiesID = new int[4];
		String[] abbName = new String[4];
		
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st = con.prepareStatement("select * from THEME_CHARACTER where themeID="+UserInfo.getCurrentTheme(userID)+" and characterID=?");
			st.setInt(1, characterID);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				charName = rs.getString("nome");
				charDesc = rs.getString("descricao");
			} 
			rs.close();
			st.close();
			
			PreparedStatement st_ab = con.prepareStatement("select * from ABILITY where characterID=?");
			st_ab.setInt(1, characterID);
			ResultSet rs_ab = st_ab.executeQuery();

			int count = 0;
			while (rs_ab.next()) {
				int abilityID = rs_ab.getInt("abilityID");
				cooldowns[count] = rs_ab.getInt("cooldown");
				abilitiesID[count] = abilityID;
				
				PreparedStatement st_ab_info = con.prepareStatement("select * from THEME_ABILITY where themeID="+UserInfo.getCurrentTheme(userID)+" and abilityID=?");
				st_ab_info.setInt(1, abilityID);
				ResultSet rs_ab_info = st_ab_info.executeQuery();
				if (rs_ab_info.next()) {
					abbName[count] = rs_ab_info.getString("nome");
					desciptions[count] = rs_ab_info.getString("descricao");
				} 
				rs_ab_info.close();
				st_ab_info.close();
	
				count++;
			} 
			rs_ab.close();
			st_ab.close();
			
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
		String big = "<div id=\"bigScreen\" class=\"row\">\r\n" + 
				"        	<div class=\"col-5 text-end\">\r\n" + 
				"        		<img src=\"ViewCharacter?id="+characterID+"\">\r\n" + 
				"        	</div>\r\n" + 
				"        	<div class=\"col-6 text-start\">\r\n" + 
				"        		<div class=\"charName\">"+charName+"</div>\r\n" + 
				"        		<hr class=\"solid\">\r\n" + 
				"        		<div class=\"charDesc\">"+charDesc+"</div>\r\n" + 
				"        	</div>\r\n" + 
				"        	\r\n" + 
				"        	<div class=\"row\">\r\n" + 
				"        		<div class=\"col-2\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[0]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col-4 text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[0]+"</div>\r\n" + 
				"	        		<hr class=\"solid\">\r\n" + 
				"	        		<div class=\"abbDesc\">"+desciptions[0]+"</div>\r\n" + 
				"	        	    <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	    <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[0]+"</div>\r\n" + 
				"	        	    <div class=\"nature\">/ NATURE: "+getNatures(abilitiesID[0])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	        	\r\n" + 
				"	        	<div class=\"col-2\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[1]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col-4 text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[1]+"</div>\r\n" + 
				"	        		<hr style=\"width: 461px;\" class=\"solid\">\r\n" + 
				"	        		<div style=\"width: 461px;\" class=\"abbDesc\">"+desciptions[1]+"</div>\r\n" + 
				"	        	   <!-- <img src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\"> --> \r\n" + 
				"	        	   <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	   <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[1]+"</div>\r\n" + 
				"	        	   <div class=\"nature\">/ NATURE: "+getNatures(abilitiesID[1])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"        	</div>\r\n" + 
				"        	\r\n" + 
				"        	<div class=\"row\">\r\n" + 
				"        		<div class=\"col-2\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[2]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col-4 text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[2]+"</div>\r\n" + 
				"	        		<hr style=\"width: 461px;\" class=\"solid\">\r\n" + 
				"	        		<div style=\"width: 461px;\" class=\"abbDesc\">"+desciptions[2]+"</div>\r\n" + 
				"	        	   <!-- <img src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\"> --> \r\n" + 
				"	        	   <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	   <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[2]+"</div>\r\n" + 
				"	        	   <div class=\"nature\">/ NATURE: "+getNatures(abilitiesID[2])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	        	\r\n" + 
				"	        	<div class=\"col-2\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[3]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col-4 text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[3]+"</div>\r\n" + 
				"	        		<hr style=\"width: 461px;\" class=\"solid\">\r\n" + 
				"	        		<div style=\"width: 461px;\" class=\"abbDesc\">"+desciptions[3]+"</div>\r\n" + 
				"	        	   <!-- <img src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\"> --> \r\n" + 
				"	        	   <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	   <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[3]+"</div>\r\n" + 
				"	        	   <div class=\"nature\">/ NATURE: "+getNatures(abilitiesID[3])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"        	</div>\r\n" + 
				"      \r\n" + 
				"        </div>";
		
		String small = "<div id=\"smallScreen\" class=\"row\">\r\n" + 
				"        	<div class=\"col text-end\">\r\n" + 
				"        		<img src=\"ViewCharacter?id="+characterID+"\">\r\n" + 
				"        	</div>\r\n" + 
				"        	<div class=\"col text-start\">\r\n" + 
				"        		<div class=\"charName\">"+charName+"</div>\r\n" + 
				"        		<hr class=\"solid\">\r\n" + 
				"        		<div class=\"charDesc\">"+charDesc+"</div>\r\n" + 
				"        	</div>\r\n" + 
				"        	\r\n" + 
				"        	<div class=\"row\">\r\n" + 
				"        		<div class=\"col\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[0]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[0]+"</div>\r\n" + 
				"	        		<hr class=\"solid\">\r\n" + 
				"	        		<div class=\"abbDesc\">"+desciptions[0]+"</div>\r\n" + 
				"	        	    <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	    <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[0]+"</div>\r\n" + 
				"	        	    <div class=\"nature\">/ NATURE REQUIRED: "+getNatures(abilitiesID[0])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	        </div>\r\n" + 
				"	        <div class=\"row\">\r\n" + 
				"	        	<div class=\"col\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[1]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[1]+"</div>\r\n" + 
				"	        		<hr style=\"width: 461px;\" class=\"solid\">\r\n" + 
				"	        		<div style=\"width: 461px;\" class=\"abbDesc\">"+desciptions[1]+"</div>\r\n" + 
				"	        	   <!-- <img src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\"> --> \r\n" + 
				"	        	   <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	   <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[1]+"</div>\r\n" + 
				"	        	   <div class=\"nature\">/ NATURE REQUIRED: "+getNatures(abilitiesID[1])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"        	</div>\r\n" + 
				"        	\r\n" + 
				"        	<div class=\"row\">\r\n" + 
				"        		<div class=\"col\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[2]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[2]+"</div>\r\n" + 
				"	        		<hr style=\"width: 461px;\" class=\"solid\">\r\n" + 
				"	        		<div style=\"width: 461px;\" class=\"abbDesc\">"+desciptions[2]+"</div>\r\n" + 
				"	        	   <!-- <img src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\"> --> \r\n" + 
				"	        	   <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	   <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[2]+"</div>\r\n" + 
				"	        	   <div class=\"nature\">/ NATURE REQUIRED: "+getNatures(abilitiesID[2])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"        	</div>\r\n" + 
				"        	<div class=\"row\">\r\n" + 
				"	        	<div class=\"col\">\r\n" + 
				"	        		<div class=\"text-end\">\r\n" + 
				"        				<img src=\"ViewAbility?id="+abilitiesID[3]+"\">\r\n" + 
				"	        		</div>\r\n" + 
				"	        	</div>\r\n" + 
				"	    \r\n" + 
				"	        	<div class=\"col text-start\">\r\n" + 
				"	        		<div class=\"abbName\">"+abbName[3]+"</div>\r\n" + 
				"	        		<hr style=\"width: 461px;\" class=\"solid\">\r\n" + 
				"	        		<div style=\"width: 461px;\" class=\"abbDesc\">"+desciptions[3]+"</div>\r\n" + 
				"	        	   <!-- <img src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\"> --> \r\n" + 
				"	        	   <img style=\"width:561px; margin-left:-100px\" src=\"img/divider_line_and_cd_nature_box_container/nature_and_cds.png\">\r\n" + 
				"	        	   <div class=\"cooldown\">/ COOLDOWN: "+cooldowns[3]+"</div>\r\n" + 
				"	        	   <div class=\"nature\">/ NATURE REQUIRED: "+getNatures(abilitiesID[3])+"</div>\r\n" + 
				"	        	</div>\r\n" + 
				"        	</div>\r\n" + 
				"      \r\n" + 
				"        </div>\r\n" + 
				"  	\r\n" + 
				"  		\r\n" + 
				"  	</section>";
		
	
		return big+""+small;
	}
	
	
	private String getPart1(HttpSession session, int userID) {
		
		String isAdmin = "";
		String notLogged = "";
		String log = "";
		
		if (session.getAttribute("loggedIn") != null && (boolean)session.getAttribute("loggedIn")) {

			log +="<ul class=\"nav navbar-nav ml-auto justify-content-end\"  id=\"isLog\">\r\n" + 
					"	  <li class=\"navbar-nav ml-auto\">\r\n" + 
					"                    <a class=\"nav-link dropdown-toggle\" id=\"userDropdown\" role=\"button\"\r\n" + 
					"                        data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">\r\n" + 
					"                        <span id=\"navWelcome\" class=\"welcome\">Welcome, ";
			
			log += UserInfo.getPlayerUsername(userID);
			log+=" </span>\r\n" + 
					"                        <img class=\"img-profile rounded-circle online\" style=\"width: 50px; height: 50px;\" src=\"ViewAvatar?id="+userID+"\"";
			log+=">        </a>\r\n" + 
					"                    <!-- Dropdown - User Information -->\r\n" + 
					"                    <div class=\"dropdown-menu dropdown-menu-right shadow animated--grow-in \"\r\n" + 
					"                        aria-labelledby=\"userDropdown\">\r\n" + 
					"                        <a id=\"navProfile\" class=\"dropdown-item\" href=\"ViewProfile?username="+UserInfo.getPlayerUsername(userID)+"\"";
			log+=">\r\n" + 
					"                            Profile\r\n" + 
					"                        </a>\r\n" + 
					"                        <div class=\"dropdown-divider\"></div>\r\n" + 
					"                        <a id=\"navAvatar\" class=\"dropdown-item\" href=\"Settings?action=changeAvatar\">\r\n" + 
					"                            Change avatar\r\n" + 
					"                        </a>\r\n" + 
					"						 <a id=\"navBack\" class=\"dropdown-item\" href=\"Settings?action=changeBack\">\r\n" + 
					"                            Change backgrounds\r\n" + 
					"                        </a>"+
					"                        <a id=\"navMail\" class=\"dropdown-item\" href=\"Settings?action=changeEmail\">\r\n" + 
					"                            Change e-mail\r\n" + 
					"                        </a>\r\n" + 
					"                        <a id=\"navPass\" class=\"dropdown-item\" href=\"Settings?action=changePassword\">\r\n" + 
					"                            Change password\r\n" + 
					"                        </a>\r\n" + 
					" 						 <div class=\"dropdown-divider\"></div>\r\n" + 
					"                        <a class=\"dropdown-item\" onclick=\"setLanguage('english', 'characterInfo')\">\r\n" + 
					"                            English\r\n" + 
					"                         </a>\r\n" + 
					"                         <a class=\"dropdown-item\" onclick=\"setLanguage('portuguese', 'characterInfo')\">\r\n" + 
					"                            Português\r\n" + 
					"                         </a>"+
					"                         <div class=\"dropdown-divider\"></div>\r\n" + 
					"                        <a id=\"navLogout\" class=\"dropdown-item\" href=\"logout.jsp\">\r\n" + 
					"                            Logout\r\n" + 
					"                        </a>\r\n" + 
					"                    </div></ul>";
			
			
			
			if ( ((String)session.getAttribute("tipoUser")).equalsIgnoreCase("administrador") ) {
				isAdmin+="<li class=\"nav-item\" id=\"admin\">\r\n" + 
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
						"                </li>";
			}
		}
		else {
			notLogged+="         <ul class=\"navbar-nav justify-content-end\" id=\"login\">\r\n" + 
					"				<li class=\"nav-item\" id=\"login\">\r\n" + 
					"					<a id=\"navLogin\" class=\"nav-link navBarItem\" href=\"login.jsp\">LOGIN</a>\r\n" + 
					"				</li>\r\n" + 
					"			</ul>\r\n" + 
					"			<ul class=\"navbar-nav justify-content-end\" id=\"register\">\r\n" + 
					"				<li class=\"nav-item\" id=\"register\">\r\n" + 
					"					<a id=\"navRegister\" class=\"nav-link navBarItem\" href=\"register.jsp\">REGISTER</a>\r\n" + 
					"				</li>\r\n" + 
					"			</ul>";
		}
		
		
		String nav = "<nav class=\"navbar navbar-expand-lg navbar-dark bg-faded\">\r\n" + 
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
				"                <li class=\"nav-item active\">\r\n" + 
				"                	<a class=\"nav-link navBarItem\" id=\"navHelp\" href=\"help.jsp\">GAME INFO</a>\r\n" + 
				"                </li>\r\n" + 
				isAdmin+
				"            </ul>\r\n" + 
				notLogged+
				"			\r\n" + 
				log+
				"        </div>\r\n" + 
				"    </nav>";
		
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
				"    <link href=\"css/help.css\" rel=\"stylesheet\">\r\n" + 
				"    <link href=\"css/helpCharacters.css\" rel=\"stylesheet\">\r\n" + 
				"    <link href=\"css/characterInfo.css\" rel=\"stylesheet\">\r\n" + 
				"	 <script type=\"text/javascript\" src=\"js/help.js\"></script>\r\n" + 
				"    <script type=\"text/javascript\" src=\"js/language.js\"></script>" +
				"\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n" + 
				"\r\n" + 
				nav+	
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
				"    </section>\r\n" + 
				"\r\n" + 
				"  	<section id=\"helpInfo\">\r\n" + 
				"  	\r\n" + 
				"  		<ul class=\"list-inline\">\r\n" + 
				"            <li class=\"list-inline-item\"><a id=\"navIngame\" href=\"help.jsp\">IN-GAME</a></li>\r\n" + 
				"            <li class=\"list-inline-item activeItem\"><a id=\"navChars\" href=\"#\">CHARACTERS & SKILLS</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"\r\n" + 
				"        <div class=\"group\">\r\n" + 
				"	         <ul class=\"list-inline characterCosmeticsStyle\" style=\"background-color: #000000;\">\r\n" + 
				"	             <li id=\"nav-bleach\" class=\"list-inline-item activeAnime\"><a onclick=\"setNavChars('bleach')\">BLEACH</a></li>\r\n" + 
				"	             <li id=\"nav-DS\" class=\"list-inline-item notActiveAnimeType\"><a onclick=\"setNavChars('DS')\">DEMON SLAYER</a></li>\r\n" + 
				"	             <li id=\"nav-hunter\" class=\"list-inline-item notActiveAnimeType\"><a onclick=\"setNavChars('hunter')\">HUNTER X HUNTER</a></li>\r\n" + 
				"	             <li id=\"nav-OnePunchMan\" class=\"list-inline-item notActiveAnimeType\"><a onclick=\"setNavChars('OnePunchMan')\">ONE PUNCH MAN</a></li>\r\n" + 
				"	             <li id=\"nav-SAO\" class=\"list-inline-item notActiveAnimeType\"><a onclick=\"setNavChars('SAO')\">SWORD ART ONLINE</a></li>\r\n" + 
				"	         </ul>\r\n" + 
				"	   \r\n" + 
				"         	<div id=\"upText2\" class=\"text\">\r\n" + 
				"         		/ Anime-Arena makes you select 3 characters when you start a new game. \r\n" + 
				"         		This page shows you the information of a character.\r\n" + 
				"         	</div>\r\n" + 
				"        </div>";
	}
	
	private String getPart2(HttpSession session, int userID) {
		return " <section id=\"footer\">\r\n" + 
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
				getScript(session, userID) +
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
	private String getScript(HttpSession session, int userID) {
		if (session.getAttribute("userID")!=null) {
			return "<script>\r\n" + 
					"window.onload = function() {"+
					"languageOnLoad("+session.getAttribute("currentTheme")+ ", \"characterInfo\""+
					");"+
					"};\r\n" + 
					"</script>";
		}
		return "";
	}

	private String getNatures(int abilityID) {
		
		int taijutsu = nTaijutsu(abilityID);
		int heart = nHeart(abilityID);
		int energy = nEnergy(abilityID);
		int spirit = nSpirit(abilityID);
		int random = nRandom(abilityID);
		
		if (taijutsu==0 && heart==0 && energy==0 && spirit==0 && random==0) {
			return "none";
		}
		
		String retorno = "";
		if (taijutsu>0) {
			for (int i = 0; i < taijutsu; i++) {
				retorno += "<img src='battle/Taijutsu.png'>";
			}
		}
		if (heart>0) {
			for (int i = 0; i < heart; i++) {
				retorno += "<img src='battle/Heart.png'>";
			}
		}
		if (energy>0) {
			for (int i = 0; i < energy; i++) {
				retorno += "<img src='battle/Energy.png'>";
			}
		}
		if (spirit>0) {
			for (int i = 0; i < spirit; i++) {
				retorno += "<img src='battle/Spirit.png'>";
			}
		}
		if (random>0) {
			for (int i = 0; i < random; i++) {
				retorno += "<img src='battle/Random.png'>";
			}
		}
		
		return retorno;
	}
	
	private int nTaijutsu(int abilityID) {
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st_e1 = con.prepareStatement("select * from ABILITY_E1 where abilityID=?");
			st_e1.setInt(1, abilityID);
			ResultSet rs_e1 = st_e1.executeQuery();
			if (rs_e1.next()) {
				return rs_e1.getInt("quantity");
			} 
			rs_e1.close();
			st_e1.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	private int nHeart(int abilityID) {
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st_e1 = con.prepareStatement("select * from ABILITY_E2 where abilityID=?");
			st_e1.setInt(1, abilityID);
			ResultSet rs_e1 = st_e1.executeQuery();
			if (rs_e1.next()) {
				return rs_e1.getInt("quantity");
			} 
			rs_e1.close();
			st_e1.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	private int nEnergy(int abilityID) {
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st_e1 = con.prepareStatement("select * from ABILITY_E3 where abilityID=?");
			st_e1.setInt(1, abilityID);
			ResultSet rs_e1 = st_e1.executeQuery();
			if (rs_e1.next()) {
				return rs_e1.getInt("quantity");
			} 
			rs_e1.close();
			st_e1.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	private int nSpirit(int abilityID) {
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st_e1 = con.prepareStatement("select * from ABILITY_E4 where abilityID=?");
			st_e1.setInt(1, abilityID);
			ResultSet rs_e1 = st_e1.executeQuery();
			if (rs_e1.next()) {
				return rs_e1.getInt("quantity");
			} 
			rs_e1.close();
			st_e1.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	private int nRandom(int abilityID) {
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st_e1 = con.prepareStatement("select * from ABILITY_E5 where abilityID=?");
			st_e1.setInt(1, abilityID);
			ResultSet rs_e1 = st_e1.executeQuery();
			if (rs_e1.next()) {
				return rs_e1.getInt("quantity");
			} 
			rs_e1.close();
			st_e1.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
