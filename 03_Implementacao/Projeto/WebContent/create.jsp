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
<%@ page import="main.CreateCharacterMission"%>
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
    


</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark bg-faded">
        <a class="navbar-brand " href="#"><img id="adjustLeft" src="img/logo_small.png" style="width:50%;"></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto w-100 justify-content-center">
                <li class="nav-item ">
                    <a class="nav-link navBarItem" href="index.jsp">ANNOUNCEMENTS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" href="#">LEADERBOARDS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" href="missions.jsp">MISSIONS</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link navBarItem" href="help.jsp">GAME INFO</a>
                </li>
                <li class="nav-item active" id="admin" style="display:none;">
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
					<a class="nav-link navBarItem" href="login.jsp">LOGIN</a>
				</li>
			</ul>
			<ul class="navbar-nav justify-content-end" id="register">
				<li class="nav-item" id="register">
					<a class="nav-link navBarItem" href="register.jsp">REGISTER</a>
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
                        <a class="dropdown-item" href="Settings?action=changeAvatar">
                            Change avatar
                        </a>
                        <a class="dropdown-item" href="Settings?action=changeEmail">
                            Change e-mail
                        </a>
                        <a class="dropdown-item" href="Settings?action=changePassword">
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
    
    <section id="createContent">

                <div class="container-fluid">
                
                <form class="modal-content animate" method="post"
					enctype="multipart/form-data"
					action="${pageContext.request.contextPath}/CreateCharacterMission"
				>
		
					<div class="container">
		
						<div class="container">
							<label for="characterName">
								<b>Character Name</b>
							</label>
							<input type="text" placeholder="Character Name" name="characterName"
								pattern="[A-Za-z\s]{1,32}" required
							/>
						</div>
						<div class="container">
							<label for="characterDescription">
								<b>Character Description</b>
							</label>
							<textarea placeholder="Write Character Description (max 5000chars)" name="characterDescription"
								style="min-height: 200px; width: 100%;" maxlength="5000" required
							></textarea>
						</div>
		
						<div class="container">
							<label for="charPic">Character Picture</label>
							<input type="file" accept="image/*" name="charPic" required />
						</div>
						<br>
						<br>
						
						<%for (int i = 1; i <= 4; i++) { %>
						
						<div class="container">
							<label for="abilityName<%=i%>">
								<b>Ability<%=i%> Name</b>
							</label>
							<input type="text" placeholder="Ability Name" name="ability<%=i%>"
								pattern="[A-Za-z\s]{1,32}" required
							/>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>Description">
								<b>Ability<%=i%> Description</b>
							</label>
							<textarea placeholder="Write Ability <%=i%> Description (max 5000chars)" name="ability<%=i%>Description"
								style="min-height: 200px; width: 100%;" maxlength="5000" required
							></textarea>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>target">Who is the target of this ability?</label>
							<select name="ability<%=i%>target" id="ability<%=i%>target" required>
								<option value="self">Self</option>
								<option value="enemy">Enemy</option>
								<option value="allEnemies">All Enemies</option>
								<option value="ally">Ally</option>
								<option value="allAllies">All Allies</option>
								<option value="allTeam">All Team</option>
								<option value="allyORself">Ally or Self</option>
								
							</select>
						</div>
						<div class="container">
							<label for="ability<%=i%>damage">Does this ability do damage?</label>
							<select name="ability<%=i%>damage" id="ability<%=i%>damage" required onchange="ability<%=i%>DoesDamage(this)">
								<option value="no">No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>DoesDamage" style="display: none;">
								<label for="ability<%=i%>damageNumber">Damage Value:</label>
								<input type="number" name="ability<%=i%>damageNumber" min="0" max="1000">
								<label for="ability<%=i%>damageDuration">Turn duration:</label>
								<input type="number" name="ability<%=i%>damageDuration" min="0" max="1000">
							</div>	
							<br>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>increaseAbilityDamage">Ability damage increase per use (0 if none)</label>
							<input type="number" name="ability<%=i%>increaseAbilityDamage" min="0" max="1000" required>
							<br>
							<label for="ability<%=i%>HealIncreasePerUse">Heal Increase Per Use (0 if none)</label>
							<input type="number" name="ability<%=i%>HealIncreasePerUse" min="0" max="1000" required>
							<br>
							<label for="ability<%=i%>increasePermanentDamage">Permanent Character increase damage (0 if none)</label>
							<input type="number" name="ability<%=i%>increasePermanentDamage" min="0" max="1000" required>
							<br>
							<label for="ability<%=i%>stun">Stun Duration (0 if none)</label>
							<input type="number" name="ability<%=i%>stun" min="0" max="1000" required>
							<br>
							<label for="ability<%=i%>beInvul">How many turns this ability makes character invulnerable (0 if none)</label>
							<input type="number" name="ability<%=i%>beInvul" min="0" max="1000" required>
							<br>							
							<label for="ability<%=i%>ignoreInvul">Does this ability ignore invulnerability?</label>
							<select name="ability<%=i%>ignoreInvul" id="ability<%=i%>ignoreInvul" required>
								<option value="false" >No</option>
								<option value="true">Yes</option>
							</select>
							<br>
							
						
						</div>
				
						<div class="container">
							<label for="ability<%=i%>removeNature">Does this ability remove Natures?</label>
							<select name="ability<%=i%>removeNature" id="ability<%=i%>removeNature" required onchange="ability<%=i%>removesNature(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>

							<div id="ability<%=i%>removesNature" style="display: none;">
								<label for="ability<%=i%>removesNatureNumber">How Many?:</label>
								<input type="number" name="ability<%=i%>removesNatureNumber" min="0" max="1000">
								<label for="ability<%=i%>removesNatureDuration">Turn duration:</label>
								<input type="number" name="ability<%=i%>removesNatureDuration" min="0" max="1000">
							</div>	
							<br>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>gainNature">Does this ability gain Natures?</label>
							<select name="ability<%=i%>gainNature" id="ability<%=i%>gainNature" required onchange="ability<%=i%>gainsNature(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>gainsNature" style="display: none;">
								<label for="ability<%=i%>gainNatureNumber">How Many?:</label>
								<input type="number" name="ability<%=i%>gainNatureNumber" min="0" max="1000">
								<label for="ability<%=i%>gainNatureDuration">Turn duration:</label>
								<input type="number" name="ability<%=i%>gainNatureDuration" min="0" max="1000">
							</div>	
							<br>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>gainHP">Does this ability gain Health?</label>
							<select name="ability<%=i%>gainHP" id="ability<%=i%>gainHP" required onchange="ability<%=i%>gainsHP(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>gainsHP" style="display: none;">
								<label for="ability<%=i%>gainHPNumber">How Many?:</label>
								<input type="number" name="ability<%=i%>gainHPNumber" min="0" max="1000">
								<label for="ability<%=i%>gainHPDuration">Turn duration:</label>
								<input type="number" name="ability<%=i%>gainHPDuration" min="0" max="1000">
							</div>	
							<br>
						</div>
						
	
						<div class="container">
							<label for="ability<%=i%>gainDR">Does this ability gain Damage Reduction?</label>
							<select name="ability<%=i%>gainDR" id="ability<%=i%>gainDR" required onchange="ability<%=i%>gainsDR(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>gainsDR" style="display: none;">
								<label for="ability<%=i%>gainDRNumber">How Many?:</label>
								<input type="number" name="ability<%=i%>gainDRNumber" min="0" max="1000">
								<label for="ability<%=i%>gainDRDuration">Turn duration:</label>
								<input type="number" name="ability<%=i%>gainDRDuration" min="0" max="1000">
							</div>	
							<br>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>extraDmamagePerSelfHPLost">Does this ability deal aditional damage per self HP lost?</label>
							<select name="ability<%=i%>extraDmamagePerSelfHPLost" id="ability<%=i%>extraDmamagePerSelfHPLost" onchange="ability<%=i%>DoesExtraDmamagePerSelfHPLost(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>DoesExtraDmamagePerSelfHPLost" style="display: none;">
								<label for="ability<%=i%>extraDmamagePerSelfHPLostNumber">How much damage?:</label>
								<input type="number" name="ability<%=i%>extraDmamagePerSelfHPLostNumber" min="0" max="1000">
								<label for="ability<%=i%>extraDmamagePerSelfHPLostHP">How much HP lost?:</label>
								<input type="number" name="ability<%=i%>extraDmamagePerSelfHPLostHP" min="0" max="1000">
							</div>	
							<br>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>extraDmamagePerEnemyHPLost">Does this ability deal aditional damage per Enemy HP lost?</label>
							<select name="ability<%=i%>extraDmamagePerEnemyHPLost" id="ability<%=i%>extraDmamagePerEnemyHPLost" onchange="ability<%=i%>DoesExtraDmamagePerEnemyHPLost(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>DoesExtraDmamagePerEnemyHPLost" style="display: none;">
								<label for="ability<%=i%>extraDmamagePerEnemyHPLostNumber">How much damage?:</label>
								<input type="number" name="ability<%=i%>extraDmamagePerEnemyHPLostNumber" min="0" max="1000">
								<label for="ability<%=i%>extraDmamagePerEnemyHPLostHP">How much HP lost?:</label>
								<input type="number" name="ability<%=i%>extraDmamagePerEnemyHPLostHP" min="0" max="1000">
							</div>	
							<br>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>extraDamageTemporary">Does this ability increase Character damage temporarly?</label>
							<select name="ability<%=i%>extraDamageTemporary" id="ability<%=i%>extraDamageTemporary" onchange="ability<%=i%>DoesExtraDmamageTemporary(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>DoesExtraDamageTemporary" style="display: none;">
								<label for="ability<%=i%>extraDamageTemporaryNumber">How much damage?:</label>
								<input type="number" name="ability<%=i%>extraDamageTemporaryNumber" min="0" max="1000">
								<label for="ability<%=i%>extraDamageTemporaryDuration">Duration:</label>
								<input type="number" name="ability<%=i%>extraDamageTemporaryDuration" min="0" max="1000">
								<label for="ability<%=i%>extraDamageTemporaryTarget">Target: </label>
								<select name="ability<%=i%>extraDamageTemporaryTarget" id="ability<%=i%>extraDamageTemporaryTarget">
									<option value="self">Self</option>
									<option value="enemy">Enemy</option>
									<option value="ally">Ally</option>
								</select>
								<label for="ability<%=i%>extraDamageTemporaryWhich">Which Abilities: </label>
								<select name="ability<%=i%>extraDamageTemporaryWhich" id="ability<%=i%>extraDamageTemporaryWhich">
									<option value="all">All</option>
									<option value="current">Current</option>									
								</select>
							</div>	
							<br>
						</div>
						
						<div class="container">
							<label for="ability<%=i%>ActiveSkill">Does this ability have an active skill?</label>
							<select name="ability<%=i%>ActiveSkill" id="ability<%=i%>ActiveSkill" onchange="ability<%=i%>HasActiveSkill(this)">
								<option value="no" >No</option>
								<option value="yes">Yes</option>
							</select>
	
							<div id="ability<%=i%>HasActiveSkill" style="display: none;">
								<label for="ability<%=i%>activeTarget">Target: </label>
								<select name="ability<%=i%>activeTarget" id="ability<%=i%>activeTarget">
									<option value="self">Self</option>
									<option value="enemy">Enemy</option>
									<option value="ally">Ally</option>
								</select>
								<label for="ability<%=i%>activeText">Text: </label>
								<textarea placeholder="Write Active Text (max 5000chars)" name="ability<%=i%>activeText"
									style="min-height: 200px; width: 100%;" maxlength="5000" 
								></textarea>
								<label for="ability<%=i%>activeDuration">Duration:</label>
								<input type="text" name="ability<%=i%>activeDuration">
							</div>	
							<br>
						</div>
						
						
						<div class="container">
							<label for="ability<%=i%>taijutsu">Taijutsu (minimum 0):</label>
							<input type="number" name="ability<%=i%>taijutsu" min="0" max="100" required><br>
							<label for="ability<%=i%>heart">Heart (minimum 0):</label>
							<input type="number" name="ability<%=i%>heart" min="0" max="100" required><br>
							<label for="ability<%=i%>energy">Energy (minimum 0):</label>
							<input type="number" name="ability<%=i%>energy" min="0" max="100" required><br>
							<label for="ability<%=i%>spirit">Spirit (minimum 0):</label>
							<input type="number" name="ability<%=i%>spirit" min="0" max="100" required><br>
							<label for="ability<%=i%>random">Random (minimum 0):</label>
							<input type="number" name="ability<%=i%>random" min="0" max="100" required><br>
						</div>
						<div class="container">
							<label for="ability<%=i%>cd">Cooldown (minimum 0):</label>
							<input type="number" name="ability<%=i%>cd" min="0" max="100" required>
						</div>	
						<div class="container">
							<label for="ability<%=i%>image">Ability<%=i%> Picture</label>
							<input type="file" accept="image/*" name="ability<%=i%>image" required />
						</div>
						<br><br>
						<%} %>
						
		
						<div class="container">
							<label for="charAnime">Character Anime</label>
							<select name="charAnime" id="characterAnime">
								<option value="Bleach">Bleach</option>
								<option value="DemonSlayer">Demon Slayer</option>
								<option value="OnePunchMan">One Punch Man</option>
								<option value="HunterXHunter">Hunter X Hunter</option>
								<option value="SAO">Sword Art Online</option>
							</select>
						</div>
						
						<div class="container">
							<label for="defaultmission">Default or Mission Character ?</label>
							<select name="defaultmission" id="defaultORmission" onchange="isMission(this)">
								<option value="Default">Default</option>
								<option value="Mission">Mission</option>
							</select>
						</div>
						
						<div id="showMission" style="display:none">
							<div class="container">
								<label for="missionName">
									<b>Mission Name</b>
								</label>
								<input type="text" placeholder="Mission Name" name="missionName"/>
							</div>
							<div class="container">
								<label for="missionDescription">
									<b>Mission Description</b>
								</label>
								<textarea placeholder="Write Mission Description (max 5000chars)" name="missionDescription"
									style="min-height: 200px; width: 100%;" 
								></textarea>
							</div>
							
							<div class="container">
								<label for="minLevel">Required Level (minimum 1):</label>
								<input type="number" name="minLevel" min="1" max="1000">
							</div>	
							
							<div class="container">
								<label for="missionImage">Mission Picture</label>
								<input type="file" accept="image/*" name="missionImage" />
							</div>
							 <br>
							<label>
								<b>Requirements:</b>
							</label>
							<div class="container">
							
								<label>With which character?</label>
								<select name="whichCharacter" id="whichCharacter" >
									<option value="none">None</option>
									 <%
									Class.forName(Connector.drv);
									try (Connection conn = Connector.getConnection();) {
										Statement stmt = conn.createStatement();
										
										ResultSet rs = stmt.executeQuery("select * from THEME_CHARACTER where themeID=1;");
										while (rs.next()) {
											String characterID = rs.getString("characterID");
											String nome = rs.getString("nome");
									%>
									<option value="<%=characterID%>"><%=nome%></option>
									<%}
										rs.close();
									} catch (SQLException | IOException e) {
									System.out.println(e.getMessage());
									}
									%>
								</select>
								<br>
								<label>How many wins?</label>
								<input type="number" name="nWinsCharacter" min="1" max="1000">
								<br>
								<label>Are wins in a row?</label>
								<select name="rowCharacter" id="rowCharacter">
									<option value="yes">Yes</option>
									<option value="no">No</option>
								</select>
								<br>
								<br>
								
								<label>With which character?</label>
								<select name="whichCharacter2" id="whichCharacter2" >
									<option value="none">None</option>
										 <%
									Class.forName(Connector.drv);
									try (Connection conn = Connector.getConnection();) {
										Statement stmt = conn.createStatement();
										
										ResultSet rs = stmt.executeQuery("select * from THEME_CHARACTER where themeID=1;");
										while (rs.next()) {
											String characterID = rs.getString("characterID");
											String nome = rs.getString("nome");
									%>
									<option value="<%=characterID%>"><%=nome%></option>
									<%}
										rs.close();
									} catch (SQLException | IOException e) {
									System.out.println(e.getMessage());
									}
									%>
								</select>
								<br>
								<label>How many wins?</label>
								<input type="number" name="nWinsCharacter2" min="1" max="1000">
								<br>
								<label>Are wins in a row?</label>
								<select name="rowCharacter2" id="rowCharacter2">
									<option value="yes">Yes</option>
									<option value="no">No</option>
								</select>
								<br>
								<br>
								<label>With which anime?</label>
								<select name="whichAnime" id="whichAnime" >
									<option value="none">None</option>
									<option value="hunterxhunter">Hunter X Hunter</option>
									<option value="sao">Sword Art Online</option>
									<option value="onepunchman">One Punch Man</option>
									<option value="bleach">Bleach</option>
									<option value="demonslayer">Demon Slayer</option>
								</select>
								<br>
								<label for="nWinsAnime">How many wins?</label>
								<input type="number" name="nWinsAnime" min="1" max="1000">
								<br>
								<label>Are wins in a row?</label>
								<select name="rowAnime" id="rowAnime">
									<option value="yes">Yes</option>
									<option value="no">No</option>
								</select>
								
								
							</div>	
							
						</div>
						<br>
		
						<button type="submit">Submit</button>
					</div>
				</form>
                
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

<script>
function isMission(m) {
	
    if (m.value=="Mission") {
        document.getElementById("showMission").style.display="block";
    }
    else if (m.value=="Default") {
        document.getElementById("showMission").style.display="none";
    }
}

function ability1DoesDamage(m) {
	
	if (m.value=="yes") {
		document.getElementById("ability1DoesDamage").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1DoesDamage").style.display="none";
	}
}
function ability1removesNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability1removesNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1removesNature").style.display="none";
	}
}

function ability1gainsNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability1gainsNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1gainsNature").style.display="none";
	}
}

function ability1gainsHP(m) {

	if (m.value=="yes") {
		document.getElementById("ability1gainsHP").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1gainsHP").style.display="none";
	}
}

function ability1gainsDR(m) {

	if (m.value=="yes") {
		document.getElementById("ability1gainsDR").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1gainsDR").style.display="none";
	}
}
function ability1DoesExtraDmamagePerSelfHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability1DoesExtraDmamagePerSelfHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1DoesExtraDmamagePerSelfHPLost").style.display="none";
	}
}

function ability1DoesExtraDmamagePerEnemyHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability1DoesExtraDmamagePerEnemyHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1DoesExtraDmamagePerEnemyHPLost").style.display="none";
	}
}
function ability1DoesExtraDmamageTemporary(m) {

	if (m.value=="yes") {
		document.getElementById("ability1DoesExtraDamageTemporary").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1DoesExtraDamageTemporary").style.display="none";
	}
}
//2
function ability2DoesDamage(m) {
	
	if (m.value=="yes") {
		document.getElementById("ability2DoesDamage").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2DoesDamage").style.display="none";
	}
}
function ability2removesNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability2removesNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2removesNature").style.display="none";
	}
}

function ability2gainsNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability2gainsNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2gainsNature").style.display="none";
	}
}

function ability2gainsHP(m) {

	if (m.value=="yes") {
		document.getElementById("ability2gainsHP").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2gainsHP").style.display="none";
	}
}

function ability2gainsDR(m) {

	if (m.value=="yes") {
		document.getElementById("ability2gainsDR").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2gainsDR").style.display="none";
	}
}
function ability2DoesExtraDmamagePerSelfHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability2DoesExtraDmamagePerSelfHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2DoesExtraDmamagePerSelfHPLost").style.display="none";
	}
}

function ability2DoesExtraDmamagePerEnemyHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability2DoesExtraDmamagePerEnemyHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2DoesExtraDmamagePerEnemyHPLost").style.display="none";
	}
}
function ability2DoesExtraDmamageTemporary(m) {

	if (m.value=="yes") {
		document.getElementById("ability2DoesExtraDamageTemporary").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2DoesExtraDamageTemporary").style.display="none";
	}
}
//3
function ability3DoesDamage(m) {
	
	if (m.value=="yes") {
		document.getElementById("ability3DoesDamage").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3DoesDamage").style.display="none";
	}
}
function ability3removesNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability3removesNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3removesNature").style.display="none";
	}
}

function ability3gainsNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability3gainsNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3gainsNature").style.display="none";
	}
}

function ability3gainsHP(m) {

	if (m.value=="yes") {
		document.getElementById("ability3gainsHP").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3gainsHP").style.display="none";
	}
}


function ability3gainsDR(m) {

	if (m.value=="yes") {
		document.getElementById("ability3gainsDR").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3gainsDR").style.display="none";
	}
}
function ability3DoesExtraDmamagePerSelfHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability3DoesExtraDmamagePerSelfHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3DoesExtraDmamagePerSelfHPLost").style.display="none";
	}
}

function ability3DoesExtraDmamagePerEnemyHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability3DoesExtraDmamagePerEnemyHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3DoesExtraDmamagePerEnemyHPLost").style.display="none";
	}
}
function ability3DoesExtraDmamageTemporary(m) {

	if (m.value=="yes") {
		document.getElementById("ability3DoesExtraDamageTemporary").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3DoesExtraDamageTemporary").style.display="none";
	}
}
//4
function ability4DoesDamage(m) {
	
	if (m.value=="yes") {
		document.getElementById("ability4DoesDamage").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4DoesDamage").style.display="none";
	}
}
function ability4removesNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability4removesNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4removesNature").style.display="none";
	}
}

function ability4gainsNature(m) {

	if (m.value=="yes") {
		document.getElementById("ability4gainsNature").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4gainsNature").style.display="none";
	}
}

function ability4gainsHP(m) {

	if (m.value=="yes") {
		document.getElementById("ability4gainsHP").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4gainsHP").style.display="none";
	}
}

function ability4gainsDR(m) {

	if (m.value=="yes") {
		document.getElementById("ability4gainsDR").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4gainsDR").style.display="none";
	}
}
function ability4DoesExtraDmamagePerSelfHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability4DoesExtraDmamagePerSelfHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4DoesExtraDmamagePerSelfHPLost").style.display="none";
	}
}

function ability4DoesExtraDmamagePerEnemyHPLost(m) {

	if (m.value=="yes") {
		document.getElementById("ability4DoesExtraDmamagePerEnemyHPLost").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4DoesExtraDmamagePerEnemyHPLost").style.display="none";
	}
}
function ability4DoesExtraDmamageTemporary(m) {

	if (m.value=="yes") {
		document.getElementById("ability4DoesExtraDamageTemporary").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4DoesExtraDamageTemporary").style.display="none";
	}
}


function ability1HasActiveSkill(m) {
	if (m.value=="yes") {
		document.getElementById("ability1HasActiveSkill").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability1HasActiveSkill").style.display="none";
	}
}

function ability2HasActiveSkill(m) {
	if (m.value=="yes") {
		document.getElementById("ability2HasActiveSkill").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability2HasActiveSkill").style.display="none";
	}
}

function ability3HasActiveSkill(m) {
	if (m.value=="yes") {
		document.getElementById("ability3HasActiveSkill").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability3HasActiveSkill").style.display="none";
	}
}

function ability4HasActiveSkill(m) {
	if (m.value=="yes") {
		document.getElementById("ability4HasActiveSkill").style.display="block";
	}
	else if (m.value=="no"){
		document.getElementById("ability4HasActiveSkill").style.display="none";
	}
}
</script>
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

    <!-- Bootstrap core JavaScript-->
    <script src="js/jquery.js"></script>
    <script src="extras/bootstrap/js/bootstrap.bundle.min.js"></script>

    <script src="js/jquery.easing.js"></script>

    <script src="js/interface.js"></script>

</body>

</html>