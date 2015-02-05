/**
 * 
 */
package com.zhihuigu.sosoOffice.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author Ring
 *
 */
public class PhoneInfo {
	/**  
	* ���ص�ǰ����汾��  
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
	    } catch (Exception e) {   
//	    	Sc_MyApplication.opLogger.error(e);
//	        Log.e("VersionInfo", "Exception", e);   
	    }   
	    return versionName;   
	} 
}
