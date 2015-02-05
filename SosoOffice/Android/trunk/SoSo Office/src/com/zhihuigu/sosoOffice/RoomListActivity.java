/**
 * 
 */
package com.zhihuigu.sosoOffice;
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
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoBuildInfo;
import com.zhihuigu.sosoOffice.model.SoSoBuildUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @author 刘星星
 * @createDate 2013/1/18
 * 房源列表界面
 */
public class RoomListActivity extends BaseActivity implements Activity_interface,IXListViewListener{
	private Button backBtn,addRoomBtn;
	private XListView roomListView;
	private SimpleAdapter sa = null;//列表的适配器
	private ArrayList<HashMap<String, Object>>  roomList;
	
	private SoSoUploadData uploaddata;//服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	
	private LinearLayout noDataLayout;//没有数据时显示的布局
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_roomlist);
		findView();
		initView();
		initData();
		MyApplication.getInstance().setRoomListBack(false);
		if(getIntent().getIntExtra("tag", 0)==1){
			new Thread(runnable).start();
		}
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			Intent intent = new Intent(this, MainActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("activityid",intent);
		    View view = w.getDecorView();
		    //把View添加大ActivityGroup中
		    MainFirstTab.group.setContentView(view);
		} else if (v == addRoomBtn) {//添加房源
			MyApplication.getInstance().setAddRoomFlag(0);
			Intent intent = new Intent(this, ReleaseNewRoomActivity.class);
			intent.putExtra("activityFlag", 0);
			startActivity(intent);
		}else if(v == noDataImg){//没有数据时点击图片刷新
			new Thread(runnable).start();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this, MainActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MainTabActivity.mTabHost.setCurrentTab(0);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("MainActivity",intent);
		    View view = w.getDecorView();
		    MainFirstTab.group.setContentView(view);
		}
		return false;
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		roomListView = (XListView) findViewById(R.id.roomList);
		addRoomBtn = (Button) findViewById(R.id.addRoomBtn);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
	}
	@Override
	public void initView() {
		roomListView.setPullLoadEnable(false);
		roomListView.setPullRefreshEnable(true);
		roomListView.setXListViewListener(this);
		backBtn.setOnClickListener(this);
		addRoomBtn.setOnClickListener(this);
	}
	@Override
	public void initData() {
//		roomList = new ArrayList<HashMap<String,Object>>();
//		HashMap<String,Object> map = new HashMap<String, Object>();
//		map.put("name", "锦辉商业办公大厦");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "绿地大厦");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "美罗城");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "长城新世纪");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "房地大厦");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "金茂大厦");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "环球金融中心大厦");
//		roomList.add(map);
//		map = new HashMap<String, Object>();
//		map.put("name", "长城大厦");
//		roomList.add(map);
//		notifiView();
	}
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		notifiView();
		super.onResume();
	}
	
	@Override
	public void notifiView() {
		getUserBuildsList();
		sa = new SimpleAdapter(this, roomList, R.layout.item_city_list,
				new String[]{"name"}, new int[]{R.id.cityItemText});
		roomListView.setAdapter(sa);
		roomListView.setOnItemClickListener(new listItemClickListener());
		roomListView.setCacheColorHint(0);
		roomListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(roomList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
			noDataImg.setOnClickListener(this);
		}else{
			noDataLayout.setVisibility(View.GONE);
			noDataImg.setOnClickListener(null);
		}
	}
	/**
	 * 刷新listView列表数据
	 *
	 * 作者：Ring
	 * 创建于：2013-1-31
	 */
	public void notifyAdapter(){
		getUserBuildsList();
		if(sa!=null){
			sa.notifyDataSetChanged();
		}
	}
	
	/**
	 * 根据用户id获取楼盘列表
	 *
	 * 作者：Ring
	 * 创建于：2013-1-31
	 */
	
	public void getUserBuildsList(){
		//将集合中的数据清空，重新获取数据
		if(roomList==null){
			roomList = new ArrayList<HashMap<String,Object>>();
		}
		roomList.clear();
		SoSoBuildUserInfo soSoBuildUserInfo = new SoSoBuildUserInfo();
		HashMap<String, Object> map = null;
		String sql = null;
		if(MyApplication.getInstance(this).getSosouserinfo()!=null){
			sql = "select distinct buildid,address,userid,buildmc,buildtype from soso_builduserinfo where userid='" + MyApplication.getInstance(this).getSosouserinfo().getUserID()
				+ "'";
		}else{
			sql = "select distinct buildid,address,userid,buildmc, buildtype from soso_builduserinfo";
		}
		LinkedList<SoSoBuildUserInfo> soSoBuildUserInfos = soSoBuildUserInfo.SelectBuildInfo(this, sql);
		if(soSoBuildUserInfos!=null){
			for (Iterator<SoSoBuildUserInfo> iterator = soSoBuildUserInfos
					.iterator(); iterator.hasNext();) {
				soSoBuildUserInfo = (SoSoBuildUserInfo) iterator.next();
				if(soSoBuildUserInfo.getBuildID()!=null
						&&!soSoBuildUserInfo.getBuildID().equals("")
						&&soSoBuildUserInfo.getBuildMC()!=null
						&&!soSoBuildUserInfo.getBuildMC().equals("")){
					map = new HashMap<String, Object>();
					map.put("id", soSoBuildUserInfo.getBuildID());
					map.put("name", soSoBuildUserInfo.getBuildMC());
					roomList.add(map);
				}
			}
		}
		
	}
	
	/**
	 * 房源列表的点击事件监听
	 * @author 刘星星
	 * @createDate 2013/1/18
	 *
	 */
	class listItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(RoomListActivity.this,RoomManagerActivity.class);
			intent.putExtra("tag", 1);
			MyApplication.getInstance().setBuildid(roomList.get(arg2-1).get("id").toString());
			MyApplication.getInstance().setRoomBack(true);
			MyApplication.getInstance().isRoomBack();
			startActivity(intent);
		}
		
	}
	

	/**
	 * 处理耗时操作
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
			if (NetworkCheck.IsHaveInternet(RoomListActivity.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);//开启进度条
				b=getUserBuilds();
				handler.sendEmptyMessage(6);// 关闭进度条
				if(b){
					handler.sendEmptyMessage(1);//刷新列表
				}else{
					handler.sendEmptyMessage(3);//刷新列表
				}
				
				if(runnable_tag){
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
	 * @author Ring
	 * @since 2013-01-31
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 刷新列表
				notifiView();
				roomListView.stopRefresh();
				break;
			case 2:// 从登录界面跳转到注册界面
				break;
			case 3:// 登录失败给用户提示
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoNoData.getValue())) {
					errormsg = "您暂无任何房源数据，请点击右上角的加号，赶紧发布您的房源吧";
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				roomListView.stopRefresh();
				noDataText.setText(errormsg);
//				Toast.makeText(RoomListActivity.this.getParent(), errormsg, Toast.LENGTH_SHORT).show();
				break;
			case 4:// 没有网络时给用户提示
				roomListView.stopRefresh();
				MessageBox.CreateAlertDialog(RoomListActivity.this.getParent());
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(RoomListActivity.this.getParent());
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
			}
		};
	};
	
	

	/**
	 * 获取用户所有楼盘
	 *
	 * 作者：Ring
	 * 创建于：2013-1-31
	 */
	public boolean getUserBuilds() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		// params 请求的参数列表
//		if(DBHelper
//				.getInstance(this)
//				.selectRow(
//						"select * from soso_configurationinfo where name='TBUILD_TIME' and updatedate = value and value<>'' and userid = '"
//				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
//						null).size()>0){
//			return true;
//		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TBUILD_TIME' and userid = '"
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

		//params 请求的参数列表
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UpdateDate", ""));
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(this).getSosouserinfo().getUserID()));
		uploaddata = new SoSoUploadData(this, "BuildSelectByUserId.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication.getInstance(this).getSosouserinfo().getUserID());
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
	 * 作者：Ring
	 * 创建于：2013-1-31
	 * @param districtid
	 */
	private void dealReponse(String userid) {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TBUILD_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TBUILD_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoBuildUserInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoBuildUserInfo> soSoBuildUserInfos = null;
			SoSoBuildUserInfo soSoBuildUserInfo = new SoSoBuildUserInfo();
			soSoBuildUserInfos = gson.fromJson(reponse, listType);
			if (soSoBuildUserInfos != null && soSoBuildUserInfos.size() > 0) {
				soSoBuildUserInfo.InsertBuildInfos(this, soSoBuildUserInfos,userid);
			}
			if (soSoBuildUserInfos != null) {
				soSoBuildUserInfos.clear();
				soSoBuildUserInfos = null;
			}
			
			Type listType1 = new TypeToken<LinkedList<SoSoBuildInfo>>() {
			}.getType();
			LinkedList<SoSoBuildInfo> soSoBuildInfos = null;
			SoSoBuildInfo soSoBuildInfo = new SoSoBuildInfo();
			soSoBuildInfos = gson.fromJson(reponse, listType1);
			if (soSoBuildInfos != null && soSoBuildInfos.size() > 0) {
				soSoBuildInfo.InsertBuildInfos(this, soSoBuildInfos);
			}
			if (soSoBuildInfos != null) {
				soSoBuildInfos.clear();
				soSoBuildInfos = null;
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
						"select updatedate from soso_configurationinfo where name='TBUILD_TIME' and userid = '"
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
		roomListView.setRefreshTime(updatedate);
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
