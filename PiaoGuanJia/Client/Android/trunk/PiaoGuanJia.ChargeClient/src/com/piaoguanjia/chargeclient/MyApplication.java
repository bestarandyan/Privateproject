package com.piaoguanjia.chargeclient;


import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


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
	public int screenW = 0;
	public int screenH = 0;
	public String phtotPath = "";//��ǰƾ֤��·��
	public String username = "";//�û���
    public String password = "";//����
    public int currentChargeType = 1;//������ͨ�û���ֵ  2����ר���˻���ֵ
    public String hasAddPerm = "";//��ô����ӳ�ֵ��Ȩ�� 1������
    public String hasAuditPerm = "";//��ô����˵�Ȩ�� 1������
    
	public String getHasAddPerm() {
		return hasAddPerm;
	}
	public void setHasAddPerm(String hasAddPerm) {
		this.hasAddPerm = hasAddPerm;
	}
	public String getHasAuditPerm() {
		return hasAuditPerm;
	}
	public void setHasAuditPerm(String hasAuditPerm) {
		this.hasAuditPerm = hasAuditPerm;
	}
	public int getCurrentChargeType() {
		return currentChargeType;
	}
	public void setCurrentChargeType(int currentChargeType) {
		this.currentChargeType = currentChargeType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhtotPath() {
		return phtotPath;
	}
	public void setPhtotPath(String phtotPath) {
		this.phtotPath = phtotPath;
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
