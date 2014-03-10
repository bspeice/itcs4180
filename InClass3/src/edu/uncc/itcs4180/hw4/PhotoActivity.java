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
	public int[] imageUrlIds = {R.string.uncc_main_image, R.string.football_main_image,
			R.string.ifest_main_image, R.string.commencement_main_image
	};
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
			if (result == null)//set failed images to a default image
			{
				//set blank image
			}

			bitmapList.add(result);
			
			downloadProgress++;
			if(downloadProgress>=imageUrlIds.length)
			{
				progress.dismiss();
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
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position) 
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
				vi = getLayoutInflater().inflate(R.layout.grid_schema, null);
				
				holder.textView = (TextView)vi.findViewById(R.id.textView1);
				holder.imageView = (ImageView)vi.findViewById(R.id.imageView1);
				
				vi.setTag(holder);
			}
			else
			{
				holder = (Holder)(vi.getTag());
			}
			holder.textView.setText(imageUrlIds[position]);
			if(bitmapList.get(position)!=null)
				holder.imageView.setImageBitmap(bitmapList.get(position));
			
			return vi;
		}
	}
	
	static class Holder
	{
		TextView textView;
		ImageView imageView;
	}
}
