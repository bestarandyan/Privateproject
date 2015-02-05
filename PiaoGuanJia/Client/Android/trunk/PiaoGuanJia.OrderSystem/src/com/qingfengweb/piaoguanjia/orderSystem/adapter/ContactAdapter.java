package com.qingfengweb.piaoguanjia.orderSystem.adapter;

import java.util.ArrayList;
import java.util.List;

import com.qingfengweb.piaoguanjia.orderSystem.R;
import com.qingfengweb.piaoguanjia.orderSystem.model.PlayerInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 */
public class ContactAdapter extends BaseAdapter {
	public List<PlayerInfo> list;
	public Context context;

	public ContactAdapter(Context context, List<PlayerInfo> list) {
		this.context = context;
		if(list==null){
			list = new ArrayList<PlayerInfo>();
		}
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
					R.layout.contactlistitem, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setContent(position);
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name;
		public void setContent(int position) {
			if(list.get(position).getName()!=null
					&&!list.get(position).getName().equals("")){
				tv_name.setText(list.get(position).getName());
			}
		}
	}
}
