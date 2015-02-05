package com.qingfengweb.pinhuo.datamanage;

import java.io.File;

public class MyApplication {
	public static MyApplication myApplication = null;
	public MyApplication() {
		// TODO Auto-generated constructor stub
	}
	public static MyApplication getInstance(){
		if(myApplication == null){
			myApplication = new MyApplication();
		}
		return myApplication;
	}
	public int screenW = 0;//屏幕宽
	public int screenH = 0;//屏幕高
	public int typeUser = 0;//用户类型
	
	public int getTypeUser() {
		return typeUser;
	}
	public void setTypeUser(int typeUser) {
		this.typeUser = typeUser;
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
	
	
}
