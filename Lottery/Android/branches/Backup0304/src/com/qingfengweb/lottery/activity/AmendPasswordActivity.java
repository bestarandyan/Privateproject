/**
 * 
 */
package com.qingfengweb.lottery.activity;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.MessageBox;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.util.StringUtils;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author qingfeng
 *
 */
public class AmendPasswordActivity extends Activity implements OnClickListener{
	private TextView userNameTv ;
	private EditText oldPwdEt,newPwdEt,againPwdEt;
	private ProgressDialog progressdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_amendpassword);
		userNameTv = (TextView) findViewById(R.id.userNameTv);
		oldPwdEt = (EditText) findViewById(R.id.oldPwd);
		newPwdEt = (EditText) findViewById(R.id.newPwd);
		againPwdEt = (EditText) findViewById(R.id.againNewPwd);
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.submitBtn).setOnClickListener(this);
		userNameTv.setText("用户名:"+MyApplication.getInstance().getCurrentUserName());
	}
	private void showDialog(){
		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage("正在提交请求，请稍候...");
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
	Runnable amendPwdRunnable = new Runnable() {
		@Override
		public void run() {
			String msg = RequestServerFromHttp.amendPWD(oldPwdEt.getText().toString().trim(), newPwdEt.getText().toString().trim());
			if(msg.equals("1")){//修改密码成功
				handler.sendEmptyMessage(0);
			}else if(msg.equals("-10")){//旧密码错误
				handler.sendEmptyMessage(1);
			}else if(msg.equals("404")){//访问服务器失败
				handler.sendEmptyMessage(2);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}else{//修改失败
				handler.sendEmptyMessage(3);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//修改密码成功
				progressdialog.dismiss();
				Intent intent = new Intent(AmendPasswordActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}else if(msg.what == 1){//旧密码错误
				progressdialog.dismiss();
				MessageBox.CreateAlertDialog("提示！","请输入正确的旧密码！",AmendPasswordActivity.this);
			}else if(msg.what == 2){//访问服务器失败
				progressdialog.dismiss();
			}else if(msg.what == 3){//修改失败
				progressdialog.dismiss();
				MessageBox.CreateAlertDialog("提示！","修改失败，请重试！",AmendPasswordActivity.this);
			}else if(msg.what == 102){//token超时
				progressdialog.dismiss();
				Toast.makeText(getApplicationContext(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(AmendPasswordActivity.this,LoginActivity.class);
				intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
				startActivity(intent);
			}
			super.handleMessage(msg);
		}
		
	};
	public boolean textValidate() {
		if (oldPwdEt.getText().toString().trim().equals("")) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名不能为空！");
			MessageBox.CreateAlertDialog("提示！","请输入旧密码！",AmendPasswordActivity.this);
			return false;
		}else if (oldPwdEt.getText().toString().length()>16 || oldPwdEt.getText().toString().length()<6) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码长度为6-16！");
			MessageBox.CreateAlertDialog("提示！","密码长度为6-16！",AmendPasswordActivity.this);
			return false;
		} else if (newPwdEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码不能为空！");
			MessageBox.CreateAlertDialog("提示！","请输入新密码！",AmendPasswordActivity.this);
			return false;
		} else if (newPwdEt.getText().toString().length()>16 || newPwdEt.getText().toString().length()<6) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码长度为6-16！");
			MessageBox.CreateAlertDialog("提示！","密码长度为6-16！",AmendPasswordActivity.this);
			return false;
		}else if (againPwdEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码不能为空！");
			MessageBox.CreateAlertDialog("提示！","请输入确认密码！",AmendPasswordActivity.this);
			return false;
		} else if (!againPwdEt.getText().toString().equals(newPwdEt.getText().toString())) {
//			wornLayout3.setVisibility(View.VISIBLE);
//			wornTv3.setText("两次输入的密码不一致！");
			MessageBox.CreateAlertDialog("提示！","两次输入的密码不一致！",AmendPasswordActivity.this);
			return false;
		} 
		return true;
	}
	Dialog dialog = null;
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.submitBtn){
			if(NetworkCheck.IsHaveInternet(this)){
				if(textValidate()){
					showDialog();
					new Thread(amendPwdRunnable).start();
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
			            dialog.dismiss();
					}
				});
				alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						dialog.dismiss();
					}
				});
				dialog = alert.create();
				dialog.show();
			}
		}else if(v.getId() == R.id.backBtn){
			finish();
		}
	}
	
}
