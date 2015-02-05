package com.chinaLife.claimAssistant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import com.chinaLife.claimAssistant.tools.sc_BigStone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class sc_MyApplication {
	private static sc_MyApplication mInstance = null;
//	public static String URL = "http://192.168.1.188:7001/selfClaim/interface/";// 程序访问路径
	public static String URL = "http://selfclaim.gpic-sslvpn.com:9999/selfClaim/interface/";// 程序访问路径
//	public static String IMAGEURL = "http://192.168.1.115:8080/ChinaLife.AdminSite";// 图片获取路径
//	public final static String TELECOM_URL = "http://selfclaim.gpic-sslvpn.com:9999/selfClaim/interface/";//电信网
//	public final static String MOVE_URL = "http://selfclaim.gpic-sslvpn.com:9999/selfClaim/interface/";//移动网
//	public final static String LINK_URL = "http://selfclaim.gpic-sslvpn.com:9999/selfClaim/interface/";//联通网
//	public final static String NEI_NET_URL = "http://100.250.140.111:9999/selfClaim/interface/";//内网
//	public static String MOVE_URL = "http://192.168.1.104:7001/selfClaim/interface/";//移动网
//	public static String LINK_URL = "http://192.168.1.104:7001/selfClaim/interface/";//联通网
//	public static String NEI_NET_URL = "http://192.168.1.104:7001/selfClaim/interface/";//内网
//	public static String TELECOM_URL = "http://192.168.1.104:7001/selfClaim/interface/";//内网
	public final static String APPID = "862679"; // 应用程序id
	public final static String APPKEY = "c907d5df8b26550eaf2841ed9bcf51bb44b9ae1b"; // 应用程序密匙
	public static Long ERROR_VALUE = 0L;// 时间同步误差值
	public static String cache = android.os.Environment
			.getExternalStorageDirectory() + "/DCIM/ChinaLife/cache";//照片保存路劲
	
	public  String URL_PIC = android.os.Environment
			.getExternalStorageDirectory() + "/DCIM/ChinaLife/CaseOfSingle";//照片保存路劲
	
	public  String URL_PIC1 = android.os.Environment
			.getExternalStorageDirectory() + "/DCIM/ChinaLife/selfcache";//照片保存路劲
	public static String rootUrl = android.os.Environment.getRootDirectory().getAbsolutePath()+"/DCIM/ChinaLife/CaseOfSingle";
	public static String rootUrl1 = android.os.Environment.getRootDirectory().getAbsolutePath()+"/DCIM/ChinaLife/selfcache";
	public static String URL_LOG = android.os.Environment
			.getExternalStorageDirectory() + "/DCIM/ChinaLife/log";//日志保存路劲
	public static List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	public static boolean switch_tag = true;//静态线程的控制按钮
//	public Bitmap imageBitmap = null;
	public static int isqiangzhi = 0;//是否强制更新
	public static boolean hasnewverson = false;//是否有新版本
//	public static Logger opLogger=null;

	private sc_MyApplication() {
		
	}
	public static sc_MyApplication getInstance() {
		if (mInstance == null) {
			mInstance = new sc_MyApplication();
		}
		return mInstance;
	}
	public sc_BigStone[] mStones = null;
	private Activity context = null;
	private Context context2 = null;
	private ProgressDialog progressdialog;
	private String responseword="";// 获取服务器的响应
	private String responseword1="";// 获取理赔状态服务器的响应
	private String caseid="";// 当前案件id
	private String claimid="";// 当前对应的案件的理赔id
	private String phonenumber="";// 查询案件时的手机号
	private String platenumber="";// 查询案件时的车牌号
	private String password = "";// 用户专属密码
	
	private int photo_tag=1;// 区别是应急拍照，还是自助查勘拍照
	private int casedescription_tag=1;// 判断是从确认服务跳到自助查勘还是从主页面的应急拍照调到自助查勘
	private Timer mytimer;// 获取理赔的计时器
	private int claimidstate=0;// 理赔状态
	private int caseidstate=0;// 案件状态
	private int stepstate = 0;// 流程中的第几步
	private int legendid = 1;// 处理的当前的图例id
	private String claimuserid;// 理赔专员用户id
	private boolean helpupdate = true;// 是否要更新帮助页面
	private File file;//拍照的图片文件
	private boolean uploadon = false;// 正在上传图片
//	public byte[] dataByte;
	public int messageNumber = 0;//消息通知条数
	public int sayNumber = 0;//即时消息条数
	public int selfHelpFlag = 0;//自助演示标记  1代表为自助演示的模式
	public int server_type = 0;//确认服务方式 ,0未确定服务方式，1，自助查勘2，自助理赔
	public int progressbarmax = 0 ;//进度条最大值
	public int currentprogressbar = 0 ;//当前进度条位置
	public String address = "";//地址信息
	public float latitude=0;//经度
	public float longitude=0;//纬度
	public int sim_state = 0;//1代表移动网络  2代表联通网络   3代表电信网络 0代表无SIM卡或者sim卡无效
	public int service_number = 0;//状态栏消息推送用到的消息条数
	public boolean isagree =false;//是否同意价格
	public String servermsg ="";
	public Timer timer; //静态线程计时器
	public int handPicFlag = 0;//0代表导入的图片 1代表当场拍摄的照片
	public int sw = 0;
	public int sh = 0;
	public int maintoleft = 0;
	public boolean log_start = true; 
	public String nowTime = null;
	
	
	public String secretsystem = "";
	public String secretclient = "";
	
	public String getNowTime() {
		return nowTime;
	}
	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	/*public Bitmap getImageBitmap() {
		return imageBitmap;
	}
	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}*/
	
	
	
	public boolean isLog_start() {
		return log_start;
	}
	public sc_BigStone[] getmStones() {
		return mStones;
	}
	public void setmStones(sc_BigStone[] mStones) {
		this.mStones = mStones;
	}
	public String getURL_PIC() {
		return URL_PIC;
	}
	public void setURL_PIC(String uRL_PIC) {
		URL_PIC = uRL_PIC;
	}
	public String getURL_PIC1() {
		return URL_PIC1;
	}
	public void setURL_PIC1(String uRL_PIC1) {
		URL_PIC1 = uRL_PIC1;
	}
	public void setLog_start(boolean log_start) {
		this.log_start = log_start;
	}
	public int getMaintoleft() {
		return maintoleft;
	}
	public void setMaintoleft(int maintoleft) {
		this.maintoleft = maintoleft;
	}
	public int getSw() {
		return sw;
	}
	public void setSw(int sw) {
		this.sw = sw;
	}
	public int getSh() {
		return sh;
	}
	public void setSh(int sh) {
		this.sh = sh;
	}
	public int getHandPicFlag() {
		return handPicFlag;
	}
	public void setHandPicFlag(int handPicFlag) {
		this.handPicFlag = handPicFlag;
	}
	public String getServermsg() {
		return servermsg;
	}
	public void setServermsg(String servermsg) {
		this.servermsg = servermsg;
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	public boolean isIsagree() {
		return isagree;
	}
	public void setIsagree(boolean isagree) {
		this.isagree = isagree;
	}
	public int getService_number() {
		return service_number;
	}
	public void setService_number(int service_number) {
		this.service_number = service_number;
	}
	public int getSim_state() {
		return sim_state;
	}
	public void setSim_state(int sim_state) {
		this.sim_state = sim_state;
	}
	public String getAddress() {
//		address = "深圳市南山区科发路";
//		address = "江苏省南京市玄武区珠江路588号";
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getLatitude() {
//		longitude = 113.930444f;
//		longitude = 118.805508f;
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
//		longitude = 22.532987f;
//		longitude = 32.05387f;
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public int getCurrentprogressbar() {
		return currentprogressbar;
	}
	public void setCurrentprogressbar(int currentprogressbar) {
		this.currentprogressbar = currentprogressbar;
	}
	public int getProgressbarmax() {
		return progressbarmax;
	}
	public void setProgressbarmax(int progressbarmax) {
		this.progressbarmax = progressbarmax;
	}
	public int getServer_type() {
		return server_type;
	}

	public void setServer_type(int server_type) {
		this.server_type = server_type;
	}


	public int getMessageNumber() {
		return messageNumber;
	}


	public int getSayNumber() {
		return sayNumber;
	}


	public void setSayNumber(int sayNumber) {
		this.sayNumber = sayNumber;
	}


	public int getSelfHelpFlag() {
		return selfHelpFlag;
	}


	public void setSelfHelpFlag(int selfHelpFlag) {
		this.selfHelpFlag = selfHelpFlag;
	}


	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}


	public Activity getContext() {
		return context;
	}

/*	public byte[] getDataByte() {
		return dataByte;
	}

	public void setDataByte(byte[] dataByte) {
		this.dataByte = dataByte;
	}*/

	public void setContext(Activity context) {
		this.context = context;
	}

	public Context getContext2() {
		return context2;
	}

	public void setContext2(Context context2) {
		this.context2 = context2;
	}

	public ProgressDialog getProgressdialog() {
		return progressdialog;
	}

	public void setProgressdialog(ProgressDialog progressdialog) {
		this.progressdialog = progressdialog;
	}

	public String getResponseword() {
		return responseword;
	}

	public void setResponseword(String responseword) {
		this.responseword = responseword;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getClaimid() {
		return claimid;
	}

	public void setClaimid(String claimid) {
		this.claimid = claimid;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPlatenumber() {
		return platenumber;
	}

	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}

	public int getPhoto_tag() {
		return photo_tag;
	}

	public void setPhoto_tag(int photo_tag) {
		this.photo_tag = photo_tag;
	}

	public int getCasedescription_tag() {
		return casedescription_tag;
	}

	public void setCasedescription_tag(int casedescription_tag) {
		this.casedescription_tag = casedescription_tag;
	}

	public Timer getMytimer() {
		return mytimer;
	}

	public void setMytimer(Timer mytimer) {
		this.mytimer = mytimer;
	}

	public int getClaimidstate() {
		return claimidstate;
	}

	public void setClaimidstate(int claimidstate) {
		this.claimidstate = claimidstate;
	}

	public int getCaseidstate() {
		return caseidstate;
	}

	public void setCaseidstate(int caseidstate) {
		this.caseidstate = caseidstate;
	}

	public int getStepstate() {
		return stepstate;
	}

	public void setStepstate(int stepstate) {
		this.stepstate = stepstate;
	}

	public int getLegendid() {
		return legendid;
	}

	public void setLegendid(int legendid) {
		this.legendid = legendid;
	}

	public boolean isUploadon() {
		return uploadon;
	}

	public void setUploadon(boolean uploadon) {
		this.uploadon = uploadon;
	}

	public String getResponseword1() {
		return responseword1;
	}

	public void setResponseword1(String responseword1) {
		this.responseword1 = responseword1;
	}

	public String getClaimuserid() {
		return claimuserid;
	}  

	public void setClaimuserid(String claimuserid) {
		this.claimuserid = claimuserid;
	}

	public boolean isHelpupdate() {
		return helpupdate;
	}

	public void setHelpupdate(boolean helpupdate) {
		this.helpupdate = helpupdate;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	public String getSecretsystem() {
		return secretsystem;
	}
	public void setSecretsystem(String secretsystem) {
		this.secretsystem = secretsystem;
	}
	public String getSecretclient() {
		return secretclient;
	}
	public void setSecretclient(String secretclient) {
		this.secretclient = secretclient;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
