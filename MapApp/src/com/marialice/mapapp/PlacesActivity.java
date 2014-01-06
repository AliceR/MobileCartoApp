package com.marialice.mapapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class PlacesActivity extends ListActivity {
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
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				list_values));
	}
}
