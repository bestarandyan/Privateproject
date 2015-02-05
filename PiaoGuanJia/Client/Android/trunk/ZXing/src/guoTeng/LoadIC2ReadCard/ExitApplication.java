package guoTeng.LoadIC2ReadCard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

public class ExitApplication extends Application{
	public static ExitApplication exitApp = null;
	// 程序退出标记  
    public static List<Activity> activities = new ArrayList<Activity>(); 
    public Context context;
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
        int a = activities.size();
        int aa=  a;
    } 
    @Override 
    public void onTerminate() { 
        super.onTerminate(); 
        for (Activity activity : activities) { 
            activity.finish(); 
        } 
        activities.remove(activities);
        android.os.Process.killProcess(android.os.Process.myPid()); 
    } 
   
}
