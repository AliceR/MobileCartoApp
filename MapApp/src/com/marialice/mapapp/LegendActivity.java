package com.marialice.mapapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class LegendActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legend);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.legend, menu);
		return true;
	}

/*	public void buttonClose(View view) {
		Intent mapintent = new Intent(this, MapActivity.class);
		startActivity(mapintent);
	}*/

}
