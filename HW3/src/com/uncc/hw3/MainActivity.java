package com.uncc.hw3;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 3
 * MainActivity.java
 */

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity
{
	
	ImageView iv;
	public int [] imageViews = {R.id.imgTile1_1, R.id.imgTile1_2, R.id.imgTile1_3, R.id.imgTile1_4,
								R.id.imgTile2_1, R.id.imgTile2_2, R.id.imgTile2_3, R.id.imgTile2_4,
								R.id.imgTile3_1, R.id.imgTile3_2, R.id.imgTile3_3, R.id.imgTile3_4,
								R.id.imgTile4_1, R.id.imgTile4_2, R.id.imgTile4_3, R.id.imgTile4_4,};
	public int[] iconIds = {R.drawable.diamond, R.drawable.garnet, R.drawable.gem, R.drawable.pearl, R.drawable.ruby, R.drawable.sapphire, R.drawable.swarovski, R.drawable.toppaz};
	static ArrayList<Tile> tiles;
	static ArrayList<Tile> focusImages;
	Handler handler = new Handler();
	static int focusIndex;
	static long startTime, endTime;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// If the result activity calls us with an EXIT in the extras, quit.
		if (getIntent().getBooleanExtra("EXIT", false))
			finish();
		
		newGame((View)findViewById(R.id.LinearLayout1));
		focusImages.get(focusIndex).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void newGame(View v)
	{
		tiles = new ArrayList<Tile>();
		focusImages = new ArrayList<Tile>();
		ArrayList<Tile> focusImagesTemp = new ArrayList<Tile>();
		
		//Create list of board tiles and tiles to find
		for(int x: iconIds)
		{
			tiles.add(new Tile(null, x));
			tiles.add(new Tile(null, x));
			focusImagesTemp.add(new Tile((ImageView)findViewById(R.id.imgResult), x));
		}
		
		//Shuffle  Tiles
		Collections.shuffle(tiles);
		Collections.shuffle(focusImagesTemp);
		
		//Set focusImages list
		for(int x=0; x<focusImagesTemp.size(); x++)
		{
			focusImages.add(focusImagesTemp.get(x));
			focusImages.add(focusImagesTemp.get(x));
		}
		
		//Assign ImageViews id to the tiles and assign listener to all imageViews
		for(int x=0; x<imageViews.length; x++)
		{
			tiles.get(x).setImageView((ImageView)findViewById(imageViews[x]));
			tiles.get(x).reset();
			((ImageView)findViewById(imageViews[x])).setOnClickListener(tiles.get(x));
		}
		
		//reset focus images and show
		focusIndex = 0;
		focusImages.get(focusIndex).show();
		
		startTime = System.currentTimeMillis();
	}
	
	public void uncover(View v)
	{
		//Show all of the tiles
		for(int x=0; x<imageViews.length; x++)
		{
			tiles.get(x).show();
		}
		handler.postDelayed(new Runnable() {
			public void run() {
				for(int x=0; x<imageViews.length; x++)
				{
					//Cover the tiles that aren't already found
					if(!tiles.get(x).getMatched())
					{
						tiles.get(x).cover();
					}
				}
			}
		}, 1000);
	}
}
