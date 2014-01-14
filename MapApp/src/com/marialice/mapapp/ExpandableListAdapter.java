package com.marialice.mapapp;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	} 
	
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);
		int id_res = 0;
		long group_id = getGroupId(groupPosition);
		
		if (group_id == 0) {
			id_res = R.drawable.poi_sightseeing;
		} else if (group_id == 1) {
			id_res = R.drawable.poi_museum;
		} else if (group_id == 2) {
			id_res = R.drawable.poi_shopping;
		} else if (group_id == 3) {
			id_res = R.drawable.poi_eat;
		} else if (group_id == 4) {
			id_res = R.drawable.poi_cafe;
		} else if (group_id == 5) {
			id_res = R.drawable.poi_bar;
		} else if (group_id == 6) {
			id_res = R.drawable.poi_hidden;
		}

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
			ViewHolderListitem holderImage = new ViewHolderListitem(convertView);
			holderImage.imageview.setImageResource(id_res);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.PoiListItem);

		txtListChild.setText(childText);
		return convertView;
	}
	
	

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}
	
	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		String headerTitle = (String) getGroup(groupPosition);
		int id_res = 0;

		if (headerTitle.equals("Sightseeing")) {
			id_res = R.drawable.poi_sightseeing;
		} else if (headerTitle.equals("Museum, venue")) {
			id_res = R.drawable.poi_museum;
		} else if (headerTitle.equals("Shopping")) {
			id_res = R.drawable.poi_shopping;
		} else if (headerTitle.equals("Eat, drink")) {
			id_res = R.drawable.poi_eat;
		} else if (headerTitle.equals("Café, tea room")) {
			id_res = R.drawable.poi_cafe;
		} else if (headerTitle.equals("Bar, club")) {
			id_res = R.drawable.poi_bar;
		} else if (headerTitle.equals("Hidden, chill out")) {
			id_res = R.drawable.poi_hidden;
		}
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
			ViewHolderHeader holder = new ViewHolderHeader(convertView);
			holder.imageview.setImageResource(id_res);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}
	
	public class ViewHolderHeader {
		public TextView text;
		public ImageView imageview;
		public ViewHolderHeader(View v) {
			this.imageview = (ImageView) v.findViewById(R.id.headerimage);
		}
	}
	public class ViewHolderListitem {
		public TextView text;
		public ImageView imageview;
		public ViewHolderListitem(View v) {
			this.imageview = (ImageView) v.findViewById(R.id.listimage);
		}
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}