package com.example.hw4;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 4
 * GalleryActivity.java
 */

import java.net.URL;
import java.util.ArrayList;

import com.example.hw4.MainActivity.Holder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity 
{
	GridView gallery;
	ImageView imageView;
	Button exitButton;
	String[] thumbUrls;
	ArrayList<Bitmap> thumbs;
	ImageAdapter imageAdapter;
	Intent imageViewerIntent;
	ProgressDialog progress;
	int photosId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		
		imageViewerIntent = new Intent(this, ImageViewerActivity.class);
		thumbUrls = getResources().getStringArray(getIntent().getExtras().getInt("thumbsId"));
		photosId = getIntent().getExtras().getInt("photosId");
		gallery = (GridView)findViewById(R.id.gallery);
		exitButton = (Button)findViewById(R.id.buttonExit);
		thumbs = new ArrayList<Bitmap>();
		
		progress = new ProgressDialog(this);
		progress.setMessage("Loading...");
		progress.setCancelable(false);
		progress.show();
		
		for(String url: thumbUrls)
		{
			new DownloadThumbs().execute(url);
		}
		
		exitButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gallery, menu);
		return true;
	}

	private class DownloadThumbs extends AsyncTask<String, Void, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(String... url) 
		{
			Bitmap image = null;
			
			try
			{
				URL imageUrl = new URL(url[0]);
				image = BitmapFactory.decodeStream(imageUrl.openStream());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return image;
		}
		
		@Override
		protected void onPostExecute(Bitmap result)
		{
			if(result != null)
			{
				thumbs.add(result);
			}
			else
			{
				thumbs.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
			}
			
			if(thumbs.size() >= thumbUrls.length)
			{
				imageAdapter = new ImageAdapter(gallery.getContext());
				gallery.setAdapter(imageAdapter);
				
				gallery.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) 
					{
						imageViewerIntent.putExtra("urls", (int)photosId);
						imageViewerIntent.putExtra("index", (int)position);
						startActivity(imageViewerIntent);
					}
				});
				progress.dismiss();
			}
		}
	}
	
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
			return thumbs.size();
		}

		@Override
		public Object getItem(int position)//no purpose.
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
			ImageView imageView;
			View vi = convertView;
			
			if(vi == null)
			{
				imageView = new ImageView(getBaseContext());
				//imageView.setLayoutParams(new GridView.LayoutParams(50,50));
				imageView.setPadding(5, 5, 5, 5);
			}
			else
			{
				imageView = (ImageView)vi;
			}
			
			imageView.setImageBitmap(thumbs.get(position));
			return imageView;
		}
	}
}
