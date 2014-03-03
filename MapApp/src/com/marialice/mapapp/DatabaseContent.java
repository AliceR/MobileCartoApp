package com.marialice.mapapp;

/* 
 * this is a class to provide content of the db in a nice, practical format.
 * we can use the Lists to access our data all over the app
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseContent extends Activity {

	public List<Poi> queryPoisFromDatabase(Context context) {

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
			dbCursor = db.rawQuery("SELECT * FROM pois;", null);
			dbCursor.moveToFirst();

			int idindex = dbCursor.getColumnIndex("id");
			int latindex = dbCursor.getColumnIndex("lat");
			int lonindex = dbCursor.getColumnIndex("lon");
			int catindex = dbCursor.getColumnIndex("category");
			int titleindex = dbCursor.getColumnIndex("title");
			int descindex = dbCursor.getColumnIndex("description");
			int wifiindex = dbCursor.getColumnIndex("wifi");
			int terraceindex = dbCursor.getColumnIndex("terrace");
			int sundaysindex = dbCursor.getColumnIndex("sundays");
			int calmplaceindex = dbCursor.getColumnIndex("calmplace");
			int nonsmokingindex = dbCursor.getColumnIndex("nonsmoking");
			int touristclassicindex = dbCursor.getColumnIndex("touristclassic");

			while (!dbCursor.isAfterLast()) {

				int id = dbCursor.getInt(idindex);
				Double lat = dbCursor.getDouble(latindex);
				Double lon = dbCursor.getDouble(lonindex);
				String category = dbCursor.getString(catindex);
				String title = dbCursor.getString(titleindex);
				String description = dbCursor.getString(descindex);
				Boolean wifi = dbCursor.getInt(wifiindex) > 0;
				Boolean terrace = dbCursor.getInt(terraceindex) > 0;
				Boolean sundays = dbCursor.getInt(sundaysindex) > 0;
				Boolean calmplace = dbCursor.getInt(calmplaceindex) > 0;
				Boolean nonsmoking = dbCursor.getInt(nonsmokingindex) > 0;
				Boolean touristclassic = dbCursor.getInt(touristclassicindex) > 0;

				Poi poi = new Poi();

				poi.setId(id);
				poi.setLat(lat);
				poi.setLon(lon);
				poi.setCategory(category);
				poi.setTitle(title);
				poi.setDescription(description);
				poi.setWifi(wifi);
				poi.setTerrace(terrace);
				poi.setSundays(sundays);
				poi.setCalmplace(calmplace);
				poi.setNonsmoking(nonsmoking);
				poi.setTouristclassic(touristclassic);

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
	
	public List<StaticMarker> queryMarkersFromDatabase(Context context) {

		SQLiteDatabase db = null;
		Cursor dbCursor;
		DatabaseHelper dbHelper = new DatabaseHelper(context);

		List<StaticMarker> dbmarkers = new ArrayList<StaticMarker>();

		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery("SELECT * FROM markers;", null);
			dbCursor.moveToFirst();

			int idindex = dbCursor.getColumnIndex("id");
			int latindex = dbCursor.getColumnIndex("lat");
			int lonindex = dbCursor.getColumnIndex("lon");
			int titleindex = dbCursor.getColumnIndex("title");
			int descindex = dbCursor.getColumnIndex("description");
			int categoryindex = dbCursor.getColumnIndex("category");
			int iconindex = dbCursor.getColumnIndex("icon");
			int rotationindex = dbCursor.getColumnIndex("rotation");
			int anchorindex = dbCursor.getColumnIndex("anchor");
			int infowinanchorindex = dbCursor.getColumnIndex("infowinanchor");
			

			while (!dbCursor.isAfterLast()) {

				int id = dbCursor.getInt(idindex);
				Double lat = dbCursor.getDouble(latindex);
				Double lon = dbCursor.getDouble(lonindex);
				String title = dbCursor.getString(titleindex);
				String description = dbCursor.getString(descindex);
				String category = dbCursor.getString(categoryindex);
				String icon = dbCursor.getString(iconindex);
				int rotation = dbCursor.getInt(rotationindex);
				String anchor = dbCursor.getString(anchorindex);
				String infowinanchor = dbCursor.getString(infowinanchorindex);

				StaticMarker marker = new StaticMarker();

				marker.setId(id);
				marker.setLat(lat);
				marker.setLon(lon);
				marker.setTitle(title);
				marker.setDescription(description);
				marker.setCategory(category);
				marker.setIcon(icon);
				marker.setRotation(rotation);
				marker.setAnchor(anchor);
				marker.setInfowinanchor(infowinanchor);

				dbmarkers.add(marker);

				dbCursor.moveToNext();

			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
		return dbmarkers;
	}
	
	public List<Hint> queryHintsFromDatabase(Context context) {

		SQLiteDatabase db = null;
		Cursor dbCursor;
		DatabaseHelper dbHelper = new DatabaseHelper(context);

		List<Hint> dbhints = new ArrayList<Hint>();

		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery("SELECT * FROM hints;", null);
			dbCursor.moveToFirst();

			int idindex = dbCursor.getColumnIndex("id");
			int hinttextindex = dbCursor.getColumnIndex("hint");

			while (!dbCursor.isAfterLast()) {

				int id = dbCursor.getInt(idindex);
				String hinttext = dbCursor.getString(hinttextindex);

				Hint hint = new Hint();

				hint.setId(id);
				hint.setHinttext(hinttext);

				dbhints.add(hint);

				dbCursor.moveToNext();

			}
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
		return dbhints;
	}
	
}
