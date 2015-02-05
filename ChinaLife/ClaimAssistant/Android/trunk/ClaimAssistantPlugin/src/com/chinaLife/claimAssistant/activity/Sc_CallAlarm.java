package com.chinaLife.claimAssistant.activity;

import java.util.List;
import java.util.Map;


import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_GetMessage;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_SharedPreferencesinfo;
import com.sqlcrypt.database.sqlite.SQLiteDatabase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Sc_CallAlarm extends BroadcastReceiver {
	
	private boolean b = true;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Sc_MyApplication.getInstance().setContext2(context);
		try {
			if (sc_NetworkCheck.IsHaveInternet(Sc_MyApplication.getInstance()
					.getContext2())) {
				startServer(context);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void startServer(Context context) {
		System.out.println("来了");
		if(!b){
			return;
		}else{
			b = false;
		}
		new Thread(runnable).start();
	}
	
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
	        List<Map<String, Object>> selectresult = sc_DBHelper.getInstance().selectRow(
					"select * from loginfo where status = 0", null);
	        int n= 0;
	        sendToNotification(Sc_MyApplication.getInstance()
					.getContext2());
	        if(selectresult.size()<=0){
	        	b = true;
	        	return;
	        }else if(selectresult.size()<10){
	        	n = selectresult.size();
	        }else{
	        	n = 10;
	        }
	        int i;
			for(i=0;i<n;i++){
				if(!sc_LogUtil.sendLog(Sc_MyApplication.getInstance()
						.getContext2(),selectresult.get(i))){
					break;
				}
				try {
					Thread.sleep(1000*5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			b = true;
		}
	};
	
	
//	/**
//	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
//	 * 
//	 * @param actionUrl
//	 * @param params
//	 * @param files
//	 * @return
//	 * @throws IOException
//	 */
//	private String post(String actionUrl, Map<String, String> params,
//			Map<String, File> files) throws IOException {
//
//		String BOUNDARY = java.util.UUID.randomUUID().toString();
//		String PREFIX = "--", LINEND = "\r\n";
//		String MULTIPART_FROM_DATA = "multipart/form-data";
////		String CHARSET = "UTF-8";
//		String reponse = "";
//		URL uri = new URL(actionUrl);
//		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
//		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
//		conn.setDoInput(true);// 允许输入
//		conn.setDoOutput(true);// 允许输出
//		conn.setUseCaches(false); // 不允许使用缓存
//		conn.setRequestMethod("POST");
//		conn.setRequestProperty("connection", "keep-alive");
////		conn.setRequestProperty("Charsert", "UTF-8");
//		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
//				+ ";boundary=" + BOUNDARY);
//		// 首先组拼文本类型的参数
//		StringBuilder sb = new StringBuilder();
//		for (Map.Entry<String, String> entry : params.entrySet()) {
//			sb.append(PREFIX);
//			sb.append(BOUNDARY);
//			sb.append(LINEND);
//			sb.append("Content-Disposition: form-data; name=\""
//					+ entry.getKey() + "\"" + LINEND);
//			sb.append("Content-Type: text/plain"
//					+ LINEND);
//			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
//			sb.append(LINEND);
//			sb.append(entry.getValue());
//			sb.append(LINEND);
//		}
//		DataOutputStream outStream = new DataOutputStream(
//				conn.getOutputStream());
//		outStream.write(sb.toString().getBytes());
//		InputStream in = null;
//		// 发送文件数据
//		if (files != null) {
//			for (Map.Entry<String, File> file : files.entrySet()) {
//				StringBuilder sb1 = new StringBuilder();
//				sb1.append(PREFIX);
//				sb1.append(BOUNDARY);
//				sb1.append(LINEND);
//				sb1.append("Content-Disposition: form-data; name=\""
//						+ file.getValue().getName() + "\"; filename=\""
//						+ file.getKey() + "\"" + LINEND);
//				sb1.append("Content-Type: " + MULTIPART_FROM_DATA
//						+LINEND);
//				sb1.append(LINEND);
//				outStream.write(sb1.toString().getBytes());
//
//				in = new FileInputStream(file.getValue());
//				byte[] buffer = new byte[1024];
//				int len = 0;
//				while ((len = in.read(buffer)) != -1) {
//					outStream.write(buffer, 0, len);
//				}
//				in.close();
//				outStream.write(LINEND.getBytes());
//				in = null;
//			}
//			// 请求结束标志
//			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND)
//					.getBytes();
//			outStream.write(end_data);
//			outStream.flush();
//			// 得到响应码
//			int res = conn.getResponseCode();
//			if (res == 200) {
//				in = conn.getInputStream();
//				int ch;
//				StringBuilder sb2 = new StringBuilder();
//				while ((ch = in.read()) != -1) {
//					sb2.append((char) ch);
//				}
//				reponse = sb2.toString();
//			}
//			outStream.close();
//			conn.disconnect();
//		}
//		return reponse;
//	}

	public void sendToNotification(Context context) {
		SharedPreferences shared = sc_SharedPreferencesinfo.getdata(context);
		String phonenumber = shared.getString("phonenumber", "");
		String platenumber = shared.getString("platenumber", "");
		Sc_MyApplication.getInstance().setPhonenumber(phonenumber);
		Sc_MyApplication.getInstance().setPlatenumber(platenumber);
		System.out.println(phonenumber+"|000000000000000|"+platenumber);
		String phone = Sc_MyApplication.getInstance().getPhonenumber();
		String plate = Sc_MyApplication.getInstance().getPlatenumber();
		if (phone != null && !phone.equals("") && plate != null
				&& !plate.equals("")) {
			sc_GetMessage.startMyThread();
			List<Map<String, Object>> list = sc_GetMessage.getInstance().selectNumber();
			if (list != null && list.size() == 0) {
				NotificationManager manager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				manager.cancelAll();
				Sc_MyApplication.getInstance().setService_number(0);
				return;
			} else {
				if (list != null
						&& list.size() > Sc_MyApplication.getInstance()
								.getService_number()) {
					Sc_MyApplication.getInstance().setService_number(list.size());
					String str = (String) list.get(0).get("content");
					addNotificaction(context, str);
				}
			}
		}
	}
	/**
	 * 添加一个notification
	 */
	@SuppressWarnings("deprecation")
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
		Intent intent = new Intent(context, Sc_MessageListActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		// 点击状态栏的图标出现的提示信息设置
		if (msg.length() > 20) {
			msg = msg.substring(0, 20) + "....";
		}
		notification.setLatestEventInfo(context, "内容提示：", msg, pendingIntent);
		manager.notify(1, notification);
	}
}
