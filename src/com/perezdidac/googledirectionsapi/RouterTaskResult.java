package com.perezdidac.googledirectionsapi;

import java.util.ArrayList;
import java.util.List;

public class RouterTaskResult {

	private List<Route> routes;

	public RouterTaskResult() {
		routes = new ArrayList<Route>();
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void addRoute(Route route) {
		routes.add(route);
	}
}
