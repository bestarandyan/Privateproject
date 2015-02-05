package com.qingfengweb.pinhuo;


import com.qingfengweb.pinhuo.datamanage.MyApplication;
import com.qingfengweb.pinhuo.utils.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author qingfeng。刘星星
 * @createDate 2014、4、2
 * 登陆拼货网
 *
 */
public class LoginActivity extends BaseActivity{
	EditText usernameEt,pswEt;//用户名和密码的输入框
	ImageButton deletePhoneBtn;//删除用户名的按钮
	Button loginBtn;//登陆按钮
	TextView registerNewAccount,forgetPsw;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_login);
		initView();
	}
	/**
	 * 初始化控件
	 */
	public void initView() {
		usernameEt = (EditText) findViewById(R.id.input_phone_et);
		pswEt = (EditText) findViewById(R.id.input_psw_et);
		deletePhoneBtn = (ImageButton) findViewById(R.id.deleteBtn);
		loginBtn = (Button) findViewById(R.id.btn_login);
		registerNewAccount = (TextView) findViewById(R.id.register_new_account);
		forgetPsw = (TextView) findViewById(R.id.forget_psw);
		loginBtn.setOnClickListener(this);
		deletePhoneBtn.setOnClickListener(this);
		registerNewAccount.setOnClickListener(this);
		forgetPsw.setOnClickListener(this);
		usernameEt.addTextChangedListener(watcher);
	}
	/**
	 * 数据初始化
	 */
	public void initData() {
		
		
	}
	public int backFlag = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(backFlag == 0){
				handler.sendEmptyMessageDelayed(1, 2000);
				backFlag = 1;
				Toast.makeText(this, "再按一次，退出56拼货网！", 2000).show();
			}else if(backFlag == 1){
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){//过两秒后返回键按的第一次失效
				backFlag =0;
			}
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onClick(View v) {
		if(v == loginBtn){//登陆按钮事件
			if(usernameEt.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				usernameEt.startAnimation(shake);
			}else if(!StringUtils.checkPhoneNumber(usernameEt.getText().toString().trim())){
				Toast.makeText(this, "手机号码格式错误！", 3000).show();
			}else if(pswEt.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				pswEt.startAnimation(shake);
			}else{
				MyApplication.getInstance().setTypeUser(0);
				Intent intent = new Intent(this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		}else if(v == deletePhoneBtn){//删除手机号码事件
			usernameEt.setText("");
			pswEt.setText("");
			deletePhoneBtn.setVisibility(View.INVISIBLE);
		}else if(v == registerNewAccount){//注册新账号事件
			Intent intent = new Intent(this,SelectUserTypeActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(v == forgetPsw){//忘记密码事件
			
		}
		super.onClick(v);
	}
	TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(usernameEt.getText().toString().trim().length()>=1 && deletePhoneBtn.getVisibility() == View.INVISIBLE){
				deletePhoneBtn.setVisibility(View.VISIBLE);
				deletePhoneBtn.setClickable(true);
			}else if(usernameEt.getText().toString().equals("")){
				deletePhoneBtn.setVisibility(View.INVISIBLE);
				deletePhoneBtn.setClickable(false);
			}
		}
	};
}
