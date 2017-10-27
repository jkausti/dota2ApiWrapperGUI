package api.dota2.model;

public class HeroWinrate {

	private String heroName;
	private String winRate;

	HeroWinrate(String heroName, String winRate) {
		this.heroName = heroName;
		this.winRate = winRate;
	}

	public String getHeroName() {
		return heroName;
	}

	public String getWinRate() {
		return winRate;
	}
}
