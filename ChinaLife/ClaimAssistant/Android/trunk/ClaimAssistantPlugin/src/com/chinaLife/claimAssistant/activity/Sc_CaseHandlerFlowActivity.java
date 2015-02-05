package com.chinaLife.claimAssistant.activity;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinaLife.claimAssistant.Interface.sc_InitInterface;
import com.chinaLife.claimAssistant.Interface.sc_ProgressBarInterface;
import com.chinaLife.claimAssistant.Interface.sc_SurveySelfInterface;
import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.adapter.sc_CaseHandlerFlowAdapter;
import com.chinaLife.claimAssistant.adapter.sc_SpinnerAdapter;
import com.chinaLife.claimAssistant.bean.sc_ClaimInfo;
import com.chinaLife.claimAssistant.bean.sc_ClaimPhotoInfo;
import com.chinaLife.claimAssistant.bean.sc_GetText;
import com.chinaLife.claimAssistant.bean.sc_LegendInfo;
import com.chinaLife.claimAssistant.content.sc_Contentvalues;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_GetMsg;
import com.chinaLife.claimAssistant.thread.sc_MyHandler;
import com.chinaLife.claimAssistant.thread.sc_PicHandler;
import com.chinaLife.claimAssistant.thread.sc_UploadData;
import com.chinaLife.claimAssistant.thread.sc_UploadData1;
import com.chinaLife.claimAssistant.thread.sc_UploadData3;
import com.chinaLife.claimAssistant.thread.sc_UploadPhotoFile;
import com.chinaLife.claimAssistant.tools.sc_BitmapCache;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
public class Sc_CaseHandlerFlowActivity extends Activity implements sc_InitInterface,
		sc_ProgressBarInterface, OnClickListener,sc_SurveySelfInterface {
	private Button topBackBtn;// 顶部返回按钮
	private Button notifiCationBtn,notifiBtn;// 消息按钮
	private ImageView stepImgBtn1,stepImgBtn2,stepImgBtn3;// 用来显示当前步伐的图标
	private ImageButton step1Btn, step2Btn, /*step3Btn,*/ step4Btn, step5Btn, img1, /*img2,*/ img3, img4;// 第1步的按钮 第2步的按钮 第3步的按钮 第4步的按钮 第5步的按钮 第1条杠 第2条杠 第3条杠 第4条杠
	public Button step2, /*step3,*/ step4, step5;
	private TextView currentStep;// 用来表示当前为第几步
	private GridView imgListView;// 用来装载图片的列表控件
	private Button submitBtn; // 提交按钮
	public static int legendid = 1;// 图例编号
	private Dialog alertDialog;// 自定义的弹出框用来显示签到的两个选项
	public static sc_DBHelper database = null;
	public static int listPosition = 0;// 标识listData这个集合中的行
	private  Bitmap bitmap = null;
	private Location m_location = null;
	private byte[] imagedata;
	private Spinner spReason;// 不同意原因
	private static File file = null;// 照相产生的图片文件或从sd卡读取的图片文件
/*	private MKSearch mSearch;
	private GeoPoint geoPoint;*/
	private LinearLayout linearofbtn;
	private String address = "";//定位获取到的地址
	private double latitude = 0.0f;//经度
	private double longitude = 0.0f;//纬度
	public static ArrayList<HashMap<String, Object>> myprogressbars = new ArrayList<HashMap<String, Object>>();
	private ProgressDialog progressdialog = null;
	private Button cancleBtn;// 取消按钮
	private LinearLayout linearOfAskClaim;// 申请索赔对应的层
	private LinearLayout linearOfDanZheng;// 单证对应的层
	private LinearLayout linearOfSureBank;// 确认银行账号对应的层
	private ScrollView suopeiLinear;// 索赔界面
	private RelativeLayout relativeDanzheng, relativeBank;// 单证补全的显示框 确认银行账号的显示框
	private ArrayList<HashMap<String, Object>> stepList = new ArrayList<HashMap<String, Object>>();
	private LinearLayout moneyLinear;// 申请索赔界面
	private TextView money_t1;// 赔款金额
	private TextView money_t3;// 维护费用
	private TextView money_t4;// 报销费
	private TextView money_t5;// 免赔费
	private TextView money_t6;// 损耗费
	private RadioGroup radioGroup;// 同意与不同意的控件组
	private RadioButton money_rb1, money_rb2;// 申请索赔页面的同意和不同意按钮
	private EditText money_et;// 申请索赔页面不同意的原因输入框
	// private ListView danzhengLinear;// 单证补全界面
	private LinearLayout sureBankLinear;// 确认银行账号界面
	private EditText bank_et1, bank_et2, bank_et3;// 确认银行账号的三个文本框
	private EditText bankZong,phone;//总行   手机号码
	private CheckBox cb1;// 确定银行账号勾选
	private LinearLayout detailOver;// 结案信息界面
	private TextView detailTime, detailContent;// 结案信息时间 结案信息内容
//	private ImageButton suo1Btn, suo2Btn, suo3Btn;// 用来收缩申请索赔界面的三个按钮
//	private TextView detailText;// 右边的详情两字 申明它只是为了显示和隐藏它 数据控制人员不必去控制它
	private int claim_mode = 0;
	private String price;
	public boolean operate_tag = false;// 操作是否成功
	private ArrayList<HashMap<String, Integer>> legendinfo = new ArrayList<HashMap<String, Integer>>();
	private int tag = 0;// 0,caselistactivity,1,caseinfoactivity,2,messagelistactivity
	private byte btn1state = 0;// 0不可点，1可点
	private byte btn2state = 0;
	private byte btn3state = 0;
	private byte btn4state = 0;
	private Timer timer = null;
	private int isSuccessConfirmBank = 0;// 0有单证时，确定银行账号成功，1，无单证时提交银行账号成功
	private StringBuilder danzhengtext =new StringBuilder();// 缺少哪些单证的文本
	private boolean haszhizhi = false;//是否涉及纸质
	private final static float TARGET_HEAP_UTILIZATION = 0.75f; 
	private boolean btn_tag1 = true;
	private int claimstate_pre;//消息通知历史状态
	private RelativeLayout suoLinear1,suoLinear2,suoLinear3;
	private TextView siteTakePicText;//现场拍照的文字
	private EditText password_ET = null;
	
	
	private String bank_name= "";
	private String bank_code="";
	
	private String insuredName ="";
	
	private int back_activity = 0;
	public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    
    public boolean flag = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sc_a_casehandleflow);
		back_activity = getIntent().getIntExtra("back_activity", 0);
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction("android.intent.action.PHONE_STATE");
		myIntentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(callReceiver, myIntentFilter);
		Sc_ExitApplication.getInstance().context = Sc_CaseHandlerFlowActivity.this;
		Sc_ExitApplication.getInstance().addActivity(Sc_CaseHandlerFlowActivity.this);
		initView();
		initViewFun();
		initData();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = format.format(new Date());
		Sc_MyApplication.getInstance().setNowTime(nowTime);
		getLocation();// 定位
		System.gc();
	}
	private void getLocation(){
		mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类   
		mLocationClient.registerLocationListener(myListener );   	//注册监听函数
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
				System.out.println("获取到的当前地址为：================"+location.getAddrStr());
			}
			
			}
		public void onReceivePoi(BDLocation poiLocation) {	
				if (poiLocation == null){		
					return ;		
					}		
				}
			}
	/**
	 * 动态添加申请索赔界面的概述
	 * 
	 * @author  刘星星
	 */

	public void createMoneyDiv(ArrayList<HashMap<String, String>> list) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout moneygaisu = (LinearLayout) moneyLinear
				.findViewById(R.id.moneygaisu);
		if(list.size()==0){
			LinearLayout gaishuLinear = (LinearLayout) moneyLinear.findViewById(R.id.linearGaiShu);
			gaishuLinear.setVisibility(View.GONE);
			return;
		}
		LinearLayout linearLayout = null;
		TextView textKey = null;
		TextView textValue = null;
		
		HashMap<String, String> map = null;
		moneygaisu.removeAllViews();
		for (int i = 0; i < list.size(); i++) {
			linearLayout = (LinearLayout) inflater.inflate(
					R.layout.sc_item_money_list, null);
			textKey = (TextView) linearLayout.findViewById(R.id.textKey);
			textValue = (TextView) linearLayout.findViewById(R.id.tvValue);
			map = list.get(i);
			textKey.setText(map.get("key"));
			textValue.setText(map.get("value"));
			moneygaisu.addView(linearLayout, i);
		}
	}

	@Override
	public void initView() {
		topBackBtn = (Button) findViewById(R.id.fanhui);
		notifiCationBtn = (Button) findViewById(R.id.notication);
		notifiBtn = (Button) findViewById(R.id.notifiBtn);
		stepImgBtn1 = (ImageView) findViewById(R.id.stepImage1);
		stepImgBtn2 = (ImageView) findViewById(R.id.stepImage2);
		stepImgBtn3 = (ImageView) findViewById(R.id.stepImage3);
		stepImgBtn1.setVisibility(View.VISIBLE);
		stepImgBtn2.setVisibility(View.GONE);
		stepImgBtn3.setVisibility(View.GONE);
		step1Btn = (ImageButton) findViewById(R.id.step1);
		step2Btn = (ImageButton) findViewById(R.id.step2);
//		step3Btn = (ImageButton) findViewById(R.id.step3);
		step4Btn = (ImageButton) findViewById(R.id.step4);
		step5Btn = (ImageButton) findViewById(R.id.step5);
		img1 = (ImageButton) findViewById(R.id.image2);
//		img2 = (ImageButton) findViewById(R.id.image4);
		img3 = (ImageButton) findViewById(R.id.image6);
		img4 = (ImageButton) findViewById(R.id.image8);
		currentStep = (TextView) findViewById(R.id.currentStep);
		imgListView = (GridView) findViewById(R.id.listView);
		submitBtn = (Button) findViewById(R.id.submit);
		cancleBtn = (Button) findViewById(R.id.cancle);
		linearofbtn = (LinearLayout) findViewById(R.id.linear7);
		linearOfAskClaim = (LinearLayout) findViewById(R.id.linear6);
		linearOfDanZheng = (LinearLayout) findViewById(R.id.linear10);
		linearOfSureBank = (LinearLayout) findViewById(R.id.linear11);
		relativeDanzheng = (RelativeLayout) findViewById(R.id.linear8);
		relativeBank = (RelativeLayout) findViewById(R.id.linear9);
		suopeiLinear = (ScrollView) findViewById(R.id.suopei);
		moneyLinear = (LinearLayout) getLayoutInflater().inflate(
				R.layout.sc_item_money, null);
		money_t1 = (TextView) moneyLinear.findViewById(R.id.money);
		money_t3 = (TextView) moneyLinear.findViewById(R.id.tv9);
		money_t4 = (TextView) moneyLinear.findViewById(R.id.tv10);
		money_t5 = (TextView) moneyLinear.findViewById(R.id.tv1);
		money_t6 = (TextView) moneyLinear.findViewById(R.id.tv2);
		radioGroup = (RadioGroup) moneyLinear.findViewById(R.id.radioGroup);
		money_rb1 = (RadioButton) moneyLinear.findViewById(R.id.radioBtn1);
		money_rb2 = (RadioButton) moneyLinear.findViewById(R.id.radioBtn2);
		money_et = (EditText) moneyLinear.findViewById(R.id.reason);
		spReason = (Spinner) moneyLinear.findViewById(R.id.spreason);
		sureBankLinear = (LinearLayout) getLayoutInflater().inflate(
				R.layout.sc_item_bank, null);
		bank_et1 = (EditText) sureBankLinear.findViewById(R.id.bank);
		bank_et2 = (EditText) sureBankLinear.findViewById(R.id.name);
		bank_et3 = (EditText) sureBankLinear.findViewById(R.id.banknumer);
		bankZong = (EditText) sureBankLinear.findViewById(R.id.bankZong);
		bankZong.setFocusable(false);
		bankZong.setOnClickListener(this);
		phone = (EditText) sureBankLinear.findViewById(R.id.phone);
		cb1 = (CheckBox)sureBankLinear.findViewById(R.id.cb1);
		bank_et3.setFilters(new InputFilter[] {new InputFilter.LengthFilter(23)});  
		bank_et3.addTextChangedListener(new MyTextWatcher(bank_et3, "0123456789 "));
		suoLinear1 = (RelativeLayout) findViewById(R.id.linear5);
		/*suoLinear2 = (RelativeLayout) findViewById(R.id.linear8);
		suoLinear3 = (RelativeLayout) findViewById(R.id.linear9);*/
		/*suo1Btn = (ImageButton) findViewById(R.id.suo1);
		suo2Btn = (ImageButton) findViewById(R.id.suo2);
		suo3Btn = (ImageButton) findViewById(R.id.suo3);*/
		linearOfAskClaim.removeAllViews();
		linearOfDanZheng.removeAllViews();
		linearOfSureBank.removeAllViews();
		linearOfAskClaim.addView(moneyLinear);
		// linearOfDanZheng.addView(danzhengLinear);
		linearOfSureBank.addView(sureBankLinear);
		detailOver = (LinearLayout) findViewById(R.id.detailLinear);
		detailTime = (TextView) findViewById(R.id.detailTime);
		detailContent = (TextView) findViewById(R.id.detailContent);
//		detailText = (TextView) findViewById(R.id.detailT);
		step2 = (Button) findViewById(R.id.step2Btn);
//		step3 = (Button) findViewById(R.id.step3Btn);
		step4 = (Button) findViewById(R.id.step4Btn);
		step5 = (Button) findViewById(R.id.step5Btn);
		siteTakePicText = (TextView) findViewById(R.id.tv31);
	}
	
	class MyTextWatcher implements TextWatcher {
		private String tmp = "";
		private String digits = "abcdef";// 可以输入的字符
		private EditText editText;

		public MyTextWatcher(final EditText editText, final String digits) {
			this.editText = editText;
			this.digits = digits;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			tmp = s.toString();// 获得改变前的字符串
		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (str.equals(tmp)) {
				return;// 如果tmp==str则返回，因为这是我们设置的结果。否则会形成死循环。
			}

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				if (digits.indexOf(str.charAt(i)) >= 0) {// 判断字符是否在可以输入的字符串中
					sb.append(str.charAt(i));// 如果是，就添加到结果里，否则跳过
				}
			}
			int index = bank_et3.getSelectionStart();
			tmp = sb.toString();// 设置tmp，因为下面一句还会导致该事件被触发
			editText.setText(sc_StringUtils.format("#### ", tmp.replace(" ", "")));// 设置结果
			editText.invalidate();
			if(index>=bank_et3.getText().toString().replace(" ", "").toCharArray().length){
				bank_et3.setSelection(bank_et3.getText().toString().toCharArray().length);
			}else{
				bank_et3.setSelection(index);
			}

		}
	}

	
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		int index = bank_et3.getSelectionStart();
//		bank_et3.setText(StringUtils.format("#### ", bank_et3.getText().toString().replace(" ", "")));
//		if(index>=bank_et3.getText().toString().replace(" ", "").toCharArray().length){
//			bank_et3.setSelection(bank_et3.getText().toString().toCharArray().length);
//		}else{
//			bank_et3.setSelection(index);
//		}
//		
//		return true;
//	}
	
	public OnCheckedChangeListener radioGroupListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId == R.id.radioBtn1) {
				money_et.setVisibility(View.GONE);
				spReason.setVisibility(View.GONE);
			} else {
				money_et.setVisibility(View.VISIBLE);
				spReason.setVisibility(View.VISIBLE);
			}
		}
	};
	
	@Override
	protected void onPause() {
		sc_UploadPhotoFile.isstop = true;
		Sc_MyApplication.getInstance().setUploadon(false);
		super.onStop();
	};
	
	@Override
	protected void onStop() {
		sc_UploadPhotoFile.isstop = true;
		Sc_MyApplication.getInstance().setUploadon(false);
		super.onStop();
	}
	/*
	 * 创建一个进度条，在每次需要显示进度条的时候都必须调用此方法，否则有的手机会出现进度条假死状态
	 */
	private void createProgress(){
		if(progressdialog!=null)
			progressdialog = null;
		progressdialog = new ProgressDialog(this);
		progressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressdialog.setCanceledOnTouchOutside(false);
		progressdialog.setCancelable(false);
		Sc_MyApplication.getInstance().setProgressdialog(progressdialog);
	}
	@Override
	public void initData() {
		Sc_MyApplication.getInstance().setSayNumber(0);
		if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
			Sc_MyApplication.getInstance().setContext(this);
			Sc_MyApplication.getInstance().setContext2(getApplicationContext());
			sc_MyHandler.getInstance();
			database = sc_DBHelper.getInstance();
			createProgress();
			Sc_MyApplication.getInstance().setUploadon(false);
			tag = getIntent().getIntExtra("tag", 0);
			if(tag == 2){
				claimstate_pre = getIntent().getIntExtra("claimstate", 0);
				flag = getIntent().getBooleanExtra("flag", false);
				if(flag){
					linearofbtn.setVisibility(View.GONE);
				}
				if(claimstate_pre>Sc_MyApplication.getInstance().getClaimidstate()){
					Sc_MyApplication.getInstance().setClaimidstate(claimstate_pre);
				}
			}
			Sc_MyApplication.getInstance().setStepstate(sc_GetText.pareStatus(Sc_MyApplication.getInstance().getClaimidstate()));
			init();
			return;
		}
		
		Sc_MyApplication.getInstance().setContext(this);
		Sc_MyApplication.getInstance().setContext2(getApplicationContext());
		sc_MyHandler.getInstance();
		database = sc_DBHelper.getInstance();
		createProgress();
		tag = getIntent().getIntExtra("tag", 0);
		if(tag == 2){
			claimstate_pre = getIntent().getIntExtra("claimstate", 0);
			flag = getIntent().getBooleanExtra("flag", false);
			if(flag){
				linearofbtn.setVisibility(View.GONE);
			}
			if(claimstate_pre>Sc_MyApplication.getInstance().getClaimidstate()){
				Sc_MyApplication.getInstance().setClaimidstate(claimstate_pre);
			}
		}
		Sc_MyApplication.getInstance().setStepstate(sc_GetText.pareStatus(Sc_MyApplication.getInstance().getClaimidstate()));
		init();
		timer = new Timer();
		timer.schedule(task, 0,5*1000);
	}
	
	TimerTask task = new TimerTask() {
		public void run() {
			sc_GetMsg.getMsg();
			handlerrunnable.sendEmptyMessage(8);
		}
	};
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) { // 创建菜单项，实现附加功能
//		super.onCreateOptionsMenu(menu);
//		MenuInflater inflater = new MenuInflater(getApplicationContext());
//		inflater.inflate(R.menu.menu, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		// 响应每个菜单项(通过菜单项的ID)
//		case R.id.menu_main:
//			Intent i = new Intent(this,sc_MainActivity.class);
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
//		return true;
//	}
	/**
	 * 刷新列表布局的数据
	 * 
	 * @param listFlag
	 *            刷新布局需要的数据列表ID 2代表第二步的数据 3代表第三步的数据
	 * @author  刘星星
	 */
	public void notifyView() {
		sc_CaseHandlerFlowAdapter adapter = new sc_CaseHandlerFlowAdapter(Sc_CaseHandlerFlowActivity.this,stepList);
		imgListView.setAdapter(adapter);
		imgListView.setCacheColorHint(0);
	}

	public ArrayList<HashMap<String, String>> getClaimoverview(
			String claimoverview) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String[] claimoverview1;
		String[] claimoverview2;
		claimoverview1 = claimoverview.split("%%");
		for (int i = 0; i < claimoverview1.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			claimoverview2 = claimoverview1[i].split("@@");
			try {
				map.put("key", claimoverview2[0]);
			} catch (Exception e) {
				map.put("key", "");
			}
			try {
				map.put("value", claimoverview2[1]);
			} catch (Exception e) {
				map.put("value", "");
			}
			if(map.get("value").equals("")
					&&map.get("key").equals("")){
				
			}else{
				list.add(map);
			}
		}

		return list;
	}

	/**
	 * 刷新列表布局的数据
	 * 
	 * @param listFlag
	 *            刷新布局需要的数据列表ID 2代表第二步的数据 3代表第三步的数据
	 * @author  刘星星
	 */
	private void notifyAdapter() {
		imgListView.setAdapter(new sc_CaseHandlerFlowAdapter(this,stepList));
		imgListView.setCacheColorHint(0);
	}

	/**
	 * 用来将Item为一个对象的List的数据加入 Item为两个对象的List 适合于列表数据赋值（一行有两个对象）
	 * 
	 * @param fromList
	 *            Item为一个对象的List
	 * @param toList
	 *            Item 为两个对象的List
	 * @return Item为两个对象的List
	 * @author  刘星星
	 */
	public ArrayList<ArrayList<HashMap<String, Object>>> setTheListData(
			ArrayList<HashMap<String, Object>> fromList,
			ArrayList<ArrayList<HashMap<String, Object>>> toList) {
		for (int a = 0; a < fromList.size(); a += 2) {
			ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
			if (fromList.size() % 2 == 0) {
				al.add(fromList.get(a));
				al.add(fromList.get(a + 1));
			} else {
				if (a < fromList.size() - 1) {
					al.add(fromList.get(a));
					al.add(fromList.get(a + 1));
				} else if (a == fromList.size() - 1) {
					al.add(fromList.get(a));
				}
			}
			toList.add(al);
		}
		return toList;
	}

	@Override
	public void initViewFun() {
		topBackBtn.setOnClickListener(this);
		notifiCationBtn.setOnClickListener(this);
		notifiBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
		step2.setOnClickListener(this);
		step4.setOnClickListener(this);
		step5.setOnClickListener(this);
		cb1.setOnClickListener(this);
		radioGroup.setOnCheckedChangeListener(radioGroupListener);
		String numbers[]=new String[] {
				"不确定赔付金额能否满足维修；", "赔付金额太低，需重新核定；",
				"不确定是否需要保险公司赔付；", "其他" };
		sc_SpinnerAdapter adapter = new sc_SpinnerAdapter(this,
		        android.R.layout.simple_spinner_item, numbers);
		spReason.setPrompt("请选择不同意的原因：");
		spReason.setAdapter(adapter);
	}

	/**
	 * 现场拍照获取列表数据的信息
	 */
	public void getStep2List() {
		legendinfo.clear();
		String legendmode = "1,1,1;2,1,1;3,1,1;4,1,1;5,1,0;6,1,0;7,1,0;8,1,0;9,1,0;10,1,0";
		if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
			legendmode = "1,1,1;2,1,1;3,1,1;4,1,1;5,1,0;6,1,0;7,1,0;8,1,0;9,1,0;10,1,0";
		}
		String sql1 = "select legends from claiminfo where claimid = '"
				+ Sc_MyApplication.getInstance().getClaimid() + "'";
		List<Map<String, Object>> selectresult1 = database.selectRow(sql1, null);
		
		if(selectresult1.size()>0){
			if(selectresult1.get(0).get("legends")!=null){
				if(selectresult1.get(0).get("legends").toString().contains(",")){
					legendinfo = changeCertificates(selectresult1.get(0).get("legends").toString());
				}else{
					legendinfo = changeCertificates(legendmode);
				}
			}else{
				legendinfo = changeCertificates(legendmode);
			}
			
		}else{
			legendinfo = changeCertificates(legendmode);
		}
		
		if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
			legendinfo = changeCertificates(legendmode);
		}
		stepList.clear();
		HashMap<String, Object> listmap = null;
		myprogressbars.clear();
		for (int i = 0; i < legendinfo.size(); i++) {
			listmap = new HashMap<String, Object>();
			String sql = "select uinfo.savepath"
					+ ",uinfo.legendid,uinfo.claimid,cinfo.type"
					+ ",cinfo.review_result,cinfo.review_reason,cinfo.filename,cinfo.photoid "
					+ "from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type = cinfo.type where uinfo.claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid()
					+ "' and uinfo.legendid = '"
					+ legendinfo.get(i).get("legendid").toString()
					+ "' and cinfo.type = 1";
			List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
					.selectRow(sql, null);
			if (selectresult.size() > 0
					&& selectresult.get(0).get("savepath") != null
					&& !selectresult.get(0).get("savepath").toString().trim()
							.equals("")) {
				file = new File(selectresult.get(0).get("savepath").toString());
				if (file.exists()) {
					listmap.put("savepath", file.getAbsolutePath());
					listmap.put("ischange", 1);
					listmap.put("state", 2);
				} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 16) == 16
						|| (Sc_MyApplication.getInstance().getClaimidstate() & 8) == 8
						|| (Sc_MyApplication.getInstance().getClaimidstate() & 4) == 4) {
					listmap.put("savepath", R.drawable.sc_icon_default_photo);
					listmap.put("ischange", 1);
					listmap.put("state", 0);
				} else {
					listmap.put("savepath", sc_LegendInfo.getLegendImage(legendinfo
							.get(i).get("legendid").toString()));
					listmap.put("ischange", 0);
					listmap.put("state", 0);
				}

			}else {
				listmap.put("savepath", sc_LegendInfo.getLegendImage(legendinfo
						.get(i).get("legendid").toString()));
				listmap.put("ischange", 0);
				listmap.put("state", 0);
			}

			if ((Sc_MyApplication.getInstance().getClaimidstate() & 16) == 16) {
				if(selectresult.size()>0){
					try{
					if (selectresult.get(0)
							.get("review_result")!=null
							&&Integer.parseInt(selectresult.get(0).get("review_result")
							.toString()) == 1) {
						listmap.put("checked", 1);// 不可点
						listmap.put("ischange", 1);
						listmap.put("state", 2);
						listmap.put("pic_state", 1);
					} else if (selectresult.get(0)
							.get("review_result")!=null
							&&Integer.parseInt(selectresult.get(0)
							.get("review_result").toString()) == -1) {
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 0);
						listmap.put("state", 0);
					}else if (selectresult.get(0)
							.get("review_result")!=null
							&&Integer.parseInt(selectresult.get(0)
							.get("review_result").toString()) == -2) {
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 1);
						listmap.put("state", 2);
					} else {
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 0);
						listmap.put("state", 2);
						listmap.put("pic_state", 2);
					}
					}catch(Exception e){
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 0);
						listmap.put("state", 0);
					}
				}else {
					listmap.put("checked", 2);// 可点
					listmap.put("ischange", 0);
					listmap.put("state", 0);
				}
				
				
			}else if ((Sc_MyApplication.getInstance().getClaimidstate() & 8) == 8) {
				if(Integer.parseInt(listmap.get("ischange").toString()) == 1){
					listmap.put("pic_state", 1);
					listmap.put("ischange", 1);
				}
				
				listmap.put("checked", 1);// 不可点
			} else if (sc_GetText.isOnclickBtn(Sc_MyApplication.getInstance()
					.getClaimidstate(), Sc_MyApplication.getInstance()
					.getStepstate())) {
				if(Integer.parseInt(listmap.get("ischange").toString()) == 1){
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
				listmap.put("reviewreason", selectresult.get(0).get("review_reason")
						.toString());
			} catch (Exception e) {
				listmap.put("reviewreason", "");
			}
			if((Sc_MyApplication.getInstance().getClaimidstate() & 131072) == 131072){
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
			stepList.add(listmap);
		}
	}

	/**
	 * 补全单证获取列表数据的信息
	 */

	public void getStep3List() {
		danzhengtext.reverse();
		legendinfo.clear();
		String legendmode = "6,1,1;7,1,1;8,1,1;9,1,0;10,1,0;11,1,0;12,1,0";
		if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
			legendmode = "8,1,1;9,1,1;10,1,1";
		}
		String sql1 = "select certificates from claiminfo where claimid = '"
				+ Sc_MyApplication.getInstance().getClaimid() + "'";
		List<Map<String, Object>> selectresult1 = database.selectRow(sql1, null);
		
		if(selectresult1.size()>0){
			if(selectresult1.get(0).get("certificates")!=null){
				if(selectresult1.get(0).get("certificates").toString().contains(",")){
					legendinfo = changeCertificates(selectresult1.get(0).get("certificates").toString());
				}else{
					legendinfo = changeCertificates(legendmode);
				}
			}else{
				legendinfo = changeCertificates(legendmode);
			}
			
		}else{
			legendinfo = changeCertificates(legendmode);
		}
		if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
			legendinfo = changeCertificates(legendmode);
		}
		if(Sc_MyApplication.getInstance().getSelfHelpFlag()!=1){
			
		}
		
		HashMap<String, Object> listmap = null;
		stepList.clear();
		myprogressbars.clear();
		
		
		for (int i = 0; i < legendinfo.size(); i++) {
			if(legendinfo.get(i).get("paper")!=null&&!legendinfo.get(i).get("paper").equals("")){
				if((Integer.parseInt(legendinfo.get(i).get("paper").toString())&2)==2){
					haszhizhi = true;
				}
			}
			listmap = new HashMap<String, Object>();
			String sql = "select uinfo.savepath"
					+ ",uinfo.legendid,uinfo.claimid,cinfo.type"
					+ ",cinfo.review_result,cinfo.review_reason,cinfo.filename,cinfo.photoid "
					+ "from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type = cinfo.type where uinfo.claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid()
					+ "' and uinfo.legendid = '"
					+ legendinfo.get(i).get("legendid").toString()
					+ "' and cinfo.type = 2";
			List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
					.selectRow(sql, null);
			if (selectresult.size() > 0
					&& selectresult.get(0).get("savepath") != null
					&& !selectresult.get(0).get("savepath").toString().trim()
							.equals("")) {
				file = new File(selectresult.get(0).get("savepath").toString());
				if (file.exists()) {
					listmap.put("savepath", file.getAbsolutePath());
					listmap.put("ischange", 1);
					listmap.put("state", 2);
				} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256
						|| (Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128
						|| (Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64) {
					listmap.put("savepath", R.drawable.sc_icon_default_photo);
					listmap.put("ischange", 1);
					listmap.put("state", 0);
				} else {
					listmap.put("savepath", sc_LegendInfo.getLegendImage(legendinfo
							.get(i).get("legendid").toString()));
					listmap.put("ischange", 0);
					listmap.put("state", 0);
				}

			}else {
				listmap.put("savepath", sc_LegendInfo.getLegendImage(legendinfo
						.get(i).get("legendid").toString()));
				listmap.put("ischange", 0);
				listmap.put("state", 0);
			}

			if ((Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256) {
				if(selectresult.size()>0){
					try{
					if (selectresult.get(0)
							.get("review_result")!=null
							&&Integer.parseInt(selectresult.get(0).get("review_result")
							.toString()) == 1) {
						listmap.put("checked", 1);// 不可点
						listmap.put("ischange", 1);
						listmap.put("state", 2);
						listmap.put("pic_state", 1);
					} else if (selectresult.get(0)
							.get("review_result")!=null
							&&Integer.parseInt(selectresult.get(0)
							.get("review_result").toString()) == -1) {
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 0);
						listmap.put("state", 0);
					}else if (selectresult.get(0)
							.get("review_result")!=null
							&&Integer.parseInt(selectresult.get(0)
							.get("review_result").toString()) == -2) {
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 1);
						listmap.put("state", 2);
					} else {
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 0);
						listmap.put("state", 2);
						listmap.put("pic_state", 2);
					}
					}catch(Exception e){
						listmap.put("checked", 2);// 可点
						listmap.put("ischange", 0);
						listmap.put("state", 0);
					}
				}else {
					listmap.put("checked", 2);// 可点
					listmap.put("ischange", 0);
					listmap.put("state", 0);
				}
				
				
			}else if ((Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128) {
				if(Integer.parseInt(listmap.get("ischange").toString()) == 1){
					listmap.put("pic_state", 1);
					listmap.put("ischange", 1);
				}
				
				listmap.put("checked", 1);// 不可点
			} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64) {
				if(Integer.parseInt(listmap.get("ischange").toString()) == 1){
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
				listmap.put("reviewreason", selectresult.get(0).get("review_reason")
						.toString());
			} catch (Exception e) {
				listmap.put("reviewreason", "");
			}
			if((Sc_MyApplication.getInstance().getClaimidstate() & 131072) == 131072){
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
				e.printStackTrace();
			}
			listmap.put(
					"name",sc_LegendInfo.getLegendText(legendinfo
							.get(i).get("legendid").toString()));
			listmap.put("photofile", Sc_MyApplication.getInstance().getFile());
			listmap.put(
					"legendid",
					Integer.parseInt(legendinfo.get(i).get("legendid")
							.toString()));
			danzhengtext.append(sc_LegendInfo.getLegendText(legendinfo
					.get(i).get("legendid").toString())+",");
			listmap.put("checked", flag?1:2);// 不可点
			stepList.add(listmap);
		}
	}
	
	/**
	 * 顶端的水滴按钮跳转
	 */
	private void historyFlow(int step){
		if (step == 1) {
			if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
				return;
			}
			setLinearLayoutViewLayout(2);
			imgListView.setVisibility(View.GONE);
			imgListView = (GridView) findViewById(R.id.listView);
			stepList.clear();
			getStep2List();
			setViewVisibility(2);
			suoLinear1.setVisibility(View.VISIBLE);
			currentStep.setText("现场拍照");
			if(Sc_MyApplication.getInstance().getStepstate() != 0){
				submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
				submitBtn.setClickable(false);
			}else{
				if(btn_tag1){
					submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2);
					submitBtn.setClickable(true);
				}else{
					submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
					submitBtn.setClickable(false);
				}
			}
			notifyView();
			
		} else if (step == 2) {
			if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
				return;
			}
//			String sql = "select * from claiminfo where claimid = '"
//					+ MyApplication.getInstance().getClaimid() + "'";
//			List<Map<String, Object>> selectresult = database.selectRow(sql, null);
			claim_mode = 1;
//			if (selectresult.get(0).get("claim_mode") == null
//					|| selectresult.get(0).get("claim_mode").toString().equals("")
//					|| selectresult.get(0).get("claim_mode").toString().equals("-1")) {
//				getMode();
//			} else {
//				claim_mode = Integer.parseInt(selectresult.get(0).get("claim_mode")
//						.toString());
//			}
			if((Sc_MyApplication.getInstance().getClaimidstate()&32)==32&&claim_mode ==2){
//				setLinearLayoutViewLayout(step3, stepImgBtn);
				stepList.clear();
				imgListView.setVisibility(View.GONE);
				imgListView = (GridView) findViewById(R.id.listView);
				getStep3List();
				setViewVisibility(2);
				suoLinear1.setVisibility(View.VISIBLE);
				currentStep.setText("补全单证");
				if(Sc_MyApplication.getInstance().getStepstate() != 1){
					submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
					submitBtn.setClickable(false);
				}else{
					if(btn_tag1){
						submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2);
						submitBtn.setClickable(true);
					}else{
						submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
						submitBtn.setClickable(false);
					}
				}
				notifyView();
			}
		} else if (step==3) {
			if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
				return;
			}
			setLinearLayoutViewLayout(3);
			claim_mode =1;
//			String sql1 = "select * from claiminfo where claimid = '"
//					+ MyApplication.getInstance().getClaimid() + "'";
//			List<Map<String, Object>> selectresult1 = database.selectRow(sql1, null);
//			if (selectresult1.get(0).get("claim_mode") == null
//					|| selectresult1.get(0).get("claim_mode").toString().equals("")
//					|| selectresult1.get(0).get("claim_mode").toString().equals("-1")) {
//				getMode();
//			} else {
//				claim_mode = Integer.parseInt(selectresult1.get(0).get("claim_mode")
//						.toString());
//			}
			if (((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64 
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256)
					&& claim_mode == 1) {
				setDanzhengLinear(false);
			} else {
				setDanzhengLinear(true);
			}
			setViewVisibility(3);
			suoLinear1.setVisibility(View.GONE);
			currentStep.setText("赔款信息");
			if (((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64 
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256)
					&& claim_mode == 1) {
				stepList.clear();
				getStep3List();
				LinearLayout linearlay = (LinearLayout) getLayoutInflater().inflate(
						R.layout.sc_item_danzheng, null);
				imgListView = (GridView)linearlay.findViewById(R.id.listDanzhengView);
				int height = Integer.parseInt(getResources().getString(R.string.imageListView_height));
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
						(stepList.size()/2+stepList.size()%2)*height);
				
				imgListView.setLayoutParams(param);
				linearOfDanZheng.removeAllViews();
				linearOfDanZheng.addView(linearlay);
				notifyView();
			}
			String sql = "select * from claiminfo where claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid() + "'";
			List<Map<String, Object>> selectresult = database.selectRow(sql, null);
			if (selectresult!=null&&selectresult.size() >= 0) {
				if(selectresult.get(0).get("claim_overview") != null
						&& !selectresult.get(0).get("claim_overview").equals("")){
					if (selectresult.get(0).get("claim_overview").toString().equals("")) {
						getPrice();
						createMoneyDiv(getClaimoverview(selectresult.get(0)
								.get("claim_overview").toString()));
					}else{
						createMoneyDiv(getClaimoverview(selectresult.get(0)
								.get("claim_overview").toString()));
					}
				}
				try {
					
					money_t1.setText(sc_StringUtils.changeMoney("00.00"
							, selectresult.get(0).get("claim_amount")
							.toString())+"元");
				} catch (Exception e) {
					money_t1.setText("");
				}
				try {
					bank_et1.setText(selectresult.get(0).get("bank_name")
							.toString());
				} catch (Exception e) {
					bank_et1.setHint("如：招商银行深圳市宝安支行");
				}
				try {
					bank_et2.setText(selectresult.get(0).get("account_name")
							.toString());
				} catch (Exception e) {
					bank_et2.setHint("如：张晓枫");
				}
				try {
					bank_et3.setText(selectresult.get(0).get("account_number")
							.toString());
				} catch (Exception e) {
					bank_et3.setHint("如：6222 0123 1234 2344");
				}
				String bankname="如：招商银行";
				try {
					if(selectresult.get(0).get("bankcode")!=null&&!selectresult.get(0).get("bankcode").toString().trim().equals("")){
						if(getBank(
								selectresult.get(0).get("bankcode").toString())!=null
								&&getBank(
										selectresult.get(0).get("bankcode").toString()).size()>0
										&&getBank(selectresult.get(0).get("bankcode").toString())
												.get(0).get("name")!=null){
							bankname = getBank(
									selectresult.get(0).get("bankcode").toString())
									.get(0).get("name").toString();
							bank_code=selectresult.get(0).get("bankcode").toString();
							bank_name=bankname;
							bankZong.setText(bankname);
						}
						
					}
				} catch (Exception e) {
					bankZong.setHint(bankname);
				}
				try {
					phone.setText(selectresult.get(0).get("bankphonenumber")
							.toString());
				} catch (Exception e) {
					phone.setHint("如：13800000000");
				}
				try {
					insuredName = selectresult.get(0).get("insuredname")
							.toString();
				} catch (Exception e) {
					insuredName = "";
				}
				int i = 0;
				try {
					i = Integer.parseInt(selectresult.get(0)
							.get("iscomfirm").toString());
				} catch (Exception e) {
					i = 0;
				}
				if(i == 0){
					cb1.setChecked(false);
				}else{
					cb1.setChecked(true);
				}
			}
			if(Sc_MyApplication.getInstance().getStepstate() != 2){
				bank_et1.setFocusable(false);
				bank_et1.setClickable(false);
				bank_et1.setEnabled(false);
				bank_et2.setFocusable(false);
				bank_et2.setClickable(false);
				bank_et2.setEnabled(false);
				bank_et3.setFocusable(false);
				bank_et3.setClickable(false);
				bank_et3.setEnabled(false);
				
				bankZong.setFocusable(false);
				bankZong.setClickable(false);
				bankZong.setEnabled(false);
				phone.setFocusable(false);
				phone.setClickable(false);
				phone.setEnabled(false);
				money_rb1.setChecked(true);
				money_rb2.setClickable(false);
				submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
				submitBtn.setClickable(false);
				cb1.setClickable(false);
			}else{
				if(btn_tag1){
					submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2);
					submitBtn.setClickable(true);
				}else{
					submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
					submitBtn.setClickable(false);
				}
			}
		} else if (step==4) {
			suoLinear1.setVisibility(View.VISIBLE);
			currentStep.setText("描述");
			step2Btn.setImageResource(R.drawable.sc_dot_ls);
//			step3Btn.setImageResource(R.drawable.dot_dls);
			step4Btn.setImageResource(R.drawable.sc_dot);
			step5Btn.setImageResource(R.drawable.sc_dot);
			img1.setBackgroundResource(R.drawable.sc_line_ls);
//			img2.setBackgroundResource(R.drawable.line_ls);
			img3.setBackgroundResource(R.drawable.sc_line_ls);
			img4.setBackgroundResource(R.drawable.sc_line_ls);
			setLinearLayoutViewLayout(4);
			setViewVisibility(4);
			linearofbtn.setVisibility(View.GONE);
			detailOver.setBackgroundDrawable(null);
			detailTime.setVisibility(View.GONE);
			detailContent.setText(sc_GetText.getMsg1(Sc_MyApplication.getInstance().getClaimidstate()
					, Sc_MyApplication.getInstance().getCaseidstate()));//结案信息内容
			
			if(sc_GetText.isCancle(Sc_MyApplication.getInstance().getClaimidstate()
					, Sc_MyApplication.getInstance().getCaseidstate())){
				if ((Sc_MyApplication.getInstance().getClaimidstate() & 8) == 8) {
					step2Btn.setImageResource(R.drawable.sc_dot_ls);
				} else {
					step2.setClickable(false);
					step2Btn.setImageResource(R.drawable.sc_dot_on);
				}
				if ((Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128) {
//					step3Btn.setImageResource(R.drawable.dot_ls);
				} else {
//					step3.setClickable(false);
//					step3Btn.setImageResource(R.drawable.dot_on);
				}
				if ((Sc_MyApplication.getInstance().getClaimidstate() & 2048) == 2048) {
					step4Btn.setImageResource(R.drawable.sc_dot_ls);
				} else {
					step4.setClickable(false);
					step4Btn.setImageResource(R.drawable.sc_dot_on);
				}
				step5Btn.setImageResource(R.drawable.sc_dot_on);
			}else{
				step2Btn.setImageResource(R.drawable.sc_dot_ls);
//				step3Btn.setImageResource(R.drawable.dot_ls);
				step4Btn.setImageResource(R.drawable.sc_dot_ls);
				step5Btn.setImageResource(R.drawable.sc_dot_dls);
			}
//			stepImgBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == step2 && btn1state == 1) {
			historyFlow(1);
		} /*else if (v == step3 && btn2state == 1) {
			historyFlow(2);
		} */else if (v == step4 && btn3state == 1) {
			historyFlow(3);
		} else if (v == step5 && btn4state == 1) {
			historyFlow(4);
		} else if (v == notifiCationBtn) {
			Intent intent = new Intent(this, Sc_SayActivity.class);
			startActivity(intent);
		}else if(v == notifiBtn){
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
			/*else if (v == suoLinear1 || v == suo1Btn) {
			if (moneyLinear.isShown()) {
				moneyLinear.setVisibility(View.GONE);
				imgListView.setVisibility(View.GONE);
				sureBankLinear.setVisibility(View.GONE);
				suo1Btn.setImageResource(R.drawable.zk01);
				suo2Btn.setImageResource(R.drawable.zk01);
				suo3Btn.setImageResource(R.drawable.zk01);
			} else {
				moneyLinear.setVisibility(View.VISIBLE);
				imgListView.setVisibility(View.GONE);
				sureBankLinear.setVisibility(View.GONE);
				suo1Btn.setImageResource(R.drawable.zk02);
				suo2Btn.setImageResource(R.drawable.zk01);
				suo3Btn.setImageResource(R.drawable.zk01);
			}
		} else if (v == suoLinear2 || v == suo2Btn) {
			if (imgListView.isShown()) {
				moneyLinear.setVisibility(View.GONE);
				imgListView.setVisibility(View.GONE);
				sureBankLinear.setVisibility(View.GONE);
				suo1Btn.setImageResource(R.drawable.zk01);
				suo2Btn.setImageResource(R.drawable.zk01);
				suo3Btn.setImageResource(R.drawable.zk01);
			} else {
				moneyLinear.setVisibility(View.GONE);
				imgListView.setVisibility(View.VISIBLE);
				sureBankLinear.setVisibility(View.GONE);
				suo1Btn.setImageResource(R.drawable.zk01);
				suo2Btn.setImageResource(R.drawable.zk02);
				suo3Btn.setImageResource(R.drawable.zk01);
			}
		} else if (v == suoLinear3 || v == suo3Btn) {
			if (sureBankLinear.isShown()) {
				moneyLinear.setVisibility(View.GONE);
				imgListView.setVisibility(View.GONE);
				sureBankLinear.setVisibility(View.GONE);
				suo1Btn.setImageResource(R.drawable.zk01);
				suo2Btn.setImageResource(R.drawable.zk01);
				suo3Btn.setImageResource(R.drawable.zk01);
			} else {
				moneyLinear.setVisibility(View.GONE);
				imgListView.setVisibility(View.GONE);
				sureBankLinear.setVisibility(View.VISIBLE);
				suo1Btn.setImageResource(R.drawable.zk01);
				suo2Btn.setImageResource(R.drawable.zk01);
				suo3Btn.setImageResource(R.drawable.zk02);
			}
		} */else if (v == submitBtn) {
			if (!sc_NetworkCheck.IsHaveInternet(this)
					&&Sc_MyApplication.getInstance().getSelfHelpFlag() != 1) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("请检查网络是否连入").setPositiveButton("确定", null)
						.show();
			} else if (Sc_MyApplication.getInstance().getClaimidstate() == 4
					|| Sc_MyApplication.getInstance().getClaimidstate() == 8
					|| Sc_MyApplication.getInstance().getClaimidstate() == 8
					|| Sc_MyApplication.getInstance().getClaimidstate() == 64
					|| Sc_MyApplication.getInstance().getClaimidstate() == 128
					|| Sc_MyApplication.getInstance().getClaimidstate() == 1024
					|| Sc_MyApplication.getInstance().getClaimidstate() == 2048
					|| Sc_MyApplication.getInstance().getClaimidstate() == 4096) {
				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("系统正在处理，请稍后查询").setPositiveButton("我知道了", null)
						.show();
			} else {
				submitBtn();
			}

		} else if (v == cancleBtn) {
			
				Intent i = new Intent();
				if (tag == 0) {
					i.putExtra("back_activity", 1);//返回
					i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_CaseListActivity.class);
				} else if (tag == 1) {
					i.putExtra("back_activity", 1);//返回
					i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_CaseInfoActivity.class);
				} else if (tag == 2) {
					i.putExtra("back_activity", 1);//返回
					i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_MessageListActivity.class);
				}
				startActivity(i);
				finish();
		} else if (v == topBackBtn) {
			Intent i = new Intent();
			if (tag == 0) {
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_CaseListActivity.class);
			} else if (tag == 1) {
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_CaseInfoActivity.class);
			} else if (tag == 2) {
				i.putExtra("back_activity", 1);//返回
				i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_MessageListActivity.class);
			}
			startActivity(i);
			finish();
		} else if(v == cb1){
			if (Sc_MyApplication.getInstance().getSelfHelpFlag() == 1) {
				return;
			}
			if(!yanzheng1()){
				cb1.setChecked(false);
			}
		}else if(v == bankZong){
			Intent intent = new Intent(this,Sc_SelectBankActivity.class);
			startActivityForResult(intent, 3);
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
	public void submitBtn() {
		Sc_MyApplication.getInstance().setServer_type(2);
		if(!money_rb2.isChecked()){
			int count=0;
			int count1=0;
			for (int i = 0; i < legendinfo.size(); i++) {
				try {
					if (Integer
							.parseInt(stepList.get(i).get("ischange").toString()) == 0
							&& legendinfo.get(i).get("isupload") == 1) {
						Toast.makeText(
								this,sc_LegendInfo.getLegendText(legendinfo
										.get(i).get("legendid").toString())
										+ "图例必须拍照上传", 2000).show();
						return;
					}
					if(Integer.parseInt(stepList.get(i).get("ischange").toString()) == 0){
						count++;
					}
					try {
						if (Integer.parseInt(stepList.get(i).get("pic_state")
								.toString()) == 1) {
							count1++;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
				}
			}
			if(Sc_MyApplication.getInstance().getStepstate()!=2){
				if(count == legendinfo.size()-count1&&legendinfo.size()>0){
					Toast.makeText(this,"未发现可以上传的图片", 2000).show();
					return;
				}
			}
		}
		
		
		if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
			if (Sc_MyApplication.getInstance().getStepstate() == 0) {
				new AlertDialog.Builder(Sc_MyApplication.getInstance().getContext())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("提示：")
				.setMessage("尊敬的客户：您已成功上传照片，请耐心等待我司审核。")
				.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Sc_MyApplication.getInstance().setClaimidstate(559);
						Sc_MyApplication.getInstance().setCaseidstate(1);
						Intent i = new Intent(Sc_CaseHandlerFlowActivity.this,Sc_CaseListActivity.class);
						startActivity(i);
						finish();
					}
				}).show();
				return;
			}else if (Sc_MyApplication.getInstance().getStepstate() == 1) {
				
				new AlertDialog.Builder(Sc_MyApplication.getInstance().getContext())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("提示：")
				.setMessage("尊敬的客户：您已成功上传所缺单证，请耐心等待我司审核。")
				.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Sc_MyApplication.getInstance().setClaimidstate(Sc_MyApplication.getInstance().getClaimidstate()+128+512);
						Sc_MyApplication.getInstance().setCaseidstate(1);
						Intent i = new Intent(Sc_CaseHandlerFlowActivity.this,Sc_CaseListActivity.class);
						startActivity(i);
						finish();
					}
				}).show();
				return;
			}else if (Sc_MyApplication.getInstance().getStepstate() == 2) {
				new AlertDialog.Builder(Sc_MyApplication.getInstance().getContext())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("提示：")
				.setMessage(money_rb2.isChecked()?this.getResources().getString(R.string.sc_money_submit2):getResources().getString(R.string.sc_money_submit1))
				.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Sc_MyApplication.getInstance().setClaimidstate(Sc_MyApplication.getInstance().getClaimidstate()+
								2048+4096+8192+16384);
						Sc_MyApplication.getInstance().setCaseidstate(1);
						Intent i = new Intent(Sc_CaseHandlerFlowActivity.this,Sc_CaseListActivity.class);
						startActivity(i);
						finish();
					}
				}).show();
				return;
			}else if (Sc_MyApplication.getInstance().getStepstate() == 3) {
//				MyApplication.getInstance().setClaimidstate(MyApplication.getInstance().getClaimidstate()+16384+8);
//				Intent i = new Intent(this,CaseListActivity.class);
//				startActivity(i);
//				finish();
				return;
			}
		}
		
		
		
		ContentValues values = new ContentValues();
		values.put("status", 0);
		boolean b = database.update("uploadinfo", values,
				"claimid = ? and status = ?", new String[] {
						Sc_MyApplication.getInstance().getClaimid(), "-1" });
		String sql = "";
		if (Sc_MyApplication.getInstance().getStepstate() == 0) {
			sql = "select uinfo.*,cinfo.type from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type=cinfo.type where uinfo.claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid()
					+ "' and (uinfo.status =0 or uinfo.status =3) and cinfo.type = 1";
		} else {
			sql = "select uinfo.*,cinfo.type from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type=cinfo.type where uinfo.claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid()
					+ "' and (uinfo.status =0 or uinfo.status =3) and cinfo.type = 2";
		}
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
				.selectRow(sql, null);
		System.out.println("上传图片的数量为："+selectresult.size());
		sc_DBHelper.getInstance().close();
		if (Sc_MyApplication.getInstance().getStepstate() == 0
				|| Sc_MyApplication.getInstance().getStepstate() == 1) {
			if (selectresult.size() > 0) {
				Sc_MyApplication.getInstance().setUploadon(true);
				if(Sc_MyApplication.getInstance().getStepstate() == 0){
					createProgress();
					progressdialog.setMessage("正在上传照片，请稍候…");
					progressdialog.setOnKeyListener(new OnKeyListener() {
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							return true;
						}
					});
				}else{
					createProgress();
					progressdialog.setMessage("正在上传单证照片，请稍候…");
					progressdialog.setOnKeyListener(new OnKeyListener() {
						
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							return true;
						}
					});
				}
				progressdialog.show();
				Sc_MyApplication.getInstance().setUploadon(true);
				Message msg = new Message();
				Bundle data = new Bundle();
				msg.what = -2;
				msg.setData(data);
				sc_MyHandler.getInstance().sendMessage(msg);
			} else {
				new Thread(runnable7).start();
			}
		} else if (Sc_MyApplication.getInstance().getStepstate() == 2) {
			if (((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64 
					|| (Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256)
					&& (Sc_MyApplication.getInstance().getClaimidstate() & 128) != 128
					&& claim_mode == 1) {
				if (!yanzheng()) {
					return;
				}
				createProgress();
				progressdialog.setMessage("正在上传单证照片以及提交索赔申请，请稍候…");
				progressdialog.show();
				isSuccessConfirmBank = 0;
				if (selectresult.size() > 0) {
					ishasFile = true;
				}else{
					ishasFile = false;
				}
				new Thread(runnable6).start();
//				
			} else {
				if (!yanzheng()) {
					return;
				}
				createProgress();
				progressdialog.setMessage("正在提交索赔申请，请稍候…");
				progressdialog.show();
				new Thread(runnable5).start();
			}
		} else if (Sc_MyApplication.getInstance().getStepstate() == 3) {

		}

	}
	
	private boolean ishasFile = true;
	
	private Runnable runnable7 = new Runnable() {
		
		@Override
		public void run() {
				SurveySelf();
		}
	};
	private Runnable runnable6 = new Runnable() {
		
		@Override
		public void run() {
			boolean b = false;
			if((Sc_MyApplication.getInstance().getClaimidstate()&2048)!=2048){
				b = ComfirmBank();
			}
			if(!b){
				if (ishasFile) {
					Sc_MyApplication.getInstance().setUploadon(true);
					Message msg = new Message();
					Bundle data = new Bundle();
					msg.what = -2;
					msg.setData(data);
					sc_MyHandler.getInstance().sendMessage(msg);
				} else {
					SurveySelf();
				}
			}
		}
	};

	private Runnable runnable5 = new Runnable() {

		@Override
		public void run() {
			isSuccessConfirmBank = 1;
			
			if((Sc_MyApplication.getInstance().getClaimidstate()&2048)!=2048){
				ComfirmBank();
			}
		}
	};

	/**
	 * 设置控件的位置
	 * @param step 代表理赔流程步骤
	 * @author  刘星星
	 */
	public void setLinearLayoutViewLayout(int step) {
		/*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				v.getWidth(), v.getHeight());
		params.setMargins(RelativeView.getLeft()+10, RelativeView.getTop()-10, 0, 0);// 通过自定义坐标来放置你的控件
		v.setLayoutParams(params);*/
		if(step == 2){
			stepImgBtn1.setVisibility(View.VISIBLE);
			stepImgBtn2.setVisibility(View.GONE);
			stepImgBtn3.setVisibility(View.GONE);
		}else if(step == 3){
			stepImgBtn2.setVisibility(View.VISIBLE);
			stepImgBtn1.setVisibility(View.GONE);
			stepImgBtn3.setVisibility(View.GONE);
		}else if(step == 4){
			stepImgBtn3.setVisibility(View.VISIBLE);
			stepImgBtn2.setVisibility(View.GONE);
			stepImgBtn1.setVisibility(View.GONE);
		}
		
	}

	public void setLinearLayoutViewLayoutBottom(View RelativeView, View v) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				v.getWidth(), v.getHeight());
		params.setMargins(RelativeView.getLeft(), RelativeView.getTop(), 0, 0);// 通过自定义坐标来放置你的控件
		v.setLayoutParams(params);
	}

	/**
	 * 设置控件的显示和隐藏
	 * 
	 * @param flag
	 *            用来标识应该显示的控件
	 * @author  刘星星
	 */
	public void setViewVisibility(int flag) {
		if (flag == 2) {
			imgListView.setVisibility(View.VISIBLE);
			suopeiLinear.setVisibility(View.GONE);
			detailOver.setVisibility(View.GONE);
//			detailText.setVisibility(View.GONE);
//			suo1Btn.setVisibility(View.GONE);
		} else if (flag == 3) {
			detailOver.setVisibility(View.GONE);
			imgListView.setVisibility(View.GONE);
			suopeiLinear.setVisibility(View.VISIBLE);
//			detailText.setVisibility(View.VISIBLE);
//			suo1Btn.setVisibility(View.VISIBLE);
			moneyLinear.setVisibility(View.VISIBLE);
			sureBankLinear.setVisibility(View.VISIBLE);
			/*suo1Btn.setImageResource(R.drawable.zk02);
			suo2Btn.setImageResource(R.drawable.zk01);
			suo3Btn.setImageResource(R.drawable.zk01);*/
		} else if (flag == 4) {
			imgListView.setVisibility(View.GONE);
			suopeiLinear.setVisibility(View.GONE);
			detailOver.setVisibility(View.VISIBLE);
//			detailText.setVisibility(View.GONE);
//			suo1Btn.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				if (resultCode == RESULT_OK) {
//					imagedata = MyApplication.getInstance().getDataByte();
					
					createProgress();
					/*progressdialog.setMessage("正在加载图片，请稍候。。。");
					progressdialog.show();
					Thread thread = new Thread(lxxRunnable);
					thread.start();*/
					if(bitmap!=null){
						bitmap.recycle();
						bitmap = null;
					}
					Sc_MyApplication.getInstance().setLegendid(legendid);
					Message msg = new Message();
					msg.what = 0;
					lxxHandler.sendMessage(msg);
					Sc_MyApplication.switch_tag = true;
				}
			} else if (requestCode == 2) {
				if(data != null){
				Uri imageUri = data.getData();
				ContentResolver cr = getContentResolver();
				String path = "";
				Cursor s = cr.query(imageUri, null, null, null, null);
				if (s != null) {
					int column_index = s.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					s.moveToFirst(); 
					path = s.getString(column_index);
				} else {
					path = imageUri.getPath();
				}
				InputStream imgIS;
				try {
					imgIS = cr.openInputStream(imageUri);
					try {
						if(imagedata !=null){
							imagedata = null;
							System.gc();
						}
						imagedata = readStream(imgIS);
					} catch (Exception e) {
						AlertDialog.Builder callDailog = new AlertDialog.Builder(Sc_CaseHandlerFlowActivity.this);
						callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示")
								.setMessage("您导入的图片过大，或者图片已删除，请重新选择。").setPositiveButton("我知道了", null)
								.show();
						e.printStackTrace();
					}
					File fi = new File(path);
					Sc_MyApplication.getInstance().setFile(fi);
					imgIS.close();
					createProgress();
					progressdialog.setMessage("正在加载图片，请稍候。。。");
					progressdialog.show();
					Thread thread = new Thread(lxxRunnable);
					thread.start();
				} catch (FileNotFoundException e) {
					AlertDialog.Builder callDailog = new AlertDialog.Builder(
							Sc_CaseHandlerFlowActivity.this);
					callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示")
							.setMessage("您导入的图片过大，或者图片已删除，请重新选择。").setPositiveButton("我知道了", null)
							.show();
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			}else if(requestCode == 3){
				if(data!=null){
					bank_name = data.getStringExtra("bank_name");
					bank_code = data.getStringExtra("bank_code");
					bankZong.setText(bank_name);
				}
			}
		}
	}

	/**
	 * @param bitmap
	 *            拍照得到的照片
	 * @param listPosition
	 *            listData这个集合的一行
	 * @param itemPosition
	 *            listData集合的一行的元素下表 最大值为1
	 * @author  刘星星
	 */
	public void updateListItemData(File file, int listPosition){
		if(file!=null && file.exists()){
			stepList.get(listPosition).put("savepath", file.getAbsolutePath());
			stepList.get(listPosition).put("name", stepList.get(listPosition).get("name"));
			stepList.get(listPosition).put("state", 2);
			stepList.get(listPosition).put("uploadpath",
					stepList.get(listPosition).get("uploadpath"));
			stepList.get(listPosition).put("photoid",
					stepList.get(listPosition).get("photoid"));
			stepList.get(listPosition).put("ischange", 1);
			stepList.get(listPosition).put("reviewreason", stepList.get(listPosition).get("reviewreason"));
			stepList.get(listPosition).put("checked",
					stepList.get(listPosition).get("checked"));
			stepList.get(listPosition).put("photofile", Sc_MyApplication.getInstance().getFile());
			stepList.get(listPosition).put("legendid",
					stepList.get(listPosition).get("legendid"));
			stepList.get(listPosition).remove("pic_state");
		}
	}
	
	/**
	 * @param bitmap
	 *            拍照得到的照片
	 * @param listPosition
	 *            listData这个集合的一行
	 * @param itemPosition
	 *            listData集合的一行的元素下表 最大值为1
	 * @author  刘星星
	 */
	public void updateListItemData(String file, int listPosition,
			int itemPosition){
			stepList.get(listPosition).put("savepath", file);
			for(int i = 0;i<legendinfo.size();i++){
				if(legendinfo.get(i).get("legendid")==legendid && legendinfo.get(i).get("isupload") == 1){
					stepList.get(listPosition).put("pic_state", 4);
				}
			}
			stepList.get(listPosition).put("name", stepList.get(listPosition).get("name"));
			stepList.get(listPosition).put("state", 0);
			stepList.get(listPosition).put("uploadpath",stepList.get(listPosition).get("uploadpath"));
			stepList.get(listPosition).put("photoid",stepList.get(listPosition).get("photoid"));
			stepList.get(listPosition).put("ischange", 0);
			stepList.get(listPosition).put("reviewreason",stepList.get(listPosition).get("reviewreason"));
			stepList.get(listPosition).put("checked",stepList.get(listPosition).get("checked"));
			stepList.get(listPosition).put("photofile", Sc_MyApplication.getInstance().getFile());
			stepList.get(listPosition).put("legendid",stepList.get(listPosition).get("legendid"));
			stepList.get(listPosition).remove("pic_state");
	}
/*
	*//**
	 * 用来处理拍摄得到的照片 包括压缩和打水印
	 * 
	 * @author  刘星星
	 */
	public Runnable lxxRunnable = new Runnable() {
		@Override
		public void run() {
			if(bitmap!=null){
				bitmap.recycle();
				bitmap = null;
			}
			Sc_MyApplication.getInstance().setLegendid(legendid);
			sc_PicHandler ph = new sc_PicHandler(imagedata);
			bitmap = ph.handlePic();
			Message msg = new Message();
			msg.what = 0;
			lxxHandler.sendMessage(msg);
			Sc_MyApplication.switch_tag = true;
		}
	};

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
	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.flush();
		outStream.close();
		inStream.close();
		return data;
	}

	/**
	 * 图片处理完后用来改变手机ＵＩ
	 * 
	 * @author  刘星星
	 */
	public Handler lxxHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if(Sc_MyApplication.getInstance().getFile()!=null && Sc_MyApplication.getInstance().getFile().exists()){
					updateListItemData(Sc_MyApplication.getInstance().getFile(), listPosition);
					notifyAdapter();
					progressdialog.dismiss();
					imgListView.setSelection(listPosition);
				}else{
					Toast.makeText(Sc_CaseHandlerFlowActivity.this, "抱歉，图片处理失败，请重新拍摄。。", 5000).show();
				}
				sc_BitmapCache.getInstance().clearCache();
			}
			super.handleMessage(msg);
		}

	};

	
	@Override
	protected void onDestroy() {
		unregisterReceiver(callReceiver);
		sc_UploadPhotoFile.isstop = true;
		Sc_MyApplication.getInstance().setUploadon(false);
		if(Sc_CaseHandlerFlowActivity.myprogressbars!= null){
			ArrayList<HashMap<String, Object>> myprogressbars = Sc_CaseHandlerFlowActivity.myprogressbars;
			//System.out.println(myprogressbars.size());
			for (int i = 0; i < myprogressbars.size(); i++) {
					ImageView imageview = (ImageView) myprogressbars.get(i).get(
							"image");
					imageview.destroyDrawingCache();
			}
		}
		if(imgListView !=null){
			 int count = imgListView.getChildCount();
		     ImageView v2 = null;
		     for (int i = 0; i < count; i++) {
		        v2 = (ImageView) imgListView.getChildAt(i).findViewById(R.id.image2);
		        if(v2!=null){
		        	((BitmapDrawable) v2.getDrawable()).setCallback(null);
		        }
		     }
		}
		if(stepList!=null){
			stepList.clear();
			stepList=null;
		}
		if(stepList!=null){
			stepList.clear();
			stepList=null;
		}
		if(stepList!=null){
			stepList.clear();
			stepList=null;
		}
		if (imagedata != null) {
			imagedata = null;
		}
		if(timer!=null){
			timer.cancel();
			timer = null;
		}
		if(legendinfo!=null){
			legendinfo.clear();
			legendinfo = null;
		}
		if(danzhengtext!=null){
			danzhengtext.reverse();
			danzhengtext = null;
		}
		if(bitmap!=null){
			bitmap.recycle();
			bitmap = null;
		}
		mLocationClient.stop();
		System.gc();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Sc_MyApplication.getInstance().setServer_type(2);
		super.onResume();
	}

	/**
	 * 
	 * @param legendid图例编号
	 *            更新进度条
	 */

	public void updateProgressBar(String legendid_, int position) {
		ArrayList<HashMap<String, Object>> myprogressbars = null;
			myprogressbars = Sc_CaseHandlerFlowActivity.myprogressbars;
		for (int i = 0; i < myprogressbars.size(); i++) {
			String mylegendid = myprogressbars.get(i).get("legendid")
					.toString();
			if (mylegendid.equals(legendid_)) {
				ProgressBar progressbar = (ProgressBar) myprogressbars.get(i)
						.get("progressbar");
				progressbar.setProgress(position);
				Sc_MyApplication.getInstance().setCurrentprogressbar(position);
				progressbar.setMax(Sc_MyApplication.getInstance().getProgressbarmax());
				TextView numberupload = (TextView) myprogressbars.get(i)
						.get("numberupload");
				// 创建一个数值格式化对象
				NumberFormat numberFormat = NumberFormat.getInstance();
				// 设置精确到小数点后2位
				numberFormat.setMaximumFractionDigits(0);
				String result = numberFormat.format((float)position/(float)Sc_MyApplication.getInstance().getProgressbarmax()*100);
				numberupload.setText(result+"%");
				break;
			}
		}
	}

	/**
	 * 
	 * @param legendid图例编号
	 *            显示进度条
	 */
	public void startProgressBar(String legendid_, int length) {
		ArrayList<HashMap<String, Object>> myprogressbars = null;
		myprogressbars = Sc_CaseHandlerFlowActivity.myprogressbars;
		int i;
		for (i = 0; i < myprogressbars.size(); i++) {
			String mylegendid = myprogressbars.get(i).get("legendid")
					.toString();
			if (mylegendid.equals(legendid_)) {
				ProgressBar progressbar = (ProgressBar) myprogressbars.get(i)
						.get("progressbar");
				progressbar.setMax(length);
				progressbar.setProgress(0);
				progressbar.setVisibility(View.VISIBLE);
				TextView numberupload = (TextView) myprogressbars.get(i)
						.get("numberupload");
				
				numberupload.setText("0%");
//				progressbar.getProgressDrawable().setAlpha(50);
				ImageView imageview = (ImageView) myprogressbars.get(i).get("image");
				imageview.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						ContentValues values = new ContentValues();
						values.put("status", 0);
						listPosition = v.getId();
						legendid = (Integer) stepList.get(listPosition).get("legendid");
						database.update("uploadinfo", values,
								"claimid = ? and legendid <> ? and status = ?",
								new String[] {Sc_MyApplication.getInstance()
												.getClaimid(), legendid + "",
										"-1" });
//						new Thread(runnable).start();

						updateListItemData(1, listPosition);

						int state = (Integer) stepList.get(listPosition).get("state");
						//System.out.println("你点击了图例" + legendid + "状态为" + state);
						String file = stepList.get(listPosition).get("savepath").toString();
						try {
							showQianDaoDialog(state, file);
						} catch (Exception e) {

						}
					}
				});
			}
			break;
		}
		
		for (int j = 0; j < stepList.size(); j++) {
			String mylegendid = stepList.get(j).get("legendid")
					.toString();
			if (mylegendid.equals(legendid_)) {
				imgListView.setSelection(j);
			}
		}
	}

	

	/**
	 * 
	 * @param legendid图例编号
	 *            关闭进度条
	 */

	public void closeProgressBar(String legendid_) {
		ArrayList<HashMap<String, Object>> myprogressbars = null;
		myprogressbars = Sc_CaseHandlerFlowActivity.myprogressbars;
		for (int i = 0; i < myprogressbars.size(); i++) {
			String mylegendid = myprogressbars.get(i).get("legendid")
					.toString();
			if (mylegendid.equals(legendid_)) {
				ProgressBar progressbar = (ProgressBar) myprogressbars.get(i)
						.get("progressbar");
				progressbar.setVisibility(View.GONE);
				TextView numberupload = (TextView) myprogressbars.get(i)
						.get("numberupload");
				ImageView imageview = (ImageView) myprogressbars.get(i).get(
						"image");
				imageview.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						ContentValues values = new ContentValues();
						values.put("status", 0);
						listPosition = v.getId();
						legendid = (Integer) stepList.get(listPosition).get("legendid");
						boolean b = database.update("uploadinfo", values,
								"claimid = ? and legendid <> ? and status = ?",
								new String[] {
										Sc_MyApplication.getInstance()
												.getClaimid(), legendid + "",
										"-1" });
//						new Thread(runnable).start();

						updateListItemData(2, listPosition);

						int state = (Integer) stepList.get(listPosition).get("state");
						String file =stepList.get(listPosition).get("savepath").toString();
						try {
							showQianDaoDialog(state, file);
						} catch (Exception e) {
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
		System.gc();
		stepList.get(listPosition).put("savepath", stepList.get(listPosition).get("savepath"));
		stepList.get(listPosition).put("name", stepList.get(listPosition).get("name"));
		stepList.get(listPosition).put("state", state);
		stepList.get(listPosition).put("uploadpath",
				stepList.get(listPosition).get("uploadpath"));
		stepList.get(listPosition).put("photoid",
				stepList.get(listPosition).get("photoid"));
		stepList.get(listPosition).put("ischange",
				stepList.get(listPosition).get("ischange"));
		stepList.get(listPosition).put("checked",
				stepList.get(listPosition).get("checked"));
		stepList.get(listPosition).put("photofile",
				stepList.get(listPosition).get("photofile"));
		stepList.get(listPosition).put("legendid",
				stepList.get(listPosition).get("legendid"));
		stepList.get(listPosition).put("reviewreason", stepList.get(listPosition).get("reviewreason"));
		stepList.get(listPosition).remove("pic_state");
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
			Intent it = new Intent(Sc_CaseHandlerFlowActivity.this,
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
				Intent i = new Intent(Sc_CaseHandlerFlowActivity.this,
						Sc_MyCameraActivity.class);
				Sc_CaseHandlerFlowActivity.this.startActivityForResult(i, 1);
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
				Sc_CaseHandlerFlowActivity.this.startActivityForResult(i, 2);
				alertDialog.dismiss();
			}
		});
		rb3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				ArrayList<String> al = new ArrayList<String>();
				al.clear();
				al.add(file);
				Sc_MyApplication.getInstance().setLegendid(legendid);
				Intent it = new Intent(Sc_CaseHandlerFlowActivity.this,
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


	public void setDanzhengLinear(boolean flag) {// true 隐藏，false 显示
		if (flag) {
			relativeDanzheng.setVisibility(View.GONE);
			linearOfDanZheng.setVisibility(View.GONE);
			imgListView.setVisibility(View.GONE);
		} else {
			linearOfDanZheng.setVisibility(View.VISIBLE);
			imgListView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 进入现场拍照流程
	 */
	public void flow1Access() {
		suoLinear1.setVisibility(View.VISIBLE);
		stepList.clear();
		getStep2List();
		stepList = stepList;
		imgListView = (GridView) findViewById(R.id.listView);
		setViewVisibility(2);
		currentStep.setText("现场拍照");
		step2Btn.setImageResource(R.drawable.sc_dot_dls);
//		step3Btn.setImageResource(R.drawable.dot);
		step4Btn.setImageResource(R.drawable.sc_dot);
		step5Btn.setImageResource(R.drawable.sc_dot);
		img1.setBackgroundResource(R.drawable.sc_line_ls);
//		img2.setBackgroundResource(R.drawable.line);
		img3.setBackgroundResource(R.drawable.sc_line);
		img4.setBackgroundResource(R.drawable.sc_line);
		setLinearLayoutViewLayout(2);
		if((Sc_MyApplication.getInstance().getClaimidstate() & 131072) == 131072){
			btn_tag1 = false;
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			submitBtn.setClickable(false);
		}
		if((Sc_MyApplication.getInstance().getClaimidstate() & 16) == 16){
			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					Sc_CaseHandlerFlowActivity.this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("提示")
					.setMessage("尊敬的客户：您的部分现场照片未审核通过，请您将标记为“重拍”的照片重新拍摄上传。").setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
			
		}else if ((Sc_MyApplication.getInstance().getClaimidstate() & 8) == 8) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					Sc_CaseHandlerFlowActivity.this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("提示")
					.setMessage("尊敬的客户：您的现场照片已审核通过，可驶离现场。稍后，我司将使用本系统通知您后续理赔事宜，请注意查收。").setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			btn_tag1 = false;
			submitBtn.setClickable(false);

		} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 4) == 4) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(
					Sc_MyApplication.getInstance().getContext());
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("提示")
					.setMessage("尊敬的客户：您已成功上传照片，请耐心等待我司审核。")
					.setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			btn_tag1 = false;
			submitBtn.setClickable(false);
		}
		notifyView();
//		stepImgBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * 进入补充单证流程
	 */
	public void flow2Access() {
		if(stepList==null){
			return;
		}
		suoLinear1.setVisibility(View.VISIBLE);
		stepList.clear();
		getStep3List();
		imgListView = (GridView) findViewById(R.id.listView);
		currentStep.setText("补充单证");
		step2Btn.setImageResource(R.drawable.sc_dot_ls);
//		step3Btn.setImageResource(R.drawable.dot_dls);
		step4Btn.setImageResource(R.drawable.sc_dot);
		step5Btn.setImageResource(R.drawable.sc_dot);
		img1.setBackgroundResource(R.drawable.sc_line_ls);
//		img2.setBackgroundResource(R.drawable.line_ls);
		img3.setBackgroundResource(R.drawable.sc_line);
		img4.setBackgroundResource(R.drawable.sc_line);
//		setLinearLayoutViewLayout(step3, stepImgBtn);
		setViewVisibility(2);
		if((Sc_MyApplication.getInstance().getClaimidstate() & 131072) == 131072){
			btn_tag1 = false;
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			submitBtn.setClickable(false);
		}
		if ((Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("提示")
			.setMessage("尊敬的客户：您的部分单证照片未审核通过，请您将标记为“重拍”的照片重新拍摄上传。")
			.setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
				notifyView();
		}else if ((Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示")
					.setMessage("尊敬的客户：您补传的单证已审核通过，我司将尽快为您核定保险赔款，请注意查收并确认。")
					.setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			btn_tag1 = false;
			submitBtn.setClickable(false);
		} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64) {
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("提示")
			.setMessage("尊敬的客户：您已成功上传所缺单证，请耐心等待我司审核。")
			.setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
			btn_tag1 = false;
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			submitBtn.setClickable(false);
		}else if((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32){
			String msg = "";
			String danzheng="";
			try {
				danzheng = danzhengtext.substring(0,
						danzhengtext.length() - 1);
			} catch (Exception e) {
				danzheng = "相关单证";
			}
			if(haszhizhi){
				msg = "尊敬的客户：您的索赔单证缺少"+danzheng+",如果您需要申请索赔，请携带以上单证前往我司理赔网点办理。";
			}else{
				msg ="尊敬的客户：您的索赔单证中缺少"+danzheng+"，如果您需要我司赔付，请您于3个工作日内使用本系统拍照补传；超过3个工作日，赔案将在本系统中自动取消，届时请携带所缺单证前往我司理赔网点办理。";
			}
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("提示")
			.setMessage(msg)
			.setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
		}
		notifyView();
//		stepImgBtn.setVisibility(View.VISIBLE);
		if(tag == 2&&Sc_MyApplication.getInstance().getSelfHelpFlag() != 1){
			claimstate_pre = getIntent().getIntExtra("claimstate", 0);
			if(claimstate_pre>=Sc_MyApplication.getInstance().getClaimidstate()){
				Sc_MyApplication.getInstance().setClaimidstate(claimstate_pre);
				return;
			}else{
				historyFlow(sc_GetText.pareStatus(claimstate_pre)+1);
			}
		}
	}

	/**
	 * 进入索赔流程
	 */
	public void flow3Access() {
		if(stepList==null){
			return;
		}
		suoLinear1.setVisibility(View.GONE);
		if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
			currentStep.setText("赔款信息");
			step2Btn.setImageResource(R.drawable.sc_dot_ls);
//			step3Btn.setImageResource(R.drawable.dot_ls);
			step4Btn.setImageResource(R.drawable.sc_dot_dls);
			step5Btn.setImageResource(R.drawable.sc_dot);
			img1.setBackgroundResource(R.drawable.sc_line_ls);
//			img2.setBackgroundResource(R.drawable.line_ls);
			img3.setBackgroundResource(R.drawable.sc_line_ls);
			img4.setBackgroundResource(R.drawable.sc_line);
			money_et.setVisibility(View.GONE);// 初始化不同意金额原因控件为隐藏
			spReason.setVisibility(View.GONE);
			setLinearLayoutViewLayout(3);
			setViewVisibility(3);
			setDanzhengLinear(false);
			stepList.clear();
			stepList.clear();
			getStep3List();
			LinearLayout linearlay = (LinearLayout) getLayoutInflater().inflate(
					R.layout.sc_item_danzheng, null);
			imgListView = (GridView)linearlay.findViewById(R.id.listDanzhengView);
			linearOfDanZheng.removeAllViews();
			int height = Integer.parseInt(getResources().getString(R.string.imageListView_height));
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
					(stepList.size()/2+stepList.size()%2)*height);
			
			imgListView.setLayoutParams(param);
			linearOfDanZheng.addView(linearlay);
			notifyView();
			createMoneyDiv(getClaimoverview("前杠拆装@@150元%%前杠修复费用@@150元%%前杠喷漆费用@@700元"));
			money_t1.setText("1000元");
			bank_et1.setText("建设银行北京分行");
			bank_et2.setText("张三");
			bank_et3.setText("888888888888");
			bankZong.setText("建设银行");
			phone.setText("13800000000");
			bank_et1.setFocusable(false);
			bank_et1.setClickable(false);
			bank_et1.setEnabled(false);
			bank_et2.setFocusable(false);
			bank_et2.setClickable(false);
			bank_et2.setEnabled(false);
			bank_et3.setFocusable(false);
			bank_et3.setClickable(false);
			bank_et3.setEnabled(false);
			
			bankZong.setFocusable(false);
			bankZong.setClickable(false);
			bankZong.setEnabled(false);
			phone.setFocusable(false);
			phone.setClickable(false);
			phone.setEnabled(false);
//			stepImgBtn.setVisibility(View.VISIBLE);
			moneyLinear.setVisibility(View.VISIBLE);
			imgListView.setVisibility(View.VISIBLE);
			sureBankLinear.setVisibility(View.VISIBLE);
			suoLinear1.setVisibility(View.GONE);
//			suo1Btn.setImageResource(R.drawable.zk02);
//			suo2Btn.setImageResource(R.drawable.zk01);
//			suo3Btn.setImageResource(R.drawable.zk01);
			return;
		}
		if (((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32
				|| (Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64 
				|| (Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128
				|| (Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256)
				&& claim_mode == 1) {
			setDanzhengLinear(false);
		} else {
			setDanzhengLinear(true);
		}
		currentStep.setText("赔款信息");
		step2Btn.setImageResource(R.drawable.sc_dot_ls);
//		step3Btn.setImageResource(R.drawable.dot_ls);
		step4Btn.setImageResource(R.drawable.sc_dot_dls);
		step5Btn.setImageResource(R.drawable.sc_dot);
		img1.setBackgroundResource(R.drawable.sc_line_ls);
//		img2.setBackgroundResource(R.drawable.line_ls);
		img3.setBackgroundResource(R.drawable.sc_line_ls);
		img4.setBackgroundResource(R.drawable.sc_line);
		money_et.setVisibility(View.GONE);// 初始化不同意金额原因控件为隐藏
		spReason.setVisibility(View.GONE);
		setLinearLayoutViewLayout(3);
		boolean btn_tag = false;
//		int hasdanzhen = 0;//0不缺少单证1为缺少单证，2为单证不合格，3单证合格
		if ((Sc_MyApplication.getInstance().getClaimidstate() & 4096) == 4096) {
			if(!btn_tag)
				btn_tag = false;
		} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 2048) == 2048) {
			if(!btn_tag)
				btn_tag = false;
		} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 1024) == 1024) {
			if(!btn_tag)
				btn_tag = false;
		} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 512) == 512) {
			if(!btn_tag)
				btn_tag = true;
		}
		if (((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32
				|| (Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64 
				|| (Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128
				|| (Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256)
				&& claim_mode == 1) {
			stepList.clear();
			getStep3List();
			stepList = stepList;
			LinearLayout linearlay = (LinearLayout) getLayoutInflater().inflate(
					R.layout.sc_item_danzheng, null);
			imgListView = (GridView)linearlay.findViewById(R.id.listDanzhengView);
			linearOfDanZheng.removeAllViews();
			int height = Integer.parseInt(getResources().getString(R.string.imageListView_height));
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 
					(stepList.size()/2+stepList.size()%2)*height);
			
			imgListView.setLayoutParams(param);
			linearOfDanZheng.addView(linearlay);
			if ((Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256) {
				notifyView();
				if(!btn_tag)
					btn_tag = true;
			} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128) {
				notifyView();
			} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64) {
				notifyView();
				if(!btn_tag)
					btn_tag = false;
				
			} else if ((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32) {
				if(!btn_tag)
					if((Sc_MyApplication.getInstance().getClaimidstate() & 1024)!=1024){
						btn_tag = true;
					}
				notifyView();
			}
		}
		setViewVisibility(3);
		String msg = "";
		String sql = "select * from claiminfo where claimid = '"
				+ Sc_MyApplication.getInstance().getClaimid() + "'";
		List<Map<String, Object>> selectresult = database.selectRow(sql, null);
		String moneytext = "";
		if((Sc_MyApplication.getInstance().getClaimidstate() & 131072) == 131072){
			btn_tag = false;
			submitBtn.setClickable(false);
		}
		if(btn_tag){
			try {
				moneytext = sc_StringUtils.changeMoney("00.00"
						, selectresult.get(0).get("claim_amount")
						.toString());
			} catch (Exception e) {
				moneytext="0";
			}
			if(Sc_MyApplication.getInstance().selfHelpFlag == 1){
				moneytext = "1000";
			}
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2);
			submitBtn.setClickable(true);
		}else{
			btn_tag1 = false;
			submitBtn.setBackgroundResource(R.drawable.sc_aj_btn2_gray);
			submitBtn.setClickable(false);
			spReason.setEnabled(false);
			money_et.setEnabled(false);
			money_rb1.setClickable(false);
			money_rb2.setClickable(false);
			bank_et1.setEnabled(false);
			bank_et2.setEnabled(false);
			bank_et3.setEnabled(false);
			bankZong.setEnabled(false);
			bankZong.setClickable(false);
			phone.setEnabled(false);
			cb1.setClickable(false);
		}
		if((Sc_MyApplication.getInstance().getClaimidstate()&16384)==16384){
			msg = "尊敬的客户：您的索赔单证齐全、有效，且同意我司核定赔款。稍后，我司将按此金额向您所提供的账户信息支付赔款。支付成功后，我司关于本事故之一切赔偿责任终了。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&2048)==2048
				&&(Sc_MyApplication.getInstance().getClaimidstate()&32)!=32){
			msg = "尊敬的客户：您的索赔单证齐全、有效，且同意我司核定赔款。稍后，我司将按此金额向您所提供的账户信息支付赔款。支付成功后，我司关于本事故之一切赔偿责任终了。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&1024)==1024
				&&(Sc_MyApplication.getInstance().getClaimidstate()&32)!=32){
			msg = "尊敬的客户：您对我司核定赔款存在异议，请耐心等待我司复核。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&2048)!=2048
				&&(Sc_MyApplication.getInstance().getClaimidstate()&256)==256){
			msg = "尊敬的客户:您的部分单证照片未审核通过，请您将标记为“重拍”的照片重新拍摄上传。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&1024)==1024
				&&(Sc_MyApplication.getInstance().getClaimidstate()&32)==32
						&&(Sc_MyApplication.getInstance().getClaimidstate()&256)!=256
				&&(Sc_MyApplication.getInstance().getClaimidstate()&64)==64){
			msg = "尊敬的客户：您已成功上传单证照片，但对我司核定赔款存在异议，请耐心等待我司复核。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&1024)==1024
				&&(Sc_MyApplication.getInstance().getClaimidstate()&32)==32
				&&(Sc_MyApplication.getInstance().getClaimidstate()&256)!=256
				&&(Sc_MyApplication.getInstance().getClaimidstate()&64)!=64){
			msg = "尊敬的客户：您对我司核定赔款存在异议，请耐心等待我司复核。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&2048)==2048
				&&(Sc_MyApplication.getInstance().getClaimidstate()&32)==32
				&&(Sc_MyApplication.getInstance().getClaimidstate()&256)!=256
				&&(Sc_MyApplication.getInstance().getClaimidstate()&64)==64){
			msg = "尊敬的客户：您已同意我司核定赔款，且成功上传单证照片，请耐心等待我司审核。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&512)!=512
				&&(Sc_MyApplication.getInstance().getClaimidstate()&1024)!=1024
				&&(Sc_MyApplication.getInstance().getClaimidstate()&256)==256){
			msg = "尊敬的客户：您的部分单证照片未审核通过，请您将标记为“重拍”的照片重新拍摄上传。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&512)==512
				&&(Sc_MyApplication.getInstance().getClaimidstate()&1024)!=1024
				&&(Sc_MyApplication.getInstance().getClaimidstate()&256)==256){
			msg = "尊敬的客户：您的部分单证照片未审核通过，请您将标记为“重拍”的照片重新拍摄上传。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&512)!=512
				&&(Sc_MyApplication.getInstance().getClaimidstate()&1024)!=1024
				&&(Sc_MyApplication.getInstance().getClaimidstate()&128)==128){
			msg = "尊敬的客户：您的单证照片已审核通过，请耐心等待我司重新核定赔款。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&512)==512
				&&(Sc_MyApplication.getInstance().getClaimidstate()&1024)!=1024
				&&(Sc_MyApplication.getInstance().getClaimidstate()&128)==128){
			if(claim_mode ==1){
				msg = "尊敬的客户：您的单证照片已审核通过，请您确认事故赔款。";
				
			}else{
				msg = "尊敬的客户：您的索赔单证齐全、有效。经我司核定，本事故赔款为"+moneytext+"元。如果您同意，我司将按此金额向您所提供的账户信息支付赔款。支付成功后，我司关于本事故之一切赔偿责任终了。";
			}
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&512)==512
				&&(Sc_MyApplication.getInstance().getClaimidstate()&32)!=32){
			msg = "尊敬的客户：您的索赔单证齐全、有效。经我司核定，本事故赔款为"+sc_StringUtils.changeMoney("00.00"
					, selectresult.get(0).get("claim_amount")
					.toString())+"元。如果您同意，我司将按此金额向您所提供的账户信息支付赔款。支付成功后，我司关于本事故之一切赔偿责任终了。";
		}else if((Sc_MyApplication.getInstance().getClaimidstate()&512)==512
				&&(Sc_MyApplication.getInstance().getClaimidstate()&32)==32
						&&(Sc_MyApplication.getInstance().getClaimidstate()&128)!=128
				&&(Sc_MyApplication.getInstance().getClaimidstate()&256)!=256){
			String danzheng="";
			try {
				danzheng = danzhengtext.substring(0, danzhengtext.length() - 1);
			} catch (Exception e) {
				danzheng = "相关单证";
			}
			if(haszhizhi){
				
				msg = "尊敬的客户：经我司核定，本事故赔款为"+sc_StringUtils.changeMoney("00.00"
						, selectresult.get(0).get("claim_amount")
						.toString())+"元。但索赔单证缺少"+danzheng+"，如果您对我司核定赔款无异议，且需要申请索赔，请携带以上单证择时前往我司理赔网点办理。";
			}else{
				msg = "尊敬的客户：经我司核定，本事故赔款为"+sc_StringUtils.changeMoney("00.00"
						, selectresult.get(0).get("claim_amount")
						.toString())+"元，但索赔单证缺少"+danzheng+"。如果您对我司核定赔款无异议，且需要申请索赔，请您于3个工作日内使用本系统拍照补传，届时我司将向您所提供的账户信息支付赔款；超过3个工作日，赔案将在本系统中自动取消，届时请携带上述单证前往我司理赔网点办理。";
			}
		}
		
		if(claim_mode == 2){
			if((Sc_MyApplication.getInstance().getClaimidstate()&1024) == 1024){
				msg = "尊敬的客户：您对我司核定赔款存在异议，请耐心等待我司复核。";
			}else if((Sc_MyApplication.getInstance().getClaimidstate()&2048) == 2048){
				msg = "尊敬的客户：您的索赔单证齐全、有效，且同意我司核定赔款。稍后，我司将按此金额向您所提供的账户信息支付赔款。支付成功后，我司关于本事故之一切赔偿责任终了。";
			}
			
		}
	
		if(!msg.toString().equals("")){
			AlertDialog.Builder callDailog = new AlertDialog.Builder(Sc_MyApplication.getInstance().getContext());
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage(msg.toString())
					.setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
		}
		
		
		if (selectresult!=null&&selectresult.size() >= 0) {
			try {
				
				money_t1.setText(sc_StringUtils.changeMoney("00.00"
						, selectresult.get(0).get("claim_amount")
						.toString())+"元");
			} catch (Exception e) {
				money_t1.setText("");
			}
			try {
				bank_et1.setText(selectresult.get(0).get("bank_name")
						.toString());
			} catch (Exception e) {
				bank_et1.setText("");
			}
			try {
				bank_et2.setText(selectresult.get(0).get("account_name")
						.toString());
			} catch (Exception e) {
				bank_et2.setText("");
			}
			try {
				bank_et3.setText(sc_StringUtils.format("#### ", selectresult.get(0).get("account_number")
						.toString().replace(" ", "")));
			} catch (Exception e) {
				bank_et3.setText("");
			}
			try {
				bank_et3.setText(sc_StringUtils.format("#### ", selectresult.get(0).get("account_number")
						.toString().replace(" ", "")));
			} catch (Exception e) {
				bank_et3.setText("");
			}
			String bankname="";
			try {
				bankname = getBank(
						selectresult.get(0).get("bankcode").toString())
						.get(0).get("name").toString();
				bank_code=selectresult.get(0).get("bankcode").toString();
				bank_name=bankname;
			} catch (Exception e) {
			}
			bankZong.setText(bankname);
			try {
				phone.setText(selectresult.get(0).get("bankphonenumber")
						.toString());
			} catch (Exception e) {
				phone.setText("");
			}
			try {
				insuredName=selectresult.get(0).get("insuredname")
						.toString();
			} catch (Exception e) {
				insuredName="";
			}
			int i = 0;
			try {
				i = Integer.parseInt(selectresult.get(0)
						.get("iscomfirm").toString());
			} catch (Exception e) {
				i = 0;
			}
			if(i == 0){
				cb1.setChecked(false);
			}else{
				cb1.setChecked(true);
			}
			if(selectresult.get(0).get("claim_overview") != null
					&& !selectresult.get(0).get("claim_overview").equals("")){
				if (selectresult.get(0).get("claim_overview").toString().equals("")) {
					getPrice();
					createMoneyDiv(getClaimoverview(selectresult.get(0)
							.get("claim_overview").toString()));
				}else{
					createMoneyDiv(getClaimoverview(selectresult.get(0)
							.get("claim_overview").toString()));
				}
			}
			if((Sc_MyApplication.getInstance().getClaimidstate()&1024)==1024){
				try {
					money_et.setText(selectresult.get(0).get("disagreereason")
							.toString());
				} catch (Exception e) {
					money_et.setText("");
				}
				money_et.setVisibility(View.VISIBLE);
				try {
					spReason.setSelection(Integer.parseInt(selectresult.get(0).get("disagreereasonid")
							.toString()));
				} catch (Exception e) {
					spReason.setSelection(0);
				}
				spReason.setVisibility(View.VISIBLE);
				money_rb2.setChecked(true);
			}else if((Sc_MyApplication.getInstance().getClaimidstate()&2048)==2048){
				spReason.setClickable(false);
				spReason.setVisibility(View.GONE);
				money_et.setVisibility(View.GONE);
				money_rb1.setChecked(true);
				money_rb2.setClickable(false);
				bank_et1.setEnabled(false);
				bank_et2.setEnabled(false);
				bank_et3.setEnabled(false);
				bankZong.setEnabled(false);
				bankZong.setClickable(false);
				phone.setEnabled(false);
				cb1.setClickable(false);
				cb1.setChecked(true);
			}
		}
//		stepImgBtn.setVisibility(View.VISIBLE);
		moneyLinear.setVisibility(View.VISIBLE);
		imgListView.setVisibility(View.VISIBLE);
		sureBankLinear.setVisibility(View.VISIBLE);
//		suo1Btn.setImageResource(R.drawable.zk02);
//		suo2Btn.setImageResource(R.drawable.zk01);
//		suo3Btn.setImageResource(R.drawable.zk01);
		if(tag == 2&&Sc_MyApplication.getInstance().getSelfHelpFlag() != 1){
			claimstate_pre = getIntent().getIntExtra("claimstate", 0);
			if(claimstate_pre>Sc_MyApplication.getInstance().getClaimidstate()){
				Sc_MyApplication.getInstance().setClaimidstate(claimstate_pre);
				return;
			}else{
				historyFlow(sc_GetText.pareStatus(claimstate_pre)+1);
			}
		}
	}

	/**
	 * 进入结案流程
	 */
	public void flow4Access() {
		suoLinear1.setVisibility(View.VISIBLE);
		currentStep.setText("描述");
		step2Btn.setImageResource(R.drawable.sc_dot_ls);
//		step3Btn.setImageResource(R.drawable.dot_ls);
		step4Btn.setImageResource(R.drawable.sc_dot_ls);
		step5Btn.setImageResource(R.drawable.sc_dot_dls);
		img1.setBackgroundResource(R.drawable.sc_line_ls);
//		img2.setBackgroundResource(R.drawable.line_ls);
		img3.setBackgroundResource(R.drawable.sc_line_ls);
		img4.setBackgroundResource(R.drawable.sc_line_ls);
		setLinearLayoutViewLayout(4);
		setViewVisibility(4);
		linearofbtn.setVisibility(View.GONE);
		detailOver.setBackgroundDrawable(null);
		detailTime.setVisibility(View.GONE);
		detailContent.setText(sc_GetText.getMsg1(Sc_MyApplication.getInstance().getClaimidstate()
				, Sc_MyApplication.getInstance().getCaseidstate()));//结案信息内容
		
		if(sc_GetText.isCancle(Sc_MyApplication.getInstance().getClaimidstate()
				, Sc_MyApplication.getInstance().getCaseidstate())){
			if((Sc_MyApplication.getInstance().getClaimidstate() & 16384) != 16384){
				img4.setBackgroundResource(R.drawable.sc_line_on);
				step5Btn.setImageResource(R.drawable.sc_dot_on);
			}
			if((Sc_MyApplication.getInstance().getClaimidstate() & 2048) != 2048){
				img4.setBackgroundResource(R.drawable.sc_line_on);
				step4.setClickable(false);
				step5Btn.setImageResource(R.drawable.sc_dot_on);
				step4Btn.setImageResource(R.drawable.sc_dot_on);
			}
			if((Sc_MyApplication.getInstance().getClaimidstate() & 128) != 128){
				img3.setBackgroundResource(R.drawable.sc_line_on);
//				step3.setClickable(false);
//				step3Btn.setImageResource(R.drawable.dot_on);
			}
			if((Sc_MyApplication.getInstance().getClaimidstate() & 8) != 8){
//				img2.setBackgroundResource(R.drawable.line_on);
				step2.setClickable(false);
				step2Btn.setImageResource(R.drawable.sc_dot_on);
			}
		}
//		stepImgBtn.setVisibility(View.VISIBLE);
		if(tag == 2&&Sc_MyApplication.getInstance().getSelfHelpFlag() != 1){
			claimstate_pre = getIntent().getIntExtra("claimstate", 0);
			if(claimstate_pre>Sc_MyApplication.getInstance().getClaimidstate()){
				Sc_MyApplication.getInstance().setClaimidstate(claimstate_pre);
				return;
			}else{
				int step = sc_GetText.pareStatus(claimstate_pre)+1;
				if(step !=4){
					historyFlow(step);
				}
			}
		}
		String msg = "尊敬的客户：您的索赔单证齐全、有效，且同意我司核定赔款。在向您所提供的账户信息成功支付赔款后，我司关于本事故之一切赔偿责任终了。";
		if((Sc_MyApplication.getInstance().getClaimidstate()&16384)==16384){
			AlertDialog.Builder callDailog = new AlertDialog.Builder(Sc_MyApplication.getInstance().getContext());
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage(msg.toString())
					.setPositiveButton("我知道了", null);
			if (back_activity == 0) {
				callDailog.show();
			}
		}
		
		
	}



	private void init() {
		switch (Sc_MyApplication.getInstance().getStepstate()) {
		case 0:
			if(back_activity == 0){
				new Thread(newrunnable1).start();
			}else{
				btn1state = 1;
				btn2state = 0;
				btn3state = 0;
				btn4state = 0;
				handlerrunnable.sendEmptyMessage(1);
			}

			break;
		case 1:
			claim_mode = 1;
			
			if(back_activity == 0){
				if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
					new Thread(newrunnable2).start();
					return;
				}
				new Thread(newrunnable2).start();
			}else{
				btn1state = 1;
				btn2state = 1;
				btn3state = 0;
				btn4state = 0;
				handlerrunnable.sendEmptyMessage(2);
			}
			
			break;
		case 2:
			claim_mode = 1;
			if(back_activity == 0){
				if(Sc_MyApplication.getInstance().getSelfHelpFlag() == 1){
					new Thread(newrunnable3).start();
					return;
				}
				new Thread(newrunnable3).start();
			}else{
				if (claim_mode == 1) {
					btn1state = 1;
					btn2state = 0;
					btn3state = 1;
					btn4state = 0;
				} else {
					btn1state = 1;
					btn2state = 1;
					btn3state = 1;
					btn4state = 0;
				}
				handlerrunnable.sendEmptyMessage(3);
			}
			break;
		case 3:
			if(back_activity == 0){
				new Thread(newrunnable4).start();
			}else{
				if (claim_mode == 1) {
					btn1state = 1;
					btn2state = 0;
					btn3state = 1;
					btn4state = 1;
				} else {
					btn1state = 1;
					btn2state = 1;
					btn3state = 1;
					btn4state = 1;
				}
				handlerrunnable.sendEmptyMessage(4);
			}
			break;

		default:
			break;
		}
	}

	
	

	/**
	 * 所有图片都上传完毕后，点击上传按钮发送结束标记
	 */
	public void SurveySelf() {
		if (!money_rb2.isChecked()) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "over"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("caseid", Sc_MyApplication
					.getInstance().getCaseid()));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			if (Sc_MyApplication.getInstance().getStepstate() == 0) {
				params.add(new BasicNameValuePair("type", "1"));
				sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
						params, 9);
				uploaddata.Post();
			} else {
				params.add(new BasicNameValuePair("type", "2"));
				sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
						params, 99);
				uploaddata.Post();
			}

		}else{
			if(progressdialog!=null&&progressdialog.isShowing()){
				progressdialog.dismiss();
			}
			sc_MyHandler.getInstance().sendEmptyMessage(-13);
		}
	}
	/**
	 * 根据理赔编号获取图片的信息列表（合格和不合格时）
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
					params, 14);
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
		if(claimPhotoinfos==null){
			return;
		}
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
			values.put("claimid", Sc_MyApplication.getInstance().getClaimid());
			values.put("type", claimphotoinfo.getType());
			values.put("savepath", Environment.getExternalStorageDirectory()+"/fkdsfhsdk.ttt");
			if (selectresult1.size() <= 0) {
				database.insert("uploadinfo", values);
			}
			values.clear();
		}
		values = null;
	}

	/**
	 * 获取理赔模式
	 */
	public void getMode() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getMode"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
					params, 15);
			uploaddata.Post();
		}
	}

	/**
	 * 获取理赔模式
	 */

	public void parseMode(int tag, String response) {
		if (tag == 2) {
//			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
//			callDailog.setIcon(android.R.drawable.ic_dialog_info)
//					.setTitle("提示").setMessage("未能够获取服务器信息，请检查网络稍后重试")
//					.setPositiveButton("我知道了", null).show();
			return;
		}
		ContentValues values = new ContentValues();
		values.put("claim_mode", response);
		database.update("claiminfo", values, "claimid = ?",
				new String[] { Sc_MyApplication.getInstance().getClaimid() });
		claim_mode = Integer.parseInt(response);
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
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("type", "2"));
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
	public boolean parseJosnLegendids(String response) {
//		if (tag == 2) {
//			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
//			callDailog.setIcon(android.R.drawable.ic_dialog_info)
//					.setTitle("提示").setMessage("未能够获取服务器信息，请检查网络稍后重试")
//					.setPositiveButton("我知道了", null).show();
//			return false;
//		}
//		database.execSql("update claiminfo set legends ='"+response+"' where claimid='"+MyApplication.getInstance().getClaimid()+"'");
		ContentValues values = new ContentValues();
		values.put("legends", response);
		database.update("claiminfo", values, "claimid=?", new String[]{Sc_MyApplication.getInstance().getClaimid()});
		return true;
	}

	/**
	 * 获取单证信息
	 */
	public void getCertificates() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getCertificates"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			sc_UploadData1 uploaddata = new sc_UploadData1(Sc_MyApplication.URL + "claim",
					params);
			uploaddata.Post();
			String reponse = uploaddata.getReponse();
			if(!reponse.contains("-")){
				parseJosnCertificates(reponse);
			}
		}
	}

	/**
	 * 解析获取的单证图例信息
	 */
	public boolean  parseJosnCertificates(String response) {
//		if (tag == 2) {
//			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
//			callDailog.setIcon(android.R.drawable.ic_dialog_info)
//					.setTitle("提示").setMessage("未能够获取服务器信息，请检查网络稍后重试")
//					.setPositiveButton("我知道了", null).show();
//			return false;
//		}
//		database.execSql("update claiminfo set certificates ='"+response+"' where claimid='"+MyApplication.getInstance().getClaimid()+"'");
		ContentValues values = new ContentValues();
		values.put("legends", response);
		database.update("claiminfo", values, "certificates=?", new String[]{Sc_MyApplication.getInstance().getClaimid()});
		return true;
	}
	/**
	 * 将获取的单证或查勘的图片解析出来
	 * @param str
	 */
	public ArrayList<HashMap<String, Integer>> changeCertificates(String str){
		ArrayList<HashMap<String, Integer>> leg = new ArrayList<HashMap<String,Integer>>();
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

	/**
	 * 获取价格
	 */
	public void getPrice() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "getPrice"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			sc_UploadData uploaddata = new sc_UploadData(Sc_MyApplication.URL + "claim",
					params, 16);
			uploaddata.Post();
		}
	}

	/**
	 * 解析获取的价格
	 */
	public void parseJosnPrice(int tag, String response) {
		if (tag == 2) {
			return;
		}
		Gson gson = new Gson();// 创建Gson对象，
		ContentValues values = new ContentValues();
		sc_ClaimInfo claiminfo = gson.fromJson(response, sc_ClaimInfo.class);
		if(claiminfo==null){
			return;
		}
		try {
			values.put("claim_amount", claiminfo.getClaimamount());
		} catch (Exception e) {
			values.put("claim_amount", "");
		}
		try {
			values.put("claim_overview", claiminfo.getClaimoverview());
		} catch (Exception e) {
			values.put("claim_overview", "");
		}
		try {
			values.put("bank_name", claiminfo.getBankname());
		} catch (Exception e) {
			values.put("bank_name", "");
		}
		try {
			values.put("account_name", claiminfo.getAccountname());
		} catch (Exception e) {
			values.put("account_name", "");
		}
		try {
			values.put("account_number", claiminfo.getAccountnumber());
		} catch (Exception e) {
			values.put("account_number", "");
		}
		try {
			values.put("bankcode", claiminfo.getBankcode());
		} catch (Exception e) {
			values.put("bankcode ", "");
		}
		try {
			values.put("bankphonenumber ", claiminfo.getBankphonenumber());
		} catch (Exception e) {
			values.put("bankphonenumber ", "");
		}
		try {
			values.put("disagreereason", claiminfo.getReason());
		} catch (Exception e) {
			values.put("disagreereason", "");
		}
		try {
			values.put("disagreereasonid", claiminfo.getUaid());
		} catch (Exception e) {
			values.put("disagreereasonid", "");
		}
		database.update("claiminfo", values, "claimid = ?",
				new String[] { Sc_MyApplication.getInstance().getClaimid() });
	}


	private boolean yanzheng() {
		if(!money_rb2.isChecked()&&!money_rb1.isChecked()){
			tiXing("请确认定损价格！");
			return false;
		} else if (money_rb1.isChecked()&&bank_code=="") {
			tiXing("总行不能为空！");
			return false;
		} else if (money_rb1.isChecked()&&bank_et1.getText().toString().trim().equals("")) {
			tiXing("开户行不能为空！");
			return false;
		} else if (money_rb1.isChecked()&&bank_et2.getText().toString().trim().equals("")) {
			tiXing("户名不能为空！");
			return false;
		} else if (!bank_et2.getText().toString().trim().equals("")&&!insuredName.equals("")&&!insuredName.equals(bank_et2.getText().toString().trim())) {
			tiXing("您输入的账户归属人与被保险人姓名不一致，请重新输入！");
			return false;
		} else if (money_rb1.isChecked()&&bank_et3.getText().toString().trim().equals("")) {
			tiXing("银行账号不能为空！");
			return false;
		} else if (money_rb1.isChecked()&&phone.getText().toString().trim().equals("")) {
			tiXing("电话号码不能为空！");
			return false;
		} else if (money_rb1.isChecked()&&!sc_StringUtils.isCellphone(phone.getText().toString())) {
			tiXing("电话号码格式错误！");
			return false;
		} else if (money_rb1.isChecked()&&!cb1.isChecked()) {
			tiXing("请同意将本事故赔款划入上述账户！");
			return false;
		}
		return true;
	}
	
	private boolean yanzheng1() {
		if (bank_et1.getText().toString().trim().equals("")) {
			tiXing("开户行不能为空！");
			return false;
		} else if (bank_et2.getText().toString().trim().equals("")) {
			tiXing("户名不能为空！");
			return false;
		} else if (bank_et3.getText().toString().trim().equals("")) {
			tiXing("银行账号不能为空！");
			return false;
		} else if (bank_code=="") {
			tiXing("总行不能为空！");
			return false;
		} else if (phone.getText().toString().trim().equals("")) {
			tiXing("电话号码不能为空！");
			return false;
		} else if (!sc_StringUtils.isCellphone(phone.getText().toString())) {
			tiXing("电话号码格式错误！");
			return false;
		}
		return true;
	}

	public void tiXing(String msg) {
		AlertDialog.Builder callDailog = new AlertDialog.Builder(
				Sc_CaseHandlerFlowActivity.this);
		callDailog.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(msg).setPositiveButton("我知道了", null).show();
	}

	
	/**
	 * 成功后
	 */
	public void parseJosnBank1(String response) {
		if (!response.equals("0")) {
			if (response.equals("-31")||
					response.equals("-30")||
					response.contains("-33")) {
				handlerrunnable.sendEmptyMessage(9);
				return;
			}
			handlerrunnable.sendEmptyMessage(10);
			return;
		}

		if (isSuccessConfirmBank == 0&&!ishasFile) {
			sc_MyHandler.getInstance().sendEmptyMessage(-11);
		} else if (isSuccessConfirmBank == 1) {
			sc_MyHandler.getInstance().sendEmptyMessage(-11);
			Intent i = new Intent(this, Sc_CaseListActivity.class);
			startActivity(i);
			this.finish();
		}
		ContentValues values = new ContentValues();
		values.put("iscomfirm", cb1.isChecked()?1:0);
		values.put("disagreereason", money_et.getText()
				.toString());
		values.put("disagreereasonid", spReason.getSelectedItemPosition());
		database.update("claiminfo", values, "claimid = ?",
				new String[] { Sc_MyApplication.getInstance().getClaimid() });
	}
	/**
	 * 成功后
	 */
	public void parseJosnBank(int tag, String response) {
		if (tag == 2) {
			if (response.equals("-31")||
					response.equals("-30")||
					response.contains("-33")) {
				showInputDialog(Sc_MyApplication.getInstance().getPassword());
				return;
			}
			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
			callDailog.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("提示").setMessage("提交信息失败，请稍后重试")
					.setPositiveButton("我知道了", null).show();
			return;
		}

		if (isSuccessConfirmBank == 0&&!ishasFile) {
			progressdialog.dismiss();
		} else if (isSuccessConfirmBank == 1) {
			progressdialog.dismiss();
			Intent i = new Intent(this, Sc_CaseListActivity.class);
			startActivity(i);
			this.finish();
		}
		ContentValues values = new ContentValues();
		values.put("iscomfirm", cb1.isChecked()?1:0);
		values.put("disagreereason", money_et.getText()
				.toString());
		values.put("disagreereasonid", spReason.getSelectedItemPosition());
		database.update("claiminfo", values, "claimid = ?",
				new String[] { Sc_MyApplication.getInstance().getClaimid() });
	}
	public void showProgressDialog() {
		new Thread(progressrunnable).start();
	}
	private Runnable progressrunnable = new Runnable() {
		@Override
		public void run() {
			sc_MyHandler.getInstance().sendEmptyMessage(-8);
		}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
				Intent i = new Intent();
				if (tag == 0) {
					i.putExtra("back_activity", 1);//返回
					i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_CaseListActivity.class);
				} else if (tag == 1) {
					i.putExtra("back_activity", 1);//返回
					i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_CaseInfoActivity.class);
				} else if (tag == 2) {
					i.putExtra("back_activity", 1);//返回
					i.setClass(Sc_CaseHandlerFlowActivity.this, Sc_MessageListActivity.class);
				}
				startActivity(i);
				finish();
		} else if (KeyEvent.KEYCODE_MENU == keyCode) {
			return false;
		}
		return true;
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
						if(!progressdialog.isShowing()){
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
	
	
	SQLiteDatabase db;
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/ChinaLife";
	private String DATABASE_FILENAME = "bank.sqlite.sqlite";

	// 初始化数据库
	private SQLiteDatabase openDatabase() {
		try {
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			if (!dir.exists())
				dir.mkdir();
			if (!(new File(databaseFilename)).exists()) {
				InputStream is = getAssets().open("bank.sqlite.sqlite");
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
			return db;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> getBank(String str) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Cursor mCursor = null;
		String sql = "select name from bank where code='"+str+"'";
		try {
			db = openDatabase();
			mCursor = db.rawQuery(sql, null);
			Map<String, Object> map = null;
			int iColumnCount = mCursor.getColumnCount();
			while (mCursor.moveToNext()) {
				map = new HashMap<String, Object>();
				for (int i = 0; i < iColumnCount; i++) {
					map.put(mCursor.getColumnName(i).toLowerCase(),
							mCursor.getString(i));
				}
				result.add(map);
			}
		} catch (Exception e) {
			Log.e("bank_dbhelp","selectRow error:" + sql + "\nException:" + e.getMessage());
		} finally {
			if (mCursor != null) {
				mCursor.close();
			}
		}
		return result;
	}
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
						e.printStackTrace();
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
	 * 更新对话框的点击状态
	 * 
	 * @param bitmap
	 * @param listPosition
	 * @param itemPosition
	 */
	public void updateListItemData(int state, int listPosition,boolean b) {
		stepList.get(listPosition).put("savepath", sc_LegendInfo.getLegendImage(stepList.get(listPosition).get("legendid").toString()));
		stepList.get(listPosition).put("name", stepList.get(listPosition).get("name"));
		stepList.get(listPosition).put("state", 0);
		stepList.get(listPosition).put("uploadpath",
				stepList.get(listPosition).get("uploadpath"));
		stepList.get(listPosition).put("photoid",
				stepList.get(listPosition).get("photoid"));
		stepList.get(listPosition).put("ischange",0);
		stepList.get(listPosition).put("checked",
				stepList.get(listPosition).get("checked"));
		stepList.get(listPosition).put("photofile","");
		stepList.get(listPosition).put("legendid",
				stepList.get(listPosition).get("legendid"));
		stepList.get(listPosition).put("reviewreason", stepList.get(listPosition).get("reviewreason"));
		stepList.get(listPosition).remove("pic_state");
	}
	
	

	/**
	 * 根据理赔编号获取图片的信息列表（合格和不合格时）
	 */

	public boolean GetImageInfo() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getFullImages"));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("claimid", Sc_MyApplication
				.getInstance().getClaimid()));
		sc_UploadData3 uploaddata = new sc_UploadData3(Sc_MyApplication.URL + "claim",params);
		uploaddata.Post();
		String response = uploaddata.getResponse();
		ParseImageInfo(response);
		params.clear();
		params = null;
		if (response.startsWith("[")) {
			return true;
		}
		return false;
	}

	/**
	 * 根据理赔编号获取图片的信息列表,给图片打上合格和重拍字样
	 */

	public void ParseImageInfo(String response) {
		if(response.startsWith("[")){
			Type listType = new TypeToken<LinkedList<sc_ClaimPhotoInfo>>() {
			}.getType();
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			LinkedList<sc_ClaimPhotoInfo> claimPhotoinfos = null;
			sc_ClaimPhotoInfo claimphotoinfo = null;
			claimPhotoinfos = gson.fromJson(response, listType);
			if(claimPhotoinfos==null){
				return;
			}
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
				values.put("claimid", Sc_MyApplication.getInstance().getClaimid());
				values.put("type", claimphotoinfo.getType());
				values.put("savepath", Environment.getExternalStorageDirectory()+"/fkdsfhsdk.ttt");
				if (selectresult1.size() <= 0) {
					database.insert("uploadinfo", values);
				}
				values.clear();
			}
			values = null;
		}
		
	}

	/**
	 * 获取理赔模式
	 */
	public boolean GetMode() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getMode"));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("claimid", Sc_MyApplication
				.getInstance().getClaimid()));
		sc_UploadData3 uploaddata = new sc_UploadData3(Sc_MyApplication.URL + "claim",
				params);
		String response = uploaddata.getResponse();
		params.clear();
		params = null;
		if (response.equals("1")||response.equals("2")) {
			ParseImageInfo(response);
			return true;
		}
		return false;
	}

	/**
	 * 获取理赔模式
	 */

	public void ParseMode(String response) {
		ContentValues values = new ContentValues();
		values.put("claim_mode", response);
		database.update("claiminfo", values, "claimid = ?",
				new String[] { Sc_MyApplication.getInstance().getClaimid() });
		claim_mode = Integer.parseInt(response);
	}

	/**
	 * 获取图例信息
	 */
	public boolean GetLegendids() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getImageLayout"));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("type", "2"));
		params.add(new BasicNameValuePair("claimid", Sc_MyApplication
				.getInstance().getClaimid()));
		sc_UploadData3 uploaddata = new sc_UploadData3(Sc_MyApplication.URL + "claim",
				params);
		uploaddata.Post();
		String response = uploaddata.getResponse();
		if (!response.contains("-")) {
			ParseJosnLegendids(response);
			return true;
		}
		return false;
	}

	/**
	 * 解析获取的图例信息
	 */
	public void ParseJosnLegendids(String response) {
		ContentValues values = new ContentValues();
		values.put("legends", response);
		database.update("claiminfo", values, "claimid=?", new String[]{Sc_MyApplication.getInstance().getClaimid()});
	}

	/**
	 * 获取单证信息
	 */
	public boolean GetCertificates() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getCertificates"));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("claimid", Sc_MyApplication
				.getInstance().getClaimid()));
		sc_UploadData3 uploaddata = new sc_UploadData3(Sc_MyApplication.URL + "claim",
				params);
		uploaddata.Post();
		String response = uploaddata.getResponse();
		if (!response.contains("-")) {
			ParseJosnCertificates(response);
			return true;
		}
		return false;
	}

	/**
	 * 解析获取的单证图例信息
	 */
	public void  ParseJosnCertificates(String response) {
		ContentValues values = new ContentValues();
		values.put("certificates", response);
		database.update("claiminfo", values, "claimid=?", new String[]{Sc_MyApplication.getInstance().getClaimid()});
	}

	/**
	 * 获取价格
	 */
	public boolean GetPrice() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "getPrice"));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		params.add(new BasicNameValuePair("claimid", Sc_MyApplication
				.getInstance().getClaimid()));
		sc_UploadData3 uploaddata = new sc_UploadData3(Sc_MyApplication.URL + "claim",params);
		uploaddata.Post();
		String response = uploaddata.getResponse();
		if (response.startsWith("{")) {
			ParseJosnPrice(response);
			return true;
		}
		return false;
	}

	/**
	 * 解析获取的价格
	 */
	public void ParseJosnPrice(String response) {
		Gson gson = new Gson();// 创建Gson对象，
		ContentValues values = new ContentValues();
		sc_ClaimInfo claiminfo = gson.fromJson(response, sc_ClaimInfo.class);
		if(claiminfo==null){
			return;
		}
		try {
			values.put("claim_amount", claiminfo.getClaimamount());
		} catch (Exception e) {
			values.put("claim_amount", "");
		}
		try {
			values.put("claim_overview", claiminfo.getClaimoverview());
		} catch (Exception e) {
			values.put("claim_overview", "");
		}
		try {
			values.put("bank_name", claiminfo.getBankname());
		} catch (Exception e) {
			values.put("bank_name", "");
		}
		try {
			values.put("account_name", claiminfo.getAccountname());
		} catch (Exception e) {
			values.put("account_name", "");
		}
		try {
			values.put("account_number", claiminfo.getAccountnumber());
		} catch (Exception e) {
			values.put("account_number", "");
		}
		try {
			values.put("bankcode", claiminfo.getBankcode());
		} catch (Exception e) {
			values.put("bankcode ", "");
		}
		try {
			values.put("bankphonenumber ", claiminfo.getBankphonenumber());
		} catch (Exception e) {
			values.put("bankphonenumber ", "");
		}
		try {
			values.put("disagreereason", claiminfo.getReason());
		} catch (Exception e) {
			values.put("disagreereason", "");
		}
		try {
			values.put("disagreereasonid", claiminfo.getUaid());
		} catch (Exception e) {
			values.put("disagreereasonid", "");
		}
		database.update("claiminfo", values, "claimid = ?",
				new String[] { Sc_MyApplication.getInstance().getClaimid() });
	}
	
	/**
	 * 提交确认银行账号并且是否同意价格的接口
	 */
	public boolean ComfirmBank() {
		if (sc_NetworkCheck.IsHaveInternet(this)) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", Sc_MyApplication.APPID));
			params.add(new BasicNameValuePair("appkey", Sc_MyApplication.APPKEY));
			params.add(new BasicNameValuePair("action", "confirm"));
			params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
			params.add(new BasicNameValuePair("phoneNumber", Sc_MyApplication
					.getInstance().getPhonenumber()));
			params.add(new BasicNameValuePair("plateNumber", Sc_MyApplication
					.getInstance().getPlatenumber()));
			params.add(new BasicNameValuePair("password", Sc_MyApplication.getInstance().getPassword()));
			params.add(new BasicNameValuePair("claimid", Sc_MyApplication
					.getInstance().getClaimid()));
			params.add(new BasicNameValuePair("caseid", Sc_MyApplication
					.getInstance().getCaseid()));
			if (money_rb2.isChecked()) {
				Sc_MyApplication.getInstance().isagree = false;
				params.add(new BasicNameValuePair("isAgree", "3"));
				params.add(new BasicNameValuePair("reason", money_et.getText()
						.toString()));
				params.add(new BasicNameValuePair("rea", spReason.getSelectedItemPosition()+""));
			}else{
				Sc_MyApplication.getInstance().isagree = true;
				params.add(new BasicNameValuePair("isAgree", "2"));
			}
			params.add(new BasicNameValuePair("accountName", bank_et2
					.getText().toString()));
			params.add(new BasicNameValuePair("bankName", bank_et1
					.getText().toString()));
			params.add(new BasicNameValuePair("bankCode", bank_code));
			params.add(new BasicNameValuePair("isConfirmBankInfo", cb1.isChecked()?"1":"0"));
			params.add(new BasicNameValuePair("bankPhoneNumber", phone
					.getText().toString()));
			params.add(new BasicNameValuePair("accountNumber", bank_et3
					.getText().toString().replace(" ", "")));
			params.add(new BasicNameValuePair("insuredName", insuredName));
			sc_UploadData3 uploaddata = new sc_UploadData3(Sc_MyApplication.URL + "claim",
					params);
			uploaddata.Post();
			String response = uploaddata.getResponse();
			parseJosnBank1(response);
			if (response.equals("-31")||
					response.equals("-30")||
					response.contains("-33")) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * 成功后
	 */
	public void ParseJosnBank(String response) {
		if (!response.equals("0")) {
			if (response.equals("-31")||
					response.equals("-30")||
					response.contains("-33")) {
				handlerrunnable.sendEmptyMessage(9);
				return;
			}
			handlerrunnable.sendEmptyMessage(10);
			return;
		}
		ContentValues values = new ContentValues();
		values.put("iscomfirm", cb1.isChecked()?1:0);
		values.put("disagreereason", money_et.getText()
				.toString());
		values.put("disagreereasonid", spReason.getSelectedItemPosition());
		database.update("claiminfo", values, "claimid = ?",
				new String[] { Sc_MyApplication.getInstance().getClaimid() });
	}
	
public Runnable newrunnable1 = new Runnable() {
		
		@Override
		public void run() {
			if (sc_NetworkCheck.IsHaveInternet(Sc_CaseHandlerFlowActivity.this)) {
				handlerrunnable.sendEmptyMessage(5);
				if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}else{
					GetLegendids();
					String ids = checkLegends(1);
					if(ids!=null
							&&!ids.equals("")){
						getLegendinfo(ids);
					}
					GetImageInfo();
				}
				getStep2List();
				handlerrunnable.sendEmptyMessage(1);
				handlerrunnable.sendEmptyMessage(6);
			} else {
				handlerrunnable.sendEmptyMessage(7);// 没有网络时给用户提示
			}
		}
	};
	
	
	public Runnable newrunnable2 = new Runnable() {
		
		@Override
		public void run() {
			if (sc_NetworkCheck.IsHaveInternet(Sc_CaseHandlerFlowActivity.this)) {
				handlerrunnable.sendEmptyMessage(5);
				if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}else{
					GetLegendids();
					String ids = checkLegends(1);
					if(ids!=null
							&&!ids.equals("")){
						getLegendinfo(ids);
					}
					GetCertificates();
					ids = checkLegends(2);
					if(ids!=null
							&&!ids.equals("")){
						getLegendinfo(ids);
					}
					GetImageInfo();
				}
				btn1state = 1;
				btn2state = 1;
				btn3state = 0;
				btn4state = 0;
				handlerrunnable.sendEmptyMessage(2);
				handlerrunnable.sendEmptyMessage(6);
			} else {
				handlerrunnable.sendEmptyMessage(7);// 没有网络时给用户提示
			}
		}
	};
	
	public Runnable newrunnable3 = new Runnable() {
		
		@Override
		public void run() {
			if (sc_NetworkCheck.IsHaveInternet(Sc_CaseHandlerFlowActivity.this)) {
				handlerrunnable.sendEmptyMessage(5);
				if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}else{
					if ((Sc_MyApplication.getInstance().getClaimidstate() & 512) == 512) {
						GetPrice();
					}
					GetLegendids();
					String ids = checkLegends(1);
					if(ids!=null
							&&!ids.equals("")){
						getLegendinfo(ids);
					}
					GetCertificates();
					ids = checkLegends(2);
					if(ids!=null
							&&!ids.equals("")){
						getLegendinfo(ids);
					}
					GetImageInfo();
					if (((Sc_MyApplication.getInstance().getClaimidstate() & 32) == 32
							|| (Sc_MyApplication.getInstance().getClaimidstate() & 64) == 64 
							|| (Sc_MyApplication.getInstance().getClaimidstate() & 128) == 128
							|| (Sc_MyApplication.getInstance().getClaimidstate() & 256) == 256)
							&& claim_mode == 1) {
					}
				}
				
				if (claim_mode == 1) {
					btn1state = 1;
					btn2state = 0;
					btn3state = 1;
					btn4state = 0;
				} else {
					btn1state = 1;
					btn2state = 1;
					btn3state = 1;
					btn4state = 0;
				}
				handlerrunnable.sendEmptyMessage(3);
				handlerrunnable.sendEmptyMessage(6);
			} else {
				handlerrunnable.sendEmptyMessage(7);// 没有网络时给用户提示
			}
		}
	};
	
	public Runnable newrunnable4 = new Runnable() {
		
		@Override
		public void run() {
			if (sc_NetworkCheck.IsHaveInternet(Sc_CaseHandlerFlowActivity.this)) {
				handlerrunnable.sendEmptyMessage(5);
				if(Sc_MyApplication.getInstance().getSelfHelpFlag()==1){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
					}
				}else{
					GetLegendids();
					String ids = checkLegends(1);
					if(ids!=null
							&&!ids.equals("")){
						getLegendinfo(ids);
					}
					GetCertificates();
					ids = checkLegends(2);
					if(ids!=null
							&&!ids.equals("")){
						getLegendinfo(ids);
					}
					GetImageInfo();
				}
				if (claim_mode == 1) {
					btn1state = 1;
					btn2state = 0;
					btn3state = 1;
					btn4state = 1;
				} else {
					btn1state = 1;
					btn2state = 1;
					btn3state = 1;
					btn4state = 1;
				}
				handlerrunnable.sendEmptyMessage(4);
				handlerrunnable.sendEmptyMessage(6);
			} else {
				handlerrunnable.sendEmptyMessage(7);// 没有网络时给用户提示
			}
		}
	};
	
	
	
	private Handler handlerrunnable = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			AlertDialog.Builder callDailog = null;
			switch (msg.what) {
			case 1:
				flow1Access();
				break;
			case 2:
				flow2Access();
				break;
			case 3:
				flow3Access();
				break;
			case 4:
				flow4Access();
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(Sc_CaseHandlerFlowActivity.this);
				progressdialog.setMessage("系统处理中，请稍等~~~");
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
				
			case 7:// 关闭进度条
				callDailog = new AlertDialog.Builder(Sc_CaseHandlerFlowActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("请检查网络是否连入").setPositiveButton("确定", null)
						.show();
				break;
			case 8:
				int number  = Sc_MyApplication.getInstance().getSayNumber();
				if(number>0){
					notifiCationBtn.setVisibility(View.VISIBLE);
					notifiCationBtn.setText(Sc_MyApplication.getInstance().getSayNumber()+"");
				}else{
					notifiCationBtn.setVisibility(View.INVISIBLE);
				}
				break;
			case 9:
				sc_MyHandler.getInstance().sendEmptyMessage(-11);
				showInputDialog(Sc_MyApplication.getInstance().getPassword());
				break;
			case 10:
				sc_MyHandler.getInstance().sendEmptyMessage(-11);
				callDailog = new AlertDialog.Builder(Sc_CaseHandlerFlowActivity.this);
				callDailog.setIcon(android.R.drawable.ic_dialog_info)
						.setTitle("提示").setMessage("提交信息失败，请稍后重试")
						.setPositiveButton("我知道了", null).show();
				break;
			case 12:
				if(Sc_MyApplication.getInstance().getFile()!=null && Sc_MyApplication.getInstance().getFile().exists()){
					updateListItemData(Sc_MyApplication.getInstance().getFile(), listPosition);
					notifyAdapter();
					progressdialog.dismiss();
					imgListView.setSelection(listPosition);
				}else{
					Toast.makeText(Sc_CaseHandlerFlowActivity.this, "抱歉，图片处理失败，请重新拍摄。。", 5000).show();
				}
				sc_BitmapCache.getInstance().clearCache();
				break;
			}
		}

	};

}
