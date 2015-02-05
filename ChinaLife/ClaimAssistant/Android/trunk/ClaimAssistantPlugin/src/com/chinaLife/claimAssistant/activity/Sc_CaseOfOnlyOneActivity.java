package com.chinaLife.claimAssistant.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import com.sqlcrypt.database.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinaLife.claimAssistant.Interface.sc_ProgressBarInterface;
import com.chinaLife.claimAssistant.Interface.sc_SurveySelfInterface;
import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.adapter.sc_CaseOfOneAdapter;
import com.chinaLife.claimAssistant.bean.sc_ClaimPhotoInfo;
import com.chinaLife.claimAssistant.bean.sc_GetText;
import com.chinaLife.claimAssistant.bean.sc_LegendInfo;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_GetMsg;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_PicHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData;
import com.chinaLife.claimAssistant.thread.sc_UploadData1;
import com.chinaLife.claimAssistant.thread.sc_UploadPhotoFile;
import com.chinaLife.claimAssistant.tools.sc_BitmapCache;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Sc_CaseOfOnlyOneActivity extends Activity implements OnClickListener,
		sc_ProgressBarInterface, sc_SurveySelfInterface ,OnItemClickListener{
	private Button btn3, btn4, back;
	private TextView tv1;
	private ArrayList<HashMap<String, Object>> objectList = new ArrayList<HashMap<String, Object>>();
	// private ArrayList<HashMap<String, HashMap<String, Object>>> listData =
	// new ArrayList<HashMap<String, HashMap<String, Object>>>();
	private GridView imageListView;
	private byte[] imagedata;
	public static ProgressDialog progressdialog = null;
	private Dialog alertDialog;// 自定义的弹出框用来显示签到的两个选项
	public static int listPosition = 0;// 标识listData这个集合中的行
	// public static Bitmap PicBitmap = null;
	// private static Bitmap bitmap = null;
	public static int legendid = 1;// 图例编号
	private Button notifiCationBtn,notifiBtn;// 消息按钮
	public static sc_DBHelper database = null;
	private Timer timer = null;
	public static ArrayList<HashMap<String, Object>> myprogressbars = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Integer>> legendinfo = new ArrayList<HashMap<String, Integer>>();
	private int tag = 0;
	private int claimstate_pre;// 消息通知历史状态
	private EditText password_ET = null;
	
	private int back_activity = 0;
	public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_caseofonlyone);
		back_activity = getIntent().getIntExtra("back_activity", 0);
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("android.intent.action.PHONE_STATE");
		myIntentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(callReceiver, myIntentFilter);
		Sc_ExitApplication.getInstance().context = Sc_CaseOfOnlyOneActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_CaseOfOnlyOneActivity.this);
		initView();
		initViewFun();
		initData();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = format.format(new Date());
		Sc_MyApplication.getInstance().setNowTime(nowTime);
		mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类   
		mLocationClient.registerLocationListener( myListener );   	//注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())	{
			mLocationClient.requestLocation();
		}else 	{
			Log.d("LocSDK3", "locClient is null or not started");
		}
		System.gc();
}
public class MyLocationListener implements BDLocationListener {	
	@Override	
	public void onReceiveLocation(BDLocation location) {
		if (location == null)		
			return ;	
		Sc_MyApplication.getInstance().setLatitude((float) location.getLatitude());
		Sc_MyApplication.getInstance().setLongitude((float) location.getLongitude());
		if(location.getAddrStr()!=null){
			Sc_MyApplication.getInstance().setAddress(location.getAddrStr());
		}
		System.out.println("获取到的当前地址为：================"+location.getAddrStr());
		}
	public void onReceivePoi(BDLocation poiLocation) {	
			if (poiLocation == null){		
				return ;		
				}		
			}
		}
	TimerTask task = new TimerTask() {
		public void run() {
			sc_GetMsg.getMsg();
			handler.sendEmptyMessage(2);
		}
	};

	private void initViewFun() {
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		back.setOnClickListener(this);
		notifiCationBtn.setOnClickListener(this);
		notifiBtn.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		sc_UploadPhotoFile.isstop = true;
		Sc_MyApplication.getInstance().setUploadon(false);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(callReceiver);
		sc_UploadPhotoFile.isstop = true;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (myprogressbars != null) {
			for (int i = 0; i < myprogressbars.size(); i++) {
				ImageView imageview = (ImageView) myprogressbars.get(i).get(
						"image");
				imageview.destroyDrawingCache();
			}
		}

//		try {
			if (imagedata != null) {
				imagedata = null;
			}
			/*
			 * if (PicBitmap != null && !PicBitmap.isRecycled()) {
			 * PicBitmap.recycle(); PicBitmap = null; }
			 */
			/*
			 * if (bitmap != null && !bitmap.isRecycled()) { bitmap.recycle();
			 * bitmap = null; }
			 */

//		} catch (Exception e) {
//			if (MyApplication.opLogger != null) {
//				MyApplication.opLogger.error("自助查勘界面出错", e);
//			}
//
//		}
		mLocationClient.stop();
		System.gc();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Sc_MyApplication.getInstance().setServer_type(1);
		super.onResume();
	}

	private void initData() {
		Sc_MyApplication.getInstance().setSayNumber(0);
		if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			Sc_MyApplication.getInstance().setContext(this);
			Sc_MyApplication.getInstance().setContext2(getApplicationContext());
			sc_MyHandler.getInstance();
			database = sc_DBHelper.getInstance();
			progressdialog = new ProgressDialog(this);
			progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressdialog.setCanceledOnTouchOutside(false);
			Sc_MyApplication.getInstance().setProgressdialog(progressdialog);
			tag = getIntent().getIntExtra("tag", 0);
			if (Sc_MyApplication.getInstance().getClaimidstate() == 0) {
				objectList.clear();
				getObjectList();
				notifyAdapter();
				return;
			} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 16384) == 16384) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("自助查勘结束").setPositiveButton("我知道了", null);
				if(back_activity==0){
					callDailog.show();
				}
				objectList.clear();
				getObjectList();
				notifyAdapter();
				btn3.setBackgroundResource(R.drawable.sc_aj_btn3_gray);
				btn3.setClickable(false);
			} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 8) == 8) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示")
						.setMessage(
								"尊敬的客户：您已完成自助查勘，可驶离现场。稍后，我司工作人员将及时与您联系，为您提供后续理赔指引。如有疑问，欢迎拨打我司客服热线95519垂询。")
						.setPositiveButton("我知道了", null);
				if(back_activity==0){
					callDailog.show();
				}
				objectList.clear();
				getObjectList();
				notifyAdapter();
				btn3.setBackgroundResource(R.drawable.sc_aj_btn3_gray);
				btn3.setClickable(false);
			} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 4) == 4) {
				objectList.clear();
				getObjectList();
				notifyAdapter();
			} else {
				objectList.clear();
				getObjectList();
				notifyAdapter();
			}
			return;
		}

		if (!sc_NetworkCheck.IsHaveInternet(this)
				&& Sc_MyApplication.getInstance().getSelfHelpFlag() != 1) {
			btn3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder callDailog = new AlertDialog.Builder(
							Sc_CaseOfOnlyOneActivity.this);
					callDailog.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle("提示").setMessage("您手机未能联网，无法进行提交！")
							.setPositiveButton("确定", null).show();
				}
			});
		}

		if (!sc_GetText.isActive(Sc_MyApplication.getInstance().getClaimidstate(),
				Sc_MyApplication.getInstance().getCaseidstate())) {
			btn3.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			btn3.setClickable(false);
		}
		tag = getIntent().getIntExtra("tag", 0);
		if (tag == 2) {
			claimstate_pre = getIntent().getIntExtra("claimstate", 0);
			if (claimstate_pre > Sc_MyApplication.getInstance().getClaimidstate()) {
//				MyApplication.getInstance().setClaimidstate(claimstate_pre);
			}
		}
		Sc_MyApplication.getInstance().setContext(this);
		Sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(false);
		Sc_MyApplication.getInstance().setProgressdialog(progressdialog);
		if (Sc_MyApplication.getInstance().getCasedescription_tag() == 1) {
			tv1.setText("离线拍照");
		} else {
			tv1.setText("自助查勘");
		}

		if (Sc_MyApplication.getInstance().getCasedescription_tag() == 1) {
			hurryCase();
			progressdialog.setMessage("正在获取系统数据，请稍等！");
			if(back_activity==0){
				progressdialog.show();
				new Thread(runnable4).start();
			}
			return;
		}
		progressdialog.setMessage("正在获取系统数据，请稍等！");
		timer = new Timer();
		timer.schedule(task, 0, 5 * 1000);
		if(back_activity==0){
			progressdialog.show();
			new Thread(runnable1).start();
		}else{
			handler.sendEmptyMessage(1);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				accessView();
				progressdialog.dismiss();
				break;
			case 2:
				int number  = Sc_MyApplication.getInstance().getSayNumber();
				if(number>0){
//					Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.xx_btn_dot);
//					int a = (int)getResources().getDimension(R.dimen.xx_text_toleft);
//					int b = (int)getResources().getDimension(R.dimen.xx_text_totop);
//					int size = (int)getResources().getDimension(R.dimen.xx_text_size);
//					notifiCationBtn.setImageBitmap(PicHandler.doodlexx(image,null, MyApplication.getInstance().getSayNumber(), size,a,b));
//					notifiCationBtn.setBackgroundResource(R.drawable.xx_btn_dot);
//					notifiCationBtn.setText(MyApplication.getInstance().getSayNumber()+"");
//					if(number>9){
//						notifiCationBtn.setPadding(0, 4, 7, 0);
//					}else{
//						notifiCationBtn.setPadding(0, 4, 12, 0);
//					}
					notifiCationBtn.setVisibility(View.VISIBLE);
					notifiCationBtn.setText(Sc_MyApplication.getInstance().getSayNumber()+"");
				}else{
//					notifiCationBtn.setBackgroundResource(R.drawable.xx_btn);
					notifiCationBtn.setVisibility(View.INVISIBLE);
//					notifiCationBtn.setText(null);
				}
				break;
			case 3:
				progressdialog.dismiss();
				AlertDialog.Builder callDailog1 = new AlertDialog.Builder(
						Sc_CaseOfOnlyOneActivity.this);
				callDailog1
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("请在无网络信号时使用该功能，按照图例拍摄事故现场照片，您拍摄的照片将自动保存在本地相册中，待网络畅通后请通过【案件处理】功能导入并上传本次拍摄照片进行理赔。")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										hurryCase();
									}
								});
				callDailog1.show();
				break;
			case 4:
				progressdialog.dismiss();
				AlertDialog.Builder callDailog = new AlertDialog.Builder(
						Sc_CaseOfOnlyOneActivity.this);
				callDailog
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle("提示")
						.setMessage("照片已保存至相册，以便在[赔案处理]功能中导入使用或作为索赔证明材料使用。")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										hurryCase();
									}
								});
				callDailog.show();
			}
		}
	};

	private void hurryCase() {
		objectList.clear();
		getObjectList();
		notifyAdapter();
		btn3.setText("保存");
		notifiCationBtn.setVisibility(View.GONE);
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Sc_MyApplication.getInstance().getCasedescription_tag() == 1) {
					progressdialog.setMessage("正在保存图片，请稍等！");
					progressdialog.show();
					new Thread(runnable5).start();
				}
			}
		});
		btn3.setBackgroundResource(R.drawable.sc_aj_btn2);
		btn3.setClickable(true);
	}

	private Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			getLegendids();
			String ids = checkLegends(1);
			if(ids!=null
					&&!ids.equals("")){
				getLegendinfo(ids);
			}
			getClaimImageList();
			handler.sendEmptyMessage(1);
		}
	};
	private Runnable runnable4 = new Runnable() {

		@Override
		public void run() {
			File dir1 = new File(Sc_MyApplication.getInstance().getURL_PIC1());
			if (dir1.exists()) {
				dir1.delete();
			}
			handler.sendEmptyMessage(3);
		}
	};
	private Runnable runnable5 = new Runnable() {

		@Override
		public void run() {
			try {
				File dir = new File(Sc_MyApplication.getInstance().getURL_PIC());
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File dir1 = new File(Sc_MyApplication.getInstance().getURL_PIC1());
				File[] tempfiles = dir1.listFiles();
				int i;
				for (i = 0; i < tempfiles.length; i++) {
					if (tempfiles[i].getName().endsWith(".jpg")) {
						tempfiles[i].renameTo(new File(dir, tempfiles[i]
								.getName()));
						tempfiles[i].delete();// 删除
					}
				}
				if (dir1.exists()) {
					dir1.delete();
				}
				handler.sendEmptyMessage(4);
			} catch (Exception e) {
				handler.sendEmptyMessage(3);
			}
		}
	};

	public void accessView() {
		if ((Sc_MyApplication.getInstance().getClaimidstate() & 16) == 16) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示")
					.setMessage("尊敬的客户：您的部分现场照片未审核通过，请您将标记为“重拍”的照片重新拍摄上传。")
					.setPositiveButton("我知道了", null);
			if(back_activity==0){
				try {
					callDailog.show();
				} catch (Exception e) {
				}
			}
		} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 8) == 8) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示")
					.setMessage(
							"尊敬的客户：您已完成自助查勘，可驶离现场。稍后，我司工作人员将及时与您联系，为您提供后续理赔指引。如有疑问，欢迎拨打我司客服热线95519垂询。")
					.setPositiveButton("我知道了", null);
			if(back_activity==0){
				try {
					callDailog.show();
				} catch (Exception e) {
				}
			}
			btn3.setBackgroundResource(R.drawable.sc_aj_btn3_gray);
			btn3.setClickable(false);
		} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 4) == 4) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage("尊敬的客户：您已成功上传照片，请耐心等待我司审核。")
					.setPositiveButton("我知道了", null);
			if(back_activity==0){
				try {
					callDailog.show();
				} catch (Exception e) {
				}
			}
			btn3.setBackgroundResource(R.drawable.sc_aj_btn3_gray);
			btn3.setClickable(false);
		}
		notifyView();
	}

	/**
	 * 所有图片都上传完毕后，点击上传按钮发送结束标记
	 */
	public void SurveySelf() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "over"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("type", "1"));
			params.add(new BasicNameValuePair("caseid", Sc_MyApplication
					.getInstance().getCaseid()));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
					params, 9);
			uploaddata.Post();
		}
	}

	/**
	 * 获取图例信息
	 */
	public void getLegendids() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getImageLayout"));
			params.add(new BasicNameValuePair("type", "1"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			sc_UploadData1 uploaddata = new sc_UploadData1(Sc_MyApplication.URL + "claim",
					params);
			uploaddata.Post();
			String reponse = uploaddata.getReponse();
			if(!reponse.contains("-")){
				parseJosnLegendids(reponse);
			}

		}
	}

	/**
	 * 解析获取的图例信息
	 */
	public void parseJosnLegendids(String response) {
//		if (tag == 2) {
			// AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			// callDailog.setIcon(android.R.drawable.ic_dialog_info)
			// .setTitle("提示").setMessage("未能够获取服务器信息，请检查网络稍后重试")
			// .setPositiveButton("我知道了", null).show();
//			return;
//		}
//		database.execSql("update claiminfo set legends ='" + response
//				+ "' where claimid='"
//				+ MyApplication.getInstance().getClaimid() + "'");
		
		
		ContentValues values = new ContentValues();
		values.put("legends", response);
		
		database.update("claiminfo", values, "claimid=?", new String[]{Sc_MyApplication.getInstance().getClaimid()});
	}

	/**
	 * 将获取的单证或查勘的图片解析出来
	 * 
	 * @param str
	 */
	public ArrayList<HashMap<String, Integer>> changeCertificates(String str) {
		ArrayList<HashMap<String, Integer>> leg = new ArrayList<HashMap<String, Integer>>();
		String[] legendid1;
		String[] legendid2;
		legendid1 = str.split(";");
		for (int i = 0; i < legendid1.length; i++) {
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			legendid2 = legendid1[i].split(",");
			map.put("legendid", Integer.parseInt(legendid2[0]));
			map.put("paper", Integer.parseInt(legendid2[1]));
			map.put("isupload", Integer.parseInt(legendid2[2]));
			leg.add(map);
		}
		return leg;
	}

	public void notifyAdapter() {
		imageListView.setAdapter(new sc_CaseOfOneAdapter(this, objectList));
		imageListView.setCacheColorHint(0);
	}

	/**
	 * 根据理赔编号获取图片的信息列表
	 */

	public void getClaimImageList() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getFullImages"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
					params, 8);
			uploaddata.Post();
		}
	}

	/**
	 * 根据理赔编号获取图片的信息列表,给图片打上合格和重拍字样
	 */

	public void parseJsonClaimImage(int tag, String response) {
		if (tag == 2) {
			return;
		}
		Type listType = new TypeToken<LinkedList<sc_ClaimPhotoInfo>>() {
		}.getType();
		Gson gson = new Gson();
		ContentValues values = new ContentValues();
		LinkedList<sc_ClaimPhotoInfo> claimPhotoinfos = null;
		sc_ClaimPhotoInfo claimphotoinfo = null;
		claimPhotoinfos = gson.fromJson(response, listType);

		for (Iterator<sc_ClaimPhotoInfo> iterator = claimPhotoinfos.iterator(); iterator
				.hasNext();) {
			claimphotoinfo = (sc_ClaimPhotoInfo) iterator.next();
			values.put("claimid", Sc_MyApplication.getInstance().getClaimid());
			values.put("legendid", claimphotoinfo.getLegendid());
			values.put("photoid", claimphotoinfo.getPhotoid());
			values.put("filename", claimphotoinfo.getSavepath());
			values.put("review_result", claimphotoinfo.getStatus());
			values.put("type", claimphotoinfo.getType());
			values.put("review_reason", claimphotoinfo.getReviewreason());
			List<Map<String, Object>> selectresult = database.selectRow(
					"select * from claimphotoinfo where legendid = '"
							+ claimphotoinfo.getLegendid() + "' and claimid='"
							+ Sc_MyApplication.getInstance().getClaimid() + "' and type="+claimphotoinfo.getType(),
					null);
			if (selectresult.size() <= 0) {
				database.insert("claimphotoinfo", values);
			} else {
				if(selectresult.get(0).get("review_result")!=null
						&&(!selectresult.get(0).get("review_result").equals("-2")))
				database.update("claimphotoinfo", values, "claimid = ? and legendid=? and type=? ",
						new String[] { Sc_MyApplication.getInstance().getClaimid(), claimphotoinfo.getLegendid(),claimphotoinfo.getType()+""});
			}
			values.clear();

			List<Map<String, Object>> selectresult1 = database.selectRow(
					"select * from uploadinfo where legendid = '"
							+ claimphotoinfo.getLegendid() + "' and claimid='"
							+ Sc_MyApplication.getInstance().getClaimid() + "' and type ="+claimphotoinfo.getType(),
					null);
			values.put("legendid", claimphotoinfo.getLegendid());
			values.put("type", claimphotoinfo.getType());
			values.put("claimid", Sc_MyApplication.getInstance().getClaimid());
			values.put("savepath", Environment.getExternalStorageDirectory()
					+ "/fkdsfhsdk.ttt");
			if (selectresult1.size() <= 0) {
				database.insert("uploadinfo", values);
			}
			values.clear();
		}
		values = null;
	}

	/**
	 * 应急拍照的列表
	 */
	public void getObjectList1() {
		String[] str = "1,2,3,4".split(",");
		HashMap<String, Object> listmap = null;
		objectList.clear();
		myprogressbars.clear();
		for (int i = 0; i < str.length; i++) {
			listmap = new HashMap<String, Object>();
			listmap.put("savepath",sc_LegendInfo.getLegendImage(str[i]));
			listmap.put("ischange", 0);
			listmap.put("checked", 2);// 可点
			listmap.put("name",sc_LegendInfo.getLegendText(str[i]));
			listmap.put("state", 0);
			listmap.put("photofile", Sc_MyApplication.getInstance().getFile());
			listmap.put("legendid", Integer.parseInt(str[i]));
			listmap.put("reviewreason", "");
			objectList.add(listmap);

		}
	}

	public void getObjectList() {
		String claimid = "";
		int claimidstate = 1;
		legendinfo.clear();
		String legendmode = "1,1,1;2,1,1;3,1,1;4,1,1;5,1,1;6,1,0;7,1,0";
		if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			legendmode = "1,1,1;2,1,1;3,1,1;4,1,1;5,1,1;6,1,0;7,1,0";
		}
		if (Sc_MyApplication.getInstance().getCasedescription_tag() == 1) {
			legendmode = "1,1,0;2,1,0;3,1,0;4,1,0";
			claimid = "hurry0123456789";
		} else {
			claimid = Sc_MyApplication.getInstance().getClaimid();
			claimidstate = Sc_MyApplication.getInstance().getClaimidstate();
		}

		String sql1 = "select legends from claiminfo where claimid = '"
				+ claimid + "'";
		List<Map<String, Object>> selectresult1 = database
				.selectRow(sql1, null);

		if (selectresult1.size() > 0&&selectresult1.get(0).get("legends")!=null) {
			if (selectresult1.get(0).get("legends").toString().contains(",")) {
				legendinfo = changeCertificates(selectresult1.get(0)
						.get("legends").toString());
			} else {
				legendinfo = changeCertificates(legendmode);
			}
		} else {
			legendinfo = changeCertificates(legendmode);
		}
		if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1
				||Sc_MyApplication.getInstance().getCasedescription_tag()==1) {
			legendinfo = changeCertificates(legendmode);
		}
		
		HashMap<String, Object> listmap = null;
		if(objectList==null){
			objectList = new ArrayList<HashMap<String,Object>>();
		}
		objectList.clear();
		myprogressbars.clear();
		for (int i = 0; i < legendinfo.size(); i++) {
			listmap = new HashMap<String, Object>();
			String sql = "select uinfo.savepath"
					+ ",uinfo.legendid,uinfo.claimid,cinfo.type"
					+ ",cinfo.review_result,cinfo.review_reason,cinfo.filename,cinfo.photoid "
					+ "from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type=cinfo.type where uinfo.claimid = '"
					+ claimid + "' and uinfo.legendid = '"
					+ legendinfo.get(i).get("legendid").toString()
					+ "' and uinfo.type=1";
			List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
					.selectRow(sql, null);
			if (selectresult.size() > 0
					&& selectresult.get(0).get("savepath") != null
					&& !selectresult.get(0).get("savepath").toString().trim()
							.equals("")) {
				File file = new File(selectresult.get(0).get("savepath")
						.toString());
				Sc_MyApplication.getInstance().setFile(file);
				if (file.exists()) {
					listmap.put("savepath", file.getAbsolutePath());
					listmap.put("ischange", 1);
					listmap.put("state", 2);
				} else if ((claimidstate & 16) == 16 || (claimidstate & 8) == 8
						|| (claimidstate & 4) == 4) {
					listmap.put("savepath", R.drawable.sc_icon_default_photo);
					listmap.put("ischange", 1);
					listmap.put("state", 0);
				} else {
					listmap.put("savepath", sc_LegendInfo.getLegendImage(legendinfo
							.get(i).get("legendid").toString()));
					listmap.put("ischange", 0);
					listmap.put("state", 0);
				}
			} else {
				listmap.put(
						"savepath",sc_LegendInfo.getLegendImage(legendinfo
								.get(i).get("legendid").toString()));
				listmap.put("ischange", 0);
				listmap.put("state", 0);
			}

			if ((claimidstate & 16) == 16) {
				try {
					try {
						if (selectresult.get(0).get("review_result") != null
								&& Integer.parseInt(selectresult.get(0)
										.get("review_result").toString()) == 1) {
							listmap.put("checked", 1);// 不可点
							listmap.put("ischange", 1);
							listmap.put("state", 2);
							listmap.put("pic_state", 1);
						} else if (selectresult.get(0).get("review_result") != null
								&& Integer.parseInt(selectresult.get(0)
										.get("review_result").toString()) == -1) {
							listmap.put("checked", 2);// 可点
							listmap.put("ischange", 0);
							listmap.put("state", 0);
						} else if (selectresult.get(0).get("review_result") != null
								&& Integer.parseInt(selectresult.get(0)
										.get("review_result").toString()) == -2) {
							listmap.put("checked", 2);// 可点
							listmap.put("ischange", 1);
							listmap.put("state", 2);
						} else if(selectresult.get(0).get("review_result") != null
								&& Integer.parseInt(selectresult.get(0)
										.get("review_result").toString()) == 2){
							listmap.put("checked", 2);// 可点
							listmap.put("ischange", 0);
							listmap.put("state", 2);
							listmap.put("pic_state", 2);
						} else {
							listmap.put("checked", 2);// 可点
							listmap.put("ischange", 0);
							listmap.put("state", 2);
							listmap.put("pic_state", 2);
						}
					} catch (Exception e) {
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 0);
						listmap.put("state", 0);
					}
				} catch (Exception e) {
					listmap.put("checked", 2);// 可点
					listmap.put("ischange", 0);
					listmap.put("state", 0);
				}

			} else if ((claimidstate & 8) == 8) {
				if (Integer.parseInt(listmap.get("ischange").toString()) == 1) {
					listmap.put("pic_state", 1);
				}
				listmap.put("checked", 1);// 不可点
			} else if ((claimidstate & 4) == 4) {
				if (Integer.parseInt(listmap.get("ischange").toString()) == 1) {
					listmap.put("pic_state", 3);
				}

				listmap.put("checked", 1);// 不可点
			} else {
				listmap.put("checked", 2);// 可点
			}
			try {
				listmap.put("uploadpath", selectresult.get(0).get("filename")
						.toString());
			} catch (Exception e) {
				listmap.put("uploadpath", "");
			}
			try {
				listmap.put("photoid", selectresult.get(0).get("photoid")
						.toString());
			} catch (Exception e) {
				listmap.put("photoid", "");
			}

			try {
				listmap.put("reviewreason",
						selectresult.get(0).get("review_reason").toString());
			} catch (Exception e) {
				listmap.put("reviewreason", "");
			}
			if ((claimidstate & 131072) == 131072) {
				listmap.put("checked", 1);// 不可点
			}
			if(listmap.get("pic_state")!=null){
				if(!listmap.get("savepath").toString().contains(".")
						&&legendinfo.get(i).get("isupload") == 1
						&&!listmap.get("pic_state").toString().contains("1")
						&&!listmap.get("pic_state").toString().contains("2")
						&&!listmap.get("pic_state").toString().contains("3")){
					listmap.put("pic_state", 4);
				}
			}else{
				if(!listmap.get("savepath").toString().contains(".")
						&&legendinfo.get(i).get("isupload") == 1){
					listmap.put("pic_state", 4);
				}
			}
			
			try {
				if (Integer.parseInt(listmap.get("pic_state").toString()) == 2) {
					listmap.put("state", 0);
				}
			} catch (Exception e) {
			}
			listmap.put(
					"name",sc_LegendInfo.getLegendText(legendinfo
							.get(i).get("legendid").toString()));
			listmap.put("photofile", Sc_MyApplication.getInstance().getFile());
			listmap.put(
					"legendid",
					Integer.parseInt(legendinfo.get(i).get("legendid")
							.toString()));
			objectList.add(listmap);

		}
	}

	private void notifyView() {
		objectList.clear();
		getObjectList();
		notifyAdapter();
	}

	private void initView() {
		notifiCationBtn = (Button)findViewById(R.id.notication);
		notifiBtn = (Button) findViewById(R.id.notifiBtn);
		tv1 = (TextView) findViewById(R.id.tv1);
		back = (Button) findViewById(R.id.fanhui);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		imageListView = (GridView) findViewById(R.id.listView);
		imageListView.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int state = (Integer) objectList
				.get(arg2).get("state");
		String file = objectList
				.get(arg2).get("savepath").toString();
		try {
			showQianDaoDialog(state, file);
		} catch (Exception e) {

		}
	}
	@Override
	public void onClick(View v) {
		if (v == btn3) {

			if (!sc_NetworkCheck.IsHaveInternet(this)
					&& Sc_MyApplication.getInstance().getSelfHelpFlag() != 1) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				try {
					callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("请检查网络是否连入").setPositiveButton("我知道了", null)
					.show();
				} catch (Exception e) {
				}
				
			}else {
				submitBtn();
			}
		} else if (v == back) {
			Intent i = new Intent();
			if (Sc_MyApplication.getInstance().getCasedescription_tag() == 1
					&& Sc_MyApplication.getInstance().getPhoto_tag() == 2) {
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_MainActivity.class);
				startActivity(i);
				finish();
				return;
			}
			if (tag == 2) {
				i.setClass(Sc_CaseOfOnlyOneActivity.this,
						Sc_MessageListActivity.class);
			} else if(tag == 3) {
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_CaseInfoActivity.class);
			} else{
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_CaseListActivity.class);
			}

			startActivity(i);
			finish();

		} else if (v == btn4) {

			Intent i = new Intent();
			if (Sc_MyApplication.getInstance().getCasedescription_tag() == 1
					&& Sc_MyApplication.getInstance().getPhoto_tag() == 2) {
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_MainActivity.class);
				startActivity(i);
				finish();
				return;
			}
			if (tag == 2) {
				i.setClass(Sc_CaseOfOnlyOneActivity.this,
						Sc_MessageListActivity.class);
			} else if(tag == 3) {
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_CaseInfoActivity.class);
			} else{
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_CaseListActivity.class);
			}

			startActivity(i);
			finish();
		} else if (v == notifiCationBtn) {

			Intent intent = new Intent(this, Sc_SayActivity.class);
			startActivity(intent);
		}else if (v == notifiBtn) {

			Intent intent = new Intent(this, Sc_SayActivity.class);
			startActivity(intent);
		}else if(v.getId() == R.id.passwordBtn){
			Sc_MyApplication.getInstance().setPassword(password_ET.getText().toString());
			ContentValues values = new ContentValues();
			values.put("password", password_ET.getText().toString());
			sc_DBHelper.getInstance().update("userinfo", values
					, "plate_number = ? and contact_mobile_number = ?"
					, new String[]{Sc_MyApplication.getInstance().getPlatenumber()
					,Sc_MyApplication.getInstance().getPhonenumber()});
			inputShareDialog.dismiss();
		}else if(v.getId() == R.id.passwordAmendBtn){
			inputShareDialog.dismiss();
			Intent intent = new Intent(this,Sc_AmendPasswordActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Intent i = new Intent();
			if (Sc_MyApplication.getInstance().getCasedescription_tag() == 1
					&& Sc_MyApplication.getInstance().getPhoto_tag() == 2) {
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_MainActivity.class);
				startActivity(i);
				finish();
				return false;
			}
			if (tag == 2) {
				i.setClass(Sc_CaseOfOnlyOneActivity.this,
						Sc_MessageListActivity.class);
			} else if(tag == 3) {
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_CaseInfoActivity.class);
			} else{
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseOfOnlyOneActivity.this, Sc_CaseListActivity.class);
			}
			startActivity(i);
			finish();
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return false;
		}
		return true;
	}

	public void submitBtn() {
		Sc_MyApplication.getInstance().setServer_type(1);
		int count=0;
		int count1 = 0;
		for (int i = 0; i < legendinfo.size(); i++) {
			if (Integer.parseInt(objectList.get(i).get("ischange").toString()) == 0
					&& legendinfo.get(i).get("isupload") == 1) {
				Toast.makeText(
						this,sc_LegendInfo.getLegendText(objectList
								.get(i).get("legendid").toString())
								+ "图例必须拍照上传", 2000).show();
				return;
			}
			if(Integer.parseInt(objectList.get(i).get("ischange").toString()) == 0){
				count++;
			}
			try {
				if (Integer.parseInt(objectList.get(i).get("pic_state")
						.toString()) == 1) {
					count1++;
				}
			} catch (Exception e) {
			}
		}
		if(count == legendinfo.size()-count1){
			Toast.makeText(this,"未发现可以上传的图片", 2000).show();
			return;
		}
		if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("提示")
					.setMessage("尊敬的客户：您已成功上传照片，请耐心等待我司审核。")
					.setPositiveButton("我知道了",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Sc_MyApplication
											.getInstance()
											.setClaimidstate(
													Sc_MyApplication.getInstance()
															.getClaimidstate() + 16384 + 8);
									Intent i = new Intent(
											Sc_CaseOfOnlyOneActivity.this,
											Sc_CaseListActivity.class);
									startActivity(i);
									finish();
								}
							});
			try {
				callDailog.show();
			} catch (Exception e) {
			}
			return;
		}
		ContentValues values = new ContentValues();
		values.put("status", 0);
		Sc_MyApplication.getInstance().setUploadon(true);
		database.update("uploadinfo", values,
				"claimid = ? and status = ?", new String[] {
						Sc_MyApplication.getInstance().getClaimid(), "-1" });

		String sql = "select uinfo.*,cinfo.type from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type=cinfo.type where uinfo.claimid = '"
				+ Sc_MyApplication.getInstance().getClaimid()
				+ "' and (uinfo.status =0 or uinfo.status =3) and cinfo.type = 1";
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
				.selectRow(sql, null);
		System.out.println("上传图片的数量为："+selectresult.size());
		sc_DBHelper.getInstance().close();
		progressdialog.setMessage("正在提交数据，请稍等...");
		progressdialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				return true;
			}
		});
		progressdialog.show();
		if (selectresult.size() > 0) {
			new Thread(runnable2).start();
		} else {
			new Thread(runnable3).start();
		}
	}

	private Runnable runnable2 = new Runnable() {
		@Override
		public void run() {
			sc_MyHandler.getInstance().sendEmptyMessage(-2);
		}
	};
	private Runnable runnable3 = new Runnable() {
		@Override
		public void run() {
			SurveySelf();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				// imagedata = MyApplication.getInstance().getDataByte();

				/*
				 * progressdialog.setMessage("正在加载图片，请稍候。。。");
				 * progressdialog.show(); Thread thread = new
				 * Thread(lxxRunnable); thread.start();
				 */
				Sc_MyApplication.getInstance().setLegendid(legendid);
				Message msg = new Message();
				msg.what = 0;
				lxxHandler.sendMessage(msg);
				Sc_MyApplication.switch_tag = true;
			}
		} else if (requestCode == 2) {
			if (data != null) {
				Uri imageUri = data.getData();
				ContentResolver cr = getContentResolver();
				String path = "";
				Cursor s = cr.query(imageUri, null, null, null, null);
				if (s != null) {
					int column_index = s
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// 将光标移至开头 ，这个很重要，不小心很容易引起越界
					s.moveToFirst();
					// 最后根据索引值获取图片路径
					path = s.getString(column_index);
				} else {
					path = imageUri.getPath();
				}
				InputStream imgIS;
				try {
					imgIS = cr.openInputStream(imageUri);
					try {
						imagedata = readStream(imgIS);
					} catch (final OutOfMemoryError e) {
//						if (MyApplication.opLogger != null) {
//							MyApplication.opLogger.error("自助查勘界面图片内存溢出", e);
//						}
						new Thread(new Runnable() {
							@Override
							public void run() {
								sc_LogUtil.sendLog(2, "自助查勘界面导入图片内存溢出："+e.getMessage());
							}
						}).start();
						AlertDialog.Builder callDailog = new AlertDialog.Builder(
								Sc_CaseOfOnlyOneActivity.this);
						callDailog.setIcon(android.R.drawable.ic_dialog_info)
								.setTitle("提示")
								.setMessage("您导入的图片过大，或者图片已删除，请重新选择。")
								.setPositiveButton("我知道了", null).show();
						e.printStackTrace();
					}
					File fi = new File(path);
					Sc_MyApplication.getInstance().setFile(fi);
					progressdialog.setMessage("正在加载图片，请稍候。。。");
					progressdialog.show();
					Thread thread = new Thread(lxxRunnable);
					thread.start();
				} catch (final FileNotFoundException e) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							sc_LogUtil.sendLog(2, "自助查勘界面找不到图片文件："+e.getMessage());
						}
					}).start();
//					if (MyApplication.opLogger != null) {
//						MyApplication.opLogger.error("自助查勘界面找不到图片文件", e);
//					}

					AlertDialog.Builder callDailog = new AlertDialog.Builder(
							Sc_CaseOfOnlyOneActivity.this);
					callDailog.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle("提示")
							.setMessage("您导入的图片过大，或者图片已删除，请重新选择。")
							.setPositiveButton("我知道了", null).show();
					e.printStackTrace();
				}

			}
		}

	}

	/**
	 * 19
	 * 
	 * @param 将图片内容解析成字节数组
	 *            20
	 * @param inStream
	 *            21
	 * @return byte[] 22
	 * @throws Exception
	 *             23
	 */
	public static byte[] readStream(InputStream inStream) {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = null;
		try {
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			data = outStream.toByteArray();
			outStream.close();
			inStream.close();
		} catch (final IOException e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "自助查勘界面IO异常："+e.getMessage());
				}
			}).start();
//			if (MyApplication.opLogger != null) {
//				MyApplication.opLogger.error("自助查勘界面出错", e);
//			}

			e.printStackTrace();
		}

		return data;
	}

	/**
	 * 用来处理拍摄得到的照片 包括压缩和打水印
	 * 
	 * @author 刘星星
	 */
	public Runnable lxxRunnable = new Runnable() {
		@Override
		public void run() {
			Sc_MyApplication.switch_tag = false;
			Sc_MyApplication.getInstance().setLegendid(legendid);
			sc_PicHandler ph = new sc_PicHandler(imagedata);
			ph.handlePic();
			Message msg = new Message();
			msg.what = 0;
			lxxHandler.sendMessage(msg);
			Sc_MyApplication.switch_tag = true;
		}
	};
	/**
	 * 图片处理完后用来改变手机ＵＩ
	 * 
	 * @author 刘星星
	 */
	public Handler lxxHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (Sc_MyApplication.getInstance().getFile() != null
						&& Sc_MyApplication.getInstance().getFile().exists()) {
					updateListItemData(Sc_MyApplication.getInstance().getFile(),
							listPosition);
					notifyAdapter();
					progressdialog.dismiss();
					imageListView.setSelection(listPosition);
				} else {
					Toast.makeText(Sc_CaseOfOnlyOneActivity.this,
							"抱歉，图片处理失败，请重新拍摄。。", 5000).show();
				}
				sc_BitmapCache.getInstance().clearCache();
			}
			super.handleMessage(msg);
		}

	};

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = new MenuInflater(getApplicationContext());
//		inflater.inflate(R.menu.menu, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		// 响应每个菜单项(通过菜单项的ID)
//		case R.id.menu_main:
//			Intent i = new Intent(this, sc_MainActivity.class);
//			startActivity(i);
//			finish();
//			break;
//		case R.id.menu_exit:
//			sc_ExitApplication.getInstance().showExitDialog();
//			break;
//		default:
//			// 对没有处理的事件，交给父类来处理
//			return super.onOptionsItemSelected(item);
//		}
//		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
//		return super.onOptionsItemSelected(item);
//	}

	/**
	 * @param bitmap
	 *            拍照得到的照片
	 * @param listPosition
	 *            listData这个集合的一行
	 * @param itemPosition
	 *            listData集合的一行的元素下表 最大值为1
	 * @author 刘星星
	 */

	public void updateListItemData(File file, int listPosition) {
		objectList.get(listPosition).put("savepath", file.getAbsolutePath());
		objectList.get(listPosition).put("name", objectList.get(listPosition).get("name"));
		objectList.get(listPosition).put("state", 2);
		objectList.get(listPosition).put("uploadpath",
				objectList.get(listPosition).get("uploadpath"));
		objectList.get(listPosition).put("photoid",objectList.get(listPosition).get("photoid"));
		objectList.get(listPosition).put("checked",objectList.get(listPosition).get("checked"));
		objectList.get(listPosition).put("photofile", Sc_MyApplication.getInstance().getFile());
		objectList.get(listPosition).put("reviewreason", objectList.get(listPosition).get("reviewreason"));
		objectList.get(listPosition).put("ischange", 1);
		objectList.get(listPosition).put("legendid",objectList.get(listPosition).get("legendid"));
		objectList.get(listPosition).remove("pic_state");
	}

	/**
	 * @param bitmap
	 *            拍照得到的照片
	 * @param listPosition
	 *            listData这个集合的一行
	 * @param itemPosition
	 *            listData集合的一行的元素下表 最大值为1
	 * @author 刘星星
	 */

	public void updateListItemData(String file, int listPosition, int itemPosition) {
		objectList.get(listPosition).put("savepath", file);
		objectList.get(listPosition).put("name", objectList.get(listPosition).get("name"));
		for(int i = 0;i<legendinfo.size();i++){
			if(legendinfo.get(i).get("legendid")==legendid && legendinfo.get(i).get("isupload") == 1){
				objectList.get(listPosition).put("pic_state", 4);
			}
		}
		objectList.get(listPosition).put("state", 0);
		objectList.get(listPosition).put("uploadpath",
				objectList.get(listPosition).get("uploadpath"));
		objectList.get(listPosition).put("photoid",
				objectList.get(listPosition).get("photoid"));
		objectList.get(listPosition).put("checked",
				objectList.get(listPosition).get("checked"));
		objectList.get(listPosition).put("photofile", Sc_MyApplication.getInstance().getFile());
		objectList.get(listPosition).put("reviewreason", objectList.get(listPosition)
				.get("reviewreason"));
		objectList.get(listPosition).put("ischange", 0);
		objectList.get(listPosition).put("legendid",
				objectList.get(listPosition).get("legendid"));
		objectList.get(listPosition).remove("pic_state");
	}
	
	/**
	 * 点击照片弹出的对话框
	 * 
	 * @param index
	 *            调用系统功能的返回参数
	 * @param type
	 *            标识应该显示的对话框功能参数
	 * @author 刘星星
	 */
	public void showQianDaoDialog(int type, final String file) {
		alertDialog = new Dialog(this, R.style.sc_FullScreenDialog);
		LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View reNameView = mLayoutInflater.inflate(R.layout.sc_dialog_image, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		LinearLayout rb1 = (LinearLayout) reNameView.findViewById(R.id.rb1);
		LinearLayout rb2 = (LinearLayout) reNameView.findViewById(R.id.rb2);
		LinearLayout rb3 = (LinearLayout) reNameView.findViewById(R.id.rb3);
		LinearLayout rb4 = (LinearLayout) reNameView.findViewById(R.id.rb4);
		if (type == 0) {
			rb1.setVisibility(View.VISIBLE);
			rb2.setVisibility(View.VISIBLE);
			rb3.setVisibility(View.GONE);
			rb4.setVisibility(View.GONE);
		} else if (type == 1) {
			rb1.setVisibility(View.GONE);
			rb2.setVisibility(View.GONE);
			rb3.setVisibility(View.VISIBLE);
			rb4.setVisibility(View.GONE);
			if(!new File(file).exists()){
				return;
			}
			ArrayList<String> al = new ArrayList<String>();
			al.clear();
			al.add(file);
			Sc_MyApplication.getInstance().setLegendid(legendid);
			Intent it = new Intent(Sc_CaseOfOnlyOneActivity.this,
					Sc_ImageSwitcher.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putStringArrayListExtra("pathes", al);
			it.putExtra("index", 0);
			startActivity(it);
			return;
		} else if (type == 2) {
			rb1.setVisibility(View.VISIBLE);
			rb2.setVisibility(View.VISIBLE);
			rb3.setVisibility(View.VISIBLE);
			rb4.setVisibility(View.VISIBLE);
		}
		alertDialog.addContentView(reNameView, params);
		alertDialog.show();
		rb1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Sc_MyApplication.getInstance().getSelfHelpFlag() != -1
						&&Sc_MyApplication.getInstance().getCasedescription_tag() !=1){
					Sc_MyApplication.switch_tag = false;
				}
				Sc_MyApplication.getInstance().setLegendid(legendid);
				Sc_MyApplication.getInstance().setHandPicFlag(1);//设置标记为拍摄的图片
				Intent i = new Intent(Sc_CaseOfOnlyOneActivity.this,
						Sc_MyCameraActivity.class);
				Sc_CaseOfOnlyOneActivity.this.startActivityForResult(i, 1);
				alertDialog.dismiss();
			}
		});
		rb2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Sc_MyApplication.getInstance().getSelfHelpFlag() != -1
						&&Sc_MyApplication.getInstance().getCasedescription_tag() !=1){
					Sc_MyApplication.switch_tag = false;
				}
				Sc_MyApplication.getInstance().setLegendid(legendid);
				Sc_MyApplication.getInstance().setHandPicFlag(0);//设置标记为导入图片
				Intent i = new Intent();
				i.setType("image/*");
				i.setAction(Intent.ACTION_GET_CONTENT);
				Sc_CaseOfOnlyOneActivity.this.startActivityForResult(i, 2);
				alertDialog.dismiss();
			}
		});
		rb3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ArrayList<String> al = new ArrayList<String>();
				al.clear();
				al.add(file);
				Sc_MyApplication.getInstance().setLegendid(legendid);
				Intent it = new Intent(Sc_CaseOfOnlyOneActivity.this,
						Sc_ImageSwitcher.class);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.putStringArrayListExtra("pathes", al);
				it.putExtra("index", 0);
				startActivity(it);
				alertDialog.dismiss();
			}
		});
		rb4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int case_type = 0;
				if (Sc_MyApplication.getInstance().getStepstate() == 0) {
					case_type = 1;
				} else {
					if(Sc_MyApplication.getInstance().getServer_type()==1){
						case_type = 1;
					}else{
						case_type = 2;
					}
				}
				Sc_MyApplication.getInstance().setLegendid(legendid);
				File file1 = new File(file);
				if(file1!=null&&file1.exists()){
					file1.delete();
				}
				
				database.delete(
						"uploadinfo",
						"claimid = ? and legendid= ? and type= ?",
						new String[] { Sc_MyApplication.getInstance().getClaimid(),
								legendid + "",case_type+"" });
				database.delete(
						"claimphotoinfo",
						"claimid = ? and legendid= ? and type= ?",
						new String[] {  Sc_MyApplication.getInstance().getClaimid(),
								legendid + "" ,case_type+""});
				updateListItemData(sc_LegendInfo.getLegendImage(legendid+""), listPosition,true);
				notifyAdapter();
				alertDialog.dismiss();
			}
		});

	}
	// android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(File file) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(file);
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	

	/**
	 * 
	 * @param legendid图例编号
	 *            显示进度条
	 */

	public void startProgressBar(String legendid_, int length) {
		ArrayList<HashMap<String, Object>> myprogressbars = Sc_CaseOfOnlyOneActivity.myprogressbars;
		// System.out.println();
		for (int i = 0; i < myprogressbars.size(); i++) {
			String mylegendid = myprogressbars.get(i).get("legendid")
					.toString();
			if (mylegendid.equals(legendid_)) {
				ProgressBar progressbar = (ProgressBar) myprogressbars.get(i)
						.get("progressbar");
				progressbar.setMax(length);
				progressbar.setVisibility(View.VISIBLE);
				progressbar.getProgressDrawable().setAlpha(50);
				TextView numberupload = (TextView) myprogressbars.get(i).get(
						"numberupload");
				numberupload.setText("0%");
				ImageView imageview = (ImageView) myprogressbars.get(i).get(
						"image");
				imageview.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						ContentValues values = new ContentValues();
						values.put("status", 0);
						listPosition = v.getId();
						legendid = (Integer) objectList.get(listPosition).get("legendid");
						database.update("uploadinfo", values,
								"claimid = ? and legendid <> ? and status = ?",
								new String[] {
										Sc_MyApplication.getInstance()
												.getClaimid(), legendid + "",
										"-1" });
						// MyHandler.getInstance().post(runnable);

						updateListItemData(1, listPosition);

						int state = (Integer) objectList.get(listPosition).get("state");
						// System.out.println("你点击了图例" + legendid + "状态为" +
						// state);
						final String file = objectList.get(listPosition).get("savepath").toString();
						try {
							showQianDaoDialog(state, file);
						} catch (final Exception e) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									sc_LogUtil.sendLog(2, "自助查勘界面点击图片进行拍照和导入出现错误："+e.getMessage());
								}
							}).start();
//							if (MyApplication.opLogger != null) {
//								MyApplication.opLogger.error("自助查勘界面出错", e);
//							}

						}
					}
				});
			}
		}
		for (int i = 0; i < objectList.size(); i++) {
			String mylegendid = objectList.get(i).get("legendid").toString();
			if (mylegendid.equals(legendid_)) {
				imageListView.setSelection((int) i / 2);
			}
		}
	}

	/**
	 * 
	 * @param legendid图例编号
	 *            更新进度条
	 */

	public void updateProgressBar(String legendid_, int position) {
		ArrayList<HashMap<String, Object>> myprogressbars = Sc_CaseOfOnlyOneActivity.myprogressbars;
		for (int i = 0; i < myprogressbars.size(); i++) {
			String mylegendid = myprogressbars.get(i).get("legendid")
					.toString();
			if (mylegendid.equals(legendid_)) {
				ProgressBar progressbar = (ProgressBar) myprogressbars.get(i)
						.get("progressbar");
				progressbar.setProgress(position);
				progressbar.setMax(Sc_MyApplication.getInstance()
						.getProgressbarmax());
				TextView numberupload = (TextView) myprogressbars.get(i).get(
						"numberupload");
				// 创建一个数值格式化对象
				NumberFormat numberFormat = NumberFormat.getInstance();
				// 设置精确到小数点后2位
				numberFormat.setMaximumFractionDigits(0);
				String result = numberFormat.format((float) position
						/ (float) Sc_MyApplication.getInstance()
								.getProgressbarmax() * 100);
				numberupload.setText(result + "%");
			}
		}
	}

	/**
	 * 
	 * @param legendid图例编号
	 *            关闭进度条
	 */
	@Override
	public void closeProgressBar(String legendid_) {
		ArrayList<HashMap<String, Object>> myprogressbars = Sc_CaseOfOnlyOneActivity.myprogressbars;
		for (int i = 0; i < myprogressbars.size(); i++) {
			String mylegendid = myprogressbars.get(i).get("legendid")
					.toString();
			if (mylegendid.equals(legendid_)) {
				ProgressBar progressbar = (ProgressBar) myprogressbars.get(i)
						.get("progressbar");
				progressbar.setVisibility(View.GONE);
				TextView numberupload = (TextView) myprogressbars.get(i).get(
						"numberupload");
				numberupload.setText("100%");
				ImageView imageview = (ImageView) myprogressbars.get(i).get(
						"image");
				imageview.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						ContentValues values = new ContentValues();
						values.put("status", 0);
						listPosition = v.getId();
						legendid = (Integer) objectList.get(listPosition).get("legendid");
						database.update("uploadinfo", values,
								"claimid = ? and legendid <> ? and status = ?",
								new String[] {
										Sc_MyApplication.getInstance()
												.getClaimid(), legendid + "",
										"-1" });
						updateListItemData(2, listPosition);

						int state = (Integer) objectList.get(listPosition).get("state");
						// System.out.println("你点击了图例" + legendid + "状态为" +
						// state);
						final String file = objectList.get(listPosition).get("savepath").toString();

						try {
							showQianDaoDialog(state, file);
						} catch (final Exception e) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									sc_LogUtil.sendLog(2, "自助查勘界面点击图片进行拍照和导入出现错误："+e.getMessage());
								}
							}).start();
						}
					}
				});
			}
		}
	}

	/**
	 * 更新对话框的点击状态
	 * 
	 * @param bitmap
	 * @param listPosition
	 * @param itemPosition
	 */
	public void updateListItemData(int state, int listPosition) {
		objectList.get(listPosition).put("savepath",
				objectList.get(listPosition).get("savepath"));
		objectList.get(listPosition).put("name", objectList.get(listPosition).get("name"));
		objectList.get(listPosition).put("state", state);
		objectList.get(listPosition).put("uploadpath",
				objectList.get(listPosition).get("uploadpath"));
		objectList.get(listPosition).put("photoid",
				objectList.get(listPosition).get("photoid"));
		objectList.get(listPosition).put("ischange",
				objectList.get(listPosition).get("ischange"));
		objectList.get(listPosition).put("checked",
				objectList.get(listPosition).get("checked"));
		objectList.get(listPosition).put("reviewreason", objectList.get(listPosition)
				.get("reviewreason"));
		objectList.get(listPosition).put("photofile",
				objectList.get(listPosition).get("photofile"));
		objectList.get(listPosition).put("legendid",
				objectList.get(listPosition).get("legendid"));
		objectList.get(listPosition).remove("pic_state");
	}


	/**
	 * 处理来电时的服务类
	 */

	private BroadcastReceiver callReceiver = new BroadcastReceiver() {
		private static final String TAG = "PhoneStatReceiver";
		boolean incomingFlag = false;

		String incoming_number = null;

		@Override
		public void onReceive(Context context, Intent intent) {
			// 如果是拨打电话
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				incomingFlag = false;
				String phoneNumber = intent
						.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Log.i(TAG, "call OUT:" + phoneNumber);
				System.out.println("打出去的电话：" + phoneNumber);
			} else {
				// 如果是来电
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Service.TELEPHONY_SERVICE);

				switch (tm.getCallState()) {
				case TelephonyManager.CALL_STATE_RINGING:
					incomingFlag = true;// 标识当前是来电
					incoming_number = intent.getStringExtra("incoming_number");
					Log.i(TAG, "RINGING :" + incoming_number);
					System.out.println("打进来的电话：" + incoming_number);
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if (incomingFlag) {
						Log.i(TAG, "incoming ACCEPT :" + incoming_number);
						System.out.println("接了：" + incoming_number);
					}
					break;

				case TelephonyManager.CALL_STATE_IDLE:
					if (incomingFlag) {
						if (!progressdialog.isShowing()) {
							sc_MyHandler.getInstance().sendEmptyMessage(-14);
						}
						Log.i(TAG, "incoming IDLE");
						System.out.println("停了：" + incoming_number);
					}
					break;
				}
			}
		}
	};
	
	/**
	 * 获取未更新的图例
	 * @param type 1 现场拍照图片 2 单证图片
	 * @return
	 */
	
	public String checkLegends(int type){
		String legends = "";
		String sql1 = "";
		if(type ==1){
			sql1 = "select legends from claiminfo where claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid() + "'";
		}else{
			sql1 = "select certificates from claiminfo where claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid() + "'";
		}
		
		List<Map<String, Object>> selectresult1 = database.selectRow(sql1, null);
		
		if(selectresult1.size()>0){
			if(selectresult1.get(0).get("legends")!=null){
				legends = selectresult1.get(0).get("legends").toString();
			}
		}
		String mylegends = "";
		ArrayList<String> leg = new ArrayList<String>();
		ArrayList<String> leg1 = new ArrayList<String>();
		String[] legendids = legends.split(";");
		String[] legendids1;
		String sql = "";
		StringBuffer sqllegendid = new StringBuffer("(");
		for (int i = 0; i < legendids.length; i++) {
			legendids1 = legendids[i].split(",");
			leg.add(legendids1[0]);
			sqllegendid.append("'"+legendids1[0]+"',");
		}
		if(sqllegendid.length()>2){
			sql = sqllegendid.subSequence(0, sqllegendid.length()-1)+")";
		}
		List<Map<String, Object>>  selectResult  = null;
		selectResult = sc_DBHelper.getInstance().selectRow(
				"select legendid from legendinfo where legendid in "+sql, null);
		
		if(selectResult!=null&&selectResult.size()>0){
			for(int j = 0; j<selectResult.size();j++){
				if(selectResult.get(j).get("legendid")!=null
						&&!selectResult.get(j).get("legendid").toString().equals("")){
					leg1.add(selectResult.get(j).get("legendid").toString());
				}
			}
		}
		leg.removeAll(leg1);
		
		StringBuffer buffer = new StringBuffer("");
		
		for(int i = 0;i<leg.size();i++){
			buffer.append(leg.get(i));
			buffer.append(",");
		}
		if(buffer.length()>1){
			mylegends = buffer.substring(0, buffer.length()-1);
		}
		return mylegends;
	}
	
	/**
	 * 获取图例对象列表
	 */
	
	public void getLegendinfo(String legendis) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getLegendListByIds"));

		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("legendids", legendis));
		sc_UploadData1 uploaddata = new sc_UploadData1(Sc_MyApplication.URL + "claim",params);
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
	
	Dialog inputShareDialog = null;//输入密码弹出框
	/**
	 * 输入密码框
	 */
	public void showInputDialog(String oldPassword){
		inputShareDialog = new Dialog(this, R.style.sc_myDialogStyle);
		View view = LayoutInflater.from(this).inflate(R.layout.sc_dialog_password, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(Sc_MyApplication.getInstance().getSw()*0.9),
				LayoutParams.WRAP_CONTENT);
		view.findViewById(R.id.passwordBtn).setOnClickListener(this);
		view.findViewById(R.id.passwordAmendBtn).setOnClickListener(this);
		TextView oldPass = (TextView) view.findViewById(R.id.passExplainTv);
		password_ET = (EditText) view.findViewById(R.id.passwordEt2);
		String content = "客户端保存的密码为:<font color=\"#004bb2\">"+oldPassword+"</font>，不能通过校验，请输入您最新设置的密码或修改密码。";
		oldPass.setText(Html.fromHtml(content));
		inputShareDialog.addContentView(view, params);
		inputShareDialog.setCanceledOnTouchOutside(false);
		inputShareDialog.show();
	}
	
	/**
	 * 更新对话框的点击状态
	 * 
	 * @param bitmap
	 * @param listPosition
	 * @param itemPosition
	 */
	public void updateListItemData(int state, int listPosition,boolean b) {
		objectList.get(listPosition).put("savepath", sc_LegendInfo.getLegendImage(objectList.get(listPosition).get("legendid").toString()));
		objectList.get(listPosition).put("name", objectList.get(listPosition).get("name"));
		objectList.get(listPosition).put("state", 0);
		objectList.get(listPosition).put("uploadpath",
				objectList.get(listPosition).get("uploadpath"));
		objectList.get(listPosition).put("photoid",
				objectList.get(listPosition).get("photoid"));
		objectList.get(listPosition).put("ischange",0);
		objectList.get(listPosition).put("checked",
				objectList.get(listPosition).get("checked"));
		objectList.get(listPosition).put("photofile","");
		objectList.get(listPosition).put("legendid",
				objectList.get(listPosition).get("legendid"));
		objectList.get(listPosition).put("reviewreason", objectList.get(listPosition).get("reviewreason"));
		objectList.get(listPosition).remove("pic_state");
	}
}
