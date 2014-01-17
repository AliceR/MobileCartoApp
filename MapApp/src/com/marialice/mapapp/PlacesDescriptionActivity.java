package com.marialice.mapapp;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlacesDescriptionActivity extends Activity{
	SQLiteDatabase db = null;
	Cursor dbCursor;
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	
	TextToBitmap drawtext = new TextToBitmap();
	
	public Double poi_lat;
	public Double poi_lon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_places_description);
		setupActionBar();
		createDetails();

	}

	private void setupActionBar() {
		// Show the Up button in the action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.description, menu);
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

	public void createDetails() {
		TextView textViewTitle = (TextView) findViewById(R.id.desc_title);
		TextView textViewDesc = (TextView) findViewById(R.id.description);
		ImageView poi_icon = (ImageView) findViewById(R.id.desc_icon);
		

		Intent i = getIntent();
		// getting attached intent data
		String poi_namels = i.getStringExtra("listDataChild");
		// displaying selected point name
		textViewTitle.setText(poi_namels);

		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery("SELECT * FROM cbpois;", null);

			int lat = dbCursor.getColumnIndex("lat");
			int lon = dbCursor.getColumnIndex("lon");
			int id = dbCursor.getColumnIndex("id");
			int cat = dbCursor.getColumnIndex("category");
			int title = dbCursor.getColumnIndex("title");
			int desc = dbCursor.getColumnIndex("description");

			dbCursor.moveToFirst();
			while (!dbCursor.isAfterLast()) {
				String poi_namedb = dbCursor.getString(title);
				String category = dbCursor.getString(cat);
				String number = dbCursor.getString(id);
				
				poi_lat = dbCursor.getDouble(lat);
				poi_lon = dbCursor.getDouble(lon);
				
				int symbol = 0;

				if (poi_namedb.equals(poi_namels)) {
					String description = dbCursor.getString(desc);
					textViewDesc.setText(description);

					if (category.equals("bar")) {
						symbol = R.drawable.poi_bar;
					} else if (category.equals("cafe")) {
						symbol = R.drawable.poi_cafe;
					} else if (category.equals("eat")) {
						symbol = R.drawable.poi_shadow;
					} else if (category.equals("poi_hidden")) {
						symbol = R.drawable.poi_hidden;
					} else if (category.equals("museum")) {
						symbol = R.drawable.poi_museum;
					} else if (category.equals("shopping")) {
						symbol = R.drawable.poi_shopping;
					} else if (category.equals("sightseeing")) {
						symbol = R.drawable.poi_sightseeing;
					} else {
						symbol = R.drawable.poi_bar;
					}
					poi_icon.setImageBitmap(drawtext.drawTextToBitmap(
							getApplicationContext(), symbol, number));
					
					return;

				} else {
					textViewDesc.setText("not working");
				}
				dbCursor.moveToNext();
			}

		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
	}

	/*// draw text over the icons - pois numbers
	private Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
		Resources resources = gContext.getResources();
		float scale = resources.getDisplayMetrics().density;
		Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);

		android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
		if (bitmapConfig == null) {
			bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
		}
		bitmap = bitmap.copy(bitmapConfig, true);

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/DINNextRounded.otf");

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setTypeface(tf);
		paint.setTextSize((int) (13 * scale));
		// paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

		Rect bounds = new Rect();
		paint.getTextBounds(gText, 0, gText.length(), bounds);
		int x = (bitmap.getWidth() - bounds.width()) / 4;
		int y = (bitmap.getHeight() + bounds.height()) / 6;

		canvas.drawText(gText, x * scale, y * scale, paint);

		return bitmap;
	}
*/
	public void gotomap(View view) {
		if (poi_lat == null) {
			Context context = getApplicationContext();
			CharSequence text = "no lat lon values!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		} else {		
			Intent mapintent = new Intent(this, MapActivity.class);	
			mapintent.putExtra("lat", poi_lat);
			mapintent.putExtra("lon", poi_lon);			
			startActivity(mapintent);
		}
	}

}
