package com.qingfengweb.baoqi.ext;

import java.util.List;

import com.qingfengweb.baoqi.propertyInsurance.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class GenerationAdapter extends BaseAdapter {
	private Context mContext;
	List<String[]> list=null;
	public GenerationAdapter(Context context,List<String[]>args){
		mContext=context;
		list=args;
	}
	private ViewHolder vh = null;
	
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
  
	String[] args={"1","丈夫","李丽","28","女","二级"};
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.plan_adapter_layout, null);
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
		private CheckBox id;
		private TextView bypeople;
		private TextView name;
		private TextView bir;
		private TextView sex;
		private TextView type;
		public ViewHolder(View layout) {
			id=(CheckBox) layout.findViewById(R.id.p1);
			bypeople=(TextView) layout.findViewById(R.id.p2);
			name=(TextView) layout.findViewById(R.id.p3);
			bir=(TextView) layout.findViewById(R.id.p4);
			sex=(TextView) layout.findViewById(R.id.p5);
			type=(TextView) layout.findViewById(R.id.p6);
		}

		public void setContent(String [] args) {
//			id.setText(args[0]);
			bypeople.setText(args[1]);
			name.setText(args[2]);
			bir.setText(args[3]);
			sex.setText(args[4]);
			type.setText(args[5]);
		}
	}		
}
