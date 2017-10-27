package api.dota2.model;


/**
 * 
 * Players-klassen används för att spara data som hämtats från APIn.
 *
 */
public class Players
{
	private boolean radiant_win;

	private String gold_per_min;

    private String player_slot;

    private String assists;

    private String account_id;

    private String tower_damage;

    private String last_hits;

    private String hero_id;

    private String gold_spent;

    private String hero_damage;

    private String level;

    private String xp_per_min;

    private String item_1;

    private String item_0;

    private String item_3;

    private String deaths;

    private String hero_healing;

    private String item_2;

    private String item_5;

    private String leaver_status;

    private String item_4;

    private String kills;

    private String gold;

    private String denies;
    
    private String match_id;

    public String getMatch_id() {
		return match_id;
	}

	public void setMatch_id(String match_id) {
		this.match_id = match_id;
	}

	public String getGold_per_min ()
    {
        return gold_per_min;
    }

    public void setGold_per_min (String gold_per_min)
    {
        this.gold_per_min = gold_per_min;
    }

    public String getPlayer_slot ()
    {
        return player_slot;
    }

    public void setPlayer_slot (String player_slot)
    {
        this.player_slot = player_slot;
    }

    public String getAssists ()
    {
        return assists;
    }

    public void setAssists (String assists)
    {
        this.assists = assists;
    }

    public String getAccount_id ()
    {
        return account_id;
    }

    public void setAccount_id (String account_id)
    {
        this.account_id = account_id;
    }

    public String getTower_damage ()
    {
        return tower_damage;
    }

    public void setTower_damage (String tower_damage)
    {
        this.tower_damage = tower_damage;
    }

    public String getLast_hits ()
    {
        return last_hits;
    }

    public void setLast_hits (String last_hits)
    {
        this.last_hits = last_hits;
    }

    public String getHero_id ()
    {
        return hero_id;
    }

    public void setHero_id (String hero_id)
    {
        this.hero_id = hero_id;
    }

    public String getGold_spent ()
    {
        return gold_spent;
    }

    public void setGold_spent (String gold_spent)
    {
        this.gold_spent = gold_spent;
    }

    public String getHero_damage ()
    {
        return hero_damage;
    }

    public void setHero_damage (String hero_damage)
    {
        this.hero_damage = hero_damage;
    }

    public String getLevel ()
    {
        return level;
    }

    public void setLevel (String level)
    {
        this.level = level;
    }

    public String getXp_per_min ()
    {
        return xp_per_min;
    }

    public void setXp_per_min (String xp_per_min)
    {
        this.xp_per_min = xp_per_min;
    }

    public String getItem_1 ()
    {
        return item_1;
    }

    public void setItem_1 (String item_1)
    {
        this.item_1 = item_1;
    }

    public String getItem_0 ()
    {
        return item_0;
    }

    public void setItem_0 (String item_0)
    {
        this.item_0 = item_0;
    }

    public String getItem_3 ()
    {
        return item_3;
    }

    public void setItem_3 (String item_3)
    {
        this.item_3 = item_3;
    }

    public String getDeaths ()
    {
        return deaths;
    }

    public void setDeaths (String deaths)
    {
        this.deaths = deaths;
    }

    public String getHero_healing ()
    {
        return hero_healing;
    }

    public void setHero_healing (String hero_healing)
    {
        this.hero_healing = hero_healing;
    }

    public String getItem_2 ()
    {
        return item_2;
    }

    public void setItem_2 (String item_2)
    {
        this.item_2 = item_2;
    }

    public String getItem_5 ()
    {
        return item_5;
    }

    public void setItem_5 (String item_5)
    {
        this.item_5 = item_5;
    }

    public String getLeaver_status ()
    {
        return leaver_status;
    }

    public void setLeaver_status (String leaver_status)
    {
        this.leaver_status = leaver_status;
    }

    public String getItem_4 ()
    {
        return item_4;
    }

    public void setItem_4 (String item_4)
    {
        this.item_4 = item_4;
    }

    public String getKills ()
    {
        return kills;
    }

    public void setKills (String kills)
    {
        this.kills = kills;
    }

    public String getGold ()
    {
        return gold;
    }

    public void setGold (String gold)
    {
        this.gold = gold;
    }

    public String getDenies ()
    {
        return denies;
    }

    public void setDenies (String denies)
    {
        this.denies = denies;
    }
    
    public boolean isWin() {
		return radiant_win;
	}

	public void setWin(boolean win) {
		this.radiant_win = win;
	}

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [gold_per_min = "+gold_per_min+", player_slot = "+player_slot+", assists = "+assists+", account_id = "+account_id+", tower_damage = "+tower_damage+", last_hits = "+last_hits+", hero_id = "+hero_id+", gold_spent = "+gold_spent+", hero_damage = "+hero_damage+", level = "+level+", xp_per_min = "+xp_per_min+", item_1 = "+item_1+", item_0 = "+item_0+", item_3 = "+item_3+", deaths = "+deaths+", hero_healing = "+hero_healing+", item_2 = "+item_2+", item_5 = "+item_5+", leaver_status = "+leaver_status+", item_4 = "+item_4+", kills = "+kills+", gold = "+gold+", denies = "+denies+"]";
//    }
}
