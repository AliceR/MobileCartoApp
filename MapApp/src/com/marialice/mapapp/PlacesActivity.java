package com.marialice.mapapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.app.Activity;

public class PlacesActivity extends ListActivity {
	// ListView list;
	SQLiteDatabase db = null;
	Cursor dbCursor;
	DatabaseHelper dbHelper = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		setupActionBar();
		queryDataFromDatabase();
	}

	private void setupActionBar() {
		// Defines the action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.places, menu);
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

	public void queryDataFromDatabase() {
		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		List<String> list_values = new ArrayList<String>();
		try {

			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery("SELECT title FROM cbpois;", null);
			dbCursor.moveToFirst();
			int index = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				list_values.add(dbCursor.getString(index));
				dbCursor.moveToNext();

				// String title= dbCursor.getString(index);
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// try to dynamically change the icon in the poilist
		TextView poiTV = (TextView) findViewById(R.id.poilistitem);
		//poiTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.poi_museum, 0, 0, 0);  // there is a bug here! :(
		

		// Binding resources Array to ListAdapter
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.poilistitem, list_values));

		ListView poiList = getListView();

		// listening to single list item on click
		poiList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// selected item
				String poi_name = ((TextView) view).getText().toString();

				// Launching new Activity on selecting single List Item
				Intent i = new Intent(getApplicationContext(),
						PlacesDescriptionActivity.class);
				// sending data to new activity
				i.putExtra("poi_name", poi_name);
				startActivity(i);

			}
		});
	}

}
