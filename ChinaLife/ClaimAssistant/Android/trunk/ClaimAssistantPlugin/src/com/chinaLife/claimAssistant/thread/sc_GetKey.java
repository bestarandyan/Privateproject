package com.chinaLife.claimAssistant.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.bean.sc_SecretInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.google.gson.Gson;
import com.sqlcrypt.database.ContentValues;

public class sc_GetKey {
	/**
	 * 获取密钥
	 */
	
	public static boolean getKey(Context context) {
		String updatetime = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = sc_DBHelper.getInstance().selectRow(
				"select * from appinfo", null);
		System.out.println(selectresult);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"secret_update_time") != null) {
				updatetime = (selectresult.get(selectresult.size() - 1).get(
						"secret_update_time").toString());
			}
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"secret_client") != null) {
				String secret_client = (selectresult.get(selectresult.size() - 1).get(
						"secret_client").toString());
				Sc_MyApplication.getInstance().setSecretclient(secret_client);
			}
			
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"secret_system") != null) {
				String secret_system = (selectresult.get(selectresult.size() - 1).get(
						"secret_system").toString());
				Sc_MyApplication.getInstance().setSecretsystem(secret_system);
			}
			
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getKey"));

		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(context)));
		params.add(new BasicNameValuePair("updateTime", ""));
		sc_UploadData2 uploaddata = new sc_UploadData2(Sc_MyApplication.URL + "key",params);
		uploaddata.Post();
		String reponse = uploaddata.getReponse();
		dealreponse(reponse);
		params.clear();
		params = null;
		if(reponse.contains("{")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 解析密钥
	 */
	public static void dealreponse(String reponse){
		if(reponse.contains("{")){
			ContentValues values = new ContentValues();
			Gson gson = new Gson();// 创建Gson对象
			sc_SecretInfo secretinfo = gson.fromJson(reponse, sc_SecretInfo.class);
			if (secretinfo != null) {
				Sc_MyApplication.getInstance().setSecretsystem(secretinfo.getSystemkey());
				Sc_MyApplication.getInstance().setSecretclient(secretinfo.getClientkey());
				values.put("secret_update_time", secretinfo.getUpdatetime());
				values.put("secret_client", secretinfo.getClientkey());
				values.put("secret_system", secretinfo.getSystemkey());
				if (sc_DBHelper.getInstance().selectRow(
								"select * from appinfo", null)
						.size() <= 0) {
					sc_DBHelper.getInstance().insert("appinfo",
							values);
				} else {
					sc_DBHelper.getInstance().update("appinfo",
							values, null,
							null);
				}
			}
			values.clear();
			values = null;
			sc_DBHelper.getInstance().close();
		}
	}
}
