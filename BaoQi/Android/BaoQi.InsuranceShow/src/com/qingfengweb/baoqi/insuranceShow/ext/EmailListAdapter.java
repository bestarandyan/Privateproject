package com.qingfengweb.baoqi.insuranceShow.ext;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.insuranceShow.ProductInfoActivity;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EmailListAdapter extends BaseAdapter {
	private Context context; // 承接上文内容
	private List<HashMap<String,Object>> listItems; // listview中的数据项
	private LayoutInflater listContainer;
	private ViewHolder vh = null;
	
	
	/**
	 * 构造函数ConnectAdapter
	 */

	public EmailListAdapter(Context context, List<HashMap<String,Object>> listItems) {
		this.context = context;
		listContainer = LayoutInflater.from(context);
		this.listItems = listItems;

	}

	public int getCount() {

		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);

	}

	public long getItemId(int position) {

		// TODO Auto-generated method stub
		return position;

	}
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.email_listitem, null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(listItems.get(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
//	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = listContainer.inflate(R.layout.email_listitem, null);
//		ImageView image = (ImageView)convertView.findViewById(R.id.image);
//		TextView text = (TextView)convertView.findViewById(R.id.text);
//		text.setText(listItems.get(position).get("text").toString());
//		image.setImageBitmap((Bitmap)listItems.get(position).get("email"));
//		
//		
//		
//		return convertView;
//	}
	private class ViewHolder {
		private ImageView image ;
		private TextView time;
//		private TextView ower ;
		public ViewHolder(View layout) {
			image=(ImageView) layout.findViewById(R.id.image);
			time=(TextView) layout.findViewById(R.id.text);
//			ower = (TextView)layout.findViewById(R.id.ower);
		}

		public void setContent(HashMap<String, Object> map) {
			image.setImageBitmap((Bitmap)map.get("email"));
			time.setText(map.get("text").toString());
//			ower.setText(map.get("ower").toString());
		}
	}
}
