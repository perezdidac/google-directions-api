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

import java.util.ArrayList;
import java.util.List;

import com.tenea.googledirectionsapi.routes.Location;

public class RouteQuery {

	private Location origin;
	private Location destination;
	private RouteQueryOptions routeQueryOptions;
	private List<Location> waypoints;

	public RouteQuery(Location origin, Location destination, RouteQueryOptions routeQueryOptions) {
		setOrigin(origin);
		setDestination(destination);
		setWaypoints(new ArrayList<Location>());
		setRouteQueryOptions(routeQueryOptions);
	}

	public RouteQuery(Location origin, Location destination,
			List<Location> waypoints, RouteQueryOptions routeQueryOptions) {
		setOrigin(origin);
		setDestination(destination);
		setWaypoints(waypoints);
		setRouteQueryOptions(routeQueryOptions);
	}

	public RouteQuery(List<Location> locations,
			RouteQueryOptions routeQueryOptions) {
		if (locations.size() > 0) {
			setOrigin(locations.get(0));
			setDestination(locations.get(0));
		}
		if (locations.size() > 1) {
			setDestination(locations.get(1));
		}

		List<Location> waypoints = new ArrayList<Location>();
		for (int k = 2; k < locations.size(); ++k) {
			waypoints.add(locations.get(k));
		}

		setWaypoints(waypoints);
		setRouteQueryOptions(routeQueryOptions);
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

	public RouteQueryOptions getRouteQueryOptions() {
		return routeQueryOptions;
	}

	public void setRouteQueryOptions(RouteQueryOptions routeQueryOptions) {
		this.routeQueryOptions = routeQueryOptions;
	}

	public List<Location> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Location> waypoints) {
		this.waypoints = waypoints;
	}

}
