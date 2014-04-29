package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * DirectionListener.java
 */

import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class DirectionListener implements LocationListener 
{
	GeomagneticField geoField;
	Location toGo;
	float bearing;
	float distance;
	
	public DirectionListener(Location toGo)
	{
		this.toGo = toGo;
		this.bearing = 0;
		this.distance = 0;
	}
	
	@Override
	public void onLocationChanged(Location location) 
	{
		
		geoField = new GeomagneticField(
				(float)location.getLatitude(),
				(float)location.getLongitude(),
				(float)location.getAltitude(),
				System.currentTimeMillis()
		);
		
		bearing = location.bearingTo(toGo);
		distance = location.distanceTo(toGo);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public float getBearing()
	{
		return bearing;
	}
	
	public float getDeclination()
	{
		if(geoField!=null)
			return geoField.getDeclination();
		else
			return 0;
	}
	
	public float getDistance()
	{
		return distance;
	}
}