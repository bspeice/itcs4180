package edu.uncc.itcs4180.hw5;

import edu.uncc.itcs4180.hw5.twitter.Tweet;
import edu.uncc.itcs4180.hw5.twitter.TweetList;
import edu.uncc.itcs4180.hw5.twitter.TwitterClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TweetsListActivity extends Activity {
	
	private ProgressDialog dialog;
	private TweetList list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweets_list);
		
		// Fetch our tweets
		String handle = getIntent().getExtras().getString("handle");
		
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setMessage("Downloading tweets for: " + handle);
		dialog.show();
		
		// For whatever reason, we have to call the TwitterClient downloader ourselves.
		// AsyncTask inside AsyncTask simply doesn't work. This leads to an ugly double-new,
		// despite me trying to write a nice clean API.
		// Also, totally didn't know Java syntax allowed me to do this.
		new TwitterClient().new TweetListDownloader(){
			@Override
			protected void onPostExecute(TweetList result) {
				displayTweets(result);
				dialog.cancel();
			};
		}.execute(handle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweets_list, menu);
		return true;
	}
	
	public TweetList getTweetList() {
		return list;
	}
	
	private void displayTweets(TweetList list) {
		this.list = list;
		ListView lv = (ListView)findViewById(R.id.listTweetList);
		ListAdapter la = new TweetListAdapter(this, list.toArray(new Tweet[list.size()]));
		lv.setAdapter(la);
		Log.d("ClickListener", "Setting click listener!");
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos,
					long l) {
				Log.d("ClickListener", "You clicked me!");
				Intent i = new Intent(TweetsListActivity.this, DetailedTweetActivity.class);
				i.putExtra("tweet", getTweetList().get(pos));
				startActivity(i);
			}
		});
	}

}
