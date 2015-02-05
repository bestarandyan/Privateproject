/**
 * 
 */
package com.qingfengweb.weddingideas.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengweb.weddingideas.R;

/**
 * @author qingfeng
 *
 */
public class MarryLogAdapter extends BaseAdapter{
	public Context context;
	public List<Map<String,Object>> list;
	String state = "";
	String time = "";
	String content = "";
	public MarryLogAdapter(Context context,List<Map<String,Object>> list) {
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
		TextView timeTv,contentTv,stateTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_marrylog, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.stateImg);
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
			holder.stateTv = (TextView) convertView.findViewById(R.id.stateTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map = list.get(position);
		if(map.get("isOk")!=null){
			state = map.get("isOk").toString().trim();
		}
		if(map.get("stimeString")!=null){
			time = map.get("stimeString").toString().trim();
		}
		if(map.get("logContent")!=null){
			content = map.get("logContent").toString().trim();
		}
		if(state!=null && state.equals("0")){//为完成
			holder.img.setImageResource(R.drawable.img_plan_unfinished);
			holder.stateTv.setText("未完成");
			holder.stateTv.setTextColor(Color.parseColor("#333333"));
			holder.timeTv.setTextColor(Color.parseColor("#333333"));
			holder.contentTv.setTextColor(Color.parseColor("#333333"));
		}else if(state!=null && state.equals("1")){//已完成
			holder.stateTv.setTextColor(Color.parseColor("#ffffff"));
			holder.timeTv.setTextColor(Color.parseColor("#ffffff"));
			holder.contentTv.setTextColor(Color.parseColor("#ffffff"));
			holder.img.setImageResource(R.drawable.img_plan_finished);
			holder.stateTv.setText("已完成");
		}
		holder.contentTv.setText(content);
		holder.timeTv.setText(time);
		return convertView;
	}
}
