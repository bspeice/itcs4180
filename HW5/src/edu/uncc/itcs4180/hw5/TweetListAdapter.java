package edu.uncc.itcs4180.hw5;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * TweetListAdapter.java
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;

import edu.uncc.itcs4180.hw5.database.DataManager;
import edu.uncc.itcs4180.hw5.database.SavedTweet;
import edu.uncc.itcs4180.hw5.twitter.Tweet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TweetListAdapter extends ArrayAdapter<Tweet> {
	private final int TWEET_TAG_KEY = 1337;
	Activity activity;
	Tweet[] tweets;
	private static DataManager dm;
	
	public TweetListAdapter(Activity activity, Tweet[] tweets) {
		super(activity, R.layout.tweet_list, tweets);
		this.tweets = tweets;
		this.activity = activity;
		dm = new DataManager(activity.getBaseContext());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		TweetView tv = null;
		Tweet tweet = tweets[position];
		
		if (rowView == null) {
			// Inflate a new row
			rowView = activity.getLayoutInflater().inflate(R.layout.tweet_list, null);
			
			tv = new TweetView();
			tv.imgProfileImage = (ImageView) rowView.findViewById(R.id.imgProfileImage);
			tv.txtTweetText = (TextView) rowView.findViewById(R.id.txtTweetText);
			tv.txtTweetInfo = (TextView) rowView.findViewById(R.id.txtTweetInfo);
			tv.imgIsRetweet = (ImageView) rowView.findViewById(R.id.imgIsRetweet);
			tv.ibtnSaveTweet = (ImageButton) rowView.findViewById(R.id.ibtnSaveTweet);
			
			rowView.setTag(tv);
			rowView.setTag(R.id.TWEET_TAG_KEY, tweet);
		} else {
			tv = (TweetView) rowView.getTag();
		}
		
		// Add information to the current row
		// Start up our BitmapDownloader - it will update the ImageView for us
		new BitmapDownloader(tv.imgProfileImage).execute(tweet.getUser().getProfileImageUrl());
		tv.txtTweetText.setText(tweet.getText());
		tv.txtTweetInfo.setText(tweet.getDateCreated());
		// Set the retweet image
		if (tweet.isRetweet()) {
			tv.imgIsRetweet.setImageDrawable(activity.getResources().getDrawable(R.drawable.retweeted));
		}
		// Save tweets when we are clicked
		tv.ibtnSaveTweet.setTag(tweet);
		tv.ibtnSaveTweet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveTweet((Tweet)v.getTag());
			}
		});
		
		return rowView;
	}
	
	protected static class TweetView {
		ImageView imgProfileImage;
		TextView txtTweetText;
		TextView txtTweetInfo;
		ImageView imgIsRetweet;
		ImageButton ibtnSaveTweet;
	}
	
	private void saveTweet(Tweet t) {
		SavedTweet save = new SavedTweet();
		save.setUsername(t.getUser().getName());
		save.setText(t.getText());
		save.setTime(t.getDateCreated());
		save.setProfileImageUrl(t.getUser().getProfileImageUrl());
		save.setIsRetweet((t.isRetweet()) ? 1 : 0);
		dm.saveTweet(save);
		Toast.makeText(activity, "Saved in DB!", Toast.LENGTH_SHORT).show();
	}

}
