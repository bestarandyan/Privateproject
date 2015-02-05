package com.qingfengweb.baoqi.propertyInsurance.ext;
import java.util.List;
import com.qingfengweb.baoqi.bean.CustomerQueryDataBean;
import com.qingfengweb.baoqi.propertyInsurance.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class DataAdapter extends BaseAdapter {
private Context mContext=null;
private ViewHolder vh = null;
private List<CustomerQueryDataBean> list=null;
public DataAdapter( Context montext, List<CustomerQueryDataBean> lists){
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
						.inflate(R.layout.hquery_adapter, null);
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
		private TextView byPeople;
		private TextView cardstate;
		private TextView time;
		private TextView baofei;
		private TextView starttime;
		private TextView sf;
		private TextView address=null;
		public ViewHolder(View layout) {
			id=(TextView) layout.findViewById(R.id.id);
			byPeople=(TextView) layout.findViewById(R.id.topic);
			cardstate=(TextView) layout.findViewById(R.id.name);
			time=(TextView) layout.findViewById(R.id.type);
			baofei=(TextView) layout.findViewById(R.id.tele);
			starttime=(TextView) layout.findViewById(R.id.source);
			sf=(TextView) layout.findViewById(R.id.state);
			address=(TextView) layout.findViewById(R.id.address);
		}

		public void setContent(CustomerQueryDataBean args) {
			id.setText(args.getId());
			byPeople.setText(args.getTopic());
			cardstate.setText(args.getName());
			time.setText(args.getType());
			baofei.setText(args.getTel());
			starttime.setText(args.getFrom());
			sf.setText(args.getState());
			address.setText(args.getAddress());
			
		}
	}
}
