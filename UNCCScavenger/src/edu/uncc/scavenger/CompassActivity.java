package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * CompassActivity.java
 */
/* References:
 * stackoverflow.com/questions/4308262/calculate-compass-bearing-heading-to-location-in-android
 * stackoverflow.com/questions/5479753/using-orientation-sensor-to-point-towards-a-specific-location
 */

import edu.uncc.scavenger.rest.RestLocation;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CompassActivity extends Activity implements SensorEventListener
{
	final int SEARCH_PROXIMITY = 10;
	final int MOVING_AVERAGE_SIZE = 20;
	ImageView arrowView, searchImageView;
	Button backButton;
	SensorManager sManager;
	Sensor aSensor, mSensor;
	float[] compassValues = new float[3];
	float[] rotation =  new float[16];
	float[] rotated = new float[16];
	float[] inclination = new float[16];
	float[] gravity = new float[3];
	float[] magneticField = new float[3];
	float[] coordinates = new float[3];
	LocationManager locationManager;
	DirectionListener locationListener;
	Location searchLocation;
	RestLocation restLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);

		restLocation = (RestLocation)(getIntent().getSerializableExtra("restLocation"));
		
		arrowView = (ImageView)findViewById(R.id.arrowView);
		searchImageView = (ImageView)findViewById(R.id.searchImageView);
		searchImageView.setVisibility(View.INVISIBLE);
		Bitmap b = BitmapAccess.loadBitmap(this, restLocation.getName());
		if(b != null)
		{
			searchImageView.setImageBitmap(b);
		}
		
		searchLocation = new Location(LocationManager.NETWORK_PROVIDER);
		searchLocation.setLatitude((float)(restLocation.getLocationLat())); 
		searchLocation.setLongitude((float)(restLocation.getLocationLong()));
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationListener = new DirectionListener(searchLocation);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
		
		sManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		aSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensor = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		
		backButton = (Button)findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		sManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_GAME);
		sManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		sManager.unregisterListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		//Check sensor that changed
		int type = event.sensor.getType();
		if(type == Sensor.TYPE_ACCELEROMETER)
		{
			gravity = event.values;
		}
		else if(type == Sensor.TYPE_MAGNETIC_FIELD)
		{
			magneticField = event.values;
		}
		else
		{
			return;
		}
		
		//Compute compass  orientation values
		SensorManager.getInclination(inclination);
		SensorManager.getRotationMatrix(rotation, inclination, gravity, magneticField);
		SensorManager.getOrientation(rotation, compassValues);
		
		//azimuth is compassValues[0]
		//Calculate true north and angle to desired location
		float trueHeading = (float)(Math.toDegrees(compassValues[0]) + locationListener.getDeclination());
		//Calculate bearing to search location
		float rotateArrow = (float) (trueHeading - locationListener.getBearing());
		
		//Rotate compass pointer accordingly
		arrowView.setRotation((long)(-1 * rotateArrow));
		
		if(locationListener.getDistance() <= SEARCH_PROXIMITY)
		{
			arrowView.setVisibility(View.INVISIBLE);
			searchImageView.setVisibility(View.VISIBLE);
		}
		else
		{
			arrowView.setVisibility(View.VISIBLE);
			searchImageView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}
