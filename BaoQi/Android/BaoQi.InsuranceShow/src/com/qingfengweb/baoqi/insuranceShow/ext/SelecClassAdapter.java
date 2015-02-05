package com.qingfengweb.baoqi.insuranceShow.ext;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;




public class SelecClassAdapter extends BaseAdapter{

	private Context mContext;

	public SelecClassAdapter(Context context) {

		this.mContext=context;

	}

	@Override
	public int getCount() {
		return mThumbIds.length;
		}

	@Override
	public Object getItem(int position) {

		return position;

	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//定义一个ImageView,显示在GridView里

		TextView text;
		if(convertView==null){

			text=new TextView(mContext);

			text.setLayoutParams(new GridView.LayoutParams(120, 50));
			text.setTextColor(Color.BLUE);
			text.setTextSize(16);
			text.setPadding(8, 8, 8, 8);

		}else{

			text = (TextView) convertView;
			}

		text.setText(mThumbIds[position]);

		return text;
	}


	//展示信息
	private String[] mThumbIds = {
			"分红险","万能险","投连险","少儿险","健康险"
			,"养老险","保障险","两全险","意外险","附加险","连生险"
		};

@Override
public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
}
}