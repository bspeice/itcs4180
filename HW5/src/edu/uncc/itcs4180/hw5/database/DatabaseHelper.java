package edu.uncc.itcs4180.hw5.database;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * DatabaseHelper.java
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	final static String DATABASE_NAME = "savedtweets.db";
	final static int DATABASE_VERSION = 2;
	final static String TAG = "STdb1";
	
	DatabaseHelper(Context mContext)
	{
		super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db)
	{
		super.onOpen(db);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		SavedTweetTable.onCreate(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(TAG, "Upgrading db from version "+ oldVersion+ " to "+ newVersion);
		SavedTweetTable.onUpgrade(db, oldVersion, newVersion);
	}
}
