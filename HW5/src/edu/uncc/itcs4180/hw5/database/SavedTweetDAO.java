package edu.uncc.itcs4180.hw5.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SavedTweetDAO 
{
	private SQLiteDatabase db;
	
	public SavedTweetDAO(SQLiteDatabase db)
	{
		this.db = db;
	}
	
	public long save(SavedTweet tweet)
	{
		ContentValues values = new ContentValues();
		values.put(SavedTweetTable.USER_NAME, tweet.getUsername());
		values.put(SavedTweetTable.TEXT, tweet.getText());
		values.put(SavedTweetTable.TIME, tweet.getTime());
		values.put(SavedTweetTable.PROFILE_IMAGE_URL, tweet.getProfileImageUrl());
		return db.insert(SavedTweetTable.TABLE_NAME, null, values);
	}
	
	public boolean update(SavedTweet tweet)
	{
		ContentValues values = new ContentValues();
		values.put(SavedTweetTable.USER_NAME, tweet.getUsername());
		values.put(SavedTweetTable.TEXT, tweet.getText());
		values.put(SavedTweetTable.TIME, tweet.getTime());
		values.put(SavedTweetTable.PROFILE_IMAGE_URL, tweet.getProfileImageUrl());
		return db.update(SavedTweetTable.TABLE_NAME, values, SavedTweetTable.ID+"="+tweet.getId(), null)<0;
	}
	
	public boolean delete(SavedTweet tweet)
	{
		return db.delete(SavedTweetTable.TABLE_NAME, SavedTweetTable.ID+"="+tweet.getId(), null)>0;
	}
	
	public SavedTweet get(long id)
	{
		SavedTweet tweet = null;
		Cursor c = db.query(true, SavedTweetTable.TABLE_NAME,
				new String[]{SavedTweetTable.ID, SavedTweetTable.USER_NAME, SavedTweetTable.TEXT, SavedTweetTable.TIME, SavedTweetTable.PROFILE_IMAGE_URL},
				SavedTweetTable.ID+"="+id, null, null, null, null, null);
		if(c!=null)
		{
			c.moveToFirst();
			tweet = this.buildFromCursor(c);
		}
		if(!c.isClosed())
		{
			c.close();
		}
		return tweet;
	}
	
	public List<SavedTweet> getAll()
	{
		List<SavedTweet> list = new ArrayList<SavedTweet>();
		Cursor c = db.query(SavedTweetTable.TABLE_NAME,
				new String[]{SavedTweetTable.ID, SavedTweetTable.USER_NAME, SavedTweetTable.TEXT, SavedTweetTable.TIME, SavedTweetTable.PROFILE_IMAGE_URL},
				null, null, null, null, null);
		if(c!=null)
		{
			c.moveToFirst();
			do
			{
				SavedTweet tweet = this.buildFromCursor(c);
				if(tweet != null)
				{
					list.add(tweet);
				}
			}while(c.moveToNext());
		}
		if(!c.isClosed())
		{
			c.close();
		}
		return list;
	}
	
	private SavedTweet buildFromCursor(Cursor c)
	{
		SavedTweet tweet = null;
		if(c!=null)
		{
			try
			{
				tweet = new SavedTweet();
				tweet.setId(c.getLong(0));
				tweet.setUsername(c.getString(1));
				tweet.setText(c.getString(2));
				tweet.setTime(c.getString(3));
				tweet.setProfileImageUrl(c.getString(4));
			}
			catch(Exception e)
			{
				return null;
			}
		}
		return tweet;
	}
	
	public boolean deleteAll()
	{
		try
		{
			db.execSQL("delete from "+ SavedTweetTable.TABLE_NAME);
			db.execSQL("delete from sqlite_sequence where name='"+ SavedTweetTable.TABLE_NAME+"'");
			return true;
		}
		catch(SQLException e)
		{
			return false;
		}
	}
}
