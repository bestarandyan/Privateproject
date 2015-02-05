package com.qingfengweb.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CacheManager;

import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.database.MyAppLication;


public class LoadingActivity extends Activity{
	public DBHelper dbHelper;
	public SQLiteDatabase database;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
//	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.a_loading);
	DisplayMetrics dm = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(dm);
	MyAppLication.getInstant().setScreenW(dm.widthPixels);
	MyAppLication.getInstant().setScreenH(dm.heightPixels);
	dbHelper = new DBHelper(this);
	database = dbHelper.getReadableDatabase();
//	deleteCache();
	new Thread(runnable).start();
}
public void deleteCache(){
	File file = CacheManager.getCacheFileBaseDir();

	   if (file != null && file.exists() && file.isDirectory()) {
	    for (File item : file.listFiles()) {
	     item.delete();
	    }
	    file.delete();
	   }
	   
	  this.deleteDatabase("webview.db");
	  this.deleteDatabase("webviewCache.db");
}
Runnable runnable = new Runnable() {
	
	@Override
	public void run() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			handler.sendEmptyMessage(0);//开始初始化
	}
};
Handler handler = new Handler(){

	@Override
	public void handleMessage(Message msg) {
		if(msg.what == 0){//初始化促销活动数据
			getCuxiaoData();
		}else if(msg.what == 1){//初始化特惠商品数据
			getShopData();
		}else if(msg.what == 2){//初始化品牌数据
			getDatabaseData(DatabaseSql.BRAND_TABLENAME_STRING);
		}else if(msg.what == 3){//初始化楼层数据
			getDatabaseData(DatabaseSql.FLOOR_BRAND_TABLENAME_STRING);
		}else if(msg.what == 4){//初始化 美食数据
			getFoodsData();
		}else if(msg.what == 5){//跳转到主界面
			Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		super.handleMessage(msg);
	}
	
};
/**
 * 初始化促销活动的数据
 */
public void getCuxiaoData() {
	try{
		ArrayList<HashMap<String, String>>  list = new ArrayList<HashMap<String,String>>();
		Cursor cursor = database.query(DatabaseSql.CUXIAO_TABLENAME_STRING, 
				null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("title", cursor.getString(cursor.getColumnIndex("title")));
			map.put("brand_name", cursor.getString(cursor.getColumnIndex("brand_name")));
			map.put("floor_name", cursor.getString(cursor.getColumnIndex("floor_name")));
			map.put("brand_id", cursor.getString(cursor.getColumnIndex("brand_id")));
			map.put("floor_id", cursor.getString(cursor.getColumnIndex("floor_id")));
			map.put("start_date", cursor.getString(cursor.getColumnIndex("start_date")));
			map.put("end_date", cursor.getString(cursor.getColumnIndex("end_date")));
			map.put("info_id", cursor.getString(cursor.getColumnIndex("info_id")));
			map.put("promotion_type", cursor.getString(cursor.getColumnIndex("promotion_type")));
			map.put("promotion_url", cursor.getString(cursor.getColumnIndex("promotion_url")));
			list.add(map);
		}
		MyAppLication.getInstant().setCuxiaoList(list);
		cursor.close();
		
	}catch(Exception e){
		e.printStackTrace();
	}
	handler.sendEmptyMessage(1);
}

/**
 * 初始化特惠商品数据
 */
public void getShopData(){
	//查询出数据保存在list集合中
	ArrayList<HashMap<String, String>>  list = new ArrayList<HashMap<String,String>>();
	Cursor cursor = database.query(DatabaseSql.SPECIALGOODS_TABLENAME_STRING,
			null, null, null, null, null, null);
	while (cursor.moveToNext()) {
		HashMap<String, String > map = new HashMap<String, String>();
		map.put("title",cursor.getString(cursor.getColumnIndex("title")));
		map.put("brand_name",cursor.getString(cursor.getColumnIndex("brand_name")));
		map.put("brand_id",cursor.getString(cursor.getColumnIndex("brand_id")));
		map.put("floor_name",cursor.getString(cursor.getColumnIndex("floor_name")));
		map.put("floor_id",cursor.getString(cursor.getColumnIndex("floor_id")));
		map.put("special_pic",cursor.getString(cursor.getColumnIndex("special_pic")));
		map.put("price",cursor.getString(cursor.getColumnIndex("price")));
		map.put("goods_no",cursor.getString(cursor.getColumnIndex("goods_no")));
		map.put("start_date",cursor.getString(cursor.getColumnIndex("start_date")));
		map.put("end_date",cursor.getString(cursor.getColumnIndex("end_date")));
		map.put("info_id",cursor.getString(cursor.getColumnIndex("info_id")));
		map.put("special_url",cursor.getString(cursor.getColumnIndex("special_url")));
		list.add(map);
	}
	MyAppLication.getInstant().setShopList(list);
	cursor.close();
	handler.sendEmptyMessage(2);
}
/**
 * 查询品牌列表或者楼层列表的数据。
 */
public void getDatabaseData(String tableName){
	try{
 ArrayList<HashMap<String, Object>>  list = new ArrayList<HashMap<String,Object>>();
	//从数据库查询出数据保存在list中
	ArrayList<String> floorList = new ArrayList<String>();
	Cursor cursor = database.query(tableName, 
			new String[]{"floor_name"}, null, null, null, null, "floor_sort desc");
	while (cursor.moveToNext()) {
		String floorNameString = cursor.getString(cursor.getColumnIndex("floor_name"));
		if(floorList !=null && !floorList.contains(floorNameString)){
			floorList.add(floorNameString);
		}
	}
	for (int i = 0; i < floorList.size(); i++) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("louceng", floorList.get(i));
		Cursor  floorCursor = database.query(tableName, 
				null, "floor_name=?", new String[]{floorList.get(i)}, null, null, null);
		ArrayList<String> pinpaiList = new ArrayList<String>();
		while (floorCursor.moveToNext()) {
			if(tableName.equals(DatabaseSql.BRAND_TABLENAME_STRING)){
				pinpaiList.add(floorCursor.getString(floorCursor.getColumnIndex("brand_name")));
			}else if(tableName.equals(DatabaseSql.FLOOR_BRAND_TABLENAME_STRING)){
				pinpaiList.add(floorCursor.getString(floorCursor.getColumnIndex("cat_name")));
			}
			
		}
		map.put("brandList", pinpaiList);
		list.add(map);
		floorCursor.close();
	}
	if(tableName.equals(DatabaseSql.BRAND_TABLENAME_STRING)){
		MyAppLication.getInstant().setBrandList(list);
		handler.sendEmptyMessage(3);
	}else if(tableName.equals(DatabaseSql.FLOOR_BRAND_TABLENAME_STRING)){
		MyAppLication.getInstant().setFloorList(list);
		handler.sendEmptyMessage(4);
	}
	cursor.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
public void getFoodsData(){
	ArrayList<HashMap<String, String>>  list = new ArrayList<HashMap<String,String>>();
	Cursor cursor = database.query(DatabaseSql.FOOD_TABLENAME_STRING, null, null, null, null, null, null);
	while (cursor.moveToNext()) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("delivery_time1", cursor.getString(cursor.getColumnIndex("delivery_time1")));
		map.put("food_tel", cursor.getString(cursor.getColumnIndex("food_tel")));
		map.put("floor_name", cursor.getString(cursor.getColumnIndex("floor_name")));
		map.put("condition", cursor.getString(cursor.getColumnIndex("condition")));
		map.put("delivery_time2", cursor.getString(cursor.getColumnIndex("delivery_time2")));
		map.put("delivery_time3", cursor.getString(cursor.getColumnIndex("delivery_time3")));
		map.put("brand_name", cursor.getString(cursor.getColumnIndex("brand_name")));
		map.put("brand_id", cursor.getString(cursor.getColumnIndex("brand_id")));
		map.put("food_pic", cursor.getString(cursor.getColumnIndex("food_pic")));
		map.put("info_id", cursor.getString(cursor.getColumnIndex("info_id")));
		list.add(map);
	}
	MyAppLication.getInstant().setFoodList(list);
	cursor.close();
	handler.sendEmptyMessage(5);
}
@Override
protected void onDestroy() {
	database.close();
	database.releaseReference();
	database.releaseMemory();
	super.onDestroy();
}

@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	if(keyCode == KeyEvent.KEYCODE_BACK){
		return true;
	}
	return true;
}
}
