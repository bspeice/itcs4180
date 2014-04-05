package edu.uncc.scavenger.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uncc.scavenger.R;
import android.content.Context;

import retrofit.RestAdapter;

public class LocationClient {
	
	private Context ctx;
	
	public LocationClient(Context ctx) {
		this.ctx = ctx;
	}
	
	public LocationService getAdapter() {
		String endpoint = ctx.getString(R.string.endpoint);
		RestAdapter ra = new RestAdapter.Builder().setEndpoint(endpoint).build();
		return ra.create(LocationService.class);
	}
	
	public String validateLocation(int id, String key) {
		LocationService client = getAdapter();
		Map<String, String> keys = new HashMap<String, String>();
		keys.put("key", key);
		keys.put("id", String.valueOf(id));
		return client.getResult(keys);
	}
	
	public List<Location> getLocations(Context ctx) {
		LocationService client = getAdapter();
		return client.listLocations();
	}
}
