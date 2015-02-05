package com.qingfengweb.baoqi.hospitalquery;

import android.app.Application;
import android.widget.Toast;

import com.baidu.mapapi.*;

public class BMapApiDemoApp extends Application {
	
	static BMapApiDemoApp mDemoApp;
	
	//�ٶ�MapAPI�Ĺ�����
	public BMapManager mBMapMan = null;
	
	// ��ȨKey
	// TODO: ����������Key,
	// �����ַ��http://dev.baidu.com/wiki/static/imap/key/
	public String mStrKey = "7959E752C81B3DD04058CDD5FF04330FF0DF93ED";
	boolean m_bKeyRight = true;	// ��ȨKey��ȷ����֤ͨ��
	
	// �����¼���������������ͨ�������������Ȩ��֤�����
	public static class MyGeneralListener implements MKGeneralListener {
		public void onGetNetworkState(int iError) {
			Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), "���������������",
					Toast.LENGTH_LONG).show();
		}

		public void onGetPermissionState(int iError) {
			if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
				// ��ȨKey����
				Toast.makeText(BMapApiDemoApp.mDemoApp.getApplicationContext(), 
						"����BMapApiDemoApp.java�ļ�������ȷ����ȨKey��",
						Toast.LENGTH_LONG).show();
				BMapApiDemoApp.mDemoApp.m_bKeyRight = false;
			}
		}
		
	}
	
	@Override
    public void onCreate() {
		mDemoApp = this;
		mBMapMan = new BMapManager(this);
		mBMapMan.init(this.mStrKey, new MyGeneralListener());
		super.onCreate();
	}
	@Override
	//��������app���˳�֮ǰ����mapadpi��destroy()�����������ظ���ʼ��������ʱ������
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onTerminate();
	}

}