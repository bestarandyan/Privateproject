package com.chinaLife.claimAssistant.tools;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;


public class sc_PhoneInfo {
	
	public static final int NETWORK_TYPE_EHRPD=14; // Level 11
    public static final int NETWORK_TYPE_EVDO_B=12; // Level 9
    public static final int NETWORK_TYPE_HSPAP=15; // Level 13
    public static final int NETWORK_TYPE_IDEN=11; // Level 8
    public static final int NETWORK_TYPE_LTE=13; // Level 11
	/**
	 * 
	 * 返回手机的串号
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	/**  
	* 返回当前程序版本名  
	*/   
	public static String getAppVersionName(Context context) {   
	    String versionName = "";   
	    try {   
	        // ---get the package info---   
	        PackageManager pm = context.getPackageManager();   
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);   
	        versionName = pi.versionName;  
	        if (versionName == null || versionName.length() <= 0) {   
	            return "";   
	        }   
	    } catch (final Exception e) {   
	    	new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "获取版本出现错误："+e.getMessage());
				}
			}).start();
	    }   
	    return versionName;   
	} 
	
	 public static boolean isConnectedFast(Context context){
	        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo info = cm.getActiveNetworkInfo();
	        return (info != null && info.isConnected() && isConnectionFast(info.getType(),info.getSubtype()));
	    }
	
	public static boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            //System.out.println("CONNECTED VIA WIFI");
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            // NOT AVAILABLE YET IN API LEVEL 7
            case NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            // Unknown
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
            }
        }else{
            return false;
        }
    }
	
	
}
