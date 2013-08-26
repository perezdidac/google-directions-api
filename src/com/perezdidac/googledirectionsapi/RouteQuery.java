package com.perezdidac.googledirectionsapi;

import java.util.List;

public class RouteQuery {
	
	private Location origin;
	private Location destination;
	private List<Location> waypoints;

	public RouteQuery(Location origin, Location destination) {
		setOrigin(origin);
		setDestination(destination);
	}

	public RouteQuery(Location origin, Location destination, List<Location> waypoints) {
		setOrigin(origin);
		setDestination(destination);
		setWaypoints(waypoints);
	}

	public RouteQuery(List<Location> locations) {
		if (locations.size() > 1) {
			origin = locations.get(0);
			destination = locations.get(1);
		}
		
		for (int k = 2; k <locations.size(); ++k) {
			waypoints.add(locations.get(k));
		}
	}

	public Location getOrigin() {
		return origin;
	}
	
	public void setOrigin(Location origin) {
		this.origin = origin;
	}
	
	public Location getDestination() {
		return destination;
	}
	
	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public List<Location> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Location> waypoints) {
		this.waypoints = waypoints;
	}
	
}
