package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * @createDate 2013/1/11
 * @author 刘星星
 * 使用保证列表的自定义适配器
 *
 */
public class EmployHelpAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<HashMap<String,Object>> list;
	public EmployHelpAdapter(Context context,ArrayList<HashMap<String,Object>> arrayList) {
		this.context = context;
		this.list = arrayList;
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
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.item_employlist, null);
		TextView textView  = (TextView) convertView.findViewById(R.id.textView);
		textView.setText(list.get(position).get("name").toString());
		return convertView;
	}

}
