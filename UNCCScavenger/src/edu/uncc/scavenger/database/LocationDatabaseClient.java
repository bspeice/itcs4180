package edu.uncc.scavenger.database;

import java.util.List;

import edu.uncc.scavenger.rest.RestLocation;

public class LocationDatabaseClient {
	
	/*
	 * Super high-level API for working with locations.
	 * Also helpful for just drawing out skeletons of code.
	 */
	
	public List<RestLocation> getAllLocations() {
		// TODO: Implement method to get all locations that exist in the database
		return null;
	}
	
	public void persistLocation(RestLocation location) {
		// TODO: Save a location to the database
	}
	
	public void persistLocation(List<RestLocation> locations) {
		for (RestLocation l: locations) {
			persistLocation(l);
		}
	}
	
	public void synchronizeLocations(List<RestLocation> locations) {
		// TODO: Implement method to save all values in locations, and remove values that aren't there.
	}
	
}
