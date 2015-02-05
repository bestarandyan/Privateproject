package com.qingfengweb.pinhuo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ApproveDriverActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_approve_driver);
		initView();
	}
	private void initView(){
		findViewById(R.id.nextBtn).setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.nextBtn){
			Intent intent = new Intent(this,SuccessApproveActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
		super.onClick(v);
	}

}
