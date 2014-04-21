package edu.uncc.scavenger.rest;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * LocationClient.java
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import android.content.Context;
import android.os.AsyncTask;
import edu.uncc.scavenger.R;

public class LocationClient {

	private static LocationService getAdapter(Context ctx) {
		String endpoint = ctx.getString(R.string.endpoint);
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
		RestAdapter ra = new RestAdapter.Builder().setEndpoint(endpoint)
				.setConverter(new GsonConverter(gson))
				.build();
		return ra.create(LocationService.class);
	}

	public static String validateLocation(Context ctx, int id, String key) {
		LocationService client = getAdapter(ctx);
		Map<String, String> keys = new HashMap<String, String>();
		keys.put("key", key);
		keys.put("id", String.valueOf(id));
		return client.getResult(keys);
	}

	public static class LocationsDownloader extends
			AsyncTask<Void, Void, List<RestLocation>> {
		private Context ctx;

		public LocationsDownloader(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		protected List<RestLocation> doInBackground(Void... arg0) {
			return getAdapter(ctx).listLocations();
		}
	}
}
