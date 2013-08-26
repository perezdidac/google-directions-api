package com.perezdidac.googledirectionsapi;

public class Bounds {

	private Coordinates northeast;
	private Coordinates southwest;
	
	public Bounds(Coordinates northeast, Coordinates southwest) {
		setNortheast(northeast);
		setSouthwest(southwest);
	}

	public Coordinates getNortheast() {
		return northeast;
	}
	
	public void setNortheast(Coordinates northeast) {
		this.northeast = northeast;
	}
	
	public Coordinates getSouthwest() {
		return southwest;
	}
	
	public void setSouthwest(Coordinates southwest) {
		this.southwest = southwest;
	}
	
}
