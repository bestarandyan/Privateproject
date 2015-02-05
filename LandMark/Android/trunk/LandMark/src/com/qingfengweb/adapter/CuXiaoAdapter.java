/**
 * 
 */
package com.qingfengweb.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.activity.R;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/5/29
 *
 */
public class CuXiaoAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String, String>> list;
	public CuXiaoAdapter(Context context,ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_cuxiao, null);
			holder.introTv = (TextView) convertView.findViewById(R.id.text);
			holder.floorTv = (TextView) convertView.findViewById(R.id.floor);
			holder.brandTv = (TextView) convertView.findViewById(R.id.brand);
			holder.data = (TextView) convertView.findViewById(R.id.data);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		try{
		if(position < list.size()){
			HashMap<String, String> map = list.get(position);
			if(map!=null){
				holder.floorTv.setText(map.get("floor_name"));
				holder.brandTv.setText(map.get("brand_name"));
				holder.introTv.setText(map.get("title").toString());
				String startDate = map.get("start_date").toString().substring(5,7)+"/"
							+map.get("start_date").toString().substring(8,10);
				String endDate = map.get("end_date").toString().substring(5,7)+"/"
						+map.get("end_date").toString().substring(8,10);
				holder.data.setText(startDate+"-"+endDate);
			}
			if(position>0 && position < list.size() && map.get("floor_name").equals(list.get(position-1).get("floor_name"))){
				holder.floorTv.setVisibility(View.GONE);
			}else{
				holder.floorTv.setVisibility(View.VISIBLE);
			}
			}
		}catch(NullPointerException e){
			System.out.println("报空指针异常了、、、、、");
		}
		
		return convertView;
	}
	class ViewHolder{
		TextView floorTv,brandTv,introTv,data;
	}
}
