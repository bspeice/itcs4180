package edu.uncc.itcs4180.hw5;

import edu.uncc.itcs4180.hw5.twitter.Tweet;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedTweetActivity extends Activity {
	
	private Tweet tweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_tweet);
		
		tweet = (Tweet)getIntent().getExtras().getSerializable("tweet");
		
		TextView userName = (TextView)findViewById(R.id.txtDetailUsername);
		TextView tweetText = (TextView)findViewById(R.id.txtDetailTweet);
		TextView favoritesCount = (TextView)findViewById(R.id.txtFavoritesCount);
		TextView retweetCount = (TextView)findViewById(R.id.txtRetweetCount);
		ImageView profileBackground = (ImageView)findViewById(R.id.imgDetailBackground);
		
		userName.setText(tweet.getUser().getName());
		tweetText.setText(tweet.getText());
		favoritesCount.setText("Favorites Count: " + tweet.getFavoriteCount());
		retweetCount.setText("ReTweets Count: " + tweet.getRetweetCount());
		
		new BitmapDownloader(profileBackground).execute(tweet.getUser().getProfileBackgroundImageUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detailed_tweet, menu);
		return true;
	}
	
	public void onClickBack(View v) {
		finish();
	}

}
