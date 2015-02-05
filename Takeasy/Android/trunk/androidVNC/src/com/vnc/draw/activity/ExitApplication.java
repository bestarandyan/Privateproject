package com.vnc.draw.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

public class ExitApplication extends Application{
	public static ExitApplication exitApp = null;
	// �����˳����  
    public static List<Activity> activities = new ArrayList<Activity>(); 
    public Context context;
    public AlarmManager am; 
    public ExitApplication(){
    	
    }
    public static ExitApplication getInstance(){
    	if(exitApp == null){
    		exitApp = new ExitApplication();
    	}
    	return exitApp;
    }
    public void addActivity(Activity activity) { 
        activities.add(activity); 
       
    } 
    @Override 
    public void onTerminate() { 
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
	 * �˳�ϵͳʱ����ʾ���� @author ������
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(context);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("��ʾ")
					.setMessage("��ȷ��Ҫ�˳�ϵͳ��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									int[] data ={1029,1,14,0,241};
									MyApplication.getInstance().getSendToService().sDraw(data);
									MyApplication.getInstance().getSendToService().closeSocket();
									((Activity) context).finish();
									onTerminate();
								}
							}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  AlertDialog alert =((Builder) callDailog).create();
		  alert.show();
	}
	
	 /**
		 * �˳�ϵͳʱ����ʾ���� @author ������
		 */
		public void showExitDialog(final Context context){
			AlertDialog.Builder callDailog = new AlertDialog.Builder(context);
			  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
			  			.setTitle("��ʾ")
						.setMessage("��ȷ��Ҫ�˳�ϵͳ��")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										int[] data ={1029,1,14,0,241};
										MyApplication.getInstance().getSendToService().sDraw(data);
										MyApplication.getInstance().getSendToService().closeSocket();
										((Activity) context).finish();
										onTerminate();
									}
								}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										return ;
									}
								});
			  AlertDialog alert =((Builder) callDailog).create();
			  alert.show();
		}
}