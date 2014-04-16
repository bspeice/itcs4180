package edu.uncc.scavenger;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import edu.uncc.scavenger.database.LocationDatabaseHelper;
import edu.uncc.scavenger.rest.LocationClient;
import edu.uncc.scavenger.rest.RestLocation;

public class MainActivity extends Activity {
	
	ListView locationList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get our list of events loaded
		locationList = (ListView)findViewById(R.id.listLocations);
		List<RestLocation> locations = LocationDatabaseHelper.getInstance(this).fetchAll();
		if (locations != null && locations.size() > 0) {
			LocationAdapter mLocationAdapter = new LocationAdapter(locations);
			locationList.setAdapter(mLocationAdapter);
		} else {
			// We don't yet have any locations...
			((TextView)findViewById(R.id.txtNoLocations)).setVisibility(View.VISIBLE);
			((ListView)findViewById(R.id.listLocations)).setVisibility(View.GONE);
		}
		
		// And kick off contacting to server to see if there are any new ones
		new LocationClient.LocationsDownloader(this) {
			@Override
			protected void onPostExecute(List<RestLocation> result) {
				super.onPostExecute(result);
				// And update our adapter when done
				LocationAdapter mLocationAdapter = new LocationAdapter(result);
				locationList.setAdapter(mLocationAdapter);
				
				// Always show the ListView and hide the TextView
				// We're back on the main thread at this point, so it's legal.
				((TextView)findViewById(R.id.txtNoLocations)).setVisibility(View.GONE);
				((ListView)findViewById(R.id.listLocations)).setVisibility(View.VISIBLE);
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
