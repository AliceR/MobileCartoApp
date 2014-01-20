package com.marialice.mapapp;
/* 
 * this activity shows info about us, about use-it, about the app
 */
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// the content of this activity is mostly created via layout xml
		setContentView(R.layout.activity_about);
		// Show the Up button in the action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		createLinks();
	}

	public void createLinks() {
		// create links in text views
		((TextView) findViewById(R.id.link_useit))
				.setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) findViewById(R.id.link_useit)).setText(Html
				.fromHtml(getResources().getString(R.string.link_useit)));

		((TextView) findViewById(R.id.contact_mail))
				.setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) findViewById(R.id.contact_mail)).setText(Html
				.fromHtml(getResources().getString(R.string.contact_mail)));

		((TextView) findViewById(R.id.link_cbuseit))
				.setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) findViewById(R.id.link_cbuseit)).setText(Html
				.fromHtml(getResources().getString(R.string.link_cbuseit)));

		((TextView) findViewById(R.id.link_fbuseit))
				.setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) findViewById(R.id.link_fbuseit)).setText(Html
				.fromHtml(getResources().getString(R.string.link_fbuseit)));

		((TextView) findViewById(R.id.link_fbcbck))
				.setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) findViewById(R.id.link_fbcbck)).setText(Html
				.fromHtml(getResources().getString(R.string.link_fbcbck)));
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
