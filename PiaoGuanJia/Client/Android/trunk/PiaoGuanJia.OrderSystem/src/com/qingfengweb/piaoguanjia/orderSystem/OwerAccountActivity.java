package com.qingfengweb.piaoguanjia.orderSystem;

import java.util.ArrayList;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.qingfengweb.piaoguanjia.orderSystem.adapter.ProduceAdapter;
import com.qingfengweb.piaoguanjia.orderSystem.model.UserInfo;
import com.qingfengweb.piaoguanjia.orderSystem.request.SimpleServlet;
import com.qingfengweb.piaoguanjia.orderSystem.request.UserServlet;
import com.qingfengweb.piaoguanjia.orderSystem.util.JsonUtils;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;
import com.qingfengweb.piaoguanjia.orderSystem.util.NetworkCheck;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView;
import com.qingfengweb.piaoguanjia.orderSystem.view.XListView.IXListViewListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class OwerAccountActivity extends MainFrameActivity {

	@ViewInject(R.id.parent)
	private View parent;

	private ArrayList<TextView> views = new ArrayList<TextView>();// 装载Textview集合

	private int type = 0;// 0我的账户，1编辑个人资料
	@ViewInject(R.id.linearbtn1)
	private LinearLayout linearbtn1;
	@ViewInject(R.id.linearbtn2)
	private LinearLayout linearbtn2;
	@ViewInject(R.id.btn_exit)
	private Button btn_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getListScrollView();
		addCenterView(view);
		ViewUtils.inject(this);
		parent.setOnTouchListener(this);
		initView();
		initData();
		new Thread(runnableOwer).start();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		UserInfo userinfo = MyApplication.userinfo;
		views.get(0).setText(userinfo.getUsername());
		views.get(1).setText(userinfo.getName());
		views.get(2).setText(userinfo.getPhoneNumber());
		views.get(3).setText(userinfo.getCdiNumber());
		views.get(4).setText(userinfo.getEmail());
		views.get(5).setText(userinfo.getQq());
		views.get(6).setText(userinfo.getMsn());
		views.get(7).setText(userinfo.getAlipay());
		views.get(8).setText(userinfo.getAccountName());
		views.get(9).setText(userinfo.getAccountNumber());
		views.get(10).setText(userinfo.getBankName());
	}

	/***
	 * 初始化控件
	 */
	private void initView() {
		tv_title.setText("我的账户");
	}

	/**
	 * 将输入框全部变成文本框（true） 将输入框恢复（false）
	 */
	public void changeEdit(boolean b) {
		if (views != null && views.size() > 0) {
			if (b) {
				tv_title.setText("我的账户");
				for (View v : views) {
					v.setBackgroundColor(Color.WHITE);
					v.setEnabled(false);
				}
			} else {
				tv_title.setText("编辑个人资料");
				for (View v : views) {
					if(v==views.get(0))
						continue;
					v.setBackgroundResource(R.drawable.login_bg_border);
					v.setEnabled(true);
				}
			}
		}
	}

	/**
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getListScrollView() {
		ScrollView scrollView = new ScrollView(this);
		scrollView.setVerticalScrollBarEnabled(true);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(prarms);
		linearLayout.setPadding(20, 20, 20, 20);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		String[] names = { "用户名", "姓名", "手机号码", "身份证号码", "邮箱", "QQ", "msn",
				"支付宝", "账户名称", "银行账户", "开户行" };
		if (views == null) {
			views = new ArrayList<TextView>();
		}
		views.clear();
		for (int i = 0; i < names.length; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.listitem1,
					null);
			((TextView) (view.findViewById(R.id.tv1))).setText(names[i]);
			((TextView) (view.findViewById(R.id.tv2))).setTag(view);
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
		View view = LayoutInflater.from(this).inflate(R.layout.listitem2, null);
		linearLayout.addView(view);
		scrollView.addView(linearLayout);
		return scrollView;
	}

	@OnClick({ R.id.btn_exit, R.id.linearbtn1, R.id.linearbtn2 })
	public void btnonClick(View v) {
		if (v == btn_exit) {
			if (type == 0) {// 退出
				Intent i = new Intent(this, LoginActivity.class);
				startActivity(i);
				MyApplication.clearActivity();
				if (MyApplication.maintabactivity != null
						&& !MyApplication.maintabactivity.isFinishing())
					MyApplication.maintabactivity.finish();
			} else if (type == 1) {// 保存
				new Thread(runnableUpdateOwer).start();
			}
		} else if (v == linearbtn1) {
			changeEdit(false);
			linearbtn1.setVisibility(View.GONE);
			linearbtn2.setVisibility(View.GONE);
			btn_exit.setText("保存");
			type = 1;
		} else if (v == linearbtn2) {
			Intent i = new Intent(this,PasswordActivity.class);
			startActivity(i);
		}
		super.onClick(v);
	}
	
	
	/***
	 * 获取个人信息
	 */
	private Runnable runnableOwer = new Runnable() {
		
		@Override
		public void run() {
			if(NetworkCheck.IsHaveInternet(OwerAccountActivity.this)){
				String response = UserServlet.actionUser();
				if(checkResponse(response)){
					try {
						UserInfo userinfo = JsonUtils.jsonObject(UserInfo.class, response);
						MyApplication.userinfo = userinfo;
						MyApplication.dbuser.saveOrUpdate(userinfo);
						handlerRunnable.sendEmptyMessage(1);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}else{
				handler.sendEmptyMessage(NONETWORK_HANDLER);
			}
		}
	};
	
	/***
	 * 保存个人信息
	 */
	private Runnable runnableUpdateOwer = new Runnable() {
		
		@Override
		public void run() {
			if(NetworkCheck.IsHaveInternet(OwerAccountActivity.this)){
				PROGRESSMSG = "正在保存个人信息，请稍等...";
				handler.sendEmptyMessage(PROGRESSSTART_HANDLER);
				UserInfo userinfo = getUserInfo();
				String response = UserServlet.actionUpdateuser(userinfo);
				handler.sendEmptyMessage(PROGRESSEND_HANDLER);
				if(checkResponse(response)){
					try {
						MyApplication.userinfo = userinfo;
						MyApplication.dbuser.saveOrUpdate(userinfo);
						handlerRunnable.sendEmptyMessage(2);
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}else{
				handler.sendEmptyMessage(NONETWORK_HANDLER);
			}
		}
	};
	
	/**
	 * 获取用户信息
	 * @return
	 */
	private UserInfo getUserInfo() {
		UserInfo userinfo = MyApplication.userinfo;
		userinfo.setName(views.get(1).getText().toString());
		userinfo.setPhoneNumber(views.get(2).getText().toString());
		userinfo.setCdiNumber(views.get(3).getText().toString());
		userinfo.setEmail(views.get(4).getText().toString());
		userinfo.setQq(views.get(5).getText().toString());
		userinfo.setMsn(views.get(6).getText().toString());
		userinfo.setAlipay(views.get(7).getText().toString());
		userinfo.setAccountName(views.get(8).getText().toString());
		userinfo.setAccountNumber(views.get(9).getText().toString());
		userinfo.setBankName(views.get(10).getText().toString());
		return userinfo;
	}
	
	public Handler handlerRunnable = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				initData();
				break;
			case 2:
				type = 0;
				changeEdit(true);
				btn_exit.setText("退出登录");
				linearbtn1.setVisibility(View.VISIBLE);
				linearbtn2.setVisibility(View.VISIBLE);
				break;
			}
		}
		
	};

}
