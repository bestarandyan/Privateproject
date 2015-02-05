package com.qingfengweb.baoqi.hospitalquery;

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
import com.qingfengweb.baoqi.propertyInsurance.PropertyInsuranceMainActivity;
import com.qingfengweb.baoqi.propertyInsurance.R;
import com.qingfengweb.baoqi.propertyInsurance.SupportActivity;
import com.qingfengweb.baoqi.mytask.MyTaskMainActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class HospitalQuery extends MapActivity{ 
	private int tag = 0;
	private Button shouyeBtn,querybtn;
	private Spinner spinner1,spinner2,spinner3;
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
	private  ArrayAdapter<CharSequence> adapter1;
	private  ArrayAdapter<CharSequence> adapter2;
	private  ArrayAdapter<CharSequence> adapter3;
	private int spinner1_item=0,spinner2_item=0,spinner3_item=0;

	private String quStr[]={"������","������","������","������","������","������","ƽ����","������","�̺���","������"};
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		setContentView(R.layout.hospital_query);
		initView();
		Intent intent  = getIntent();
		tag = intent.getIntExtra("tag", 0);
		locationOverlayFun();
		MyLocationFun();
		viewListener();
		super.onCreate(arg0);
	}

	private void initView(){
		
		shouyeBtn=(Button) findViewById(R.id.h_backhomebtn);
		querybtn=(Button) findViewById(R.id.query_hospitalBtn);
		spinner1=(Spinner) findViewById(R.id.h_spinner1);
		spinner2=(Spinner) findViewById(R.id.h_spinner2);
		spinner3=(Spinner) findViewById(R.id.h_spinner3);
		
	}
	private void viewListener(){
		
		shouyeBtn.setOnClickListener(new backHomeListener());
		adapter1=ArrayAdapter.createFromResource(getApplicationContext(), R.array.h_shen,android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);
		adapter2=ArrayAdapter.createFromResource(getApplicationContext(), R.array.h_shi_jinan,android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		adapter3=ArrayAdapter.createFromResource(getApplicationContext(), R.array.h_qu_jinan,android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(adapter3);
		spinner3.setOnItemSelectedListener(new SpinnListener());
		if(tag ==0){
			querybtn.setText("����ҽ�ƻ�����ѯ");
		}else{
			querybtn.setText("����ά�޵��ѯ");
		}
		
	}
	 
	class SpinnListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
		
			  
				spinner3_item = arg2;
				if(spinner3_item==0){
					return;
				}else{
					  sb = new StringBuilder();  
					  mMKSearch.poiSearchInCity(quStr[spinner3_item], "ҽԺ");
				}
		
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	
	
	
	
	
	class backHomeListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(HospitalQuery.this, PropertyInsuranceMainActivity.class);
			startActivity(intent);
		}
		
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
	     
	     public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {  
	    	// ����ſɲο�MKEvent�еĶ���
				if (iError != 0 || result == null) {
					Toast.makeText(HospitalQuery.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(HospitalQuery.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
			    routeOverlay.setData(result.getPlan(0).getRoute(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    mMapView.getController().animateTo(result.getStart().pt);

	     }  
	   
	     /** 
	      * POI�����������Χ����������POI�������ܱ߼����� 
	      * @param result ������� 
	      * @param type ���ؽ�����ͣ�11,12,21:poi�б� 7:�����б� 
	      * @param iError ����ţ�0��ʾ��ȷ���أ� 
	      */  
	
	     public void onGetPoiResult(MKPoiResult result, int type, int iError) {  
	    	 if (result == null) {  
	                return;  
	            }  
	    	  mapController = mMapView.getController();  
	    	  mapController.setZoom(10);  
	            // �����ͼ�����е����и�����   
	    	 mMapView.getOverlays().clear();  
	            // PoiOverlay��baidu map api�ṩ��������ʾPOI��Overlay   
	            PoiOverlay poioverlay = new PoiOverlay(HospitalQuery.this, mMapView);  
	            // ������������POI����   
	            poioverlay.setData(result.getAllPoi());  
	            // �ڵ�ͼ����ʾPoiOverlay��������������Ȥ���ע�ڵ�ͼ�ϣ�   
	            mMapView.getOverlays().add(poioverlay);  
	  
	            if(result.getNumPois() > 0) {  
	                // ��������һ������������ڵ�������Ϊ��ͼ������   
	                MKPoiInfo poiInfo = result.getPoi(0);  
	                mapController.setCenter(poiInfo.pt);  
	            }  
	              
	            sb.append("��������").append(result.getNumPois()).append("��POI/n");  
	            // ������ǰҳ���ص�POI��Ĭ��ֻ����10����   
	            for (MKPoiInfo poiInfo : result.getAllPoi()) { 
	                sb.append("���ƣ�").append(poiInfo.name);  
	                //sb.append("��ַ��").append(poiInfo.address).append("/n");   
	                //sb.append("���ȣ�").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");   
	                //sb.append("γ�ȣ�").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");   
	            }  
	  
	            // ͨ��AlertDialog��ʾ��ǰҳ��������POI   
	            new AlertDialog.Builder(HospitalQuery.this)  
	            .setTitle("��������POI��Ϣ")  
	            .setMessage(sb.toString())  
	            .setPositiveButton("�ر�", new DialogInterface.OnClickListener() {  
	                public void onClick(DialogInterface dialog, int whichButton) {  
	                    dialog.dismiss();  
	                }  
	            }).create().show();  
	     }  
	   
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
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableCompass(); // �ر�ָ����
        app.mBMapMan.getLocationManager().removeUpdates(myLocationListener);
		app.mBMapMan.stop();
		super.onPause();
		 
	}
	@Override
	protected void onResume() {
	 app = (BMapApiDemoApp)this.getApplication();
		// ע�ᶨλ�¼�����λ�󽫵�ͼ�ƶ�����λ��
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
        app.mBMapMan.getLocationManager().requestLocationUpdates(myLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // ��ָ����
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(HospitalQuery.this,SupportActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	

}
