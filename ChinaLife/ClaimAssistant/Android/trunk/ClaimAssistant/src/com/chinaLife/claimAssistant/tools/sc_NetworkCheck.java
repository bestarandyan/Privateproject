package com.chinaLife.claimAssistant.tools;

import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class sc_NetworkCheck {

	// 是否联网网络
	public static boolean IsHaveInternet(final Context context) {
		try {
			ConnectivityManager manger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo info = manger.getActiveNetworkInfo();
			boolean b = (info != null && info.isConnected());
			if (!b) {
				sc_MyHandler.getInstance().sendEmptyMessage(-12);
			}
			return b;
		} catch (final Exception e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "网络出现错误："+e.getMessage());
				}
			}).start();
			return false;
		}
	}
}
