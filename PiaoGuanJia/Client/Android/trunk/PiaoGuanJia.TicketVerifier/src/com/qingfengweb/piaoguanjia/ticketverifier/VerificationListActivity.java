package com.qingfengweb.piaoguanjia.ticketverifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.qingfengweb.piaoguanjia.ticketverifier.adapter.ValidateAdapter;
import com.qingfengweb.piaoguanjia.ticketverifier.bean.ValidateResultInfo;
import com.qingfengweb.piaoguanjia.ticketverifier.database.DBHelper;
import com.qingfengweb.piaoguanjia.ticketverifier.database.TableCreate;
import com.qingfengweb.piaoguanjia.ticketverifier.request.HandleData;
import com.qingfengweb.piaoguanjia.ticketverifier.request.RequestServerFromHttp;
import com.qingfengweb.piaoguanjia.ticketverifier.util.NetworkCheck;
import com.qingfengweb.piaoguanjia.ticketverifier.view.XListView;
import com.qingfengweb.piaoguanjia.ticketverifier.view.XListView.IXListViewListener;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VerificationListActivity extends MainFrameActivity {
	private ArrayList<HashMap<String, Object>> list;
	private ValidateAdapter Adapter;
	private XListView listView;
	public MediaPlayer mp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView = new XListView(this);
		listView.setDivider(null);
		mp = MediaPlayer.create(this, R.raw.bobaoyuan);
		addCenterView(listView);
		setBottomEnable(true);
		setCheckboxAble();
		initView("订单信息验证", "验证", true);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
	}

	@Override
	protected void onResume() {
		notifyAdapter();
		super.onResume();
	}

	/**
	 * 刷新列表
	 */
	public void notifyAdapter() {
		getList();
		if (list.size() <= 0) {
			noDataLayout.setVisibility(View.VISIBLE);
			noDataMsg.setText("暂无数据");
		} else {
			noDataLayout.setVisibility(View.GONE);
		}
		if (Adapter == null) {
			Adapter = new ValidateAdapter(this, list, 1);
		}
		listView.setAdapter(Adapter);
		Adapter.notifyDataSetChanged();

	}

	/**
	 * 获取list列表
	 */

	public void getList() {
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		list.clear();
		HashMap<String, Object> map = null;
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_VALIDATEINFO
						+ " where userid = '" + TicketApplication.userid
						+ "' order by julianday(ordertime) desc", null);
		if (selectresult != null && selectresult.size() > 0) {
			for (int i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, Object>();
				// 主键
				if (selectresult.get(i).get("orderid") != null) {
					map.put("orderid", selectresult.get(i).get("orderid")
							.toString());
				}
				// 订单号
				if (selectresult.get(i).get("ordernumber") != null) {
					map.put("ordernumber",
							selectresult.get(i).get("ordernumber").toString());
				}
				// 预定时间
				if (selectresult.get(i).get("ordertime") != null) {
					map.put("ordertime", selectresult.get(i).get("ordertime")
							.toString());
				}
				// 票种
				if (selectresult.get(i).get("productname") != null) {
					map.put("productname",
							selectresult.get(i).get("productname").toString());
				}
				// 游客姓名
				if (selectresult.get(i).get("name") != null) {
					map.put("name", selectresult.get(i).get("name").toString());
				}

				// 预定数量
				if (selectresult.get(i).get("totalamount") != null) {
					map.put("totalamount",
							selectresult.get(i).get("totalamount").toString());
				}
				list.add(map);
			}
		}
	}

	public void ShowConnectDialog() {
		int num = 0;
		int ticketnum = 0;
		for (Entry<Integer, Boolean> entry : Adapter.getIsSelected().entrySet()) {
			if (entry.getValue()) {
				num++;
				ticketnum += Integer.parseInt(list.get(entry.getKey())
						.get("totalamount").toString());
			}
		}
		CreateAlertDialog1(num, ticketnum, this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == submitbtn) {
			if (validate()) {
				ShowConnectDialog();
			}
		} else if (v == dialog_btn1) {
			relay_dialog.setVisibility(View.GONE);
			linear_dialog.setVisibility(View.GONE);
		} else if (v == dialog_btn2) {
			relay_dialog.setVisibility(View.GONE);
			linear_dialog.setVisibility(View.GONE);
			if(!((TextView) lock.findViewById(R.id.dialog_title)).getText().toString().equals("提示")){
				new Thread(runnableVerification).start();
			}else{
				new Thread(this).start();
			}
		} else if (v == checkbox) {
			if (checkbox.isChecked()) {
				// 遍历list的长度，将MyAdapter中的map值全部设为true
				for (int i = 0; i < list.size(); i++) {
					Adapter.getIsSelected().put(i, true);
				}
				// 刷新listview和TextView的显示
				dataChanged();
			} else {
				// 遍历list的长度，将已选的按钮设为未选
				for (int i = 0; i < list.size(); i++) {
					if (Adapter.getIsSelected().get(i)) {
						Adapter.getIsSelected().put(i, false);
					}
				}
				// 刷新listview和TextView的显示
				dataChanged();
			}
		}
	}

	/***
	 * 验证
	 */

	public Runnable runnableVerification = new Runnable() {

		@Override
		public void run() {
			if (!clicked) {
				clicked = true;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(VerificationListActivity.this)) {
				handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
				PROGRESSMSG = "正在验证，请稍等...";
				String response = "";
				StringBuffer ids = new StringBuffer("");
				for (Entry<Integer, Boolean> entry : Adapter.getIsSelected()
						.entrySet()) {
					if (entry.getValue()) {
						ids.append(Integer.parseInt(list.get(entry.getKey())
								.get("ordernumber").toString())
								+ ",");
					}
				}
				String ordernumbers = "";
				try {
					ordernumbers = ids.substring(0, ids.length() - 1);
				} catch (Exception e) {
				}
				response = RequestServerFromHttp.validate(ordernumbers);//
				Message msg = new Message();
				msg.what = RESPONSE_HANDLER;
				Bundle b = new Bundle();
				b.putString(KEY_RESPONSE, response);
				msg.setData(b);
				runnablehandler.sendMessage(msg);
				handler.sendEmptyMessage(PROGRESSEND_HANDLER);
			} else {
				runnablehandler.sendEmptyMessage(NONETWORK_HANDLER);
			}
			clicked = false;
		}
	};

	/**
	 * 页面逻辑处理
	 */
	public Handler runnablehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RESPONSE_HANDLER:
				String errormsg = "";
				Bundle b = msg.getData();
				String response = b.getString(KEY_RESPONSE);
				if (response.startsWith("{") && response.endsWith("}")) {
					Gson gson = new Gson();// 创建Gson对象
					ValidateResultInfo bean = null;
					try {
						bean = gson
								.fromJson(response, ValidateResultInfo.class);// 解析json对象
					} catch (Exception e) {
					}
					int n = 0;
					for (int i = 0; i < list.size(); i++) {
						if (Adapter.getIsSelected().get(i)) {
							n++;
						}
					}
					if (n==1) {
						mp.start();
						CreateAlertDialog("验证成功", getParent());
					}else{
						Adapter.initDate();
						CreateAlertDialog("成功数量:"+bean.getSuccessCount()+"\n"
								+"失败数量:"+bean.getFailureCount()+"\n"
								+"总数量:"+bean.getTotalCount()+"\n", getParent());
						notifyAdapter();
					}
					break;
				} else if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-37")) {
					errormsg = "没有权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了充值客户端登录，如有问题，请联系票管家！";
				} else {
					errormsg = "请求失败，错误编号为" + response;
				}
				// else if (response.equals("-1000")) {
				// errormsg = "请求超时，请稍后重试！";
				// } else {
				// errormsg = "请求失败，错误编号为"+response;
				// }
				if (!errormsg.equals("")) {
					CreateAlertDialog(errormsg, VerificationListActivity.this);
				}
				break;
			}
		}
	};

	private void dataChanged() {
		// 通知listView刷新
		Adapter.notifyDataSetChanged();
	};

	@Override
	public boolean validate() {
		int num = 0;
		int ticketnum = 0;
		for (Entry<Integer, Boolean> entry : Adapter.getIsSelected().entrySet()) {
			if (entry.getValue()) {
				num++;
				ticketnum += Integer.parseInt(list.get(entry.getKey())
						.get("totalamount").toString());
			}
		}
		if (num == 0 || ticketnum == 0) {
			CreateAlertDialog("没有可以验证的票劵", this);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 数据逻辑处理
	 */
	@Override
	public void run() {
		if (!clicked) {
			clicked = true;
		} else {
			return;
		}
		if (NetworkCheck.IsHaveInternet(this)) {
			PROGRESSMSG = "正在更新列表，请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);// 开启进度条
			String response = RequestServerFromHttp.orderNumber(getIntent().getStringExtra("id"));// 查询
			if(response.equals("-9")){
				DBHelper.getInstance().execSql("delete from "+TableCreate.TABLENAME_VALIDATEINFO);
			}
			HandleData.handleOrderNumber(response,0);
			refushListHandler.sendEmptyMessage(0);
			handler.sendEmptyMessage(PROGRESSEND_HANDLER);//关闭进度条
		}
		clicked = false;
	}
	
	private Handler refushListHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			notifyAdapter();
		}
		
	};
}
