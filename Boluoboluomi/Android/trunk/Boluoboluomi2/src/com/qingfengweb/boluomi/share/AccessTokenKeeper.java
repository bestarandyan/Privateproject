package com.qingfengweb.boluomi.share;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 该类用于保存Oauth2AccessToken到sharepreference，并提供读取功能
 * 
 * 
 */
public class AccessTokenKeeper {
	public static final String SINA_PREFERENCES_NAME = "sina_preferences_name";
	public static final String TENCENT_PREFERENCES_NAME = "tencent_preferences_name";
	public static final String TENCENTZONE_PREFERENCES_NAME = "tencentzone_preferences_name";

	/**
	 * 保存accesstoken到SharedPreferences
	 * 
	 * @param context
	 *            Activity 上下文环境
	 * @param token
	 *            Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context,String preferences_name,String token, String uid,long expirestime) {
		SharedPreferences pref = context.getSharedPreferences(preferences_name,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token);
		editor.putString("uid", uid);
		editor.putLong("expiresTime", expirestime);
		editor.commit();
	}
	
	/**
	 * 保存accesstoken到SharedPreferences
	 * 
	 * @param context
	 *            Activity 上下文环境
	 * @param token
	 *            Oauth2AccessToken
	 */
	public static void keepAccessToken(Context context,String preferences_name,String token, String uid,String expirestime,String key) {
		SharedPreferences pref = context.getSharedPreferences(preferences_name,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString("token", token);
		editor.putString("uid", uid);
		editor.putString("expiresTime", expirestime);
		editor.putString("key", key);
		editor.commit();
	}

	/**
	 * 清空sharepreference
	 * 
	 * @param context
	 */
	public static void clear(Context context, String preferences_name) {
		SharedPreferences pref = context.getSharedPreferences(preferences_name,
				Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 从SharedPreferences读取uid
	 * 
	 * @param context
	 * @return String
	 */
	public static String getUid(Context context, String preferences_name) {
		SharedPreferences pref = context.getSharedPreferences(preferences_name,
				Context.MODE_APPEND);
		return pref.getString("uid", "");
	}

	/**
	 * 从SharedPreferences读取数据
	 * 
	 * @param context
	 */
	public static SharedPreferences readAccessToken(Context context,
			String preferences_name) {
		SharedPreferences pref = context.getSharedPreferences(preferences_name,
				Context.MODE_APPEND);
		return pref;
	}
}
