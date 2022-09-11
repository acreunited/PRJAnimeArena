package users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Connector;

public class UserInfo {
	
	public static int getCurrentTheme(String userID) {
		return getCurrentTheme(Integer.parseInt("userID"));
	}
	
	public static int getPlayerID(String username) {
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from USERS where username=?;");
			user.setString(1, username);
			ResultSet rs = user.executeQuery();
		
			if (rs.next()) {
				return rs.getInt("userID");
			} 
			rs.close();
			user.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static int getCurrentTheme(int userID) {
		
		if (userID>=0) {
			try {
				Connection con = Connector.getConnection();
				
				PreparedStatement user = con.prepareStatement("select * from USERS where userID=?;");
				user.setInt(1, userID);
				ResultSet rs = user.executeQuery();
			
				if (rs.next()) {
					return rs.getInt("currentTheme");
				} 
				rs.close();
				user.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return 1;
	}
	
	public static String getCharAnime(int characterID) {
		
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement bleach = con.prepareStatement("SELECT * FROM BLEACH WHERE bleachID =?;");
			bleach.setInt(1, characterID);
			ResultSet rs_bleach = bleach.executeQuery();
		
			if (rs_bleach.next()) {
				return "bleach";
			} 
			rs_bleach.close();
			bleach.close();
			
			PreparedStatement hunter = con.prepareStatement("SELECT * FROM HUNTERXHUNTER WHERE hunterxhunterID =?;");
			hunter.setInt(1, characterID);
			ResultSet rs_hunter = hunter.executeQuery();
		
			if (rs_hunter.next()) {
				return "hunter";
			} 
			rs_hunter.close();
			hunter.close();
			
			PreparedStatement sao = con.prepareStatement("SELECT * FROM SAO WHERE saoID =?;");
			sao.setInt(1, characterID);
			ResultSet rs_sao = sao.executeQuery();
		
			if (rs_sao.next()) {
				return "SAO";
			} 
			rs_sao.close();
			sao.close();
			
			PreparedStatement ds = con.prepareStatement("SELECT * FROM DEMONSLAYER WHERE demonslayerID =?;");
			ds.setInt(1, characterID);
			ResultSet rs_ds = ds.executeQuery();
		
			if (rs_ds.next()) {
				return "DS";
			} 
			rs_ds.close();
			ds.close();
			
			PreparedStatement onepunch = con.prepareStatement("SELECT * FROM ONEPUNCHMAN WHERE onepunchmanID =?;");
			onepunch.setInt(1, characterID);
			ResultSet rs_onepunch = onepunch.executeQuery();
		
			if (rs_onepunch.next()) {
				return "OnePunchMan";
			} 
			rs_ds.close();
			onepunch.close();
			
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "noAnimeError";
		
	}
	

	public static boolean hasRequiredLevel(int playerID, int minLvl) {
		
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from USERS where userID=?;");
			user.setInt(1, playerID);
			ResultSet rs = user.executeQuery();
		
			if (rs.next()) {
				int xp = rs.getInt("xp");
				if ( getLevel(xp)>= minLvl ) {
					return true;
				}
			} 
			rs.close();
			user.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static boolean isAdmin(int userID) {
		
		
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from ADMINISTRATOR where administratorID=?");
			user.setInt(1, userID);
			ResultSet rs = user.executeQuery();
		
			if (rs.next()) {
				return true;
			} 
			rs.close();
			user.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getCharName(int characterID) {
		
		String name = "";
		
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from THEME_CHARACTER where themeID=1 and characterID=?");
			user.setInt(1, characterID);
			ResultSet rs = user.executeQuery();
		
			if (rs.next()) {
				name = rs.getString("nome");
			} 
			rs.close();
			user.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return name;
		
		
	}
	
	public static String getRankName(int lvl) {

		if (lvl >= 46) {
			return "Hashira";
		}
		else if (lvl >= 41) {
			return "Admiral";
		}
		else if (lvl >= 36 ) {
			return "Jinchuriki";
		}
		else if (lvl >= 31) {
			return "Cipher Pol";
		}
		else if (lvl >= 26 ) {
			return "Tsuguko";
		}
		else if (lvl >= 21 ) {
			return "Lieutenant";
		}
		else if (lvl >= 16 ) {
			return "Missing-nin";
		}
		else if (lvl >= 11 ) {
			return "Ryoka";
		}
		else if (lvl >= 6 ) {
			return "Demon Slayer";
		}
		else {
			return "Cabin Boy";
		}
	}
	
	public static int getLevel(int xp) {

		if (xp>=50000) {
			return 50;
		}
		else if (xp>=48000){
			return 49;
		}
		else if (xp>=46400){
			return 48;
		}
		else if (xp>=44000){
			return 47;
		}
		else if (xp>=42800){
			return 46;
		}
		else if (xp>=41000){
			return 45;
		}
		else if (xp>=39400){
			return 44;
		}
		else if (xp>=37800){
			return 43;
		}
		else if (xp>=36200){
			return 42;
		}
		else if (xp>=34650){
			return 41;
		}
		else if (xp>=33100){
			return 40;
		}
		else if (xp>=31600){
			return 39;
		}
		else if (xp>=30150){
			return 38;
		}
		else if (xp>=28700){
			return 37;
		}
		else if (xp>=27340){
			return 36;
		}
		else if (xp>=25950){
			return 35;
		}
		else if (xp>=24600){
			return 34;
		}
		else if (xp>=23350){
			return 33;
		}
		else if (xp>=22100){
			return 32;
		}
		else if (xp>=20880){
			return 31;
		}
		else if (xp>=19650){
			return 30;
		}
		else if (xp>=18500){
			return 29;
		}
		else if (xp>=17400){
			return 28;
		}
		else if (xp>=16300){
			return 27;
		}
		else if (xp>=15255){
			return 26;
		}
		else if (xp>=14250){
			return 25;
		}
		else if (xp>=13250){
			return 24;
		}
		else if (xp>=12300){
			return 23;
		}
		else if (xp>=11380){
			return 22;
		}
		else if (xp>=10495){
			return 21;
		}
		else if (xp>=9660){
			return 20;
		}
		else if (xp>=8830){
			return 19;
		}
		else if (xp>=8050){
			return 18;
		}
		else if (xp>=7305){
			return 17;
		}
		else if (xp>=6570){
			return 16;
		}
		else if (xp>=5925){
			return 15;
		}
		else if (xp>=5250){
			return 14;
		}
		else if (xp>=4655){
			return 13;
		}
		else if (xp>=4125){
			return 12;
		}
		else if (xp>=3500){
			return 11;
		}
		else if (xp>=3025){
			return 10;
		}
		else if (xp>=2540){
			return 9;
		}
		else if (xp>=2120){
			return 8;
		}
		else if (xp>=1730){
			return 7;
		}
		else if (xp>=1345){
			return 6;
		}
		else if (xp>=1005){
			return 5;
		}
		else if (xp>=705){
			return 4;
		}
		else if (xp>=450){
			return 3;
		}
		else if (xp>=200){
			return 2;
		}
		else {
			return 1;
		}
	}
	public static String getLevel(String xp) {
		return ""+ getLevel( Integer.parseInt(xp) );

	}
	
	public static float xpNeededNextLvl(int currentXP) {
		
		int currentLvl = getLevel(currentXP);
		int xpNeeded = getXPeachLvl(currentLvl+1) - getXPeachLvl(currentLvl);
		
		int sub = currentXP - getXPeachLvl(currentLvl);
		
		return (float) ((1.0*sub*190)/xpNeeded);
	}
	
	private static int getXPeachLvl(int lvl) {
		switch (lvl) {
		case 1: return 0;
		case 2: return 200;
		case 3: return 450;
		case 4: return 705;
		case 5: return 1005;
		case 6: return 1345;
		case 7: return 1730;
		case 8: return 2120;
		case 9: return 2540;
		case 10: return 3025;
		case 11: return 3500;
		case 12: return 4125;
		case 13: return 4655;
		case 14: return 5250;
		case 15: return 5925;
		case 16: return 6570;
		case 17: return 7305;
		case 18: return 8050;
		case 19: return 8830;
		case 20: return 9660;
		case 21: return 10495;
		case 22: return 11380;
		case 23: return 12300;
		case 24: return 13250;
		case 25: return 14250;
		case 26: return 15255;
		case 27: return 16300;
		case 28: return 17400;
		case 29: return 18500;
		case 30: return 19650;
		case 31: return 20880;
		case 32: return 22100;
		case 33: return 23350;
		case 34: return 24600;
		case 35: return 25950;
		case 36: return 27340;
		case 37: return 28700;
		case 38: return 30150;
		case 39: return 31600;
		case 40: return 33100;
		case 41: return 34650;
		case 42: return 36200;
		case 43: return 37800;
		case 44: return 39400;
		case 45: return 41000;
		case 46: return 42800;
		case 47: return 44000;
		case 48: return 46400;
		case 49: return 48000;
		case 50: return 50000;
		}
		
		return 0;
	}
	
	public static String getPlayerUsername(int id) {
		
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from USERS where userID=?");
			user.setInt(1, id);
			ResultSet rs = user.executeQuery();
		
			if (rs.next()) {
				String username = rs.getString("username");
				return username;
			} 
			rs.close();
			user.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int getLadderRank(int id) {
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement rank = con.prepareStatement("select * from USERS order by xp DESC;");
			ResultSet rs_rank = rank.executeQuery();
			int count = 0;
			while (rs_rank.next()) {
				count++;
				if (rs_rank.getInt("userID")==id) {
					return count;
				}
			}

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public static String getWinPercentage(String w, String l) {
		
		int wins = Integer.parseInt(w);
		int losses = Integer.parseInt(l);
		
		if (wins==0) {
			return "0";
		}
		
		float result = (wins*100)/(wins+losses);
		
		return ""+result;
	}
	
	public static boolean missionIsComplete(int userID, int characterID) {
		
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement rank = con.prepareStatement("select * from USER_CHARACTER where userID="+userID);
			ResultSet rs_rank = rank.executeQuery();
			
			while (rs_rank.next()) {
				
				if (rs_rank.getInt("characterID")==characterID) {
					return true;
				}
			}

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
