package com.uncc.hw3;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends Activity {
	
	static final int winTime = 50;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		float elapsedTime = getIntent().getExtras().getFloat("ELAPSED_TIME");
		
		// We always show the elapsed time, so let's do that now
		TextView elapsedText = (TextView)findViewById(R.id.txtResultElapsed);
		elapsedText.setText("Time elapsed : " + elapsedTime);
		
		// The layout is set for success, so we only need to change it if the
		// user lost the game...
		if (elapsedTime > winTime) {
			TextView resultText = (TextView)findViewById(R.id.txtResultValue);
			resultText.setText(R.string.txtResultValue_Failure);
			ImageView imgChest = (ImageView)findViewById(R.id.imgResult);
			imgChest.setImageDrawable(getResources().getDrawable(R.drawable.lose));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}
	
	public void onClick(View v) {
		// Figure out if the Try Again or Exit button was clicked
		if (v.getId() == R.id.btnTryAgain)
			startActivity(new Intent(this, MainActivity.class));
		else if (v.getId() == R.id.btnExit)
			finish();
	}

}
