package com.qingfengweb.baoqi.ext;
import java.util.List;
import com.qingfengweb.baoqi.bean.CustomerDataBean;
import com.qingfengweb.baoqi.insuranceShow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class QueryAdapter extends BaseAdapter {
	private List<CustomerDataBean> list=null;
	private Context mContext=null;
	private ViewHolder vh = null;
	public QueryAdapter(Context context,List<CustomerDataBean>  list ){
		this.list=list;
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
						.inflate(R.layout.query_data_adapter_layout, null);
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
		private TextView people;
		private TextView byPeople;
		private TextView cardstate;
		private TextView time;
		private TextView baofei;
		private TextView starttime;
		private TextView sf;

		public ViewHolder(View layout) {
			id=(TextView) layout.findViewById(R.id.ids);
			people=(TextView) layout.findViewById(R.id.people);
			byPeople=(TextView) layout.findViewById(R.id.bypeople);
			cardstate=(TextView) layout.findViewById(R.id.cardstate);
			time=(TextView) layout.findViewById(R.id.time);
			baofei=(TextView) layout.findViewById(R.id.baofei);
			starttime=(TextView) layout.findViewById(R.id.starttime);
			sf=(TextView) layout.findViewById(R.id.sf);
		}

		public void setContent(CustomerDataBean args) {
			id.setText(args.getId());
			people.setText(args.getPeople());
			byPeople.setText(args.getByPeople());
			cardstate.setText(args.getCardState());
			time.setText(args.getTime());
			baofei.setText(args.getBaoFei());
			starttime.setText(args.getStartTime());
			sf.setText(args.getsF());
		}
	}
}
