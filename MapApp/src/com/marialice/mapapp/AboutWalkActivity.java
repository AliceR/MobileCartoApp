package com.marialice.mapapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutWalkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// the content of this activity is mostly created via layout xml
		setContentView(R.layout.activity_about_walk);
		// Show the Up button in the action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		createLinks();
		
		((RelativeLayout) findViewById(R.id.sights)).setBackgroundResource(R.drawable.border_sights);
		((RelativeLayout) findViewById(R.id.beer)).setBackgroundResource(R.drawable.border_beer);
		((RelativeLayout) findViewById(R.id.river)).setBackgroundResource(R.drawable.border_river);
	}

	public void createLinks() {
		// create links in text views
		((TextView) findViewById(R.id.about_sponsors_link))
				.setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) findViewById(R.id.about_sponsors_link)).setText(Html
				.fromHtml(getResources().getString(R.string.link_about_sponsor)));
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
	}