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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.RecommendManagerAdapterForAgency;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoPushInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author 刘星星
 * @createDate 2013/2/19
 *
 */
public class RecommendManagerForAgency extends BaseActivity implements OnItemClickListener,Activity_interface,IXListViewListener{
	private Button backBtn;
	private XListView listView;
	private ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
	RecommendManagerAdapterForAgency adapter = null;
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private int tag = 0;//第一次进入activity
	
	private LinearLayout noDataLayout;//没有数据时显示的布局
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_recommendmanagerforagency);
		findView();
		initView();
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			MyApplication.getInstance().setRecommendAgencyBackBtnVisibility(false);
			MainTabActivity.mTabHost.setCurrentTab(0);
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
			adapter.notifyDataSetChanged();
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
			adapter.notifyDataSetChanged();
			dismiss();
		}else if(v == noDataImg){//没有数据时点击图片刷新
			new Thread(runnable).start();
		}
		super.onClick(v);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MyApplication.getInstance().setOfficeid(list.get(arg2-1).get("officeid").toString());
		Intent intent = new Intent(this,DetailRoomInfoActivity.class);
		startActivity(intent);
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		listView = (XListView) findViewById(R.id.listView);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
		
	}
	@Override
	public void initView() {
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		backBtn.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		
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
		DBHelper.getInstance(this).execSql(
				"update soso_countinfo set pushcount=0 where userid = '"
						+ MyApplication.getInstance().getSosouserinfo()
								.getUserID() + "'");
		MyApplication.getInstance().setPushcount(0);
		if (MyApplication.getInstance().isRecommendAgencyBackBtnVisibility()) {
			backBtn.setVisibility(View.VISIBLE);

		} else {
			backBtn.setVisibility(View.GONE);
		}
		switch (MyApplication.getInstance().getRoleid()) {
		case Constant.TYPE_AGENCY:
			// MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(1),
			// MyApplication.getInstance().getLettercount());
			MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget
					.getChildAt(2), MyApplication.getInstance().getPushcount());
			break;
		case Constant.TYPE_CLIENT:
			// MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(1),
			// MyApplication.getInstance().getLettercount());
			// // setButtonNumber(mTabWidget.getChildAt(2),number);
			break;
		case Constant.TYPE_PROPRIETOR:
			// setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
			// MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(2),
			// MyApplication.getInstance().getPushcount());
			// MainTabActivity.mTabWidget.setVisibility(View.GONE);
			// setButtonNumber(mTabWidget.getChildAt(3),MyApplication.getInstance().getRcount());
			break;
		}
		new Thread(runnable).start();
		super.onResume();
	}
	@Override
	public void initData() {
		getListData();
		notifiView();
//		list.clear();
//		for(int i=0;i<20;i++){
//			HashMap<String,Object> map = new HashMap<String, Object>();
//			map.put("state", "1");
//			map.put("roomNumber", "2020");
//			map.put("name", "数娱大厦");
//			map.put("money", "3.5-5.0");
//			map.put("acreage", "1000");
//			map.put("phone", "18917216840");
//			list.add(map);
//		}
//		notifiView();
	}
	/**
	 * 收藏房源列表数据
	 *
	 * 作者：Ring
	 * 创建于：2013-2-21
	 */
	private void getListData() {
		String name= "";
		String sdpath="";
		File file1;//缩略图文件
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		list.clear();
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
		List<Map<String, Object>> selectresult = null;
		String sql = "select count(officeid) as count, imageid, pushid,pushdate" +
				",pushtarget,pushstate,reviceuserid,userid,officeid,officemc" +
				",username,areaup,areadown,address,telephone,priceup,pricedown" +
				",buildid,buildmc from soso_pushinfo where reviceuserid = '"
				+ MyApplication.getInstance(this).getSosouserinfo(this)
						.getUserID() + "' and pushtarget=1 group by officeid order by pushdate desc";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			HashMap<String, Object> map = null;
			for (int i = 0; i < selectresult.size(); i++) {
				if(selectresult.get(i).get("officeid")!=null&&!
						selectresult.get(i).get("officeid").toString().equals("")
						&&selectresult.get(i).get("pushid")!=null&&!
						selectresult.get(i).get("pushid").toString().equals("")){
					map = new HashMap<String, Object>();
					map.put("pushid", selectresult.get(i).get("pushid")
							.toString());
					if (selectresult.get(i).get("officemc") != null) {
						map.put("roomNumber", selectresult.get(i).get("officemc")
								.toString());
					} else {
						map.put("roomNumber", "");
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
					list.add(map);	
				}
				
			}
		}
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
		adapter = new RecommendManagerAdapterForAgency(this, list);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setCacheColorHint(0);
		if(list.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(backBtn.getVisibility() == View.GONE){
				showExitDialog();
			}else{
				MyApplication.getInstance().setRecommendAgencyBackBtnVisibility(false);
				MainTabActivity.mTabHost.setCurrentTab(0);
			}
			
		}
		return true;
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
			if (NetworkCheck.IsHaveInternet(RecommendManagerForAgency.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);// 开启进度条
				b=getAgencyPush();
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
					errormsg = "您暂时还没有收到任何推送的房源信息，如有新的推送信息，我们会尽快为您更新数据";
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				noDataText.setText(errormsg);
				listView.stopRefresh();
//				Toast.makeText(RecommendManagerForAgency.this
//						.getParent(), errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// 没有网络时给用户提示
				listView.stopRefresh();
				MessageBox.CreateAlertDialog(RecommendManagerForAgency.this
						.getParent());
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						RecommendManagerForAgency.this.getParent());
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
	public boolean getAgencyPush() {
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='TPUSH_TIME' and updatedate = value and value<>'' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null).size()>0){
			return true;
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TPUSH_TIME' and userid = '"
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
		params.add(new BasicNameValuePair("orderBy", "PushDate"));
		uploaddata = new SoSoUploadData(this, "PushGetPaged.aspx",
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
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TPUSH_TIME' and userid = '"
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
						values.put("pushstate", soSoPushInfo.getPushState());
						values.put("reviceuserid", userid);
						values.put("userid", soSoPushInfo.getUserID());
						values.put("officeid", soSoPushInfo.getOfficeID());
						values.put("officemc", soSoPushInfo.getOfficeMC());
						values.put("officestate", soSoPushInfo.getOfficeState());
						values.put("buildmc", soSoPushInfo.getBuildMC());
						values.put("buildid", soSoPushInfo.getBuildID());
						values.put("username",
								soSoPushInfo.getUserName());
						values.put("areaup",
								soSoPushInfo.getAreaUp());
						values.put("areadown", soSoPushInfo.getAreaDown());
						values.put("address", soSoPushInfo.getAddress());
						values.put("telephone", soSoPushInfo.getTelePhone());
						values.put("priceup", soSoPushInfo.getPriceUp());
						values.put("pricedown", soSoPushInfo.getPricedown());
						values.put("buildid", soSoPushInfo.getBuildID());
						values.put("buildmc", soSoPushInfo.getBuildMC());
						values.put("imageid", soSoPushInfo.getShowImageID());
						if (DBHelper
								.getInstance(RecommendManagerForAgency.this)
								.selectRow(
										"select * from soso_pushinfo where pushid = '"
												+ soSoPushInfo.getPushID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RecommendManagerForAgency.this)
									.insert("soso_pushinfo", values);
						} else {
							DBHelper.getInstance(RecommendManagerForAgency.this)
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
						values.put("imageid", soSoPushInfo.getShowImageID());
						values.put("isprice", "");
						values.put("offadddate", "");
						values.put("roomrate", "");
						values.put("nextalertdate", "");

						if (DBHelper
								.getInstance(RecommendManagerForAgency.this)
								.selectRow(
										"select * from soso_officeinfo where officeid = '"
												+ soSoPushInfo.getOfficeID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RecommendManagerForAgency.this)
									.insert("soso_officeinfo", values);
						} else {
							DBHelper.getInstance(RecommendManagerForAgency.this)
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
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TPUSH_TIME' and userid = '"
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
