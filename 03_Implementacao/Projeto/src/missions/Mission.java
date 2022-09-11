package missions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import main.Connector;
import users.UserInfo;

public class Mission {
	
	public void resetRow(int userID, int char1, int char2, int char3) {
		Document document = readDocument("D:\\GitHub\\Projeto\\missions"+userID+".xml");
		String fileName = "D:\\GitHub\\Projeto\\missions"+userID+".xml";
		document.getDocumentElement().normalize();
		
		checkMissionsRow(userID, fileName, char1);
		checkMissionsRow(userID, fileName, char2);
		checkMissionsRow(userID, fileName, char3);
		

	}
	
	private void checkMissionsRow(int userID, String filename, int characterID) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 boolean alterou = false;
		 
		 try (InputStream is = new FileInputStream(filename)) {

	            DocumentBuilder db = dbf.newDocumentBuilder();

	            Document doc = db.parse(is);

	            NodeList listOfMissions = doc.getElementsByTagName("mission");	            
	            for (int i = 0; i < listOfMissions.getLength(); i++) {
	            	Node mission = listOfMissions.item(i);
	            	
	            	int minLvl = Integer.parseInt( mission.getAttributes().getNamedItem("minLvl").getTextContent() );
	            	String completed = mission.getAttributes().getNamedItem("completed").getTextContent();
	            	
	            	if (completed.equalsIgnoreCase("false") && hasRequiredLevel(userID, minLvl)) {
	            		NodeList missionInfo = mission.getChildNodes();
	            		for (int j = 0; j < missionInfo.getLength(); j++) {
	            			Node charRequirement = missionInfo.item(j);
	            			
	            			if (charRequirement.getNodeType() == Node.ELEMENT_NODE) {
	            				if (charRequirement.getNodeName().equalsIgnoreCase("character")) {
	            					
	            					String xmlCharID = charRequirement.getAttributes().getNamedItem("characterID").getTextContent();
	            					String row = charRequirement.getAttributes().getNamedItem("row").getTextContent();
	            					
	    	            			if (xmlCharID.equalsIgnoreCase(""+characterID) && row.equalsIgnoreCase("true")) {
	    	            				int current = Integer.parseInt( charRequirement.getAttributes().getNamedItem("current").getTextContent() );	
	    	            				int needed = Integer.parseInt( charRequirement.getAttributes().getNamedItem("need").getTextContent() );
	    	            				
	    	            				if (current < needed) {
	    	            					charRequirement.getAttributes().getNamedItem("current").setTextContent( "0" );
		    	            				alterou = true;
	    	            				}
	    	            				
	    	            			}
	            				}
	            				if (charRequirement.getNodeName().equalsIgnoreCase("anime")) {
	            					String anime = charRequirement.getAttributes().getNamedItem("type").getTextContent();
	            					String row = charRequirement.getAttributes().getNamedItem("row").getTextContent();
	            					
	            					if (charIsFromAnime(anime, characterID) && row.equalsIgnoreCase("true")) {
	            						int current = Integer.parseInt( charRequirement.getAttributes().getNamedItem("current").getTextContent() );	
	    	            				int needed = Integer.parseInt( charRequirement.getAttributes().getNamedItem("need").getTextContent() );
	    	            				
	    	            				if (current < needed) {
	    	            					charRequirement.getAttributes().getNamedItem("current").setTextContent( "0" );
		    	            				alterou = true;
	    	            				}
	            					}
	            				}
	            			}
	            			
	            		}
	            	}   
	            }
	            
	            if (alterou) {
	            	Transformer xformer;
					try {
						xformer = TransformerFactory.newInstance().newTransformer();
						xformer.transform(new DOMSource(doc), new StreamResult((filename)));	
						
					} catch (TransformerConfigurationException e) {
						e.printStackTrace();
					} catch (TransformerFactoryConfigurationError e) {
						e.printStackTrace();
					} catch (TransformerException e) {
						e.printStackTrace();
					}
	            	 
	             }
	            
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

	//id das personagens que utilizou
	public void updateMissions( int userID, int char1, int char2, int char3 ) {
		Document document = readDocument("D:\\GitHub\\Projeto\\missions"+userID+".xml");
		String fileName = "D:\\GitHub\\Projeto\\missions"+userID+".xml";
		document.getDocumentElement().normalize();

		incrementProgressCharacter(fileName, char1, document, userID);
		incrementProgressCharacter(fileName, char2, document, userID);
		incrementProgressCharacter(fileName, char3, document, userID);
		
		document = readDocument("D:\\GitHub\\Projeto\\missions"+userID+".xml");
		ArrayList<String> completed = checkIfMissionComplete(document);
		if (completed.size()>0) {
			updateCompleted(completed, userID, fileName);
		}
		completed.clear();
	}
	
	private void updateCompleted(ArrayList<String> completed, int userID, String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 boolean alterou = false;
		 
		 try (InputStream is = new FileInputStream(filename)) {

	            DocumentBuilder db = dbf.newDocumentBuilder();

	            Document doc = db.parse(is);

	            NodeList listOfMissions = doc.getElementsByTagName("mission");	            
	            for (int i = 0; i < listOfMissions.getLength(); i++) {
	            	Node mission = listOfMissions.item(i);
	            	
	            	String missionID = mission.getAttributes().getNamedItem("id").getTextContent();
	            	boolean isCompleted = mission.getAttributes().getNamedItem("completed").getTextContent().equalsIgnoreCase("true");
	            	for (int j = 0; j < completed.size(); j++) {
	            	//for (String s : completed) {
	            		String s = completed.get(j);
	            		if (missionID.equalsIgnoreCase(s)) {
	            			if (isCompleted) {
	            				completed.set(j, "delete");
	            			}
	            			else {
	            				alterou = true;
		            			mission.getAttributes().getNamedItem("completed").setTextContent( "true" );
	            			}
	            			
	            		}
	            	}	            	
	            	
	            }
	            
	            if (alterou) {
	            	updateMissionsSQL(completed, userID);
	            	Transformer xformer;
					try {
						xformer = TransformerFactory.newInstance().newTransformer();
						xformer.transform(new DOMSource(doc), new StreamResult((filename)));	
			
						
					} catch (TransformerConfigurationException e) {
						e.printStackTrace();
					} catch (TransformerFactoryConfigurationError e) {
						e.printStackTrace();
					} catch (TransformerException e) {
						e.printStackTrace();
					}
	            	 
	             }
	            
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	private void updateMissionsSQL(ArrayList<String> completed, int userID) {
		
		for (String s : completed) {
			if (s!=null && s.length()>0 && !s.equalsIgnoreCase("delete")) {
				try {
					Connection con = Connector.getConnection();
					
					//atualizar estado da missao
					PreparedStatement pstmt =con.prepareStatement("UPDATE USER_MISSION SET completed =? WHERE userID=? and missionID=?"); 
				             pstmt.setBoolean(1, true);
				             pstmt.setInt(2, userID);
				             pstmt.setInt(3, Integer.parseInt(s));
					
				     pstmt.executeUpdate();  
				     
				    //dar ao jogador a personagem que desbloqueou
				    PreparedStatement user = con.prepareStatement("select * from MISSION where missionID="+Integer.parseInt(s));
					ResultSet rs = user.executeQuery();

					if (rs.next()) {
						int characterID = rs.getInt("characterID");
			
						 PreparedStatement give =con.prepareStatement("INSERT into USER_CHARACTER values ( (?),(?) )"); 
						 give.setInt(1, userID);
						 give.setInt(2, characterID);
				
						 give.executeUpdate();  
					}
					
					
					rs.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}
			
		}
		
	}
	
	
	
	private void incrementProgressCharacter(String filename, int characterID, Document input, int userID) {
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 boolean alterou = false;
		 
		 try (InputStream is = new FileInputStream(filename)) {

	            DocumentBuilder db = dbf.newDocumentBuilder();

	            Document doc = db.parse(is);

	            NodeList listOfMissions = doc.getElementsByTagName("mission");	            
	            for (int i = 0; i < listOfMissions.getLength(); i++) {
	            	Node mission = listOfMissions.item(i);
	            	
	            	int minLvl = Integer.parseInt( mission.getAttributes().getNamedItem("minLvl").getTextContent() );
	            	String completed = mission.getAttributes().getNamedItem("completed").getTextContent();
	            	
	            	if (completed.equalsIgnoreCase("false") && hasRequiredLevel(userID, minLvl)) {
	            		NodeList missionInfo = mission.getChildNodes();
	            		
	            		for (int j = 0; j < missionInfo.getLength(); j++) {
	            			Node charRequirement = missionInfo.item(j);
	            			
	            			if (charRequirement.getNodeType() == Node.ELEMENT_NODE) {
	            				if (charRequirement.getNodeName().equalsIgnoreCase("character")) {
	            					String xmlCharID = charRequirement.getAttributes().getNamedItem("characterID").getTextContent();
	    	            			if (xmlCharID.equalsIgnoreCase(""+characterID)) {
	    	            				
	    	            				int newValue = Integer.parseInt( charRequirement.getAttributes().getNamedItem("current").getTextContent() );	
	    	            				int needed = Integer.parseInt( charRequirement.getAttributes().getNamedItem("need").getTextContent() );
	    	            				if (newValue+1 <= needed) {
	    	            					charRequirement.getAttributes().getNamedItem("current").setTextContent( ""+ (newValue+1) );
		    	            				alterou = true;
	    	            				}
	    	            				
	    	            			}
	            				}
	            				if (charRequirement.getNodeName().equalsIgnoreCase("anime")) {
	            					String anime = charRequirement.getAttributes().getNamedItem("type").getTextContent();
	            					if (charIsFromAnime(anime, characterID)) {
	            						int newValue = Integer.parseInt( charRequirement.getAttributes().getNamedItem("current").getTextContent() );	
	    	            				int needed = Integer.parseInt( charRequirement.getAttributes().getNamedItem("need").getTextContent() );
	    	            				if (newValue+1 <= needed) {
	    	            					charRequirement.getAttributes().getNamedItem("current").setTextContent( ""+ (newValue+1) );
		    	            				alterou = true;
	    	            				}
	            					}
	            				}
	            			}
	            			
	            		}
	            	}   
	            }
	            
	            if (alterou) {
	               
	            	Transformer xformer;
					try {
						xformer = TransformerFactory.newInstance().newTransformer();
						xformer.transform(new DOMSource(doc), new StreamResult((filename)));	
						
						
			
						
					} catch (TransformerConfigurationException e) {
						e.printStackTrace();
					} catch (TransformerFactoryConfigurationError e) {
						e.printStackTrace();
					} catch (TransformerException e) {
						e.printStackTrace();
					}
	            	 
	             }
	            
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		 
	}
	
	private boolean hasRequiredLevel(int id, int minLevel) {
		try {
			
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from USERS where userID="+id);
			ResultSet rs = user.executeQuery();
			if (rs.next()) {
				int xp = rs.getInt("xp");
				if (UserInfo.getLevel(xp)>=minLevel) {
					return true;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return false;
	}
	
	/*private String getQuery(Document document) {
		boolean hasChar = hasAnimeCharQuery(document, "character");
		boolean hasAnime = hasAnimeCharQuery(document, "anime");
		
		if (hasChar && hasAnime) {
			return "//mission[character[@current=@need] and anime[@current=@need]]/@id";
		}
		else if (hasChar) {
			return "//mission[character[@current=@need]]/@id";
		}
		else if (hasAnime) {
			return "//mission[anime[@current=@need]]/@id";
		}
		
		System.out.println("missão sem anime nem character");
		return null;
	}*/
		
	/*private boolean hasAnimeCharQuery(Document document, String animeChar) {
		
		document.getDocumentElement().normalize();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String anime = "count(//mission/"+animeChar+")";
		String value = null;
		
		try {
			value = (String) xpath.evaluate(anime, document, XPathConstants.STRING);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		
		if (value==null || value.length()<=0) {
			return false;
		}
		
		return (Integer.parseInt(value)>0) ? true : false;
	}*/
	
	private ArrayList<String> checkIfMissionComplete(Document document) {
		XPath xpath = XPathFactory.newInstance().newXPath();

		//String query = getQuery(document);
		
		ArrayList<String> completed = new ArrayList<String>();
		
		String[] query = {
				"//mission[count(character)=0 and anime[@current=@need]]/@id",
				"//mission[count(anime)=0 and character[@current=@need]]/@id",
				"//mission[character[@current=@need] and anime[@current=@need]]/@id"
			};
		
		for (int q = 0; q < query.length; q++) {
			NodeList result=null;
			try {
				result = (NodeList)xpath.evaluate(query[q], document, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			} 
			
			
			for(int i = 0; i < result.getLength(); i++) {      
			    Node node = result.item(i);
			    String name = node.getNodeValue();
			    if (name!=null && name.length()>0) {
			    	completed.add(name);
			    }
			    
			}
		}
		
		return completed;
	}
	
	private boolean charIsFromAnime(String anime, int characterID) {
		
		//0 - table name
		//1 id name
		String[] table = getTableAnime(anime);
		
		try {
			
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from CHARACTERS INNER JOIN "+table[0]+" where "+characterID+"="+table[1]+";");
			ResultSet rs = user.executeQuery();
			while (rs.next()) {
				int sqlCharacter = rs.getInt("characterID");
				if (sqlCharacter == characterID) {
					return true;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return false;
	}
	
	private String[] getTableAnime(String anime) {
		String[] retorno = new String[2];
		
		if (anime.equalsIgnoreCase("hunterxhunter")) {
			retorno[0] = "HUNTERXHUNTER";
			retorno[1] = "hunterxhunterID";
		}
		else if (anime.equalsIgnoreCase("bleach")) {
			retorno[0] = "BLEACH";
			retorno[1] = "bleachID";
		}
		else if (anime.equalsIgnoreCase("demonslayer")) {
			retorno[0] = "DEMONSLAYER";
			retorno[1] = "demonslayerID";
		}
		else {
			System.out.println("erro tabela de anime");
			
		}
		return retorno;
	}
	
	

	private Document readDocument(String input) {
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      Document doc=null;
	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         doc = builder.parse(input);
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	      return doc;
	}
	
	private Document readDocument(InputStream input) {
		// create a new DocumentBuilderFactory
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      Document doc=null;
	      try {
	         // use the factory to create a documentbuilder
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         doc = builder.parse(input);
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }
	      return doc;
	}
	

}
