package edu.uncc.itcs4180.hw5.twitter;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 5
 * TwitterClient.java
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;

public class TwitterClient {
	
	private final String API_KEY = "HuEbvsXOxQsKVmTneyilVtsV6";
	private final String API_SECRET = "ZIJqmKEGoQUKLCIit15SKz6XIl4PP1xgDm1jVCIBSDhmImzFqk";
	
	private final String URL_TOKEN = "https://api.twitter.com/oauth2/token";
	private final String URL_API = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
	
	// Code mimicked in large part from:
	// https://github.com/Rockncoder/TwitterTutorial/blob/master/src/com/example/TwitterTutorial/MainActivity.java
	
	public TweetList getTweetList(String handle) {
		try {
			return new TweetListDownloader().execute(handle).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public class TweetListDownloader extends AsyncTask<String, Void, TweetList> {

		@Override
		protected TweetList doInBackground(String... params) {
			if (params.length > 0)
				return getTweetList(params[0]);
			else
				return null;					
		}
		
		private String getResponse(HttpRequestBase request) {
			// Does all our HTTP work for us
			StringBuilder sb = new StringBuilder();
			
			try {
				DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
				HttpResponse response;
				response = client.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				String reason = response.getStatusLine().getReasonPhrase();
				
				if (statusCode == 200) {
					
					HttpEntity entity = response.getEntity();
					InputStream input = entity.getContent();
					BufferedReader br = new BufferedReader(new InputStreamReader(input));
					String line = null;
					while ((line = br.readLine()) != null)
						sb.append(line);
					
				} else
					sb.append(reason);
				
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
			
			
			return sb.toString();
		}
		
		private TwitterAuthentication responseToAuth(String response) {
			TwitterAuthentication auth = null;
			if (response != null && response.length() > 0) {
				Gson gson = new Gson();
				auth = gson.fromJson(response, TwitterAuthentication.class);
			}
			
			return auth;
		}
		
		private TweetList responseToTweets(String response) {
			TweetList tweets = null;
			if (response != null && response.length() > 0) {
				Gson gson = new Gson();
				// We have to use TweetList because ArrayList<Tweet>.class doesn't exist
				tweets = gson.fromJson(response, TweetList.class);
			}
			return tweets;
		}
		
		private TweetList getTweetList(String handle) {
			try {
				// Set up our keys
				String urlApiKey = URLEncoder.encode(API_KEY, "UTF-8");
				String urlApiSecret = URLEncoder.encode(API_SECRET, "UTF-8");
				String combined = urlApiKey + ":" + urlApiSecret;
				String b64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
				
				// Get our token
				HttpPost post = new HttpPost(URL_TOKEN);
				post.setHeader("Authorization", "Basic " + b64Encoded);
				post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				post.setEntity(new StringEntity("grant_type=client_credentials"));
				TwitterAuthentication auth = responseToAuth(getResponse(post));
				
				// And now get our actual tweets list
				HttpGet get = new HttpGet(URL_API + handle);
				get.setHeader("Authorization", "Bearer " + auth.accessToken);
				get.setHeader("Content-Type", "application/json");
				return responseToTweets(getResponse(get));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
}
