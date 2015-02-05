/**
 * 
 */
package com.qingfengweb.adapter;

import java.util.List;
import java.util.Map;

import com.qingfengweb.id.blm_goldenLadies.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013、9、16
 *
 */
public class IntegraMainListAdapter  extends BaseAdapter{
	public List<Map<String,Object>> list;
	Context context;
	public IntegraMainListAdapter(Context context,List<Map<String,Object>> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_integra, null);
			holder = new ViewHolder();
			holder.titleTv = (TextView) convertView.findViewById(R.id.title);
			holder.img1 = (ImageView) convertView.findViewById(R.id.img1);
			holder.img2 = (ImageView) convertView.findViewById(R.id.img2);
			holder.img3 = (ImageView) convertView.findViewById(R.id.img3);
			holder.img4 = (ImageView) convertView.findViewById(R.id.img4);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleTv.setText(list.get(position).get("type").toString());
		holder.img1.setImageResource(Integer.parseInt(list.get(position).get("img1").toString()));
		holder.img2.setImageResource(Integer.parseInt(list.get(position).get("img2").toString()));
		holder.img3.setImageResource(Integer.parseInt(list.get(position).get("img3").toString()));
		holder.img4.setImageResource(Integer.parseInt(list.get(position).get("img4").toString()));
		return convertView;
	}
	class ViewHolder{
		TextView titleTv;
		ImageView img1,img2,img3,img4;
		
	}
}
