/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/4/18
 * 输入查询的结果的适配器
 *
 */
public class InputSelectAdapter extends BaseAdapter{
	Context context;
	ArrayList<String> list;
	public InputSelectAdapter(Context context,ArrayList<String> list) {
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
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_inputselect, null);
			holder.tv = (TextView) convertView.findViewById(R.id.searchItemText);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(list.get(position).toString());
		return convertView;
	}
	class ViewHolder{
		TextView tv;
	}
}
