package com.example.hw4;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 4
 * GalleryActivity.java
 */

import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
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
		
		// Code modified from below, updated to use non-deprecated parts of API
		// http://stackoverflow.com/questions/4098198/adding-fling-gesture-to-an-image-view-android
		final GestureDetector gdt = new GestureDetector(this, new ImageGestures());
		findViewById(R.id.imgViewer).setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// To be honest, reading documentation at:
				//	http://developer.android.com/reference/android/view/GestureDetector.OnGestureListener.html
				//	http://developer.android.com/reference/android/view/View.OnTouchListener.html
				// would lead me to think that we should just return gdt.onTouchEvent(arg1).
				// However, as someone pointed out on StackOverflow above, they need to be inverted. Going
				// to ask professor about that tonight.
				return !gdt.onTouchEvent(arg1);
			}
		});
		
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
		dialog.setMessage("Loading...");
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
	
	// Gesture detection from:
	//	https://developer.android.com/training/gestures/detector.html
	//	http://stackoverflow.com/questions/4098198/adding-fling-gesture-to-an-image-view-android
	// Please note: Gesture recognition code borrowed heavily from StackOverflow. Because there's so
	// little actual code, not sure there's any way to re-do this code that won't look like plagiarism.
	
	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private class ImageGestures extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                displayPrev();
                return true;
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                displayNext();
                return true;
            }
            
            return false;
        }
    }

}
