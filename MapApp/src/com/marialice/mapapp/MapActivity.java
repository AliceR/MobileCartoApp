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
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

public class MapActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
		OnMyLocationButtonClickListener, OnInfoWindowClickListener, OnMapLongClickListener {

	// include our classes
	TextToBitmap drawclass = new TextToBitmap();
	DatabaseContent dbclass = new DatabaseContent();

	// private static final String MAPBOX_BASEMAP_URL_FORMAT =
	// "http://api.tiles.mapbox.com/v3/maridani.go26lm2h/%d/%d/%d.png";
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
		switch (item.getItemId()) {
		case R.id.goto_actlikelocal:
			// start the pop up with a random hint
			Random randomi = new Random();
			int i = randomi.nextInt((9 - 0) + 1) + 0;
			// nextInt((max - min) + 1) + min;
			createPopUp(i);
			return true;

		case R.id.goto_about:
			Intent aboutintent = new Intent(MapActivity.this,
					AboutActivity.class);
			startActivity(aboutintent);
			return true;
		case R.id.goto_places:
			Intent placesintent = new Intent(MapActivity.this,
					PlacesActivity.class);
			startActivity(placesintent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

		// call the methods that create the markers
		addMarkersToMap();

		List<Poi> dbpois = dbclass.queryDataFromDatabase(this);

		for (int i = 0; i < dbpois.size(); i++) {
			Poi poi = dbpois.get(i);
			mMap.addMarker(new MarkerOptions()
					.position(poi.getLatLng())
					.title(poi.getTitle())
					.snippet(poi.getCategoryName())
					.icon(BitmapDescriptorFactory.fromBitmap(drawclass
							.drawTextToBitmap(getApplicationContext(),
									poi.getSymbol(), poi.getNumber()))));
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

	// the method creates static markers, that are added to the map
	private void addMarkersToMap() {
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.cb_cerna_vez))
				.position(new LatLng(48.9754689, 14.4761153)).anchor(0.5f, 1f)
				.title("Èerná vìž").snippet("Black tower").rotation(10)
				.flat(true).infoWindowAnchor(0.0f, 0.0f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.cb_bila_vez))
				.position(new LatLng(48.9756647, 14.4717539)).anchor(0.5f, 1f)
				.title("Klášter dominikánù").snippet("Dominican monastery")
				.rotation(0).flat(true).infoWindowAnchor(0.5f, 0.5f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.cb_kasna))
				.position(new LatLng(48.9745117, 14.4743214))
				.anchor(0.5f, 0.75f).rotation(4).title("Samsonova kašna")
				.snippet("Samson fountain").flat(true)
				.infoWindowAnchor(0.5f, 0.5f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.cb_bazen))
				.position(new LatLng(48.9744025, 14.4691572))
				.anchor(0.5f, 0.75f).rotation(4).title("Plavecký bazén")
				.snippet("Swiming pool").flat(true)
				.infoWindowAnchor(0.5f, 0.5f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.cb_kino))
				.position(new LatLng(48.9716589, 14.4711061))
				.anchor(0.5f, 0.75f).rotation(0).title("Kino")
				.snippet("Cinema").flat(true).infoWindowAnchor(0.5f, 0.5f));

		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.cb_zeleznapanna))
				.position(new LatLng(48.9728661, 14.4725350))
				.anchor(0.5f, 0.75f).rotation(355).title("Železná panna")
				.snippet("Iron maiden").flat(true).infoWindowAnchor(0.5f, 0.5f));

		// ATM poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9761031, 14.4736317)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9739667, 14.4751539)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9751006, 14.4752786)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9723736, 14.4789214)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9746206, 14.4795131)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9744658, 14.4820906)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9743461, 14.4875100)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9744478, 14.4881442)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_atm))
				.flat(true).position(new LatLng(48.9729239, 14.4873264)));

		// Pharmacy poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_pharmacy)).flat(true)
				.position(new LatLng(48.9744306, 14.4752925)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_pharmacy)).flat(true)
				.position(new LatLng(48.9745075, 14.4823564)));

		// Hospital poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hospital)).flat(true)
				.position(new LatLng(48.9776061, 14.4773158)));

		// Sport poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_sport)).flat(true)
				.position(new LatLng(48.9762472, 14.4692947)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_sport)).flat(true)
				.position(new LatLng(48.9747333, 14.4657783)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_sport)).flat(true)
				.position(new LatLng(48.9721067, 14.4677606)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_sport)).flat(true)
				.position(new LatLng(48.9705756, 14.4712883)));

		// Hostel poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hostel)).flat(true)
				.position(new LatLng(48.9770194, 14.4719103)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hostel)).flat(true)
				.position(new LatLng(48.9717511, 14.4865869)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hostel)).flat(true)
				.position(new LatLng(48.9774292, 14.4799075)));

		// Hungry window poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hungry_window)).flat(true)
				.position(new LatLng(48.9757681, 14.4746567)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hungry_window)).flat(true)
				.position(new LatLng(48.9761078, 14.4732386)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hungry_window)).flat(true)
				.position(new LatLng(48.9743269, 14.4762639)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hungry_window)).flat(true)
				.position(new LatLng(48.9749553, 14.4769472)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hungry_window)).flat(true)
				.position(new LatLng(48.9751936, 14.4779286)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hungry_window)).flat(true)
				.position(new LatLng(48.9765461, 14.4766164)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_hungry_window)).flat(true)
				.position(new LatLng(48.9742575, 14.4797833)));

		// Post poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_post))
				.flat(true).position(new LatLng(48.9739094, 14.4784342)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_post))
				.flat(true).position(new LatLng(48.9755022, 14.4875664)));

		// Shopping centre poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_shopping_centre))
				.flat(true).position(new LatLng(48.9725289, 14.4874875)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_shopping_centre))
				.flat(true).position(new LatLng(48.9741897, 14.4837125)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_shopping_centre))
				.flat(true).position(new LatLng(48.9763278, 14.4740906)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_shopping_centre))
				.flat(true).position(new LatLng(48.9676978, 14.4714622)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_shopping_centre))
				.flat(true).position(new LatLng(48.9678106, 14.4725336)));

		// Grocery shopping poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_grocery)).flat(true)
				.position(new LatLng(48.9730150, 14.4745900)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_grocery)).flat(true)
				.position(new LatLng(48.9731958, 14.4757633)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_grocery)).flat(true)
				.position(new LatLng(48.9737147, 14.4751703)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_grocery)).flat(true)
				.position(new LatLng(48.9738767, 14.4741642)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_grocery)).flat(true)
				.position(new LatLng(48.9758239, 14.4741028)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.poi_grocery)).flat(true)
				.position(new LatLng(48.9761247, 14.4749742)));

		// Infocentre poi
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_info))
				.flat(true).position(new LatLng(48.9744750, 14.4731875)));
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_info))
				.flat(true).position(new LatLng(48.9760192, 14.4725292)));

		// Bus station
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.bus_station)).flat(true)
				.alpha(0.7f).position(new LatLng(48.9726139, 14.4873772))
				.rotation(345));
		// Train station
		mMap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.train_station)).flat(true)
				.alpha(0.7f).position(new LatLng(48.9744633, 14.4885883))
				.rotation(75));

	}

	// this is what happens when an info window is clicked
	@Override
	public void onInfoWindowClick(Marker marker) {
		String title = marker.getTitle();
		if (title.equals("Èerná vìž") | title.equals("Klášter dominikánù")
				| title.equals("Samsonova kašna")
				| title.equals("Plavecký bazén") | title.equals("Kino")
				| title.equals("Železná panna")) {
			// do nothing
		} else {
			// start the description with extras in the intent
			Intent infowindowintent = new Intent(this,
					PlacesDescriptionActivity.class);
			infowindowintent.putExtra("listDataChild", title);
			startActivity(infowindowintent);
		}
	}

	private int createPopUp(int i) {

		List<String> hintlist = new ArrayList<String>();
		String text1 = getResources().getString(R.string.actlikealocal_text1);
		String text2 = getResources().getString(R.string.actlikealocal_text2);
		String text3 = getResources().getString(R.string.actlikealocal_text3);
		String text4 = getResources().getString(R.string.actlikealocal_text4);
		String text5 = getResources().getString(R.string.actlikealocal_text5);
		String text6 = getResources().getString(R.string.actlikealocal_text6);
		String text7 = getResources().getString(R.string.actlikealocal_text7);
		String text8 = getResources().getString(R.string.actlikealocal_text8);
		String text9 = getResources().getString(R.string.actlikealocal_text9);
		String text10 = getResources().getString(R.string.actlikealocal_text10);
		hintlist.add(text1);
		hintlist.add(text2);
		hintlist.add(text3);
		hintlist.add(text4);
		hintlist.add(text5);
		hintlist.add(text6);
		hintlist.add(text7);
		hintlist.add(text8);
		hintlist.add(text9);
		hintlist.add(text10);

		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(this, R.style.PopUpHint));
		// 2. Chain together various setter methods to set the dialog
		// characteristics
		final int count = hintlist.size();	
		if (i < count && i>=0) {
			String message = hintlist.get(i);
			int hintnumber = i+1;
			builder.setTitle(R.string.actlikealocal).setMessage(message+" (Hint "+ hintnumber +"/10)");
			builder.setIcon(R.drawable.action_bulb);

			builder.setNeutralButton(R.string.close,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked 'close' button
						}
					});
			
			final int h = i - 1;
			builder.setNegativeButton(R.string.previous,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked 'previous' button
							if (h>=0){
								createPopUp(h);
							} else {
								int g=count-1;
								// start with the last hint after first is displayed
								createPopUp(g);
							}
						}
					});
			
			final int j = i + 1;
			builder.setPositiveButton(R.string.next,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked 'Next' button
							if (j<count){
								createPopUp(j);
							} else {
								// start with the first hint after last is displayed
								createPopUp(0);
							}
						}
					});
			
			// 3. Get the AlertDialog from create()
			AlertDialog popup = builder.create();
			popup.show();
			
		} else{
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
