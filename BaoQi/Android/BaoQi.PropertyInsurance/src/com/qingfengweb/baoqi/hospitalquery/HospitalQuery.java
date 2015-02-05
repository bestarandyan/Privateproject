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
	public LocationListener mLocationListener = null;//onResume时注册此listener，onPause时需要Remove
	public MyLocationOverlay mLocationOverlay = null;	//定位图层
	public LocationListener myLocationListener = null;//create时注册此listener，Destroy时需要Remove
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

	private String quStr[]={"市中区","历下区","天桥区","槐荫区","历城区","长清区","平阴县","济阳县","商河县","章丘市"};
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
			querybtn.setText("定点医疗机构查询");
		}else{
			querybtn.setText("定点维修点查询");
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
					  mMKSearch.poiSearchInCity(quStr[spinner3_item], "医院");
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
	         // 如果使用地图SDK，请初始化地图Activity
	         super.initMapActivity(app.mBMapMan);
	         //layout1=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	          //relative=(RelativeLayout) layout1.inflate(R.layout.map_view, null);
	         mMapView = (MapView)findViewById(R.id.bmapView);
	         mMapView.setBuiltInZoomControls(true);
	         //设置在缩放动画过程中也显示overlay,默认为不绘制
	         mMapView.setDrawOverlayWhenZooming(true);
	         
	 		// 添加定位图层
	         mLocationOverlay = new MyLocationOverlay(this, mMapView);
	        
	 		mMapView.getOverlays().add(mLocationOverlay);
	         // 注册定位事件
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
		 * 当前经纬度定位
		 * 及根据经纬度定位当前街道信息
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
	        // 注册定位事件
	        myLocationListener = new LocationListener(){
				public void onLocationChanged(Location location) {
				
					jingdu=location.getLongitude();
					weidu=location.getLatitude();
					if(location != null){
						String strLog = String.format("您当前的位置:\r" +
								"纬度:%f\r" +
								"经度:%f",
								location.getLongitude(), location.getLatitude());
						 try {  
			                    // 将用户输入的经纬度值转换成int类型   
			                    int longitude = (int) (1E6 * jingdu);  
			                    int latitude = (int) (1E6 *weidu);  
			                    // 查询该经纬度值所对应的地址位置信息   
			                    
			                    mMKSearch.reverseGeocode(new GeoPoint(latitude, longitude));  
			                    
			                } catch (Exception e) {  
			                	jingwei="查询出错，请检查您输入的经纬度值！";  
			                }  
						

					}
				}
	        };
	       
	       return jingwei;
	 }
	 /** 
	  * 实现MKSearchListener接口,用于实现异步搜索服务，得到搜索结果 
	  *  
	  * @author liufeng 
	  */  
	 public class MySearchListener implements MKSearchListener {  
	     /** 
	      * 根据经纬度搜索地址信息结果 
	      * @param result 搜索结果 
	      * @param iError 错误号（0表示正确返回） 
	      */  
	 
		
	        public void onGetAddrResult(MKAddrInfo result, int iError) {  
	        	if (result == null) {  
	                return;  
	            }  
	            StringBuffer sb = new StringBuffer();  
	            // 经纬度所对应的位置   
	            sb.append(result.strAddr).append("/n");  
	  
	            // 判断该地址附近是否有POI（Point of Interest,即兴趣点）   
	             /*if (null != result.poiList) {  
	                // 遍历所有的兴趣点信息   
	                for (MKPoiInfo poiInfo : result.poiList) {  
	                    sb.append("名称：").append(poiInfo.name).append("/n");  
	                    sb.append("地址：").append(poiInfo.address).append("/n");  
	                    sb.append("经度：").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");  
	                    sb.append("纬度：").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");  
	                    sb.append("电话：").append(poiInfo.phoneNum).append("/n");  
	                    sb.append("邮编：").append(poiInfo.postCode).append("/n");  
	                    // poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路   
	                    sb.append("类型：").append(poiInfo.ePoiType).append("/n");  
	                }  
	            } */
	            // 将地址信息、兴趣点信息显示在TextView上   
	      

	            jingwei=sb.toString();
	        }  

	   
	     /** 
	      * 驾车路线搜索结果 
	      * @param result 搜索结果 
	      * @param iError 错误号（0表示正确返回） 
	      */  
	     
	     public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {  
	    	// 错误号可参考MKEvent中的定义
				if (iError != 0 || result == null) {
					Toast.makeText(HospitalQuery.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(HospitalQuery.this, mMapView);
			    // 此处仅展示一个方案作为示例
			    routeOverlay.setData(result.getPlan(0).getRoute(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.invalidate();
			    mMapView.getController().animateTo(result.getStart().pt);

	     }  
	   
	     /** 
	      * POI搜索结果（范围检索、城市POI检索、周边检索） 
	      * @param result 搜索结果 
	      * @param type 返回结果类型（11,12,21:poi列表 7:城市列表） 
	      * @param iError 错误号（0表示正确返回） 
	      */  
	
	     public void onGetPoiResult(MKPoiResult result, int type, int iError) {  
	    	 if (result == null) {  
	                return;  
	            }  
	    	  mapController = mMapView.getController();  
	    	  mapController.setZoom(10);  
	            // 清除地图上已有的所有覆盖物   
	    	 mMapView.getOverlays().clear();  
	            // PoiOverlay是baidu map api提供的用于显示POI的Overlay   
	            PoiOverlay poioverlay = new PoiOverlay(HospitalQuery.this, mMapView);  
	            // 设置搜索到的POI数据   
	            poioverlay.setData(result.getAllPoi());  
	            // 在地图上显示PoiOverlay（将搜索到的兴趣点标注在地图上）   
	            mMapView.getOverlays().add(poioverlay);  
	  
	            if(result.getNumPois() > 0) {  
	                // 设置其中一个搜索结果所在地理坐标为地图的中心   
	                MKPoiInfo poiInfo = result.getPoi(0);  
	                mapController.setCenter(poiInfo.pt);  
	            }  
	              
	            sb.append("共搜索到").append(result.getNumPois()).append("个POI/n");  
	            // 遍历当前页返回的POI（默认只返回10个）   
	            for (MKPoiInfo poiInfo : result.getAllPoi()) { 
	                sb.append("名称：").append(poiInfo.name);  
	                //sb.append("地址：").append(poiInfo.address).append("/n");   
	                //sb.append("经度：").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");   
	                //sb.append("纬度：").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");   
	            }  
	  
	            // 通过AlertDialog显示当前页搜索到的POI   
	            new AlertDialog.Builder(HospitalQuery.this)  
	            .setTitle("搜索到的POI信息")  
	            .setMessage(sb.toString())  
	            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {  
	                public void onClick(DialogInterface dialog, int whichButton) {  
	                    dialog.dismiss();  
	                }  
	            }).create().show();  
	     }  
	   
	     /** 
	      * 公交换乘路线搜索结果 
	      * @param result 搜索结果 
	      * @param iError 错误号（0表示正确返回） 
	      */  
	
	     public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {  
	     }  
	   
	     /** 
	      * 步行路线搜索结果 
	      * @param result 搜索结果 
	      * @param iError 错误号（0表示正确返回） 
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
        mLocationOverlay.disableCompass(); // 关闭指南针
        app.mBMapMan.getLocationManager().removeUpdates(myLocationListener);
		app.mBMapMan.stop();
		super.onPause();
		 
	}
	@Override
	protected void onResume() {
	 app = (BMapApiDemoApp)this.getApplication();
		// 注册定位事件，定位后将地图移动到定位点
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
        app.mBMapMan.getLocationManager().requestLocationUpdates(myLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // 打开指南针
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
