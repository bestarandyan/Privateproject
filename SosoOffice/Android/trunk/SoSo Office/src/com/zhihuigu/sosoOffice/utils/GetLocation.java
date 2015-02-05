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
 * @author ������
 * @createDate 2013/1/31
 * ��λ��ǰλ�õ���
 *
 */
public class GetLocation {
	private MKSearch mSearch;
	private GeoPoint geoPoint;
	public String address = "";
	//�ٶ�MapAPI�Ĺ�����
		public  BMapManager mBMapMan = null;
		// ��ȨKey
		// �����ַ��http://dev.baidu.com/wiki/static/imap/key/
		public String mStrKey = "7959E752C81B3DD04058CDD5FF04330FF0DF93ED";
		public  boolean m_bKeyRight = true;	// ��ȨKey��ȷ����֤ͨ��
		public  Context context;
		public GetLocation(Context context) {
			this.context = context;
		}
	// �����¼���������������ͨ�������������Ȩ��֤�����
		public  class MyGeneralListener implements MKGeneralListener {
			public void onGetNetworkState(int iError) {
				Toast.makeText(context, "��Ǹ����λʧ�ܣ���ȷ�������Ƿ����ӡ�����",	Toast.LENGTH_LONG).show();
				if (mBMapMan != null) {
					mBMapMan.destroy();
					mBMapMan = null;
				}
//					CaseOfOnlyOneActivity.unRegisterLocationListener();
				
			}
			public void onGetPermissionState(int iError) {
				if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
					// ��ȨKey����
					Toast.makeText(context, "����BMapApiDemoApp.java�ļ�������ȷ����ȨKey��",Toast.LENGTH_LONG).show();
					m_bKeyRight = false;
				}
			}
			
		}
		/**
		 * ��λ��ǰλ���Լ����� ���������Application��
		 * @author ������
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
		// ע�ᶨλ�¼�
		mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		mBMapMan.start();
	*/}
	/**
	 * �رն�λ���ܣ���ֹ����
	 * @author ������
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
					// �Ƴ�listener
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
					// �Ƴ�listener
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
