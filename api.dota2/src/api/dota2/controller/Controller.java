package api.dota2.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import api.dota2.model.APIHandler;
import api.dota2.model.HeroWinrate;
import api.dota2.model.Heroes;
import api.dota2.model.LineChartData;
import api.dota2.model.Matches;
import api.dota2.model.Players;
import api.dota2.model.SQLiteJDBC;
import api.dota2.view.View;

public class Controller {

	private View theView;
	private APIHandler apiHandler;
	private SQLiteJDBC database;

	public Controller(View theView, APIHandler theModel) {
		this.theView = theView;
		this.apiHandler = theModel;
		database = new SQLiteJDBC();
		theView.enableTabs(database.checkDBExists());
		if (database.checkDBExists()) {
			try {
				theView.setSteamNameCombobox(database.getUserNameList());
				theView.setStatsHeroSelectionComboBox(database.getHeroNameList());
				theView.setChooseExportPlayerComboBox(database.getUserNameList());
			} catch (Exception e) {
				e.printStackTrace();
				e.getMessage();
			}
		}
		this.theView.addAPICallListener(new APICallListener());
		this.theView.addStatQueryListener(new StatisticsDataListener());
		this.theView.addJFXListener(new JFXListener());
		this.theView.addTglBtnListener(new TglBtnListener());
		this.theView.addExportDataListener(new ExportDataListener());
		this.theView.addChooseDecryptFileListener(new ChooseDecryptFileListener());
		this.theView.addDecryptAndExportListener(new DecryptAndExportListener());
	}

	class APICallListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (theView.getSteamIDText().equals("")) {
				theView.SetGetDataConsoleText("No steamID specified\n");
			} else {
				Thread t1 = new Thread() {
					public void run() {
						getData();
					}
				};
				t1.start();
			}
		}

		public void getData() {

			theView.setProgressLbl("Downloading data...");
			theView.SetGetDataConsoleText(getCurrentTime() + " - Query Started\n");
			theView.disableGetDataBtn();
			theView.setWaitCursor();

			ArrayList<Heroes> heroList = new ArrayList<Heroes>();
			ArrayList<Matches> matchList = new ArrayList<Matches>();
			ArrayList<Players> playerList = new ArrayList<Players>();

			try {
				heroList = apiHandler.getHeroList();
			} catch (Exception e0) {
				e0.printStackTrace();
				e0.getMessage();
			}

			try {
				matchList = apiHandler.getMatchHistory(theView.getSteamIDText());

			} catch (Exception e1) {
				e1.printStackTrace();
				e1.getMessage();
			}

			ExecutorService executor = Executors.newFixedThreadPool(10);
			List<Future<List<Players>>> handles = new ArrayList<Future<List<Players>>>();
			Future<List<Players>> handle;

			if (!matchList.isEmpty()) {

				int i = 0;
				for (final Matches m : matchList) {

					handle = executor.submit(new Callable<List<Players>>() {

						public List<Players> call() {
							List<Players> tempList = null;
							try {
								tempList = apiHandler.getPlayerHistory(m.getMatch_id());
							} catch (Exception e) {
								e.getMessage();
								e.printStackTrace();
							}
							return tempList;
						}
					});
					handles.add(handle);
				}

				for (Future<List<Players>> f : handles) {
					List<Players> tmpList = null;

					try {
						tmpList = f.get();
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
					theView.setProgressBar((double) i / matchList.size());
					for (Players p : tmpList) {
						playerList.add(p);
					}
				}

				theView.setProgressLbl("Storing data in database...");
				theView.SetGetDataConsoleText(database.createDbTables());
				theView.SetGetDataConsoleText(database.insertHeroData(heroList));
				theView.SetGetDataConsoleText(database.insertMatchData(matchList));
				theView.SetGetDataConsoleText(database.insertPlayerData(playerList));
				database.storeUserNames(apiHandler.steamIDtoName(database.getAccountIdNullList()));
				theView.enableTabs(database.checkDBExists());

			} else {
				theView.SetGetDataConsoleText("No more matches to retrieve or Steam ID doesn't exist.\n");
				theView.setProgressLbl("...");
			}

			theView.SetGetDataConsoleText(database.getSummaryDataMatches() + database.getSummaryDataPlayers());
			theView.setStatsHeroSelectionComboBox(database.getHeroNameList());
			theView.setSteamNameCombobox(database.getUserNameList());
			theView.SetGetDataConsoleText(getCurrentTime() + " - Query Ended\n");
			theView.enableGetDataBtn();
			theView.setChooseExportPlayerComboBox(database.getUserNameList());
			theView.setProgressLbl("Done");
			theView.setDefaultCursor();
		}
	}

	class StatisticsDataListener implements ActionListener {
		Object[] columnHeaders = { "MEASURES", "Kills", "Deaths", "Assists", "Gold Per Minute", "XP Per Minute",
				"Last Hits", "Denies" };

		@Override
		public void actionPerformed(ActionEvent e) {

			String steamID = database.getSteamID(theView.getUserNameComboBox());

			theView.setFactsTable(
					database.getStatisticsData(steamID, theView.getHeroComboBox(), theView.getTeamComboBox()),
					columnHeaders);

			theView.setLblUserHeroWinRatioValue(database.getUserHeroWinRatio(theView.getHeroComboBox(), steamID));
			theView.setLblUserWinrateValue(database.getUserWinrate(steamID));
			theView.setLblHeroWinrateValue(database.getHeroWinrate(theView.getHeroComboBox()));

			ArrayList<HeroWinrate> bestHeroes = new ArrayList<HeroWinrate>();
			bestHeroes = database.getTop10PHeroes();

			ArrayList<HeroWinrate> worstHeroes = new ArrayList<HeroWinrate>();
			worstHeroes = database.getBottom10Heroes();

			theView.setTop10Heroes(bestHeroes);
			theView.setBottom10Heroes(worstHeroes);
		}
	}

	class JFXListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Map<String, Integer> pieData;
			Map<String, Number> barData;
			ArrayList<LineChartData> lineChartDataArray;
			String[] heroList = new String[10];

			String userName = theView.showSteamIDDialog(database.getUserNameList());
			String steamID = database.getSteamID(userName);
			String faction = theView.getChartFactionComboBox();
			String lobbyType = theView.getChartLobbyTypeComboBox();

			pieData = database.getPieChartData(steamID, faction, lobbyType);

			int i = 0;
			for (Map.Entry<String, Integer> entry : pieData.entrySet()) {
				heroList[i] = entry.getKey();
				i++;
			}
			barData = database.getBarChartData(steamID, heroList);
			lineChartDataArray = database.getLineChartData(steamID, faction, lobbyType);

			theView.runLineChartJFX(lineChartDataArray);
			theView.runPieChartJFX(pieData);
			theView.runBarChartJFX(barData);
			theView.SetGetDataConsoleText(getCurrentTime() + " - Charts for player " + userName + " displayed\n");
		}
	}

	class TglBtnListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			if (e.getStateChange() == ItemEvent.SELECTED) {
				theView.setTglBtn(true);
				theView.activatePassWordField();
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				theView.setTglBtn(false);
				theView.deactivatePassWordField();
			}
		}
	}

	class ExportDataListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				String steamID = database.getSteamID(theView.getChooseExportPlayerComboBox());
				String destinationFolder = theView.selectDestinationFolder();
				if (!destinationFolder.equals(null) && theView.isRdbtnUserSpecificData()) {
					theView.SetGetDataConsoleText(
							getCurrentTime() + " - " + database.exportUserDataToCSV(steamID, theView.getCSVName(),
									destinationFolder, theView.getChosenPassWord(), theView.getTglBtnState()));
				} else if (!destinationFolder.equals(null) && theView.isRdbtnAllData()) {
					theView.SetGetDataConsoleText(
							getCurrentTime() + " - " + database.exportAllDataToCSV(theView.getCSVName(),
									destinationFolder, theView.getChosenPassWord(), theView.getTglBtnState()));
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	class ChooseDecryptFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				theView.setChosenPath(theView.selectFileToDecrypt());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	class DecryptAndExportListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				theView.SetGetDataConsoleText(
						getCurrentTime() + " - "
								+ database.exportDecryptedCSV(theView.getChosenPath(),
										theView.selectDestinationFolder(), theView.getDecryptedCSVName(),
										theView.getDecryptPassWord()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private String getCurrentTime() {
		SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentTime = timeFormatter.format(System.currentTimeMillis());
		return currentTime;
	}
}
