package com.zhihuigu.sosoOffice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SoSoOfficeService extends Service {
	
	private boolean THREAD_TAG = false;//false线程停止，true线程开始

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		THREAD_TAG = true;
		new Thread(runnable).start();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		THREAD_TAG = false;
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			while(THREAD_TAG){
				try {
					Thread.sleep(10*1000);
					System.out.println("正在更新数据");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

}
