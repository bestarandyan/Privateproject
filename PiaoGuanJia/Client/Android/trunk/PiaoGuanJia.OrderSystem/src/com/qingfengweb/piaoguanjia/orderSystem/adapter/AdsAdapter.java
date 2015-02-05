package com.qingfengweb.piaoguanjia.orderSystem.adapter;


import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.piaoguanjia.orderSystem.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 */
public class AdsAdapter extends BaseAdapter {
	public ArrayList<String> list;
	public Context context;

	public AdsAdapter(Context context,
			ArrayList<String> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.adslistitem, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setContent(position);
		return convertView;
	}

	class ViewHolder {

		public void setContent(int position) {
		}
	}
}
