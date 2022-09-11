package resources;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/ViewProfile")
public class ViewProfile extends HttpServlet {
	
	private static final long serialVersionUID = 7215979604673189309L;
	
	public ViewProfile() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter();
		
		String username = request.getParameter("username");
		
		if (session.getAttribute("userID")!=null) {
			int userID = UserInfo.getPlayerID(username);
			
			pw.print(getPart1(session, userID));
			pw.print(getProfile(session, userID));
			pw.print(getPart2(session, userID));
		}
		else {
			response.sendRedirect("login.jsp");
		}
		
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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
			
			log += UserInfo.getPlayerUsername( (int)session.getAttribute("userID") );
			log+=" </span>\r\n" + 
					"                        <img class=\"img-profile rounded-circle online\" style=\"width: 50px; height: 50px;\" src=\"ViewAvatar?id="+session.getAttribute("userID")+"\"";
			log+=">        </a>\r\n" + 
					"                    <!-- Dropdown - User Information -->\r\n" + 
					"                    <div class=\"dropdown-menu dropdown-menu-right shadow animated--grow-in \"\r\n" + 
					"                        aria-labelledby=\"userDropdown\">\r\n" + 
					"                        <a id=\"navProfile\" class=\"dropdown-item\" href=\"ViewProfile?username="+UserInfo.getPlayerUsername((int)session.getAttribute("userID"))+"\"";
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
					"                        <a class=\"dropdown-item\" onclick=\"setLanguage('english', 'profile')\">\r\n" + 
					"                            English\r\n" + 
					"                         </a>\r\n" + 
					"                         <a class=\"dropdown-item\" onclick=\"setLanguage('portuguese', 'profile')\">\r\n" + 
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
				"    <link href=\"css/profile.css\" rel=\"stylesheet\">\r\n" + 
				"    <link href=\"css/announcements.css\" rel=\"stylesheet\">\r\n" + 
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
				"  	<section id=\"showProfile\">" + 
				"  	\r\n" + 
				"  		<div class=\"container\">    \r\n" + 
				"			\r\n" + 
				"			 <div class=\"rankTitle\">\r\n" + 
				"            Profile\r\n" + 
				"            </div>\r\n" + 
				"            <div class=\"group\">\r\n" + 
				"            <div class=\"blackBackground\">.</div>\r\n" + 
				"            <div class=\"group-rank\">\r\n" + 
				"		<div class=\"text-center\">\r\n" + 
				"		  <img class=\"img-profile rounded-circle\"  src=\"ViewAvatar?id="+userID+"\" style=\"padding:10px;\">\r\n" + 
				"		  \r\n" + 
				"		  <table class=\"table table-bordered\">";
		
		
	}
	
	private String getProfile(HttpSession session, int userID) {
		
		String profile = "";
		
		PreparedStatement stmt = null;
		
		try {
			Class.forName(Connector.drv);

			Connection conn = Connector.getConnection();

			String query = String.format("select * FROM USERS where userID='%s'", userID); 
			
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String username = rs.getString("username");
				String registerDate = rs.getString("registerDate");
				String state = rs.getString("estado");
				int nWins = rs.getInt("nWins");
				int nLosses = rs.getInt("nLosses");
				int streak = rs.getInt("streak");
				int highestStreak = rs.getInt("highestStreak");
				int highestLevel = rs.getInt("streak");
				int xp = rs.getInt("xp");
				float percentage = ( (nWins+nLosses)>0) ? ((nWins*100) / (nWins+nLosses))*1.0f : 0;
				
				profile += getProfileInfo(userID, session, username, registerDate, state, nWins, nLosses, 
						streak, highestStreak, highestLevel, xp, percentage);
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
		return profile;
	}

	private String getProfileInfo(int userID, HttpSession session,
			String username, String registerDate, String state, int nWins, int nLosses, 
			int streak, int highestStreak, int highestLevel, int xp, float percentage) {
		
		String siteRank = (UserInfo.isAdmin(userID)) ? "Administrator" : "Player";
		
		String tbody = "<tbody>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Username:</td>\r\n" + 
				"		        <td>"+username+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Site rank:</td>\r\n" + 
				"		        <td>"+siteRank+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Registered on:</td>\r\n" + 
				"		        <td>"+registerDate+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>State:</td>\r\n" + 
				"		        <td>"+state+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Level:</td>\r\n" + 
				"		        <td>"+UserInfo.getLevel(xp)+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Ladder Rank:</td>\r\n" + 
				"		        <td>#"+UserInfo.getLadderRank(userID)+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Experience Points:</td>\r\n" + 
				"		        <td>"+xp+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Wins:</td>\r\n" + 
				"		        <td>"+nWins+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Losses:</td>\r\n" + 
				"		        <td>"+nLosses+"</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Win percentage:</td>\r\n" + 
				"		        <td>"+percentage+"%</td>\r\n" + 
				"		      </tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Streak:</td><td>"+ ((streak>=0) ? "+"+streak : streak)+"</td></tr>\r\n" + 
				"		      <tr>\r\n" + 
				"		        <td>Highest Streak:</td>\r\n" + 
				"		        <td>+"+highestStreak+"</td>\r\n" + 
						"		      </tr>\r\n" + 
						"		      <tr>\r\n" + 
						"		        <td>Highest Level:</td>\r\n" + 
						"		        <td>"+highestLevel+"</td>\r\n" + 
								"		      </tr>\r\n" + 
								"		    </tbody>\r\n" + 
								"		  </table>";	
		
		tbody += "<div id=\"adminZone\"";
		if (UserInfo.isAdmin( (int) session.getAttribute("userID") )) {
			tbody += "style=\"display:block\"";
		}
		else {
			tbody += "style=\"display:none\"";
		}
		tbody += ">\r\n" + 
				"		  	 <form action=\"UpdateProfile\">\r\n" + 
				"		  		  <label>Update Site Rank:</label>\r\n" + 
				"				  <select name=\"siteRank\" id=\"siteRank\">\r\n" + 
				"						<option value=\"player\">Player</option>\r\n" + 
				"						<option value=\"admin\">Admin</option>\r\n" + 
				"					</select><br>\r\n" + 
				"				  <label>Update State:</label>\r\n" + 
				"				  <input type=\"date\" id=\"state\" name=\"state\"><br>\r\n" + 
				"				  <input type=\"hidden\" name=\"action\" value=\"changeUser\">\r\n" + 
				"				  <input type=\"hidden\" name=\"user\" value=\""+username+"\">\r\n" + 
				"				  <input type=\"submit\">\r\n" + 
				"			  </form>\r\n" + 
				"		  </div>\r\n" + 
				"			\r\n" + 
				"		</div>"+
				" 				</div>\r\n" + 
				"			</div>\r\n" + 
				"		</div>\r\n" + 
				"	</section>";
		
		return tbody;
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
					"languageOnLoad("+session.getAttribute("currentTheme")+ ", \"profile\""+
					");"+
					"};\r\n" + 
					"</script>";
		}
		return "";
	}
}
