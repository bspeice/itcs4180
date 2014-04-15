package edu.uncc.itcs4180.hw5;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * SavedNewsActivity.java
 */

import java.util.List;

import edu.uncc.itcs4180.hw5.database.DataManager;
import edu.uncc.itcs4180.hw5.database.SavedTweet;
import edu.uncc.itcs4180.hw5.twitter.Tweet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SavedNewsActivity extends Activity {

	private List<SavedTweet> tweets;
	private DataManager dm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_news);
		
		dm = new DataManager(this);
		tweets = dm.getAllTweets();
		ListView lv = (ListView)findViewById(R.id.listTweetList);
		ListAdapter la = new SavedNewsAdapter(this, tweets.toArray(new SavedTweet[tweets.size()]));
		lv.setAdapter(la);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_news, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onDestroy()
	{
		dm.close();
		super.onDestroy();
	}
}
