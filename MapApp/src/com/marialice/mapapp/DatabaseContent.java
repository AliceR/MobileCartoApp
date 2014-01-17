package com.marialice.mapapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseContent extends Activity {

//	public final List<String> listSightseeing = new ArrayList<String>();
//	public final List<String> listMuseum = new ArrayList<String>();
//	public final List<String> listShopping = new ArrayList<String>();
//	public final List<String> listEat = new ArrayList<String>();
//	public final List<String> listCafe = new ArrayList<String>();
//	public final List<String> listBar = new ArrayList<String>();
//	public final List<String> listHidden = new ArrayList<String>();

	public List<Poi> queryDataFromDatabase(Context context) {
		
		SQLiteDatabase db = null;
		Cursor dbCursor;
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		
		List<Poi> dbpois = new ArrayList<Poi>();

		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery("SELECT * FROM cbpois;", null);
			dbCursor.moveToFirst();

			int idindex = dbCursor.getColumnIndex("id");
			int latindex = dbCursor.getColumnIndex("lat");
			int lonindex = dbCursor.getColumnIndex("lon");
			int catindex = dbCursor.getColumnIndex("category");
			int titleindex = dbCursor.getColumnIndex("title");
			int descindex = dbCursor.getColumnIndex("description");

			while (!dbCursor.isAfterLast()) {

				int id = dbCursor.getInt(idindex);
				Double lat = dbCursor.getDouble(latindex);
				Double lon = dbCursor.getDouble(lonindex);
				String category = dbCursor.getString(catindex);
				String title = dbCursor.getString(titleindex);
				String description = dbCursor.getString(descindex);

				Poi poi = new Poi();

				poi.setId(id);
				poi.setLat(lat);
				poi.setLon(lon);
				poi.setCategory(category);
				poi.setTitle(title);
				poi.setDescription(description);
				
				dbpois.add(poi);

				dbCursor.moveToNext();
				
			}		
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
		return dbpois;
	}
}
