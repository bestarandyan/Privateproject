package com.chinaLife.claimAssistant.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_UploadData4;
import com.sqlcrypt.database.ContentValues;

public class sc_LogUtil {
	
	
	/***
	 * type 表示是新创建的日志1，还是上传已经创建过的 2
	 * 发送异常信息
	 * @return
	 */
	public static boolean sendLog(Context context, Map<String, Object> map) {
		String id = map.get("_id").toString();
		String createtime = map.get("createtime").toString();
		String phonenumber = map.get("phonenumber").toString();
		String platenumber = map.get("platenumber").toString();
		String type = map.get("type").toString();
		String content = map.get("content").toString();
		String imei = map.get("imei").toString();
		if(platenumber==null||platenumber.equals("")
				||phonenumber.equals("")||phonenumber==null){
			return false;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "uploadLog"));
		params.add(new BasicNameValuePair("phoneNumber", phonenumber));
		params.add(new BasicNameValuePair("plateNumber", platenumber));
		params.add(new BasicNameValuePair("createTime", createtime));
		params.add(new BasicNameValuePair("IMEI", imei));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("type", type));
		sc_UploadData4 uploaddata = new sc_UploadData4(sc_MyApplication.URL + "log",
				params);
		uploaddata.Post();
		String reponse = uploaddata.getReponse();
		params.clear();
		params = null;

		if (reponse.equals("0")) {
			dealmsg(id);
			return true;
		}
		return false;
	}
	
	/***
	 * type 表示是新创建的日志1，还是上传已经创建过的 2
	 * 发送异常信息
	 * @return
	 */
	public static boolean sendLog(int type, String content) {

		String createtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		String phonenumber = sc_MyApplication.getInstance().getPhonenumber();
		String platenumber = sc_MyApplication.getInstance().getPlatenumber();
		if(platenumber==null||platenumber.equals("")
				||phonenumber.equals("")||phonenumber==null){
			return false;
		}
		String imei = sc_PhoneInfo.getIMEI(sc_MyApplication.getInstance().getContext2());
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "uploadLog"));
		params.add(new BasicNameValuePair("phoneNumber", phonenumber));
		params.add(new BasicNameValuePair("plateNumber", platenumber));
		params.add(new BasicNameValuePair("createTime", createtime));
		params.add(new BasicNameValuePair("IMEI", imei));
		params.add(new BasicNameValuePair("content", content));
		params.add(new BasicNameValuePair("type", type+""));
		sc_UploadData4 uploaddata = new sc_UploadData4(sc_MyApplication.URL + "log",
				params);
		uploaddata.Post();
		String reponse = uploaddata.getReponse();
		params.clear();
		params = null;
		if (reponse.equals("0")) {
			return true;
		}
		dealmsg(createtime, type, phonenumber, platenumber, imei, content);
		return false;
	}
	
	/***
	 * 处理服务器返回值
	 * @param reponse2
	 */
	private static void dealmsg(String createtime,int type,String phonenumber
			,String platenumber,String imei,String content) {
		if(platenumber==null||platenumber.equals("")
				||phonenumber.equals("")||phonenumber==null){
			return;
		}
		ContentValues values = new ContentValues();
		values.put("plateNumber", platenumber);
		values.put("phoneNumber",phonenumber );
		values.put("imei", imei);
		values.put("createtime",createtime);
		values.put("type", type);
		values.put("content", content);
		values.put("status", "0");
		sc_DBHelper.getInstance().insert("loginfo", values);
		sc_DBHelper.getInstance().close();
	}
	
	/***
	 * 处理服务器返回值
	 * @param reponse2
	 */
	private static void dealmsg(String id) {
		sc_DBHelper.getInstance().execSql("delete from loginfo where _id = "+Integer.parseInt(id));
		sc_DBHelper.getInstance().close();	
	}
}
