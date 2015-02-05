package com.boluomi.children.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boluomi.children.R;

public class HelpListAdapter extends BaseAdapter{
	private List<Map<String,Object>> list;
	private Context context;
	public HelpListAdapter(Context context,List<Map<String,Object>> list) {
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout itemLayout = null;
		TextView textView;
			LayoutInflater l = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (LinearLayout) l.inflate(R.layout.item_helplist, null);
		textView = (TextView) itemLayout.findViewById(R.id.helpListText);
		textView.setText(list.get(position).get("title").toString());
		if(position%2 == 0){
			itemLayout.setBackgroundColor(Color.TRANSPARENT);
		}else{
			itemLayout.setBackgroundColor(Color.TRANSPARENT);
		}
		return itemLayout;
	}

}
