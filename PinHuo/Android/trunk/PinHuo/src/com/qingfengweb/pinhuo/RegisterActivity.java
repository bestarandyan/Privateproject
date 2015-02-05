package com.qingfengweb.pinhuo;

import com.qingfengweb.pinhuo.utils.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {
	EditText userNameEt,pswEt,surePswEt,nameEt,companyEt,codeEt;
	TextView codeTv,company_text;
	CheckBox checkBox;
	Button nextBtn;
	ImageButton deleteBtn1,deleteBtn2,deleteBtn3;
	String type = "";//用户类型  1代表货主   2代表司机 3代表专线 
	ScrollView scrollView;
	TextView userNameTishi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_register);
		initView();
	}
	@Override
	public void onClick(View v) {
		if(v == codeTv){//获取验证码按钮
			
		}else if(v == nextBtn){//下一步按钮
			if(userNameEt.getText().toString().equals("")){
				scrollView.scrollTo(0, 0);
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				userNameEt.startAnimation(shake);
				userNameEt.requestFocus();
			}else if(!StringUtils.checkPhoneNumber(userNameEt.getText().toString().trim())){
				scrollView.scrollTo(0, 0);
				Toast.makeText(this, "手机号码格式错误！", 3000).show();
			}else if(pswEt.getText().toString().equals("")){
				scrollView.scrollTo(0, 0);
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				pswEt.startAnimation(shake);
				pswEt.requestFocus();
			}else if(pswEt.getText().toString().length()<6 || pswEt.getText().toString().length()>20){
				scrollView.scrollTo(0, 0);
				Toast.makeText(this, "密码格式错误，由6-20位数字和字母组成", 3000).show();
			}else if(surePswEt.getText().toString().equals("")){
				scrollView.scrollTo(0, 0);
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				surePswEt.startAnimation(shake);
				surePswEt.requestFocus();
			}else if(!surePswEt.getText().toString().equals(pswEt.getText().toString())){
				scrollView.scrollTo(0, 0);
				Toast.makeText(this, "确认密码输入有误！", 3000).show();
			}else if(nameEt.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				nameEt.startAnimation(shake);
				nameEt.requestFocus();
			}else if((type.equals("1") || type.equals("3")) && companyEt.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				companyEt.startAnimation(shake);
				companyEt.requestFocus();
			}else if(codeEt.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				codeEt.startAnimation(shake);
				codeEt.requestFocus();
			}else if(!checkBox.isChecked()){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				checkBox.startAnimation(shake);
			}else{
				Intent intent = new Intent();
				if(type.equals("1")){//货主
					intent.setClass(this, MainActivity.class);
				}else if(type.equals("2")){//司机
					intent.setClass(this, EditInfoActivity.class);
				}else if(type.equals("3")){//专线
					intent.setClass(this, EditInfoActivity.class);
				}
				intent.putExtra("typeUser", type);
				startActivity(intent);
				finish();
			}
		}else if(v == deleteBtn1){//清空手机号码输入框
			userNameEt.setText("");
			deleteBtn1.setVisibility(View.INVISIBLE);
			userNameTishi.setVisibility(View.GONE);
		}else if(v == deleteBtn2){//清空密码输入框
			deleteBtn2.setVisibility(View.INVISIBLE);
			pswEt.setText("");
		}else if(v == deleteBtn3){//清空确认密码输入框
			deleteBtn3.setVisibility(View.INVISIBLE);
			surePswEt.setText("");
		}else if(v.getId() == R.id.backBtn){
			finish();
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
			if(userNameEt.isFocused()){
				if(userNameEt.getText().toString().trim().length()>=1 && deleteBtn1.getVisibility() == View.INVISIBLE){
					deleteBtn1.setVisibility(View.VISIBLE);
					deleteBtn1.setClickable(true);
				}else if(userNameEt.getText().toString().equals("")){
					deleteBtn1.setVisibility(View.INVISIBLE);
					deleteBtn1.setClickable(false);
				}
			}else if(pswEt.isFocused()){
				if(pswEt.getText().toString().trim().length()>=1 && deleteBtn2.getVisibility() == View.INVISIBLE){
					deleteBtn2.setVisibility(View.VISIBLE);
					deleteBtn2.setClickable(true);
				}else if(pswEt.getText().toString().equals("")){
					deleteBtn2.setVisibility(View.INVISIBLE);
					deleteBtn2.setClickable(false);
				}
			}else if(surePswEt.isFocused()){
				if(surePswEt.getText().toString().trim().length()>=1 && deleteBtn3.getVisibility() == View.INVISIBLE){
					deleteBtn3.setVisibility(View.VISIBLE);
					deleteBtn3.setClickable(true);
				}else if(surePswEt.getText().toString().equals("")){
					deleteBtn3.setVisibility(View.INVISIBLE);
					deleteBtn3.setClickable(false);
				}
			}
			
		}
	};
	public void initView(){
		userNameEt = (EditText) findViewById(R.id.input_phone_et);
		pswEt = (EditText) findViewById(R.id.input_psw_et);
		surePswEt = (EditText) findViewById(R.id.input_spsw_et);
		nameEt = (EditText) findViewById(R.id.input_name_et);
		companyEt = (EditText) findViewById(R.id.input_company_name_et);
		codeEt = (EditText) findViewById(R.id.input_code_et);
		codeTv = (TextView) findViewById(R.id.text_reget_code);
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		nextBtn = (Button) findViewById(R.id.nextBtn);
		deleteBtn1 = (ImageButton) findViewById(R.id.deleteUsernameBtn);
		deleteBtn2 = (ImageButton) findViewById(R.id.deletePswBtn);
		deleteBtn3 = (ImageButton) findViewById(R.id.deletesPswBtn);
		company_text = (TextView) findViewById(R.id.company_text);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		findViewById(R.id.backBtn).setOnClickListener(this);
		userNameTishi = (TextView) findViewById(R.id.userNameTishi);
		userNameEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(StringUtils.checkPhoneNumber(userNameEt.getText().toString().trim())){
						userNameTishi.setVisibility(View.VISIBLE);
					}else{
						userNameTishi.setVisibility(View.GONE);
					}
				}
			}
		});
		codeTv.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		deleteBtn1.setOnClickListener(this);
		deleteBtn2.setOnClickListener(this);
		deleteBtn3.setOnClickListener(this);
		userNameEt.addTextChangedListener(watcher);
		pswEt.addTextChangedListener(watcher);
		surePswEt.addTextChangedListener(watcher);
		type = getIntent().getStringExtra("typeUser");
		if(type.equals("1") || type.equals("3")){//货主或专线
			company_text.setVisibility(View.VISIBLE);
			companyEt.setVisibility(View.VISIBLE);
		}else if(type.equals("2")){//司机
			company_text.setVisibility(View.GONE);
			companyEt.setVisibility(View.GONE);
		}
	}
}
