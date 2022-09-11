package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DB.InsertIntoBD;
import main.Connector;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Theme")
@MultipartConfig(maxFileSize=65536)
public class Theme extends HttpServlet {
	private static final long serialVersionUID = 7215979604673189309L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Theme() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		int newTheme = createTheme();

		insertChars(newTheme, request);
		insertAbbs(newTheme, request);
		
		response.sendRedirect("index.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void insertAbbs(int newTheme, HttpServletRequest request) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String query = "select * from THEME_ABILITY where themeID=1;";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
					
			while (rs.next()) {
				int abilityID = rs.getInt("abilityID");
				Blob avatar = rs.getBlob("avatar");
				String nome = rs.getString("nome");
				
				String descricao = (String) request.getParameter("abbDesc"+abilityID);
				
				if (descricao!=null)
				insertThemeAbility(newTheme, abilityID, nome, avatar, descricao);
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}
	
	private void insertChars(int newTheme, HttpServletRequest request) {
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String query = "select * from THEME_CHARACTER where themeID=1;";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
					
			while (rs.next()) {
				int characterID = rs.getInt("characterID");
				Blob avatar = rs.getBlob("avatar");
				String nome = rs.getString("nome");
				
				String descricao = (String) request.getParameter("charDesc"+characterID);
				if (descricao!=null)
				insertThemeCharacter(newTheme, characterID, nome, avatar, descricao);
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
	}
	
	private boolean insertThemeCharacter(int themeID, int characterID, String nome, Blob avatar, String descricao) {
		
		String sql = "INSERT INTO THEME_CHARACTER values (?, ?, ?, ?, ?);";
		
		PreparedStatement pstmt;
		try {
			pstmt = Connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, characterID);
			pstmt.setInt(2, themeID);
			pstmt.setString(3, nome);
			pstmt.setBlob(4, avatar);
			pstmt.setString(5, descricao);
			pstmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		return false;
	}
	
	private boolean insertThemeAbility(int themeID, int abilityID, String nome, Blob avatar, String descricao) {
		
		String sql = "INSERT INTO THEME_ABILITY values (?, ?, ?, ?, ?);";
		
		PreparedStatement pstmt;
		try {
			pstmt = Connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, abilityID);
			pstmt.setInt(2, themeID);
			pstmt.setString(3, nome);
			pstmt.setBlob(4, avatar);
			pstmt.setString(5, descricao);
			pstmt.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

		return false;
	}
	
	private int nElements(String table) {
		int n = 0;
		try {
			Class.forName(Connector.drv);
			Connection conn = Connector.getConnection();
			
			String query = "select COUNT(*) from THEME_"+table+" where themeID=1;";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				System.out.println(rs.getInt("COUNT(*)"));
				n = rs.getInt("COUNT(*)");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			for (int i = 0; i < 10; i++)
				System.out.println("Erro a encontrar o JDBC");
			e.printStackTrace();
		}
		
		return n;
		
	}
	

	private int createTheme() {
		
		int id = 0;
		
		String getID = "select * from THEME order by themeID DESC LIMIT 1;";
		PreparedStatement stmt_id;
		try {
			stmt_id = Connector.getConnection().prepareStatement(getID);
			ResultSet rs_id = stmt_id.executeQuery();
			
			if (rs_id.next()) {
				id = Integer.parseInt(rs_id.getString("themeID"))+1;
			}
			
			String sql = "INSERT INTO THEME values (?);";
			
			PreparedStatement pstmt = Connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, id);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
}
