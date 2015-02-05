/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.beans.UserBean;
import com.qingfengweb.weddingideas.data.DBHelper;
import com.qingfengweb.weddingideas.data.JsonData;
import com.qingfengweb.weddingideas.data.RequestServerFromHttp;
import com.qingfengweb.weddingideas.utils.MessageBox;
import com.qingfengweb.weddingideas.utils.StringUtils;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/12/30
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR) public class LoginOrSignActivity extends BaseActivity{
	private LinearLayout signLayout,loginLayout;
	private LinearLayout layout1,layout2;
	private EditText signPhoneEt,signNameEt,loginPhoneEt,loginPasswordEt;
	private Button signBtn,loginBtn;
	DBHelper dbHelper;
	private int typeFlag = 0;
	TextView titleTv,title1Tv,title2Tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_loginorsign);
		initView();
		dbHelper = DBHelper.getInstance(this);
	}
	
	private void initView(){
		signLayout = (LinearLayout) findViewById(R.id.signLayout);
		loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		signPhoneEt = (EditText) findViewById(R.id.signPhoneEt);
		signNameEt = (EditText) findViewById(R.id.signNameEt);
		loginPhoneEt = (EditText) findViewById(R.id.userNameEt);
		loginPasswordEt = (EditText) findViewById(R.id.passwordEt);
		signBtn = (Button) findViewById(R.id.signBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		signBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		layout1.setOnClickListener(this);
		layout2.setOnClickListener(this);
		titleTv = (TextView) findViewById(R.id.topView);
		title1Tv = (TextView) findViewById(R.id.title1Tv);
		title2Tv = (TextView) findViewById(R.id.title2Tv);
		title1Tv.setTextColor(Color.parseColor(LoadingActivity.templateList.get(0).get("login_title_text_color")));
		title2Tv.setTextColor(Color.parseColor(LoadingActivity.templateList.get(0).get("login_title_text_color")));
		titleTv.setText(LoadingActivity.configList.get(0).get("store_name"));
	}
	private void insertSignData(){
		ContentValues values1 = new ContentValues();
		values1.put("islogin", "0");
		dbHelper.update(UserBean.tbName, values1, null, null);
		ContentValues values = new ContentValues();
		values.put("name", signNameEt.getText().toString());
		values.put("username", signPhoneEt.getText().toString());
		values.put("islogin", "1");
		values.put("type", "0");
		boolean b = dbHelper.update(UserBean.tbName, values, "username=?", new String[]{signPhoneEt.getText().toString()});
		if(!b){
			dbHelper.insert(UserBean.tbName, values);
		}
	}
	private void insertLoginData(){
		ContentValues values1 = new ContentValues();
		values1.put("islogin", "0");
		dbHelper.update(UserBean.tbName, values1, null, null);
		ContentValues values = new ContentValues();
		values.put("password", loginPasswordEt.getText().toString());
		values.put("username", loginPhoneEt.getText().toString());
		values.put("islogin", "1");
		values.put("type", "1");
		boolean b = dbHelper.update(UserBean.tbName, values, "username=?", new String[]{loginPhoneEt.getText().toString()});
		if(!b){
			dbHelper.insert(UserBean.tbName, values);
		}
	}
	public boolean signValidate() {
		if (signPhoneEt.getText().toString().trim().equals("")) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名不能为空！");
			MessageBox.CreateAlertDialog("提示！","手机号码不能为空",this);
			return false;
		}else if (!StringUtils.checkPhoneNumber(signPhoneEt.getText().toString().trim())) {
//			wornLayout3.setVisibility(View.VISIBLE);
//			wornTv3.setText("两次输入的密码不一致！");
			MessageBox.CreateAlertDialog("提示！","输入的手机号码格式不匹配",this);
			return false;
		} else if (signNameEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码不能为空！");
			MessageBox.CreateAlertDialog("提示！","真实姓名不能为空",this);
			return false;
		}
		return true;
	}
	public boolean loginValidate() {
		if (loginPhoneEt.getText().toString().trim().equals("")) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名不能为空！");
			MessageBox.CreateAlertDialog("提示！","手机号码不能为空",this);
			return false;
		}else if (!StringUtils.checkPhoneNumber(loginPhoneEt.getText().toString().trim())) {
//			wornLayout3.setVisibility(View.VISIBLE);
//			wornTv3.setText("两次输入的密码不一致！");
			MessageBox.CreateAlertDialog("提示！","输入的手机号码格式不匹配",this);
			return false;
		} else if (loginPasswordEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码不能为空！");
			MessageBox.CreateAlertDialog("提示！","请输入密码",this);
			return false;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == signBtn){
			if(signValidate()){
				showProgressDialog("正在提交请求，请稍候... ...");
				new Thread(signRunnable).start();
			}
		}else if(v == loginBtn){
			if(loginValidate()){
				showProgressDialog("正在提交请求，请稍候... ...");
				new Thread(loginRunnable).start();
			}
		}else if(v == layout1){//送祝福
			if(signLayout.getVisibility() == View.VISIBLE){
				signLayout.setVisibility(View.GONE);
			}else{
				signLayout.setVisibility(View.VISIBLE);
			}
			loginLayout.setVisibility(View.GONE);
		}else if(v == layout2){//我要赴宴
			if(loginLayout.getVisibility() == View.VISIBLE){
				loginLayout.setVisibility(View.GONE);
			}else{
				loginLayout.setVisibility(View.VISIBLE);
			}
			signLayout.setVisibility(View.GONE);
		}
		super.onClick(v);
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				progressDialog.dismiss();
				insertSignData();
				Intent intent = new Intent(LoginOrSignActivity.this,MainVideoActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}else  if(msg.what == 1){
				progressDialog.dismiss();
				MessageBox.CreateAlertDialog("提示", "提交失败！请检查网络连接或重试！",LoginOrSignActivity.this);
			}else  if(msg.what == 2){
				progressDialog.dismiss();
				insertLoginData();
				Intent intent = new Intent(LoginOrSignActivity.this,MainVideoActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}else  if(msg.what == 3){
				progressDialog.dismiss();
				MessageBox.CreateAlertDialog("提示", "登陆失败！请检查网络连接或重试！",LoginOrSignActivity.this);
			}else if(msg.what == 4){//用户名或密码错误
				progressDialog.dismiss();
				MessageBox.CreateAlertDialog("提示", "用户名或密码错误，请核对后再重试！",LoginOrSignActivity.this);
			}else if(msg.what == 5){//签收失败
				progressDialog.dismiss();
				MessageBox.CreateAlertDialog("提示", "签收失败！请检查网络连接或重试！",LoginOrSignActivity.this);
			}else if(msg.what == 6){//用户名或密码错误
				progressDialog.dismiss();
				MessageBox.CreateAlertDialog("提示", "用户名或密码错误！",LoginOrSignActivity.this);
			}
			super.handleMessage(msg);
		}
	};
	/**
	 * 签收接口访问
	 */
	Runnable signRunnable = new Runnable() {
		
		@Override
		public void run() {
			String yinglouid = LoadingActivity.configList.get(0).get("storeid");
			String  msgStr = RequestServerFromHttp.userSign(yinglouid,signPhoneEt.getText().toString().trim(), signNameEt.getText().toString().trim());
			if(msgStr.startsWith("{") && JsonData.jsonSuccessData(msgStr).equals("0")){
				handler.sendEmptyMessage(0);
			}else if(msgStr.startsWith("{") && JsonData.jsonSuccessData(msgStr).equals("-998")){
				handler.sendEmptyMessage(6);
			}else if(msgStr.startsWith("{") && !JsonData.jsonSuccessData(msgStr).equals("0")){
				handler.sendEmptyMessage(5);
			}else if(msgStr.equals("404")){
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(1);
			}
		}
	};
			
	
	/**
	 * 签收接口访问
	 */
	Runnable loginRunnable = new Runnable() {
		
		@Override
		public void run() {
			String yinglouid = LoadingActivity.configList.get(0).get("storeid");
			String  msgStr = RequestServerFromHttp.userLogin(yinglouid, loginPhoneEt.getText().toString().trim(), loginPasswordEt.getText().toString().trim(), "");
			if(msgStr.startsWith("{") && JsonData.jsonSuccessData(msgStr).equals("0")){
				handler.sendEmptyMessage(2);
			}else if(msgStr.startsWith("{") && JsonData.jsonSuccessData(msgStr).equals("-998")){
				handler.sendEmptyMessage(6);
			}else if(msgStr.startsWith("{") && JsonData.jsonSuccessData(msgStr).equals("-31")){
				handler.sendEmptyMessage(4);
			}else if(msgStr.equals("404")){
				handler.sendEmptyMessage(3);
			}else{
				handler.sendEmptyMessage(1);
			}
		}
	};
}
