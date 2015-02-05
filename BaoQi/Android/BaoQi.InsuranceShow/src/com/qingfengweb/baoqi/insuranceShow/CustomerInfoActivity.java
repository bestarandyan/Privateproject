package com.qingfengweb.baoqi.insuranceShow;

import com.qingfengweb.baoqi.insuranceShow.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

public class CustomerInfoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.customer_layout);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		Intent intent=new Intent(this,InsuranceShowMainActivity.class);
		this.startActivity(intent);
		this.finish();
		return true;
	}
	
}
