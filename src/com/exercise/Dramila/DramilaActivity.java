package com.exercise.Dramila;

import org.openintents.samples.TestFileManager.TestFileManager;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * Application launcher activity.
 * 
 * @author sandesh
 */
public class DramilaActivity extends Activity {
	
	
	private static final int REQUEST_SEARCH = 1001;
	
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setCustomTypefaces();
		
		// show search screen after 2 sec's
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startBPSActivity();
				
			}
		}, 2000L);
	}
	
	private void setCustomTypefaces() {
		// load sap font
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/SAP-SANS2002-Bold.ttf");
		
		// set font for text views in about layout
		((TextView)findViewById(R.id.AboutAppName)).setTypeface(face);
		((TextView)findViewById(R.id.AboutAppVersion)).setTypeface(face);
		((TextView)findViewById(R.id.AboutEmail)).setTypeface(face);
	}
	
	private void startBPSActivity() {
		Intent openPersonListIntent = new Intent(
				DramilaActivity.this, TestFileManager.class);
		
		// start Business Partner Search activity 
		// #onActivityResult will be called when started activity finishes
		startActivityForResult(openPersonListIntent, REQUEST_SEARCH);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// exit application when BPS activity finishes
		
		switch(requestCode){
		case REQUEST_SEARCH:
				{
					finish();
				
				}
			}
		}
	
}
