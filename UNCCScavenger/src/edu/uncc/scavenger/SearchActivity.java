package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * SearchActivity.java
 */

import java.net.URL;

import edu.uncc.scavenger.database.LocationDatabaseHelper;
import edu.uncc.scavenger.rest.LocationClient;
import edu.uncc.scavenger.rest.RestLocation;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {

	ImageView locationImage;
	Button compassButton, scanButton;
	TextView locationText, riddleView;
	Intent intent;
	RestLocation restLocation;
	String key;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		locationText = (TextView)findViewById(R.id.locationText);
		locationImage = (ImageView)findViewById(R.id.locationImage);
		compassButton = (Button)findViewById(R.id.compassButton);
		scanButton = (Button)findViewById(R.id.scanButton);
		riddleView = (TextView)findViewById(R.id.riddleView);
		
		restLocation = (RestLocation)(getIntent().getSerializableExtra("restLocation"));
		//Log.d("restLocation", restLocation.getName());
		
		locationText.setText(restLocation.getName());
		riddleView.setText(restLocation.getRiddle());
		
		scanButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try{
					intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "SCAN_MODE");
					intent.putExtra("SAVE_HISTORY", false);
					startActivityForResult(intent, 0);
				}
				catch(ActivityNotFoundException e){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
					builder.setMessage(getString(R.string.zxing_missing_message));
					builder.setPositiveButton(getString(R.string.dialog_ok_text), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Does not work on an emulator because there is no access to the market
							Uri marketUri = Uri.parse(getString(R.string.zxing_market_uri));
							Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
							startActivity(marketIntent);
						}
					});
					builder.setNegativeButton(getString(R.string.dialog_cancel_text), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();	
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
		compassButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				
				if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
				{
					intent = new Intent(SearchActivity.this, CompassActivity.class);
					intent.putExtra("restLocation", restLocation);
					startActivity(intent);
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
					builder.setMessage(getString(R.string.gps_disabled_message));
					builder.setPositiveButton(getString(R.string.dialog_ok_text), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					});
					builder.setNegativeButton(getString(R.string.dialog_cancel_text), new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();	
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});
		
		//Toast.makeText(getApplicationContext(), restLocation.getRiddleImageUrl(), Toast.LENGTH_SHORT).show();
		Bitmap locationPicture = BitmapAccess.loadBitmap(this, restLocation.getName());
		if(locationPicture != null)
		{
			locationImage.setImageBitmap(locationPicture);
		}
		else
		{
			new ImageDownloader().execute(restLocation.getRiddleImageUrl());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK)
            {
            	String contents = data.getStringExtra("SCAN_RESULT"); 
            	new LocationClient.VerifyAsync(this) {
        			@Override
        			protected void onPostExecute(String result) {
        				super.onPostExecute(result);
        				key = result;
        				//Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
        				if(key!= null)
                        {
                        	restLocation.setKey(key);
                        	LocationDatabaseHelper.getInstance(SearchActivity.this).updateRecord(restLocation);
                        	intent = new Intent(SearchActivity.this, FoundActivity.class);
                        	intent.putExtra("restLocation", restLocation);
                        	startActivity(intent);
                        	finish();
                        }
                        else
                        {
                        	Toast.makeText(SearchActivity.this, "Incorrect place found", Toast.LENGTH_SHORT).show();
                        }
        			}
        		}.execute(restLocation.getName(), contents);
            } 
            else if (resultCode == RESULT_CANCELED) 
            {
            	Toast.makeText(SearchActivity.this, "Error scanning code", Toast.LENGTH_SHORT).show();
            }
        }
    }
	
	private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			try
			{
				URL imageUrl = new URL(params[0]);
				return BitmapFactory.decodeStream(imageUrl.openStream());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			
			if(result!=null)
			{
				locationImage.setImageBitmap(result);
				BitmapAccess.saveBitmap(SearchActivity.this, result, restLocation.getName());
			}
		}
	}
}
