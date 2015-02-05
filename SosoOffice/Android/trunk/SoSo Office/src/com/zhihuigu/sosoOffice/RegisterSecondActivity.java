package com.zhihuigu.sosoOffice;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterSecondActivity extends BaseActivity{
	//下一步按钮
	private  Button submitBtn;
	//以下控件  以type打头的代表三种用户的大图    image打头的代表勾
	private ImageView type1,type2,type3,image1,image2,image3;
	
	private int userType = -1;//代表用户类型
	
	private SoSoUploadData uploaddata;// 注册请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private TextView introUserType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_registersecond);
		findView();
		if((MyApplication.getInstance().getRoleid()==UserType.UserTypeCustomer.getValue()
				||MyApplication.getInstance().getRoleid()==UserType.UserTypeOwner.getValue()
				||MyApplication.getInstance().getRoleid()==UserType.UserTypeIntermediary.getValue()
				)){
			switch(MyApplication.getInstance().getRoleid()){
			case 4:
				image3.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image1.setVisibility(View.GONE);
			userType = Constant.TYPE_CLIENT;//客户
			introUserType.setText(getResources().getString(R.string.client_intro_text));
			break;
			case 1:
				image1.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_PROPRIETOR;//业主
			introUserType.setText(getResources().getString(R.string.proprietor_intro_text));
				break;
			case 2:
				image2.setVisibility(View.VISIBLE);
			image1.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_AGENCY;//中介
			introUserType.setText(getResources().getString(R.string.agency_intro_text));
				break;
			}
		}
	}
	private void findView(){
		submitBtn = (Button) findViewById(R.id.submitBtn);
		type1 = (ImageView) findViewById(R.id.type1);
		type2 = (ImageView) findViewById(R.id.type2);
		type3 = (ImageView) findViewById(R.id.type3);
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		image3 = (ImageView) findViewById(R.id.image3);
		introUserType = (TextView) findViewById(R.id.introUserType);
		submitBtn.setOnClickListener(this);
		type1.setOnClickListener(this);
		type2.setOnClickListener(this);
		type3.setOnClickListener(this);
	}
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		String userid = "0";
		if(MyApplication.getInstance().getSosouserinfo()!=null&&MyApplication.getInstance().getSosouserinfo().getUserID()!=null){
			userid = MyApplication.getInstance().getSosouserinfo().getUserID();
		}
		ContentValues values = new ContentValues();
		values.put("roleid", MyApplication.getInstance().getRoleid());
		DBHelper.getInstance(this).update(
				"sososettinginfo",
				values,
				"userid = ?",
				new String[] {userid});
		if (values != null) {
			values.clear();
			values = null;
		}
		super.onDestroy();
	}
	
	/**
	 * author by Ring 注册前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if(userType == -1){
			MessageBox.CreateAlertDialog("对不起，你还未选择用户类型无法进行下一步！",
					RegisterSecondActivity.this);
			return false;
		} else if (MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null) {
			ContentValues values = new ContentValues();
			values.put("roleid", userType);
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] {"0"});
			if (values != null) {
				values.clear();
				values = null;
			}
			Intent i = new Intent();
			MyApplication.getInstance().setRoleid(userType);
			i.putExtra("tag", 1);
			if(ApplicationSettingInfo.initSetting(RegisterSecondActivity.this,"0")){
				i.setClass(RegisterSecondActivity.this, CityListActivity.class);
			}else{
				i.setClass(RegisterSecondActivity.this, MainTabActivity.class);
			}
			RegisterSecondActivity.this.startActivity(i);
			RegisterSecondActivity.this.finish();
			return false;
		} 
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == submitBtn){
			if(textValidate()){
				new Thread(runnable).start();
			}
		}else if(v == type1){
			image1.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_PROPRIETOR;//业主
			introUserType.setText(getResources().getString(R.string.proprietor_intro_text));
		}else if(v == type2){
			image2.setVisibility(View.VISIBLE);
			image1.setVisibility(View.GONE);
			image3.setVisibility(View.GONE);
			userType = Constant.TYPE_AGENCY;//中介
			introUserType.setText(getResources().getString(R.string.agency_intro_text));
		}else if(v == type3){
			image3.setVisibility(View.VISIBLE);
			image2.setVisibility(View.GONE);
			image1.setVisibility(View.GONE);
			userType = Constant.TYPE_CLIENT;//客户
			introUserType.setText(getResources().getString(R.string.client_intro_text));
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(getIntent().getIntExtra("tag", 0)==1){
				showExitDialog(this);
			}else{
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
		return true;
	}
	
	/**
	 * author by Ring 更改用户类型
	 * return true更改成功，false 更改失败
	 */
	public boolean alterUserinfo() {
		if(MyApplication.getInstance(this).getSosouserinfo()!=null
				&&MyApplication.getInstance(this).getSosouserinfo().getUserID()!=null){
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
					this).getAPPID()));
			params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
					this).getAPPKEY()));
			params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(this).getSosouserinfo().getUserID()));
			params.add(new BasicNameValuePair("RoleID", userType + ""));
			uploaddata = new SoSoUploadData(this, "UserUpdateRoleID.aspx", params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse(userType);
			params.clear();
			params = null;
			if (StringUtils.CheckReponse(reponse)) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
		
	}
	/**
	 * author by Ring 处理更改用户类型的结构
	 */
	public void dealReponse(int roleid) {
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_userinfo set soso_roleid = '"+roleid+"' where soso_userid = '"
		+MyApplication.getInstance(this).getSosouserinfo().getUserID()+"'");
			MyApplication.getInstance(this).getSosouserinfo().setRoleID(roleid);
			MyApplication.getInstance(this).setRoleid(roleid);
		}
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
			if (NetworkCheck.IsHaveInternet(RegisterSecondActivity.this)) {
				handler.sendEmptyMessage(5);
				boolean b = alterUserinfo();
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
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 从注册界面2跳转到注册成功界面3
				if(MyApplication.getInstance(RegisterSecondActivity.this).getRoleid()==Constant.TYPE_CLIENT){
					i.putExtra("tag", 1);
					if(ApplicationSettingInfo.initSetting(RegisterSecondActivity.this,MyApplication.getInstance(RegisterSecondActivity.this)
							.getSosouserinfo().getUserID())){
						i.setClass(RegisterSecondActivity.this, CityListActivity.class);
						RegisterSecondActivity.this.startActivity(i);
						RegisterSecondActivity.this.finish();
					}else{
						if(MyApplication.getInstance().getNotlogin()==1){
							MyApplication.getInstance().setNotlogin(0);
							RegisterSecondActivity.this.finish();
						}else{
							i.setClass(RegisterSecondActivity.this, MainTabActivity.class);
							RegisterSecondActivity.this.startActivity(i);
							RegisterSecondActivity.this.finish();
						}
						
						
					}
				}else{
					i.setClass(RegisterSecondActivity.this,
							RegisterThirdActivity.class);
					i.putExtra("userType", userType);
					RegisterSecondActivity.this.startActivity(i);
					RegisterSecondActivity.this.finish();
				}
				
				break;
			case 2:// 从注册界面跳转到登录界面
//				i.setClass(RegisterFirstActivity.this, LoginActivity.class);
//				RegisterFirstActivity.this.startActivity(i);
//				RegisterFirstActivity.this.finish();
				break;
			case 3:// 注册失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else {
					message = getResources().getString(R.string.dialog_message_submit_error);
				}
				MessageBox.CreateAlertDialog(message,
						RegisterSecondActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(RegisterSecondActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(RegisterSecondActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
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
			}
		};
	};
}
