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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.CollectManagerAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoBuildInfo;
import com.zhihuigu.sosoOffice.model.SoSoFavoriteInfo;
import com.zhihuigu.sosoOffice.model.SoSoOfficeInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author 刘星星
 * @createDate 2013.2.1 这是一个收藏管理的类哦。。。。。。
 * 
 */
public class CollectManagerActivity extends BaseActivity implements
		Activity_interface, OnItemClickListener, IXListViewListener {
	private Button backBtn, editBtn;
	private XListView listView;
	private ArrayList<HashMap<String, Object>> list;
	private CollectManagerAdapter adapter;

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
		setContentView(R.layout.a_collectmanager);
		findView();
		initView();
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
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
		if(MyApplication.getInstance().isCollectBackBtnVisibility()){
			backBtn.setVisibility(View.VISIBLE);
		}else{
			backBtn.setVisibility(View.GONE);
		}
		new Thread(runnable).start();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			MyApplication.getInstance().setCollectBackBtnVisibility(false);
			Intent intent = new Intent(this, MainActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MainTabActivity.mTabHost.setCurrentTab(0);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("MainActivity", intent);
			View view = w.getDecorView();
			MainFirstTab.group.setContentView(view);
		} else if (v == editBtn) {
			String btnStr = editBtn.getText().toString();
			if (btnStr.equals("编辑")) {
				notifiListView(1);
				editBtn.setText("完成");
			} else if (btnStr.equals("完成")) {
				notifiListView(0);
				editBtn.setText("编辑");
			}

		} else if(v == displayImgBtn){//显示房源图片
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			if(backBtn.getVisibility() == View.VISIBLE){
				MyApplication.getInstance().setCollectBackBtnVisibility(false);
				Intent intent = new Intent(this, MainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainTabActivity.mTabHost.setCurrentTab(0);
				Window w = MainFirstTab.group.getLocalActivityManager()
						.startActivity("MainActivity", intent);
				View view = w.getDecorView();
				MainFirstTab.group.setContentView(view);
			}else if(backBtn.getVisibility() == View.GONE){
				showExitDialog();
			}
			/**/
			
			return true;
		}
		return true;
	}

	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		editBtn = (Button) findViewById(R.id.editBtn);
		listView = (XListView) findViewById(R.id.listView);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		// 后退跳转到第一个Activity界面
		Intent intent = new Intent(this, MainActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Window w = MainFirstTab.group.getLocalActivityManager().startActivity(
				"MainActivity", intent);
		View view = w.getDecorView();
		MainFirstTab.group.setContentView(view);
	}

	@Override
	public void initView() {
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
		backBtn.setOnClickListener(this);
		editBtn.setOnClickListener(this);
		
	}

	@Override
	public void initData() {
		if(MyApplication.getInstance().getSosouserinfo(this)==null){
			return;
		}
		list = new ArrayList<HashMap<String, Object>>();
		getListData();
		notifiListView(0);

	}
	/**
	 * 收藏房源列表数据
	 *
	 * 作者：Ring
	 * 创建于：2013-2-21
	 */
	private void getListData() {
		if (list == null) {
			list = new ArrayList<HashMap<String, Object>>();
		}
		list.clear();
		String name= "";
		String sdpath="";
		File file1;//缩略图文件
		// "_id integer primary key autoincrement,"+//自动增长_id
		// "favoriteid text,"+//收藏id
		// "userid text,"+//用户id
		// "username text,"+//用户名称
		// "officeid text,"+//房源id
		// "officemc text,"+//房源名称
		// "type integer,"+//收藏类型1：房源收藏，2：用户收藏
		// "groupid text,"+//分组id
		// "adddate text,"+//收藏日期
		// "oprice text,"+//房源价格
		// "oarea text,"+//房源面积
		// "buildmc text,"+//楼盘名称
		// "buildid text,"+//楼盘id
		// "ocreateuserid text,"+//房源所属人的id
		// "ocreateuserphone text"+//房源所属人的电话
		List<Map<String, Object>> selectresult = null;
		String sql = "select * from soso_favoriteinfo where userid = '"
				+ MyApplication.getInstance(this).getSosouserinfo(this)
						.getUserID() + "' order by favoriteid desc";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			HashMap<String, Object> map = null;
			for (int i = 0; i < selectresult.size(); i++) {
				if(selectresult.get(i).get("officeid")!=null&&!
						selectresult.get(i).get("officeid").toString().equals("")
						&&selectresult.get(i).get("favoriteid")!=null&&!
						selectresult.get(i).get("favoriteid").toString().equals("")){
					map = new HashMap<String, Object>();
					map.put("favoriteid", selectresult.get(i).get("favoriteid")
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
					if (selectresult.get(i).get("ocreateuserid") != null) {
						map.put("ocreateuserid",
								selectresult.get(i).get("ocreateuserid").toString());
					} else {
						map.put("ocreateuserid", "");
					}
					
					if (selectresult.get(i).get("ocreateusername") != null) {
						map.put("ocreateusername",
								selectresult.get(i).get("ocreateusername").toString());
					} else {
						map.put("ocreateusername", "");
					}
					if (selectresult.get(i).get("buildmc") != null) {
						map.put("name", selectresult.get(i).get("buildmc").toString());
					} else {
						map.put("name", "");
					}
					if (selectresult.get(i).get("oprice") != null) {
						map.put("money", selectresult.get(i).get("oprice").toString());
					} else {
						map.put("money", "");
					}
					if (selectresult.get(i).get("oarea") != null) {
						map.put("acreage", selectresult.get(i).get("oarea").toString());
					} else {
						map.put("acreage", "");
					}
					if (selectresult.get(i).get("ocreateuserphone") != null) {
						map.put("phone", selectresult.get(i).get("ocreateuserphone")
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
					map.put("sql", "update soso_pushinfo set thumb_sdpath='"+file1.getAbsolutePath()+"' where officeid='"+selectresult.get(i).get("officeid")
							.toString()+"'");
					map.put("request_name", "ImageFileCutForCustom.aspx");
					
					list.add(map);	
				}
				
			}
		}
	}

	@Override
	public void notifiView() {

	}

	/**
	 * 刷新adapter
	 * 
	 * 作者：Ring 创建于：2013-2-20
	 */
	public void notifyAdapter() {
		getListData();
		String btnStr = editBtn.getText().toString();
		if (btnStr.equals("编辑")) {
			notifiListView(0);
		} else if (btnStr.equals("完成")) {
			notifiListView(1);
		}
		if(list.size() == 0){//当没有数据的时候隐藏编辑按钮
			editBtn.setVisibility(View.GONE);
		}else{
			editBtn.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 根据状态加载列表控件
	 * 
	 * @author 刘星星
	 * @createDate 2013/2/1
	 * @param type状态
	 *            0代表非编辑状态 1代表编辑状态
	 */
	public void notifiListView(int type) {
		adapter = new CollectManagerAdapter(this, list, type);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(type == 1){
			listView.setOnItemClickListener(null);
		}else if(type == 0){
			listView.setOnItemClickListener(this);
		}
		if(list.size() == 0){//当没有数据的时候隐藏编辑按钮
			editBtn.setVisibility(View.GONE);
		}else{
			editBtn.setVisibility(View.VISIBLE);
		}
		if(list.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MyApplication.getInstance().setOfficeid(list.get(arg2-1).get("officeid").toString());
		Intent intent = new Intent(this, DetailRoomInfoActivity.class);
		MyApplication.getInstance().setCollectRoom(false);
		startActivity(intent);
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
			if (NetworkCheck.IsHaveInternet(CollectManagerActivity.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);// 开启进度条
				b = getUserFavorite();
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
					errormsg = "您暂时还没有收藏任何房源，查看房源，有合适的可以放到收藏夹中防止自己忘记哦!";
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				listView.stopRefresh();
				noDataText.setText(errormsg);
//				Toast.makeText(CollectManagerActivity.this.getParent(), errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// 没有网络时给用户提示
				listView.stopRefresh();
				MessageBox.CreateAlertDialog(CollectManagerActivity.this
						.getParent());
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						CollectManagerActivity.this.getParent());
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
	public boolean getUserFavorite() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='TFAVORITES_TIME' and updatedate = value and value<>'' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null).size()>0){
			return true;
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TFAVORITES_TIME' and userid = '"
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

		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UpdateDate", updatedate));
		params.add(new BasicNameValuePair("UserID", MyApplication
				.getInstance(this).getSosouserinfo(this).getUserID()));
		params.add(new BasicNameValuePair("GroupID", ""));
		uploaddata = new SoSoUploadData(this, "FavoritesSelect.aspx",
				params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication
				.getInstance(this).getSosouserinfo().getUserID());
		params.clear();
		params = null;
		if(StringUtils.CheckReponse(reponse)){
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
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TFAVORITES_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TFAVORITES_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoFavoriteInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoFavoriteInfo> soSoFavoriteInfos = null;
			SoSoFavoriteInfo soSoFavoriteInfo = null;
			ContentValues values = new ContentValues();
			soSoFavoriteInfos = gson.fromJson(reponse, listType);
			if (soSoFavoriteInfos != null && soSoFavoriteInfos.size() > 0) {
				for (Iterator<SoSoFavoriteInfo> iterator = soSoFavoriteInfos
						.iterator(); iterator.hasNext();) {
					soSoFavoriteInfo = (SoSoFavoriteInfo) iterator.next();
					if (soSoFavoriteInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_favoriteinfo",
								"favoriteid = ?", new String[] { soSoFavoriteInfo
								.getFavId() });
						continue;
					}
					if (soSoFavoriteInfo != null
							&& soSoFavoriteInfo.getFavId() != null) {
						values.put("favoriteid", soSoFavoriteInfo.getFavId());
						values.put("userid", userid);
						values.put("username", soSoFavoriteInfo.getUserName());
						values.put("type", soSoFavoriteInfo.getType());
						values.put("groupid", 0);
						values.put("adddate", soSoFavoriteInfo.getAdddate());
						values.put("oprice", soSoFavoriteInfo.getOPrice());
						values.put("oarea", soSoFavoriteInfo.getOArea());
						values.put("buildmc", soSoFavoriteInfo.getBuildMC());
						values.put("buildid", soSoFavoriteInfo.getBuildID());
						values.put("ocreateuserid",
								soSoFavoriteInfo.getOCreateUserID());
						values.put("ocreateusername",
								soSoFavoriteInfo.getOCreateUserName());
						values.put("ocreateuserphone",
								soSoFavoriteInfo.getOCreateUserPhone());
						values.put("officeid", soSoFavoriteInfo.getOfficeId());
						values.put("officemc", soSoFavoriteInfo.getOfficeMc());
						values.put("officestate", soSoFavoriteInfo.getOfficeState());
						values.put("officestate", soSoFavoriteInfo.getOfficeState());
						values.put("imageid", soSoFavoriteInfo.getShowImageID());
						if (DBHelper
								.getInstance(CollectManagerActivity.this)
								.selectRow(
										"select * from soso_favoriteinfo where favoriteid = '"
												+ soSoFavoriteInfo.getFavId()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(CollectManagerActivity.this)
									.insert("soso_favoriteinfo", values);
						} else {
							DBHelper.getInstance(CollectManagerActivity.this)
									.update("soso_favoriteinfo",
											values,
											"favoriteid = ?",
											new String[] { soSoFavoriteInfo
													.getFavId() });
						}

						values.clear();

						values.put("officeid", soSoFavoriteInfo.getOfficeId());
						values.put("createuserid",
								soSoFavoriteInfo.getOCreateUserID());
						values.put("areaup", soSoFavoriteInfo.getOArea());
						values.put("areadown", "");
						values.put("address", "");
						values.put("telephone",
								soSoFavoriteInfo.getOCreateUserPhone());
						values.put("storey", 0);
						values.put("floors", 0);
						values.put("priceup", soSoFavoriteInfo.getOPrice());
						values.put("pricedown", "");
						values.put("officemc", soSoFavoriteInfo.getOfficeMc());
						values.put("wymanagementfees", "");
						values.put("officetype", 0);
						values.put("keywords", "");
						values.put("fycx", "");
						values.put("zcyh", "");
						values.put("tsyh", "");
						values.put("fyjj", "");
						values.put("updatedate", "");
						values.put("buildid", soSoFavoriteInfo.getBuildID());
						values.put("officestate", soSoFavoriteInfo.getOfficeState());
						values.put("officestatus", "");
						values.put("isprice", "");
						values.put("offadddate", "");
						values.put("roomrate", "");
						values.put("nextalertdate", "");
						values.put("imageid", soSoFavoriteInfo.getShowImageID());

						if (DBHelper
								.getInstance(CollectManagerActivity.this)
								.selectRow(
										"select * from soso_officeinfo where officeid = '"
												+ soSoFavoriteInfo.getOfficeId()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(CollectManagerActivity.this)
									.insert("soso_officeinfo", values);
						} else {
							DBHelper.getInstance(CollectManagerActivity.this)
									.update("soso_officeinfo",
											values,
											"officeid = ?",
											new String[] { soSoFavoriteInfo
													.getOfficeId() });
						}
						values.clear();

					}
				}
				if (soSoFavoriteInfos != null) {
					soSoFavoriteInfos.clear();
					soSoFavoriteInfos = null;
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
						"select updatedate from soso_configurationinfo where name='TFAVORITES_TIME' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get("updatedate") != null) {
				updatedate = (selectresult.get(selectresult.size() - 1).get(
						"updatedate").toString());
			}
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
