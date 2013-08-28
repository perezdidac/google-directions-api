/*
Copyright 2013 TENEA TECNOLOGÍAS. All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:
 
   1. Redistributions of source code must retain the above copyright notice, this list of
      conditions and the following disclaimer.
 
   2. Redistributions in binary form must reproduce the above copyright notice, this list
      of conditions and the following disclaimer in the documentation and/or other materials
      provided with the distribution.
 
THIS SOFTWARE IS PROVIDED BY TENEA TECNOLOGÍAS ''AS IS'' AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL TENEA TECNOLOGÍAS OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
The views and conclusions contained in the software and documentation are those of the
authors and should not be interpreted as representing official policies, either expressed
or implied, of TENEA TECNOLOGÍAS.
*/

package com.tenea.googledirectionsapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.tenea.googledirectionsapi.routes.Bounds;
import com.tenea.googledirectionsapi.routes.Coordinates;
import com.tenea.googledirectionsapi.routes.Distance;
import com.tenea.googledirectionsapi.routes.Duration;
import com.tenea.googledirectionsapi.routes.Leg;
import com.tenea.googledirectionsapi.routes.Location;
import com.tenea.googledirectionsapi.routes.Polyline;
import com.tenea.googledirectionsapi.routes.Route;
import com.tenea.googledirectionsapi.routes.Step;
import com.tenea.googledirectionsapi.routes.Step.TravelMode;

public class RouterTask extends AsyncTask<RouteQuery, Void, RouterTaskResult> {

	private static final String DIRECTIONS_URL = "http://maps.googleapis.com/maps/api/directions/json?";

	private RouterTaskListener routerTaskListener;

	public RouterTask(RouterTaskListener routerTaskListener) {
		//
		this.routerTaskListener = routerTaskListener;
	}

	protected RouterTaskResult doInBackground(RouteQuery... params) {
		RouterTaskResult result = new RouterTaskResult();

		RouteQuery routeQuery = params[0];

		// Build the URL
		String url = buildUrl(routeQuery);

		try {
			// Perform the GET
			String response = get(url);

			// No error during the get operation, so let's analyze
			// the content of the response
			List<Route> routes = getRoutes(response);

			// Set the routes
			result.setRoutes(routes);
		} catch (Exception e) {
			// Some error has occurred
			result.setRoutes(new ArrayList<Route>());
		}

		return result;
	}

	protected void onPostExecute(RouterTaskResult result) {
		List<Route> routes = result.getRoutes();
		routerTaskListener.onRoutesReceived(routes);
	}

	private String buildUrl(RouteQuery routeQuery) {
		Location origin = routeQuery.getOrigin();
		Location destination = routeQuery.getDestination();
		RouteQueryOptions routeQueryOptions = routeQuery.getRouteQueryOptions();
		
		// TODO: implement waypoints
		@SuppressWarnings("unused")
		List<Location> waypoints = routeQuery.getWaypoints();

		String url = DIRECTIONS_URL;

		// Set origin coordinates
		url += "origin=";
		url += origin.getCoordinates().getLatitude();
		url += ',';
		url += origin.getCoordinates().getLongitude();

		// Set destination coordinates
		url += "&destination=";
		url += destination.getCoordinates().getLatitude();
		url += ',';
		url += destination.getCoordinates().getLongitude();

		// Set waypoints
		if (!waypoints.isEmpty()) {
			url += "&waypoints=";

			// Check if route must be optimized
			if (routeQueryOptions.isOptimized()) {
				url += "optimize:true%7C";
			}
			
			// Loop through the waypoints
			for (int k = 0; k < waypoints.size(); ++k) {
				Location waypoint = waypoints.get(k);
				url += waypoint.getCoordinates().getLatitude();
				url += ',';
				url += waypoint.getCoordinates().getLongitude();
				
				if (k + 1 < waypoints.size()) {
					url += "%7C";
				}
			}
		}
		
		// Set route options
		// TODO: set route options
		url += "&sensor=true&mode=driving";

		return url;
	}

	private List<Route> getRoutes(String response) {
		List<Route> routes = new ArrayList<Route>();

		// Parse the received JSON data
		try {
			// Build the JSON object
			JSONObject json = new JSONObject(response);

			// Get the array of routes
			JSONArray jsonRoutes = json.getJSONArray("routes");

			// Loop through the full list of routes
			for (int k = 0; k < jsonRoutes.length(); ++k) {
				Route route = new Route();

				// Get the route object
				JSONObject jsonRoute = jsonRoutes.getJSONObject(k);

				// Get bounds
				Bounds bounds = getBounds(jsonRoute);
				route.setBounds(bounds);

				// Get copyrights
				String copyrights = jsonRoute.getString("copyrights");
				route.setCopyrights(copyrights);

				// Get legs and leg contents
				List<Leg> legs = getLegs(jsonRoute);
				route.setLegs(legs);

				// Finally, add the route
				routes.add(route);
			}
		} catch (JSONException exception) {

		}

		return routes;
	}

	private Bounds getBounds(JSONObject jsonRoute) throws JSONException {
		// Parse bounds
		JSONObject jsonBounds = jsonRoute.getJSONObject("bounds");
		JSONObject jsonBoundsNortheast = jsonBounds.getJSONObject("northeast");
		JSONObject jsonBoundsSouthwest = jsonBounds.getJSONObject("southwest");
		Coordinates boundsNortheast = new Coordinates(
				jsonBoundsNortheast.getDouble("lat"),
				jsonBoundsNortheast.getDouble("lng"));
		Coordinates boundsSouthwest = new Coordinates(
				jsonBoundsSouthwest.getDouble("lat"),
				jsonBoundsSouthwest.getDouble("lng"));
		return new Bounds(boundsNortheast, boundsSouthwest);
	}

	private Distance getDistance(JSONObject json) throws JSONException {
		JSONObject jsonDistance = json.getJSONObject("distance");
		String text = jsonDistance.getString("text");
		double value = jsonDistance.getDouble("value");
		return new Distance(text, value);
	}

	private Duration getDuration(JSONObject json) throws JSONException {
		JSONObject jsonDuration = json.getJSONObject("duration");
		String text = jsonDuration.getString("text");
		double value = jsonDuration.getDouble("value");
		return new Duration(text, value);
	}

	private Coordinates getCoordinates(JSONObject json) throws JSONException {
		double latitude = json.getDouble("lat");
		double longitude = json.getDouble("lng");
		return new Coordinates(latitude, longitude);
	}

	private List<Step> getSteps(JSONObject jsonLeg) throws JSONException {
		List<Step> steps = new ArrayList<Step>();

		JSONArray jsonSteps = jsonLeg.getJSONArray("steps");

		for (int k = 0; k < jsonSteps.length(); ++k) {
			// Get the step
			JSONObject jsonStep = jsonSteps.getJSONObject(k);

			// Get distance and duration
			Distance distance = getDistance(jsonStep);
			Duration duration = getDuration(jsonStep);

			// Get origin and destination
			Coordinates originCoordinates = getCoordinates(jsonStep
					.getJSONObject("start_location"));
			Coordinates destinationCoordinates = getCoordinates(jsonStep
					.getJSONObject("end_location"));
			Location origin = new Location("", originCoordinates);
			Location destination = new Location("", destinationCoordinates);

			// TODO: get the substeps

			// Get the travel mode
			TravelMode travelMode;
			String jsonTravelMode = jsonStep.getString("travel_mode");

			if (jsonTravelMode.equalsIgnoreCase("driving")) {
				travelMode = TravelMode.TRAVEL_MODE_DRIVING;
			} else if (jsonTravelMode.equalsIgnoreCase("walking")) {
				travelMode = TravelMode.TRAVEL_MODE_WALKING;
			} else if (jsonTravelMode.equalsIgnoreCase("bicycling")) {
				travelMode = TravelMode.TRAVEL_MODE_BICYCLING;
			} else if (jsonTravelMode.equalsIgnoreCase("transit")) {
				travelMode = TravelMode.TRAVEL_MODE_TRANSIT;
			} else {
				travelMode = TravelMode.TRAVEL_MODE_DRIVING;
			}

			JSONObject jsonPolyline = jsonStep.getJSONObject("polyline");

			Polyline polyline = getPolyline(jsonPolyline);

			Step step = new Step(distance, duration, origin, destination,
					polyline, travelMode);
			steps.add(step);
		}

		return steps;
	}

	private Polyline getPolyline(JSONObject jsonPolyline) throws JSONException {
		// Get the encoded points string
		String points = jsonPolyline.getString("points");

		// Decode it
		int len = points.length();
		int index = 0;
		List<Coordinates> coordinates = new ArrayList<Coordinates>();
		int lat = 0;
		int lng = 0;

		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
				b = points.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = points.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			
			double latitude = lat / 1E5;
			double longitude = lng / 1E5;

			coordinates.add(new Coordinates(latitude, longitude));
		}
		
		// Finally, return the list of coordinates into the Polyline object
		return new Polyline(coordinates);
	}

	private List<Leg> getLegs(JSONObject jsonRoute) throws JSONException {
		List<Leg> legs = new ArrayList<Leg>();

		JSONArray jsonLegs = jsonRoute.getJSONArray("legs");

		for (int k = 0; k < jsonLegs.length(); ++k) {
			// Get the leg
			JSONObject jsonLeg = jsonLegs.getJSONObject(k);

			// Get distance and duration
			Distance distance = getDistance(jsonLeg);
			Duration duration = getDuration(jsonLeg);

			// Get origin and destination
			Coordinates originCoordinates = getCoordinates(jsonLeg
					.getJSONObject("start_location"));
			Coordinates destinationCoordinates = getCoordinates(jsonLeg
					.getJSONObject("end_location"));
			// TODO: retrieve the address/location name if exists
			Location origin = new Location("", originCoordinates);
			Location destination = new Location("", destinationCoordinates);

			// Get the steps
			List<Step> steps = getSteps(jsonLeg);

			Leg leg = new Leg(distance, duration, origin, destination, steps);
			legs.add(leg);
		}

		return legs;
	}

	private String get(String url) throws Exception {
		// Prepare return string
		String response;

		// Create the connection object
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		// Set some parameters
		HttpParams httpParams = httpClient.getParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);

		// Execute the call
		HttpResponse httpResponse = httpClient.execute(httpGet);

		// Analyze status response
		StatusLine statusLine = httpResponse.getStatusLine();
		if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
			// Get the response
			HttpEntity httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		} else {
			// Unexpected status response
			throw new IOException(statusLine.getReasonPhrase());
		}

		return response;
	}

}