/**
 * 
 */
package com.qingfengweb.lottery.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.util.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author 刘星星
 * @createDate 2013、11、22
 *
 */
@SuppressLint("HandlerLeak")
public class TypeCaiAdapter  extends BaseAdapter{
	private List<Map<String, Object>> list;
	private Context context;
	public TypeCaiAdapter(Context context,List<Map<String, Object>> list) {
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
		ImageView img,img1,img2,img3;
		TextView name;
		TextView intro,time;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_typecai, null);
			holder.img = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.intro = (TextView) convertView.findViewById(R.id.intro);
			holder.img1 = (ImageView) convertView.findViewById(R.id.image1);
			holder.img2 = (ImageView) convertView.findViewById(R.id.image2);
			holder.img3 = (ImageView) convertView.findViewById(R.id.image3);
			holder.time = (TextView) convertView.findViewById(R.id.timeText);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map< String, Object> map = list.get(position);
		holder.name.setText((CharSequence) map.get("name"));
		holder.intro.setText((CharSequence) map.get("intro"));
		holder.time.setText((CharSequence)map.get("time"));
		holder.img.setImageResource((Integer) map.get("imgId"));
		if((Integer)map.get("imgId1") == 0){
			holder.img1.setVisibility(View.GONE);
		}else{
			holder.img1.setVisibility(View.VISIBLE);
			holder.img1.setImageResource((Integer) map.get("imgId1"));
		}
		if((Integer)map.get("imgId2") == 0){
			holder.img2.setVisibility(View.GONE);
		}else{
			holder.img2.setVisibility(View.VISIBLE);
			holder.img2.setImageResource((Integer) map.get("imgId2"));
		}
		if((Integer)map.get("imgId3") == 0){
			holder.img3.setVisibility(View.GONE);
		}else{
			holder.img3.setVisibility(View.VISIBLE);
			holder.img3.setImageResource((Integer) map.get("imgId3"));
		}
		
		return convertView;
	}
}
