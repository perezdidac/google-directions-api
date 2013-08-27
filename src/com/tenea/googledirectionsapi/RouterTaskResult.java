package com.tenea.googledirectionsapi;

import java.util.ArrayList;
import java.util.List;

import com.tenea.googledirectionsapi.routes.Route;

public class RouterTaskResult {

	private List<Route> routes;

	public RouterTaskResult() {
		routes = new ArrayList<Route>();
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
}
