package com.marialice.mapapp;

import com.google.android.gms.maps.model.LatLng;

public class WalkSights {

	private Double lat;
	private Double lon;
	

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}
	
	public LatLng getLatLng() {
		LatLng position = new LatLng(lat, lon);
		return position;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}


}