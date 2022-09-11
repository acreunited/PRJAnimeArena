package game;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import legacy.MatchmakingLegacy;
import main.Connector;
import users.UserInfo;

@WebServlet("/FindOpponent")
public class FindOpponent extends HttpServlet {
	
	private static final long serialVersionUID = 7215979604673189309L;

	
	public FindOpponent() {
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		session.setAttribute("cancel", false);
		
		PrintWriter pw = response.getWriter(); 
		
		String metodo = request.getParameter("metodo");
		
		if (session.getAttribute("userID")!=null) {
			int id = (int) session.getAttribute("userID");
			
			String char1 = (String) request.getParameter("char1");
			String char2 = (String) request.getParameter("char2");
			String char3 = (String) request.getParameter("char3");
			String battleType = (String) request.getParameter("battleType");
			String privateOpp = null;
			
			Semaphore sem = null;
			List<Queue> matches = null;
			Hashtable<Queue,  Queue> found = null;
			
			if (metodo.equalsIgnoreCase("cancel")) {
				session.setAttribute("cancel", true);
			}
			else {
				if (battleType.equalsIgnoreCase("quick")) {
					sem = GameUtils.semQuick;
					matches = GameUtils.matchQuick;
					found = GameUtils.matchQuickFound;
				}
				else if (battleType.equalsIgnoreCase("ladder")) {
					sem = GameUtils.semLadder;
					matches = GameUtils.matchLadder;
					found = GameUtils.matchLadderFound;
				}
				else if (battleType.equalsIgnoreCase("private")) {
					sem = GameUtils.semPrivate;
					matches = GameUtils.matchPrivate;
					found = GameUtils.matchPrivateFound;
					privateOpp = (String) request.getParameter("usernameOpp");
				}
				
				//métodos
				if (metodo.equalsIgnoreCase("enterQueue")) {
					enterQueue(id, char1, char2, char3, sem, matches, privateOpp);
				}
				else if (metodo.equalsIgnoreCase("searchingOpp")) {
					//GameUtils.matchQuickFound.put(thisQueue, q);
					searchingOpp(matches, sem, id, found, privateOpp, pw, session);
				}		
			}
		}
		

		
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}

	private void searchingOpp(
			List<Queue> matches, Semaphore sem, int id, Hashtable<Queue, Queue> found, 
			String privateOpp, PrintWriter pw, HttpSession session) {
		
		String state = "START";
		boolean search = true;
		
		while(search) {
			switch (state) {
			
			case "END":
				pw.write("found");
				search = false;
				break;
				
			case "CANCEL":
				matches.remove( getThisQueue(id, matches) );
				
				pw.write("cancel");
				search = false;
				break;
			
			case "START":
				if ((boolean) session.getAttribute("cancel")) {
					state = "CANCEL";
				}
				else {
					state = (matches.size()!=1) ? "PAIRING" : "START";
				}
				
				break;
			
			case "PAIRING":
				try {
					sem.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if ((boolean) session.getAttribute("cancel")) {
					state = "CANCEL";
				}
				else {
					Queue thisQueue = getThisQueue(id, matches);
					
					boolean privateMatch = false;
					
					//se for null é porque já tiraram = já tem oponente definido
					if (thisQueue!=null) {
						
						for (Queue q : matches) {
							
							//private game
							if (privateOpp!=null) {
							
								if (UserInfo.getPlayerUsername(q.getPlayer()).equalsIgnoreCase(privateOpp) && 
									UserInfo.getPlayerUsername(id).equalsIgnoreCase(q.getSearchingFor())
									) {
									found.put(thisQueue, q);
									matches.remove(thisQueue);
									matches.remove(q);
									privateMatch = true;
									break;
								}
							}
							//quick or ladder
							else if (q.getPlayer()!=id) {
								//GameUtils.matchQuickFound.put(thisQueue, q);
								found.put(thisQueue, q);
								matches.remove(thisQueue);
								matches.remove(q);
								break;
							}
						}
						state = (privateMatch) ? "END" : "START";
						
					}
					else {
						state = "END";
					}
				}
				
				
				sem.release();
				break;
			}
		}
	}
	
	private void enterQueue(int id, String char1, String char2, String char3, Semaphore sem, List<Queue> matches, String privateOpp) {
		Queue queue = (privateOpp==null) ? 
				new Queue(id, new Team(char1, char2, char3)) : 
				new Queue(id, new Team(char1, char2, char3), privateOpp);
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		matches.add(queue);
		sem.release();
		
	}
	
	private Queue getThisQueue(int id, List<Queue> matches) {
		
		for (Queue q : matches) {
			if (q.getPlayer()==id) {
				return q;
			}
		}

		return null;
	}
	



	
	
	
}
