package com.tenea.googledirectionsapi;

import java.util.List;

import com.tenea.googledirectionsapi.routes.Route;

public interface RouterTaskListener {

	public void onRoutesReceived(List<Route> routes);
}
