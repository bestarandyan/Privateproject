package com.qingfengweb.lottery.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.NetworkCheck;

@SuppressLint("HandlerLeak")
public class LoadingActivity extends Activity{
	AlertDialog alert;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_loading);
		if (!NetworkCheck.IsHaveInternet(LoadingActivity.this)) {//检查是否有网络连接
			handler.sendEmptyMessageDelayed(0, 600);
		}else{
			new Thread(getServerTimeRunnable).start();
		}
	}
	Runnable getServerTimeRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.getServerTime();
			if(msg.length()>4){//获取时间成
				MyApplication.getInstance().setCurrentServerTime(msg);
			}
			handler.sendEmptyMessageDelayed(0, 600);
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
			super.handleMessage(msg);
		}
		
	};
}
