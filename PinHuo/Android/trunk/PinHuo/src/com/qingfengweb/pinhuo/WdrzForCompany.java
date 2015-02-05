package com.qingfengweb.pinhuo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class WdrzForCompany extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_wdrz_for_company);
		initView();
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}
		super.onClick(v);
	}
	public void initView(){
		findViewById(R.id.backBtn).setOnClickListener(this);
	}

}
