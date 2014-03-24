package com.example.hw4;

import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class ImageViewerActivity extends Activity {
	
	int urlsId;
	String[] urls;
	int currentIndex;
	
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		
		Bundle extras = getIntent().getExtras();
		urlsId = extras.getInt("urls");
		urls = getResources().getStringArray(urlsId);
		currentIndex = extras.getInt("index");
		
		displayImage(currentIndex);
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
		// Wrapping backwards requires more than just modulo
		if (currentIndex == 0)
			currentIndex = urls.length - 1;
		else
			currentIndex -= 1;
		
		displayImage(currentIndex);
	}
	
	private void displayNext() {
		currentIndex = (currentIndex + 1) % urls.length;
		displayImage(currentIndex);
	}
	
	public void displayImage(int indexToDisplay) {
		ImageView view = (ImageView)findViewById(R.id.imgViewer);
		view.setImageBitmap(null);
		new ImageDownloader().execute(urls[indexToDisplay]);
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.show();
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
			dialog.cancel();
			ImageView view = (ImageView)findViewById(R.id.imgViewer);
			view.setImageBitmap(result);
		}
	}

}
