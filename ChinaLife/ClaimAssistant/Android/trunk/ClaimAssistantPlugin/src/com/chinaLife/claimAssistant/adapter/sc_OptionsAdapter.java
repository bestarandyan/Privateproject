package com.chinaLife.claimAssistant.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.chinaLife.claimAssistant.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//自定义适配器Adapter
public class sc_OptionsAdapter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
	private Activity activity = null;
	private Handler handler;

	/**
	 * 自定义构造方法
	 * 
	 * @param activity
	 * @param handler
	 * @param list
	 */
	public sc_OptionsAdapter(Activity activity, Handler handler,
			ArrayList<HashMap<String, String>> list) {
		this.activity = activity;
		this.handler = handler;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			// 下拉项布局
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.sc_item_optionsadapter, null);
			holder.textView = (TextView) convertView
					.findViewById(R.id.item_text);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.delImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == list.size()-1){
			convertView.setBackgroundResource(R.drawable.sc_phone_text2);
		}else{
			convertView.setBackgroundResource(R.drawable.sc_phone_text1);
		}

		holder.textView.setText(list.get(position).get("phonenumber").toString());

		// 为下拉框选项文字部分设置事件，最终效果是点击将其文字填充到文本框
		holder.textView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Message msg = new Message();
				Bundle data = new Bundle();
				// 设置选中索引
				data.putInt("selIndex", position);
				msg.setData(data);
				msg.what = 1;
				// 发出消息
				handler.sendMessage(msg);
			}
		});

		// 为下拉框选项删除图标部分设置事件，最终效果是点击将该选项删除
		holder.imageView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Message msg = new Message();
				Bundle data = new Bundle();
				// 设置删除索引
				data.putInt("delIndex", position);
				msg.setData(data);
				msg.what = 2;
				// 发出消息
				handler.sendMessage(msg);
			}
		});

		return convertView;
	}

}

class ViewHolder {
	TextView textView;
	ImageView imageView;
}
