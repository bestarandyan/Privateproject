package com.zhihuigu.sosoOffice.constant;


import java.util.ArrayList;

import com.zhihuigu.sosoOffice.LoginActivity;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.model.Messages;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class MyApplication {
	public static MyApplication myApplication = null;
	private static ImageDownloaderId imageDownloader = null;
	public MyApplication(Context context) {
		String address = "";
		boolean b = context.getResources().getBoolean(R.bool.select_url);
		if(b){
			address = context.getResources().getString(R.string.url);
		}else{
			address = context.getResources().getString(R.string.test_url);
		}
		setAPPID(context.getResources().getString(R.string.appid));
		setURL("http://"+address);
		setAPPKEY(context.getResources().getString(R.string.appkey));
		imageDownloader = new ImageDownloaderId(context, 10);
	}
	public MyApplication() {
	}
	public static MyApplication getInstance(Context context){
		if(myApplication == null){
			myApplication = new MyApplication(context);
		}
		return myApplication;
	}
	public static MyApplication getInstance(){
		if(myApplication == null){
			myApplication = new MyApplication();
		}
		return myApplication;
	}
	
	public ImageDownloaderId getImageDownloaderId(){
		if(imageDownloader == null){
			imageDownloader = new ImageDownloaderId(context, 10);
		}
		return imageDownloader;
	}
	private  String URL = "";// 程序访问路径
	private  String APPID = ""; // 应用程序id
	private  String APPKEY = ""; // 应用程序密匙
	private Activity context;//上下文
	private int ScreenWidth = 0;//屏幕的宽
	private int ScreenHeight = 0;//屏幕的高
	private SoSoUserInfo sosouserinfo = null;//用户信息对象 
	public String fileName = "";
	private int roleid = 0;//用户类型，0无类型，1,业主2,中介4客户
	private String cityid = "";//城市id
	private String cityname = "";//城市名称
	private int lettertype = 1;//信件类型；1收件箱，2发件箱，3管理员信件
	private int usertype = 2;//用户类型1谁收藏我的房源2谁看过我的房源3满足搜索条件的用户
	public int room_list_state = 1;//1代表房源管理的文字形式的列表为非编辑状态 ，2代表为编辑状态
	public int room_state_for_examine= 1;//1代码审核通过的房源  2代表待审核房源
	public String buildid = "";//楼盘id
	public String officeid = "";//房源id；
	public String currentAddress  = "";//当前地址
	public String currentCity = "";//当前城市
	public boolean roomBack = false;//控制房源列表的返回 业主用的
	public boolean demandBack = false;//控制需求界面的返回 客户的
	public int displayRoomPhoto = 0;//是否显示图片，0提示，1显示,2不显示
	public boolean linkManBack = false;//控制联系人详情返回到联系人\
	public boolean roomListBack = false;//控制从添加房源返回到房源列表界面
	private int lettercount=0;//站内信数字
	private int pushcount=0;//推送数字
	private int rcount=0;//需求数字
	public float latitude=0;//经度
	public float longitude=0;//纬度
	public boolean collectRoom = true;//用来标记是否从收藏管理页面跳转到房源详情页面
	public static boolean checkupdate = true;//检查更新
	public static long error_value = 0;
	public String linkManTitle = "谁看过我的房源";//联系人中标题中显示的文字
	public int map_display_flag = 0;//0代表搜索中显示地图   1代表显示列表
	public boolean cityBack = false;//代表是不是从城市列表中返回到主界面
	public boolean searchBack = false;//用来代表是否应该显示精确搜索页面
	public int roomManagerFlag = 0;//0代表从楼盘列表进入房源管理界面   1代表从添加房源成功进入房源管理的待审核列表 2代表从房源详情界面进入3代表审核通过的房源修改
	public int addRoomFlag = 0;////0代表从楼盘列表中进入添加房源 1代表从房源列表也就是房源管理界面进入添加房源
	public Bundle searchbundle = null;//搜索bundle值
	public boolean RecommendOwnerBackBtnVisibility = false;//控制业主中的推送管理中的返回按钮是否显示
	public boolean RecommendAgencyBackBtnVisibility = false;//控制中介中的推送管理中的返回按钮是否显示
	public boolean StationInLetterBackBtnVisibility = false;//控制站内信中的返回键是否显示
	public boolean PotentialDemandBack = false;// 控制潜在需求中的返回按钮是否显示
	public boolean CollectBackBtnVisibility = false;//控制收藏管理界面中的返回按钮是否显示
	public boolean SearchRoomBackVisibility = false;//控制精确搜索界面中的返回按钮是否显示
	public boolean ClientDemandBackBtnVisiblity = false;//控制客户需求中的返回按钮是否显示
//	public int roomManagerStateFlag = 0;//审核通过和待审核的房源状态
	public int roomManagerForm = 0;//0:审核通过的房源中图形列表    1文字列表的状态
	public int notlogin = 0;//1检查还未登陆
	public String currentKeyt ="";
	
	ArrayList<Messages> messageList;//用来保存
//	
//	public int getRoomManagerStateFlag() {
//		return roomManagerStateFlag;
//	}
//	public void setRoomManagerStateFlag(int roomManagerStateFlag) {
//		this.roomManagerStateFlag = roomManagerStateFlag;
//	}
	
	public int getRoomManagerForm() {
		return roomManagerForm;
	}
	public String getCurrentKeyt() {
		return currentKeyt;
	}
	public void setCurrentKeyt(String currentKeyt) {
		this.currentKeyt = currentKeyt;
	}
	public boolean isClientDemandBackBtnVisiblity() {
		return ClientDemandBackBtnVisiblity;
	}
	public void setClientDemandBackBtnVisiblity(boolean clientDemandBackBtnVisiblity) {
		ClientDemandBackBtnVisiblity = clientDemandBackBtnVisiblity;
	}
	public void setRoomManagerForm(int roomManagerForm) {
		this.roomManagerForm = roomManagerForm;
	}
	public boolean isSearchRoomBackVisibility() {
		return SearchRoomBackVisibility;
	}
	public void setSearchRoomBackVisibility(boolean searchRoomBackVisibility) {
		SearchRoomBackVisibility = searchRoomBackVisibility;
	}
	public boolean isCollectBackBtnVisibility() {
		return CollectBackBtnVisibility;
	}
	public void setCollectBackBtnVisibility(boolean collectBackBtnVisibility) {
		CollectBackBtnVisibility = collectBackBtnVisibility;
	}
	public boolean isRecommendAgencyBackBtnVisibility() {
		return RecommendAgencyBackBtnVisibility;
	}
	public void setRecommendAgencyBackBtnVisibility(
			boolean recommendAgencyBackBtnVisibility) {
		RecommendAgencyBackBtnVisibility = recommendAgencyBackBtnVisibility;
	}
	
	public boolean isPotentialDemandBack() {
		return PotentialDemandBack;
	}
	public void setPotentialDemandBack(boolean potentialDemandBack) {
		PotentialDemandBack = potentialDemandBack;
	}
	public boolean isStationInLetterBackBtnVisibility() {
		return StationInLetterBackBtnVisibility;
	}
	public void setStationInLetterBackBtnVisibility(
			boolean stationInLetterBackBtnVisibility) {
		StationInLetterBackBtnVisibility = stationInLetterBackBtnVisibility;
	}
	public boolean isRecommendOwnerBackBtnVisibility() {
		return RecommendOwnerBackBtnVisibility;
	}
	public void setRecommendOwnerBackBtnVisibility(
			boolean recommendOwnerBackBtnVisibility) {
		RecommendOwnerBackBtnVisibility = recommendOwnerBackBtnVisibility;
	}
	public Bundle getSearchbundle() {
		return searchbundle;
	}
	public void setSearchbundle(Bundle searchbundle) {
		this.searchbundle = searchbundle;
	}
	public int getAddRoomFlag() {
		return addRoomFlag;
	}
	public void setAddRoomFlag(int addRoomFlag) {
		this.addRoomFlag = addRoomFlag;
	}
	public int getRoomManagerFlag() {
		return roomManagerFlag;
	}
	public void setRoomManagerFlag(int roomManagerFlag) {
		this.roomManagerFlag = roomManagerFlag;
	}
	public boolean isSearchBack() {
		return searchBack;
	}
	public void setSearchBack(boolean searchBack) {
		this.searchBack = searchBack;
	}
	public boolean isCityBack() {
		return cityBack;
	}
	public void setCityBack(boolean cityBack) {
		this.cityBack = cityBack;
	}
	public int getMap_display_flag() {
		return map_display_flag;
	}
	public void setMap_display_flag(int map_display_flag) {
		this.map_display_flag = map_display_flag;
	}
	public String getLinkManTitle() {
		return linkManTitle;
	}
	public void setLinkManTitle(String linkManTitle) {
		this.linkManTitle = linkManTitle;
	}
	public boolean isCollectRoom() {
		return collectRoom;
	}
	public void setCollectRoom(boolean collectRoom) {
		this.collectRoom = collectRoom;
	}
	public int getLettercount() {
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return 0;
		}
		return lettercount;
	}
	public void setLettercount(int lettercount) {
		this.lettercount = lettercount;
	}
	public int getPushcount() {
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null||roleid==1){
			return 0;
		}
		return pushcount;
	}
	public void setPushcount(int pushcount) {
		this.pushcount = pushcount;
	}
	public int getRcount() {
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return 0;
		}
		return rcount;
	}
	public void setRcount(int rcount) {
		this.rcount = rcount;
	}
	public boolean isRoomListBack() {
		return roomListBack;
	}
	public void setRoomListBack(boolean roomListBack) {
		this.roomListBack = roomListBack;
	}
	public boolean isLinkManBack() {
		return linkManBack;
	}
	public void setLinkManBack(boolean linkManBack) {
		this.linkManBack = linkManBack;
	}
	public int getDisplayRoomPhoto() {
		return displayRoomPhoto;
	}
	public void setDisplayRoomPhoto(int displayRoomPhoto) {
		this.displayRoomPhoto = displayRoomPhoto;
	}
	public boolean isDemandBack() {
		return demandBack;
	}
	public void setDemandBack(boolean demandBack) {
		this.demandBack = demandBack;
	}
	public boolean isRoomBack() {
		return roomBack;
	}
	public void setRoomBack(boolean roomBack) {
		this.roomBack = roomBack;
	}
	public String getCurrentAddress() {
		return currentAddress;
	}
	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	public int getRoom_state_for_examine() {
		return room_state_for_examine;
	}
	public void setRoom_state_for_examine(int room_state_for_examine) {
		this.room_state_for_examine = room_state_for_examine;
	}
	public int getRoom_list_state() {
		return room_list_state;
	}
	public void setRoom_list_state(int room_list_state) {
		this.room_list_state = room_list_state;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getScreenWidth() {
		return ScreenWidth;
	}
	public void setScreenWidth(int screenWidth) {
		ScreenWidth = screenWidth;
	}
	public int getScreenHeight() {
		return ScreenHeight;
	}
	public void setScreenHeight(int screenHeight) {
		ScreenHeight = screenHeight;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getAPPID() {
		return APPID;
	}
	public void setAPPID(String aPPID) {
		APPID = aPPID;
	}
	public String getAPPKEY() {
		return APPKEY;
	}
	public void setAPPKEY(String aPPKEY) {
		APPKEY = aPPKEY;
	}
	public Activity getContext() {
		return context;
	}
	public void setContext(Activity context) {
		this.context = context;
	}
	public SoSoUserInfo getSosouserinfo(Activity context) {
		if(sosouserinfo==null){
//			sosouserinfo = new SoSoUserInfo();
			Intent i = new Intent();
			i.setClass(context, LoginActivity.class);
			context.startActivity(i);
			notlogin = 1;
		}
		return sosouserinfo;
	}
	public SoSoUserInfo getSosouserinfo() {
		return sosouserinfo;
	}
	public void setSosouserinfo(SoSoUserInfo sosouserinfo) {
		this.sosouserinfo = sosouserinfo;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public int getLettertype() {
		return lettertype;
	}
	public void setLettertype(int lettertype) {
		this.lettertype = lettertype;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getBuildid() {
		return buildid;
	}
	public void setBuildid(String buildid) {
		this.buildid = buildid;
	}
	public String getOfficeid() {
		return officeid;
	}
	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public int getNotlogin() {
		return notlogin;
	}
	public void setNotlogin(int notlogin) {
		this.notlogin = notlogin;
	}
	
	
}
