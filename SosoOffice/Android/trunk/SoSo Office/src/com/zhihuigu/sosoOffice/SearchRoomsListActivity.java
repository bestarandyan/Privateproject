/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.MainListAdapter;
import com.zhihuigu.sosoOffice.Adapter.SearchRoomsListAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.AreaSelectView;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.MapNumberInfo;
import com.zhihuigu.sosoOffice.model.SoSoOfficeInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;
import com.zhihuigu.sosoOffice.utils.ZoneUtil;

/**
 * @author 刘星星
 * @createDate 2013/1/25
 *
 */
public class SearchRoomsListActivity extends Activity implements
		Activity_interface, OnClickListener, OnItemClickListener,
		IXListViewListener {
	// 返回按钮 地址按钮 地图搜索按钮
	private Button backBtn, addressBtn, searchBtn;
	// 类型布局 价格布局 面积布局 周边搜索布局
	private RelativeLayout typeLayout, priceLayout, acreageLayout, rimLayout;
	private XListView roomsLV;// 房源列表
	// 房源类型 价格 面积 查看周边房源 的文本控件 用来控制其字体的颜色
	private TextView typeText, priceText, acreageText, rimText;
	// 房源类型 价格 面积 查看周边房源布局中的右边的箭头控件 用来控制其颜色
	private ImageView typeImg, priceImg, acreageImg, rimImg;
	// 房源数据列表集合
	private ArrayList<HashMap<String, Object>> roomsListData;
	// 楼盘的集合
	private ArrayList<HashMap<String, Object>> loupanlistdata;

	// 楼盘的集合
	private ArrayList<HashMap<String, Object>> loupanlistdata1;
	// 列表适配器
	private SearchRoomsListAdapter adapter;

	private LinearLayout lookMoreInfoLayout;
	private TextView lookMoreInfoText;
	// ------------------------地图需要的变量 start------------------------------------

	// 初始为于勉庄
	private double userLongitude = 33.49087222349736 * 1E6;// 纬度
	private double userLatitude = 115.27130064453128 * 1E6;// 经度
	// 添加百度相关控件
	private MapView mMapView;
	private BMapManager bMapManager;// 加载地图的引擎
	// 百度地图上的key值
	private String keyString = "01331AFA954E7E300428A5F0C9C829E0E16F87A3";
	// 在百度地图上添加一些控件，例如放大、缩小
	private MKSearch mSearch = null;
//	private BMapManager bMapManager;// 加载地图的引擎
	public MKMapViewListener mMapListener = null;
	// 定位相关
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
	    public NotifyLister mNotifyer=null;
	    MyLocationOverlay myLocationOverlay = null;
	    LocationData locData = null;
	// 在百度地图上添加一些控件，例如放大、缩小
	private MapController mMapController;
//	private MKLocationManager mLocationManager;
	private GeoPoint mypoint;
	// --------------------------end------------------------------------------------------------------
	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private String whereclause = "";// 搜索房源的条件语句；
	private int searchtype = -1;// 0,全部搜索，1，房源类型搜索，2，价格搜索，3，面积搜索，4，周边搜索,5,点击更多，6，点击商圈查询
	private boolean isclickmore = false;
	private SeekBar seekBar;// 选择距离
	private TextView currentDistance;// 显示距离
	private TextView noDataMsg;// 无数据时显示的提示
	private ImageView noDataImg;// 无数据的图片logo展示

	private boolean officetype1 = false;// 房源类型1
	private boolean officetype2 = false;// 房源类型2
	private boolean officetype3 = false;// 房源类型3
	private boolean officetype4 = false;// 房源类型4
	private boolean officetype5 = false;// 房源类型5
	private String priceup = "20";// 价格上限
	private String pricedown = "0";// 价格下限
	private String areaup = "";// 面积上限
	private String areadown = "";// 面积下限
	private int range = 0;// 目标距离;

	private boolean ishasfoot = true;// 是否有更多按钮
	private int map_display_flag = 0;// 0代表应该显示地图 1代表应该显示列表
	private GeoPoint shangquanpoint = null;
	private String districtid = "";// 商圈id
	private String areaid = "";//区id
	private String cityid = "'";
	private String chengshiid = "";
	private String quid = "";
	private String shangquanid = "";
	private String keyword = "";
	private int tag = 0;
	private int type = 1;
	private LinearLayout noDataLayout;
	private int currentType = 2;// 0代表无数据 1代表列表 2代表地图
	private int tag1 = 0;// 第一次进入activity
	private int activity_tag = 0;// 第一次进入activity
	private boolean move_tag = false;// 当为true时不移动地图，当false时移动地图
//	private boolean shangquan_tag = true;// 当为true时商圈是移动地图改变的，false商圈是点击改变的

	private Double shangquan_latitude = 0.0;
	private Double shangquan_longitude = 0.0;

	private LinearLayout countLayout;
	private TextView countText;
	private int mapzoom = 11;

	// private TextView currentCityLayout;
	// private TextView currentCityText;

	private String recordcount = "";
	
	private boolean isshowtoast = false;
	
	private LinearLayout dialogNoDataLayout;//没有数据时显示的布局
	//-----------地图级别   最小缩小到11 市级别的11~13  区级别的14 商圈级别的15~16~18
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DemoApplication app = new DemoApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(DemoApplication.strKey,new DemoApplication.MyGeneralListener());
        }
		setContentView(R.layout.a_searchroomslist);
		map_display_flag = MyApplication.getInstance().getMap_display_flag();
		isshowtoast = true;
		findView();//初始化控件
		initView();//为控件添加监听事件
		locationMap();
		initData();//初始化列表数据
		mSearch.poiSearchInCity(MyApplication.getInstance().getCityname(), "区");
		
	}
	@Override
	public void onClick(View v) {
		isshowtoast = true;
		if(v == backBtn){
//			if(MyApplication.getInstance().getNotlogin_search()==1){
//				Intent i = new Intent();
//				i.setClass(this, LoginActivity.class);
//				this.startActivity(i);
//				this.finish();
//				AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
//				  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
//				  			.setTitle("提示")
//							.setMessage("您确定要退出系统吗？")
//							.setPositiveButton("确定",
//									new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog,
//												int which) {
//											android.os.Process.killProcess(android.os.Process.myPid());
//											finish();
//											System.exit(0);
//										}
//									}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//										public void onClick(DialogInterface dialog,
//												int which) {
//											return ;
//										}
//									});
//				  callDailog.show();
//			}else{
				finish();
//			}
		}else if(v == addressBtn){//选择商圈
//			initDialogWindow(5);
			CommonUtils.hideSoftKeyboard(SearchRoomsListActivity.this);
			showDialogBusiness();
		}else if(v == searchBtn){//切换到地图模式
			if(currentType == 2){//根据列表控件的显示与否来切换房源显示模式
				roomsLV.setVisibility(View.VISIBLE);//切换至列表模式
				currentType = 1;
				roomsListData.clear();
				recordcount = "";
				notifiView();
				searchBtn.setBackgroundResource(R.drawable.soso_map_btn);
				MyApplication.getInstance().setMap_display_flag(1);
				tag  = 0;
				whereclause = getWhereclause();
				new Thread(runnable).start();
				
			} else if (currentType == 1) {// 切换至地图模式
				if(!click_limit){
					return;
				}
				quid = "";
				shangquanid = "";
				chengshiid = "";
				roomsLV.setVisibility(View.GONE);
				noDataLayout.setVisibility(View.GONE);
				countLayout.setVisibility(View.GONE);
				searchBtn.setBackgroundResource(R.drawable.soso_table_btn);
				MyApplication.getInstance().setMap_display_flag(0);
				currentType = 2;
				move_tag = true;
				searchtype = -1;
//				if (mMapView.getZoomLevel() < 11) {
//					mapController.setZoom(11);
//				}
				
				GeoPoint geoPoint = mMapView.getMapCenter();
				mSearch.reverseGeocode(geoPoint);
				// locationOverlayFun();
				// MyLocationFun();
				// getRimRoom();
			}
			
		}else if(v == typeLayout){//按房源类型搜索
			searchtype = 1;
			setViewBackground(true,false,false,false);
			initDialogWindow(3);
		}else if(v == priceLayout){//按价格搜索
			searchtype = 2;
			setViewBackground(false,true,false,false);
			initDialogWindow(1);
			areaSelect = new AreaSelectView(this, 0.0f, 20.0f);
			selectLayout.addView(areaSelect.getView("元"));
			new Thread(priceRunnable).start();
			
		}else if(v == acreageLayout){//按面积搜索
			searchtype = 3;
			setViewBackground(false,false,true,false);
//			initDialogWindow(2);
			showDialogAcreage();
/*			areaSelect = new AreaSelectView(this, 10, 100);
			selectLayout.addView(areaSelect.getView("m²"));
*/		}else if(v == rimLayout){//周边搜索
			searchtype = 4;
			setViewBackground(false,false,false,true);
			initDialogWindow(4);
//			timeSelect = new AreaSelectView(this, 0, 100);
//			selectTimeLayout.addView(timeSelect.getView("天"));
//			distanceSelect = new AreaSelectView(this, 0, 100);
//			selectDistanceLayout.addView(distanceSelect.getView("公里"));
		}else if(v == lookMoreInfoText){//查看更多房源
//			searchtype = 5;
			isclickmore = true;
			new Thread(runnable).start();
		}else if(v == sureSearchBtn){//确认搜索
			keyword = "";
			switch (searchtype) {
			case 0://0,全部搜索，1，房源类型搜索，2，价格搜索，3，面积搜索，4，周边搜索
				dismiss();
//				whereclause = "1=1";
				break;
			case 1:
				//纯0写字楼  3商务中心  4园区  1商住两用2酒店式公寓
				dismiss();
				getofficeType();
//				whereclause = getofficeType();
				break;
			case 2:
				dismiss();
//				whereclause = "PriceUP<= "+areaSelect.maxText.getText().toString()+" and PriceDown>="+areaSelect.minText.getText().toString();
				priceup = areaSelect.maxText.getText().toString();
				pricedown = areaSelect.minText.getText().toString();
				break;
			case 3:
				acreageDialog.dismiss();
//				whereclause = "AreaUP<= "+MaxAcreageEt.getText().toString()+" and AreaDown>="+minAcreageEt.getText().toString();
				areaup = MaxAcreageEt.getText().toString();
				areadown = minAcreageEt.getText().toString();
				MaxAcreageEt.clearFocus();
				minAcreageEt.clearFocus();
				CommonUtils.hideSoftKeyboard(this);
				break;
			case 4:
				dismiss();
				if(MyApplication.getInstance().getMap_display_flag() == 0){
					if(mypoint!=null){
//						if(seekBar.getProgress()<5){
//			        		 mapController.setZoom(18);
//			        	}else if(seekBar.getProgress()<10){
//			        		 mapController.setZoom(17);
//			        	}else if(seekBar.getProgress()<15){
//			        		 mapController.setZoom(16);
//			        	}else if(seekBar.getProgress()<20){
//			        		 mapController.setZoom(15);
//			        	}
						tag = 1;
						mMapView.getController().animateTo(mypoint);
						mSearch.reverseGeocode(mypoint);
						return;
					}
				}
//				whereclause = "1=1";
				range = 0;
				break;

			default:
				break;
			}
			if(MyApplication.getInstance().getMap_display_flag() == 0){
				tag=1;
				whereclause = getWhereclause1();
			}else{
				tag = 0;
				whereclause = getWhereclause();
			}
			new Thread(runnable).start();
			

		}else if(v == imageView){//隐藏商圈对话框
			ad.dismiss();
		}else if(v == noLimitText){//设置搜索的商圈为不限定
//			searchRoomText.setText("");
			districtid = "";
			quid = "";
			shangquanid = "";
			addressBtn.setText(MyApplication.getInstance().getCityname());
			CommonUtils.hideSoftKeyboard(SearchRoomsListActivity.this);
			ad.dismiss();
			mMapView.getController().setZoom(11);
			if(MyApplication.getInstance().getMap_display_flag() == 0){
				tag = 1;
				move_tag = false;
//				shangquan_tag = false;
				mSearch.reverseGeocode(mMapView.getMapCenter());
				return ;
			}
			searchtype = 6;
			whereclause = getWhereclause();
			tag = 0;
			new Thread(runnable).start();
		}else if(v == displayImgBtn){//显示房源图片
			MyApplication.getInstance().setDisplayRoomPhoto(1);
			ContentValues values = new ContentValues();
			values.put("isshowimage", 1);
			String userid = "0";
			if(MyApplication.getInstance().getSosouserinfo()!=null&&
					MyApplication.getInstance().getSosouserinfo().getUserID()==null){
				userid =  MyApplication.getInstance(this)
						.getSosouserinfo(this).getUserID() ;
			}
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] {userid});
			if (values != null) {
				values.clear();
				values = null;
			}
			notifiView();
			dismiss1();
		}else if(v == hideImgBtn){//不显示房源图片
			MyApplication.getInstance().setDisplayRoomPhoto(2);
			ContentValues values = new ContentValues();
			String userid = "0";
			if(MyApplication.getInstance().getSosouserinfo()!=null&&
					MyApplication.getInstance().getSosouserinfo().getUserID()==null){
				userid =  MyApplication.getInstance(this)
						.getSosouserinfo(this).getUserID() ;
			}
			values.put("isshowimage", 2);
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] {userid});
			if (values != null) {
				values.clear();
				values = null;
			}
			notifiView();
			dismiss1();
		}else if(v==noDataImg){
			tag = 0;
			new Thread(runnable).start();
		}/*else if(v == currentCityLayout){
			Intent intent = new Intent(SearchRoomsListActivity.this, CityListActivity.class);
//			intent.putExtra("tag", 1);
			startActivity(intent);
			CommonUtils.hideSoftKeyboard(SearchRoomsListActivity.this);
			if(ad!=null){
				ad.dismiss();
			}
		}*/
	}
	
	/**
	 * 查询条件的拼凑
	 *
	 * 作者：Ring
	 * 创建于：2013-3-18
	 * @return
	 */
	
	public String getWhereclause(){
		StringBuffer whereclause = new StringBuffer("");
//		private boolean officetype1 = false;//房源类型1
//		private boolean officetype2 = false;//房源类型2
//		private boolean officetype3 = false;//房源类型3
//		private boolean officetype4 = false;//房源类型4
//		private boolean officetype5 = false;//房源类型5
//		private String priceup = "0";//价格上限
//		private String pricedown = "0";//价格下限
//		private String areaup = "0";//面积上限
//		private String areadown = "0";//面积下限
		whereclause.append(" ischecked=1 and isrent=0 and ");
		String officetype = getofficeType1();
		if(officetype!=null&&officetype.length()>0){
			whereclause.append(officetype+" and");
		}
		Double priceup_ = 0.0;
		try{
			priceup_ = Double.parseDouble(priceup);
		}catch(Exception e){
			
		}
		if(priceup_<20&&priceup_>0){
			whereclause.append(" PriceUP<='"+priceup+"' and");
		}
		Double pricedown_ = 0.0;
		try{
			pricedown_ = Double.parseDouble(pricedown);
		}catch(Exception e){
			
		}
		if(pricedown_>0&&pricedown_<20){
			whereclause.append(" PriceUP>='"+pricedown+"' and");
		}
		if(areaup!=null&&!areaup.equals("")
				&&!areaup.equals("0")
				&&!areaup.equals("0.0")
				){
			whereclause.append(" AreaUP<='"+areaup+"' and");
		}
		if(areadown!=null&&!areadown.equals("")
				&&!areadown.equals("0")
				&&!areadown.equals("0.0")){
			whereclause.append(" AreaUP>='"+areadown+"' and");
		}
		if (keyword != null && !keyword.trim().equals("")) {
			whereclause.append(" ((Keywords like '%" + keyword + "%' or");
			whereclause.append(" FYJJ like '%" + keyword + "%') or");
			whereclause.append(" BuildID in ( select BuildID from TBuild where");
			whereclause.append(" (BuildMC like '%" + keyword + "%' or");
			whereclause.append(" Address like '%" + keyword + "%'))) and");
		}
		whereclause.append(" BuildID in ( select BuildID from TBuild where");
//		private String chengshiid = "";
//		private String quid = "";
//		private String shangquanid = "";
		if(chengshiid!=null&&!chengshiid.trim().equals("")){
			whereclause.append(" CityID='"+chengshiid+"' and");
		}else if(cityid!=null&&!cityid.trim().equals("")){
			whereclause.append(" CityID='"+cityid+"' and");
		}
		
		if(shangquanid!=null&&!shangquanid.trim().equals("")){
			whereclause.append(" DistrictID='"+shangquanid+"' and");
		}else if(districtid!=null&&!districtid.trim().equals("")){
			whereclause.append(" DistrictID='"+districtid+"' and");
		}
		
		if(quid!=null&&!quid.trim().equals("")){
			whereclause.append(" AreaID='"+quid+"' and");
		}
		String whereString = "1=1";
		if(whereclause!=null&&whereclause.length()>3){
			whereString=whereclause.subSequence(0, whereclause.length()-3).toString()+")";
		}
		return whereString;
	}
	
	/**
	 * 查询地图数字
	 *
	 * 作者：Ring
	 * 创建于：2013-3-18
	 * @return
	 */
	
	public String getWhereclause1(){
		StringBuffer whereclause = new StringBuffer("");
//		private boolean officetype1 = false;//房源类型1
//		private boolean officetype2 = false;//房源类型2
//		private boolean officetype3 = false;//房源类型3
//		private boolean officetype4 = false;//房源类型4
//		private boolean officetype5 = false;//房源类型5
//		private String priceup = "0";//价格上限
//		private String pricedown = "0";//价格下限
//		private String areaup = "0";//面积上限
//		private String areadown = "0";//面积下限
		String officetype = getofficeType1();
		whereclause.append(" ischecked=1 and isrent=0 and");
		if(officetype!=null&&officetype.length()>0){
			whereclause.append(officetype+" and");
		}
		Double priceup_ = 0.0;
		try{
			priceup_ = Double.parseDouble(priceup);
		}catch(Exception e){
			
		}
		if(priceup_<20&&priceup_>0){
			whereclause.append(" PriceUP<='"+priceup+"' and");
		}
		Double pricedown_ = 0.0;
		try{
			pricedown_ = Double.parseDouble(pricedown);
		}catch(Exception e){
			
		}
		if(pricedown_>0&&pricedown_<20){
			whereclause.append(" PriceUP>='"+pricedown+"' and");
		}
		if(areaup!=null&&!areaup.equals("")
				&&!areaup.equals("0")
				&&!areaup.equals("0.0")
				){
			whereclause.append(" AreaUP<='"+areaup+"' and");
		}
		if(areadown!=null&&!areadown.equals("")
				&&!areadown.equals("0")
				&&!areadown.equals("0.0")){
			whereclause.append(" AreaUP>='"+areadown+"' and");
		}
		if (keyword != null && !keyword.trim().equals("")) {
			whereclause.append(" ((Keywords like '%" + keyword + "%' or");
			whereclause.append(" FYJJ like '%" + keyword + "%') or");
			whereclause.append(" BuildID in ( select BuildID from TBuild where");
			whereclause.append(" (BuildMC like '%" + keyword + "%' or");
			whereclause.append(" Address like '%" + keyword + "%'))) and");
		}
		String whereString = "1=1";
		if(whereclause!=null&&whereclause.length()>3){
			whereString=whereclause.subSequence(0, whereclause.length()-3).toString();
		}
		return whereString;
	}
	
	/**
	 * 查询条件的拼凑
	 *
	 * 作者：Ring
	 * 创建于：2013-3-18
	 * @param type 相关类型 1城市 2区，3商圈
	 * @param xgid  相关id
	 * @return
	 */
	public String getWhereclause(int type,String xgid){
		StringBuffer whereclause = new StringBuffer("");
//		private boolean officetype1 = false;//房源类型1
//		private boolean officetype2 = false;//房源类型2
//		private boolean officetype3 = false;//房源类型3
//		private boolean officetype4 = false;//房源类型4
//		private boolean officetype5 = false;//房源类型5
//		private String priceup = "0";//价格上限
//		private String pricedown = "0";//价格下限
//		private String areaup = "0";//面积上限
//		private String areadown = "0";//面积下限
		String officetype = getofficeType1();
		whereclause.append(" ischecked=1 and isrent=0 and ");
		if(officetype!=null&&officetype.length()>0){
			whereclause.append(officetype+" and");
		}
		Double priceup_ = 0.0;
		try{
			priceup_ = Double.parseDouble(priceup);
		}catch(Exception e){
			
		}
		if(priceup_<20&&priceup_>0){
			whereclause.append(" PriceUP<='"+priceup+"' and");
		}
		Double pricedown_ = 0.0;
		try{
			pricedown_ = Double.parseDouble(pricedown);
		}catch(Exception e){
			
		}
		if(pricedown_>0&&pricedown_<20){
			whereclause.append(" PriceUP>='"+pricedown+"' and");
		}
		if(areaup!=null&&!areaup.equals("")
				&&!areaup.equals("0")
				&&!areaup.equals("0.0")
				){
			whereclause.append(" AreaUP<='"+areaup+"' and");
		}
		if(areadown!=null&&!areadown.equals("")
				&&!areadown.equals("0")
				&&!areadown.equals("0.0")){
			whereclause.append(" AreaUP>='"+areadown+"' and");
		}
		
		whereclause.append(" BuildID in ( select BuildID from TBuild where");
		if(cityid!=null&&!cityid.trim().equals("")){
			whereclause.append(" CityID='"+cityid+"' and");
		}
		if(type ==1){
			whereclause.append(" AreaID='"+xgid+"' and");
		}else if(type == 2){
			whereclause.append(" DistrictID='"+xgid+"' and");
		}else if(type == 3){
			whereclause.append(" BuildID='"+xgid+"' and");
		}
		String whereString = "1=1";
		if(whereclause!=null&&whereclause.length()>3){
			whereString=whereclause.subSequence(0, whereclause.length()-3).toString()+")";
		}
		return whereString;
	}
	
	/**
	 * 根据officetype搜索
	 *
	 * 作者：Ring
	 * 创建于：2013-2-28
	 * @return
	 */
	public String getofficeType1(){
		//纯0写字楼  4商务中心 3园区  1商住两用2酒店式公寓
//		private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5;
		StringBuffer whereclause = new StringBuffer("");
		if(officetype1){
			whereclause.append(" OfficeType='"+0+"' or");
		}
		if(officetype2){
			whereclause.append(" OfficeType='"+4+"' or");
		}
		if(officetype3){
			whereclause.append(" OfficeType='"+3+"' or");
		}
		if(officetype4){
			whereclause.append(" OfficeType='"+1+"' or");
		}
		if(officetype5){
			whereclause.append(" OfficeType='"+2+"' or");
		}
		String whereString = "";
		if(whereclause!=null&&whereclause.length()>2){
			whereString=whereclause.subSequence(0, whereclause.length()-2).toString();
			if(whereString.contains("or")){
				whereString = "("+whereString+")";
			}
		}
		return whereString;
	}
	
	/**
	 * 根据officetype搜索
	 *
	 * 作者：Ring
	 * 创建于：2013-2-28
	 * @return
	 */
	public void getofficeType(){
		//纯0写字楼  3商务中心  4园区  1商住两用2酒店式公寓
//		private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5;
//		StringBuffer whereclause = new StringBuffer("");
		if(checkBox1.isChecked()){
//			whereclause.append("OfficeType='"+0+"' or");
			officetype1 = true;
		}else{
			officetype1 = false;
		}
		if(checkBox2.isChecked()){
//			whereclause.append(" OfficeType='"+3+"' or");
			officetype2 = true;
		}else{
			officetype2 = false;
		}
		if(checkBox3.isChecked()){
//			whereclause.append(" OfficeType='"+4+"' or");
			officetype3 = true;
		}else{
			officetype3 = false;
		}
		if(checkBox4.isChecked()){
//			whereclause.append(" OfficeType='"+1+"' or");
			officetype4 = true;
		}else{
			officetype4 = false;
		}
		if(checkBox5.isChecked()){
//			whereclause.append(" OfficeType='"+2+"' or");
			officetype5 = true;
		}else{
			officetype5 = false;
		}
//		String whereString = "1=1";
//		if(whereclause!=null&&whereclause.length()>2){
//			whereString=whereclause.subSequence(0, whereclause.length()-2).toString();
//		}
//		return whereString;
	}
	/**
	 * 当点击某一种搜索条件时，设置相应的布局的样式
	 * @param type 房源类型布局是否被选中
	 * @param price 价格布局是否被选中
	 * @param acreage 面积布局是否被选中
	 * @param rim 周边搜索布局是否被选中
	 * @author 刘星星
	 * @createDate 2013/1/25
	 */
	private void setViewBackground(boolean type,boolean price,boolean acreage,boolean rim){
		if(type){
			typeLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			typeText.setTextColor(Color.WHITE);
			typeImg.setImageResource(R.drawable.soso_house_sanjiao);
		}else{
			typeLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			typeText.setTextColor(Color.BLACK);
			typeImg.setImageResource(R.drawable.soso_house_sanjiao1);
		}
		if(price){
			priceLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			priceText.setTextColor(Color.WHITE);
			priceImg.setImageResource(R.drawable.soso_house_sanjiao);
		}else{
			priceLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			priceText.setTextColor(Color.BLACK);
			priceImg.setImageResource(R.drawable.soso_house_sanjiao1);
		}
		if(acreage){
			acreageLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			acreageText.setTextColor(Color.WHITE);
			acreageImg.setImageResource(R.drawable.soso_house_sanjiao);
		}else{
			acreageLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			acreageText.setTextColor(Color.BLACK);
			acreageImg.setImageResource(R.drawable.soso_house_sanjiao1);
		}
		if(rim){
			rimLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			rimText.setTextColor(Color.WHITE);
			rimImg.setImageResource(R.drawable.soso_house_sanjiao);
		}else{
			rimLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			rimText.setTextColor(Color.BLACK);
			rimImg.setImageResource(R.drawable.soso_house_sanjiao1);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(MyApplication.getInstance().getNotlogin_search()==1){
//			AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
//			  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
//			  			.setTitle("提示")
//						.setMessage("您确定要退出系统吗？")
//						.setPositiveButton("确定",
//								new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int which) {
//										android.os.Process.killProcess(android.os.Process.myPid());
//										finish();
//										System.exit(0);
//									}
//								}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//									public void onClick(DialogInterface dialog,
//											int which) {
//										return ;
//									}
//								});
//			  callDailog.show();
//			  return false;
//		}else{
			finish();
			return true;
//		}
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		addressBtn= (Button) findViewById(R.id.topAddress);
		searchBtn= (Button) findViewById(R.id.mapBtn);
		typeLayout = (RelativeLayout) findViewById(R.id.typeLayout);
		priceLayout = (RelativeLayout) findViewById(R.id.priceLayout);
		acreageLayout = (RelativeLayout) findViewById(R.id.acreageLayout);
		rimLayout = (RelativeLayout) findViewById(R.id.rimLayout);
		roomsLV = (XListView) findViewById(R.id.roomListView);
		typeText = (TextView) findViewById(R.id.typeText);
		priceText = (TextView) findViewById(R.id.priceText);
		acreageText = (TextView) findViewById(R.id.acreageText);
		rimText = (TextView) findViewById(R.id.rimText);
		typeImg = (ImageView) findViewById(R.id.typeImg);
		priceImg = (ImageView) findViewById(R.id.priceImg);
		acreageImg = (ImageView) findViewById(R.id.acreageImg);
		rimImg = (ImageView) findViewById(R.id.rimImg);
		mMapView =(MapView) findViewById(R.id.bmapView); 
		lookMoreInfoLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.view_lookmoreinfo, null);
		lookMoreInfoText = (TextView) lookMoreInfoLayout.findViewById(R.id.lookMoreText);
		noDataLayout = (LinearLayout) findViewById(R.id.noTextLayout);
		noDataMsg = (TextView) findViewById(R.id.noDataMsg);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		countLayout = (LinearLayout) findViewById(R.id.countLayout);
		countText = (TextView) findViewById(R.id.countText);
		
	}

	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
		addressBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		priceLayout.setOnClickListener(this);
		acreageLayout.setOnClickListener(this);
		rimLayout.setOnClickListener(this);
		noDataImg.setOnClickListener(this);
//		lookMoreInfoText.setOnClickListener(this);
//		roomsLV.setOnScrollListener(this);
//		roomsLV.addFooterView(lookMoreInfoLayout);
		roomsLV.setPullLoadEnable(true);
		roomsLV.setPullRefreshEnable(false);
		roomsLV.setXListViewListener(this);
		if(map_display_flag == 0){//0代表应该显示地图  1代表应该显示列表
			countLayout.setVisibility(View.GONE);
			searchBtn.setBackgroundResource(R.drawable.soso_table_btn);
			roomsLV.setVisibility(View.GONE);
			currentType = 2;
		}else if(map_display_flag == 1){//1代表应该显示列表
			searchBtn.setBackgroundResource(R.drawable.soso_map_btn);
			roomsLV.setVisibility(View.VISIBLE);
			currentType = 1;
		}
//		roomsLV.setVisibility(View.GONE);
//		c.setVisibility(View.GONE);
	}

	public void setCheckbox(int officetype){
		switch (officetype) {
		case 0:
			officetype1=true;
			break;
		case 1:
			officetype4=true;
			break;
		case 2:
			officetype5=true;
			break;
		case 3:
			officetype2=true;
			break;
		case 4:
			officetype3=true;
			break;

		default:
			break;
		}
	}
	
	@Override
	public void initData() {
		if(roomsListData==null){
			roomsListData = new ArrayList<HashMap<String,Object>>();
		}
		if(loupanlistdata==null){
			loupanlistdata = new ArrayList<HashMap<String,Object>>();
		}
		if(loupanlistdata1==null){
			loupanlistdata1 = new ArrayList<HashMap<String,Object>>();
		}
		if(getIntent().getStringExtra("whereclause")!=null){
			whereclause = getIntent().getStringExtra("whereclause").toString();
		}
		//----------------------设置checkbox里面的选项
		int officetype = -1;
		try {
			officetype = Integer.parseInt(getIntent().getStringExtra(
					"officetype"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		setCheckbox(officetype);
		//----------------------设置商圈名称
		if(getIntent().getStringExtra("businessname")!=null){
			addressBtn.setText(getIntent().getStringExtra("businessname").toString());
		}
		//----------------------设置商圈id
		if(getIntent().getStringExtra("districtid")!=null){
			districtid = getIntent().getStringExtra("districtid").toString();
		}
		//----------------------设置区id
		if(getIntent().getStringExtra("areaid")!=null){
			areaid = getIntent().getStringExtra("areaid").toString();
		}
		//----------------------设置城市id
		if(getIntent().getStringExtra("cityid")!=null&&!getIntent().getStringExtra("cityid").equals("")){
			cityid = getIntent().getStringExtra("cityid").toString();
		}else{
			cityid = MyApplication.getInstance(this).getCityid();
		}
		//----------------------设置价格
		if(getIntent().getStringExtra("keyword")!=null){
			keyword = getIntent().getStringExtra("keyword").toString();
		}
		if(getIntent().getStringExtra("priceup")!=null){
			priceup = getIntent().getStringExtra("priceup").toString();
		}
		if(getIntent().getStringExtra("pricedown")!=null){
			pricedown = getIntent().getStringExtra("pricedown").toString();
		}
		//----------------------设置面积
		if(getIntent().getStringExtra("areaup")!=null
				&&!getIntent().getStringExtra("areaup").toString().equals("0")
				&&!getIntent().getStringExtra("areaup").toString().equals("0.0")){
			areaup = getIntent().getStringExtra("areaup").toString();
		}
		if(getIntent().getStringExtra("areadown")!=null
				&&!getIntent().getStringExtra("areadown").toString().equals("0")
				&&!getIntent().getStringExtra("areadown").toString().equals("0.0")){
			areadown = getIntent().getStringExtra("areadown").toString();
		}
		if(getIntent().getStringExtra("longitude")!=null
				&&getIntent().getStringExtra("latitude")!=null){
			try{
				shangquanpoint = new GeoPoint((int)(Double.parseDouble(getIntent().getStringExtra("latitude"))*1E6),
						(int)(Double.parseDouble(getIntent().getStringExtra("longitude"))*1E6));
			}catch(Exception e){
			}
		}
		if(!districtid.equals("")){
			String name="";
			name =ZoneUtil.getName(SearchRoomsListActivity.this,districtid);
			if(name.length()>8){
				name = name.substring(0, 8)+"...";
			}
			addressBtn.setText(name);
			mapzoom= 15;
			new Thread(mapzoomrunnable).start();//显示商圈下的楼盘
		}else if(!areaid.equals("")){
			String name="";
			name =ZoneUtil.getName(this,areaid,3);
			if(name.length()>8){
				name = name.substring(0, 8)+"...";
			}
			addressBtn.setText(name);
			mapzoom= 14;
			new Thread(mapzoomrunnable).start();//显示区下面的商圈
		}else{
			String name="";
			name =ZoneUtil.getName(this,cityid,2);
			if(name.length()>8){
				name = name.substring(0,8)+"...";
			}
			addressBtn.setText(name);
			mapzoom= 11;
			new Thread(mapzoomrunnable).start();//显示城市下的区
		}
		if(MyApplication.getInstance().getMap_display_flag()==0){
			quid = "";
			shangquanid = "";
			chengshiid  = "";
			move_tag = false;
			new Thread(mapmoverunnable).start();
			if(shangquanpoint!=null){
				mSearch.reverseGeocode(shangquanpoint);
			}else if(mypoint!=null){
				mSearch.reverseGeocode(mypoint);
				mMapController.animateTo(mypoint);
			}
//			shangquan_tag = false;
			return;
		}
//		cityname = MyApplication.getInstance(this).getCityname();
		if(MyApplication.getInstance().getMap_display_flag()==0){
			tag=1;
		}else{
			tag =0;
			notifiView();
		}
		new Thread(mapmoverunnable).start();
		quid = areaid;
		shangquanid = districtid;
		chengshiid  = cityid;
//	  	mSearch.poiSearchInCity(cityname, "区");
		new Thread(runnable).start();
	}
	
	/**
	 * 刷新列表
	 *
	 * 作者：Ring
	 * 创建于：2013-2-23
	 */
	
//	public void notifyAdapter() {
//		if(adapter!=null){
//			adapter.notifyDataSetChanged();
//		}
//	}

	@Override
	public void notifiView() {
		if(roomsListData==null){
			return;
		}
		if(!ishasfoot){
			roomsLV.addFooterView(lookMoreInfoLayout);
			ishasfoot = true;
		}
		adapter = new SearchRoomsListAdapter(SearchRoomsListActivity.this, roomsListData);
		if(adapter==null && roomsLV == null){
			return;
		}
		roomsLV.setAdapter(adapter);
		roomsLV.setCacheColorHint(0);
		roomsLV.setSelector(new ColorDrawable(Color.TRANSPARENT));
		roomsLV.setOnItemClickListener(this);
		if(roomsListData.size() == 0 && currentType == 1){//因为列表无数据  所以列表消失  无数据状态显示
			noDataLayout.setVisibility(View.VISIBLE);
			countLayout.setVisibility(View.GONE);
			roomsLV.setVisibility(View.GONE);
		}else if(roomsListData.size() > 0 && currentType == 1){//列表显示
			noDataLayout.setVisibility(View.GONE);
			countLayout.setVisibility(View.VISIBLE);
			if(searchtype==4){
				countText.setText(roomsListData.size()+"");
			}else{
				int count = 0;
				try{
					count = Integer.parseInt(recordcount);
				}catch(Exception e){
					
				}
				if(roomsListData.size()==count){
					roomsLV.setPullLoadEnable(false);
				}else{
					roomsLV.setPullLoadEnable(true);
				}
				countText.setText(recordcount);
			}
			
			roomsLV.setVisibility(View.VISIBLE);
		}else if(currentType == 2){//地图显示  列表消失   无数据状态消失
			noDataLayout.setVisibility(View.GONE);
			countLayout.setVisibility(View.GONE);
			roomsLV.setVisibility(View.GONE);
		}
	}
	public void notifiView1(ArrayList<HashMap<String,Object>> list) {
//		ishasfoot = false;
//		roomsLV.removeFooterView(lookMoreInfoLayout);
		adapter = new SearchRoomsListAdapter(this, list);
		roomsLV.setAdapter(adapter);
		roomsLV.setCacheColorHint(0);
		roomsLV.setSelector(new ColorDrawable(Color.TRANSPARENT));
		roomsLV.setOnItemClickListener(this);
	}
	@Override
	protected void onDestroy() {
//			mMapView.setTraffic(false);
		if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        DemoApplication app = (DemoApplication)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }
		if(roomsListData!=null){
			roomsListData.clear();
			roomsListData = null;
		}
		if(loupanlistdata!=null){
			loupanlistdata.clear();
			loupanlistdata = null;
		}
			this.finish();
			System.gc();
			if (bMapManager != null) {
				bMapManager.destroy();
				bMapManager = null;
				}
		super.onDestroy();
	}

		// 停止
		@Override
	protected void onPause() {
			mMapView.onPause();
		super.onPause();
		String userid = "0";
		if (MyApplication.getInstance(this).getSosouserinfo()!= null
				&& MyApplication.getInstance(this).getSosouserinfo()
						.getUserID() != null) {
			userid = MyApplication.getInstance(this).getSosouserinfo().getUserID();
		}
		ContentValues values = new ContentValues();
		values.put("ismapdisplay", MyApplication.getInstance()
				.getMap_display_flag());
		DBHelper.getInstance(this).update(
				"sososettinginfo",
				values,
				"userid = ?",
				new String[] {userid});
		if (values != null) {
			values.clear();
			values = null;
		}

		if (bMapManager != null) {
//			mLocationManager.removeUpdates(locationListener);
			bMapManager.stop();
		}
	}

		@Override
		protected void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			mMapView.onSaveInstanceState(outState);
		}
		   @Override
		protected void onRestoreInstanceState(Bundle savedInstanceState) {
			super.onRestoreInstanceState(savedInstanceState);
			mMapView.onRestoreInstanceState(savedInstanceState);
		}
		// 重启
		@Override
	protected void onResume() {
			mMapView.onResume();
		super.onResume();
//		if (MyApplication.getInstance().getNotlogin_search() == 1) {
//			String cityname = "";
//			MyApplication.getInstance(this).setCityname(
//					ZoneUtil.getCityName(this, MyApplication.getInstance()
//							.getCityid()));
//			if (MyApplication.getInstance(this).getCityid() != null
//					&& !MyApplication.getInstance(this).getCityname()
//							.equals("")) {
//				cityname = MyApplication.getInstance(this).getCityname();
//			}
//			if (cityname.length() > 4) {
//				cityname = cityname.substring(0, 4) + "...";
//			}
//			if (!cityname.equals("")&&!cityname.equals(addressBtn.getText().toString())) {
//				cityname_qiehuan=cityname;
//				addressBtn.setText(cityname);
//				mSearch.poiSearchInCity(cityname, cityname);
//			}
//
//		}
		// quid = "";
		// shangquanid = "";
		// chengshiid = "";
		if (bMapManager != null) {
			bMapManager.start();
		}
	}

	/*--------------------------地图加载需要的函数 start----------------------------------------------------
	 * -------------------------------------------------------------------------------------------------------*/

		/**
	     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	     */
		boolean b = false;
	    public class MyLocationListenner implements BDLocationListener {
	        @Override
	        public void onReceiveLocation(BDLocation location) {
	            if (location == null)
	                return ;
	            if (location != null) {
					userLatitude = location.getLatitude() * 1E6;
					userLongitude = location.getLongitude() * 1E6;
					mypoint = new GeoPoint((int) (userLatitude),
							(int) (userLongitude));
					MyApplication.getInstance().setLatitude((float) userLatitude);
					MyApplication.getInstance().setLongitude((float) userLongitude);
				}
//				if (mMapView.getZoomLevel() < 11) {
//					mapController.setZoom(11);
//				}
//				if(currentCityText.getText().toString().equals("正在定位")){
//					mMapView.getController().animateTo(mypoint);
//					mSearch.reverseGeocode(mypoint);
//				}
				if(MyApplication.getInstance().getMap_display_flag()==0&&shangquanpoint!=null){
					move_tag = true;
//					shangquan_tag = false;
					mSearch.reverseGeocode(shangquanpoint);
//					mMapView.getController().animateTo(shangquanpoint);
					return;
				}else{
					move_tag = true;
//					shangquan_tag = false;
					mSearch.reverseGeocode(mypoint);
					if(activity_tag==0){
						activity_tag=1;
						if(mypoint!=null&&mMapView!=null){
							mMapView.getController().animateTo(mypoint);
						}
					}
				}
				if(shangquanpoint!=null){
//					mMapView.getController().animateTo(shangquanpoint);
				}
	            locData.latitude = location.getLatitude();
	            locData.longitude = location.getLongitude();
	            locData.accuracy = location.getRadius();
	            locData.direction = location.getDerect();
	            myLocationOverlay.setData(locData);
	            mMapView.refresh();
	            if(!b){
	            	mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)), mHandler.obtainMessage(1));
	            }
	            }
	        
	        public void onReceivePoi(BDLocation poiLocation) {
	            if (poiLocation == null){
	                return ;
	            }
	        }
	    }
	    
	    public class NotifyLister extends BDNotifyListener{
	        public void onNotify(BDLocation mlocation, float distance) {
	        }
	    }
		
	    Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            Toast.makeText(SearchRoomsListActivity.this, "msg:" +msg.what, Toast.LENGTH_SHORT).show();
	        };
	    };
	    private void initMapView() {
	        mMapView.setLongClickable(true);
	        //mMapController.setMapClickEnable(true);
	        //mMapView.setSatellite(false);
	    }
		public void locationMap(){
		mMapController = mMapView.getController();
        initMapView();
        mLocClient = new LocationClient( this );
        mLocClient.registerLocationListener( myListener );
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);
        mMapListener = new MKMapViewListener() {
			
			@Override
			public void onMapMoveFinish() {
				keyword = "";
				move_tag = true;
				searchtype = -1;
				if (mMapView.getZoomLevel() < 11) {
					mMapController.setZoom(11);
				}
				System.out.println("地图改变了" + mMapView.getZoomLevel());
				GeoPoint geoPoint = mMapView.getMapCenter();
				if(tag1==0){
					tag1=1;
				}else{
					mSearch.reverseGeocode(geoPoint);
				}
			}
			
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					Toast.makeText(SearchRoomsListActivity.this,title,Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		mMapView.regMapViewListener(DemoApplication.getInstance().mBMapManager, mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
	    myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
		mSearch = new MKSearch();
		mSearch.init(DemoApplication.getInstance().mBMapManager, mkSearchListener);
		
	}
	
	
	public MKSearchListener mkSearchListener = new MKSearchListener() {
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
		}

		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		}

		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			if(mMapView!=null
					&&mSearch!=null
					&&arg0!=null
					&&arg0.getPoi(0)!=null
					&&arg0.getPoi(0).pt!=null){
				if(shangquanpoint!=null){
					mMapView.getController().animateTo(shangquanpoint);
					mSearch.reverseGeocode(shangquanpoint);
				}else{
					mMapView.getController().animateTo(arg0.getPoi(0).pt);
					mSearch.reverseGeocode(arg0.getPoi(0).pt);
				}
			}
		}

		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		}

		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			String chengshiid1 = "";
			String quid1 = "";
//			String shangquanid1="";
			System.out.println("来了");
			  if (arg0 == null) {   
	                return;   
	            }   
			  	MKGeocoderAddressComponent kk=arg0.addressComponents;
			  	chengshiid1 = getCitybySomeThing(kk.city, 2,"");
//			  	MyApplication.getInstance().setCityid(chengshiid1);
//			  	chengshiid1 = MyApplication.getInstance().getCityid();
			  	quid1 = getCitybySomeThing(kk.district, 3,chengshiid1);
			  	if(!chengshiid1.equals(MyApplication.getInstance().getCityid())&&!MyApplication.getInstance().getCityid().equals("")){
			  		chengshiid="";
			  		quid = "";
			  		shangquanid="";
			  		return;
			  	}
//			  	if(MyApplication.getInstance().getNotlogin_search()==1){
//			  		MyApplication.getInstance().setCityid(chengshiid1);
//				  	cityid = chengshiid1;
//				  	String cityname = kk.city;
//				  	if(cityname.length()>4){
//						cityname = cityname.substring(0, 4)+"...";
//					}
//				  	cityname_qiehuan=cityname;
//				  	addressBtn.setText(cityname);
//			  	}
//			  	if(!move_tag){
//			  		mMapView.getController().animateTo(arg0.geoPt);
//			  	}
			  	if(mMapView.getZoomLevel()<=13){
					if(chengshiid1!=null&&!chengshiid1.trim().equals("")&&!chengshiid.equals(chengshiid1)){
						chengshiid = chengshiid1;
						tag  = 1;
						type = 1;
						whereclause = getWhereclause1();
						addressBtn.setText(kk.city);
						new Thread(runnable).start();
					}
				}else if(mMapView.getZoomLevel()<=14){
					if(quid1!=null&&!quid1.trim().equals("")&&!quid.equals(quid1)){
						tag  = 1;
						type = 2;
						quid = quid1;
						whereclause = getWhereclause1();
						addressBtn.setText(kk.district);
						new Thread(runnable).start();
					}
				}else if(mMapView.getZoomLevel() <= 18){
					if(quid1!=null&&!quid1.trim().equals("")){
						shangquan_latitude = arg0.geoPt.getLatitudeE6()/(1E6);
						shangquan_longitude = arg0.geoPt.getLongitudeE6()/(1E6);
						tag  = 1;
						type = 3;
						quid = quid1;
						whereclause = getWhereclause1();
						new Thread(runnable).start();
					}
//					if(shangquan_tag){
//						shangquanid1 = getShangquanID(loupanlistdata1, arg0.geoPt.getLongitudeE6()/(1E6), arg0.geoPt.getLatitudeE6()/(1E6));
//						if(shangquanid1!=null&&!shangquanid1.trim().equals("")&&!shangquanid.equals(shangquanid1)){
//							shangquanid = shangquanid1;
//						}
//					}
//					shangquan_tag = true;
//					if(shangquanid.equals("")){
//						return;
//					}
//					tag  = 1;
//					type = 3;
//					whereclause = getWhereclause1();
//					new Thread(runnable).start();
				}
//				String city=kk.city; 
//				System.out.println(kk.district);
//				System.out.println(city);
//	            System.out.println(arg0.strAddr);
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	public String getCitybySomeThing(String whereClause,int type,String parentid) {
		String id = "";
		String sql = "";
		if(type == 2){
			sql = "select id,name from region where name like '%" + whereClause
					+ "%' and type = '"+type+"'";
		}else if(type == 3){
			sql = "select id,name from region where name like '%" + whereClause
					+ "%' and type = '"+type+"' and parentid = '"+parentid+"'";
		}
		
		List<Map<String, Object>> selectresult = ZoneUtil.getregion(this, sql);
		if (selectresult != null && selectresult.size() > 0
				&&selectresult.get(0).get("id").toString()!=null) {
			id = selectresult.get(0).get("id").toString();
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		return id;
	}
	
	
	//获取经度纬度
	private LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			if (location != null) {
				userLatitude = location.getLatitude() * 1E6;
				userLongitude = location.getLongitude() * 1E6;
				mypoint = new GeoPoint((int) (userLatitude),
						(int) (userLongitude));
				MyApplication.getInstance().setLatitude((float) userLatitude);
				MyApplication.getInstance().setLongitude((float) userLongitude);
			}
//			if (mMapView.getZoomLevel() < 11) {
//				mapController.setZoom(11);
//			}
//			if(currentCityText.getText().toString().equals("正在定位")){
//				mMapView.getController().animateTo(mypoint);
//				mSearch.reverseGeocode(mypoint);
//			}
			if(MyApplication.getInstance().getMap_display_flag()==0&&shangquanpoint!=null){
				move_tag = true;
//				shangquan_tag = false;
				mSearch.reverseGeocode(shangquanpoint);
//				mMapView.getController().animateTo(shangquanpoint);
				return;
			}else{
				move_tag = true;
//				shangquan_tag = false;
				mSearch.reverseGeocode(mypoint);
				if(activity_tag==0){
					activity_tag=1;
					if(mypoint!=null&&mMapView!=null){
						mMapView.getController().animateTo(mypoint);
					}
				}
			}
			if(shangquanpoint!=null){
//				mMapView.getController().animateTo(shangquanpoint);
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	};
//		class OverItemT extends ItemizedOverlay<OverlayItem>{
//			private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
//
//			public OverItemT(Drawable marker, Context context, GeoPoint pt, String title) {
//				super(boundCenterBottom(marker));
//				
//				mGeoList.add(new OverlayItem(pt, title, null));
//
//				populate();
//			}
//
//			@Override
//			protected OverlayItem createItem(int i) {
//				return mGeoList.get(i);
//			}
//
//			@Override
//			public int size() {
//				return mGeoList.size();
//			}
//
//			@Override
//			public boolean onSnapToItem(int i, int j, Point point, MapView mapview) {
//				Log.e("ItemizedOverlayDemo","enter onSnapToItem()!");
//				return false;
//			}
//		}
	/**
	 * @author 刘星星
	 * @createDate 2013/1/26
	 * 获取周边房源并添加标记
	 */
		GeoPoint point = null;
		ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
	private void getRimRoom(){/*
        *//** 
         * 创建自定义的ItemizedOverlay 
         *//*  
        CustomItemizedOverlay overlay = new CustomItemizedOverlay(null, SearchRoomsListActivity.this);
        int count = 0;
        if(loupanlistdata.size()<=0){//当获取的数据的长度为0时，更新地图，移除所有点
        	if(mMapView!=null&&mMapView.getOverlays()!=null){
        		boolean b = false;
            	for(int i=0;i<mMapView.getOverlays().size();i++){
            		if(i != 0){
            			b = true;
            			mMapView.getOverlays().remove(i);
            		} 
            	}
            	//更新地图
            	if(b){
            		mMapView.invalidate();
            	}
            }
        	if(isshowtoast&&MyApplication.getInstance().getMap_display_flag()==0){
        		isshowtoast = false;
        		String errormsg = "";
        		if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = "请求超时";
				} else {
					errormsg = "未搜索到房源";
				}
        		if(!errormsg.equals("")){
        			Toast.makeText(SearchRoomsListActivity.this, errormsg, 2000).show();
        		}
        	}
        	return;
        }
        if(mMapView!=null&&mMapView.getOverlays()!=null){
        	boolean b  = false;
        	for(int i=0;i<mMapView.getOverlays().size();i++){
        		if(i != 0){
        			b = true;
        			mMapView.getOverlays().remove(i);
        		}
        	}
        	if(b){
        		mMapView.invalidate();
        	}
        }
        int i;
//        map.put("id", MapNumberInfo.getId());
//		map.put("name",MapNumberInfo.getName());
//		map.put("number", MapNumberInfo.getNumber());
//		map.put("latitude", MapNumberInfo.getLatitude());
//		map.put("longitude", MapNumberInfo.getLongitude());
        for(i= 0;i<loupanlistdata.size();i++){
        	if(loupanlistdata.get(i).get("id")!=null
        			&&!loupanlistdata.get(i).get("id").toString().trim().equals("")
        			&&loupanlistdata.get(i).get("number")!=null
        			&&!loupanlistdata.get(i).get("number").toString().trim().equals("")
//        			&&loupanlistdata.get(i).get("latitude")!=null
//                	&&!loupanlistdata.get(i).get("latitude").toString().trim().equals("")
//                	&&loupanlistdata.get(i).get("longitude")!=null
//                	&&!loupanlistdata.get(i).get("longitude").toString().trim().equals("")
                	&&loupanlistdata.get(i).get("name")!=null
                	&&!loupanlistdata.get(i).get("name").toString().trim().equals("")){
        		try {
					count = Integer.parseInt(loupanlistdata.get(i)
							.get("number").toString());
				} catch (Exception e) {
				}
        		long distance = 0;
        		if(mMapView.getZoomLevel() <= 13){
        			distance = 100000;
        		}else if(mMapView.getZoomLevel() <= 14){
        			distance = 10000;
        		}else if(mMapView.getZoomLevel() <= 18){
        			distance = 1000;
        		}
        		if(loupanlistdata.get(i).get("latitude")!=null
                    	&&!loupanlistdata.get(i).get("latitude").toString().trim().equals("")
                    	&&loupanlistdata.get(i).get("longitude")!=null
                    	&&!loupanlistdata.get(i).get("longitude").toString().trim().equals("")){
        			point = new GeoPoint((int)(Double.parseDouble(loupanlistdata.get(i).get("latitude").toString())*1E6),
    						(int)(Double.parseDouble(loupanlistdata.get(i).get("longitude").toString())*1E6));
        		}else{
        			point = new GeoPoint(mMapView.getMapCenter().getLatitudeE6()+(int)(distance*i), mMapView.getMapCenter().getLongitudeE6()+(int)(distance*i));
        		}
//        		point = new GeoPoint((int)(Double.parseDouble(loupanlistdata.get(i).get("latitude").toString())*1E6),
//						(int)(Double.parseDouble(loupanlistdata.get(i).get("longitude").toString())*1E6));
            	//   	 创建标记（新疆福海县）   
        		if(point!=null){
        			OverlayItem overlayItem = new OverlayItem(point, loupanlistdata.get(i).get("type").toString(), loupanlistdata.get(i).get("id").toString()
        					+";"+
        					loupanlistdata.get(i).get("name").toString());  //将楼盘的id放在title里面
//                	// 将标记添加到图层中（可添加多个OverlayItem）   
                	if(count>0){
                		int type = 0;
        				try {
        					type = Integer.parseInt(loupanlistdata.get(i).get("type").toString());
        				} catch (Exception e) {
        				}
        				if(type == 1){
        					 Drawable marker = dooble(count,loupanlistdata.get(i).get("name").toString());   
        				        // 为maker定义位置和边界    
        				        marker.setBounds(-10, -60, marker.getIntrinsicWidth()-10, marker.getIntrinsicHeight()-60);   
        					overlayItem.setMarker(marker);
//        					overlayItem.getMarker(0).setBounds(0, 5, 0, 0);
        				}else{
        					overlayItem.setMarker(dooble(count,""));
        				}
                	}
                	overlay.addOverlay(overlayItem);
        		}
        	}
        }
//        while (iterator.hasNext()) {
//        	String buildid = iterator.next();
//        	count = 0;
//        	for(int j = 0 ;j<roomsListData.size();j++){
//        		if(roomsListData.get(j).get("buildid").toString().equals(buildid)){
//        			if(roomsListData.get(j).get("latitude")!=null
//        					&&!roomsListData.get(j).get("latitude").toString().equals("")
//        					&&roomsListData.get(j).get("longitude")!=null
//                					&&!roomsListData.get(j).get("longitude").toString().equals("")){
////        				try {
////							longitude = Double.parseDouble(roomsListData.get(j)
////									.get("longitude").toString());
////						} catch (Exception e) {
////						}
////        				try {
////        					latitude = Double.parseDouble(roomsListData.get(j)
////									.get("latitude").toString());
////						} catch (Exception e) {
////						}
//        			}
//        			count++;
//        		}
//        	}
//
//        	index++;
//        	point = new GeoPoint(latitude+1000*index, longitude+1000*index);
////        	 创建标记（新疆福海县）   
//	        OverlayItem overlayItem = new OverlayItem(point, buildid, "湖南省娄底市新化县");  //将楼盘的id放在title里面
//	        // 将标记添加到图层中（可添加多个OverlayItem）   
//	        overlayItem.setMarker(dooble(count));
//	        overlay.addOverlay(overlayItem);
//		}
//        for(int i=0;i<3;i++){
//	        // 构造一个经纬度点   
//	         point = new GeoPoint(latitude+i*1000, longitude+i*1000);  
//	        // 创建标记（新疆福海县）   
//	        OverlayItem overlayItem = new OverlayItem(point, "刘星星的家乡", "湖南省娄底市新化县");  
//	        // 将标记添加到图层中（可添加多个OverlayItem）   
//	        overlayItem.setMarker(dooble(i*2+i));
//	        overlay.addOverlay(overlayItem);  
//        }
        *//** 
         * 往地图上添加自定义的ItemizedOverlay 
         *//*  
        List<Overlay> mapOverlays = mMapView.getOverlays();  
		if (overlay != null && overlay.size() > 0) {
			mapOverlays.add(overlay);
		}
  
        *//** 
         * 取得地图控制器对象，用于控制MapView 
         *//*  
       
        mMapController = mMapView.getController();  
        if(MyApplication.getInstance().getMap_display_flag()==0&&searchtype == 4){
        }else{
        	if(!move_tag){
        		// 设置地图的中心   
        		if (point != null) {
        			mMapController.animateTo(point);
        		}
                // 设置地图默认的缩放级别   
//                mapController.setZoom(16);  	
        	}else{
//        		move_tag = false;
        	}
        	
        }
        
        mMapView.invalidate();
        

	
	*/}
	

	public ArrayList<HashMap<String,Object>> getBuildInfo(String buildid){
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		int i ;
		for(i=0;i<roomsListData.size();i++){
			if(roomsListData.get(i).get("buildid")!=null&&roomsListData.get(i).get("buildid").toString().equals(buildid)){
				list.add(roomsListData.get(i));
			}
		}
		return list;
	}
	/**
	 * 将房源数量打印在图片上
	 * @param number  房源数量
	 * @author 刘星星
	 * @createDate 2013/2/28
	 * @return 
	 */
	private Drawable dooble(int number,String name){
//		int length = (String.valueOf(number)+name).length()+2;
//////		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soso_map_over);
//		Bitmap canvasBitmap = Bitmap.createBitmap(length*35,100,Config.ARGB_8888);
//		Canvas canvas = new Canvas(canvasBitmap);
//		canvas.drawBitmap(bitmap, 0, 0,null);
		int btnHeight = 80;
		int btnTextSize = 16;
		int pL = 15 ;
		int pT = 12 ; 
		int pR = 12 ;
		int pB = 0 ;
		int height = MyApplication.getInstance(this).getScreenHeight();
		Button btn = new Button(this);
		btn.setBackgroundResource(R.drawable.soso_map_over);
		if (height == 1280) {
			btnHeight = 130;
			btnTextSize = 20;
			pL = 30 ;
			pT = 20 ; 
		    pR = 30 ;
			pB = 0 ;
		} else if (height == 800) {
			btnHeight = 90;
			btnTextSize = 16;
			pL = 15 ;
			pT = 12 ; 
		    pR = 12 ;
			pB = 0 ;
		} else if (height == 960) {
			btnHeight = 90;
			btnTextSize = 16;
			pL = 25 ;
			pT = 15 ; 
		    pR = 22 ;
			pB = 0 ;
		} else if (height == 854) {
			btnHeight = 90;
			btnTextSize = 16;
			pL = 15 ;
			pT = 12 ; 
		    pR = 12 ;
			pB = 0 ;
		} else if (height == 480) {
			btnHeight =42;
			btnTextSize = 11;
			pL = 10 ;
			pT = 6 ; 
		    pR = 10 ;
			pB = 0 ;
			btn.setBackgroundResource(R.drawable.soso_map_over1);
		}
		
		btn.setHeight(btnHeight);
		btn.setPadding(pL, pT, pR, pB);
		btn.setTextColor(Color.BLACK);
		btn.setTextSize(btnTextSize);
		btn.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
		if(name.equals("")){
			btn.setText(number+"套");
		}else{
			btn.setText(name+" "+number+"套");
		}
		Bitmap bmp = CommonUtils.convertViewToBitmap(btn);
		BitmapDrawable bd = new BitmapDrawable(bmp);
		return bd;
	}
	
	private Canvas dooble(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soso_house_map);
		Bitmap canvasBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Config.ARGB_8888);
		Canvas canvas = new Canvas(canvasBitmap);
		canvas.drawBitmap(bitmap, 0, 0,null);
		return canvas;
	}

//		public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {
//
//			private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
//			private Context context;
//
//			public CustomItemizedOverlay(Drawable defaultMarker) {
//				super(boundCenterBottom(defaultMarker));
//			}
//
//			public CustomItemizedOverlay(Drawable marker, Context context) {
//				super(boundCenterBottom(marker));
//				this.context = context;
//			}
//
//			@Override
//			protected OverlayItem createItem(int i) {
//				return overlayItemList.get(i);
//			}
//
//			@Override
//			public int size() {
//				return overlayItemList.size();
//			}
//
//			public void addOverlay(OverlayItem overlayItem) {
//				overlayItemList.add(overlayItem);
//				this.populate();
//			}
//
//			@Override
//			public void draw(Canvas canvas, MapView mapView, boolean shadow) {
////			        Point point1 = mapView.getProjection().toPixels(point, null);  
////			        canvas.drawText("经纬度是："+point.getLongitudeE6()/1e6+","+point.getLatitudeE6()/1e6, point1.x, point1.y, new Paint());  
////			        Bitmap bitmap = ((BitmapDrawable)dooble(4,"长宁区")).getBitmap(); 
////			        canvas.drawBitmap(bitmap,  point1.x, point1.y-bitmap.getHeight(),  new Paint());
//				super.draw(canvas, mapView, shadow);
//			}
//
//			@Override
//			// 处理点击事件
//			protected boolean onTap(int i) {
//				keyword = "";
//				setFocus(overlayItemList.get(i));
//				int type = 0;
//				try {
//					type = Integer.parseInt(overlayItemList.get(i).getTitle());
//				} catch (Exception e) {
//				}
//				String str[] = overlayItemList.get(i).getSnippet().split(";");
//				String name = "";
//				String xgid = "";
//				if(str.length>=2){
//					name = str[1];
//					xgid=str[0];
//				}
//				if(type == 1){
//					mMapView.getController().setZoom(14);
//				}else if(type == 2){ 
//					addressBtn.setText(name);
//					mMapView.getController().setZoom(15);
//				}else if(type == 3){
//					tag = 0;
////					notifiView1(getBuildInfo(buildid));
//					whereclause = getWhereclause(type, xgid);
//					searchtype = 7;
//					MyApplication.getInstance().setMap_display_flag(1);
//					new Thread(runnable).start();
//					return true;
//				}
//				mMapView.getController().animateTo(overlayItemList.get(i).getPoint());
//				move_tag = false;
//				searchtype = -1;
//				mSearch.reverseGeocode(overlayItemList.get(i).getPoint());
//				return true;
//			}
//		}
		
		
		
		/*-------------------------- 地图end----------------------------------------------------
		 * -------------------------------------------------------------------------------------------------------*/
	/**
	 * 弹出对话框 进行条件的刷选
	 * @author 刘星星
	 * @createDate 2013/1/25
	 * 
	 */
	private void showDialog(){
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_roommanagerlist, null);
		dialog.setView(view);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams param = window.getAttributes();
		param.alpha = 0.9f;
		window.setBackgroundDrawable(null);
		window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,   
				 WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
		window.setAttributes(param);
		
		TextView titleText = (TextView) view.findViewById(R.id.titleText);
		TextView contentText = (TextView) view.findViewById(R.id.contentText);
		Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
		Button cancleBtn = (Button) view.findViewById(R.id.cancleBtn);
		dialog.show();
	}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			MyApplication.getInstance().setOfficeid(roomsListData.get(arg2-1).get("officeid").toString());
			Intent intent = new Intent(this,DetailRoomInfoActivity.class);
			startActivity(intent);
		}
		
/*--------------------------------------------------------对话框处理处  start----------------------------------------------------------------------------
 * ---------------------------------------------------------------------------------------------------------------------------------------------------------
 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------
 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------*/		
		private LinearLayout parent = null;
		private PopupWindow selectPopupWindow = null;
		private LinearLayout selectLayout;
//		private LinearLayout selectDistanceLayout/*,selectTimeLayout*/;
		private TextView selectText;
		private AreaSelectView areaSelect = null;//价格选择
//		private AreaSelectView distanceSelect/*,timeSelect*/;
		//纯0写字楼  3商务中心  4园区  1商住两用2酒店式公寓
		private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5;
		private Button sureSearchBtn;//确认搜索按钮
//		private TextView searchRoomText;//楼盘
		private ExpandableListView mainListView;// 显示商圈的列表
//		private EditText searchAreaEt;// 输入关键字的文本框
		private ImageView imageView;
		private TextView noLimitText;//不限定商圈控件
		
		private EditText minAcreageEt,MaxAcreageEt;
		/**
		 * 初始化弹出框窗口的布局
		 * @author 刘星星
		 * @creageDate 2013/1/30
		 */
	private void initDialogWindow(int dialogType) {
		int width = 0;
		int height = 0;
		LinearLayout loupanwindow = null;
		height = 200;
		if (dialogType == 1) {// 选择价格区间
			loupanwindow = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.dialog_select, null);
			sureSearchBtn = (Button) loupanwindow
					.findViewById(R.id.sureSearchBtn);
			sureSearchBtn.setOnClickListener(this);
			selectLayout = (LinearLayout) loupanwindow
					.findViewById(R.id.selectLayout);
			selectText = (TextView) loupanwindow.findViewById(R.id.selectText);
			selectText.setText("选择价格:每平方米每天的价格(元/平方米/天)");
//			loupanwindow.getBackground().setAlpha(100);
		} else if (dialogType == 2) {// 面积区间
			loupanwindow = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.dialog_select_acreage, null);
			sureSearchBtn = (Button) loupanwindow
					.findViewById(R.id.sureSearchBtn);
			sureSearchBtn.setOnClickListener(this);
			minAcreageEt = (EditText) loupanwindow
					.findViewById(R.id.minAcreageEt);
			MaxAcreageEt = (EditText) loupanwindow
					.findViewById(R.id.maxAcreageEt);
			selectText = (TextView) loupanwindow.findViewById(R.id.selectText);
			selectText.setText("选择面积:平方米");
//			minAcreageEt.setText(areadown);
//			MaxAcreageEt.setText(areaup);
//			loupanwindow.getBackground().setAlpha(100);
		} else if (dialogType == 3) {// 房源类型
			loupanwindow = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.dialog_roomtype, null);
			sureSearchBtn = (Button) loupanwindow
					.findViewById(R.id.sureSearchBtn);
			sureSearchBtn.setOnClickListener(this);
			checkBox1 = (CheckBox) loupanwindow.findViewById(R.id.checkBox1);
			checkBox2 = (CheckBox) loupanwindow.findViewById(R.id.checkBox2);
			checkBox3 = (CheckBox) loupanwindow.findViewById(R.id.checkBox3);
			checkBox4 = (CheckBox) loupanwindow.findViewById(R.id.checkBox4);
			checkBox5 = (CheckBox) loupanwindow.findViewById(R.id.checkBox5);
			if (officetype1) {
				checkBox1.setChecked(true);
			}else{
				checkBox1.setChecked(false);
			}
			if (officetype2) {
				checkBox2.setChecked(true);
			}else{
				checkBox2.setChecked(false);
			}
			if (officetype3) {
				checkBox3.setChecked(true);
			}else{
				checkBox3.setChecked(false);
			}
			if (officetype4) {
				checkBox4.setChecked(true);
			}else{
				checkBox4.setChecked(false);
			}
			if (officetype5) {
				checkBox5.setChecked(true);
			} else{
				checkBox5.setChecked(false);
			}
//			loupanwindow.getBackground().setAlpha(100);
		} else if (dialogType == 4) {// 选择周边房源搜索条件
			loupanwindow = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.dialog_selectdistanceandtime, null);
			// selectDistanceLayout = (LinearLayout)
			// loupanwindow.findViewById(R.id.selectDistanceLayout);
			// selectTimeLayout = (LinearLayout)
			// loupanwindow.findViewById(R.id.selectTimeLayout);
			seekBar = (SeekBar) loupanwindow.findViewById(R.id.seek);
			currentDistance = (TextView) loupanwindow
					.findViewById(R.id.currentDistance);
			sureSearchBtn = (Button) loupanwindow
					.findViewById(R.id.sureSearchBtn);
			sureSearchBtn.setOnClickListener(this);
			seekBar.setMax(20);
			seekBar.setProgress(range);
			currentDistance.setText(range + "千米");
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					
					//3~18    50m~2000km
					int leve = seekBar.getProgress()/3;
					mMapController.setZoom(18-leve);
//					if(seekBar.getProgress()<5){
//		        		 mapController.setZoom(18);
//		        	}else if(seekBar.getProgress()<10){
//		        		 mapController.setZoom(17);
//		        	}else if(seekBar.getProgress()<15){
//		        		 mapController.setZoom(16);
//		        	}else if(seekBar.getProgress()<20){
//		        		 mapController.setZoom(11);
//		        	}
					if(mMapView!=null&&mypoint!=null){
						mMapView.getController().animateTo(mypoint);
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					currentDistance.setText(progress + "千米");
				}
			});
			height = 300;
//			loupanwindow.getBackground().setAlpha(100);
		} else if (dialogType == 5) {// 选择商圈
			loupanwindow = (LinearLayout) this.getLayoutInflater().inflate(
					R.layout.dialog_selectbusiness, null);
			height = 300;
//			searchRoomText = (TextView) loupanwindow
//					.findViewById(R.id.searchRoomText);
			mainListView = (ExpandableListView) loupanwindow
					.findViewById(R.id.mainListView);
			initAreaList();
		}
		parent = (LinearLayout) findViewById(R.id.parent);
		width = (int) (parent.getWidth() * 0.9f);
		selectPopupWindow = new PopupWindow(loupanwindow, width,
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
//		selectPopupWindow.getBackground().setAlpha(180);
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.showAsDropDown(parent,
				(parent.getWidth() - width) / 2,
				-(parent.getHeight() - (parent.getHeight() - height) / 2));
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				dismiss();
			}
		});
	}
		
		/**
		 * PopupWindow消失
		 */
		public void dismiss() {
			selectPopupWindow.dismiss();
		}	
		/**
		 * 点击商圈控件时弹出对话框
		 * @author 刘星星
		 * @createDate 2013//3/22
		 */
		Dialog ad;
		public void showDialogBusiness(){
			ad = new Dialog(this,R.style.dialog);
			View view = LayoutInflater.from(this).inflate(R.layout.dialog_selectbusiness, null);
			LayoutParams params = new LayoutParams((int) (MyApplication.getInstance().getScreenWidth()*0.7),
					LayoutParams.WRAP_CONTENT);
			ad.addContentView(view, params);
			ad.show();
//			Window window = ad.getWindow();
//			window.setBackgroundDrawable(new ColorDrawable(0));  
//			window.setContentView(R.layout.dialog_selectbusiness); 
			
//			searchRoomText = (TextView) view.findViewById(R.id.searchRoomText);
			mainListView = (ExpandableListView) view.findViewById(R.id.mainListView);
//			searchAreaEt = (EditText) view.findViewById(R.id.searchAreaEt);
			imageView = (ImageView) view.findViewById(R.id.mainListJiantou);
			dialogNoDataLayout = (LinearLayout) view.findViewById(R.id.noDataLayout);
//			currentCityLayout = (TextView)  view.findViewById(R.id.mainTopBtnLinear);
//			currentCityLayout.setOnClickListener(this);
			
//			searchAreaEt.addTextChangedListener(new MyTextWatcher());
			mainListView.setOnChildClickListener(new AreaListChildClickListener());
			imageView.setOnClickListener(this);
			ad.setCancelable(true);
			ad.setCanceledOnTouchOutside(true);
			noLimitText = (TextView) view.findViewById(R.id.noLimitText);
//			noLimitText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//			noLimitText.setText(Html.fromHtml("(xuqingfeng77博客)"));
			noLimitText.setOnClickListener(this);
//			if(addressBtn.getText().toString().equals("选择商圈")){
				noLimitText.setText(MyApplication.getInstance().getCityname());
//			}else{
//				noLimitText.setText(addressBtn.getText().toString());
//			}
			
//			if(MyApplication.getInstance().getNotlogin_search()==1){
//				noLimitText.setText(cityname_qiehuan);
//			}else{
//				currentCityLayout.setVisibility(View.GONE);
//				currentCityText.setVisibility(View.GONE);
//			}
			initAreaList();
		}
		/**
		 * 点击面积控件时弹出对话框
		 */
		Dialog acreageDialog;
		public void showDialogAcreage(){
			acreageDialog = new Dialog(this,R.style.dialog);
			acreageDialog.show();
			acreageDialog.setCancelable(true);
			acreageDialog.setCanceledOnTouchOutside(true);
			View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_acreage, null);
//			dialog.setView(view);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			acreageDialog.addContentView(view, params);
			sureSearchBtn = (Button) view.findViewById(R.id.sureSearchBtn);
			sureSearchBtn.setOnClickListener(this);
			minAcreageEt = (EditText) view.findViewById(R.id.minAcreageEt);
			MaxAcreageEt = (EditText) view.findViewById(R.id.maxAcreageEt);
			minAcreageEt.setText(areadown+"");
			MaxAcreageEt.setText(areaup+"");
			selectText = (TextView) view.findViewById(R.id.selectText);
			selectText.setText("选择面积:平方米");
		}
		
		
		
		/**
		 * 地图缩放
		 */
		
		public Runnable mapzoomrunnable = new Runnable() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(12);
			}
		};
		/**
		 * 地图移动
		 */
		
		public Runnable mapmoverunnable = new Runnable() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(13);
			}
		};

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
		keyword = "";
		isshowtoast = true;
//		searchRoomText.setText(childArray.get(groupPosition)
//				.get(childPosition).get("name").toString());
		districtid = childArray.get(groupPosition)
				.get(childPosition).get("id").toString();
		areaid = groupArray.get(groupPosition).get("id").toString();
//		shangquanid = childArray.get(groupPosition)
//				.get(childPosition).get("id").toString();
//		addressBtn.setText(childArray.get(groupPosition)
//				.get(childPosition).get("name").toString());
		
		if(childArray.get(groupPosition)
				.get(childPosition).get("latitude")!=null
				&&childArray.get(groupPosition)
				.get(childPosition).get("longitude")!=null){
			try{
				shangquanpoint = new GeoPoint((int)(Double.parseDouble(childArray.get(groupPosition)
						.get(childPosition).get("latitude").toString())*1E6),
						(int)(Double.parseDouble(childArray.get(groupPosition)
								.get(childPosition).get("longitude").toString())*1E6));
			}catch(Exception e){
				
			}
		}
		CommonUtils.hideSoftKeyboard(SearchRoomsListActivity.this);
		ad.dismiss();
		for (int i = 0; i < parent.getChildCount(); i++) {
			if (parent.getChildAt(i) == v) {
				parent.getChildAt(i).setBackgroundColor(
						Color.rgb(160, 201, 1));
			} else {
				parent.getChildAt(i).setBackgroundColor(
						Color.rgb(255, 255, 255));
			}
		}
		if(!districtid.equals("")){
			String name="";
			name =ZoneUtil.getName(SearchRoomsListActivity.this,districtid);
			if(name.length()>8){
				name = name.substring(0, 8)+"...";
			}
			addressBtn.setText(name);
			mapzoom=15;
			new Thread(mapzoomrunnable).start();
		}else if(!areaid.equals("")){
			String name="";
			name =ZoneUtil.getName(SearchRoomsListActivity.this,areaid,3);
			if(name.length()>8){
				name = name.substring(0, 8)+"...";
			}
			addressBtn.setText(name);
			mapzoom=14;
			new Thread(mapzoomrunnable).start();//显示区下面的商圈
		}else{
			String name="";
			name =ZoneUtil.getName(SearchRoomsListActivity.this,cityid,2);
			if(name.length()>8){
				name = name.substring(0, 8)+"...";
			}
			addressBtn.setText(name);
			mapzoom=11;
			new Thread(mapzoomrunnable).start();//显示城市下的区
		}
		if(MyApplication.getInstance().getMap_display_flag()==0&&shangquanpoint!=null){
			quid = "";
			shangquanid = "";
			chengshiid  = "";
			move_tag = false;
//			shangquan_tag = false;
			mSearch.reverseGeocode(shangquanpoint);
			new Thread(mapmoverunnable).start();
			return true;
		}
		
//		if(shangquanpoint!=null){
//			mapController.setZoom(16);
//			mMapView.getController().animateTo(shangquanpoint);
//		}
//		if(MyApplication.getInstance().getMap_display_flag() == 0&&shangquanpoint!=null){
//			move_tag = true;
//			shangquan_tag = false;
//			mSearch.reverseGeocode(shangquanpoint);
//			return true;
//		}
//		try{
//			quid = childArray.get(groupPosition)
//					.get(childPosition).get("parentid").toString();
//		}catch(Exception e){
//			quid = "";
//		}
		new Thread(mapmoverunnable).start();
		quid = areaid;
		shangquanid = districtid;
		chengshiid  = cityid;
		searchtype = 6;
		whereclause = getWhereclause();
		new Thread(runnable).start();
		return true;
	}
}
//		/***
//		 * 监听文本改变
//		 */
//		class MyTextWatcher implements TextWatcher {
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				initAreaList1(searchAreaEt.getText().toString());
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//
//			}
//
//		}
		/**
		 * 初始化商圈数据 author by Ring
		 * 
		 */
		private List<HashMap<String, Object>> groupArray;
		private List<List<HashMap<String, Object>>> childArray;
//		private String cityname = "";
		private void initAreaList() {
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
										&& selectresult1.get(j).get("name") != null
										&&selectresult1.get(j).get("latitude") != null
										&& selectresult1.get(j).get("longitude") != null) {
									gMap = new HashMap<String, Object>();
									gMap.put("name",
											selectresult1.get(j).get("name")
													.toString());
									gMap.put("id",
											selectresult1.get(j).get("id")
													.toString());
									gMap.put("latitude", selectresult1.get(j).get("latitude")
											.toString());
									gMap.put("longitude", selectresult1.get(j).get("longitude")
											.toString());
									gMap.put("parentid", selectresult.get(i).get("id").toString());
									tempArray.add(gMap);
								}
							}
							if(tempArray.size()>0){
								childArray.add(tempArray);
							}
						}
					}
				}
			}
			if(childArray.size()>0){
				notifyListView();
//				searchRoomText.setText(childArray.get(0).get(0).get("name").toString());
			}else{
				if(groupArray.size() == 0){
					dialogNoDataLayout.setVisibility(View.VISIBLE);
					mainListView.setVisibility(View.GONE);
				}else{
					dialogNoDataLayout.setVisibility(View.GONE);
					mainListView.setVisibility(View.VISIBLE);
				}
			}
			
		}
		
//		/**
//		 * 初始化商圈数据 author by Ring whereClause查询条件
//		 */
//		private void initAreaList1(String whereClause) {
//			if (groupArray == null) {
//				groupArray = new ArrayList<HashMap<String, Object>>();
//			}
//			if (childArray == null) {
//				childArray = new ArrayList<List<HashMap<String, Object>>>();
//			}
//			childArray.clear();
//			groupArray.clear();
//
//			String sql = "select regionid,name,id,latitude,longitude from district where name like '%"
//					+ whereClause + "%'";
//			List<Map<String, Object>> selectresult = null;
//			List<Map<String, Object>> selectresult1 = null;
//			selectresult = ZoneUtil.getregion(this, sql);
//			if (selectresult != null && selectresult.size() > 0) {
//				int i;
//				HashMap<String, Object> gMap = null;
//				for (i = 0; i < selectresult.size(); i++) {
//					if (selectresult.get(i).get("regionid") != null
//							&& selectresult.get(i).get("name") != null) {
//						selectresult1 = ZoneUtil.getregion(this, "select id,name from region where id='"
//								+ selectresult.get(i).get("regionid")
//								.toString() + "' and type='3' and parentid='" +cityid
//					+ "'");
//						if (selectresult1 != null && selectresult1.size() > 0) {
//							gMap = new HashMap<String, Object>();
//							gMap.put("name",
//									selectresult1.get(selectresult1.size() - 1)
//											.get("name").toString());
//							gMap.put("id",
//									selectresult1.get(selectresult1.size() - 1)
//											.get("id").toString());
//								groupArray.add(gMap);
//							List<HashMap<String, Object>> tempArray = new ArrayList<HashMap<String, Object>>();
//							gMap = new HashMap<String, Object>();
//							gMap.put("name", selectresult.get(i).get("name")
//									.toString());
//							try{
//								gMap.put("latitude", selectresult.get(i).get("latitude")
//										.toString());
//								gMap.put("longitude", selectresult.get(i).get("longitude")
//										.toString());
//							}catch(Exception e){
//								
//							}
//							gMap.put("id", selectresult.get(i).get("id")
//									.toString());
//							gMap.put("parentid", selectresult1.get(selectresult1.size() - 1).get("id")
//									.toString());
//							tempArray.add(gMap);
//							childArray.add(tempArray);
//						}
//					}
//				}
//			}
//			groupArray  = getGroupArray(groupArray);//整理父控件数据
//			childArray = getChildArray(childArray);//整理子控件数据
//			notifyListView();
////			if(childArray.size()>0)
////				searchRoomText.setText(childArray.get(0).get(0).get("name").toString());
//		}
		/**
		 * 针对搜索功能做的数据处理函数
		 * 根据id给groupArray的数据进行整理，当Id相同的时候只取一个。
		 * 这个函数能用的前提是集合中要有字段是id  而且数据类型都要能匹配
		 * @param 要被整理的数据
		 * @return 一个整理后的数据集合
		 * @author 刘星星
		 * @createDate 2013.2.1
		 */
//		private List<HashMap<String, Object>> getGroupArray(List<HashMap<String, Object>> oldList){
//			List<HashMap<String, Object>> newList = new ArrayList<HashMap<String,Object>>();
//			for(int i=0;i<oldList.size();i++){
//				HashMap<String,Object> map = oldList.get(i);
//				if(i == 0){
//					newList.add(map);
//				}else{
//					if(!map.get("id").toString().equals(oldList.get(i-1).get("id"))){
//						newList.add(map);
//					}
//				}
//			}
//			return newList;
//		}
//		/**
//		 * 这是一个对树控件的子集数据集合进行整理的函数哦。。。
//		 * 跟据父控件的id进行对比，把父控件id相同的子集放入同一个集合。
//		 * 得到每一个子集中的字段虽然挺麻烦，但是都是一层一层剥的，还是蛮容易理解的啦，，，
//		 * @param 需要被整理的数据集合
//		 * @return 一个新的集合
//		 * @author 刘星星
//		 * @createDate 2013.2.1
//		 */
//		private List<List<HashMap<String, Object>>> getChildArray(List<List<HashMap<String, Object>>> oldList){
//			List<List<HashMap<String, Object>>> newList = new ArrayList<List<HashMap<String, Object>>>();
//			for(int i=0;i<groupArray.size();i++){//子集的个数当然是父控件的个数啦。。。。。
//				HashMap<String,Object> groupMap = groupArray.get(i);//得到每一个父控件。。。。。。。。
//				String parentid = groupMap.get("id").toString();//拿到父控件的id啦，方便后面与子集中的parentid进行对比。这是一一对应的啦。。。看似很方便哦。。。。
//				List<HashMap<String, Object>> newChildList = new ArrayList<HashMap<String,Object>>();//这个就是一个新的子集了。。
//				for(int j=0;j<oldList.size();j++){//对子集集合进行遍历
//					List<HashMap<String, Object>> oldChildList = (List<HashMap<String, Object>>) oldList.get(j);//得到每个子集集合对象中的每一个子集
//					for(int k=0;k<oldChildList.size();k++){//遍历每一个子集中的对象
//						HashMap<String,Object> childMap = oldChildList.get(k);//得到每一个子集对象
//						String childId = childMap.get("parentid").toString();//得到每一个子集的父id啦。。。。。。。
//						if(parentid.equals(childId)){//把父id和子id进行对比啦，如果相同的话就加入新的子集集合中喽。
//							newChildList.add(childMap);
//						}
//					}
//				}
//				newList.add(newChildList);
//			}
//			return newList;
//		}
		/**
		 * 刷新列表布局 给区域列表控件加载数据
		 * 顶部的选择商圈布局的刷新
		 */
		private void notifyListView() {
			if(childArray!=null&&childArray.size()>0&&groupArray!=null&&groupArray.size()>0){
				mainListView.setAdapter(new MainListAdapter(this, groupArray,
						childArray));
				setExpandableViewOpen();
			}else if(childArray!=null&&groupArray!=null){
				mainListView.setAdapter(new MainListAdapter(this, groupArray,
						childArray));
				setExpandableViewOpen();
			}
			
			if(groupArray.size() == 0){
				dialogNoDataLayout.setVisibility(View.VISIBLE);
				mainListView.setVisibility(View.GONE);
			}else{
				dialogNoDataLayout.setVisibility(View.GONE);
				mainListView.setVisibility(View.VISIBLE);
			}
		}
		/**
		 * 设置ExpandableListView默认为子项全部打开
		 * 
		 * @author 刘星星
		 * @createDate 2013/1/7
		 */
		private void setExpandableViewOpen() {
//			int groupCount = mainListView.getCount();
//			for (int i = 0; i < groupCount; i++) {
//				mainListView.expandGroup(i);
//			}
		}
		/*--------------------------------------------------------对话框处理处  end----------------------------------------------------------------------------
		 * ---------------------------------------------------------------------------------------------------------------------------------------------------------
		 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------
		 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------*/		
		
		
		/**
		 * 搜索房源信息列表
		 *
		 * 作者：Ring
		 * 创建于：2013-2-23
		 * wherec
		 */
		public boolean getSearchRoom(String whereclause){
			// params 请求的参数列表
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			if(!isclickmore){
				roomsListData.clear();
				loupanlistdata.clear();
				recordcount = "";
			}
			int index = (roomsListData.size()/10);
			int count = roomsListData.size()%10;
			if(count!=0){
				index++;
			}
			params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
					this).getAPPID()));
			params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
					this).getAPPKEY()));
			if(whereclause==null||whereclause.trim().equals("")){
				whereclause = "1=1";
			}
			params.add(new BasicNameValuePair("UpdateDate", ""));
			params.add(new BasicNameValuePair("whereClause", whereclause));
			params.add(new BasicNameValuePair("orderBy", "officeid desc"));
			params.add(new BasicNameValuePair("pageIndex", index+""));
			params.add(new BasicNameValuePair("pageLength", "10"));
			uploaddata = new SoSoUploadData(this, "OfficeGetPaged.aspx", params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse(reponse);
			params.clear();
			params = null;
			if (StringUtils.CheckReponse(reponse)) {
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * 搜索周边房源
		 *
		 * 作者：Ring
		 * 创建于：2013-2-23
		 * wherec
		 */
		public boolean getSearchRoomaround(){
			// params 请求的参数列表
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			if(!isclickmore){
				roomsListData.clear();
				loupanlistdata.clear();
				recordcount = "";
			}
			int index = (roomsListData.size()/10);
			int count = roomsListData.size()%10;
			if(count!=0){
				index++;
			}
			params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
					this).getAPPID()));
			params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
					this).getAPPKEY()));
			if(whereclause==null||whereclause.trim().equals("")){
				whereclause = "1=1";
			}
			params.add(new BasicNameValuePair("whereClause", whereclause));
			params.add(new BasicNameValuePair("orderBy", "officeid desc"));
			params.add(new BasicNameValuePair("Longitude", MyApplication.getInstance().getLongitude()+""));
			params.add(new BasicNameValuePair("Latitude", MyApplication.getInstance().getLatitude()+""));
			params.add(new BasicNameValuePair("Range", seekBar.getProgress()*1000+""));
			uploaddata = new SoSoUploadData(this, "OfficeGetAround.aspx", params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse(reponse);
			params.clear();
			params = null;
			if (StringUtils.CheckReponse(reponse)) {
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * 处理服务器响应值，将返回的房源对象信息保存下来
		 * 
		 * 作者：Ring 创建于：2013-1-31
		 * 
		 * @param districtid
		 */
		private void dealReponse(String reponse) {
			if (StringUtils.CheckReponse(reponse)) {
				String name= "";
				String sdpath="";
				File file1;//缩略图文件
				Type listType = new TypeToken<LinkedList<SoSoOfficeInfo>>() {
				}.getType();
				Gson gson = new Gson();
				LinkedList<SoSoOfficeInfo> sosoofficeinfos = null;
				SoSoOfficeInfo sosoofficeinfo = null;
				try {
					JSONObject json = new JSONObject(reponse);
					recordcount = json.getString("recordcount");
					String officedatas = json.getString("data");
					sosoofficeinfos = gson.fromJson(officedatas, listType);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (sosoofficeinfos != null && sosoofficeinfos.size() > 0) {
					HashMap<String, Object> map = null;
					for (Iterator<SoSoOfficeInfo> iterator = sosoofficeinfos
							.iterator(); iterator.hasNext();) {
						sosoofficeinfo = (SoSoOfficeInfo) iterator.next();
						if (sosoofficeinfo.getIsUsed() == 1) {
							DBHelper.getInstance(this).delete("soso_officeinfo",
									"officeid = ?", new String[] { sosoofficeinfo
									.getOfficeID() });
							continue;
						}
						if (sosoofficeinfo != null
								&& sosoofficeinfo.getOfficeID() != null) {
							map = new HashMap<String, Object>();
							map.put("roomNumber", sosoofficeinfo.getOfficeMC());
							map.put("state",sosoofficeinfo.getIsRent());
							map.put("name", sosoofficeinfo.getBuildMC());
							map.put("money", sosoofficeinfo.getPriceUp());
							map.put("phone", sosoofficeinfo.getTelePhone());
							map.put("acreage", sosoofficeinfo.getAreaUp());
							map.put("buildid", sosoofficeinfo.getBuildID());
							map.put("buildmc", sosoofficeinfo.getBuildMC());
							map.put("officeid", sosoofficeinfo.getOfficeID());
							map.put("latitude", sosoofficeinfo.getLatitude());
							map.put("longitude", sosoofficeinfo.getLongitude());
							
							if(sosoofficeinfo.getShowImageID()!=null){
								name = MD5.getMD5(sosoofficeinfo.getShowImageID()
										+ "缩略图.jpg")
										+ ".jpg";
							}
							
							file1 = FileTools
										.getFile(
												getResources().getString(
														R.string.root_directory),
														getResources().getString(
																R.string.room_image), name);
							if(sosoofficeinfo.getShowImageID()!=null){
								map.put("id", sosoofficeinfo.getShowImageID());
							}else{
								map.put("id", "");
							}
							map.put("file", file1);
							map.put("pixelswidth", (MyApplication.getInstance(this).getScreenWidth() / 3 - 10));
							map.put("pixelsheight", (MyApplication.getInstance(this).getScreenWidth() / 3 - 10));
							map.put("sql", "");
							map.put("request_name", "ImageFileCutForCustom.aspx");
							
							roomsListData.add(map);
//							loupanlistdata.add(sosoofficeinfo.getBuildID());
						}
					}
					if (sosoofficeinfos != null) {
						sosoofficeinfos.clear();
						sosoofficeinfos = null;
					}
				}
			}
		}
		Runnable priceRunnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					handler.sendEmptyMessage(7);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		/**
		 * 处理耗时操作
		 * 
		 * @author Ring
		 * @since 2013-01-31
		 */
		public Runnable runnable = new Runnable() {

			@Override
			public void run() {
				int listindex = 0;
				if(roomsListData!=null&&roomsListData.size()>0){
					listindex = roomsListData.size()-1;
				}
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				if (NetworkCheck.IsHaveInternet(SearchRoomsListActivity.this)) {
					if(tag == 1){
						if(MyApplication.getInstance().getMap_display_flag() == 1){
							return;
						}
						tag =0;
						runnable_tag = true;
						click_limit = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						boolean b = false;
//						handler.sendEmptyMessage(5);// 开启进度条
						if(type == 1){
							if(chengshiid.trim().equals("")){
								return;
							}
							b=getMapNumber(1,chengshiid);
						}else if(type == 2){
							if(quid.trim().equals("")){
								return;
							}
							b=getMapNumber(2,quid);
						}else if(type == 3){
							if(quid.trim().equals("")){
								return;
							}
							b=getMapNumber(2,quid);
							String shangquanid1 = getShangquanID(loupanlistdata1, shangquan_longitude, shangquan_latitude);
							if(shangquanid1!=null&&!shangquanid1.trim().equals("")&&!shangquanid.equals(shangquanid1)){
								shangquanid = shangquanid1;
								b=getMapNumber(3,shangquanid);
							}
						}
//						handler.sendEmptyMessage(6);// 关闭进度条
						if(b){
							handler.sendEmptyMessage(8);// 刷新列表
						}else{
							handler.sendEmptyMessage(3);// 失败
							handler.sendEmptyMessage(8);// 刷新列表
						}
//						if (runnable_tag) {
//							runnable_tag = false;
//							click_limit = true;
//							return;
//						}
					}else{
						if (click_limit) {
							click_limit = false;
						} else {
							return;
						}
						if(MyApplication.getInstance().getMap_display_flag() == 0){
							return;
						}
						boolean b = false;
						handler.sendEmptyMessage(5);// 开启进度条
						if(searchtype==4){
							b=getSearchRoomaround();
						}else{
							b=getSearchRoom(whereclause);
						}
						handler.sendEmptyMessage(6);// 关闭进度条
						if(b){
							handler.sendEmptyMessage(1);// 刷新列表
						}else{
							handler.sendEmptyMessage(3);// 失败
//							handler.sendEmptyMessage(1);// 刷新列表
						}
						if (runnable_tag) {
							runnable_tag = false;
							click_limit = true;
							return;
						}
					}
				} else {
					handler.sendEmptyMessage(4);// 没有网络时给用户提示

				}
				if(isclickmore){
					Bundle b = new Bundle();
					b.putInt("listindex", listindex);
					Message msg = new Message();
					msg.what = 10;
					msg.setData(b);
					handler.sendMessage(msg);// 列表移动到最后一项
				}
				isclickmore = false;
				click_limit = true;
			}
		};
		/**
		 * 处理逻辑业务
		 * 
		 * @author Ring
		 * @since 2013-01-31
		 */
		public Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				Intent i = new Intent();
				switch (msg.what) {
				case 1:// 刷新列表
					roomsLV.setPullLoadEnable(true);
					if(searchtype == 7){
						roomsLV.setVisibility(View.VISIBLE);//切换至列表模式
						searchBtn.setBackgroundResource(R.drawable.soso_map_btn);
						MyApplication.getInstance().setMap_display_flag(1);
						currentType = 1;
						notifiView();	
//						notifiView1(roomsListData);
					}else{
						notifiView();	
					}
					roomsLV.stopLoadMore();
					handler.sendEmptyMessage(9);
					break;
				case 2:// 从登录界面跳转到注册界面
					break;
				case 3:// 登录失败给用户提示
					String errormsg = "";
					if (StringUtils.getErrorState(reponse).equals(
							ErrorType.SoSoTimeOut.getValue())) {
						roomsLV.setPullLoadEnable(true);
						errormsg = getResources().getString(
								R.string.progress_timeout);
					} else if (StringUtils.getErrorState(reponse).equals(
							ErrorType.SoSoNoData.getValue())) {
						roomsLV.setPullLoadEnable(false);
						errormsg = getResources().getString(
								R.string.progress_nodata);
					} else {
						roomsLV.setPullLoadEnable(true);
						errormsg = getResources().getString(
								R.string.toast_message_failure);
					}
					roomsLV.stopLoadMore();
					noDataMsg.setText(errormsg);
					notifiView();
//					Toast.makeText(SearchRoomsListActivity.this, errormsg, Toast.LENGTH_SHORT).show();
					break;
				case 4:// 没有网络时给用户提示
					roomsLV.stopLoadMore();
					MessageBox.CreateAlertDialog(SearchRoomsListActivity.this);
					break;
				case 5:// 打开进度条
					progressdialog = new ProgressDialog(SearchRoomsListActivity.this);
					progressdialog.setMessage(getResources().getString(
							R.string.progress_message_loading));
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
				case 7:
					areaSelect.initData();
					float scale = areaSelect.leftScale;
					Double mypriceup = 20.0;
					try {
						mypriceup = Double.parseDouble(priceup);
					} catch (Exception e) {
						// TODO: handle exception
					}
					Double mypricedown = 0.0;
					try {
						mypricedown = Double.parseDouble(pricedown);
					} catch (Exception e) {
						// TODO: handle exception
					}
					int wMin = (int) (mypricedown/scale);
					int wMax = (int) ((20-mypriceup)/scale);
					areaSelect.minText.setText(mypricedown+"");
					areaSelect.maxText.setText(mypriceup+"");
					LinearLayout.LayoutParams paramLeft = new LinearLayout.LayoutParams(wMin+areaSelect.leftBtn.getWidth(), areaSelect.leftImage.getHeight());
					areaSelect.leftImage.setLayoutParams(paramLeft);
					
					LinearLayout.LayoutParams paramRight = new LinearLayout.LayoutParams(wMax, areaSelect.rightImage.getHeight());
					paramRight.setMargins(-2, 0, 0, 0);
					areaSelect.rightImage.setLayoutParams(paramRight);
					
					break;
				case 8:
					getRimRoom();
					break;
				case 9://
					if(MyApplication.getInstance().getDisplayRoomPhoto()==0){
						if(tag == 0){
							displayRoomImgDialog();
							tag = 1;
						}
					}
					break;
				case 10://
					Bundle b= msg.getData();
					roomsLV.setSelection(b.getInt("listindex", 0));
					break;
				case 11://
					Bundle b1= msg.getData();
					addressBtn.setText(b1.getString("address"));
					break;
				case 12://缩放地图
					if(mMapController!=null){
						mMapController.setZoom(mapzoom);//显示商圈下的楼盘
					}
				case 13://移动地图
					if(mMapController!=null&&shangquanpoint!=null){
						mMapController.animateTo(shangquanpoint);
					}
					break;
				}
			};
		};
		
//		private int visibleLastIndex = 0;   //最后的可视项索引  
//	    private int visibleItemCount;       // 当前窗口可见项总数  
//	    private int totalCount = 0;     // 数据总数
//		/* (non-Javadoc)
//		 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
//		 */
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem,
//				int visibleItemCount, int totalItemCount) {
//			this.visibleItemCount = visibleItemCount;  
//	        visibleLastIndex = firstVisibleItem + visibleItemCount - 1; 
//	        totalCount= totalItemCount;
//			
//		}
//		/* (non-Javadoc)
//		 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
//		 */
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//			int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引  
//	        int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项  
//	        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex&&totalCount>visibleItemCount) {  
//	        	isclickmore = true;
//				new Thread(runnable).start();
//	        }  
//		}
		/*--------------------------------------------------------对话框处理处  start----------------------------------------------------------------------------
		 * ---------------------------------------------------------------------------------------------------------------------------------------------------------
		 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------
		 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------*/		
				public LinearLayout parent1 = null;
				public PopupWindow selectPopupWindow1 = null;
				public LinearLayout messageLayout;
				public Button displayImgBtn,hideImgBtn;
				/**
				 * 初始化弹出框窗口的布局
				 * @author 刘星星
				 * @creageDate 2013/1/30
				 */
				public void displayRoomImgDialog(){
					int width = 0;
					int height = 0;
					LinearLayout dialogwindow = null;
					height = 200;
					try{
					dialogwindow = (LinearLayout) this.getParent().getLayoutInflater().
								inflate(R.layout.dialog_recommend, null);
					}catch(Exception e){
						dialogwindow = (LinearLayout) this.getLayoutInflater().
								inflate(R.layout.dialog_recommend, null);
					}
					messageLayout = (LinearLayout) dialogwindow.findViewById(R.id.messageLayout);
					displayImgBtn = (Button) dialogwindow.findViewById(R.id.displayImgBtn);
					hideImgBtn = (Button) dialogwindow.findViewById(R.id.hideImgBtn);
//							dialogwindow.getBackground().setAlpha(100);
					parent1 = (LinearLayout) findViewById(R.id.parent);
					displayImgBtn.setOnClickListener(this);
					hideImgBtn.setOnClickListener(this);
					width = (int)(parent1.getWidth()*0.8f);
					selectPopupWindow1 = new PopupWindow(dialogwindow, width,	LayoutParams.WRAP_CONTENT, true);
					// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
					// 没有这一句则效果不能出来，但并不会影响背景
					selectPopupWindow1.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.dialog_recommend_border));
//					selectPopupWindow1.getBackground().setAlpha(180);
					selectPopupWindow1.setOutsideTouchable(true);
					selectPopupWindow1.showAsDropDown(parent1, (parent1.getWidth()-width)/2, -(parent1.getHeight()-(parent1.getHeight()-height)/2));
					selectPopupWindow1.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss() {
							dismiss1();
						}
					});
				}
				
				/**
				 * PopupWindow消失
				 */
				public void dismiss1() {
					if(selectPopupWindow1!=null && selectPopupWindow1.isShowing()){
						selectPopupWindow1.dismiss();
					}
				}	
				/*--------------------------------------------------------对话框处理处  end----------------------------------------------------------------------------
				 * ---------------------------------------------------------------------------------------------------------------------------------------------------------
				 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------
				 * * ---------------------------------------------------------------------------------------------------------------------------------------------------------*/		
		/**
		 * 
		 *获取地图上的数字信息
		 * 作者：Ring
		 * 创建于：2013-3-15
		 */
		public boolean getMapNumber(int type,String xgid){
			// params 请求的参数列表
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
					this).getAPPID()));
			params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
					this).getAPPKEY()));
			params.add(new BasicNameValuePair("XgID", xgid));
			params.add(new BasicNameValuePair("whereClause", "1=1 and "+whereclause));
			params.add(new BasicNameValuePair("Type", type+""));//1城市id，2区id，3商圈id
			uploaddata = new SoSoUploadData(this, "MapCountSelect.aspx", params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse1(reponse, type,xgid);
			params.clear();
			params = null;
			if (StringUtils.CheckReponse(reponse)) {
				if(type == 1){
					quid = "";
					shangquanid = "";
				}else if(type == 2){
					chengshiid = "";
					shangquanid = "";
				}else if(type == 3){
					chengshiid = "";
					quid = "";
				}
				return true;
			}else{
				chengshiid = "";
				quid = "";
				shangquanid = "";
				return false;
			}
		}
		/**
		 *解析服务器的数据
		 *
		 * 作者：Ring
		 * 创建于：2013-3-15
		 */
		
		public void dealReponse1(String reponse, int type,String xgid){
			if(loupanlistdata==null){
				loupanlistdata=new ArrayList<HashMap<String,Object>>();
			}
			loupanlistdata.clear();
			if (StringUtils.CheckReponse(reponse)) {
				Type listType = new TypeToken<LinkedList<MapNumberInfo>>() {
				}.getType();
				Gson gson = new Gson();
				LinkedList<MapNumberInfo> MapNumberInfos = null;
				MapNumberInfo MapNumberInfo = null;
				try {
					MapNumberInfos = gson.fromJson(reponse, listType);
				} catch (Exception e) {
				}
				if (MapNumberInfos != null && MapNumberInfos.size() > 0) {
					HashMap<String, Object> map = null;
					for (Iterator<MapNumberInfo> iterator = MapNumberInfos
							.iterator(); iterator.hasNext();) {
						MapNumberInfo = (MapNumberInfo) iterator.next();
						if (MapNumberInfo != null
								&& MapNumberInfo.getId() != null) {
							map = new HashMap<String, Object>();
							map.put("id", MapNumberInfo.getId());
							map.put("name",MapNumberInfo.getName());
							map.put("number", MapNumberInfo.getNumber());
							map.put("latitude", MapNumberInfo.getLatitude());
							map.put("longitude", MapNumberInfo.getLongitude());
							map.put("type", type);
							if(loupanlistdata!=null&&map!=null){
								loupanlistdata.add(map);
							}
						}
					}
					
					if(type == 2){
						if(loupanlistdata1 == null){
							loupanlistdata1 = new ArrayList<HashMap<String,Object>>();
						}
//						loupanlistdata1.clear();
						loupanlistdata1 = loupanlistdata;
					}
					
					if (MapNumberInfos != null) {
						MapNumberInfos.clear();
						MapNumberInfos = null;
					}
				}
			}
		}
		
		/**
		 * 返回距离中心点最近的商圈的商圈id
		 *
		 * 作者：Ring
		 * 创建于：2013-3-18
		 * @param list  含有经纬度的商圈列表
		 * @param Longitude 地图中心点的经度
		 * @param Latitude 地图中心点的纬度
		 * @return 商圈id
		 */
		
		public String getShangquanID(ArrayList<HashMap<String,Object>> list,double Longitude,double Latitude ){
			String id = "";
			String name = "";
			if(list!=null){
				int i;
				double distance = 0;
				for(i=0;i<list.size();i++){
					if(list.get(i).get("type")!=null
							&&!list.get(i).get("type").toString().trim().equals("")
							&&list.get(i).get("longitude")!=null
							&&!list.get(i).get("longitude").toString().trim().equals("")
							&&list.get(i).get("latitude")!=null
							&&!list.get(i).get("latitude").toString().trim().equals("")
							&&!list.get(i).get("name").toString().trim().equals("")){
						int type = Integer.parseInt(list.get(i).get("type").toString());
						double Longitude1 = Double.parseDouble(list.get(i).get("longitude").toString());
						double Latitude1 = Double.parseDouble(list.get(i).get("latitude").toString());
						double distance1 = GetDistance(Latitude, Longitude, Latitude1, Longitude1);
						if(type == 2&&(distance == 0||distance1<distance)){
							distance = distance1;
							id = list.get(i).get("id").toString();
							name = list.get(i).get("name").toString();
						}
					}
				}
			}
			if(!id.equals("")&&move_tag){
				name =ZoneUtil.getName(SearchRoomsListActivity.this,id);
				if(name.length()>8){
					name = name.substring(0, 8)+"...";
				}
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("address", name);
				msg.setData(b);
				msg.what = 11;
				handler.sendMessage(msg);
			}
			return id;
			
		}
		
		private final static double EARTH_RADIUS = 6378.137;
		private static double rad(double d)
		{
		   return d * Math.PI / 180.0;
		}

		/**
		 * 获取两个经纬度之间的距离
		 *
		 * 作者：Ring
		 * 创建于：2013-3-18
		 * @param lat1
		 * @param lng1
		 * @param lat2
		 * @param lng2
		 * @return
		 */
		public static double GetDistance(double LonA, double LatA, double LonB, double LatB)
		{
			// 东西经，南北纬处理，只在国内可以不处理(假设都是北半球，南半球只有澳洲具有应用意义)  
		    double MLonA = LonA;  
		    double MLatA = LatA;  
		    double MLonB = LonB;  
		    double MLatB = LatB;  
		    // 地球半径（千米）  
		    double R = 6371.004;  
		    double C = Math.sin(rad(LatA)) * Math.sin(rad(LatB)) + Math.cos(rad(LatA)) * Math.cos(rad(LatB)) * Math.cos(rad(MLonA - MLonB));  
		    return (R * Math.acos(C));  
		}
		/* (non-Javadoc)
		 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
		 */
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			
		}
		/* (non-Javadoc)
		 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onLoadMore()
		 */
		@Override
		public void onLoadMore() {
			isclickmore = true;
			tag = 0;
			new Thread(runnable).start();
		}
}
