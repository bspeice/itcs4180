package edu.uncc.scavenger.rest;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * RestLocation.java
 */

import java.io.Serializable;

import android.location.Location;

public class RestLocation implements Serializable{
	
	private int id;
	private String name;
	private String riddle;
	private double locationLong;
	private double locationLat;
	private String riddleImageURL;
	private String key;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRiddle() {
		return riddle;
	}
	public void setRiddle(String riddle) {
		this.riddle = riddle;
	}
	public double getLocationLong() {
		return locationLong;
	}
	public void setLocationLong(double locationLong) {
		this.locationLong = locationLong;
	}
	public double getLocationLat() {
		return locationLat;
	}
	public void setLocationLat(double locationLat) {
		this.locationLat = locationLat;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getRiddleImageURL() {
		return riddleImageURL;
	}
	public void setRiddleImageURL(String riddleImageURL) {
		this.riddleImageURL = riddleImageURL;
	}
	
	public Location getLocation() {
		android.location.Location mLocation = new android.location.Location("NinerFinderServer");
		mLocation.setLatitude(getLocationLat());
		mLocation.setLongitude(getLocationLong());
		return mLocation;
	}

	public float bearingTo(RestLocation target) {
		return getLocation().bearingTo(target.getLocation());
	}
}
