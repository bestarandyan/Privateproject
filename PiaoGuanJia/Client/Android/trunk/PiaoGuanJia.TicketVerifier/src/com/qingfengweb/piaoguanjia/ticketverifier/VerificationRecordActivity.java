package com.qingfengweb.piaoguanjia.ticketverifier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qingfengweb.piaoguanjia.ticketverifier.adapter.BidInfoPagerAdapter;
import com.qingfengweb.piaoguanjia.ticketverifier.adapter.ValidateAdapter;
import com.qingfengweb.piaoguanjia.ticketverifier.database.DBHelper;
import com.qingfengweb.piaoguanjia.ticketverifier.database.TableCreate;
import com.qingfengweb.piaoguanjia.ticketverifier.request.HandleData;
import com.qingfengweb.piaoguanjia.ticketverifier.request.RequestServerFromHttp;
import com.qingfengweb.piaoguanjia.ticketverifier.util.NetworkCheck;
import com.qingfengweb.piaoguanjia.ticketverifier.view.XListView;
import com.qingfengweb.piaoguanjia.ticketverifier.view.XListView.IXListViewListener;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class VerificationRecordActivity extends BaseActivity implements
		OnItemClickListener, IXListViewListener, OnPageChangeListener {
	private int isUpOrDown = 2;// 向上刷新还是向下刷新 1向上，2向下
	private ArrayList<HashMap<String, Object>> list;
	private BaseAdapter chargeAdapter;
	private String starttime = "";// 最新时间
	private String endtime = "";// 最新时间

	private int list_state = 1;// 1最近记录，2历史记录

	public LinearLayout linearInclude, noDataLayout, view;
	public TextView tv_title, noDataMsg;
	public Button submitbtn;
//	public ImageView btn_jiantou;
	public LinearLayout btn_back;

	private ImageView jiantou_im1, jiantou_im2;
	private RelativeLayout rel1, rel2;
	private TextView relay1_tv1, relay2_tv2;//
	private View line1, line2;//

	private BidInfoPagerAdapter bidinfopageradapter;
	private ViewPager viewPager;
	private List<View> pageViews;
	private XListView Xlist1;
	private XListView Xlist2;
	private XListView Xlist;
	private RelativeLayout relay_dialog;
	private LinearLayout linear_dialog;
	private Button dialog_btn1, dialog_btn2;
	private LockLayer lockLayer = null;
	View lock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		setContentView(R.layout.activity_verificaterecord);
		lock = View.inflate(this, R.layout.activity_verificaterecord, null);
		lockLayer = new LockLayer(this);
		lockLayer.setLockView(lock);
		relay_dialog = (RelativeLayout) lock.findViewById(R.id.relay_dialog);
		linear_dialog = (LinearLayout) lock.findViewById(R.id.linear_dialog);
		dialog_btn1 = (Button) lock.findViewById(R.id.dialog_btn1);
		dialog_btn2 = (Button) lock.findViewById(R.id.dialog_btn2);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		lockLayer.lock();
		if(currIndex==0){
			jiantou_im1.setVisibility(View.VISIBLE);
			line1.setVisibility(View.VISIBLE);
			jiantou_im2.setVisibility(View.INVISIBLE);
			line2.setVisibility(View.INVISIBLE);
		}else{
			jiantou_im2.setVisibility(View.VISIBLE);
			line2.setVisibility(View.VISIBLE);
			jiantou_im1.setVisibility(View.INVISIBLE);
			line1.setVisibility(View.INVISIBLE);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		lockLayer.unlock();
		super.onPause();
	}

	/***
	 * 初始化控件
	 */
	private void initView() {
		linearInclude = (LinearLayout) lock.findViewById(R.id.view3);
		tv_title = (TextView) lock.findViewById(R.id.tv_title);
//		btn_jiantou = (ImageView) lock.findViewById(R.id.btn_jiantou);
		btn_back = (LinearLayout) lock.findViewById(R.id.btn_back);
//		btn_jiantou.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		noDataLayout = (LinearLayout) lock.findViewById(R.id.noDataLayout);
		noDataLayout.setVisibility(View.GONE);
		noDataMsg = (TextView) lock.findViewById(R.id.noDataMsg);
		jiantou_im1 = (ImageView) lock.findViewById(R.id.jiantou1_im);
		jiantou_im2 = (ImageView) lock.findViewById(R.id.jiantou2_im);
		relay1_tv1 = (TextView) lock.findViewById(R.id.relay1_tv1);
		relay2_tv2 = (TextView) lock.findViewById(R.id.relay2_tv2);
		rel1 = (RelativeLayout) lock.findViewById(R.id.relay1);
		rel2 = (RelativeLayout) lock.findViewById(R.id.relay2);
		line1 = (View) lock.findViewById(R.id.line1);
		line2 = (View) lock.findViewById(R.id.line2);
		jiantou_im1.setVisibility(View.VISIBLE);
		line1.setVisibility(View.VISIBLE);
		jiantou_im2.setVisibility(View.INVISIBLE);
		line2.setVisibility(View.INVISIBLE);
		relay1_tv1.setTextColor(Color.parseColor("#FC7C19"));
		relay2_tv2.setTextColor(Color.parseColor("#535353"));
		rel1.setOnClickListener(this);
		rel2.setOnClickListener(this);
		btn_back.setOnTouchListener(btnbackOntouch);
		pageViews = new ArrayList<View>();
		Xlist1 = new XListView(this);
		Xlist2 = new XListView(this);
		Xlist1.setPullLoadEnable(true);
		Xlist1.setDivider(null);
		Xlist1.setPullRefreshEnable(true);
		Xlist1.setXListViewListener(this);

		Xlist2.setPullLoadEnable(true);
		Xlist2.setPullRefreshEnable(true);
		Xlist2.setDivider(null);
		Xlist2.setXListViewListener(this);
		pageViews.add(Xlist1);
		pageViews.add(Xlist2);
		bidinfopageradapter = new BidInfoPagerAdapter(pageViews);
		viewPager = (ViewPager) lock.findViewById(R.id.awesomepager);
		viewPager.setAdapter(bidinfopageradapter);
		viewPager.setOnPageChangeListener(this);
		Xlist = Xlist1;
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
			Xlist.setPullLoadEnable(false);
		} else {
			String tag = "";
			if (Xlist.getTag() != null) {
				tag = Xlist.getTag().toString();
			}
			if (list.size() % 20 != 0 || tag.equals("无数据")) {
				Xlist.setPullLoadEnable(false);
			} else {
				Xlist.setPullLoadEnable(true);
			}
			noDataLayout.setVisibility(View.GONE);
		}
		if (chargeAdapter == null) {
			chargeAdapter = new ValidateAdapter(this, list, 0);
		}
		Xlist.setAdapter(chargeAdapter);
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
		String sql = "";
		String datetime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (list_state == 1) {
			sql = "select * from " + TableCreate.TABLENAME_VALIDATEINFOHISTORY
					+ " where userid = '" + TicketApplication.userid
					+ "' and validatetime like '" + datetime
					+ "%' order by julianday(validatetime) desc";
		} else {
			sql = "select * from " + TableCreate.TABLENAME_VALIDATEINFOHISTORY
					+ " where userid = '" + TicketApplication.userid
					+ "' order by julianday(validatetime) desc";
		}
		selectresult = DBHelper.getInstance().selectRow(sql, null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(0).get("validatetime") != null) {
				starttime = selectresult.get(0).get("validatetime").toString();
			}
			if (selectresult.get(selectresult.size() - 1).get("validatetime") != null) {
				endtime = selectresult.get(selectresult.size() - 1)
						.get("validatetime").toString();
			}
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

	@Override
	public void onClick(View v) {
		/*if (v == btn_jiantou) {
			finish();
		} else */if (v == btn_back) {
			finish();
		} else if (v == rel1) {
			viewPager.setCurrentItem(0);
			// jiantou_im1.setVisibility(View.VISIBLE);
			// line1.setVisibility(View.VISIBLE);
			// jiantou_im2.setVisibility(View.INVISIBLE);
			// line2.setVisibility(View.INVISIBLE);
			// relay1_tv1.setTextColor(Color.parseColor("#FC7C19"));
			// relay2_tv2.setTextColor(Color.parseColor("#535353"));
		} else if (v == rel2) {
			viewPager.setCurrentItem(1);
			// jiantou_im2.setVisibility(View.VISIBLE);
			// line2.setVisibility(View.VISIBLE);
			// jiantou_im1.setVisibility(View.INVISIBLE);
			// line1.setVisibility(View.INVISIBLE);
			// relay2_tv2.setTextColor(Color.parseColor("#FC7C19"));
			// relay1_tv1.setTextColor(Color.parseColor("#535353"));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (list.get(arg2 - 1).get("chargid") != null) {
			// Intent i = new Intent();
			// i.putExtra("id", list.get(arg2-1).get("chargid").toString());
			// i.setClass(this, RechargeDetailActivity.class);
			// startActivity(this, i);
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
				Xlist.stopLoadMore();
				Xlist.stopRefresh();
				if (response.startsWith("[") && response.endsWith("]")) {
					if (response.equals("[]")) {
						Xlist.setTag("无数据");
					}
					break;
				} else if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-37")) {
					errormsg = "没有验证权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了客户端登录，如有问题，请联系您的服务专员！";
				} else if (response.equals("-12")) {
					errormsg = "订单编号不存在";
				} else if (response.equals("-11")) {
					errormsg = "订单编号为空，或者格式不正确";
				} else if (response.equals("-13")) {
					errormsg = "退出密码为空";
				} else if (response.equals("-14")) {
					errormsg = "退出密码不正确";
				} else if (response.equals("-1000")) {
					errormsg = "请求超时，请稍后重试！";
				} else {
					errormsg = "请求失败，错误编号为" + response;
				}
				if (!errormsg.equals("")) {
//					CreateAlertDialog(errormsg, VerificationRecordActivity.this);
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
			String time = "";
			if (isUpOrDown == 2) {
				time = starttime;
			} else {
				time = endtime;
			}
			String response = RequestServerFromHttp.list(20, time, list_state,
					isUpOrDown);// 验证列表
			HandleData.handleOrderNumber(response, 1);
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

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;

	@Override
	public void onPageSelected(int arg0) {
		list_state = arg0 + 1;
		offset = TicketApplication.widthPixels / 2;
		Animation animation = null;
		Animation animation1 = null;
		switch (arg0) {
		case 0:
			if (currIndex == 1) {
				animation = new TranslateAnimation(offset, 0, 0, 0);
				animation1 = new TranslateAnimation(0,offset*-1, 0, 0);
			}
			Xlist = Xlist1;
			jiantou_im1.setVisibility(View.VISIBLE);
			line1.setVisibility(View.VISIBLE);
			jiantou_im2.setVisibility(View.VISIBLE);
			line2.setVisibility(View.VISIBLE);
			relay1_tv1.setTextColor(Color.parseColor("#FC7C19"));
			relay2_tv2.setTextColor(Color.parseColor("#535353"));
			notifyAdapter();
			break;
		case 1:
			if (currIndex == 0) {
				animation = new TranslateAnimation(0, offset, 0, 0);
				animation1 = new TranslateAnimation(offset*-1, 0, 0, 0);
			}
			Xlist = Xlist2;
			jiantou_im2.setVisibility(View.VISIBLE);
			line2.setVisibility(View.VISIBLE);
			jiantou_im1.setVisibility(View.VISIBLE);
			line1.setVisibility(View.VISIBLE);
			relay2_tv2.setTextColor(Color.parseColor("#FC7C19"));
			relay1_tv1.setTextColor(Color.parseColor("#535353"));
			if (rel2.getTag() != null
					&& rel2.getTag().toString().equals("clicked")) {
				notifyAdapter();
			} else {
				notifyAdapter();
				rel2.setTag("clicked");
				noDataLayout.setVisibility(View.VISIBLE);
				noDataMsg.setText("正在努力加载中...");
				new Thread(this).start();
			}
			break;
		default:
			break;
		}
		currIndex = arg0;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		animation1.setFillAfter(true);// True:图片停在动画结束位置
		animation1.setDuration(300);
		jiantou_im1.startAnimation(animation);
		line1.startAnimation(animation);
		jiantou_im2.startAnimation(animation1);
		line2.startAnimation(animation1);
	}

	/**
	 * 提示框</br> title ：提示框标题</br> msg : 提示信息</br> conntext ：Activity</br>
	 * */
	public void CreateAlertDialog(String msg, Context context) {
		relay_dialog.setVisibility(View.VISIBLE);
		linear_dialog.setVisibility(View.VISIBLE);
		TextView dialog_title = (TextView) lock.findViewById(R.id.dialog_title);
		LinearLayout dialog_content1 = (LinearLayout) lock
				.findViewById(R.id.dialog_content1);
		TextView content_tv1 = (TextView) lock.findViewById(R.id.content_tv1);
		TextView content_tv2 = (TextView) lock.findViewById(R.id.content_tv2);
		TextView content_tv3 = (TextView) lock.findViewById(R.id.content_tv3);
		dialog_btn1.setVisibility(View.GONE);
		dialog_content1.setVisibility(View.GONE);
		content_tv3.setVisibility(View.VISIBLE);
		content_tv3.setText(msg);
	}

	/**
	 * 提示框</br> title ：提示框标题</br> msg : 提示信息</br> conntext ：Activity</br>
	 * */
	public void CreateAlertDialog1(int num1, int num2, Context context,
			OnClickListener listener) {
		relay_dialog.setVisibility(View.VISIBLE);
		linear_dialog.setVisibility(View.VISIBLE);
		TextView dialog_title = (TextView) lock.findViewById(R.id.dialog_title);
		LinearLayout dialog_content1 = (LinearLayout) lock
				.findViewById(R.id.dialog_content1);
		TextView content_tv1 = (TextView) lock.findViewById(R.id.content_tv1);
		TextView content_tv2 = (TextView) lock.findViewById(R.id.content_tv2);
		TextView content_tv3 = (TextView) lock.findViewById(R.id.content_tv3);
		dialog_content1.setVisibility(View.VISIBLE);
		content_tv3.setVisibility(View.GONE);
	}
}
