/* 
 * Copyright (C) 2008 OpenIntents.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openintents.samples.TestFileManager;

import java.io.File;
import java.util.ArrayList;

import org.openintents.intents.FileManagerIntents;
import org.openintents.notepad.util.FileUriUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.exercise.Dramila.CustomerDetailList;
import com.exercise.Dramila.R;
import com.exercise.Dramila.RelData;

public class TestFileManager extends Activity {
	
	protected static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 10;
	protected EditText mEditText;
	
	/*
	 * Activity's loading state
	 */
	private Boolean isLoading = false;
	private ProgressDialog progressDialog;
	
	private static final String STATE_IS_LOADING = "is_loading";
	
	/*
	 * Alert dialog state
	 */
	private AlertDialog alertDialog;
	private String currentMessage;
	

	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
   
    	setContentView(R.layout.main);
        mEditText = (EditText) findViewById(R.id.file_path);
        
       
        ImageButton buttonFileManager = (ImageButton) findViewById(R.id.file_manager);
        buttonFileManager.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				openFile();
			}
        });
        
        Button button = (Button) findViewById(R.id.open);
        button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				openFile();		
			}
        });

        button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				saveFile();
			}
        });

        button = (Button) findViewById(R.id.pick_directory);
        button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				pickDirectory();
			}
        });
    
                
    }
/*
    private File getSdCardPath() {
    	return android.os.Environment
			.getExternalStorageDirectory();
    }
*/    

 	    
    
/*
private void openFromSdCard() {

	File sdcard = getSdCardPath();
	String directory = sdcard.getAbsolutePath();
	if (!directory.endsWith("/")) {
		directory += "/";
	}
	Uri uri = FileUriUtils.getUri(directory);
	
//	Intent i = new Intent(this, DialogHostingActivity.class);
//	i.putExtra(DialogHostingActivity.EXTRA_DIALOG_ID, DialogHostingActivity.DIALOG_ID_OPEN);
//	i.setData(uri);
	
	
//	startActivityForResult(i, REQUEST_CODE_OPEN);
//	startActivityForResult(i, PICK.FILE);
}
*/
    
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
     * Opens the file manager to select a file to open.
     */
    private void openFile() {
		String fileName = mEditText.getText().toString();
		
		Intent intent = new Intent(FileManagerIntents.ACTION_PICK_FILE);
		
		// Construct URI from file name.
		intent.setData(Uri.parse("file://" + fileName));
	
		// Set fancy title and button (optional)
		intent.putExtra(FileManagerIntents.EXTRA_TITLE, getString(R.string.open_title));
		intent.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, getString(R.string.open_button));
		
		try {
			startActivityForResult(intent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
			
			
		} catch (ActivityNotFoundException e) {
			// No compatible file manager was found.
			Toast.makeText(this, R.string.no_filemanager_installed, 
					Toast.LENGTH_SHORT).show();
		}
	}

    /**
     * Opens the file manager to select a location for saving a file.
     */
    private void saveFile() {
		String fileName = mEditText.getText().toString();
		
		Intent intent = new Intent(FileManagerIntents.ACTION_PICK_FILE);
		
		// Construct URI from file name.
		intent.setData(Uri.parse("file://" + fileName));
		
		// Set fancy title and button (optional)
		intent.putExtra(FileManagerIntents.EXTRA_TITLE, getString(R.string.save_title));
		intent.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, getString(R.string.save_button));
		
		try {
			startActivityForResult(intent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
		} catch (ActivityNotFoundException e) {
			// No compatible file manager was found.
			Toast.makeText(this, R.string.no_filemanager_installed, 
					Toast.LENGTH_SHORT).show();
		}
	}

    /**
     * Opens the file manager to pick a directory.
     */
    private void pickDirectory() {
		String fileName = mEditText.getText().toString();
		
		// Note the different intent: PICK_DIRECTORY
		Intent intent = new Intent(FileManagerIntents.ACTION_PICK_DIRECTORY);
		
		// Construct URI from file name.
		intent.setData(Uri.parse("file://" + fileName));
		
		// Set fancy title and button (optional)
		intent.putExtra(FileManagerIntents.EXTRA_TITLE, getString(R.string.pick_directory_title));
		intent.putExtra(FileManagerIntents.EXTRA_BUTTON_TEXT, getString(R.string.pick_directory_button));
		
		try {
			startActivityForResult(intent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
		} catch (ActivityNotFoundException e) {
			// No compatible file manager was found.
			Toast.makeText(this, R.string.no_filemanager_installed, 
					Toast.LENGTH_SHORT).show();
		}
	}
    

    /**
     * This is called after the file manager finished.
     */
	@Override
	protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
			if (resultCode == RESULT_OK && data != null) {
				// obtain the filename
				String strfilename = data.getDataString();
				
				RelData.setmyfilename(FileUriUtils.getFile(data.getData()));
				File mfile=RelData.getmyfilename();
				
				if (strfilename != null) {
					// Get rid of URI prefix:
					if (strfilename.startsWith("file://")) {
						strfilename = strfilename.substring(7);
					}
					mEditText.setText(strfilename);
				
					
				
					if (mfile != null && mfile.exists() && !mfile.isDirectory()){
								// set loading state and call file read
								setLoadingState(true);
								RelData.mlistSB = RelData.readFile(mfile);
								RelData.processtext(1); // first record by default
					
					/*
	    			if (mfile.exists() && !mfile.isDirectory()) {
	    				// start main application
	    				Intent i = new Intent(this, CustomerSearch.class);
	    				i.setAction(Intent.ACTION_VIEW);
	    				i.setData(data.getData());
	    				i.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
	    				startActivity(i);
	    				finish();
	    			} else {
	    				Toast.makeText(this, R.string.file_not_found,
	    						Toast.LENGTH_SHORT).show();
	    			}
	    			*/
					
					Intent openPersonListIntent = new Intent(
							this, CustomerDetailList.class);
					
					// create bundle for passing data to list activity

					Bundle bundle = new Bundle();
					bundle.putStringArrayList(CustomerDetailList.CUSTOMERS_LIST,
						new ArrayList<String>(RelData.mlistresult));
					openPersonListIntent.putExtras(bundle);
					
					// hide loading dialog  before starting new activity
					//
					// start business partner list activity
					setLoadingState(false);
					startActivity(openPersonListIntent);					
					finish();
					}
					
				}				
				
			}
		break;
		}
	}
}
