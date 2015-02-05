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

/**
 * @author 刘星星
 * @createDate 2013/3/6
 * 写信中 联系人的显示适配器
 *
 */
public class LinkManGvAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String,String>> list;
	public LinkManGvAdapter(Context context,ArrayList<HashMap<String,String>> list) {
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
		convertView = LayoutInflater.from(context).inflate(R.layout.item_linkmangv, null);
		Button btn = (Button) convertView.findViewById(R.id.btn);
		btn.setText(list.get(position).get("name").toString());
		return convertView;
	}

}
