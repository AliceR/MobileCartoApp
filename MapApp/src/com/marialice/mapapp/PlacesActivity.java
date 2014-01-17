package com.marialice.mapapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class PlacesActivity extends Activity {

	DatabaseContent dbclass = new DatabaseContent();

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
		// Defines the action bar to go back/up
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

		List<Poi> dbpois = dbclass.queryDataFromDatabase(this);

		List<String> listSightseeing = new ArrayList<String>();
		List<String> listMuseum = new ArrayList<String>();
		List<String> listShopping = new ArrayList<String>();
		List<String> listEat = new ArrayList<String>();
		List<String> listCafe = new ArrayList<String>();
		List<String> listBar = new ArrayList<String>();
		List<String> listHidden = new ArrayList<String>();

		for (int i = 0; i < dbpois.size(); i++) {
			Poi poi = dbpois.get(i);
			if (poi.getCategory().equals("sightseeing")) {
				listSightseeing.add(poi.getNumber());
				//listSightseeing.add(poi.getTitle());
				//String[] listarray = listSightseeing.toArray(new String[listSightseeing.size()]);
				//Log.v("Listenergebnis", listarray[i]);
			} else if (poi.getCategory().equals("museum")) {
				listMuseum.add(poi.getTitle());
			} else if (poi.getCategory().equals("shopping")) {
				listShopping.add(poi.getTitle());
			} else if (poi.getCategory().equals("eat")) {
				listEat.add(poi.getTitle());
			} else if (poi.getCategory().equals("cafe")) {
				listCafe.add(poi.getTitle());
			} else if (poi.getCategory().equals("bar")) {
				listBar.add(poi.getTitle());
			} else if (poi.getCategory().equals("hidden")) {
				listHidden.add(poi.getTitle());
			} else {
				Log.v("Mary says:","WTF! there is an poi without category! " +
						"...what a terrible failure...");
			}
		}

		// Header, Child data
		listDataChild.put(listDataHeader.get(0), listSightseeing);
		listDataChild.put(listDataHeader.get(1), listMuseum);
		listDataChild.put(listDataHeader.get(2), listShopping);
		listDataChild.put(listDataHeader.get(3), listEat);
		listDataChild.put(listDataHeader.get(4), listCafe);
		listDataChild.put(listDataHeader.get(5), listBar);
		listDataChild.put(listDataHeader.get(6), listHidden);

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				
//				we need to transmit the id, not the title!

				Intent listintent = new Intent(getApplicationContext(),
						PlacesDescriptionActivity.class);

				listintent.putExtra("listDataChild",
						listDataChild.get(listDataHeader.get(groupPosition))
								.get(childPosition));
				startActivity(listintent);
				return false;
			}
		});
	}
}