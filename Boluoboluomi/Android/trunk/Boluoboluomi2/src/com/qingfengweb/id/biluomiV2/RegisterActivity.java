package com.qingfengweb.id.biluomiV2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.UserInfo;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.MessageBox;
import com.qingfengweb.util.StringUtils;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;

public class RegisterActivity extends BaseActivity {
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private Button backBtn, registerBtn;
	private EditText userName, passWord, againPassWord;
	private RadioGroup radioGroup;
	private RadioButton man, female;
	private ProgressDialog progressdialog;
	private EditText inputMessageEt;
	private Button getMessageBtn;
	private String Verification_code = "";
	private TextView storeTv;//门店的名称
	TableRow codeLayout;//验证码布局
	private DBHelper dbHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_register);
		dbHelper = DBHelper.getInstance(this);
		findView();
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		userName = (EditText) findViewById(R.id.unsername);
		passWord = (EditText) findViewById(R.id.password);
		againPassWord = (EditText) findViewById(R.id.againPassword);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		man = (RadioButton) findViewById(R.id.man);
		female = (RadioButton) findViewById(R.id.female);
		backBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		inputMessageEt = (EditText) findViewById(R.id.inputMessageEt);
		getMessageBtn = (Button) findViewById(R.id.getMessageBtn);
		getMessageBtn.setOnClickListener(this);
		storeTv = (TextView) findViewById(R.id.storeTextView);
		storeTv.setText(UserBeanInfo.getInstant().getCurrentStore());
		codeLayout = (TableRow) findViewById(R.id.codeLayout);
		String isShowCodeLayout = "";
		if( UserBeanInfo.storeDetail!=null &&  UserBeanInfo.storeDetail.size()>0){
			isShowCodeLayout = UserBeanInfo.storeDetail.get(0).get("validate_required").toString();
		}
		if(isShowCodeLayout.equals("0")){//不需要验证码
			codeLayout.setVisibility(View.GONE);
		}else if(isShowCodeLayout.equals("1")){//需要验证码
			codeLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == backBtn) {
			handler.sendEmptyMessage(2);// 从注册界面跳转到登录界面
		} else if (v == registerBtn) {//注册
			if (textValidate()) {
				new Thread(registerRunnable).start();// 注册验证
			}
		} else if(v==getMessageBtn){//获取验证码
			if (textValidate1()) {
				new Thread(getCodeRunnable).start();//验证
			}
		}
	}


	/**
	 * author by Ring 注册前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (userName.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.username_null),
					RegisterActivity.this);
			return false;
		} else if (userName.getText().toString().length()>11) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.username_count),
					RegisterActivity.this);
			return false;
		} else if (passWord.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.password_null),
					RegisterActivity.this);
			return false;
		} else if (passWord.getText().toString().length()>10) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.password_count),
					RegisterActivity.this);
			return false;
		} else if (!againPassWord.getText().toString().equals(passWord.getText().toString())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.password_confirm),
					RegisterActivity.this);
			return false;
		} else if (!StringUtils.isCellphone(userName.getText().toString()
				.trim())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.phone_error),
					RegisterActivity.this);
			return false;
		} else if(codeLayout.getVisibility() == View.VISIBLE && inputMessageEt.getText().toString().trim().equals("")){
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.verification_null),
					RegisterActivity.this);
			return false;
		} else if(codeLayout.getVisibility() == View.VISIBLE && !inputMessageEt.getText().toString().trim().equals("")
				&&!Verification_code.equals("")
				&&!Verification_code.equals(inputMessageEt.getText().toString().trim())){
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.verification_error),
					RegisterActivity.this);
			return false;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			handler.sendEmptyMessage(2);// 从注册界面跳转到登录界面
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * author by 刘星星 注册用户线程
	 */
	public Runnable registerRunnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {//防止重复操作
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(RegisterActivity.this)) {
				handler.sendEmptyMessage(5);
				String username = userName.getText().toString().trim();
				String password = passWord.getText().toString().trim();
				String code = inputMessageEt.getText().toString().trim();
				String msgStr = RequestServerFromHttp.registerUser(password, "", username, "",man.isChecked()?"1":"2", "", code);
				handler.sendEmptyMessage(6);//关闭进度条
				if(msgStr.equals("404")){//访问服务器失败
					handler.sendEmptyMessage(3);// 注册失败
				}else if(msgStr.startsWith("-204")){//注册失败
					handler.sendEmptyMessage(3);//注册失败
				}else if(msgStr.startsWith("-203")){//用户已存在
					handler.sendEmptyMessage(9);//用户已存在
				}else if(msgStr.startsWith("-206")){//验证码输入错误
					handler.sendEmptyMessage(10);//验证码输入错误
				}else{//获取到有效的数据  
					JsonData.jsonUserData(msgStr, dbHelper.open(),password,"1");//注册成功 不保存密码且不自动登陆
					handler.sendEmptyMessage(1);// 注册成功
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

			click_limit = true;
		}
	};
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 从注册界面跳转到注册成功界面
				int getValue1 = getIntent().getIntExtra("activityType",0);
				if(getValue1==0){
					i.setClass(RegisterActivity.this, SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					RegisterActivity.this.startActivity(i);
					finish();
				}else if(getValue1 ==1){//从样照欣赏的推荐跳入来的
					i.setClass(RegisterActivity.this, SuccessRegisterActivity.class);
					i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					RegisterActivity.this.startActivity(i);
					finish();
				}else if(getValue1 == 2){//从主页的我的相册跳入的
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					startActivity(i);
					finish();
				}else if(getValue1 == 3){//
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					i.putExtra("map", getIntent().getSerializableExtra("map"));
					startActivity(i);
					finish();
				}else if(getValue1 == 4){//查件系统
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					startActivity(i);
					finish();
				}else if(getValue1 == 5){//查件系统
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					i.putExtra("integralMap", getIntent().getSerializableExtra("integralMap"));
					startActivity(i);
					finish();
				}else if(getValue1 == 6){//推荐好友
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					startActivity(i);
					finish();
				}else if(getValue1 == 7){//微信分享
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					startActivity(i);
					finish();
				}else if(getValue1 == 8){//我要点评
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					startActivity(i);
					finish();
				}else if(getValue1 == 9){//我要定制
					i.setClass(RegisterActivity.this,SuccessRegisterActivity.class);
					i.putExtra("activityType", getValue1);
					i.putExtra("username", userName.getText().toString());
					startActivity(i);
					finish();
				}
				break;
			case 2:// 从注册界面跳转到登录界面
				int getValue11 = getIntent().getIntExtra("activityType",0);
				if(getValue11==0){
					i.setClass(RegisterActivity.this, LoginActivity.class);
					i.putExtra("activityType", getValue11);
					RegisterActivity.this.startActivity(i);
					finish();
				}else if(getValue11 ==1){//从样照欣赏的推荐跳入来的
					i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
					i.setClass(RegisterActivity.this, LoginActivity.class);
					i.putExtra("activityType", getValue11);
					RegisterActivity.this.startActivity(i);
					finish();
				}else if(getValue11 == 2){//从主页的我的相册跳入的
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("activityType", getValue11);
					startActivity(i);
					finish();
				}else if(getValue11 == 3){//从主页的我的相册跳入的
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("activityType", getValue11);
					i.putExtra("map", getIntent().getSerializableExtra("map"));
					startActivity(i);
					finish();
				}else if(getValue11 == 4){//从查件系统跳入的
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("activityType", getValue11);
					startActivity(i);
					finish();
				}else if(getValue11 == 5){//积分兑换
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("integralMap", getIntent().getSerializableExtra("integralMap"));
					i.putExtra("activityType", getValue11);
					startActivity(i);
					finish();
				}else if(getValue11 == 6){//好友推荐
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("activityType", getValue11);
					startActivity(i);
					finish();
				}else if(getValue11 == 7){//微信分享
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("activityType", getValue11);
					startActivity(i);
					finish();
				}else if(getValue11 == 8){//我要点评
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("activityType", getValue11);
					startActivity(i);
					finish();
				}else if(getValue11 == 9){//我要定制
					i.setClass(RegisterActivity.this,LoginActivity.class);
					i.putExtra("activityType", getValue11);
					startActivity(i);
					finish();
				}
				break;
			case 3:// 注册失败给用户提示
				String message = "";
					message = getResources().getString(R.string.register_error);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),message,
						RegisterActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.error_net),
						RegisterActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(RegisterActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_register));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// 获取验证码成功
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.verifice_success),
						RegisterActivity.this);
				break;
			case 8:// 获取验证码失败
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.verifice_falise),
						RegisterActivity.this);
				break;
			case 9:
				message = getResources().getString(R.string.register_error_exist);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),message,
						RegisterActivity.this);
				break;
			case 10:
				message = "验证码输入有误，请核对后再输入！";
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),message,
						RegisterActivity.this);
				break;
			}
		};
	};

	/**
	 * author by Ring 处理验证码操作
	 */
	public Runnable getCodeRunnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {//防止重复操作
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(RegisterActivity.this)) {//检查网络连接是否正常
				handler.sendEmptyMessage(5);//开启进度条
				String msgStr = RequestServerFromHttp.getMsgCode(userName.getText().toString().trim());//访问服务器获取验证码
				handler.sendEmptyMessage(6);//关闭进度条
				if(msgStr.equals("404")){//访问服务器失败
					handler.sendEmptyMessage(8);// 验证获取失败
				}else if(JsonData.isNoData(msgStr)){//无数据或参数错误
					handler.sendEmptyMessage(8);// 验证获取失败
				}else{//获取到有效的数据
					handler.sendEmptyMessage(7);// 验证获取成功
					Verification_code = msgStr;
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

			click_limit = true;
		}
	};
	
	/**
	 * author by Ring 注册前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate1() {
		if (userName.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.username_null),
					RegisterActivity.this);
			return false;
		} else if (userName.getText().toString().length()>11) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.username_count),
					RegisterActivity.this);
			return false;
		} else if (!StringUtils.isCellphone(userName.getText().toString()
				.trim())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.phone_error),
					RegisterActivity.this);
			return false;
		}  
		return true;
	}
}
