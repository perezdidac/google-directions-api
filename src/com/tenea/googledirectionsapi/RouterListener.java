package com.tenea.googledirectionsapi;

import java.util.List;

import com.tenea.googledirectionsapi.routes.Route;

public interface RouterListener {
	
	public void onRoutesReceived(List<Route> routes);
	public void onRoutesError();

}
