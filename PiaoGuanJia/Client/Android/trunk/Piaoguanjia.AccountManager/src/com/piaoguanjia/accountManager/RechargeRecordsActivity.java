package com.piaoguanjia.accountManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.piaoguanjia.accountManager.adapter.ChargeAdapter;
import com.piaoguanjia.accountManager.database.DBHelper;
import com.piaoguanjia.accountManager.database.TableCreate;
import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;
import com.piaoguanjia.accountManager.view.XListView;
import com.piaoguanjia.accountManager.view.XListView.IXListViewListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class RechargeRecordsActivity extends MainFrameActivity implements
		OnItemClickListener, IXListViewListener {
	private int isUpOrDown = 2;// 向上刷新还是向下刷新 1向上，2向下
	private ArrayList<HashMap<String, Object>> list;
	private BaseAdapter chargeAdapter;
	private XListView listView;

	private String starttime = "";// 最新时间
	private String endtime = "";// 最新时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView = new XListView(this);
		listView.setDivider(null);
		listView.setDividerHeight((int) getResources().getDimension(
				R.dimen.marginsizel));
		listView.setOnItemClickListener(this);
		addCenterView(listView);
		setBottomEnable(false);
		setTopEnable(false);
		initView("充值记录", "", "", "", "", true);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		isUpOrDown = 2;
		noDataLayout.setVisibility(View.VISIBLE);
		noDataMsg.setText("正在努力加载中...");
		new Thread(this).start();
	}

	/**
	 * 刷新列表
	 */
	public void notifyAdapter() {
		getList();
		if (list.size() <= 0) {
			noDataLayout.setVisibility(View.VISIBLE);
			noDataMsg.setText("暂无数据");
			listView.setPullLoadEnable(false);
		} else {
			String tag = "";
			if (listView.getTag() != null) {
				tag = listView.getTag().toString();
			}
			if (tag.equals("无数据")) {
				listView.setPullLoadEnable(false);
			} else {
				listView.setPullLoadEnable(true);
			}
		}
		if (chargeAdapter == null) {
			chargeAdapter = new ChargeAdapter(this, list,CheckManagerActivity.TYPE_CHOGNZHI);
			listView.setAdapter(chargeAdapter);
		}
		chargeAdapter.notifyDataSetChanged();

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
		selectresult = DBHelper.getInstance()
				.selectRow(
						"select * from " + TableCreate.TABLENAME_CHARGEINFO
								+ " where userid = '"
								+ AccountApplication.userid + "'", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(0).get("createtime") != null) {
				starttime = selectresult.get(0).get("createtime").toString();
			}
			if (selectresult.get(0).get("audittime") != null) {
				starttime = selectresult.get(0).get("audittime").toString();
			}
			for (int i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, Object>();
				if (selectresult.get(i).get("createtime") != null&&!selectresult.get(i).get("createtime").equals("")) {
					endtime = selectresult.get(i)
							.get("createtime").toString();
				}
				if (selectresult.get(i).get("audittime") != null&&!selectresult.get(i).get("audittime").equals("")) {
					endtime = selectresult.get(i)
							.get("audittime").toString();
				}
				if (selectresult.get(i).get("chargid") != null) {
					map.put("chargid", selectresult.get(i).get("chargid")
							.toString());
				}
				if (selectresult.get(i).get("username") != null) {
					map.put("username", selectresult.get(i).get("username")
							.toString());
				}
				if (selectresult.get(i).get("accounttype") != null) {
					map.put("accounttype",
							selectresult.get(i).get("accounttype").toString());
				}
				if (selectresult.get(i).get("createtime") != null) {
					map.put("createtime", selectresult.get(i).get("createtime")
							.toString());
				}
				if (selectresult.get(i).get("type") != null) {
					map.put("type", selectresult.get(i).get("type")
							.toString());
				}
				if (selectresult.get(i).get("productname") != null) {
					map.put("productname", selectresult.get(i)
							.get("productname").toString());
				}
				if (selectresult.get(i).get("audittime") != null) {
					map.put("audittime", selectresult.get(i).get("audittime")
							.toString());
				}
				if (selectresult.get(i).get("status") != null) {
					map.put("status", selectresult.get(i).get("status")
							.toString());
				}
				if (selectresult.get(i).get("amount") != null) {
					map.put("amount", selectresult.get(i).get("amount")
							.toString());
				}
				list.add(map);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (list.get(arg2-1).get("chargid") != null) {
			Intent i = new Intent();
			i.putExtra("id", list.get(arg2-1).get("chargid").toString());
			i.setClass(this, RechargeDetailActivity.class);
			startActivity(this, i);
		}
	}

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
				listView.stopLoadMore();
				listView.stopRefresh();
				if (response.startsWith("{") && response.endsWith("}")) {
					if (response.contains("[]")) {
						listView.setTag("无数据");
					}else{
						listView.setTag("");
					}
					break;
				} else if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-37")) {
					errormsg = "没有权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了充值客户端登录，如有问题，请联系票管家！";
				}
				// else if (response.equals("-1000")) {
				// errormsg = "请求超时，请稍后重试！";
				// } else {
				// errormsg = "请求失败，错误编号为"+response;
				// }
				if (!errormsg.equals("")) {
					MessageBox.CreateAlertDialog(errormsg,
							RechargeRecordsActivity.this);
				}
				break;
			}
			noDataLayout.setVisibility(View.GONE);
			notifyAdapter();
		}
	};

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
			PROGRESSMSG = "正在获取充值记录，请稍等...";
			String time = "";
			if (isUpOrDown == 2) {
				time = starttime;
			} else {
				time = endtime;
			}
			String response = RequestServerFromHttp.listCharge(20, time, 3,
					isUpOrDown);// 充值列表
			// response = "[]";
			HandleData.handleChargeList(response,isUpOrDown,-1);
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			msg.setData(b);
			runnablehandler.sendMessage(msg);
		} else {
			runnablehandler.sendEmptyMessage(NONETWORK_HANDLER);
		}
		clicked = false;
	}

	@Override
	public void onRefresh() {
		isUpOrDown = 2;
		new Thread(this).start();
	}

	@Override
	public void onLoadMore() {
		isUpOrDown = 1;
		new Thread(this).start();
	}
}
