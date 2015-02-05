package com.qingfengweb.adapter;

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

import com.qingfengweb.id.blm_goldenLadies.R;

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
		convertView = LayoutInflater.from(context).inflate(R.layout.item_helplist, null);
		TextView textView;
		textView = (TextView) convertView.findViewById(R.id.helpListText);
		textView.setText(list.get(position).get("title").toString());
		if(position%2 == 0){
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}else{
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

}
