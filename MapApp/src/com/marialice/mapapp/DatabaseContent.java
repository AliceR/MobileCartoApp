package com.marialice.mapapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.Marker;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseContent extends Activity{
	SQLiteDatabase db = null;
	Cursor dbCursor;
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	
	
	public final List<Marker> dbMarker = new ArrayList<Marker>();
	
	public final List<String> listSightseeing = new ArrayList<String>();
	public final List<String> listMuseum = new ArrayList<String>();
	public final List<String> listShopping = new ArrayList<String>();
	public final List<String> listEat = new ArrayList<String>();
	public final List<String> listCafe = new ArrayList<String>();
	public final List<String> listBar = new ArrayList<String>();
	public final List<String> listHidden = new ArrayList<String>();
	
	
	public void queryDataFromDatabase(){
		try {
			dbHelper.createDataBase();
		} catch (IOException ioe) {
		}
		try {
			db = dbHelper.getDataBase();
			dbCursor = db.rawQuery(
					"SELECT * FROM cbpois;",
					null);
			dbCursor.moveToFirst();
			
			int idindex = dbCursor.getColumnIndex("id");
			int latindex = dbCursor.getColumnIndex("lat");
			int lonindex = dbCursor.getColumnIndex("lon");
			int catindex = dbCursor.getColumnIndex("category");
			int titleindex = dbCursor.getColumnIndex("title");
			int descindex = dbCursor.getColumnIndex("description");
			
			return;
		} finally {
			if (db != null) {
				dbHelper.close();
			}
		}
	}
}
