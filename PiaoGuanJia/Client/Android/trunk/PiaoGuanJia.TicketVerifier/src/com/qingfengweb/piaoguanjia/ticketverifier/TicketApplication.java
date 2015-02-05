package com.qingfengweb.piaoguanjia.ticketverifier;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.qingfengweb.piaoguanjia.ticketverifier.database.DBHelper;
import com.qingfengweb.piaoguanjia.ticketverifier.database.TableCreate;


import android.app.Activity;
import android.app.Application;
import android.os.Environment;

public class TicketApplication extends Application {

	private static TicketApplication mInstance = null;
	public static ArrayList<Activity> activitylist = new ArrayList<Activity>();
	public static int widthPixels;
	public static int heightPixels;
	public static String username;
	public static String password;
	public static String userid;
	public static String productname;
	public static int permissions;
	
	
	public static String rootpath = Environment.getExternalStorageDirectory()
			+ "/piaoguanjias/image";

	public static TicketApplication getInstance() {
		return mInstance;
	}

	
	public static String getPassword(){
		return sha1(password);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		setUser();
	}

	private void setUser() {
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper.getInstance().selectRow(
				"select * from " + TableCreate.TABLENAME_USERINFO
						+ " where iskeep = 1", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(0).get("userid") != null)
				TicketApplication.userid = selectresult.get(0).get("userid")
						.toString();
			if (selectresult.get(0).get("username") != null)
				TicketApplication.username = selectresult.get(0)
						.get("username").toString();
			if (selectresult.get(0).get("password") != null)
				TicketApplication.password = selectresult.get(0)
						.get("password").toString();
		}
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
	 *                字节数组
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