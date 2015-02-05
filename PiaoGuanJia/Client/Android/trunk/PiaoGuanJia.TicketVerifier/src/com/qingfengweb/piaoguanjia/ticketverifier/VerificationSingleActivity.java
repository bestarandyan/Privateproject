package com.qingfengweb.piaoguanjia.ticketverifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.qingfengweb.piaoguanjia.ticketverifier.bean.ValidateResultInfo;
import com.qingfengweb.piaoguanjia.ticketverifier.database.DBHelper;
import com.qingfengweb.piaoguanjia.ticketverifier.database.TableCreate;
import com.qingfengweb.piaoguanjia.ticketverifier.request.HandleData;
import com.qingfengweb.piaoguanjia.ticketverifier.request.RequestServerFromHttp;
import com.qingfengweb.piaoguanjia.ticketverifier.util.NetworkCheck;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class VerificationSingleActivity extends MainFrameActivity {

	public static final int ID_SCROLLVIEW1 = 0x7f44764;// 滚动视图1
	private String orderid = "";// 验证id
	private String ordernumber = "";// 验证劵号
	private int type = 0; // 0历史记录，1验证记录
	public MediaPlayer mp;
	private ArrayList<TextView> views = new ArrayList<TextView>();// 装载Textview集合
	private boolean clicked1 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mp = MediaPlayer.create(this, R.raw.bobaoyuan);
		type = getIntent().getIntExtra("type", 0);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getListScrollView();
		scrollView1.setId(ID_SCROLLVIEW1);
		addCenterView(scrollView1);
		if (type == 0) {
			initView("验证记录详情", "验证", true);
			setBottomEnable(false);
			initData2();
		} else {
			initView("订单详情", "验证", true);
			initData();
			setBottomEnable(true);
		}
		new Thread(this).start();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		List<Map<String, Object>> selectresult = null;
		orderid = getIntent().getStringExtra("orderid");
		ordernumber = getIntent().getStringExtra("ordernumber");
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_INFO
						+ " where orderid = '" + orderid + "'", null);
		if (selectresult == null || selectresult.size() <= 0) {
			return;
		}
		// "orderid text," + // 订单编号 获取详情使用
		// "ordernumber text	," + // 订单号 显示在列表上
		// "ordertime text," + // 预定时间 yyyy-MM-dd
		// "productname text," + // 票种
		// "totalamount text," + // 预定数量
		// "validatetime text," + // 验证时间
		// "parentname text," + // 接口
		// "name text," + // 姓名
		// "phonenumber integer," + // 手机号码
		// "credentialsnumber integer," + // 身份证号码
		// "isvalidate integer" + // 是否验证（1已验证，0未验证） 用来客户端识别页面
		// "接口", "订单是否有效", "预计时间", "票种", "数量", "订单号", "姓名",
		// "手机", "身份证号", "来源"
		// 接口
		if (selectresult.get(0).get("parentname") != null) {
			views.get(0).setText(
					selectresult.get(0).get("parentname").toString());
		} else {
			views.get(0).setText("");
		}
		// 订单是否有效
		views.get(1).setText("有效");
		// 预计时间
		if (selectresult.get(0).get("ordertime") != null) {
			views.get(2).setText(
					selectresult.get(0).get("ordertime").toString());
		} else {
			views.get(2).setText("");
		}
		// String time = "";
		// if (selectresult.get(0).get("ordertime") != null) {
		// try {
		// time = new SimpleDateFormat("yyyy-MM-dd").format(new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(selectresult.get(0).get("ordertime").toString()));
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// }
		// views.get(2).setText(time);
		// 票种
		if (selectresult.get(0).get("productname") != null) {
			views.get(3).setText(
					selectresult.get(0).get("productname").toString());
		} else {
			views.get(3).setText("");
		}
		// 数量
		if (selectresult.get(0).get("totalamount") != null) {
			views.get(4).setText(
					selectresult.get(0).get("totalamount").toString());
		} else {
			views.get(4).setText("");
		}
		// 订单号
		if (selectresult.get(0).get("ordernumber") != null) {
			views.get(5).setText(
					selectresult.get(0).get("ordernumber").toString());
		} else {
			views.get(5).setText("");
		}
		// 姓名
		if (selectresult.get(0).get("name") != null) {
			views.get(6).setText(selectresult.get(0).get("name").toString());
		} else {
			views.get(6).setText("");
		}
		// 手机
		if (selectresult.get(0).get("phonenumber") != null) {
			views.get(7).setText(
					selectresult.get(0).get("phonenumber").toString());
		} else {
			views.get(7).setText("");
		}
		// 身份证号
		if (selectresult.get(0).get("credentialsnumber") != null) {
			views.get(8).setText(
					selectresult.get(0).get("credentialsnumber").toString());
		} else {
			views.get(8).setText("");
		}
		// 来源
		views.get(9).setText("票管家");
	}

	/**
	 * 初始化数据验证记录详情
	 */
	private void initData2() {
		List<Map<String, Object>> selectresult = null;
		orderid = getIntent().getStringExtra("orderid");
		ordernumber = getIntent().getStringExtra("ordernumber");
		type = getIntent().getIntExtra("type", 0);
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_INFO
						+ " where orderid = '" + orderid + "'", null);
		if (selectresult == null || selectresult.size() <= 0) {
			return;
		}

		// 接口
		if (selectresult.get(0).get("parentname") != null) {
			views.get(0).setText(
					selectresult.get(0).get("parentname").toString());
		} else {
			views.get(0).setText("");
		}
		// 验证时间
		if (selectresult.get(0).get("validatetime") != null) {
			views.get(1).setText(
					selectresult.get(0).get("validatetime").toString());
		} else {
			views.get(1).setText("");
		}
		// 验证状态
		views.get(2).setText("已验证");
		// 票种
		if (selectresult.get(0).get("productname") != null) {
			views.get(3).setText(
					selectresult.get(0).get("productname").toString());
		} else {
			views.get(3).setText("");
		}
		// 数量
		if (selectresult.get(0).get("totalamount") != null) {
			views.get(4).setText(
					selectresult.get(0).get("totalamount").toString());
		} else {
			views.get(4).setText("");
		}
		// 预订时间
		if (selectresult.get(0).get("ordertime") != null) {
			views.get(5).setText(
					selectresult.get(0).get("ordertime").toString());
		} else {
			views.get(5).setText("");
		}
		// 订单号
		if (selectresult.get(0).get("ordernumber") != null) {
			views.get(6).setText(
					selectresult.get(0).get("ordernumber").toString());
		} else {
			views.get(6).setText("");
		}
		// 姓名
		if (selectresult.get(0).get("name") != null) {
			views.get(7).setText(selectresult.get(0).get("name").toString());
		} else {
			views.get(7).setText("");
		}
		// 手机
		if (selectresult.get(0).get("phonenumber") != null) {
			views.get(8).setText(
					selectresult.get(0).get("phonenumber").toString());
		} else {
			views.get(8).setText("");
		}
		// 身份证号
		if (selectresult.get(0).get("credentialsnumber") != null) {
			views.get(9).setText(
					selectresult.get(0).get("credentialsnumber").toString());
		} else {
			views.get(9).setText("");
		}
		// 来源
		views.get(10).setText("票管家");
	}

	/**
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getListScrollView() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(prarms);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		String[] names = null;
		if (type == 0) {
			names = new String[] { "接口", "验证时间", "验证状态", "票种", "数量", "预订时间",
					"订单号", "姓名", "手机", "身份证号", "来源" };
		} else {
			names = new String[] { "接口", "订单是否有效", "预订时间", "票种", "数量", "订单号",
					"姓名", "手机", "身份证号", "来源" };
		}
		if (views == null) {
			views = new ArrayList<TextView>();
		}
		views.clear();
		for (int i = 0; i < names.length; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.listitem1,
					null);
			if (names[i].equals("验证状态") || names[i].equals("数量")||names[i].equals("订单是否有效")) {
				((TextView) (view.findViewById(R.id.tv2)))
						.setTextColor(getResources()
								.getColor(R.color.xuehongse));
			}
			((TextView) (view.findViewById(R.id.tv1))).setText(names[i]);
			((TextView) (view.findViewById(R.id.tv2))).setTag(view);
			((TextView) (view.findViewById(R.id.tv1)))
					.setWidth(TicketApplication.widthPixels / 3);
			if (names.length <= 1) {
				view.setBackgroundResource(R.drawable.app_list_corner_round);
			} else {
				if (i == 0) {
					view.setBackgroundResource(R.drawable.app_list_corner_round_top);
				} else if (i == names.length - 1) {
					view.setBackgroundResource(R.drawable.app_list_corner_round_bottom);
				} else if (i == names.length - 2) {
					view.setBackgroundResource(R.drawable.linearbg1);
				} else {
					view.setBackgroundResource(R.drawable.linearbg);
				}
			}
			views.add((TextView) (view.findViewById(R.id.tv2)));
			linearLayout.addView(view);
		}
		scrollView.addView(linearLayout);
		return scrollView;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == submitbtn) {
			CreateAlertDialog2(this);
		} else if (v.getId() == R.id.dialog_btn1) {
		} else if (v.getId() == R.id.dialog_btn2) {
			new Thread(runnableVerification).start();
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
				boolean b1 = b.getBoolean("list");
				if (response.startsWith("{") && response.endsWith("}") && !b1) {
					HandleData.handleStatus(ordernumber);
					Gson gson = new Gson();// 创建Gson对象
					ValidateResultInfo bean = null;
					try {
						bean = gson
								.fromJson(response, ValidateResultInfo.class);// 解析json对象
					} catch (Exception e) {
					}
					String msginfo = "";
					if (bean.getSuccessCount().equals("1")) {
						mp.start();
						msginfo = "验证成功";
					} else {
						msginfo = "验证失败";
					}
					View view = CreateAlertDialog(msginfo, getParent());
					view.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});
					break;
				} else if (response.startsWith("{") && response.endsWith("}")) {
					if(type==0){
						initData2();
					}else{
						initData();
					}
					break;
				}if (response.equals("-7")) {
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
				}else if (response.equals("-404")) {
					errormsg = "服务器处于维护状态，请稍后重试！";
				}else if (response.equals("-1000")) {
					errormsg = "请求超时，请稍后重试！";
				} else {
					errormsg = "请求失败，错误编号为"+response;
				}
				if (!errormsg.equals("")) {
					CreateAlertDialog(errormsg, VerificationSingleActivity.this);
				}
				break;
			}
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
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
			String response = RequestServerFromHttp.order(orderid);// 充值详情
			HandleData.handleInfo(response);
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			b.putBoolean("list", true);
			msg.setData(b);
			runnablehandler.sendMessage(msg);
			handler.sendEmptyMessage(PROGRESSEND_HANDLER);
		} else {
			runnablehandler.sendEmptyMessage(NONETWORK_HANDLER);
		}
		clicked = false;
	}

	/***
	 * 验证
	 */

	public Runnable runnableVerification = new Runnable() {

		@Override
		public void run() {
			if (!clicked1) {
				clicked1 = true;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(VerificationSingleActivity.this)) {
				handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
				PROGRESSMSG = "正在验证，请稍等...";
				String response = "";
				response = RequestServerFromHttp.validate(ordernumber);//
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
			clicked1 = false;
		}
	};
}
