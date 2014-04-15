package edu.uncc.itcs4180.hw5;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * SavedNewsAdapter.java
 */

import edu.uncc.itcs4180.hw5.database.SavedTweet;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SavedNewsAdapter extends ArrayAdapter<SavedTweet>
{
	private final int TWEET_TAG_KEY = 1337;
	Activity activity;
	SavedTweet[] tweets;
	
	public SavedNewsAdapter(Activity activity, SavedTweet[] tweets) {
		super(activity, R.layout.saved_tweets_list, tweets);
		this.tweets = tweets;
		this.activity = activity;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		TweetView tv = null;
		SavedTweet tweet = tweets[position];
		
		if (rowView == null) {
			// Inflate a new row
			rowView = activity.getLayoutInflater().inflate(R.layout.saved_tweets_list, null);
			
			tv = new TweetView();
			tv.imgProfileImage = (ImageView) rowView.findViewById(R.id.imgProfileImage);
			tv.txtTweetText = (TextView) rowView.findViewById(R.id.txtTweetText);
			tv.txtTweetInfo = (TextView) rowView.findViewById(R.id.txtTweetInfo);
			tv.imgIsRetweet = (ImageView) rowView.findViewById(R.id.imgIsRetweet);
			
			rowView.setTag(tv);
			rowView.setTag(R.id.TWEET_TAG_KEY, tweet);
		} else {
			tv = (TweetView) rowView.getTag();
		}
		
		// Add information to the current row
		// Start up our BitmapDownloader - it will update the ImageView for us
		new BitmapDownloader(tv.imgProfileImage).execute(tweet.getProfileImageUrl());
		tv.txtTweetText.setText(tweet.getText());
		tv.txtTweetInfo.setText(tweet.getTime());
		//Set the retweet image
		if (tweet.getIsRetweet()==1) {
			tv.imgIsRetweet.setImageDrawable(activity.getResources().getDrawable(R.drawable.retweeted));
		}
		
		return rowView;
	}
	
	protected static class TweetView {
		ImageView imgProfileImage;
		TextView txtTweetText;
		TextView txtTweetInfo;
		ImageView imgIsRetweet;
	}
}
