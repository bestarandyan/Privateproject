package com.boluomi.children.adapter;

import java.io.File;
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

import com.boluomi.children.R;
import com.boluomi.children.data.MyApplication;

public class PageAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;
	LayoutInflater layoutInflater;

	public PageAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {

		return list.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			// imageView = new ImageView(mContext);
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.item_gradview, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView);
//			int width = MyApplication.getInstance(mContext).getWidthPixels();
//			int height = MyApplication.getInstance(mContext).getHeightPixels();
//			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//					width, height);
//			holder.imageView.setLayoutParams(param);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {
		ImageView imageView;

		private void setImageView(Bitmap bm) {
			imageView.setImageBitmap(bm);
		}
	}
}