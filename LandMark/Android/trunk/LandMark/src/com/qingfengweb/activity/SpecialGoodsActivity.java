package com.qingfengweb.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CacheManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.adapter.CuXiaoAdapter;
import com.qingfengweb.adapter.SpecialGoodsAdapter;
import com.qingfengweb.constant.DatabaseSql;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.database.DataAnalyzing;
import com.qingfengweb.database.MyAppLication;
import com.qingfengweb.network.GetData;


/**
 * Created by 鍒樻槦鏄�on 13-5-23.
 * 缇庨閫熼�绫�
 */
@SuppressLint("NewApi")
public class SpecialGoodsActivity extends BaseActivity implements OnClickListener{
	private GridView gridView;
//	public static WebView detailWv;
	private ArrayList<HashMap<String, String>>  list = new ArrayList<HashMap<String,String>>();
	public String msgString = "";
	LinearLayout proLayout;
	TextView stateTv;
	String lastUpdateTimeString = "";
	DBHelper dbHelper;
	SQLiteDatabase database;
	public static TextView failText;
	ImageButton leftBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_specialgoods);
        findview();
        dbHelper = new DBHelper(this);
        new Thread(getDbData).start();//更新数据
    }
    	
    Runnable getDbData = new Runnable(){
		@Override
		public void run() {
		      list = MyAppLication.getInstant().getShopList();
		      handler.sendEmptyMessage(0);//刷新列表
		      handler.sendEmptyMessage(3);//开始更新数据
		}
	};
	Runnable getListDataRunnable  = new Runnable() {
			
			@Override
			public void run() {
				ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
		        	proFlag = true;
		        	handler.sendEmptyMessage(2);//网络未连接
		        }else {
		        	msgString = GetData.getListData("special"/*, lastUpdateTimeString*/);
					if (msgString.length()>3) {
						proFlag = true;
						getData();
					}else{
						proFlag = true;
						handler.sendEmptyMessage(1);//访问服务器失败
					}				
		        }
				
			}
		};
		private boolean proFlag = false;//false代表显示    不隐藏
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				notiFyListView();
				if(list.size()!=0){
					failText.setVisibility(View.GONE);
				}
			}else if (msg.what == 1) {
//				Toast.makeText(SpecialGoodsActivity.this, "获取数据失败，请重试！", 3000).show();
//				showDialog();
				if(list==null || list.size()==0){
					failText.setVisibility(View.VISIBLE);
				}else{
					failText.setVisibility(View.GONE);
					notiFyListView();
				}
				proLayout.setVisibility(View.GONE);
			}else if (msg.what == 2) {
//				dialogWronFun("未检测到网络，请检查网络连接！", SpecialGoodsActivity.this);
				if(list==null || list.size()==0){
					failText.setVisibility(View.VISIBLE);
				}else{
					failText.setVisibility(View.GONE);
					notiFyListView();
				}
				proLayout.setVisibility(View.GONE);
			}else if(msg.what == 3){//开始后台更新数据
				if(list.size()==0){
					proLayout.setVisibility(View.VISIBLE);
				}else{
					proLayout.setVisibility(View.GONE);
				}
				new Thread(getListDataRunnable).start();
			}else if(msg.what == 5){
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}else{
					notiFyListView();
				}
			}
			if(proFlag){
				proLayout.setVisibility(View.GONE);
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
				synchronized (writeLock) {
			        new Thread(getListDataRunnable).start();
		        }
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
	SpecialGoodsAdapter adapter = null;
	private void notiFyListView(){
		 adapter = new SpecialGoodsAdapter(this, list,database);
		gridView.setAdapter(adapter);
	}
	private void findview(){
		gridView = (GridView) findViewById(R.id.shopGv);
//		gridView.setOnItemClickListener(this);
		leftBtn = (ImageButton) findViewById(R.id.backBtn);
		leftBtn.setOnClickListener(this);
		failText = (TextView) findViewById(R.id.connectFailText);
    	failText.setOnClickListener(this);
		proLayout = (LinearLayout) findViewById(R.id.proLayout);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		detailWv = (WebView) findViewById(R.id.detailWv);
		stateTv = (TextView) findViewById(R.id.stateText);
//		detailWv.getSettings().setJavaScriptEnabled(true);
//		detailWv.getSettings().setDefaultTextEncodingName("utf-8");
//		detailWv.getSettings().setAllowFileAccess(true);
//		detailWv.getSettings().setBuiltInZoomControls(true);
//		detailWv.getSettings().setSupportZoom(true);
//		detailWv.getSettings().setBuiltInZoomControls(true);
//		detailWv.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
//		detailWv.getSettings().setUseWideViewPort(true);
//		detailWv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  
//		HomeActivity.detailWvView.setInitialScale(25);//为25%，最小缩放等级 
//		int sysVersion = Integer.parseInt(VERSION.SDK);  
//		if(sysVersion>=11){
//			detailWv.getSettings().setDisplayZoomControls(false);
//		}
//		detailWv.setWebViewClient(new WebViewClient(){
//	        	@SuppressLint("NewApi")
//				@Override
//	        	public void onReceivedSslError(WebView view,
//	        			SslErrorHandler handler, SslError error) {
//	        		
//	        		super.onReceivedSslError(view, handler, error);
//	        	}
//
//				@Override
//				public void onPageFinished(WebView view, String url) {
//					if(url.equals(""));
//					super.onPageFinished(view, url);
//				}
//	        	
//	        });
	}
	private void getData(){
		ArrayList<String > strings = DataAnalyzing.analyzingData(msgString);
		synchronized (writeLock) {
			database = dbHelper.getWritableDatabase();
			DataAnalyzing.jsonSpecialData(strings.get(1), SpecialGoodsActivity.this,database);
			getDatabaseData();
		}
	}
	/**
	 * 获取数据库数据
	 */
	public void getDatabaseData(){
		//查询出数据保存在list集合中
		database = dbHelper.getReadableDatabase();
		Cursor cursor = dbHelper.getReadableDatabase().query(DatabaseSql.SPECIALGOODS_TABLENAME_STRING,
				null, null, null, null, null, null);
		list.clear();
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
		cursor.close();
		handler.sendEmptyMessage(5);
		}
	@Override
	public void onClick(View v) {
		if(v == failText && failText.getVisibility() == View.VISIBLE){
			if(list.size()==0){
				proLayout.setVisibility(View.VISIBLE);
			}else{
				proLayout.setVisibility(View.GONE);
			}
			failText.setVisibility(View.GONE);
			new Thread(getListDataRunnable).start();
		}else if(v == leftBtn){
    		MainActivity.mHost.setCurrentTab(0);
    	}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	/*	if(keyCode == KeyEvent.KEYCODE_BACK && detailWv.canGoBack()){
			detailWv.clearCache(true);
        	detailWv.clearHistory();
        	detailWv.clearFocus();
        	detailWv.clearMatches();
        	detailWv.loadDataWithBaseURL(null, "","text/html", "utf-8",null);  
			detailWv.setVisibility(View.GONE);
			return true;
		}else */if(keyCode == KeyEvent.KEYCODE_BACK){
//			AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
//			alerBuilder.setTitle("提示！");
//			alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
//			alerBuilder.setMessage("确定要退出程序吗？");
//			alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					File file = CacheManager.getCacheFileBaseDir();
//
//					   if (file != null && file.exists() && file.isDirectory()) {
//					    for (File item : file.listFiles()) {
//					     item.delete();
//					    }
//					    file.delete();
//					   }
//					   
//					   SpecialGoodsActivity.this.deleteDatabase("webview.db");
//					   SpecialGoodsActivity.this.deleteDatabase("webviewCache.db");
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
////			if (detailWv.getVisibility() == View.VISIBLE) {
////				detailWv.setVisibility(View.GONE);
////				return true;
////			}else {
//				Dialog dialog = alerBuilder.create();
//				dialog.show();
////			}
			MainActivity.mHost.setCurrentTab(0);
		}
		return true;
	}
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
//        	dialogWronFun("未检测到网络，请检查网络连接！", SpecialGoodsActivity.this);
//        }else {
//        	detailWv.clearCache(true);
//        	detailWv.clearHistory();
//        	detailWv.clearFocus();
//        	detailWv.clearMatches();
////        	detailWv.loadDataWithBaseURL(null, "","text/html", "utf-8",null);  
//        	detailWv.setVisibility(View.VISIBLE);
//    		detailWv.loadUrl(list.get(arg2).get("special_url").toString().trim());
//    		MyAppLication.getInstant().setBackFlag(1);
//		}
//		
//	}
	
	@Override
	protected void onDestroy() {
		database.close();
		database.releaseReference();
		database.releaseMemory();
		super.onDestroy();
	}
}

