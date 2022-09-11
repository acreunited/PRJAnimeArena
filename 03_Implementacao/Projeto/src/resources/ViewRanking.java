package resources;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.Connector;
import users.UserInfo;

@WebServlet("/ViewRanking")
public class ViewRanking extends HttpServlet {
	
	private static final long serialVersionUID = 7215979604673189309L;
	
	public ViewRanking() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw = response.getWriter(); 
		HttpSession session = request.getSession();
		
		if (session.getAttribute("userID")!=null) {
			int userID = (int) session.getAttribute("userID");
			int oppID = (int) session.getAttribute("opp_id");
			
			String action = request.getParameter("action");
			if (action.equalsIgnoreCase("hat")) {
				response.setContentType("text/html");
				
				writeRank(pw, userID);
				pw.write("player");
				writeRank(pw, oppID);
				
			}
		}
		
		

	}
	
	private void writeRank(PrintWriter pw, int id) {
		try {
			Connection con = Connector.getConnection();
			PreparedStatement st = con.prepareStatement("select * from USERS where userID=?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				int xp = rs.getInt("xp");
				String rank = getHat(xp);

				pw.write("<img src=\""+rank+"\">");	
				pw.write("player");
				pw.write( getRankName(xp) );
				
			} else {
				System.out.println("user xp not found");
			}
			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private String getRankName(int xp) {
		if (xp >= 42800) {
			return "HASHIRA";
		}
		else if (xp >= 34650) {
			return "ADMIRAL";
		}
		else if (xp >= 27340 ) {
			return "JINCH";
		}
		else if (xp >= 20880 ) {
			return "CIPHER";
		}
		else if (xp >= 15255 ) {
			return "TSUGUKO";
		}
		else if (xp >= 10495 ) {
			return "LIUTENANT";
		}
		else if (xp >= 6570 ) {
			return "MISSING-NIN";
		}
		else if (xp >= 3500 ) {
			return "RYOKA";
		}
		else if (xp >= 1345 ) {
			return "DEMON SLAYER";
		}
		else {
			return "CABIN BOY";
		}
	}
	
	private String getHat(int xp) {
		//battle/hats/default/cipher.png

		if (xp >= 42800) {
			return "battle/hats/default/hashira.png";
		}
		else if (xp >= 34650) {
			return "battle/hats/default/admiral.png";
		}
		else if (xp >= 27340 ) {
			return "battle/hats/default/jinch.png";
		}
		else if (xp >= 20880 ) {
			return "battle/hats/default/cipher.png";
		}
		else if (xp >= 15255 ) {
			return "battle/hats/default/tsuguko.png";
		}
		else if (xp >= 10495 ) {
			return "battle/hats/default/lieutenant.png";
		}
		else if (xp >= 6570 ) {
			return "battle/hats/default/missing-nin.png";
		}
		else if (xp >= 3500 ) {
			return "battle/hats/default/ryoka.png";
		}
		else if (xp >= 1345 ) {
			return "battle/hats/default/DS.png";
		}
		else {
			return "battle/hats/default/cabinboy.png";
		}
		
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}


}
