package com.tenea.googledirectionsapi.routes;

import java.util.List;


public class Leg {
	
	private Distance distance;
	private Duration duration;
	private Location origin;
	private Location destination;
	private List<Step> steps;
	
	public Leg(Distance distance, Duration duration, Location origin, Location destination, List<Step> steps) {
		this.setDistance(distance);
		this.setDuration(duration);
		this.setOrigin(origin);
		this.setDestination(destination);
		this.setSteps(steps);
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

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	
}
