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
		//��ȡ��LocationManager����
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //�������õ�Criteria���󣬻�ȡ����ϴ˱�׼��provider����
        currentProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER).getName();
        
        //���ݵ�ǰprovider�����ȡ���һ��λ����Ϣ
        currentLocation = locationManager.getLastKnownLocation(currentProvider);
        //���λ����ϢΪnull�����������λ����Ϣ
        if(currentLocation == null){
            locationManager.requestLocationUpdates(currentProvider, 0, 0, locationListener);
        }
        //����GPS״̬������
        locationManager.addGpsStatusListener(gpsListener);
	}
	
	
	@Override
	public void run() {
		// ֱ��������һ��λ����ϢΪֹ�����δ������һ��λ����Ϣ������ʾĬ�Ͼ�γ��
		// ÿ��10���ȡһ��λ����Ϣ
		while (isrun) {
			currentLocation = locationManager
					.getLastKnownLocation(currentProvider);
			if (currentLocation != null) {
				Log.d("Location", "Latitude: " + currentLocation.getLatitude());
				Log.d("Location", "location: " + currentLocation.getLongitude());
				System.out.println(currentLocation.getLatitude()+"longitude"+currentLocation.getLongitude());
				// // ������ַ����ʾ
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
        //GPS״̬�����仯ʱ����
        @Override
        public void onGpsStatusChanged(int event) {
            //��ȡ��ǰ״̬
//            gpsstatus=locationManager.getGpsStatus(null);
            switch(event){
                //��һ�ζ�λʱ���¼�
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    break;
                //��ʼ��λ���¼�
                case GpsStatus.GPS_EVENT_STARTED:
                    break;
                //����GPS����״̬�¼�
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
                //ֹͣ��λ�¼�
                case GpsStatus.GPS_EVENT_STOPPED:
                    break;
            }
        }
    };
    
    
    //����λ�ü�����
    private LocationListener locationListener = new LocationListener(){
        //λ�÷����ı�ʱ����
        @Override
        public void onLocationChanged(Location location) {
        }

        //providerʧЧʱ����
        @Override
        public void onProviderDisabled(String provider) {
        }

        //provider����ʱ����
        @Override
        public void onProviderEnabled(String provider) {
        }

        //״̬�ı�ʱ����
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    
    /**
     * ��ȡ��ǰλ��
     */
    
    public Location getCurrentLoaction(){
    	return currentLocation;
    }
    
    /**
     * ֹͣGPS��λ
     */
    
    public  void stopGPSLocation(){
    	isrun = false;
    	locationManager.removeGpsStatusListener(gpsListener);
    	locationManager.removeUpdates(locationListener);
    }
}
