package com.boluomi.children.adapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.boluomi.children.R;
import com.boluomi.children.activity.GrowUpPhotoPreviewActivity;
import com.boluomi.children.activity.GrowupMsgActivity;
import com.tencent.mm.sdk.platformtools.BackwardSupportUtil.BitmapFactory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GrowUpMainListAdapter extends BaseAdapter{
	private List<Map<String,Object>> list = null;
	private Context context;
	public GrowUpMainListAdapter(Context context,List<Map<String,Object>> list) {
		this.context = context;
		this.list = list;
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
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_growuplist, null);
			holder.photo = (ImageView) convertView.findViewById(R.id.photo);
			holder.photoName = (TextView) convertView.findViewById(R.id.photoName);
			holder.photoIntro = (TextView) convertView.findViewById(R.id.photoIntro);
			holder.photoMsgNumber = (TextView) convertView.findViewById(R.id.msgNumber);
			holder.msgLayout = (LinearLayout) convertView.findViewById(R.id.msgLayout);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object>  map = list.get(position);
		Bitmap bitmap = (Bitmap) map.get("bitmap");
		if(bitmap!=null){
			holder.photo.setImageBitmap(bitmap);
		}else{
			holder.photo.setImageResource(R.drawable.babe_chengzhang_photo);
		}
		String titleStr = map.get("nicheng").toString()+map.get("age").toString()+"岁";
		String introStr = map.get("remark").toString();
		holder.photoName.setText(titleStr);
		holder.photoIntro.setText(introStr);
		holder.msgLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,GrowupMsgActivity.class);
				context.startActivity(intent);
			}
		});
		/*holder.photo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,GrowUpPhotoListActivity.class);
				intent.putExtra("photoList", (Serializable)list);
				context.startActivity(intent);
				
			}
		});*/
		return convertView;
	}
	class ViewHolder{
		ImageView photo;
		TextView photoName;
		TextView photoIntro;
		TextView photoMsgNumber;
		LinearLayout msgLayout;
	}
}
