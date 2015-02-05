/**
 * 
 */
package com.qingfengweb.client.data;


/**
 * @author 刘星星
 *
 */
public class MyApplication {
	public static MyApplication myApplication = null;
	public MyApplication() {
		// TODO Auto-generated constructor stub
	}
	public static MyApplication getInstant(){
		if(myApplication == null){
			myApplication = new MyApplication();
		}
		return myApplication;
	}
	public int screenW = 0;
	public int screenH = 0;
	public String device_brand = "";//设备品牌
	public String device_model = "";//设备型号
	public String device_osname ="";//系统类型
	public String device_osversion = "";//系统版本
	public String device_token = "";//设备标识
	public int proCurrentPro = 0;//表示当前进度条的进度
	
	
	public int getProCurrentPro() {
		return proCurrentPro;
	}
	public void setProCurrentPro(int proCurrentPro) {
		this.proCurrentPro = proCurrentPro;
	}
	public String getDevice_brand() {
		return device_brand;
	}
	public void setDevice_brand(String device_brand) {
		this.device_brand = device_brand;
	}
	public String getDevice_model() {
		return device_model;
	}
	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}
	public String getDevice_osname() {
		return device_osname;
	}
	public void setDevice_osname(String device_osname) {
		this.device_osname = device_osname;
	}
	public String getDevice_osversion() {
		return device_osversion;
	}
	public void setDevice_osversion(String device_osversion) {
		this.device_osversion = device_osversion;
	}
	public String getDevice_token() {
		return device_token;
	}
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
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
