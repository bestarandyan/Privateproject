package com.chinaLife.claimAssistant.activity;

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

public class Sc_ExitApplication extends Application{
	public static Sc_ExitApplication exitApp = null;
	// 程序退出标记  
    public static List<Activity> activities = new ArrayList<Activity>(); 
    public Context context;
    public AlarmManager am; 
    public Sc_ExitApplication(){
    	
    }
    public static Sc_ExitApplication getInstance(){
    	if(exitApp == null){
    		exitApp = new Sc_ExitApplication();
    	}
    	return exitApp;
    }
    public void addActivity(Activity activity) { 
    	 int a = activities.size();
    	 if(a>=1){
    		activities.remove(0);
    	 }
        activities.add(activity); 
       
    } 
    @Override 
    public void onTerminate() { 
    	
    	if(Sc_MyApplication.getInstance().getTimer()!=null){
    		Sc_MyApplication.getInstance().getTimer().cancel();
    	}
//    	try{
//    		ContentValues values = new ContentValues();
//    		values.put("status", 1);
//    		DBHelper.getInstance().update("loginfo", values,
//    				"name = ?",
//    				new String[] { "client_" });
//    	}catch(final Exception e){
//    		new Thread(new Runnable() {
//				@Override
//				public void run() {
//					LogUtil.sendLog(2, "退出应用保存数据时数据库出现错误："+e.getMessage());
//				}
//			}).start();
//    		if(MyApplication.opLogger!=null){
//    			MyApplication.opLogger.error(e);
//    		}
//    	}
    	
    	Intent intent1 = new Intent(Sc_MyApplication.getInstance().getContext2(),sc_LogService.class);
    	Sc_MyApplication.getInstance().getContext2().stopService(intent1);
    	SharedPreferences.Editor sharedata = Sc_MyApplication.getInstance().getContext2().getSharedPreferences("userinfo", 0)
				.edit();
    	sharedata.putInt("threaddemo", 0);
		sharedata.commit();
    	if(am == null){
			 am = (AlarmManager) Sc_MyApplication.getInstance().getContext2().getSystemService(Sc_MyApplication.getInstance().getContext2().ALARM_SERVICE);
				Intent intent = new Intent(Sc_MyApplication.getInstance().getContext2(), Sc_CallAlarm.class);
				PendingIntent sender = PendingIntent.getBroadcast(
						Sc_MyApplication.getInstance().getContext2(), 0, intent, 0);
				am.setRepeating(AlarmManager.RTC, 0,60*1000, sender);
		}
        super.onTerminate(); 
 
        for (Activity activity : activities  ) { 
            activity.finish(); 
        } 
        activities.remove(activities);
        android.os.Process.killProcess(android.os.Process.myPid()); 
      
    } 
    
    public void exitActivity(){
    	for (Activity activity : activities) { 
            activity.finish(); 
        } 
        activities.remove(activities);
    }
    /**
	 * 退出系统时的提示函数 @author 刘星星
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(context);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("提示")
					.setMessage("您确定要退出系统吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if(Sc_MyApplication.getInstance().getMytimer()!=null){
										Sc_MyApplication.getInstance().getMytimer().cancel();
									}
									((Activity) context).finish();
									onTerminate();
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  Dialog dialog = callDailog.create();
		  dialog.show();
	}
	
	/**
	 * 退出应用程序
	 */
	public void exitPro(){
		if(Sc_MyApplication.getInstance().getMytimer()!=null){
			Sc_MyApplication.getInstance().getMytimer().cancel();
		}
		((Activity) context).finish();
		onTerminate();
	}
}