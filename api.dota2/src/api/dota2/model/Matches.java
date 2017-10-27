package api.dota2.model;

/**
 * 
 * Matches-klassenanvänds för att lagra data som hämtats från APIn.
 *
 */
public class Matches {

	private String match_id;

	private String start_time;

	private String dire_team_id;

	private String lobby_type;

	private String radiant_team_id;

	private String match_seq_num;

	public String getMatch_id() {
		return match_id;
	}

	public void setMatch_id(String match_id) {
		this.match_id = match_id;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getDire_team_id() {
		return dire_team_id;
	}

	public void setDire_team_id(String dire_team_id) {
		this.dire_team_id = dire_team_id;
	}

	public String getLobby_type() {
		return lobby_type;
	}

	public void setLobby_type(String lobby_type) {
		this.lobby_type = lobby_type;
	}

	public String getRadiant_team_id() {
		return radiant_team_id;
	}

	public void setRadiant_team_id(String radiant_team_id) {
		this.radiant_team_id = radiant_team_id;
	}

	public String getMatch_seq_num() {
		return match_seq_num;
	}

	public void setMatch_seq_num(String match_seq_num) {
		this.match_seq_num = match_seq_num;
	}

//	@Override
//	public String toString() {
//		return "ClassPojo [players = " + players + ", match_id = " + match_id + ", start_time = " + start_time
//				+ ", dire_team_id = " + dire_team_id + ", lobby_type = " + lobby_type + ", radiant_team_id = "
//				+ radiant_team_id + ", match_seq_num = " + match_seq_num + "]";
//	}
}
