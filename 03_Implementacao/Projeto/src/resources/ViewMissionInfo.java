package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.Connector;
import users.UserInfo;

@WebServlet("/ViewMissionInfo")
public class ViewMissionInfo extends HttpServlet {
	
	private static final long serialVersionUID = 7215979604673189309L;
	
	public ViewMissionInfo() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		
		HttpSession session = request.getSession();
		PrintWriter pw = response.getWriter(); 
		

		if (session.getAttribute("userID")!=null) {
			
			int userID = (int) session.getAttribute("userID");
			int missionID = Integer.parseInt(request.getParameter("id"));
			//session.setAttribute("", value);
			String animeType = request.getParameter("anime");
			try {

				Connection con = Connector.getConnection();
				PreparedStatement st = con.prepareStatement("select * from MISSION where missionID=?");
				st.setInt(1, missionID);
				ResultSet rs = st.executeQuery();

				if (rs.next()) {
					String missionName = rs.getString("nome");
					int minLvl = rs.getInt("minimumLevel");
					int characterID = rs.getInt("characterID");
					
					PreparedStatement st_char = con.prepareStatement("select * from THEME_CHARACTER where themeID=1 and characterID=?");
					st_char.setInt(1, characterID);
					ResultSet rs_char = st_char.executeQuery();
					if (rs_char.next()) {
						String charName = rs_char.getString("nome");
						
						createPage(pw, session, animeType, missionID, missionName, minLvl, characterID, charName);
					}
					
				} 
				rs.close();
				st.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		else {
			response.sendRedirect("login.jsp");
		}
		

	}
	
	private int getMissionID(String name) {
		System.out.println(name);
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st_char = con.prepareStatement("select * from MISSION where nome=?");
			st_char.setString(1, name);
			ResultSet rs_char = st_char.executeQuery();
			if (rs_char.next()) {
				return rs_char.getInt("missionID");
			}
			rs_char.close();
			st_char.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Couldnt find mission id with name");
		return -1;
	}
	
	private void defineRequirements(HttpSession session, int userID, int missionID) {

		int nChars = nCharsAnime(userID, missionID, "character");
		int nAnime = nCharsAnime(userID, missionID, "anime");
		
		session.setAttribute("mission_nChars", nChars);
		session.setAttribute("mission_nAnime", nAnime);
		
		if (nChars > 0) {
			ArrayList<String> charsReq = getCharsReq(userID, missionID);
			//ArrayList<String> charsText = new ArrayList<String>();
			int count = 0;
			for (int i = 0; i < charsReq.size(); i+=4) {
				String row = "";
				if (charsReq.get(i+3).equalsIgnoreCase("true")) {
					row += "in a row ";
				}
				
				String text = "Win "+charsReq.get(i+2)+" battles "+row+"with "+getCharName(charsReq.get(i))+
						" ("+charsReq.get(i+1)+"/"+charsReq.get(i+2)+")";

				session.setAttribute("mission_reqChars"+count, text);
				count++;
			}
			//session.setAttribute("mission_charsText", charsText);
		}
		if (nAnime > 0) {
			ArrayList<String> animeReq = getAnimeReq(userID, missionID);
			String row = "";
			if (animeReq.get(3).equalsIgnoreCase("true")) {
				row += " in a row ";
			}
			String text = "Win "+animeReq.get(2)+""+row+" battles with any character from "+
					getAnime(animeReq.get(0))+" ("+animeReq.get(1)+"/"+animeReq.get(2)+")";
			
			session.setAttribute("mission_animeText", text);
					
		}
	}
	
	private String getAnime(String anime) {
		if (anime.equalsIgnoreCase("demonslayer")) {
			return "Demon Slayer";
		}
		else if (anime.equalsIgnoreCase("hunterxhunter")) {
			return "Hunter X Hunter";
		}
		else if (anime.equalsIgnoreCase("bleach")) {
			return "Bleach";
		}
		return "error get anime";
	}
	private String getCharName(String charID) {
		int characterID = Integer.parseInt(charID);
		try {

			Connection con = Connector.getConnection();
			PreparedStatement st_char = con.prepareStatement("select * from THEME_CHARACTER where themeID=1 and characterID=?");
			st_char.setInt(1, characterID);
			ResultSet rs_char = st_char.executeQuery();
			if (rs_char.next()) {
				return rs_char.getString("nome");
			}
			rs_char.close();
			st_char.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private ArrayList<String> getAnimeReq(int userID, int missionID) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String filename = "D:\\GitHub\\Projeto\\missions"+userID+".xml";
		ArrayList<String> animeReq = new ArrayList<String>();
		
		try (InputStream is = new FileInputStream(filename)) {
			DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(is);
            
            NodeList listOfMissions = doc.getElementsByTagName("mission");	            
            for (int i = 0; i < listOfMissions.getLength(); i++) {
            	Node mission = listOfMissions.item(i);
            	
            	String mID = mission.getAttributes().getNamedItem("id").getTextContent();
            	if (Integer.parseInt(mID)==missionID) {
            		NodeList missionInfo = mission.getChildNodes();
            		for (int j = 0; j < missionInfo.getLength(); j++) {
            			Node charRequirement = missionInfo.item(j);
            			
            			if (charRequirement.getNodeType() == Node.ELEMENT_NODE) {
            				if (charRequirement.getNodeName().equalsIgnoreCase("anime")) {
            					String current = charRequirement.getAttributes().getNamedItem("current").getTextContent();	
            					String need = charRequirement.getAttributes().getNamedItem("need").getTextContent();
            					String animeType = charRequirement.getAttributes().getNamedItem("type").getTextContent();
            					String row = charRequirement.getAttributes().getNamedItem("row").getTextContent();
            					animeReq.add(animeType);
            					animeReq.add(current);
            					animeReq.add(need);
            					animeReq.add(row);
            					return animeReq;
            				}
            			}
            		}
            	}
            
            
            }
            
            
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	private int nCharsAnime(int userID, int missionID, String charAnime) {
		Document document = readDocument("D:\\GitHub\\Projeto\\missions"+userID+".xml");
		document.getDocumentElement().normalize();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String anime = "count(//mission[@id="+missionID+"]/"+charAnime+")";
		String value = null;
		
		try {
			value = (String) xpath.evaluate(anime, document, XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return Integer.parseInt(value);
		
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
					"                <li class=\"nav-item active\">\r\n" + 
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
					"    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx\" crossorigin=\"anonymous\">\r\n" + 
					"    <link href=\"css/site.css\" rel=\"stylesheet\">\r\n" + 
					"    <link href=\"css/missions.css\" rel=\"stylesheet\">\r\n" + 
					"    <link href=\"css/missionInfo.css\" rel=\"stylesheet\">\r\n" + 
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
					"    </section>\r\n";
		}
	
	
	private String createHead() {
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
				"\r\n" + 
				"    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx\" crossorigin=\"anonymous\">\r\n" + 
				"    <link href=\"css/site.css\" rel=\"stylesheet\">\r\n" + 
				"    <link href=\"css/missions.css\" rel=\"stylesheet\">\r\n" + 
				"    <link href=\"css/missionInfo.css\" rel=\"stylesheet\">\r\n" + 
				"    <script type=\"text/javascript\" src=\"js/language.js\"></script>" +
				"\r\n" + 
				"\r\n" + 
				"</head>";
	}
	
	private String createNav(HttpSession session, int userID) {
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
					"                        <a id=\"navProfile\" class=\"dropdown-item\" href=\"ViewProfile?username="+UserInfo.getPlayerUsername(userID);
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
					"                        <a class=\"dropdown-item\" onclick=\"setLanguage('english', 'missionInfo')\">\r\n" + 
					"                            English\r\n" + 
					"                         </a>\r\n" + 
					"                         <a class=\"dropdown-item\" onclick=\"setLanguage('portuguese', 'missionInfo')\">\r\n" + 
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
				"                <li class=\"nav-item active\">\r\n" + 
				"                    <a class=\"nav-link navBarItem\" id=\"navMissions\" href=\"missions.jsp\">MISSIONS</a>\r\n" + 
				"                </li>\r\n" + 
				"                <li class=\"nav-item\">\r\n" + 
				"                	<a class=\"nav-link navBarItem\" id=\"navHelp\" href=\"help.jsp\">GAME INFO</a>\r\n" + 
				"                </li>\r\n" + 
				isAdmin+
				"            </ul>\r\n" + 
				notLogged+
				"			\r\n" + 
				log+
				"        </div>\r\n" + 
				"    </nav>";
		
		return nav;
	}
	
	private String createPlay() {
		return "<section id=\"play\">\r\n" + 
				"        <div class=\"text-center\">\r\n" + 
				"\r\n" + 
				"                <img src=\"img/Buttons/play button.png\">\r\n" + 
				"                <br>\r\n" + 
				"                <img src=\"img/logo_full.png\">\r\n" + 
				"                <br>\r\n" + 
				"                <p id=\"banner\">YOUR #1 ONLINE ANIME STRATEGIC GAME</p>\r\n" + 
				"        </div>\r\n" + 
				"    </section>";
	}
	
	private String createCharacterCosmetics(String animeType) {
		String characterCosmetics = " <section id=\"characterCosmetics\">\r\n" + 
				"		<br>\r\n" + 
				"		\r\n" + 
				"        <div class=\"groupAnimesInfo\">\r\n" + 
				"            <ul class=\"list-inline characterCosmeticsStyle\" style=\"background-color: #000000;\">";
		
		if (animeType.equalsIgnoreCase("hunter")) {
			characterCosmetics += "<li class=\"list-inline-item activeAnime\"><a href=\"#\">HUNTER X HUNTER</a></li>";
		}
		else {
			characterCosmetics += "<li class=\"list-inline-item notActiveAnimeType\"><a href=\"#\">HUNTER X HUNTER</a></li>";
		}
		
		if (animeType.equalsIgnoreCase("bleach")) {
			characterCosmetics += "<li class=\"list-inline-item activeAnime\"><a href=\"#\">BLEACH</a></li>";
		}
		else {
			characterCosmetics += "<li class=\"list-inline-item notActiveAnimeType\"><a href=\"#\">BLEACH</a></li>";	
		}
		
		if (animeType.equalsIgnoreCase("demonslayer")) {
			characterCosmetics += "<li class=\"list-inline-item activeAnime\"><a href=\"#\">DEMON SLAYER</a></li>";
		}
		else {
			characterCosmetics += "<li class=\"list-inline-item notActiveAnimeType\"><a href=\"#\">DEMON SLAYER</a></li>";
		}
		
		characterCosmetics += "</ul>\r\n" + 
				"        </div>\r\n" + 
				"\r\n" + 
				"    </section>";
		
		return characterCosmetics;
	}
	
	private String createInfo(int userID, int minLvl, String charName, int missionID, String missionName, int characterID) {
		
		String lanMissionName = (UserInfo.getCurrentTheme(userID)==2) ? "Nome da Missão: " : "Mission Name: ";
		String lanMissionLevel = (UserInfo.getCurrentTheme(userID)==2) ? "Nível da Missão: " : "Mission Level: ";
		String lanRequisitos = (UserInfo.getCurrentTheme(userID)==2) ? "> REQUISITOS" : "> REQUIREMENTS";
		String lanInformation = (UserInfo.getCurrentTheme(userID)==2) ? "> INFORMAÇÃO" : "> INFORMATION";
		String lanRank = (UserInfo.getCurrentTheme(userID)==2) ? "Pelo menos" : " At least";
		String lanGoals = (UserInfo.getCurrentTheme(userID)==2) ? "> OBJETIVOS" : "> GOALS";
		String lanReward = (UserInfo.getCurrentTheme(userID)==2) ? "> RECOMPENSA" : "> REWARD";
		String lanChar = (UserInfo.getCurrentTheme(userID)==2) ? "Personagem desbloqueada:" : "Character unlocked:";
		
		
		String info = "<section id=\"info\">\r\n" + 
				"\r\n" + 
				"        <div class=\"groupInfo text-center\">\r\n" + 
				"\r\n" + 
				"            <div class=\"container py-4\">\r\n" + 
				"                <div class=\"row py-4\" >\r\n" + 
				"                  <div class=\"col-lg-4 col-md-6 mb-4 mb-lg-0\" style=\"margin-top: -24px;\">\r\n" + 
				"                    <div class=\"requiredLevelInfo\">";
		
		info += "LEVEL "+minLvl+" </div>  \r\n" + 
				"                    <div class=\"characterNameInfo\">"+charName+"</div>";
		
		info += "<img src=\"ViewMission?id="+missionID+"\">\r\n" + 
				"                  </div>";
		
		info += "<div class=\"col-lg-2 col-md-6 mb-4 mb-lg-0\">\r\n" + 
				"                    <div class=\"informationTitle mb-4\""+lanInformation+"</div>\r\n" + 
				"                    <ul class=\"list-unstyled mb-0\">\r\n" + 
				"                      <li class=\"mb-2\"><a class=\"information\" id=\"mName\">"+lanMissionName+"<b>"+missionName+"</b></a></li>\r\n" + 
				"                      <li class=\"mb-2\"><a class=\"information\">"+lanMissionLevel+"<b>"+minLvl+"</b></a></li>\r\n" + 
				"                    </ul>\r\n" + 
				"                    <br><br><br>\r\n" + 
				"                    <div class=\"informationTitle mb-4\">"+lanRequisitos+" </div>\r\n" + 
				"                    <ul class=\"list-unstyled mb-0\">\r\n" + 
				"                    <li class=\"mb-2\"><a class=\"information\">RANK: "+lanRank+" <b><i>"+UserInfo.getRankName(minLvl)+"</i></b></a></li>\r\n" + 
				"                    </ul>\r\n" + 
				"                  </div>";
		
		info += "<div class=\"col-lg-2 col-md-6 mb-4 mb-lg-0\">\r\n" + 
				"                    <div class=\"informationTitle mb-4\""+lanGoals+"\r\n" + 
				"                    </div>\r\n" + 
				"                    <ul class=\"list-unstyled mb-0\" id=\"requirementsUpdate\">";
	
		
		info += getRequirements(userID, missionID);
		
		
		
		info +="</ul>\r\n" + 
				"                  </div>";
		
		info += "<div class=\"col-lg-4 col-md-6 mb-lg-0\">\r\n" + 
				"                    <div class=\"informationTitle mb-4\">"+lanReward+"\r\n" + 
				"                    </div>\r\n" + 
				"                    <ul class=\"list-unstyled mb-0\">\r\n" + 
				"                      <li class=\"mb-2\"><a class=\"information\">"+lanChar+"</a></li>\r\n" + 
				"                      <li class=\"mb-2\"> <img src=\"ViewCharacter?id="+characterID+"\"> </li>\r\n" + 
				"                      <li class=\"mb-2\"><a class=\"information\"><b> "+charName+" </b></a></li>\r\n" + 
				"                    </ul>\r\n" + 
				"                  </div>\r\n" + 
				"                </div>\r\n" + 
				"              </div></div>\r\n" + 
				"        \r\n" + 
				"    </section>";
		
		return info;
	}
	
	private String getRequirements(int userID, int missionID) {
		int nChars = nCharsAnime(userID, missionID, "character");
		int nAnime = nCharsAnime(userID, missionID, "anime");
		
		//session.setAttribute("mission_nChars", nChars);
		//session.setAttribute("mission_nAnime", nAnime);
		
		String req = "";
		
		if (nChars > 0) {
			ArrayList<String> charsReq = getCharsReq(userID, missionID);
			//ArrayList<String> charsText = new ArrayList<String>();
			//int count = 0;
			for (int i = 0; i < charsReq.size(); i+=4) {
				
			
				req += "<li class=\"mb-2\"><a class=\"information\">";
				
				String row = "";
				if (charsReq.get(i+3).equalsIgnoreCase("true")) {
					if (UserInfo.getCurrentTheme(userID)==2) {
						row += "seguidas ";
					}
					else {
						row += "in a row ";
					}
				}
				
				String text = "";
				if (UserInfo.getCurrentTheme(userID)==2) {
					text += "Vencer "+charsReq.get(i+2)+" batalhas "+row+"com "+getCharName(charsReq.get(i))+
							" ("+charsReq.get(i+1)+"/"+charsReq.get(i+2)+")";
				}
				else {
					text = "Win "+charsReq.get(i+2)+" battles "+row+"with "+getCharName(charsReq.get(i))+
							" ("+charsReq.get(i+1)+"/"+charsReq.get(i+2)+")";
				}
				
				
				
				req += text+" </a></li>";

				//session.setAttribute("mission_reqChars"+count, text);
				//count++;
			}
			//session.setAttribute("mission_charsText", charsText);
		}
		if (nAnime > 0) {
			ArrayList<String> animeReq = getAnimeReq(userID, missionID);
			
			req += "<li class=\"mb-2\"><a class=\"information\">";
			
			String row = "";
			if (animeReq.get(3).equalsIgnoreCase("true")) {
				if (UserInfo.getCurrentTheme(userID)==2) {
					row += "seguidas ";
				}
				else {
					row += "in a row ";
				}
			}
			
			String text = "";
			if (UserInfo.getCurrentTheme(userID)==2) {
				text += "Vencer "+animeReq.get(2)+""+row+" batalhas com qualquer personagem do anime "+
						getAnime(animeReq.get(0))+" ("+animeReq.get(1)+"/"+animeReq.get(2)+")";
			}
			else {
				text = "Win "+animeReq.get(2)+""+row+" battles with any character from "+
						getAnime(animeReq.get(0))+" ("+animeReq.get(1)+"/"+animeReq.get(2)+")";
			}
			
			req += text+" </a></li>";
			//session.setAttribute("mission_animeText", text);
					
		}
		
		return req;
	}
	
	private String createFooter() {
		return "    <section id=\"footer\">\r\n" + 
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
				"            <li class=\"list-inline-item\"><a id=\"footerReserved\" href=\"#\">All rights reserved</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"    </section>";
	}
	
	private String createScrollUp() {
		return "        <!-- Scroll to Top Button-->\r\n" + 
				"    <a class=\"scroll-to-top rounded\" href=\"#page-top\">\r\n" + 
				"        <i class=\"fas fa-angle-up\"></i>\r\n" + 
				"    </a>";
	}
	
	private String createScripts(HttpSession session) {
		String scripts = "<script type=\"text/javascript\">\r\n" + 
				"\r\n" + 
				"function displayUsers(tipoUser) {\r\n" + 
				"    if (tipoUser==\"administrador\") {\r\n" + 
				"        document.getElementById(\"admin\").style.display=\"block\";\r\n" + 
				"    }\r\n" + 
				"    else if (tipoUser==\"player\") {\r\n" + 
				"        document.getElementById(\"admin\").style.display=\"none\";\r\n" + 
				"    }\r\n" + 
				"    else {\r\n" + 
				"        document.getElementById(\"admin\").style.display=\"none\";\r\n" + 
				"    }\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"</script>";
		
		String tipoUser = (String) session.getAttribute("tipoUser");
		
		scripts+= "<script>\r\n" + 
				"	var tipo = \""+tipoUser+"\";\r\n" + 
				"	\r\n" + 
				"	if (tipo!=null) {\r\n" + 
				"		displayUsers( tipo );\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"</script>";
		
		if (session.getAttribute("userID")!=null) {
			scripts += "<script>\r\n" + 
					"window.onload = function() {\r\n" + 
					"	\r\n" + 
					"	  languageOnLoad("+session.getAttribute("currentTheme")+", \"missionInfo\");\r\n" + 
					"	  \r\n" + 
					"};\r\n" + 
					"</script>";
		}

		
		return scripts;
	}
	
	private String createEnd() {
		return "<script src=\"js/jquery.js\"></script>\r\n" + 
				"<script src=\"extras/bootstrap/js/bootstrap.bundle.min.js\"></script>\r\n" + 
				"<script src=\"js/jquery.easing.js\"></script>\r\n" + 
				"<script src=\"js/interface.js\"></script>\r\n" + 
				"\r\n" + 
				"</body>\r\n" + 
				"</html>";
	}
 	private void createPage(PrintWriter pw, HttpSession session,
			String animeType, int missionID, String missionName, int minLvl, int characterID, String charName) {

		int userID = (int) session.getAttribute("userID");
		
		//pw.print(createHead());
		//pw.print(createNav(session, userID));
		//pw.print(createPlay());
		pw.print(getPart1(session, userID));
		pw.print(createCharacterCosmetics(animeType));
		pw.print(createInfo(userID, minLvl, charName, missionID, missionName, characterID));
		pw.print(createFooter());
		pw.print(createScrollUp());
		pw.print(createScripts(session));
		
		//TODO ONBEFOREUNLOAD MAYBE
		
		pw.print(createEnd());
	}
	
	private ArrayList<String> getCharsReq(int userID, int missionID) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String filename = "D:\\GitHub\\Projeto\\missions"+userID+".xml";
		
		ArrayList<String> charsReq = new ArrayList<String>();
		
		try (InputStream is = new FileInputStream(filename)) {
			DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(is);
            
            NodeList listOfMissions = doc.getElementsByTagName("mission");	            
            for (int i = 0; i < listOfMissions.getLength(); i++) {
            	Node mission = listOfMissions.item(i);
            	
            	String mID = mission.getAttributes().getNamedItem("id").getTextContent();
            	if (Integer.parseInt(mID)==missionID) {
            		NodeList missionInfo = mission.getChildNodes();
            		for (int j = 0; j < missionInfo.getLength(); j++) {
            			Node charRequirement = missionInfo.item(j);
            			
            			if (charRequirement.getNodeType() == Node.ELEMENT_NODE) {
            				if (charRequirement.getNodeName().equalsIgnoreCase("character")) {
            					String current = charRequirement.getAttributes().getNamedItem("current").getTextContent();	
            					String need = charRequirement.getAttributes().getNamedItem("need").getTextContent();
            					String charID = charRequirement.getAttributes().getNamedItem("characterID").getTextContent();
            					String row = charRequirement.getAttributes().getNamedItem("row").getTextContent();
            					charsReq.add(charID);
            					charsReq.add(current);
            					charsReq.add(need);
            					charsReq.add(row);
            				}
            			}
            		}
            	}
            
            
            }
            
            
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return charsReq;
	}
	
	private Document readDocument(String input) {
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      Document doc=null;
	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         doc = builder.parse(input);
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	      return doc;
	}
	
	private Document readDocument(InputStream input) {
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      Document doc=null;
	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         doc = builder.parse(input);
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	      return doc;
	}


}
