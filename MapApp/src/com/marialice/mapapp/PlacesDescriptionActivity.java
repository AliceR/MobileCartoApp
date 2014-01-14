package com.marialice.mapapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class PlacesDescriptionActivity extends Activity {
	SQLiteDatabase db = null;
	Cursor dbCursor;
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	
	TextView textViewTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_places_description);
		setupActionBar();
		createDetails();
	//	addDescriptionFromDatabase();

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

		Intent i = getIntent();
		// getting attached intent data
		String poi_name = i.getStringExtra("listDataChild");
		// displaying selected point name
		textViewTitle.setText(poi_name);

		ImageView imgView1 = (ImageView) findViewById(R.id.desc_icon);
		imgView1.setImageBitmap(drawTextToBitmap(getApplicationContext(),
				R.drawable.poi_shopping, "15")); // MAKE DYNAMIC!
	}
	
	
	// draw text over the icons - pois numbers
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
		int x = (bitmap.getWidth() - bounds.width()) / 10 * 4;
		int y = (bitmap.getHeight() + bounds.height()) / 4;

		canvas.drawText(gText, x * scale, y * scale, paint);

		return bitmap;
	}
	
	/*public void addDescriptionFromDatabase() {
		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery("SELECT description FROM cbpois WHERE title = 'SECRET OF BUDVAR';", null);
			dbCursor.moveToFirst();
			int des = dbCursor.getColumnIndex("description");
			while (!dbCursor.isAfterLast()) {

				String description = dbCursor.getString(des);

				TextView textViewDescription = (TextView) findViewById(R.id.TextView2);
				textViewDescription.setText(description);
				dbCursor.moveToNext();
			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
	}
*/

}
