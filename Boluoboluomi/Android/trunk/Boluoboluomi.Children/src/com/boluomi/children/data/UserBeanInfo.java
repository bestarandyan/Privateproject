/**
 * 
 */
package com.boluomi.children.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 刘星星
 * @createDate 2013/9/9
 * 用户信息类
 *
 */
public class UserBeanInfo {
	public static UserBeanInfo userBeanInfo = null;
	public UserBeanInfo() {
	}
	public static UserBeanInfo getInstant(){
		if(userBeanInfo == null){
			userBeanInfo = new UserBeanInfo();
		}
		return userBeanInfo;
	}
	public String currentStore = "";//当前门店名称
	public String currentStoreId = "";//当前门店Id
	public String currentCityId = "";//当前城市id
	public boolean isLogined = false;//表示是否已经登陆
	public static List<Map<String,Object>> storeDetail = new ArrayList<Map<String,Object>>();//门店详情数据集合
	public String userid = "";//当前登陆的用户id
	public String userScore = "";//当前用户总积分
	public String userName = "";//当前用户名
	public String password = "";//当前用户的密码
	public boolean isAutoLogin = false;
	
	public boolean isAutoLogin() {
		return isAutoLogin;
	}
	public void setAutoLogin(boolean isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}
	public boolean isLogined() {
		return isLogined;
	}
	public void setLogined(boolean isLogined) {
		this.isLogined = isLogined;
	}
	public static UserBeanInfo getUserBeanInfo() {
		return userBeanInfo;
	}
	public static void setUserBeanInfo(UserBeanInfo userBeanInfo) {
		UserBeanInfo.userBeanInfo = userBeanInfo;
	}
	public String getCurrentStore() {
		return currentStore;
	}
	public void setCurrentStore(String currentStore) {
		this.currentStore = currentStore;
	}
	public String getCurrentStoreId() {
		return currentStoreId;
	}
	public void setCurrentStoreId(String currentStoreId) {
		this.currentStoreId = currentStoreId;
	}
	public String getCurrentCityId() {
		return currentCityId;
	}
	public void setCurrentCityId(String currentCityId) {
		this.currentCityId = currentCityId;
	}
	public static List<Map<String, Object>> getStoreDetail() {
		return storeDetail;
	}
	public static void setStoreDetail(List<Map<String, Object>> storeDetail) {
		UserBeanInfo.storeDetail = storeDetail;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserScore() {
		return userScore;
	}
	public void setUserScore(String userScore) {
		this.userScore = userScore;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
