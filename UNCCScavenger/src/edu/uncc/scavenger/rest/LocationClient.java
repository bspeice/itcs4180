package edu.uncc.scavenger.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import edu.uncc.scavenger.R;
import edu.uncc.scavenger.database.LocationDatabaseHelper;
import android.content.Context;
import android.os.AsyncTask;
import retrofit.RestAdapter;

public class LocationClient {

	private static LocationService getAdapter(Context ctx) {
		String endpoint = ctx.getString(R.string.endpoint);
		RestAdapter ra = new RestAdapter.Builder().setEndpoint(endpoint)
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
		Context ctx;

		public LocationsDownloader(Context ctx) {
			this.ctx = ctx;
		}

		@Override
		protected List<RestLocation> doInBackground(Void... arg0) {
			return getAdapter(ctx).listLocations();
		}
	}

	public static AsyncTask<Void, Void, List<RestLocation>> updateDatabase(Context ctx) {
		// Start the downloader and wait until it's finished.
		return new LocationsDownloader(ctx) {
			@Override
			protected void onPostExecute(List<RestLocation> result) {
				LocationDatabaseHelper.getInstance(ctx).persistAll(result);
			}
		}.execute();
	}
}
