package com.qingfengweb.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengweb.adapter.ShoppingGuideAdapter;
import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.database.DataAnalyzing;
import com.qingfengweb.database.MyAppLication;
import com.qingfengweb.network.GetData;

/**
 * Created by 鍒樻槦鏄�on 13-5-23.
 * 娲诲姩涓撳尯绫�
 */
public class ShoppingGuideActivity extends BaseActivity implements OnClickListener{
	private ListView listView;
	private TextView pinPaiView,louCengView;
	ImageButton leftBtn;
	LinearLayout centerLayout;
	ImageView bottomImg;
	private ArrayList<HashMap<String, Object>>  list = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>>  list1 = new ArrayList<HashMap<String,Object>>();
	private String msgString = "";
	public static final String BRAND_TYPE_STRING = "brand";
	public static final String FLOOR_TYPE_STRING = "floor";
	public String currentType = BRAND_TYPE_STRING;
	public String currentDownloadType = BRAND_TYPE_STRING;//当前下载类型
//	public ProgressDialog progressDialog;
	LinearLayout proLayout;
	TextView stateTv;
	DBHelper dbHelper;
	SQLiteDatabase database;
	public static TextView failText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_shoppingguide);
		dbHelper = new DBHelper(this);
		findview();
//		getdataFromBrand(DatabaseSql.BRAND_TABLENAME_STRING);
//		createProgress("正在加载数据请稍候！");
		getData();
		
	}
	private void getData(){
		new Thread(getDataRunnable).start();
	}
	Runnable getDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			initDataList();
		}
	};
	 private void initDataList(){
		 if(currentType.equals(BRAND_TYPE_STRING)){
				handler.sendEmptyMessage(4);
			}else if(currentType.equals(FLOOR_TYPE_STRING)){
				handler.sendEmptyMessage(5);
			}
	 }
	Runnable getListDataRunnable  = new Runnable() {
		
		@Override
		public void run() {
			 ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	         if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
	        	 	handler.sendEmptyMessage(3);
	        	 	if(currentDownloadType.equals(BRAND_TYPE_STRING)){
	        	 		loadRunableFlag = false;
	 				}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
	 					loadRunableFlag = false;
	 				}
	         }else {
	        	 	handler.sendEmptyMessage(6);//为了隐藏刷新控件
	        	 	msgString = GetData.getBrandDetailData(currentDownloadType);
		 			if(msgString.length()>3){
		 				loadRunableFlag = true;
		 				if(currentDownloadType.equals(BRAND_TYPE_STRING)){
		 					getData(DatabaseSql.BRAND_TABLENAME_STRING);
		 					proLayoutFlag = true;
		 					handler.sendEmptyMessage(0);
		 				}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
		 					getData(DatabaseSql.FLOOR_BRAND_TABLENAME_STRING);
		 					proLayoutFlag = true;
		 					handler.sendEmptyMessage(1);
		 				}
		 				
		 			}else{
		 				if(currentDownloadType.equals(BRAND_TYPE_STRING)){
		 					loadRunableFlag = false;
		 				}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
		 					loadRunableFlag = false;
		 				}
		 				handler.sendEmptyMessage(2);//访问服务器失败
		 			}
			}
			
		}
	};
	private boolean proLayoutFlag = false;//用来判断进度条是否应该显示 false为应该显示
	private boolean loadRunableFlag = false;//判断数据是否加载过。
	Handler handler = new Handler(){
	
		@Override
		public void handleMessage(Message msg) {
//				progressDialog.dismiss();
				if(msg.what == 2){
//					showDialog();
					if(list==null || list.size()==0){
						failText.setVisibility(View.VISIBLE);
					}else{
						failText.setVisibility(View.GONE);
						notiFyListView(0,MyAppLication.getInstant().getBrandList());
					}
					proLayout.setVisibility(View.GONE);
				}else if (msg.what == 3) {
//					dialogWronFun("未检测到网络，请检查网络连接！", ShoppingGuideActivity.this);
					if(list==null || list.size()==0){
						failText.setVisibility(View.VISIBLE);
					}else{
						failText.setVisibility(View.GONE);
						notiFyListView(0,MyAppLication.getInstant().getBrandList());
					}
					proLayout.setVisibility(View.GONE);
				}else if(msg.what == 4){//点击品牌罗列时
					notiFyListView(0,MyAppLication.getInstant().getBrandList());
					if(!loadRunableFlag){
						if(MyAppLication.getInstant().getBrandList().size()==0){
							proLayout.setVisibility(View.VISIBLE);
						}else{
							proLayout.setVisibility(View.GONE);
							failText.setVisibility(View.GONE);
						}
						currentDownloadType = BRAND_TYPE_STRING;
						new Thread(getListDataRunnable).start();
						loadRunableFlag = true;
					}
					
				}else if(msg.what == 5){//点击楼层列表时
					notiFyListView(1,MyAppLication.getInstant().getFloorList());
					if(!loadRunableFlag){
						if(MyAppLication.getInstant().getFloorList().size()==0){
							proLayout.setVisibility(View.VISIBLE);
						}else{
							proLayout.setVisibility(View.GONE);
							failText.setVisibility(View.GONE);
						}
						new Thread(getListDataRunnable).start();
						loadRunableFlag = true;
					}
				}else if(msg.what == 6){//隐藏刷新控件
					failText.setVisibility(View.GONE);
				}else if(msg.what == 0) {//品牌罗列下载完毕
					if(currentType.equals(BRAND_TYPE_STRING)){
						notiFyListView(msg.what,MyAppLication.getInstant().getBrandList());
						if(list.size()!=0){
							failText.setVisibility(View.GONE);
						}
						if(proLayoutFlag){
							proLayout.setVisibility(View.GONE);
						}
					}
					currentDownloadType = FLOOR_TYPE_STRING;
					new Thread(getListDataRunnable).start();
				}else if(msg.what == 1) {//楼层列表下载完毕
					if(currentType.equals(FLOOR_TYPE_STRING)){
						notiFyListView(msg.what,MyAppLication.getInstant().getFloorList());
					}
					if(list1.size()!=0){
						failText.setVisibility(View.GONE);
					}
					if(proLayoutFlag){
						proLayout.setVisibility(View.GONE);
					}
					
				}
				
			super.handleMessage(msg);
		}
		
	};
	
	private void showDialog(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("提示");
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.setMessage("可能网络状态不好，数据加载失败，是否重试？");
		alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				proLayout.setVisibility(View.VISIBLE);
				new Thread(getListDataRunnable).start();
			}
		});
		alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		Dialog dialog = alertDialog.create();
		dialog.show();
	}
	ShoppingGuideAdapter adapter;
	private void notiFyListView(int type,ArrayList<HashMap<String, Object>> adapterList){
		 adapter = new ShoppingGuideAdapter(this, adapterList,type);
		listView.setAdapter(adapter);
			if(currentType == BRAND_TYPE_STRING){
				centerLayout.setBackgroundColor(Color.rgb(18, 71, 133));
				bottomImg.setBackgroundResource(R.drawable.zl_bottom_bg1);
				pinPaiView.setTextColor(Color.WHITE);
				louCengView.setTextColor(Color.rgb(170, 170, 170));
			}else if (currentType == FLOOR_TYPE_STRING) {
				centerLayout.setBackgroundColor(Color.rgb(0, 136, 144));
				bottomImg.setBackgroundResource(R.drawable.zl_bottom_bg);
				louCengView.setTextColor(Color.WHITE);
				pinPaiView.setTextColor(Color.rgb(170, 170, 170));
			}
	}
	private void findview(){
		listView = (ListView) findViewById(R.id.centerLv);
		pinPaiView = (TextView) findViewById(R.id.pinpaiTv);
		louCengView = (TextView) findViewById(R.id.loucengTv);
		leftBtn = (ImageButton) findViewById(R.id.backBtn);
		leftBtn.setOnClickListener(this);
		pinPaiView.setOnClickListener(this);
		louCengView.setOnClickListener(this);
		failText = (TextView) findViewById(R.id.connectFailText);
    	failText.setOnClickListener(this);
		centerLayout = (LinearLayout) findViewById(R.id.centerLayout);
		bottomImg = (ImageView) findViewById(R.id.bottomImg);
		proLayout = (LinearLayout) findViewById(R.id.proLayout);
		stateTv = (TextView) findViewById(R.id.stateText);
	}
	/**
	 * 解析获取的数据
	 * @param tableName
	 */
	private void getData(String tableName){
		//解析并保存数据
		synchronized (writeLock) {
			database = dbHelper.getWritableDatabase();
			ArrayList<String> object = DataAnalyzing.analyzingData(msgString);
			if(currentDownloadType.equals(BRAND_TYPE_STRING)){
				DataAnalyzing.jsonBrandDetail(object.get(1),database);
			}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
				DataAnalyzing.jsonFloorData(object.get(1).toString(),database);
			}
			getDatabaseData(tableName);
		}
	}
	
	/**
	 * 从数据库获取数据
	 */
	public void getDatabaseData(String tableName){
//		synchronized (writeLock) {
		try{
		//从数据库查询出数据保存在list中
		ArrayList<String> floorList = new ArrayList<String>();
		Cursor cursor = dbHelper.getReadableDatabase().query(tableName, 
				new String[]{"floor_name"}, null, null, null, null, "floor_sort desc");
		
		if(currentDownloadType.equals(BRAND_TYPE_STRING)){
			list.clear();
		}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
			list1.clear();
		}
		while (cursor.moveToNext()) {
			String floorNameString = cursor.getString(cursor.getColumnIndex("floor_name"));
			if(floorList !=null && !floorList.contains(floorNameString)){
				floorList.add(floorNameString);
			}
		}
		for (int i = 0; i < floorList.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("louceng", floorList.get(i));
			Cursor  floorCursor = dbHelper.getReadableDatabase().query(tableName, 
					null, "floor_name=?", new String[]{floorList.get(i)}, null, null, null);
			ArrayList<String> pinpaiList = new ArrayList<String>();
			while (floorCursor.moveToNext()) {
				if(currentDownloadType.equals(BRAND_TYPE_STRING)){
					pinpaiList.add(floorCursor.getString(floorCursor.getColumnIndex("brand_name")));
				}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
					pinpaiList.add(floorCursor.getString(floorCursor.getColumnIndex("cat_name")));
				}
				
			}
			map.put("brandList", pinpaiList);
			if(currentDownloadType.equals(BRAND_TYPE_STRING)){
				list.add(map);
			}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
				list1.add(map);
			}
			
		}
		
		if(currentDownloadType.equals(BRAND_TYPE_STRING)){
			MyAppLication.getInstant().setBrandList(list);
		}else if(currentDownloadType.equals(FLOOR_TYPE_STRING)){
			MyAppLication.getInstant().setFloorList(list1);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
//		}
	}
	private void getdataFromBrand(String tableName){
		database = dbHelper.getWritableDatabase();
		database.execSQL(DatabaseSql.BRAND_SQL_STRING);
		database.execSQL(DatabaseSql.FLOOR_BRAND_SQL_STRING);
		Cursor cursor = database.rawQuery("select * from "+tableName+" limit 1,1", null);
		String floor  = "";
		while(cursor.moveToNext()){
			 floor  = cursor.getString(cursor.getColumnIndex("floor_name"));
		}
		if(floor!=null && floor.length()>0){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("louceng", floor);
			Cursor  floorCursor = database.query(tableName, 
					null, "floor_name=?", new String[]{floor}, null, null, null);
			ArrayList<String> pinpaiList = new ArrayList<String>();
			while (floorCursor.moveToNext()) {
				if(currentType.equals(BRAND_TYPE_STRING)){
					pinpaiList.add(floorCursor.getString(floorCursor.getColumnIndex("brand_name")));
				}else if(currentType.equals(FLOOR_TYPE_STRING)){
					pinpaiList.add(floorCursor.getString(floorCursor.getColumnIndex("cat_name")));
				}
				
			}
			map.put("brandList", pinpaiList);
			list.add(map);
		}
	}
	@Override
	public void onClick(View v) {
		if(v == pinPaiView){
			currentType = BRAND_TYPE_STRING;
			proLayoutFlag = false;
			handler.sendEmptyMessage(4);
			centerLayout.setBackgroundColor(Color.rgb(18, 71, 133));
			bottomImg.setBackgroundResource(R.drawable.zl_bottom_bg1);
			pinPaiView.setTextColor(Color.WHITE);
			louCengView.setTextColor(Color.rgb(170, 170, 170));
		}else if (v == louCengView) {
			currentType = FLOOR_TYPE_STRING;
			proLayoutFlag = false;
			handler.sendEmptyMessage(5);
			centerLayout.setBackgroundColor(Color.rgb(0, 136, 144));
			bottomImg.setBackgroundResource(R.drawable.zl_bottom_bg);
			louCengView.setTextColor(Color.WHITE);
			pinPaiView.setTextColor(Color.rgb(170, 170, 170));
		}else if(v == failText && failText.getVisibility() == View.VISIBLE){
//			if(list.size()==0){
				proLayout.setVisibility(View.VISIBLE);
//			}else{
//				proLayout.setVisibility(View.GONE);
//			}
			failText.setVisibility(View.GONE);
			new Thread(getListDataRunnable).start();
			
		}else if(v == leftBtn){
    		MainActivity.mHost.setCurrentTab(0);
    	}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
//			alerBuilder.setTitle("提示！");
//			alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//			alerBuilder.setMessage("确定要退出程序吗？");
//			alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					android.os.Process.killProcess(android.os.Process.myPid());
//					finish();
//					System.gc();
//				}
//			});
//			alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					return ;
//				}
//			});
//			Dialog dialog = alerBuilder.create();
//			
//			dialog.show();
			MainActivity.mHost.setCurrentTab(0);
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		database.close();
		database.releaseReference();
		database.releaseMemory();
		super.onDestroy();
	}
}
