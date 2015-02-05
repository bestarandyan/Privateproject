package com.qingfengweb.pinhuo.adapter;

import java.util.List;
import java.util.Map;

import com.qingfengweb.pinhuo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AreaSelectAdapter extends BaseAdapter {
	Context context;
	List<Map<String,Object>> list;
	public AreaSelectAdapter(Context context,List<Map<String,Object>> list) {
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
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_area_select, null);
			holder = new ViewHolder();
			holder.View = convertView.findViewById(R.id.view);
			holder.textView = (TextView) convertView.findViewById(R.id.nameTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		String name = map.get("name").toString();
		holder.textView.setText(name);
		return convertView;
	}
	
	class ViewHolder{
		View View;
		TextView textView;
	}

}
