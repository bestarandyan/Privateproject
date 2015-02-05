package com.chinaLife.claimAssistant.adapter;
import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.thread.sc_PicHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class sc_GridViewAdapter extends BaseAdapter {

	private Context mContext;
	private Integer[] mThumbIds;
	private Bitmap image = null;
	private Bitmap circle = null;
	public sc_GridViewAdapter(Context context,Integer[] mThumbIds) {

		this.mContext = context;
		this.mThumbIds = mThumbIds;
		if(image!=null){
			image.recycle();
			image = null;
			System.gc();
		}
		if(circle!=null){
			circle.recycle();
			circle = null;
			System.gc();
		}
		image = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sc_home_btn5);
		circle = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sc_home_dot);
	}

	public int getCount() {
		return mThumbIds.length;
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
		if(position == 4){
			int number = Sc_MyApplication.getInstance().getMessageNumber();
//			number = 15;
			if(number == 0){
				imageView.setImageResource(mThumbIds[4]);
			}else{
				imageView.setImageBitmap(sc_PicHandler.doodlexx(image, circle, number, 20,0,0));
				System.gc();
			}
		}else{
			imageView.setImageResource(mThumbIds[position]);
		}
		
		return imageView;
	}
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
}