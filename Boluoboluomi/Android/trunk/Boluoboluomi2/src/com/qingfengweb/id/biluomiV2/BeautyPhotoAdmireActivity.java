package com.qingfengweb.id.biluomiV2;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qingfengweb.adapter.BeautyPhotoAdapter;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.model.PictureThemes;
import com.qingfengweb.model.SystemUpdateInfo;
import com.qingfengweb.model.UserPhotoInfo;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.CommonUtils;
import com.qingfengweb.util.FileTools;
import com.qingfengweb.util.MD5;
import com.qingfengweb.util.MessageBox;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BeautyPhotoAdmireActivity extends BaseActivity  implements ListView.OnScrollListener{
	private ListView helpList;
	private Button backBtn;
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private String albumid = "";// 相册id;
	private ProgressDialog progressdialog;
	public static  boolean mBusy = false;
	private UploadData uploaddata = null;
	private boolean runnable_tag = true;
	public final String SDPATH = Environment.getExternalStorageDirectory()
			+ "";
	public DBHelper db = null;
	
	private TextView refreshDataTv;
	private LinearLayout refreshLayout;
	private TextView noDataTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_beautyphoto);
		findView();// 初始化控件
		db = DBHelper.getInstance(this);
		noDataTv = (TextView) findViewById(R.id.noDataTv);
		noDataTv.setOnClickListener(this);
		initData();
	}
	private void initData(){
		String sql = "select *from "+PictureThemes.TableName+" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId();
		list = db.selectRow(sql, null);
		if(list.size()>0){//数据库如果有该用户的照片存在。
			handler.sendEmptyMessage(5);
		}
		new Thread(systemUpdateRunnable).start();//开启更新机制
	}
	/**
	 * 系统更新机制线程
	 */
	Runnable systemUpdateRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
					+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.ServerTime.getValue();
			List<Map<String,Object>> systemList = db.selectRow(sqlTime, null);
			String systemTimeStr = "";
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
			}
			System.out.println("本次更新时间======"+systemTimeStr);
			if(UserBeanInfo.getInstant().getCurrentStoreId()==null || UserBeanInfo.getInstant().getCurrentStoreId().equals("")){//如果没有门店ID则不进行对服务器的访问
				return;
			}
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.systemUpdate(UserBeanInfo.getInstant().getCurrentStoreId(), localTimeStr);//请求服务器获取更新内容
					if(msgStr.equals("404")){//访问服务器失败
						System.out.println("本次系统更新接口访问服务器失败");
					}else if(JsonData.isNoData(msgStr)){//参数错误或者无数据
						System.out.println("本次系统更新接口返回无数据");
					}else{//请求成功并且有有效数据
						JsonData.jsonUpdateTimeData(msgStr, db.open());//解析数据并将数据保存到数据库
						ContentValues values = new ContentValues();
						values.put("localtime", systemTimeStr);
						db.update(SystemUpdateInfo.TableName, values, "type=?", new String[]{UpdateType.ServerTime.getValue()});
//						String sql = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"'";
//						systemList = dbHelper.selectRow(sql, null);
//						System.out.println(systemList.size()+"");
//						handler.sendEmptyMessage(0);//处理UI
					}
				}
				
			handler.sendEmptyMessage(0);
		}
	};
	/***
	 * author by Ring 处理界面跳转，从加载页跳转到主界面
	 * 
	 */

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if(!runnable_tag)
				return;
			switch (msg.what) {
			case 0:
				new Thread(getPhotoRunnable).start();
				break;
			case 1:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.error_net),
						BeautyPhotoAdmireActivity.this);
				break;
			case 2:// 打开进度条
				progressdialog = new ProgressDialog(BeautyPhotoAdmireActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_loading));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = false;
//						if (downloaddata != null) {
//							downloaddata.overReponse();
//						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 3:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 4:// 初始化界面
				initData();
				break;
			case 5://刷新布局
				notifyList();
				if(list==null|| list.size()==0){
					refreshLayout.setVisibility(View.GONE);
					noDataTv.setVisibility(View.VISIBLE);
					noDataTv.setText("暂无数据！");
					helpList.setVisibility(View.GONE);
				}else{
					refreshLayout.setVisibility(View.GONE);
					noDataTv.setVisibility(View.GONE);
					helpList.setVisibility(View.VISIBLE);
				}
				break;
			}
		};
	};

	/***
	 * @author 刘星星
	 * @createDate 2013、9、18
	 * 获取照片id线程
	 */
	private Runnable getPhotoRunnable = new Runnable() {

		@Override
		public void run() {
				//检查系统更新机制表  看该用户的相册是否有更新
				String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
						+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.StoreAlbum.getValue();
				List<Map<String,Object>> systemList = db.selectRow(sqlTime, null);
				String systemTimeStr = "";//最新更新时间
				String localTimeStr = "";//历史更新数据时间
				if(systemList!=null && systemList.size()>0){
					Map<String,Object> map = systemList.get(0);
					systemTimeStr = map.get("updatetime").toString();
					localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
					if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
						getPhoto(systemTimeStr,localTimeStr);//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					}
				}
		}
	};
	/**
	 * 访问服务器获取照片的ID 
	 * @param updateTime 最新更新时间
	 * @param localTime 上一次更新的时间
	 */
	private void getPhoto(String updateTime,String localTime){
		if (NetworkCheck.IsHaveInternet(BeautyPhotoAdmireActivity.this)) {//判断网络状态
			String msgStr = RequestServerFromHttp.getBeautyPhotoThemes(UserBeanInfo.getInstant().getCurrentStoreId(), localTime);
			if(msgStr.equals("404")){
				
			}else if(JsonData.isNoData(msgStr)){//访问服务器失败
				handler.sendEmptyMessage(5);
			}else if(msgStr.startsWith("[") && msgStr.length()>3){
				String sql = "select *from "+PictureThemes.TableName+" where storeid="+UserBeanInfo.getInstant().getCurrentStoreId();
				JsonData.jsonBeautyPhotoThemes(msgStr, db.open(),UserBeanInfo.getInstant().getCurrentStoreId());
				list = db.selectRow(sql, null);
				
				//此时对该用户的照片id已经更新完毕，把更新机制中的历史时间更新成为最新更新时间
				ContentValues values = new ContentValues();
				values.put("localtime", updateTime);
				db.update(SystemUpdateInfo.TableName, values, "storeid=? and type =?", new String[]{UserBeanInfo.getInstant().getCurrentStoreId(),UpdateType.StoreAlbum.getValue()});
				handler.sendEmptyMessage(5);
			}
		}else{//没有网络
			
		}
	}
	/**
	 * 初始化控件
	 */
	private void findView() {
		helpList = (ListView) findViewById(R.id.beautyPhotoList);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		refreshDataTv = (TextView) findViewById(R.id.refreshDataTv);
		refreshLayout = (LinearLayout) findViewById(R.id.refreshLayout);
//		helpList.setOnScrollListener(this);
	}

	/**

	/**
	 * 刷新列表布局
	 */
	private void notifyList() {
//		getListData1();
		BeautyPhotoAdapter adapter = new BeautyPhotoAdapter(this, list);
		helpList.setAdapter(adapter);
		helpList.setCacheColorHint(0);
		helpList.setOnItemClickListener(new listItemClickListener());
	}

	@Override
	protected void onDestroy() {
//		if (list != null) {
//			for (int i = 0; i < list.size(); i++) {
//				if (list.get(i).get("image") != null) {
//					((Bitmap) list.get(i).get("image")).recycle();
//				}
//			}
//			list.clear();
//			list = null;
//		}
		super.onDestroy();
	}

	/**
	 * 列表控件的Item点击事件监听器
	 * 
	 * @author qingfeng
	 * 
	 */
	class listItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(BeautyPhotoAdmireActivity.this,DetailBeautyActivity.class);
			intent.putExtra("theme", (Serializable)list.get(position));
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}

	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
		}else if(v == noDataTv){
			noDataTv.setText("正在刷新数据...");
			handler.sendEmptyMessage(0);
		}
		super.onClick(v);
	}
	


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			Intent i = new Intent(this, MainActivity.class);
//			startActivity(i);
			finish();
		}
		return false;
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

}
