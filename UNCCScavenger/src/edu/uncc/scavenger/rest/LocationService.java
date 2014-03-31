package edu.uncc.scavenger.rest;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
import retrofit.http.QueryMap;

public interface LocationService {
	
	@GET("/locations")
	List<Location> listLocations();
	
	@GET("/validate")
	String getResult(@QueryMap Map<String, String> keys);

}
