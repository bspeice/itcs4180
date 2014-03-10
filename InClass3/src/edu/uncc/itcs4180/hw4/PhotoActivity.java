package edu.uncc.itcs4180.hw4;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * In Class 3
 * PhotoActivity.java
 */
 
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhotoActivity extends Activity {

	ProgressDialog progress;
	LinearLayout root;
	GridView photoGrid;
	int[] imageUrlIds = {R.string.uncc_main_image, R.string.football_main_image,
			R.string.ifest_main_image, R.string.commencement_main_image
	};
	int[] imageNames = {R.string.uncc, R.string.sports, R.string.ifest, R.string.commencement};
	ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
	int downloadProgress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		
		root = (LinearLayout)findViewById(R.id.LinearLayout1);
		photoGrid = (GridView)findViewById(R.id.GridView1);
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
		Button exit = (Button)findViewById(R.id.ExitButton);
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
		getMenuInflater().inflate(R.menu.photo, menu);
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
			
			downloadProgress++;
			if(downloadProgress>=imageUrlIds.length)
			{
				progress.dismiss();
				//all images are loaded, so set them in the grid
				photoGrid.setAdapter(new ImageAdapter(photoGrid.getContext()));
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
				holder.textView.setText(getString(imageNames[position]));	
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
