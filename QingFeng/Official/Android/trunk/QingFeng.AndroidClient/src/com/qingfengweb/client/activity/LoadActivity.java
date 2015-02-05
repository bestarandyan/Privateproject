/**
 * 
 */
package com.qingfengweb.client.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.qingfengweb.android.R;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.data.MyApplication;
import com.qingfengweb.client.database.DBHelper;

/**
 * @author ¡ı–«–«
 * @createDate 2013/6/8
 *
 */
public class LoadActivity extends Activity{
	DBHelper dbHelper = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_load);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.getInstant().setScreenW(dm.widthPixels);
		MyApplication.getInstant().setScreenH(dm.heightPixels);
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);  
		MyApplication.getInstant().setDevice_brand(android.os.Build.BRAND);
		MyApplication.getInstant().setDevice_model(android.os.Build.MODEL);
		MyApplication.getInstant().setDevice_osversion(android.os.Build.VERSION.RELEASE);
//		String s2 = System.getProperty("os.name");
		MyApplication.getInstant().setDevice_osname("Android");
		
		MyApplication.getInstant().setDevice_token(tm.getDeviceId());
		
		dbHelper = DBHelper.getInstance(this);
		new Thread(runnable).start();
	}
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
//			2013-07-29 00:00:00
//			try {
//				Thread.sleep(1000);
				String sql = "select * from "+UpdateSystemTime.tableName+" where type='0'";
				List<Map<String,Object>> sysList = dbHelper.selectRow(sql, null);
				if(sysList!=null && sysList.size()>0){
					String msg = AccessServer.getUpdateContent(sysList.get(0).get("systime").toString());
					System.out.println(msg+"");
					if(msg.substring(0, 1).equals("{") || msg.substring(0, 1).equals("[")){
						JsonData.jsonUpdateTimeData(msg, LoadActivity.this, dbHelper.open());
					}else if(AccessServer.isNoData(msg)){
						String state = msg.split(",")[0];
						String updateTime = msg.split(",")[1];
						System.out.println(state);
						System.out.println(updateTime);
					}
					handler.sendEmptyMessage(0);
				}else{
					String msg = AccessServer.getUpdateContent("2013-07-29 10:00:00");
					System.out.println(msg+"");
					if(msg.length() == 0) return;
					if(msg.substring(0, 1).equals("{") || msg.substring(0, 1).equals("[")){
						JsonData.jsonUpdateTimeData(msg, LoadActivity.this, dbHelper.open());
					}else if(msg.equals("404")){
						
					}else{
						String[] m = msg.split(",");
						String state = m[0];
						String updateTime = m[1];
						System.out.println(state);
						System.out.println(updateTime);
					}
//					JsonData.jsonUpdateTimeData(msg, LoadActivity.this, dbHelper.open());
					handler.sendEmptyMessage(0);
				}
				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				Intent intent = new Intent(LoadActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
			super.handleMessage(msg);
		}
		
	};
}
