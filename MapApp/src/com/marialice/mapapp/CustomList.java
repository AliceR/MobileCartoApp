package com.marialice.mapapp;

/// I THINK THIS FILE IS NOT USED ANYMORE???
/// 
/*
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] title;

	public CustomList(Activity context, String[] title) {
		super(context, R.layout.list_item, title);
		this.context = context;
		this.title = title;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_item, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.poi_item);
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.poi_icon);
		txtTitle.setText(title[position]);
		//imageView.setImageResource(R.drawable.poi_hidden);
		return rowView;
	}
}*/