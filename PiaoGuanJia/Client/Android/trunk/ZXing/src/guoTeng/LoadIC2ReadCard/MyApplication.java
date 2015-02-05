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
	public int screenW = 480;//ÆÁÄ»¿í
	public int screenH = 640;//ÆÁÄ»¸ß
	public String currentExitPassword = "";//±¾´ÎµÇÂ½ºóµÄÃÜÂë
			
	public String getCurrentExitPassword() {
		return currentExitPassword;
	}
	public void setCurrentExitPassword(String currentExitPassword) {
		this.currentExitPassword = currentExitPassword;
	}
	public int getScreenW() {
		return screenW;
	}
	public void setScreenW(int screenW) {
		this.screenW = screenW;
	}
	public int getScreenH() {
		return screenH;
	}
	public void setScreenH(int screenH) {
		this.screenH = screenH;
	}
	public PiaoGuanJiaActivity getPgjActivity() {
		return pgjActivity;
	}
	public void setPgjActivity(PiaoGuanJiaActivity pgjActivity) {
		this.pgjActivity = pgjActivity;
	}
	
	

}
