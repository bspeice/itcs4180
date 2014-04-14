package edu.uncc.itcs4180.hw5.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SavedTweetTable 
{
	final static String TABLE_NAME = "savedtweets";
	final static String ID = "_id";
	final static String USER_NAME = "username";
	final static String TEXT = "text";
	final static String TIME = "time";
	final static String PROFILE_IMAGE_URL = "profileimageurl";
	
	public static void onCreate(SQLiteDatabase db)
	{
		String createTableSql = "CREATE TABLE "+ SavedTweetTable.TABLE_NAME + "("+
				SavedTweetTable.ID+ " integer primary key autoincrement, "+
				SavedTweetTable.USER_NAME+ " text not null, "+
				SavedTweetTable.TEXT+ " text not null, "+
				SavedTweetTable.TIME+ " text not null, "+
				SavedTweetTable.PROFILE_IMAGE_URL+ " text not null);";
		
		try
		{
			db.execSQL(createTableSql);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS "+ SavedTweetTable.TABLE_NAME);
		SavedTweetTable.onCreate(db);
	}
}
