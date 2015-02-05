package com.boluomi.children.model;

import java.util.List;
import java.util.Map;

import com.boluomi.children.R;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.database.DBHelper;

import android.content.Context;
import android.content.SharedPreferences;

public class AppInfo {
	public static String TABLE_CREATE = 
			"create table " 
					+ "appinfo" + 
			"(" + "_id integer primary key autoincrement," + 
			"name text," + 
			"value text," + 
			"updatetime text" + 
			")";

	private String name;
	private String value;
	
	
	
	
	public static void initAPP(Context context){
		MyApplication.APPID = context.getResources().getString(R.string.appid);
		MyApplication.APPKEY = context.getResources().getString(R.string.appkey);
		String address = context.getResources().getString(R.string.test_url);
		String port = context.getResources().getString(R.string.port);
		String url_postfix = context.getResources().getString(R.string.url_postfix);
		MyApplication.URL = "http://"+address+":"+port+url_postfix;
	}

	/**
	 * ģ�ͳ�ʼ��
	 * @return
	 */
	public AppInfo() {
		setName("");
		setValue("");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static boolean firstApp(Context context) {
		boolean b = false;
		SharedPreferences sharedata = context
				.getSharedPreferences("appinfo", 0);
		if (sharedata.getInt("app_state", 0) == 0) {
			SharedPreferences.Editor setsharedata = context
					.getSharedPreferences("appinfo", 0).edit();
			setsharedata.putInt("app_state", 1);
			setsharedata.commit();
			b = true;
		}
		return b;
	}
	
	
	public static String isUpdateData(DBHelper db,String name) {
		String updatetime = "";
		String value = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = db.selectRow(
						"select updatetime,value from appinfo where name='"+name+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get("updatetime") != null
					&&selectresult.get(selectresult.size() - 1).get("value") != null) {
				updatetime = (selectresult.get(selectresult.size() - 1).get(
						"updatetime").toString());
				value = (selectresult.get(selectresult.size() - 1).get(
						"value").toString());
				if(value.equals(updatetime)){
					return "-1";
				}else{
					return updatetime;
				}
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		return "";
	}
}
