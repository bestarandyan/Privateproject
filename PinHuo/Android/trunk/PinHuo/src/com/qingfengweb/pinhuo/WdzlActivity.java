package com.qingfengweb.pinhuo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class WdzlActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_wdzl);
		initView();//初始化控件
	}
	public void initView(){
		findViewById(R.id.backBtn).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}
		super.onClick(v);
	}
}
