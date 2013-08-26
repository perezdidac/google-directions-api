package com.perezdidac.googledirectionsapi;

import java.util.List;

import com.perezdidac.googledirectionsapi.routes.Route;

public interface RouterTaskListener {

	public void onRoutesReceived(List<Route> routes);
}
