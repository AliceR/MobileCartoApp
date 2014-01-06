package com.marialice.mapapp;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class MapActivity extends FragmentActivity {

	private static final String MAPBOX_BASEMAP_URL_FORMAT = "http://api.tiles.mapbox.com/v3/maridani.go26lm2h/%d/%d/%d.png";
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setUpMapIfNeeded();
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

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the map.
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

		//add the tile overlay to the map
		mMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));

		// call the function that creates the markers
		addMarkersToMap();
	}

	private void addMarkersToMap() {
		mMap.addMarker(new MarkerOptions()
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

		mMap.addMarker(new MarkerOptions()
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

}
