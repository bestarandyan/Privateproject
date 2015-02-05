package com.qingfengweb.pinhuo;

import com.qingfengweb.pinhuo.datamanage.MyApplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MyActivity extends BaseActivity {
	RelativeLayout wdzlLayout,myRenzheng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_my);
		initView();
	}

	private void initView(){
		wdzlLayout = (RelativeLayout) findViewById(R.id.myZiliao);
		wdzlLayout.setOnClickListener(this);
		myRenzheng = (RelativeLayout) findViewById(R.id.myRenzheng);
		myRenzheng.setOnClickListener(this);
		findViewById(R.id.backBtn).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v == wdzlLayout){
			Intent intent = new Intent(this,WdzlActivity.class);
			startActivity(intent);
		}else if(v == myRenzheng){
			Intent intent = new Intent();
			if(MyApplication.getInstance().getTypeUser() == 0){
				intent.setClass(this,WdrzForDriverActivity.class);
			}else if(MyApplication.getInstance().getTypeUser() == 1){
				intent.setClass(this,WdrzForCompany.class);
			}
			startActivity(intent);
		}else if(v.getId() == R.id.backBtn){
			finish();
		}
		super.onClick(v);
	}
	
}
