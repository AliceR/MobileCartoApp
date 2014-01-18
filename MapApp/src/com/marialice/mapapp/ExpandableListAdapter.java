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
		/*
		 * int childlistimage = 0; long group_id = getGroupId(groupPosition);
		 * 
		 * if (group_id == 0) { childlistimage = R.drawable.poi_sightseeing; }
		 * else if (group_id == 1) { childlistimage = R.drawable.poi_museum; }
		 * else if (group_id == 2) { childlistimage = R.drawable.poi_shopping; }
		 * else if (group_id == 3) { childlistimage = R.drawable.poi_eat; }
		 * else if (group_id == 4) { childlistimage = R.drawable.poi_cafe; } 
		 * else if (group_id == 5) { childlistimage = R.drawable.poi_bar; } 
		 * else if (group_id == 6) { childlistimage = R.drawable.poi_hidden; }
		 */

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
			// ViewHolderListitem holderImage = new
			// ViewHolderListitem(convertView);
			// holderImage.childimageview.setImageResource(childlistimage);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.PoiListItem);
		// txtListChild.setTypeface(tf, Typeface.BOLD);
		txtListChild.setText(childText);

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

		int group_id = (int) getGroupId(groupPosition); // we need the id wrt headerTitle 	
		int headercolor = 0;
		switch (group_id) {
		case 0:
			headercolor = R.color.lime;
			break;
		case 1:
			headercolor = R.color.fuchsia;
			break;
		case 2:
			headercolor = R.color.maroon;
			break;
		case 3:
			headercolor = R.color.teal;
			break;
		case 4:
			headercolor = R.color.green;
			break;
		case 5:
			headercolor = R.color.navy;
			break;
		case 6:
			headercolor = R.color.actionbar;
			break;
		default:
			headercolor = R.color.silver;
			break;
		}

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView ListHeader = (TextView) convertView
				.findViewById(R.id.ListHeader);
		// ListHeader.setTypeface(tf, Typeface.BOLD);     // for some reason this makes trouble...
		ListHeader.setText(headerTitle);
		ListHeader.setBackgroundColor(context.getResources().getColor(
				headercolor));

		return convertView;
	}

	/*
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