package com.qingfengweb.piaoguanjia.ticketverifier.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import com.qingfengweb.piaoguanjia.ticketverifier.R;
import com.qingfengweb.piaoguanjia.ticketverifier.VerificationSingleActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ValidateAdapter extends BaseAdapter implements OnClickListener {
	public ArrayList<HashMap<String, Object>> list;
	public Context context;
	// 用来控制CheckBox的选中状况
	private HashMap<Integer, Boolean> isSelected;
	private int type = 0;// 0没有checkbox，1有checkbox
	HashMap<Integer, View> map = new HashMap<Integer, View>();

	public ValidateAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, int type) {
		this.context = context;
		this.type = type;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();
		initDate();
	}

	// 初始化isSelected的数据
	public void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, false);
		}
	}

	public HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (map.get(position) == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.listitem2, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_orderform = (TextView) convertView
					.findViewById(R.id.tv_orderform);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_class = (TextView) convertView
					.findViewById(R.id.tv_class);
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.linear_btn = (LinearLayout) convertView
					.findViewById(R.id.linear_btn);
			holder.imageview_gou1 = (ImageView) convertView
					.findViewById(R.id.imageview_gou1);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkbox);
			map.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView = map.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setContent(position);
		return convertView;
	}

	class ViewHolder {
		private TextView tv_name, tv_orderform, tv_time, tv_class, tv_num;
		private LinearLayout linear_btn;
		private ImageView imageview_gou1;
		private CheckBox checkbox;

		public void setContent(int position) {

			// 订单号
			if (list.get(position).get("ordernumber") != null) {
				tv_orderform.setText(list.get(position).get("ordernumber")
						.toString());
			} else {
				tv_orderform.setText("");
			}
			// 预定时间
			if (list.get(position).get("ordertime") != null) {
				tv_time.setText(list.get(position).get("ordertime").toString());
			}
			// 票种
			if (list.get(position).get("productname") != null) {
				tv_class.setText(list.get(position).get("productname")
						.toString());
			} else {
				tv_class.setText("");
			}
			// 游客姓名
			if (list.get(position).get("name") != null) {
				tv_name.setText(list.get(position).get("name").toString());
			} else {
				tv_name.setText("");
			}

			// 预定数量
			if (list.get(position).get("totalamount") != null) {
				tv_num.setText(list.get(position).get("totalamount").toString());
			} else {
				tv_num.setText("");
			}
			if (type == 0) {
				imageview_gou1.setVisibility(View.VISIBLE);
				checkbox.setVisibility(View.GONE);
			} else {
				imageview_gou1.setVisibility(View.GONE);
				checkbox.setVisibility(View.VISIBLE);
			}
			imageview_gou1.setOnClickListener(ValidateAdapter.this);
			imageview_gou1.setId(position);
			linear_btn.setId(position);
			linear_btn.setOnClickListener(ValidateAdapter.this);
			try {
				checkbox.setId(position);
				checkbox.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						isSelected.put(checkbox.getId(), cb.isChecked());
					}
				});
				checkbox.setChecked(getIsSelected().get(position));
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		i.putExtra("type", type);
		i.putExtra("ordernumber",
				list.get(v.getId()).get("ordernumber")
						.toString());
		i.putExtra("orderid",
				list.get(v.getId()).get("orderid")
						.toString());
		i.setClass(context, VerificationSingleActivity.class);
		context.startActivity(i);
	}
}
