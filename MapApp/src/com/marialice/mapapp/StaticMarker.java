package com.marialice.mapapp;
import com.google.android.gms.maps.model.LatLng;
/* 
 * This class creates the <Marker> object.
 * All fields from the database are introduces as variables
 * filled using the DatabaseContent class
 * or calculated in own methods.
 * 
 */

public class StaticMarker {

	private int id;
	private Double lat;
	private Double lon;
	private String category;
	private String title;
	private String description;
	private int rotation;
	private String anchor;
	private String infowinanchor;
	

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getSymbol() {
		int symbol = 0;
		if (category.equals("atm")) {
			symbol = R.drawable.poi_atm;
		} else if (category.equals("sport")) {
			symbol = R.drawable.poi_sport;
		} else {
			symbol = R.drawable.poi_action_bar3;
		}

		return symbol;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getInfowinanchor() {
		return infowinanchor;
	}

	public void setInfowinanchor(String infowinanchor) {
		this.infowinanchor = infowinanchor;
	}


}