package com.chinaLife.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.chinaLife.claimAssistant.sc_CaseHandlerFlowActivity;
import com.chinaLife.claimAssistant.sc_CaseOfOnlyOneActivity;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
public class sc_MessageListAdapter extends BaseAdapter {
	private Activity context; // 承接上文内容
	private List<HashMap<String, String>> list; // listview中的数据项
	private List<Object> linearList = new ArrayList<Object>();
	private ViewHolder vh = null;

	/**
	 * 构造函数ConnectAdapter
	 */

	public sc_MessageListAdapter(Activity context,
			List<HashMap<String, String>> listItems) {
		this.context = context;
		this.list = listItems;

	}

	public int getCount() {

		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);

	}

	public long getItemId(int position) {

		return position;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			if (convertView == null
					|| (convertView != null && convertView.getTag() == null)) {
				convertView = ((LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.sc_item_message, null);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.setContent(list.get(position),position);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(position%2 == 0){
			convertView.setBackgroundColor(Color.rgb(255, 255, 255));
		}else{
			convertView.setBackgroundColor(Color.rgb(242, 242, 242));
		}
		return convertView;
	}
	
	private class ViewHolder {
		private LinearLayout btnLinear;
		private LinearLayout infoLinear;
		private Button detail;
		private TextView tv1;
		private TextView tv2;
		private LinearLayout convertView;
		public ViewHolder(View layout) {
			convertView = (LinearLayout) layout;
			btnLinear = (LinearLayout) convertView.findViewById(R.id.linear2);
			infoLinear = (LinearLayout) convertView.findViewById(R.id.linear1);
			tv1 = (TextView) convertView.findViewById(R.id.time);
			tv2 = (TextView) convertView.findViewById(R.id.content);
			detail = (Button) btnLinear.findViewById(R.id.bt1);
		}

		public void setContent(HashMap<String, String> args,int position) {
			if (position % 2 == 1) {
				convertView.setBackgroundColor(Color.parseColor("#ededed"));
			}
			
			if(position == 0){
				btnLinear.setVisibility(View.VISIBLE);
			}else{
				btnLinear.setVisibility(View.GONE);
			}
			detail.setId(position);
			detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					

					Intent intent = new Intent();
					intent.putExtra("tag", 2);
					sc_MyApplication.getInstance().setClaimid(list.get(v.getId()).get("claimid").toString());
					sc_MyApplication.getInstance().setClaimidstate(Integer.parseInt(list.get(v.getId()).get("claimstatus").toString()));
					intent.putExtra("claimstate", Integer.parseInt(list.get(v.getId()).get("clainstatuspre").toString()));
					if(Integer.parseInt(list.get(v.getId()).get("claimstatus").toString())
							!=Integer.parseInt(list.get(v.getId()).get("clainstatuspre").toString())){
						intent.putExtra("flag", true);
					}
					sc_MyApplication.getInstance().setCaseid(list.get(v.getId()).get("caseid").toString());
					sc_MyApplication.getInstance().setCaseidstate(Integer.parseInt(list.get(v.getId()).get("casestatus").toString()));
					if(Integer.parseInt(list.get(v.getId()).get("service_type").toString())==1){
						intent.setClass(context, sc_CaseOfOnlyOneActivity.class);
					}else{
						intent.setClass(context, sc_CaseHandlerFlowActivity.class);
					}
					context.startActivity(intent);
					context.finish();
				}
			});

			tv1.setText(args.get("create_time").toString());
			tv2.setText(args.get("content").toString());
			tv1.setTag(position);
			tv2.setTag(position);
			btnLinear.setTag(position);
			infoLinear.setTag(btnLinear);
			infoLinear.setId(position);
			if (position == 0) {
				btnLinear.setVisibility(View.VISIBLE);
			}
			linearList.add(btnLinear);

			infoLinear.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					int a = v.getId();
					for (int i = 0; i < linearList.size(); i++) {
						if (a != i) {
							((View) linearList.get(i)).setVisibility(View.GONE);
						}
					}
					if (a == Integer.parseInt(btnLinear.getTag().toString())) {
						if (!(btnLinear.isShown())) {
							btnLinear.setVisibility(View.VISIBLE);
						} else {
							btnLinear.setVisibility(View.GONE);
						}
					}

				}

			});
		}
	}

}