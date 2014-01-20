package com.marialice.mapapp;
/* 
 * this is the activity for the detail view of places,
 * individual for each place
 */
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
		ImageView imageViewWifi = (ImageView) findViewById(R.id.wifi);
		ImageView imageViewTerrace = (ImageView) findViewById(R.id.terrace);
		ImageView imageViewSundays = (ImageView) findViewById(R.id.sundays);
		ImageView imageViewCalmPlace = (ImageView) findViewById(R.id.calmplace);
		ImageView imageViewSmoking = (ImageView) findViewById(R.id.smoking);

		Intent listintent = getIntent();
		String titlels = listintent.getStringExtra("listDataChild");
		// creates the title according to the intent extra
		textViewTitle.setText(titlels);

		List<Poi> dbpois = dbclass.queryDataFromDatabase(this);
		for (int i = 0; i < dbpois.size(); i++) {
			Poi poi = dbpois.get(i);
			String titledb = poi.getTitle();

			if (titledb.equals(titlels)) {

				// creates the description based on title comparison
				textViewDesc.setText(poi.getDescription());
				// creates the icon in the TextToBitmap class using symbol and
				// number
				imageViewIcon.setImageBitmap(drawclass.drawTextToBitmap(
						getApplicationContext(), poi.getSymbol(),
						poi.getNumber()));
				// requests lat and lon for usage with the button 'goto_map'
				lat = poi.getLat();
				lon = poi.getLon();

				// if the entrance in db is true, set the extra info icon
				if (poi.getWifi() == true) {
					imageViewWifi.setImageResource(R.drawable.description_wifi);
				}
				if (poi.getTerrace() == true) {
					imageViewTerrace
							.setImageResource(R.drawable.description_terrace);
				}
				if (poi.getSundays() == true) {
					imageViewSundays
							.setImageResource(R.drawable.description_sunday);
				}
				if (poi.getCalmplace() == true) {
					imageViewCalmPlace
							.setImageResource(R.drawable.description_calmplace);
				}
				if (poi.getNonsmoking() == true) {
					imageViewSmoking
							.setImageResource(R.drawable.description_smoking);
				}
			}
		}
	}

	// this method is called when the button is clicked
	public void gotomap(View view) {
		if (lat == null) {
			// this toast is for catching and indicating errors in data flow
			Context context = getApplicationContext();
			CharSequence text = "no lat lon values!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {
			// using the lat lon values requested previously, the map activity is
			// started with the values as extras attached
			Intent mapintent = new Intent(this, MapActivity.class);
			mapintent.putExtra("lat", lat);
			mapintent.putExtra("lon", lon);
			startActivity(mapintent);
		}
	}

}
