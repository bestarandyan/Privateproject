package com.qingfengweb.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.MyApplication;

public class TreasureChesViewAdapter extends BaseAdapter{
	private Context context;
	private List<Map<String, Object>> list;
	public TreasureChesViewAdapter(Context context, List<Map<String, Object>> list) {
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
		ViewHolder holder = null;
		LayoutInflater layout = null;
		if(convertView == null){
			holder = new ViewHolder();
			layout = LayoutInflater.from(context);
			convertView = layout.inflate(R.layout.item_treasurechest, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.dotBtn = (Button) convertView.findViewById(R.id.layoutDot);
//			int width = MyApplication.getInstance(context).getWidthPixels()/3-10;
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
		TextView title;
		Button dotBtn;
		Map<String,Object> map = null;
		public void setViewContent(int position){
			//id=1, icon=1, storeid=10, _id=1, name=��װ, number=0, deleted=0, ranking=1
			 map = list.get(position);
			 title.setText(map.get("name").toString());
			 image.setImageResource(Integer.parseInt(map.get("imgDrawable").toString()));
			 int number = Integer.parseInt(map.get("number").toString());
			 if(number>0){
				 dotBtn.setVisibility(View.VISIBLE);
				 dotBtn.setText(number+"");
			 }else{
				 dotBtn.setVisibility(View.GONE);
			 }
		}
	}
}
