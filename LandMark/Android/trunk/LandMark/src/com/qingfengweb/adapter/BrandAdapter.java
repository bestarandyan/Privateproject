package com.qingfengweb.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class BrandAdapter  extends BaseAdapter{
	Context context;
	ArrayList<String> list;
	public BrandAdapter(Context context,ArrayList<String> list) {
		this.context = context;
		this.list = list;
				
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView brandTv = new TextView(context);
		if(list.size()>0 && position < list.size()){
			brandTv.setText(list.get(position));
		}else{
		}
		return brandTv;
	}
}
