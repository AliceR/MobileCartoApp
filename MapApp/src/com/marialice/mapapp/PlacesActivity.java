package com.marialice.mapapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlacesActivity extends ListActivity {

	SQLiteDatabase db = null;
	Cursor dbCursor;
	DatabaseHelper dbHelper = new DatabaseHelper(this);

	ListView poiList;
	TextView poiTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		setupActionBar();
		createPoiList();
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

	public void createPoiList() {
		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		List<String> list_values = new ArrayList<String>();
		try {

			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery("SELECT title, category FROM cbpois;", null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			int catindex = dbCursor.getColumnIndex("category");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				String cat = dbCursor.getString(catindex);
				list_values.add(cat + ": " + title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// Binding resources Array to ListAdapter
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.poilistitem, list_values));
		
		dynamicPoiImg();
		clickOnListItem();

	}

	/* dynamically change the icon in the poilist */
	public void dynamicPoiImg() {
		this.onContentChanged();
		TextView poiTV = (TextView) this.findViewById(R.id.poilistitem);
		// the textview does not exist at this point

		Drawable img = this.getResources().getDrawable(R.drawable.poi_museum);
		if (poiTV != null) {
			poiTV.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
			// apparently it is null, the icon stays as it is defined in xml.
		}
	}

	public void clickOnListItem() {
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
