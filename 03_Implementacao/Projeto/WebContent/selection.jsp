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
<%@page import="game.Matchmaking"%>
<%@ page import="game.FindOpponent"%>
<%@ page import="resources.ViewBack"%>

<html lang="en" id="move">

<%
    session=request.getSession(false);
    if(session.getAttribute("userID")==null) {
        response.sendRedirect("login.jsp");
    }
    else {

%> 

<head>


  <meta charset="UTF-8">
  <meta name="viewport" content="minimum-scale=0.5">

  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Game Selection</title>
  
 <link href="css/ingameSelection.css" rel="stylesheet">
 <script type="text/javascript" src="js/selection.js"></script>
 <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>



</head>


<body style="padding: 0px;">

<div id="app" class="central">
    <div id="root">
        <div class="mc_custom">
          
			   <img src="ViewBack?type=selectionBackground&id=<%=session.getAttribute("userID")%>" style="width: 770px; height: 560px;">   
			  <%
			Class.forName(Connector.drv);
			try (Connection conn = Connector.getConnection();) {
				Statement stmt = conn.createStatement();
				
				ResultSet rs = stmt.executeQuery("select * from THEME_CHARACTER where themeID=1;");
				
				while (rs.next()) {
					String characterID = rs.getString("characterID");
					
					ResultSet especific =  conn.createStatement().executeQuery("select * from THEME_CHARACTER where themeID="+UserInfo.getCurrentTheme( (int)session.getAttribute("userID") )+" and characterID="+characterID);
					if (especific.next()) {
						String name = especific.getString("nome");
						String descricao = especific.getString("descricao");
						
			%>
					<div class="topo" id="displayCharacter<%=characterID%>" style="display:none">
						<div class="conteudo">
						   <div class="nome">
						      <%=name%>
						   </div>
						   <img src="ViewCharacter?id=<%=characterID %>" class="foto borda" onclick="displayCharacterDescription(<%=characterID %>)"> 
		
						   <div id="charDescription<%=characterID %>">
							   <div class="desc">
							   	<%=descricao%>
							   </div>
							    <div class="nomeskill">
							      <%=name%>
							   </div>
							   <img src="ViewCharacter?id=<%=characterID %>" class="fotoskill borda"> 
						   </div>
						   
						   <% 
					       ResultSet abilities_clicks = conn.createStatement().executeQuery("select * from ABILITY where characterID="+characterID+";");
						   while (abilities_clicks.next()) {
										
								String abilityID = abilities_clicks.getString("abilityID");
								
								ResultSet abilit = conn.createStatement().executeQuery(
										"select * from THEME_ABILITY where themeID="+UserInfo.getCurrentTheme( (int)session.getAttribute("userID") )+" and abilityID="+abilityID+";");
								if (abilit.next()) {
									String descricao_ability = abilit.getString("descricao");
									String name_ability = abilit.getString("nome");
								
								%>
								
								<div id="ability<%=abilityID%>" style="display:none;">
								   <div class="desc">
								   	<%=descricao_ability%>
								   </div>
								   <img src="ViewCharacter?id=<%=characterID %>" class="foto borda" onclick="displayCharacterDescription(<%=characterID %>)"> 
								   <div class="nomeskill">
								      <%=name_ability %>
								   </div>
								   <img src="ViewAbility?id=<%=abilityID %>" class="fotoskill borda"> 
								   
								   	<div class="nomeEnergy">
								      ENERGY:
					      	           <div class="ability_info_energy_img" id="natures<%=abilityID%>">
						     
						               </div>
								   </div>
								   <%
								   		}
							   ResultSet cool = conn.createStatement().executeQuery(
										"select * from ABILITY where abilityID="+abilityID+";");
								if (cool.next()) {
									int cooldown = cool.getInt("cooldown");
								 %>
								<div class="cooldown">
							   		Cooldown: <%=cooldown%>
							   </div>
							   </div>
							   
							   <%
								}
								abilit.close();
						   }
						   abilities_clicks.close();
								
							   %>
						   
						</div>
					    
					    
					    <nav>
					       <ul class="listaskills">
					       <% 
					       ResultSet abilities = conn.createStatement().executeQuery("select * from ABILITY where characterID="+characterID+";");

						   int abilityPos = -1;
					       while (abilities.next()) {
					    	    abilityPos++;	
								String abilityID = abilities.getString("abilityID");
						   %>
					          <li><img src="ViewAbility?id=<%=abilityID %>" class="fotolista borda" onclick="abilityFooterInfo(<%=abilityID %>)"></li>
					         <!-- <li><img src="https://i.imgur.com/oEjXE4N.jpg" class="fotolista borda"></li>
					          <li><img src="https://i.imgur.com/ZnI4UeN.jpg" class="fotolista borda"></li>
					          <li><img src="https://i.imgur.com/xmnDpJO.jpg" class="fotolista borda"></li>-->
					           <%				
							}
						
						abilities.close();
						%>	
					       </ul>
					    </nav>
					
					</div>
					<%
					}
					especific.close();
					}
					rs.close();
					} catch (SQLException | IOException e) {
					System.out.println(e.getMessage());
					}
					%>
			     
			     
			     
			            
         
            <div>
                <div class="rodape">
                    <!-- <input type="text" placeholder="Search Group" class="searchbargroup">
                    <input type="text" placeholder="Search Character" class="searchbar"> -->
                    <ul>
                    <%
					Class.forName(Connector.drv);
					try (Connection conn = Connector.getConnection();) {
						Statement stmt = conn.createStatement();
						
						ResultSet rs = stmt.executeQuery("select characterID from THEME_CHARACTER where themeID=1 LIMIT 21;");
						
						while (rs.next()) {
							
							String characterID = rs.getString("characterID");	
							

							boolean has = false;
							ResultSet userHasChar =  conn.createStatement().executeQuery("select * from USER_CHARACTER where userID="+session.getAttribute("userID"));
							while (userHasChar.next()) {
								if ( userHasChar.getInt("characterID") == Integer.parseInt(characterID) ) {
									has = true;
									break;
								}
							}
							
							if (has) {
								%>
								
						
                        <li class="personagem_fundo borda" id="savePos<%=characterID %>" ondrop="drop(event)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event)">
                        <img id="charpic<%=characterID %>" src="ViewCharacter?id=<%=characterID %>" class="personagem" ondblclick="dbChar(<%=characterID %>)" onclick="displayInfo(<%=characterID %>)">
                        
							<%
							} else {
								%>
								
                        <li style="opacity: 0.5" class="personagem_fundo borda" id="savePos<%=characterID %>">
                        <img id="charpic<%=characterID %>" src="ViewCharacter?id=<%=characterID %>" class="personagem" onclick="displayInfo(<%=characterID %>)">
                       
							<%
							}
							%>	
							
					
						
                      
                        
                        </li>
                       
                        <%
					}
						rs.close();
					} catch (SQLException | IOException e) {
						System.out.println(e.getMessage());
					}
					%>
            
                    </ul>
                    
					<%
					Class.forName(Connector.drv);
					try (Connection conn = Connector.getConnection();) {
						Statement stmt = conn.createStatement();
						
						ResultSet rs = stmt.executeQuery("select * from USERS where userID="+session.getAttribute("userID"));
						if (rs.next()) {
							String username = rs.getString("username");
							int userID = rs.getInt("userID");
							int xp = rs.getInt("xp");
							String nWins = rs.getString("nWins");
							String nLosses = rs.getString("nLosses");
							int streak = rs.getInt("streak");
							int level = UserInfo.getLevel(xp);
					%>
                    <div class="perfil"><img src="ViewAvatar?id=<%=userID %>" class="avatar borda"><img class="mold">
                        <div class="texto" style="white-space: nowrap;">
                        	<span><%=username %></span><br>
                            <span><%=UserInfo.getRankName( UserInfo.getLevel(xp) ) %></span>
                            <br><span>Level:</span> <%=level %> (<%=xp %> XP)
                            <br><span>Ladderrank:</span> #<%=UserInfo.getLadderRank(userID)%>
                            <br><span>Ratio:</span> <%=nWins %>-<%=nLosses %> (
                            <%if (streak>0) { %>+<%} %>
                             <%=streak %>)</div>
                    </div>
                    <%
                    }
					rs.close();
					} catch (SQLException | IOException e) {
					System.out.println(e.getMessage());
					}
					%>
                    <div class="selecionados">
                        <div class="selectedsnum">
                            <div>1</div>
                            <div>2</div>
                            <div>3</div>
                        </div>
                        <div class="containers">
                        
                            <div id="items1" ondrop="drop(event)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event)"class="items"></div>
                            <div id="items2" ondrop="drop(event)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event)" class="items"></div>
                            <div id="items3" ondrop="drop(event)" ondragover="allowDrop(event)" draggable="true" ondragstart="drag(event)" class="items"></div>
                        	<input type="hidden" name="char1" id="inputChar1">
                        	<input type="hidden" name="char2" id="inputChar2">
                        	<input type="hidden" name="char3" id="inputChar3">
                        	
                        	
              		
                        </div>
                    </div>
          
		
			<!-- <img id="drag1" src="selection/background.png" draggable="true" ondragstart="drag(event)" width="336" height="69" style="postion: absolute;">-->
                
                </div>
            </div>
            
               <div class="menu">
                <ul>
                    <li><a href="logout.jsp"><img src="selection/buttonLogout.png"></a></li>
                    <li><img src="selection/buttonLadder.png" id="btnLadder" style="opacity:0.7" onclick="searchOpp('ladder', this)"></li>
                    <li style="margin-left: 3px;">
              		<!-- <input type="hidden" id="inputChar1" name="inputChar1" value="getChar1()"/> -->
 					<img src="selection/buttons_quick.png" id="btnQuick" style="opacity:0.7" onclick="searchOpp('quick', this)">
                    </li>
                    <li style="margin-left: 2px;"><img src="selection/button_private.png" id="btnPrivate" onclick="privateBattle(this)" style="opacity:0.7"></li>
                    
                </ul>
            </div>
    
            <div class="holders holdanimes" id="searchingOpp" style="display: none;">
                <span class="rbattle">Searching for an opponent</span> 
                <div class="lds-hourglass">
            
                </div>
                <div class="btncancels" onclick="hideSearching()"></div>
             </div>
             
              <div class="holders holdanimes" id="foundOpp" style="display: none;">
                <span class="rbattle">Opponent Found</span> 
                <div class="lds-hourglass-found">
            
                </div>
                
             </div>
             
			<div class="holders holdanimes" id="pbBattle" style="display:none;">
				<span class="rbattle">Start Private Battle</span>
				<span class="pbent">Enter your opponents name:</span>
				<input type="text" class="pbtext">
				<div class="btnok" onclick="startPB(this)"></div> 
				<div class="btncancel" onclick="hidePB()"></div>
				
			</div>
           
            <div class="arrow_left"></div>
            <div class="arrow_right"></div>
          

        </div>
     
    </div>
</div>

</body>

<%} %>

</html>
