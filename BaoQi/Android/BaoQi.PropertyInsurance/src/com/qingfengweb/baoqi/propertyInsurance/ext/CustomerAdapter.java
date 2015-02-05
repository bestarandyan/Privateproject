package com.qingfengweb.baoqi.propertyInsurance.ext;
import java.util.List;
import com.qingfengweb.baoqi.bean.CustomerBean;
import com.qingfengweb.baoqi.propertyInsurance.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class CustomerAdapter extends BaseAdapter {
	private Context mContext=null;
	private ViewHolder vh = null;
	private  List<CustomerBean> list=null;
	public CustomerAdapter(Context context,List<CustomerBean> lists){
		mContext=context;
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
						.inflate(R.layout.customer_data_layout, null);
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
		private TextView id;
		private TextView name;
		private TextView byPeople;
		private TextView cardstate;
		private TextView time;
		private TextView baofei;
		private TextView starttime;

		public ViewHolder(View layout) {
			id=(TextView) layout.findViewById(R.id.id);
			name=(TextView) layout.findViewById(R.id.name);
			byPeople=(TextView) layout.findViewById(R.id.sex);
			cardstate=(TextView) layout.findViewById(R.id.sr);
			time=(TextView) layout.findViewById(R.id.pj);
			baofei=(TextView) layout.findViewById(R.id.cid);
			starttime=(TextView) layout.findViewById(R.id.phone);
		}

		public void setContent(CustomerBean args) {
			id.setText(args.getId());
			name.setText(args.getName());
			byPeople.setText(args.getSex());
			cardstate.setText(args.getSr());
			time.setText(args.getPj());
			baofei.setText(args.getCardID());
			starttime.setText(args.getPhone());
		}
	}
}
