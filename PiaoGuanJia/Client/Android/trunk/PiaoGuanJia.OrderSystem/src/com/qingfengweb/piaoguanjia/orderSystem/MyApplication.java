package com.qingfengweb.piaoguanjia.orderSystem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.qingfengweb.piaoguanjia.orderSystem.model.UserInfo;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Environment;

public class MyApplication extends Application {
	private static MyApplication mInstance = null;
	public static ArrayList<Activity> activitylist = new ArrayList<Activity>();
	public static Activity maintabactivity;
	public static int widthPixels;
	public static int heightPixels;
	public static String username="";
	public static String password="";
	public static String userid="";
	public static UserInfo userinfo;
	public static DbUtils db;
	public static DbUtils dbuser;
	public static String rootpath = Environment.getExternalStorageDirectory()
			+ "/piaoguanjias/xiadan/image";

	public static MyApplication getInstance() {
		return mInstance;
	}

	public static void clearActivity() {
		if (activitylist != null) {
			for (Activity a : activitylist) {
				if (a != null && !a.isFinishing()) {
					a.finish();
				}
			}
		}
	}

	public static String getPassword() {
		return sha1(password);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		db = DbUtils.create(this, "piaoguanjia");
		mInstance = this;
		getdata();
		initUserinfo();
	}

	private void initUserinfo() {
		try {
			if (!MyApplication.username.equals("")){
				userinfo = db.findFirst(
						Selector.from(UserInfo.class).where("username", "=",
								MyApplication.username));
				MyApplication.userid = userinfo.userid;
				MyApplication.password = userinfo.password;
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public void setdata() {
		SharedPreferences.Editor sharedata = getSharedPreferences(
				"piaoguanjia", 0).edit();
		sharedata.putString("username", MyApplication.username);
		sharedata.putString("password", MyApplication.password);
		sharedata.putInt("tag", 1);
		sharedata.commit();
	}

	public void getdata() { //
		SharedPreferences sharedata = getSharedPreferences("piaoguanjia", 0);
		MyApplication.username = sharedata.getString("username", "");
		MyApplication.password = sharedata.getString("password", "");
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final String sha1(String s) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("sha-1");
			return toHexString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 字节数组转字符串
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 返回被转换的字符串
	 */
	public static final String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10)
				buffer.append("0");
			buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buffer.toString();
	}

}