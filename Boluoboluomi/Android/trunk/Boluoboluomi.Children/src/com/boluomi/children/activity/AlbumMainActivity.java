package com.boluomi.children.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boluomi.children.R;
import com.boluomi.children.adapter.GrowUpMainListAdapter;
import com.boluomi.children.adapter.OtherGrowUpListAdapter;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.JsonData;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.boluomi.children.data.UpdateType;
import com.boluomi.children.data.UserBeanInfo;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.model.GrowUpImgInfo;
import com.boluomi.children.model.GrowUpInfo;
import com.boluomi.children.model.SystemUpdateInfo;
import com.boluomi.children.model.UserPhotoInfo;
import com.boluomi.children.network.NetworkCheck;
import com.boluomi.children.util.CommonUtils;
import com.boluomi.children.util.MessageBox;
import com.qingfengweb.imagehandle.PicHandler;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumMainActivity extends BaseActivity implements OnClickListener{
//	private ImageView imageBtn;// 相框图片
	private Button backBtn;// 顶部返回按钮
//	private TextView userName;// 用户号
	private Bitmap bitmap;// 我的相册主页图片封面
	private ProgressDialog progressdialog;
	private boolean runnable_tag = true;
	private ImageButton takePhoto;
	private final String IMAGE_TYPE = "image/*";
	private DBHelper db = null;
	private List<Map<String,Object>> list = null;//数据集合对象
	private List<Map<String,Object>> otherList = null;//数据集合对象
	private ListView listView;
	private Button myGrowUpBtn,otherGrowupBtn;
	
	GrowUpMainListAdapter adapter = null;
	OtherGrowUpListAdapter otherAdapter = null;
	LinearLayout listViewLayout;
	ListView otherListView;
	ImageView photoLine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_mygrowupmain);
		findview();
		initData();// 初始化数据
		
	}
	
	/**
	 * 获取本地数据的我的成长备忘录的数据
	 */
	private void getLocalMyGrowupData(){
		String sql = "select * from "+GrowUpInfo.tablename+" where userid="+UserBeanInfo.getInstant().getUserid();
		list = db.selectRow(sql, null);
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			String id = map.get("id").toString();
			String sql1 = "select * from "+GrowUpImgInfo.tablename+" where growupid='"+id+"'";
			List<Map<String,Object>> imgList = db.selectRow(sql1, null);
			if(imgList!=null && imgList.size()>0){
				String imgPath = imgList.get(0).get("imglocalurl").toString();
				Bitmap bm = PicHandler.getDrawable(imgPath, 1680, 1980);
				if(bm!=null){
					bm = PicHandler.cutImg(bm);
				}
				list.get(i).put("bitmap", bm);
				list.get(i).put("imgpath", imgPath);
			}
		}
		notifyAdapter();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			getLocalMyGrowupData();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}/*else if(v == imageBtn){//相册图片
			MyApplication.getInstant().setAlbumid("");
			Intent intent = new Intent(AlbumMainActivity.this,
					PhotoListActivity.class);
			startActivity(intent);
		}*/else if(v == takePhoto){//打开获取图片菜单
//			popupWindwShowing();
			Intent intent = new Intent(this,GrowUpPhotoSendActivity.class);
			startActivityForResult(intent,1);
		}else if(v.getId() == R.id.closeWindowBtn){//关闭菜单按钮
			dismiss();
		}else if(v == myGrowUpBtn){//我的成长备忘录
			otherGrowupBtn.setBackgroundColor(Color.TRANSPARENT);
			myGrowUpBtn.setBackgroundResource(R.drawable.babe_chengzhang_btn);
			otherGrowupBtn.setTextColor(Color.BLACK);
			myGrowUpBtn.setTextColor(Color.WHITE);
			
			notifyAdapter();
		}else if(v == otherGrowupBtn){//其他人的成长备忘录
			myGrowUpBtn.setBackgroundColor(Color.TRANSPARENT);
			otherGrowupBtn.setBackgroundResource(R.drawable.babe_chengzhang_btn);
			otherGrowupBtn.setTextColor(Color.WHITE);
			myGrowUpBtn.setTextColor(Color.BLACK);
			notifyOtherAdapter();
		}
		super.onClick(v);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
//		for(int i=0;i<list.size();i++){
//			Map<String,Object> map = new HashMap<String, Object>();
//			map.put("imgpath", list.get(i).get("imgpath"));
//			map.put("imageid", "1");
//			list1.add(map);
//		}
		Map<String,Object> map = list.get(arg2);
		String id = map.get("id").toString();
		String growupName = map.get("nicheng").toString()+"   "+map.get("age").toString()+"岁";
		String sql1 = "select * from "+GrowUpImgInfo.tablename+" where growupid='"+id+"'";
		List<Map<String,Object>> imgList = db.selectRow(sql1, null);
		Intent intent = new Intent(this,GrowUpPhotoGVActivity.class);
		intent.putExtra("photoList", (Serializable)imgList);
		intent.putExtra("growupName", growupName);
		intent.putExtra("index", arg2);
		AlbumMainActivity.this.startActivity(intent);
		super.onItemClick(arg0, arg1, arg2, arg3);
	}
	
	private void findview(){
//		imageBtn = (ImageView) findViewById(R.id.imageBtn);
		backBtn = (Button) findViewById(R.id.backBtn);
		listView = (ListView) findViewById(R.id.listView);
		otherListView = (ListView) findViewById(R.id.otherListView);
		myGrowUpBtn = (Button) findViewById(R.id.myGrowUpBtn);
		otherGrowupBtn = (Button) findViewById(R.id.otherGrowUpBtn);
		myGrowUpBtn.setOnClickListener(this);
		otherGrowupBtn.setOnClickListener(this);
//		userName = (TextView) findViewById(R.id.userName);
//		userName.setText(UserBeanInfo.getInstant().getUserName());
		takePhoto = (ImageButton) findViewById(R.id.takePhoto);
		backBtn.setOnClickListener(this);
//		imageBtn.setOnClickListener(this);
		takePhoto.setOnClickListener(this);
		
		listView.setOnItemClickListener(this);
		listViewLayout = (LinearLayout) findViewById(R.id.listViewLayout);
		photoLine = (ImageView) findViewById(R.id.photoLine);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	

	/***
	 * author Ring 初始化数据
	 * 
	 */
	public void initData() {
		db = DBHelper.getInstance(this);
		list = new ArrayList<Map<String,Object>>();
		otherList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<10;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("id", "1");
			map.put("zan", i);
			otherList.add(map);
		}
		getLocalMyGrowupData();
	}
	private void notifyAdapter(){
		adapter = new GrowUpMainListAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setVisibility(View.VISIBLE);
		photoLine.setVisibility(View.VISIBLE);
		takePhoto.setVisibility(View.VISIBLE);
//		listView.setDivider(new ColorDrawable(Color.parseColor("#f5f5f5")));
//		listView.setDividerHeight(15);
//		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		param.setMargins(20, 15, 20, 15);
//		listViewLayout.setLayoutParams(param);
	}
	/**
	 * 其他人的成长备忘录列表刷新
	 */
	@SuppressWarnings("deprecation")
	private void notifyOtherAdapter(){
		otherAdapter = new OtherGrowUpListAdapter(this, otherList);
		otherListView.setAdapter(otherAdapter);
		listView.setVisibility(View.GONE);
		takePhoto.setVisibility(View.GONE);
		photoLine.setVisibility(View.GONE);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	
	
	@Override
	protected void onStop() {
//		if(downloaddata!=null){
//			downloaddata.overReponse();
//		}
		super.onStop();
	}
	@Override
	protected void onResume() {
//		new Thread(systemUpdateRunnable).start();
		super.onResume();
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
						db.update(SystemUpdateInfo.TableName, values, "type=? and userid=? and storeid=?", 
								new String[]{UpdateType.ServerTime.getValue(),UserBeanInfo.getInstant().getUserid(),UserBeanInfo.getInstant().getCurrentStoreId()});
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
						AlbumMainActivity.this);
				break;
			case 2:// 打开进度条
				progressdialog = new ProgressDialog(AlbumMainActivity.this);
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
			case 6:
				handler.sendEmptyMessage(3);
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
			String sql = "select *from "+UserPhotoInfo.TableName+" where username="+UserBeanInfo.getInstant().getUserName();
			List<Map<String,Object>> oldPhotoList = db.selectRow(sql, null);
			if(oldPhotoList.size()>0){//数据库如果有该用户的照片存在。
				String firstPhoto = oldPhotoList.get(0).get("imageid").toString();//得到该用户的第一张照片用户显示在相册的封面上
				Message msg = new Message();
				msg.obj = firstPhoto;
				msg.what = 5;
				handler.sendMessage(msg);
			}
				//检查系统更新机制表  看该用户的相册是否有更新
				String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
						+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.UserAlbum.getValue();
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
		if (NetworkCheck.IsHaveInternet(AlbumMainActivity.this)) {//判断网络状态
			handler.sendEmptyMessage(2);//开启进度条
			String msgStr = RequestServerFromHttp.getUserPhoto(UserBeanInfo.getInstant().getUserName(), UserBeanInfo.getInstant().getPassword(), localTime);
			if(msgStr.equals("404")){
				
			}else if(JsonData.isNoData(msgStr)){
				
			}else if(msgStr.startsWith("[") && msgStr.length()>3){
				String sql = "select *from "+UserPhotoInfo.TableName+" where username="+UserBeanInfo.getInstant().getUserName();
				JsonData.jsonUserPhotos(msgStr, db.open(),UserBeanInfo.getInstant().getUserName());
				List<Map<String,Object>> list = db.selectRow(sql, null);
				String firstPhoto = list.get(0).get("imageid").toString();//得到该用户的第一张照片用户显示在相册的封面上
				System.out.println(firstPhoto);
				
				//此时对该用户的照片id已经更新完毕，把更新机制中的历史时间更新成为最新更新时间
				ContentValues values = new ContentValues();
				values.put("localtime", updateTime);
				db.update(SystemUpdateInfo.TableName, values, "storeid=? and type =? and userid=?", 
						new String[]{UserBeanInfo.getInstant().getCurrentStoreId(),UpdateType.UserAlbum.getValue(),UserBeanInfo.getInstant().getUserid()});
				Message msg = new Message();
				msg.obj = firstPhoto;
				msg.what = 5;
				handler.sendMessage(msg);
			}else{
				handler.sendEmptyMessage(6);
			}
		}else{//没有网络
			
		}
	}
}
