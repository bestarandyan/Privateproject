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
//		registerDevice();//�豸ע��
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
		new Thread(gpsLocation).start();//����GPS��λ�߳�
		String sql = "select state from "+DeviceInfo.TABLE_NAME;
		List<Map<String, Object>> list = dbHelper.selectRow(sql, null);
		if(list!=null && list.size()>0 && list.get(0).get("state").toString().equals("0")){//�жϱ��������Ƿ񱣴����ϴ��ɹ������ֶ�
			handler.sendEmptyMessage(1);//����У���ֱ���ϴ�λ�÷���
		}else{// ������ݿ���û��  ����ע���豸���߳�
			new Thread(registerDeviceRunnable).start();//����ע���豸���߳�
		}
	}
	
	Runnable uploadLocationRunnable = new Runnable(){

		@Override
		public void run() {
			try {
				Thread.sleep(10000);
				location = gpsLocation.getCurrentLoaction();
				if(location==null){//gps��λʧ��
					gpsLocation.stopGPSLocation();
					BStationLocation bsLocation = new BStationLocation(MainActivity.this);
					String bsString = bsLocation.getLocation();
					if(bsString!=null && bsString.trim().length()>0){//��վ��ַ��ȡ�ɹ�
						AccessServer.uploadLocation("2", bsString);
					}else{//��վ��ַ��ȡʧ��
						AccessServer.uploadLocation("3", bsString);
					}
				}else{//gps��λ�ɹ�
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
			if(msg.what == 0){//�豸ע��ɹ��������ݱ��浽���أ��������ϴ�λ����Ϣ���̡߳�
				ContentValues  contentValues = new ContentValues();
				contentValues.put("state", "0");
				dbHelper.delete(DeviceInfo.TABLE_NAME);
				dbHelper.insert(DeviceInfo.TABLE_NAME, contentValues);
				new Thread(uploadLocationRunnable).start();
			}else if(msg.what == 1){//���ش洢��������ʾ�Ѿ��ɹ�ע������ֻ�������ֱ���ϴ�λ����Ϣ
				new Thread(uploadLocationRunnable).start();
			}else if(msg.what == 2){//�ر�gps��λ���񣬷�ֹ��ʱ�䶨λ�����ĵ�����
				gpsLocation.stopGPSLocation();
			}
			super.handleMessage(msg);
		}
		
	};
	/**
	 * ע���豸�߳�
	 */
	Runnable registerDeviceRunnable = new Runnable() {
		
		@Override
		public void run() {
			String rgStr = AccessServer.registerDevice();
			if(rgStr.equals("0")){//ע���豸�ɹ���
				handler.sendEmptyMessage(0);
			}else{
				handler.sendEmptyMessage(2);//ע��ʧ�ܣ��رն�λ����
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
		alerBuilder.setTitle("��ʾ!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alerBuilder.setMessage("ȷ��Ҫ�˳�Ӧ�ó�����");
		alerBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				android.os.Process.killProcess(android.os.Process.myPid());
				System.gc();
				finish();
			}
		});
		alerBuilder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		
		Dialog dialog = alerBuilder.create();
		dialog.show();
	}
	
}
