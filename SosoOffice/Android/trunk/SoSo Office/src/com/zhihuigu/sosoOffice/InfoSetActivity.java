package com.zhihuigu.sosoOffice;

import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InfoSetActivity extends BaseActivity{
	private Button backBtn;//返回按钮
	private TextView textView;//属性名称
	private EditText editText;//输入框
	private Button submitBtn;//提交按钮
	private TextView title;//标题
	private int type = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_infoset);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.textView);
		editText = (EditText) findViewById(R.id.NameEt);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		initView();
	}
	private void initView(){
		editText.setText(getIntent().getStringExtra("value"));
		 type = getIntent().getIntExtra("type", 1);
		switch(type){
		case 1:
			title.setText("真实姓名");
			textView.setText("真实姓名");
			editText.setHint("请输入您的真实姓名");
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 2:
			title.setText("性别");
			textView.setText("性别");
			editText.setHint("请输入您的性别");
			break;
		case 3:
			title.setText("生日");
			textView.setText("生日");
			editText.setHint("请输入您的生日");
			break;
		case 4:
			title.setText("电子邮箱");
			textView.setText("电子邮箱");
			editText.setHint("请输入您的电子邮箱");
			break;
		case 5:
			title.setText("手机号码");
			textView.setText("手机号码");
			editText.setHint("请输入您的手机号码");
			editText.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		}
	}
	
	
	/**
	 * author by Ring 注册前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (editText.getText().toString().trim().equals("")&&type==5) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.telephone_null),
					this);
			return false;
		} else if (!StringUtils
						.isCellphone(editText.getText().toString().trim())&&type==5) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.phone_error),
					this);
			return false;
		}else if (editText.getText().toString().trim().equals("")&&type==4) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.email_null),
					this);
			return false;
		}  else if (!StringUtils.isEmail(editText.getText().toString().trim())&&type==4) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.email_error),
					this);
			return false;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v == submitBtn){
			if(textValidate()){
				Intent intent = new Intent();
				intent.putExtra("value", editText.getText().toString());
				intent.putExtra("type", type);
				setResult(RESULT_OK, intent);
				finish();
			}
			
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
}
