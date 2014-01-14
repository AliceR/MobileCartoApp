package com.marialice.mapapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class PlacesActivity extends Activity {

	SQLiteDatabase db = null;
	Cursor dbCursor;
	DatabaseHelper dbHelper = new DatabaseHelper(this);

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		setupActionBar();

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);
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
	
	// Preparing the list data
	private void prepareListData() {
		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Sightseeing");
		listDataHeader.add("Museum, venue");
		listDataHeader.add("Shopping");
		listDataHeader.add("Eat, drink");
		listDataHeader.add("Café, tea room");
		listDataHeader.add("Bar, club");
		listDataHeader.add("Hidden, chill out");

		// Adding sightseeing data
		List<String> sightseeing = new ArrayList<String>();
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery(
					"SELECT title FROM cbpois WHERE category = 'sightseeing';",
					null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				sightseeing.add(title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// Adding museum data
		List<String> museum = new ArrayList<String>();
		try {
			db = dbHelper.getDataBase();
			dbCursor = db
					.rawQuery(
							"SELECT title FROM cbpois WHERE category = 'museum';",
							null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				museum.add(title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// Adding shopping data
		List<String> shopping = new ArrayList<String>();
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery(
					"SELECT title FROM cbpois WHERE category = 'shopping';",
					null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				shopping.add(title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// Adding eat data
		List<String> eat = new ArrayList<String>();
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery(
					"SELECT title FROM cbpois WHERE category = 'eat';", null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				eat.add(title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// Adding cafe data
		List<String> cafe = new ArrayList<String>();
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery(
					"SELECT title FROM cbpois WHERE category = 'cafe';", null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				cafe.add(title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// Adding bar data
		List<String> bar = new ArrayList<String>();
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery(
					"SELECT title FROM cbpois WHERE category = 'bar';", null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				bar.add(title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}

		// Adding hidden data
		List<String> hidden = new ArrayList<String>();
		try {

			db = dbHelper.getDataBase();
			dbCursor = db
					.rawQuery(
							"SELECT title FROM cbpois WHERE category = 'hidden';",
							null);
			dbCursor.moveToFirst();
			int titleindex = dbCursor.getColumnIndex("title");
			while (!dbCursor.isAfterLast()) {
				String title = dbCursor.getString(titleindex);
				hidden.add(title);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
		
		// Header, Child data
		listDataChild.put(listDataHeader.get(0), sightseeing); 
		listDataChild.put(listDataHeader.get(1), museum);
		listDataChild.put(listDataHeader.get(2), shopping);
		listDataChild.put(listDataHeader.get(3), eat);
		listDataChild.put(listDataHeader.get(4), cafe);
		listDataChild.put(listDataHeader.get(5), bar);
		listDataChild.put(listDataHeader.get(6), hidden);

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
				Intent i = new Intent(getApplicationContext(), PlacesDescriptionActivity.class);
				
			    i.putExtra("listDataChild", listDataChild.get(listDataHeader.get(groupPosition))
						.get(childPosition));
			    startActivity(i);
			    return false;
			}
		});
	}
}