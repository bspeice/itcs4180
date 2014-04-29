package edu.uncc.scavenger.database;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * LocationDatabaseHelper.java
 */

// Design pattern from: http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html

import java.util.ArrayList;
import java.util.List;

import edu.uncc.scavenger.rest.RestLocation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocationDatabaseHelper extends SQLiteOpenHelper {
	
	private static LocationDatabaseHelper helper;
	
	private static final String DATABASE_NAME = "locations.db";
	private static final String TABLE_NAME = "LOCATIONS";
	private static final int DATABASE_VERSION = 1;
	
	private static final String KEY_ID = "ID";
	private static final String KEY_NAME = "NAME";
	private static final String KEY_RIDDLE = "RIDDLE";
	private static final String KEY_LOCATION_LONG = "LOCATION_LONG";
	private static final String KEY_LOCATION_LAT = "LOCATION_LAT";
	private static final String KEY_VALIDATION_KEY = "VALIDATION_KEY";
	private static final String KEY_RIDDLE_IMAGE_URL = "RIDDLE_IMAGE_URL";
	
	private LocationDatabaseHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static LocationDatabaseHelper getInstance(Context ctx) {
		if (helper == null) {
			helper = new LocationDatabaseHelper(ctx.getApplicationContext());
		}
		return helper;
	}

	private static final String CREATE_QUERY = 
			"CREATE TABLE " + TABLE_NAME + "(" +
				KEY_ID + " int PRIMARY KEY," +
				KEY_NAME + " VARCHAR(255)," +
				KEY_RIDDLE + " VARCHAR(1024)," +
				KEY_LOCATION_LONG + " REAL," +
				KEY_LOCATION_LAT + " REAL," +
				KEY_VALIDATION_KEY + " VARCHAR(255)," +
				KEY_RIDDLE_IMAGE_URL + " VARCHAR(255)" +
				");";
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_NAME + ";");
	}
	
	public RestLocation locationFromCursor(Cursor c) {
		RestLocation location = null;
		if (c != null) {
			location = new RestLocation();
			location.setId(c.getInt(0));
			location.setName(c.getString(1));
			location.setRiddle(c.getString(2));
			location.setLocationLong(c.getDouble(3));
			location.setLocationLat(c.getDouble(4));
			location.setKey(c.getString(5));
			location.setRiddleImageUrl(c.getString(6));
		}
		
		return location;
	}
	
	public ArrayList<RestLocation> fetchAll() {
		ArrayList<RestLocation> results = new ArrayList<RestLocation>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_NAME, KEY_RIDDLE, KEY_LOCATION_LONG,
											KEY_LOCATION_LAT, KEY_VALIDATION_KEY, KEY_RIDDLE_IMAGE_URL},
				null, null, null, null, null);
		
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			do {
				RestLocation loc = locationFromCursor(c);
				if (loc != null) {
					results.add(loc);
				}
			} while (c.moveToNext());
		}
		
		return results;
	}
	
	public void persist(RestLocation loc) {
		ContentValues values = new ContentValues();
		values.put(KEY_ID, loc.getId());
		values.put(KEY_NAME, loc.getName());
		values.put(KEY_RIDDLE, loc.getRiddle());
		values.put(KEY_LOCATION_LONG, loc.getLocationLong());
		values.put(KEY_LOCATION_LAT, loc.getLocationLat());
		values.put(KEY_VALIDATION_KEY, loc.getKey());
		values.put(KEY_RIDDLE_IMAGE_URL, loc.getRiddleImageUrl());
		helper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}
	
	public void persistAll(List<RestLocation> locs) {
		for (RestLocation l : locs) {
			persist(l);
		}
	}
	
	public void updateRecord(RestLocation loc){
		ContentValues values = new ContentValues();
		values.put(KEY_ID, loc.getId());
		values.put(KEY_NAME, loc.getName());
		values.put(KEY_RIDDLE, loc.getRiddle());
		values.put(KEY_LOCATION_LONG, loc.getLocationLong());
		values.put(KEY_LOCATION_LAT, loc.getLocationLat());
		values.put(KEY_VALIDATION_KEY, loc.getKey());
		values.put(KEY_RIDDLE_IMAGE_URL, loc.getRiddleImageUrl());
		helper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
}
