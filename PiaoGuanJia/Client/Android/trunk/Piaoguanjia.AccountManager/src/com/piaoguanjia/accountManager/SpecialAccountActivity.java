package com.piaoguanjia.accountManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.piaoguanjia.accountManager.bean.AccountInfo;
import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.MD5;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class SpecialAccountActivity extends MainFrameActivity {

	public static final int ID_SCROLLVIEW1 = 0x7f44764;// 滚动视图1
	private LinearLayout btn_linear1, btn_linear2;// 短信，通知
	public ImageView imageview_gou1, imageview_gou2;
	public TextView productname;
	public EditText tv_username, tv_produceid, tv_money1, tv_money2, tv_money3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getScrollView();
		scrollView1.setId(ID_SCROLLVIEW1);
		addCenterView(scrollView1);
		setBottomEnable(false);
		setTopEnable(false);
		initView("增加专用账户", "", "", "", "", true);
	}

	/**
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getScrollView() {
		ScrollView scrollView = new ScrollView(this);
		View view = LayoutInflater.from(this).inflate(R.layout.listitem5, null);
		view.setOnTouchListener(this);
		btn_linear1 = (LinearLayout) view.findViewById(R.id.btn_linear1);
		btn_linear2 = (LinearLayout) view.findViewById(R.id.btn_linear2);
		imageview_gou1 = (ImageView) view.findViewById(R.id.imageview_gou1);
		imageview_gou2 = (ImageView) view.findViewById(R.id.imageview_gou2);
		btn_linear1.setOnClickListener(this);
		btn_linear2.setOnClickListener(this);
		tv_username = (EditText) view.findViewById(R.id.tv_username);
		tv_produceid = (EditText) view.findViewById(R.id.tv_produceid);
		tv_produceid.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					new Thread(seleprorunnable).start();
			}
		});
		tv_money1 = (EditText) view.findViewById(R.id.tv_money1);
		tv_money2 = (EditText) view.findViewById(R.id.tv_money2);
		tv_money3 = (EditText) view.findViewById(R.id.tv_money3);
		tv_money1.addTextChangedListener(simpleWatcher);
		tv_money2.addTextChangedListener(simpleWatcher);
		tv_money3.addTextChangedListener(simpleWatcher);
		productname = (TextView) view.findViewById(R.id.productname);
		view.findViewById(R.id.btn_submit).setOnClickListener(this);
		view.findViewById(R.id.btn_submit).setOnTouchListener(btnviewOntouch);
		scrollView.addView(view);
		return scrollView;
	}

	public TextWatcher simpleWatcher = new TextWatcher() {
		public void afterTextChanged(Editable edt) {
			String temp = edt.toString();
			int posDot = temp.indexOf(".");
			if (posDot <= 0)
				return;
			if (temp.length() - posDot - 1 > 2) {
				edt.delete(posDot + 3, posDot + 4);
			}
		}

		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
	};

	@Override
	public void onClick(View v) {
		if (v == btn_linear1) {
			imageview_gou2.setVisibility(View.INVISIBLE);
			imageview_gou1.setVisibility(View.VISIBLE);
		} else if (v == btn_linear2) {
			imageview_gou1.setVisibility(View.INVISIBLE);
			imageview_gou2.setVisibility(View.VISIBLE);
		} else if (v.getId() == R.id.btn_submit) {
			if (validate()) {
				new Thread(this).start();
			}
		}
		super.onClick(v);
	}

	@Override
	public boolean validate() {
		String username = tv_username.getText().toString();
		int chargeLimit = -1;
		int remindAmount = -1;
		int onlineChargeLimit = 0;
		try {
			chargeLimit = Integer.parseInt(tv_money1.getText().toString());
		} catch (Exception e) {
			chargeLimit = -2;
		}
		try {
			remindAmount = Integer.parseInt(tv_money2.getText().toString());
		} catch (Exception e) {
			remindAmount = -2;
		}
		try {
			onlineChargeLimit = Integer
					.parseInt(tv_money3.getText().toString());
		} catch (Exception e) {
			onlineChargeLimit = -2;
		}
		String productid = tv_produceid.getText().toString();
		int remindType = 0;
		if (imageview_gou1.isShown()) {
			remindType = 1;
		} else if (imageview_gou2.isShown()) {
			remindType = 2;
		}
		if (username.equals("")) {
			MessageBox.CreateAlertDialog("用户名不能为空！", this);
			return false;
		} else if (productid.equals("")) {
			MessageBox.CreateAlertDialog("产品编号不能为空！", this);
			return false;
		} else if (chargeLimit < 0) {
			MessageBox.CreateAlertDialog("请输入正确的充值限制金额！", this);
			return false;
		} else if (remindAmount < 0) {
			MessageBox.CreateAlertDialog("请输入正确的提醒金额！", this);
			return false;
		} else if (onlineChargeLimit < 0
				&& !tv_money3.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog("请输入正确的直充限制金额！", this);
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
				linearInclude.removeAllViews();
				ScrollView scrollView1 = getScrollView();
				scrollView1.setId(ID_SCROLLVIEW1);
				addCenterView(scrollView1);
				MessageBox.CreateAlertDialog("添加充值成功！",
						SpecialAccountActivity.this);
				break;
			case 0x11:
				Bundle b = msg.getData();
				String response = b.getString(KEY_RESPONSE);
				productname.setText("产品名称：" + response);
				productname.setVisibility(View.VISIBLE);
				break;
			case 0x12:
				productname.setVisibility(View.GONE);
				break;
			}
		}
	};

	public class productinfo {
		private String productName;

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

	}

	public Runnable seleprorunnable = new Runnable() {

		@Override
		public void run() {
			String response = RequestServerFromHttp.productname(tv_produceid
					.getText().toString());
			Gson gson = new Gson();// 创建Gson对象
			productinfo bean = null;
			try {
				bean = gson.fromJson(response, productinfo.class);// 解析json对象
			} catch (Exception e) {
			}
			if (bean == null || bean.getProductName() == null
					|| bean.getProductName().equals("")) {
				runnablehandler.sendEmptyMessage(0x12);
				return;
			}
			Message msg = new Message();
			msg.what = 0x11;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, bean.getProductName());
			msg.setData(b);
			runnablehandler.sendMessage(msg);
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
			PROGRESSMSG = "正在添加专用账户，请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);// 开启进度条
			String username = tv_username.getText().toString();
			int chargeLimit = -1;
			int remindAmount = -1;
			int onlineChargeLimit = 0;
			try {
				chargeLimit = Integer.parseInt(tv_money1.getText().toString());
			} catch (Exception e) {
				chargeLimit = -2;
			}
			try {
				remindAmount = Integer.parseInt(tv_money2.getText().toString());
			} catch (Exception e) {
				remindAmount = -2;
			}
			try {
				onlineChargeLimit = Integer.parseInt(tv_money3.getText()
						.toString());
			} catch (Exception e) {
				onlineChargeLimit = -2;
			}
			String productid = tv_produceid.getText().toString();
			int remindType = 0;
			if (imageview_gou1.isShown()) {
				remindType = 1;
			} else if (imageview_gou2.isShown()) {
				remindType = 2;
			}

			String response = RequestServerFromHttp.addAccount(username,
					productid, chargeLimit + "", remindAmount + "",
					onlineChargeLimit, remindType + "");
			HandleData.handleAccountInfo(response);
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			msg.setData(b);
			int accountid = 0;
			try {
				accountid = Integer.parseInt(response);
			} catch (Exception e) {
			}
			if (accountid > 0) {
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
