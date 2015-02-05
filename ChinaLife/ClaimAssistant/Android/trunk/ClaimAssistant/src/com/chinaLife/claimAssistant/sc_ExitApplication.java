package com.chinaLife.claimAssistant;

import java.util.ArrayList;
import java.util.List;

import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_LogService;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class sc_ExitApplication extends Application {
	public static sc_ExitApplication exitApp = null;
	// 程序退出标记
	public static List<Activity> activities = new ArrayList<Activity>();
	public Context context;
	public AlarmManager am;

	public sc_ExitApplication() {

	}

	public static sc_ExitApplication getInstance() {
		if (exitApp == null) {
			exitApp = new sc_ExitApplication();
		}
		return exitApp;
	}

	public void addActivity(Activity activity) {
		int a = activities.size();
		if (a >= 1) {
			activities.remove(0);
		}
		activities.add(activity);

	}

	@Override
	public void onTerminate() {

		if (sc_MyApplication.getInstance().getTimer() != null) {
			sc_MyApplication.getInstance().getTimer().cancel();
		}
		// try{
		// ContentValues values = new ContentValues();
		// values.put("status", 1);
		// DBHelper.getInstance().update("loginfo", values,
		// "name = ?",
		// new String[] { "client_" });
		// }catch(final Exception e){
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		// LogUtil.sendLog(2, "退出应用保存数据时数据库出现错误："+e.getMessage());
		// }
		// }).start();
		// if(MyApplication.opLogger!=null){
		// MyApplication.opLogger.error(e);
		// }
		// }

		Intent intent1 = new Intent(sc_MyApplication.getInstance()
				.getContext2(), sc_LogService.class);
		sc_MyApplication.getInstance().getContext2().stopService(intent1);
		SharedPreferences.Editor sharedata = sc_MyApplication.getInstance()
				.getContext2().getSharedPreferences("userinfo", 0).edit();
		sharedata.putInt("threaddemo", 0);
		sharedata.commit();
		am = (AlarmManager) sc_MyApplication.getInstance().getContext2()
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(
				sc_MyApplication.getInstance().getContext2(),
				sc_CallAlarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(sc_MyApplication
				.getInstance().getContext2(), 0, intent, 0);
		am.setRepeating(AlarmManager.RTC, 0, 60 * 1000, sender);
		super.onTerminate();

		for (Activity activity : activities) {
			activity.finish();
		}
		activities.remove(activities);
		// android.os.Process.killProcess(android.os.Process.myPid());

	}

	public void exitActivity() {
		for (Activity activity : activities) {
			activity.finish();
		}
		activities.remove(activities);
	}

	/**
	 * 退出系统时的提示函数 @author 刘星星
	 */
	public void showExitDialog() {
		AlertDialog.Builder callDailog = new AlertDialog.Builder(context);
		callDailog
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("提示")
				.setMessage("您确定要退出系统吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (sc_MyApplication.getInstance().getMytimer() != null) {
							sc_MyApplication.getInstance().getMytimer()
									.cancel();
						}
						((Activity) context).finish();
						new Thread(new Runnable() {

							@Override
							public void run() {
								onTerminate();
							}
						}).start();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
		Dialog dialog = callDailog.create();
		dialog.show();
	}
}