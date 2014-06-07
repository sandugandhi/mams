package com.exercise.Dramila;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.exercise.Dramila.customerlist.adapters.CustomerDetailListAdapter;
/**
 * Customer List activity.
 * 
 * @author sandesh
 */
public class CustomerDetailList extends Activity {
	
	/**
	 * partners list key
	 */
	public static final String CUSTOMERS_LIST = "customers_list";
	
	/*
	 * view's
	 */
	private TextView titleTextView;
	private ListView userListView;
	private Button backButton;
	private Button nextButton;
	private Button prevButton;
	private Button saveButton;	
	protected EditText c_rdg;
	
	
	/*
	 * partner list adapter
	 */
	private CustomerDetailListAdapter customerDetailListAdaptor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_list);
		findViews();
		addListeners();
		setCustomTypefaces();
		titleTextView.setText(R.string.list_title);
		setupListAdapter();
		List<String> people = getIntent().getExtras().getStringArrayList(
							CustomerDetailList.CUSTOMERS_LIST);
		customerDetailListAdaptor.setData(people);
	
	}
	
	/**
	 * Find activity's layout views and save to class member fields
	 */
	private void findViews() {
		titleTextView = (TextView) findViewById(R.id.titleText);
		userListView = (ListView) findViewById(R.id.personList);
		backButton = (Button) findViewById(R.id.back);
		saveButton = (Button) findViewById(R.id.savetofile);
		nextButton = (Button) findViewById(R.id.next);
		prevButton = (Button) findViewById(R.id.prev);
	    c_rdg = (EditText) findViewById(R.id.crnt_rdg);

	}
	
	/**
	 * Add event listeners for activity's layout views 
	 */
	private void addListeners() {
		// add click event handler for back button
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// add click event handler for save button
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//finish();
			}
		});
		
		// add click event handler for next button
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RelData.nextRecord();
				customerDetailListAdaptor.setData(RelData.mlistresult);
			}
		});
		
		// add click event handler for previous button
		prevButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RelData.prevRecord();
				customerDetailListAdaptor.setData(RelData.mlistresult);
			}
		});
		
		// add click event for save button
		ImageButton buttonSave = (ImageButton) findViewById(R.id.imagebutton_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				RelData.validate_and_write(c_rdg.getText().toString().trim());
				RelData.processtext(RelData.getrecNumber());
				customerDetailListAdaptor.setData(RelData.mlistresult);	
			}
        });
     
        c_rdg.setOnClickListener(new View.OnClickListener() {
            @Override
        	public void onClick(View arg0){
        	
            	if (c_rdg.getText().toString().contains("Enter")){
            	c_rdg.setText(R.string.nullstr);
            	}
          	}   	
        });
     

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
	 * Setup customer list contents
	 */
	/**
	 * Setup customer list contents
	 */
	private void setupListAdapter() {
		customerDetailListAdaptor = new CustomerDetailListAdapter(this, R.layout.person);
		userListView.setAdapter(customerDetailListAdaptor);
		
	}
	private void updateListAdaptor(List<String> people){
	//	List<String> people = getIntent().getExtras().getStringArrayList(
	//			CustomerDetailList.CUSTOMERS_LIST);
		customerDetailListAdaptor.setData(people);	
		}


}

