package com.marialice.mapapp;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlacesDescriptionActivity extends Activity {

	// include our classes
	DatabaseContent dbclass = new DatabaseContent();
	TextToBitmap drawclass = new TextToBitmap();
	
	Double lat;
	Double lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_places_description);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		createDetails();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void createDetails() {
		TextView textViewTitle = (TextView) findViewById(R.id.desc_title);
		TextView textViewDesc = (TextView) findViewById(R.id.description);
		ImageView imageViewIcon = (ImageView) findViewById(R.id.desc_icon);

		Intent listintent = getIntent();
		String titlels = listintent.getStringExtra("listDataChild");
		textViewTitle.setText(titlels);
		

		List<Poi> dbpois = dbclass.queryDataFromDatabase(this);
		for (int i = 0; i < dbpois.size(); i++) {
			Poi poi = dbpois.get(i);
			String titledb = poi.getTitle();
			if (titledb.equals(titlels)) {
				textViewDesc.setText(poi.getDescription());
				imageViewIcon.setImageBitmap(drawclass.drawTextToBitmap(
						getApplicationContext(), poi.getSymbol(),
						poi.getNumber()));
				lat = poi.getLat();
				lon = poi.getLon();
			}
		}
	}

	public void gotomap(View view) {
		if (lat == null) {
			Context context = getApplicationContext();
			CharSequence text = "no lat lon values!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			Intent mapintent = new Intent(this, MapActivity.class);
			mapintent.putExtra("lat", lat);
			mapintent.putExtra("lon", lon);
			startActivity(mapintent);
		}
	}

}
