package com.marialice.mapapp;
/* 
 * This class creates the <Poi> object.
 * All fields from the database are introduces as variables
 * filled using the DatabaseContent class
 * or calculated in own methods.
 * 
 */
import com.google.android.gms.maps.model.LatLng;

public class Poi {

	private int id;
	private Double lat;
	private Double lon;
	private String category;
	private String title;
	private String description;
	private Boolean wifi;
	private Boolean sundays;
	private Boolean terrace;
	private Boolean calmplace;
	private Boolean nonsmoking;
	private Boolean touristclassic;
	

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LatLng getLatLng() {
		LatLng position = new LatLng(lat, lon);
		return position;
	}

	public String getNumber() {
		String number = Integer.toString(id);
		return number;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getSymbol() {
		int symbol = 0;
		if (category.equals("bar")) {
			symbol = R.drawable.poi_bar_shadow;
		} else if (category.equals("cafe")) {
			symbol = R.drawable.poi_cafe_shadow;
		} else if (category.equals("eat")) {
			symbol = R.drawable.poi_eat_shadow;
		} else if (category.equals("hidden")) {
			symbol = R.drawable.poi_hidden_shadow;
		} else if (category.equals("museum")) {
			symbol = R.drawable.poi_museum_shadow;
		} else if (category.equals("shopping")) {
			symbol = R.drawable.poi_shopping_shadow;
		} else if (category.equals("sightseeing")) {
			symbol = R.drawable.poi_sightseeing_shadow;
		} else {
			symbol = R.drawable.poi_action_bar3;
		}

		return symbol;
	}

	public String getCategoryName() {
		String categoryName;
		if (category.equals("bar")) {
			categoryName = "bar, club";
		} else if (category.equals("cafe")) {
			categoryName = "caf�, tea room";
		} else if (category.equals("eat")) {
			categoryName = "eat, drink";
		} else if (category.equals("hidden")) {
			categoryName = "hidden, chill out";
		} else if (category.equals("museum")) {
			categoryName = "museum, venue";
		} else if (category.equals("shopping")) {
			categoryName = "shopping";
		} else if (category.equals("sightseeing")) {
			categoryName = "sightseeing";
		} else {
			categoryName = "no category";
		}

		return categoryName;
	}

	public Boolean getWifi() {
		return wifi;
	}

	public void setWifi(Boolean wifi) {
		this.wifi = wifi;
	}

	public Boolean getSundays() {
		return sundays;
	}

	public void setSundays(Boolean sundays) {
		this.sundays = sundays;
	}

	public Boolean getTerrace() {
		return terrace;
	}

	public void setTerrace(Boolean terrace) {
		this.terrace = terrace;
	}

	public Boolean getCalmplace() {
		return calmplace;
	}

	public void setCalmplace(Boolean calmplace) {
		this.calmplace = calmplace;
	}

	public Boolean getNonsmoking() {
		return nonsmoking;
	}

	public void setNonsmoking(Boolean nonsmoking) {
		this.nonsmoking = nonsmoking;
	}

	public Boolean getTouristclassic() {
		return touristclassic;
	}

	public void setTouristclassic(Boolean touristclassic) {
		this.touristclassic = touristclassic;
	}


}