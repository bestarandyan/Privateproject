package com.boluomi.children.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.data.MyApplication;

public class InvitationTemplateAdapter extends BaseAdapter{
	public Context context;
	public ArrayList<HashMap<String,Object>> list;
	public InvitationTemplateAdapter(Context context,ArrayList<HashMap<String,Object>> list) {
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
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_invitationtemplate, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.textView = (TextView) convertView.findViewById(R.id.title);
//			int width = MyApplication.getInstance(context).getWidthPixels()/4-10;
//			int height = width;
//			LinearLayout.LayoutParams param= new LinearLayout.LayoutParams(width, height);
//			holder.image.setLayoutParams(param);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setViewContent(position);
		return convertView;
	}
	class ViewHolder{
		ImageView image;
		TextView textView;
		public void setViewContent(int position){
			image.setImageBitmap((Bitmap) list.get(position).get("image"));
			textView.setText(list.get(position).get("title").toString());
		}
	}
}
