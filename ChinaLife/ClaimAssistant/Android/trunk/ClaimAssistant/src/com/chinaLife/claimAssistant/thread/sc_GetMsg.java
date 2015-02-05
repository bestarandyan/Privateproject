package com.chinaLife.claimAssistant.thread;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.sqlcrypt.database.ContentValues;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.sc_SayActivity;
import com.chinaLife.claimAssistant.bean.sc_MessageInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class sc_GetMsg {


	// 服务器url
	private static String url;
	private static String ids;
	private static boolean state = true;

	public static void getMsg() {
		if(state){
			state = false;
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
			if(ids==null){
				params.add(new BasicNameValuePair("ids", ""));
			}else{
				params.add(new BasicNameValuePair("ids", ids));
			}
			params.add(new BasicNameValuePair("action", "receive"));
			try {
				params.add(new BasicNameValuePair("time", ""));
			} catch (Exception e) {
				params.add(new BasicNameValuePair("time", ""));
			}
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo
					.getIMEI(sc_MyApplication.getInstance().getContext())));
			params.add(new BasicNameValuePair("claimid", sc_MyApplication
					.getInstance().getClaimid()));
			sc_GetMsg.url = sc_MyApplication.URL + "claim";
			sc_UploadData3 upload = new sc_UploadData3(url, params);
			upload.Post();
			String reponse = upload.getResponse();
			parseJson(reponse);
			params.clear();
			params = null;
			state = true;
		}
	}




	private static void parseJson(String token) {
		if (token.endsWith("]")) {
			Type listType = new TypeToken<LinkedList<sc_MessageInfo>>() {
			}.getType();
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			LinkedList<sc_MessageInfo> messageinfos = null;
			sc_MessageInfo messageinfo = null;
			messageinfos = gson.fromJson(token, listType);
			StringBuilder messageids = new StringBuilder("");
			boolean b = false;
			for (Iterator<sc_MessageInfo> iterator = messageinfos.iterator(); iterator
					.hasNext();) {
				b = true;
				messageinfo = (sc_MessageInfo) iterator.next();
				values.put("claimid", messageinfo.getClaimid());
				values.put("messageid", messageinfo.getMessageid());
				messageids.append(messageinfo.getMessageid());
				messageids.append(",");
				values.put("content", messageinfo.getContent());
				values.put("create_time", messageinfo.getCreatetime());
				values.put("status", 1);
				values.put("sender", messageinfo.getSender());
				List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
						.selectRow(
								"select * from messageinfo where messageid = '"
										+ messageinfo.getMessageid() + "'", null);

				if (selectresult.size() <= 0) {
//					try {
//						SayActivity a = (SayActivity) MyApplication.getInstance()
//								.getContext();
//						int sender = 2;
//						String content = messageinfo.getContent();
//						String create_time = messageinfo.getCreatetime();
//						HashMap<String, Object> map = new HashMap<String, Object>();
//						map.put("sender", sender);
//						map.put("content", content);
//						map.put("create_time", create_time);
//						MyApplication.list.add(map);
//					} catch (final Exception e) {
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								LogUtil.sendLog(2, "获取消息通知错误："+e.getMessage());
//							}
//						}).start();
//						
//					}
					sc_DBHelper.getInstance().insert("messageinfo", values);
				} else {
					sc_DBHelper.getInstance().update("messageinfo", values,
							"messageid = ?",
							new String[] { messageinfo.getMessageid()+"" });

				}
				values.clear();
			}
			if (b) {
				ids = messageids.substring(0, messageids.length() - 1);
			}
			
//			if(messageinfos.size()>0){
//				
//			}
			values = null;
			List<Map<String, Object>> selectresult = sc_DBHelper.getInstance().selectRow(
					"select * from messageinfo where claimid = '"
							+ sc_MyApplication.getInstance().getClaimid()
							+ "' and status = 1 and sender <> 1", null);
			sc_MyApplication.getInstance().setSayNumber(selectresult.size());
		}
	}

}
