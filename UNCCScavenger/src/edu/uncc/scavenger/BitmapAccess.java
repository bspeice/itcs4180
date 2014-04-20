package edu.uncc.scavenger;

/*
 * Bradlee Speice, Brandon Rodenmayer
 * ITIS 4180
 * UNCCScavenger (NinerFinder)
 * CompassActivity.java
 */

//Reference: stackoverflow.com/questions/19978100/how-to-save-bitmap-on-internal-storage-download-from-internet

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmapAccess 
{
	public static Bitmap loadBitmap(Context context, String picName)
	{
		Bitmap b = null;
		FileInputStream fis;
		
		try
		{
			fis = context.openFileInput(picName);
			b = BitmapFactory.decodeStream(fis);
			fis.close();
		}
		catch(FileNotFoundException e)
		{
			Log.d("BitmapAccess", "FileNotFound_Load");
		}
		catch(IOException e)
		{
			Log.d("BitmapAccess", "IOException_Load");
		}
		
		return b;
	}
	
	public static boolean saveBitmap(Context context, Bitmap b, String picName)
	{
		boolean saved = false;
		FileOutputStream fos;
		
		try
		{
			fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
			b.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		}
		catch(FileNotFoundException e)
		{
			Log.d("BitmapAccess", "FileNotFound_Save");
		}
		catch(IOException e)
		{
			Log.d("BitmapAccess", "IOException_Save");
		}
		
		return saved;
	}
}
