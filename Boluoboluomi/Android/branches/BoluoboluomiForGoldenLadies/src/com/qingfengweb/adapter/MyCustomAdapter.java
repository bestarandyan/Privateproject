package com.qingfengweb.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;

public class MyCustomAdapter extends BaseAdapter{
	Context context;
	List<Map<String, Object>> list;
//	private ImageDownloader imageDownloader = null;
//	ArrayList<ImageView> imageList = new ArrayList<ImageView>();
	public MyCustomAdapter(Context context,List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
//		imageDownloader = new ImageDownloader(context,20);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				LayoutInflater layout = LayoutInflater.from(context);
				holder = new ViewHolder();
				convertView = layout.inflate(R.layout.item_mycustom, null);
				holder.text = (TextView) convertView.findViewById(R.id.text);
				holder.image1 = (ImageView) convertView.findViewById(R.id.imageView1);
				holder.image2 = (ImageView) convertView.findViewById(R.id.imageView2);
				holder.image3 = (ImageView) convertView.findViewById(R.id.imageView3);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
		return convertView;
	}
	class ViewHolder{
		TextView text;
		ImageView image1,image2,image3;
	}
}
