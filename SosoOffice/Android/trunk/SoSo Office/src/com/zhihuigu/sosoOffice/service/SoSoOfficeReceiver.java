package com.zhihuigu.sosoOffice.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SoSoOfficeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//����һ��service����ά��sosooffice��״̬������
		int tag = intent.getIntExtra("tag", 1);
		Intent serviceIntent = new Intent(context,SoSoOfficeService.class);
		if(tag==1){
			context.startService(serviceIntent);
		}else{
			context.stopService(serviceIntent);
		}
		
	}

}
