package api.dota2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * 
 * 
 * ### APIHandler-klassen ###
 * 
 * APIHandler innehåller metoder som gör url-förfrågningar mot VALVE's API, tar
 * emot JSON-objekten och parsar dem till java-objekt som sedan hanteras av
 * SQLiteJDBC. Metoderna getMatchHistory och getPlayerHistory är de metoder som
 * används för att kommunicera med VALVE's API. Metoderna körs multi-trådat för
 * att snabba upp processen eftersom ett stort antal URL-förfrågningar görs.
 * Metoden steamIDtoName använder web-scraping bibliotek för att läsa in
 * HTML-strukturen för en webbsida och möjliggör hämtning av information om
 * spelarnas användarnamn och inte bara deras användar ID'n. Övriga metoder är
 * hjälp-metoder som bearbetar data till lämpligt format.
 * 
 *
 * 
 * 
 *
 */

public class APIHandler {

	ArrayList<Matches> matchList;
	// ArrayList<Players> playerList;
	ArrayList<Heroes> heroList = new ArrayList<Heroes>();
	private static OkHttpClient client = new OkHttpClient();

	public static String getJSON(String url) throws IOException {

		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	/**
	 * 
	 * 
	 * En metod som gör en eller flera url-calls till Valves API för att hämta
	 * matcher. APIn ger max 100 matcher på en gång så ifall det finns fler än
	 * 100 matcher på ett visst ID så görs flera hämtningar genom att
	 * specificera lägsta match-id:t från förra hämntningen.
	 * 
	 * @param steamID
	 *            En sträng innehållande en spelares steamID som specificeras i
	 * @return En ArrayList<Matches> innehållende ett matchobjekt för varje
	 *         match som finns i Valves API för ett visst steamID.
	 * @throws JSONException
	 */
	public ArrayList<Matches> getMatchHistory(String steamID) throws JSONException {

		matchList = new ArrayList<Matches>();
		String json = null;
		JSONArray matches = null;
		System.out.println(steamID);
		List<JSONArray> matchArrays = new ArrayList<JSONArray>();
		boolean lastMatchFetched = false;
		String lastMatchID = "0";

		int i = 0;
		while (!lastMatchFetched) {
			try {
				System.out.println("lastMatchID: " + lastMatchID);
				json = getJSON("https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?start_at_match_id="
						+ lastMatchID + "&key=91948C066D558F324F307E027CFA74EE&account_id=" + steamID);
				JSONObject obj = new JSONObject(json);
				matches = obj.getJSONObject("result").getJSONArray("matches");

				if (matches.length() > 0) {
					lastMatchFetched = false;
					matchArrays.add(matches);
					lastMatchID = findLastMatchID(matches);
				} else {
					lastMatchFetched = true;
				}

			} catch (Exception e) {
				e.getMessage();

				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				i++;
				if (i > 20) {
					System.out.println(i);
					return new ArrayList<Matches>();
				}
				continue;
			}
		}

		for (int j = 0; j < matchArrays.size(); j++) {
			for (int k = 0; k < matchArrays.get(j).length(); k++) {

				json = matchArrays.get(j).getJSONObject(k).toString();
				Gson gson = new Gson();
				Matches match = gson.fromJson(json, Matches.class);
				System.out.println(match.getStart_time());
				matchList.add(match);
			}

		}

		System.out.println(matchArrays.size() + "\n" + matchList.size());
		return matchList;
	}

	/**
	 * Tar in en JSONArray med match-objekt och söker efter lägsta id:t.
	 * 
	 * @param jArray
	 * @return en sträng innehållande lägsta match-id:t.
	 * @throws JSONException
	 */
	public String findLastMatchID(JSONArray jArray) throws JSONException {

		long pointer;
		long min = Long.MAX_VALUE;

		for (int i = 0; i < jArray.length(); i++) {
			pointer = Math.abs(jArray.getJSONObject(i).getLong("match_id"));

			if (pointer < min) {
				min = pointer;
			}
			System.out.println("Smallest match_id: " + min);
		}

		return Long.toString(min - 1);
	}

	/**
	 * En metod som hämtar detaljerad information om spelarna i en viss match.
	 * 
	 * @param matchID
	 * @return En ArrayList innehållande alla hämtade spelarobjekt.
	 * @throws JSONException
	 */
	public ArrayList<Players> getPlayerHistory(String matchID) throws JSONException {

		ArrayList<Players> playerList = new ArrayList<Players>();
		String json = null;
		JSONArray players;
		JSONObject result;

		while (true) {
			try {
				json = getJSON(
						"https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/V001/?key=91948C066D558F324F307E027CFA74EE&match_id="
								+ matchID);
				JSONObject obj = new JSONObject(json);
				result = obj.getJSONObject("result");
				players = obj.getJSONObject("result").getJSONArray("players");
				break;
			} catch (Exception e) {
				e.getMessage();
				continue;
			}
		}

		for (int i = 0; i < players.length(); i++) {

			json = players.getJSONObject(i).toString();

			Gson gson = new Gson();
			Players player = gson.fromJson(json, Players.class);

			// Sets matchID manually since jsonArray doesn't contain the field
			// matchID
			player.setMatch_id(matchID);
			player.setWin(result.getBoolean("radiant_win"));

			playerList.add(player);
		}

		playerList = transformAnonIDs(playerList);
		return playerList;
	}

	/**
	 * En metod som hämtar en lista på alla heroes som finns i spelet.
	 * 
	 * @return En ArrayList med hero-objekt.
	 * @throws JSONException
	 */
	public ArrayList<Heroes> getHeroList() throws JSONException {

		String json = null;

		try {
			json = getJSON(
					"https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001/?key=91948C066D558F324F307E027CFA74EE&language=en_us");
		} catch (Exception e) {
			e.getMessage();
		}

		JSONObject obj = new JSONObject(json);
		JSONArray heroes = obj.getJSONObject("result").getJSONArray("heroes");

		for (int i = 0; i < heroes.length(); i++) {

			json = heroes.getJSONObject(i).toString();

			Gson gson = new Gson();
			Heroes hero = gson.fromJson(json, Heroes.class);
			heroList.add(hero);
		}
		
		return heroList;
	}

	/**
	 * Hjälpmetod för att ändra anonymaspelar id:n så att två spelare i samma
	 * match inte får samma id i databasen.
	 * 
	 * @param playerList
	 * @return En ArrayList med Players-objekt där samma ID inte förekommer två
	 *         gånger i samma match.
	 */
	public ArrayList<Players> transformAnonIDs(ArrayList<Players> playerList) {

		int anonCounter = 0;
		for (Players p : playerList) {
			if (p.getAccount_id().equals("4294967295")) {
				p.setAccount_id("4294967295" + Integer.toString(anonCounter));
				anonCounter += 1;
			}
		}
		
		return playerList;
	}

	/**
	 * Konverterar ett SteamID till en användarnamn genom att hämta
	 * användarnamnet från en hemsida vars URL innehåller spelarens SteamID.
	 * 
	 * @param steamIDs
	 * @return En Map med en spelares steamID och användarnamn.
	 */
	public Map<String, String> steamIDtoName(ArrayList<String> steamIDs) {
		String[] steamID64bit = new String[steamIDs.size()];
		Map<String, String> mapIDNames = new ConcurrentHashMap<String, String>();

		for (int i = 0; i < steamIDs.size(); i++) {
			System.out.println(steamIDs.get(i));
			if (!steamIDs.get(i).contains("42949672")) {
				steamID64bit[i] = steamID64bitConverter(steamIDs.get(i));

			} else {
				steamID64bit[i] = steamIDs.get(i);
			}
		}

		ExecutorService executorService = Executors.newFixedThreadPool(100);
		List<Future<String>> handles = new ArrayList<Future<String>>();
		Future<String> handle;
		for (final String s : steamID64bit) {
			if (!s.contains("42949672")) {
				handle = executorService.submit(new Callable<String>() {

					public String call() {
						while (true) {
							try {
								Document doc = Jsoup.connect("http://steamcommunity.com/profiles/" + s).get();
								Elements e = doc.select(".actual_persona_name");
								return (s + "," + e.get(0).text());
							} catch (Exception e) {
								e.printStackTrace();
								continue;
							}
						}

					}
				});
				handles.add(handle);
			} else {
				mapIDNames.put(s, "Anonymous");
			}
		}
		executorService.shutdown();

		for (Future<String> s : handles) {
			String[] tmp = new String[2];
			try {
				tmp = s.get().split(",");
			} catch (Exception e) {
				e.printStackTrace();
			}
			mapIDNames.put(steamID32bitConverter(tmp[0]), tmp[1]);
		}

		return mapIDNames;
	}

	/**
	 * Konverterar ett 32-bitars steamID till ett 64-bitars.
	 * 
	 * @param steamID32bit
	 * @return steamID64bit
	 */
	public String steamID64bitConverter(String steamID32bit) {

		Long steamIDkey = new Long("76561197960265728");
		String steamID64bit = Long.toString((steamIDkey + Long.parseLong(steamID32bit)));

		return steamID64bit;
	}

	/**
	 * Konverterar ett 64-bitars steamID till ett 32-bitars.
	 * 
	 * @param steamID64bit
	 * @return steamID32bit
	 */
	public String steamID32bitConverter(String steamID64bit) {

		Long steamIDkey = new Long("76561197960265728");
		String steamID32bit = Long.toString((Long.parseLong(steamID64bit)) - steamIDkey);

		return steamID32bit;
	}
}
