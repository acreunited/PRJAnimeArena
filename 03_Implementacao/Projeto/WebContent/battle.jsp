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
<%@page import="game.InGame"%>
<%@page import="mechanics.Character"%>
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
  
 <link href="css/ingameBattle.css" rel="stylesheet">
 
 <script type="text/javascript" src="js/battle.js"></script>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>


</head>
<body>



<div id="app" class="central">
         <div id="root">
            <div class="mc_custom">
               
               <img src="ViewBack?type=battleBackground&id=<%=session.getAttribute("userID")%>" style="width: 770px; height: 560px;">
               <div class="mc_top">
                 <%
                 	Class.forName(Connector.drv);
                 		try (Connection conn = Connector.getConnection();) {
                 			Statement stmt = conn.createStatement();
                 			
                 			ResultSet rs = stmt.executeQuery("select * from USERS where userID="+session.getAttribute("this_id")+";");
                 			
                 			if (rs.next()) {
                 				String userID = rs.getString("userID");
                 				String username = rs.getString("username");
                 %>
                  <div class="mc_play1">
                     <div class="mc_username1" id="currentPlayer">
                        <%=username%>
                     </div>
                     <div class="mc_userrank1" id="rankName1">
                       
                     </div>
                     <div class="mc_avatar1">
                     
                       <img src="ViewAvatar?id=<%=userID%>" onclick="playerFooterInfo('my')" alt="" > 
                     </div>
                  </div>
                  <%
                  	}
           			rs.close();
           			} catch (SQLException | IOException e) {
           			System.out.println(e.getMessage());
           			}
                  %>
                  
                  
                  <!----> 
                  <div class="mc_control">
                     <div class="mc_bar_system">
                        <div class="mc_bar_back" id="updateTime" >
                           <div class="mc_bar_fill" id="timeBar" style="width: 191px;"></div>
                        </div>
                        <div class="mc_bar_ready opp_text" id="oppTurnDisable">
                           Opponent Turn...
                        </div>
                        <div class="mc_bar_ready my_turn" id="passTurn" onclick="updateCurrentRandom()">
                           Press To End Turn
                        </div>
						<div class="mc_bar_ready" id="winnerTurn" style="display: none;">
                           WINNER
                        </div>
                        <div class="mc_bar_ready" id="loserTurn" style="display: none;">
                           LOSER
                        </div>
				
                        <div class="mc_energy_system my_turn" onclick="exchangeNature()">
			                <div class="mc_energy_bar"></div>
			                <div class="mc_energy_txt" id="natures">
			                  <strong class="energy0">x<%=session.getAttribute("taijutsu") %></strong>
			                  <strong class="energy1">x<%=session.getAttribute("heart") %> </strong>
			                  <strong class="energy2">x<%=session.getAttribute("energy") %></strong>
			                  <strong class="energy3">x<%=session.getAttribute("spirit") %></strong>
			                  <strong class="energy4">x<%=session.getAttribute("random") %></strong>
			                </div>
			                <div class="mc_energy_exchange" >EXCHANGE ENERGY</div>
			             </div>
                     </div>
                     <!---->
                  </div>
                <%
                	Class.forName(Connector.drv);
                		try (Connection conn = Connector.getConnection();) {
                			Statement stmt = conn.createStatement();
                			
                			ResultSet rs = stmt.executeQuery("select * from USERS where userID="+session.getAttribute("opp_id")+";");
                			
                			if (rs.next()) {
                				String userID = rs.getString("userID");
                				String username = rs.getString("username");
                %>
                  <div class="mc_play2">
                     <div class="mc_username2">
                        <%=username%>
                     </div>
                     <div class="mc_userrank2"  id="rankName2">
                       
                     </div>
                     <div class="mc_avatar2">
                        <a><img src="ViewAvatar?id=<%=userID%>" onclick="playerFooterInfo('opp')"> </a>
     	             </div>
                  </div>
                      <%
                      	}
               			rs.close();
               			} catch (SQLException | IOException e) {
               			System.out.println(e.getMessage());
               			}
                      %>
                  <!---->
               </div>
               <div class="mc_combat">
                 <%
                 	Class.forName(Connector.drv);
                 		try (Connection conn = Connector.getConnection();) {
                 			Statement stmt = conn.createStatement();
                 			
                 			
                 			
                 			ResultSet rs = stmt.executeQuery(
                 					"select * from THEME_CHARACTER where themeID="+UserInfo.getCurrentTheme( (int)session.getAttribute("userID") )+" and (characterID="+session.getAttribute("this_char1")+ 
                 					" or characterID="+ session.getAttribute("this_char2")+" or characterID="+session.getAttribute("this_char3")+")"+
                 					"order by case when characterID="+session.getAttribute("this_char1")+" then 1 when characterID="+session.getAttribute("this_char2")+" then 2 when characterID="+session.getAttribute("this_char3")+" then 3 else null end;");
                 			int countChars = 0;
                 			while (rs.next()) {
                 				String characterID = rs.getString("characterID");
                 				int count = 0;
                 				int count_my = 0;
                 %>
                  <div class="mc_char_0<%=countChars%>" id="ally<%=countChars%>">
                  	
                     <div class="mc_char_section">
                        <div class="mc_char_section_perg"></div>
                        <div class="mask">
                           <div class="mc_char_section_skill opp_turn" style="display:none;">
                              <div class="mc_char_section_selected"></div>
                              <div class="mc_char_section_skills">
                              
                        <%
                     	ResultSet abilities = conn.createStatement().executeQuery("select * from ABILITY where characterID="+characterID+";");
                     			    while (abilities.next()) {
                     							
                     					String abilityID = abilities.getString("abilityID");
                     %>
                                 <div class="skillimg<%=count%>">
                                    <a onclick="abilityFooterInfo(<%=abilityID%>, <%=countChars%>, <%=count%>)"><img src="ViewAbility?id=<%=abilityID%>" class="disabled" > </a>
                                 </div>
  
                                  <%
                                    	count++;
                           			   }
                           			   abilities.close();
                                    %>
                              </div>
                           </div>
                           
                           <div class="mc_char_section_skill my_turn">
                              
                              <div class="mc_char_section_skills">
                              
                        <%
                          ResultSet abilities_my = conn.createStatement().executeQuery("select * from ABILITY where characterID="+characterID+";");
                          while (abilities_my.next()) {
                                                      							
                          String abilityID_my = abilities_my.getString("abilityID");
                          %>
                                 <div class="skillimg<%=count_my%>" id="allSkillsChar<%=countChars%>">
                                    <a onclick="abilityClick(<%=abilityID_my%>, <%=countChars%>, <%=count_my%>)"><img src="ViewAbility?id=<%=abilityID_my%>" id="imageClickMaybe<%=abilityID_my%>"></a>
                                	 <span class="cooldown" id="cooldown<%=countChars%>-<%=count_my%>" style="display: none"></span> 
                                 </div>
  
                                  <%
                                    	count_my++;
                                    	}
                                    	abilities_my.close();
                                    %>
                              </div>
                              <div class="mc_char_section_selected" id="selected<%=countChars%>"  ondblclick="cancelArrayFirst(<%=countChars%>, this)">
                              	<img src='battle/skillact.png' id="selectedNone">
                              </div>
                           </div>
                        </div>
                 
                     </div>
                     
                     <div class="mc_char_card">
                        <!-- <div class="mc_char_card_rank2  ">
                          <img src="">  
                        </div> -->
                    
                        <div class="mc_char_card_rank">
                        
                           <a class="rankAlly" onclick="characterFooterInfo(<%=characterID%>, 'ally', <%=countChars%>)">
                           		
                           </a>
                         
                        </div> 
                         <div class="mc_char_card_avatar  ">
                         	<!-- <img class="abs " src="https://naruto-arena.net/images/dead.png">-->
                         	 <img class="abs" id="dead_0<%=countChars%>" src="ViewCharacter?id=<%=characterID%>">
                        </div>
                     </div>
                   
                     <div class="mc_char_card_lifebar" id="hpAlly<%=countChars%>">
                        <div id="bar_0<%=countChars%>" style=" background-color: #3BDF3F; width: 100%"></div>
                        <div id="bar_text_0<%=countChars%>" class="mc_char_card_lifetext">
                           100/100
                        </div>
                     </div>
                   <!--  <div>
                        <div class="mc_char_card_avatar pulse_avt" >
                           <img style="border: none" id="dead_00" >
                        </div>
                     </div>-->
                     
                      <div class="effects" id="effectsAlly<%=countChars%>">
                     </div>
                     
                     <!-- TODO CRIAR MANUALMENTE -->
                   <!-- <div class="effects">
                     	<div class="effects_border0 zindex1">
                  			<img src="ViewAbility?id=id" onmouseover="seeActiveSkill(id)" onmouseleave="hideActiveSkill()">
                  			<span class="tooltiptext" id="tooltiptextid">
	                    		<span class="tooltiptextname">SPRINKLING NEEDLES</span>
	                    		<span class="tooltiptextdesc">This character will take 10 damage.</span>
	                    		<span class="tooltiptextduration">1 TURN LEFT</span>
                    		</span>
                   		</div>
                    </div>
                      -->
                  </div>
                  
                    <%
                     	countChars++; 
                     			}
                     			rs.close();
                     			} catch (SQLException | IOException e) {
                     			System.out.println(e.getMessage());
                     			}
                     			
                     		Class.forName(Connector.drv);
                     		try (Connection conn = Connector.getConnection();) {
                     			Statement stmt = conn.createStatement();
                     			
                     			ResultSet rs = stmt.executeQuery(
                     					"select * from THEME_CHARACTER where themeID="+UserInfo.getCurrentTheme( (int)session.getAttribute("userID") )+" and (characterID="+session.getAttribute("opp_char1")+ 
                     					" or characterID="+ session.getAttribute("opp_char2")+" or characterID="+session.getAttribute("opp_char3")+")"+
                     					"order by case when characterID="+session.getAttribute("opp_char1")+" then 1 when characterID="+session.getAttribute("opp_char2")+" then 2 when characterID="+session.getAttribute("opp_char3")+" then 3 else null end;");
                     			int countChars = 0;
                     			while (rs.next()) {
                     				String characterID = rs.getString("characterID");
                     				int count = 0;
                                      %>
                  <div class="mc_char_1<%=countChars%>" id="enemy<%=countChars%>">
                  	<%
                  		countChars++;
                  	%>
                     <div class="mc_char_card">
                       <!-- <div class="mc_char_card_rank2 revert rankenemy">
                            <img src="battle/hats/default/cipher.png">
                        </div>-->
                        <div class="mc_char_card_avatar revert mc_char_card_avatar_en">
                          <!--  <img class="abs revert" src="https://naruto-arena.net/images/dead.png"> -->
                           
                           <img class="abs" id="dead_1<%=countChars%>" src="ViewCharacter?id=<%=characterID%>">
                        </div>
                        <div class="mc_char_card_rank revert">
                        	<a class="rankEnemy" onclick="characterFooterInfo(<%=characterID%>, 'enemy', <%=countChars%>)">
                           		
                           </a>
                          <!-- <a onclick="characterFooterInfo(<%=characterID%>, 'enemy', <%=countChars%>)"><img src=""></a> --> 
                        </div>
                     </div>
                 
                     <!--<div class="nochoose en2"></div>--> 
                     <div class="mc_char_card_lifebar en" id="hpEnemy<%=countChars%>">
                        <div id="bar_10" style=" background-color: #3BDF3F; width: 100%"></div>
                        <div id="bar_text_10" class="mc_char_card_lifetext">
                           100/100
                        </div>
                     </div>
                     <!-- <div class="revert pulseenemy">
                        <div class="mc_char_card_avatar   pulse_avt">
                          <img style="border: none" id="dead_10" >
                        </div>
                     </div> -->
                     
                    <!-- TODO CRIAR MANUALMENTE --> 
                     <div class="effects1" id="effectsEnemy<%=countChars%>">
                     </div>
                    <!--  <div class="effects1">
                     	<div class="effects_border1 zindex1">
                  			<img src="ViewAbility?id=id" onmouseover="seeActiveSkillEnemy(id)" onmouseleave="hideActiveSkillEnemy()">
                  			<span class="tooltiptext1" id="tooltiptext1id">
	                    		<span class="tooltiptextname">SPRINKLING NEEDLES</span>
	                    		<span class="tooltiptextdesc">This character will take 10 damage.</span>
	                    		<span class="tooltiptextduration">1 TURN LEFT</span>
                    		</span>
                   		</div>
                    </div>
                     -->
                  </div>
                    <%
                    	}
               			rs.close();
               			} catch (SQLException | IOException e) {
               			System.out.println(e.getMessage());
               			}
                    %>
                 
                  </div>
                  
                 
				<div class="holder holdanime" id="chooseRandom" style="display:none;">
		
				  <div class="txtroundup">
				    <span class="chp1">CHOOSE</span>
				    <span class="random" id="nRandom">1</span>
				    <span class="chp2">RANDOM NATURE</span>
				    <span class="enp1">CURRENT NATURE:</span>
				    <span class="enp2">SELECTED NATURE:</span>
				    <ul class="textleft">
				      <li>
				        <div class="ex">
				          <img src="battle/Taijutsu.png">
				          <div>TAIJUTSU</div>
				          <div id="currentTaijutsu"><%=session.getAttribute("taijutsu") %></div>
				        </div>
				        <div class="ex2">
				          <img src="battle/Minus.png" class="min" onclick="minus('Taijutsu')">
				          <img src="battle/Plus.png" class="plus" onclick="plus('Taijutsu')">
				        </div>
				        <div class="ex">
				          <img src="battle/Taijutsu.png">
				          <div>TAIJUTSU</div>
				          <div id="toRemoveTaijutsu">0</div>
				        </div>
				      </li>
				      <li>
				        <div class="ex">
				          <img src="battle/Heart.png">
				          <div>HEART</div>
				          <div id="currentHeart"><%=session.getAttribute("heart") %></div>
				        </div>
				        <div class="ex2">
				          <img src="battle/Minus.png" class="min" onclick="minus('Heart')">
				          <img src="battle/Plus.png" class="plus" onclick="plus('Heart')">
				        </div>
				        <div class="ex">
				          <img src="battle/Heart.png">
				          <div>HEART</div>
				          <div id="toRemoveHeart">0</div>
				        </div>
				      </li>
				      <li>
				        <div class="ex">
				          <img src="battle/Energy.png">
				          <div>ENERGY</div>
				          <div id="currentEnergy"><%=session.getAttribute("energy") %></div>
				        </div>
				        <div class="ex2">
				           <img src="battle/Minus.png" class="min" onclick="minus('Energy')">
				          <img src="battle/Plus.png" class="plus" onclick="plus('Energy')">
				        </div>
				        <div class="ex">
				          <img src="battle/Energy.png">
				          <div>ENERGY</div>
				          <div id="toRemoveEnergy">0</div>
				        </div>
				      </li>
				      <li>
				        <div class="ex">
				          <img src="battle/Spirit.png">
				          <div>SPIRIT</div>
				          <div id="currentSpirit"><%=session.getAttribute("spirit") %></div>
				        </div>
				        <div class="ex2">
				          <img src="battle/Minus.png" class="min" onclick="minus('Spirit')">
				          <img src="battle/Plus.png" class="plus" onclick="plus('Spirit')">
				        </div>
				        <div class="ex">
				          <img src="battle/Spirit.png">
				          <div>SPIRIT</div>
				          <div id="toRemoveSpirit">0</div>
				        </div>
				      </li>
				    </ul>
				  </div>
				  <div class="container">
				    <span id="showAbilitiesEndTurn">
				      <!-- <img src="ViewAbility?id=4" class="item">
				      <img src="ViewAbility?id=8" class="item"> -->
				    </span>
				  </div>
				  <div class="btnok" onclick="storeAbilities();">
				    
				  </div>
				  <div class="btncancel" onclick="cancelTurn()">
				    
				  </div>
				</div>
              </div>
              
              <div class="holder holdanime" id="exchangeRandom" style="display:none;">
              
              <ul class="chooseenergy">
				  <li>
				    <img src="battle/Taijutsu.png" id="exchangeTaijutsu" style="opacity: 0.5;" onclick="exchangeFor('Taijutsu')">
				  </li>
				  <li>
				    <img src="battle/Heart.png" id="exchangeHeart" style="opacity: 0.5;" onclick="exchangeFor('Heart')">
				  </li>
				  <li>
				    <img src="battle/Energy.png" id="exchangeEnergy" style="opacity: 0.5;" onclick="exchangeFor('Energy')">
				  </li>
				  <li>
				    <img src="battle/Spirit.png" id="exchangeSpirit" style="opacity: 0.5;" onclick="exchangeFor('Spirit')">
				  </li>
				</ul>
				
				  <div class="txtroundup">
				    <span class="chp1_exchange">CHOOSE</span>
				    <span class="random_exchange" id="nRandom_exchange">1</span>
				    <span class="chp2_exchange" id="aboveBelow">RANDOM NATURE ABOVE</span>
				    <span class="enp1_exchange">CURRENT NATURE:</span>
				    <span class="enp2_exchange">SELECTED NATURE:</span>
				    <ul class="textleft_exchange">
				      <li>
				        <div class="ex_exchange">
				          <img src="battle/Taijutsu.png">
				          <div>TAIJUTSU</div>
				          <div id="currentTaijutsu_exchange"></div>
				        </div>
				        <div class="ex2_exchange">
				          <img src="battle/Minus.png" class="min_exchange" onclick="minusExchange('Taijutsu')">
				          <img src="battle/Plus.png" class="plus_exchange" onclick="plusExchange('Taijutsu')">
				        </div>
				        <div class="ex_exchange">
				          <img src="battle/Taijutsu.png">
				          <div>TAIJUTSU</div>
				          <div id="toRemoveTaijutsu_exchange">0</div>
				        </div>
				      </li>
				      <li>
				        <div class="ex_exchange">
				          <img src="battle/Heart.png">
				          <div>HEART</div>
				          <div id="currentHeart_exchange"></div>
				        </div>
				        <div class="ex2_exchange">
				          <img src="battle/Minus.png" class="min_exchange" onclick="minusExchange('Heart')">
				          <img src="battle/Plus.png" class="plus_exchange" onclick="plusExchange('Heart')">
				        </div>
				        <div class="ex_exchange">
				          <img src="battle/Heart.png">
				          <div>HEART</div>
				          <div id="toRemoveHeart_exchange">0</div>
				        </div>
				      </li>
				      <li>
				        <div class="ex_exchange">
				          <img src="battle/Energy.png">
				          <div>ENERGY</div>
				          <div id="currentEnergy_exchange"></div>
				        </div>
				        <div class="ex2_exchange">
				           <img src="battle/Minus.png" class="min_exchange" onclick="minusExchange('Energy')">
				          <img src="battle/Plus.png" class="plus_exchange" onclick="plusExchange('Energy')">
				        </div>
				        <div class="ex_exchange">
				          <img src="battle/Energy.png">
				          <div>ENERGY</div>
				          <div id="toRemoveEnergy_exchange">0</div>
				        </div>
				      </li>
				      <li>
				        <div class="ex_exchange">
				          <img src="battle/Spirit.png">
				          <div>SPIRIT</div>
				          <div id="currentSpirit_exchange"></div>
				        </div>
				        <div class="ex2_exchange">
				          <img src="battle/Minus.png" class="min_exchange" onclick="minusExchange('Spirit')">
				          <img src="battle/Plus.png" class="plus_exchange" onclick="plusExchange('Spirit')">
				        </div>
				        <div class="ex_exchange">
				          <img src="battle/Spirit.png">
				          <div>SPIRIT</div>
				          <div id="toRemoveSpirit_exchange">0</div>
				        </div>
				      </li>
				    </ul>
				  </div>

				  <div class="btnok" onclick="confirmExchange()">
				    
				  </div>
				  <div class="btncancel" onclick="cancelExchange()">
				    
				  </div>
				</div>
              </div>
              
               <div class="mc_footer">
               <%
               	Class.forName(Connector.drv);
               			try (Connection conn = Connector.getConnection();) {
               				Statement stmt = conn.createStatement();
               				
               				ResultSet player0 = stmt.executeQuery("select * from USERS where userID="+session.getAttribute("this_id")+";");
               				
               				if (player0.next()) {
               					int userID = player0.getInt("userID");
               					String username = player0.getString("username");
               					String xp = player0.getString("xp");
               					String wins = player0.getString("nWins");
               					String losses = player0.getString("nLosses");
               					int streak = player0.getInt("streak");
               %>
					<div class="mc_info" id="player0" style="display:none;">
					 
                     <div class="mc_info_avatar">
                        <img src="ViewAvatar?id=<%=userID%>"> 
                     </div>
                     
                     <div class="mc_info_name"><%=username%></div>
                     <div class="mc_info_desc">
                     Cabin Boy<br>
                     Level: <%=UserInfo.getLevel(xp)%><br>
                     Ladderrank: #<%=UserInfo.getLadderRank(userID)%><br>
                     Ratio: <%=wins%>-<%=losses%> (
                      <%if (streak>0) { %>+ <%} %>
                             <%=streak %>)</div>
                     <div class="mc_info_team">
                        <div class="mc_info_team1"><img src="ViewCharacter?id=<%=session.getAttribute("this_char1")%>"></div>
                        <div class="mc_info_team2"><img src="ViewCharacter?id=<%=session.getAttribute("this_char2")%>"></div>
                        <div class="mc_info_team3"><img src="ViewCharacter?id=<%=session.getAttribute("this_char3")%>"></div>
                     </div>
                  </div>
                  <%
                  	}
                  			player0.close();
                  			} catch (SQLException | IOException e) {
                  			System.out.println(e.getMessage());
                  			}	
                                
                  			Class.forName(Connector.drv);
                  			try (Connection conn = Connector.getConnection();) {
                  				Statement stmt = conn.createStatement();
                  				
                  				ResultSet player1 = stmt.executeQuery("select * from USERS where userID="+session.getAttribute("opp_id")+";");
                  				
                  				if (player1.next()) {
                  					int userID = player1.getInt("userID");
                  					String username = player1.getString("username");
                  					String xp = player1.getString("xp");
                  					String wins = player1.getString("nWins");
                  					String losses = player1.getString("nLosses");
                  					int streak = player1.getInt("streak");
                  %>
					<div class="mc_info" id="player1" style="display:block;">
					 
                     <div class="mc_info_avatar">
                        <img src="ViewAvatar?id=<%=userID%>"> 
                     </div>
                     
                     <div class="mc_info_name"><%=username%></div>
                     <div class="mc_info_desc">
                     Cabin Boy<br>
                     Level: <%=UserInfo.getLevel(xp)%><br>
                     Ladderrank: #<%=UserInfo.getLadderRank(userID)%><br>
                     Ratio: <%=wins%>-<%=losses%>  (
                     <%if (streak>0) { %>+ <%} %>
                             <%=streak %>)</div>
                     <div class="mc_info_team">
                        <div class="mc_info_team1"><img src="ViewCharacter?id=<%=session.getAttribute("opp_char1")%>"></div>
                        <div class="mc_info_team2"><img src="ViewCharacter?id=<%=session.getAttribute("opp_char2")%>"></div>
                        <div class="mc_info_team3"><img src="ViewCharacter?id=<%=session.getAttribute("opp_char3")%>"></div>
                     </div>
                  </div>
                  <%
                  	}
                  			player1.close();
                  			} catch (SQLException | IOException e) {
                  			System.out.println(e.getMessage());
                  			}
                  %>
                  
                  
                    <%
                  	Class.forName(Connector.drv);
                  			try (Connection conn = Connector.getConnection();) {
                  				Statement stmt = conn.createStatement();
                  				
                  				ResultSet characters = stmt.executeQuery(
                  						"select * from THEME_CHARACTER where themeID="+UserInfo.getCurrentTheme( (int)session.getAttribute("userID") )+" and (characterID="+session.getAttribute("this_char1")+ 
                  						" or characterID="+session.getAttribute("this_char2")+ " or characterID="+session.getAttribute("this_char3")+ 
                  						" or characterID="+session.getAttribute("opp_char1")+"  or characterID="+session.getAttribute("opp_char2")+ 
                  						"  or characterID="+session.getAttribute("opp_char3")+");");
                  				
                  				while (characters.next()) {
                  					String characterID = characters.getString("characterID");
                  					String nome = characters.getString("nome");
                  					String descricao = characters.getString("descricao");
                  %>
					<div class="mc_info" id="character<%=characterID%>" style="display:none;">
					 
                     <div class="mc_info_avatar">
                        <img src="ViewCharacter?id=<%=characterID%>"> 
                     </div>
                     
                     <div class="char_info_name"><%=nome%></div>
                     <div class="char_info_desc"><%=descricao%></div>

                   </div>
                  
                  		<%
                  			ResultSet abilities = conn.createStatement().executeQuery("select * from ABILITY where characterID="+characterID+";");
                  						   while (abilities.next()) {
                  							   String abilityID = abilities.getString("abilityID");
                  							   
                  							   ResultSet abilit = conn.createStatement().executeQuery("select * from THEME_ABILITY where themeID="+UserInfo.getCurrentTheme( (int)session.getAttribute("userID") )+" and abilityID="+abilityID+";");
                  							   while (abilit.next()) {
                  								   String nome_ab = abilit.getString("nome");
                  								   String descricao_ab = abilit.getString("descricao");
                  		%>
								   <div class="mc_info" id="ability<%=abilityID%>" style="display:none;">
					 
				                     <div class="mc_info_avatar">
				                        <img src="ViewAbility?id=<%=abilityID%>"> 
				                     </div>
				                     
				                     <div class="ability_info_name"><%=nome_ab%></div>
				                     <div class="ability_info_desc"><%=descricao_ab%></div>
									 
									 <%
									 ResultSet cool = conn.createStatement().executeQuery(
												"select * from ABILITY where abilityID="+abilityID+";");
										if (cool.next()) {
											int cooldown = cool.getInt("cooldown");
										 %>
									 
									 <div class="ability_info_cooldown">
									    COOLDOWN: <%=cooldown%>
									 </div>
									 <%
										}
									 %>
									 
									 <div class="ability_info_energy">
							              ENERGY:
							            <div class="ability_info_energy_img" id="natures<%=abilityID%>">
							     
							            </div>
									</div>
				                   </div>
								 <%
								 	}
								 					   abilit.close();
								 				   }
								                  		abilities.close();
								 			}
								 		characters.close();
								 			} catch (SQLException | IOException e) {
								 			System.out.println(e.getMessage());
								 			}
								 %>
                  
                  <div class="mc_menu">
                     <div class="mc_surrender" id="surrenderClick"></div>
                     <div class="mc_chat"></div>
                     <div class="mc_volume">
                     	<input type="range" min="0" max="10" value="10" class="slider" onchange="changeVolume(this.value)">
                     </div>
                     <div class="mc_render" style="background-image: url(battle/bottomrender.png);"></div>
                  </div>
               </div>
               
				<div class="holder holdanime" id="askingSurrender" style="display: none;">
					<div class="txtsurrender">Are you sure you wish to surrender?</div> 
					<img class="surrenderimg"> 
					<div class="btncancel" id="surrenderCancel"></div>
					<div class="btnok" id="surrenderConfirm"></div> 
				</div>
				
				<%
				Class.forName(Connector.drv);
         		try (Connection conn = Connector.getConnection();) {
         			Statement stmt = conn.createStatement();
         			
				ResultSet vs = conn.createStatement().executeQuery("select username from USERS where userID="+session.getAttribute("opp_id"));
				   if (vs.next()) {
					   String opp_username = vs.getString("username");
					   
				%>
				<div class="endgame holdanime" id="winnerQuick" style="display: none;">
					<img src="battle/winner.png"> 
					<div class="endtitle">WINNER</div> 
					<div class="txtendgame" id="textWinnerQuick">
					against <%=opp_username %>.<br><br>
					</div> 
					<div class="btncontinue" onclick="redirectSelection()">
						<span>CONTINUE</span>
					</div>
				</div> 
				
				<div class="endgame holdanime" id="loserQuick" style="display: none;">
					<img src="battle/loser.png"> 
					<div class="endtitle">LOSER</div> 
					<div class="txtendgame" id="textLoserQuick">
					against <%=opp_username %>.<br><br>
					</div> 
					<div class="btncontinue" onclick="redirectSelection()">
						<span>CONTINUE</span>
					</div>
				</div>  
				<%
				   }
         		}
         		catch (SQLException | IOException e) {
		 			System.out.println(e.getMessage());
		 			}
				%>
				             
            </div>
            <!-- <div id="cursor" style="opacity: 1; left: 616px; top: 324px; background-image: url(&quot;../images/kunai.png&quot;);"></div>
            <div id="shuri" style="opacity: 0; left: 616px; top: 324px;"></div>-->
            
            
          
    
<script>
$('#surrenderClick').click(function() {
	$('#askingSurrender').css("display", "block");
});
$('#surrenderCancel').click(function() {
	$('#askingSurrender').css("display", "none");
});
$('#surrenderConfirm').click(function() {
	loser();
});

function UpdateTimeLoop(){
  
   
    if (<%=session.getAttribute("battleEnded")%> != true) {
    	
    	 updateTime();
    	 setTimeout(UpdateTimeLoop, 1000);
	}
   
}

UpdateTimeLoop();

</script>
<script>
window.onload = function() {
	if (<%=session.getAttribute("loggedIn")%> != true) {
		window.location.href = "login.jsp";
	}
	else {
		
		var turn = <%=session.getAttribute("turn")%>;
		defineTurns(turn);
		hats();
	}
};
</script>


<audio id="soundClickEnemy" src="Sounds/Click_Enemy.mp3"></audio>
<audio id="soundClickSkill" src="Sounds/Click_Skill.mp3"></audio>
<audio id="soundDead" src="Sounds/Dead.mp3"></audio>
<audio id="soundEndTurn" src="Sounds/End_Turn.mp3"></audio>
<audio id="soundLose" src="Sounds/LoseV3.mp3"></audio>
<audio id="soundWin" src="Sounds/Win.mp3"></audio>

    
</body>

<%} %>

</html>

