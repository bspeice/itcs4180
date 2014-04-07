package edu.uncc.itcs4180.hw5;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.uncc.itcs4180.hw5.twitter.TweetList;
import edu.uncc.itcs4180.hw5.twitter.TwitterClient;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private static final Map<String, String> newsSites;
	static {
		Map<String, String> mMap = new HashMap<String, String>();
		mMap.put("CNN Breaking News", "cnnbrk");
		
		newsSites = Collections.unmodifiableMap(mMap);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
