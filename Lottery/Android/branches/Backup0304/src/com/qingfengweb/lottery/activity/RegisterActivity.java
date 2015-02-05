/**
 * 
 */
package com.qingfengweb.lottery.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.MessageBox;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.util.StringUtils;

/**
 * @author  刘星星
 * @createDate 2013/11/27
 * 用户注册界面
 *
 */
@SuppressLint("HandlerLeak")
public class RegisterActivity extends Activity implements OnClickListener{
	EditText userNameEt,passwordEt,againPasswordEt;
	DBHelper dbHelper;
	private ProgressDialog progressdialog;
	private LinearLayout wornLayout1,wornLayout2,wornLayout3;
	private TextView wornTv1,wornTv2,wornTv3;
	private CheckBox radioBtn1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_register);
		findview();
		dbHelper = DBHelper.getInstance(this);
	}
	private void findview(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.loginBtn).setOnClickListener(this);
		findViewById(R.id.registerBtn).setOnClickListener(this);
		userNameEt = (EditText) findViewById(R.id.userNameEt);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		againPasswordEt = (EditText) findViewById(R.id.againPasswordEt);
		wornLayout1 = (LinearLayout) findViewById(R.id.worningLayout1);
		wornLayout2 = (LinearLayout) findViewById(R.id.worningLayout2);
		wornLayout3 = (LinearLayout) findViewById(R.id.worningLayout3);
		wornTv1 = (TextView) findViewById(R.id.worningUserNameEt);
		wornTv2 = (TextView) findViewById(R.id.worningPasswordEt);
		wornTv3 = (TextView) findViewById(R.id.worningAgainPassEt);
		radioBtn1 = (CheckBox) findViewById(R.id.radioBtn1);
	}
	private void showDialog(){
		progressdialog = new ProgressDialog(RegisterActivity.this);
		progressdialog.setMessage("正在提交注册请求，请稍候...");
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
	Dialog dialog = null;
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.loginBtn){
			
				if(LoginActivity.isActive){
					finish();
				}else{
					Intent intent = new Intent(this,LoginActivity.class);
					startActivity(intent);
					finish();
				}
		}else if(v.getId() == R.id.registerBtn){
			if(NetworkCheck.IsHaveInternet(this)){
				if(textValidate()){
					if(radioBtn1.isChecked()){
					showDialog();
					new Thread(registerRunnable).start();
					}else{
						Toast.makeText(this, "请同意注册相关条款和协议！", Toast.LENGTH_LONG).show();
					}
				}
			}else{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("提示：");
				alert.setIcon(android.R.drawable.ic_dialog_info);
				alert.setMessage("网络未连接，是否查看网络设置？");
				alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
			            startActivity(intent);
			            dialog.dismiss();
			            RegisterActivity.this.dialog.dismiss();
					}
				});
				alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						RegisterActivity.this.dialog.dismiss();
					}
				});
				dialog = alert.create();
				dialog.show();
			}	
		}
	}
	public boolean textValidate() {
		if (userNameEt.getText().toString().trim().equals("")) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名不能为空！");
			MessageBox.CreateAlertDialog("提示！","用户名不能为空！",RegisterActivity.this);
			return false;
		} else if (!StringUtils.checkPhoneNumber(userNameEt.getText().toString().trim()) && !StringUtils.isEmail(userNameEt.getText().toString().trim())) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名的格式不正确！");
			MessageBox.CreateAlertDialog("提示！","用户名的格式不正确！",RegisterActivity.this);
			return false;
		} else if (passwordEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码不能为空！");
			MessageBox.CreateAlertDialog("提示！","密码不能为空！",RegisterActivity.this);
			return false;
		} else if (passwordEt.getText().toString().length()>16 || passwordEt.getText().toString().length()<6) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码长度为6-16！");
			MessageBox.CreateAlertDialog("提示！","密码长度为6-16！",RegisterActivity.this);
			return false;
		} else if (!againPasswordEt.getText().toString().equals(passwordEt.getText().toString())) {
//			wornLayout3.setVisibility(View.VISIBLE);
//			wornTv3.setText("两次输入的密码不一致！");
			MessageBox.CreateAlertDialog("提示！","两次输入的密码不一致！",RegisterActivity.this);
			return false;
		} 
		return true;
	}
	Runnable registerRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.registerUser(userNameEt.getText().toString(), passwordEt.getText().toString());
			System.out.println("注册用户返回值--------------------------"+msg);
			handler.sendEmptyMessage(3);
			if(msg.startsWith("{") && msg.length()>3){//注册成功
				JsonData.jsonRegisterUserData(msg, dbHelper.open());
				MyApplication.getInstance().setCurrentUserName(userNameEt.getText().toString());
				MyApplication.getInstance().setCurrentPassword(passwordEt.getText().toString());
				Intent intent = new Intent(RegisterActivity.this,AmendMyInfoActivity.class);
				startActivity(intent);
				finish();
			}else if(msg.equals("404")){//访问接口失败
				handler.sendEmptyMessage(0);
			}else if(msg.equals("-6")){//用户已存在
				handler.sendEmptyMessage(1);
			}else if(msg.equals("-901")){//系统错误
				handler.sendEmptyMessage(2);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				MessageBox.CreateAlertDialog("提示！","访问服务器失败，请检查网络连接或重试！",RegisterActivity.this);
			}else if(msg.what == 1){
				MessageBox.CreateAlertDialog("提示！","该用户名已被注册！",RegisterActivity.this);
				userNameEt.setText("");
				userNameEt.requestFocus();
			}else if(msg.what == 2){
				MessageBox.CreateAlertDialog("提示！","注册失败！请重试",RegisterActivity.this);
			}else if(msg.what == 3){
				progressdialog.dismiss();
			}
			super.handleMessage(msg);
		}
		
	};
}
