package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * SearchActivity.java
 */

import edu.uncc.scavenger.rest.RestLocation;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
	TextView riddleView;
	Intent intent;
	RestLocation restLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		locationImage = (ImageView)findViewById(R.id.locationImage);
		compassButton = (Button)findViewById(R.id.compassButton);
		scanButton = (Button)findViewById(R.id.scanButton);
		riddleView = (TextView)findViewById(R.id.riddleView);
		
		//TODO
		//Load picture
		//Load riddle
		//Load hints
		
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
					//Does not work on an emulator because there is no access to the market
					Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
					Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
					startActivity(marketIntent);
				}
			}
		});
		compassButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				intent = new Intent(getApplicationContext(), CompassActivity.class);
				intent.putExtra("searchLat", restLocation.getLocationLat());
				intent.putExtra("searchLong", restLocation.getLocationLong());
				startActivity(intent);
			}
		});
		
		restLocation = (RestLocation)(getIntent().getSerializableExtra("restLocation"));
		Log.d("restLocation", restLocation.getName());
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
                if(contents.equals(restLocation.getName()))
                {
                	intent = new Intent(getApplicationContext(), FoundActivity.class);
                	startActivity(intent);
                	finish();
                }
                else
                {
                	Toast.makeText(getApplicationContext(), "Incorrect url found: "+contents, Toast.LENGTH_SHORT).show();
                }
            } 
            else if (resultCode == RESULT_CANCELED) 
            {
            	Toast.makeText(getApplicationContext(), "Error scanning code", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
