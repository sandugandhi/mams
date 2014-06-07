package com.exercise.Dramila;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openintents.notepad.util.FileUriUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.exercise.Dramila.customersearch.service.CustSResultListener;
import com.exercise.Dramila.customersearch.service.CustSearchService;

/**
 * Business Partner Search activity.
 * 
 * @author gatisje
 */
public class CustomerSearch extends Activity implements
		CustSResultListener {
	
	/*
	 * credentials dialog id
	 */
	private static final int CREDENTIALS_DIALOG = 0;
	
	/*
	 * state keys
	 */
	private static final String STATE_BPNAME = "bp_name";
	private static final String STATE_USERNAME = "username";
	private static final String STATE_PASSWORD = "password";
	private static final String STATE_IS_LOADING = "is_loading";
	private static final String STATE_MESSAGE = "alert_message";
	private static final String STATE_SERVICE  = "service";
	
	/*
	 * view's
	 */
	private TextView titleTextView;
	private TextView mycustomer;
	private Button searchButton;
	
	/*
	 * Activity's loading state
	 */
	private Boolean isLoading = false;
	private ProgressDialog progressDialog;
	
	/*
	 * Alert dialog state
	 */
	private AlertDialog alertDialog;
	private String currentMessage;
	
	/*
	 * Auth credentials
	 */
	private String username = null;
	private String password = null;
	private File filename;

	private CustSearchService bpSearchService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent inte = getIntent();
		inte.setData(inte.getData());
		filename=FileUriUtils.getFile(inte.getData());
		setContentView(R.layout.search);
		findViews();
		addListeners();
		setCustomTypefaces();
		titleTextView.setText(R.string.search_title);
		initActivityState(savedInstanceState);
	}
	
	/**
	 * Add event listeners for activity's layout views 
	 */
	private void addListeners() {
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show loading dialog
				setLoadingState(true);
				// call search service
				callService();
			}
		});
	}
	
	/**
	 * Search customer by {@link #mycustomer} name and using
	 * credentials ({@link #username} and {@link #password})
	 */
	/*
	private int str2num(String str){
		int i=1,power=1;
		int strlen=str.length();// 214
		for (int j = 1; j <= (strlen-1); j++) {
			power=10*power;
		}
		for(int temp=strlen;temp>0;temp--)
		{		
		str.getBytes
		}
		return i; 
	}
	*/
	private void callService() {
		bpSearchService.searchByNum( mycustomer.getText().toString(),filename,
				username, password);
	}

	/**
	 * Change activity's loading state and show/hide loading dialog.
	 * 
	 * @param newState
	 *            loading flag
	 */
	protected void setLoadingState(Boolean newState) {
		synchronized (isLoading) {
			if (newState && !isLoading) {
				progressDialog = ProgressDialog.show(this, "Please wait...",
						"Loading...", true);
			} else if (!newState && progressDialog != null) {
				progressDialog.dismiss();
			}
			isLoading = newState;
		}
	}
	
	/**
	 * Find activity's layout views and save to class member fields
	 */
	private void findViews() {
		titleTextView = (TextView) findViewById(R.id.titleText);
		mycustomer = (TextView) findViewById(R.id.mycustomerInput);
		searchButton = (Button) findViewById(R.id.search);
	}

	/**
	 * Creates credentials dialog OK button click listener.
	 * 
	 * @param userInput
	 *            username input field
	 * @param pswInput
	 *            password input field
	 * @return created listener
	 */
	private DialogInterface.OnClickListener getCredentialsOKListener(
			final EditText userInput, final EditText pswInput) {
		return new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				// save user entered credentials
				username = userInput.getText().toString();
				password = pswInput.getText().toString();
				
				// set loading state and call BPS search service
				setLoadingState(true);
				callService();
			}
		};
	}
	
	/**
	 * Initializes activity state - creates new or restores previous (activity
	 * recreation caused by configuration changes - screen orientation changes). 
	 */
	private void initActivityState(Bundle savedInstanceState) {
		@SuppressWarnings("unchecked")
		Map<String, Object> state = (Map<String, Object>) getLastNonConfigurationInstance();
		// if state found (quick activity recreation on configuration changes) 
		if(state != null) {
			// restore state member fields
			username = (String) state.get(STATE_USERNAME);
 			password = (String) state.get(STATE_PASSWORD);
			bpSearchService = (CustSearchService) state.get(STATE_SERVICE);
			// restore business partner input text value
			mycustomer.setText((CharSequence) state.get(STATE_BPNAME));
			// subscribe for responses
			// if there is suspended response form previous activity's instance
			// it will be immediately posted for handling 
			bpSearchService.ready(this);
			// restore loading state (loading dialog will be shown if previous
			// instance was in middle of call)
			setLoadingState((Boolean) state.get(STATE_IS_LOADING));
			// show message if any
			currentMessage = (String) state.get(STATE_MESSAGE);
			if(currentMessage != null) {
				showMessage(currentMessage);
			}
		} else { 
			// restore fields form saved state if applicable
			if(savedInstanceState != null) {
				String partner = savedInstanceState.getString(STATE_USERNAME);
				if(partner != null) {
					mycustomer.setText(partner);
				}
				username = savedInstanceState.getString(STATE_USERNAME);
				password = savedInstanceState.getString(STATE_PASSWORD);
			}
			// create new search service instance
			bpSearchService = new CustSearchService(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAuthRequied() {
		// hide loading dialog
		setLoadingState(false);
		// show credentials dialog
		showDialog(CREDENTIALS_DIALOG);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater factory = LayoutInflater.from(this);
		switch (id) {
		case CREDENTIALS_DIALOG:
			final View credentialsEntryView = factory.inflate(
					R.layout.alert_dialog_credentials_entry, null);
			
			// create credentials input dialog
			return new AlertDialog.Builder(CustomerSearch.this) //
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Enter credentials")
				.setView(credentialsEntryView)
				.setPositiveButton("Ok", getCredentialsOKListener(
										(EditText) credentialsEntryView
												.findViewById(R.id.username),
										(EditText) credentialsEntryView
												.findViewById(R.id.password)))
				.setNegativeButton("Cancel", null).create();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onError(final Exception error) {
		// hide loading dialog
		setLoadingState(false);
		// show error alert
		showError(error);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onResponse(List<String> response) {
	//	if (response == null || response.isEmpty()) {
		if (response == null){
		// alert user
			showMessage("No results found"); 
		} else { // start business partner result list activity and pass data to it
			
			Intent openPersonListIntent = new Intent(
					CustomerSearch.this, CustomerDetailList.class);
			
			// create bundle for passing data to list activity
			Bundle bundle = new Bundle();
			bundle.putStringArrayList(CustomerDetailList.CUSTOMERS_LIST,
				new ArrayList<String>(response));
//			bundle.putString(CustomerDetailList.CUSTOMERS_LIST, new String(response));
//			openPersonListIntent.putExtras(bundle);
			
			// start business partner list activity
			startActivity(openPersonListIntent);
		}
		// hide loading dialog
		setLoadingState(false);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		// stop receiving notifications as activity is not visible or will be destroyed 
		bpSearchService.dismissListener();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		// start receiving notifications as activity becomes visible 
		bpSearchService.ready(this);
	}  
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// if progress dialog is shown, it must be removed 
		if(isShowing(progressDialog)) {
			progressDialog.dismiss();
		}
		// if message alert dialog is shown, it must be removed 
		if(isShowing(alertDialog)) {
			alertDialog.dismiss();
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		// prepare state data to be passed to new activity instance 
		Map<String, Object> state = new HashMap<String, Object>();
		state.put(STATE_BPNAME, mycustomer.getText());
		state.put(STATE_USERNAME, username);
		state.put(STATE_PASSWORD, password);
		state.put(STATE_IS_LOADING, isLoading);
		state.put(STATE_SERVICE, bpSearchService);
		if(isShowing(alertDialog)) {
			state.put(STATE_MESSAGE, currentMessage);
		}
		return state;
	}
	
	
	/**
	 * Set custom sap font to text views
	 */
	private void setCustomTypefaces() {
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/SAP-SANS2002-Bold.ttf");
		// set custom font for title
		titleTextView.setTypeface(face);
	}
	
	/**
	 * Show error with alert
	 * 
	 * @param error
	 *            error to show
	 */
	private void showError(Exception error) {
		showMessage(error.getMessage());
	}
	
	/**
	 * Show alert message to user
	 * 
	 * @param message
	 *            message to show
	 */
	private void showMessage(String message) {
		currentMessage = message;
		alertDialog = new AlertDialog.Builder(this)
	      .setMessage(message)
	      .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					currentMessage = null;
				}
			})
	      
	      .show();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// persist user entered data
		outState.putString(STATE_BPNAME, mycustomer.getText().toString());
		outState.putString(STATE_USERNAME, username);
		outState.putString(STATE_PASSWORD, password);
	}
	
	/**
	 * @param dialog
	 *            dialog to check
	 * @return <code>true</code> if dialog not null and is visible or
	 *         <code>false</code> otherwise
	 */
	private boolean isShowing(Dialog dialog) {
		return dialog != null && dialog.isShowing();
	}
}