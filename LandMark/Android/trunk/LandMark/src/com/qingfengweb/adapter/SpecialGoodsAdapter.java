/**
 * 
 */
package com.qingfengweb.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.activity.R;
import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.network.AsyncImageLoad;
import com.qingfengweb.network.CallbackImpl;
import com.qingfengweb.utils.FileUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author ¡ı–«–«
 * @createDate 2013/5/30
 *Ãÿª›…Ã∆∑  ≈‰∆˜
 */
public class SpecialGoodsAdapter extends BaseAdapter{
	ArrayList<HashMap<String, String>> list = null;
	AsyncImageLoad asyncImageLoad = new AsyncImageLoad();
	Context context;
	SQLiteDatabase database;
	public SpecialGoodsAdapter(Context context,ArrayList<HashMap<String, String>> list,SQLiteDatabase database) {
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
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping
					, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.shopImg);
			holder.textView = (TextView) convertView.findViewById(R.id.shopMoney);
			convertView.setTag(holder);
		}else{
			holder  = (ViewHolder) convertView.getTag();
		}
		if(list.size()>0 && position < list.size()){
		HashMap<String, String> map = null;
		if(list.size()>0){
			map = list.get(position);
		}else{
			return null;
		}
		holder.textView.setText("”≈ª›º€:£§"+map.get("price"));
		String[] fileStrings = map.get("special_pic").toString().trim().split("/");
		String fileName = fileStrings[fileStrings.length-1];
		File file = new File(FileUtils.SDPATH+ConstantClass.IMG_URL_STRING+fileName);
		if(file.exists()){
			Bitmap btimapBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			holder.imageView.setImageBitmap(btimapBitmap);
		}else{
			ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
	        	
	        }else {
	        	CallbackImpl callbackImpl = new CallbackImpl(holder.imageView);
				asyncImageLoad.loadDrawable(context,R.drawable.default_png,map.get("special_pic").toString().trim(), callbackImpl);
	        }
			
		}
		}
		return convertView;
	}
	class ViewHolder{
		ImageView imageView;
		TextView textView;
	}

}
