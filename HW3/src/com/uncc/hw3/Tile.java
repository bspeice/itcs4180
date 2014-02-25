package com.uncc.hw3;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * Homework 3
 * ResultActivity.java
 */

public class Tile implements View.OnClickListener
{
	private ImageView iv;
	private int id;
	private boolean show;
	private boolean matched;
	private boolean touchEnabled;
	Handler handler= new Handler();
	
	public Tile(ImageView iv, int id) 
	{
		this.iv = iv;
		this.id = id;
		show = false;
		matched = false;
		touchEnabled = true;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public boolean getShow()
	{
		return show;
	}
	
	public void show()
	{
		show = true;
		touchEnabled = false;
		iv.setImageResource(id);
	}
	
	public void cover()
	{
		show = false;
		touchEnabled = true;
		iv.setImageResource(R.drawable.cover);
	}
	
	public void reset()
	{
		show = false;
		touchEnabled = true;
		matched = false;
		iv.setImageResource(R.drawable.cover);
	}
	
	public boolean touchEnabled()
	{
		return touchEnabled;
	}
	
	public boolean getMatched()
	{
		return matched;
	}
	
	public void setMatched()
	{
		matched = true;
	}
	
	public void setImageView(ImageView iv)
	{
		this.iv = iv;
	}
	
	public void onClick(View v)
	{
		if(!matched)
		{
			if(touchEnabled)
			{
				show();
				if(id==MainActivity.focusImages.get(MainActivity.focusIndex).getId())
				{
					setMatched();
					try
					{
						MainActivity.focusIndex++;
						MainActivity.focusImages.get(MainActivity.focusIndex).show();
					}
					catch(Exception e)
					{
						Intent intent = new Intent(iv.getContext(), ResultActivity.class);
						MainActivity.endTime = System.currentTimeMillis();
						intent.putExtra("ELAPSED_TIME", (float)((MainActivity.endTime - MainActivity.startTime)/1000.0));
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						iv.getContext().startActivity(intent);
					}
				}
				else
				{
					handler.postDelayed(new Runnable() {
						public void run() {
							cover();
						}
					}, 1000);
				}
			}
		}
	}
}
