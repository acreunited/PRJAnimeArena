package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ManipulateXML {
	
	private String zeroIfNull(String s) {
		return (s==null) ? "0" : s;
	}
	
//	temporaryDamageIncreaseTarget1, temporaryDamageIncreaseWhich1,
//	activeTarget1,activeText1,activeDuration1
	public boolean writeXML(String abilityID, String name, String description, String taijutsu, String heart, String energy, 
			String spirit, String random, String cooldown, String targetClick, String damageNumber, String damageDuration,
			String ignoresInvul, String stunDuration, String becomesInvul, String damageIncreasePerUse, String healIncreasePerUse,
			String permanentDamageIncrease, String removeNatureNumber, String removeNatureDuration,
			String gainNatureNumber, String gainNatureDuration, String gainHPNumber, String gainHPDuration, String gainDRNumber, 
			String gainDRDuration, String moreDamagePerHPLostDamage, 
			String moreDamagePerHPLostHP, String moreDamagePerEnemyHPLostDamage, String moreDamagePerEnemyHPLostHP, 
			String temporaryDamageIncreaseDamage, String temporaryDamageIncreaseDuration, String temporaryDamageIncreaseTarget, 
			String temporaryDamageIncreaseWhich, String activeTarget, String activeText, String activeDuration) {

		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			//document = builder.parse(this.getClass().getResourceAsStream("/resources/skeleton.xml"));

			Document document = documentBuilder.parse("D:\\GitHub\\Projeto\\abilities.xml");
			DOMSource source = new DOMSource(document);
			
			Element root = document.getDocumentElement();
			
			Element newAbility = document.createElement("ability");
			((Element) newAbility).setAttribute("abilityID", abilityID);
			
			Element abilityName = document.createElement("name");
			abilityName.appendChild(document.createTextNode(name));
			newAbility.appendChild(abilityName);
			
			Element descricao = document.createElement("description");
			descricao.appendChild(document.createTextNode(description));
			newAbility.appendChild(descricao);
			
			Element nature = document.createElement("nature");
			Element tai = document.createElement("taijutsu");
			tai.appendChild(document.createTextNode(taijutsu));
			nature.appendChild(tai);
			Element hear = document.createElement("heart");
			hear.appendChild(document.createTextNode(heart));
			nature.appendChild(hear);
			Element ener = document.createElement("energy");
			ener.appendChild(document.createTextNode(energy));
			nature.appendChild(ener);
			Element spir = document.createElement("spirit");
			spir.appendChild(document.createTextNode(spirit));
			nature.appendChild(spir);
			Element ran = document.createElement("random");
			ran.appendChild(document.createTextNode(random));
			nature.appendChild(ran);
			newAbility.appendChild(nature);
			
			Element cool = document.createElement("cooldown");
			cool.appendChild(document.createTextNode(cooldown));
			newAbility.appendChild(cool);
			
			Element clickT = document.createElement("targetClick");
			clickT.appendChild(document.createTextNode(targetClick));
			newAbility.appendChild(clickT);
			
			if (damageNumber!=null && damageDuration!=null) {
				Element dmg = document.createElement("damage");
				Element dmgNumber = document.createElement("number");
				dmgNumber.appendChild(document.createTextNode(damageNumber));
				dmg.appendChild(dmgNumber);
				Element dmgDuration = document.createElement("duration");
				dmgDuration.appendChild(document.createTextNode(damageDuration));
				dmg.appendChild(dmgDuration);
				newAbility.appendChild(dmg);
			}
			
			
			if (ignoresInvul!=null) {
				Element ignoresInv = document.createElement("ignoresInvulnerability");
				ignoresInv.appendChild(document.createTextNode(ignoresInvul));
				newAbility.appendChild(ignoresInv);
			}
			
			if (stunDuration!=null) {
				Element stun = document.createElement("stunDuration");
				stun.appendChild(document.createTextNode(stunDuration));
				newAbility.appendChild(stun);
			}
			
			
			if (becomesInvul!=null) {
				Element invul = document.createElement("becomeInvulnerable");
				invul.appendChild(document.createTextNode(becomesInvul));
				newAbility.appendChild(invul);
			}
			
			if (damageIncreasePerUse!=null) {
				Element damageIncreaseUse = document.createElement("damageIncreasePerUse");
				damageIncreaseUse.appendChild(document.createTextNode(damageIncreasePerUse));
				newAbility.appendChild(damageIncreaseUse);
			}
			
			if (healIncreasePerUse!=null) {
				Element healIncreaseUse = document.createElement("healIncreasePerUse");
				healIncreaseUse.appendChild(document.createTextNode(healIncreasePerUse));
				newAbility.appendChild(healIncreaseUse);
			}
			
			if (permanentDamageIncrease!=null) {
				Element permDamageInc = document.createElement("permanentDamageIncrease");
				permDamageInc.appendChild(document.createTextNode(permanentDamageIncrease));
				newAbility.appendChild(permDamageInc);
			}
			
			if (removeNatureNumber!=null && removeNatureDuration!=null) {
				Element removeEner = document.createElement("removeNature");
				Element removeEnerNumber = document.createElement("number");
				removeEnerNumber.appendChild(document.createTextNode(removeNatureNumber));
				removeEner.appendChild(removeEnerNumber);
				Element removeEnerDuration = document.createElement("duration");
				removeEnerDuration.appendChild(document.createTextNode(removeNatureDuration));
				removeEner.appendChild(removeEnerDuration);
				newAbility.appendChild(removeEner);
			}
			
		
			if (gainNatureNumber!=null && gainNatureDuration!=null) {
				Element gainEner = document.createElement("gainNature");
				Element gainEnerNumber = document.createElement("number");
				gainEnerNumber.appendChild(document.createTextNode(gainNatureNumber));
				gainEner.appendChild(gainEnerNumber);
				Element gainEnerDuration = document.createElement("duration");
				gainEnerDuration.appendChild(document.createTextNode(gainNatureDuration));
				gainEner.appendChild(gainEnerDuration);
				newAbility.appendChild(gainEner);
			}
			
			if (gainDRNumber!=null && gainDRDuration!=null) {
				Element dr = document.createElement("gainDR");
				Element drNumber = document.createElement("number");
				drNumber.appendChild(document.createTextNode(gainDRNumber));
				dr.appendChild(drNumber);
				Element drDuration = document.createElement("duration");
				drDuration.appendChild(document.createTextNode(gainDRDuration));
				dr.appendChild(drDuration);
				newAbility.appendChild(dr);
			}
			
			if (moreDamagePerHPLostDamage !=null && moreDamagePerHPLostHP!=null) {
				Element moreDmgHP = document.createElement("moreDamagePerHPLost");
				Element extraDmg = document.createElement("extraDamage");
				extraDmg.appendChild(document.createTextNode(moreDamagePerHPLostDamage));
				moreDmgHP.appendChild(extraDmg);
				Element extraDmgHP = document.createElement("hpLost");
				extraDmgHP.appendChild(document.createTextNode(moreDamagePerHPLostHP));
				moreDmgHP.appendChild(extraDmgHP);
				newAbility.appendChild(moreDmgHP);
			}
			
		
			if (gainHPNumber!=null && gainHPDuration!=null) {
				Element gainHP = document.createElement("gainHP");
				Element hpNumber = document.createElement("number");
				hpNumber.appendChild(document.createTextNode(gainHPNumber));
				gainHP.appendChild(hpNumber);
				Element hpDuration = document.createElement("duration");
				hpDuration.appendChild(document.createTextNode(gainHPDuration));
				gainHP.appendChild(hpDuration);
				newAbility.appendChild(gainHP);
			}
			
			if (moreDamagePerEnemyHPLostDamage!=null && moreDamagePerEnemyHPLostHP!=null) {
				Element moreDmgHPenemy = document.createElement("moreDamageEnemyHPLost");
				Element extraDmgenemy = document.createElement("extraDamage");
				extraDmgenemy.appendChild(document.createTextNode(moreDamagePerEnemyHPLostDamage));
				moreDmgHPenemy.appendChild(extraDmgenemy);
				Element extraDmgHPenemy = document.createElement("hpLost");
				extraDmgHPenemy.appendChild(document.createTextNode(moreDamagePerEnemyHPLostHP));
				moreDmgHPenemy.appendChild(extraDmgHPenemy);
				newAbility.appendChild(moreDmgHPenemy);
			}

			
			if (temporaryDamageIncreaseDamage!=null && temporaryDamageIncreaseDuration!=null &&
					temporaryDamageIncreaseTarget!=null && temporaryDamageIncreaseWhich!=null) {
				
				Element temporaryDamage = document.createElement("temporaryDamageIncrease");
				Element tempDamage = document.createElement("extraDamage");
				tempDamage.appendChild(document.createTextNode(temporaryDamageIncreaseDamage));
				temporaryDamage.appendChild(tempDamage);
				Element tempDuration = document.createElement("duration");
				tempDuration.appendChild(document.createTextNode(temporaryDamageIncreaseDuration));
				temporaryDamage.appendChild(tempDuration);
				
				Element tempTarget = document.createElement("target");
				tempTarget.appendChild(document.createTextNode(temporaryDamageIncreaseTarget));
				temporaryDamage.appendChild(tempTarget);
				
				Element tempWhich = document.createElement("whichAbilities");
				tempWhich.appendChild(document.createTextNode(temporaryDamageIncreaseWhich));
				temporaryDamage.appendChild(tempWhich);
				
				newAbility.appendChild(temporaryDamage);
			}

			if (activeTarget!=null && activeText!=null && activeDuration!=null) {
				Element active = document.createElement("active");
				
				Element elementActiveTarget = document.createElement("target");
				elementActiveTarget.appendChild(document.createTextNode(activeTarget));
				active.appendChild(elementActiveTarget);
				
				Element elementActiveText= document.createElement("text");
				elementActiveText.appendChild(document.createTextNode(activeText));
				active.appendChild(elementActiveText);
				
				Element elementActiveDuration = document.createElement("duration");
				elementActiveDuration.appendChild(document.createTextNode(activeDuration));
				active.appendChild(elementActiveDuration);
				
				newAbility.appendChild(active);
			}
			
			
			root.appendChild(newAbility);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult("D:\\GitHub\\Projeto\\abilities.xml");
			transformer.transform(source, result);

			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public void writeMission(String minLevel, String name,
			String requirementChar1, String requirementCharWins1, boolean isRowChar1,
			String requirementChar2, String requirementCharWins2, boolean isRowChar2,
			String requirementAnime, String animeWins, boolean isRowAnime
			) {
		
		
		
		PreparedStatement stmt = null;
		try {
			Class.forName(Connector.drv);

			Connection conn = Connector.getConnection();

			String query = String.format("select * FROM USERS"); 
			
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int userID = rs.getInt("userID");
				
				try {
					DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
					//document = builder.parse(this.getClass().getResourceAsStream("/resources/skeleton.xml"));

					Document document = documentBuilder.parse("D:\\GitHub\\Projeto\\missions"+userID+".xml");
					String updateEnd = "D:\\GitHub\\Projeto\\missions"+userID+".xml";

					writeMission(document, minLevel, name, updateEnd,
							requirementChar1, requirementCharWins1, isRowChar1,
							requirementChar2, requirementCharWins2, isRowChar2,
							requirementAnime, animeWins, isRowAnime);
					
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			conn.close();
	
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		//general xml for when a player creates account
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			//document = builder.parse(this.getClass().getResourceAsStream("/resources/skeleton.xml"));

			Document document = documentBuilder.parse("D:\\GitHub\\Projeto\\missions.xml");
			String updateEnd = "D:\\GitHub\\Projeto\\missions.xml";

			writeMission(document, minLevel, name, updateEnd,
					requirementChar1, requirementCharWins1, isRowChar1,
					requirementChar2, requirementCharWins2, isRowChar2,
					requirementAnime, animeWins, isRowAnime);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void writeMission(Document document, String minLevel, String name, String updateEnd,
			String requirementChar1, String requirementCharWins1, boolean isRowChar1,
			String requirementChar2, String requirementCharWins2, boolean isRowChar2,
			String requirementAnime, String animeWins, boolean isRowAnime) {
		
		try {
			DOMSource source = new DOMSource(document);
			
			Element root = document.getDocumentElement();
			
			Element newMission = document.createElement("mission");
			((Element) newMission).setAttribute("id", ""+getLastMissionID() );
			((Element) newMission).setAttribute("minLvl", minLevel );
			((Element) newMission).setAttribute("completed", "false" );
			
			Element missionName = document.createElement("name");
			missionName.appendChild(document.createTextNode(name));
			newMission.appendChild(missionName);
			
			if (!requirementChar1.equalsIgnoreCase("none")) {
				Element reqChar1 = document.createElement("character");
				((Element) reqChar1).setAttribute("characterID", requirementChar1);
				((Element) reqChar1).setAttribute("current", "0");
				((Element) reqChar1).setAttribute("need", requirementCharWins1);
				((Element) reqChar1).setAttribute("row", ((isRowChar1) ? "true" : "false") );
				newMission.appendChild(reqChar1);
			}
			if (!requirementChar2.equalsIgnoreCase("none")) {
				Element reqChar2 = document.createElement("character");
				((Element) reqChar2).setAttribute("characterID", requirementChar2);
				((Element) reqChar2).setAttribute("current", "0");
				((Element) reqChar2).setAttribute("need", requirementCharWins2);
				((Element) reqChar2).setAttribute("row", ((isRowChar2) ? "true" : "false")  );
				newMission.appendChild(reqChar2);
			}
			//String requirementAnime, String animeWins, boolean isRowAnime
			if (!requirementAnime.equalsIgnoreCase("none")) {
				Element reqAnime = document.createElement("anime");
				((Element) reqAnime).setAttribute("type", requirementAnime);
				((Element) reqAnime).setAttribute("current", "0");
				((Element) reqAnime).setAttribute("need", animeWins);
				((Element) reqAnime).setAttribute("row", ((isRowAnime) ? "true" : "false")  );
				newMission.appendChild(reqAnime);
			}
			
			root.appendChild(newMission);
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult result = new StreamResult( updateEnd );
			transformer.transform(source, result);
		
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getLastMissionID() {
		
		PreparedStatement stmt = null;
		int id = -1;
		
		try {
			Class.forName(Connector.drv);

			Connection conn = Connector.getConnection();

			String query = String.format("select * from MISSION order by missionID DESC LIMIT 1;"); 
			
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			
			if (rs.next()) {
				id = Integer.parseInt(rs.getString("missionID"))+1;
			}
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (id<0) {
			System.out.println("erro id mission ManipulateXML");
		}
		return id;
	}

}
