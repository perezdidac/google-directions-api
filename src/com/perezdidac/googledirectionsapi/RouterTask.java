package com.perezdidac.googledirectionsapi;

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
import android.util.Log;

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
		// TODO: set waypoints
		
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
				
				// Get legs
				List<Leg> legs = getLegs(jsonRoute);
				route.setLegs(legs);
			}
		}
		
		

			// Get the leg, only one leg as we don't support waypoints
			final JSONObject leg = jsonRoute.getJSONArray("legs")
					.getJSONObject(0);
			// Get the steps for this leg
			final JSONArray steps = leg.getJSONArray("steps");
			// Number of steps for use in for loop
			final int numSteps = steps.length();
			// Set the name of this route using the start & end addresses
			route.setName(leg.getString("start_address") + " to "
					+ leg.getString("end_address"));
			// Get google's copyright notice (tos requirement)
			route.setCopyright(jsonRoute.getString("copyrights"));
			// Get the total length of the route.
			route.setLength(leg.getJSONObject("distance").getInt("value"));
			// Get any warnings provided (tos requirement)
			if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
				route.setWarning(jsonRoute.getJSONArray("warnings")
						.getString(0));
			}
			/*
			 * Loop through the steps, creating a segment for each one and
			 * decoding any polylines found as we go to add to the route
			 * object's map array. Using an explicit for loop because it is
			 * faster!
			 */
			for (int i = 0; i < numSteps; i++) {
				// Get the individual step
				final JSONObject step = steps.getJSONObject(i);
				// Get the start position for this step and set it on the
				// segment
				final JSONObject start = step.getJSONObject("start_location");
				final LatLng position = new LatLng(
						(int) (start.getDouble("lat") * 1E6),
						(int) (start.getDouble("lng") * 1E6));
				segment.setPoint(position);
				// Set the length of this segment in metres
				final int length = step.getJSONObject("distance").getInt(
						"value");
				distance += length;
				segment.setLength(length);
				segment.setDistance(distance / 1000);
				// Strip html from google directions and set as turn instruction
				segment.setInstruction(step.getString("html_instructions")
						.replaceAll("<(.*?)*>", ""));
				// Retrieve & decode this segment's polyline and add it to the
				// route.
				route.addPoints(decodePolyLine(step.getJSONObject("polyline")
						.getString("points")));
				// Push a copy of the segment to the route
				route.addSegment(segment.copy());
			}
		} catch (JSONException e) {
			Log.e(e.getMessage(), "Google JSON Parser - " + feedUrl);
		}
		return route;
	}

	private Bounds getBounds(JSONObject jsonRoute) {
		// Parse bounds
		JSONObject jsonBounds = jsonRoute.getJSONObject("bounds");
		JSONObject jsonBoundsNortheast = jsonBounds.getJSONObject("northeast");
		JSONObject jsonBoundsSouthwest = jsonBounds.getJSONObject("southwest");
		Coordinates boundsNortheast = new Coordinates(jsonBoundsNortheast.getDouble("lat"), jsonBoundsNortheast.getDouble("lng"));
		Coordinates boundsSouthwest = new Coordinates(jsonBoundsSouthwest.getDouble("lat"), jsonBoundsSouthwest.getDouble("lng"));
		Bounds bounds = new Bounds(boundsNortheast, boundsSouthwest);
	}
	
	private Distance getDistance(JSONObject json) {
		JSONObject jsonDistance = json.getJSONObject("distance");
		String text = jsonDistance.getString("text");
		double value = jsonDistance.getDouble("value");
		return new Distance(text, value);
	}

	private Duration getDuration(JSONObject json) {
		JSONObject jsonDuration = json.getJSONObject("duration");
		String text = jsonDuration.getString("text");
		double value = jsonDuration.getDouble("value");
		return new Duration(text, value);
	}
	
	private Location getOrigin(JSONObject json) {
		JSONObject startLocation = json.getJSONObject("start_location");
		double latitude = startLocation.getDouble("lat");
		double longitude = startLocation.getDouble("lng");
		Coordinates coordinates = new Coordinates(latitude, longitude);
		
		return new Location(name, coordinates);
	}

	private List<Leg> getLegs(JSONObject jsonRoute) {
		List<Leg> legs = new ArrayList<Leg>();
		
		JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
		
		for (int k = 0; k < jsonLegs.length(); ++k) {
			// Get the leg
			JSONObject jsonLeg = jsonLegs.getJSONObject(k);
			
			// Get distance and duration
			Distance distance = getDistance(jsonLeg);
			Duration duration = getDuration(jsonLeg);
			
			// Get origin and destination
			Location origin = getOrigin(jsonLeg);
			Location destination = getDestination(jsonLeg);
			
			Leg leg = new Leg(distance, duration, origin, destination, steps)
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