package com.perezdidac.googledirectionsapi;

import java.util.ArrayList;
import java.util.List;

import com.perezdidac.googledirectionsapi.routes.Location;

public class RouteQuery {

	private Location origin;
	private Location destination;
	private RouteQueryOptions routeQueryOptions;
	private List<Location> waypoints;

	public RouteQuery(Location origin, Location destination, RouteQueryOptions routeQueryOptions) {
		setOrigin(origin);
		setDestination(destination);
		setWaypoints(new ArrayList<Location>());
		setRouteQueryOptions(routeQueryOptions);
	}

	public RouteQuery(Location origin, Location destination,
			List<Location> waypoints, RouteQueryOptions routeQueryOptions) {
		setOrigin(origin);
		setDestination(destination);
		setWaypoints(waypoints);
		setRouteQueryOptions(routeQueryOptions);
	}

	public RouteQuery(List<Location> locations,
			RouteQueryOptions routeQueryOptions) {
		if (locations.size() > 1) {
			setOrigin(locations.get(0));
			setDestination(locations.get(1));
		}

		List<Location> waypoints = new ArrayList<Location>();
		for (int k = 2; k < locations.size(); ++k) {
			waypoints.add(locations.get(k));
		}

		setWaypoints(waypoints);
		setRouteQueryOptions(routeQueryOptions);
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

	public RouteQueryOptions getRouteQueryOptions() {
		return routeQueryOptions;
	}

	public void setRouteQueryOptions(RouteQueryOptions routeQueryOptions) {
		this.routeQueryOptions = routeQueryOptions;
	}

	public List<Location> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Location> waypoints) {
		this.waypoints = waypoints;
	}

}
