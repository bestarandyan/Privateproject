package com.boluomi.children.activity;

import com.boluomi.children.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class SuccessRegisterActivity extends BaseActivity{
	private Button backBtn,loginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_successregister);
		backBtn = (Button) findViewById(R.id.backBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		backBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == backBtn){
			Intent i = new Intent();
			int getValue = getIntent().getIntExtra("activityType",0);
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
				i.setClass(SuccessRegisterActivity.this, TuiJianActivity.class);
				SuccessRegisterActivity.this.startActivity(i);
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				i.setClass(SuccessRegisterActivity.this,AlbumMainActivity.class);
				startActivity(i);
				finish();
			}
		}else if(v == loginBtn){
			Intent i = new Intent();
			int getValue = getIntent().getIntExtra("activityType",0);
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
				i.setClass(SuccessRegisterActivity.this, TuiJianActivity.class);
				SuccessRegisterActivity.this.startActivity(i);
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				i.setClass(SuccessRegisterActivity.this,AlbumMainActivity.class);
				startActivity(i);
				finish();
			}
			
			
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent();
			int getValue = getIntent().getIntExtra("activityType",0);
			if(getValue==0){
				finish();
			}else if(getValue ==1){//从样照欣赏的推荐跳入来的
				i.putExtra("themeMap", getIntent().getSerializableExtra("themeMap"));
				i.setClass(SuccessRegisterActivity.this, TuiJianActivity.class);
				SuccessRegisterActivity.this.startActivity(i);
				finish();
			}else if(getValue == 2){//从主页的我的相册跳入的
				i.setClass(SuccessRegisterActivity.this,AlbumMainActivity.class);
				startActivity(i);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
