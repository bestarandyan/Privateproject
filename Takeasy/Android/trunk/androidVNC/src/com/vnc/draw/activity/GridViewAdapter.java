package com.vnc.draw.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.androidVNC.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<HashMap<String,Object>> list;
	public GridViewAdapter(Context context,ArrayList<HashMap<String,Object>> list)  {

		this.mContext = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}
	public Object getItem(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// 定义一个ImageView,显示在GridView里
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
		} else {
			imageView = (ImageView) convertView;
		}
		//imageView.setAdjustViewBounds(true);
		imageView.setMinimumWidth(MyApplication.getInstance().getScreenWidth()/5);
        imageView.setMinimumHeight(MyApplication.getInstance().getScreenHeight()/5);
		Bitmap bitmap = (Bitmap) list.get(position).get("bm");
         if(bitmap!=null){
        	 imageView.setImageBitmap((bitmap));
         }else{
        	 imageView.setImageResource(R.drawable.tool1);
         }
        
         
         imageView.setBackgroundResource(R.drawable.image_border);
		return imageView;
	}
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}