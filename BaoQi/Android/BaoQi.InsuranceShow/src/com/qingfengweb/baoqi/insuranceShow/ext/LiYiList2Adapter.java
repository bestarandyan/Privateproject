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
public class LiYiList2Adapter extends BaseAdapter {
private Context mContext=null;
private ViewHolder vh = null;
private List<HashMap<String, String>> list=null;
public LiYiList2Adapter( Context montext, List<HashMap<String, String>> lists){
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
						.inflate(R.layout.l_li_xian_2_item, null);
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
		t1,
		t2,t3,t4,t5,t6,t7,t8,t9;
		public ViewHolder(View layout) {
			t1=(TextView) layout.findViewById(R.id.t2_1);
			t2=(TextView) layout.findViewById(R.id.t2_2);
			t3=(TextView) layout.findViewById(R.id.t2_3);
			t4=(TextView) layout.findViewById(R.id.t2_4);
			t5=(TextView) layout.findViewById(R.id.t2_5);
			t6=(TextView) layout.findViewById(R.id.t2_6);
			t7=(TextView) layout.findViewById(R.id.t2_7);
			t8=(TextView) layout.findViewById(R.id.t2_8);
			t9=(TextView) layout.findViewById(R.id.t2_9);
			

		}

		public void setContent(HashMap<String, String> args) {
			t1.setText(args.get("t1").toString());
			t2.setText(args.get("t2").toString());
			t3.setText(args.get("t3").toString());
			t4.setText(args.get("t4").toString());
			t5.setText(args.get("t5").toString());
			t6.setText(args.get("t6").toString());
			t7.setText(args.get("t7").toString());
			t8.setText(args.get("t8").toString());
			t9.setText(args.get("t9").toString());
			
			
		}
	}
}
