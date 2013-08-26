package com.perezdidac.googledirectionsapi;

import java.util.List;

import android.os.AsyncTask;

public class RouterTask extends AsyncTask<RouteQuery, Void, RouterTaskResult> {

	private RouterTaskListener routerTaskListener;
	
	public RouterTask(RouterTaskListener routerTaskListener) {
		//
		this.routerTaskListener = routerTaskListener;
	}
	
	protected RouterTaskResult doInBackground(RouteQuery... params) {
		RouterTaskResult result = new RouterTaskResult();
		
		return result;
	}
	
	protected void onPostExecute(RouterTaskResult result) {
		List<Route> routes = result.getRoutes();
		routerTaskListener.onRoutesReceived(routes);
	}
	
}