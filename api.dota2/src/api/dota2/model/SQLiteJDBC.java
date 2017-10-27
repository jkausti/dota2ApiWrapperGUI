package api.dota2.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * 
 *
 *
 * ### SQLiteJDBC-klassen ###
 * 
 * SQLiteJDBC innehåller metoder för att skapa en SQLite databas (om en sådan
 * redan inte finns) med tabeller för matcher, spelare, hjältar och
 * användarnamn. Metoderna som börjar med insert.. och store... körs då en
 * hämtning initieras i programmets grafiska gränssnitt. Generate-metoderna är
 * hjälpmetoder som modifierar den data som erhållits i json-format. Övriga
 * metoder gör förfrågningar mot databasen för att hämta information för olika
 * funktioner i programmet.
 * 
 * 
 *
 */

public class SQLiteJDBC {

	private static Connection c = null;
	private static java.sql.Statement stmt = null;

	public SQLiteJDBC() {

	}

	/**
	 * Skapar databasen ifall den inte existerar från tidigare och initierar
	 * schemat.
	 * 
	 * @return En sträng som visas i användargränssnittets konsolruta.
	 */
	public String createDbTables() {
		String sql = null;

		// Creates database if it doesn't exist
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		System.out.println("Connected to database successfully");

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");

			stmt = c.createStatement();
			sql = "CREATE TABLE IF NOT EXISTS MATCHES " + "(MATCH_ID TEXT PRIMARY KEY," + " START_TIME           INT, "
					+ " DIRE_TEAM_ID            INT, " + " LOBBY_TYPE        STRING, "
					+ " RADIANT_TEAM_ID         INT, " + " MATCH_SEQ_NUM         INT)";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS HEROES " + "(HERO_ID TEXT PRIMARY KEY," + " HERO_NAME TEXT)";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS USERNAMES " + "(ACCOUNT_ID TEXT PRIMARY KEY NOT NULL, "
					+ "USER_NAME TEXT)";
			stmt.executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS PLAYERS " + "(ACCOUNT_ID 	TEXT 	REFERENCES USERNAMES(ACCOUNT_ID),"
					+ "PLAYER_TEAM           TEXT, " + "WIN			INT, " + "KILLS        INT, "
					+ "DEATHS         INT, " + "ASSISTS         INT, " + "GOLD_PER_MINUTE         INT, "
					+ "XP_PER_MINUTE         INT, " + "HERO_LEVEL         INT, " + "LAST_HITS         INT, "
					+ "DENIES         INT, " + "TOWER_DAMAGE         INT, "
					+ "HERO_ID            TEXT REFERENCES HEROES(HERO_ID), "
					+ "MATCH_ID	     INT REFERENCES MATCHES(MATCH_ID), " + "PRIMARY KEY(ACCOUNT_ID, MATCH_ID))";
			stmt.executeUpdate(sql);

			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return "\nThe tables MATCHES, HEROES, PLAYERS\nand USERNAMES were created successfully. \n";

	}

	/**
	 * Kollar ifall databasen existerar
	 * 
	 * @return En boolean returneras som används för att aktivera/deaktivera
	 *         programmets tabbar
	 */
	public boolean checkDBExists() {

		File file = new File("dota2.db");
		if (file.exists()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * En metod som sätter in rader i HEROES-tabellen ifall de inte existerar
	 * från tidigare.
	 * 
	 * @param heroList
	 *            en lista på heroes som hämtats från APIn.
	 * @return En sträng som visas användargränssnittets konsolruta.
	 */
	public String insertHeroData(ArrayList<Heroes> heroList) {
		String sql = "INSERT OR IGNORE INTO HEROES (HERO_ID,HERO_NAME) VALUES ";
		int i = 0;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			stmt = c.createStatement();

			while (i < heroList.size()) {
				String id = heroList.get(i).getId();
				String name = heroList.get(i).getLocalizedName();
				name = name.replaceAll("'", "''");

				sql += "(" + id + ", ";
				sql += "'" + name + "'";
				sql += "), ";
				i++;
			}
			sql = sql.substring(0, sql.length() - 2);
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
			System.out.println("Hero data stored");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return "Hero data stored succesfully. \n";
	}

	/**
	 * Sätter in match-data i databasen i tabellen MATCHES.
	 * 
	 * @param matchList
	 *            - En lista på match-objekt som hämtats från APIn.
	 * @return En sträng som visas i användargränssnittets konsolruta.
	 */
	public String insertMatchData(ArrayList<Matches> matchList) {
		String sql = "INSERT OR IGNORE INTO MATCHES (MATCH_ID,START_TIME,DIRE_TEAM_ID,LOBBY_TYPE,RADIANT_TEAM_ID,MATCH_SEQ_NUM) VALUES ";
		int i = 0;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			stmt = c.createStatement();

			while (i < matchList.size()) {
				sql += "(" + matchList.get(i).getMatch_id() + ", ";
				sql += matchList.get(i).getStart_time() + ", ";
				sql += matchList.get(i).getDire_team_id() + ", ";
				sql += matchList.get(i).getLobby_type() + ", ";
				sql += matchList.get(i).getRadiant_team_id() + ", ";
				sql += matchList.get(i).getMatch_seq_num();
				sql += "), ";

				i++;
			}
			sql = sql.substring(0, sql.length() - 2);
			stmt.executeUpdate(sql);
			System.out.println("Match data stored");
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return "Match data stored succesfully. \n";
	}

	/**
	 * Sätter in spelar-data i databasen i tabellen PLAYERS.
	 * 
	 * @param playerList
	 *            en lista med Players-objekt som innehåller data hämtade från
	 *            APIn.
	 * @return En sträng som visas i användargränssnittets konsolruta.
	 */
	public String insertPlayerData(ArrayList<Players> playerList) {
		String sql = "INSERT OR IGNORE INTO PLAYERS (ACCOUNT_ID,PLAYER_TEAM,WIN,HERO_ID,KILLS,DEATHS,ASSISTS,GOLD_PER_MINUTE,XP_PER_MINUTE,HERO_LEVEL,LAST_HITS,DENIES,MATCH_ID) "
				+ "VALUES ";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			stmt = c.createStatement();

			for (int i = 0; i < playerList.size(); i++) {
				sql += "(" + playerList.get(i).getAccount_id() + ", ";
				sql += "'" + generateTeam(playerList.get(i).getPlayer_slot()) + "', ";
				sql += Integer.toString(
						generateWin(playerList.get(i).isWin(), generateTeam(playerList.get(i).getPlayer_slot())))
						+ ", ";
				sql += playerList.get(i).getHero_id() + ", ";
				sql += playerList.get(i).getKills() + ", ";
				sql += playerList.get(i).getDeaths() + ", ";
				sql += playerList.get(i).getAssists() + ", ";
				sql += playerList.get(i).getGold_per_min() + ", ";
				sql += playerList.get(i).getXp_per_min() + ", ";
				sql += playerList.get(i).getLevel() + ", ";
				sql += playerList.get(i).getLast_hits() + ", ";
				sql += playerList.get(i).getDenies() + ", ";
				sql += playerList.get(i).getMatch_id();
				sql += "), ";

			}
			sql = sql.substring(0, sql.length() - 2);
			stmt.executeUpdate(sql);

			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		sql = "INSERT OR IGNORE INTO " + " USERNAMES (ACCOUNT_ID) VALUES ";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			stmt = c.createStatement();

			for (int j = 0; j < playerList.size(); j++) {
				sql += "(" + playerList.get(j).getAccount_id();
				sql += "), ";
			}
			sql = sql.substring(0, sql.length() - 2);
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return "Player data stored succesfully. \n";
	}

	/**
	 * En hjälpmetod som avgör, på basis av data hämtat från APIn, på vilket lag
	 * spelaren spelat i en viss match.
	 * 
	 * @param playerSlot
	 *            - En sträng som innehåller siffror hämtade från APIn. Se
	 *            API-dokumentation för mera info.
	 * @return Laget som spelaren spelat på.
	 */
	private String generateTeam(String playerSlot) {

		if (playerSlot.length() == 1) {
			String team = "Radiant";
			return team;
		} else if (playerSlot.length() > 1) {
			String team = "Dire";
			return team;
		} else {
			return null;
		}
	}

	/**
	 * En hjälpmetod för att konvertera data om ifall spelaren vunnit matchen
	 * eller ej.
	 * 
	 * @param radiant_win
	 *            - En boolean hämtad från apin. True: Radiant vann, False: Dire
	 *            vann.
	 * @param team
	 *            - Laget som spelare spelat på.
	 * @return En Int som är 1 eller 0. 1 ifall vinst, 0 ifall förlust.
	 *         Returnerar 2 ifall något gått snett.
	 */
	private int generateWin(boolean radiant_win, String team) {
		int win = 2;

		if (radiant_win && team.equals("Radiant")) {
			win = 1;
		} else if (radiant_win && team.equals("Dire")) {
			win = 0;
		} else if (!radiant_win && team.equals("Dire")) {
			win = 1;
		} else if (!radiant_win && team.equals("Radiant")) {
			win = 0;
		} else {
			win = 2;
		}

		return win;
	}

	/**
	 * Lagrar användarnamn i databastabellen USERNAMES.
	 * 
	 * @param userIDNames
	 *            - En Map innehållande ett steamID och dess användarnamn.
	 */
	public void storeUserNames(Map<String, String> userIDNames) {

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			stmt = c.createStatement();

			for (Map.Entry<String, String> entry : userIDNames.entrySet()) {
				String tmp = entry.getValue().replaceAll("'", "''");
				System.out.println(tmp);
				String sql = "UPDATE USERNAMES " + "SET USER_NAME = '" + tmp + "' WHERE ACCOUNT_ID = '" + entry.getKey()
						+ "';";
				stmt.executeUpdate(sql);
			}
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			;
		}
	}

	public String getSummaryDataMatches() {
		String matchesStored = "There are ";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(MATCH_ID) AS MATCH_AMOUNT FROM MATCHES;");
			matchesStored += rs.getString("MATCH_AMOUNT");
			if (matchesStored.equals("")) {
				return "There are no matches stored\n";
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		matchesStored += " matches stored in the database\n";
		return matchesStored;
	}

	public String getSummaryDataPlayers() {
		String mostOccurringPlayers = "The top ten most occurring players are:\n";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT ACCOUNT_ID, COUNT(ACCOUNT_ID) AS OCCURRENCE FROM PLAYERS GROUP BY ACCOUNT_ID ORDER BY OCCURRENCE DESC LIMIT 10;");
			while (rs.next()) {
				mostOccurringPlayers += "Steam ID - " + rs.getString("ACCOUNT_ID") + ": ";
				mostOccurringPlayers += rs.getString("OCCURRENCE") + " matches\n";
			}
			if (mostOccurringPlayers.equals("")) {
				return "There are no players stored";
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return mostOccurringPlayers;
	}

	/**
	 * Hämtar namn på alla heroes ur databasen.
	 * 
	 * @return En ArrayList med alla hero-namn.
	 */
	public ArrayList<String> getHeroNameList() {
		ArrayList<String> heroNameList = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT HERO_NAME FROM HEROES ORDER BY HERO_NAME;");
			while (rs.next()) {
				heroNameList.add(rs.getString("HERO_NAME"));
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return heroNameList;
	}

	/**
	 * Hämtar namn på alla spelare ur databasen.
	 * 
	 * @return En ArrayList med alla användarnamn.
	 */
	public ArrayList<String> getUserNameList() {
		ArrayList<String> userNameList = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT USER_NAME FROM USERNAMES WHERE USER_NAME NOT LIKE 'ANONYMOUS' ORDER BY USER_NAME ASC;");

			while (rs.next()) {
				userNameList.add(rs.getString("USER_NAME"));
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return userNameList;
	}

	/**
	 * Hämtar ett givet användarnamns steamID.
	 * 
	 * @param userName
	 * @return steamID
	 */
	public String getSteamID(String userName) {
		String steamID = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ACCOUNT_ID FROM USERNAMES WHERE USER_NAME = '" + userName + "';");
			steamID = rs.getString("ACCOUNT_ID");
			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return steamID;
	}

	/**
	 * Hämtar ett givet steamIDs användarnamn.
	 * 
	 * @param steamID
	 * @return userName
	 */
	public String getUserName(String steamID) {
		String userName = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT USER_NAME FROM USERNAMES WHERE ACCOUNT_ID = '" + steamID + "';");
			userName = rs.getString("USER_NAME");
			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return userName;
	}

	/**
	 * Hämtar ett hero-namn för ett givet heroID ur databasen.
	 * 
	 * @param heroID
	 * @return heroName
	 */
	public String getHeroName(String heroID) {
		String heroName = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT HERO_NAME FROM HEROES WHERE HERO_ID = '" + heroID + "';");
			heroName = rs.getString("USER_NAME");
			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return heroName;
	}

	/**
	 * Hämtar en lista på alla användare i databastabellen USERNAME som saknar
	 * användarnamn.
	 * 
	 * @return En ArrayList med användarid som saknar namn i databasen.
	 */
	public ArrayList<String> getAccountIdNullList() {
		ArrayList<String> accountIDString = new ArrayList<String>();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ACCOUNT_ID FROM USERNAMES WHERE USER_NAME IS NULL;");
			while (rs.next()) {
				accountIDString.add(rs.getString("ACCOUNT_ID"));
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return accountIDString;
	}

	/**
	 * Hämtar data som behövs för att visa statistik i Statistics-tabben i
	 * programmet.
	 * 
	 * @param id
	 *            - SteamID
	 * @param heroName
	 *            - Heroens namn.
	 * @param team
	 *            - Lag man spelat på.
	 * @return En tvådimensionell lista av typen Object som innehåller
	 *         statistikdata.
	 */
	public Object[][] getStatisticsData(String id, String heroName, String team) {
		String[] rowHeaders = { "AVG", "MAX", "MIN" };
		Object[][] data = new Object[3][8];
		String sql = "";

		if (team.equals("Both")) {
			sql = "SELECT avg(KILLS), avg(DEATHS), avg(ASSISTS), avg(GOLD_PER_MINUTE), avg(XP_PER_MINUTE), avg(LAST_HITS), "
					+ "avg(DENIES), "
					+ "max(KILLS), max(DEATHS), max(ASSISTS), max(GOLD_PER_MINUTE), max(XP_PER_MINUTE), max(LAST_HITS), "
					+ "max(DENIES), "
					+ "min(KILLS), min(DEATHS), min(ASSISTS), min(GOLD_PER_MINUTE), min(XP_PER_MINUTE), min(LAST_HITS), "
					+ "min(DENIES) " + "FROM PLAYERS INNER JOIN HEROES ON PLAYERS.HERO_ID = HEROES.HERO_ID "
					+ "WHERE PLAYERS.ACCOUNT_ID = " + id + " " + "AND "
					+ "(SELECT HEROES.HERO_ID FROM HEROES WHERE HEROES.HERO_NAME = '" + heroName + "') "
					+ "= PLAYERS.HERO_ID";
		} else {
			sql = "SELECT avg(KILLS), avg(DEATHS), avg(ASSISTS), avg(GOLD_PER_MINUTE), avg(XP_PER_MINUTE), avg(LAST_HITS), "
					+ "avg(DENIES), "
					+ "max(KILLS), max(DEATHS), max(ASSISTS), max(GOLD_PER_MINUTE), max(XP_PER_MINUTE), max(LAST_HITS), "
					+ "max(DENIES), "
					+ "min(KILLS), min(DEATHS), min(ASSISTS), min(GOLD_PER_MINUTE), min(XP_PER_MINUTE), min(LAST_HITS), "
					+ "min(DENIES) " + "FROM PLAYERS INNER JOIN HEROES ON PLAYERS.HERO_ID = HEROES.HERO_ID "
					+ "WHERE PLAYERS.ACCOUNT_ID = " + id + " " + "AND "
					+ "(SELECT HEROES.HERO_ID FROM HEROES WHERE HEROES.HERO_NAME = '" + heroName + "') "
					+ "= PLAYERS.HERO_ID " + "AND PLAYERS.PLAYER_TEAM = '" + team + "'";
		}

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i < 8; i++) {
					data[0][i] = Math.round((rs.getFloat(rsmd.getColumnLabel(i))) * 100.0) / 100.0;
				}
				for (int i = 1; i < 8; i++) {
					data[1][i] = rs.getInt(rsmd.getColumnLabel(i + 7));
				}
				for (int i = 1; i < 8; i++) {
					data[2][i] = rs.getInt(rsmd.getColumnLabel(i + 14));
				}
			}

			for (int i = 0; i < rowHeaders.length; i++) {
				data[i][0] = rowHeaders[i];
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		return data;
	}

	/**
	 * Hämtar data som behövs för att visa information i Visuals-tabbens
	 * piechart.
	 * 
	 * @param accountID
	 * @param faction
	 *            - Lag
	 * @param lobbyType
	 * @return En Map innehållande de tio mest spelade Heronamnen för en viss
	 *         spelare och antalet matcher en användare spelat med de heroes.
	 */
	public Map<String, Integer> getPieChartData(String accountID, String faction, String lobbyType) {
		Map<String, Integer> pieDataInput = new HashMap<String, Integer>();
		ResultSet rs = null;
		int lobbyTypeValue = 0;

		if (lobbyType.equals("Ranked")) {
			lobbyTypeValue = 7;
		} else if (lobbyType.equals("Normal")) {
			lobbyTypeValue = 0;
		}

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			if (faction.equals("Both") && lobbyType.equals("All")) {
				System.out.println("1" + lobbyType + faction);
				rs = stmt.executeQuery("SELECT COUNT(HEROES.HERO_NAME) AS TIMES_PLAYED, HEROES.HERO_NAME "
						+ "FROM HEROES INNER JOIN PLAYERS ON PLAYERS.HERO_ID = HEROES.HERO_ID "
						+ "WHERE PLAYERS.ACCOUNT_ID = '" + accountID + "' " + "GROUP BY HEROES.HERO_NAME "
						+ "ORDER BY COUNT(HEROES.HERO_NAME) DESC LIMIT 10;");

			} else if (faction.equals("Both") && !lobbyType.equals("All")) {
				System.out.println("2" + lobbyType + faction);
				rs = stmt.executeQuery("SELECT COUNT(HEROES.HERO_NAME) AS TIMES_PLAYED, HEROES.HERO_NAME "
						+ "FROM HEROES INNER JOIN PLAYERS ON PLAYERS.HERO_ID = HEROES.HERO_ID "
						+ "INNER JOIN MATCHES ON MATCHES.MATCH_ID = PLAYERS.MATCH_ID WHERE PLAYERS.ACCOUNT_ID = '"
						+ accountID + "' AND MATCHES.LOBBY_TYPE = '" + lobbyTypeValue + "' "
						+ "GROUP BY HEROES.HERO_NAME " + "ORDER BY COUNT(HEROES.HERO_NAME) DESC LIMIT 10;");

			} else if (!faction.equals("Both") && lobbyType.equals("All")) {
				System.out.println("3" + lobbyType + faction);
				rs = stmt.executeQuery("SELECT COUNT(HEROES.HERO_NAME) AS TIMES_PLAYED, HEROES.HERO_NAME "
						+ "FROM HEROES INNER JOIN PLAYERS ON PLAYERS.HERO_ID = HEROES.HERO_ID "
						+ "INNER JOIN MATCHES ON MATCHES.MATCH_ID = PLAYERS.MATCH_ID WHERE PLAYERS.ACCOUNT_ID = '"
						+ accountID + "' AND PLAYERS.PLAYER_TEAM = '" + faction + "' " + "GROUP BY HEROES.HERO_NAME "
						+ "ORDER BY COUNT(HEROES.HERO_NAME) DESC LIMIT 10;");

			} else if (!lobbyType.equals("All") && !faction.equals("Both")) {
				System.out.println("4" + lobbyType + faction);
				rs = stmt.executeQuery("SELECT COUNT(HEROES.HERO_NAME) AS TIMES_PLAYED, HEROES.HERO_NAME "
						+ "FROM HEROES INNER JOIN PLAYERS ON PLAYERS.HERO_ID = HEROES.HERO_ID "
						+ "INNER JOIN MATCHES ON MATCHES.MATCH_ID = PLAYERS.MATCH_ID WHERE PLAYERS.ACCOUNT_ID = '"
						+ accountID + "' AND PLAYERS.PLAYER_TEAM = '" + faction + "' AND MATCHES.LOBBY_TYPE = '"
						+ lobbyTypeValue + "' GROUP BY HEROES.HERO_NAME ORDER BY COUNT(HEROES.HERO_NAME) DESC "
						+ "LIMIT 10;");
			}

			while (rs.next()) {
				pieDataInput.put(rs.getString("HERO_NAME"), rs.getInt("TIMES_PLAYED"));
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return pieDataInput;
	}

	/**
	 * Hämtar vinstprocentdata med hjälp av metoden getUserHeroWinRatio, för ett
	 * visst antal heroes.
	 * 
	 * @param steamID
	 *            - spelarens ID.
	 * @param heroList
	 *            - Lista innehållande heroes man vill ha data om.
	 * @return En Map med Heronamn och en användares vinstprocent för den
	 *         heroen.
	 */
	public Map<String, Number> getBarChartData(String steamID, String[] heroList) {
		Map<String, Number> barDataInput = new HashMap<String, Number>();

		for (int i = 0; i < heroList.length; i++) {
			barDataInput.put(heroList[i], Double.parseDouble(getUserHeroWinRatio(heroList[i], steamID)));
		}

		return barDataInput;
	}

	/**
	 * 
	 * Hämtar data som behövs för att visa Linecharten i Visuals-tabben.
	 * 
	 * @param steamID
	 * @param faction
	 * @param lobbyType
	 * @return En lista med LineChartData-objekt.
	 */
	public ArrayList<LineChartData> getLineChartData(String steamID, String faction, String lobbyType) {
		ArrayList<LineChartData> lineChartDataArray = new ArrayList<LineChartData>();
		ResultSet rs = null;
		int lobbyTypeValue = 0;

		if (lobbyType.equals("Ranked")) {
			lobbyTypeValue = 7;
		} else if (lobbyType.equals("Normal")) {
			lobbyTypeValue = 0;
		}

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			if (faction.equals("Both") && lobbyType.equals("All")) {
				rs = stmt.executeQuery("SELECT PLAYERS.MATCH_ID, PLAYERS.GOLD_PER_MINUTE, PLAYERS.XP_PER_MINUTE"
						+ " FROM PLAYERS" + " WHERE PLAYERS.ACCOUNT_ID = '" + steamID + "'"
						+ " ORDER BY PLAYERS.MATCH_ID ASC" + " LIMIT 100;");

			} else if (faction.equals("Both") && !lobbyType.equals("All")) {
				rs = stmt.executeQuery("SELECT PLAYERS.MATCH_ID, PLAYERS.GOLD_PER_MINUTE, PLAYERS.XP_PER_MINUTE "
						+ "FROM PLAYERS INNER JOIN MATCHES ON MATCHES.MATCH_ID = PLAYERS.MATCH_ID "
						+ "WHERE PLAYERS.ACCOUNT_ID = '" + steamID + "' AND MATCHES.LOBBY_TYPE = '" + lobbyTypeValue
						+ "' " + "ORDER BY PLAYERS.MATCH_ID ASC LIMIT 100;");

			} else if (lobbyType.equals("All") && !faction.equals("Both")) {
				rs = stmt.executeQuery("SELECT PLAYERS.MATCH_ID, PLAYERS.GOLD_PER_MINUTE, PLAYERS.XP_PER_MINUTE "
						+ "FROM PLAYERS INNER JOIN MATCHES ON MATCHES.MATCH_ID = PLAYERS.MATCH_ID "
						+ "WHERE PLAYERS.ACCOUNT_ID = '" + steamID + "' AND PLAYERS.PLAYER_TEAM = '" + faction + "' "
						+ "ORDER BY PLAYERS.MATCH_ID ASC LIMIT 100;");

			} else if (!lobbyType.equals("All") && !faction.equals("Both")) {
				rs = stmt.executeQuery("SELECT PLAYERS.MATCH_ID, PLAYERS.GOLD_PER_MINUTE, PLAYERS.XP_PER_MINUTE "
						+ "FROM PLAYERS INNER JOIN MATCHES ON MATCHES.MATCH_ID = PLAYERS.MATCH_ID "
						+ "WHERE PLAYERS.ACCOUNT_ID = '" + steamID + "' AND PLAYERS.PLAYER_TEAM = '" + faction
						+ "' AND MATCHES.LOBBY_TYPE = '" + lobbyTypeValue + "' "
						+ "ORDER BY PLAYERS.MATCH_ID ASC LIMIT 100;");

			}

			while (rs.next()) {
				lineChartDataArray.add(new LineChartData(rs.getString("MATCH_ID"), rs.getInt("GOLD_PER_MINUTE"),
						rs.getInt("XP_PER_MINUTE")));
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return lineChartDataArray;
	}

	/**
	 * En metod som tar hämtar och räknar ut vinstprocent för en viss spelare
	 * och hero.
	 * 
	 * @param heroName
	 * @param steamID
	 * @return Vinstprocenten som en sträng.
	 */
	public String getUserHeroWinRatio(String heroName, String steamID) {

		String sql1 = "SELECT COUNT(PLAYERS.MATCH_ID)" + "FROM PLAYERS " + "WHERE PLAYERS.ACCOUNT_ID = '" + steamID
				+ "' AND PLAYERS.HERO_ID = " + "(SELECT HEROES.HERO_ID " + "FROM HEROES " + "WHERE HEROES.HERO_NAME = '"
				+ heroName + "')" + ";";

		String sql2 = "SELECT COUNT(PLAYERS.WIN) " + "FROM PLAYERS " + "WHERE PLAYERS.ACCOUNT_ID = '" + steamID
				+ "' AND PLAYERS.HERO_ID = " + "(SELECT HEROES.HERO_ID " + "FROM HEROES " + "WHERE HEROES.HERO_NAME = '"
				+ heroName + "') " + "AND PLAYERS.WIN = 1" + ";";

		double matches = 0;
		double wins = 0;
		double winRatio = 0.0;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();

			ResultSet rs1 = stmt.executeQuery(sql1);

			matches = rs1.getFloat(1);

			rs1.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();

			ResultSet rs2 = stmt.executeQuery(sql2);

			wins = rs2.getFloat(1);

			rs2.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}

		winRatio = Math.round(((double) 100 * (wins / matches)) * 100.0) / 100.0;
		return Double.toString(winRatio);
	}

	/**
	 * En metod som räknar ut en användares totala vinstprocent i databasen.
	 * 
	 * @param steamID
	 * @return Vinstprocenten som en sträng.
	 */
	public String getUserWinrate(String steamID) {

		double matches = 0;
		double wins = 0;
		double winRatio = 0.0;

		String sql1 = "SELECT COUNT(PLAYERS.MATCH_ID) " + "FROM PLAYERS " + "WHERE PLAYERS.ACCOUNT_ID = '" + steamID
				+ "';";

		String sql2 = "SELECT COUNT(PLAYERS.WIN) " + "FROM PLAYERS " + "WHERE PLAYERS.ACCOUNT_ID = '" + steamID + "' "
				+ " AND PLAYERS.WIN = 1;";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery(sql1);

			matches = rs.getFloat(1);

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery(sql2);

			wins = rs.getFloat(1);

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}

		winRatio = Math.round(((double) 100 * (wins / matches)) * 100.0) / 100.0;
		return Double.toString(winRatio);
	}

	/**
	 * Räknar ut totala vinstprocenten för en viss hero i databasen.
	 * 
	 * @param heroName
	 * @return Vinstprocenten som en sträng.
	 */
	public String getHeroWinrate(String heroName) {

		double matches = 0;
		double wins = 0;
		double winRatio = 0.0;

		String sql1 = "SELECT COUNT(PLAYERS.MATCH_ID) " + "FROM PLAYERS " + "WHERE PLAYERS.HERO_ID = "
				+ "(SELECT HEROES.HERO_ID " + "FROM HEROES " + "WHERE HEROES.HERO_NAME = '" + heroName + "');";

		String sql2 = "SELECT COUNT(PLAYERS.MATCH_ID) " + "FROM PLAYERS " + "WHERE PLAYERS.HERO_ID = "
				+ "(SELECT HEROES.HERO_ID " + "FROM HEROES WHERE HEROES.HERO_NAME = '" + heroName + "') "
				+ "AND PLAYERS.WIN = 1;";

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery(sql1);

			matches = rs.getFloat(1);

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();

			ResultSet rs = stmt.executeQuery(sql2);

			wins = rs.getFloat(1);

			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}

		winRatio = Math.round(((double) 100 * (wins / matches)) * 100.0) / 100.0;
		return Double.toString(winRatio);
	}

	/**
	 * Räknar ut vilka heroes som har bäst vinstprocent i databasen.
	 * 
	 * @return En lista med HeroWinRate-objekt.
	 */
	public ArrayList<HeroWinrate> getTop10PHeroes() {

		ArrayList<HeroWinrate> top10Heroes = new ArrayList<HeroWinrate>();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT (ROUND(AVG(PLAYERS.WIN), 4)*100) AS WINRATE, HEROES.HERO_NAME "
					+ "FROM PLAYERS INNER JOIN HEROES ON PLAYERS.HERO_ID = HEROES.HERO_ID "
					+ "GROUP BY HEROES.HERO_NAME " + "HAVING COUNT(PLAYERS.HERO_ID) > 15 " + "ORDER BY WINRATE DESC "
					+ "LIMIT 10");

			while (rs.next()) {
				top10Heroes.add(new HeroWinrate(rs.getString("HERO_NAME"), rs.getString("WINRATE") + "%"));
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return top10Heroes;
	}

	/**
	 * Räknar ut vilka heroes som har sämst vinstprocent i databasen.
	 * 
	 * @return En lista med HeroWinRate-objekt.
	 */
	public ArrayList<HeroWinrate> getBottom10Heroes() {

		ArrayList<HeroWinrate> bottom10Heroes = new ArrayList<HeroWinrate>();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			c.setAutoCommit(false);
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT (ROUND(AVG(PLAYERS.WIN), 4)*100) AS WINRATE, HEROES.HERO_NAME "
					+ "FROM PLAYERS INNER JOIN HEROES ON PLAYERS.HERO_ID = HEROES.HERO_ID "
					+ "GROUP BY HEROES.HERO_NAME " + "HAVING COUNT(PLAYERS.HERO_ID) > 15 " + "ORDER BY WINRATE ASC "
					+ "LIMIT 10;");

			while (rs.next()) {
				bottom10Heroes.add(new HeroWinrate(rs.getString("HERO_NAME"), rs.getString("WINRATE") + "%"));
			}

			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
		return bottom10Heroes;
	}

	/**
	 * Skapar en CSV-fil på basis av angivna parametrar. Hämtar data för en viss
	 * användare. En modifierad version av PLAYERS-tabellen.
	 * 
	 * @param steamID
	 * @param fileName
	 *            - Namnet man vill ha på filen.
	 * @param directory
	 *            - Path dit man vill spara csv-filen.
	 * @param passWord
	 *            - Lösenord ifall man vill kryptera filen.
	 * @param encrypted
	 *            - boolean som anger ifall man vill ha filen krypterad eller
	 *            ej.
	 * @return CSV-sträng.
	 */
	public String exportUserDataToCSV(String steamID, String fileName, String directory, String passWord,
			boolean encrypted) {

		StringBuilder csv = new StringBuilder();
		String fileNameType = directory + "\\" + fileName + ".csv";
		String sql = "SELECT PLAYERS.ACCOUNT_ID, USERNAMES.USER_NAME, PLAYERS.PLAYER_TEAM, PLAYERS.WIN, PLAYERS.KILLS, PLAYERS.DEATHS, PLAYERS.ASSISTS, "
				+ "PLAYERS.GOLD_PER_MINUTE, PLAYERS.XP_PER_MINUTE, PLAYERS.HERO_LEVEL, PLAYERS.LAST_HITS, PLAYERS.DENIES, HEROES.HERO_NAME, PLAYERS.MATCH_ID "
				+ "FROM PLAYERS INNER JOIN HEROES " + "ON PLAYERS.HERO_ID = HEROES.HERO_ID "
				+ "INNER JOIN USERNAMES ON USERNAMES.ACCOUNT_ID = PLAYERS.ACCOUNT_ID " + "WHERE PLAYERS.ACCOUNT_ID = '"
				+ steamID + "'";
		try {
			FileWriter fw = new FileWriter(fileNameType);
			csv.append("ACCOUNT_ID, USER_NAME, PLAYER_TEAM, WIN (1) / Loss (0), KILLS, DEATHS, ASSISTS, "
					+ "GOLD_PER_MINUTE, XP_PER_MINUTE, HERO_LEVEL, LAST_HITS, DENIES, HERO_NAME, MATCH_ID\n");
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				csv.append(rs.getString("ACCOUNT_ID") + ",");
				csv.append(rs.getString("USER_NAME") + ",");
				csv.append(rs.getString("PLAYER_TEAM") + ",");
				csv.append(rs.getString("WIN") + ",");
				csv.append(rs.getString("KILLS") + ",");
				csv.append(rs.getString("DEATHS") + ",");
				csv.append(rs.getString("ASSISTS") + ",");
				csv.append(rs.getString("GOLD_PER_MINUTE") + ",");
				csv.append(rs.getString("XP_PER_MINUTE") + ",");
				csv.append(rs.getString("HERO_LEVEL") + ",");
				csv.append(rs.getString("LAST_HITS") + ",");
				csv.append(rs.getString("DENIES") + ",");
				csv.append(rs.getString("HERO_NAME") + ",");
				csv.append(rs.getString("MATCH_ID"));
				csv.append('\n');
			}

			if (encrypted) {
				csv.replace(0, csv.length() - 1, EncryptCSV(csv.toString(), passWord));
			}

			fw.append(csv.toString());
			System.out.println(fw.toString());
			rs.close();
			stmt.close();
			fw.flush();
			fw.close();
			c.close();

			if (encrypted) {
				return "Encrypted CSV file was created successfully\n";
			} else {
				return "CSV File was created successfully\n";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Something went wrong while exporting data\n";
	}

	/**
	 * Skapar en csv-fil på basis av angivna parametrar. Hämtar data för alla
	 * användare. En modifierad version av PLAYERS-tabellen.
	 * 
	 * @param fileName
	 *            - Namnet man vill ha på filen.
	 * @param directory
	 *            - Path dit man vill spara csv-filen.
	 * @param passWord
	 *            - Lösenord ifall man vill kryptera filen.
	 * @param encrypted
	 *            - boolean som anger ifall man vill ha filen krypterad eller
	 *            ej.
	 * @return En sträng som visas i avändargränssnittets konsol.
	 */
	public String exportAllDataToCSV(String fileName, String directory, String passWord, boolean encrypted) {

		StringBuilder csv = new StringBuilder();
		String fileNameType = directory + "\\" + fileName + ".csv";
		String sql = "SELECT PLAYERS.ACCOUNT_ID, USERNAMES.USER_NAME, PLAYERS.PLAYER_TEAM, PLAYERS.WIN, PLAYERS.KILLS, PLAYERS.DEATHS, PLAYERS.ASSISTS, "
				+ "PLAYERS.GOLD_PER_MINUTE, PLAYERS.XP_PER_MINUTE, PLAYERS.HERO_LEVEL, PLAYERS.LAST_HITS, PLAYERS.DENIES, HEROES.HERO_NAME, PLAYERS.MATCH_ID "
				+ "FROM PLAYERS INNER JOIN HEROES ON PLAYERS.HERO_ID = HEROES.HERO_ID "
				+ "INNER JOIN USERNAMES ON USERNAMES.ACCOUNT_ID = PLAYERS.ACCOUNT_ID";
		try {
			FileWriter fw = new FileWriter(fileNameType);
			csv.append("ACCOUNT_ID, USER_NAME, PLAYER_TEAM, WIN (1) / Loss (0), KILLS, DEATHS, ASSISTS, "
					+ "GOLD_PER_MINUTE, XP_PER_MINUTE, HERO_LEVEL, LAST_HITS, DENIES, HERO_NAME, MATCH_ID\n");
			Class.forName("org.sqlite.JDBC");
			Connection c = DriverManager.getConnection("jdbc:sqlite:dota2.db");
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {

				csv.append(rs.getString("ACCOUNT_ID") + ",");
				csv.append(rs.getString("USER_NAME") + ",");
				csv.append(rs.getString("PLAYER_TEAM") + ",");
				csv.append(rs.getString("WIN") + ",");
				csv.append(rs.getString("KILLS") + ",");
				csv.append(rs.getString("DEATHS") + ",");
				csv.append(rs.getString("ASSISTS") + ",");
				csv.append(rs.getString("GOLD_PER_MINUTE") + ",");
				csv.append(rs.getString("XP_PER_MINUTE") + ",");
				csv.append(rs.getString("HERO_LEVEL") + ",");
				csv.append(rs.getString("LAST_HITS") + ",");
				csv.append(rs.getString("DENIES") + ",");
				csv.append(rs.getString("HERO_NAME") + ",");
				csv.append(rs.getString("MATCH_ID"));
				csv.append('\n');
			}

			if (encrypted) {
				csv.replace(0, csv.length() - 1, EncryptCSV(csv.toString(), passWord));
			}

			fw.append(csv.toString());

			rs.close();
			stmt.close();
			fw.flush();
			fw.close();
			c.close();
			if (encrypted) {
				return "Encrypted CSV file was created successfully\n";
			} else {
				return "CSV file was created successfully\n";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Something went wrong while exporting data\n";
	}

	/**
	 * Tar in en sträng och krypterar innehållet med hjälp av
	 * JASYPT-biblioteket.
	 * 
	 * @param csv
	 * @param passWord
	 * @return en krypterad sträng.
	 */
	private String EncryptCSV(String csv, String passWord) {

		BasicTextEncryptor cryptor = new BasicTextEncryptor();
		cryptor.setPassword(passWord);
		String csvEncrypted = cryptor.encrypt(csv);

		return csvEncrypted;
	}

	/**
	 * Tar in en sträng och dekrypterar innehållet med hjälp av
	 * JASYPT-biblioteket.
	 * 
	 * @param csv
	 *            - Sträng.
	 * @param passWord
	 *            - Krypteringens lösenord.
	 * @return en dekrypterad sträng.
	 */
	public String DecryptCSV(String csv, String passWord) {

		BasicTextEncryptor cryptor = new BasicTextEncryptor();
		cryptor.setPassword(passWord);
		String csvDecrypted = cryptor.decrypt(csv);

		return csvDecrypted;
	}

	/**
	 * Tar in, dekrypterar och skapar en ny okrypterad CSV-fil.
	 * 
	 * @param readFileName
	 *            - filens path.
	 * @param exportPath
	 *            - path dit man vill spara filen.
	 * @param exportFileName
	 *            - filnamnet på filen man vill dekryptera.
	 * @param passWord
	 *            - lösenordet för att dekryptera filen.
	 * @return En sträng som visas i avändargränssnittets konsol.
	 */
	public String exportDecryptedCSV(String readFileName, String exportPath, String exportFileName, String passWord) {

		BufferedReader reader = null;
		StringBuilder csv = new StringBuilder();
		String line;
		String decryptedCSV = ("ACCOUNT_ID, USER_NAME, PLAYER_TEAM, WIN (1) / Loss (0), KILLS, DEATHS, ASSISTS, "
				+ "GOLD_PER_MINUTE, XP_PER_MINUTE, HERO_LEVEL, LAST_HITS, DENIES, HERO_NAME, MATCH_ID\n");
		try {
			reader = new BufferedReader(new FileReader(readFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while ((line = reader.readLine()) != null) {
				csv.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		String tmp = DecryptCSV(csv.toString(), passWord);
		decryptedCSV += tmp;
		try {
			FileWriter fw = new FileWriter(exportPath + "\\" + exportFileName + ".csv");
			fw.append(decryptedCSV);
			fw.flush();
			fw.close();
			return "Decrypted CSV file was created successfully\n";

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Something went wrong while exporting data";
	}
}