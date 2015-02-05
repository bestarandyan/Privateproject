/**
 * 
 */
package com.boluomi.children.adapter;

import java.util.List;
import java.util.Map;

import com.boluomi.children.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author 刘星星
 * 成长备忘录的点评适配器
 *
 */
public class GrowUpMsgAdapter extends BaseAdapter{
	List<Map<String,Object>> list;
	Context context;
	public GrowUpMsgAdapter(Context context,List<Map<String,Object>> list) {
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
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_growupmsg, null);
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.userNameTV = (TextView) convertView.findViewById(R.id.username);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2==0){
			convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
		}else{
			convertView.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}
	class ViewHolder{
		ImageView imageView;
		TextView userNameTV,content;
	}
}
