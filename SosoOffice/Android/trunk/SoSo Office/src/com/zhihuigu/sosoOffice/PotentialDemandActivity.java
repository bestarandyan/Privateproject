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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.PotentialDemandAdapter;
import com.zhihuigu.sosoOffice.Adapter.RecommendManagerAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoDemandInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 刘星星
 * @createDate 2013/1/31
 * 潜在客户求类
 *
 */
public class PotentialDemandActivity extends BaseActivity implements Activity_interface,OnItemClickListener,IXListViewListener{
	private Button backBtn;
	private RelativeLayout oneLayout,twoLayout,threeLayout;//谁看过我的房源的布局  
	private TextView oneText,twoText,threeText;//
	private ImageView oneImg,twoImg,threeImg;//
	private XListView listView;//这个就是下面的列表控件啦。。。。。
	PotentialDemandAdapter listAdapter ; //这是列表控件的适配器啦。。。
	private ArrayList<HashMap<String,Object>> list;
//	private ArrayList<HashMap<String,Object>> twoList;
//	private ArrayList<HashMap<String,Object>> threeList;
	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private String officeid = "";
	private int tag = 0;//第一次进入activity
	
	private LinearLayout noDataLayout;//没有数据时显示的布局
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_potentialdemand);
		findView();//这里便是找到控件啦。。。
		initView();
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
	}
	@Override
	public void onClick(View v) {//用户类型1谁收藏我的房源2谁看过我的房源3满足搜索条件的用户
		if(v == backBtn){
			Intent intent = new Intent(this, MainActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("id", intent);
			View view = w.getDecorView();
			MainFirstTab.group.setContentView(view);
		}else if(v == oneLayout){//谁看过我的房源
			oneText.setTextColor(Color.WHITE);
			twoText.setTextColor(Color.BLACK);
			threeText.setTextColor(Color.BLACK);
			oneLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			twoLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			threeLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			oneImg.setImageResource(R.drawable.soso_house_sanjiao);
			twoImg.setImageResource(R.drawable.soso_house_sanjiao1);
			threeImg.setImageResource(R.drawable.soso_house_sanjiao1);
			MyApplication.getInstance().setUsertype(2);
			MyApplication.getInstance().setLinkManTitle("客户查看记录");
			new Thread(runnable).start();
		}else if(v == twoLayout){//谁搜索过我的房源
			oneText.setTextColor(Color.BLACK);
			twoText.setTextColor(Color.WHITE);
			threeText.setTextColor(Color.BLACK);
			oneLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			twoLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			threeLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			oneImg.setImageResource(R.drawable.soso_house_sanjiao1);
			twoImg.setImageResource(R.drawable.soso_house_sanjiao);
			threeImg.setImageResource(R.drawable.soso_house_sanjiao1);
			MyApplication.getInstance().setUsertype(1);
			MyApplication.getInstance().setLinkManTitle("客户搜索记录");
			new Thread(runnable).start();
		}else if(v == threeLayout){//谁收藏过我的房源
			oneText.setTextColor(Color.BLACK);
			twoText.setTextColor(Color.BLACK);
			threeText.setTextColor(Color.WHITE);
			oneLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			twoLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			threeLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			oneImg.setImageResource(R.drawable.soso_house_sanjiao1);
			twoImg.setImageResource(R.drawable.soso_house_sanjiao1);
			threeImg.setImageResource(R.drawable.soso_house_sanjiao);
			MyApplication.getInstance().setUsertype(3);
			MyApplication.getInstance().setLinkManTitle("客户收藏记录");
			new Thread(runnable).start();
		}else if(v == displayImgBtn){//显示房源图片
			MyApplication.getInstance().setDisplayRoomPhoto(1);
			ContentValues values = new ContentValues();
			values.put("isshowimage", 1);
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] { MyApplication.getInstance(this)
							.getSosouserinfo(this).getUserID() });
			if (values != null) {
				values.clear();
				values = null;
			}
			notifiView();
			dismiss();
		}else if(v == hideImgBtn){//不显示房源图片
			MyApplication.getInstance().setDisplayRoomPhoto(2);
			ContentValues values = new ContentValues();
			values.put("isshowimage", 2);
			DBHelper.getInstance(this).update(
					"sososettinginfo",
					values,
					"userid = ?",
					new String[] { MyApplication.getInstance(this)
							.getSosouserinfo(this).getUserID() });
			if (values != null) {
				values.clear();
				values = null;
			}
			notifiView();
			dismiss();
		}else if(v == noDataImg){//没有数据时点击图片刷新
			new Thread(runnable).start();
		}
		super.onClick(v);
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		oneLayout = (RelativeLayout) findViewById(R.id.oneLayout);
		twoLayout = (RelativeLayout) findViewById(R.id.twoLayout);
		oneText = (TextView) findViewById(R.id.oneText);
		twoText = (TextView) findViewById(R.id.twoText);
		oneImg = (ImageView) findViewById(R.id.oneRightImg);
		twoImg = (ImageView) findViewById(R.id.twoRightImg);
		threeLayout = (RelativeLayout) findViewById(R.id.threeLayout);
		threeImg = (ImageView) findViewById(R.id.threeImg);
		threeText = (TextView) findViewById(R.id.threeText);
		listView = (XListView) findViewById(R.id.listView);
		
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
	}

	@Override
	public void initView() {//这里就是给控件设置监听事件的啦。。。。。
		backBtn.setOnClickListener(this);
		oneLayout.setOnClickListener(this);
		twoLayout.setOnClickListener(this);
		threeLayout.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
	}

	@Override
	public void initData() {
//		DBHelper.getInstance(this).execSql("update soso_countinfo set rcount=0 where userid = '"
//				+MyApplication.getInstance().getSosouserinfo().getUserID()+"'");
//					MyApplication.getInstance().setRcount(0);
		if(list==null){
			list = new ArrayList<HashMap<String,Object>>();
		}
		getListData();
		notifiView();
		new Thread(runnable).start();
	}
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			super.onResume();
			return;
		}
		initData();
		getListData();
		notifiView();
		int type = MyApplication.getInstance().getUsertype();
		switch(type){
		case 1://搜索
			oneText.setTextColor(Color.BLACK);
			twoText.setTextColor(Color.WHITE);
			threeText.setTextColor(Color.BLACK);
			oneLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			twoLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			threeLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			oneImg.setImageResource(R.drawable.soso_house_sanjiao1);
			twoImg.setImageResource(R.drawable.soso_house_sanjiao);
			threeImg.setImageResource(R.drawable.soso_house_sanjiao1);
			break;
		case 2://查看
			oneText.setTextColor(Color.WHITE);
			twoText.setTextColor(Color.BLACK);
			threeText.setTextColor(Color.BLACK);
			oneLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			twoLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			threeLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			oneImg.setImageResource(R.drawable.soso_house_sanjiao);
			twoImg.setImageResource(R.drawable.soso_house_sanjiao1);
			threeImg.setImageResource(R.drawable.soso_house_sanjiao1);
			break;
		case 3://收藏
			oneText.setTextColor(Color.BLACK);
			twoText.setTextColor(Color.BLACK);
			threeText.setTextColor(Color.WHITE);
			oneLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			twoLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			threeLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			oneImg.setImageResource(R.drawable.soso_house_sanjiao1);
			twoImg.setImageResource(R.drawable.soso_house_sanjiao1);
			threeImg.setImageResource(R.drawable.soso_house_sanjiao);
			break;
		}
//		if(MyApplication.getInstance().isPotentialDemandBack()){
//			backBtn.setVisibility(View.VISIBLE);
//		}else{
//			backBtn.setVisibility(View.GONE);
//		}
		super.onResume();
	}
	
//	/**
//	 * 这个函数了就是拿到"谁看过我的房源"列表需要的数据啦，要小心使用哦
//	 * @author 刘星星
//	 * @createDate 2013/1/31
//	 */
//	public void getOneListData(){
//		if(oneList!=null){
//			oneList.clear();
//			for(int i=0;i<20;i++){
//				HashMap<String,Object> map = new HashMap<String, Object>();
//				map.put("name", "数娱大厦");
//				map.put("number","1616室");
//				map.put("money", "￥3.5-5.0");
//				map.put("acreage", "1000平米");
//				map.put("people", "刘星星  武国庆  程康  安迪 杨雨珊 刘杨 朱鹏 欧阳剑");
//				oneList.add(map);
//			}
//			notifiView() ;
//		}
//	}
//	/**
//	 * 这个函数了就是谁搜索过我的房源列表需要的数据啦，要小心使用哦
//	 * @author 刘星星
//	 * @createDate 2013/1/31
//	 */
//	public void getTwoListData(){
//		if(twoList!=null){
//			twoList.clear();
//			for(int i=0;i<20;i++){
//				HashMap<String,Object> map = new HashMap<String, Object>();
//				map.put("name", "金茂大厦");
//				map.put("number","1216室");
//				map.put("money", "￥7.5-15.0");
//				map.put("acreage", "2000平米");
//				map.put("people", " 刘杨 朱鹏 欧阳剑");
//				twoList.add(map);
//			}
//		}
//	}
	/**
	 * 这个函数了就是拿到谁收藏过我的房源列表需要的数据啦，要小心使用哦
	 * @author 刘星星
	 * @createDate 2013/1/31
	 * 用户类型1谁收藏我的房源2谁看过我的房源3满足搜索条件的用户
	 */
	public void getListData(){
		String name= "";
		String sdpath="";
		File file1;//缩略图文件
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		list.clear();
		
		List<Map<String, Object>> selectresult = null;
		HashMap<String, Object> map = null;
		selectresult=DBHelper
		.getInstance(PotentialDemandActivity.this)
		.selectRow(
				"select * from soso_demandinfo where usertype = "+MyApplication.getInstance().getUsertype()
						+" and createuserid='"+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()
						+"' group by officeid", null);
		if (selectresult != null) {
			for (int i = 0; i < selectresult.size(); i++) {
//				values.put("officeid", soSoDemandInfo.getOfficeID());
//				values.put("userid",
//						soSoDemandInfo.getUserID());
//				values.put("usertype", usertype);
//				values.put("areaup", "");
//				values.put("areadown", "");
//				values.put("address", "");
//				values.put("priceup", "");
//				values.put("pricedown", "");
//				values.put("officemc", "");
//				values.put("buildid", "");
//				values.put("buildmc", "");
//				values.put("officestate","1");
//				values.put("username", soSoDemandInfo.getUserName());
//				values.put("email", soSoDemandInfo.getEmail());
//				values.put("telephone", soSoDemandInfo.getTelephone());
				
//				map.put("name", "数娱大厦");
//				map.put("number","1616室");
//				map.put("money", "￥3.5-5.0");
//				map.put("acreage", "1000平米");
//				map.put("people", "刘星星  武国庆  程康  安迪 杨雨珊 刘杨 朱鹏 欧阳剑");
				if(selectresult.get(i).get("officeid")!=null&&!
						selectresult.get(i).get("officeid").toString().equals("")){
					map = new HashMap<String, Object>();
					map.put("officeid", selectresult.get(i).get("officeid")
							.toString());
					if (selectresult.get(i).get("officemc") != null) {
						map.put("number", selectresult.get(i).get("officemc")
								.toString());
					} else {
						map.put("number", "");
					}
					if (selectresult.get(i).get("userid") != null) {
						map.put("createuserid", selectresult.get(i).get("userid").toString());
					} else {
						map.put("createuserid", "");
					}
					if (selectresult.get(i).get("username") != null) {
						map.put("username", selectresult.get(i).get("username")
								.toString());
					} else {
						map.put("username", "");
					}
					if (selectresult.get(i).get("buildid") != null) {
						map.put("buildid", selectresult.get(i).get("buildid")
								.toString());
					} else {
						map.put("buildid", "");
					}
					if (selectresult.get(i).get("buildmc") != null) {
						map.put("name", selectresult.get(i).get("buildmc").toString());
					} else {
						map.put("name", "");
					}
					if (selectresult.get(i).get("priceup") != null) {
						map.put("money", selectresult.get(i).get("priceup").toString());
					} else {
						map.put("money", "");
					}
					if (selectresult.get(i).get("areaup") != null) {
						map.put("acreage", selectresult.get(i).get("areaup").toString());
					} else {
						map.put("acreage", "");
					}
					if (selectresult.get(i).get("telephone") != null) {
						map.put("telephone", selectresult.get(i).get("telephone")
								.toString());
					} else {
						map.put("telephone", "");
					}
					if (selectresult.get(i).get("officestate") != null) {
						map.put("state", selectresult.get(i).get("officestate")
								.toString());
					} else {
						map.put("state", 1);
					}
					
					if (selectresult.get(i).get("roleid") != null) {
						map.put("roleid", selectresult.get(i).get("roleid")
								.toString());
					} else {
						map.put("roleid", 1);
					}
					
					String names = getWhousername(selectresult.get(i).get("officeid")
							.toString());
					map.put("people", names);
					if(selectresult.get(i).get("imageid")!=null){
						name = MD5.getMD5(selectresult.get(i).get("imageid")
								.toString()
								+ "缩略图.jpg")
								+ ".jpg";
					}
					if (selectresult.get(i).get("thumb_sdpath") != null) {
						file1 = new File(selectresult.get(i).get("thumb_sdpath")
								.toString());
						if (!(file1.exists() && file1.isFile())) {
							file1 = FileTools
									.getFile(getResources().getString(
													R.string.root_directory),
													getResources().getString(
															R.string.room_image), name);
						}
					}else{
						file1 = FileTools
								.getFile(
										getResources().getString(
												R.string.root_directory),
												getResources().getString(
														R.string.room_image), name);
					}
					if(selectresult.get(i).get("imageid")!=null){
						map.put("id", selectresult.get(i).get("imageid")
								.toString());
					}else{
						map.put("id", "");
					}
					map.put("file", file1);
					map.put("pixelswidth", (MyApplication.getInstance(this).getScreenWidth() / 3 - 10));
					map.put("pixelsheight", (MyApplication.getInstance(this).getScreenWidth() / 3 - 10));
					map.put("sql", "update soso_pushinfo set thumb_sdpath='"+file1.getAbsolutePath()+"' where officeid='"+selectresult.get(i).get("officeid")
							.toString()+"'");
					map.put("request_name", "ImageFileCutForCustom.aspx");
					if(names.length()>0){
						list.add(map);	
					}
				}
				
			}
		}
	}
	
	/**
	 * 根据officeid，满足的用户列表
	 *
	 * 作者：Ring
	 * 创建于：2013-3-12
	 * @param officeid
	 * @return
	 */
	public String getWhousername(String officeid){
		List<Map<String, Object>> selectresult = null;
		StringBuffer buffer = new StringBuffer("");
		selectresult=DBHelper
		.getInstance(PotentialDemandActivity.this)
		.selectRow(
				"select * from soso_demandinfo where usertype = "+MyApplication.getInstance().getUsertype()
						+" and createuserid='"+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()
						+"' and officeid = '"+officeid+"' and roleid = '4'", null);
		if (selectresult != null) {
			for (int i = 0; i < selectresult.size(); i++) {
				if(i>=5){
					break;
				}
				if (selectresult.get(i).get("username") != null) {
					buffer.append(selectresult.get(i).get("username")
							.toString()+"、");
				} else {
					buffer.append("");
				}
			}
		}
		
		String str = "";
		if(buffer!=null&&buffer.length()>1){
			str = buffer.subSequence(0, buffer.length()-1).toString();
		}
		
		return str;
	}
	
	
	/**
	 * 根据officeid，满足的用户列表
	 *
	 * 作者：Ring
	 * 创建于：2013-3-12
	 * @param officeid
	 * @return
	 */
	public ArrayList<HashMap<String,Object>> getWhousernameList(String officeid){
		List<Map<String, Object>> selectresult = null;
		HashMap<String, Object> map = null;
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		selectresult=DBHelper
		.getInstance(PotentialDemandActivity.this)
		.selectRow(
				"select * from soso_demandinfo where usertype = "+MyApplication.getInstance().getUsertype()
						+" and createuserid='"+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()
						+"' and officeid = '"+officeid+"'  and roleid = '4'", null);
		if (selectresult != null) {
			for (int i = 0; i < selectresult.size(); i++) {
				map = new HashMap<String, Object>();
				int roleid=0;
				try {
					roleid = Integer.parseInt(selectresult.get(i)
							.get("roleid").toString());
				} catch (Exception e) {
				}
				if(roleid==UserType.UserTypeIntermediary.getValue()){//中介类型
					map.put("headImageId", R.drawable.soso_zhongjei);
				} else if(roleid==UserType.UserTypeCustomer.getValue()){
					map.put("headImageId", R.drawable.soso_kehu);
				} else if(roleid==UserType.UserTypeOwner.getValue()){
					map.put("headImageId", R.drawable.soso_yezhu);
				} else{
					map.put("headImageId", R.drawable.soso_yezhu);
				}
				map.put("contactuserid", selectresult.get(i).get("userid").toString());
				map.put("name", selectresult.get(i).get("username").toString());
				list.add(map);
			}
		}
		
		return list;
	}
	
	/**
	 * 刷新adapter
	 * 
	 * 作者：Ring 创建于：2013-2-20
	 */
	public void notifyAdapter() {
		getListData();
		notifiView();
	}
	
	@Override
	public void notifiView() {
		listAdapter = new PotentialDemandAdapter(this, list);
		listView.setAdapter(listAdapter);
		listView.setCacheColorHint(0);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(list.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}
//	/**
//	 * 刷新有谁看过我的房源列表
//	 * @author 刘星星
//	 * @createDate 2013/1/31
//	 */
//	public void notifiTwoListView(){
//		listAdapter = new PotentialDemandAdapter(this, twoList);
//		listView.setAdapter(listAdapter);
//		listView.setCacheColorHint(0);
//		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//	}
//	/**
//	 * 刷新有谁收藏过我的房源列表
//	 * @author 刘星星
//	 * @createDate 2013/1/31
//	 */
//	public void notifiThreeListView(){
//		listAdapter = new PotentialDemandAdapter(this, threeList);
//		listView.setAdapter(listAdapter);
//		listView.setCacheColorHint(0);
//		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this, MainActivity.class)
			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("id", intent);
			View view = w.getDecorView();
			MainFirstTab.group.setContentView(view);
		}
		return true;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg0 == listView){
			MyApplication.getInstance().setPotentialDemandBack(true);
			Intent intent = new Intent(this,LinkmanActivity.class);
			intent.putExtra("activityFlag", 2);
			intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) getWhousernameList(list.get(arg2-1).get("officeid").toString()));
			startActivity(intent);
		}
	}
	
	
	/**
	 * 处理耗时操作
	 * 
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(PotentialDemandActivity.this.getParent())) {
				boolean b = false;
				handler.sendEmptyMessage(5);// 开启进度条
				b=getUseroffices();
				handler.sendEmptyMessage(6);// 关闭进度条
				if(b){
					handler.sendEmptyMessage(1);// 刷新列表
				}else{
					handler.sendEmptyMessage(3);// 刷新列表
				}
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
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
				notifyAdapter();
				handler.sendEmptyMessage(7);
				listView.stopRefresh();
				break;
			case 2:// 从登录界面跳转到注册界面
				break;
			case 3:// 登录失败给用户提示
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoNoData.getValue())) {
					errormsg = "您的房源还很冷清哦，赶快完善您的房源吧，数据越完善，被关注的概率越高哦，完善房源请到主界面中的房源管理中进行完善！";
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				noDataText.setText(errormsg);
//				Toast.makeText(PotentialDemandActivity.this.getParent(), errormsg, Toast.LENGTH_SHORT).show();
				listView.stopRefresh();
				notifyAdapter();
				break;
			case 4:// 没有网络时给用户提示
				listView.stopRefresh();
				MessageBox.CreateAlertDialog(PotentialDemandActivity.this.getParent());
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(PotentialDemandActivity.this.getParent());
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
//				displayRoomImgDialog();
				break;
			case 7://是否设置过显示图片
				if(MyApplication.getInstance().getDisplayRoomPhoto()==0){
					if(tag == 0){
						displayRoomImgDialog();
						tag = 1;
					}
				}
				break;
			}
		};
	};

	/**
	 * 获取用户所有楼盘
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 */
	public boolean getUseroffices() {//用户类型1谁收藏我的房源2谁看过我的房源3满足搜索条件的用户
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		int type = MyApplication.getInstance().getUsertype();
		String time = "";
		String tablename = "";
		if(type==1){
			time="updatedate";
			tablename = "TVISITOFFICE_TIME";
		}else if(type==2){
			time="updatedate1";
			tablename = "TVISITOFFICE_TIME";
		}else if(type==3){
			time="updatedate2";
			tablename = "TFAVORITES_TIME";
		}
		// params 请求的参数列表
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='"+tablename+"' and "+time+" = value and value<>'' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null).size()>0){
			return true;
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select "+time+" from soso_configurationinfo where name='"+tablename+"' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(time) != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(time).toString());
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
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(this).getSosouserinfo(this).getUserID()));
		String myinter = "";
		if(type==1){
			myinter="whoWhereOfficeSelect.aspx";
		}else if(type==2){
			params.add(new BasicNameValuePair("UpdateDate", updatedate));
			myinter="whoVisitOfficeSelect.aspx";
		}else if(type==3){
			params.add(new BasicNameValuePair("UpdateDate", updatedate));
			myinter="whoFavOfficeSelect.aspx";
		}
		uploaddata = new SoSoUploadData(this, myinter, params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(type,MyApplication.getInstance(this).getSosouserinfo(this)
				.getUserID(),time,tablename);
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
	 * @param usertype //用户类型1谁收藏我的房源2谁看过我的房源3满足搜索条件的用户
	 */
	private void dealReponse(int usertype,String userid,String time,String tablename) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set "+time+" = value where name='"+tablename+"' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set "+time+" = value where name='"+tablename+"' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoDemandInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoDemandInfo> soSoDemandInfos = null;
			ContentValues values = new ContentValues();
			SoSoDemandInfo soSoDemandInfo = null;
			soSoDemandInfos = gson.fromJson(reponse, listType);
			if (soSoDemandInfos != null && soSoDemandInfos.size() > 0) {
				for (Iterator<SoSoDemandInfo> iterator = soSoDemandInfos
						.iterator(); iterator.hasNext();) {
					soSoDemandInfo = (SoSoDemandInfo) iterator.next();
					if (soSoDemandInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_demandinfo",
								"officeid = ? and usertype = ? and userid = ? and createuserid = ?", new String[] { soSoDemandInfo
								.getOfficeId(),usertype+"",soSoDemandInfo.getUserId(),userid });
						continue;
					}
//					"officeid text,"+//房源id
//					"userid text,"+//userid
//					"usertype integer,"+//用户类型1谁收藏我的房源2谁看过我的房源3满足搜索条件的用户
//					"areaup text,"+//面积上限
//					"areadown text,"+//面积下线
//					"address text,"+//地址
//					"priceup text,"+//单价上限
//					"pricedown text,"+//单价下限
//					"officemc text,"+//房源名称
//					"officetype integer,"+//房源类型 0纯写字楼，1商住楼2，酒店式公寓，3园区，4商务中心
//					"buildid text,"+//所属楼盘id
//					"buildmc text,"+//所属楼盘名称
//					"officestate integer,"+//房源状态 :0；未审核，1：审核通过/未租，2：已租3：私信提醒中4：房源导入5：待删除
//					"username text,"+//用户名
//					"email text,"+//邮箱
//					"telephone text,"+//电话
					if (soSoDemandInfo != null
							&& soSoDemandInfo.getOfficeId() != null) {
						values.put("officeid", soSoDemandInfo.getOfficeId());
						values.put("userid",
								soSoDemandInfo.getUserId());
						values.put("createuserid",userid);
						values.put("usertype", usertype);
						values.put("areaup", soSoDemandInfo.getAreaUp());
						values.put("areadown", "");
						values.put("address", soSoDemandInfo.getAddress());
						values.put("priceup", soSoDemandInfo.getPriceUp());
						values.put("pricedown", "");
						values.put("officemc", soSoDemandInfo.getOfficeMC());
						values.put("buildid", soSoDemandInfo.getBuildId());
						values.put("buildmc", soSoDemandInfo.getBuildMC());
						values.put("officestate","1");
						values.put("username", soSoDemandInfo.getUserName());
						values.put("email", soSoDemandInfo.getEmail());
						values.put("telephone", soSoDemandInfo.getTelephone());
						values.put("imageid", soSoDemandInfo.getShowImageID());
						values.put("roleid", soSoDemandInfo.getRoleId());
//						values.put("tsyh", SoSoDemandInfo.getTSYH());
//						values.put("fyjj", SoSoDemandInfo.getFYJJ());
//						values.put("updatedate", "");
//						values.put("buildid", SoSoDemandInfo.getBuildID());
//						values.put("buildmc", SoSoDemandInfo.getBuildMC());
//						values.put("officestate",
//								SoSoDemandInfo.getOfficeState());
//						values.put("officestatus", "");
//						values.put("isprice", "");
//						values.put("offadddate", "");
//						values.put("roomrate", SoSoDemandInfo.getRoomRate());
//						values.put("nextalertdate", "");

						if (DBHelper
								.getInstance(PotentialDemandActivity.this)
								.selectRow(
										"select * from soso_demandinfo where officeid = '"
												+ soSoDemandInfo.getOfficeId()
												+ "' and usertype = "+usertype+" and userid='"+soSoDemandInfo.getUserId()+"' and createuserid = '"+userid+"'", null).size() <= 0) {
							DBHelper.getInstance(PotentialDemandActivity.this)
									.insert("soso_demandinfo", values);
						} else {
							DBHelper.getInstance(PotentialDemandActivity.this)
									.update("soso_demandinfo",
											values,
											"officeid = ? and usertype = ? and userid = ? and createuserid = ?", new String[] { soSoDemandInfo
													.getOfficeId(),usertype+"",soSoDemandInfo.getUserId(),userid });
						}
						values.clear();
					}
				}
				if (soSoDemandInfos != null) {
					soSoDemandInfos.clear();
					soSoDemandInfos = null;
				}
				if(values!=null){
					values.clear();
					values = null;
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		int type = MyApplication.getInstance().getUsertype();
		String time = "";
		String tablename = "";
		if(type==1){
			time="updatedate";
			tablename = "TVISITOFFICE_TIME";
		}else if(type==2){
			time="updatedate1";
			tablename = "TVISITOFFICE_TIME";
		}else if(type==3){
			time="updatedate2";
			tablename = "TFAVORITES_TIME";
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select "+time+" from soso_configurationinfo where name='"+tablename+"' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(time) != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(time).toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		listView.setRefreshTime(updatedate);
		new Thread(runnable).start();
	}
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onLoadMore()
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	
}
