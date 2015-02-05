package com.qingfengweb.database;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAppLication {
	public static MyAppLication myAppLication = null;
	public MyAppLication() {
		// TODO Auto-generated constructor stub
	}
	public static MyAppLication getInstant(){
		if (myAppLication == null) {
			myAppLication = new MyAppLication();
		}
		return myAppLication;
	}
	public int backFlag = 0;//���ؼ��Ĺ���  0�����ص���ҳ 1�����ص��ػ���Ʒҳ�� 2�����ص���������
	public int screenW = 0;
	public int screenH = 0;
	public ArrayList<HashMap<String, Object>> brandList = new ArrayList<HashMap<String,Object>>();//Ʒ���б�����
	public ArrayList<HashMap<String, Object>> floorList = new ArrayList<HashMap<String,Object>>();//¥���б�����
	public ArrayList<HashMap<String, String>> cuxiaoList = new ArrayList<HashMap<String,String>>();//������б�����
	public ArrayList<HashMap<String, String>>  shopList = new ArrayList<HashMap<String,String>>();//�ػ���Ʒ����
	public ArrayList<HashMap<String, String>> foodList = new ArrayList<HashMap<String,String>>();//��ʳ�б�����
	
	public ArrayList<HashMap<String, Object>> getBrandList() {
		return brandList;
	}
	public void setBrandList(ArrayList<HashMap<String, Object>> brandList) {
		this.brandList = brandList;
	}
	public ArrayList<HashMap<String, Object>> getFloorList() {
		return floorList;
	}
	public void setFloorList(ArrayList<HashMap<String, Object>> floorList) {
		this.floorList = floorList;
	}
	public ArrayList<HashMap<String, String>> getCuxiaoList() {
		return cuxiaoList;
	}
	public void setCuxiaoList(ArrayList<HashMap<String, String>> cuxiaoList) {
		this.cuxiaoList = cuxiaoList;
	}
	public ArrayList<HashMap<String, String>> getShopList() {
		return shopList;
	}
	public void setShopList(ArrayList<HashMap<String, String>> shopList) {
		this.shopList = shopList;
	}
	public ArrayList<HashMap<String, String>> getFoodList() {
		return foodList;
	}
	public void setFoodList(ArrayList<HashMap<String, String>> foodList) {
		this.foodList = foodList;
	}
	public int getBackFlag() {
		return backFlag;
	}
	public void setBackFlag(int backFlag) {
		this.backFlag = backFlag;
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
