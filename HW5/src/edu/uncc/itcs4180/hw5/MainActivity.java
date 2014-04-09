package edu.uncc.itcs4180.hw5;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.uncc.itcs4180.hw5.twitter.TweetList;
import edu.uncc.itcs4180.hw5.twitter.TwitterClient;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final Map<String, String> newsSites;
	private static final String[] newsSitesTitles;
	static {
		Map<String, String> mMap = new LinkedHashMap<String, String>();
		// Put news sites here
		mMap.put("CNN Breaking News", "cnnbrk");
		mMap.put("The New York Times", "nytimes");
		mMap.put("NBA", "nba");
		mMap.put("BBC Breaking News", "bbcbreaking");
		
		newsSites = Collections.unmodifiableMap(mMap);
		newsSitesTitles = newsSites.keySet().toArray(new String[newsSites.size()]);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set up all the news feeds
		ListView feeds = (ListView)findViewById(R.id.listNewsFeeds);
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.news_site, R.id.txtSiteName, newsSitesTitles);
		feeds.setAdapter(adapter);
		feeds.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String key = ((TextView)view.findViewById(R.id.txtSiteName)).getText().toString();
				String handle = newsSites.get(key);
				// Shenanigans to get the parent instance
				// http://stackoverflow.com/questions/2076037/inside-onclicklistener-i-cannot-access-a-lot-of-things-how-to-approach
				Intent i = new Intent(MainActivity.this, TweetsListActivity.class);
				i.putExtra("handle", handle);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onViewSaved(View v) {
		
	}
	
	public void onClearSaved(View v) {
		// TODO: Implement the database clearing functionality here.
		Toast.makeText(this, "All Saved News are Cleared!", Toast.LENGTH_SHORT).show();
	}
	
}
