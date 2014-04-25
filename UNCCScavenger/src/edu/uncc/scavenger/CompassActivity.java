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

import java.util.ArrayList;
import java.util.List;

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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CompassActivity extends Activity implements SensorEventListener
{
	final int SEARCH_PROXIMITY = 10;
	final int MOVING_AVERAGE_SIZE = 10;
	ImageView compassRoseView, arrowView, searchImageView;
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
	ArrayList<Float> rotationAverage = new ArrayList<Float>(MOVING_AVERAGE_SIZE);
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
		compassRoseView = (ImageView)findViewById(R.id.compassRoseView);
		searchImageView = (ImageView)findViewById(R.id.searchImageView);
		searchImageView.setVisibility(View.INVISIBLE);
		Bitmap b = BitmapAccess.loadBitmap(this, restLocation.getName());
		if(b != null)
		{
			searchImageView.setImageBitmap(b);
		}
		
		/*Test Values 
		searchLocation.setLatitude(35.30719258);//woodward eagle
		searchLocation.setLongitude(-80.73505447);
		searchLocation = new Location(LocationManager.NETWORK_PROVIDER);
		searchLocation.setLatitude(35.310043);//Bottom of Student Union bridge
		searchLocation.setLongitude(-80.733734);*/
		
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
		
		Toast.makeText(this, ""+searchLocation.getLatitude()+", "+searchLocation.getLongitude(), Toast.LENGTH_SHORT).show();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		
		//Calculate true north and angle to desired location
		//float azimuth = (float) (Math.toDegrees(compassValues[0]));
		float trueHeading = (float)(Math.toDegrees(compassValues[0]) + locationListener.getDeclination());
		//Calculate bearing to search location
		float rotateArrow = (float) (trueHeading - locationListener.getBearing());
		// Take moving average of bearing to smooth it out
		if (rotationAverage.size() == MOVING_AVERAGE_SIZE) {
			rotationAverage.remove(0);
		}
		rotationAverage.add(rotateArrow);
		float finalRotation = findAverage(rotationAverage);
		
		
		//Rotate compass and arrow. Rotations must be opposite to counteract device movement
		compassRoseView.setRotation((long)(-1 * trueHeading));
		arrowView.setRotation((long)(-1 * finalRotation));
		
		if(locationListener.getDistance() <= SEARCH_PROXIMITY)
		{
			arrowView.setVisibility(View.INVISIBLE);
			compassRoseView.setVisibility(View.INVISIBLE);
			searchImageView.setVisibility(View.VISIBLE);
		}
		else
		{
			arrowView.setVisibility(View.VISIBLE);
			compassRoseView.setVisibility(View.VISIBLE);
			searchImageView.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	private float findAverage(List<Float> list) {
		float sum = 0.0f;
		for (Float f : list) {
			sum += f;
		}
		return sum / list.size();
	}
}
