package announcements;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.Connector;
import users.UserInfo;

@WebServlet("/Announcement")
public class Announcement extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Announcement() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter(); 
		
		String action = (String) request.getParameter("action");
		if (action.equalsIgnoreCase("create")) {
			int userID = (int) session.getAttribute("userID");
			
			String title = request.getParameter("title");
			String category = request.getParameter("category");
			String small = request.getParameter("smallDescription");
			String full = request.getParameter("writeAnnouncement");
			
			
			insertAnnBD(userID, title, category, small, fromTextToHTML(full));
			
			response.sendRedirect("index.jsp");
		}
		else if (action.equalsIgnoreCase("load")) {
			response.setContentType("text/plain");
			lastAnnouncements(pw);
		}
		else if (action.equalsIgnoreCase("viewFull")) {
			response.setContentType("text/html");
			int annID = Integer.parseInt(request.getParameter("id"));
			createAnn(session, pw, annID);
		}
		else if (action.equalsIgnoreCase("preview")) {
			response.setContentType("text/html");
			String current = request.getParameter("current");
			String current2 = current.replaceAll("asdfg", "[");
			String current3 = current2.replaceAll("gfdsa", "]");
			pw.write( fromTextToHTML(current3) );
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
	
	private String getPastNews1() {
		return "<div class=\"container\">\r\n" + 
				"          \r\n" + 
				"            <div id=\"pastNewsBigText\">\r\n" + 
				"            PAST NEWS\r\n" + 
				"            </div>\r\n" + 
				"\r\n" + 
				"            <div class=\"row\">";
	}
	private String getPastNews2() {
		return " </div>\r\n" + 
				"        </div>";
	}
	
	private void createAnn(HttpSession session, PrintWriter pw, int annID) {
		pw.print( getPart1(session) );
		
		try {
			
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from ANNOUNCEMENT where announcementID ="+annID);
			ResultSet rs = user.executeQuery();

			if (rs.next()) {
				String title = rs.getString("title");
				String category = rs.getString("category");
				String smallDesc = rs.getString("smallDesc");
				String fullDesc = rs.getString("fullDesc");
				int author = rs.getInt("author");
				java.sql.Date date = rs.getDate("postDate");
				
				pw.print( getAnn(title, smallDesc, fullDesc, date.toString(), author) );
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		
		pw.print(getPart2(session));
	}
	
	private String getAnn(String title, String small, String full, String date, int author) {
		String ann = "<div class=\"annoucementTitle\">";
		ann+=title;
		ann+="</div>\r\n" + 
				"                <div class=\"annoucementText text-start\">";
		ann+=small;
		ann+="</div>\r\n" + 
				"                <div style=\"background-color: #0E0E0F;\" class=\"text-start\">\r\n" + 
				"                	<div class=\"textAnnInfo\">";
		ann+=full;
		ann+="</div>\r\n" + 
				"                \r\n" + 
				"                </div>\r\n" + 
				"                <img id=\"imgAnnoucement2\" src=\"img/Announcement_banner2.png\">\r\n" + 
				"            </div>\r\n" + 
				"\r\n" + 
				"            <div class=\"annoucementDate\" >\r\n" + 
				"                <p>";
		ann+=date;
		ann+=" ---------------------- BY\r\n" + 
				"                    <span style=\"color: #fa2742;\">";
		ann+=UserInfo.getPlayerUsername(author);
		ann+="</span>";
		
		return ann;
	}
	
	private String getPart1(HttpSession session) {
		String isAdmin = "";
		String notLogged = "";
		String log = "";
		
		if (session.getAttribute("loggedIn") != null && (boolean)session.getAttribute("loggedIn")) {
			int userID = (int) session.getAttribute("userID");
			
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
				"                <li class=\"nav-item active\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navAnn\" href=\"index.jsp\">ANNOUNCEMENTS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navRanks\" href=\"leaderboards.jsp\">LEADERBOARDS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navMissions\" href=\"missions.jsp\">MISSIONS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item \">\r\n" + 
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
				"    <section id=\"annoucements\">\r\n" + 
				"\r\n" + 
				"        <div class=\"container\">\r\n" + 
				"\r\n" + 
				"            <div id=\"annoucementsText\">\r\n" + 
				"            ANNOUNCEMENT\r\n" + 
				"            </div>\r\n" + 
				"              \r\n" + 
				"            <div class=\"containerAnnoucements\">\r\n" + 
				"                <img id=\"imgAnnoucement\" src=\"img/Announcement_banner1.png\">";
	}
	
	private String getPart2(HttpSession session) {
		String s =  " </p>\r\n" + 
				"            </div>\r\n" + 
				"       \r\n" + 
				"        </div>\r\n" + 
				"        \r\n" + 
				"    </section>\r\n" + 
				"\r\n" + 
				"    \r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    <section id=\"footer\">\r\n" + 
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
				"\r\n";
		
				if (session.getAttribute("userID")!=null) {
					s += "<script>\r\n" + 
							"window.onload = function() {\r\n" + 
							"	\r\n" + 
							"	  languageOnLoad("+session.getAttribute("currentTheme")+", \"todo\");\r\n" + 
							"	  \r\n" + 
							"};\r\n" + 
							"</script>";
				}
		
				
				s+= "<script src=\"js/jquery.js\"></script>\r\n" + 
						"<script src=\"extras/bootstrap/js/bootstrap.bundle.min.js\"></script>\r\n" + 
						"<script src=\"js/jquery.easing.js\"></script>\r\n" + 
						"<script src=\"js/interface.js\"></script>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"</body>\r\n" + 
						"\r\n" + 
						"</html>";
				
			return s;
	}
	
	private void insertAnnBD(int userID, String title, String category, String small, String full) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			int id = 0;
			String lastID_query = "select announcementID from ANNOUNCEMENT order by announcementID DESC LIMIT 1;";
			PreparedStatement stmt_id = conn.prepareStatement(lastID_query);
			ResultSet rs_id = stmt_id.executeQuery();
			
			if (rs_id.next()) {
				id = Integer.parseInt(rs_id.getString("announcementID"))+1;
			}
		
			Date date = new Date();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			
			String query = "insert into ANNOUNCEMENT ("
					+ "announcementID, title, category, smallDesc, fullDesc, author, postDate) "
					+ "values ((?),(?),(?),(?),(?),(?),(?))";
			
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			stmt.setString(2, title);
			stmt.setString(3, category);
			stmt.setString(4, small);
			stmt.setString(5, full);
			stmt.setInt(6, userID);
			stmt.setDate(7, sqlDate);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}
	
	private String fromTextToHTML(String input) {
		
		String r1 = input.replace("[color=red]", "<span style='color:red;'>");
		String r2 = r1.replace("[color=yellow]", "<span style='color:yellow;'>");
		String r3 = r2.replace("[/color]", "</span>");
		String r4 = r3.replace("[img=", "<img src='");
		String r5 = r4.replace("/img]", "'>");
		String r6 = r5.replace("[color=blue]", "<span style='blue;'>");
		String r7 = r6.replace("[color=purple]", "<span style='purple;'>");
		String r8 = r7.replace("[color=pink]", "<span style='pink;'>");
		String r9 = r8.replace("[color=green]", "<span style='green;'>");
		String r10 = r9.replace("[color=orange]", "<span style='orange;'>");
		String r11 = r10.replace("[b]", "<b>");
		String r12 = r11.replace("[/b]", "</b>");
		String r13 = r12.replace("[i]", "<i>");
		String r14 = r13.replace("[/i]", "</i>");
		
		return r14;
	}
	
	private void lastAnnouncements(PrintWriter pw) {
		
		try {
			
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from ANNOUNCEMENT ORDER BY announcementID DESC LIMIT 4");
			ResultSet rs = user.executeQuery();
			int count = 0;
			while (rs.next()) {
				int annID = rs.getInt("announcementID");
				String title = rs.getString("title");
				String category = rs.getString("category");
				String smallDesc = rs.getString("smallDesc");
				String fullDesc = rs.getString("fullDesc");
				int author = rs.getInt("author");
				java.sql.Date date = rs.getDate("postDate");
				
				if (count==0) {
					pw.write(getMainAnn(annID, title, smallDesc, date.toString(), UserInfo.getPlayerUsername(author)));
					pw.write("break");
					pw.write( getPastNews1() );
				}
				else {
					pw.write( getPastNewsAnn(annID, date.toString(), category, title) );
				}
				
				count++;
			}
			pw.write( getPastNews2() );
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private String getPastNewsAnn(int annID, String date, String category, String title) {
		return "<div class=\"col\">\r\n" + 
				"                    <a href=\"Announcement?action=viewFull&id="+annID+"\"><img id=\"imgPastNews\" src=\"img/Past_news_thumbnail.png\"></a>\r\n" + 
				"                    <div class=\""+date+"\" >\r\n" + 
				"                        <p>29/07/2022 ---------------------- \r\n" + 
				"                            <span style=\"color: #fa2742;\">"+category+"</span>\r\n" + 
				"                        </p>\r\n" + 
				"                    </div>\r\n" + 
				"                    <div class=\"pastNewsTitle\">\r\n" + 
				"                       "+title+"\r\n" + 
				"                    </div>               \r\n" + 
				"                </div>";
	}
	
	private String getMainAnn(int annID, String title, String small, String date, String author) {
		
		
		return " <div class=\"container\">\r\n" + 
				"\r\n" + 
				"            <div id=\"annoucementsText\">\r\n" + 
				"            ANNOUNCEMENT\r\n" + 
				"            </div>\r\n" + 
				"              \r\n" + 
				"            <div class=\"containerAnnoucements\">\r\n" + 
				"                <a href=\"Announcement?action=viewFull&id="+annID+"\"><img id=\"imgAnnoucement\" src=\"img/Announcement_banner.png\"></a>\r\n" + 
				"                <div class=\"annoucementTitle\">"+title+"</div>\r\n" + 
				"                <div class=\"annoucementText\">"+small+"</div>\r\n" + 
				"            </div>\r\n" + 
				"\r\n" + 
				"            <div class=\"annoucementDate\" >\r\n" + 
				"                <p>"+date+" ---------------------- BY\r\n" + 
				"                    <span style=\"color: #fa2742;\">"+author+"</span>\r\n" + 
				"                </p>\r\n" + 
				"            </div>\r\n" + 
				"       \r\n" + 
				"        </div>";
		
	}

}
