/**
 * 
 */
package com.boluomi.children.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boluomi.children.R;
import com.boluomi.children.activity.GrowUpPhotoPreviewActivity;
import com.boluomi.children.activity.GrowupMsgActivity;
import com.boluomi.children.data.MyApplication;

/**
 * @author 刘星星
 * @createDate 2013/11/11
 *其他人的成长备忘录
 */
public class OtherGrowUpListAdapter extends BaseAdapter implements OnItemClickListener,OnClickListener{
	private List<Map<String,Object>> list = null;
	private Context context;
	public OtherGrowUpListAdapter(Context context,List<Map<String,Object>> list) {
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
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_other_growup, null);
			holder.photoIV = (ImageView) convertView.findViewById(R.id.photo);
			holder.photoNameTv = (TextView) convertView.findViewById(R.id.title);
			holder.photoIntroTv = (TextView) convertView.findViewById(R.id.content);
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeText);
			holder.sendMsgBtn = (ImageButton) convertView.findViewById(R.id.sendMsgBtn);
			holder.photoGv = (GridView) convertView.findViewById(R.id.photoGv);
			holder.msgLayout = (LinearLayout) convertView.findViewById(R.id.msgLayout);
			holder.msgAddLayout = (LinearLayout) convertView.findViewById(R.id.msgAddLayout);
			holder.zanBtn = (ImageButton) convertView.findViewById(R.id.zanBtn);
			holder.zanTv = (TextView) convertView.findViewById(R.id.zanTv);
			holder.pinglunTv = (TextView) convertView.findViewById(R.id.pinglunTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object>  map = list.get(position);
//		Bitmap bitmap = (Bitmap) map.get("bitmap");
//		if(bitmap!=null){
//			holder.photoIV.setImageBitmap(bitmap);
//		}else{
//			holder.photoIV.setImageResource(R.drawable.babe_chengzhang_photo);
//		}
		
		LinearLayout.LayoutParams headParam = new LinearLayout.LayoutParams(MyApplication.getInstant().getWidthPixels()/5, MyApplication.getInstant().getWidthPixels()/5);
		holder.photoIV.setLayoutParams(headParam);
		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo5);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo5);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo5);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo1);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo2);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo3);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo5);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo5);
		list1.add(map1);
		map1 = new HashMap<String, Object>();
		map1.put("imgid", R.drawable.photo1);
		list1.add(map1);
	
		holder.photoGv.setAdapter(new OtherGrowUpPhotoGvAdapter(context, list1));
		int hLine = (list1.size()%3==0?list1.size()/3:list1.size()/3+1);
		int height = MyApplication.getInstant().getWidthPixels()/4*hLine+(hLine-1)*2;
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
		holder.photoGv.setLayoutParams(param);
		if(holder.msgAddLayout.getChildCount()<3){
			map1 = new HashMap<String, Object>();
			map1.put("userName", "幸运星");
			map1.put("content", "你们家宝宝萌翻了都...");
			holder.msgAddLayout.addView(addMsgLayout(context,map1));
			map1 = new HashMap<String, Object>();
			map1.put("userName", "东风之神");
			map1.put("content", "好想跟你做亲家啊...");
			holder.msgAddLayout.addView(addMsgLayout(context,map1));
			map1 = new HashMap<String, Object>();
			map1.put("userName", "孤傲的天狼");
			map1.put("content", "这宝宝真可爱");
			holder.msgAddLayout.addView(addMsgLayout(context,map1));
		}
		holder.photoGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		holder.photoGv.setOnItemClickListener(this);
		holder.zanBtn.setTag(position);
		holder.sendMsgBtn.setTag(position);
		holder.zanTv.setTag(position);
		holder.pinglunTv.setTag(position);
		holder.sendMsgBtn.setOnClickListener(this);
		holder.zanBtn.setOnClickListener(this);
		holder.zanTv.setOnClickListener(this);
		holder.pinglunTv.setOnClickListener(this);
		holder.zanTv.setText("赞("+map.get("zan").toString()+")");
		return convertView;
	}
	private View addMsgLayout(Context context,Map<String,Object> map){
		View view = LayoutInflater.from(context).inflate(R.layout.view_msglayout, null);
		((TextView)view.findViewById(R.id.userName)).setText(map.get("userName").toString());
		((TextView)view.findViewById(R.id.content)).setText(map.get("content").toString());
		return view;
	}
	class ViewHolder{
		ImageView photoIV;
		TextView photoNameTv;
		TextView photoIntroTv;
		TextView timeTv;
		ImageButton zanBtn,sendMsgBtn;
		TextView zanTv,pinglunTv;
		GridView photoGv;
		LinearLayout msgLayout,msgAddLayout;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		Intent intent = new Intent(context,GrowUpPhotoPreviewActivity.class);
//		context.startActivity(intent);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.zanBtn || v.getId() == R.id.zanTv){//点赞 
			Map<String,Object> map = this.list.get((Integer) v.getTag());
			map.put("zan", (Integer)map.get("zan")+1);
			this.notifyDataSetChanged();
		}else if(v.getId() == R.id.sendMsgBtn || v.getId() == R.id.pinglunTv){//发表评论
			Intent intent = new Intent(context,GrowupMsgActivity.class);
			context.startActivity(intent);
		}
		
	}
}
