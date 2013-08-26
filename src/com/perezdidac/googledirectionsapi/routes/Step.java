package com.perezdidac.googledirectionsapi.routes;



public class Step {

	public enum TravelMode {
		TRAVEL_MODE_DRIVING,
		TRAVEL_MODE_WALKING,
		TRAVEL_MODE_BICYCLING,
		TRAVEL_MODE_TRANSIT
	}
	
	private Distance distance;
	private Duration duration;
	private Location origin;
	private Location destination;
	private Polyline polyline;
	private TravelMode travelMode;
	
	public Step(Distance distance, Duration duration, Location origin, Location destination, Polyline polyline, TravelMode travelMode) {
		this.setDistance(distance);
		this.setDuration(duration);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setPolyline(polyline);
		this.setTravelMode(travelMode);
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}

	public Polyline getPolyline() {
		return polyline;
	}

	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}

	public TravelMode getTravelMode() {
		return travelMode;
	}

	public void setTravelMode(TravelMode travelMode) {
		this.travelMode = travelMode;
	}

}
