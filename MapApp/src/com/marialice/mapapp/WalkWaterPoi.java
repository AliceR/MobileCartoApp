package com.marialice.mapapp;

import com.google.android.gms.maps.model.LatLng;

public class WalkWaterPoi {

	private int id;
	private Double lat;
	private Double lon;
	private String title;
	private String description;
	private String name;
	private String icon;
	private Boolean wifi;
	private Boolean sundays;
	private Boolean terrace;
	private Boolean calmplace;
	private Boolean nonsmoking;
	private Boolean touristclassic;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public LatLng getLatLng() {
		LatLng position = new LatLng(lat, lon);
		return position;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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