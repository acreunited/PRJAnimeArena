package main;

import java.io.IOException;

import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

import DB.InsertIntoBD;



@WebServlet("/CreateCharacterMission")
@MultipartConfig(maxFileSize = 134217728) 

public class CreateCharacterMission extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateCharacterMission() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		ManipulateXML xml = new ManipulateXML();
		
		try {
			HttpSession session = request.getSession(false); // Se j? est? a adicionar recursos e n?o tem sessao 
			
			String characterName = request.getParameter("characterName");
			if (characterName == null || characterName.equals("null") || characterName.equals("")) {
				sendFailMessage(request, response, "Character Name cannot be null");
				return;
			}
			String characterDescription = request.getParameter("characterDescription");
			if (characterDescription == null || characterDescription.equals("null") || characterDescription.equals("")) {
				sendFailMessage(request, response, "Character Description cannot be null");
				return;
			}
			
			Part characterPic = request.getPart("charPic");
			
			String charAnime = request.getParameter("charAnime");
			
			String defaultORmission = request.getParameter("defaultmission");

			InsertIntoBD insert = new InsertIntoBD();

			InputStream inputCharPic = null; // input stream of the upload file
			int characterID = insert.createCharacter();
			
			//TODO se algo correu mal, ? preciso apagar esta personagem!
			if (characterID == -1) {
				sendFailMessage(request, response, "Character Was Not Created");
				return;
			}
			
			//se personagem foi criada, precisamos adicionar ao anime correspondente
			switch (charAnime) {
			
			case "Bleach":
				insert.createBleach(characterID);
				break;
			case "DemonSlayer":
				insert.createDemonSlayer(characterID);
				break;
			case "OnePunchMan":
				insert.createOnePunchMan(characterID);
				break;
			case "HunterXHunter":
				insert.createHunterXHunter(characterID);
				break;
			case "SAO":
				insert.createSAO(characterID);
				break;
			}
			
			//create Character Theme
			if (characterPic!=null && characterPic.getSize()!=0) {
				inputCharPic = characterPic.getInputStream();
				
				insert.createThemeCharacter(characterID, 0, "name char "+characterID, null, "descricao char "+characterID);
				insert.createThemeCharacter(characterID, 1, characterName, inputCharPic, characterDescription);
			}
			
			//create abilities
			String ability1Name = request.getParameter("ability1");
			if (ability1Name == null || ability1Name.equals("null") || ability1Name.equals("")) {
				sendFailMessage(request, response, "Ability Name cannot be null");
				return;
			}
			String ability1Description = request.getParameter("ability1Description");
			if (ability1Description == null || ability1Description.equals("null") || ability1Description.equals("")) {
				sendFailMessage(request, response, "Ability Description cannot be null");
				return;
			}
			String ability1taijutsu = request.getParameter("ability1taijutsu");
			if (ability1taijutsu == null || ability1taijutsu.equals("null") || ability1taijutsu.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability1heart = request.getParameter("ability1heart");
			if (ability1heart == null || ability1heart.equals("null") || ability1heart.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability1energy = request.getParameter("ability1energy");
			if (ability1energy == null || ability1energy.equals("null") || ability1energy.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability1spirit = request.getParameter("ability1spirit");
			if (ability1spirit == null || ability1spirit.equals("null") || ability1spirit.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability1random = request.getParameter("ability1random");
			if (ability1random == null || ability1random.equals("null") || ability1random.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability1cooldown = request.getParameter("ability1cd");
			if (ability1cooldown == null || ability1cooldown.equals("null") || ability1cooldown.equals("")) {
				sendFailMessage(request, response, "Ability Cooldown cannot be null");
				return;
			}
			
			int abilityID = insert.createAbility(Integer.parseInt(ability1cooldown), characterID);
			insert.createAbilityNature(
					abilityID, 
					Integer.parseInt(ability1taijutsu), 
					Integer.parseInt(ability1heart), 
					Integer.parseInt(ability1energy), 
					Integer.parseInt(ability1spirit), 
					Integer.parseInt(ability1random)
			);

			Part ability1Pic = request.getPart("ability1image");
			InputStream inputAbility1 = null; 
			
			//create THEME_ABILITY
			if (ability1Pic!=null && ability1Pic.getSize()!=0) {
				inputAbility1 = ability1Pic.getInputStream();
				
				insert.createAbilityTheme(abilityID, 0, "name ability "+abilityID, null, "descricao ability "+abilityID);
				insert.createAbilityTheme(abilityID, 1, ability1Name, inputAbility1, ability1Description);
			}
			
			String targetClick1 = request.getParameter("ability1target");
			
			String damageNumber1 = null;
			String damageDuration1 = null;
			String doesDamage = request.getParameter("ability1damage");
			if (doesDamage.equalsIgnoreCase("yes")) {
				damageNumber1 = request.getParameter("ability1damageNumber");
				damageDuration1 = request.getParameter("ability1damageDuration");
			}

			//ability<%=i%>HealIncreasePerUse
			String damageIncreasePerUse1 = request.getParameter("ability1increaseAbilityDamage");
			String healIncreasePerUse1 = request.getParameter("ability1HealIncreasePerUse");
			String permanentDamageIncrease1 = request.getParameter("ability1increasePermanentDamage");
			String stunDuration1 = request.getParameter("ability1stun");
			String becomesInvul1 = request.getParameter("ability1beInvul");
			String ignoresInvul1 = request.getParameter("ability1ignoreInvul");
			
			String removeNatureNumber1 = null;
			String removeNatureDuration1 = null;
			String removesNature = request.getParameter("ability1removeNature");
			if (removesNature.equalsIgnoreCase("yes")) {
				removeNatureNumber1 = request.getParameter("ability1removesNatureNumber");
				removeNatureDuration1 = request.getParameter("ability1removesNatureDuration");
			}

			
			String gainNatureNumber1 = null;
			String gainNatureDuration1 = null;
			String gainsNature = request.getParameter("ability1gainNature");
			if (gainsNature.equalsIgnoreCase("yes")) {
				gainNatureNumber1 = request.getParameter("ability1gainNatureNumber");
				gainNatureDuration1 = request.getParameter("ability1gainNatureDuration");
			}
	
			
			
			String gainHPNumber1 = null;
			String gainHPDuration1 = null;
			String gainsHP = request.getParameter("ability1gainHP");
			if (gainsHP.equalsIgnoreCase("yes")) {
				gainHPNumber1 = request.getParameter("ability1gainHPNumber");
				gainHPDuration1 = request.getParameter("ability1gainHPDuration");
			}

			
			String gainDRNumber1 = null;
			String gainDRDuration1 = null;
			String gainsDR = request.getParameter("ability1gainDR");
			if (gainsDR.equalsIgnoreCase("yes")) {
				gainDRNumber1 = request.getParameter("ability1gainDRNumber");
				gainDRDuration1 = request.getParameter("ability1gainDRDuration");
			}

			
			String moreDamagePerHPLostDamage1 = null;
			String moreDamagePerHPLostHP1 = null;
			String damagePerHP = request.getParameter("ability1extraDmamagePerSelfHPLost");
			if (damagePerHP.equalsIgnoreCase("yes")) {
				moreDamagePerHPLostDamage1 = request.getParameter("ability1extraDmamagePerSelfHPLostNumber");
				moreDamagePerHPLostHP1 = request.getParameter("ability1extraDmamagePerSelfHPLostHP");
			}

			
			String moreDamagePerEnemyHPLostDamage1 = null;
			String moreDamagePerEnemyHPLostHP1 = null;
			String damagePerHPEnemy = request.getParameter("ability1extraDmamagePerEnemyHPLost");
			if (damagePerHPEnemy.equalsIgnoreCase("yes")) {
				moreDamagePerEnemyHPLostDamage1 = request.getParameter("ability1extraDmamagePerEnemyHPLostNumber");
				moreDamagePerEnemyHPLostHP1 = request.getParameter("ability1extraDmamagePerEnemyHPLostHP");
			}

			String temporaryDamageIncreaseDamage1 = null;
			String temporaryDamageIncreaseDuration1 = null;
			String temporaryDamageIncreaseTarget1 = null;
			String temporaryDamageIncreaseWhich1 = null;
			String damageTemp = request.getParameter("ability1extraDamageTemporary");
			if (damageTemp.equalsIgnoreCase("yes")) {
				temporaryDamageIncreaseDamage1 = request.getParameter("ability1extraDamageTemporaryNumber");
				temporaryDamageIncreaseDuration1 = request.getParameter("ability1extraDamageTemporaryDuration");
				temporaryDamageIncreaseTarget1 = request.getParameter("ability1extraDamageTemporaryTarget");
				temporaryDamageIncreaseWhich1 = request.getParameter("ability1extraDamageTemporaryWhich");
			}

			
			String activeTarget1 = null;
			String activeText1 = null;
			String activeDuration1 = null;
			String active1 = request.getParameter("ability1ActiveSkill");
			if (active1.equalsIgnoreCase("yes")) {
				activeTarget1 = request.getParameter("ability1activeTarget");
				activeText1 = request.getParameter("ability1activeText");
				activeDuration1 = request.getParameter("ability1activeDuration");
			}

			
			xml.writeXML(""+abilityID, ability1Name, ability1Description, ability1taijutsu, ability1heart,
					ability1energy, ability1spirit, ability1random, ability1cooldown, targetClick1, 
					damageNumber1, damageDuration1, ignoresInvul1, stunDuration1, becomesInvul1, 
					damageIncreasePerUse1, healIncreasePerUse1, permanentDamageIncrease1, removeNatureNumber1, 
					removeNatureDuration1, gainNatureNumber1, gainNatureDuration1, gainHPNumber1, 
					gainHPDuration1, gainDRNumber1, gainDRDuration1, 
					moreDamagePerHPLostDamage1, moreDamagePerHPLostHP1, moreDamagePerEnemyHPLostDamage1, 
					moreDamagePerEnemyHPLostHP1, temporaryDamageIncreaseDamage1, temporaryDamageIncreaseDuration1,
					temporaryDamageIncreaseTarget1, temporaryDamageIncreaseWhich1,
					activeTarget1,activeText1,activeDuration1);
			
			
			
			//ABILITY 2
			String ability2Name = request.getParameter("ability2");
			if (ability2Name == null || ability2Name.equals("null") || ability2Name.equals("")) {
				sendFailMessage(request, response, "Ability Name cannot be null");
				return;
			}
			String ability2Description = request.getParameter("ability2Description");
			if (ability2Description == null || ability2Description.equals("null") || ability2Description.equals("")) {
				sendFailMessage(request, response, "Ability Description cannot be null");
				return;
			}
			String ability2taijutsu = request.getParameter("ability2taijutsu");
			if (ability2taijutsu == null || ability2taijutsu.equals("null") || ability2taijutsu.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability2heart = request.getParameter("ability2heart");
			if (ability2heart == null || ability2heart.equals("null") || ability2heart.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability2energy = request.getParameter("ability2energy");
			if (ability2energy == null || ability2energy.equals("null") || ability2energy.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability2spirit = request.getParameter("ability2spirit");
			if (ability2spirit == null || ability2spirit.equals("null") || ability2spirit.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability2random = request.getParameter("ability2random");
			if (ability2random == null || ability2random.equals("null") || ability2random.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability2cooldown = request.getParameter("ability2cd");
			if (ability2cooldown == null || ability2cooldown.equals("null") || ability2cooldown.equals("")) {
				sendFailMessage(request, response, "Ability Cooldown cannot be null");
				return;
			}
			
			int ability2ID = insert.createAbility(Integer.parseInt(ability2cooldown), characterID);
			insert.createAbilityNature(
					ability2ID, 
					Integer.parseInt(ability2taijutsu), 
					Integer.parseInt(ability2heart), 
					Integer.parseInt(ability2energy), 
					Integer.parseInt(ability2spirit), 
					Integer.parseInt(ability2random)
			);

			Part ability2Pic = request.getPart("ability2image");
			InputStream inputAbility2 = null; 
			
			//create THEME_ABILITY
			if (ability2Pic!=null && ability2Pic.getSize()!=0) {
				inputAbility2 = ability2Pic.getInputStream();
				
				insert.createAbilityTheme(ability2ID, 0, "name ability "+ability2ID, null, "descricao ability "+ability2ID);
				insert.createAbilityTheme(ability2ID, 1, ability2Name, inputAbility2, ability2Description);
			}
			
			String targetClick2 = request.getParameter("ability2target");
			
			String damageNumber2 = null;
			String damageDuration2 = null;
			String doesDamage2 = request.getParameter("ability2damage");
			if (doesDamage2.equalsIgnoreCase("yes")) {
				damageNumber2 = request.getParameter("ability2damageNumber");
				damageDuration2 = request.getParameter("ability2damageDuration");
			}

			
			String damageIncreasePerUse2 = request.getParameter("ability2increaseAbilityDamage");
			String healIncreasePerUse2 = request.getParameter("ability2HealIncreasePerUse");
			String permanentDamageIncrease2 = request.getParameter("ability2increasePermanentDamage");
			String stunDuration2 = request.getParameter("ability2stun");
			String becomesInvul2 = request.getParameter("ability2beInvul");
			String ignoresInvul2 = request.getParameter("ability2ignoreInvul");
			
			String removeNatureNumber2 = null;
			String removeNatureDuration2 = null;
			String removesNature2 = request.getParameter("ability2removeNature");
			if (removesNature2.equalsIgnoreCase("yes")) {
				removeNatureNumber2 = request.getParameter("ability2removesNatureNumber");
				removeNatureDuration2 = request.getParameter("ability2removesNatureDuration");
			}

			
			String gainNatureNumber2 = null;
			String gainNatureDuration2 = null;
			String gainsNature2 = request.getParameter("ability2gainNature");
			if (gainsNature2.equalsIgnoreCase("yes")) {
				gainNatureNumber2 = request.getParameter("ability2gainNatureNumber");
				gainNatureDuration2 = request.getParameter("ability2gainNatureDuration");
			}

			
			
			String gainHPNumber2 = null;
			String gainHPDuration2 = null;
			String gainsHP2 = request.getParameter("ability2gainHP");
			if (gainsHP2.equalsIgnoreCase("yes")) {
				gainHPNumber2 = request.getParameter("ability2gainHPNumber");
				gainHPDuration2 = request.getParameter("ability2gainHPDuration");
			}
	
			String gainDRNumber2 = null;
			String gainDRDuration2 = null;
			String gainsDR2 = request.getParameter("ability2gainDR");
			if (gainsDR2.equalsIgnoreCase("yes")) {
				gainDRNumber2 = request.getParameter("ability2gainDRNumber");
				gainDRDuration2 = request.getParameter("ability2gainDRDuration");
			}

			
			String moreDamagePerHPLostDamage2 = null;
			String moreDamagePerHPLostHP2 = null;
			String damagePerHP2 = request.getParameter("ability2extraDmamagePerSelfHPLost");
			if (damagePerHP2.equalsIgnoreCase("yes")) {
				moreDamagePerHPLostDamage2 = request.getParameter("ability2extraDmamagePerSelfHPLostNumber");
				moreDamagePerHPLostHP2 = request.getParameter("ability2extraDmamagePerSelfHPLostHP");
			}

			String moreDamagePerEnemyHPLostDamage2 = null;
			String moreDamagePerEnemyHPLostHP2 = null;
			String damagePerHPEnemy2 = request.getParameter("ability2extraDmamagePerEnemyHPLost");
			if (damagePerHPEnemy2.equalsIgnoreCase("yes")) {
				moreDamagePerEnemyHPLostDamage2 = request.getParameter("ability2extraDmamagePerEnemyHPLostNumber");
				moreDamagePerEnemyHPLostHP2 = request.getParameter("ability2extraDmamagePerEnemyHPLostHP");
			}
	
			
			String temporaryDamageIncreaseDamage2 = null;
			String temporaryDamageIncreaseDuration2 = null;
			String temporaryDamageIncreaseTarget2 = null;
			String temporaryDamageIncreaseWhich2 = null;
			String damageTem2 = request.getParameter("ability2extraDamageTemporary");
			if (damageTem2.equalsIgnoreCase("yes")) {
				temporaryDamageIncreaseDamage2 = request.getParameter("ability2extraDamageTemporaryNumber");
				temporaryDamageIncreaseDuration2 = request.getParameter("ability2extraDamageTemporaryDuration");
				temporaryDamageIncreaseTarget2 = request.getParameter("ability2extraDamageTemporaryTarget");
				temporaryDamageIncreaseWhich2 = request.getParameter("ability2extraDamageTemporaryWhich");
			}

	
			String activeTarget2 = null;
			String activeText2 = null;
			String activeDuration2 = null;
			String active2 = request.getParameter("ability2ActiveSkill");
			if (active2.equalsIgnoreCase("yes")) {
				activeTarget2 = request.getParameter("ability2activeTarget");
				activeText2 = request.getParameter("ability2activeText");
				activeDuration2 = request.getParameter("ability2activeDuration");
			}

			xml.writeXML(""+ability2ID, ability2Name, ability2Description, ability2taijutsu, ability2heart,
					ability2energy, ability2spirit, ability2random, ability2cooldown, targetClick2, 
					damageNumber2, damageDuration2, ignoresInvul2, stunDuration2, becomesInvul2, 
					damageIncreasePerUse2, healIncreasePerUse2, permanentDamageIncrease2, removeNatureNumber2, 
					removeNatureDuration2, gainNatureNumber2, gainNatureDuration2, gainHPNumber2, 
					gainHPDuration2, gainDRNumber2, gainDRDuration2, 
					moreDamagePerHPLostDamage2, moreDamagePerHPLostHP2, moreDamagePerEnemyHPLostDamage2, 
					moreDamagePerEnemyHPLostHP2, temporaryDamageIncreaseDamage2, temporaryDamageIncreaseDuration2, 
					temporaryDamageIncreaseTarget2, temporaryDamageIncreaseWhich2,
					activeTarget2,activeText2,activeDuration2);
			
			//ABILITY 3
			String ability3Name = request.getParameter("ability3");
			if (ability3Name == null || ability3Name.equals("null") || ability3Name.equals("")) {
				sendFailMessage(request, response, "Ability Name cannot be null");
				return;
			}
			String ability3Description = request.getParameter("ability3Description");
			if (ability3Description == null || ability3Description.equals("null") || ability3Description.equals("")) {
				sendFailMessage(request, response, "Ability Description cannot be null");
				return;
			}
			String ability3taijutsu = request.getParameter("ability3taijutsu");
			if (ability3taijutsu == null || ability3taijutsu.equals("null") || ability3taijutsu.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability3heart = request.getParameter("ability3heart");
			if (ability3heart == null || ability3heart.equals("null") || ability3heart.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability3energy = request.getParameter("ability3energy");
			if (ability3energy == null || ability3energy.equals("null") || ability3energy.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability3spirit = request.getParameter("ability3spirit");
			if (ability3spirit == null || ability3spirit.equals("null") || ability3spirit.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability3random = request.getParameter("ability3random");
			if (ability3random == null || ability3random.equals("null") || ability3random.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability3cooldown = request.getParameter("ability3cd");
			if (ability3cooldown == null || ability3cooldown.equals("null") || ability3cooldown.equals("")) {
				sendFailMessage(request, response, "Ability Cooldown cannot be null");
				return;
			}
			
			int ability3ID = insert.createAbility(Integer.parseInt(ability3cooldown), characterID);
			insert.createAbilityNature(
					ability3ID, 
					Integer.parseInt(ability3taijutsu), 
					Integer.parseInt(ability3heart), 
					Integer.parseInt(ability3energy), 
					Integer.parseInt(ability3spirit), 
					Integer.parseInt(ability3random)
			);

			Part ability3Pic = request.getPart("ability3image");
			InputStream inputAbility3 = null; 
			
			//create THEME_ABILITY
			if (ability3Pic!=null && ability3Pic.getSize()!=0) {
				inputAbility3 = ability3Pic.getInputStream();
				
				insert.createAbilityTheme(ability3ID, 0, "name ability "+ability3ID, null, "descricao ability "+ability3ID);
				insert.createAbilityTheme(ability3ID, 1, ability3Name, inputAbility3, ability3Description);
			}
			
			
			String targetClick3 = request.getParameter("ability3target");
			
			String damageNumber3 = null;
			String damageDuration3 = null;
			String doesDamage3 = request.getParameter("ability3damage");
			if (doesDamage3.equalsIgnoreCase("yes")) {
				damageNumber3 = request.getParameter("ability3damageNumber");
				damageDuration3 = request.getParameter("ability3damageDuration");
			}
	
			
			String damageIncreasePerUse3 = request.getParameter("ability3increaseAbilityDamage");
			String healIncreasePerUse3 = request.getParameter("ability3HealIncreasePerUse");
			String permanentDamageIncrease3 = request.getParameter("ability3increasePermanentDamage");
			String stunDuration3 = request.getParameter("ability3stun");
			String becomesInvul3 = request.getParameter("ability3beInvul");
			String ignoresInvul3 = request.getParameter("ability3ignoreInvul");
			
			String removeNatureNumber3 = null;
			String removeNatureDuration3 = null;
			String removesNature3 = request.getParameter("ability3removeNature");
			if (removesNature3.equalsIgnoreCase("yes")) {
				removeNatureNumber3 = request.getParameter("ability3removesNatureNumber");
				removeNatureDuration3 = request.getParameter("ability3removesNatureDuration");
			}

			
			String gainNatureNumber3 = null;
			String gainNatureDuration3 = null;
			String gainsNature3 = request.getParameter("ability3gainNature");
			if (gainsNature3.equalsIgnoreCase("yes")) {
				gainNatureNumber3 = request.getParameter("ability3gainNatureNumber");
				gainNatureDuration3 = request.getParameter("ability3gainNatureDuration");
			}

			
			String gainHPNumber3 = null;
			String gainHPDuration3 = null;
			String gainsHP3 = request.getParameter("ability3gainHP");
			if (gainsHP3.equalsIgnoreCase("yes")) {
				gainHPNumber3 = request.getParameter("ability3gainHPNumber");
				gainHPDuration3 = request.getParameter("ability3gainHPDuration");
			}

						
			String gainDRNumber3 = null;
			String gainDRDuration3 = null;
			String gainsDR3 = request.getParameter("ability3gainDR");
			if (gainsDR3.equalsIgnoreCase("yes")) {
				gainDRNumber3 = request.getParameter("ability3gainDRNumber");
				gainDRDuration3 = request.getParameter("ability3gainDRDuration");
			}

			
			String moreDamagePerHPLostDamage3 = null;
			String moreDamagePerHPLostHP3 = null;
			String damagePerHP3 = request.getParameter("ability3extraDmamagePerSelfHPLost");
			if (damagePerHP3.equalsIgnoreCase("yes")) {
				moreDamagePerHPLostDamage3 = request.getParameter("ability3extraDmamagePerSelfHPLostNumber");
				moreDamagePerHPLostHP3 = request.getParameter("ability3extraDmamagePerSelfHPLostHP");
			}
			
			String moreDamagePerEnemyHPLostDamage3 = null;
			String moreDamagePerEnemyHPLostHP3 = null;
			String damagePerHPEnemy3 = request.getParameter("ability3extraDmamagePerEnemyHPLost");
			if (damagePerHPEnemy3.equalsIgnoreCase("yes")) {
				moreDamagePerEnemyHPLostDamage3 = request.getParameter("ability3extraDmamagePerEnemyHPLostNumber");
				moreDamagePerEnemyHPLostHP3 = request.getParameter("ability3extraDmamagePerEnemyHPLostHP");
			}
	
			String temporaryDamageIncreaseDamage3 = null;
			String temporaryDamageIncreaseDuration3 = null;
			String temporaryDamageIncreaseTarget3 = null;
			String temporaryDamageIncreaseWhich3 = null;
			String damageTemp3 = request.getParameter("ability3extraDamageTemporary");
			if (damageTemp3.equalsIgnoreCase("yes")) {
				temporaryDamageIncreaseDamage3 = request.getParameter("ability3extraDamageTemporaryNumber");
				temporaryDamageIncreaseDuration3 = request.getParameter("ability3extraDamageTemporaryDuration");
				temporaryDamageIncreaseTarget3 = request.getParameter("ability3extraDamageTemporaryTarget");
				temporaryDamageIncreaseWhich3 = request.getParameter("ability3extraDamageTemporaryWhich");
			}

			String activeTarget3 = null;
			String activeText3 = null;
			String activeDuration3 = null;
			String active3 = request.getParameter("ability3ActiveSkill");
			if (active3.equalsIgnoreCase("yes")) {
				activeTarget3 = request.getParameter("ability3activeTarget");
				activeText3 = request.getParameter("ability3activeText");
				activeDuration3 = request.getParameter("ability3activeDuration");
			}

			xml.writeXML(""+ability3ID, ability3Name, ability3Description, ability3taijutsu, ability3heart,
					ability3energy, ability3spirit, ability3random, ability3cooldown, targetClick3, 
					damageNumber3, damageDuration3, ignoresInvul3, stunDuration3, becomesInvul3, 
					damageIncreasePerUse3, healIncreasePerUse3, permanentDamageIncrease3, removeNatureNumber3, 
					removeNatureDuration3, gainNatureNumber3, gainNatureDuration3, gainHPNumber3, 
					gainHPDuration3, gainDRNumber3, gainDRDuration3, 
					moreDamagePerHPLostDamage3, moreDamagePerHPLostHP3, moreDamagePerEnemyHPLostDamage3, 
					moreDamagePerEnemyHPLostHP3, temporaryDamageIncreaseDamage3, temporaryDamageIncreaseDuration3,
					temporaryDamageIncreaseTarget3, temporaryDamageIncreaseWhich3,
					activeTarget3,activeText3,activeDuration3);
			
			
			//ABILITY 4
			String ability4Name = request.getParameter("ability4");
			if (ability4Name == null || ability4Name.equals("null") || ability4Name.equals("")) {
				sendFailMessage(request, response, "Ability Name cannot be null");
				return;
			}
			String ability4Description = request.getParameter("ability4Description");
			if (ability4Description == null || ability4Description.equals("null") || ability4Description.equals("")) {
				sendFailMessage(request, response, "Ability Description cannot be null");
				return;
			}
			String ability4taijutsu = request.getParameter("ability4taijutsu");
			if (ability4taijutsu == null || ability4taijutsu.equals("null") || ability4taijutsu.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability4heart = request.getParameter("ability4heart");
			if (ability4heart == null || ability4heart.equals("null") || ability4heart.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability4energy = request.getParameter("ability4energy");
			if (ability4energy == null || ability4energy.equals("null") || ability4energy.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability4spirit = request.getParameter("ability4spirit");
			if (ability4spirit == null || ability4spirit.equals("null") || ability4spirit.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability4random = request.getParameter("ability4random");
			if (ability4random == null || ability4random.equals("null") || ability4random.equals("")) {
				sendFailMessage(request, response, "Nature cannot be null");
				return;
			}
			String ability4cooldown = request.getParameter("ability4cd");
			if (ability4cooldown == null || ability4cooldown.equals("null") || ability4cooldown.equals("")) {
				sendFailMessage(request, response, "Ability Cooldown cannot be null");
				return;
			}
			
			int ability4ID = insert.createAbility(Integer.parseInt(ability4cooldown), characterID);
			insert.createAbilityNature(
					ability4ID, 
					Integer.parseInt(ability4taijutsu), 
					Integer.parseInt(ability4heart), 
					Integer.parseInt(ability4energy), 
					Integer.parseInt(ability4spirit), 
					Integer.parseInt(ability4random)
			);

			Part ability4Pic = request.getPart("ability4image");
			InputStream inputAbility4 = null; 
			
			//create THEME_ABILITY
			if (ability4Pic!=null && ability4Pic.getSize()!=0) {
				inputAbility4 = ability4Pic.getInputStream();
				
				insert.createAbilityTheme(ability4ID, 0, "name ability "+ability4ID, null, "descricao ability "+ability4ID);
				insert.createAbilityTheme(ability4ID, 1, ability4Name, inputAbility4, ability4Description);
			}
			
			String targetClick4 = request.getParameter("ability4target");
			
			String damageNumber4 = null;
			String damageDuration4 = null;
			String doesDamage4 = request.getParameter("ability4damage");
			if (doesDamage4.equalsIgnoreCase("yes")) {
				damageNumber4 = request.getParameter("ability4damageNumber");
				damageDuration4 = request.getParameter("ability4damageDuration");
			}

			
			String damageIncreasePerUse4 = request.getParameter("ability4increaseAbilityDamage");
			String healIncreasePerUse4 = request.getParameter("ability4HealIncreasePerUse");
			String permanentDamageIncrease4 = request.getParameter("ability4increasePermanentDamage");
			String stunDuration4 = request.getParameter("ability4stun");
			String becomesInvul4 = request.getParameter("ability4beInvul");
			String ignoresInvul4 = request.getParameter("ability4ignoreInvul");
			
			String removeNatureNumber4 = null;
			String removeNatureDuration4 = null;
			String removesNature4 = request.getParameter("ability4removeNature");
			if (removesNature4.equalsIgnoreCase("yes")) {
				removeNatureNumber4 = request.getParameter("ability4removesNatureNumber");
				removeNatureDuration4 = request.getParameter("ability4removesNatureDuration");
			}

			
			String gainNatureNumber4 = null;
			String gainNatureDuration4 = null;
			String gainsNature4 = request.getParameter("ability4gainNature");
			if (gainsNature4.equalsIgnoreCase("yes")) {
				gainNatureNumber4 = request.getParameter("ability4gainNatureNumber");
				gainNatureDuration4 = request.getParameter("ability4gainNatureDuration");
			}
				
			
			String gainHPNumber4 = null;
			String gainHPDuration4 = null;
			String gainsHP4 = request.getParameter("ability4gainHP");
			if (gainsHP4.equalsIgnoreCase("yes")) {
				gainHPNumber4 = request.getParameter("ability4gainHPNumber");
				gainHPDuration4 = request.getParameter("ability4gainHPDuration");
			}

						
			String gainDRNumber4 = null;
			String gainDRDuration4 = null;
			String gainsDR4 = request.getParameter("ability4gainDR");
			if (gainsDR4.equalsIgnoreCase("yes")) {
				gainDRNumber4 = request.getParameter("ability4gainDRNumber");
				gainDRDuration4 = request.getParameter("ability4gainDRDuration");
			}
	
			
			String moreDamagePerHPLostDamage4 = null;
			String moreDamagePerHPLostHP4 = null;
			String damagePerHP4 = request.getParameter("ability4extraDmamagePerSelfHPLost");
			if (damagePerHP4.equalsIgnoreCase("yes")) {
				moreDamagePerHPLostDamage4 = request.getParameter("ability4extraDmamagePerSelfHPLostNumber");
				moreDamagePerHPLostHP4 = request.getParameter("ability4extraDmamagePerSelfHPLostHP");
			}

			
			String moreDamagePerEnemyHPLostDamage4 = null;
			String moreDamagePerEnemyHPLostHP4 = null;
			String damagePerHPEnemy4 = request.getParameter("ability4extraDmamagePerEnemyHPLost");
			if (damagePerHPEnemy4.equalsIgnoreCase("yes")) {
				moreDamagePerEnemyHPLostDamage4 = request.getParameter("ability4extraDmamagePerEnemyHPLostNumber");
				moreDamagePerEnemyHPLostHP4 = request.getParameter("ability4extraDmamagePerEnemyHPLostHP");
			}
			
			String temporaryDamageIncreaseDamage4 = null;
			String temporaryDamageIncreaseDuration4 = null;
			String temporaryDamageIncreaseTarget4 = null;
			String temporaryDamageIncreaseWhich4 = null;
			String damageTemp4 = request.getParameter("ability4extraDamageTemporary");
			if (damageTemp4.equalsIgnoreCase("yes")) {
				temporaryDamageIncreaseDamage4 = request.getParameter("ability4extraDamageTemporaryNumber");
				temporaryDamageIncreaseDuration4 = request.getParameter("ability4extraDamageTemporaryDuration");
				temporaryDamageIncreaseTarget4 = request.getParameter("ability4extraDamageTemporaryTarget");
				temporaryDamageIncreaseWhich4 = request.getParameter("ability4extraDamageTemporaryWhich");
			}

			
			String activeTarget4 = null;
			String activeText4 = null;
			String activeDuration4 = null;
			String active4 = request.getParameter("ability4ActiveSkill");
			if (active4.equalsIgnoreCase("yes")) {
				activeTarget4 = request.getParameter("ability4activeTarget");
				activeText4 = request.getParameter("ability4activeText");
				activeDuration4 = request.getParameter("ability4activeDuration");
			}
			System.out.println(active1);
			System.out.println(active2);

			System.out.println(active3);
			System.out.println(active4);

			
			
			xml.writeXML(""+ability4ID, ability4Name, ability4Description, ability4taijutsu, ability4heart,
					ability4energy, ability4spirit, ability4random, ability4cooldown, targetClick4, 
					damageNumber4, damageDuration4, ignoresInvul4, stunDuration4, becomesInvul4, 
					damageIncreasePerUse4, healIncreasePerUse4, permanentDamageIncrease4, removeNatureNumber4, 
					removeNatureDuration4, gainNatureNumber4, gainNatureDuration4, gainHPNumber4, 
					gainHPDuration4, gainDRNumber4, gainDRDuration4, 
					moreDamagePerHPLostDamage4, moreDamagePerHPLostHP4, moreDamagePerEnemyHPLostDamage4, 
					moreDamagePerEnemyHPLostHP4, temporaryDamageIncreaseDamage4, temporaryDamageIncreaseDuration4,
					temporaryDamageIncreaseTarget4, temporaryDamageIncreaseWhich4,
					activeTarget4, activeText4, activeDuration4);
			
			
			
			//MISSION
			String isMission = request.getParameter("defaultmission"); 
			if (isMission.equalsIgnoreCase("Mission")) {
				String missionName = request.getParameter("missionName");
				String missionDescription = request.getParameter("missionDescription");
				String minLevel = request.getParameter("minLevel");
				Part missionMic = request.getPart("missionImage");
				InputStream inputMission = null;
				if (missionMic!=null && missionMic.getSize()!=0) {
					inputMission = missionMic.getInputStream();
					insert.createMission(missionName, missionDescription, inputMission, Integer.parseInt(minLevel), characterID);
				}
				else {
					sendFailMessage(request, response, "Mission Picture cannot be null");
					return;
				}
				
				String requirementChar1 = request.getParameter("whichCharacter");
				String requirementCharWins1 = request.getParameter("nWinsCharacter");
				boolean isRowChar1 = request.getParameter("rowCharacter").equalsIgnoreCase("yes");
				
				String requirementChar2 = request.getParameter("whichCharacter2");
				String requirementCharWins2 = request.getParameter("nWinsCharacter2");
				boolean isRowChar2 = request.getParameter("rowCharacter2").equalsIgnoreCase("yes");
				
				String requirementAnime = request.getParameter("whichAnime");
				String animeWins = request.getParameter("nWinsAnime");
				boolean isRowAnime = request.getParameter("rowAnime").equalsIgnoreCase("yes");
				
				xml.writeMission(minLevel, missionName,
						requirementChar1, requirementCharWins1, isRowChar1,
						requirementChar2, requirementCharWins2, isRowChar2,
						requirementAnime, animeWins, isRowAnime
						);
				
				
				/*String tipoUser = (String) session.getAttribute("tipoUser");
				if ( tipoUser.equalsIgnoreCase("administrador") ) {
					insert.giveCharsToAdmins();
				}*/
			}
			else { //personagem default
				//System.out.println("default");
				insert.giveCharacterPlayers(characterID);
				
			}
			
			response.sendRedirect("index.jsp");
			
		} catch (MysqlDataTruncation e) {
			sendFailMessage(request, response, "One of the files is too big");
			e.printStackTrace();

		} catch (SQLException e) {
			sendFailMessage(request, response, "Something went wrong, fire your programmer!");
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void sendFailMessage(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		if (message.equals("") || message == null) {
			message = "Error in Form";
		}
		PrintWriter out = response.getWriter();
		request.setAttribute("error", message);

		out.println("<div class='container modal'><h3>" + message + "</h3></div>");

		RequestDispatcher rd = request.getRequestDispatcher("/create.jsp");
		rd.include(request, response);
		return;
	}
	
	

}
