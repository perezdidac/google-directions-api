package com.perezdidac.googledirectionsapi;

import java.util.List;

import com.perezdidac.googledirectionsapi.routes.Route;

public class Router implements RouterTaskListener {
	
	private RouterListener routerListener;
	private RouterTask routerTask;
	
	public Router(RouterListener routerListener) {
		this.routerListener = routerListener;
	}
	
	public void query(RouteQuery routeQuery) {
		routerTask = new RouterTask(this);
		
		routerTask.execute(routeQuery);
	}

	@Override
	public void onRoutesReceived(List<Route> routes) {
		if (!routes.isEmpty()) {
			routerListener.onRoutesReceived(routes);
		} else {
			routerListener.onRoutesError();
		}
	}
	
}
