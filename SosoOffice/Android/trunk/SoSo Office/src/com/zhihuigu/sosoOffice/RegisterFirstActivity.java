package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.CityListActivity.MyTextWatcher;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFirstActivity extends BaseActivity {
	private Button backBtn, submitBtn;
	private EditText userNameEt, passwordEt, passwordAgainEt, emailEt, phoneEt;
	private SoSoUploadData uploaddata;// 注册请求对象
	private SoSoUploadData uploaddata1;// 用户名唯一性验证请求对象
	private SoSoUploadData uploaddata2;// 电话唯一性验证请求对象
	private SoSoUploadData uploaddata3;// 邮箱唯一性验证请求对象

	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止

	private TextView userNameCheck, passCheck, passAgainCheck, emailCheck,
			phoneCheck;// 注册时的字段验证
	private LinearLayout uncLayout, pcLayout, pacLayout, ecLayout,
			phoneCheckLayout;// 注册验证不通过时出现的验证层

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_registerfirst);
		findView();// 控件初始化
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		userNameEt = (EditText) findViewById(R.id.userNameEt);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		passwordAgainEt = (EditText) findViewById(R.id.passwordAgainEt);
		emailEt = (EditText) findViewById(R.id.emailEt);
		phoneEt = (EditText) findViewById(R.id.phoneEt);
		backBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		userNameEt.setOnFocusChangeListener(textfocuschange);
		phoneEt.setOnFocusChangeListener(textfocuschange);
		emailEt.setOnFocusChangeListener(textfocuschange);
		passwordEt.setOnFocusChangeListener(textfocuschange);
		passwordAgainEt.setOnFocusChangeListener(textfocuschange);
		emailEt.setOnFocusChangeListener(textfocuschange);
		phoneEt.setOnFocusChangeListener(textfocuschange);
		emailEt.addTextChangedListener(new MyTextWatcher());
		phoneEt.addTextChangedListener(new MyTextWatcher());
		userNameCheck = (TextView) findViewById(R.id.userNameCheck);
		passCheck = (TextView) findViewById(R.id.passCheck);
		passAgainCheck = (TextView) findViewById(R.id.passAgainCheck);
		emailCheck = (TextView) findViewById(R.id.emailCheck);
		phoneCheck = (TextView) findViewById(R.id.phoneCheck);
		uncLayout = (LinearLayout) findViewById(R.id.userNameCheckLayout);
		pcLayout = (LinearLayout) findViewById(R.id.passCheckLayout);
		pacLayout = (LinearLayout) findViewById(R.id.passAgainCheckLayout);
		ecLayout = (LinearLayout) findViewById(R.id.emailCheckLayout);
		phoneCheckLayout = (LinearLayout) findViewById(R.id.phoneCheckLayout);
	}
	
	public void focusChangeFun(){
		Message msg = new Message();
		Bundle b = new Bundle();
		if (userNameEt.getText().toString().trim().equals("")) {
			b.clear();
			b.putBoolean("isshow", true);
			b.putString("msg",
					getResources().getString(R.string.username_null));
			msg.setData(b);
			msg.what = 7;
			handler.sendMessage(msg);
		} else if ((userNameEt.getText().toString().length() < 6 || userNameEt
						.getText().toString().length() > 15)) {
			b.clear();
			b.putBoolean("isshow", true);
			b.putString("msg",
					getResources().getString(R.string.username_count));
			msg.setData(b);
			msg.what = 7;
			handler.sendMessage(msg);
		} else if (!StringUtils.checkusername(userNameEt.getText()
						.toString().trim())) {
			b.clear();
			b.putBoolean("isshow", true);
			b.putString("msg",
					getResources().getString(R.string.username_check));
			msg.setData(b);
			msg.what = 7;
			handler.sendMessage(msg);
		} else if (!userNameEt.getText().toString().trim().equals("")
				) {
			uncLayout.setVisibility(View.GONE);
//			new Thread(runnable1).start();
		} else{
			uncLayout.setVisibility(View.GONE);
		}
		if (passwordEt.getText().toString().trim().equals("")) {
			b.clear();
			b.putBoolean("isshow", true);
			b.putString("msg",
					getResources().getString(R.string.password_null));
			msg.setData(b);
			msg.what = 8;
			handler.sendMessage(msg);
		} else if (passwordEt.getText().toString().length() < 6
				|| passwordEt.getText().toString().length() > 15) {
			b.clear();
			b.putBoolean("isshow", true);
			b.putString("msg",
					getResources().getString(R.string.password_count));
			msg.setData(b);
			msg.what = 8;
			handler.sendMessage(msg);
		} else {
			pcLayout.setVisibility(View.GONE);
		}
		if (!passwordAgainEt.getText().toString()
						.equals(passwordEt.getText().toString())) {
			b.clear();
			b.putBoolean("isshow", true);
			b.putString("msg",
					getResources().getString(R.string.password_confirm));
			msg.setData(b);
			msg.what = 9;
			handler.sendMessage(msg);
		} else {
			pacLayout.setVisibility(View.GONE);
		}

		if ((phoneEt.getText().toString().trim().equals("")
				|| !StringUtils.isCellphone(phoneEt.getText().toString()
						.trim()))) {
			String info = "";
			if(emailEt.getText().toString().trim().equals("")&& phoneEt.getText().toString().trim().equals("")){
				info = getResources().getString(R.string.telephone_null);
			}else if(!phoneEt.getText().toString().trim().equals("")&&!StringUtils.isCellphone(phoneEt.getText().toString()
					.trim())){
				info = getResources().getString(R.string.phone_error);
			}else{
				return;
			}
			b.clear();
			b.putBoolean("isshow", true);
			b.putString("msg",info);
			msg.setData(b);
			msg.what = 10;
			handler.sendMessage(msg);
		} else if (!phoneEt.getText().toString().trim().equals("")
				) {
			phoneCheckLayout.setVisibility(View.GONE);
//			new Thread(runnable2).start();
		} else{
			phoneCheckLayout.setVisibility(View.GONE);
		}
		
		if ((emailEt.getText().toString().trim().equals("")
				|| !StringUtils
						.isEmail(emailEt.getText().toString().trim()))) {
			String info = "";
			if(emailEt.getText().toString().trim().equals("")&& phoneEt.getText().toString().trim().equals("")){
				info = getResources().getString(R.string.email_null);
			}else if(!emailEt.getText().toString().trim().equals("")&&!StringUtils
					.isEmail(emailEt.getText().toString().trim())){
				info = getResources().getString(R.string.email_error);
			}else{
				return;
			}
			b.putBoolean("isshow", true);
			b.putString("msg",info);
			msg.setData(b);
			msg.what = 11;
			handler.sendMessage(msg);
		} else if (!emailEt.getText().toString().trim().equals("")
				) {
			ecLayout.setVisibility(View.GONE);
//			new Thread(runnable3).start();
		} else {
			ecLayout.setVisibility(View.GONE);
		}
	}
	
	
	/***
	 * 监听文本改变
	 */
	class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			if(arg0.length()>0){
				if(phoneEt.getText().toString().equals("")){
					phoneCheckLayout.setVisibility(View.GONE);
				}
				if(emailEt.getText().toString().equals("")){
					ecLayout.setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	}

	/***
	 * author by Ring 文本焦点监听事件
	 */
	private OnFocusChangeListener textfocuschange = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				return;
			}
			Message msg = new Message();
			Bundle b = new Bundle();
			if (v == userNameEt
					&& userNameEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",
						getResources().getString(R.string.username_null));
				msg.setData(b);
				msg.what = 7;
				handler.sendMessage(msg);
			} else if (v == userNameEt
					&& (userNameEt.getText().toString().length() < 6 || userNameEt
							.getText().toString().length() > 15)) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",
						getResources().getString(R.string.username_count));
				msg.setData(b);
				msg.what = 7;
				handler.sendMessage(msg);
			} else if (v == userNameEt
					&& !StringUtils.checkusername(userNameEt.getText()
							.toString().trim())) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",
						getResources().getString(R.string.username_check));
				msg.setData(b);
				msg.what = 7;
				handler.sendMessage(msg);
			} else if (v == userNameEt
					&& !userNameEt.getText().toString().trim().equals("")
					) {
				uncLayout.setVisibility(View.GONE);
				new Thread(runnable1).start();
			} else if (v == userNameEt) {
				uncLayout.setVisibility(View.GONE);
			}
			if (v == passwordEt
					&& passwordEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",
						getResources().getString(R.string.password_null));
				msg.setData(b);
				msg.what = 8;
				handler.sendMessage(msg);
			} else if (v == passwordEt
					&& passwordEt.getText().toString().length() < 6
					|| passwordEt.getText().toString().length() > 15) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",
						getResources().getString(R.string.password_count));
				msg.setData(b);
				msg.what = 8;
				handler.sendMessage(msg);
			} else if (v == passwordEt) {
				pcLayout.setVisibility(View.GONE);
			}
			if (v == passwordAgainEt
					&& !passwordAgainEt.getText().toString()
							.equals(passwordEt.getText().toString())) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",
						getResources().getString(R.string.password_confirm));
				msg.setData(b);
				msg.what = 9;
				handler.sendMessage(msg);
			} else if (v == passwordAgainEt) {
				pacLayout.setVisibility(View.GONE);
			}

			if (v == phoneEt
					&&(phoneEt.getText().toString().trim().equals("")
					|| !StringUtils.isCellphone(phoneEt.getText().toString()
							.trim()))) {
				String info = "";
				if(emailEt.getText().toString().trim().equals("")&& phoneEt.getText().toString().trim().equals("")){
					info = getResources().getString(R.string.telephone_null);
				}else if(!phoneEt.getText().toString().trim().equals("")&&!StringUtils.isCellphone(phoneEt.getText().toString()
						.trim())){
					info = getResources().getString(R.string.phone_error);
				}else{
					return;
				}
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg",info);
				msg.setData(b);
				msg.what = 10;
				handler.sendMessage(msg);
			} else if (v == phoneEt
					&& !phoneEt.getText().toString().trim().equals("")
					) {
				phoneCheckLayout.setVisibility(View.GONE);
				new Thread(runnable2).start();
			} else if (v == phoneEt) {
				phoneCheckLayout.setVisibility(View.GONE);
			}
			
			if (v == emailEt
					&&(emailEt.getText().toString().trim().equals("")
					|| !StringUtils
							.isEmail(emailEt.getText().toString().trim()))) {
				String info = "";
				if(emailEt.getText().toString().trim().equals("")&& phoneEt.getText().toString().trim().equals("")){
					info = getResources().getString(R.string.email_null);
				}else if(!emailEt.getText().toString().trim().equals("")&&!StringUtils
						.isEmail(emailEt.getText().toString().trim())){
					info = getResources().getString(R.string.email_error);
				}else{
					return;
				}
				b.putBoolean("isshow", true);
				b.putString("msg",info);
				msg.setData(b);
				msg.what = 11;
				handler.sendMessage(msg);
			} else if (v == emailEt
					&& !emailEt.getText().toString().trim().equals("")
					) {
				ecLayout.setVisibility(View.GONE);
				new Thread(runnable3).start();
			} else if (v == emailEt) {
				ecLayout.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		} else if (v == submitBtn) {
			userNameEt.clearFocus();
			phoneEt.clearFocus();
			emailEt.clearFocus();
			passwordEt.clearFocus();
			passwordAgainEt.clearFocus();
			emailEt.clearFocus();
			phoneEt.clearFocus();
			if (textValidate()) {
				new Thread(runnable).start();
			}
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}

	/**
	 * author by Ring 向服务器提交注册信息 return true注册成功，false 注册失败
	 */
	public boolean userRegister() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("username", userNameEt.getText()
				.toString()));
		params.add(new BasicNameValuePair("password", passwordEt.getText()
				.toString()));
		params.add(new BasicNameValuePair("Telephone", phoneEt.getText()
				.toString()));
		params.add(new BasicNameValuePair("Email", emailEt.getText().toString()));
		params.add(new BasicNameValuePair("RoleID", UserType.UserTypeNone
				.getValue() + ""));
		uploaddata = new SoSoUploadData(this, "UserAdd.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * author by Ring 处理提交登录信息的服务器响应值
	 */

	public void dealReponse() {
		if (StringUtils.CheckReponse(reponse)) {
			ContentValues values = new ContentValues();
			Gson gson = new Gson();// 创建Gson对象
			SoSoUserInfo sosouserinfo = null;
			try {
				sosouserinfo = gson.fromJson(reponse, SoSoUserInfo.class);// 解析json对象
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sosouserinfo != null && sosouserinfo.getUserName() != null
					&& sosouserinfo.getUserID() != null) {
				sosouserinfo.setUserName(userNameEt.getText().toString());
				sosouserinfo.setPassWord(passwordEt.getText().toString());
				sosouserinfo.setTelephone(phoneEt.getText().toString());
				sosouserinfo.setEmail(emailEt.getText().toString());
				values.put("soso_userid", sosouserinfo.getUserID());
				values.put("soso_username", sosouserinfo.getUserName());
				values.put("soso_password", passwordEt.getText().toString());
				values.put("soso_realname", sosouserinfo.getRealName());
				values.put("soso_registerdate", sosouserinfo.getRegisterDate());
				values.put("soso_userimageurl", sosouserinfo.getHeadImage());
				values.put("soso_logindate", sosouserinfo.getLoginDate());
				values.put("soso_sex", sosouserinfo.getSex());
				values.put("soso_roleid", sosouserinfo.getRoleID());
				values.put("soso_rolemc", sosouserinfo.getRoleMC());
				values.put("soso_region", sosouserinfo.getRegion());
				values.put("soso_birthday", sosouserinfo.getBirthday());
				values.put("soso_email", sosouserinfo.getEmail());
				values.put("soso_telephone", sosouserinfo.getTelephone());
				values.put("soso_bigintegral", sosouserinfo.getBigintegral());
				values.put("soso_credibility", sosouserinfo.getCredibility());
				values.put("soso_customerstate",
						sosouserinfo.getCustomerState());
				values.put("autologin", 0);
				values.put("loginlast", 1);

				DBHelper.getInstance(RegisterFirstActivity.this)
						.execSql(
								"update soso_userinfo set autologin = 0 ,loginlast = 0");
				if (DBHelper
						.getInstance(RegisterFirstActivity.this)
						.selectRow(
								"select * from soso_userinfo where soso_userid = '"
										+ sosouserinfo.getUserID() + "'", null)
						.size() <= 0) {
					DBHelper.getInstance(RegisterFirstActivity.this).insert(
							"soso_userinfo", values);
				} else {
					DBHelper.getInstance(RegisterFirstActivity.this).update(
							"soso_userinfo", values, "soso_userid = ?",
							new String[] { sosouserinfo.getUserID() });
				}
				sosouserinfo.setPassWord(passwordEt.getText().toString());
				MyApplication.getInstance(RegisterFirstActivity.this)
						.setSosouserinfo(sosouserinfo);
				
//				MyApplication.getInstance(RegisterFirstActivity.this)
//						.setRoleid(sosouserinfo.getRoleID());
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			DBHelper.getInstance(RegisterFirstActivity.this).close();
		}
	}

	/**
	 * author by Ring 检查用户名的唯一性 return true唯一，false 不唯一
	 */
	public boolean checkUsernameUnique() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UserName", userNameEt.getText()
				.toString()));
		uploaddata1 = new SoSoUploadData(this, "CheckUserName.aspx", params);
		uploaddata1.Post();
		reponse = uploaddata1.getReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			if (StringUtils.getErrorState(reponse).equals(
					ErrorType.SoSoTimeOut.getValue())) {
				return true;
			}
			return false;
		}
	}

	/**
	 * author by Ring 向服务器提交注册信息 return true注册成功，false 注册失败
	 */
	public boolean checkPhoneUnique() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("Telephone", phoneEt.getText()
				.toString()));
		uploaddata2 = new SoSoUploadData(this, "CheckTelephone.aspx", params);
		uploaddata2.Post();
		reponse = uploaddata2.getReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			if (StringUtils.getErrorState(reponse).equals(
					ErrorType.SoSoTimeOut.getValue())) {
				return true;
			}
			return false;
		}
	}

	/**
	 * author by Ring 向服务器提交注册信息 return true注册成功，false 注册失败
	 */
	public boolean checkEmailUnique() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("Email", emailEt.getText().toString()));
		uploaddata3 = new SoSoUploadData(this, "CheckEmail.aspx", params);
		uploaddata3.Post();
		reponse = uploaddata3.getReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			if (StringUtils.getErrorState(reponse).equals(
					ErrorType.SoSoTimeOut.getValue())) {
				return true;
			}
			return false;
		}
	}

	/**
	 * author by Ring 注册前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (userNameEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.username_null),
					RegisterFirstActivity.this);
			return false;
		} else if (userNameEt.getText().toString().length() <= 6
				&& userNameEt.getText().toString().length() >= 12) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.username_count),
					RegisterFirstActivity.this);
			return false;
		} else if (!StringUtils.checkusername(userNameEt.getText().toString()
				.trim())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.username_check),
					RegisterFirstActivity.this);
			return false;
		} else if (passwordEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.password_null),
					RegisterFirstActivity.this);
			return false;
		} else if (passwordEt.getText().toString().length() <= 6
				&& passwordEt.getText().toString().length() >= 15) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.password_count),
					RegisterFirstActivity.this);
			return false;
		} else if (!passwordAgainEt.getText().toString()
				.equals(passwordEt.getText().toString())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.password_confirm),
					RegisterFirstActivity.this);
			return false;
		}else if (!phoneEt.getText().toString().trim().equals("")
				&&!StringUtils
				.isCellphone(phoneEt.getText().toString().trim())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.phone_error),
					RegisterFirstActivity.this);
			return false;
		} else if (!emailEt.getText().toString().trim().equals("")
				&& !StringUtils.isEmail(emailEt.getText().toString().trim())) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.email_error),
					RegisterFirstActivity.this);
			return false;
		}else if(phoneEt.getText().toString().trim().equals("")
				&&emailEt.getText().toString().trim().equals("")){
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.telephoneandemail),
					RegisterFirstActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * author by Ring 处理注册耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(RegisterFirstActivity.this)) {
				handler.sendEmptyMessage(5);
				boolean b = userRegister();
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// 从注册界面跳转到注册成功界面
				} else {
					handler.sendEmptyMessage(3);// 注册失败给用户提示
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

			click_limit = true;
		}
	};
	/**
	 * author by Ring 处理用户名唯一性验证耗时操作
	 */
	public Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			if (NetworkCheck.IsHaveInternet(RegisterFirstActivity.this)) {
				if (uploaddata1 != null) {
					uploaddata1.overReponse();
				}
				boolean b = checkUsernameUnique();
				if (b) {
					bundle.clear();
					bundle.putBoolean("isshow", false);
					msg.setData(bundle);
					msg.what = 7;
					handler.sendMessage(msg);
				} else {
					bundle.clear();
					bundle.putBoolean("isshow", true);
					bundle.putString("msg",
							getResources().getString(R.string.username_again));
					msg.setData(bundle);
					msg.what = 7;
					handler.sendMessage(msg);
				}
			} else {
				// handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
		}
	};
	/**
	 * author by Ring 处理用户名唯一性验证耗时操作
	 */
	public Runnable runnable2 = new Runnable() {

		@Override
		public void run() {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			if (NetworkCheck.IsHaveInternet(RegisterFirstActivity.this)) {
				if (uploaddata2 != null) {
					uploaddata2.overReponse();
				}
				boolean b = checkPhoneUnique();
				if (b) {
					bundle.clear();
					bundle.putBoolean("isshow", false);
					msg.setData(bundle);
					msg.what = 10;
					handler.sendMessage(msg);
				} else {
					bundle.clear();
					bundle.putBoolean("isshow", true);
					bundle.putString("msg",
							getResources().getString(R.string.phone_again));
					msg.setData(bundle);
					msg.what = 10;
					handler.sendMessage(msg);
				}
			} else {
				// handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
		}
	};
	/**
	 * author by Ring 处理用户名唯一性验证耗时操作
	 */
	public Runnable runnable3 = new Runnable() {

		@Override
		public void run() {
			Message msg = new Message();
			Bundle bundle = new Bundle();
			if (NetworkCheck.IsHaveInternet(RegisterFirstActivity.this)) {
				if (uploaddata3 != null) {
					uploaddata3.overReponse();
				}
				boolean b = checkEmailUnique();
				if (b) {
					bundle.clear();
					bundle.putBoolean("isshow", false);
					msg.setData(bundle);
					msg.what = 11;
					handler.sendMessage(msg);
				} else {
					bundle.clear();
					bundle.putBoolean("isshow", true);
					bundle.putString("msg",
							getResources().getString(R.string.email_again));
					msg.setData(bundle);
					msg.what = 11;
					handler.sendMessage(msg);
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
		}
	};

	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			Bundle b = null;
			switch (msg.what) {
			case 1:// 从注册界面跳转到注册成功界面
				i.setClass(RegisterFirstActivity.this,
						RegisterSecondActivity.class);
				RegisterFirstActivity.this.startActivity(i);
				RegisterFirstActivity.this.finish();
				break;
			case 2:// 从注册界面跳转到登录界面
				i.setClass(RegisterFirstActivity.this, LoginActivity.class);
				RegisterFirstActivity.this.startActivity(i);
				RegisterFirstActivity.this.finish();
				break;
			case 3:// 注册失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.UserNameAgain.getValue())) {
					message = getResources().getString(R.string.username_again);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.PhoneAgain.getValue())) {
					message = getResources().getString(R.string.phone_again);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.EmailAgain.getValue())) {
					message = getResources().getString(R.string.email_again);
				} else {
					message = getResources().getString(R.string.register_error);
				}
				MessageBox.CreateAlertDialog(message,
						RegisterFirstActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(RegisterFirstActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(RegisterFirstActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_register));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// 用户名下面的验证层的控制
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						uncLayout.setVisibility(View.VISIBLE);
						userNameCheck.setText(b.getString("msg"));
					}
				} else {
					uncLayout.setVisibility(View.GONE);
				}
				break;
			case 8:// 密码层下面的验证层的控制
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						pcLayout.setVisibility(View.VISIBLE);
						passCheck.setText(b.getString("msg"));
					}
				} else {
					pcLayout.setVisibility(View.GONE);
				}
				break;
			case 9:// 确认密码层下面的验证层的控制
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						pacLayout.setVisibility(View.VISIBLE);
						passAgainCheck.setText(b.getString("msg"));
					}
				} else {
					pacLayout.setVisibility(View.GONE);
				}
				break;
			case 10:// 手机号下面的验证层控制
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						phoneCheckLayout.setVisibility(View.VISIBLE);
						phoneCheck.setText(b.getString("msg"));
					}
				} else {
					phoneCheckLayout.setVisibility(View.GONE);
				}
				break;
			case 11:// 邮箱下面的验证层的控制
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						ecLayout.setVisibility(View.VISIBLE);
						emailCheck.setText(b.getString("msg"));
					}
				} else {
					ecLayout.setVisibility(View.GONE);
				}
				break;
			}
		};
	};

}
