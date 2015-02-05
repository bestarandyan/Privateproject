/**
 * 
 */
package com.qingfengweb.weddingideas.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.data.MyApplication;

/**
 * @author 刘星星
 *
 */
public class PhotoListAdapter  extends  BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	Map<String ,Object> map = null;
	Bitmap bitmap = null;
	int w = MyApplication.getInstance().getScreenW()-20;
	public PhotoListAdapter(Context context,List<Map<String,Object>> list) {
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
	class ViewHolder{
		ImageView img;
	}
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_photolist, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.imageView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		map = list.get(position);
		bitmap = (Bitmap) map.get("bitmap");
		holder.img.setImageBitmap(bitmap);
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, bitmap.getHeight());
//		holder.img.setLayoutParams(params);
//		Drawable drawable = new BitmapDrawable(bitmap);
//		holder.img.setBackgroundDrawable(drawable);
		return convertView;
	}
}
