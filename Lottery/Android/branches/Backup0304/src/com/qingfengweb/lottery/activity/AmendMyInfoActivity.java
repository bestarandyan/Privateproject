/**
 * 
 */
package com.qingfengweb.lottery.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.bean.UserBean;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.MessageBox;
import com.qingfengweb.lottery.util.NetworkCheck;
import com.qingfengweb.lottery.util.StringUtils;

/**
 * @author 刘星星
 *
 */
@SuppressLint("HandlerLeak")
public class AmendMyInfoActivity  extends Activity implements OnClickListener{
	private EditText nickNameEt,realNameEt,identityEt,phoneEt;
	Button submitBtn;
	private ProgressDialog progressdialog;
	DBHelper dbHelper = null;
	TextView goBackBtn;//以后再说按钮
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_amendmyinfo);
		findview();
		dbHelper = DBHelper.getInstance(this);
	}
	private void findview(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		nickNameEt = (EditText) findViewById(R.id.nichenEt);
		realNameEt = (EditText) findViewById(R.id.nameEt);
		identityEt = (EditText) findViewById(R.id.idNumberEt);
		phoneEt = (EditText) findViewById(R.id.phonenumberEt);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		goBackBtn = (TextView) findViewById(R.id.goBack);
		goBackBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		String nickName = MainActivity.userMap.get("nick_name").toString();
		String realName = MainActivity.userMap.get("real_name").toString();
		String idnumber = MainActivity.userMap.get("identity_card").toString();
		String phonenumber = MainActivity.userMap.get("mobile").toString();
		if(nickName!=null && !nickName.equals("null")){
			nickNameEt.setText(nickName);
		}
		if(realName!=null && !realName.equals("null")){
			realNameEt.setText(realName);
		}
		if(idnumber!=null && !idnumber.equals("null")){
			identityEt.setText(idnumber);
		}
		if(phonenumber!=null && !phonenumber.equals("null")){
			phoneEt.setText(phonenumber);
		}
	}
	Dialog dialog = null;
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v == submitBtn){
			if(NetworkCheck.IsHaveInternet(this)){
				if(textValidate()){
					DeviceTool.disShowSoftKey(this,phoneEt);
					showDialog();
					new Thread(amendInfoRunnable).start();
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
			            AmendMyInfoActivity.this.dialog.dismiss();
					}
				});
				alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						AmendMyInfoActivity.this.dialog.dismiss();
					}
				});
				dialog = alert.create();
				dialog.show();
			}
		}else if(v == goBackBtn){
			if(LoginActivity.loginActivity!=null && !LoginActivity.loginActivity.isFinishing()){
				LoginActivity.loginActivity.finish();
			}
			finish();
		}
	}
	public boolean textValidate() {
		if (nickNameEt.getText().toString().trim().equals("")) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名不能为空！");
			MessageBox.CreateAlertDialog("提示！","昵称不能为空！",this);
			return false;
		} else if (nickNameEt.getText().toString().trim().length()<2 || nickNameEt.getText().toString().trim().length()>15) {
//			wornLayout1.setVisibility(View.VISIBLE);
//			wornTv1.setText("用户名的格式不正确！");
			MessageBox.CreateAlertDialog("提示！","昵称长度在2-15之间！",this);
			return false;
		} else if (realNameEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码不能为空！");
			MessageBox.CreateAlertDialog("提示！","真实姓名不能为空！",this);
			return false;
		} else if (identityEt.getText().toString().trim().equals("")) {
//			wornLayout2.setVisibility(View.VISIBLE);
//			wornTv2.setText("密码长度为6-16！");
			MessageBox.CreateAlertDialog("提示！","身份证号不能为空！",this);
			return false;
		} else if (!StringUtils.isIdentityNumber(identityEt.getText().toString().trim())) {
//			wornLayout3.setVisibility(View.VISIBLE);
//			wornTv3.setText("两次输入的密码不一致！");
			MessageBox.CreateAlertDialog("提示！","输入的身份证号码格式不匹配！",this);
			return false;
		} else if (!StringUtils.checkPhoneNumber(phoneEt.getText().toString().trim())) {
//			wornLayout3.setVisibility(View.VISIBLE);
//			wornTv3.setText("两次输入的密码不一致！");
			MessageBox.CreateAlertDialog("提示！","输入的手机号码格式不匹配！",this);
			return false;
		} 
		return true;
	}
	private void showDialog(){
		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage("正在提交请求，请稍候...");
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(true);
		progressdialog.show();
	}
	Runnable amendInfoRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.amendUserInfo(nickNameEt.getText().toString().trim(), realNameEt.getText().toString().trim(),
					identityEt.getText().toString().trim(), phoneEt.getText().toString().trim());
			System.out.println("修改用户资料返回值--------------------------"+msg);
			if(msg.equals("1")){//信息完善成功
				ContentValues values = new ContentValues();
				values.put("nick_name", nickNameEt.getText().toString().trim());
				values.put("real_name", realNameEt.getText().toString().trim());
				values.put("identity_card", identityEt.getText().toString().trim());
				values.put("mobile", phoneEt.getText().toString().trim());
				MainActivity.userMap.put("nick_name", nickNameEt.getText().toString().trim());
				MainActivity.userMap.put("real_name", realNameEt.getText().toString().trim());
				MainActivity.userMap.put("identity_card", identityEt.getText().toString().trim());
				MainActivity.userMap.put("mobile", phoneEt.getText().toString().trim());
				DBHelper dbhelper = DBHelper.getInstance(AmendMyInfoActivity.this);
				dbhelper.update(UserBean.tbName, values, "username=?", new String[]{MyApplication.getInstance().getCurrentUserName()});
				handler.sendEmptyMessage(5);
			}else if(msg.equals("404")){//访问接口失败
				handler.sendEmptyMessage(0);
			}else if(msg.equals("-100")){//用户标识不存在
				handler.sendEmptyMessage(1);
			}else if(msg.equals("-7")){//用户标识不存在
				handler.sendEmptyMessage(4);
			}else if(msg.equals("-901")){//系统错误
				handler.sendEmptyMessage(2);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				progressdialog.dismiss();
				MessageBox.CreateAlertDialog("提示！","访问服务器失败，请检查网络连接或重试！",AmendMyInfoActivity.this);
			}else if(msg.what == 1){
				progressdialog.dismiss();
				MessageBox.CreateAlertDialog("提示！","该用户名已被注册！",AmendMyInfoActivity.this);
			}else if(msg.what == 2){
				progressdialog.dismiss();
				MessageBox.CreateAlertDialog("提示！","注册失败！请重试",AmendMyInfoActivity.this);
			}else if(msg.what == 3){
				progressdialog.dismiss();
			}else if(msg.what == 4){
				progressdialog.dismiss();
				MessageBox.CreateAlertDialog("提示！","身份证号已经存在！",AmendMyInfoActivity.this);
			}else if(msg.what == 5){
				progressdialog.dismiss();
				if(LoginActivity.loginActivity!=null && !LoginActivity.loginActivity.isFinishing()){
					LoginActivity.loginActivity.finish();
				}
				finish();
			}else if(msg.what == 102){//token超时
				progressdialog.dismiss();
				Toast.makeText(getApplicationContext(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(AmendMyInfoActivity.this,LoginActivity.class);
				intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
				startActivity(intent);
			}
			super.handleMessage(msg);
		}
		
	};
}
