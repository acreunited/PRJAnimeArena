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
                    <a class="nav-link navBarItem" href="index.jsp">ANNOUNCEMENTS</a>
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
                
            </ul>
            <ul class="navbar-nav justify-content-end" id="login">
				<li class="nav-item" id="login">
					<a class="nav-link navBarItem" href="login.jsp">LOGIN</a>
				</li>
			</ul>
			<ul class="navbar-nav justify-content-end" id="register">
				<li class="nav-item" id="register">
					<a class="nav-link navBarItem" href="register.jsp">REGISTER</a>
				</li>
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

   <section id="state">
   
		<div class="text-center">
		
			<%
			if ( ((String)session.getAttribute("email")).equalsIgnoreCase("valid")) {
			%>
			Your account is verified, you can start playing!
			<%
			}
			else {
			%>
			Thank you for registering, only 1 step left!<br>
			A verification email was sent to you. Please validade it, in order to start playing.
			<%
			}
			%>
		</div>
		
   </section>




 <!-- Bootstrap core JavaScript-->
<script src="js/jquery.js"></script>
<script src="extras/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="js/jquery.easing.js"></script>
<script src="js/interface.js"></script>


</body>

</html>