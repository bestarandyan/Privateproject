package guoTeng.LoadIC2ReadCard;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;


public class MyApplication {
	public static MyApplication myApplication = null;
	public MyApplication() {
		
	}
	public static MyApplication getInstance(){
		if(myApplication == null){
			myApplication = new MyApplication();
		}
		return myApplication;
	}
	public PiaoGuanJiaActivity pgjActivity;
	public PiaoGuanJiaActivity getPgjActivity() {
		return pgjActivity;
	}
	public void setPgjActivity(PiaoGuanJiaActivity pgjActivity) {
		this.pgjActivity = pgjActivity;
	}
	
	

}
