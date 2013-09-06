package com.bmarohnic.java2week1;

import java.util.Locale;

import com.bmarohnic.java2week1.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText editText;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	      Bundle bundle = intent.getExtras();
	      if (bundle != null) {
	        String string = bundle.getString(MyService.FILEPATH);
	        int resultCode = bundle.getInt(MyService.RESULT);
	        if (resultCode == RESULT_OK) {
	          Toast.makeText(MainActivity.this,
	              "Download complete. Download URI: " + string,
	              Toast.LENGTH_LONG).show();
	          editText.setText("Download done");
	        } else {
	          Toast.makeText(MainActivity.this, "Download failed",
	              Toast.LENGTH_LONG).show();
	          editText.setText("Download failed");
	        }
	      }
	    }
	  };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		editText = (EditText) findViewById(R.id.searchField);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	  protected void onResume() {
	    super.onResume();
	    registerReceiver(receiver, new IntentFilter(MyService.NOTIFICATION));
	  }
	  @Override
	  protected void onPause() {
	    super.onPause();
	    unregisterReceiver(receiver);
	  }
	  
	public void onClick(View view) {

	    Intent intent = new Intent(this, MyService.class);
	    EditText field = (EditText) findViewById(R.id.searchField);
		String zipCode = field.getText().toString().toUpperCase(Locale.US);
	    // Populate values that will be supplied to the service
//	    intent.putExtra(MyService.FILENAME, "index.html");
		intent.putExtra(MyService.FILENAME, "JSONData.txt");
	    intent.putExtra(MyService.URL,
	        "http://api.8coupons.com/v1/getdeals?key=67714ceb7f857482a7f3e890ae52a8730c7d60663de10661e527d93a9236c547a1c5c3d15f1cb29e6aa3430a54a2091b&zip=" + zipCode + "&categoryid=1&format=json");
	    startService(intent);
	    editText.setText("Download Commencing");
	  }

}
