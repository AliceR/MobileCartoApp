package com.marialice.mapapp;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.text.SpannableString;
import android.widget.TextView;
import android.text.style.ForegroundColorSpan;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnInfoWindowClickListener{

	private static final String MAPBOX_BASEMAP_URL_FORMAT = "http://api.tiles.mapbox.com/v3/maridani.go26lm2h/%d/%d/%d.png";
	private GoogleMap mMap;
	private Marker mCernavez;
	private Marker mBazen;
	
	//For the connection to the database
	SQLiteDatabase db = null;
	Cursor dbCursor;
	DatabaseHelper dbHelper = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setUpMapIfNeeded();
		queryDataFromDatabase();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_search:
			return true;
		case R.id.goto_places:
			Intent intent = new Intent(MapActivity.this, PlacesActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class CustomInfoWindowAdapter implements InfoWindowAdapter {
		private final View mWindow;

		CustomInfoWindowAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, mWindow);
			return mWindow;
		}

		private void render(Marker marker, View view) {
			int badge;
			// Use the equals() method on a Marker to check for equals. Do not use ==.
			if (marker.equals(mCernavez)) {
				badge = R.drawable.cb_cerna_vez;
			} else if (marker.equals(mBazen)) {
				badge = R.drawable.cb_bazen;
			} else {
				// Passing 0 to setImageResource will clear the image view.
				badge = 0;
			}
			((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				// Spannable string allows us to edit the formatting of the text.
				SpannableString titleText = new SpannableString(title);
				titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
						titleText.length(), 0);
				titleUi.setText(titleText);
			} else {
				titleUi.setText("");
			}

			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null ) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
						snippet.length(), 0);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}

		@Override
		public View getInfoContents(Marker arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// declare some map properties
		mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				48.9744094, 14.4746094), 15));

		// create the tile overlay
		TileProvider tileProvider = new UrlTileProvider(256, 256) {
			@Override
			public synchronized URL getTileUrl(int x, int y, int zoom) {
				String s = String.format(Locale.US, MAPBOX_BASEMAP_URL_FORMAT,
						zoom, x, y);
				URL url = null;
				try {
					url = new URL(s);
				} catch (MalformedURLException e) {
					throw new AssertionError(e);
				}
				return url;
			}
		};

		// add the tile overlay to the map
		mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));

		// call the function that creates the markers
		addMarkersToMap();
	}
	
	private final List<Marker> poiMarker = new ArrayList<Marker>();
	
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
			int lat = dbCursor.getColumnIndex("lat");
			int lon = dbCursor.getColumnIndex("lon");
			int title = dbCursor.getColumnIndex("title");
			
			while (!dbCursor.isAfterLast()) {
				poiMarker.add(mMap.addMarker(new MarkerOptions()
					.position(new LatLng(
						dbCursor.getFloat(lat), dbCursor.getFloat(lon))
						)
					.title(dbCursor.getString(title))
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
						));
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
		
	}

	private void addMarkersToMap() {
		mCernavez = mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.cb_cerna_vez))
				.position(new LatLng(48.9754689, 14.4761153)).anchor(0.5f, 1f)
				.title("Èerná vìž").snippet("Black tower").rotation(10)
				.infoWindowAnchor(0.0f, 0.0f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.cb_bila_vez))
				.position(new LatLng(48.9756647, 14.4717539)).anchor(0.5f, 1f)
				.title("Klášter dominikánù").snippet("Dominican monastery")
				.rotation(0).infoWindowAnchor(0.5f, 0.5f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.cb_kasna))
				.position(new LatLng(48.9745117, 14.4743214))
				.anchor(0.5f, 0.75f).rotation(4).title("Samsonova kašna")
				.snippet("Samson fountain").infoWindowAnchor(0.5f, 0.5f));

		mBazen = mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.cb_bazen))
				.position(new LatLng(48.9744025, 14.4691572))
				.anchor(0.5f, 0.75f).rotation(4).title("Plavecký bazén")
				.snippet("Swiming pool").infoWindowAnchor(0.5f, 0.5f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.cb_kino))
				.position(new LatLng(48.9716589, 14.4711061))
				.anchor(0.5f, 0.75f).rotation(0).title("Kino")
				.snippet("Cinema").infoWindowAnchor(0.5f, 0.5f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.cb_zeleznapanna))
				.position(new LatLng(48.9728661, 14.4725350))
				.anchor(0.5f, 0.75f).rotation(355).title("Železná panna")
				.snippet("Iron maiden").infoWindowAnchor(0.5f, 0.5f));
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

}
