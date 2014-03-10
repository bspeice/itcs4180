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
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.HttpConnection;

import edu.uncc.itcs4180.hw4.PhotoActivity.Holder;
import edu.uncc.itcs4180.hw4.PhotoActivity.ImageAdapter;

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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoThread extends Activity {
	
	ProgressDialog dialog;
	public int[] imageUrlIds = {R.string.uncc_main_image, R.string.football_main_image,
			R.string.ifest_main_image, R.string.commencement_main_image
	};
	int[] imageNames = {R.string.uncc, R.string.sports, R.string.ifest, R.string.commencement};
	GridView photoGrid;
	ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
	ArrayList<String> bitmapNames = new ArrayList<String>();
	private final int MSG_WHAT_ERROR = -1;
	
	public void onClickExit(View v) {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_thread);
		
		photoGrid = (GridView)findViewById(R.id.grid_thread);
		
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
		pool.execute(new DownloadThread(getString(R.string.uncc_main_image), handler, R.string.uncc));
		pool.execute(new DownloadThread(getString(R.string.football_main_image), handler, R.string.sports));
		pool.execute(new DownloadThread(getString(R.string.ifest_main_image), handler, R.string.ifest));
		pool.execute(new DownloadThread(getString(R.string.commencement_main_image), handler, R.string.commencement));
	}
	
	private class DownloadThread implements Runnable{
		String url;
		Handler handler;
		int name;
		
		public DownloadThread(String url, Handler handler, int name) {
			this.url = url;
			this.handler = handler;
			this.name = name;
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
					msg.what = name;
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
				bitmapList.add(image);
				bitmapNames.add(ctx.getString(msg.what));
			}
			
			// If (max - increment == current progress) we're done
			if (dialog.getMax() - progressIncrement == dialog.getProgress()) {
				photoGrid.setAdapter(new ImageAdapter(photoGrid.getContext()));
				dialog.dismiss();
			}
			else
				dialog.setProgress(dialog.getProgress() + progressIncrement);
			
			return true;
		}
		
	}
	
	// Code very similar to PhotoActivity, but tweaked slightly for different ID's, etc.
	
	public class ImageAdapter extends BaseAdapter
	{
		private Context context;
		
		public ImageAdapter(Context context)
		{
			this.context = context;
		}
		
		@Override
		public int getCount() 
		{
			return imageUrlIds.length;
		}

		@Override
		public Object getItem(int position)//no purpose. only to fill the requirement of needing the method.
		{
			return position;
		}

		@Override
		public long getItemId(int position)//no purpose. only to fill the requirement of needing the method.
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			Holder holder = new Holder();
			View vi = convertView;
			
			if(vi == null)
			{
				//create layout of what we want one grid section to look like
				vi = getLayoutInflater().inflate(R.layout.grid_schema, null);
				
				holder.textView = (TextView)vi.findViewById(R.id.textView1);
				holder.imageView = (ImageView)vi.findViewById(R.id.imageView1);
				
				vi.setTag(holder);//associate the views in the holder to the grid
			}
			else
			{
				holder = (Holder)(vi.getTag());
			}
			//set the views in the grid to what was loaded
			holder.textView.setText(getString(R.string.download_error));
			if(bitmapList.get(position)!=null)
			{
				holder.imageView.setImageBitmap(bitmapList.get(position));
				holder.textView.setText(bitmapNames.get(position));	
			}
			
			return vi;
		}
	}
	
	//views included in one grid section
	static class Holder
	{
		TextView textView;
		ImageView imageView;
	}

}
