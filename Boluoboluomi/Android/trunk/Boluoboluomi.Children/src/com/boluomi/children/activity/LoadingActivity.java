/**
 * 
 */
package com.boluomi.children.activity;


import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.boluomi.children.R;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.UserBeanInfo;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.model.AppInfo;
import com.boluomi.children.model.SDKInfo;
import com.boluomi.children.model.UserInfo;
import com.boluomi.children.network.NetworkCheck;
import com.boluomi.children.network.UploadData;
import com.boluomi.children.util.MessageBox;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

/**
 * @author 刘星星、武国庆
 * @createDate 2013、8、20
 * 程序入口，加载页。
 */
public class LoadingActivity extends BaseActivity{
	
	private String reponse = "";// 服务器响应值
	private UploadData uploaddata = null;
	private DBHelper db = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_loading);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.getInstant().setWidthPixels(dm.widthPixels);
		MyApplication.getInstant().setHeightPixels(dm.heightPixels);
		initData();
	}
	
	/**
	 * 初始化数据
	 */
	
	private void initData() {
		db = DBHelper.getInstance(this);
		if (!NetworkCheck.IsHaveInternet(LoadingActivity.this)) {//检查是否有网络连接
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("没有检测到网络，请检查您的网络连接！")
			.setTitle("提示！")
			       .setCancelable(false)
			       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   new Thread(checkUserRunnable).start();
			        	   dialog.dismiss();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}else{
			 new Thread(checkUserRunnable).start();
		}
		
	}
	


	/***
	 * @author 刘星星 
	 * 进入软件时，开始检查登录情况，初始化应用信息
	 */
	public Runnable checkUserRunnable = new Runnable() {

		@Override
		public void run() {
				if (checkLogin()) {//查找是否有最后登录的用户 这里如果有登陆过的用户，会将用户的门店信息初始化在应用程序中
					handler.sendEmptyMessageDelayed(1, 2000);
				}else if(checkGuide() && checkSelectStore()){
					handler.sendEmptyMessageDelayed(1, 2000);
				}else if(checkGuide()){
					handler.sendEmptyMessageDelayed(3, 2000);
				}else{//如果 没有用户登录过  这里也包括软件第一次运行 则跳转到引导页
					handler.sendEmptyMessageDelayed(2, 2000);
				}
				
		}
	};
	/**
	 * 检查软件是否曾经通过了引导页
	 * @return
	 */
	private boolean checkGuide(){
		String sql = "select ispassguide from "+SDKInfo.TableName;
		List<Map<String,Object>> list = db.selectRow(sql, null);
		if(list!=null && list.size()>0){
			if(list.get(0).get("ispassguide").equals("0")){
				return false;
			}else{
				return true;
			}
		}else{
			return false;
		}
	}
	/**
	 * 检查该软件是否选择过门店
	 * @return
	 */
	private boolean checkSelectStore(){
		String sql = "select *from "+SDKInfo.TableName;
		List<Map<String,Object>> list = db.selectRow(sql, null);
		if(list!=null && list.size()>0){
			if(list.get(0).get("isselectstore").equals("0")){
				return false;
			}else if(list.get(0).get("isselectstore").equals("1")){
				UserBeanInfo.getInstant().setCurrentStore(list.get(0).get("store").toString());
				UserBeanInfo.getInstant().setCurrentStoreId(list.get(0).get("storeid").toString());
				return true;
			}
		}else{
			return false;
		}
		return false;
	}
	/***
	 * author:Ring 
	 * 处理界面跳转，从加载页跳转到主界面
	 * 
	 */

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1://如果有最新登录的用户且在上一次登陆时选择的是自动登陆 则直接跳转到软件主界面
				 Intent i = new Intent(LoadingActivity.this,MainActivity.class);
				 LoadingActivity.this.startActivity(i);
				 LoadingActivity.this.finish();
				break;
			case 2://如果 没有用户登录过  这里也包括软件第一次运行 则跳转到引导页
				 Intent i1 = new Intent(LoadingActivity.this,WelcomeActivity.class);
				 LoadingActivity.this.startActivity(i1);
				 LoadingActivity.this.finish();
				break;
			case 3://通过了引导页
				Intent i2 = new Intent(LoadingActivity.this,CitySelectActivity.class);
				 LoadingActivity.this.startActivity(i2);
				 LoadingActivity.this.finish();
				break;
			}
		};
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (reponse != null) {
			reponse = null;
		}
	}

	/***
	 * @author 刘星星
	 * 查找是否有登陆过的用户
	 */

	public boolean checkLogin() {
		String  sql = "select * from "+UserInfo.TableName;
		List<Map<String, Object>> selectresult =db.selectRow(sql, null);
		if (selectresult != null && selectresult.size() > 0) {//判断是否有用户
			for(int i=0;i<selectresult.size();i++){
				Map<String,Object> map = selectresult.get(i);
				if(map.get("islastuser").equals("1")){//把最后登陆的用户信息初始化给应用程序
					UserBeanInfo.getInstant().setCurrentStoreId(map.get("storeid").toString());
					UserBeanInfo.getInstant().setCurrentStore(map.get("storename").toString());
					UserBeanInfo.getInstant().setUserid(map.get("userid").toString());
					UserBeanInfo.getInstant().setUserName(map.get("username").toString());
					UserBeanInfo.getInstant().setUserScore(map.get("points").toString());
					UserBeanInfo.getInstant().setPassword(map.get("password").toString());
					if(map.get("isautologin").toString().equals("1")){
						UserBeanInfo.getInstant().setAutoLogin(true);
						UserBeanInfo.getInstant().setLogined(true);
					}else{
						UserBeanInfo.getInstant().setAutoLogin(false);
						UserBeanInfo.getInstant().setLogined(false);
					}
				}
			}
			return true;
		}
		return false;
	}

	/***
	 * author:Ring 
	 * 自动登录时检测，每隔三天向服务器提交以下数据 return true 已经超过三天 false 未超过三天
	 */
	@SuppressWarnings("deprecation")
	public boolean checkThreeDay() {
//		String time1 = "";
//		String time2 = "";
//		List<Map<String, Object>> selectresult1 = DBHelper
//				.getInstance(this)
//				.selectRow(
//						"select value from settingsinfo where name='SERVER_TIME'",
//						null);
//		if (selectresult1 != null && selectresult1.size() > 0) {
//			if (selectresult1.get(selectresult1.size() - 1) != null
//					&& selectresult1.get(selectresult1.size() - 1).get("value") != null) {
//				time1 = selectresult1.get(selectresult1.size() - 1)
//						.get("value").toString();
//			}
//		}
//		List<Map<String, Object>> selectresult2 = db.selectRow(
//						"select lastlogintime from userinfo where username='"
//								+ MyApplication.getInstant().getUserinfo()
//										.getUsername()
//								+ "' and password='"
//								+ MyApplication.getInstant().getUserinfo()
//										.getPassword() + "'", null);
//		if (selectresult2 != null && selectresult2.size() > 0) {
//			if (selectresult2.get(selectresult2.size() - 1) != null
//					&& selectresult2.get(selectresult2.size() - 1).get("value") != null) {
//				time2 = selectresult2.get(selectresult2.size() - 1)
//						.get("value").toString();
//			}
//		}
//
//		if (!time1.equals("") && !time2.equals("")) {
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			try {
//				Date date1 = dateFormat.parse(time1);
//				Date date2 = dateFormat.parse(time2);
//				int days = (date1.getDate() - date2.getDate())
//						/ (1000 * 60 * 60 * 24);
//				if (days >= 3) {
//					return true;
//				}
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(uploaddata!=null){
			uploaddata.overReponse();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * author:Ring 
	 * 向服务器提交登录信息 return true登录成功，false 登录失败
	 */
	public void userLogin() {
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.APPID));
//		params.add(new BasicNameValuePair("appkey", MyApplication.APPKEY));
//		params.add(new BasicNameValuePair("action", "login"));
//		params.add(new BasicNameValuePair("username", MyApplication
//				.getInstant().getUserinfo().getUsername()));
//		params.add(new BasicNameValuePair("password", MyApplication
//				.getInstant().getUserinfo().getPassword()));
//		uploaddata = new UploadData(MyApplication.URL+"user.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		params.clear();
//		params = null;
	}
}
