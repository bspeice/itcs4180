package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * FoundActivity.java
 */

import edu.uncc.scavenger.rest.RestLocation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class FoundActivity extends Activity {

	Button tryMoreButton;
	WebView moreInfoWebView;
	Intent intent;
	RestLocation restLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_found);

		moreInfoWebView = (WebView)findViewById(R.id.moreInfoWebView);
		tryMoreButton = (Button)findViewById(R.id.tryMoreButton);
		
		restLocation = (RestLocation)(getIntent().getSerializableExtra("restLocation"));
		moreInfoWebView.loadData(restLocation.getKey(), "text/html", "UTF-8");
		
		tryMoreButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				intent = new Intent(FoundActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.found, menu);
		return true;
	}
}
