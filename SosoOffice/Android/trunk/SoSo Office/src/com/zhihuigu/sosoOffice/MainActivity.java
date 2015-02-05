package com.zhihuigu.sosoOffice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.model.SoSoCountInfo;
import com.zhihuigu.sosoOffice.model.SoSoDistrictInfo;
import com.zhihuigu.sosoOffice.model.SoSoZoneInfo;
import com.zhihuigu.sosoOffice.service.SoSoOfficeReceiver;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.Adapter.MainGridViewAdapter;
import com.zhihuigu.sosoOffice.Adapter.MainListAdapter;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.OfficeType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.PhoneInfo;
import com.zhihuigu.sosoOffice.utils.StringUtils;
import com.zhihuigu.sosoOffice.utils.ZoneUtil;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity{
	private LinearLayout mainTopBtnLinear;// 显示城市的区域，用来点击展开列表
	public  TextView mainTopCityText;// 显示城市的文本控件
	private EditText searchRoomEt;// 搜索房源的文本框
	private LinearLayout searchEtLayout;// 用于放搜素框的布局
	private Button searchBtn;// 搜索按钮
	private LinearLayout searchListLayout;// 用于显示商圈的列表布局
//	private TextView noTv;//无商圈数据时显示
	private LinearLayout noDataLayout;//没有数据时显示的布局
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	private TextView searchRoomText;// 用于显示当前选中的商圈
	private ImageView mainListJianTou;// 用于展开或隐藏列表的按钮
//	private EditText searchAreaEt;// 输入关键字的文本框
	private ExpandableListView mainListView;// 显示商圈的列表
	private TextView noLimitText;//不限定商圈控件
	private List<HashMap<String, Object>> groupArray;
	private List<List<HashMap<String, Object>>> childArray;
	private GridView mainGv;// 功能控件
	private List<HashMap<String, Object>> mainGvList;
	private SoSoUploadData uploaddata;
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;

	private String cityid="";// 城市id;
	private String areaid = "";//区id
	private String districtid = "";//商圈id
	
	
	private String cityname = "";//城市名称
	
	private String latitude = "";
	private String longitude = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
//		Intent intent = new Intent(this, SoSoOfficeReceiver.class);
//		intent.putExtra("tag", 1);
//		this.sendBroadcast(intent);
		findView();
		initView();
		initGVData(getIntent().getIntExtra("userType",
				MyApplication.getInstance(this).getRoleid()));
		
		if(MyApplication.checkupdate){
			checkUpdate();
			MyApplication.checkupdate = false;
		}
		
	}

	/**
	 *检查更新，给用户提示
	 * 作者：Ring
	 * 创建于：2013-3-7
	 */
	private void checkUpdate() {
		List<Map<String, Object>> selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_versioninfo",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if(selectresult.get(selectresult.size() - 1) != null
					&&selectresult.get(selectresult.size() - 1).get("version_number") != null
					&&!selectresult.get(selectresult.size() - 1).get("version_number").toString().trim().equals("")
					&&selectresult.get(selectresult.size() - 1).get("url") != null
					&&!selectresult.get(selectresult.size() - 1).get("url").toString().trim().equals("")
					&&selectresult.get(selectresult.size() - 1).get("ismustupdate") != null
					&&!selectresult.get(selectresult.size() - 1).get("ismustupdate").toString().trim().equals("")){
				String appversion1 = PhoneInfo.getAppVersionName(this);
				String appversion2 = selectresult.get(selectresult.size() - 1).get("version_number").toString();
				final String url = selectresult.get(selectresult.size() - 1).get("url").toString();
				final String ismustupdate = selectresult.get(selectresult.size() - 1).get("ismustupdate").toString();
				boolean b = appversion1.compareTo(appversion2)<0;
				if (b) {
					new AlertDialog.Builder(MainActivity.this.getParent())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("提示")
					.setMessage("有新版本是否立即更新？")
					.setCancelable(false)
					.setOnKeyListener(new OnKeyListener() {
						
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
							if(ismustupdate.equals("1")){
								MainActivity.this.finish();
								System.gc();
								System.exit(0);
							}
							return false;
						}
					})
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							Uri content_url =null;
							try {
								content_url = Uri.parse(url);
								intent.setData(content_url);
								MainActivity.this.startActivity(intent);
							} catch (Exception e) {
								content_url = Uri.parse("http://"+url);
								intent.setData(content_url);
								MainActivity.this.startActivity(intent);
							}
							MainActivity.this.finish();
							System.gc();
							System.exit(0);
						}
					})
					.setNegativeButton("取消", null).show();
				}
			}
		}
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		mainTopBtnLinear = (LinearLayout) findViewById(R.id.mainTopBtnLinear);
		mainTopCityText = (TextView) findViewById(R.id.mainTopCityText);
		searchRoomEt = (EditText) findViewById(R.id.searchRoomEt);
		searchEtLayout = (LinearLayout) findViewById(R.id.searchEtLinear);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchListLayout = (LinearLayout) findViewById(R.id.searchListLinear);
		searchRoomText = (TextView) findViewById(R.id.searchRoomText);
		mainListJianTou = (ImageView) findViewById(R.id.mainListJiantou);
//		searchAreaEt = (EditText) findViewById(R.id.searchAreaEt);
//		searchAreaEt.addTextChangedListener(new MyTextWatcher());
		mainListView = (ExpandableListView) findViewById(R.id.mainListView);
		noLimitText = (TextView) findViewById(R.id.noLimitText);
//		noLimitText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//		noLimitText.setText(Html.fromHtml("(xuqingfeng77博客)"));
		
		mainGv = (GridView) findViewById(R.id.mainGv);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
	}

//	/***
//	 * 监听文本改变
//	 */
//	class MyTextWatcher implements TextWatcher {
//
//		@Override
//		public void afterTextChanged(Editable arg0) {
//			initAreaList1(searchAreaEt.getText().toString());
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//
//		}
//
//	}
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			MyApplication.getInstance(this).setCityid(ApplicationSettingInfo
					.getCitySetting(this, "0"));
		}else{
			MyApplication.getInstance(this).setCityid(ApplicationSettingInfo
					.getCitySetting(this, MyApplication.getInstance().getSosouserinfo(this).getUserID()));
		}
		
		if(MyApplication.getInstance(this).getCityid()!=null
				&&!MyApplication.getInstance(this).getCityid().equals("")){
			cityid = MyApplication.getInstance(this).getCityid();
		}
		MyApplication.getInstance(this).setCityname(ZoneUtil.getName(this,cityid,2));
		if(MyApplication.getInstance(this).getCityid()!=null
				&&!MyApplication.getInstance(this).getCityname().equals("")){
			cityname = MyApplication.getInstance(this).getCityname();
		}
		if(cityname.length()>4){
			cityname = cityname.substring(0, 4)+"...";
		}
		mainTopCityText.setText(cityname);
		new Thread(getNumberRunnable).start();
		super.onResume();
	}
	Runnable getNumberRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(1500);
				getDistrictByCityId(MyApplication.getInstance().getCityid());
				getNumberHandler.sendEmptyMessage(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	Handler getNumberHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			getNumber();
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onStop() {
		runnable_tag = true;
		click_limit = true;
		if (uploaddata != null) {
			uploaddata.overReponse();
		}
		super.onStop();
	}

	/**
	 * 控件事件监听
	 */
	private void initView() {
		mainTopBtnLinear.setOnClickListener(this);
		mainListJianTou.setOnClickListener(this);
		searchEtLayout.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		mainListView.setOnChildClickListener(new AreaListChildClickListener());
		noLimitText.setOnClickListener(this);
		searchRoomEt.setOnClickListener(this);
		noDataLayout.setOnClickListener(this);
	}
	
	/**
	 * 商圈列表的子项点击事件监听类
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/7
	 */
	private class AreaListChildClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			searchRoomText.setText(childArray.get(groupPosition)
					.get(childPosition).get("name").toString());
			districtid = childArray.get(groupPosition)
					.get(childPosition).get("id").toString();
			areaid = groupArray.get(groupPosition).get("id").toString();
//			searchRoomEt.setText(searchRoomText.getText().toString());
			searchListLayout.setVisibility(View.GONE);
			for (int i = 0; i < parent.getChildCount(); i++) {
				if (parent.getChildAt(i) == v) {
					parent.getChildAt(i).setBackgroundColor(
							Color.rgb(160, 201, 1));
				} else {
					parent.getChildAt(i).setBackgroundColor(
							Color.rgb(255, 255, 255));
				}
			}
			if(childArray.get(groupPosition)
					.get(childPosition).get("latitude")!=null
					&&childArray.get(groupPosition)
					.get(childPosition).get("longitude")!=null){
				latitude = childArray.get(groupPosition)
						.get(childPosition).get("latitude").toString();
				longitude = childArray.get(groupPosition)
						.get(childPosition).get("longitude").toString();
			}
			CommonUtils.hideSoftKeyboard(MainActivity.this);
			Intent intent = new Intent(MainActivity.this, SearchRoomsListActivity.class);
			intent.putExtra("whereclause", getWhereClause());
			intent.putExtra("cityid", cityid);
			intent.putExtra("areaid", areaid);
			intent.putExtra("districtid", districtid);
			intent.putExtra("officetype", "");
			intent.putExtra("priceup", "20");
			intent.putExtra("pricedown", "0");
			intent.putExtra("areaup", "0");
			intent.putExtra("areadown", "0");
//			intent.putExtra("businessname",searchRoomText.getText()) ;
			intent.putExtra("longitude",longitude);
			intent.putExtra("latitude",latitude) ;
			startActivity(intent);
			return true;
		}
	}
	
	

	/**
	 * 初始化商圈数据
	 * 
	 * @createDate 2013/1/7
	 */
	private void initAreaList() {
		groupArray = new ArrayList<HashMap<String, Object>>();
		childArray = new ArrayList<List<HashMap<String, Object>>>();
		// 以下为模拟数据
		HashMap<String, Object> gMap1 = new HashMap<String, Object>();
		gMap1.put("name", "徐汇区");
		groupArray.add(gMap1);
		HashMap<String, Object> gMap2 = new HashMap<String, Object>();
		gMap2.put("name", "浦东新区");
		groupArray.add(gMap2);

		List<HashMap<String, Object>> tempArray1 = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> cMap1 = new HashMap<String, Object>();
		cMap1.put("name", "星星商业大厦");
		cMap1.put("latitude", "");
		cMap1.put("longitude", "");
		tempArray1.add(cMap1);
		HashMap<String, Object> cMap2 = new HashMap<String, Object>();
		cMap2.put("name", "数码大厦");
		cMap2.put("latitude", "");
		cMap2.put("longitude", "");
		tempArray1.add(cMap2);
		HashMap<String, Object> cMap3 = new HashMap<String, Object>();
		cMap3.put("name", "美罗城");
		cMap3.put("latitude", "");
		cMap3.put("longitude", "");
		tempArray1.add(cMap3);
		HashMap<String, Object> cMap4 = new HashMap<String, Object>();
		cMap4.put("name", "锦荣商业大厦");
		cMap4.put("latitude", "");
		cMap4.put("longitude", "");
		tempArray1.add(cMap4);
		childArray.add(tempArray1);
		List<HashMap<String, Object>> tempArray2 = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> cMap5 = new HashMap<String, Object>();
		cMap5.put("name", "环球金融中心");
		cMap5.put("latitude", "");
		cMap5.put("longitude", "");
		tempArray2.add(cMap5);
		HashMap<String, Object> cMap6 = new HashMap<String, Object>();
		cMap6.put("name", "经贸大厦");
		cMap6.put("latitude", "");
		cMap6.put("longitude", "");
		tempArray2.add(cMap6);
		HashMap<String, Object> cMap7 = new HashMap<String, Object>();
		cMap7.put("name", "房地大厦");
		cMap7.put("latitude", "");
		cMap7.put("longitude", "");
		tempArray2.add(cMap7);
		HashMap<String, Object> cMap8 = new HashMap<String, Object>();
		cMap8.put("name", "星星大厦");
		cMap8.put("latitude", "");
		cMap8.put("longitude", "");
		tempArray2.add(cMap8);
		childArray.add(tempArray2);
		notifyListView();
	}

	/**
	 * 初始化商圈数据 author by Ring
	 * 
	 */
	private void initAreaList1() {
		if (groupArray == null) {
			groupArray = new ArrayList<HashMap<String, Object>>();
		}
		if (childArray == null) {
			childArray = new ArrayList<List<HashMap<String, Object>>>();
		}
		childArray.clear();
		groupArray.clear();
		String sql = "select * from region where parentid='" +cityid
				+ "' and type = '3'";
		List<Map<String, Object>> selectresult = null;
		List<Map<String, Object>> selectresult1 = null;
		selectresult = ZoneUtil.getregion(this, sql);
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, Object> gMap = null;
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("id") != null
						&& selectresult.get(i).get("name") != null) {
					selectresult1 = ZoneUtil.getregion(this, "select id,name,latitude,longitude from district where regionid='"
							+ selectresult.get(i).get("id").toString()
							+ "'");
					if (selectresult1 != null && selectresult1.size() > 0) {
						int j;
						gMap = new HashMap<String, Object>();
						gMap.put("name", selectresult.get(i).get("name")
								.toString());
						gMap.put("id", selectresult.get(i).get("id").toString());
						groupArray.add(gMap);
						List<HashMap<String, Object>> tempArray= new ArrayList<HashMap<String, Object>>();
					gMap = new HashMap<String,Object>();
					gMap.put("name","不限");
					gMap.put("id","");
					try {
						gMap.put("latitude", selectresult.get(i)
								.get("latitude").toString());
					} catch (Exception e) {
						gMap.put("latitude", "0");
					}
					try {
						gMap.put("longitude", selectresult.get(i)
								.get("longitude").toString());
					} catch (Exception e) {
						gMap.put("longitude", "0");
					}
					gMap.put("parentid", "");
					tempArray.add(gMap);
						for (j = 0; j < selectresult1.size(); j++) {
							if (selectresult1.get(j).get("id") != null
									&& selectresult1.get(j).get("name") != null) {
								gMap = new HashMap<String, Object>();
								gMap.put("name",
										selectresult1.get(j).get("name")
												.toString());
								gMap.put("id",
										selectresult1.get(j).get("id")
												.toString());
								try {
									gMap.put("latitude", selectresult1.get(j)
											.get("latitude").toString());
								} catch (Exception e) {
									gMap.put("latitude", "0");
								}
								try {
									gMap.put("longitude", selectresult1.get(j)
											.get("longitude").toString());
								} catch (Exception e) {
									gMap.put("longitude", "0");
								}
								gMap.put("parentid", selectresult.get(i).get("id").toString());
								tempArray.add(gMap);
							}
						}
						childArray.add(tempArray);
					}
				}
			}
		}
		notifyListView();
//		if(childArray.size()>0)
//		searchRoomText.setText(childArray.get(0).get(0).get("name").toString());
	}

//	/**
//	 * 初始化商圈数据 author by Ring whereClause查询条件
//	 */
//	private void initAreaList1(String whereClause) {
//		if (groupArray == null) {
//			groupArray = new ArrayList<HashMap<String, Object>>();
//		}
//		if (childArray == null) {
//			childArray = new ArrayList<List<HashMap<String, Object>>>();
//		}
//		childArray.clear();
//		groupArray.clear();
//
//		String sql = "select regionid,name,id,latitude,longitude from district where name like '%"
//				+ whereClause + "%'";
//		List<Map<String, Object>> selectresult = null;
//		List<Map<String, Object>> selectresult1 = null;
//		selectresult = ZoneUtil.getregion(this, sql);
//		if (selectresult != null && selectresult.size() > 0) {
//			int i;
//			HashMap<String, Object> gMap = null;
//			for (i = 0; i < selectresult.size(); i++) {
//				if (selectresult.get(i).get("regionid") != null
//						&& selectresult.get(i).get("name") != null) {
//					selectresult1 = ZoneUtil.getregion(this, "select id,name from region where id='"
//							+ selectresult.get(i).get("regionid")
//							.toString() + "' and type='3' and parentid='" +cityid
//				+ "'");
//					if (selectresult1 != null && selectresult1.size() > 0) {
//						gMap = new HashMap<String, Object>();
//						gMap.put("name",
//								selectresult1.get(selectresult1.size() - 1)
//										.get("name").toString());
//						gMap.put("id",
//								selectresult1.get(selectresult1.size() - 1)
//										.get("id").toString());
//							groupArray.add(gMap);
//						List<HashMap<String, Object>> tempArray = new ArrayList<HashMap<String, Object>>();
//						gMap = new HashMap<String, Object>();
//						gMap.put("name", selectresult.get(i).get("name")
//								.toString());
//						gMap.put("id", selectresult.get(i).get("id")
//								.toString());
//						gMap.put("parentid", selectresult1.get(selectresult1.size() - 1).get("id")
//								.toString());
//						try{
//							gMap.put("latitude", selectresult.get(i).get("latitude")
//									.toString());
//							gMap.put("longitude", selectresult.get(i).get("longitude")
//									.toString());
//						}catch(Exception e){
//							
//						}
//						tempArray.add(gMap);
//						childArray.add(tempArray);
//					}
//				}
//			}
//		}
//		groupArray  = getGroupArray(groupArray);//整理父控件数据
//		childArray = getChildArray(childArray);//整理子控件数据
//		notifyListView();
//		if(childArray.size()>0)
//			searchRoomText.setText(childArray.get(0).get(0).get("name").toString());
//	}

	/**
	 * 刷新列表布局 给区域列表控件加载数据
	 */
	private void notifyListView() {
		mainListView.setAdapter(new MainListAdapter(this, groupArray,
				childArray));
		setExpandableViewOpen();
		if(groupArray.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
			mainListView.setVisibility(View.GONE);
		}else{
			noDataLayout.setVisibility(View.GONE);
			mainListView.setVisibility(View.VISIBLE);
		}
	}
//	/**
//	 * 针对搜索功能做的数据处理函数
//	 * 根据id给groupArray的数据进行整理，当Id相同的时候只取一个。
//	 * 这个函数能用的前提是集合中要有字段是id  而且数据类型都要能匹配
//	 * @param 要被整理的数据
//	 * @return 一个整理后的数据集合
//	 * @author 刘星星
//	 * @createDate 2013.2.1
//	 */
//	private List<HashMap<String, Object>> getGroupArray(List<HashMap<String, Object>> oldList){
//		List<HashMap<String, Object>> newList = new ArrayList<HashMap<String,Object>>();
//		for(int i=0;i<oldList.size();i++){
//			HashMap<String,Object> map = oldList.get(i);
//			if(i == 0){
//				newList.add(map);
//			}else{
//				if(!map.get("id").toString().equals(oldList.get(i-1).get("id"))){
//					newList.add(map);
//				}
//			}
//		}
//		return newList;
//	}
//	/**
//	 * 这是一个对树控件的子集数据集合进行整理的函数哦。。。
//	 * 跟据父控件的id进行对比，把父控件id相同的子集放入同一个集合。
//	 * 得到每一个子集中的字段虽然挺麻烦，但是都是一层一层剥的，还是蛮容易理解的啦，，，
//	 * @param 需要被整理的数据集合
//	 * @return 一个新的集合
//	 * @author 刘星星
//	 * @createDate 2013.2.1
//	 */
//	private List<List<HashMap<String, Object>>> getChildArray(List<List<HashMap<String, Object>>> oldList){
//		List<List<HashMap<String, Object>>> newList = new ArrayList<List<HashMap<String, Object>>>();
//		for(int i=0;i<groupArray.size();i++){//子集的个数当然是父控件的个数啦。。。。。
//			HashMap<String,Object> groupMap = groupArray.get(i);//得到每一个父控件。。。。。。。。
//			String parentid = groupMap.get("id").toString();//拿到父控件的id啦，方便后面与子集中的parentid进行对比。这是一一对应的啦。。。看似很方便哦。。。。
//			List<HashMap<String, Object>> newChildList = new ArrayList<HashMap<String,Object>>();//这个就是一个新的子集了。。
//			for(int j=0;j<oldList.size();j++){//对子集集合进行遍历
//				List<HashMap<String, Object>> oldChildList = (List<HashMap<String, Object>>) oldList.get(j);//得到每个子集集合对象中的每一个子集
//				for(int k=0;k<oldChildList.size();k++){//遍历每一个子集中的对象
//					HashMap<String,Object> childMap = oldChildList.get(k);//得到每一个子集对象
//					String childId = childMap.get("parentid").toString();//得到每一个子集的父id啦。。。。。。。
//					if(parentid.equals(childId)){//把父id和子id进行对比啦，如果相同的话就加入新的子集集合中喽。
//						newChildList.add(childMap);
//					}
//				}
//			}
//			newList.add(newChildList);
//		}
//		return newList;
//	}
	/**
	 * 根据不同的用户类型加载功能区的不同数据
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/7
	 */
	private void initGVData(int type) {
		mainGvList = new ArrayList<HashMap<String, Object>>();
		switch (type) {
		case Constant.TYPE_PROPRIETOR://业主
			mainGvList.clear();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico1);
			map.put("mainTv", "房源管理");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico2);
			map.put("mainTv", "推送管理");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico3);
			map.put("mainTv", "站内信");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico4);
			map.put("mainTv", "联系人");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico8);
			map.put("mainTv", "客户需求");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico9);
			map.put("mainTv", "潜在客户");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			break;
		case Constant.TYPE_AGENCY:
			mainGvList.clear();
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico2);
			map.put("mainTv", "推送管理");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico5);
			map.put("mainTv", "搜索写字楼");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico6);
			map.put("mainTv", "收藏管理");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico3);
			map.put("mainTv", "站内信");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico4);
			map.put("mainTv", "联系人");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			break;
		case Constant.TYPE_CLIENT:
			mainGvList.clear();
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico5);
			map.put("mainTv", "搜索写字楼");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico7);
			map.put("mainTv", "需求管理");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico6);
			map.put("mainTv", "收藏管理");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico3);
			map.put("mainTv", "站内信");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico4);
			map.put("mainTv", "联系人");
			map.put("mainBtn", 0);
			mainGvList.add(map);
			break;
		}
		notifyGridView();
	}
	/**
	 * 动态改变主页功能数字
	 * @author 刘星星
	 * @createDate 2013/2/28
	 */
	public void getNumber(){
		int typeUser = MyApplication.getInstance(this).getRoleid();
		HashMap<String, Object> map = null;
		switch(typeUser){
		case Constant.TYPE_PROPRIETOR://业主
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico2);
			map.put("mainTv", "推送管理");
			map.put("mainBtn", 0/*MyApplication.getInstance().getPushcount()*/);
			mainGvList.set(1, map);
			
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico3);
			map.put("mainTv", "站内信");
			map.put("mainBtn", MyApplication.getInstance().getLettercount());
			mainGvList.set(2, map);
			
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico8);
			map.put("mainTv", "客户需求");
			map.put("mainBtn", MyApplication.getInstance().getRcount());
			mainGvList.set(4, map);
			notifyGridView();
			break;
		case Constant.TYPE_CLIENT://客户
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico7);
			map.put("mainTv", "需求管理");
			map.put("mainBtn", MyApplication.getInstance().getRcount());
			mainGvList.set(1, map);
			
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico3);
			map.put("mainTv", "站内信");
			map.put("mainBtn", MyApplication.getInstance().getLettercount());
			mainGvList.set(3, map);
			notifyGridView();
		break;
		case Constant.TYPE_AGENCY://中介
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico2);
			map.put("mainTv", "推送管理");
			map.put("mainBtn", MyApplication.getInstance().getPushcount());
			mainGvList.set(0, map);
			
			map = new HashMap<String, Object>();
			map.put("mainImage", R.drawable.soso_index_ico3);
			map.put("mainTv", "站内信");
			map.put("mainBtn", MyApplication.getInstance().getLettercount());
			mainGvList.set(3, map);
			notifyGridView();
			break;
		}
	}
	/**
	 * 刷新功能控件布局 根据不同的用户类型给功能区控件加载数据
	 */
	MainGridViewAdapter gvAdapter = null;//主页功能适配器
	private void notifyGridView() {
		gvAdapter = new MainGridViewAdapter(this, mainGvList); 
		mainGv.setAdapter(gvAdapter);
		mainGv.setCacheColorHint(0);
		mainGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// mainGv.setOnItemClickListener(new GvItemClickListener());
	}

	class GvItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg2 == 2) {
				Intent intent = new Intent(MainActivity.this,
						StationInLetterActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// 把一个Activity转换成一个View
				Window w = MainFirstTab.group.getLocalActivityManager()
						.startActivity("StationInLetterActivity", intent);
				View view = w.getDecorView();
				// 把View添加大ActivityGroup中
				MainFirstTab.group.setContentView(view);
			}
		}

	}

	/**
	 * 设置ExpandableListView默认为子项全部打开
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/7
	 */
	private void setExpandableViewOpen() {
//		int groupCount = mainListView.getCount();
//		for (int i = 0; i < groupCount; i++) {
//			mainListView.expandGroup(i);
//		}
	}
//	/**
//	 * 点击商圈控件时弹出对话框
//	 * @author 刘星星
//	 * @createDate 2013//3/22
//	 */
//	Dialog ad;
//	private ImageView imageView;
//	private TextView currentCityLayout;
////	private TextView currentCityText;
//	public void showDialogBusiness(){
//		ad = new Dialog(this.getParent(),R.style.dialog);
//		View view = LayoutInflater.from(this.getParent()).inflate(R.layout.dialog_selectbusiness, null);
//		LayoutParams params = new LayoutParams(440,
//				LayoutParams.WRAP_CONTENT);
//		ad.addContentView(view, params);
//		ad.show();
////		Window window = ad.getWindow();
////		window.setBackgroundDrawable(new ColorDrawable(0));  
////		window.setContentView(R.layout.dialog_selectbusiness); 
//		
//		searchRoomText = (TextView) view.findViewById(R.id.searchRoomText);
//		mainListView = (ExpandableListView) view.findViewById(R.id.mainListView);
//		searchAreaEt = (EditText) view.findViewById(R.id.searchAreaEt);
//		imageView = (ImageView) view.findViewById(R.id.mainListJiantou);
//		currentCityLayout = (TextView)  view.findViewById(R.id.mainTopBtnLinear);
//		currentCityLayout.setOnClickListener(this);
////		currentCityText = (TextView)  view.findViewById(R.id.mainTopCityText);
////		if(MyApplication.getInstance().getNotlogin_search()==1){
////			currentCityText.setText(cityname_qiehuan);
////		}else{
//			currentCityLayout.setVisibility(View.GONE);
////			currentCityText.setVisibility(View.GONE);
////		}
//		searchAreaEt.addTextChangedListener(new MyTextWatcher());
//		mainListView.setOnChildClickListener(new AreaListChildClickListener());
//		imageView.setOnClickListener(this);
//		ad.setCancelable(true);
//		ad.setCanceledOnTouchOutside(true);
//		noLimitText = (TextView) view.findViewById(R.id.noLimitText);
////		noLimitText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
////		noLimitText.setText(Html.fromHtml("(xuqingfeng77博客)"));
//		noLimitText.setOnClickListener(this);
//		noLimitText.setText(cityname);
//		initAreaList1();
//	}
	@Override
	public void onClick(View v) {
		if (v == mainListJianTou) {//隐藏选择商圈的布局
			searchListLayout.setVisibility(View.GONE);
//			searchRoomEt.setText(searchRoomText.getText().toString());
			CommonUtils.hideSoftKeyboard(MainActivity.this);
		}else if(v == mainTopBtnLinear){//选择城市
			Intent intent = new Intent(MainActivity.this, CityListActivity.class);
			startActivity(intent);
			finish();
		} else if (v == searchEtLayout) {//收缩下拉框 点击会弹出商圈的数据列表
			handler.sendEmptyMessage(1);
			searchRoomText.setText(cityname);
			searchListLayout.setVisibility(View.VISIBLE);
//			showDialogBusiness();
		} else if (v == searchBtn) {
			Intent intent = new Intent(this, SearchRoomsListActivity.class);
			intent.putExtra("whereclause", getWhereClause());
			intent.putExtra("cityid", cityid);
			intent.putExtra("areaid", areaid);
			intent.putExtra("districtid", districtid);
			intent.putExtra("officetype", "");
			intent.putExtra("priceup", "20");
			intent.putExtra("pricedown", "0");
			intent.putExtra("areaup", "0");
			intent.putExtra("areadown", "0");
			intent.putExtra("businessname",searchRoomText.getText()) ;
			intent.putExtra("longitude",longitude);
			intent.putExtra("latitude",latitude) ;
			startActivity(intent);
		}else if(v == noLimitText){//设置搜索的商圈为不限定
			searchRoomText.setText(cityname);
			districtid = "";
//			searchRoomEt.setText(cityname);
			searchListLayout.setVisibility(View.GONE);
			CommonUtils.hideSoftKeyboard(MainActivity.this);
			Intent intent = new Intent(MainActivity.this, SearchRoomsListActivity.class);
			intent.putExtra("whereclause", getWhereClause());
			intent.putExtra("cityid", cityid);
			intent.putExtra("areaid", "");
			intent.putExtra("districtid", "");
			intent.putExtra("officetype", "");
			intent.putExtra("priceup", "20");
			intent.putExtra("pricedown", "0");
			intent.putExtra("areaup", "0");
			intent.putExtra("areadown", "0");
//			intent.putExtra("businessname",searchRoomText.getText()) ;
			intent.putExtra("longitude","");
			intent.putExtra("latitude","") ;
			startActivity(intent);
		}else if(v == searchRoomEt){
			Intent intent = new Intent(this,InputSelectActivity.class);
			startActivity(intent);
		}else if(v == noDataLayout){
			
		}
			/*else if(v == imageView){//隐藏选择商圈的对话框
		}*/
		super.onClick(v);
	}
	
	/**
	 * 整理的搜索字段字符
	 *
	 * 作者：Ring
	 * 创建于：2013-2-28
	 * @return
	 */
	public String getWhereClause(){
		StringBuffer whereclause = new StringBuffer("");
		whereclause.append(" ischecked=1 and isrent=0 and");
		whereclause.append(" BuildID in ( select BuildID from TBuild where");
		if(cityid!=null&&!cityid.trim().equals("")){
			whereclause.append(" CityID='"+cityid+"' and");
		}
		if(areaid!=null&&!areaid.trim().equals("")){
			whereclause.append(" AreaID='"+areaid+"' and");
		}
		if(districtid!=null&&!districtid.trim().equals("")){
			whereclause.append(" DistrictID='"+districtid+"' and");
		}
		String whereString = "1=1";
		if(whereclause!=null&&whereclause.length()>3){
			whereString=whereclause.subSequence(0, whereclause.length()-3).toString()+")";
		}
		return whereString;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (data != null) {
				String city = data.getStringExtra("city").toString();
				cityid = data.getStringExtra("cityid").toString();
//				new Thread(runnable).start();
				if (city != null && !city.equals("")) {
					if(city.length()>4){
						city = city.substring(0, 4)+"...";
					}
					mainTopCityText.setText(city);
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	if(keyCode == KeyEvent.KEYCODE_BACK){
		showExitDialog();
	}
	return true;
}
	@Override
	protected void onDestroy() {
//		Intent intent = new Intent(this, SoSoOfficeReceiver.class);
//		intent.putExtra("tag", 2);
//		this.sendBroadcast(intent);
		runnable_tag = true;
		click_limit = true;
		if (uploaddata != null) {
			uploaddata.overReponse();
		}
		super.onDestroy();
	}

//	/**
//	 * author by Ring 获取省市区数据
//	 */
//	public void getZone() {
//		List<Map<String, Object>> selectresult = null;
//		selectresult = DBHelper.getInstance(this).selectRow(
//				"select * from soso_zoneinfo", null);
//		if (selectresult == null || selectresult.size() <= 0) {
//			uploaddata = new SoSoUploadData(this, "GetProvCityAreaAll.aspx");
//			uploaddata.Post();
//			reponse = uploaddata.getReponse();
//			dealReponse1();
//		}
//	}

//	/***
//	 * author by Ring 将获取的省市区数据保存到本地数据库
//	 */
//	private void dealReponse1() {
//		if (StringUtils.CheckReponse(reponse)) {
//			Type listType = new TypeToken<LinkedList<SoSoZoneInfo>>() {
//			}.getType();
//			Gson gson = new Gson();
//			ContentValues values = new ContentValues();
//			LinkedList<SoSoZoneInfo> zoneinfos = null;
//			SoSoZoneInfo zoneinfo = null;
//			try {
//				zoneinfos = gson.fromJson(reponse, listType);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			List<Map<String, Object>> selectresult = null;
//			if (zoneinfos != null && zoneinfos.size() > 0) {
//				for (Iterator<SoSoZoneInfo> iterator = zoneinfos.iterator(); iterator
//						.hasNext();) {
//					zoneinfo = (SoSoZoneInfo) iterator.next();
//					values.put("id", zoneinfo.getId());
//					values.put("name", zoneinfo.getName());
//					values.put("typeid", zoneinfo.getTypeid());
//					values.put("parentid", zoneinfo.getParentid());
//					selectresult = DBHelper.getInstance(this).selectRow(
//							"select * from soso_zoneinfo where id = '"
//									+ zoneinfo.getId() + "' and typeid = '"+zoneinfo.getTypeid()+"'", null);
//					if (selectresult != null) {
//						if (selectresult.size() <= 0) {
//							DBHelper.getInstance(this).insert("soso_zoneinfo",
//									values);
//						} else {
//							DBHelper.getInstance(this).update("soso_zoneinfo",
//									values, "id = ? and typeid = ?",
//									new String[] { zoneinfo.getId(),zoneinfo.getTypeid() });
//						}
//						selectresult.clear();
//						selectresult = null;
//					}
//					values.clear();
//				}
//			}
//			if (values != null) {
//				values.clear();
//				values = null;
//			}
//			if (zoneinfos != null) {
//				zoneinfos.clear();
//				zoneinfos = null;
//			}
//			DBHelper.getInstance(this).close();
//		}
//	}
	
//	/***
//	 * 获取所有商圈信息
//	 */
//
//	public void getAllDistrict() {
//		String sql = "select id,name from soso_zoneinfo where parentid='" + cityid
//				+ "'";
//		List<Map<String, Object>> selectresult = null;
//		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
//		if (selectresult != null && selectresult.size() > 0) {
//			int i;
//			for (i = 0; i < selectresult.size(); i++) {
//				if(runnable_tag){
//					break;
//				}
//				if (selectresult.get(i).get("id") != null
//						&& selectresult.get(i).get("name") != null) {
////					getDistrictByAreaId(selectresult.get(i).get("id").toString());
//				}
//			}
//		}
//	}

//	/***
//	 * 根据区域获取商圈信息
//	 */
//
//	public void getDistrictByAreaId(String areaid) {
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
//				this).getAPPID()));
//		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
//				this).getAPPKEY()));
//		params.add(new BasicNameValuePair("AreaID", areaid));
//		uploaddata = new SoSoUploadData(this, "DistrictSelect.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		dealReponse2(areaid);
//		params.clear();
//		params = null;
//	}
	
	
	
	/***
	 * 根据城市id获取商圈信息
	 */

	public void getDistrictByCityId(String cityid) {
		if(cityid==null||
				cityid.equals("")){
			return;
		}
		String updatedate = "";
		String userid = "";
		if((MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null)){
			userid = "0";
			updatedate ="";
		}else{
			userid = MyApplication.getInstance(this).getSosouserinfo().getUserID();
		}
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='TDISTRICT_TIME' and updatedate = value and value<>'' and userid = '"
				+userid+"'",
						null).size()>0
						&&ZoneUtil.getregion(this, "SELECT * FROM district left join region on district.regionid = region.id " +
								"and type = 3 " +
								"and region.parentid = '"+cityid+"' " +
								"where region.parentid not null").size()>0){
			return;
		}
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TDISTRICT_TIME' and userid = '"
				+userid+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get("updatedate") != null
					&&ZoneUtil.getregion(this, "SELECT * FROM district left join region on district.regionid = region.id " +
							"and type = 3 " +
							"and region.parentid = '"+cityid+"' " +
							"where region.parentid not null").size()>0) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(
						"updatedate").toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UpdateDate", updatedate));
		params.add(new BasicNameValuePair("CityID", cityid));
		uploaddata = new SoSoUploadData(this, "DistrictByCityIDSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse2(userid);
		params.clear();
		params = null;
	}
	/***
	 * author by Ring 将获取的区域数据保存到本地数据库
	 */
	private void dealReponse2(String userid) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TDISTRICT_TIME' and userid = '"
					+userid+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TDISTRICT_TIME' and userid = '"
					+userid+"'");
			Type listType = new TypeToken<LinkedList<SoSoDistrictInfo>>() {
			}.getType();
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			LinkedList<SoSoDistrictInfo> districtinfos = null;
			SoSoDistrictInfo districtinfo = null;
			districtinfos = gson.fromJson(reponse, listType);
			List<Map<String, Object>> selectresult = null;
//			SQLiteDatabase db = openDatabase(this);
			if (districtinfos != null && districtinfos.size() > 0) {
				for (Iterator<SoSoDistrictInfo> iterator = districtinfos
						.iterator(); iterator.hasNext();) {
					districtinfo = (SoSoDistrictInfo) iterator.next();
					values.put("areaid", districtinfo.getAreaID());
					values.put("districtid", districtinfo.getDistrictID());
					values.put("districtmc", districtinfo.getDistrictMC());
					values.put("latitude", districtinfo.getLatitude());
					values.put("longitude", districtinfo.getLongitude());
					selectresult = DBHelper.getInstance(this).selectRow(
							"select * from soso_districtinfo where districtid = '"
									+ districtinfo.getDistrictID() + "'", null);
					if (selectresult != null) {
						if (selectresult.size() <= 0) {
							DBHelper.getInstance(this).insert(
									"soso_districtinfo", values);
						} else {
							DBHelper.getInstance(this)
									.update("soso_districtinfo",
											values,
											"districtid = ?",
											new String[] { districtinfo
													.getDistrictID() });
						}
						selectresult.clear();
						selectresult = null;
					}
					values.clear();
					
//					values.put("id", districtinfo.getDistrictID());
//					values.put("name",districtinfo.getDistrictMC());
//					values.put("regionid",districtinfo.getAreaID());
//					try{
//						values.put("longitude", districtinfo.getLongitude());
//					}catch(Exception e){
//						values.put("longitude", "");
//					}
//					try{
//						values.put("latitude", districtinfo.getLatitude());
//					}catch(Exception e){
//						values.put("latitude", "");
//					}
//					selectresult = selectRow(db,"select * from district where id='"+ districtinfo
//							.getDistrictID() +"'",
//							null);
//					if (selectresult != null) {
//						if (selectresult.size() <= 0) {
//							db.insert("district", null, values);
//						} else {
//							db.update("district", values, "id=?", new String[] { districtinfo
//									.getDistrictID() });
//						}
//						selectresult.clear();
//						selectresult = null;
//					}
//					values.clear();
				}
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			if (districtinfos != null) {
				districtinfos.clear();
				districtinfos = null;
			}
//			if(db!=null){
//				db.close();
//			}
			DBHelper.getInstance(this).close();
		}
	}
	
//	private static final String TAG = "region.sqlite.sqlite";
//	
//	private static String DATABASE_FILENAME = "region.sqlite.sqlite";
//	
//	// 初始化数据库
//		private  SQLiteDatabase openDatabase(Context context) {
//			final String DATABASE_PATH = android.os.Environment
//					.getDataDirectory().getAbsolutePath() + "/data/"+context.getPackageName();
//			try {
//				String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
//				File dir = new File(databaseFilename);
//				if(!dir.exists()&&!dir.isFile()){
////					dir.mkdirs();
//				}
//				if (!dir.exists()&&!dir.isFile()) {
//					InputStream is = context.getAssets().open(DATABASE_FILENAME);
//					FileOutputStream fos = new FileOutputStream(databaseFilename);
//					byte[] buffer = new byte[8192];
//					int count = 0;
//					while ((count = is.read(buffer)) > 0) {
//						fos.write(buffer, 0, count);
//					}
//					fos.close();
//					is.close();
//				}
//				SQLiteDatabase db;
//				db = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
////				importData(db,context);
//				return db;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return null;
//		}
//		
//		
//		/**
//		 * 将本地数据库的商圈数据导入外部数据库
//		 */
//		
//		public static void importData(SQLiteDatabase db,Context context){
//			List<Map<String, Object>> selectresult = null;
//			selectresult = DBHelper.getInstance(context).selectRow(
//					"select * from soso_districtinfo",
//					null);
//			ContentValues values = new ContentValues();
//			if (selectresult != null) {
//				if (selectresult.size()> 0) {
//					int i;
//					List<Map<String, Object>> selectresult1 = null;
//					for(i=0;i<selectresult.size();i++){
//						if(selectresult.get(i).get("districtid")!=null
//								&&selectresult.get(i).get("districtmc")!=null
//								&&selectresult.get(i).get("areaid")!=null){
//							values.put("id", selectresult.get(i).get("districtid").toString());
//							values.put("name",selectresult.get(i).get("districtmc").toString());
//							values.put("regionid",selectresult.get(i).get("areaid").toString());
//							try{
//								values.put("longitude", selectresult.get(i).get("longitude").toString());
//							}catch(Exception e){
//								values.put("longitude", "");
//							}
//							try{
//								values.put("latitude", selectresult.get(i).get("latitude").toString());
//							}catch(Exception e){
//								values.put("latitude", "");
//							}
//							selectresult1 = selectRow(db,"select * from district where id='"+selectresult.get(i).get("districtid").toString()+"'",
//									null);
//							if (selectresult1 != null) {
//								if (selectresult1.size() <= 0) {
//									db.insert("district", null, values);
//								} else {
//									db.update("district", values, "id=?", new String[] {selectresult.get(i).get("districtid").toString()});
//								}
//								selectresult1.clear();
//								selectresult1 = null;
//							}
//							values.clear();
//						}
//						
//					} 
//				}
//				if (values != null) {
//					values.clear();
//					values = null;
//				}
//				if (selectresult != null) {
//					selectresult.clear();
//					selectresult = null;
//				}
//			}
//		}
		
		/***
		 * 根据sql语句获取数据；
		 * 
		 * @param sql
		 *            语句
		 * @param selectionArgs
		 *            显示的字段
		 * @return List<Map<String, Object>>
		 */

		public static List<Map<String, Object>> selectRow(SQLiteDatabase db,String sql,
				String[] selectionArgs) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			Cursor mCursor = null;
			try {
				mCursor = db.rawQuery(sql, selectionArgs);
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
				Log.e("sqlite",
						"selectRow error:" + sql + "\nException:" + e.getMessage());
			} finally {
				if (mCursor != null) {
					mCursor.close();
				}
			}
			return result;
		}
	
//	/***
//	 * author by Ring 将获取的区域数据保存到本地数据库
//	 */
//	private void dealReponse2(String areaid) {
//		if (StringUtils.CheckReponse(reponse)) {
//			Type listType = new TypeToken<LinkedList<SoSoDistrictInfo>>() {
//			}.getType();
//			Gson gson = new Gson();
//			ContentValues values = new ContentValues();
//			LinkedList<SoSoDistrictInfo> districtinfos = null;
//			SoSoDistrictInfo districtinfo = null;
//			districtinfos = gson.fromJson(reponse, listType);
//			List<Map<String, Object>> selectresult = null;
//			if (districtinfos != null && districtinfos.size() > 0) {
//				for (Iterator<SoSoDistrictInfo> iterator = districtinfos
//						.iterator(); iterator.hasNext();) {
//					districtinfo = (SoSoDistrictInfo) iterator.next();
//					values.put("areaid", areaid);
//					values.put("districtid", districtinfo.getDistrictID());
//					values.put("districtmc", districtinfo.getDistrictMC());
//					selectresult = DBHelper.getInstance(this).selectRow(
//							"select * from soso_districtinfo where districtid = '"
//									+ districtinfo.getDistrictID() + "'", null);
//					if (selectresult != null) {
//						if (selectresult.size() <= 0) {
//							DBHelper.getInstance(this).insert(
//									"soso_districtinfo", values);
//						} else {
//							DBHelper.getInstance(this)
//									.update("soso_districtinfo",
//											values,
//											"districtid = ?",
//											new String[] { districtinfo
//													.getDistrictID() });
//						}
//						selectresult.clear();
//						selectresult = null;
//					}
//					values.clear();
//				}
//			}
//			if (values != null) {
//				values.clear();
//				values = null;
//			}
//			if (districtinfos != null) {
//				districtinfos.clear();
//				districtinfos = null;
//			}
//			DBHelper.getInstance(this).close();
//		}
//	}
	
	
	
	
	/**
	 * author by Ring 处理耗时操作
	 */
//	public Runnable runnable = new Runnable() {
//
//		@Override
//		public void run() {
//			if (click_limit) {
//				click_limit = false;
//			} else {
//				return;
//			}
//			if (NetworkCheck.IsHaveInternet(MainActivity.this)) {
//				handler.sendEmptyMessage(5);// 没有网络时给用户提示
//				if(!runnable_tag){
//					getCount();
//				}
//				if(!runnable_tag){
//				}
//				if(runnable_tag){
//					runnable_tag = false;
//					click_limit = true;
//					return;
//				}
//				handler.sendEmptyMessage(6);// 没有网络时给用户提示
//				handler.sendEmptyMessage(1);//登录成功后
//			} else {
//				handler.sendEmptyMessage(4);// 没有网络时给用户提示
//
//			}
//			click_limit = true;
//		}
//	};
	
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 从登录界面跳转到主界面
				initAreaList1();
//				if(MyApplication.getInstance(LoginActivity.this).getRoleid()==0){
//					i.setClass(LoginActivity.this, RegisterSecondActivity.class);
//					LoginActivity.this.startActivity(i);
//					LoginActivity.this.finish();
//				}else{
//					i.putExtra("tag", 1);
//					i.setClass(LoginActivity.this, MainTabActivity.class);
//					LoginActivity.this.startActivity(i);
//					LoginActivity.this.finish();
//				}
				
				break;
			case 2:// 从登录界面跳转到注册界面
//				i.setClass(LoginActivity.this, RegisterFirstActivity.class);
//				LoginActivity.this.startActivity(i);
//				LoginActivity.this.finish();
				break;
			case 3:// 登录失败给用户提示
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else {
					errormsg = getResources().getString(
							R.string.login_error_check);
				}
				MessageBox.CreateAlertDialog(errormsg, MainActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(MainActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(MainActivity.this.getParent());
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_login));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						click_limit = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
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
			}
		};
	};
}
