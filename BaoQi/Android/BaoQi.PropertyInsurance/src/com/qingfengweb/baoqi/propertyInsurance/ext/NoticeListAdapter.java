package com.qingfengweb.baoqi.propertyInsurance.ext;
import java.util.HashMap;
import java.util.List;
import com.qingfengweb.baoqi.propertyInsurance.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticeListAdapter extends BaseAdapter {
	private Context context; // 承接上文内容
	private List<HashMap<String,String>> listItems; // listview中的数据项
	private ViewHolder vh = null;
	
	
	/**
	 * 构造函数ConnectAdapter
	 */

	public NoticeListAdapter(Context context, List<HashMap<String,String>> listItems) {
		this.context = context;
		this.listItems = listItems;

	}

	public int getCount() {

		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);

	}

	public long getItemId(int position) {

		// TODO Auto-generated method stub
		return position;

	}
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.notice_item, null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(listItems.get(position));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
//	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = listContainer.inflate(R.layout.notice_item, null);
//		TextView content = (TextView)convertView.findViewById(R.id.content);
//		TextView time = (TextView)convertView.findViewById(R.id.time);
//		TextView ower = (TextView)convertView.findViewById(R.id.ower);
//		content.setText(listItems.get(position).get("content").toString());
//		time.setText(listItems.get(position).get("time").toString());
//		ower.setText(listItems.get(position).get("ower").toString());
//		return convertView;
//	}
	private class ViewHolder {
		private TextView content ;
		private TextView time;
		private TextView ower ;
		public ViewHolder(View layout) {
            content=(TextView) layout.findViewById(R.id.content);
			time=(TextView) layout.findViewById(R.id.time);
			ower = (TextView)layout.findViewById(R.id.ower);
		}

		public void setContent(HashMap<String, String> map) {
			content.setText(map.get("content").toString());
			time.setText(map.get("time").toString());
			ower.setText(map.get("ower").toString());
		}
	}

}
