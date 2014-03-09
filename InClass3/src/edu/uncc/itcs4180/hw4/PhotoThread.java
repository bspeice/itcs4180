package edu.uncc.itcs4180.hw4;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * In Class 3
 * PhotoThread.java
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PhotoThread extends Activity {
	
	ProgressDialog dialog;
	public int[] imageUrlIds = {R.string.uncc_main_image, R.string.football_main_image,
			R.string.ifest_main_image, R.string.commencement_main_image
	};
	private final int MSG_WHAT_ERROR = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_thread);
		
		// Set the initial progress dialog
		dialog = new ProgressDialog(this);
		dialog.setMessage("Downloading images...");
		dialog.setCancelable(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();
		
		// Asynchronous, we're done after starting the downloads
		Handler handler = new Handler(new PhotoThreadHandler(this));
		downloadImages(handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_thread, menu);
		return true;
	}
	
	public void downloadImages(Handler handler) {
		int progressIncrement = dialog.getMax() / imageUrlIds.length;
		
		// Start up the thread pool, and get downloading!
		ExecutorService pool = Executors.newFixedThreadPool(2);
		pool.execute(new DownloadThread(getString(R.string.uncc_main_image), handler));
		pool.execute(new DownloadThread(getString(R.string.football_main_image), handler));
		pool.execute(new DownloadThread(getString(R.string.ifest_main_image), handler));
		pool.execute(new DownloadThread(getString(R.string.commencement_main_image), handler));
	}
	
	private class DownloadThread implements Runnable{
		String url;
		Handler handler;
		
		public DownloadThread(String url, Handler handler) {
			this.url = url;
			this.handler = handler;
		}

		@Override
		public void run() {
			// Download the image at the given URL
			// If we ever encounter an issue, send an empty message
			try {
				// Connect to get our image
				URL url = new URL(this.url);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.connect();
				int statusCode = con.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					// Download the image, and send it in a message to the handler
					Bitmap image = BitmapFactory.decodeStream(con.getInputStream());
					Message msg = Message.obtain(handler);
					msg.obj = image;
					handler.sendMessage(msg);
				} else {
					handler.sendEmptyMessage(MSG_WHAT_ERROR);
				}
			} catch (MalformedURLException e) {
				handler.sendEmptyMessage(MSG_WHAT_ERROR);
				e.printStackTrace();
			} catch (IOException e) {
				// Issue in the HTTP connection, not too much we can do here...
				handler.sendEmptyMessage(MSG_WHAT_ERROR);
				e.printStackTrace();
			}
		}
		
	}
	
	private class PhotoThreadHandler implements Handler.Callback {
		Context ctx;
		
		public PhotoThreadHandler(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		public boolean handleMessage(Message msg) {
			// We are sent a message every time a download completes,
			// so update the progress, and close the dialog if we're done.
			int progressIncrement = dialog.getMax() / imageUrlIds.length;
			
			// The message actually contains a Bitmap that we need to put in the UI, so do that
			if (msg.what == MSG_WHAT_ERROR) {
				dialog.dismiss();
				Log.e("HANDLER", "Got a MSG_WHAT_ERROR, not sure what's up...");
				finish();
			} else {
				// The thread worked just fine, so let's get the Bitmap out and create a view for it
				Bitmap image = (Bitmap) msg.obj;
				ImageView newImage = new ImageView(this.ctx);
				newImage.setImageBitmap(image);
				((ViewGroup)findViewById(android.R.id.content)).addView(newImage);
			}
			
			// If (max - increment == current progress) we're done
			if (dialog.getMax() - progressIncrement == dialog.getProgress())
				dialog.dismiss();
			else
				dialog.setProgress(dialog.getProgress() + progressIncrement);
			
			return true;
		}
		
	}

}
