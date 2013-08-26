package com.perezdidac.googledirectionsapi.routes;

import java.util.List;

public class Polyline {

	private List<Coordinates> points;
	
	public Polyline(List<Coordinates> points) {
		this.setPoints(points);
	}

	public List<Coordinates> getPoints() {
		return points;
	}

	public void setPoints(List<Coordinates> points) {
		this.points = points;
	}
}
