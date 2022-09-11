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
    <meta http-equiv="content-type" content="text/html;charset=iso-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Anime-Arena">

    <title>Anime-Arena</title>
    
    <link href="extras/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-gH2yIJqKdNHPEq0n4Mqa/HGKIhSkIHeL5AyhkYV8i59U5AR6csBvApHHNl/vI1Bx" crossorigin="anonymous">
    <link href="css/site.css" rel="stylesheet">
    <link href="css/announcements.css" rel="stylesheet">
    <link href="css/help.css" rel="stylesheet">
	<script type="text/javascript" src="js/help.js"></script>
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
                <li class="nav-item">
                    <a class="nav-link navBarItem" id="navAnn" href="index.jsp">ANNOUNCEMENTS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" id="navRanks" href="leaderboards.jsp">LEADERBOARDS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" id="navMissions" href="missions.jsp">MISSIONS</a>
                </li>
                <li class="nav-item active">
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
                        <a class="dropdown-item" onclick="setLanguage('english', 'help')">
                            English
                        </a>
                        <a class="dropdown-item" onclick="setLanguage('portuguese', 'help')">
                            Português
                        </a>
                        <div class="dropdown-divider"></div>
                        <a id="navLogout" class="dropdown-item" href="logout.jsp">
                            <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
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

  	<section id="helpInfo">
  	
  		<ul class="list-inline">
            <li class="list-inline-item activeItem"><a id="navIngame" href="#">IN-GAME</a></li>
            <li class="list-inline-item"><a id="navChars" href="helpCharacters.jsp">CHARACTERS & SKILLS</a></li>
        </ul>
        
        <div class="group">
	         <ul class="list-inline characterCosmeticsStyle" style="background-color: #000000;">
	             <li id="nav-InGame" class="list-inline-item activeAnime"><a id="screenIngame" onclick="showInGame()">IN-GAME SCREENS</a></li>
	             <li id="nav-Energy" class="list-inline-item notActiveAnimeType"><a id="screenNature" onclick="showEnergy()">ENERGY TYPES</a></li>
	         </ul>
	         
	         <div id="textEnergy" style="display:none;">
	         	<div id="text1" class="text">
	         		Nature is used to perform skills.
	         		Every character has a unique set of skills and they use different nature combinations.
	         	</div>
	         </div>
	         
	        <div id="textInGame">
		        <div id="text2" class="text">
		        This section of the game manual is a basic overview of the user interface, gameplay and standard terminology within the game.
		        <br><br>
		        Anime-Arena is a turn based strategy game where your team of three characters are matched against another player´s team with the goal of reducing your opposition to 0 health points and winning the match.
		        </div> 
		        
		        <div class="beforeImage">
		        	<span style=" color: #fa2742;">></span> <span id="screenSelection">CHARACTER SELECTION SCREEN</span>
		        </div>
  			</div>
        </div>
  	
  		
  	</section>
  	
  	<section id="explanationEnergy" style="display:none;">
  	
	  	<div class="beforeImage2">
	       	<span style=" color: #fa2742;">></span> <span id="screenEnergy">NATURE TYPES EXPLAINED</span>
	    </div>
	    
	    <div class="row text groupRow text-center">
	    	<span id="exp1">
	    		The chance to get a certain nature is 25% each, as there are 4 types.
	    		This means its random, and that you can also get multiple natures of the same type.
	    		Next to these 4 types, some skills require random natures.
	    	</span>
	    
	    	<img src="img/natures.png">
	    	
	    	<span id="exp2">
	    		This kind of nature can be filled in by any of the types above.
	    	</span>
	    	
	    	<img src="img/rando.png" style="width:126px">
	    	
	    	
	    </div>
	    
	    
	    <div class="beforeImage2">
	       	<span style=" color: #fa2742;">></span> <span id="screenEnd">THE END OF YOUR TURN</span>
	    </div>
	
	    		<div id="exp3" class="text text-center">
	    			Once you have decided what skills your team will use and on whom,
	    			its time to end your turn.
	    			Do this by pressing "Press to end turn" button.
	    			This will bring you to the Skill Queue.
	    		</div>
	    		
	    		<div class="d-flex justify-content-center">
	    			<img class="big" src="img/randomEndTurn.png" style="margin-right:10px;">
    				<div class="group-form big" style="margin-left:10px;">
	            		<div class="blackBackground"></div>
			    		<div id="expText">
			    			This is where you will spend specific nature as random by clicking the up or down buttons next to each type.
			    			After spending the required nature cost, press the "OK" button to complete your turn.
			    			(note that this is still part of your turn and the timer will continue to run down while in this screen).
			    			If you want to reconsider or change something, you can press the "CANCEL" button to exit this screen.
			    			If you run out of time before completing your turn, your characters will not use any skills that turn.
						</div>
            		</div>
	    		</div>
	    		
	    			
	    		<div class="row text-center small">
	    			<img src="img/randomEndTurn.png" style="width: 392px; margin: auto; padding: 20px;">
	    		</div>
	    		<div class="row text-center small">
	    			<div class="group-formRes">
			    		<div id="expText2">
			    			This is where you will spend specific nature as random by clicking the up or down buttons next to each type.
			    			After spending the required nature cost, press the "OK" button to complete your turn.
			    			(note that this is still part of your turn and the timer will continue to run down while in this screen).
			    			If you want to reconsider or change something, you can press the "CANCEL" button to exit this screen.
			    			If you run out of time before completing your turn, your characters will not use any skills that turn.
						</div>
            		</div>
	    		</div>

        
  	</section>
    
    <section id="explanationInGame">
    
    	<div class="col text-center" style="padding:20px;">
    		<img src="img/helpSelection.png">
    		
    		<div class="row"><span id="justExample">THIS IS AN EXAMPLE, YOUR SCREEN MAY HAVE SLIGHTLY DIFFERENCES</span></div>
    		
    	</div>
    	
    	<div class="row groupRow">
    		<div class="col border border-dark ">
    			<div class="letters ">A</div>
    			<div id="expA" class="text">
					Shows your status: avatar, username, rank name, level, experience, ladder rank and your ratio.
		        </div> 
    		</div>
    		<div class="col border-top border-bottom border-dark">
    			<div class="letters ">B</div>
    			<div id="expB" class="text">
					Its where you define your team. You need to drag the characters in C or double-click them.
		        </div> 
    		</div>
    		<div class="col border border-dark">
    			<div class="letters ">C</div>
    			<div id="expC" class="text">
		          You can see all characters in the game and also the ones you have unlocked. 
		          You can not play with characters who have less opacity, means they are locked. 
		          To unlock them, you must complete their respective mission.
		        </div> 
    		</div>
    	</div>
    	
    		<div class="row groupRow">
    		<div class="col border border-top-0 border-dark">
    			<div class="letters">D</div>
    			<div id="expD" class="text">
		        When you click on a character in C, their information will be displayed here. 
		        It shows you the character and its abilities.
		        </div> 
    		</div>
    		<div class="col border-bottom border-dark">
    			<div class="letters ">E</div>
    			<div id="expE" class="text">
		        Shows you the information of the clicked character or abilities.
		        You can press on any square in D to change this text.
		        </div> 
    		</div>
    		<div class="col border border-top-0 border-dark">
    			<div class="letters ">F</div>
    			<div id="expF" class="text">
		        With this buttons you can logout or enter a battle.
		        Ladder game counts for ranking and missions.
		        Quick game only counts for missions.
		        Private game allows you to play with against a specific player, and does not count for missions nor ranking.
		        The buttons will be unclickable until you have a team formed.
		        Chose 3 characters and you´ll be able to enter battle.
		        </div> 
    		</div>
    	</div>
    	
    	<div class="beforeImage2">
        	<span style=" color: #fa2742;">></span> <span id="screenBattle">BATTLE SCREEN</span>
        </div>

		<div class="col text-center" style="padding:20px;">
    		<img src="img/helpBattle.png">
    		
    		<div class="row"><span id="justExample2">THIS IS AN EXAMPLE, YOUR SCREEN MAY HAVE SLIGHTLY DIFFERENCES</span></div>
    		
    	</div>
    	
    	<div class="row groupRow">
    		<div class="col border border-dark ">
    			<div class="letters ">A</div>
    			<div id="expAA" class="text">
					In the top of the page, you can see your avatar, username and ranking on the left part, just like the opponent you are facing.
					In between them, is shows you the amount of natures you currently have, as well as how you can end your turn.
					You can exchange 5 natures for 1 specific as a last resort.
		        </div> 
    		</div>
    		<div class="col border-top border-bottom border-dark">
    			<div class="letters ">B</div>
    			<div id="expBB" class="text">
					On the left part of the screen, your characters are displayed, along with a visual representation of your ranking. 
					Each ranking has their own image, and will change as often as your ranking.
		        </div> 
    		</div>
    		<div class="col border border-dark">
    			<div class="letters ">C</div>
    			<div id="expCC" class="text">
		          Your opponent´s team that you must defeat in order to be victorious. 
		          Just like in B, the image in the back of the characters represents your opponent´s ranking.
		        </div> 
    		</div>
    	</div>
    	
    		<div class="row groupRow">
    		<div class="col border border-top-0 border-dark">
    			<div class="letters">D</div>
    			<div id="expDD" class="text">
		       	Where you can apply your character´s abilities. 
		       	If they have lower opacity, it means you cannot use them that turn. 
		       	This can happen for several reasons like lack of natures, be on cooldown or the character is stunned.
		        Each character can only do 1 new ability per turn.
		        If you do decide to apply a ability, it will be displayed on the "?" holder.
		        If you wish to undo this action, you can do it by double clicking on the current ability.
		        </div> 
    		</div>
    		<div class="col border-bottom border-dark">
    			<div class="letters ">E</div>
    			<div id="expEE" class="text">
		        The information of the clicked element.
		        This will change according to what you wish to see, be it a player, character or ability.
		        </div> 
    		</div>
    		<div class="col border border-top-0 border-dark">
    			<div class="letters ">F</div>
    			<div id="expFF" class="text">
		        With this buttons, you can forfeit the match, visit our official discord and change the volume of the sound effects of the game.
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
  if ( <%=session.getAttribute("userID")%>!=null ) {
	  languageOnLoad(<%=session.getAttribute("currentTheme")%>, "help");
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