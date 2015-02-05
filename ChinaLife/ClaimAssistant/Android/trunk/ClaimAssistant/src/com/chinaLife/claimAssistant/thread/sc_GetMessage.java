package com.chinaLife.claimAssistant.thread;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.chinaLife.claimAssistant.sc_MainActivity;
import com.chinaLife.claimAssistant.sc_MessageListActivity;
import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.R;
import com.chinaLife.claimAssistant.bean.sc_NoticeInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_AESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_DESKeyGenerator;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_VisiteTimes2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sqlcrypt.database.AssetsDatabaseManager;
import com.sqlcrypt.database.ContentValues;
import com.sqlcrypt.database.Cursor;
import com.sqlcrypt.database.sqlite.SQLiteDatabase;

public class sc_GetMessage {

	private static sc_GetMessage mInstance = null;
	private static final String TAG = "DBHelper";
	private SQLiteDatabase db = null;
	public sc_GetMessage() {
	}

	public static sc_GetMessage getInstance() {
		if (mInstance == null) {
			mInstance = new sc_GetMessage();
		}
		return mInstance;
	}

	// 服务器url
	private static String url = sc_MyApplication.URL + "claim";
	private static String ids = "";

	// 上传数据参数列表
	public static void startMyThread() {
		if (sc_NetworkCheck.IsHaveInternet(sc_MyApplication.getInstance()
				.getContext2())) {
			if (!sc_MyApplication.getInstance().getPhonenumber().equals("")
					&& !sc_MyApplication.getInstance().getPlatenumber().equals("")) {
				getInstance().post();
			}
		}
	}
	
	private List<BasicNameValuePair> encodeParams(
			List<BasicNameValuePair> params2) throws Exception {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		String key = DESKeyGenerator.initKey("123");
		String key  = sc_MyApplication.getInstance().getSecretclient();
		for (BasicNameValuePair par : params2) {
			byte[] inputData = par.getValue().getBytes("utf-8");
			inputData = sc_DESKeyGenerator.encrypt(inputData, key);
			params.add(new BasicNameValuePair(par.getName(), new BASE64Encoder().encode(inputData)));
		}
		params.add(new BasicNameValuePair("encryption", 1 + ""));
		return params;
	}

	public void post() {
		
		if(sc_MyApplication.getInstance().getSecretclient()==null||
				sc_MyApplication.getInstance().getSecretclient().equals("")
				||sc_MyApplication.getInstance().getSecretsystem() == null
				||sc_MyApplication.getInstance().getSecretsystem().equals("")){
			return;
		}
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getNotice"));
		params.add(new BasicNameValuePair("phoneNumber", sc_MyApplication
				.getInstance().getPhonenumber()));
		params.add(new BasicNameValuePair("plateNumber", sc_MyApplication
				.getInstance().getPlatenumber()));
		params.add(new BasicNameValuePair("ids", ids));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo
				.getIMEI(sc_MyApplication.getInstance().getContext2())));
		HttpParams httpParameters = new BasicHttpParams();
		// //System.out.println(params);
		HttpPost request = new HttpPost(url);
		HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
		request.setParams(httpParameters);
		try {
			params = encodeParams(params);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (final UnsupportedEncodingException e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "参数错误："+e.getMessage());
				}
			}).start();
		}
		// 发送请求
		try {
			// 得到应答的字符串，这是一个 JSON 格式保存的数据
			String retSrc = connServerForResult(request);
			// // 生成 JSON 对象
			checkresponse(retSrc);
			if (params != null) {
				params.clear();
				params = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 得到服务器响应数据
	private String connServerForResult(HttpPost request) {
		// HttpGet对象
		String strResult = "";
		try {
			// 获得HttpResponse对象
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return "-404";
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的数据
				String outputStr = EntityUtils.toString(httpResponse.getEntity());
				String key = sc_MyApplication.getInstance().getSecretsystem();
				byte[] outputData = sc_AESKeyGenerator.decrypt(new BASE64Decoder().decodeBuffer(outputStr), key);
				strResult = new String(outputData,"utf-8");
			}
			System.out.println("服务器请求完毕后的返回值为----"+strResult);
		} catch (final Exception e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "服务器请求失败："+e.getMessage());
				}
			}).start();

			sc_VisiteTimes2.getInstance().count();
			if (sc_VisiteTimes2.getInstance().isOut()) {
				sc_MyHandler.getInstance().sendEmptyMessage(-9);

			} else {
				if (sc_VisiteTimes2.getInstance().isInit()) {
					sc_VisiteTimes2.getInstance().init();
				}
			}
			return "-1000";
		}
		return strResult;
	}

	private void checkresponse(String token) {
		// System.out.println("获取消息通知--------"+token);
		if (token.endsWith("]")) {
			// MyHandler.getInstance().sendEmptyMessage(-10);
			parseJson(token);
		}
	}

	public void parseJson(String token) {
		Type listType = new TypeToken<LinkedList<sc_NoticeInfo>>() {
		}.getType();
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		LinkedList<sc_NoticeInfo> noticeinfos = null;
		sc_NoticeInfo noticeinfo = null;
		noticeinfos = gson.fromJson(token, listType);
		StringBuilder messageids = new StringBuilder("");
		// System.out.println("长度"+noticeinfos.size()+"-----"+token);
		boolean b = false;
		// MyApplication.getInstance().setMessageNumber(noticeinfos.size());
		for (Iterator<sc_NoticeInfo> iterator = noticeinfos.iterator(); iterator
				.hasNext();) {
			b = true;
			noticeinfo = (sc_NoticeInfo) iterator.next();
			values.put("claimid", noticeinfo.getClaimid());
			values.put("noticeid", noticeinfo.getNoticeid());
			messageids.append(noticeinfo.getNoticeid());
			messageids.append(",");
			// System.out.println(messageids);
			values.put("content", noticeinfo.getContent());
			values.put("create_time", noticeinfo.getCreatetime());
			values.put("status", 1);
			values.put("claimstatus", noticeinfo.getClaimstatus());
			List<Map<String, Object>> selectresult = selectRow(
							"select * from noticeinfo where noticeid = '"
									+ noticeinfo.getNoticeid() + "'", null);

			if (selectresult.size() <= 0) {
				insert("noticeinfo", values);
			} else {
				update("noticeinfo", values,
						"noticeid = ?",
						new String[] { noticeinfo.getNoticeid() });
			}
			values.clear();
		}
		if (b) {
			ids = messageids.substring(0, messageids.length() - 1);
		}
		values = null;
		String sql = "select caseinfo.caseid,caseinfo.status as casestatus,"
				+ " claiminfo.claimid,claiminfo.status as claimstatus,"
				+ " noticeinfo.create_time,noticeinfo.content,noticeinfo.noticeid "
				+ "from noticeinfo inner join claiminfo on noticeinfo.claimid = claiminfo.claimid"
				+ " inner join caseinfo on caseinfo.caseid = claiminfo.caseid"
				+ " where caseinfo.contact_mobile_number='"
				+ sc_MyApplication.getInstance().getPhonenumber()
				+ "' and caseinfo.plate_number ='"
				+ sc_MyApplication.getInstance().getPlatenumber()
				+ "' and noticeinfo.status = 1 order by julianday(noticeinfo.create_time) desc";
		List<Map<String, Object>> selectresult = selectRow(sql, null);
		sc_MyApplication.getInstance().setMessageNumber(selectresult.size());
		sc_MainActivity.timeron = true;
		if (noticeinfos.size() > 0) {
			addNotificaction(sc_MyApplication.getInstance().getContext2(),
					selectresult.get(0).get("content").toString());
			try{
				sc_MainActivity main = (sc_MainActivity) sc_MyApplication.getInstance().getContext();
			}catch (Exception e) {
				Message message = new Message();
				message.what = -16;
				Bundle b2 = new Bundle();
				b2.putString("msg", selectresult.get(0).get("content").toString());
				message.setData(b2);
				sc_MyHandler.getInstance().sendMessage(message);
			}
		}
	}

	/**
	 * 添加一个notification
	 */
	private void addNotificaction(Context context, String msg) {
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancelAll();
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.sc_message1;
		// notification.icon = R.drawable.message;
		// 当当前的notification被放到状态栏上的时候，提示内容
		notification.tickerText = "您有新的消息通知。。。";

		/***
		 * notification.contentIntent:一个PendingIntent对象，当用户点击了状态栏上的图标时，
		 * 该Intent会被触发 notification.contentView:我们可以不在状态栏放图标而是放一个view
		 * notification.deleteIntent 当当前notification被移除时执行的intent
		 * notification.vibrate 当手机震动时，震动周期设置
		 */
		// 添加声音提示
		notification.defaults = Notification.DEFAULT_SOUND;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

		// 下边的两个方式可以添加音乐
		// notification.sound =
		// Uri.parse("file:///sdcard/notification/ringer.mp3");
		// notification.sound =
		// Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		Intent intent = new Intent(context, sc_MessageListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		// 点击状态栏的图标出现的提示信息设置
		if (msg.length() > 20) {
			msg = msg.substring(0, 20) + "....";
		}
		notification.setLatestEventInfo(context, "内容提示：", msg, pendingIntent);

		manager.notify(1, notification);

	}

	public  List<Map<String, Object>> selectNumber() {

		String sql = "select caseinfo.caseid,caseinfo.status as casestatus,"
				+ " claiminfo.claimid,claiminfo.status as claimstatus,"
				+ " noticeinfo.create_time,noticeinfo.content,noticeinfo.noticeid "
				+ "from noticeinfo inner join claiminfo on noticeinfo.claimid = claiminfo.claimid"
				+ " inner join caseinfo on caseinfo.caseid = claiminfo.caseid"
				+ " where caseinfo.contact_mobile_number='"
				+ sc_MyApplication.getInstance().getPhonenumber()
				+ "' and caseinfo.plate_number ='"
				+ sc_MyApplication.getInstance().getPlatenumber()
				+ "' and noticeinfo.status = 1"
				+ " order by julianday(noticeinfo.create_time) desc";

		List<Map<String, Object>> selectresult = selectRow(sql, null);
		// MyApplication.getInstance().setService_number(selectresult.size());
		// DBHelper.getInstance().close();
		return selectresult;
	}
	
	
	
	
	/***
	 * 根据sql语句获取数据；
	 * 
	 * @param sql
	 *            语句
	 * @param selectionArgs
	 *            显示的字段
	 * @return List<Map<String, Object>>
	 */

	public List<Map<String, Object>> selectRow(String sql,
			String[] selectionArgs) {
		open();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Cursor mCursor = null;
		try {
			mCursor = db.rawQuery(sql, selectionArgs);
			Map<String, Object> map = null;
			int iColumnCount = mCursor.getColumnCount();
			while (mCursor.moveToNext()) {
				map = new HashMap<String, Object>();
				for (int i = 0; i < iColumnCount; i++) {
					map.put(mCursor.getColumnName(i).toLowerCase(),
							mCursor.getString(i));
	
//					map.put(mCursor.getColumnName(i).toLowerCase(),
//							decodeStr(mCursor.getString(i)));
				}
				result.add(map);
			}
		} catch (Exception e) {
			Log.e(TAG,
					"selectRow error:" + sql + "\nException:" + e.getMessage());
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
		}
		return result;
	}
	
	
	public void close() {
		synchronized (OBJECTLOCK) {
			if (db != null) {
				try {
					db.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void open() {
		synchronized (OBJECTLOCK) {
			try {
				AssetsDatabaseManager manager = AssetsDatabaseManager.getManager();
				if (db == null || !db.isOpen()) {
					db = manager.getDatabase("chinalife.db.sqlite", sc_DBHelper.DATABASESECRET);
				}
				if (db == null || !db.isOpen()) {
					db = manager.getDatabase("chinalife.db.sqlite", "");
					db.resetPassword(sc_DBHelper.DATABASESECRET);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * 添加数据
	 * 
	 * @param tableName
	 *            表名称
	 * @param contentValues
	 *            封装值key-value
	 * @return long 返回编号
	 */
	public long insert(String tableName, ContentValues contentValues) {
		open();
		long isSuccess = -1;
		try {
			if ((tableName != null && tableName.length() > 0)
					&& (contentValues != null && contentValues.size() > 0)) {
				isSuccess = db.insert(tableName, null, contentValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "create Data error:" + e.getMessage());
		}
		return isSuccess;
	}
	
	
	/***
	 * 更新内容
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            值
	 * @param whereClause
	 *            where 条件
	 * @param whereArgs
	 *            where参数
	 * @return boolean 是否执行成功
	 */
	public boolean update(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		open();
		boolean isSuccess = false;
		try {
			if (table != null && table.length() > 0) {
				db.update(table, values, whereClause, whereArgs);
				isSuccess = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "update data error:" + e.getMessage());
		}
		return isSuccess;
	}
	
	private static Object OBJECTLOCK = new Object();
}
