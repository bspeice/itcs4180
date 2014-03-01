package edu.uncc.itcs4180.hw4;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(View v) {
		// One of our buttons has been clicked, let's start the activity and go!
		if (v.getId() == R.id.btnAsync)
			startActivity(new Intent(this, PhotoActivity.class));
		else
			startActivity(new Intent(this, PhotoThread.class));
	}

}
