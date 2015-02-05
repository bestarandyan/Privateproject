package com.chinaLife.claimAssistant.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.sqlcrypt.database.ContentValues;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;

public class sc_SendMsg{

	private static String url = sc_MyApplication.URL + "claim";
	public static void sendMsg() {
		sc_MyApplication.switch_tag = false;
		List<Map<String, Object>> selectresult = null;
		selectresult = checkSynData();
		int count = selectresult.size();
		int i = 0;
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		while (i < count) {
			String _id = selectresult.get(i).get("_id").toString();
			params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "send"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo
					.getIMEI(sc_MyApplication.getInstance().getContext())));
			params.add(new BasicNameValuePair("claimid", sc_MyApplication
					.getInstance().getClaimid()));
			params.add(new BasicNameValuePair("msg", selectresult.get(i)
					.get("content").toString()));
			sc_UploadData3 upload = new sc_UploadData3(url, params);
			upload.Post();
			String reponse = upload.getResponse();
			checkresponse(reponse,_id);
			params.clear();
			i++;
		}
	}

	private static List<Map<String, Object>> checkSynData() {
		String sql = "select * from messageinfo where status = 1 and sender = 1";
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
				.selectRow(sql, null);
		return selectresult;
	}

	

	private static void checkresponse(String token,String _id) {
		int messageid = 0;
		try {
			messageid = Integer.parseInt(token);
		} catch (Exception e) {
		}
		if (messageid>0) {
			ContentValues values = new ContentValues();
			values.put("status", 0);
			values.put("messageid", messageid);
			boolean b = sc_DBHelper.getInstance().update("messageinfo", values, "_id=?", new String[]{_id});
			//System.out.println(b+"'''''''''''''''''''''''''''''''''''''''''''");
			if(b){
				//System.out.println("_id为"+_id+"编号信息发送成功");
			}
			values.clear();
			values = null;
			sc_DBHelper.getInstance().close();
		}
	}

}
