/**
 * 
 */
package com.qingfengweb.client.data;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.os.Handler;
import android.util.Log;

/**
 * @author Ring
 *
 */
public class GPSLocation implements Runnable {
	private LocationManager locationManager;
	private Location currentLocation = null;
	private String currentProvider = null;
//    private GpsStatus gpsstatus;
    private Context context = null;
    private boolean isrun = true;
	public GPSLocation(Context context){
		this.context = context;
		//获取到LocationManager对象
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //根据设置的Criteria对象，获取最符合此标准的provider对象
        currentProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();
        
        //根据当前provider对象获取最后一次位置信息
        currentLocation = locationManager.getLastKnownLocation(currentProvider);
        //如果位置信息为null，则请求更新位置信息
        if(currentLocation == null){
            locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
        }
        //增加GPS状态监听器
        locationManager.addGpsStatusListener(gpsListener);
	}
	
	
	@Override
	public void run() {
		// 直到获得最后一次位置信息为止，如果未获得最后一次位置信息，则显示默认经纬度
		// 每隔10秒获取一次位置信息
		while (isrun) {
			currentLocation = locationManager
					.getLastKnownLocation(currentProvider);
			if (currentLocation != null) {
				Log.d("Location", "Latitude: " + currentLocation.getLatitude());
				Log.d("Location", "location: " + currentLocation.getLongitude());
				System.out.println(currentLocation.getLatitude()+"longitude"+currentLocation.getLongitude());
				// // 解析地址并显示
				// Geocoder geoCoder = new Geocoder(context);
				// try {
				// // int latitude = (int) currentLocation.getLatitude();
				// // int longitude = (int) currentLocation.getLongitude();
				// List<Address> list =
				// geoCoder.getFromLocation(currentLocation.getLatitude(),
				// currentLocation.getLongitude(),
				// 2);
				// for (int i = 0; i < list.size(); i++) {
				// Address address = list.get(i);
				// Message msg = new Message();
				// Bundle data = new Bundle();
				// data.putString("msg",
				// address.getLocality() + address.getAdminArea()
				// + address.getSubLocality());
				// msg.setData(data);
				// }
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				break;
			} else {
				isrun = false;
			}
		}
	}


	private GpsStatus.Listener gpsListener = new GpsStatus.Listener(){
        //GPS状态发生变化时触发
        @Override
        public void onGpsStatusChanged(int event) {
            //获取当前状态
//            gpsstatus=locationManager.getGpsStatus(null);
            switch(event){
                //第一次定位时的事件
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    break;
                //开始定位的事件
                case GpsStatus.GPS_EVENT_STARTED:
                    break;
                //发送GPS卫星状态事件
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//                    Toast.makeText(context, "GPS_EVENT_SATELLITE_STATUS", Toast.LENGTH_SHORT).show();
//                    Iterable<GpsSatellite> allSatellites = gpsstatus.getSatellites();   
//                    Iterator<GpsSatellite> it=allSatellites.iterator(); 
//                    int count = 0;
//                    while(it.hasNext())   
//                    {   
//                        count++;
//                    }
//                    Toast.makeText(context, "Satellite Count:" + count, Toast.LENGTH_SHORT).show();
                    break;
                //停止定位事件
                case GpsStatus.GPS_EVENT_STOPPED:
                    break;
            }
        }
    };
    
    
    //创建位置监听器
    private LocationListener locationListener = new LocationListener(){
        //位置发生改变时调用
        @Override
        public void onLocationChanged(Location location) {
        }

        //provider失效时调用
        @Override
        public void onProviderDisabled(String provider) {
        }

        //provider启用时调用
        @Override
        public void onProviderEnabled(String provider) {
        }

        //状态改变时调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    
    /**
     * 获取当前位置
     */
    
    public Location getCurrentLoaction(){
    	return currentLocation;
    }
    
    /**
     * 停止GPS定位
     */
    
    public  void stopGPSLocation(){
    	isrun = false;
    	locationManager.removeGpsStatusListener(gpsListener);
    	locationManager.removeUpdates(locationListener);
    }
}
