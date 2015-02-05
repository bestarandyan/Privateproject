/**
 * 
 */
package com.qingfengweb.pinhuo.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingfengweb.pinhuo.R;
import com.qingfengweb.pinhuo.datamanage.MyApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2012/1/7
 * 主界面功能控件的适配器
 *
 */
public class MainGridViewAdapter extends BaseAdapter {

	private Activity mContext;
	private List<Map<String, Object>> bitmaplist;
	LayoutInflater layoutInflater;
	int width = 0;
	public MainGridViewAdapter(Activity context,List<Map<String, Object>> bitmaplist) {
		this.mContext = context;
		this.bitmaplist = bitmaplist;
		layoutInflater = LayoutInflater.from(context);
		width = MyApplication.getInstance().getScreenW()/3-2;
	}
	public int getCount() {
		return bitmaplist.size();
	}
	public Object getItem(int position) {

		return bitmaplist.get(position);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// 定义一个ImageView,显示在GridView里
		ViewHolder holder = null;
		if (convertView == null) {
//			imageView = new ImageView(mContext);
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_main_gv, null);
			holder.image = (ImageView) convertView.findViewById(R.id.mainImage);
			holder.tv = (TextView) convertView.findViewById(R.id.mainText);
			holder.btn = (TextView) convertView.findViewById(R.id.mainCircle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = bitmaplist.get(position);
		String name = map.get("name").toString();
		if(!name.equals("nodata")){
			Bitmap bitmap = (Bitmap) map.get("img");
			holder.tv.setText(name);
			holder.image.setImageBitmap(bitmap);
		}else{
			holder.tv.setText("");
			holder.image.setImageDrawable(new ColorDrawable(Color.WHITE));
		}
		AbsListView.LayoutParams param= new AbsListView.LayoutParams(width, width);
		convertView.setLayoutParams(param);
		return convertView;
	}
	public long getItemId(int position) {
		return position;
	}
	class ViewHolder{
		ImageView image;
		TextView tv;
		TextView btn;
	}
}
