package com.perezdidac.googledirectionsapi.routes;

public class Location {

	private String name;
	private Coordinates coordinates;
	
	public Location(String name, Coordinates coordinates) {
		setName(name);
		setCoordinates(coordinates);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	
}
