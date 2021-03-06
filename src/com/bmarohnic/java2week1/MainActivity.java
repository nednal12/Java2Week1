/*
 * project		Java2Week1
 * 
 * package		com.bmarohnic.java2week1
 * 
 * @author		Brent Marohnic
 * 
 * date			Sep 7, 2013
 */
package com.bmarohnic.java2week1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity {

	ListView listview;
	TextView textview;
	Boolean _connected = false;
	private EditText editText;
	Context _context;
	
	// Used to receive messages back from the service.
	private BroadcastReceiver receiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	      Bundle bundle = intent.getExtras();
	      if (bundle != null) {
	        String string = bundle.getString(MyService.FILEPATH);
	        int resultCode = bundle.getInt(MyService.RESULT);
	        // Inform the user that all is well.
	        if (resultCode == RESULT_OK) {
	          Toast.makeText(MainActivity.this,
	              "Download complete. Download URI: " + string,
	              Toast.LENGTH_LONG).show();
	          editText.setText("Download Complete");
	          
	          // Call displayData() to populate the listview with the data retrieved from the API.
	          displayData();
	        } else {
	          /*
	          Toast.makeText(MainActivity.this, "Download failed",
	              Toast.LENGTH_LONG).show();
	          editText.setText("Download failed");
	          */
	        	editText.setText("Not Connected to Internet");
				Toast.makeText(MainActivity.this,
			              "Attempting to use saved data.",
			    Toast.LENGTH_LONG).show();
				displayData();
				Toast.makeText(MainActivity.this,
			              "Local data used.",
			    Toast.LENGTH_LONG).show();
	        }
	      }
	    }
	  };
	   
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_context = this;
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.searchField);
		
		listview = (ListView) this.findViewById(R.id.list);
		View listHeader = this.getLayoutInflater().inflate(R.layout.list_header, null);
		listview.addHeaderView(listHeader);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	  protected void onResume() {
	    super.onResume();
	    registerReceiver(receiver, new IntentFilter(MyService.NOTIFICATION));
	  }
	  
  	/* (non-Javadoc)
  	 * @see android.app.Activity#onPause()
  	 */
  	@Override
	  protected void onPause() {
	    super.onPause();
	    unregisterReceiver(receiver);
	  }
	    
	/**
	 * On click.
	 *
	 * @param view the view
	 */
	public void onClick(View view) {

		// Detect network connection
		_connected = WebStuff.getConnectionStatus(_context);
		if(_connected == true){
			Log.i("Newwork Connection", WebStuff.getConnectionType(_context));
		
			Intent intent = new Intent(this, MyService.class);
			EditText field = (EditText) findViewById(R.id.searchField);
			String zipCode = field.getText().toString().toUpperCase(Locale.US);
			// Populate values that will be supplied to the service
//	    	intent.putExtra(MyService.FILENAME, "index.html");
			
			intent.putExtra(MyService.FILENAME, "JSONData.txt");
			intent.putExtra(MyService.URL,
					"http://api.8coupons.com/v1/getdeals?key=67714ceb7f857482a7f3e890ae52a8730c7d60663de10661e527d93a9236c547a1c5c3d15f1cb29e6aa3430a54a2091b&zip=" 
	        		+ zipCode + "&categoryid=1&format=json");
			startService(intent);
			editText.setText("Download Commencing");
		} else {
			editText.setText("Not Connected to Internet");
			Toast.makeText(MainActivity.this,
		              "Attempting to use saved data.",
		              Toast.LENGTH_LONG).show();
		}
	}

	
	/**
	 * Display data.
	 */
	public void displayData(){
		
		ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
		int numberOfObjects = 0;
		@SuppressWarnings("unused")
		int length = -1;
		
		try {
			FileInputStream fis = openFileInput("JSONData.txt");
			StringBuffer fileContent = new StringBuffer("");
			byte[] buffer = new byte[1024];
			
			try {
				
				while ((length = fis.read(buffer)) != -1) {
				    fileContent.append(new String(buffer));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	        
			
			String JSONString = new String(fileContent);
			JSONArray inputArray = null;
			try {
				inputArray = new JSONArray(JSONString);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			numberOfObjects = inputArray.length();
			Log.i("numberOfObjects", "The number of objects is: " + numberOfObjects);
			  
			for (int i = 0; i < numberOfObjects; i++)
			{
				try {
					JSONObject jo = inputArray.getJSONObject(i);
					String restaurant = jo.getString("name");
					String dealTitle = jo.getString("dealTitle");
					String city = jo.getString("city");
					
					HashMap<String, String>displayMap = new HashMap<String, String>();
					displayMap.put("restaurant", restaurant);
					displayMap.put("dealTitle", dealTitle);
					displayMap.put("city", city);
					
					myList.add(displayMap);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			if(numberOfObjects == 0)
			{
				editText.setText("No results for entered zip");
				
			}
			
			SimpleAdapter adapter = new SimpleAdapter(this, myList, R.layout.list_row,
					new String[] { "restaurant", "dealTitle", "city"}, new int[] {R.id.restaurant, R.id.dealTitle, R.id.city});
			
			listview.setAdapter(adapter);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
