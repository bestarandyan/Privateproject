package com.qingfengweb.piaoguanjia.ticketverifier;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainFrameActivity extends BaseActivity {
	public LinearLayout linearInclude, noDataLayout, view4;
	public TextView tv_title, noDataMsg;
	public Button submitbtn;
//	public ImageView btn_jiantou;
	public LinearLayout btn_back;
	public CheckBox checkbox;
	private LockLayer lockLayer = null;
	public RelativeLayout relay_progressbar;
	public RelativeLayout relay_dialog;
	public LinearLayout linear_dialog;
	public Button dialog_btn1, dialog_btn2;
	public View lock = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// setContentView(R.layout.activity_mainframe);
		lock = View.inflate(this, R.layout.activity_mainframe, null);
		lockLayer = new LockLayer(this);
		lockLayer.setLockView(lock);
		linearInclude = (LinearLayout) lock.findViewById(R.id.view3);
		relay_progressbar = (RelativeLayout) lock
				.findViewById(R.id.relay_progressbar);
		relay_dialog = (RelativeLayout) lock.findViewById(R.id.relay_dialog);
		linear_dialog = (LinearLayout) lock.findViewById(R.id.linear_dialog);
		dialog_btn1 = (Button) lock.findViewById(R.id.dialog_btn1);
		dialog_btn2 = (Button) lock.findViewById(R.id.dialog_btn2);
		dialog_btn1.setOnClickListener(this);
		dialog_btn2.setOnClickListener(this);
		tv_title = (TextView) lock.findViewById(R.id.tv_title);
//		btn_jiantou = (ImageView) lock.findViewById(R.id.btn_jiantou);
		btn_back = (LinearLayout) lock.findViewById(R.id.btn_back);
		submitbtn = (Button) lock.findViewById(R.id.submitbtn);
		noDataLayout = (LinearLayout) lock.findViewById(R.id.noDataLayout);
		view4 = (LinearLayout) lock.findViewById(R.id.view4);
		noDataLayout.setVisibility(View.GONE);
		noDataMsg = (TextView) lock.findViewById(R.id.noDataMsg);
		checkbox = (CheckBox) lock.findViewById(R.id.checkbox);
		initView("title", "submitbtn", false);
	}

	@Override
	protected void onResume() {
		lockLayer.lock();
		super.onResume();
	}

	@Override
	protected void onPause() {
		lockLayer.unlock();
		super.onPause();
	}

	public void initView(String title, String submitbtn, boolean jiantou) {
		this.tv_title.setText(title);
		this.submitbtn.setOnClickListener(this);
		this.submitbtn.setText(submitbtn);
//		btn_jiantou.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_back.setOnTouchListener(btnbackOntouch);
		checkbox.setOnClickListener(this);
		if (!jiantou) {
			btn_back.setVisibility(View.GONE);
//			btn_jiantou.setVisibility(View.GONE);
		} else {
//			btn_jiantou.setVisibility(View.VISIBLE);
			btn_back.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 添加中心布局
	 */
	public void addCenterView(View view) {
		LinearLayout.LayoutParams prarms = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT);
		view.setLayoutParams(prarms);
		linearInclude.addView(view);
	}

	/**
	 * 底部button布局是否显示
	 * 
	 * @param enable
	 */
	public void setBottomEnable(boolean enable) {
		if (enable) {
			submitbtn.setVisibility(View.VISIBLE);
		} else {
			submitbtn.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置checkbox显示
	 * 
	 * @param enable
	 */
	public void setCheckboxAble() {
		view4.setVisibility(View.VISIBLE);
	}

	public void startActivity(Activity activity, Intent i) {
		activity.startActivity(i);
		activity.overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
	}

	@Override
	public void onClick(View v) {
		/*if (v == btn_jiantou) {
			finish();
		} else */if (v == btn_back) {
			finish();
		} else if (v == dialog_btn1) {
			relay_dialog.setVisibility(View.GONE);
			linear_dialog.setVisibility(View.GONE);
		} else if (v == dialog_btn2) {
			relay_dialog.setVisibility(View.GONE);
			linear_dialog.setVisibility(View.GONE);
		}
	}

	/**
	 * 页面逻辑处理
	 */
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NONETWORK_HANDLER:// 没有网络时给用户提示
				CreateAlertDialog("请检查网络是否连接！", MainFrameActivity.this);
				break;
			case PROGRESSSTART_HANDLER:// 打开进度条
				relay_progressbar.setVisibility(View.VISIBLE);
				break;
			case PROGRESSEND_HANDLER:// 关闭进度条
				relay_progressbar.setVisibility(View.GONE);
				break;
			case RESPONSE_HANDLER:
				String errormsg = "";
				Bundle b = msg.getData();
				String response = b.getString(KEY_RESPONSE);
				if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-37")) {
					errormsg = "没有验证权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了客户端登录，如有问题，请联系您的服务专员！";
				} else if (response.equals("-12")) {
					errormsg = "订单编号不存在";
				} else if (response.equals("-9")) {
					errormsg = "订单信息不存在";
				} else if (response.equals("-10")) {
					errormsg = "订单编号为空，或者格式不正确";
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
				CreateAlertDialog(errormsg, MainFrameActivity.this);
				break;
			}
		}
	};

	/**
	 * 提示框</br> title ：提示框标题</br> msg : 提示信息</br> conntext ：Activity</br>
	 * */
	public View CreateAlertDialog(String msg, Context context) {
		relay_dialog.setOnClickListener(this);
		relay_dialog.setVisibility(View.VISIBLE);
		linear_dialog.setVisibility(View.VISIBLE);
		TextView dialog_title = (TextView) lock.findViewById(R.id.dialog_title);
		LinearLayout dialog_content1 = (LinearLayout) lock
				.findViewById(R.id.dialog_content1);
		TextView content_tv1 = (TextView) lock.findViewById(R.id.content_tv1);
		TextView content_tv2 = (TextView) lock.findViewById(R.id.content_tv2);
		TextView content_tv3 = (TextView) lock.findViewById(R.id.content_tv3);
		dialog_title.setText("提示");
		dialog_btn1.setVisibility(View.GONE);
		dialog_content1.setVisibility(View.GONE);
		content_tv3.setVisibility(View.VISIBLE);
		content_tv3.setText(msg);
		return dialog_btn2;
	}

	/**
	 * 提示框</br> title ：提示框标题</br> msg : 提示信息</br> conntext ：Activity</br>
	 * */
	public void CreateAlertDialog1(int num1, int num2, Context context) {
		relay_dialog.setOnClickListener(this);
		relay_dialog.setVisibility(View.VISIBLE);
		linear_dialog.setVisibility(View.VISIBLE);
		TextView dialog_title = (TextView) lock.findViewById(R.id.dialog_title);
		dialog_title.setText("确定全部要验证吗？");
		LinearLayout dialog_content1 = (LinearLayout) lock
				.findViewById(R.id.dialog_content1);
		TextView content_tv1 = (TextView) lock.findViewById(R.id.content_tv1);
		TextView content_tv2 = (TextView) lock.findViewById(R.id.content_tv2);
		TextView content_tv3 = (TextView) lock.findViewById(R.id.content_tv3);
		content_tv1.setText(num1 + "");
		content_tv2.setText(num2 + "");
		dialog_content1.setVisibility(View.VISIBLE);
		content_tv3.setVisibility(View.GONE);
	}

	/**
	 * 提示框</br> title ：提示框标题</br> msg : 提示信息</br> conntext ：Activity</br>
	 * */
	public void CreateAlertDialog2(Context context) {
		relay_dialog.setOnClickListener(this);
		relay_dialog.setVisibility(View.VISIBLE);
		linear_dialog.setVisibility(View.VISIBLE);
		TextView dialog_title = (TextView) lock.findViewById(R.id.dialog_title);
		dialog_title.setText("确定验证吗？");
		LinearLayout dialog_content1 = (LinearLayout) lock
				.findViewById(R.id.dialog_content1);
		// TextView content_tv1 = (TextView)
		// lock.findViewById(R.id.content_tv1);
		// TextView content_tv2 = (TextView)
		// lock.findViewById(R.id.content_tv2);
		TextView content_tv3 = (TextView) lock.findViewById(R.id.content_tv3);
		dialog_content1.setVisibility(View.GONE);
		content_tv3.setVisibility(View.GONE);
	}

}
