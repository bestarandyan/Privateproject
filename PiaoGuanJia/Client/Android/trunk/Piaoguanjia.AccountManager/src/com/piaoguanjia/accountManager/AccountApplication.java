package com.piaoguanjia.accountManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.piaoguanjia.accountManager.database.DBHelper;
import com.piaoguanjia.accountManager.database.TableCreate;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

public class AccountApplication extends Application {

	private static AccountApplication mInstance = null;
	public static ArrayList<Activity> activitylist = new ArrayList<Activity>();
	public static int widthPixels;
	public static int heightPixels;
	public static String username;
	public static String password;
	public static String userid;
	public static int permissions;
	
	public static int accountnum = 0;//账户待审核
	public static int chargenum = 0;//充值待审核
	public static int creditnum = 0;//额度待审核
	
	public static String rootpath = Environment.getExternalStorageDirectory()
			+ "/piaoguanjias/image";

	public static AccountApplication getInstance() {
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
				AccountApplication.userid = selectresult.get(0).get("userid")
						.toString();
			if (selectresult.get(0).get("username") != null)
				AccountApplication.username = selectresult.get(0)
						.get("username").toString();
			if (selectresult.get(0).get("password") != null)
				AccountApplication.password = selectresult.get(0)
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