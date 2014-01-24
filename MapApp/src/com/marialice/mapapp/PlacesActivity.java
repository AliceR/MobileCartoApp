package com.marialice.mapapp;

/* 
 * this is the activity for the list view of places
 */
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

	// include our class
	DatabaseContent dbclass = new DatabaseContent();

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		// preparing list data
		prepareListData();
		// create the expandable list in an extra class
		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);
		// setting list adapter
		expListView.setAdapter(listAdapter);
		// defining what happens on click
		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// go to the place description activity
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

	// preparing the list data
	private void prepareListData() {

		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// statically adding header data to the List
		listDataHeader.add("Sightseeing");
		listDataHeader.add("Museum, venue");
		listDataHeader.add("Shopping");
		listDataHeader.add("Eat, drink");
		listDataHeader.add("Café, tea room");
		listDataHeader.add("Bar, club");
		listDataHeader.add("Hidden, chill out");

		// creating lists for each category with all titles in that category
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
			if (poi.getCategory().equals("sightseeing")
					| poi.getCategory().equals("museumsightseeing")
					| poi.getCategory().equals("shoppingsightseeing")
					| poi.getCategory().equals("barsightseeing")) {
				listSightseeing.add(poi.getTitle());
			} else if (poi.getCategory().equals("museum")
					| poi.getCategory().equals("museumsightseeing")
					| poi.getCategory().equals("museumcafe")) {
				listMuseum.add(poi.getTitle());
			} else if (poi.getCategory().equals("shopping")
					| poi.getCategory().equals("shoppingsightseeing")
					| poi.getCategory().equals("shoppingeat")
					| poi.getCategory().equals("shoppingcafe")) {
				listShopping.add(poi.getTitle());
			} else if (poi.getCategory().equals("eat")
					| poi.getCategory().equals("shoppingeat")) {
				listEat.add(poi.getTitle());
			} else if (poi.getCategory().equals("cafe")
					| poi.getCategory().equals("museumcafe")
					| poi.getCategory().equals("shoppingcafe")
					| poi.getCategory().equals("hiddencafe")
					| poi.getCategory().equals("barcafe")) {
				listCafe.add(poi.getTitle());
			} else if (poi.getCategory().equals("bar")
					| poi.getCategory().equals("barcafe")
					| poi.getCategory().equals("barsightseeing")) {
				listBar.add(poi.getTitle());
			} else if (poi.getCategory().equals("hidden")
					| poi.getCategory().equals("hiddencafe")) {
				listHidden.add(poi.getTitle());
			} else {
				Log.v("Mary says:", "WTF! there is an poi without category! "
						+ "...what a terrible failure...");
			}
		}

		// writing the lists with titles and their respective header in one
		// HashMap
		listDataChild.put(listDataHeader.get(0), listSightseeing);
		listDataChild.put(listDataHeader.get(1), listMuseum);
		listDataChild.put(listDataHeader.get(2), listShopping);
		listDataChild.put(listDataHeader.get(3), listEat);
		listDataChild.put(listDataHeader.get(4), listCafe);
		listDataChild.put(listDataHeader.get(5), listBar);
		listDataChild.put(listDataHeader.get(6), listHidden);

		// // ATTENTION!! listDataChild is not ordered as we expect!
		// // we can not rely on the correct position of the item in the HashMap
		// // which also correlates to the groupid, i guess... and makes trouble
		// // in the sorting!

	}
}