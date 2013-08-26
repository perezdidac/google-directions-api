package com.perezdidac.googledirectionsapi;

import java.util.List;

public interface RouterTaskListener {

	public void onRoutesReceived(List<Route> routes);
}
