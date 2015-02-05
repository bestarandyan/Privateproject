package com.qingfengweb.piaoguanjia.ticketverifier;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		TicketApplication.widthPixels = dm.widthPixels;
		TicketApplication.heightPixels = dm.heightPixels;
		System.out.println("宽："+dm.widthPixels+"-高："+dm.heightPixels);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent i = new Intent(SplashActivity.this,LoginActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
				startActivity(i);
				finish();
			}
		}, 2000);
	}
}
