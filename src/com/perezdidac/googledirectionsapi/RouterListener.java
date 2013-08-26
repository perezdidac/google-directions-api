package com.perezdidac.googledirectionsapi;

import java.util.List;

import com.perezdidac.googledirectionsapi.routes.Route;

public interface RouterListener {
	
	public void onRoutesReceived(List<Route> routes);
	public void onRoutesError();

}
