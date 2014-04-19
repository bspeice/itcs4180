package edu.uncc.scavenger.rest;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * LocationService.java
 */

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface LocationService {
	
	@GET("/locations")
	List<RestLocation> listLocations();
	
	@GET("/validate")
	String getResult(@QueryMap Map<String, String> keys);

}
