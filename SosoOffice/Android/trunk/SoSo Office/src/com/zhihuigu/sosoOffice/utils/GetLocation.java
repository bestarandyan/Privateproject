/**
 * 
 */
package com.zhihuigu.sosoOffice.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.zhihuigu.sosoOffice.constant.MyApplication;

/**
 * @author 刘星星
 * @createDate 2013/1/31
 * 定位当前位置的类
 *
 */
public class GetLocation {
	private MKSearch mSearch;
	private GeoPoint geoPoint;
	public String address = "";
	//百度MapAPI的管理类
		public  BMapManager mBMapMan = null;
		// 授权Key
		// 申请地址：http://dev.baidu.com/wiki/static/imap/key/
		public String mStrKey = "7959E752C81B3DD04058CDD5FF04330FF0DF93ED";
		public  boolean m_bKeyRight = true;	// 授权Key正确，验证通过
		public  Context context;
		public GetLocation(Context context) {
			this.context = context;
		}
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
		public  class MyGeneralListener implements MKGeneralListener {
			public void onGetNetworkState(int iError) {
				Toast.makeText(context, "抱歉，定位失败，请确认网络是否连接。。。",	Toast.LENGTH_LONG).show();
				if (mBMapMan != null) {
					mBMapMan.destroy();
					mBMapMan = null;
				}
//					CaseOfOnlyOneActivity.unRegisterLocationListener();
				
			}
			public void onGetPermissionState(int iError) {
				if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
					// 授权Key错误：
					Toast.makeText(context, "请在BMapApiDemoApp.java文件输入正确的授权Key！",Toast.LENGTH_LONG).show();
					m_bKeyRight = false;
				}
			}
			
		}
		/**
		 * 定位当前位置以及城市 将结果加入Application中
		 * @author 刘星星
		 * @createDate 2013/1/31
		 */
	public void getLocation() {/*
		if (mBMapMan == null) {
			mBMapMan = new BMapManager(context);
			mBMapMan.init(mStrKey,new MyGeneralListener());
		} else {
			mBMapMan.getLocationManager().removeUpdates(mLocationListener);
			mBMapMan.destroy();
			mBMapMan = null;
			mBMapMan = new BMapManager(context);
			mBMapMan.init(mStrKey,new MyGeneralListener());
		}
		mSearch = new MKSearch();
		mSearch.init(mBMapMan, mkSearchListener);
		// 注册定位事件
		mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		mBMapMan.start();
	*/}
	/**
	 * 关闭定位功能，防止错误
	 * @author 刘星星
	 * @createDate2013.1.31
	 */
	public void unRegisterLocation(){/*
		if(mBMapMan!=null){
			mBMapMan.stop();
			mBMapMan.getLocationManager().removeUpdates(mLocationListener);
			mBMapMan.destroy();
			mBMapMan = null;
		}
	*/}
	public MKSearchListener mkSearchListener = new MKSearchListener() {
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		}

		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		}

		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
		}

		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		}

		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			try {
				MKGeocoderAddressComponent kk=arg0.addressComponents;
				String city=kk.city; 
				address = arg0.strAddr;
				if (!address.equals("") && address != null) {
					MyApplication.getInstance().setCurrentAddress(address);
					MyApplication.getInstance().setCurrentCity(city);
				}
			/*	if (mBMapMan != null) {
					// 移除listener
					mBMapMan.getLocationManager().removeUpdates(
							mLocationListener);
					mBMapMan.destroy();
					mBMapMan = null;
				}*/
				System.gc();
			} catch (Exception e) {
			}
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	};

	public LocationListener mLocationListener = new LocationListener() {

		public void onLocationChanged(Location location) {
			if (location != null) {
				geoPoint = new GeoPoint((int) (location.getLatitude() * 1E6),
						(int) (location.getLongitude() * 1E6));
				mSearch.reverseGeocode(geoPoint);
				MyApplication.getInstance().setLatitude( (float) (location.getLatitude()*1E6));
				MyApplication.getInstance().setLongitude((float) location.getLongitude());
			} else {
				MyApplication.getInstance().setCurrentAddress("");
				MyApplication.getInstance().setCurrentCity("");
				if (mBMapMan != null) {
					// 移除listener
			/*		mBMapMan.getLocationManager().removeUpdates(
							mLocationListener);
					mBMapMan.destroy();
					mBMapMan = null;*/
				}
				System.gc();
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	};
}
