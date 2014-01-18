package com.marialice.mapapp;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	// include our classes
	DatabaseContent dbclass = new DatabaseContent();

	private Context context;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;

	// private Typeface tf = Typeface.createFromAsset(context.getAssets(),
	// "fonts/DINNextRounded.otf");

	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData) {
		this.context = context;
		this.listDataHeader = listDataHeader;
		this.listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		String childText = (String) getChild(groupPosition, childPosition);

		int group_id = (int) getGroupId(groupPosition);
		int color = 0;
		switch (group_id) {
		case 0:
			color = R.color.sightseeingTr;
			break;
		case 1:
			color = R.color.museumTr;
			break;
		case 2:
			color = R.color.shoppingTr;
			break;
		case 3:
			color = R.color.eatTr;
			break;
		case 4:
			color = R.color.cafeTr;
			break;
		case 5:
			color = R.color.barTr;
			break;
		case 6:
			color = R.color.hiddenTr;
			break;
		default:
			color = R.color.silver;
			break;
		}
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.PoiListItem);
		// txtListChild.setTypeface(tf, Typeface.BOLD);
		txtListChild.setText(childText);
		txtListChild.setBackgroundColor(context.getResources().getColor(color));

		List<Poi> dbpois = dbclass.queryDataFromDatabase(context);
		TextView numberListChild = (TextView) convertView
				.findViewById(R.id.PoiListNumber);
		for (int i = 0; i < dbpois.size(); i++) {
			Poi poi = dbpois.get(i);
			String titledb = poi.getTitle();
			String number = poi.getNumber();
			if (titledb.equals(childText)) {
				numberListChild.setText(number);
				numberListChild.setBackgroundColor(context.getResources()
						.getColor(color));
			}
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.listDataChild.get(this.listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		String headerTitle = this.listDataHeader.get(groupPosition);
		return headerTitle;
	}

	@Override
	public int getGroupCount() {
		return this.listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		String headerTitle = (String) getGroup(groupPosition);

		int group_id = (int) getGroupId(groupPosition);
		int color = 0;
		switch (group_id) {
		case 0:
			color = R.color.sightseeing;
			break;
		case 1:
			color = R.color.museum;
			break;
		case 2:
			color = R.color.shopping;
			break;
		case 3:
			color = R.color.eat;
			break;
		case 4:
			color = R.color.cafe;
			break;
		case 5:
			color = R.color.bar;
			break;
		case 6:
			color = R.color.hidden;
			break;
		default:
			color = R.color.silver;
			break;
		}

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView ListHeader = (TextView) convertView
				.findViewById(R.id.ListHeader);
		// ListHeader.setTypeface(tf, Typeface.BOLD);
		// for some reason this makes trouble... same problem as with drawables?
		ListHeader.setText(headerTitle);
		ListHeader.setBackgroundColor(context.getResources().getColor(color));

		return convertView;
	}

	/*
	 * For some unknown reason the use of a drawable resource instead of
	 * background color is instable... in case we find out, why, and how to
	 * avoid the mistake, we might go back to using an image
	 * 
	 * public class ViewHolderListitem { public TextView childtext; public
	 * ImageView childimageview;
	 * 
	 * public ViewHolderListitem(View v) { this.childimageview = (ImageView)
	 * v.findViewById(R.id.listimage); } }
	 */

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}