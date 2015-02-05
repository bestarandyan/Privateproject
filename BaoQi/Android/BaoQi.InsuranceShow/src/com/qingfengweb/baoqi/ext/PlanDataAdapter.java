package com.qingfengweb.baoqi.ext;
import java.util.List;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class PlanDataAdapter extends BaseAdapter {
    private List<String[]> list=null;
	private Context mContext;
	private ViewHolder vh = null;
	public PlanDataAdapter(Context context,List<String[]> lists) {
		list=lists;
		this.mContext=context;

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
						.inflate(R.layout.data_adapter, null);
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
		private TextView checkBox;
		private TextView year;
		private TextView lastyear;
		private TextView sex;
		private TextView startyear;
		private TextView lj;
		private TextView xj;
		public ViewHolder(View layout) {
			checkBox=(TextView) layout.findViewById(R.id.a1);
			year=(TextView) layout.findViewById(R.id.a2);
			lastyear=(TextView) layout.findViewById(R.id.a3);
			sex=(TextView) layout.findViewById(R.id.a4);
			startyear=(TextView) layout.findViewById(R.id.a5);
			lj=(TextView) layout.findViewById(R.id.a6);
			xj=(TextView) layout.findViewById(R.id.a7);
		}

		public void setContent(String[] text) {
			checkBox.setText("1");
			year.setText(text[0]);
			lastyear.setText(text[1]);
			sex.setText(text[2]);
			startyear.setText(text[3]);
			lj.setText(text[4]);
			xj.setText(text[5]);
		}
	}
}
