package game;

public class Queue {
	
	private int player;
	private Team team;
	private String searchingFor;
	
	public Queue(int player, Team team) {
		this.player = player;
		this.team = team;
		this.searchingFor = null;
	}
	public Queue(int player, Team team, String searchingFor) {
		this.player = player;
		this.team = team;
		this.searchingFor = searchingFor;
	}


	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	public String getSearchingFor() {
		return searchingFor;
	}
	public void setSearchingFor(String searchingFor) {
		this.searchingFor = searchingFor;
	}

}
