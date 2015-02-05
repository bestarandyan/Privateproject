package com.chinaLife.claimAssistant;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import com.sqlcrypt.database.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import com.chinaLife.claimAssistant.bean.sc_LegendInfo;
import com.chinaLife.claimAssistant.bean.sc_SecretInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_GetKey;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData;
import com.chinaLife.claimAssistant.thread.sc_UploadData1;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_SharedPreferencesinfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class sc_Splash extends Activity {
//	private boolean boo = false;
	private sc_DBHelper database = null;
	private sc_UploadData uploaddata = null;
	
	private int tag = 1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.sc_a_splash);
		sc_ExitApplication.getInstance().context = sc_Splash.this;
		sc_ExitApplication.getInstance().addActivity(sc_Splash.this);
		tag = sc_SharedPreferencesinfo.getdata(this).getInt("first", 1);
		/*SharedPreferences sharedata = getSharedPreferences("data", 0);  
		if(sharedata!=null){
			String data = sharedata.getString("item", null);   
			if(data!=null && "hello".equals(data)){
				boo = true;
			}else{
				boo = false;
				Editor sharedata1 = getSharedPreferences("data", 0).edit();   
				sharedata1.putString("item","hello");   
				sharedata1.commit();  
			}
		}else{
			boo = false;
			Editor sharedat
			a1 = getSharedPreferences("data", 0).edit();   
			sharedata1.putString("item","hello");   
			sharedata1.commit();  
		}*/
//		boo = true;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		int mItemwidth = dm.widthPixels;
//		int mItemHerght = dm.heightPixels;
		AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, sc_CallAlarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		am.cancel(sender);
		initData();
//		 final LinearLayout loginLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog, null);
//		 new AlertDialog.Builder(this)
//		 .setTitle("输入测试ip")
//		 .setView(loginLayout)//////http://10.2.51.167:7001/selfClaim//interface/
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						// 这里如果要拿到XML中的对话框的话仅仅用findviewbyid是不行的，在之前要使用开始得到
//						// 的VIEW，例如此题应当：　、
//						EditText myedittext = (EditText) loginLayout
//								.findViewById(R.id.testurl);
//						MyApplication.URL ="http://" +myedittext.getText().toString()+"/selfClaim/interface/";
//						MyApplication.LINK_URL = "http://" +myedittext.getText()
//								.toString()+"/selfClaim/interface/";
//						MyApplication.TELECOM_URL = "http://" +myedittext.getText()
//								.toString()+"/selfClaim/interface/";
//						MyApplication.NEI_NET_URL = "http://" +myedittext.getText()
//								.toString()+"/selfClaim/interface/";
//						MyApplication.MOVE_URL ="http://" + myedittext.getText().toString()+"/selfClaim/interface/";
//						initData();
//					}
//
//		 }).setCancelable(false)
//		 .show();
		
	}
	
	/**
	 * 初始化数据
	 */
	public void initData() {
		sc_MyApplication.getInstance().setContext(this);
		sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
//		database = sc_DBHelper.getInstance();
//		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
//        String operator = telManager.getSimOperator(); 
//		 if(operator!=null){ 
//		        if(operator.equals("46000") || operator.equals("46002")){ 
//		         //中国移动 
//		        	MyApplication.getInstance().setSim_state(1);
//		        }else if(operator.equals("46001")){ 
//		         //中国联通 
//		        	MyApplication.getInstance().setSim_state(2);
//		        }else if(operator.equals("46003")){ 
//		         //中国电信 
//		        	MyApplication.getInstance().setSim_state(3);
//		        } 
//		} 
//		int sim_state = MyApplication.getInstance().getSim_state();
//		if(sim_state == 0){
//			MyApplication.URL = MyApplication.MOVE_URL;
//		}else if(sim_state == 1){//移动
//			MyApplication.URL = MyApplication.MOVE_URL;
//		}else if(sim_state == 2){//联通
//			MyApplication.URL = MyApplication.LINK_URL;
//		}else if(sim_state == 3){//电信
//			MyApplication.URL = MyApplication.TELECOM_URL;
//		}
		SharedPreferences.Editor sharedata = this.getSharedPreferences("userinfo", 0)
				.edit();
		sharedata.putInt("threaddemo", 1);
		sharedata.commit();
		checkVersion();
		new Thread(runnable2).start();
	}

	private Runnable runable = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Intent intent = new Intent();
			if (tag == 1) {
				intent.setClass(sc_Splash.this, sc_GuideActivity.class);
			} else {
				intent.setClass(sc_Splash.this, sc_MainActivity.class);
			}
			startActivity(intent);
			sc_Splash.this.finish();
			uploaddata.Post();
		}
	};

	private Runnable runable1 = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<Map<String, Object>> selectresult =  new ArrayList<Map<String,Object>>();/*database.selectRow(
					"select * from appinfo", null);*/
			if (selectresult.size() > 0) {
				try{
					sc_MyApplication.ERROR_VALUE = Long.parseLong(selectresult.get(0)
							.get("error_value").toString());
				}catch(Exception e){
					sc_MyApplication.ERROR_VALUE = 0L;
				}
			} else {
				sc_MyApplication.ERROR_VALUE = 0L;
			}
			
			/*if(!boo){
				Intent intent = new Intent(Splash.this,
						ExampleActivity.class);
				startActivity(intent);
				Splash.this.finish();
			}else{*/
			Intent intent = new Intent();
			if (tag == 1) {
				intent.setClass(sc_Splash.this, sc_GuideActivity.class);
			} else {
				intent.setClass(sc_Splash.this, sc_MainActivity.class);
			}
			startActivity(intent);
			sc_Splash.this.finish();
//			}
		}
	};
	
	
	private Runnable runnable2 = new Runnable() {
		
		@Override
		public void run() {
			sc_GetKey.getKey(sc_Splash.this);
			if(isLegendUpdate()){
				getLegendinfo();
			}
		}
	};
	
	/**
	 * 获取图例更新时间
	 */
	public boolean isLegendUpdate() {

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("action", "checkoutLegend"));
		params.add(new BasicNameValuePair("updateTime", "getLegendUpdateTime"));
		sc_UploadData1 uploaddata = new sc_UploadData1(sc_MyApplication.URL + "claim",
				params);
		uploaddata.Post();
		String reponse = uploaddata.getReponse();
		params.clear();
		params = null;
		return dealreponse1(reponse);
	}
	
	/**
	 * 
	 */
	public boolean dealreponse1(String reponse){
		if(reponse!=null&&reponse.equals("1")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 获取图例对象列表
	 */
	
	public void getLegendinfo() {
		String updatetime = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = sc_DBHelper.getInstance().selectRow(
				"select max(updatetime) max_updatetime from legendinfo", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"max_updatetime") != null) {
				updatetime = (selectresult.get(selectresult.size() - 1).get(
						"max_updatetime").toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getLegendListByUpdateTime"));

		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("updateTime", updatetime));
		sc_UploadData1 uploaddata = new sc_UploadData1(sc_MyApplication.URL + "claim",params);
		uploaddata.Post();
		String reponse = uploaddata.getReponse();
		dealreponse2(reponse);
		params.clear();
		params = null;
	}
	
	
	
	
	/**
	 * 
	 */
	public void dealreponse2(String reponse){
		if(reponse.contains("[")){
			Type listType = new TypeToken<LinkedList<sc_LegendInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<sc_LegendInfo> legendinfos = null;
			sc_LegendInfo legendinfo = new sc_LegendInfo();
			ContentValues values = new ContentValues();
			legendinfos = gson.fromJson(reponse, listType);
			if(legendinfos!=null&&legendinfos.size()>0){
				for (Iterator<sc_LegendInfo> iterator = legendinfos
						.iterator(); iterator.hasNext();) {
					legendinfo = (sc_LegendInfo) iterator.next();
					values.clear();
					int legendid = 0;
					try {
						legendid = Integer.parseInt(legendinfo
								.getLegendid());
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(legendid>=1&&legendid<=13){
						continue;
					}
					values.put("legendid", legendinfo.getLegendid());
					values.put("type", legendinfo.getType());
					values.put("code", legendinfo.getCode());
					values.put("legendimageurl", legendinfo.getExampleimage());
					values.put("legendtext", legendinfo.getName());
					values.put("maskimageurl", legendinfo.getMaskimage());
					values.put("masktext", legendinfo.getMaskimagedescription());
					values.put("remark", legendinfo.getRemark());
					values.put("updatetime", legendinfo.getUpdatetime());
					if (sc_DBHelper
							.getInstance()
							.selectRow(
									"select * from legendinfo where legendid = '"
											+ legendinfo.getLegendid()
											+ "'", null).size() <= 0) {
						sc_DBHelper.getInstance()
								.insert("legendinfo", values);
					} else {
						sc_DBHelper.getInstance()
								.update("legendinfo",
										values,
										"legendid = ?",
										new String[] {legendinfo.getLegendid()});
					}
				}
			}
		}
	}

	/**
	 * 检查版本信息
	 */
	public void checkVersion() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getVersion"));
			uploaddata = new sc_UploadData(sc_MyApplication.URL + "version", params,
					1);
			new Thread(runable).start();
		} else {
			new Thread(runable1).start();
			
		}
	}

	
	@Override
	protected void onDestroy() {
		SharedPreferences.Editor sharedata = sc_MyApplication.getInstance().getContext2().getSharedPreferences("userinfo", 0)
				.edit();
		sharedata.putInt("threaddemo", 0);
		sharedata.commit();
		super.onDestroy();
	}
}
