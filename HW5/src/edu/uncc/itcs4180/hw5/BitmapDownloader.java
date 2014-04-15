package edu.uncc.itcs4180.hw5;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * BitmapDownloader.java
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BitmapDownloader extends AsyncTask<String, Void, Void> {
	ImageView iv;
	Bitmap downloaded;
	
	public BitmapDownloader(ImageView iv) {
		this.iv = iv;
	}

	@Override
	protected Void doInBackground(String... params) {
		downloaded = downloadBitmap(params[0]);
		return null;
	}

	private Bitmap downloadBitmap(String sUrl) {
		try {
			URL url = new URL(sUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.connect();
			if (con.getResponseCode() == 200) {
				return BitmapFactory.decodeStream(con.getInputStream());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		iv.setImageBitmap(downloaded);
	}
}
