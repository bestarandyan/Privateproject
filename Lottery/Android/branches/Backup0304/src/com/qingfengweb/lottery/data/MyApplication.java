package com.qingfengweb.lottery.data;

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
	public String currentUserName = "";//当前登录的用户名
	public String currentToken = "";//当前登录的用户的认证标识
	public File currentHeadImgFile = null;//当前用户头像
	public String currentBalance = "";//用户当前余额
	public String currentQiShu = "";//当前彩票的期数
	public String currentOrder = "";//当前订单号
	public String currentServerTime = "";//服务器当前时间
	public int currentSurplusTime = 0;//当前剩余时间
	public String currentPassword = "";//当前登录的用户的密码
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public int getCurrentSurplusTime() {
		return currentSurplusTime;
	}
	public void setCurrentSurplusTime(int currentSurplusTime) {
		this.currentSurplusTime = currentSurplusTime;
	}
	public String getCurrentServerTime() {
		return currentServerTime;
	}
	public void setCurrentServerTime(String currentServerTime) {
		this.currentServerTime = currentServerTime;
	}
	public String getCurrentOrder() {
		return currentOrder;
	}
	public void setCurrentOrder(String currentOrder) {
		this.currentOrder = currentOrder;
	}
	public String getCurrentQiShu() {
		return currentQiShu;
	}
	public void setCurrentQiShu(String currentQiShu) {
		this.currentQiShu = currentQiShu;
	}
	public String getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}
	public File getCurrentHeadImgFile() {
		return currentHeadImgFile;
	}
	public void setCurrentHeadImgFile(File currentHeadImgFile) {
		this.currentHeadImgFile = currentHeadImgFile;
	}
	public String getCurrentToken() {
		return currentToken;
	}
	public void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}
	public String getCurrentUserName() {
		return currentUserName;
	}
	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
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
