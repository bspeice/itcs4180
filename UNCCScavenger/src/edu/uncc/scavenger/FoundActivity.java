package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * FoundActivity.java
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FoundActivity extends Activity {

	TextView numberFoundText;
	Button seeMoreButton, tryMoreButton;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found);

		numberFoundText = (TextView)findViewById(R.id.numberFoundText);
		seeMoreButton = (Button)findViewById(R.id.seeMoreButton);
		tryMoreButton = (Button)findViewById(R.id.tryMoreButton);
		
		seeMoreButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//TODO
				//Get more information address and open web browser
				
			}
		});
		tryMoreButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				intent = new Intent(FoundActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		//TODO
		//Add found location to database
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.found, menu);
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
}
