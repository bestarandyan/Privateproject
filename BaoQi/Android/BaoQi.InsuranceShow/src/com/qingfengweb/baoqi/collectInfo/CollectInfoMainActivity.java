package com.qingfengweb.baoqi.collectInfo;
import com.qingfengweb.baoqi.insuranceShow.InsuranceShowMainActivity;
import com.qingfengweb.baoqi.insuranceShow.R;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;
import com.qingfengweb.baoqi.gereninfo.PrivateMessage;
import com.qingfengweb.baoqi.hospitalquery.BMapApiDemoApp;
import com.qingfengweb.baoqi.hospitalquery.HospitalQuery;
import com.qingfengweb.baoqi.hospitalquery.HospitalQuery.MySearchListener;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class CollectInfoMainActivity extends MapActivity implements OnClickListener{
private Button bt1,bt2,bt3,bt4;
public BMapManager mapManager;
public MKSearch mMKSearch;
public MapView mMapView = null;
public LocationListener mLocationListener = null;//onResumeʱע���listener��onPauseʱ��ҪRemove
public MyLocationOverlay mLocationOverlay = null;	//��λͼ��
public LocationListener myLocationListener = null;//createʱע���listener��Destroyʱ��ҪRemove
public Double jingdu=null,weidu=null;
private LayoutInflater layout1;
 private MapController mapController; 
 public static StringBuilder sb;  
RelativeLayout relative;
public GeoPoint pt;
BMapApiDemoApp app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.collectinfomain);
		
			bt1=(Button) findViewById(R.id.collBtn1);
			bt2=(Button) findViewById(R.id.collBtn2);
			bt3=(Button) findViewById(R.id.collBtn3);
			bt4=(Button) findViewById(R.id.collBtn4);

		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		//locationOverlayFun();
		MyLocationFun();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(CollectInfoMainActivity.this,InsuranceShowMainActivity.class);
			
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		
		intent.putExtra("dangqiandizhi", MyLocationFun());
		if(v == bt1){
			//Toast.makeText(getApplicationContext(), MyLocationFun(), Toast.LENGTH_LONG).show();
			intent.setClass(CollectInfoMainActivity.this, CollCarInfo.class);
			
		}else if(v == bt2){
			intent.setClass(CollectInfoMainActivity.this, CollQiCaiXian.class);
		}else if(v == bt3){
			intent.setClass(CollectInfoMainActivity.this, CollJiSunXian.class);
		}else if(v == bt4){
			intent.setClass(CollectInfoMainActivity.this, CollGongChengXian.class);
		}
		startActivity(intent);
		finish();
	}
	public void locationOverlayFun(){
		  BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
	 		if (app.mBMapMan == null) {
	 			app.mBMapMan = new BMapManager(getApplication());
	 			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
	 		}
	 		app.mBMapMan.start();
	         // ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
	         super.initMapActivity(app.mBMapMan);
	         //layout1=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	          //relative=(RelativeLayout) layout1.inflate(R.layout.map_view, null);
	         mMapView = (MapView)findViewById(R.id.bmapView);
	         mMapView.setBuiltInZoomControls(true);
	         //���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
	         mMapView.setDrawOverlayWhenZooming(true);
	         
	 		// ��Ӷ�λͼ��
	         mLocationOverlay = new MyLocationOverlay(this, mMapView);
	        
	 		mMapView.getOverlays().add(mLocationOverlay);
	         // ע�ᶨλ�¼�
	         mLocationListener = new LocationListener(){
	 			public void onLocationChanged(Location location) {
	 				if (location != null){
	 					 pt = new GeoPoint((int)(location.getLatitude()*1e6),
	 							(int)(location.getLongitude()*1e6));
	 					mMapView.getController().animateTo(pt);
	 					
	 				}
	 			}
	         };
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
	            sb.append(result.strAddr).append("/n");  
	  
	            // �жϸõ�ַ�����Ƿ���POI��Point of Interest,����Ȥ�㣩   
	             /*if (null != result.poiList) {  
	                // �������е���Ȥ����Ϣ   
	                for (MKPoiInfo poiInfo : result.poiList) {  
	                    sb.append("���ƣ�").append(poiInfo.name).append("/n");  
	                    sb.append("��ַ��").append(poiInfo.address).append("/n");  
	                    sb.append("���ȣ�").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");  
	                    sb.append("γ�ȣ�").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");  
	                    sb.append("�绰��").append(poiInfo.phoneNum).append("/n");  
	                    sb.append("�ʱࣺ").append(poiInfo.postCode).append("/n");  
	                    // poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·   
	                    sb.append("���ͣ�").append(poiInfo.ePoiType).append("/n");  
	                }  
	            } */
	            // ����ַ��Ϣ����Ȥ����Ϣ��ʾ��TextView��   
	      

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
		//app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		//mLocationOverlay.disableMyLocation();
     // mLocationOverlay.disableCompass(); // �ر�ָ����
      app.mBMapMan.getLocationManager().removeUpdates(myLocationListener);
		app.mBMapMan.stop();
		super.onPause();
		 
	}
	@Override
	protected void onResume() {
	 app = (BMapApiDemoApp)this.getApplication();
		// ע�ᶨλ�¼�����λ�󽫵�ͼ�ƶ�����λ��
    //  app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
      app.mBMapMan.getLocationManager().requestLocationUpdates(myLocationListener);
      //mLocationOverlay.enableMyLocation();
      //mLocationOverlay.enableCompass(); // ��ָ����
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	



}
