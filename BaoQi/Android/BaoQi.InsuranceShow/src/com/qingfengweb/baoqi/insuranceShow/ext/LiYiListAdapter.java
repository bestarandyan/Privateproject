package com.qingfengweb.baoqi.insuranceShow.ext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
public class LiYiListAdapter extends BaseAdapter {
private Context mContext=null;
private ViewHolder vh = null;
private List<HashMap<String, String>> list=null;
public LiYiListAdapter( Context montext, List<HashMap<String, String>> lists){
	mContext=montext;
	list=lists;
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
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.l_li_xian_1_item, null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(list.get(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	private class ViewHolder {
		private TextView 
//		t1,
		t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12
		,t13,t14,t15,t16,t17,t18,t19;
		public ViewHolder(View layout) {
//			t1=(TextView) layout.findViewById(R.id.t1);
			t2=(TextView) layout.findViewById(R.id.t2);
			t3=(TextView) layout.findViewById(R.id.t3);
			t4=(TextView) layout.findViewById(R.id.t4);
			t5=(TextView) layout.findViewById(R.id.t5);
			t6=(TextView) layout.findViewById(R.id.t6);
			t7=(TextView) layout.findViewById(R.id.t7);
			t8=(TextView) layout.findViewById(R.id.t8);
			t9=(TextView) layout.findViewById(R.id.t9);
			t10=(TextView) layout.findViewById(R.id.t10);
			t11=(TextView) layout.findViewById(R.id.t11);
			t12=(TextView) layout.findViewById(R.id.t12);
			t13=(TextView) layout.findViewById(R.id.t13);
			t14=(TextView) layout.findViewById(R.id.t14);
			t15=(TextView) layout.findViewById(R.id.t15);
			t16=(TextView) layout.findViewById(R.id.t16);
			t17=(TextView) layout.findViewById(R.id.t17);
			t18=(TextView) layout.findViewById(R.id.t18);
			t18=(TextView) layout.findViewById(R.id.t19);

		}

		public void setContent(HashMap<String, String> args) {
//			t1.setText(args.get("t1").toString());
			t2.setText(args.get("t2").toString());
			t3.setText(args.get("t3").toString());
			t4.setText(args.get("t4").toString());
			t5.setText(args.get("t5").toString());
			t6.setText(args.get("t6").toString());
			t7.setText(args.get("t7").toString());
			t8.setText(args.get("t8").toString());
			t9.setText(args.get("t9").toString());
			t10.setText(args.get("t10").toString());
			t11.setText(args.get("t11").toString());
			t12.setText(args.get("t12").toString());
			t13.setText(args.get("t13").toString());
			t14.setText(args.get("t14").toString());
			t15.setText(args.get("t15").toString());
			t16.setText(args.get("t16").toString());
			t17.setText(args.get("t17").toString());
			t18.setText(args.get("t18").toString());
			t19.setText(args.get("t19").toString());
			
		}
	}
}
