/**
 * 
 */
package com.qingfengweb.lottery.activity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.fragment.SampleListFragment;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.MessageBox;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.util.StringUtils;
import com.qingfengweb.share.ShareSDK;
import com.qingfengweb.share.SinaWeibo;
import com.qingfengweb.share.TencentZone;

/**
 * @author 刘星星
 * @createDate 2013/11/27
 *用户登录界面
 */
@SuppressLint("HandlerLeak")
public class LoginActivity extends Activity implements OnClickListener{
	public static boolean isActive = false;
	private EditText userNameEt,passwordEt;
	DBHelper dbHelper;
	private ProgressDialog progressdialog;
	public ShareSDK shareSDK = null;
	public String activiyFlag = "";
	public static LoginActivity  loginActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_login);
		isActive = true;//表示当前activity 是存活着的
		findview();
		dbHelper = DBHelper.getInstance(this);
		activiyFlag = getIntent().getStringExtra("activity");
		loginActivity = this;
	}
	private void showDialog(){
		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage("正在登录！请稍候...");
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
	private void findview(){
		findViewById(R.id.registerBtn).setOnClickListener(this);
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.LoginBtn).setOnClickListener(this);
		userNameEt = (EditText) findViewById(R.id.userNameEt);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		findViewById(R.id.sinaLogin).setOnClickListener(this);
		findViewById(R.id.tengLogin).setOnClickListener(this);
		String username = getIntent().getStringExtra("username");
		if(username!=null){
			userNameEt.setText(username);
		}
		MyApplication.getInstance().setCurrentUserName("");
		MyApplication.getInstance().setCurrentPassword("");
		MyApplication.getInstance().setCurrentToken("");
	}
	@Override
	protected void onDestroy() {
		isActive = false;//当activity销毁后 告诉程序当前activity已经死掉了
		super.onDestroy();
	}
	Dialog dialog = null;
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.registerBtn){
			Intent intent = new Intent(this,RegisterActivity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.LoginBtn){
			DeviceTool.disShowSoftKey(this,passwordEt);
			if(NetworkCheck.IsHaveInternet(this)){
				if(textValidate()){
					showDialog();
					new Thread(loginRunnable).start();
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
			            LoginActivity.this.dialog.dismiss();
					}
				});
				alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						LoginActivity.this.dialog.dismiss();
					}
				});
				dialog = alert.create();
				dialog.show();
			}
		}else if(v.getId() == R.id.sinaLogin){//新浪登录
			shareSDK = new SinaWeibo(this, null, handler);
			((SinaWeibo) shareSDK).authorizes();
		}else if(v.getId() == R.id.tengLogin){//腾讯登录
			shareSDK = new TencentZone(this, handler, null);
			shareSDK.authorize();
		}
	}
	public boolean textValidate() {
		if (userNameEt.getText().toString().trim().equals("")) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名不能为空！");
			MessageBox.CreateAlertDialog("提示！","用户名不能为空！",this);
			return false;
		} else if (!StringUtils.checkPhoneNumber(userNameEt.getText().toString().trim()) && !StringUtils.isEmail(userNameEt.getText().toString().trim())) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名的格式不正确！");
			MessageBox.CreateAlertDialog("提示！","用户名的格式不正确！",this);
			return false;
		} else if (passwordEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码不能为空！");
			MessageBox.CreateAlertDialog("提示！","密码不能为空！",this);
			return false;
		} else if (passwordEt.getText().toString().length()>16 || passwordEt.getText().toString().length()<6) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码长度为6-16！");
			MessageBox.CreateAlertDialog("提示！","密码长度为6-16！",this);
			return false;
		} 
		return true;
	}
Runnable loginRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.loginUser(userNameEt.getText().toString(), passwordEt.getText().toString());
			System.out.println("登陆用户返回值--------------------------"+msg);
			handler.sendEmptyMessage(3);
			if(msg.startsWith("{") && msg.length()>3){//登陆成功
				MyApplication.getInstance().setCurrentUserName(userNameEt.getText().toString());
				MyApplication.getInstance().setCurrentPassword(passwordEt.getText().toString());
				JsonData.jsonRegisterUserData(msg, dbHelper.open());
				handler.sendEmptyMessage(5);
			}else if(msg.equals("404")){//访问接口失败
				handler.sendEmptyMessage(0);
			}else if(msg.equals("-5")){//用户不存在
				handler.sendEmptyMessage(1);
			}else if(msg.equals("-901")){//系统错误
				handler.sendEmptyMessage(2);
			}else if(msg.equals("-12")){//密码输入有误
				handler.sendEmptyMessage(4);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				MessageBox.CreateAlertDialog("提示！","访问服务器失败，请检查网络连接或重试！",LoginActivity.this);
			}else if(msg.what == 1){
				MessageBox.CreateAlertDialog("提示！","用户不存在！",LoginActivity.this);
				userNameEt.setText("");
				userNameEt.requestFocus();
			}else if(msg.what == 2){
				MessageBox.CreateAlertDialog("提示！","登录失败！请重试",LoginActivity.this);
			}else if(msg.what == 3){
				progressdialog.dismiss();
			}else if(msg.what == ShareSDK.SUCCESS){//新浪登录成功
				MessageBox.CreateAlertDialog("提示！","登录成功！请重试",LoginActivity.this);
			}else if(msg.what == ShareSDK.ERROR){//新浪登录失败
				MessageBox.CreateAlertDialog("提示！","登录失败！请重试",LoginActivity.this);
			}else if(msg.what == 4){//密码错误
				MessageBox.CreateAlertDialog("提示！","密码错误！",LoginActivity.this);
			}else if(msg.what == 5){
//				Intent intent = new Intent(LoginActivity.this,AmendMyInfoActivity.class);
//				startActivity(intent);
				if(activiyFlag!=null && activiyFlag.equals("FragmentForMyLottery")){
					Message message = new Message();
					message.arg1 = 2;
					message.what = 1;
					SampleListFragment.handler.sendMessage(message);
				}else if(activiyFlag!=null && activiyFlag.equals("ChargeMoneyActivity")){
					Intent intent = new Intent(LoginActivity.this,ChargeMoneyActivity.class);
					LoginActivity.this.startActivity(intent);
				}else if(activiyFlag!=null && activiyFlag.equals("ChargeHistoryActivity")){
					Intent intent = new Intent(LoginActivity.this,ChargeHistoryActivity.class);
					LoginActivity.this.startActivity(intent);
				}else if(activiyFlag!=null && activiyFlag.equals("AmendMyInfoActivity")){
					Intent intent = new Intent(LoginActivity.this,AmendMyInfoActivity.class);
					LoginActivity.this.startActivity(intent);
				}else if(activiyFlag!=null && activiyFlag.equals("AmendPasswordActivity")){
					Intent intent = new Intent(LoginActivity.this,AmendPasswordActivity.class);
					LoginActivity.this.startActivity(intent);
				}
				finish();
			}
			super.handleMessage(msg);
		}
		
	};
}
