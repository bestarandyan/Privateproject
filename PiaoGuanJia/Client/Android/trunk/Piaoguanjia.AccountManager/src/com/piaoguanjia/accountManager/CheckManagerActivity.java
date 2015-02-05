package com.piaoguanjia.accountManager;

import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CheckManagerActivity extends MainFrameActivity {

	public static final int ID_SCROLLVIEW1 = 0x7f44764;// 滚动视图1
	private LinearLayout btn_linear1, btn_linear2, btn_linear3;//

	public static final int TYPE_CHOGNZHI = 0;// 充值
	public static final int TYPE_ZHUANYONGZHANGHU = 1;// 专用账户
	public static final int TYPE_EDU = 2;// 额度

	public TextView message_person1, message_person2, message_person3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getScrollView();
		scrollView1.setId(ID_SCROLLVIEW1);
		addCenterView(scrollView1);
		new Thread(this).start();
		setBottomEnable(false);
		setTopEnable(false);
		initView("审核", "", "", "", "", true);
		btn_linear1 = (LinearLayout) findViewById(R.id.btn_linear1);
		btn_linear2 = (LinearLayout) findViewById(R.id.btn_linear2);
		btn_linear3 = (LinearLayout) findViewById(R.id.btn_linear3);
		message_person1 = (TextView) findViewById(R.id.message_person1);
		message_person2 = (TextView) findViewById(R.id.message_person2);
		message_person3 = (TextView) findViewById(R.id.message_person3);
		btn_linear1.setOnClickListener(this);
		btn_linear2.setOnClickListener(this);
		btn_linear3.setOnClickListener(this);
		updatePendingCount();
	}

	@Override
	protected void onResume() {
		new Thread(this).start();
		super.onResume();
	}

	/**
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getScrollView() {
		ScrollView scrollView = new ScrollView(this);
		View view = LayoutInflater.from(this).inflate(R.layout.listitem4, null);
		scrollView.addView(view);
		return scrollView;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		if (v == btn_linear1) {
			if((AccountApplication.permissions&4)!=4){
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("您没有该权限")
						.setPositiveButton("知道了",null);
				Dialog dialog = callDailog.create();
				dialog.show();
			}else{
				i.putExtra("type", TYPE_CHOGNZHI);
				i.setClass(this, CheckManagerListActivity.class);
				startActivity(i);
			}
		} else if (v == btn_linear2) {
			if((AccountApplication.permissions&32)!=32){
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("您没有该权限")
						.setPositiveButton("知道了",null);
				Dialog dialog = callDailog.create();
				dialog.show();
			}else{
			i.putExtra("type", TYPE_ZHUANYONGZHANGHU);
			i.setClass(this, CheckManagerListActivity.class);
			startActivity(i);
			}
		} else if (v == btn_linear3) {
			if((AccountApplication.permissions&256)!=256){
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("您没有该权限")
						.setPositiveButton("知道了",null);
				Dialog dialog = callDailog.create();
				dialog.show();
			}else{
			i.putExtra("type", TYPE_EDU);
			i.setClass(this, CheckManagerListActivity.class);
			startActivity(i);
			}
		}
		super.onClick(v);
	}
	
	/**
	 * 
	 */
	public void updatePendingCount(){
		HandleData.selectNum();
		int num = 0;
		num = AccountApplication.chargenum;
		if (num <= 0) {
			message_person1.setVisibility(View.GONE);
		} else {
			message_person1.setVisibility(View.VISIBLE);
			message_person1.setText(num + "");
		}
		num = AccountApplication.accountnum;
		if (num <= 0) {
			message_person2.setVisibility(View.GONE);
		} else {
			message_person2.setVisibility(View.VISIBLE);
			message_person2.setText(num + "");
		}
		num = AccountApplication.creditnum;
		if (num <= 0) {
			message_person3.setVisibility(View.GONE);
		} else {
			message_person3.setVisibility(View.VISIBLE);
			message_person3.setText(num + "");
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
				if (response.startsWith("{") && response.endsWith("}")) {
					updatePendingCount();
					break;
				} else if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-37")) {
					errormsg = "没有权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了充值客户端登录，如有问题，请联系票管家！";
				}
				if (!errormsg.equals("")) {
					MessageBox.CreateAlertDialog(errormsg,
							CheckManagerActivity.this);
				}
				break;
			case NONETWORK_HANDLER:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog("请检查网络是否连接！",
						CheckManagerActivity.this);
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
			String response = "";// 服务器返回值
			response = RequestServerFromHttp.pendingCount();// 数字
			HandleData.handlePendingCount(response);
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

}
