package com.lcram.conversions;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ConversionsActivity extends Activity 
				implements OnItemSelectedListener, OnEditorActionListener,
				OnClickListener{
	
	private TextView conversionLabel;
	private Spinner conversionSpinner;
	private TextView fromLabel;
	private EditText fromEditText;
	private TextView toLabel;
	private TextView toTextView;
	private String fromString;
	private String toString;
	private float ratio;
	private float fromValue = 0;
	private float toValue = 0;
	private TextView EURLabel;
	private EditText EUREditText;
	private TextView GBPLabel;
	private EditText GBPEditText;
	private TextView JPYLabel;
	private EditText JPYEditText;
	private Button ApplyButton;
	private float GBPRate = 1.75f;
	private float EURRate = 1.35f;
	private float JPYRate = .00105f;
	private int convtype = 0;
	private SharedPreferences prefs;
	
	private int[] actPos = new int[] {};
	
	NumberFormat f = NumberFormat.getNumberInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversions);
		
		conversionSpinner = (Spinner) findViewById(R.id.conversionSpinner);
		fromLabel = (TextView) findViewById(R.id.fromLabel);
		fromEditText = (EditText) findViewById(R.id.fromEditText);
		toLabel = (TextView) findViewById(R.id.toLabel);
		toTextView = (TextView) findViewById(R.id.toTextView);
		GBPLabel = (TextView) findViewById(R.id.GBPTitleLabel);
		GBPEditText = (EditText) findViewById(R.id.GBPRateEditText);
		EURLabel = (TextView) findViewById(R.id.EURTitleLabel);
		EUREditText = (EditText) findViewById(R.id.EURRateEditText);
		JPYLabel = (TextView) findViewById(R.id.JPYTitleLabel);
		JPYEditText = (EditText) findViewById(R.id.JPYRateEditText);
		ApplyButton = (Button) findViewById(R.id.ApplyButton);

		toTextView.setText("");
		
		GBPLabel.setVisibility(View.GONE);
		GBPEditText.setVisibility(View.GONE);
		EURLabel.setVisibility(View.GONE);
		EUREditText.setVisibility(View.GONE);
		JPYLabel.setVisibility(View.GONE);
		JPYEditText.setVisibility(View.GONE);
		ApplyButton.setVisibility(View.GONE);
		
		setSpinner();
		
		
		
		
		conversionSpinner.setOnItemSelectedListener(this);
		fromEditText.setOnEditorActionListener(this);
		ApplyButton.setOnClickListener(this);
		
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		Editor ed = prefs.edit();
		ed.putInt("Last Position", conversionSpinner.getSelectedItemPosition());		
		ed.putFloat("GBPRate", GBPRate);
		ed.putFloat("EURRate", EURRate);
		ed.putFloat("EURRate", EURRate);
		ed.putFloat("fromValue", fromValue);
		ed.commit();
	}
	public void onResume() {
		super.onResume();
		
		convtype = Integer.parseInt(prefs.getString("pref_convtype", "0"));
		setSpinner();		

		conversionSpinner.setSelection(prefs.getInt("Last Position", 0));		
		GBPRate = prefs.getFloat("GBPRate", GBPRate);
		GBPEditText.setText(String.valueOf(GBPRate));
		EURRate = prefs.getFloat("EURRate", EURRate);
		EUREditText.setText(String.valueOf(EURRate));
		JPYRate = prefs.getFloat("JPYRate", JPYRate);
		JPYEditText.setText(String.valueOf(JPYRate));
		fromValue = prefs.getFloat("fromValue", fromValue);
		fromEditText.setText(String.valueOf(fromValue));
		
		calcAndDisplay();
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.conversions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(getApplicationContext(), 
										SettingsActivity.class));
			return true;
		} else if(id == R.id.menu_exchangerates) {
			GBPEditText.setText(String.valueOf(GBPRate));
			EUREditText.setText(String.valueOf(EURRate));
			JPYEditText.setText(String.valueOf(JPYRate));
			GBPLabel.setVisibility(View.VISIBLE);
			GBPEditText.setVisibility(View.VISIBLE);
			EURLabel.setVisibility(View.VISIBLE);
			EUREditText.setVisibility(View.VISIBLE);
			JPYLabel.setVisibility(View.VISIBLE);
			JPYEditText.setVisibility(View.VISIBLE);
			ApplyButton.setVisibility(View.VISIBLE);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		switch (convtype) {
			case 0:
				actPos = new int[] { 0,1,2,3,4,5,6,7,8 };
				break;
			case 1: 
				actPos = new int[] { 0,2,4,6,7,8 };
				break;
			case 2:
				actPos = new int[] { 1,3,5,6,7,8 };
				break;
			case 3:
				actPos = new int[] { 6,7,8 };
				break;
		}
		
		switch(actPos[position]){
			case 0:
				fromString = "Miles";
				toString = "Kilometers";
				ratio = 1.6093f;
				break;
			case 1:
				fromString = "Kilometers";
				toString = "Miles";
				ratio = 0.6214f;
				break;
			case 2:
				fromString = "Inches";
				toString = "Centimeters";
				ratio = 2.54f;
				break;
			case 3:
				fromString = "Centimeters";
				toString = "Inches";
				ratio = 0.3937f;
				break;
			case 4:
				fromString = "Farenheit";
				toString = "Celsius";
				break;
			case 5:
				fromString = "Celsius";
				toString = "Farnheit";
				break;
			case 6:
				fromString = "GBP";
				toString = "USD";
				ratio = GBPRate;
				break;
			case 7:
				fromString = "Euros";
				toString = "USD";
				ratio = EURRate;
				break;
			case 8:
				fromString = "Yen";
				toString = "USD";
				ratio = JPYRate;
				break;			
		}
		fromLabel.setText(fromString);
		toLabel.setText(toString);
		toTextView.setText(String.valueOf(f.format(0)));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if(actionId == EditorInfo.IME_ACTION_DONE || 
				actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
			calcAndDisplay();
		}
		return false;
	}
	
	private void calcAndDisplay(){
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
		
		try{
			fromValue = Float.parseFloat(fromEditText.getText().toString());
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			fromValue = 0f;
		}		
		if(fromLabel.getText().toString().equalsIgnoreCase("Farenheit"))
		{
			toValue = (fromValue - 32.0f) * (5.0f / 9.0f);			
		} else if (fromLabel.getText().toString().equalsIgnoreCase("Celsius"))
		{
			toValue = (fromValue * 9.0f / 5.0f) + 32.0f;	
		}else {
			if(fromValue < 0) {
				Toast.makeText(this, "Value cannot be negative.", Toast.LENGTH_LONG).show();
			} else {
				toValue = fromValue * ratio;
			}
		}
		toTextView.setText(String.valueOf(f.format(toValue)));
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.ApplyButton){
			GBPRate = Float.parseFloat(GBPEditText.getText().toString());
			EURRate = Float.parseFloat(EUREditText.getText().toString());
			JPYRate = Float.parseFloat(JPYEditText.getText().toString());
			GBPLabel.setVisibility(View.GONE);
			GBPEditText.setVisibility(View.GONE);
			EURLabel.setVisibility(View.GONE);
			EUREditText.setVisibility(View.GONE);
			JPYLabel.setVisibility(View.GONE);
			JPYEditText.setVisibility(View.GONE);
			ApplyButton.setVisibility(View.GONE);
			
			if (actPos[conversionSpinner.getSelectedItemPosition()] == 6) {
				ratio = GBPRate;
			}
			if (actPos[conversionSpinner.getSelectedItemPosition()] == 7) {
				ratio = EURRate;
			}
			if (actPos[conversionSpinner.getSelectedItemPosition()] == 8) {
				ratio = JPYRate;
			}
			calcAndDisplay();
		}
	}
	
	private void setSpinner() {
		ArrayAdapter<CharSequence> adapter;
		
		switch (convtype) {
			case 1:
				adapter = ArrayAdapter.createFromResource(this, R.array.convenglish, 
							android.R.layout.simple_spinner_item);
				break;
			case 2:
				adapter = ArrayAdapter.createFromResource(this, R.array.convmetric, 
							android.R.layout.simple_spinner_item);
				break;
			case 3:
				adapter = ArrayAdapter.createFromResource(this, R.array.convcurrency, 
							android.R.layout.simple_spinner_item);
				break;
			default:
				adapter = ArrayAdapter.createFromResource(this, R.array.conversions, 
							android.R.layout.simple_spinner_item);				
				break;				
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		conversionSpinner.setAdapter(adapter);
	}
}
