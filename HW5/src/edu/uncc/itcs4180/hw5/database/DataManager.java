package edu.uncc.itcs4180.hw5.database;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataManager 
{
	Context mContext;
	DatabaseHelper dbOpenHelper;
	SQLiteDatabase db;
	SavedTweetDAO tweetDao;
	
	public DataManager(Context mContext)
	{
		this.mContext = mContext;
		dbOpenHelper = new DatabaseHelper(mContext);
		db = dbOpenHelper.getWritableDatabase();
		tweetDao = new SavedTweetDAO(db);
	}
	
	public void close()
	{
		db.close();
	}
	
	public long saveTweet(SavedTweet tweet)
	{
		return tweetDao.save(tweet);
	}
	
	public boolean updateTweet(SavedTweet tweet)
	{
		return tweetDao.update(tweet);
	}
	
	public boolean deleteTweet(SavedTweet tweet)
	{
		return tweetDao.delete(tweet);
	}
	
	public SavedTweet getTweet(long id)
	{
		return tweetDao.get(id);
	}
	
	public List<SavedTweet> getAllTweets()
	{
		return tweetDao.getAll();
	}
	
	public boolean deleteAll()
	{
		return tweetDao.deleteAll();
	}
}
