package api.dota2.model;

/**
 * En klass som representerar ett hero-objekt.
 *
 */
public class Heroes {

	private String name;
	private String id;
	private String localized_name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocalizedName() {
		return localized_name;
	}

	public void setLocalizedName(String localizedName) {
		this.localized_name = localizedName;
	}

}