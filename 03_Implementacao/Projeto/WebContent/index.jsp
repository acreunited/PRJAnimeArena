<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="java.io.*"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="main.Connector"%>
<%@page import="users.UserInfo"%>


<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta http-equiv="content-type" content="text/html;charset=iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Anime-Arena">

    <title>Anime-Arena</title>
    
    <link href="extras/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <link href="css/site.css" rel="stylesheet">
    <link href="css/announcements.css" rel="stylesheet">
    <script type="text/javascript" src="js/index.js"></script>
    <script type="text/javascript" src="js/language.js"></script>


</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-faded">
        <a class="navbar-brand " href="#"><img id="adjustLeft" src="img/logo_small.png" style="width:50%;"></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto w-100 justify-content-center">
                <li class="nav-item active">
                    <a class="nav-link navBarItem" id="navAnn" href="index.jsp">ANNOUNCEMENTS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" id="navRanks" href="leaderboards.jsp">LEADERBOARDS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" id="navMissions" href="missions.jsp">MISSIONS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" id="navHelp" href="help.jsp">GAME INFO</a>
                </li>
                <li class="nav-item" id="admin" style="display:none;">
                    <a class="nav-link navBarItem dropdown-toggle" id="adminDropdown" role="button"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        ADMIN
                    </a>
                    <!-- Dropdown - User Information -->
                    <div class="dropdown-menu animated--grow-in "
                        aria-labelledby="adminDropdown">
                        <a id="navCchar" class="dropdown-item" href="create.jsp">
                            Create Character
                        </a>
                        <a id="navCann" class="dropdown-item" href="createAnnouncement.jsp">
                            Create Announcement
                        </a>
                        <a id="navCtheme" class="dropdown-item" href="createTheme.jsp">
                            Create Theme
                        </a>
                    </div>
                </li>
            </ul>
            <ul class="navbar-nav justify-content-end" id="login">
				<li class="nav-item" id="login">
					<a id="navLogin" class="nav-link navBarItem" href="login.jsp">LOGIN</a>
				</li>
			</ul>
			<ul class="navbar-nav justify-content-end" id="register">
				<li class="nav-item" id="register">
					<a id="navRegister" class="nav-link navBarItem" href="register.jsp">REGISTER</a>
				</li>
			</ul>
			
			<ul class="nav navbar-nav ml-auto justify-content-end"  id="isLog" style="display:none;">
            
            	<%
					Class.forName(Connector.drv);
					try (Connection conn = Connector.getConnection();) {
						Statement stmt = conn.createStatement();
						
						ResultSet rs = stmt.executeQuery("select * from USERS where userID="+session.getAttribute("userID"));
						if (rs.next()) {
							String username = rs.getString("username");
							String userID = rs.getString("userID");
					%>

                <li class="navbar-nav ml-auto">
                    <a class="nav-link dropdown-toggle" id="userDropdown" role="button"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <span id="navWelcome" class="welcome">Welcome, <%=username %> </span>
                        <img class="img-profile rounded-circle online" style="width: 50px; height: 50px;" src="ViewAvatar?id=<%=userID %>">
                    </a>
                    <!-- Dropdown - User Information -->
                    <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in "
                        aria-labelledby="userDropdown">
                        <a id="navProfile" class="dropdown-item" href="ViewProfile?username=<%=username %>">
                            Profile
                        </a>
                        <div class="dropdown-divider"></div>
                        <a id="navAvatar" class="dropdown-item" href="Settings?action=changeAvatar">
                            Change avatar
                        </a>
                        <a id="navBack" class="dropdown-item" href="Settings?action=changeBack">
                            Change backgrounds
                        </a>
                        <a id="navMail" class="dropdown-item" href="Settings?action=changeEmail">
                            Change e-mail
                        </a>
                        <a id="navPass" class="dropdown-item" href="Settings?action=changePassword">
                            Change password
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" onclick="setLanguage('english', 'index')">
                            English
                        </a>
                        <a class="dropdown-item" onclick="setLanguage('portuguese', 'index')">
                            Português
                        </a>
                        <div class="dropdown-divider"></div>
                        <a id="navLogout" class="dropdown-item" href="logout.jsp">
                            Logout
                        </a>
                    </div>
              	
                
                 <%
                }
				rs.close();
				} catch (SQLException | IOException e) {
				System.out.println(e.getMessage());
				}
				%>
            </ul>
        </div>
    </nav>


    <section id="play">
        <div class="text-center">

                <a href="selection.jsp"><img src="img/Buttons/play button.png"></a>
                <br>
                <img src="img/logo_full.png">
                <br>
                <p id="banner">YOUR #1 ONLINE ANIME STRATEGIC GAME</p>
        </div>
    </section>


    <section id="annoucements">

        <div class="container">

            <div id="annoucementsText">
            ANNOUNCEMENT
            </div>
              
            <div class="containerAnnoucements">
                <a href="Announcement?action=viewFull&id=0"><img id="imgAnnoucement" src="img/Announcement_banner.png"></a>
                <div class="annoucementTitle">Annoucement Title</div>
                <div class="annoucementText">Description of the Annoucement</div>
            </div>

            <div class="annoucementDate" >
                <p>29/07/2022 ---------------------- BY
                    <span style="color: #fa2742;">AXE</span>
                </p>
            </div>
       
        </div>
        
    </section>

    <section id="pastNews">

        <div class="container">
          
            <div id="pastNewsBigText">
            PAST NEWS
            </div>

            <div class="row">
                <div class="col">
                    <img id="imgPastNews" src="img/Past_news_thumbnail.png">
                    <div class="annoucementDate" >
                        <p>29/07/2022 ---------------------- 
                            <span style="color: #fa2742;">BALANCE UPDATE</span>
                        </p>
                    </div>
                    <div class="pastNewsTitle">
                        OLDER UPDATE TITLE GOES HERE
                    </div>               
                </div>

                <div class="col">
                    <img id="imgPastNews" src="img/Past_news_thumbnail.png">
                    <div class="annoucementDate" >
                        <p>29/07/2022 ---------------------- 
                            <span style="color: #fa2742;">NEW CHARACTERS</span>
                        </p>
                    </div>
                    <div class="pastNewsTitle">
                        OLDER UPDATE TITLE GOES HERE
                    </div>               
                </div>

                <div class="col">
                    <img id="imgPastNews" src="img/Past_news_thumbnail.png">
                    <div class="annoucementDate" >
                        <p>29/07/2022 ----------------------
                            <span style="color: #fa2742;">NEW COSMETICS</span>
                        </p>
                    </div>
                    <div class="pastNewsTitle">
                        OLDER UPDATE TITLE GOES HERE
                    </div>
                </div>
            </div>
        </div>

    </section>
    



    <section id="footer">
        <ul class="list-inline">
            <li class="list-inline-item"><a href="#"><img src="img/Buttons/Paypal.png"></a></li>
            <li class="list-inline-item"><a href="#"><img src="img/Buttons/Discord.png"></a></li>
        </ul>


        <ul class="list-inline aboveFooter">
            <li class="list-inline-item"><a id="footerPrivacy" href="#">PRIVACY NOTICE</a></li>
            <li class="list-inline-item"><a id="footerTerms" href="#">TERMS OF SERVICE</a></li>
            <li class="list-inline-item"><a id="footerCopyright" href="#">COPYRIGHT</a></li>
        </ul>

        <ul class="list-inline belowFooter">
            <li class="list-inline-item"><a href="#">Anime-Arena 2022</a></li>
            <li class="list-inline-item"><a id="footerReserved" href="#">All rights reserved</a></li>
        </ul>
    </section>

<script type="text/javascript">
function displayUsers(tipoUser) {
	console.log(tipoUser);
    if (tipoUser=="administrador") {
       /* document.getElementById("players").style.display="block";*/
        document.getElementById("admin").style.display="block";
    }
    else if (tipoUser=="player") {
        /*document.getElementById("players").style.display="block";*/
        document.getElementById("admin").style.display="none";
    }
    else {
      /*  document.getElementById("players").style.display="none";*/
        document.getElementById("admin").style.display="none";
    }
}

function displayLogged(isLog) {
	
	if (isLog=="null" || isLog=="false") {
		document.getElementById("login").style.display="block";
		document.getElementById("register").style.display="block";
        document.getElementById("isLog").style.display="none";
    }
    else {
    	document.getElementById("isLog").style.display="block";
    	document.getElementById("login").style.display="none";
    	document.getElementById("register").style.display="none";
    }
}
</script>

<script>
	var tipo = "<%=(String) session.getAttribute("tipoUser")%>";
	
	if (tipo!=null) {
		displayUsers( tipo );
	}
	
	var isLogin = "<%=session.getAttribute("loggedIn")%>";

	displayLogged(isLogin);
	
</script>

<script>
window.onload = function() {
	
  createAnn();
  if ( <%=session.getAttribute("userID")%>!=null ) {
	  languageOnLoad(<%=session.getAttribute("currentTheme")%>, "index");
  }
	  
};
</script>
 <!-- Bootstrap core JavaScript-->
<script src="js/jquery.js"></script>
<script src="extras/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="js/jquery.easing.js"></script>
<script src="js/interface.js"></script>


</body>

</html>