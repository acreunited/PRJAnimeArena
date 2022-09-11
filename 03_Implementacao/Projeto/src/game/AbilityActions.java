package game;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mechanics.Ability;
import mechanics.Character;


@WebServlet("/AbilityActions")
public class AbilityActions extends HttpServlet {
	
	private static final long serialVersionUID = 7215979604673189309L;

	public GamesInfo gameInfo;
	
	public AbilityActions() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		//setting the content type  
		PrintWriter pw = response.getWriter(); 

		HttpSession session = request.getSession();
		
		Character thisChar1 = (Character) session.getAttribute("this_char1_game");
		Character thisChar2 = (Character) session.getAttribute("this_char2_game");
		Character thisChar3 = (Character) session.getAttribute("this_char3_game");
		Character oppChar1 = (Character) session.getAttribute("opp_char1_game");
		Character oppChar2 = (Character) session.getAttribute("opp_char2_game");
		Character oppChar3 = (Character) session.getAttribute("opp_char3_game");

		int id = (int) session.getAttribute("userID");
		
		String action = request.getParameter("action");
		
		if (action.equalsIgnoreCase("seeTarget")) {
			
			int abilityPos = Integer.parseInt( request.getParameter("abilityPos") );
			int selfChar = Integer.parseInt( request.getParameter("selfChar") );

			Ability a = getSelectedAbility(abilityPos, selfChar, thisChar1, thisChar2, thisChar3);
			
			if (a!=null) {
				if (a.isIgnoresInvul()) {
					pw.write(a.getTargetClick()+"-false-false-false");
				}
				else {
					pw.write(a.getTargetClick()
							+"-"+oppChar1.isInvul()+"-"+oppChar2.isInvul()+"-"+oppChar3.isInvul());
				}
				
			}
			else {
				System.out.println("HABILIDADE NULL");
			}
			
			pw.write("break");
			pw.write( thisChar1.isDead()+"-"+ thisChar2.isDead()+"-"+ thisChar3.isDead()
				+"-"+oppChar1.isDead()+"-"+ oppChar2.isDead()+"-"+ oppChar3.isDead());
		
		}

		else if (action.equalsIgnoreCase("applyAbility")) {
			//System.out.println("chegou servlet apply ability");
			String abilityUsedID = request.getParameter("abilityUsedID");
			String allyEnemy = request.getParameter("allyEnemy");
			
			if (abilityUsedID.equalsIgnoreCase("null")) {
				pw.println("nada");
			}
			else {
				
				if (
					allyEnemy.trim().equalsIgnoreCase("ally")
						) {
					pw.println("<div class='effects_border0 zindex1'>");
				}
				else if (
						allyEnemy.trim().equalsIgnoreCase("enemy")
						) {
					pw.println("<div class='effects_border1 zindex0'>");
				}
				else {
					System.out.println("ERROR ALLYENEMY APPLY-ABILITY ABILITYACTIONS.JAVA");
				}

				String username = (String) session.getAttribute("username");
				pw.println("<img src='ViewAbility?id="+abilityUsedID+"' id='"+username+""+abilityUsedID+"' onmouseover='seeActiveSkill(id)' onmouseleave='hideActiveSkill()'>");
//				pw.println("<span class='tooltiptext' id='tooltiptextid'>");
//					pw.println("<span class='tooltiptextname'>SPRINKLING NEEDLES</span>");
//					pw.println("<span class='tooltiptextdesc'>This character will take 10 damage.</span>");
//					pw.println("<span class='tooltiptextduration'>1 TURN LEFT</span>");
//				pw.println("</span>");
				pw.println("</div>");
				
				pw.println("break");
				updateNaturesThisAbility(session, pw, new Ability(Integer.parseInt( abilityUsedID )));
				pw.println("break");
				pw.println(  new Ability(Integer.parseInt(abilityUsedID)).getTargetClick()  );
			
			}
		
		}
		else if (action.equalsIgnoreCase("saveAbilities")) {
			
			saveAbilities(request, session, id, thisChar1, thisChar2, thisChar3);
		}
		else if (action.equalsIgnoreCase("seeNatureCost")) {
			int selfChar = Integer.parseInt( request.getParameter("selfChar") );
			int abilityPos = Integer.parseInt( request.getParameter("abilityPos") );
			
			Ability a = getSelectedAbility(abilityPos, selfChar, thisChar1, thisChar2, thisChar3);
			abilityCost(pw, a);
		}
		else if (action.equalsIgnoreCase("abilityHasNature")) {
			checkCost(session, pw, thisChar1);
			checkCost(session, pw, thisChar2);
			checkCost(session, pw, thisChar3);
		}
		else if (action.equalsIgnoreCase("cancelAbility")) {
			int abilityID = Integer.parseInt( request.getParameter("id") );
			System.out.println(abilityID);
			regainAbilityCost(session, pw, new Ability(abilityID));
			
		}
		else if (action.equalsIgnoreCase("getAbilityNatureByID")) {
			int abilityID = Integer.parseInt( request.getParameter("abilityID") );
			//regainAbilityCost(session, pw, new Ability(abilityID));
			abilityCost(pw,new Ability(abilityID));
		}
		else if (action.equalsIgnoreCase("currentNature")) {
			pw.write(session.getAttribute("taijutsu")+"-"+
					session.getAttribute("heart")+"-"+
					session.getAttribute("energy")+"-"+
					session.getAttribute("spirit")+"-"+
					( (int)session.getAttribute("random") >=5 ) );
		}
		else if (action.equalsIgnoreCase("beforeTurn")) {
			
			//System.out.println((taijutsu+heart+energy+spirit) - random);
			pw.write( ""+ getRandomLeft(session) );
			
		}
		
		else if (action.equalsIgnoreCase("plus")) {
			String natureType = request.getParameter("nature");
			int toRemove = Integer.parseInt( request.getParameter("toRemove") ) +1;
			
			pw.write(""+getNatureType(session, natureType, true)+"-"+toRemove+"-"+getRandomLeft(session));
		}
		else if (action.equalsIgnoreCase("plusExchange")) {
			String natureType = request.getParameter("nature");
			int toRemove = Integer.parseInt( request.getParameter("toRemove") ) +1;			
			int nLeft = Integer.parseInt( request.getParameter("nLeft") ) - 1;			
			
			pw.write(""+getNatureType(session, natureType, true)+"-"+toRemove+"-"+ ((nLeft>=0) ? nLeft : 0) );
		}
		else if (action.equalsIgnoreCase("minus")) {
			String natureType = request.getParameter("nature");
			int toRemove = Integer.parseInt( request.getParameter("toRemove") ) -1;
			
			pw.write(""+getNatureType(session, natureType, false)+"-"+toRemove+"-"+getRandomLeft(session));
		}
		else if (action.equalsIgnoreCase("minusExchange")) {
			String natureType = request.getParameter("nature");
			int toRemove = Integer.parseInt( request.getParameter("toRemove") ) -1;
			int nLeft = Integer.parseInt( request.getParameter("nLeft") ) + 1;	
			
			pw.write(""+getNatureType(session, natureType, false)+"-"+toRemove+"-"+ ((nLeft>=0) ? nLeft : 0));
		}
		else if (action.equalsIgnoreCase("cancelRandom")) {
			session.setAttribute("taijutsu", (int)session.getAttribute("taijutsu") +  Integer.parseInt( request.getParameter("tai")) );
			session.setAttribute("heart", (int)session.getAttribute("heart") +  Integer.parseInt( request.getParameter("heart")) );
			session.setAttribute("energy", (int)session.getAttribute("energy") +  Integer.parseInt( request.getParameter("energy")) );
			session.setAttribute("spirit", (int)session.getAttribute("spirit") +  Integer.parseInt( request.getParameter("spirit")) );
			session.setAttribute("random", (int)session.getAttribute("random") +  Integer.parseInt( request.getParameter("random")) );
		}
		else if (action.equalsIgnoreCase("cancelExchange")) {
			session.setAttribute("taijutsu", (int)session.getAttribute("taijutsu") +  Integer.parseInt( request.getParameter("tai")) );
			session.setAttribute("heart", (int)session.getAttribute("heart") +  Integer.parseInt( request.getParameter("heart")) );
			session.setAttribute("energy", (int)session.getAttribute("energy") +  Integer.parseInt( request.getParameter("energy")) );
			session.setAttribute("spirit", (int)session.getAttribute("spirit") +  Integer.parseInt( request.getParameter("spirit")) );
			session.setAttribute("random", getRandom(session) );
		}
		else if (action.equalsIgnoreCase("confirmExchange")) {
			String selected = request.getParameter("selected");
			System.out.println(selected);
			
			session.setAttribute("taijutsu", (int)session.getAttribute("taijutsu") + ((selected.equalsIgnoreCase("taijutsu")) ? 1 : 0) );
			session.setAttribute("heart", (int)session.getAttribute("heart") + ((selected.equalsIgnoreCase("heart")) ? 1 : 0) );
			session.setAttribute("energy", (int)session.getAttribute("energy") + ((selected.equalsIgnoreCase("energy")) ? 1 : 0) );
			session.setAttribute("spirit",(int)session.getAttribute("spirit") + ((selected.equalsIgnoreCase("spirit")) ? 1 : 0) );
			session.setAttribute("random", getRandom(session) );
			
			pw.println(" <strong class=\"energy0\">x"+session.getAttribute("taijutsu")+"</strong>");
			pw.println(" <strong class=\"energy1\">x"+session.getAttribute("heart")+"</strong>");
			pw.println(" <strong class=\"energy2\">x"+session.getAttribute("energy")+"</strong>");
			pw.println(" <strong class=\"energy3\">x"+session.getAttribute("spirit")+"</strong>");
			pw.println(" <strong class=\"energy4\">x"+session.getAttribute("random")+"</strong>");

		}
		else if (action.equalsIgnoreCase("endTurnAbilities")) {
			
			String allAbilitiesID = request.getParameter("allAbilitiesID");
			String[] abilitiesID = allAbilitiesID.split(",");
			ArrayList<String> duplicate = new ArrayList<String>();
			for (int i = 0; i < abilitiesID.length; i++) {
				if (abilitiesID[i].length() > 0) {
					
					//String abID = abilitiesID[i];
					if (!duplicate.contains(abilitiesID[i])) {
						duplicate.add(abilitiesID[i]);
						pw.println("<img src='ViewAbility?id="+ Integer.parseInt(abilitiesID[i].split("-")[0]) +"' class='item'>");
					}
					
				}
			}

			
			//ArrayList<String> allAbilitiesID =.get(5);
			
			//.add(abilitiesID[i]);
		}
		
		pw.close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	private int getRandom(HttpSession session) {
		int t = (int) session.getAttribute("taijutsu");
		int h = (int) session.getAttribute("heart");
		int e = (int) session.getAttribute("energy");
		int s = (int) session.getAttribute("spirit");
		return t+h+e+s;
	}
	
	private int getRandomLeft(HttpSession session) {
		int taijutsu = (int) session.getAttribute("taijutsu");
		int heart = (int) session.getAttribute("heart");
		int energy = (int) session.getAttribute("energy");
		int spirit = (int) session.getAttribute("spirit");
		int random = (int) session.getAttribute("random");
		
		return (taijutsu+heart+energy+spirit) - random;
	}

	private int getNatureType(HttpSession session, String nature, boolean plus) {
		switch (nature) {
		
			case "Taijutsu":
				
				if (plus) {
					session.setAttribute("taijutsu", (int)session.getAttribute("taijutsu")-1 );
				}
				else {
					session.setAttribute("taijutsu", (int)session.getAttribute("taijutsu")+1 );
				}
				return (int)session.getAttribute("taijutsu");
				
			case "Heart":
				if (plus) {
					session.setAttribute("heart", (int)session.getAttribute("heart")-1 );
				}
				else {
					session.setAttribute("heart", (int)session.getAttribute("heart")+1 );
				}
				return (int)session.getAttribute("heart");
				
			case "Energy":
				if (plus) {
					session.setAttribute("energy", (int)session.getAttribute("energy")-1 );
				}
				else {
					session.setAttribute("energy", (int)session.getAttribute("energy")+1 );
				}
				return (int)session.getAttribute("energy");
				
			case "Spirit":
				if (plus) {
					session.setAttribute("spirit", (int)session.getAttribute("spirit")-1 );
				}
				else {
					session.setAttribute("spirit", (int)session.getAttribute("spirit")+1 );
				}
				return (int)session.getAttribute("spirit");
			
		}
		return -1;
	}
	
	private void regainAbilityCost(HttpSession session, PrintWriter pw, Ability a) {
		int newTaijutsu = (int) session.getAttribute("taijutsu") + a.getnTaijutsu();
		int newHeart = (int) session.getAttribute("heart") + a.getnHeart();
		int newEnergy = (int) session.getAttribute("energy") + a.getnEnergy();
		int newSpirit = (int) session.getAttribute("spirit") + a.getnSpirit();
		
		int newRandom = (int) session.getAttribute("random") + a.getnRandom()+
				a.getnTaijutsu()+a.getnHeart()+a.getnEnergy()+a.getnSpirit();
		//int newRandom = newTaijutsu+newHeart+newEnergy+newSpirit;
		
		updateNatureInGame(session, pw, newTaijutsu, newHeart, newEnergy, newSpirit, newRandom);
	}
	
	private void updateNaturesThisAbility(HttpSession session, PrintWriter pw, Ability a) {
		int newTaijutsu = (int) session.getAttribute("taijutsu") - a.getnTaijutsu();
		int newHeart = (int) session.getAttribute("heart") - a.getnHeart();
		int newEnergy = (int) session.getAttribute("energy") - a.getnEnergy();
		int newSpirit = (int) session.getAttribute("spirit") - a.getnSpirit();
		//int newRandom = (int) session.getAttribute("random") - (newTaijutsu+newHeart+newEnergy+newSpirit) - a.getnRandom();
		int newRandom = (int) session.getAttribute("random") - a.getnTaijutsu()-a.getnHeart()-a.getnEnergy()-a.getnSpirit() - a.getnRandom();
		
		updateNatureInGame(session, pw, newTaijutsu, newHeart, newEnergy, newSpirit, newRandom);
	}
	
	private void updateNatureInGame(HttpSession session, PrintWriter pw,
			int newTaijutsu, int newHeart, int newEnergy, int newSpirit, int newRandom) {
		

		session.setAttribute("taijutsu", newTaijutsu);
		session.setAttribute("heart", newHeart);
		session.setAttribute("energy", newEnergy);
		session.setAttribute("spirit", newSpirit);
		session.setAttribute("random", newRandom);
		
		pw.println(" <strong class=\"energy0\">x"+session.getAttribute("taijutsu")+"</strong>");
		pw.println(" <strong class=\"energy1\">x"+session.getAttribute("heart")+"</strong>");
		pw.println(" <strong class=\"energy2\">x"+session.getAttribute("energy")+"</strong>");
		pw.println(" <strong class=\"energy3\">x"+session.getAttribute("spirit")+"</strong>");
		pw.println(" <strong class=\"energy4\">x"+session.getAttribute("random")+"</strong>");

		
	}
	
	private void checkCost(HttpSession session, PrintWriter pw, Character c) {
		
		pw.println( canUse(session, c.getAbility1()) + "-");
		pw.println( canUse(session, c.getAbility2())  + "-");
		pw.println( canUse(session, c.getAbility3())  + "-");
		pw.println( canUse(session, c.getAbility4())  + "-");
		
	}
	
	private boolean canUse(HttpSession session, Ability a) {

		int taijutsu = (int) session.getAttribute("taijutsu");
		int heart = (int) session.getAttribute("heart");
		int energy = (int) session.getAttribute("energy");
		int spirit = (int) session.getAttribute("spirit");
		int random = (int) session.getAttribute("random");
		
		boolean hasTaijutsu = taijutsu >= a.getnTaijutsu();
		boolean hasHeart = heart >= a.getnHeart();
		boolean hasEnergy = energy >= a.getnEnergy();
		boolean hasSpirit = spirit >= a.getnSpirit();
		boolean hasRandom = 0 <= ( random - a.getnRandom()-a.getnSpirit()-a.getnEnergy()-a.getnHeart()-a.getnTaijutsu() );
		
		if (hasTaijutsu&&hasHeart&&hasEnergy&&hasSpirit&&hasRandom) {
			return true;
		}
				
		return false;
	}
	
	private void abilityCost(PrintWriter pw, Ability a) {
		
		if (a.getnTaijutsu()>0) {
			for (int i = 0; i < a.getnTaijutsu(); i++) {
				pw.println("<img src='battle/Taijutsu.png'>");
			}
		}
		if (a.getnHeart()>0) {
			for (int i = 0; i < a.getnHeart(); i++) {
				pw.println("<img src='battle/Heart.png'>");
			}
		}
		if (a.getnEnergy()>0) {
			for (int i = 0; i < a.getnEnergy(); i++) {
				pw.println("<img src='battle/Energy.png'>");
			}
		}
		if (a.getnSpirit() > 0) {
			for (int i = 0; i < a.getnSpirit(); i++) {
				pw.println("<img src='battle/Spirit.png'>");
			}
		}
		if (a.getnRandom()>0) {
			for (int i = 0; i < a.getnRandom(); i++) {
				pw.println("<img src='battle/Random.png'>");
			}
		}
	}

	private Ability getSelectedAbility(int abilityPos, int selfChar, Character thisChar1,Character thisChar2,Character thisChar3) {
		Character c = null;
		Ability a = null;
		
		if (selfChar==0) {
			c = thisChar1;
		}
		else if (selfChar==1) {
			c = thisChar2;
		}
		else if (selfChar==2) {
			c = thisChar3;
		}
		
		if (abilityPos==0) {
			a = c.getAbility1();
		}
		else if (abilityPos==1) {
			a = c.getAbility2();
		}
		else if (abilityPos==2) {
			a = c.getAbility3();
		}
		else if (abilityPos==3) {
			a = c.getAbility4();
		}
		return a;
	}
	
	private void saveAbilities(HttpServletRequest request, HttpSession session, int id, Character thisChar1,Character thisChar2,Character thisChar3) {
		String uuid = (String) session.getAttribute("uuid");
		
		String allAbilitiesUsed = request.getParameter("allAbilitiesUsed");
		String allCharsUsedSkill = request.getParameter("allCharsUsedSkill");
		String allTargets = request.getParameter("allTargets");
		String allAllyEnemy = request.getParameter("allAllyEnemy");
		String allAbilitiesID = request.getParameter("allAbilitiesID");
		
		String[] abilitiesUsed = allAbilitiesUsed.split(",");
		String[] charsUsedSkill = allCharsUsedSkill.split(",");
		String[] targets = allTargets.split(",");
		String[] allyEnemy = allAllyEnemy.split(",");
		String[] abilitiesID = allAbilitiesID.split(",");
		

		
		for (int i = 0; i < abilitiesUsed.length; i++) {
			if (abilitiesUsed[i].length() > 0) {

				Ability a = getSelectedAbility(Integer.parseInt(abilitiesUsed[i]), Integer.parseInt(charsUsedSkill[i]), thisChar1, thisChar2, thisChar3);
				a.setCurrentCooldown( a.getCooldown()+1 );
				
				//removeOldIfExists(id, abilitiesUsed[i], charsUsedSkill[i], targets[i], allyEnemy[i], abilitiesID[i]);
				
				GameUtils.activeAbilitiesUsed.get(id).add(abilitiesUsed[i]);
				GameUtils.activeCharsUsedSkill.get(id).add(charsUsedSkill[i]);
				GameUtils.activeTargets.get(id).add(targets[i]);
				GameUtils.activeAllyEnemy.get(id).add(allyEnemy[i]);
				GameUtils.activeAbilitiesID.get(id).add(abilitiesID[i]);
				
				GameUtils.enemy_activeAbilitiesUsed.get(id).add(abilitiesUsed[i]);
				GameUtils.enemy_activeCharsUsedSkill.get(id).add(charsUsedSkill[i]);
				GameUtils.enemy_activeTargets.get(id).add(targets[i]);
				GameUtils.enemy_activeAllyEnemy.get(id).add(allyEnemy[i]);
				GameUtils.enemy_activeAbilitiesID.get(id).add(abilitiesID[i]);
			}
			
		}
	
	}

}
