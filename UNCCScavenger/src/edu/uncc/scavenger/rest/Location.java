package edu.uncc.scavenger.rest;

public class Location {
	
	private int id;
	private String name;
	private String riddle;
	private double locationLong;
	private double locationLat;
	
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
	
	private android.location.Location getAndroidLocation() {
		android.location.Location mLocation = new android.location.Location("NinerFinderServer");
		mLocation.setLatitude(getLocationLat());
		mLocation.setLongitude(getLocationLong());
		return mLocation;
	}
	
	public float bearingTo(Location target) {
		return getAndroidLocation().bearingTo(target.getAndroidLocation());
	}
}
