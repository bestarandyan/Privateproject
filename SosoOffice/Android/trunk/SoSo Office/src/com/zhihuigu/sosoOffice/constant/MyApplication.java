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
	private  String URL = "";// �������·��
	private  String APPID = ""; // Ӧ�ó���id
	private  String APPKEY = ""; // Ӧ�ó����ܳ�
	private Activity context;//������
	private int ScreenWidth = 0;//��Ļ�Ŀ�
	private int ScreenHeight = 0;//��Ļ�ĸ�
	private SoSoUserInfo sosouserinfo = null;//�û���Ϣ���� 
	public String fileName = "";
	private int roleid = 0;//�û����ͣ�0�����ͣ�1,ҵ��2,�н�4�ͻ�
	private String cityid = "";//����id
	private String cityname = "";//��������
	private int lettertype = 1;//�ż����ͣ�1�ռ��䣬2�����䣬3����Ա�ż�
	private int usertype = 2;//�û�����1˭�ղ��ҵķ�Դ2˭�����ҵķ�Դ3���������������û�
	public int room_list_state = 1;//1����Դ�����������ʽ���б�Ϊ�Ǳ༭״̬ ��2����Ϊ�༭״̬
	public int room_state_for_examine= 1;//1�������ͨ���ķ�Դ  2�������˷�Դ
	public String buildid = "";//¥��id
	public String officeid = "";//��Դid��
	public String currentAddress  = "";//��ǰ��ַ
	public String currentCity = "";//��ǰ����
	public boolean roomBack = false;//���Ʒ�Դ�б�ķ��� ҵ���õ�
	public boolean demandBack = false;//�����������ķ��� �ͻ���
	public int displayRoomPhoto = 0;//�Ƿ���ʾͼƬ��0��ʾ��1��ʾ,2����ʾ
	public boolean linkManBack = false;//������ϵ�����鷵�ص���ϵ��\
	public boolean roomListBack = false;//���ƴ���ӷ�Դ���ص���Դ�б����
	private int lettercount=0;//վ��������
	private int pushcount=0;//��������
	private int rcount=0;//��������
	public float latitude=0;//����
	public float longitude=0;//γ��
	public boolean collectRoom = true;//��������Ƿ���ղع���ҳ����ת����Դ����ҳ��
	public static boolean checkupdate = true;//������
	public static long error_value = 0;
	public String linkManTitle = "˭�����ҵķ�Դ";//��ϵ���б�������ʾ������
	public int map_display_flag = 0;//0������������ʾ��ͼ   1������ʾ�б�
	public boolean cityBack = false;//�����ǲ��Ǵӳ����б��з��ص�������
	public boolean searchBack = false;//���������Ƿ�Ӧ����ʾ��ȷ����ҳ��
	public int roomManagerFlag = 0;//0�����¥���б���뷿Դ�������   1�������ӷ�Դ�ɹ����뷿Դ����Ĵ�����б� 2����ӷ�Դ����������3�������ͨ���ķ�Դ�޸�
	public int addRoomFlag = 0;////0�����¥���б��н�����ӷ�Դ 1����ӷ�Դ�б�Ҳ���Ƿ�Դ������������ӷ�Դ
	public Bundle searchbundle = null;//����bundleֵ
	public boolean RecommendOwnerBackBtnVisibility = false;//����ҵ���е����͹����еķ��ذ�ť�Ƿ���ʾ
	public boolean RecommendAgencyBackBtnVisibility = false;//�����н��е����͹����еķ��ذ�ť�Ƿ���ʾ
	public boolean StationInLetterBackBtnVisibility = false;//����վ�����еķ��ؼ��Ƿ���ʾ
	public boolean PotentialDemandBack = false;// ����Ǳ�������еķ��ذ�ť�Ƿ���ʾ
	public boolean CollectBackBtnVisibility = false;//�����ղع�������еķ��ذ�ť�Ƿ���ʾ
	public boolean SearchRoomBackVisibility = false;//���ƾ�ȷ���������еķ��ذ�ť�Ƿ���ʾ
	public boolean ClientDemandBackBtnVisiblity = false;//���ƿͻ������еķ��ذ�ť�Ƿ���ʾ
//	public int roomManagerStateFlag = 0;//���ͨ���ʹ���˵ķ�Դ״̬
	public int roomManagerForm = 0;//0:���ͨ���ķ�Դ��ͼ���б�    1�����б��״̬
	public int notlogin = 0;//1��黹δ��½
	public String currentKeyt ="";
	
	ArrayList<Messages> messageList;//��������
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
