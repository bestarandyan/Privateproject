/**
 * 
 */
package com.qingfengweb.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.qingfengweb.activity.R;
import com.qingfengweb.adapter.SpecialGoodsAdapter.ViewHolder;
import com.qingfengweb.constant.ConstantClass;
import com.qingfengweb.database.MyAppLication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


/**
 * @author 刘星星
 * @createDate 2013/5/30
 * 购物指南适配器
 *
 */
public class ShoppingGuideAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String, Object>> list;
	int flag = 0;//0代表品牌罗列 1代表楼层导航
	public ShoppingGuideAdapter(Context context,ArrayList<HashMap<String, Object>> list,int type) {
		this.context = context;
		this.list = list;
		this.flag = type;
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
//		ViewHolder holder;
//		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shopguide
					, null);
//			holder = new ViewHolder();
			LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout);
			TextView textView = (TextView) convertView.findViewById(R.id.loucengTv);
//			convertView.setTag(holder);
//		}else{
//			holder  = (ViewHolder) convertView.getTag();
//		}
			if(list.size() > 0 && position < list.size()){
		HashMap<String, Object> map = list.get(position);
		textView.setText(map.get("louceng").toString());
		@SuppressWarnings("unchecked")
		ArrayList<String> brandList = (ArrayList<String>) map.get("brandList");
		int cha = brandList.size() - layout.getChildCount()*ConstantClass.NUMBER;//计算品牌数与已经添加了的品牌总数的差值
		if(cha >ConstantClass.NUMBER || cha == brandList.size()){
			for (int i = 0; i < brandList.size(); i++) {
				if(i%ConstantClass.NUMBER == 0){
					LinearLayout linearLayout = new LinearLayout(context);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 5, 0, 0);
					linearLayout.setLayoutParams(params);
					linearLayout.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);
					linearLayout.setPadding(0, 0, 0, 8);
					if(layout.getChildCount()%2 == 0){
						if(flag == 0){
							linearLayout.setBackgroundColor(Color.rgb(65, 108, 157));
						}else if (flag == 1) {
							linearLayout.setBackgroundColor(Color.rgb(51, 160, 166));
						}
						
					}
					layout.addView(linearLayout);
					TextView tView = new TextView(context);
					int w = (MyAppLication.getInstant().getScreenW()-70)/ConstantClass.NUMBER;
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(w, LayoutParams.WRAP_CONTENT);
//					params.setMargins(0, 5, 0, 0);
					tView.setLayoutParams(param);
					tView.setText(brandList.get(i));
					tView.setTextColor(Color.WHITE);
					tView.setTextSize(14);
					linearLayout.addView(tView);
					
				}else{
					TextView tView = new TextView(context);
					int w = (MyAppLication.getInstant().getScreenW()-70)/ConstantClass.NUMBER;
					LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(w, LayoutParams.WRAP_CONTENT);
					param.setMargins(5, 0, 0, 0);
					tView.setLayoutParams(param);
					if(i%ConstantClass.NUMBER == 2){
						if(MyAppLication.getInstant().getScreenW()>= 720){
							tView.setPadding(0, 0, 50, 0);
						}else if(MyAppLication.getInstant().getScreenW()== 480){
							tView.setPadding(0, 0, 30, 0);
						}else{
							tView.setPadding(0, 0, 10, 0);
						}
					}
					tView.setText(brandList.get(i));
					tView.setTextColor(Color.WHITE);
					tView.setTextSize(14);
					((LinearLayout)(layout.getChildAt(layout.getChildCount()-1))).addView(tView);
				}
				
			}
		}
		convertView.setOnClickListener(null);
			}
		return convertView;
	}
	class ViewHolder{
		TextView textView;
		LinearLayout layout;
	}
}
