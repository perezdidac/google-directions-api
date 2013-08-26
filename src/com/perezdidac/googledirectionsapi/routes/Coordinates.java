package com.perezdidac.googledirectionsapi.routes;

public class Coordinates {

	private double latitude;
	private double longitude;
	
	public Coordinates(double latitude, double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
