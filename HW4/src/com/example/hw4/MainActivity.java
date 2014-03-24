package com.example.hw4;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 4
 * MainActivity.java
 */
 
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	ProgressDialog progress;
	LinearLayout root;
	GridView photoGrid;
	int[] imageUrlIds = {R.string.uncc_main_thumb, R.string.football_main_thumb,
			R.string.ifest_main_thumb, R.string.commencement_main_thumb
	}; 
	int[] imageNames = {R.string.label_uncc, R.string.label_sports, R.string.label_ifest, R.string.label_commencement};
	int[] thumbNames = {R.array.uncc_thumbs, R.array.football_thumbs, R.array.ifest_thumbs, R.array.commencement_thumbs};
	ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
	ArrayList<String> bitmapNames = new ArrayList<String>();
	int downloadProgress;
	Intent galleryIntent;
	ImageAdapter imageAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		galleryIntent = new Intent(this, GalleryActivity.class);
		root = (LinearLayout)findViewById(R.id.layout_async);
		photoGrid = (GridView)findViewById(R.id.grid_async);
		downloadProgress = 0;
		
		//set the progress dialog
		progress = new ProgressDialog(this);
		progress.setMessage("Loading...");
		progress.setCancelable(false);
		progress.show();
		
		for(int x : imageUrlIds)//download images
		{
			new DownloadPhoto().execute(getString(x));
		}
		
		//create exit button
		Button exit = (Button)findViewById(R.id.btn_exit_async);
		exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class DownloadPhoto extends AsyncTask<String, Void, Bitmap>
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
			//already a default picture included in grid_schema.xml, so no need to set a blank pic
			bitmapList.add(result);
			bitmapNames.add(getString(imageNames[downloadProgress]));
			
			downloadProgress++;
			if(downloadProgress>=imageUrlIds.length)
			{
				progress.dismiss();
				//all images are loaded, so set them in the grid
				imageAdapter = new ImageAdapter(photoGrid.getContext());
				photoGrid.setAdapter(imageAdapter);
				
				photoGrid.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) 
					{
						//Send intent with R.array.label
						galleryIntent.putExtra("thumbsId", (Integer)(imageAdapter.getItem(position)));
						startActivity(galleryIntent);
					}
				});
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
			return imageUrlIds.length;
		}

		@Override
		public Object getItem(int position)
		{
			return thumbNames[position];
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
