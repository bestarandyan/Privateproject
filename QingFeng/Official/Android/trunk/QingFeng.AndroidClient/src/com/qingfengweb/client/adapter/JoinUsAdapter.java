/**
 * 
 */
package com.qingfengweb.client.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingfengweb.android.R;

/**
 * @author ¡ı–«–«
 * @createDate 2013/6/18
 *
 */
public class JoinUsAdapter extends BaseAdapter{
	Context context;
	List<Map<String, Object>> list = null;
	public JoinUsAdapter(Context context,List<Map<String, Object>> list) {
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
	class ViewHolder{
		TextView name;
		TextView date;
		TextView address;
		TextView content;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_joinus, null);
			holder.name = (TextView) convertView.findViewById(R.id.workName);
			holder.date = (TextView) convertView.findViewById(R.id.sendDate);
			holder.address = (TextView) convertView.findViewById(R.id.workAddress);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, Object> map = list.get(position);
		holder.name.setText(map.get("name").toString());
		holder.date.setText(map.get("endtime").toString());
//		holder.address.setText(map.get("workAddress"));
		holder.content.setText(map.get("summary").toString());
		if(position%2 == 0){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.joinus_item_bg));
		}
		return convertView;
	}

}
