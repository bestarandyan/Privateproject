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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CheckManagerListActivity extends MainFrameActivity implements
		OnItemClickListener, IXListViewListener {

	private int isUpOrDown = 2;// 向上刷新还是向下刷新 1向上，2向下
	private ArrayList<HashMap<String, Object>> list;
	private BaseAdapter chargeAdapter;
	private XListView listView;

	private String starttime = "";// 最新时间
	private String endtime = "";// 最新时间
	public int type = 0;// 0未审核，1历史记录
	public int activitytype = 0;// 充值,1专用账户2额度
	public boolean first_create_tag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activitytype = getIntent().getIntExtra("type", 0);
		listView = new XListView(this);
		rl1.setBackgroundColor(Color.parseColor("#3ABFDE"));
		// ListView.LayoutParams prarms = new ListView.LayoutParams(
		// android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
		// android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
		// listView.setLayoutParams(prarms);
		listView.setDivider(null);
		listView.setDividerHeight((int) getResources().getDimension(
				R.dimen.marginsizel));
		// listView.setBackground(null);
		listView.setOnItemClickListener(this);
		listView.setXListViewListener(this);
		addCenterView(listView);
		setBottomEnable(false);
		setTopEnable(true);
		if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
			initView("充值审核", "待审核", "历史记录", "", "", true);
		} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
			initView("专用账户审核", "待审核", "历史记录", "", "", true);
		} else if (activitytype == CheckManagerActivity.TYPE_EDU) {
			initView("额度审核", "待审核", "历史记录", "", "", true);
		}
		initData();
	}

	@Override
	protected void onResume() {
		if(first_create_tag){
			first_create_tag = false;
		}else{
			notifyAdapter();
		}
		super.onResume();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		isUpOrDown = 2;
		HandleData.selectNum();
		int num = 0;
		if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
			num = AccountApplication.chargenum;
		} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
			num = AccountApplication.accountnum;
		} else if (activitytype == CheckManagerActivity.TYPE_EDU) {
			num = AccountApplication.creditnum;
		}
		if (num <= 0) {
			message_person.setVisibility(View.INVISIBLE);
		} else {
			message_person.setVisibility(View.VISIBLE);
			message_person.setText(num + "");
		}
		noDataLayout.setVisibility(View.VISIBLE);
		noDataMsg.setText("正在努力加载中...");
		rl1.setTag("clicked");
//		notifyAdapter();
		new Thread(this).start();
	}

	@Override
	public void onClick(View v) {
		starttime = "";
		endtime = "";
		if (v == rl1) {
			type = 0;
		} else {
			type = 1;
		}
		noDataLayout.setVisibility(View.VISIBLE);
		noDataMsg.setText("正在努力加载中...");
		if (v.getTag() != null && v.getTag().toString().equals("clicked")) {
			notifyAdapter();
		} else {
//			notifyAdapter();
			v.setTag("clicked");
			new Thread(this).start();
		}
		super.onClick(v);
	}

	/**
	 * 刷新列表
	 */
	public void notifyAdapter() {
		getList();
		HandleData.selectNum();
		int num = 0;
		if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
			num = AccountApplication.chargenum;
		} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
			num = AccountApplication.accountnum;
		} else if (activitytype == CheckManagerActivity.TYPE_EDU) {
			num = AccountApplication.creditnum;
		}
		if (num <= 0) {
			message_person.setVisibility(View.INVISIBLE);
		} else {
			message_person.setVisibility(View.VISIBLE);
			message_person.setText(num + "");
		}
		if (list.size() <= 0) {
			noDataLayout.setVisibility(View.VISIBLE);
			noDataMsg.setText("暂无数据");
			listView.setPullLoadEnable(false);
		} else {
			noDataLayout.setVisibility(View.GONE);
			String tag = "";
			if (listView.getTag() != null) {
				tag = listView.getTag().toString();
			}
			if (tag.equals("无数据")) {
				listView.setPullLoadEnable(false);
			}else if (list.size()<5) {
				listView.setPullLoadEnable(false);
			} else {
				listView.setPullLoadEnable(true);
			}
		}
		if (chargeAdapter == null) {
			chargeAdapter = new ChargeAdapter(this, list, activitytype);
			listView.setAdapter(chargeAdapter);
		}
		chargeAdapter.notifyDataSetChanged();

	}

	/**
	 * 获取充值列表
	 */
	public void getList() {
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		// status，0未审核，1审核通过，2审核不通过，3已取消
		list.clear();
		if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
			getChongzhiList();
		} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
			getZhuanyongList();
		} else {
			getEduList();
		}
	}

	/**
	 * 获取充值列表
	 */
	public void getChongzhiList() {
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		// status，0未审核，1审核通过，2审核不通过，3已取消
		list.clear();
		HashMap<String, Object> map = null;
		List<Map<String, Object>> selectresult = null;
		String sql = "";

		String status = (type == 0 ? " and status = 0" : " and status > 0");
		String suff = type==0?"createtime":"audittime"; 
		sql = "select * from " + TableCreate.TABLENAME_CHARGEINFO
				+ " where userid = '" + AccountApplication.userid + "' "
				+ status;
		selectresult = DBHelper.getInstance().selectRow(sql, null);
		starttime = "";
		endtime = "";
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(0).get(suff) != null) {
				starttime = selectresult.get(0).get(suff).toString();
			}
			for (int i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get(suff) != null&&!selectresult.get(i).get(suff).equals("")) {
					endtime = selectresult.get(i)
							.get(suff).toString();
				}
				map = new HashMap<String, Object>();
				if (selectresult.get(i).get("chargid") != null) {
					map.put("id", selectresult.get(i).get("chargid").toString());
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
				if (selectresult.get(i).get("productid") != null) {
					map.put("productid", selectresult.get(i).get("productid")
							.toString());
				}
				if (selectresult.get(i).get("productname") != null) {
					map.put("productname",
							selectresult.get(i).get("productname").toString());
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

	/**
	 * 获取专用账户列表
	 */

	public void getZhuanyongList() {
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		// status，0未审核，1审核通过，2审核不通过，3已取消
		list.clear();
		HashMap<String, Object> map = null;
		List<Map<String, Object>> selectresult = null;
		String sql = "";

		String status = (type == 0 ? " and (status & 1)= 1"
				: " and ((status & 1) > 1 or (status & 1) < 1)");
		String suff = type==0?"createtime":"audittime"; 
		sql = "select * from " + TableCreate.TABLENAME_ACCOUNTINFO
				+ " where userid = '" + AccountApplication.userid + "' "
				+ status;
		selectresult = DBHelper.getInstance().selectRow(sql, null);

		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(0).get(suff) != null) {
				starttime = selectresult.get(0).get(suff).toString();
			}
			if (selectresult.get(selectresult.size() - 1).get(suff) != null) {
				endtime = selectresult.get(selectresult.size() - 1)
						.get(suff).toString();
			}
			for (int i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, Object>();
				if (selectresult.get(i).get(suff) != null&&!selectresult.get(i).get(suff).equals("")) {
					endtime = selectresult.get(i)
							.get(suff).toString();
				}
				if (selectresult.get(i).get("accountid") != null) {
					map.put("id", selectresult.get(i).get("accountid")
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

				if (selectresult.get(i).get("productname") != null) {
					map.put("productname",
							selectresult.get(i).get("productname").toString());
				}
				if (selectresult.get(i).get("productid") != null) {
					map.put("productid", selectresult.get(i).get("productid")
							.toString());
				}
				if (selectresult.get(i).get("audittime") != null) {
					map.put("audittime", selectresult.get(i).get("audittime")
							.toString());
				}
				if (selectresult.get(i).get("status") != null) {
					map.put("status", selectresult.get(i).get("status")
							.toString());
				}
				/*
				 * if (selectresult.get(i).get("amount") != null) {
				 * map.put("amount", selectresult.get(i).get("amount")
				 * .toString()); }
				 */
				list.add(map);
			}
		}
	}

	/**
	 * 获取额度列表
	 */

	public void getEduList() {
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		// status，0未审核，1审核通过，2审核不通过，3已取消
		list.clear();
		HashMap<String, Object> map = null;
		List<Map<String, Object>> selectresult = null;
		String sql = "";

		String status = (type == 0 ? " and (status & 1)= 1"
				: " and ((status & 1) > 1 or (status & 1) < 1)");
		String suff = type==0?"createtime":"audittime"; 
		sql = "select * from " + TableCreate.TABLENAME_CREDITINFO
				+ " where userid = '" + AccountApplication.userid + "' "
				+ status;
		selectresult = DBHelper.getInstance().selectRow(sql, null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(0).get(suff) != null) {
				starttime = selectresult.get(0).get(suff).toString();
			}
			for (int i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get(suff) != null&&!selectresult.get(i).get(suff).equals("")) {
					endtime = selectresult.get(i)
							.get(suff).toString();
				}
				map = new HashMap<String, Object>();
				if (selectresult.get(i).get("creditid") != null) {
					map.put("id", selectresult.get(i).get("creditid")
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

				if (selectresult.get(i).get("productname") != null) {
					map.put("productname",
							selectresult.get(i).get("productname").toString());
				}
				if (selectresult.get(i).get("audittime") != null) {
					map.put("audittime", selectresult.get(i).get("audittime")
							.toString());
				}
				if (selectresult.get(i).get("status") != null) {
					map.put("status", selectresult.get(i).get("status")
							.toString());
				}
				if (selectresult.get(i).get("productid") != null) {
					map.put("productid", selectresult.get(i).get("productid")
							.toString());
				}

				if (selectresult.get(i).get("creditlimit") != null) {
					map.put("amount1", selectresult.get(i).get("creditlimit")
							.toString());
				}

				list.add(map);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent();
		i.putExtra("activitytype", activitytype);
		i.putExtra("id", list.get(arg2 - 1).get("id").toString());
		i.putExtra("status", type);
		i.setClass(this, CheckManagerInfoActivity.class);
		startActivity(this, i);
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
							CheckManagerListActivity.this);
				}
				break;
			case NONETWORK_HANDLER:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog("请检查网络是否连接！",
						CheckManagerListActivity.this);
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
				time = "";
			} else {
				time = endtime;
			}
			String response = "";// 服务器返回值
			HandleData.handlePendingCount(RequestServerFromHttp.pendingCount());
			if (activitytype == CheckManagerActivity.TYPE_CHOGNZHI) {
				response = RequestServerFromHttp.listCharge(20, time,
						type == 0 ? 1 : 2, isUpOrDown);// 充值列表
				HandleData.handleChargeList(response,isUpOrDown,type);
			} else if (activitytype == CheckManagerActivity.TYPE_ZHUANYONGZHANGHU) {
				response = RequestServerFromHttp.listAccount(20, time,
						type == 0 ? 1 : 2, isUpOrDown);// 充值列表
				HandleData.handleAccountList(response,isUpOrDown,type);
			} else {
				response = RequestServerFromHttp.listCredit(20, time,
						type == 0 ? 1 : 2, isUpOrDown);// 充值列表
				HandleData.handleCreditList(response,isUpOrDown,type);
			}
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
//		notifyAdapter();
		new Thread(this).start();
	}

	@Override
	public void onLoadMore() {
		isUpOrDown = 1;
//		notifyAdapter();
		new Thread(this).start();
	}
}
