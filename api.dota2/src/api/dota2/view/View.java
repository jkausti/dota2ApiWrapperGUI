package api.dota2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import api.dota2.controller.Controller;
import api.dota2.model.APIHandler;
import api.dota2.model.HeroWinrate;
import api.dota2.model.LineChartData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

/***
 * 
 * 
 * 
 * ### View-klassen ###
 * 
 * I View klassen initieras tråden som det grafiska gränssnittet körs på. View
 * klassen innehåller också metoden som initierar alla grafiska
 * swing-komponenter som finns i vårt program. Det grafiska gränssnittet är
 * byggt med WindowBuilder som är ett plugin till Eclipse och en del av The
 * Eclipse Tools Project av Eclipse Foundation. WindowBuilder är licensierat via
 * licensen Eclipse Public License 1.0.
 * 
 * ## Metoder ##
 * 
 * # main # - Initierar tråden som det grafiska gränssnittet körs på. - Skapar
 * en instans av objekten View, APIHandler och Controller. - Skickar
 * objektinstanser av objekten View och Apihandler till Controllern.
 * 
 * 
 * # initialize # - Skapar alla objekt som finns i vårt grafiska gränssnitt och
 * skapar layouten i JFramen. - Innehåller all kod som genererats av
 * WindowBuilder-verktyget. I WindowBuilder använder vi oss av GroupLayout som
 * layout-manager. - Vi använder oss av en TabbedPane med flera tabbar för att
 * lättare kunna navigera i programmet.
 * 
 * 
 * # Alla getters och setters # - För att kunna accessera swing-objekten från
 * Controller-klassen.
 * 
 * 
 * # runJFX-metoderna (runPieChartFX, runBarChartFX, runLineChartFX) # - Skapar
 * trådarna som JavaFX-graferna körs på. - Tar in data från Controller-klassen.
 * 
 * 
 * # initJFX-metoderna (initPieChartFX, initBarChartFX, initLineChartFX) # - Tar
 * in datan från och skickar vidare till objekten som behövs för att visa data i
 * graferna. - Använder sig också av createScene-metoderna för att skapa
 * Scene-objekten som behövs i JavaFX-panelerna.
 * 
 * 
 * # Alla addListener-metoder # - Behövs för att swing-objekten ska kunna
 * kommunicera med Controller-klassen.
 * 
 * # selectPath/selectFile-metoder # - Metoder som används för att välja en fil
 * eller specificera en adress.
 * 
 * 
 * 
 */

public class View {

	// Main-frame
	private JFrame frame;
	private JPanel panel;
	private JTabbedPane tabbedPane;
	private JPanel getData;
	private JPanel statistics;
	private JPanel visuals;
	private JPanel export;

	// Get-data-tabb
	private JTextField steamIDText;
	private JLabel steamIDLabel;
	private JTextArea getDataConsole;
	private JScrollPane consoleScrollPane;
	private JButton btnGetData;
	private JButton btnDone;
	private JLabel lblProgress;
	private JProgressBar progressBar;

	// Statistics-tabb
	private JComboBox<String> steamUserNameComboBox;
	private JComboBox<String> heroComboBox;
	private JComboBox<String> teamComboBox;
	private JComboBox<String> chooseExportPlayerComboBox;
	private JButton btnExecute;
	private DefaultTableModel tm;
	private JTable factsTable;
	private JScrollPane factsScroll;
	private JLabel lblUserHeroWinrate;
	private JLabel lblUserWinrate;
	private JLabel lblHeroWinrate;
	private JLabel lblUserHeroWinrateValue;
	private JLabel lblUserWinrateValue;
	private JLabel lblHeroWinrateValue;
	private JLabel lblSteamid;
	private JLabel lblHero;
	private JLabel lblTeam;
	private JPanel top10panel;
	private JLabel lblTopHeroes;
	private JLabel topHero1;
	private JLabel topHero2;
	private JLabel topHero3;
	private JLabel topHero4;
	private JLabel topHero5;
	private JLabel topHero6;
	private JLabel topHero7;
	private JLabel topHero8;
	private JLabel topHero9;
	private JLabel topHero10;
	private JLabel topHero1pcnt;
	private JLabel topHero2pcnt;
	private JLabel topHero3pcnt;
	private JLabel topHero4pcnt;
	private JLabel topHero5pcnt;
	private JLabel topHero6pcnt;
	private JLabel topHero7pcnt;
	private JLabel topHero8pcnt;
	private JLabel topHero9pcnt;
	private JLabel topHero10pcnt;
	private JPanel bottom10panel;
	private JLabel lblBottomHeroes;
	private JLabel botHero1;
	private JLabel botHero2;
	private JLabel botHero3;
	private JLabel botHero4;
	private JLabel botHero5;
	private JLabel botHero6;
	private JLabel botHero7;
	private JLabel botHero8;
	private JLabel botHero9;
	private JLabel botHero10;
	private JLabel botHero1pcnt;
	private JLabel botHero2pcnt;
	private JLabel botHero3pcnt;
	private JLabel botHero4pcnt;
	private JLabel botHero5pcnt;
	private JLabel botHero6pcnt;
	private JLabel botHero7pcnt;
	private JLabel botHero8pcnt;
	private JLabel botHero9pcnt;
	private JLabel botHero10pcnt;
	private JLabel lblExportCSV;

	// Graph-tabb
	private JPanel chartsPanel;
	private JFXPanel fxpiePanel;
	private JFXPanel fxbarPanel;
	private JFXPanel fxlinePanel;
	private JComboBox<String> factionComboBox;
	private JComboBox<String> lobbyTypeComboBox;
	private JLabel lblSelectLobbyType;
	private JLabel lblSelectTeam;

	// Export-tabb
	private JButton btnExecuteCSVExport;
	private JPanel imagePanel;
	private JLabel imageLabel;
	private JRadioButton rdbtnUserSpecificData;
	private JRadioButton rdbtnAllData;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField exportFileName;
	private JTextField cryptionKey;
	private JTextField chosenFile;
	private JToggleButton tglbtnEncrypted;
	private JButton btnDecryptData;
	private JButton btnChooseFileToDecrypt;
	private JSeparator separator;
	private JTextField decryptFileName;
	private JTextField decryptionKey;
	private JLabel lblDecryptCSV;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					APIHandler theModel = new APIHandler();
					new Controller(window, theModel);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public View() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setForeground(java.awt.Color.BLUE);
		frame.getContentPane().setBackground(java.awt.Color.DARK_GRAY);
		frame.getContentPane().setForeground(java.awt.Color.WHITE);
		frame.setBackground(java.awt.Color.WHITE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(new Dimension(
				java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width / 2,
				java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height / 2));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setBounds(new Rectangle(1, 1, 1, 1));
		panel.setForeground(java.awt.Color.DARK_GRAY);
		panel.setBackground(java.awt.Color.WHITE);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		tabbedPane.setBorder(new LineBorder(Color.GRAY, 2));
		tabbedPane.setForeground(java.awt.Color.DARK_GRAY);

		getData = new JPanel();
		getData.setBackground(Color.WHITE);
		getData.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab("Get data", null, getData, "Load user data into database (required on first run)");

		steamIDLabel = new JLabel("SteamID");
		steamIDLabel.setAlignmentY(Component.TOP_ALIGNMENT);
		steamIDLabel.setFont(new Font("Tahoma", Font.BOLD, 13));

		steamIDText = new JTextField();
		steamIDText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		steamIDText.setAlignmentY(Component.TOP_ALIGNMENT);
		steamIDText.setAlignmentX(Component.LEFT_ALIGNMENT);
		steamIDText.setColumns(10);

		btnGetData = new JButton("Get data");
		btnGetData.setAlignmentY(Component.TOP_ALIGNMENT);
		btnGetData.setFont(new Font("Tahoma", Font.BOLD, 15));

		lblProgress = new JLabel("Progress");
		lblProgress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblProgress.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		lblProgress.setHorizontalAlignment(JLabel.CENTER);

		progressBar = new JProgressBar(0, 100);
		progressBar.setFont(new Font("Tahoma", Font.BOLD, 13));
		progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		progressBar.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		getDataConsole = new JTextArea("DOTA 2 StatsGuru\n\n", 10, 10);
		getDataConsole.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.LIGHT_GRAY, null, null));
		getDataConsole.setFont(new Font("Arial Black", Font.BOLD, 24));
		getDataConsole.setLineWrap(true);
		getDataConsole.setEditable(false);
		consoleScrollPane = new JScrollPane(getDataConsole);
		consoleScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout gl_getData = new GroupLayout(getData);
		gl_getData.setHorizontalGroup(gl_getData.createParallelGroup(Alignment.LEADING).addGroup(gl_getData
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_getData.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_getData.createSequentialGroup()
								.addGroup(gl_getData.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblProgress, GroupLayout.PREFERRED_SIZE, 208,
												GroupLayout.PREFERRED_SIZE)
								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE))
								.addGap(825))
						.addGroup(gl_getData.createSequentialGroup().addGap(9).addComponent(consoleScrollPane)
								.addContainerGap())
						.addGroup(
								gl_getData.createSequentialGroup()
										.addComponent(steamIDLabel, GroupLayout.PREFERRED_SIZE, 89,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(steamIDText, GroupLayout.PREFERRED_SIZE, 136,
												GroupLayout.PREFERRED_SIZE)
										.addGap(53).addComponent(btnGetData, GroupLayout.PREFERRED_SIZE, 116,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap(1481, Short.MAX_VALUE)))));
		gl_getData.setVerticalGroup(gl_getData.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_getData.createSequentialGroup().addGap(20)
						.addGroup(gl_getData.createParallelGroup(Alignment.BASELINE, false).addComponent(steamIDLabel)
								.addComponent(steamIDText, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnGetData, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
				.addGap(18).addComponent(consoleScrollPane, GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE).addGap(18)
				.addComponent(lblProgress, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE).addGap(18)
				.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		getData.setLayout(gl_getData);

		statistics = new JPanel();
		statistics.setBackground(Color.WHITE);
		statistics.setAlignmentY(Component.TOP_ALIGNMENT);
		statistics.setAlignmentX(Component.LEFT_ALIGNMENT);
		statistics.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab("Statistics", null, statistics, "Statistical analysis");

		steamUserNameComboBox = new JComboBox<String>();
		steamUserNameComboBox.setAlignmentY(Component.TOP_ALIGNMENT);
		steamUserNameComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		heroComboBox = new JComboBox<String>();
		heroComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		heroComboBox.setAlignmentY(Component.TOP_ALIGNMENT);

		teamComboBox = new JComboBox<String>();
		teamComboBox.setAlignmentY(Component.TOP_ALIGNMENT);
		teamComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		teamComboBox.addItem("Both");
		teamComboBox.addItem("Radiant");
		teamComboBox.addItem("Dire");

		btnExecute = new JButton("Execute");
		btnExecute.setFont(new Font("Tahoma", Font.BOLD, 15));

		factsScroll = new JScrollPane();
		factsScroll.setAlignmentY(Component.BOTTOM_ALIGNMENT);

		lblSteamid = new JLabel("Steam Username");
		lblHero = new JLabel("Hero");
		lblTeam = new JLabel("Team");

		JPanel KPIpanel = new JPanel();
		KPIpanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		KPIpanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		KPIpanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
				"Key Performance Indicators", TitledBorder.LEADING, TitledBorder.TOP, null, Color.RED));

		top10panel = new JPanel();
		top10panel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		top10panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		top10panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Database best",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));

		topHero1 = new JLabel("1");
		topHero2 = new JLabel("2");
		topHero3 = new JLabel("3");
		topHero4 = new JLabel("4");
		topHero5 = new JLabel("5");
		topHero6 = new JLabel("6");
		topHero7 = new JLabel("7");
		topHero8 = new JLabel("8");
		topHero9 = new JLabel("9");
		topHero10 = new JLabel("10");

		topHero1pcnt = new JLabel("Value1");
		topHero1pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero2pcnt = new JLabel("Value2");
		topHero2pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero3pcnt = new JLabel("Value3");
		topHero3pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero4pcnt = new JLabel("Value4");
		topHero4pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero5pcnt = new JLabel("Value5");
		topHero5pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero6pcnt = new JLabel("Value6");
		topHero6pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero7pcnt = new JLabel("Value7");
		topHero7pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero8pcnt = new JLabel("Value8");
		topHero8pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero9pcnt = new JLabel("Value9");
		topHero9pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		topHero10pcnt = new JLabel("Value10");
		topHero10pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));

		lblTopHeroes = new JLabel("10 best heroes according to win-rate");
		lblTopHeroes.setFont(new Font("Tahoma", Font.BOLD, 14));

		GroupLayout gl_top10panel = new GroupLayout(top10panel);
		gl_top10panel.setHorizontalGroup(gl_top10panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_top10panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_top10panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_top10panel.createSequentialGroup()
										.addGroup(gl_top10panel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_top10panel.createSequentialGroup()
														.addComponent(topHero4, GroupLayout.PREFERRED_SIZE, 119,
																GroupLayout.PREFERRED_SIZE)
														.addGap(28))
								.addComponent(topHero3, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)).addGap(301))
						.addGroup(gl_top10panel.createSequentialGroup().addGroup(gl_top10panel
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_top10panel.createSequentialGroup().addGroup(gl_top10panel
										.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_top10panel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_top10panel.createSequentialGroup().addGroup(gl_top10panel
														.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_top10panel.createParallelGroup(Alignment.LEADING)
																.addComponent(topHero10, GroupLayout.PREFERRED_SIZE,
																		119, GroupLayout.PREFERRED_SIZE)
																.addComponent(topHero9, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(topHero8, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(topHero7, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addGroup(gl_top10panel.createSequentialGroup()
																		.addComponent(topHero2,
																				GroupLayout.DEFAULT_SIZE, 175,
																				Short.MAX_VALUE)
																		.addPreferredGap(ComponentPlacement.RELATED)))
														.addGroup(gl_top10panel.createSequentialGroup()
																.addComponent(topHero1, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.RELATED)))
														.addGap(27))
												.addGroup(gl_top10panel.createSequentialGroup()
														.addComponent(topHero5, GroupLayout.PREFERRED_SIZE, 119,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED)))
										.addGroup(gl_top10panel.createSequentialGroup()
												.addComponent(topHero6, GroupLayout.PREFERRED_SIZE, 119,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)))
										.addGroup(gl_top10panel.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_top10panel.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(topHero1pcnt, GroupLayout.DEFAULT_SIZE, 101,
																Short.MAX_VALUE))
												.addGroup(Alignment.LEADING, gl_top10panel
														.createParallelGroup(Alignment.TRAILING)
														.addGroup(gl_top10panel
																.createParallelGroup(Alignment.LEADING, false)
																.addComponent(topHero10pcnt, GroupLayout.DEFAULT_SIZE,
																		94, Short.MAX_VALUE)
																.addComponent(topHero9pcnt, GroupLayout.DEFAULT_SIZE,
																		94, Short.MAX_VALUE)
																.addComponent(topHero7pcnt, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(topHero8pcnt, GroupLayout.DEFAULT_SIZE,
																		94, Short.MAX_VALUE)
																.addComponent(topHero6pcnt, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(topHero4pcnt, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(topHero5pcnt, GroupLayout.DEFAULT_SIZE,
																		127, Short.MAX_VALUE)
																.addComponent(topHero3pcnt, GroupLayout.PREFERRED_SIZE,
																		125, GroupLayout.PREFERRED_SIZE))
														.addComponent(topHero2pcnt, GroupLayout.PREFERRED_SIZE, 125,
																GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addGroup(gl_top10panel.createSequentialGroup().addComponent(lblTopHeroes).addGap(92)))
								.addGap(119)))));
		gl_top10panel.setVerticalGroup(gl_top10panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_top10panel.createSequentialGroup().addGap(45).addComponent(lblTopHeroes).addGap(43)
						.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero1)
								.addComponent(topHero1pcnt))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero2)
						.addComponent(topHero2pcnt))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero3)
						.addComponent(topHero3pcnt))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero4)
						.addComponent(topHero4pcnt))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero5pcnt)
						.addComponent(topHero5))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero6pcnt)
						.addComponent(topHero6))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero7pcnt)
						.addComponent(topHero7))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero8pcnt)
						.addComponent(topHero8))
				.addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero9pcnt)
						.addComponent(topHero9)).addGap(18)
				.addGroup(gl_top10panel.createParallelGroup(Alignment.BASELINE).addComponent(topHero10pcnt)
						.addComponent(topHero10)).addContainerGap()));
		top10panel.setLayout(gl_top10panel);

		bottom10panel = new JPanel();
		bottom10panel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		bottom10panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		bottom10panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Database worst",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 200, 0)));

		botHero1 = new JLabel("1");
		botHero2 = new JLabel("2");
		botHero3 = new JLabel("3");
		botHero4 = new JLabel("4");
		botHero5 = new JLabel("5");
		botHero6 = new JLabel("6");
		botHero7 = new JLabel("7");
		botHero8 = new JLabel("8");
		botHero9 = new JLabel("9");
		botHero10 = new JLabel("10");

		botHero1pcnt = new JLabel("Value1");
		botHero1pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero2pcnt = new JLabel("Value2");
		botHero2pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero3pcnt = new JLabel("Value3");
		botHero3pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero4pcnt = new JLabel("Value4");
		botHero4pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero5pcnt = new JLabel("Value5");
		botHero5pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero6pcnt = new JLabel("Value6");
		botHero6pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero7pcnt = new JLabel("Value7");
		botHero7pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero8pcnt = new JLabel("Value8");
		botHero8pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero9pcnt = new JLabel("Value9");
		botHero9pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));
		botHero10pcnt = new JLabel("Value10");
		botHero10pcnt.setFont(new Font("Tahoma", Font.BOLD, 13));

		lblBottomHeroes = new JLabel("10 worst heroes according to win-rate");
		lblBottomHeroes.setFont(new Font("Tahoma", Font.BOLD, 14));

		GroupLayout gl_bottom10players = new GroupLayout(bottom10panel);
		gl_bottom10players.setHorizontalGroup(gl_bottom10players.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bottom10players.createSequentialGroup().addContainerGap()
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_bottom10players.createSequentialGroup()
										.addComponent(botHero4, GroupLayout.PREFERRED_SIZE, 119,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap(522, Short.MAX_VALUE))
						.addGroup(gl_bottom10players.createSequentialGroup().addGroup(gl_bottom10players
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_bottom10players.createSequentialGroup().addGroup(gl_bottom10players
										.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_bottom10players.createParallelGroup(Alignment.LEADING).addGroup(
												gl_bottom10players.createSequentialGroup().addGroup(gl_bottom10players
														.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_bottom10players
																.createParallelGroup(Alignment.LEADING)
																.addComponent(botHero10, GroupLayout.PREFERRED_SIZE,
																		119, GroupLayout.PREFERRED_SIZE)
																.addComponent(botHero9, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(botHero8, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(botHero7, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addGroup(gl_bottom10players.createSequentialGroup()
																		.addComponent(botHero2,
																				GroupLayout.DEFAULT_SIZE, 167,
																				Short.MAX_VALUE)
																		.addPreferredGap(ComponentPlacement.RELATED)))
														.addGroup(gl_bottom10players.createSequentialGroup()
																.addComponent(botHero1, GroupLayout.PREFERRED_SIZE, 119,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.RELATED)))
														.addGap(27))
												.addGroup(gl_bottom10players.createSequentialGroup()
														.addComponent(botHero5, GroupLayout.PREFERRED_SIZE, 119,
																GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED)))
										.addGroup(gl_bottom10players.createSequentialGroup()
												.addComponent(botHero6, GroupLayout.PREFERRED_SIZE, 119,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED))
										.addGroup(gl_bottom10players.createSequentialGroup()
												.addComponent(botHero3, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED)))
										.addGroup(
												gl_bottom10players.createParallelGroup(Alignment.TRAILING)
														.addGroup(gl_bottom10players
																.createParallelGroup(Alignment.LEADING, false)
																.addComponent(botHero10pcnt, GroupLayout.DEFAULT_SIZE,
																		94, Short.MAX_VALUE)
														.addComponent(botHero9pcnt, GroupLayout.DEFAULT_SIZE, 94,
																Short.MAX_VALUE)
														.addComponent(botHero7pcnt, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(botHero8pcnt, GroupLayout.DEFAULT_SIZE, 94,
																Short.MAX_VALUE)
														.addComponent(botHero6pcnt, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(botHero4pcnt, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(botHero5pcnt, GroupLayout.DEFAULT_SIZE, 127,
																Short.MAX_VALUE).addComponent(botHero3pcnt,
																		GroupLayout.PREFERRED_SIZE, 125,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														gl_bottom10players.createParallelGroup(Alignment.LEADING, false)
																.addComponent(botHero1pcnt, Alignment.TRAILING,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(botHero2pcnt, Alignment.TRAILING,
																GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
								.addGroup(gl_bottom10players.createSequentialGroup().addComponent(lblBottomHeroes)
										.addGap(92)))
								.addGap(301)))));
		gl_bottom10players.setVerticalGroup(gl_bottom10players.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_bottom10players.createSequentialGroup().addGap(45).addComponent(lblBottomHeroes).addGap(43)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero1pcnt)
								.addComponent(botHero1))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero2)
								.addComponent(botHero2pcnt))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero3pcnt)
								.addComponent(botHero3))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero4)
								.addComponent(botHero4pcnt))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero5pcnt)
								.addComponent(botHero5))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero6pcnt)
								.addComponent(botHero6))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero7pcnt)
								.addComponent(botHero7))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero8pcnt)
								.addComponent(botHero8))
						.addGap(18)
						.addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE).addComponent(botHero9pcnt)
								.addComponent(botHero9))
						.addGap(18).addGroup(gl_bottom10players.createParallelGroup(Alignment.BASELINE)
								.addComponent(botHero10pcnt).addComponent(botHero10))
						.addContainerGap()));
		bottom10panel.setLayout(gl_bottom10players);
		GroupLayout gl_statistics = new GroupLayout(statistics);
		gl_statistics.setHorizontalGroup(gl_statistics.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_statistics.createSequentialGroup().addContainerGap()
						.addGroup(gl_statistics.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnExecute, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
								.addGroup(gl_statistics.createSequentialGroup()
										.addGroup(gl_statistics.createParallelGroup(Alignment.LEADING)
												.addComponent(lblSteamid).addComponent(lblHero).addComponent(lblTeam))
								.addGap(47)
								.addGroup(gl_statistics.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(heroComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(steamUserNameComboBox, 0, 109, Short.MAX_VALUE)
										.addGroup(gl_statistics.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(teamComboBox,
														GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)))))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_statistics.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_statistics.createSequentialGroup().addComponent(KPIpanel, 0, 386, Short.MAX_VALUE)
								.addGap(2).addComponent(top10panel, GroupLayout.PREFERRED_SIZE, 314, Short.MAX_VALUE)
								.addGap(3).addComponent(bottom10panel, GroupLayout.PREFERRED_SIZE, 297, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED))
						.addComponent(factsScroll, GroupLayout.DEFAULT_SIZE, 1587, Short.MAX_VALUE)).addGap(3)));
		gl_statistics.setVerticalGroup(gl_statistics.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_statistics.createSequentialGroup()
						.addGroup(gl_statistics.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_statistics.createSequentialGroup().addGap(19)
										.addGroup(gl_statistics.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblSteamid).addComponent(steamUserNameComboBox,
														GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_statistics.createParallelGroup(Alignment.TRAILING)
										.addComponent(heroComboBox, GroupLayout.PREFERRED_SIZE, 20,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblHero))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_statistics.createParallelGroup(Alignment.TRAILING).addComponent(lblTeam)
										.addComponent(teamComboBox, GroupLayout.PREFERRED_SIZE, 20,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnExecute, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_statistics.createSequentialGroup().addContainerGap().addComponent(factsScroll,
								GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_statistics.createParallelGroup(Alignment.BASELINE)
						.addComponent(KPIpanel, GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
						.addComponent(top10panel, GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
						.addComponent(bottom10panel, GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE))));

		lblUserHeroWinrate = new JLabel("User/Hero winrate :");
		lblUserWinrate = new JLabel("User total winrate :");
		lblHeroWinrate = new JLabel("Hero total winrate :");
		lblUserHeroWinrateValue = new JLabel("Value1");
		lblUserHeroWinrateValue.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUserWinrateValue = new JLabel("Value2");
		lblUserWinrateValue.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblHeroWinrateValue = new JLabel("Value3");
		lblHeroWinrateValue.setFont(new Font("Tahoma", Font.BOLD, 14));

		GroupLayout gl_KPIpanel = new GroupLayout(KPIpanel);
		gl_KPIpanel.setHorizontalGroup(gl_KPIpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_KPIpanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_KPIpanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblUserHeroWinrate, GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
								.addComponent(lblUserWinrate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(lblHeroWinrate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addGap(28)
				.addGroup(gl_KPIpanel.createParallelGroup(Alignment.TRAILING).addComponent(lblUserWinrateValue)
						.addComponent(lblUserHeroWinrateValue).addComponent(lblHeroWinrateValue))
				.addContainerGap(438, Short.MAX_VALUE)));
		gl_KPIpanel.setVerticalGroup(gl_KPIpanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_KPIpanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_KPIpanel.createParallelGroup(Alignment.BASELINE).addComponent(lblUserHeroWinrate)
								.addComponent(lblUserHeroWinrateValue))
				.addGap(18)
				.addGroup(gl_KPIpanel.createParallelGroup(Alignment.BASELINE).addComponent(lblUserWinrate)
						.addComponent(lblUserWinrateValue)).addGap(18)
				.addGroup(gl_KPIpanel.createParallelGroup(Alignment.BASELINE).addComponent(lblHeroWinrate)
						.addComponent(lblHeroWinrateValue)).addContainerGap(352, Short.MAX_VALUE)));
		KPIpanel.setLayout(gl_KPIpanel);

		factsTable = new JTable();
		factsTable.setAlignmentX(Component.RIGHT_ALIGNMENT);
		factsTable.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		factsTable.setFillsViewportHeight(true);
		factsScroll.setViewportView(factsTable);

		statistics.setLayout(gl_statistics);

		visuals = new JPanel();
		visuals.setBackground(Color.WHITE);
		visuals.setAlignmentY(Component.TOP_ALIGNMENT);
		visuals.setAlignmentX(Component.LEFT_ALIGNMENT);
		visuals.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab("Visuals", null, visuals, "Graphical dataanalysis");

		lblSelectLobbyType = new JLabel("Select lobby type");
		lblSelectLobbyType.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSelectTeam = new JLabel("Select faction");
		lblSelectTeam.setFont(new Font("Tahoma", Font.BOLD, 13));

		btnDone = new JButton("Done");
		btnDone.setFont(new Font("Tahoma", Font.BOLD, 15));

		factionComboBox = new JComboBox<String>();
		factionComboBox.addItem("Both");
		factionComboBox.addItem("Radiant");
		factionComboBox.addItem("Dire");

		lobbyTypeComboBox = new JComboBox<String>();
		lobbyTypeComboBox.addItem("All");
		lobbyTypeComboBox.addItem("Ranked");
		lobbyTypeComboBox.addItem("Normal");

		chartsPanel = new JPanel();
		chartsPanel.setBorder(new CompoundBorder());
		chartsPanel.setBackground(Color.WHITE);
		chartsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		chartsPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		chartsPanel.setLayout(null);

		JPanel piePanel = new JPanel();
		piePanel.setBackground(Color.WHITE);
		piePanel.setBounds(0, 0, 707, 462);
		chartsPanel.add(piePanel);
		fxpiePanel = new JFXPanel();
		piePanel.add(fxpiePanel);

		JPanel barPanel = new JPanel();
		barPanel.setBackground(Color.WHITE);
		barPanel.setBounds(705, 0, 993, 462);
		chartsPanel.add(barPanel);
		fxbarPanel = new JFXPanel();
		barPanel.add(fxbarPanel);

		JPanel linePanel = new JPanel();
		linePanel.setBackground(Color.WHITE);
		linePanel.setBounds(0, 462, 1698, 492);
		chartsPanel.add(linePanel);
		fxlinePanel = new JFXPanel();
		linePanel.add(fxlinePanel);

		GroupLayout gl_visuals = new GroupLayout(visuals);
		gl_visuals.setHorizontalGroup(gl_visuals.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_visuals.createSequentialGroup().addContainerGap()
						.addGroup(gl_visuals.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_visuals.createSequentialGroup()
										.addGroup(gl_visuals.createParallelGroup(Alignment.LEADING, false)
												.addComponent(factionComboBox, 0, 142, Short.MAX_VALUE)
												.addComponent(lblSelectLobbyType, GroupLayout.PREFERRED_SIZE, 142,
														GroupLayout.PREFERRED_SIZE)
								.addComponent(btnDone, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lobbyTypeComboBox, Alignment.TRAILING, 0, 142, Short.MAX_VALUE))
								.addGap(12))
						.addGroup(gl_visuals.createSequentialGroup().addComponent(lblSelectTeam)
								.addPreferredGap(ComponentPlacement.RELATED)))
				.addComponent(chartsPanel, GroupLayout.DEFAULT_SIZE, 1721, Short.MAX_VALUE).addContainerGap()));
		gl_visuals
				.setVerticalGroup(gl_visuals.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_visuals
								.createSequentialGroup().addGap(
										16)
								.addGroup(gl_visuals.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_visuals.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblSelectTeam)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(factionComboBox, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(18).addComponent(lblSelectLobbyType)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(lobbyTypeComboBox, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(18).addComponent(btnDone, GroupLayout.PREFERRED_SIZE, 39,
														GroupLayout.PREFERRED_SIZE))
						.addComponent(chartsPanel, GroupLayout.PREFERRED_SIZE, 944, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		visuals.setLayout(gl_visuals);

		export = new JPanel();
		export.setBackground(Color.WHITE);
		export.setAlignmentY(Component.TOP_ALIGNMENT);
		export.setAlignmentX(Component.LEFT_ALIGNMENT);
		export.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		tabbedPane.addTab("Export", null, export, "Export data as CSV-file");

		chooseExportPlayerComboBox = new JComboBox<String>();

		exportFileName = new JTextField("output_example_file_name");
		exportFileName.setColumns(10);

		btnExecuteCSVExport = new JButton("Export Data");
		btnExecuteCSVExport.setFont(new Font("Tahoma", Font.BOLD, 15));

		imagePanel = new JPanel();
		imagePanel.setBackground(Color.WHITE);
		imageLabel = new JLabel();
		ImageIcon dotaImage = new ImageIcon(
				new ImageIcon("res/Dota2.jpg").getImage().getScaledInstance(1300, 821, Image.SCALE_DEFAULT));
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		imageLabel.setIcon(dotaImage);

		lblExportCSV = new JLabel("Export CSV");
		lblExportCSV.setFont(new Font("Tahoma", Font.BOLD, 14));

		rdbtnUserSpecificData = new JRadioButton("User specific data");
		rdbtnUserSpecificData.setBackground(Color.WHITE);
		buttonGroup.add(rdbtnUserSpecificData);

		rdbtnAllData = new JRadioButton("All data");
		rdbtnAllData.setBackground(Color.WHITE);
		buttonGroup.add(rdbtnAllData);

		cryptionKey = new JTextField("Type desired password");
		cryptionKey.setColumns(10);

		tglbtnEncrypted = new JToggleButton("Status: Encrypted");
		tglbtnEncrypted.setSelected(true);

		btnDecryptData = new JButton("Decrypt Data");
		btnDecryptData.setToolTipText("Remember to set filename in the field above (same as for export data)");
		btnDecryptData.setFont(new Font("Tahoma", Font.BOLD, 14));

		btnChooseFileToDecrypt = new JButton("Choose File");

		separator = new JSeparator();
		separator.setForeground(Color.BLACK);

		chosenFile = new JTextField();
		chosenFile.setColumns(10);

		decryptFileName = new JTextField("output_example_file_name");
		decryptFileName.setColumns(10);

		lblDecryptCSV = new JLabel("Decrypt CSV");
		lblDecryptCSV.setFont(new Font("Tahoma", Font.BOLD, 14));

		decryptionKey = new JTextField("Type desired password");
		decryptionKey.setColumns(10);

		GroupLayout gl_export = new GroupLayout(export);
		gl_export.setHorizontalGroup(gl_export.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_export.createSequentialGroup().addContainerGap()
						.addGroup(gl_export.createParallelGroup(Alignment.LEADING)
								.addComponent(separator, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
								.addComponent(lblDecryptCSV, GroupLayout.PREFERRED_SIZE, 190,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(decryptFileName, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_export.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnExecuteCSVExport, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(cryptionKey, Alignment.LEADING)
								.addComponent(tglbtnEncrypted, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(exportFileName, Alignment.LEADING)
								.addComponent(chooseExportPlayerComboBox, Alignment.LEADING, 0,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblExportCSV, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 190,
										Short.MAX_VALUE)
								.addComponent(rdbtnUserSpecificData, Alignment.LEADING)
								.addComponent(rdbtnAllData, Alignment.LEADING))
						.addGroup(gl_export.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnDecryptData, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(chosenFile, Alignment.LEADING)
								.addComponent(btnChooseFileToDecrypt, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(decryptionKey, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 190,
										Short.MAX_VALUE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(imagePanel, GroupLayout.PREFERRED_SIZE, 1629, GroupLayout.PREFERRED_SIZE)
						.addGap(40)));
		gl_export.setVerticalGroup(gl_export.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_export.createSequentialGroup().addGap(32)
						.addGroup(gl_export.createParallelGroup(Alignment.LEADING)
								.addComponent(imagePanel, GroupLayout.PREFERRED_SIZE, 915, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_export.createSequentialGroup()
										.addComponent(lblExportCSV, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(chooseExportPlayerComboBox, GroupLayout.PREFERRED_SIZE, 31,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(exportFileName, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(rdbtnUserSpecificData)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(rdbtnAllData)
										.addGap(18).addComponent(tglbtnEncrypted).addGap(18)
										.addComponent(cryptionKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btnExecuteCSVExport, GroupLayout.PREFERRED_SIZE, 38,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(lblDecryptCSV, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(decryptFileName, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(decryptionKey, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(btnChooseFileToDecrypt, GroupLayout.PREFERRED_SIZE, 35,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(chosenFile, GroupLayout.PREFERRED_SIZE, 32,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(btnDecryptData, GroupLayout.PREFERRED_SIZE, 40,
												GroupLayout.PREFERRED_SIZE)))
						.addGap(33)));

		imagePanel.add(imageLabel);
		export.setLayout(gl_export);
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(tabbedPane);
	}

	// Getdata-tabb-methods
	public void SetGetDataConsoleText(String output) {
		getDataConsole.append(output);
		getDataConsole.setCaretPosition(getDataConsole.getDocument().getLength());
	}

	public void setProgressBar(double progress) {
		int prog = (int) Math.round(progress * 100);
		progressBar.setValue(prog);
		progressBar.setString(prog + " %");
	}

	public void setProgressLbl(String str) {
		lblProgress.setText(str);
	}

	public void disableGetDataBtn() {
		btnGetData.setEnabled(false);
	}

	public void enableGetDataBtn() {
		btnGetData.setEnabled(true);
	}

	public String getSteamIDText() {
		return steamIDText.getText();
	}

	public void setWaitCursor() {
		tabbedPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void setDefaultCursor() {
		tabbedPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	// Statistics-tabb-methods
	public String getUserNameComboBox() {
		return steamUserNameComboBox.getSelectedItem().toString();
	}

	public String getHeroComboBox() {
		return heroComboBox.getSelectedItem().toString();
	}

	public String getTeamComboBox() {
		return teamComboBox.getSelectedItem().toString();
	}

	public void setSteamNameCombobox(ArrayList<String> steamNameList) {
		steamUserNameComboBox.removeAllItems();
		for (String s : steamNameList) {
			steamUserNameComboBox.addItem(s);
		}
	}

	public void setStatsHeroSelectionComboBox(ArrayList<String> heroList) {
		heroComboBox.removeAllItems();
		for (String s : heroList) {
			heroComboBox.addItem(s);
		}
	}

	public void setFactsTable(Object[][] data, Object[] colHeaders) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		tm = new DefaultTableModel(data, colHeaders);
		factsTable.setModel(tm);
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		factsTable.setDefaultRenderer(Object.class, centerRenderer);
	}

	public void setLblUserHeroWinRatioValue(String value) {
		this.lblUserHeroWinrateValue.setText(value + " %");
	}

	public void setLblHeroWinrateValue(String value) {
		this.lblHeroWinrateValue.setText(value + " %");
	}

	public void setLblUserWinrateValue(String value) {
		this.lblUserWinrateValue.setText(value + " %");
	}

	public void setTop10Heroes(ArrayList<HeroWinrate> best10PHeroes) {

		try {
			topHero1.setText(best10PHeroes.get(0).getHeroName());
			topHero1pcnt.setText(best10PHeroes.get(0).getWinRate());
			topHero2.setText(best10PHeroes.get(1).getHeroName());
			topHero2pcnt.setText(best10PHeroes.get(1).getWinRate());
			topHero3.setText(best10PHeroes.get(2).getHeroName());
			topHero3pcnt.setText(best10PHeroes.get(2).getWinRate());
			topHero4.setText(best10PHeroes.get(3).getHeroName());
			topHero4pcnt.setText(best10PHeroes.get(3).getWinRate());
			topHero5.setText(best10PHeroes.get(4).getHeroName());
			topHero5pcnt.setText(best10PHeroes.get(4).getWinRate());
			topHero6.setText(best10PHeroes.get(5).getHeroName());
			topHero6pcnt.setText(best10PHeroes.get(5).getWinRate());
			topHero7.setText(best10PHeroes.get(6).getHeroName());
			topHero7pcnt.setText(best10PHeroes.get(6).getWinRate());
			topHero8.setText(best10PHeroes.get(7).getHeroName());
			topHero8pcnt.setText(best10PHeroes.get(7).getWinRate());
			topHero9.setText(best10PHeroes.get(8).getHeroName());
			topHero9pcnt.setText(best10PHeroes.get(8).getWinRate());
			topHero10.setText(best10PHeroes.get(9).getHeroName());
			topHero10pcnt.setText(best10PHeroes.get(9).getWinRate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBottom10Heroes(ArrayList<HeroWinrate> worst10Heroes) {

		try {
			botHero1.setText(worst10Heroes.get(0).getHeroName());
			botHero1pcnt.setText(worst10Heroes.get(0).getWinRate());
			botHero2.setText(worst10Heroes.get(1).getHeroName());
			botHero2pcnt.setText(worst10Heroes.get(1).getWinRate());
			botHero3.setText(worst10Heroes.get(2).getHeroName());
			botHero3pcnt.setText(worst10Heroes.get(2).getWinRate());
			botHero4.setText(worst10Heroes.get(3).getHeroName());
			botHero4pcnt.setText(worst10Heroes.get(3).getWinRate());
			botHero5.setText(worst10Heroes.get(4).getHeroName());
			botHero5pcnt.setText(worst10Heroes.get(4).getWinRate());
			botHero6.setText(worst10Heroes.get(5).getHeroName());
			botHero6pcnt.setText(worst10Heroes.get(5).getWinRate());
			botHero7.setText(worst10Heroes.get(6).getHeroName());
			botHero7pcnt.setText(worst10Heroes.get(6).getWinRate());
			botHero8.setText(worst10Heroes.get(7).getHeroName());
			botHero8pcnt.setText(worst10Heroes.get(7).getWinRate());
			botHero9.setText(worst10Heroes.get(8).getHeroName());
			botHero9pcnt.setText(worst10Heroes.get(8).getWinRate());
			botHero10.setText(worst10Heroes.get(9).getHeroName());
			botHero10pcnt.setText(worst10Heroes.get(9).getWinRate());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Metoder som aktiverar/deaktiverar tabbar
	public void enableTabs(boolean enabled) {
		tabbedPane.setEnabledAt(1, enabled);
		tabbedPane.setEnabledAt(2, enabled);
		tabbedPane.setEnabledAt(3, enabled);
	}

	// Graph-tabb-methods
	public String showSteamIDDialog(ArrayList<String> steamIDs) {
		String s = (String) JOptionPane.showInputDialog(new JFrame(), "Choose one of the stored SteamID:",
				"SteamID Dialog", JOptionPane.PLAIN_MESSAGE, null, steamIDs.toArray(), steamIDs.toArray()[0]);
		return s;
	}

	public String getChartFactionComboBox() {
		return factionComboBox.getSelectedItem().toString();
	}

	public String getChartLobbyTypeComboBox() {
		return lobbyTypeComboBox.getSelectedItem().toString();
	}

	public void runPieChartJFX(final Map<String, Integer> pieChartData) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initPieChartFX(fxpiePanel, pieChartData);
			}
		});
	}

	public void initPieChartFX(JFXPanel panel, Map<String, Integer> pieChartData) {
		Scene scene = createScene(pieChartData);
		panel.setScene(scene);
	}

	public Scene createScene(Map<String, Integer> pieChartData) {
		Group root = new Group();
		Scene scene = new Scene(root);
		ObservableList<PieChart.Data> pieChartOBList = createObservableList(pieChartData);
		final PieChart pieChart = new PieChart(pieChartOBList);
		pieChart.setTitle("10 Most Played Heroes");
		pieChart.setPrefWidth(chartsPanel.getWidth() / 3);
		pieChart.setPrefHeight(chartsPanel.getHeight() / 2.2);
		pieChart.setStartAngle(180.0);
		root.getChildren().add(pieChart);

		return (scene);
	}

	public ObservableList<PieChart.Data> createObservableList(Map<String, Integer> pieChartData) {
		ObservableList<PieChart.Data> list = FXCollections.observableList(new ArrayList<PieChart.Data>());
		for (Map.Entry<String, Integer> entry : pieChartData.entrySet()) {
			String heroName = entry.getKey();
			Integer heroCount = entry.getValue();
			list.add(new PieChart.Data(heroName, heroCount));
		}

		return list;
	}

	public void runBarChartJFX(final Map<String, Number> barChartData) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initBarChartFX(fxbarPanel, barChartData);
			}
		});
	}

	public void initBarChartFX(JFXPanel panel, Map<String, Number> barChartData) {
		Scene scene = createBarChartScene(barChartData);
		panel.setScene(scene);
	}

	public Scene createBarChartScene(Map<String, Number> barChartData) {
		Group root = new Group();
		Scene scene = new Scene(root);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
		bc.setTitle("Winrate Per 10 Most Played Heroes");
		xAxis.setLabel("Hero");
		yAxis.setLabel("Winrate");

		XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();

		for (Map.Entry<String, Number> entry : barChartData.entrySet()) {
			data.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
		}

		bc.getData().add(data);
		bc.setPrefWidth(chartsPanel.getWidth() / 2);
		bc.setPrefHeight(chartsPanel.getHeight() / 2.2);
		root.getChildren().add(bc);
		return scene;
	}

	public void runLineChartJFX(final ArrayList<LineChartData> lineChartArray) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initLineChartFX(fxlinePanel, lineChartArray);
			}
		});
	}

	public void initLineChartFX(JFXPanel panel, ArrayList<LineChartData> lineChartArray) {
		Scene scene = createLineChartScene(lineChartArray);
		panel.setScene(scene);
	}

	public Scene createLineChartScene(ArrayList<LineChartData> lineChartArray) {
		Group root = new Group();
		Scene scene = new Scene(root);

		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Matches");
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("GPM/XPM");

		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setTitle("GPM & XPM");
		XYChart.Series gpmSeries = new XYChart.Series<>();
		gpmSeries.setName("GPM");
		XYChart.Series xpmSeries = new XYChart.Series<>();
		xpmSeries.setName("XPM");

		int i = 1;
		for (LineChartData s : lineChartArray) {
			gpmSeries.getData().add(new XYChart.Data<>(Integer.toString(i), s.getGpm()));
			xpmSeries.getData().add(new XYChart.Data<>(Integer.toString(i), s.getXpm()));
			i++;
		}

		lineChart.getData().addAll(gpmSeries, xpmSeries);
		lineChart.setPrefWidth(chartsPanel.getWidth());
		root.getChildren().add(lineChart);
		return scene;
	}

	// ExportCSV-tabb-methods
	public boolean getTglBtnState() {
		return tglbtnEncrypted.isSelected();
	}

	public void setTglBtn(boolean isSelected) {

		if (isSelected) {
			tglbtnEncrypted.setText("Status: Encrypted");
			cryptionKey.setText("Type desired password");
		} else {
			tglbtnEncrypted.setText("Not Encrypted");
			cryptionKey.setText("");
		}
	}

	public String getChosenPassWord() {
		return cryptionKey.getText().toString();
	}

	public void deactivatePassWordField() {
		cryptionKey.setEnabled(false);
	}

	public void activatePassWordField() {
		cryptionKey.setEnabled(true);
	}

	public boolean isRdbtnUserSpecificData() {
		return rdbtnUserSpecificData.isSelected();
	}

	public boolean isRdbtnAllData() {
		return rdbtnAllData.isSelected();
	}

	public String getChooseExportPlayerComboBox() {
		return chooseExportPlayerComboBox.getSelectedItem().toString();
	}

	public void setChooseExportPlayerComboBox(ArrayList<String> storedPlayers) {
		chooseExportPlayerComboBox.removeAllItems();
		for (String s : storedPlayers) {
			chooseExportPlayerComboBox.addItem(s);
		}
	}

	public String getCSVName() {
		return exportFileName.getText();
	}

	public String getDecryptedCSVName() {
		return decryptFileName.getText();
	}

	public String getDecryptPassWord() {
		return decryptionKey.getText().toString();
	}

	public String selectDestinationFolder() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = chooser.showSaveDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	public String selectFileToDecrypt() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setApproveButtonText("Select");
		int option = chooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			System.out.println(chooser.getSelectedFile().getAbsolutePath());
			return chooser.getSelectedFile().getAbsolutePath();
		}
		return null;
	}

	public void setChosenPath(String path) {
		chosenFile.setText(path);
	}

	public String getChosenPath() {
		return chosenFile.getText().toString();
	}

	// Listeners for interface objects
	public void addAPICallListener(ActionListener listenForAPICallButton) {
		btnGetData.addActionListener(listenForAPICallButton);
	}

	public void addStatQueryListener(ActionListener listenForExecuteButton) {
		btnExecute.addActionListener(listenForExecuteButton);
	}

	public void addJFXListener(ActionListener listenForDoneButton) {
		btnDone.addActionListener(listenForDoneButton);
	}

	public void addTglBtnListener(ItemListener listenForTglBtn) {
		tglbtnEncrypted.addItemListener(listenForTglBtn);
	}

	public void addExportDataListener(ActionListener listenforExportDataButton) {
		btnExecuteCSVExport.addActionListener(listenforExportDataButton);
	}

	public void addChooseDecryptFileListener(ActionListener listenForChooseDecryptFileListener) {
		btnChooseFileToDecrypt.addActionListener(listenForChooseDecryptFileListener);
	}

	public void addDecryptAndExportListener(ActionListener listenForDecryptAndExportButton) {
		btnDecryptData.addActionListener(listenForDecryptAndExportButton);
	}
}
