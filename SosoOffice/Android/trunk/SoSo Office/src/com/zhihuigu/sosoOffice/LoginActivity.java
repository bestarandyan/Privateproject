package com.zhihuigu.sosoOffice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.CityListActivity.MyTextWatcher;
import com.zhihuigu.sosoOffice.constant.Constant;
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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {
	private Button backBtn, registerBtn, loginBtn;
	private EditText userNameEt, passwordEt;
	private CheckBox checkBox;
	private SoSoUploadData uploaddata;
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().setRcount(0);
		MyApplication.getInstance().setLettercount(0);
		MyApplication.getInstance().setPushcount(0);
		setContentView(R.layout.a_login);
		findView();
		initView();
	}
	/***
	 * 监听文本改变
	 */
	class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			if(userNameEt.getText().toString().length()<=0){
				passwordEt.setText("");
			}
			if(MyApplication.getInstance().getSosouserinfo()!=null
					&&MyApplication.getInstance().getSosouserinfo().getUserName()!=null
					&&!MyApplication.getInstance().getSosouserinfo().getUserName().contains(userNameEt.getText().toString())){
				passwordEt.setText("");
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
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		registerBtn = (Button) findViewById(R.id.registerBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		userNameEt = (EditText) findViewById(R.id.userNameEt);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		userNameEt.addTextChangedListener(new MyTextWatcher());
	}
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().getSosouserinfo()!=null){
			String username = MyApplication.getInstance().getSosouserinfo().getUserName();
			String password = MyApplication.getInstance().getSosouserinfo().getPassWord();
			if(username!=null && password!=null){
				userNameEt.setText(username);
				passwordEt.setText(password);
			}
		}
		super.onResume();
	}
	public void initView() {
		backBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			if(MyApplication.getInstance().getNotlogin()==1){
				finish();
			}else{
				showExitDialog(this);
			}
		} else if (v == registerBtn) {
			Intent intent = new Intent(this, RegisterFirstActivity.class);
			startActivity(intent);
			finish();
		} else if (v == loginBtn) {
			CommonUtils.hideSoftKeyboard(this);
//			Intent intent = new Intent(this, MainTabActivity.class);
//			startActivity(intent);
//			finish();
			if(textValidate()){
				new Thread(runnable).start();
			}
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(MyApplication.getInstance().getNotlogin()==1){
				finish();
			}else{
				showExitDialog(this);
			}
				
		}
		return false;
	}

	/**
	 * author by Ring 向服务器提交登录信息 return true登录成功，false 登录失败
	 */
	public boolean userLogin() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("userName", userNameEt.getText()
				.toString()));
		params.add(new BasicNameValuePair("Password", passwordEt.getText()
				.toString()));
		uploaddata = new SoSoUploadData(this, "UserLogin.aspx", params);
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

	/**
	 * author by Ring 登录前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (userNameEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.username_null),
					LoginActivity.this);
			return false;
		} else if (passwordEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.password_null),
					LoginActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(LoginActivity.this)) {
				handler.sendEmptyMessage(5);// 没有网络时给用户提示
				boolean b = userLogin();
				handler.sendEmptyMessage(6);// 没有网络时给用户提示
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
				if(b){
					handler.sendEmptyMessage(1);//登录成功后
				}else{
					handler.sendEmptyMessage(3);// 登录失败时给用户提示
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
			case 1:// 从登录界面跳转到主界面
				if(MyApplication.getInstance(LoginActivity.this).getRoleid()==0){
					i.setClass(LoginActivity.this, RegisterSecondActivity.class);
					LoginActivity.this.startActivity(i);
					LoginActivity.this.finish();
				}else if((MyApplication.getInstance(LoginActivity.this).getRoleid()==Constant.TYPE_AGENCY
						||MyApplication.getInstance(LoginActivity.this).getRoleid()==Constant.TYPE_PROPRIETOR)
						&&(MyApplication.getInstance(LoginActivity.this).getSosouserinfo().getJobsImage()==null||
						MyApplication.getInstance(LoginActivity.this).getSosouserinfo().getJobsImage().equals(""))){
					if(MyApplication.getInstance(LoginActivity.this).getRoleid()!=UserType.UserTypeCustomer.getValue()
							&&MyApplication.getInstance(LoginActivity.this).getRoleid()!=UserType.UserTypeIntermediary.getValue()
							&&MyApplication.getInstance(LoginActivity.this).getRoleid()!=UserType.UserTypeOwner.getValue()){
						AlertDialog.Builder builder = new AlertDialog.Builder(
								LoginActivity.this);
						builder.setMessage("登录失败，该用户不合法？")
								.setTitle(LoginActivity.this.getResources().getString(R.string.prompt))
								.setCancelable(false)
								.setPositiveButton("确定",null);
						AlertDialog alert = builder.create();
						alert.show();
						DBHelper.getInstance(LoginActivity.this).delete("soso_userinfo", "soso_userid=?", new String[]{MyApplication.getInstance(LoginActivity.this).getSosouserinfo().getUserID()});
						return;
					}
					i.setClass(LoginActivity.this, RegisterThirdActivity.class);
					LoginActivity.this.startActivity(i);
					LoginActivity.this.finish();
				}else if(MyApplication.getInstance(LoginActivity.this)
						.getSosouserinfo() != null
						&&MyApplication.getInstance(LoginActivity.this)
						.getSosouserinfo().getUserID()!=null){
					if(MyApplication.getInstance(LoginActivity.this).getRoleid()!=UserType.UserTypeCustomer.getValue()
							&& MyApplication.getInstance(LoginActivity.this).getRoleid()!=UserType.UserTypeIntermediary.getValue()
							&& MyApplication.getInstance(LoginActivity.this).getRoleid()!=UserType.UserTypeOwner.getValue()){
						AlertDialog.Builder builder = new AlertDialog.Builder(
								LoginActivity.this);
						builder.setMessage("登录失败，该用户不合法？")
								.setTitle(LoginActivity.this.getResources().getString(R.string.prompt))
								.setCancelable(false)
								.setPositiveButton("确定",null);
						AlertDialog alert = builder.create();
						alert.show();
						DBHelper.getInstance(LoginActivity.this).delete("soso_userinfo", "soso_userid=?", new String[]{MyApplication.getInstance(LoginActivity.this).getSosouserinfo().getUserID()});
						return;
					}
					i.putExtra("tag", 1);
					if(ApplicationSettingInfo.initSetting(LoginActivity.this,MyApplication.getInstance(LoginActivity.this)
							.getSosouserinfo().getUserID())){
						i.setClass(LoginActivity.this, CityListActivity.class);
						LoginActivity.this.startActivity(i);
						LoginActivity.this.finish();
					}else{
						if(MyApplication.getInstance().getNotlogin()==1){
							MyApplication.getInstance().setNotlogin(0);
							LoginActivity.this.finish();
						}else{
							i.setClass(LoginActivity.this, MainTabActivity.class);
							LoginActivity.this.startActivity(i);
							LoginActivity.this.finish();
						}
						
						
					}
				}
				
				break;
			case 2:// 从登录界面跳转到注册界面
				i.setClass(LoginActivity.this, RegisterFirstActivity.class);
				LoginActivity.this.startActivity(i);
				LoginActivity.this.finish();
				break;
			case 3:// 登录失败给用户提示
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else {
					errormsg = getResources().getString(
							R.string.login_error_check);
				}
				MessageBox.CreateAlertDialog(errormsg, LoginActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(LoginActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(LoginActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_login));
				progressdialog.setCanceledOnTouchOutside(false);
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
			}
		};
	};

	/***
	 * author by Ring 处理提交登录信息的服务器响应值
	 */

	public void dealReponse() {
		if (StringUtils.CheckReponse(reponse)) {
			ContentValues values = new ContentValues();
			Gson gson = new Gson();// 创建Gson对象
			SoSoUserInfo sosouserinfo = null;
			List<Map<String, Object>> selectresult = null;
			try {
				sosouserinfo = gson.fromJson(reponse, SoSoUserInfo.class);//解析json对象
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sosouserinfo != null && sosouserinfo.getUserName() != null
					&& sosouserinfo.getUserID() != null) {
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
				values.put("soso_customerstate", sosouserinfo.getCustomerState());
				values.put("soso_ownerphone",
						sosouserinfo.getZJTele());
				values.put("soso_legalperson",
						sosouserinfo.getZJMC());
				values.put("soso_owneraddress",
						sosouserinfo.getZJAddress());
				values.put("soso_businesslicenseurl",
						sosouserinfo.getJobsImage());
				values.put("autologin", checkBox.isChecked() ? 1 : 0);
				values.put("loginlast", 1);
				
				DBHelper.getInstance(LoginActivity.this).execSql(
						"update soso_userinfo set autologin = 0 ,loginlast = 0");
				selectresult = DBHelper
						.getInstance(LoginActivity.this)
						.selectRow(
								"select * from soso_userinfo where soso_userid = '"
										+ sosouserinfo.getUserID() + "'", null);
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String time = StringUtils.formatCalendar1(sdf.format(new Date()),MyApplication.error_value);
				if(selectresult!=null){
					if (selectresult.size() <= 0) {
						values.put("soso_lastlogindate", "1900-01-01 12:00:00");
						DBHelper.getInstance(LoginActivity.this).insert("soso_userinfo",
								values);
						sosouserinfo.setLastLoginDate("1900-01-01 12:00:00");
					} else {
						if(selectresult.get(selectresult.size()-1).get("soso_userimageurl")!=null
								&&!sosouserinfo.equals(selectresult.get(selectresult.size()-1).get("soso_userimageurl").toString())){
							DBHelper.getInstance(this).execSql("update soso_userinfo set soso_userimagesd = '' where soso_userid ='"+sosouserinfo.getUserID()+"'");
						}
						try{
							values.put("soso_lastlogindate", selectresult.get(selectresult.size()-1).get("soso_logindate").toString());
						}catch(Exception e){
							values.put("soso_lastlogindate", "1900-01-01 12:00:00");
						}
						DBHelper.getInstance(LoginActivity.this).update("soso_userinfo",
								values, "soso_userid = ?",
								new String[] { sosouserinfo.getUserID() });
						try {
							sosouserinfo.setLastLoginDate(selectresult
									.get(selectresult.size() - 1)
									.get("soso_lastlogindate").toString());
						} catch (Exception e) {
							sosouserinfo.setLastLoginDate("1900-01-01 12:00:00");
						}
					}
				}
				sosouserinfo.setPassWord(passwordEt.getText().toString());
				MyApplication.getInstance(LoginActivity.this).setSosouserinfo(sosouserinfo);
				MyApplication.getInstance(LoginActivity.this).setRoleid(sosouserinfo.getRoleID());
			}
			if(values !=null){
				values.clear();
				values = null;
			}
			DBHelper.getInstance(LoginActivity.this).close();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
