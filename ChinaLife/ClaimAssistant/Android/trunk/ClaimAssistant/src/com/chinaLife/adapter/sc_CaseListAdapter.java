package com.chinaLife.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinaLife.claimAssistant.sc_CaseHandlerFlowActivity;
import com.chinaLife.claimAssistant.sc_CaseInfoActivity;
import com.chinaLife.claimAssistant.sc_CaseListActivity;
import com.chinaLife.claimAssistant.sc_CaseOfOnlyOneActivity;
import com.chinaLife.claimAssistant.sc_ClaimRevokeActivity;
import com.chinaLife.claimAssistant.sc_ConfirmServiceActivity;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.R;
import com.chinaLife.claimAssistant.bean.sc_GetText;
import com.chinaLife.claimAssistant.database.sc_DBHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class sc_CaseListAdapter extends BaseAdapter {
	private sc_CaseListActivity context; // 承接上文内容
	private List<HashMap<String, String>> list; // listview中的数据项
	private List<Object> linearList = new ArrayList<Object>();
	private ViewHolder holder = null;
	
	private int mPosition = 0;

	/**
	 * 构造函数ConnectAdapter
	 */

	public sc_CaseListAdapter(sc_CaseListActivity context,
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

	private class ViewHolder {
		private LinearLayout infolinear;
		private LinearLayout btnLinear;
		private ImageView telephone;
		private Button detail;
		private Button chexiao;
		private Button kancha;
		private TextView tv1;
		private TextView tv2;
		private TextView tv3;
		private TextView tv4;
		private TextView tv5;

		public void setContent(View view, int position) {
			if (position % 2 == 1) {
				view.setBackgroundColor(Color.parseColor("#ededed"));
			}
			telephone.setId(position);
			telephone.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					AlertDialog.Builder callDailog = new AlertDialog.Builder(
							context);
					final int phoneid = v.getId();
					callDailog
							.setMessage(
									list.get(v.getId()).get("claimphonenumber"))
							.setPositiveButton("拨号",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											try {
												Intent intent = new Intent(
														Intent.ACTION_CALL);
												intent.setData(Uri
														.parse("tel:"
																+ list.get(
																		phoneid)
																		.get("claimphonenumber")));
												context.startActivity(intent);
											} catch (Exception e) {
												Log.e("SampleApp",
														"Failed to invoke call",
														e);
											}
										}
									}).setNegativeButton("取消", null).show();
				}
			});
			detail.setId(position);
			detail.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (Integer.parseInt(list.get(v.getId()).get("haveclaim")) == 1) {
						sc_MyApplication.getInstance().setClaimid(
								list.get(v.getId()).get("claimid"));
						sc_MyApplication.getInstance().setClaimidstate(
								Integer.parseInt(list.get(v.getId()).get(
										"claimstatus")));
					} else {
						sc_MyApplication.getInstance().setClaimidstate(-1);
						sc_MyApplication.getInstance().setClaimid("");
					}
					sc_MyApplication.getInstance().setCaseid(
							list.get(v.getId()).get("caseid").toString());
					sc_MyApplication.getInstance().setCaseidstate(
							Integer.parseInt(list.get(v.getId()).get(
									"casestatus")));
					Intent intent = new Intent();
					intent.putExtra("tag", 0);
					intent.setClass(context, sc_CaseInfoActivity.class);
					context.startActivity(intent);
					context.finish();
				}
			});
			kancha.setId(position);
			kancha.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					sc_MyApplication.getInstance().setCasedescription_tag(2);
					if (Integer.parseInt(list.get(v.getId()).get("haveclaim")) == 1) {
						sc_MyApplication.getInstance().setClaimid(
								list.get(v.getId()).get("claimid"));
						sc_MyApplication.getInstance().setCaseid(
								list.get(v.getId()).get("caseid").toString());
						sc_MyApplication.getInstance().setClaimidstate(
								Integer.parseInt(list.get(v.getId()).get(
										"claimstatus")));
						sc_MyApplication.getInstance().setCaseidstate(
								Integer.parseInt(list.get(v.getId()).get(
										"casestatus")));
						if (Integer.parseInt(list.get(v.getId()).get(
								"servertype")) == 1) {
							sc_DBHelper database = sc_DBHelper.getInstance();
							String sql = "select claim_type_select from claiminfo where claimid='"
									+ sc_MyApplication.getInstance()
											.getClaimid() + "'";

							List<Map<String, Object>> list = database
									.selectRow(sql, null);
							Intent intent = new Intent();
							if (sc_MyApplication.getInstance()
									.getSelfHelpFlag() == 1) {
								intent.setClass(context,
										sc_CaseOfOnlyOneActivity.class);
								sc_MyApplication.getInstance()
										.setCasedescription_tag(2);
								context.startActivity(intent);
								context.finish();
								return;
							}
							intent.setClass(context,
									sc_CaseOfOnlyOneActivity.class);
							sc_MyApplication.getInstance()
									.setCasedescription_tag(2);
							context.startActivity(intent);
							context.finish();
						} else {
							sc_MyApplication.getInstance()
									.setCasedescription_tag(2);
							Intent i = new Intent(context,
									sc_CaseHandlerFlowActivity.class);
							i.putExtra("tag", 0);
							context.startActivity(i);
							context.finish();
						}
					} else {
						sc_MyApplication.getInstance()
								.setCasedescription_tag(2);
						sc_MyApplication.getInstance().setClaimidstate(-1);
						sc_MyApplication.getInstance().setClaimid("");
						sc_MyApplication.getInstance().setCaseid(
								list.get(v.getId()).get("caseid").toString());
						sc_MyApplication.getInstance().setCaseidstate(
								Integer.parseInt(list.get(v.getId()).get(
										"casestatus")));
						Intent intent = new Intent();
						intent.setClass(context,
								sc_ConfirmServiceActivity.class);
						context.startActivity(intent);
						context.finish();
					}
				}
			});
			chexiao.setId(position);
			chexiao.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					sc_MyApplication.getInstance().setCaseid(
							list.get(v.getId()).get("caseid").toString());
					sc_MyApplication.getInstance().setCaseidstate(
							Integer.parseInt(list.get(v.getId()).get(
									"casestatus")));
					Intent i = new Intent();
					i.putExtra("tag", 1);
					i.setClass(context, sc_ClaimRevokeActivity.class);
					context.startActivity(i);
					context.finish();
				}
			});
			int servertype = 1;
			try {
				servertype = Integer.parseInt(list.get(position)
						.get("servertype").toString());
			} catch (Exception e) {
			}
			tv1.setText(sc_GetText.getMsg(
					Integer.parseInt(list.get(position).get("claimstatus")
							.toString()),
					Integer.parseInt(list.get(position).get("casestatus")
							.toString()), servertype));
			if (!sc_GetText.isActive(
					Integer.parseInt(list.get(position).get("claimstatus")
							.toString()),
					Integer.parseInt(list.get(position).get("casestatus")
							.toString()))) {
				chexiao.setClickable(false);
				chexiao.setBackgroundResource(R.drawable.sc_aj_btn_gray);
				if (Integer.parseInt(list.get(position).get("haveclaim")) == 1) {
				} else {
					kancha.setClickable(false);
					kancha.setBackgroundResource(R.drawable.sc_aj_btn_gray);
				}
			}

			tv2.setText(list.get(position).get("casetime").toString());
			tv3.setText(list.get(position).get("casenumber").toString());
			tv4.setText(list.get(position).get("claimname").toString());
			tv5.setText(list.get(position).get("claimphonenumber").toString());
			tv1.setTag(position);
			tv2.setTag(position);
			tv3.setTag(position);
			tv4.setTag(position);
			tv5.setTag(position);
			btnLinear.setTag(position);
			infolinear.setTag(btnLinear);
			infolinear.setId(position);
			if (position == mPosition) {
				btnLinear.setVisibility(View.VISIBLE);
			}else{
				btnLinear.setVisibility(View.GONE);
			}
			linearList.add(btnLinear);

			infolinear.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					int a = v.getId();
					for (int i = 0; i < linearList.size(); i++) {
						if (a != i) {
							((View) linearList.get(i)).setVisibility(View.GONE);
						}
					}
					if (a == Integer.parseInt(btnLinear.getTag().toString())) {
						if (!(btnLinear.isShown())) {
							btnLinear.setVisibility(View.VISIBLE);
						}
						mPosition = a;
					}

				}

			});
		}

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = (LinearLayout) inflater.inflate(
					R.layout.sc_item_list, null);
			holder = new ViewHolder();
			holder.btnLinear = (LinearLayout) convertView
					.findViewById(R.id.linear2);
			holder.infolinear = (LinearLayout) convertView
					.findViewById(R.id.linear1);
			holder.detail = (Button) convertView.findViewById(R.id.bt1);
			holder.telephone = (ImageView) convertView
					.findViewById(R.id.telephone);
			holder.kancha = (Button) convertView.findViewById(R.id.bt2);
			holder.chexiao = (Button) convertView.findViewById(R.id.bt3);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setContent(convertView, position);
		return convertView;
	}

}