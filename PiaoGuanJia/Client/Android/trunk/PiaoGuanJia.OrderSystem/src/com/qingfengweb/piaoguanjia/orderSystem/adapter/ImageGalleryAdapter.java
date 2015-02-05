/**
 * 
 */
package com.qingfengweb.piaoguanjia.orderSystem.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.piaoguanjia.orderSystem.MyApplication;
import com.qingfengweb.piaoguanjia.orderSystem.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageGalleryAdapter extends BaseAdapter{
	Context context;
	ArrayList<String> list;
	public ImageGalleryAdapter(Context context,ArrayList<String> list) {
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
		convertView = LayoutInflater.from(context).inflate(R.layout.item_gallery, null);
		ImageView image = (ImageView) convertView.findViewById(R.id.galleryImg);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(MyApplication.widthPixels,MyApplication.widthPixels/3);
		image.setLayoutParams(param);
		return convertView;
	}

}
