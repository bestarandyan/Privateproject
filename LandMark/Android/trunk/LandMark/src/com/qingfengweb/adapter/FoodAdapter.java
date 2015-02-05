package com.qingfengweb.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.activity.R;
import com.qingfengweb.activity.SpecialGoodsActivity;
import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.network.AsyncImageLoad;
import com.qingfengweb.network.CallbackImpl;
import com.qingfengweb.utils.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String, String>> list;
	AsyncImageLoad asyncImageLoad = new AsyncImageLoad();
	SQLiteDatabase database = null;
	public FoodAdapter(Context context,ArrayList<HashMap<String, String>> list,SQLiteDatabase database) {
		this.context = context;
		this.list = list;
		this.database = database;
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_food, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.foodImg);
			holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
			holder.conditionTv = (TextView) convertView.findViewById(R.id.conditionTv);
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			holder.telTv = (TextView) convertView.findViewById(R.id.telTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(list.size()>0 && position<list.size()){
		final HashMap<String, String> map = list.get(position);
		if(map!=null){
			holder.nameTv.setText(map.get("brand_name"));
			holder.conditionTv.setText(map.get("condition"));
			holder.timeTv.setText(map.get("delivery_time1"));
			holder.telTv.setText(map.get("food_tel"));
			holder.telTv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+map.get("tel")));
					context.startActivity(intent);
				}
			});
			String[] fileStrings = map.get("food_pic").toString().trim().split("/");
			String fileName = fileStrings[fileStrings.length-1];
			File file = new File(FileUtils.SDPATH+ConstantClass.IMG_URL_STRING+fileName);
			if(file.exists()){
				Bitmap btimapBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				holder.img.setImageBitmap(btimapBitmap);
			}else{
				ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
		        	
		        }else {
		        	CallbackImpl callbackImpl = new CallbackImpl(holder.img);
					asyncImageLoad.loadDrawable(context,R.drawable.default_png,map.get("food_pic").toString().trim(), callbackImpl);
		        }
				
			}
		}
		convertView.setOnClickListener(null);
		}
		return convertView;
	}
	class ViewHolder{
		ImageView img;
		TextView nameTv;
		TextView conditionTv;
		TextView timeTv;
		TextView telTv;
	}
	

}
