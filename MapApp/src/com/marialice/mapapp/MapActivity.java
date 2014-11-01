package com.marialice.mapapp;

/* 
 * this is the main activity, the map view
 */
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

public class MapActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnMyLocationButtonClickListener, OnInfoWindowClickListener,
		OnMapLongClickListener {

	// include our classes
	TextToBitmap drawclass = new TextToBitmap();
	DatabaseContent dbclass = new DatabaseContent();

	private static final String MAPBOX_BASEMAP_URL_FORMAT = "http://api.tiles.mapbox.com/v3/maridani.h0a912jg/%d/%d/%d.png";
	private GoogleMap mMap;

	// For getting the location of the device
	private LocationClient mLocationClient;
	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// on create, some methods are called
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setUpMapIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
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
		if (item.getItemId() == R.id.goto_actlikelocal) {
			// start the pop up with a random hint
			Random randomi = new Random();
			int max = dbclass.queryHintsFromDatabase(this).size() - 1;
			int min = 0;
			int i = randomi.nextInt((max - min) + 1) + min;
			createPopUp(i);
			return true;}
		else if (item.getItemId() == R.id.goto_legend) {
			Intent legendintent = new Intent(MapActivity.this,
					LegendActivity.class);
			startActivity(legendintent);
			return true;}
		else if (item.getItemId() == R.id.goto_about) {
			Intent aboutintent = new Intent(MapActivity.this,
					AboutActivity.class);
			startActivity(aboutintent);
			return true;}
		else if (item.getItemId() == R.id.goto_places) {
			Intent placesintent = new Intent(MapActivity.this,
					PlacesActivity.class);
			startActivity(placesintent);
			return true;}
		else
			return super.onOptionsItemSelected(item);
		
	}

	private void setUpMapIfNeeded() {
		// a null check to confirm that the map is not already instantiated
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
				mMap.setMyLocationEnabled(true);
				mMap.setOnMyLocationButtonClickListener(this);
			}
		}
	}
	

	private void setUpMap() {
		// declare map properties
		mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
		mMap.setMyLocationEnabled(true); // the location button
		mMap.getUiSettings().setZoomControlsEnabled(true); // the +/- buttons
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMapLongClickListener(this);

		// the center coordinates and zoom level on the map
		Double default_lat = 48.9744094;
		Double default_lon = 14.4746094;
		Double lat = getIntent().getDoubleExtra("lat", default_lat);
		Double lon = getIntent().getDoubleExtra("lon", default_lon);
		if (lat.equals(default_lat)) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					default_lat, default_lon), 15));
		} else {
			// in case the coordinates come from the description button
			zoomFromDescription(lat, lon);
		}

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

		
		// Add polyline "walks water"		
		ArrayList<LatLng> coordListWater = new ArrayList<LatLng>();
		List<WalkWater> dbwalkwaternodes = dbclass.queryWalksWaterFromDatabase(this); 
		// Adding points to ArrayList		
		for (int i = 0; i < dbwalkwaternodes.size(); i++) {
			WalkWater location = dbwalkwaternodes.get(i);
			coordListWater.add(new LatLng(location.getLat(), location.getLon()));
			}		
		// Create polyline options with existing LatLng ArrayList		
		Polyline waterWalk = mMap.addPolyline(new PolylineOptions() 
			.addAll(coordListWater)
			.width(13)
        	.color(Color.parseColor("#B3f04e23"))
			.geodesic(true));
		waterWalk.setZIndex(1000);
		
		
		// Add polyline "walks beer"		
		ArrayList<LatLng> coordListBeer = new ArrayList<LatLng>();
		List<WalkBeer> dbwalkbeernodes = dbclass.queryWalksBeerFromDatabase(this); 
		// Adding points to ArrayList		
		for (int i = 0; i < dbwalkbeernodes.size(); i++) {
			WalkBeer location = dbwalkbeernodes.get(i);
			coordListBeer.add(new LatLng(location.getLat(), location.getLon()));
			}		
		// Create polyline options with existing LatLng ArrayList		
		Polyline beerWalk = mMap.addPolyline(new PolylineOptions() 
			.addAll(coordListBeer)
			.width(13)
        	.color(Color.parseColor("#B300868a"))
			.geodesic(true));
		beerWalk.setZIndex(1000);
		
		
		// Add polyline "walks sights"		
		ArrayList<LatLng> coordListSights = new ArrayList<LatLng>();
		List<WalkSights> dbwalksightsnodes = dbclass.queryWalksSightsFromDatabase(this); 
		// Adding points to ArrayList		
		for (int i = 0; i < dbwalksightsnodes.size(); i++) {
			WalkSights location = dbwalksightsnodes.get(i);
			coordListSights.add(new LatLng(location.getLat(), location.getLon()));
			}		
		// Create polyline options with existing LatLng ArrayList		
		Polyline sightsWalk = mMap.addPolyline(new PolylineOptions() 
			.addAll(coordListSights)
			.width(13)
        	.color(Color.parseColor("#B3b00d27"))
			.geodesic(true));
		sightsWalk.setZIndex(1000);
		
		
		//Add marker to "walks water"
		List<WalkWaterPoi> dbwalkwaterpois = dbclass.queryWalksWaterPoiFromDatabase(this);

		for (int i = 0; i < dbwalkwaterpois.size(); i++) {
			WalkWaterPoi waterpoi = dbwalkwaterpois.get(i);
			String number = String.valueOf(waterpoi.getId());
			String title = waterpoi.getTitle();
			int icon = getResources().getIdentifier(waterpoi.getIcon(), "drawable", this.getPackageName());
				mMap.addMarker(new MarkerOptions()
					.position(waterpoi.getLatLng())
					.title(title)
					.snippet(waterpoi.getName())
					.icon(BitmapDescriptorFactory.fromBitmap(drawclass
							.drawTextToBitmap(getApplicationContext(),
									icon, number, "Roboto-Light")))
					);
		}
		
		//Add marker to "walks sights"
		List<WalkSightsPoi> dbwalksightspois = dbclass.queryWalksSightsPoiFromDatabase(this);

		for (int i = 0; i < dbwalksightspois.size(); i++) {
			WalkSightsPoi sightspoi = dbwalksightspois.get(i);
			String number = String.valueOf(sightspoi.getId());
			String title = sightspoi.getTitle();
			int icon = getResources().getIdentifier(sightspoi.getIcon(), "drawable", this.getPackageName());
				mMap.addMarker(new MarkerOptions()
					.position(sightspoi.getLatLng())
					.title(title)
					.snippet(sightspoi.getName())
					.icon(BitmapDescriptorFactory.fromBitmap(drawclass
							.drawTextToBitmap(getApplicationContext(),
									icon, number, "Roboto-Light")))
					);
		}
		
		//Add marker to "walks beer"
		List<WalkBeerPoi> dbwalkbeerpois = dbclass.queryWalksBeerPoiFromDatabase(this);

		for (int i = 0; i < dbwalkbeerpois.size(); i++) {
			WalkBeerPoi beerpoi = dbwalkbeerpois.get(i);
			String number = String.valueOf(beerpoi.getId());
			String title = beerpoi.getTitle();
			int icon = getResources().getIdentifier(beerpoi.getIcon(), "drawable", this.getPackageName());
				mMap.addMarker(new MarkerOptions()
					.position(beerpoi.getLatLng())
					.title(title)
					.snippet(beerpoi.getName())
					.icon(BitmapDescriptorFactory.fromBitmap(drawclass
							.drawTextToBitmap(getApplicationContext(),
									icon, number, "Roboto-Light")))
					);
		}
		
				
		// create the markers from database
		List<StaticMarker> dbmarkers = dbclass.queryMarkersFromDatabase(this);

		for (int i = 0; i < dbmarkers.size(); i++) {
			StaticMarker marker = dbmarkers.get(i);
			String title = marker.getTitle();
			String filename = marker.getIcon().trim();
			int icon = this.getResources().getIdentifier(filename, "drawable", this.getPackageName());
			if (title.length() > 1){
				// for markers that should have an info window
				mMap.addMarker(new MarkerOptions()
					.position(marker.getLatLng())
					.title(title)
					.snippet(marker.getDescription())
					.icon(BitmapDescriptorFactory.fromResource(
								icon))
					.flat(true)
					.rotation(marker.getRotation())
					);
			} else {
				// for all others, without info window
				mMap.addMarker(new MarkerOptions()
					.position(marker.getLatLng())
					.icon(BitmapDescriptorFactory.fromResource(
								icon))
					.flat(true)
					.rotation(marker.getRotation())
					);
			}
		}

		// create the pois from database
		List<Poi> dbpois = dbclass.queryPoisFromDatabase(this);

		for (int i = 0; i < dbpois.size(); i++) {
			Poi poi = dbpois.get(i);
			int icon = getResources().getIdentifier(poi.getIcon(), "drawable", this.getPackageName());
			mMap.addMarker(new MarkerOptions()
					.position(poi.getLatLng())
					.title(poi.getTitle())
					.snippet(poi.getCategory())
					.icon(BitmapDescriptorFactory.fromBitmap(drawclass
							.drawTextToBitmap(getApplicationContext(),
									icon, poi.getNumber(), "DINNextRounded"))));
		}
	}

	
	
	// Zoom to city center button
	public void zoomCityCenter(View view) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				48.9744094, 14.4746094), 16));
	}

	// Zoom to overview button
	public void zoomOverview(View view) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				48.9799567, 14.4759178), 14));
	}

	// Zoom to selected poi from description
	public void zoomFromDescription(Double lat, Double lon) {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon),
				18));
	}

	// this is what happens when an info window is clicked
	@Override
	public void onInfoWindowClick(Marker marker) {
		String title = marker.getTitle();
		LatLng position = marker.getPosition();
		if (marker.isFlat()) {
			// do nothing
		} else if ( marker.getSnippet().equals("Water/river walk"))	{
			// start the description with extras in the intent
			Intent walkwaterinfowindowintent = new Intent(this,
					WalksWaterDescriptionActivity.class);
			walkwaterinfowindowintent.putExtra("walkWaterPoi", String.valueOf(position));
			startActivity(walkwaterinfowindowintent);
			
		} else if ( marker.getSnippet().equals("Sights walk"))	{
				// start the description with extras in the intent
			Intent walksightsinfowindowintent = new Intent(this,
					WalksSightsDescriptionActivity.class);
			walksightsinfowindowintent.putExtra("walkSightsPoi", String.valueOf(position));
			startActivity(walksightsinfowindowintent);
			
		} else if ( marker.getSnippet().equals("Beer walk"))	{
			// start the description with extras in the intent
			Intent walkbeerinfowindowintent = new Intent(this,
					WalksBeerDescriptionActivity.class);
			walkbeerinfowindowintent.putExtra("walkBeerPoi", String.valueOf(position));
			startActivity(walkbeerinfowindowintent);
				
		} else {
			// start the description with extras in the intent
			Intent infowindowintent = new Intent(this,
					PlacesDescriptionActivity.class);
			infowindowintent.putExtra("listDataChild", title);
			startActivity(infowindowintent);
		}
	}
	

	// this method creates the pop up info when clicking the bulb icon
	private int createPopUp(int i) {
		
		List<Hint> hintlist = dbclass.queryHintsFromDatabase(this);

		// Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(this, R.style.PopUpHint));

		// Chain together various setter methods to set the dialog
		// characteristics
		final int count = hintlist.size();
		if (i < count && i >= 0) {
			Hint hint = hintlist.get(i);
			String message = hint.getHinttext();
			int hintnumber = i + 1;
			builder.setTitle(R.string.actlikealocal)
					.setMessage(Html.fromHtml("<p align='justify'>" + message + "</p> (Hint " + hintnumber + "/"+ count +")"))
					.setIcon(R.drawable.action_bulb_blue)
					.setNeutralButton(R.string.close,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User clicked 'close' button
								}
							});

			final int h = i - 1;
			builder.setNegativeButton(R.string.previous,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked 'previous' button
							if (h >= 0) {
								createPopUp(h);
							} else {
								int g = count - 1;
								// start with the last hint after first is
								// displayed
								createPopUp(g);
							}
						}
					});

			final int j = i + 1;
			builder.setPositiveButton(R.string.next,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked 'Next' button
							if (j < count) {
								createPopUp(j);
							} else {
								// start with the first hint after last is
								// displayed
								createPopUp(0);
							}
						}
					});

			// Get the AlertDialog from create()
			AlertDialog popup = builder.create();
			popup.show();

		} else {
			// for debugging only. the user should never get this toast :)
			Context context = getApplicationContext();
			CharSequence text = "something is wrong...";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		return i;
	}

	// on long click listener
	@Override
	public void onMapLongClick(LatLng point) {

		LayoutInflater inflater = getLayoutInflater();
		// Inflate the Layout
		View layout = inflater.inflate(R.layout.custom_toast_map,
				(ViewGroup) findViewById(R.id.custom_toast_layout));
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.LEFT | Gravity.BOTTOM, 20, 20);
		toast.setView(layout);
		toast.show();
	}

	// the following methods are used for retrieving device's location
	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			// OnConnectionFailedListener
			mLocationClient = new LocationClient(getApplicationContext(), this,
					this);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// do nothing
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// LocationListener
		mLocationClient.requestLocationUpdates(REQUEST, this);
	}

	@Override
	public void onDisconnected() {
		// do nothing
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// do nothing
	}

	@Override
	public boolean onMyLocationButtonClick() {
		return false;
	}

}
