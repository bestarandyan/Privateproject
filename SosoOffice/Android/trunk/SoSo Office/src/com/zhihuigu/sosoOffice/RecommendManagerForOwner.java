/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.RecommendManagerAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.AreaSelectView;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoEffectiveInfo;
import com.zhihuigu.sosoOffice.model.SoSoOfficeInfo;
import com.zhihuigu.sosoOffice.model.SoSoPushInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author 刘星星
 * @createDate 2013/1/31
 */
public class RecommendManagerForOwner extends BaseActivity implements Activity_interface,OnItemClickListener,IXListViewListener{
	private Button backBtn;
	private RelativeLayout detailLayout,effectiveLayout;//详情布局  有效性验证布局
	private TextView detailText,effectiveText;//详情文字  有效性文字
	private ImageView detailImg,effectiveImg;//详情箭头   有效性箭头
	private Button /*detailBtn,*/effectiveBtn;//详情按钮 有效性箭头
	private XListView listView;//这个就是下面的列表控件啦。。。。。
	RecommendManagerAdapter listAdapter ; //这是列表控件的适配器啦。。。
	private ArrayList<HashMap<String,Object>> detailList;
	private ArrayList<HashMap<String,Object>> effectiveList;
	
	
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private int type = 1;//type 1 推送列表刷新，2有效性验证列表刷新
	private int tag = 0;//第一次进入activity
	
	private LinearLayout noDataLayout;//没有数据时显示的布局
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_recommendmanagerforowner);
		findView();//这里便是找到控件啦。。。
		initView();
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
	}
	@Override
	protected void onRestart() {
		System.out.println("restart-------------------------------");
		super.onRestart();
	}
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			super.onResume();
			return;
		}
		getEffectiveListData();
		initData();
		DBHelper.getInstance(this).execSql(
				"update soso_countinfo set pushcount=0 where userid = '"
						+ MyApplication.getInstance().getSosouserinfo()
								.getUserID() + "'");
		MyApplication.getInstance().setPushcount(0);
		System.out.println("resume-------------------------------");
		if (MyApplication.getInstance().isRecommendOwnerBackBtnVisibility()) {
			backBtn.setVisibility(View.VISIBLE);
		} else {
			backBtn.setVisibility(View.GONE);
		}
		switch (MyApplication.getInstance().getRoleid()) {
		case Constant.TYPE_AGENCY:
			// MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
			// setButtonNumber(mTabWidget.getChildAt(2),MyApplication.getInstance().getPushcount());
			break;
		case Constant.TYPE_CLIENT:
			// setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
			// // setButtonNumber(mTabWidget.getChildAt(2),number);
			break;
		case Constant.TYPE_PROPRIETOR:
			// setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
			MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget
					.getChildAt(2), MyApplication.getInstance().getPushcount());
			// MainTabActivity.mTabWidget.setVisibility(View.GONE);
			// setButtonNumber(mTabWidget.getChildAt(3),MyApplication.getInstance().getRcount());
			break;
		}
		new Thread(runnable).start();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		System.out.println("pause-------------------------------");
		dismiss();
		super.onPause();
	}
	@Override
	protected void onStart() {
		System.out.println("start-------------------------------");
		super.onStart();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(backBtn.getVisibility() == View.GONE){
				showExitDialog();
			}else if(backBtn.getVisibility() == View.VISIBLE){
				MyApplication.getInstance().setRecommendOwnerBackBtnVisibility(false);
				MainTabActivity.mTabHost.setCurrentTab(0);
			}
			
		}
		return true;
	}
	@Override
	public void onClick(View v) {
	if(v == backBtn){
		MyApplication.getInstance().setRecommendOwnerBackBtnVisibility(false);
			MainTabActivity.mTabHost.setCurrentTab(0);
		}else if(v == detailLayout){//正在推送
			type =1;
			detailText.setTextColor(Color.WHITE);
			effectiveText.setTextColor(Color.BLACK);
			detailLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			effectiveLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			detailImg.setImageResource(R.drawable.soso_house_sanjiao);
			effectiveImg.setImageResource(R.drawable.soso_house_sanjiao1);
//			effectiveBtn.setVisibility(View.VISIBLE);
//			detailBtn.setVisibility(View.GONE);
			notifiView();
			new Thread(runnable).start();
		}else if(v == effectiveLayout){//有效性验证
			type = 2;
			detailText.setTextColor(Color.BLACK);
			effectiveText.setTextColor(Color.WHITE);
			detailLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			effectiveLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			detailImg.setImageResource(R.drawable.soso_house_sanjiao1);
			effectiveImg.setImageResource(R.drawable.soso_house_sanjiao);
//			effectiveBtn.setVisibility(View.GONE);
//			detailBtn.setVisibility(View.VISIBLE);
			notifiEffectiveListView();
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
			listAdapter.notifyDataSetChanged();
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
			listAdapter.notifyDataSetChanged();
			dismiss();
		}else if(v == noDataImg){//没有数据时点击图片刷新
			new Thread(runnable).start();
		}
		super.onClick(v);
	}

	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		
		detailLayout = (RelativeLayout) findViewById(R.id.detailLayout);
		effectiveLayout = (RelativeLayout) findViewById(R.id.effectiveLayout);
		detailText = (TextView) findViewById(R.id.detailText);
		effectiveText = (TextView) findViewById(R.id.effecttiveText);
		detailImg = (ImageView) findViewById(R.id.detailImg);
		effectiveImg = (ImageView) findViewById(R.id.effectiveImg);
//		detailBtn = (Button) findViewById(R.id.detailCircle);
		effectiveBtn = (Button) findViewById(R.id.effectiveCircle);
		listView = (XListView) findViewById(R.id.listView);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
		if(type== 1){//正在推送
			noDataText.setText("您暂时还没有正在推送的房源，在房源列表里编辑房源信息，可以把您的房源推送至中介及合作网站");
		}else if(type == 2){//有效性验证
			noDataText.setText("您暂时还没有过期的房源");
		}
	}

	@Override
	public void initView() {//这里就是给控件设置监听事件的啦。。。。。
		backBtn.setOnClickListener(this);
		detailLayout.setOnClickListener(this);
		effectiveLayout.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
		if(effectiveList!=null && effectiveList.size()>0){
			effectiveBtn.setVisibility(View.VISIBLE);
			effectiveBtn.setText(effectiveList.size()+"");
		}else{
			effectiveBtn.setVisibility(View.GONE);
		}
			
	}

	@Override
	public void initData() {
		detailList = new ArrayList<HashMap<String,Object>>();
		effectiveList = new ArrayList<HashMap<String,Object>>();
		notifiView() ;
		new Thread(runnable).start();
	}
	/**
	 * 这个函数了就是拿到推送详情列表需要的数据啦，要小心使用哦
	 * @author 刘星星
	 * @createDate 2013/1/31
	 */
	public void getDetailListData(){
		String name= "";
		String sdpath="";
		File file1;//缩略图文件
		if (detailList == null) {
			detailList = new ArrayList<HashMap<String, Object>>();
		}
		detailList.clear();
//		"pushid text,"+//推送id
//		"pushdate text,"+//推送日期
//		"pushtarget integer,"+//推送目标1网站，2中介
//		"pushstate integer,"+//推送状态0成功，1失败
//		"reviceuserid text,"+//接受推荐的用户id
//		"userid text,"+//推送的用户id
//		"officeid integer,"+//房源id
//		"officemc text,"+//房源名称
//		"username text,"+//用户名
//		"areaup text,"+//面积上限
//		"areadown text,"+//面积下线
//		"address text,"+//房源地址
//		"telephone text,"+//联系该房源的电话
//		"priceup text,"+//价格上限
//		"pricedown text,"+//价格下限
//		"bulidid text,"+//楼盘id
//		"buildmc text"+//楼盘名称
		int tag = 0;
		if(type==1){
			tag=0;
		}else{
			tag=1;
		}
		List<Map<String, Object>> selectresult = null;
		String sql = "select imageid,pushid,pushdate" +
				",pushtarget,pushstate,reviceuserid,userid,officeid,officemc" +
				",username,areaup,areadown,address,telephone,priceup,pricedown" +
				",buildid,buildmc from soso_pushinfo where reviceuserid = '"
				+ MyApplication.getInstance(this).getSosouserinfo(this)
						.getUserID() + "' and pushtag="+tag+" group by officeid  order by pushdate desc";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			HashMap<String, Object> map = null;
			for (int i = 0; i < selectresult.size(); i++) {
				if(selectresult.get(i).get("officeid")!=null&&!
						selectresult.get(i).get("officeid").toString().equals("")
						&&selectresult.get(i).get("pushid")!=null&&!
						selectresult.get(i).get("pushid").toString().equals("")
						&&selectresult.get(i).get("pushstate")!=null&&!
						selectresult.get(i).get("pushstate").toString().equals("")){
					map = new HashMap<String, Object>();
					map.put("pushid", selectresult.get(i).get("pushid")
							.toString());
					map.put("pushstate", selectresult.get(i).get("pushstate")
							.toString());
					if (selectresult.get(i).get("officemc") != null) {
						map.put("number", selectresult.get(i).get("officemc")
								.toString());
					} else {
						map.put("number", "");
					}
					if (selectresult.get(i).get("userid") != null) {
						map.put("userid", selectresult.get(i).get("userid").toString());
					} else {
						map.put("userid", "");
					}
					if (selectresult.get(i).get("username") != null) {
						map.put("username", selectresult.get(i).get("username")
								.toString());
					} else {
						map.put("username", "");
					}
					map.put("officeid", selectresult.get(i).get("officeid")
								.toString());
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
						map.put("phone", selectresult.get(i).get("telephone")
								.toString());
					} else {
						map.put("phone", "");
					}
					if (selectresult.get(i).get("officestate") != null) {
						map.put("state", selectresult.get(i).get("officestate")
								.toString());
					} else {
						map.put("state", 1);
					}
					
//					List<Map<String, Object>> selectresult1 = null;
//					String sql1 = "select pushid from soso_pushinfo where reviceuserid = '"
//							+ MyApplication.getInstance(this).getSosouserinfo(this)
//									.getUserID() + "' and officeid = '"
//							+selectresult.get(i).get("officeid").toString()+"' and pushtarget = 1";
//					selectresult1 = DBHelper.getInstance(this).selectRow(sql1, null);
//					
//					List<Map<String, Object>> selectresult2 = null;
//					String sql2 = "select pushid from soso_pushinfo where reviceuserid = '"
//							+ MyApplication.getInstance(this).getSosouserinfo(this)
//									.getUserID() + "' and officeid = '"
//							+selectresult.get(i).get("officeid").toString()+"' and pushtarget = 2";
//					selectresult2 = DBHelper.getInstance(this).selectRow(sql2, null);
//					int count1 = 0;
//					if(selectresult1!=null){
//						count1 = selectresult1.size();
//					}
//					int count2 = 0;
//					if(selectresult2!=null){
//						count2 = selectresult2.size();
//					}
					map.put("agency", "0");
					map.put("website", "0");
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
					detailList.add(map);	
				}
				
			}
		}
//		if(detailList!=null){
//			detailList.clear();
//			for(int i=0;i<20;i++){
//				HashMap<String,Object> map = new HashMap<String, Object>();
//				map.put("name", "数娱大厦");
//				map.put("number","1616室");
//				map.put("money", "￥3.5-5.0");
//				map.put("acreage", "1000平米");
//				map.put("agency", "33");
//				map.put("website", "55");
//				detailList.add(map);
//			}
//		}
	}
	
//	/**
//	 * 刷新adapter
//	 * 
//	 * 作者：Ring 创建于：2013-2-20
//	 * 
//	 */
//	public void notifyAdapter() {
//		if(type==1){
//			getDetailListData();
//		}else{
//			getEffectiveListData();
//		}
//		listAdapter.notifyDataSetChanged();
//		if(MyApplication.getInstance().getDisplayRoomPhoto()==0){
//			initDialogWindow();
//		}
//	}
	
	/**
	 * 这个函数了就是拿到有效性验证列表需要的数据啦，要小心使用哦
	 * @author 刘星星
	 * @createDate 2013/1/31
	 */
	public void getEffectiveListData(){
		String name= "";
		String sdpath="";
		File file1;//缩略图文件
		if (effectiveList == null) {
			effectiveList = new ArrayList<HashMap<String, Object>>();
		}
		effectiveList.clear();
		List<Map<String, Object>> selectresult = null;
		String sql = "select * from soso_effectiveinfo where createuserid = '"
				+ MyApplication.getInstance(this).getSosouserinfo(this)
						.getUserID() + "'";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			HashMap<String, Object> map = null;
			for (int i = 0; i < selectresult.size(); i++) {
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
						map.put("phone", selectresult.get(i).get("telephone")
								.toString());
					} else {
						map.put("phone", "");
					}
					if (selectresult.get(i).get("officestate") != null) {
						map.put("state", selectresult.get(i).get("officestate")
								.toString());
					} else {
						map.put("state", 1);
					}
					
//					List<Map<String, Object>> selectresult1 = null;
//					String sql1 = "select pushid from soso_pushinfo where reviceuserid = '"
//							+ MyApplication.getInstance(this).getSosouserinfo(this)
//									.getUserID() + "' and officeid = '"
//							+selectresult.get(i).get("officeid").toString()+"' and pushtarget = 1";
//					selectresult1 = DBHelper.getInstance(this).selectRow(sql1, null);
//					
//					List<Map<String, Object>> selectresult2 = null;
//					String sql2 = "select pushid from soso_pushinfo where reviceuserid = '"
//							+ MyApplication.getInstance(this).getSosouserinfo(this)
//									.getUserID() + "' and officeid = '"
//							+selectresult.get(i).get("officeid").toString()+"' and pushtarget = 2";
//					selectresult2 = DBHelper.getInstance(this).selectRow(sql2, null);
//					int count1 = 0;
//					if(selectresult1!=null){
//						count1 = selectresult1.size();
//					}
//					int count2 = 0;
//					if(selectresult2!=null){
//						count2 = selectresult2.size();
//					}
					map.put("agency", "0");
					map.put("website", "0");
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
					map.put("sql", "update soso_effectiveinfo set thumb_sdpath='"+file1.getAbsolutePath()+"' where officeid='"+selectresult.get(i).get("officeid")
							.toString()+"'");
					map.put("request_name", "ImageFileCutForCustom.aspx");
					effectiveList.add(map);	
				}
				
			}
		}
	}
	@Override
	public void notifiView() {
		noDataText.setText("您暂时还没有正在推送的房源，在房源列表里编辑房源信息，可以把您的房源推送至中介及合作网站");
		getDetailListData();
		listAdapter = new RecommendManagerAdapter(this, detailList,1);
		listView.setAdapter(listAdapter);
		listView.setCacheColorHint(0);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(detailList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}
	/**
	 * 刷新有效性验证列表
	 * @author 刘星星
	 * @createDate 2013/1/31
	 */
	public void notifiEffectiveListView(){
		noDataText.setText("您暂时还没有过期的房源");
		getDetailListData();
		listAdapter = new RecommendManagerAdapter(this, detailList,2);
		listView.setAdapter(listAdapter);
		listView.setCacheColorHint(0);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(detailList.size()>0){
			effectiveBtn.setVisibility(View.VISIBLE);
			effectiveBtn.setText(detailList.size()+"");
		}else{
			effectiveBtn.setVisibility(View.GONE);
		}
		if(detailList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(arg0 == listView){
			if(type ==1){
				MyApplication.getInstance().setOfficeid(detailList.get(arg2-1).get("officeid").toString());
			}else{
				MyApplication.getInstance().setOfficeid(effectiveList.get(arg2-1).get("officeid").toString());
			}
			Intent intent = new Intent(this,DetailRoomInfoActivity.class);
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
			if (NetworkCheck.IsHaveInternet(RecommendManagerForOwner.this)) {
				boolean b= false;
				handler.sendEmptyMessage(5);// 开启进度条
//				if(type== 1){//正在推送
					b=getYZPushOffice();
//				}else if(type == 2){//有效性验证
//					b=getEffectiveList();
//				}
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
				if(type==1){
					notifiView();
				}else if(type == 2){
					notifiEffectiveListView();
				}
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
					if(type== 1){//正在推送
						errormsg = "您暂时还没有正在推送的房源，在房源列表里编辑房源信息，可以把您的房源推送至中介及合作网站";
					}else if(type == 2){//有效性验证
						errormsg = "您暂时还没有过期的房源";
					}
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
//				noDataText.setText(errormsg);
				listView.stopRefresh();
//				Toast.makeText(RecommendManagerForOwner.this
//						.getParent(), errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// 没有网络时给用户提示
				listView.stopRefresh();
				MessageBox.CreateAlertDialog(RecommendManagerForOwner.this
						.getParent());
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						RecommendManagerForOwner.this.getParent());
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
				if(type == 2){
					if(effectiveList.size()>0){
						effectiveBtn.setVisibility(View.VISIBLE);
						effectiveBtn.setText(effectiveList.size()+"");
					}else{
						effectiveBtn.setVisibility(View.GONE);
					}
				}
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
	 * 获取业主推送所有楼盘
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 */
	public boolean getYZPushOffice() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		String updatetime = "";
		if(type==1){
			updatetime="updatedate";
		}else{
			updatetime="updatedate1";
		}
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='TPUSH_TIME' and "+updatetime+" = value and value<>'' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null).size()>0){
			return true;
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select "+updatetime+" from soso_configurationinfo where name='TPUSH_TIME' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null);		
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(updatetime) != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(updatetime).toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}

		// params 请求的参数列表
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = StringUtils.formatCalendar1(sdf.format(new Date()),MyApplication.error_value);
		time = StringUtils.formatDate1(time,30);
		if(type==1){
			params.add(new BasicNameValuePair("whereClause", "and PushDate >= "+"'"+time+"'"));
		}else{
			params.add(new BasicNameValuePair("whereClause", "and PushDate < "+"'"+time+"'"));
		}
		params.add(new BasicNameValuePair("UpdateDate", updatedate));
		params.add(new BasicNameValuePair("UserID", MyApplication
				.getInstance(this).getSosouserinfo().getUserID()));
		uploaddata = new SoSoUploadData(this, "YZPushOffice.aspx",
				params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication
				.getInstance(this).getSosouserinfo().getUserID());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 * 
	 * @param districtid
	 */
	private void dealReponse(String userid) {
		String updatetime = "";
		if(type==1){
			updatetime="updatedate";
		}else{
			updatetime="updatedate1";
		}
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set "+updatetime+" = value where name='TPUSH_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set "+updatetime+" = value where name='TPUSH_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoPushInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoPushInfo> soSoPushInfos = null;
			SoSoPushInfo soSoPushInfo = null;
			ContentValues values = new ContentValues();
			soSoPushInfos = gson.fromJson(reponse, listType);
			if (soSoPushInfos != null && soSoPushInfos.size() > 0) {
				for (Iterator<SoSoPushInfo> iterator = soSoPushInfos
						.iterator(); iterator.hasNext();) {
					soSoPushInfo = (SoSoPushInfo) iterator.next();
					if (soSoPushInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_pushinfo",
								"pushid = ?", new String[] { soSoPushInfo.getPushID() });
						continue;
					}
					if (soSoPushInfo != null
							&& soSoPushInfo.getPushID() != null) {
//						"pushid text,"+//推送id
//								"pushdate text,"+//推送日期
//								"pushtarget text,"+//推送目标1网站，2中介
//								"pushstate text,"+//推送状态0成功，1失败
//								"reviceuserid text,"+//接受推荐的用户id
//								"userid text,"+//推送的用户id
//								"officeid integer,"+//房源id
//								"officemc text,"+//房源名称
//								"username text,"+//用户名
//								"areaup text,"+//面积上限
//								"areadown text,"+//面积下线
//								"address text,"+//房源地址
//								"telephone text,"+//联系该房源的电话
//								"priceup text,"+//价格上限
//								"pricedown text,"+//价格下限
//								"bulidid text,"+//楼盘id
//								"buildmc text"+//楼盘名称
						values.put("pushid", soSoPushInfo.getPushID());
						values.put("pushdate", soSoPushInfo.getPushDate());
						values.put("pushtarget", soSoPushInfo.getPushTarget());
						values.put("reviceuserid", userid);
						values.put("userid", soSoPushInfo.getUserID());
						values.put("officeid", soSoPushInfo.getOfficeID());
						values.put("officemc", soSoPushInfo.getOfficeMC());
						values.put("officestate", soSoPushInfo.getOfficeState());
						values.put("buildmc", soSoPushInfo.getBuildMC());
						values.put("buildid", soSoPushInfo.getBuildID());
						if(type==1){
							values.put("pushstate", soSoPushInfo.getPushState());
							values.put("pushtag", 0);
						}else{
							values.put("pushstate", 1);
							values.put("pushtag", 1);
						}
						values.put("username",
								soSoPushInfo.getUserName());
						values.put("areaup",
								soSoPushInfo.getAreaUp());
						values.put("areadown", soSoPushInfo.getAreaDown());
						values.put("address", soSoPushInfo.getAddress());
						values.put("telephone", soSoPushInfo.getTelePhone());
						values.put("priceup", soSoPushInfo.getPriceUp());
						values.put("pricedown", soSoPushInfo.getPricedown());
						values.put("imageid", soSoPushInfo.getShowImageID());
						values.put("buildid", soSoPushInfo.getBuildID());
						values.put("buildmc", soSoPushInfo.getBuildMC());
						if (DBHelper
								.getInstance(RecommendManagerForOwner.this)
								.selectRow(
										"select * from soso_pushinfo where pushid = '"
												+ soSoPushInfo.getPushID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.insert("soso_pushinfo", values);
						} else {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.update("soso_pushinfo",
											values,
											"pushid = ?",
											new String[] { soSoPushInfo
													.getPushID() });
						}

						values.clear();

						values.put("officeid", soSoPushInfo.getOfficeID());
						values.put("createuserid","");
						values.put("areaup", soSoPushInfo.getAreaUp());
						values.put("areadown",soSoPushInfo.getAreaDown());
						values.put("address", soSoPushInfo.getAddress());
						values.put("telephone",
								soSoPushInfo.getTelePhone());
						values.put("storey", 0);
						values.put("floors", 0);
						values.put("priceup", soSoPushInfo.getPriceUp());
						values.put("pricedown", soSoPushInfo.getPricedown());
						values.put("officemc", soSoPushInfo.getOfficeMC());
						values.put("wymanagementfees", "");
						values.put("officetype", 0);
						values.put("keywords", "");
						values.put("fycx", "");
						values.put("zcyh", "");
						values.put("tsyh", "");
						values.put("fyjj", "");
						values.put("updatedate", "");
						values.put("buildid", soSoPushInfo.getBuildID());
						values.put("officestate", soSoPushInfo.getOfficeState());
						values.put("officestatus", "");
						values.put("isprice", "");
						values.put("offadddate", "");
						values.put("imageid", soSoPushInfo.getShowImageID());
						values.put("roomrate", "");
						values.put("nextalertdate", "");

						if (DBHelper
								.getInstance(RecommendManagerForOwner.this)
								.selectRow(
										"select * from soso_officeinfo where officeid = '"
												+ soSoPushInfo.getOfficeID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.insert("soso_officeinfo", values);
						} else {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.update("soso_officeinfo",
											values,
											"officeid = ?",
											new String[] { soSoPushInfo
													.getOfficeID() });
						}
						values.clear();

					}
				}
				if (soSoPushInfos != null) {
					soSoPushInfos.clear();
					soSoPushInfos = null;
				}
				if (values != null) {
					values.clear();
					values = null;
				}

			}
		}
	}
	/**
	 * 
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 * 
	 * @param districtid
	 */
	private void dealReponse1(String userid) {
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoEffectiveInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoEffectiveInfo> SoSoEffectiveInfos = null;
			SoSoEffectiveInfo SoSoEffectiveInfo = null;
			ContentValues values = new ContentValues();
			SoSoEffectiveInfos = gson.fromJson(reponse, listType);
			if (SoSoEffectiveInfos != null && SoSoEffectiveInfos.size() > 0) {
				for (Iterator<SoSoEffectiveInfo> iterator = SoSoEffectiveInfos
						.iterator(); iterator.hasNext();) {
					SoSoEffectiveInfo = (SoSoEffectiveInfo) iterator.next();
					if (SoSoEffectiveInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_effectiveinfo",
								"officeid = ?", new String[] { SoSoEffectiveInfo.getOfficeID() });
						continue;
					}
					if (SoSoEffectiveInfo != null
							&& SoSoEffectiveInfo.getOfficeID() != null) {
						values.put("createuserid", SoSoEffectiveInfo.getCreateUserID());
						values.put("createusername", SoSoEffectiveInfo.getCreateUserName());
						values.put("officeid", SoSoEffectiveInfo.getOfficeID());
						values.put("officemc", SoSoEffectiveInfo.getOfficeMC());
						values.put("officestate", SoSoEffectiveInfo.getOfficeState());
						values.put("areaup",
								SoSoEffectiveInfo.getAreaUp());
						values.put("areadown", SoSoEffectiveInfo.getAreaDown());
						values.put("address", SoSoEffectiveInfo.getAddress());
						values.put("telephone", SoSoEffectiveInfo.getTelePhone());
						values.put("priceup", SoSoEffectiveInfo.getPriceUp());
						values.put("pricedown", "");
						values.put("imageid", SoSoEffectiveInfo.getShowImageID());
						values.put("buildid", SoSoEffectiveInfo.getBuildID());
						values.put("buildmc", SoSoEffectiveInfo.getBuildMC());
						if (DBHelper
								.getInstance(RecommendManagerForOwner.this)
								.selectRow(
										"select * from soso_effectiveinfo where officeid = '"
												+ SoSoEffectiveInfo.getOfficeID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.insert("soso_effectiveinfo", values);
						} else {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.update("soso_effectiveinfo",
											values,
											"officeid = ?",
											new String[] { SoSoEffectiveInfo
													.getOfficeID() });
						}

						values.clear();

						values.put("officeid", SoSoEffectiveInfo.getOfficeID());
						values.put("createuserid","");
						values.put("areaup", SoSoEffectiveInfo.getAreaUp());
						values.put("areadown",SoSoEffectiveInfo.getAreaDown());
						values.put("address", SoSoEffectiveInfo.getAddress());
						values.put("telephone",
								SoSoEffectiveInfo.getTelePhone());
						values.put("storey", 0);
						values.put("floors", 0);
						values.put("priceup", SoSoEffectiveInfo.getPriceUp());
						values.put("officemc", SoSoEffectiveInfo.getOfficeMC());
						values.put("wymanagementfees", "");
						values.put("officetype", 0);
						values.put("keywords", "");
						values.put("fycx", "");
						values.put("zcyh", "");
						values.put("tsyh", "");
						values.put("fyjj", "");
						values.put("updatedate", "");
						values.put("buildid", SoSoEffectiveInfo.getBuildID());
						values.put("officestate", SoSoEffectiveInfo.getOfficeState());
						values.put("officestatus", "");
						values.put("isprice", "");
						values.put("offadddate", "");
						values.put("roomrate", "");
						values.put("nextalertdate", "");

						if (DBHelper
								.getInstance(RecommendManagerForOwner.this)
								.selectRow(
										"select * from soso_officeinfo where officeid = '"
												+ SoSoEffectiveInfo.getOfficeID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.insert("soso_officeinfo", values);
						} else {
							DBHelper.getInstance(RecommendManagerForOwner.this)
									.update("soso_officeinfo",
											values,
											"officeid = ?",
											new String[] { SoSoEffectiveInfo
													.getOfficeID() });
						}
						values.clear();

					}
				}
				if (SoSoEffectiveInfos != null) {
					SoSoEffectiveInfos.clear();
					SoSoEffectiveInfos = null;
				}
				if (values != null) {
					values.clear();
					values = null;
				}

			}
		}
	}
	
	
	/**
	 * 获取用户所有楼盘
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 */
	public boolean getEffectiveList() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='' and updatedate = value and value<>'' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null).size()>0){
			return true;
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get("updatedate") != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(
						"updatedate").toString());
			}
		}
		if (selectresult != null) {
			selectresult.clear();
			selectresult = null;
		}
		// params 请求的参数列表
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UpdateDate", updatedate));
		String whereclause = "";
		whereclause = "CreateUserID="
				+ MyApplication.getInstance(this).getSosouserinfo(this)
						.getUserID();
		params.add(new BasicNameValuePair("whereClause", whereclause));
		params.add(new BasicNameValuePair("orderBy", "OffAdddate"));
		uploaddata = new SoSoUploadData(this, "OfficeCheck.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse1(MyApplication.getInstance(this).getSosouserinfo(this)
				.getUserID());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		}else{
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		String updatetime = "";
		if(type==1){
			updatetime="updatedate";
		}else{
			updatetime="updatedate1";
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select "+updatetime+" from soso_configurationinfo where name='TPUSH_TIME' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null);		
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(updatetime) != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(updatetime).toString());
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
