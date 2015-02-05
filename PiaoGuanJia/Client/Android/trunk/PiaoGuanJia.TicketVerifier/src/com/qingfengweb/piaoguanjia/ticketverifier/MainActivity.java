package com.qingfengweb.piaoguanjia.ticketverifier;

import com.google.gson.Gson;
import com.qingfengweb.piaoguanjia.ticketverifier.Zxing.decoding.CaptureActivity;
import com.qingfengweb.piaoguanjia.ticketverifier.bean.ValidateResultInfo;
import com.qingfengweb.piaoguanjia.ticketverifier.request.HandleData;
import com.qingfengweb.piaoguanjia.ticketverifier.request.RequestServerFromHttp;
import com.qingfengweb.piaoguanjia.ticketverifier.util.NetworkCheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	private LinearLayout linearbtn1, linearbtn2, linearbtn3, linearbtn4;
	private LockLayer lockLayer = null;
	View lock = null;
	private RelativeLayout relay_dialog;
	private RelativeLayout relay_progressbar;
	private LinearLayout linear_dialog;
	private Button dialog_btn1, dialog_btn2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// setContentView(R.layout.activity_home);
		lock = View.inflate(this, R.layout.activity_home, null);
		lockLayer = new LockLayer(this);
		lockLayer.setLockView(lock);
		relay_progressbar = (RelativeLayout) lock
				.findViewById(R.id.relay_progressbar);
		relay_dialog = (RelativeLayout) lock.findViewById(R.id.relay_dialog);
		linear_dialog = (LinearLayout) lock.findViewById(R.id.linear_dialog);
		dialog_btn1 = (Button) lock.findViewById(R.id.dialog_btn1);
		dialog_btn2 = (Button) lock.findViewById(R.id.dialog_btn2);
		dialog_btn1.setOnClickListener(this);
		dialog_btn2.setOnClickListener(this);
		linearbtn1 = (LinearLayout) lock.findViewById(R.id.linearbtn1);
		linearbtn2 = (LinearLayout) lock.findViewById(R.id.linearbtn2);
		linearbtn3 = (LinearLayout) lock.findViewById(R.id.linearbtn3);
		linearbtn4 = (LinearLayout) lock.findViewById(R.id.linearbtn4);
		linearbtn1.setOnClickListener(this);
		linearbtn2.setOnClickListener(this);
		linearbtn3.setOnClickListener(this);
		linearbtn4.setOnClickListener(this);
		
		linearbtn1.setOnTouchListener(mainviewOntouch);
		linearbtn2.setOnTouchListener(mainviewOntouch);
		linearbtn3.setOnTouchListener(mainviewOntouch);
		linearbtn4.setOnTouchListener(mainviewOntouch);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		TicketApplication.widthPixels = dm.widthPixels;
		TicketApplication.heightPixels = dm.heightPixels;
		System.out.println("宽：" + dm.widthPixels + "-高：" + dm.heightPixels);
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

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		if (v == linearbtn1) {// 验证记录
			i.setClass(this, VerificationRecordActivity.class);
			startActivity(i);
		} else if (v == linearbtn2) {// 二维码验证
			i.setClass(this, CaptureActivity.class);
			startActivity(i);
		} else if (v == linearbtn3) {// 订单信息验证
			i.setClass(this, ValidateFindActivity.class);
			startActivity(i);
		} else if (v == linearbtn4) {// 关机
			CreateAlertDialog(2, "请输入退出密码", this);
		} else if (v == dialog_btn1) {
			relay_dialog.setVisibility(View.GONE);
			linear_dialog.setVisibility(View.GONE);
		} else if (v == dialog_btn2) {
			EditText dialog_content = (EditText) lock
					.findViewById(R.id.dialog_tv);
			if (!dialog_content.getText().toString().equals("")) {
				relay_dialog.setVisibility(View.GONE);
				linear_dialog.setVisibility(View.GONE);
				new Thread(this).start();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	/**
	 * 提示框</br> title ：提示框标题</br> msg : 提示信息</br> conntext ：Activity</br>
	 * */
	public void CreateAlertDialog(int type, String msg, Context context) {
		TextView dialog_title = (TextView) lock.findViewById(R.id.dialog_title);
		LinearLayout linear_dialogtitle = (LinearLayout)lock.findViewById(R.id.linear_dialogtitle);
		EditText dialog_tv = (EditText) lock.findViewById(R.id.dialog_tv);
		if (type == 1) {
			dialog_tv.setBackground(null);
			dialog_tv.setEnabled(false);
			dialog_tv.setClickable(false);
			dialog_tv.setText(msg);
			dialog_btn1.setText("确定");
			dialog_btn2.setVisibility(View.GONE);
		} else {
			dialog_tv.setBackgroundResource(R.drawable.exitapp);
			dialog_tv.setEnabled(true);
			dialog_tv.setClickable(true);
			dialog_btn1.setText("取消");
			dialog_tv.setText("");
			linear_dialogtitle.setVisibility(View.GONE);
			dialog_btn2.setVisibility(View.VISIBLE);
		}
		relay_dialog.setVisibility(View.VISIBLE);
		relay_dialog.setOnClickListener(this);
		linear_dialog.setVisibility(View.VISIBLE);
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
				if (response.equals("1")) {
					finish();
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
				}else if (response.equals("-404")) {
					errormsg = "服务器处于维护状态，请稍后重试！";
				}else if (response.equals("-1000")) {
					errormsg = "请求超时，请稍后重试！";
				} else {
					errormsg = "请求失败，错误编号为"+response;
				}
				if (!errormsg.equals("")) {
					CreateAlertDialog(1, errormsg, MainActivity.this);
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
			PROGRESSMSG = "正在请求退出，请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);// 开启进度条
			EditText dialog_content = (EditText) lock
					.findViewById(R.id.dialog_tv);
			String response = RequestServerFromHttp.quit(dialog_content
					.getText().toString());// 登陆
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			msg.setData(b);
			runnablehandler.sendMessage(msg);

			handler.sendEmptyMessage(PROGRESSEND_HANDLER);
		} else {
			handler.sendEmptyMessage(NONETWORK_HANDLER);
		}
		clicked = false;
	}
	
	/**
	 * 页面逻辑处理
	 */
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NONETWORK_HANDLER:// 没有网络时给用户提示
				CreateAlertDialog(1, "请检查网络连接", MainActivity.this);
				break;
			case PROGRESSSTART_HANDLER:// 打开进度条
				relay_progressbar.setVisibility(View.VISIBLE);
				break;
			case PROGRESSEND_HANDLER:// 关闭进度条
				relay_progressbar.setVisibility(View.GONE);
				break;
			}
		}
	};

}
