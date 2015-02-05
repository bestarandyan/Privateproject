package com.qingfengweb.baoqi.electroncollection;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.qingfengweb.baoqi.collectInfo.CollCarInfo;
import com.qingfengweb.baoqi.collectInfo.CollGongChengXian;
import com.qingfengweb.baoqi.collectInfo.CollJiSunXian;
import com.qingfengweb.baoqi.collectInfo.CollQiCaiXian;
import com.qingfengweb.baoqi.collectInfo.CollectInfoMainActivity;
import com.qingfengweb.baoqi.collectInfo.CollectInfoMainActivity.MySearchListener;
import com.qingfengweb.baoqi.hospitalquery.BMapApiDemoApp;
import com.qingfengweb.baoqi.propertyInsurance.PropertyInsuranceMainActivity;
import com.qingfengweb.baoqi.propertyInsurance.R;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ElectronMainActivity extends MapActivity  implements OnClickListener{
	private Button bt1,bt2;
	public BMapManager mapManager;
	public MKSearch mMKSearch;
	public MapView mMapView = null;
	public LocationListener myLocationListener = null;//createʱע���listener��Destroyʱ��ҪRemove
	public Double jingdu=null,weidu=null;
	RelativeLayout relative;
	public GeoPoint pt;
	BMapApiDemoApp app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.electronmain);
		
		bt1=(Button) findViewById(R.id.collBtn1);
		bt2=(Button) findViewById(R.id.collBtn2);
	

	bt1.setOnClickListener(this);
	bt2.setOnClickListener(this);
	MyLocationFun();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
       Intent intent = new Intent();
		intent.putExtra("dangqiandizhi", MyLocationFun());
		if(v == bt1){
			//Toast.makeText(getApplicationContext(), MyLocationFun(), Toast.LENGTH_LONG).show();
			intent.setClass(ElectronMainActivity.this, ElectrolCarXian.class);
			
		}else if(v == bt2){
			intent.setClass(ElectronMainActivity.this, ElectronCaiChan.class);
		}
		startActivity(intent);
		finish();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(ElectronMainActivity.this,PropertyInsuranceMainActivity.class);
			
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	

		/*
		 * ��ǰ��γ�ȶ�λ
		 * �����ݾ�γ�ȶ�λ��ǰ�ֵ���Ϣ
		 */
	 String jingwei=null;
	 public String MyLocationFun(){
		 BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
			if (app.mBMapMan == null) {
				app.mBMapMan = new BMapManager(getApplication());
				app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
			}
			app.mBMapMan.start();
			
			mMKSearch = new MKSearch();  
			
			mapManager = new BMapManager(getApplication());
			mapManager.init(app.mStrKey, null);  
			mMKSearch.init(mapManager, new MySearchListener());  
	        // ע�ᶨλ�¼�
	        myLocationListener = new LocationListener(){
				public void onLocationChanged(Location location) {
				
					jingdu=location.getLongitude();
					weidu=location.getLatitude();
					if(location != null){
						String strLog = String.format("����ǰ��λ��:\r" +
								"γ��:%f\r" +
								"����:%f",
								location.getLongitude(), location.getLatitude());
						 try {  
			                    // ���û�����ľ�γ��ֵת����int����   
			                    int longitude = (int) (1E6 * jingdu);  
			                    int latitude = (int) (1E6 *weidu);  
			                    // ��ѯ�þ�γ��ֵ����Ӧ�ĵ�ַλ����Ϣ   
			                    
			                    mMKSearch.reverseGeocode(new GeoPoint(latitude, longitude));  
			                    
			                } catch (Exception e) {  
			                	jingwei="��ѯ��������������ľ�γ��ֵ��";  
			                }  
						

					}
				}
	        };
	       
	       return jingwei;
	 }
	 /** 
	  * ʵ��MKSearchListener�ӿ�,����ʵ���첽�������񣬵õ�������� 
	  *  
	  * @author liufeng 
	  */  
	 public class MySearchListener implements MKSearchListener {  
	     /** 
	      * ���ݾ�γ��������ַ��Ϣ��� 
	      * @param result ������� 
	      * @param iError ����ţ�0��ʾ��ȷ���أ� 
	      */  
	 
		
	        public void onGetAddrResult(MKAddrInfo result, int iError) {  
	        	if (result == null) {  
	                return;  
	            }  
	            StringBuffer sb = new StringBuffer();  
	            // ��γ������Ӧ��λ��   
	            sb.append(result.strAddr);  
	            jingwei=sb.toString();
	        }  

	   
	     /** 
	      * �ݳ�·��������� 
	      * @param result ������� 
	      * @param iError ����ţ�0��ʾ��ȷ���أ� 
	      */  
	     
	     public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {}  
	   
	     /** 
	      * POI�����������Χ����������POI�������ܱ߼����� 
	      * @param result ������� 
	      * @param type ���ؽ�����ͣ�11,12,21:poi�б� 7:�����б� 
	      * @param iError ����ţ�0��ʾ��ȷ���أ� 
	      */  
	
	     public void onGetPoiResult(MKPoiResult result, int type, int iError) {}  
	   
	     /** 
	      * ��������·��������� 
	      * @param result ������� 
	      * @param iError ����ţ�0��ʾ��ȷ���أ� 
	      */  
	
	     public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {  
	     }  
	   
	     /** 
	      * ����·��������� 
	      * @param result ������� 
	      * @param iError ����ţ�0��ʾ��ȷ���أ� 
	      */  
	   
	     public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {  
	     }  
	 }  

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		app = (BMapApiDemoApp)this.getApplication();
        app.mBMapMan.getLocationManager().removeUpdates(myLocationListener);
		app.mBMapMan.stop();
		super.onPause();
		 
	}
	@Override
	protected void onResume() {
	 app = (BMapApiDemoApp)this.getApplication();
		// ע�ᶨλ�¼�����λ�󽫵�ͼ�ƶ�����λ��
    app.mBMapMan.getLocationManager().requestLocationUpdates(myLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
