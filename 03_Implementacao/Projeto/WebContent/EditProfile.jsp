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
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Anime-Arena">

    <title>Anime-Arena</title>
    
    <link href="extras/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <link href="css/site.css" rel="stylesheet">
    <link href="css/announcements.css" rel="stylesheet">
    <link href="css/register.css" rel="stylesheet">
    <script type="text/javascript" src="js/settings.js"></script>


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
                    <a class="nav-link navBarItem" href="#">ANNOUNCEMENTS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" href="#">LEADERBOARDS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" href="missions.jsp">MISSIONS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" href="#">GAME INFO</a>
                </li>
                <li class="nav-item" id="admin" style="display:none;">
                    <a class="nav-link navBarItem dropdown-toggle" id="adminDropdown" role="button"
                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        ADMIN
                    </a>
                    <!-- Dropdown - User Information -->
                    <div class="dropdown-menu animated--grow-in "
                        aria-labelledby="adminDropdown">
                        <a class="dropdown-item" href="create.jsp">
                            Create Character
                        </a>
                    </div>
                </li>
            </ul>
			<ul class="nav navbar-nav ml-auto justify-content-end"  id="isLog">
            
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
                        <span class="welcome">Welcome, <%=username %> </span>
                        <img class="img-profile rounded-circle online" style="width: 50px; height: 50px;" src="ViewAvatar?id=<%=userID %>">
                    </a>
                    <!-- Dropdown - User Information -->
                    <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in "
                        aria-labelledby="userDropdown">
                        <a class="dropdown-item" href="ViewProfile?username=<%=username %>">
                            Profile
                        </a>
                        <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="Settings?action=settingsAvatar">
                            Change avatar
                        </a>
                        <a class="dropdown-item" href="Settings?action=settingsEmail">
                            Change e-mail
                        </a>
                        <a class="dropdown-item" href="Settings?action=settingsPassword">
                            Change password
                        </a>
                         <div class="dropdown-divider"></div>
                        <a class="dropdown-item" href="logout.jsp">
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
                <p>YOUR #1 ONLINE ANIME STRATEGIC GAME</p>
        </div>
    </section>

	<section id="settings">
		<div class="text-center">
    	
    		<div class="welcome-title">
    			Password Updated!
    		</div>
	    	
	    	
	    	<div class="col">
	    		<div class="group-form">
	    			<div class="blackBackground">.</div>
	    			<form  id="form" action="Settings">
		  				<input id="inpOld" type="password" name="inpOld" placeholder="Current password..." required><br>
		  				<input id="inpPass" type="password" name="inpPass" placeholder="New password..." 
			  				pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
			  				title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"
			  				required><br>
						<input id="inpConfirmPass" type="password" name="inpConfirmPass" placeholder="Confirm new password..."
							pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
			  				title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters"
			  				required><br>
			  			<input type="hidden" name="action" value="updatePassword">
						<div id="errorPassIncorrect"><span>Current password is not correct</span></div>
						<div id="errorPassNotMatch"><span>Passwords do not match</span></div>
						<div id="patternTitle"><span>Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters</span></div>
						<!-- <input type="image" alt="submit" src="img/login_register_icons/register.png"><br><br> -->
						<a onclick="correctPassword()"><img src="img/login_register_icons/register.png"></a><br><br>
					</form>
					
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
            <li class="list-inline-item"><a href="#">PRIVACY NOTICE</a></li>
            <li class="list-inline-item"><a href="#">TERMS OF SERVICE</a></li>
            <li class="list-inline-item"><a href="#">COPYRIGHT</a></li>
        </ul>

        <ul class="list-inline belowFooter">
            <li class="list-inline-item"><a href="#">Anime-Arena 2022</a></li>
            <li class="list-inline-item"><a href="#">All rights Reserved</a></li>
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
        document.getElementById("isLog").style.display="none";
    }
    else {
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

 <!-- Bootstrap core JavaScript-->
<script src="js/jquery.js"></script>
<script src="extras/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="js/jquery.easing.js"></script>
<script src="js/interface.js"></script>


</body>

</html>