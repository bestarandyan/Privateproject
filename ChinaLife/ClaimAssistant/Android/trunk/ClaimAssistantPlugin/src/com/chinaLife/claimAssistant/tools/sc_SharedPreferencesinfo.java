package com.chinaLife.claimAssistant.tools;

import android.content.Context;
import android.content.SharedPreferences;



public class sc_SharedPreferencesinfo {
	public static void setdata(Context context,String arg1,String arg2) { // 
		SharedPreferences.Editor sharedata = context.getSharedPreferences("userinfo", 0)
				.edit();
		sharedata.putString("phonenumber", arg1);
		sharedata.putString("platenumber", arg2);
		sharedata.commit();

	}
	public static SharedPreferences getdata(Context context) { 
		SharedPreferences sharedata = context.getSharedPreferences("userinfo", 0);
		return sharedata;
	}
}
