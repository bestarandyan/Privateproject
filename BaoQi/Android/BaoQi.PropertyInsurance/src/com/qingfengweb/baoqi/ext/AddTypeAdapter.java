package com.qingfengweb.baoqi.ext;
import java.util.List;
import com.qingfengweb.baoqi.bean.TypeBean;
import com.qingfengweb.baoqi.propertyInsurance.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class AddTypeAdapter extends BaseAdapter {
     private Context mContext=null;
     private List<TypeBean> list=null;
 	private ViewHolder vh = null;
	
	 public AddTypeAdapter(Context context, List<TypeBean> lists ){
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
						.inflate(R.layout.type_adapter_layout , null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(list.get(position),position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	private class ViewHolder {
		private CheckBox checkbox;
		private TextView id;
		private TextView code;
		private TextView name;
		private TextView number;
		private TextView bf;
		private TextView fs;
		private TextView year;
		private TextView ebf;
		public ViewHolder(View layout) {
			checkbox=(CheckBox) layout.findViewById(R.id.p1);
			id=(TextView) layout.findViewById(R.id.p2);
			code=(TextView) layout.findViewById(R.id.p3);
			name=(TextView) layout.findViewById(R.id.p4);
			number=(TextView) layout.findViewById(R.id.p5);
			bf=(TextView) layout.findViewById(R.id.p6);
			fs=(TextView) layout.findViewById(R.id.p7);
			year=(TextView) layout.findViewById(R.id.p8);
			ebf=(TextView) layout.findViewById(R.id.p9);
		}

		public void setContent(TypeBean bean,int position) {
			id.setText(position+"");
			code.setText(bean.getCode());
			name.setText(bean.getName());
			number.setText(bean.getNumber());
			bf.setText(bean.getBf());
			fs.setText(bean.getFs());
			year.setText(bean.getYear());
			ebf.setText(bean.getEbf());
		}
	}		
}
