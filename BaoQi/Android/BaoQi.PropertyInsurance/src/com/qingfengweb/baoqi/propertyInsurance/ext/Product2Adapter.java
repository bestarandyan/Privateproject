package com.qingfengweb.baoqi.propertyInsurance.ext;



import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.CardBuyActivity;
import com.qingfengweb.baoqi.propertyInsurance.FreeInsuranceActivity;
import com.qingfengweb.baoqi.propertyInsurance.ProductInfo2Activity;
import com.qingfengweb.baoqi.propertyInsurance.ProductInfoActivity;
import com.qingfengweb.baoqi.propertyInsurance.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Product2Adapter extends BaseAdapter {
	private Activity context; // 承接上文内容
	private List<HashMap<String,Object>> listItems; // listview中的数据项
	private ViewHolder vh = null;
	
	
	
	/**
	 * 构造函数ConnectAdapter
	 */

	public Product2Adapter(Activity context, List<HashMap<String,Object>> listItems) {
		this.context = context;
		this.listItems = listItems;

	}

	public int getCount() {

		return listItems.size();
	}

	public Object getItem(int position) {
		return listItems.get(position);

	}

	public long getItemId(int position) {

		return position;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.productcenter2item, null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(listItems.get(position),position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	
	
	private class ViewHolder {
		private ImageView image;
		private TextView text1;
		private TextView text2;
		private TextView text3;
		private TextView text4;
		private TextView text5;
		
		private Button btn1;
		private Button btn2;
		public ViewHolder(View layout) {
			image = (ImageView)layout.findViewById(R.id.image);
			text1 = (TextView)layout.findViewById(R.id.text1);
			text2 = (TextView)layout.findViewById(R.id.text2);
			text3 = (TextView)layout.findViewById(R.id.text3);
			text4 = (TextView)layout.findViewById(R.id.text4);
			text5 = (TextView)layout.findViewById(R.id.text5);
			
			btn1 = (Button)layout.findViewById(R.id.btn1);
			btn2 = (Button)layout.findViewById(R.id.btn2);
		}

		public void setContent(HashMap<String,Object> args,int position) {
			image.setBackgroundResource(Integer.parseInt(args.get("image").toString()));
			text1.setText(args.get("text1").toString());
			text2.setText(args.get("text2").toString());
			text3.setText(args.get("text3").toString());
			text4.setText(args.get("text4").toString());
			text5.setText(args.get("text5").toString());
			text1.setTag(position);
			text2.setTag(position);
			text3.setTag(position);
			text4.setTag(position);
			text5.setTag(position);
			btn1.setTag(position);
			btn2.setTag(position);
			
			btn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent =new Intent();
					intent.putExtra("tag", 0);
					intent.setClass(context, CardBuyActivity.class);
					context.startActivity(intent);
					context.finish();
				}
			});
			btn2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("text1", 
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text1").toString());
					bundle.putString("text2", 
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text2").toString());
					bundle.putString("text3", 
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text3").toString());
					bundle.putString("text4", 
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text4").toString());
					bundle.putString("text5", 
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text5").toString());
					bundle.putString("text6", "《"+
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text1").toString()+"适用条款》");
					intent.putExtra("infos", bundle);
					intent.setClass(context, ProductInfo2Activity.class);
					context.startActivity(intent);
					context.finish();
				}
			});
		}
	}
	
	

}
