package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhihuigu.sosoOffice.DetailLetterActivity;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.constant.MyApplication;

public class StationInLetterAdapter   extends BaseAdapter{
	private Context context;
	private ArrayList<Map<String,String>> list;
	public StationInLetterAdapter(Context c,ArrayList<Map<String,String>> list
		) {
		// TODO Auto-generated constructor stub
		this.context = c;
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
		return position;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_stationinletter, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.btn = (Button) convertView.findViewById(R.id.btn1);
			holder.linear  = (LinearLayout) convertView.findViewById(R.id.parent_linear);
		    convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position%2==0){
			convertView.setBackgroundColor(Color.rgb(242,242,242));
		}else{
			convertView.setBackgroundColor(Color.rgb(248,248,248));
		}
		holder.setContent(position);
		return convertView;
	}
	class ViewHolder{
		LinearLayout linear;
		Button btn;
		TextView tv1;
		TextView tv2;
		TextView tv3;
		public void setContent(final int position){
//			l.setId(position);  
//			linear.setTag(position);
//			btn.setTag(position);
			String title = list.get(position).get("title");
			String name = list.get(position).get("name");
			if(title.length()>14){
				title = title.substring(0, 14)+"...";
			}
			if(name.length()>10){
				name = name.substring(0, 10)+"...";
			}
			tv1.setText(title);
			tv2.setText(name);
			tv3.setText(list.get(position).get("time"));
			if(Integer.parseInt(list.get(position).get("status")) == 0){//在此处改变信件前面的图片状态
				btn.setBackgroundResource(R.drawable.soso_email_ico);
			}else{
				btn.setBackgroundResource(R.drawable.soso_email_ico_open);
			}
			linear.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
						Intent intent = new Intent(context,DetailLetterActivity.class);
//						MyApplication.getInstance().setLetterlist(list);
						intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
						intent.putExtra("index", position);
						context.startActivity(intent);
				}
			
			});
		}
	}

}
