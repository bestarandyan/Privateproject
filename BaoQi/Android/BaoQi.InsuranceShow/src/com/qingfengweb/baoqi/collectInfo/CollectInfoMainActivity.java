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
	     
	     public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {}  
	   
	     /** 
	      * POI搜索结果（范围检索、城市POI检索、周边检索） 
	      * @param result 搜索结果 
	      * @param type 返回结果类型（11,12,21:poi列表 7:城市列表） 
	      * @param iError 错误号（0表示正确返回） 
	      */  
	
	     public void onGetPoiResult(MKPoiResult result, int type, int iError) {}  
	   
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
		//app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		//mLocationOverlay.disableMyLocation();
     // mLocationOverlay.disableCompass(); // 关闭指南针
      app.mBMapMan.getLocationManager().removeUpdates(myLocationListener);
		app.mBMapMan.stop();
		super.onPause();
		 
	}
	@Override
	protected void onResume() {
	 app = (BMapApiDemoApp)this.getApplication();
		// 注册定位事件，定位后将地图移动到定位点
    //  app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
      app.mBMapMan.getLocationManager().requestLocationUpdates(myLocationListener);
      //mLocationOverlay.enableMyLocation();
      //mLocationOverlay.enableCompass(); // 打开指南针
		app.mBMapMan.start();
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	



}
