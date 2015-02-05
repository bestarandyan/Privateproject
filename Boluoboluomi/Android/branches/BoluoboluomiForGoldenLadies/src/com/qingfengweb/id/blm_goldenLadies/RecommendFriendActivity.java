package com.qingfengweb.id.blm_goldenLadies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.util.MessageBox;
import com.qingfengweb.util.StringUtils;
import com.qingfengweb.util.Util;

public class RecommendFriendActivity extends BaseActivity {
	private Button backBtn, submitBtn;
	private Button tabBtn1, tabBtn2, tabBtn3, tabBtn4, tabBtn5;
	private LinearLayout tab1,tab2,tab3,tab4,tab5;
	private EditText name, age, qq, phone, address;
	private TextView topText;
	private LinearLayout tuijian1, tuijian2;// 用来隐藏和显示控件 分别代表 推荐界面 和继续推荐页面
	private boolean flag = true;// 标记当前页面为推荐页面或者继续推荐页面 ， true代表推荐页面
								// false代表继续推荐页面
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_recommendfriend);
		findView();
		findBottomBtn();
	}

	private void findView() {
		tuijian1 = (LinearLayout) findViewById(R.id.tuijian1);
		tuijian2 = (LinearLayout) findViewById(R.id.tuijian2);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		name = (EditText) findViewById(R.id.name);
		age = (EditText) findViewById(R.id.age);
		qq = (EditText) findViewById(R.id.qq);
		phone = (EditText) findViewById(R.id.phone);
		address = (EditText) findViewById(R.id.address);
		topText = (TextView) findViewById(R.id.text);
		setTextContent();
		submitBtn.setOnClickListener(this);

	}

	private void setTextContent() {
		topText.setText("\t填取您近期需要咨询婚纱摄影服务的亲朋好友信息，我司将给予最专业的顾问服务，且系统将赠与您100积分，如果您"
				+ "所介绍的新人在您的推荐下成为我司新客户，我司将奖励您相应的积分。\n\t若您填写的信息不属实达三次以内，系统将自动扣除20积分，"
				+ "五次以上直接注销会员资格。");
	}

	private void findBottomBtn() {
		backBtn = (Button) findViewById(R.id.backBtn);
		tab1 = (LinearLayout) findViewById(R.id.tab1);
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);
		tab4 = (LinearLayout) findViewById(R.id.tab4);
		tab5 = (LinearLayout) findViewById(R.id.tab5);
		tabBtn1 = (Button) findViewById(R.id.tab1Btn);
		tabBtn2 = (Button) findViewById(R.id.tab2Btn);
		tabBtn3 = (Button) findViewById(R.id.tab3Btn);
		tabBtn4 = (Button) findViewById(R.id.tab4Btn);
		tabBtn5 = (Button) findViewById(R.id.tab5Btn);
		backBtn.setOnClickListener(this);
		tab1.setOnClickListener(this);
		tab2.setOnClickListener(this);
		tab3.setOnClickListener(this);
		tab4.setOnClickListener(this);
		tab5.setOnClickListener(this);
		tabBtn1.setBackgroundResource(R.drawable.mall_ico01);
		tabBtn2.setBackgroundResource(R.drawable.mall_ico02_on);
		tabBtn3.setBackgroundResource(R.drawable.mall_ico03);
		tabBtn4.setBackgroundResource(R.drawable.mall_ico04);
		tabBtn5.setBackgroundResource(R.drawable.mall_ico05);
	}

	@Override
	public void onClick(View v) {
		if (click_limit) {
			click_limit = false;
		} else {
			return;
		}
		if (v == submitBtn) {//推荐好友
			if (flag) {//推荐
				if (textValidate()) {
					 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
			           imm.hideSoftInputFromWindow(address.getWindowToken(),0);
					new Thread(recommendFriendRunnable).start();
				}else{
					click_limit = true;
				}
			} else {//推荐成功后的继续推荐页面
				name.setText("");
				age.setText("");
				qq.setText("");
				phone.setText("");
				address.setText("");
				tuijian2.setVisibility(View.GONE);
				tuijian1.setVisibility(View.VISIBLE);
				submitBtn.setText("确定提交");
				flag = true;
				click_limit = true;
			}
		} else if (v == tab1) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab2) {
			click_limit = true;
//			Intent i = new Intent(this, RecommendFriendActivity.class);
//			startActivity(i);
//			finish();
		} else if (v == tab3) {
			Intent i = new Intent(this, MyIntegralActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab4) {
			Intent i = new Intent(this, IntegralRuleActivity.class);
			startActivity(i);
			finish();
		} else if (v == tab5) {
			Intent i = new Intent(this, EcshopActiveActivity.class);
			startActivity(i);
			finish();
		} else if (v == backBtn) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		super.onClick(v);
	}
	
	@Override
	protected void onDestroy() {
		click_limit = true;
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(this, IntegralStoreMainActivity.class);
			startActivity(i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * author by Ring 提交前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (name.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.recommend_name_null),
					RecommendFriendActivity.this);
			return false;
		} else if (!StringUtils.isChinese(name.getText().toString())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.recommend_name_error),
					RecommendFriendActivity.this);
			return false;
		} else if (age.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.age_null),
					RecommendFriendActivity.this);
			return false;
		} else if (qq.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.qq_null),
					RecommendFriendActivity.this);
			return false;
		} else if (phone.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.phone_null),
					RecommendFriendActivity.this);
			return false;
		}/*
		 * else if (phone.getText().toString().trim().equals("")) {
		 * MessageBox.CreateAlertDialog(
		 * getResources().getString(R.string.prompt), getResources()
		 * .getString(R.string.value1_null), RecommendFriendActivity.this);
		 * return false; }
		 */else if (address.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.address_null),
					RecommendFriendActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * @author 刘星星
	 * @createDate 2013/9/17
	 * 推荐好友
	 */
	public Runnable recommendFriendRunnable = new Runnable() {

		@Override
		public void run() {
			if(Util.isNetworkConnected(RecommendFriendActivity.this)){
				handler.sendEmptyMessage(5);//打开进度条
				String msgStr = RequestServerFromHttp.recommendFriend(name.getText().toString().trim(), age.getText().toString().trim(), 
						qq.getText().toString().trim(), phone.getText().toString().trim(), address.getText().toString().trim());
				handler.sendEmptyMessage(6);//打开进度条
				if(msgStr.equals("404")){//访问服务器失败
					handler.sendEmptyMessage(7);
				}else if(msgStr.startsWith("0")){//推荐成功
					handler.sendEmptyMessage(2);//提示用户推荐成功
					handler.sendEmptyMessage(1);//跳转页面
				}else if(msgStr.startsWith("-")){//推荐失败
					handler.sendEmptyMessage(3);
				}
			}else{
				handler.sendEmptyMessage(4);
			}
			
		}
	};
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 跳转到推荐成功界面
				tuijian1.setVisibility(View.GONE);
				tuijian2.setVisibility(View.VISIBLE);
				submitBtn.setText("继续推荐");
				flag = false;
				click_limit = true;
				break;
			 case 2:// 推荐成功给用户提示
				 MessageBox.CreateAlertDialog(
							getResources().getString(R.string.prompt),
							getResources()
									.getString(R.string.recommend_success_info),
							RecommendFriendActivity.this);
				 click_limit = true;
			 break;
			case 3:// 提交失败给用户提示
				String errormsg = "";
					errormsg = getResources().getString(R.string.recommend_error_check);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						errormsg,
						RecommendFriendActivity.this);
				click_limit = true;
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.recommend_error_net),
						RecommendFriendActivity.this);
				click_limit = true;
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						RecommendFriendActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_sumbit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// 访问服务器失败
				String errormsg1 = "";
					errormsg1 = getResources().getString(R.string.progress_timeout);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						errormsg1,
						RecommendFriendActivity.this);
				click_limit = true;
				break;
			}
		};
	};

}
