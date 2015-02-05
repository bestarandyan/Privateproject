package com.qingfengweb.piaoguanjia.orderSystem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.piaoguanjia.orderSystem.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OptionAdapter extends BaseAdapter {
	public ArrayList<HashMap<String, Object>> list;
	public Context context;
	public ListView listview;
	private int selectedPosition = -1;// 选中的位置
	private Handler handler;

	public OptionAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, ListView listview,
			Handler handler) {
		this.context = context;
		this.list = list;
		this.listview = listview;
		this.handler = handler;
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

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_option_list, null);
			holder.textview = (TextView) convertView.findViewById(R.id.text);
			holder.view = (ImageView) convertView.findViewById(R.id.view);
			holder.view1 = convertView.findViewById(R.id.view1);
			holder.view2 = convertView.findViewById(R.id.view2);
			holder.view3 = convertView.findViewById(R.id.view3);
			holder.view4 = convertView.findViewById(R.id.view4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setContent(position);
		if (selectedPosition == position) {
			if (listview.getId() == R.id.listview1) {
				holder.view1.setVisibility(View.VISIBLE);
				holder.view2.setVisibility(View.VISIBLE);
				holder.view3.setVisibility(View.VISIBLE);
				holder.view4.setVisibility(View.INVISIBLE);
				convertView.setBackgroundColor(Color.parseColor("#ffffff"));
				holder.view.setBackgroundResource(R.drawable.product_view_ico3);
			} else if (listview.getId() == R.id.listview2) {
				holder.textview.setTextColor(Color.parseColor("#ffffff"));
				convertView.setBackgroundColor(Color.parseColor("#FF8201"));
				holder.view.setVisibility(View.INVISIBLE);
				holder.view1.setVisibility(View.INVISIBLE);
				holder.view2.setVisibility(View.INVISIBLE);
				holder.view3.setVisibility(View.INVISIBLE);
				holder.view4.setVisibility(View.INVISIBLE);
			}
		} else {
			if (listview.getId() == R.id.listview1) {
				holder.view1.setVisibility(View.INVISIBLE);
				holder.view2.setVisibility(View.INVISIBLE);
				holder.view3.setVisibility(View.INVISIBLE);
				holder.view4.setVisibility(View.VISIBLE);
				convertView.setBackgroundColor(Color.parseColor("#EDEBEC"));
				holder.view.setBackgroundResource(R.drawable.product_view_ico4);
			} else if (listview.getId() == R.id.listview2) {
				holder.view1.setVisibility(View.INVISIBLE);
				holder.view2.setVisibility(View.INVISIBLE);
				holder.view3.setVisibility(View.INVISIBLE);
				holder.view4.setVisibility(View.INVISIBLE);
				holder.textview.setTextColor(Color.parseColor("#FF8201"));
				convertView.setBackgroundColor(Color.parseColor("#ffffff"));
				holder.view.setVisibility(View.INVISIBLE);
			}

		}
		convertView.setId(position);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setSelectedPosition(v.getId());
				notifyDataSetChanged();
				if (listview.getId() == R.id.listview1) {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("id", v.getId());
					msg.setData(bundle);
					msg.what = 5;
					handler.sendMessage(msg);
				} else if (listview.getId() == R.id.listview2) {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("id", v.getId());
					msg.setData(bundle);
					msg.what = 6;
					handler.sendMessage(msg);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		private TextView textview;
		private ImageView view;
		private View view1, view2, view3, view4;

		public void setContent(int position) {
			textview.setText(list.get(position).get("name").toString());
		}
	}
}
