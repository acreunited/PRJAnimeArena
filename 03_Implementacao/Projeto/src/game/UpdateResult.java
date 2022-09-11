package game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Connector;
import users.UserInfo;

public class UpdateResult {
	

	
	public void update(int id, boolean isWinner) {
		
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement user = con.prepareStatement("select * from USERS where userID="+id);
			ResultSet rs = user.executeQuery();

			if (rs.next()) {
				int xp = rs.getInt("xp");
				int wins = rs.getInt("nWins");
				int losses = rs.getInt("nLosses");
				int streak = rs.getInt("streak");
				int hStreak = rs.getInt("highestStreak");
				int hLvl = rs.getInt("highestLevel");
				
				if (isWinner) {
					updateXP(id, xp+600);
					//win(id, streak, hStreak);
					updateWins(id, wins+1);
					updateStreak(id, ((streak>0) ? streak+1 : 1), hStreak);
					updateHlvl(id, xp, hLvl);
				}
				else {
					updateXP(id, xp-150);
					//loss(id, streak, hStreak);
					updateLosses(id, losses+1);
					updateStreak(id, ((streak<0) ? streak-1 : -1), hStreak);
				}
				//battleResult(id, isWinner, wins, losses);
				

			} else {
				System.out.println("Player "+ id + " Couldnt Update");
			}
			rs.close();
			user.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void updateHlvl(int id, int xp, int hLvl) {
		int currentLevel = UserInfo.getLevel(xp);
		if (currentLevel > hLvl) {
			try {
				Connection con = Connector.getConnection();
				
				PreparedStatement pstmt =con.prepareStatement(  "UPDATE USERS SET highestLevel =? WHERE userID=?"); 
			             pstmt.setInt(1, currentLevel);
			             pstmt.setInt(2, id);
				
			     pstmt.executeUpdate();  

				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
	}
	
	private void updateXP(int id, int xp) {
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement pstmt =con.prepareStatement(  "UPDATE USERS SET xp =? WHERE userID=?"); 
		             pstmt.setInt(1, ((xp>=0) ? xp : 0));
		             pstmt.setInt(2, id);
			
		     pstmt.executeUpdate();  

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void updateStreak(int id, int streak, int hStreak) {
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement pstmt =con.prepareStatement(  "UPDATE USERS SET streak =? WHERE userID=?"); 
		             //pstmt.setInt(1, ((streak>0) ? streak : 0) );
			pstmt.setInt(1, streak );
		             pstmt.setInt(2, id);
			
		     pstmt.executeUpdate();  

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		if (streak > hStreak) {
			updateHighestStreak(id, streak);
		}
	}
	
	private void updateHighestStreak(int id, int newStreak) {
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement pstmt =con.prepareStatement(  "UPDATE USERS SET highestStreak =? WHERE userID=?"); 
		             pstmt.setInt(1, newStreak);
		             pstmt.setInt(2, id);
			
		     pstmt.executeUpdate();  

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void updateWins(int id, int nWins) {
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement pstmt =con.prepareStatement(  "UPDATE USERS SET nWins =? WHERE userID=?"); 
		             pstmt.setInt(1, nWins);
		             pstmt.setInt(2, id);
			
		     pstmt.executeUpdate();  

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void updateLosses(int id, int nLosses) {
		try {
			Connection con = Connector.getConnection();
			
			PreparedStatement pstmt =con.prepareStatement(  "UPDATE USERS SET nLosses =? WHERE userID=?"); 
		             pstmt.setInt(1, ( (nLosses>0) ? nLosses : 0) );
		             pstmt.setInt(2, id);
			
		     pstmt.executeUpdate();  

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}


}
