package com.perezdidac.googledirectionsapi;

import java.util.List;

public interface RouterListener {
	
	public void onRoutesReceived(List<Route> routes);
	public void onRoutesError();

}
