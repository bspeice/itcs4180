package com.example.hw4;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;

public class ImageViewerActivity extends Activity {
	
	int urlsId;
	String[] urls;
	int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		
		Bundle extras = getIntent().getExtras();
		urlsId = extras.getInt("urls");
		urls = getResources().getStringArray(urlsId);
		currentIndex = extras.getInt("index");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_viewer, menu);
		return true;
	}
	
	public void onClickPrev(View v) {
		displayPrev();
	}
		
	public void onClickNext(View v) {
		displayNext();
	}
	
	public void onClickBack(View v) {
		finish();
	}
	
	private void displayPrev() {
		currentIndex -= 1;
		displayImage(currentIndex);
	}
	
	private void displayNext() {
		currentIndex += 1;
		displayImage(currentIndex);
	}
	
	public void displayImage(int indexToDisplay) {
		
		
	}
	
	private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}

}
