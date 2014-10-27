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
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WalksSightsDescriptionActivity extends Activity {

	// include our classes
	DatabaseContent dbclass = new DatabaseContent();
	TextToBitmap drawclass = new TextToBitmap();

	Double lat;
	Double lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_walks_description);
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
		TextView textViewWalk = (TextView) findViewById(R.id.walkname);
		TextView textViewDesc = (TextView) findViewById(R.id.description);
		TextView textViewAddress = (TextView) findViewById(R.id.address);
		ImageView imageViewIcon = (ImageView) findViewById(R.id.desc_icon);
		ImageView imageViewWifi = (ImageView) findViewById(R.id.wifi);
		ImageView imageViewTerrace = (ImageView) findViewById(R.id.terrace);
		ImageView imageViewSundays = (ImageView) findViewById(R.id.sundays);
		ImageView imageViewCalmPlace = (ImageView) findViewById(R.id.calmplace);
		ImageView imageViewSmoking = (ImageView) findViewById(R.id.smoking);
		Button buttonShowMap = (Button) findViewById(R.id.ShowMap);

		Intent walksightsinfowindowintent = getIntent();
		String positionmap = walksightsinfowindowintent.getStringExtra(String.valueOf("walkSightsPoi"));
		// creates the title according to the intent extra

		List<WalkSightsPoi> dbwalksightspois = dbclass.queryWalksSightsPoiFromDatabase(this);
		for (int i = 0; i < dbwalksightspois.size(); i++) {
			WalkSightsPoi sightspoi = dbwalksightspois.get(i);
			String positiondb = String.valueOf(sightspoi.getLatLng());
			int icon = getResources().getIdentifier(sightspoi.getIcon(), "drawable", this.getPackageName());

			if (positiondb.equals(positionmap)) {

				// creates the description and walk name based on title comparison
				textViewDesc.setText(Html.fromHtml(sightspoi.getDescription()));
				textViewWalk.setText(Html.fromHtml(sightspoi.getName()));
				textViewTitle.setText(Html.fromHtml(sightspoi.getTitle()));
				textViewAddress.setText(Html.fromHtml(sightspoi.getAddress()));
				// creates the icon in the TextToBitmap class using symbol and
				// number
				imageViewIcon.setImageBitmap(drawclass.drawTextToBitmap(
						getApplicationContext(), icon, String.valueOf(sightspoi.getId())));
				// requests lat and lon for usage with the button 'goto_map'
				lat = sightspoi.getLat();
				lon = sightspoi.getLon();

				// if the entrance in db is true, set the extra info icon
				if (sightspoi.getWifi() == true) {
					imageViewWifi.setImageResource(R.drawable.description_wifi);
					imageViewWifi.setVisibility(View.VISIBLE);
					// OnLongClickListener();
				}
				if (sightspoi.getTerrace() == true) {
					imageViewTerrace
							.setImageResource(R.drawable.description_terrace);
					imageViewTerrace.setVisibility(View.VISIBLE);
					// OnLongClickListener();
				}
				if (sightspoi.getSundays() == true) {
					imageViewSundays
							.setImageResource(R.drawable.description_sunday);
					imageViewSundays.setVisibility(View.VISIBLE);
					// OnLongClickListener();
				}
				if (sightspoi.getCalmplace() == true) {
					imageViewCalmPlace
							.setImageResource(R.drawable.description_calmplace);
					imageViewCalmPlace.setVisibility(View.VISIBLE);
					// OnLongClickListener();
				}
				if (sightspoi.getNonsmoking() == true) {
					imageViewSmoking
							.setImageResource(R.drawable.description_smoking);
					imageViewSmoking.setVisibility(View.VISIBLE);
					// OnLongClickListener();
				}
				if (sightspoi.getTouristclassic() == true) {
					textViewDesc.setBackgroundResource(R.drawable.border_tc);
					// OnLongClickListener();
				}

				int button = getResources().getIdentifier("walk" + sightspoi.getIcon() + "button", "drawable", this.getPackageName());
				buttonShowMap.setBackgroundResource(button);
				
			}
		} 

	}



	// on click listener
	public void showLegend(View view) {
		LayoutInflater inflater = getLayoutInflater(); // Inflate the Layout
		View layout = inflater.inflate(R.layout.custom_toast_description,
				(ViewGroup) findViewById(R.id.custom_toast_layout));

		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.RIGHT, 10, 300);
		toast.setView(layout);
		toast.show();

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
			// using the lat lon values requested previously, the map activity
			// is
			// started with the values as extras attached
			Intent mapintent = new Intent(this, MapActivity.class);
			mapintent.putExtra("lat", lat);
			mapintent.putExtra("lon", lon);
			startActivity(mapintent);
		}
	}

}
