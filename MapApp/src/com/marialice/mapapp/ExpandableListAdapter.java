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

	private Context context;
	private List<String> listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> listDataChild;

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
		int childlistimage = 0;
		long group_id = getGroupId(groupPosition);

		if (group_id == 0) {
			childlistimage = R.drawable.poi_sightseeing;
		} else if (group_id == 1) {
			childlistimage = R.drawable.poi_museum;
		} else if (group_id == 2) {
			childlistimage = R.drawable.poi_shopping;
		} else if (group_id == 3) {
			childlistimage = R.drawable.poi_eat;
		} else if (group_id == 4) {
			childlistimage = R.drawable.poi_cafe;
		} else if (group_id == 5) {
			childlistimage = R.drawable.poi_bar;
		} else if (group_id == 6) {
			childlistimage = R.drawable.poi_hidden;
		}

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
			ViewHolderListitem holderImage = new ViewHolderListitem(convertView);
			holderImage.childimageview.setImageResource(childlistimage);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.PoiListItem);

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
		return this.listDataHeader.get(groupPosition);
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
		
		int headerlistimage = 0;
		long group_id = getGroupId(groupPosition);

		if (group_id == 0) {
			headerlistimage = R.drawable.poi_sightseeing;
		} else if (group_id == 1) {
			headerlistimage = R.drawable.poi_museum;
		} else if (group_id == 2) {
			headerlistimage = R.drawable.poi_shopping;
		} else if (group_id == 3) {
			headerlistimage = R.drawable.poi_eat;
		} else if (group_id == 4) {
			headerlistimage = R.drawable.poi_cafe;
		} else if (group_id == 5) {
			headerlistimage = R.drawable.poi_bar;
		} else if (group_id == 6) {
			headerlistimage = R.drawable.poi_hidden;
		}

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
			ViewHolderHeader holder = new ViewHolderHeader(convertView);
			holder.headerimageview.setImageResource(headerlistimage);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	public class ViewHolderHeader {
		public TextView headertext;
		public ImageView headerimageview;

		public ViewHolderHeader(View v) {
			this.headerimageview = (ImageView) v.findViewById(R.id.headerimage);
		}
	}

	public class ViewHolderListitem {
		public TextView childtext;
		public ImageView childimageview;

		public ViewHolderListitem(View v) {
			this.childimageview = (ImageView) v.findViewById(R.id.listimage);
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