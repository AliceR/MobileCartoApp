package com.marialice.mapapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	public TextView text;
	public ImageView imageview;

	public ViewHolder(View v) {
		this.imageview = (ImageView) v.findViewById(R.id.image1);
	}

}
