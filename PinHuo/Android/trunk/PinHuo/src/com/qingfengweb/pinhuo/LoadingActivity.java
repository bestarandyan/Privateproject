package com.qingfengweb.pinhuo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.qingfengweb.pinhuo.datamanage.MyApplication;

public class LoadingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_loading);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.getInstance().setScreenW(dm.widthPixels);
		MyApplication.getInstance().setScreenH(dm.heightPixels);
		System.out.println("手机屏幕宽度"+dm.widthPixels);
		System.out.println("手机屏幕高低"+dm.heightPixels);
		handler.sendEmptyMessageDelayed(1, 2000);
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent(LoadingActivity.this,LoginActivity.class);
			startActivity(intent);
			finish();
//			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			super.handleMessage(msg);
		}
		
	};
	
}
