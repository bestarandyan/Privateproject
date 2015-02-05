package com.piaoguanjia.accountManager;

import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LoginActivity extends MainFrameActivity {

	public static final int ID_BUTTON = 0x7f44725;// button
	public EditText et_username, et_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getScrollView();
		addCenterView(scrollView1);
		setBottomEnable(false);
		setTopEnable(false);
		initView("用户登录", "", "", "", "", false);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password.setText(AccountApplication.password);
		et_username.setText(AccountApplication.username);
	}

	/**
	 * 
	 * @param 登陆的视图
	 * @return
	 */

	public ScrollView getScrollView() {
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT,
				android.support.v4.view.ViewPager.LayoutParams.WRAP_CONTENT);
		linearLayout.setLayoutParams(params);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		View view = LayoutInflater.from(this)
				.inflate(R.layout.item_login, null);
		linearLayout.addView(view);

		Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.btn_corner_round);
		params.setMargins(0,
				(int) (2 * getResources().getDimension(R.dimen.marginsizeh)),
				0, 0);
		btn.setLayoutParams(params);
		btn.setText("登录");
		btn.setTextSize(23);
		btn.setTextColor(Color.WHITE);
		btn.setId(ID_BUTTON);
		btn.setOnClickListener(this);
		btn.setOnTouchListener(btnviewOntouch);
		// btn.setPadding((int)(getResources().getDimension(R.dimen.marginsizel)),
		// (int)(getResources().getDimension(R.dimen.marginsizel)),
		// (int)(getResources().getDimension(R.dimen.marginsizel)),
		// (int)(getResources().getDimension(R.dimen.marginsizel)));
		linearLayout.addView(btn);
		scrollView.addView(linearLayout);
		return scrollView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ID_BUTTON) {
			if (validate()) {
				new Thread(this).start();
			}
		}
		super.onClick(v);
	}

	@Override
	public boolean validate() {
		String username = et_username.getText().toString().trim();
		String password = et_password.getText().toString().trim();
		if (username.equals("")) {
			MessageBox.CreateAlertDialog("用户名不能为空", LoginActivity.this);
			return false;
		} else if (password.equals("")) {
			MessageBox.CreateAlertDialog("密码不能为空", LoginActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * 页面逻辑处理
	 */
	public Handler runnablehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RESPONSE_HANDLER:
				Intent i = new Intent();
				i.setClass(LoginActivity.this, MainActivity.class);
				startActivity(i);
				finish();

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
			PROGRESSMSG = "正在登陆，请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);// 开启进度条
			AccountApplication.username = et_username.getText().toString();
			AccountApplication.password = et_password.getText().toString();
			String response = RequestServerFromHttp.loginUser();// 登陆
//			response = "{\"userid\":\"1\",\"permissions\":\"1\"}";
			HandleData.handleLogin(response);
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			msg.setData(b);
			if (response.contains("userid")) {
				runnablehandler.sendMessage(msg);
			} else {
				handler.sendMessage(msg);
			}
			handler.sendEmptyMessage(PROGRESSEND_HANDLER);
		} else {
			handler.sendEmptyMessage(NONETWORK_HANDLER);
		}
		clicked = false;
	}

}
