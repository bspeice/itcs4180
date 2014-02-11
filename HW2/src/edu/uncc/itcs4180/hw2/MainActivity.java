package edu.uncc.itcs4180.hw2;

import java.text.NumberFormat;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

// Our main activity implements a lot of different functionality.
// If we had even more things we needed to add, likely need to split off
// into separate files. As it stands though, for purpose of project,
// easier to leave it in one area.
public class MainActivity extends Activity implements OnCheckedChangeListener,
		OnSeekBarChangeListener, View.OnClickListener, TextWatcher 
{
	final static double TEN_PERCENT = .10;
	final static double TWENTY_FIVE_PERCENT = .25;
	final static double FIFTY_PERCENT = .50;
	RadioGroup percents;
	SeekBar customBar;
	Button exit;
	EditText listPrice;
	TextView saved, paid, customPercent;
	NumberFormat currencyFormat, percentFormat;
	int currentCustomPercent;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		currencyFormat = NumberFormat.getCurrencyInstance();
		percentFormat = NumberFormat.getPercentInstance();
		
		customPercent = (TextView) findViewById(R.id.lblCustomPercentValue);
		customPercent.setText(percentFormat.format(0));
		saved = (TextView) findViewById(R.id.lblUserSavesValue);
		paid = (TextView) findViewById(R.id.lblUserPaysValue);

		customBar = (SeekBar) findViewById(R.id.seekCustom);
		customBar.setOnSeekBarChangeListener(this);

		percents = (RadioGroup) findViewById(R.id.rdgDiscountPercent);
		percents.setOnCheckedChangeListener(this);
		
		listPrice = (EditText) findViewById(R.id.txtListedPrice);
		listPrice.addTextChangedListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onProgressChanged(SeekBar bar, int progress, boolean fromUser) 
	{
		customPercent.setText(percentFormat.format(progress/100.0));
		currentCustomPercent = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar bar) 
	{
		// No need
	}

	@Override
	public void onStopTrackingTouch(SeekBar bar)
	{
		bar.setSecondaryProgress(bar.getProgress());
		updateCost();
	}

	@Override
	public void onClick(View v) 
	{
		// Exit button is the only thing to click on this application
		finish();
	}

	@Override
	public void onCheckedChanged(RadioGroup rg, int checkedId) 
	{
		updateCost();
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		updateCost();
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		updateCost();
		if(listPrice.getText().toString().trim().equals(""))
		{
			listPrice.setError("Enter List Price");
		}
	}
	
	public void updateCost()
	{
		double savedAmount;
		double paidAmount;
		try {
			double startPrice = Double.parseDouble(listPrice.getText().toString());
			if (((RadioButton)findViewById(R.id.rad10Pct)).isChecked()) 
			{
				savedAmount=startPrice * TEN_PERCENT;
				paidAmount=startPrice * (1 - TEN_PERCENT);
			} 
			else if (((RadioButton)findViewById(R.id.rad25Pct)).isChecked())  
			{
				savedAmount=startPrice * TWENTY_FIVE_PERCENT;
				paidAmount=startPrice * (1 - TWENTY_FIVE_PERCENT);
			} 
			else if (((RadioButton)findViewById(R.id.rad50Pct)).isChecked()) 
			{
				savedAmount=startPrice * FIFTY_PERCENT;
				paidAmount=startPrice * (1 - FIFTY_PERCENT);
			} 
			else 
			{
				savedAmount=startPrice * currentCustomPercent/100;
				paidAmount=startPrice * (1 - currentCustomPercent/100);
			} 
		} 
		catch (Exception e) 
		{
			if(listPrice.getText().toString().trim().equals(""))
			{
				listPrice.setError("Enter List Price");
			}
			savedAmount=0;
			paidAmount=0;
		}
		saved.setText(currencyFormat.format(savedAmount));
		paid.setText(currencyFormat.format(paidAmount));
	}
}
