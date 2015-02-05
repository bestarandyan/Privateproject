package com.chinaLife.claimAssistant.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.activity.Sc_CaseHandlerFlowActivity;
import com.chinaLife.claimAssistant.activity.Sc_CaseInfoActivity;
import com.chinaLife.claimAssistant.activity.Sc_CaseListActivity;
import com.chinaLife.claimAssistant.activity.Sc_CaseOfOnlyOneActivity;
import com.chinaLife.claimAssistant.activity.Sc_ClaimRevokeActivity;
import com.chinaLife.claimAssistant.activity.Sc_ConfirmServiceActivity;
import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
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
import android.widget.Toast;

public class sc_CaseListAdapter extends BaseAdapter {
	private Sc_CaseListActivity context; // 承接上文内容
	private List<HashMap<String, String>> list; // listview中的数据项
	private List<Object> linearList = new ArrayList<Object>();
	

	/**
	 * 构造函数ConnectAdapter
	 */

	public sc_CaseListAdapter(Sc_CaseListActivity context,
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

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.sc_item_list,
				null);
		final LinearLayout btnLinear = (LinearLayout) l
				.findViewById(R.id.linear2);
		if (position % 2 == 1) {
			l.setBackgroundColor(Color.parseColor("#ededed"));
		}
		LinearLayout infoLinear = (LinearLayout) l.findViewById(R.id.linear1);
		Button detail = (Button) l.findViewById(R.id.bt1);
		ImageView telephone = (ImageView) l.findViewById(R.id.telephone);
		telephone.setId(position);
		telephone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(
						context);
				final int phoneid = v.getId();
				callDailog
						.setMessage(list.get(v.getId()).get("claimphonenumber"))
						.setPositiveButton("拨号",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											Intent intent = new Intent(
													Intent.ACTION_CALL);
											intent.setData(Uri
													.parse("tel:"
															+ list.get(phoneid)
																	.get("claimphonenumber")));
											context.startActivity(intent);
										} catch (Exception e) {
											Log.e("SampleApp",
													"Failed to invoke call", e);
										}
									}
								}).setNegativeButton("取消", null).show();
			}
		});
		detail.setId(position);
		detail.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (Integer.parseInt(list.get(v.getId()).get("haveclaim")) == 1) {
					Sc_MyApplication.getInstance().setClaimid(list.get(v.getId()).get("claimid"));
					Sc_MyApplication.getInstance().setClaimidstate(
							Integer.parseInt(list.get(v.getId()).get("claimstatus")));
				}else{
					Sc_MyApplication.getInstance().setClaimidstate(-1);
					Sc_MyApplication.getInstance().setClaimid("");
				}
				Sc_MyApplication.getInstance().setCaseid(list.get(v.getId()).get("caseid").toString());
				Sc_MyApplication.getInstance().setCaseidstate(
						Integer.parseInt(list.get(v.getId()).get("casestatus")));
				Intent intent = new Intent();
				intent.putExtra("tag", 0);
				intent.setClass(context, Sc_CaseInfoActivity.class);
				context.startActivity(intent);
				context.finish();
			}
		});
		Button kancha = (Button) l.findViewById(R.id.bt2);
		kancha.setId(position);
		kancha.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Sc_MyApplication.getInstance().setCasedescription_tag(2);
				if (Integer.parseInt(list.get(v.getId()).get("haveclaim")) == 1) {
					Sc_MyApplication.getInstance().setClaimid(list.get(v.getId()).get("claimid"));
					Sc_MyApplication.getInstance().setCaseid(list.get(v.getId()).get("caseid").toString());
					Sc_MyApplication.getInstance().setClaimidstate(
							Integer.parseInt(list.get(v.getId()).get("claimstatus")));
					Sc_MyApplication.getInstance().setCaseidstate(
							Integer.parseInt(list.get(v.getId()).get("casestatus")));
					if (Integer.parseInt(list.get(v.getId()).get("servertype")) == 1) {
						sc_DBHelper database = sc_DBHelper.getInstance();
						String sql = "select claim_type_select from claiminfo where claimid='"+Sc_MyApplication.getInstance().getClaimid()+"'";
						
						List<Map<String,Object>> list = database.selectRow(sql, null);
						Intent intent = new Intent();
						if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
							intent.setClass(context, Sc_CaseOfOnlyOneActivity.class);
							Sc_MyApplication.getInstance().setCasedescription_tag(2);
							context.startActivity(intent);
							context.finish();
							return;
						}
//						try{
//						Map<String, Object> map = list.get(0);
//						String state =(String) map.get("claim_type_select");
//						if(Integer.parseInt(state) == 1){
							intent.setClass(context, Sc_CaseOfOnlyOneActivity.class);
//						}else{
//							intent.setClass(context, CaseDescriptionActivity.class);
//						}
//						}catch(Exception e) {
//							intent.setClass(context, CaseDescriptionActivity.class);
//						}
						Sc_MyApplication.getInstance().setCasedescription_tag(2);
						context.startActivity(intent);
						context.finish();
					} else {
						Sc_MyApplication.getInstance().setCasedescription_tag(2);
						Intent i = new Intent(context, Sc_CaseHandlerFlowActivity.class);
						i.putExtra("tag", 0);
						context.startActivity(i);
						context.finish();
					}
				} else {
					Sc_MyApplication.getInstance().setCasedescription_tag(2);
					Sc_MyApplication.getInstance().setClaimidstate(-1);
					Sc_MyApplication.getInstance().setClaimid("");
					Sc_MyApplication.getInstance().setCaseid(
							list.get(v.getId()).get("caseid").toString());
					Sc_MyApplication.getInstance().setCaseidstate(
							Integer.parseInt(list.get(v.getId()).get("casestatus")));
					Intent intent = new Intent();
					intent.setClass(context, Sc_ConfirmServiceActivity.class);
					context.startActivity(intent);
					context.finish();
				}
			}
		});
		Button chexiao = (Button) l.findViewById(R.id.bt3);
		chexiao.setId(position);
		chexiao.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Sc_MyApplication.getInstance().setCaseid(list.get(v.getId()).get("caseid").toString());
				Sc_MyApplication.getInstance().setCaseidstate(
						Integer.parseInt(list.get(v.getId()).get("casestatus")));
				Intent i = new Intent();
				i.putExtra("tag", 1);
				i.setClass(context, Sc_ClaimRevokeActivity.class);
				context.startActivity(i);
				context.finish();
			}
		});
		TextView tv1 = (TextView) l.findViewById(R.id.tv1);
		TextView tv2 = (TextView) l.findViewById(R.id.tv2);
		TextView tv3 = (TextView) l.findViewById(R.id.tv3);
		TextView tv4 = (TextView) l.findViewById(R.id.tv4);
		TextView tv5 = (TextView) l.findViewById(R.id.tv5);
		int servertype = 1; 
		try {
			servertype = Integer.parseInt(list.get(position).get("servertype")
					.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		tv1.setText(sc_GetText.getMsg(Integer.parseInt(list.get(position).get("claimstatus").toString())
				,Integer.parseInt(list.get(position).get("casestatus").toString()),servertype));
		if(!sc_GetText.isActive(Integer.parseInt(list.get(position).get("claimstatus").toString())
				,Integer.parseInt(list.get(position).get("casestatus").toString()))){
			chexiao.setClickable(false);
			chexiao.setBackgroundResource(R.drawable.sc_aj_btn_gray);
			if(Integer.parseInt(list.get(position).get("haveclaim")) == 1){
			}else{
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
		infoLinear.setTag(btnLinear);
//		l.setId(position);
		infoLinear.setId(position);
		if (position == 0) {
			btnLinear.setVisibility(View.VISIBLE);
		}
		linearList.add(btnLinear);

		infoLinear.setOnClickListener(new View.OnClickListener() {
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
					} /*else {
						btnLinear.setVisibility(View.GONE);
					}*/
				}

			}

		});
		return l;
	}

}