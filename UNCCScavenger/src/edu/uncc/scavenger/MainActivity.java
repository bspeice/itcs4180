package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * MainActivity.java
 */

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import edu.uncc.scavenger.database.LocationDatabaseHelper;
import edu.uncc.scavenger.rest.LocationClient;
import edu.uncc.scavenger.rest.RestLocation;

public class MainActivity extends Activity {
	
	ListView locationList;
	List<RestLocation> locations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		locationList = (ListView)findViewById(R.id.listLocations);
		
		// Get our list of events loaded
		locationList = (ListView)findViewById(R.id.listLocations);
 		locations = LocationDatabaseHelper.getInstance(this).fetchAll();
 		if (locations != null && locations.size() > 0) {
 			LocationAdapter mLocationAdapter = new LocationAdapter(locations);
 			locationList.setAdapter(mLocationAdapter);
			locationList.setOnItemClickListener(new OnItemClickListener(){
				
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Intent intent = new Intent(MainActivity.this, SearchActivity.class);
				intent.putExtra("restLocation", locations.get(position));
				startActivity(intent);
				}
			});
		 		} 
 		else {
		 		// We don't yet have any locations...
		 		((TextView)findViewById(R.id.txtNoLocations)).setVisibility(View.VISIBLE);
		 		((ListView)findViewById(R.id.listLocations)).setVisibility(View.GONE);
		 }
 		
		// And kick off contacting to server to see if there are any new ones
		locationList = (ListView)findViewById(R.id.listLocations);
		new LocationClient.LocationsDownloader(this) {
			@Override
			protected void onPostExecute(List<RestLocation> result) {
				super.onPostExecute(result);
				// And update our adapter when done
				for(int x=0; x<locations.size(); x++)
				{
					result.remove(0);
				}
				locations.addAll(result);
				LocationAdapter mLocationAdapter = new LocationAdapter(locations);
				locationList.setAdapter(mLocationAdapter);
				locationList.setOnItemClickListener(new OnItemClickListener(){
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						Intent intent = new Intent(MainActivity.this, SearchActivity.class);
						intent.putExtra("restLocation", locations.get(position));
						startActivity(intent);
						}
					});
				
				// Always show the ListView and hide the TextView
				// We're back on the main thread at this point, so it's legal.
				((TextView)findViewById(R.id.txtNoLocations)).setVisibility(View.GONE);
				((ListView)findViewById(R.id.listLocations)).setVisibility(View.VISIBLE);
				
				// And we're even kind enough to update the database
				LocationDatabaseHelper.getInstance(MainActivity.this).persistAll(locations);
				locations = LocationDatabaseHelper.getInstance(MainActivity.this).fetchAll();
			}
		}.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private class LocationAdapter extends BaseAdapter {
		private List<RestLocation> locations;
		
		public LocationAdapter(List<RestLocation> locations) {
			this.locations = locations;
		}

		@Override
		public int getCount() {
			return locations.size();
		}

		@Override
		public Object getItem(int position) {
			return locations.get(position);
		}

		@Override
		public long getItemId(int position) {
			return locations.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = new Holder();
			View v = convertView;
			
			if (v == null) {
				v = getLayoutInflater().inflate(R.layout.list_location, null);
				
				holder.imgFound = (ImageView)v.findViewById(R.id.imgIsFound);
				holder.name = (TextView)v.findViewById(R.id.txtName);
				holder.riddle = (TextView)v.findViewById(R.id.txtRiddle);
				
				if(locations.get(position).getKey()!=null)
				{
					holder.imgFound.setImageResource(R.drawable.checkbox_checked);
				}
				else
				{
					holder.imgFound.setImageResource(R.drawable.checkbox_unchecked);
				}
				
				v.setTag(holder);
			} else {
				holder = (Holder)v.getTag();
			}
			
			RestLocation location = locations.get(position);
			holder.name.setText(location.getName());
			holder.riddle.setText(location.getRiddle());
			
			return v;
		}
	}
	
	static class Holder {
		ImageView imgFound;
		TextView name;
		TextView riddle;
	}
}
