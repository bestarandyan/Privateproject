package com.piaoguanjia.accountManager;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.piaoguanjia.accountManager.SpecialAccountActivity.productinfo;
import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;

import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;

public class LimitAccountActivity extends MainFrameActivity {

	public int usertype = 1;// 1 总帐户，2专用账户
	public int rechargetype = 1;// 有凭证，2无凭证

	public static final int ID_IMAGEVIEW = 0x7f44763;// 凭证图片id
	public static final int ID_SCROLLVIEW1 = 0x7f44764;// 滚动视图1
	public static final int ID_SCROLLVIEW2 = 0x7f44765;// 滚动视图2
	private LinearLayout btn_linear1, btn_linear2;// 总账户，专用账户
	public ImageView imageview_gou1, imageview_gou2;

	public EditText tv_username, tv_produceid, tv_money, tv_reason;
	public TextView productname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getScrollView();
		scrollView1.setId(ID_SCROLLVIEW1);
		addCenterView(scrollView1);

		setBottomEnable(false);
		setTopEnable(false);
		initView("增加额度", "", "", "", "", true);
	}


	/**
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getScrollView() {
		ScrollView scrollView = new ScrollView(this);
		View view = LayoutInflater.from(this).inflate(R.layout.listitem6, null);
		view.setOnTouchListener(this);
		btn_linear1 = (LinearLayout) view.findViewById(R.id.btn_linear1);
		btn_linear2 = (LinearLayout) view.findViewById(R.id.btn_linear2);
		imageview_gou1 = (ImageView) view.findViewById(R.id.imageview_gou1);
		imageview_gou2 = (ImageView) view.findViewById(R.id.imageview_gou2);
		btn_linear1.setOnClickListener(this);
		btn_linear2.setOnClickListener(this);
		tv_username = (EditText) view.findViewById(R.id.tv_username);
		tv_produceid = (EditText) view.findViewById(R.id.tv_produceid);
		productname = (TextView) view.findViewById(R.id.productname);
		tv_produceid.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					new Thread(seleprorunnable).start();
			}
		});
		tv_reason = (EditText) view.findViewById(R.id.tv_reason);
		tv_money = (EditText) view.findViewById(R.id.tv_money);
		tv_money.addTextChangedListener(new TextWatcher()
		  {
		      public void afterTextChanged(Editable edt)
		      {
		          String temp = edt.toString();
		          int posDot = temp.indexOf(".");
		          if (posDot <= 0) return;
		          if (temp.length() - posDot - 1 > 2)
		          {
		              edt.delete(posDot + 3, posDot + 4);
		          }
		      }

		      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		      public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
		  });
		view.findViewById(R.id.btn_submit).setOnTouchListener(btnviewOntouch);
		view.findViewById(R.id.btn_submit).setOnClickListener(this);
		scrollView.addView(view);
		return scrollView;
	}

	@Override
	public void onClick(View v) {
		if (v == btn_linear1) {
			tv_produceid.setVisibility(View.GONE);
			productname.setVisibility(View.GONE);
			imageview_gou1.setVisibility(View.VISIBLE);
			imageview_gou2.setVisibility(View.INVISIBLE);
		} else if (v == btn_linear2) {
			tv_produceid.setVisibility(View.VISIBLE);
			tv_produceid.setText("");
//			productname.setVisibility(View.VISIBLE);
			imageview_gou2.setVisibility(View.VISIBLE);
			imageview_gou1.setVisibility(View.INVISIBLE);
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
		String reason = tv_reason.getText().toString();
		int creditLimit = -1;
		try {
			creditLimit = Integer.parseInt(tv_money.getText().toString());
		} catch (Exception e) {
			creditLimit = -2;
		}
		String productid = tv_produceid.getText().toString();
		int accountType = 0;
		if (imageview_gou1.isShown()) {
			accountType = 1;
		} else if (imageview_gou2.isShown()) {
			accountType = 2;
		}
		if (username.equals("")) {
			MessageBox.CreateAlertDialog("用户名不能为空！", this);
			return false;
		} else if (productid.equals("")&&accountType == 2) {
			MessageBox.CreateAlertDialog("产品编号不能为空！", this);
			return false;
		} else if (creditLimit < 0) {
			MessageBox.CreateAlertDialog("请输入正确的信用额度！", this);
			return false;
		} else if (reason.equals("")) {
			MessageBox.CreateAlertDialog("添加原因不能为空！", this);
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
				MessageBox.CreateAlertDialog("添加额度成功！",
						LimitAccountActivity.this);
				break;
			case 0x11:
				Bundle b = msg.getData();
				String response = b.getString(KEY_RESPONSE);
				productname.setText("产品名称："+response);
				productname.setVisibility(View.VISIBLE);
				break;
			case 0x12:
				productname.setVisibility(View.GONE);
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
			PROGRESSMSG = "正在添加专用账户，请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);// 开启进度条
			String username = tv_username.getText().toString();
			String reason = tv_reason.getText().toString();
			int creditLimit = -1;
			try {
				creditLimit = Integer.parseInt(tv_money.getText().toString());
			} catch (Exception e) {
				creditLimit = -2;
			}
			String productid = tv_produceid.getText().toString();
			int accountType = 0;
			if (imageview_gou1.isShown()) {
				accountType = 1;
			} else if (imageview_gou2.isShown()) {
				accountType = 2;
			}

			String response = RequestServerFromHttp.addCredit(username,
					accountType, creditLimit, reason, productid);
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
}
