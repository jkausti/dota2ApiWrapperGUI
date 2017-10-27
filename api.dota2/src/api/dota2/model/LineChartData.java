package api.dota2.model;

/**
 * 
 * En klass som håller data som används i LineCharten.
 *
 */
public class LineChartData {

	String matchID;
	int gpm;
	int xpm;
	
	public LineChartData(String matchID, int i, int j) {
		
		this.matchID = matchID;
		this.gpm = i;
		this.xpm = j;

	}

	public String getMatchID() {
		return matchID;
	}

	public int getGpm() {
		return gpm;
	}

	public int getXpm() {
		return xpm;
	}

}
