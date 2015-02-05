package com.qingfengweb.baoqi.insuranceShow.ext;



import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.insuranceShow.AdvertiseActivity;
import com.qingfengweb.baoqi.insuranceShow.PlanActivity;
import com.qingfengweb.baoqi.insuranceShow.ProductInfoActivity;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {
	private Activity context; // 承接上文内容
	private List<HashMap<String,Object>> listItems; // listview中的数据项
	private LayoutInflater listContainer;
	private ViewHolder vh = null;
	
	
	
	/**
	 * 构造函数ConnectAdapter
	 */

	public ProductAdapter(Activity context, List<HashMap<String,Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context);
		this.listItems = listItems;

	}

	public int getCount() {

		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);

	}

	public long getItemId(int position) {

		// TODO Auto-generated method stub
		return position;

	}

	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = listContainer.inflate(R.layout.productcenteritem, null);
		
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.productcenteritem, null);
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
	
	
	public String getSDPath(){ 
		File sdDir = null; 
		boolean sdCardExist = Environment.getExternalStorageState() 
		.equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在 
		if (sdCardExist) 
		{ 
		sdDir = Environment.getExternalStorageDirectory();//获取跟目录 
		} 
		return sdDir.toString(); 

		}
	
	
	private class ViewHolder {
		private ImageView image;
		private TextView text1;
		private TextView text2;
		private TextView text3;
		private TextView text4;
		private TextView text5;
		private TextView text6;
		
		private Button btn1;
		private Button btn2;
		private Button btn3;
		private Button btn4;
		private Button btn5;
		public ViewHolder(View layout) {
			image = (ImageView)layout.findViewById(R.id.image);
			text1 = (TextView)layout.findViewById(R.id.text1);
			text2 = (TextView)layout.findViewById(R.id.text2);
			text3 = (TextView)layout.findViewById(R.id.text3);
			text4 = (TextView)layout.findViewById(R.id.text4);
			text5 = (TextView)layout.findViewById(R.id.text5);
			text6 = (TextView)layout.findViewById(R.id.text6);
			
			btn1 = (Button)layout.findViewById(R.id.btn1);
			btn2 = (Button)layout.findViewById(R.id.btn2);
			btn3 = (Button)layout.findViewById(R.id.btn3);
			btn4 = (Button)layout.findViewById(R.id.btn4);
			btn5 = (Button)layout.findViewById(R.id.btn5);
		}

		public void setContent(HashMap<String,Object> args,int position) {
			image.setBackgroundResource(Integer.parseInt(args.get("image").toString()));
			
			
			
			text1.setText(args.get("text1").toString());
			text2.setText(args.get("text2").toString());
			text3.setText(args.get("text3").toString());
			text4.setText(args.get("text4").toString());
			text5.setText(args.get("text5").toString());
			text6.setText(args.get("text6").toString());
			text1.setTag(position);
			text2.setTag(position);
			text3.setTag(position);
			text4.setTag(position);
			text5.setTag(position);
			text6.setTag(position);
			btn1.setTag(position);
			btn2.setTag(position);
			btn3.setTag(position);
			btn4.setTag(position);
			btn5.setTag(position);
			
			btn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println(getSDPath());
					Intent it = new Intent(Intent.ACTION_VIEW);  
			           Uri uri = Uri.parse(getSDPath()+"/video.mp4");  
			           it.setDataAndType(uri , "video/mp4");  
			          context.startActivity(it);
				}
			});
			btn2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("tag", 0);
					intent.setClass(context, AdvertiseActivity.class);
					context.startActivity(intent);
					context.finish();
					
				}
			});
			
			btn3.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(context,PlanActivity.class);
					context.startActivity(intent);
					context.finish();
				}
			});
			btn4.setOnClickListener(new View.OnClickListener() {
				
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
					bundle.putString("text4", 
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text4").toString());
					bundle.putString("text5", 
							listItems.get(Integer.parseInt(v.getTag().toString()))
							.get("text5").toString());
					intent.putExtra("infos", bundle);
					intent.setClass(context, ProductInfoActivity.class);
					context.startActivity(intent);
					context.finish();
				}
			});
			if(position == 1||position ==2){
				btn1.setVisibility(View.GONE);
				btn2.setVisibility(View.GONE);
			}
		}
	}
	
	

}
