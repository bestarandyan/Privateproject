package com.qingfengweb.id.blm_goldenLadies;


import com.qingfengweb.id.blm_goldenLadies.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WeiXinSuccessActivity  extends BaseActivity{
	private Button backBtn;
	private TextView title;
	private TextView content;
	private int flag = 1;//用来标记是从哪一个Activity跳转过来的 1代表从微信跳转过来， 2代表从积分兑换跳转过来。
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_weixinsuccess);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
		flag = getIntent().getIntExtra("type", 1);
		if( flag == 1){
			title.setText("微信");
			content.setText("恭喜您提交成功！");
		}else if(flag == 2){
			title.setText("积分兑换");
			content.setText("恭喜您兑换成功！");
		}
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			if(flag == 1){
				Intent intent = new Intent(this, WeiXinActivity.class);
				startActivity(intent);
				finish();
			}else if(flag ==2 ){
				Intent intent = new Intent(this, IntegralStoreMainActivity.class);
				startActivity(intent);
				finish();
			}
			
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(flag == 1){
				Intent intent = new Intent(this, WeiXinActivity.class);
				startActivity(intent);
				finish();
			}else if(flag ==2 ){
				Intent intent = new Intent(this, IntegralStoreMainActivity.class);
				startActivity(intent);
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
