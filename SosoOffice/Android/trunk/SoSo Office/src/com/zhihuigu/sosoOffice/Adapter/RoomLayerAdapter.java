/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/1/24
 *  显示楼层信息的适配器
 *
 */
public class RoomLayerAdapter extends BaseAdapter{
	Context context;
	ArrayList<String> list;
	private int number = 1;//用来作为设置背景色的标志
	public RoomLayerAdapter(Context context,ArrayList<String> list) {
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
		ViewHolder h = null;
		if(convertView == null){
			h = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_layer, null);
			h.layerText = (TextView) convertView.findViewById(R.id.layerText);
			convertView.setTag(h);
		}else{
			h = (ViewHolder) convertView.getTag();
		}
		h.setViewContent(position);
		return convertView;
	}
	 class ViewHolder{
		 TextView layerText;
		 public void setViewContent(int position){
			 String layer = list.get(position);
			 layerText.setText("第"+layer+"层");
			 if(((position)/4)%2==0){
				 layerText.setBackgroundColor(Color.rgb(179,194,113));
				 layerText.setTextColor(Color.WHITE);
			 }else if(((position)/4)%2==1){
				 layerText.setBackgroundColor(Color.rgb(255,255,255));
				 layerText.setTextColor(Color.BLACK);
			 }
		 }
	 }
}
