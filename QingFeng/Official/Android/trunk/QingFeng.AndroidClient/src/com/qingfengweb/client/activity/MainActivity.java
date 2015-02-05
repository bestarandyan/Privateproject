package com.qingfengweb.client.activity;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qingfengweb.android.R;
import com.qingfengweb.client.bean.DeviceInfo;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.BStationLocation;
import com.qingfengweb.client.data.GPSLocation;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.client.fragmengs.CompanyIntrocductionFragment;
import com.qingfengweb.client.fragmengs.IntroAboutUsFragment;


public class MainActivity extends BaseActivity {
	Location location;
	GPSLocation gpsLocation;
	DBHelper dbHelper = null;
	public MainActivity() {
		super(R.string.left_and_right);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = DBHelper.getInstance(this);
//		gpsLocation = new GPSLocation(this);
//		registerDevice();//设备注册
		setSlidingActionBarEnabled(true);
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		setContentView(R.layout.content_frame);
		fragmentManager
		.beginTransaction()
		.add(R.id.content_frame, companyF)
		.commit();
		
		sm.setSecondaryMenu(R.layout.menu_frame_two);
		sm.setSecondaryShadowDrawable(R.drawable.shadowright);
		fragmentManager
		.beginTransaction()
		.add(R.id.menu_frame_two, new IntroAboutUsFragment())
		.commit();
		currentFragment = CompanyIntrocductionFragment.newInstance("CompanyIntrocductionFragment");
//		new ImageLoadFromUrl().loadDrawable(this, (Integer) null, null, null, new CallbackImpl(new ImageView(this)));
	}
	private void registerDevice(){
		new Thread(gpsLocation).start();//开启GPS定位线程
		String sql = "select state from "+DeviceInfo.TABLE_NAME;
		List<Map<String, Object>> list = dbHelper.selectRow(sql, null);
		if(list!=null && list.size()>0 && list.get(0).get("state").toString().equals("0")){//判断本地数据是否保存了上传成功过的字段
			handler.sendEmptyMessage(1);//如果有，则直接上传位置服务
		}else{// 如果数据库中没有  则开启注册设备的线程
			new Thread(registerDeviceRunnable).start();//开启注册设备的线程
		}
	}
	
	Runnable uploadLocationRunnable = new Runnable(){

		@Override
		public void run() {
			try {
				Thread.sleep(10000);
				location = gpsLocation.getCurrentLoaction();
				if(location==null){//gps定位失败
					gpsLocation.stopGPSLocation();
					BStationLocation bsLocation = new BStationLocation(MainActivity.this);
					String bsString = bsLocation.getLocation();
					if(bsString!=null && bsString.trim().length()>0){//基站地址获取成功
						AccessServer.uploadLocation("2", bsString);
					}else{//基站地址获取失败
						AccessServer.uploadLocation("3", bsString);
					}
				}else{//gps定位成功
					gpsLocation.stopGPSLocation();
					AccessServer.uploadLocation("1", location.getLatitude()+","+location.getLongitude());
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//设备注册成功，将数据保存到本地，并开启上传位置信息的线程。
				ContentValues  contentValues = new ContentValues();
				contentValues.put("state", "0");
				dbHelper.delete(DeviceInfo.TABLE_NAME);
				dbHelper.insert(DeviceInfo.TABLE_NAME, contentValues);
				new Thread(uploadLocationRunnable).start();
			}else if(msg.what == 1){//本地存储的数据显示已经成功注册过本手机，所以直接上传位置信息
				new Thread(uploadLocationRunnable).start();
			}else if(msg.what == 2){//关闭gps定位服务，防止长时间定位，消耗电量。
				gpsLocation.stopGPSLocation();
			}
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 注册设备线程
	 */
	Runnable registerDeviceRunnable = new Runnable() {
		
		@Override
		public void run() {
			String rgStr = AccessServer.registerDevice();
			if(rgStr.equals("0")){//注册设备成功。
				handler.sendEmptyMessage(0);
			}else{
				handler.sendEmptyMessage(2);//注册失败，关闭定位服务。
			}
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(!sm.getMenu().isShown() && !sm.getSecondaryMenu().isShown()){
				showDialog();
			}
		}
		return true;
	}
	
	private void showDialog(){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alerBuilder.setMessage("确定要退出应用程序吗？");
		alerBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.gc();
				finish();
			}
		});
		alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		
		Dialog dialog = alerBuilder.create();
		dialog.show();
	}
	
}
