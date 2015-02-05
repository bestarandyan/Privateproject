package com.qingfengweb.weddingideas.activity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.beans.Product;
import com.qingfengweb.weddingideas.beans.UserBean;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.DBHelper;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.data.RequestServerFromHttp;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;

@TargetApi(Build.VERSION_CODES.ECLAIR) @SuppressLint("HandlerLeak")
public class LoadingActivity extends Activity {
	DBHelper dbHelper;
	Bitmap bitmap = null;
	public static ArrayList<Map<String,String>> configList;
	public static ArrayList<Map<String,String>> templateList;
	public static String photoOne = "4001681314";
	public static String photoTwo = "4001681314";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_loading);
		if(configList!=null && configList.size()>0){
			configList.clear();
		}
		if(templateList!=null && templateList.size()>0){
			templateList.clear();
		}
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		MyApplication.getInstance().setScreenW(dm.widthPixels);
		MyApplication.getInstance().setScreenH(dm.heightPixels);
		System.out.println("手机屏幕宽度"+dm.widthPixels);
		System.out.println("手机屏幕高低"+dm.heightPixels);
		dbHelper = DBHelper.getInstance(this);
		new Thread(runnable).start();
		
	}
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			configList = pullXml(R.xml.config);
			templateList = pullXml(R.xml.template);
			if(configList!=null && configList.size()>0){
				if(configList.get(0).get("store_phone_one")!=null && !configList.get(0).get("store_phone_one").equals("")){
					photoOne = configList.get(0).get("store_phone_one").toString();
					photoOne = photoOne.replace("-", "");
					photoOne = photoOne.replace(" ", "");
				}
				if(configList.get(0).get("store_phone_two")!=null && !configList.get(0).get("store_phone_two").equals("")){
					photoTwo = configList.get(0).get("store_phone_two").toString();
					photoTwo = photoTwo.replace("-", "");
					photoTwo = photoTwo.replace(" ", "");
				}
			}
			
			RequestServerFromHttp.SERVER_ADDRESS = configList.get(0).get("server_url");
			RequestServerFromHttp.APPID = configList.get(0).get("appid");
			RequestServerFromHttp.APPKEY = configList.get(0).get("appkey");
			RequestServerFromHttp.madeid = configList.get(0).get("madeid");
//			String msgStr = RequestServerFromHttp.getSystemTime();
//			System.out.println(msgStr);
			File file = new File(ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png");
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_share_photo);
			PicHandler.OutPutImage(file, bitmap);
			String sql = "select *from "+UserBean.tbName+" where islogin='1'";
			List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
			if(list!=null && list.size()>0){
				handler.sendEmptyMessageDelayed(1, 1000);
			}else{
				handler.sendEmptyMessageDelayed(0, 1000);
			}
		}
	};
	
	
	
	@Override
	protected void onDestroy() {
		if(bitmap!=null){
			bitmap.recycle();
		}
		super.onDestroy();
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				if(configList.get(0).get("photo_album_is_have").equals("0")){
					Intent intent = new Intent(LoadingActivity.this,LoginOrSignActivity.class);
					startActivity(intent);
					finish();
				}else{
					Intent intent = new Intent(LoadingActivity.this,GuideActivity.class);
					startActivity(intent);
					finish();
				}
				
			}else if(msg.what == 1){
					Intent intent = new Intent(LoadingActivity.this,MainVideoActivity.class);
					startActivity(intent);
					finish();
			}
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			super.handleMessage(msg);
		}
		
	};
	/**
	 * 解析配置文件
	 * @return
	 */
	private ArrayList<Map<String,String>> pullXml(int xml) {
		XmlResourceParser parser = getResources().getXml(xml);
		ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		try {
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG && parser.getName().equalsIgnoreCase("item")) {
					map.put(parser.getAttributeValue(0), parser.getAttributeValue(1));
				}
				eventType = parser.next();
			}
			list.add(map);
			return list;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
