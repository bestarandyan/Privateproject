/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.RoomLayerAdapter;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerAdatper;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerForListAdapter;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerGridViewAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoBuildUserInfo;
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
 * @createDate 2013/1/18 房源管理类
 */
public class RoomManagerActivity extends BaseActivity implements
		Activity_interface, OnItemClickListener,IXListViewListener{
	private Button backBtn, switchBtn, addRoomBtn;// 返回按钮 切换编辑与非编辑按钮 添加房源按钮
	private RelativeLayout roomListLayout, daiShenHeLayout;// 审核通过的房源列表    待审核的房源列表切换
	private TextView roomListText, daiShenHeText;
	private ImageView roomListImg, daiShenHeImg;
	private TextView addressText;
	private GridView layerGv;
	public static XListView roomLV;
	private Button bianJi;// 编辑按钮
	private ArrayList<String> layerList = new ArrayList<String>();
	private ArrayList<HashMap<String, Object>> roomList = new ArrayList<HashMap<String, Object>>();// 图形列表的房源结合
	private ArrayList<HashMap<String, Object>> roomListForLinear = new ArrayList<HashMap<String, Object>>();// 房源文字形式列表
	private ArrayList<HashMap<String, Object>> roomListForDaiShenHe = new ArrayList<HashMap<String, Object>>();// //待审核的房源列表数据集合
//	public static int list_type = 0;// 0代表为图形列表显示 1代表为文字列表显示
	private int bianji_state = 0;// 0代表编辑按钮的功能为切换到编辑模式， 1代表切换到非编辑模式

	private SoSoUploadData uploaddata;// 服务器请求对象
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private String buildid = "";// 楼盘id
	private String buildmc = "";// 楼盘名称
	private RoomManagerGridViewAdapter roomadapter = null;// 房间列表适配器

	private LinearLayout layerLayout;
	private int tag = 0;//1从楼盘列表界面跳出来的，2从添加房源跳转过来的
	private int tag1 = 0;//第一次进入activity
	TextView layerNumber;
	private boolean dialogDisplay = false;//专门处理从添加新房源首次进入房源管理界面时点击显示图片时刷新列表
	
	private LinearLayout noDataLayout;//没有数据时显示的布局
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	private LinearLayout floorLayout;//点击回到楼层列表布局
	private ImageView floorImg;//回到楼层列表的图片
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_roommanager);
		findView();
		initView();
		initData();
		tag = getIntent().getIntExtra("tag", 0);
		if (tag == 1||tag == 2) {
			new Thread(runnable).start();
		}
	}
	/**
	 * 返回函数
	 * @author 刘星星
	 * @createDate 2013/4/9
	 */
	private void backFun(){
		if(MyApplication.getInstance().getRoom_state_for_examine() == 1){
//			MyApplication.getInstance().setRoomManagerStateFlag(0);
//			MyApplication.getInstance().setRoomManagerForm(list_type);
		}else{
//			MyApplication.getInstance().setRoomManagerStateFlag(1);
		}
		MyApplication.getInstance(this).setRoom_list_state(1);// 设置当前房源列表为非编辑状态
//		MyApplication.getInstance(this).setRoom_state_for_examine(1);// 设置当前列表为审核通过的房源列表
		finish();
	}
	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			backFun();
		} else if (v == switchBtn) {
			if (MyApplication.getInstance().getRoomManagerForm() == 0) {// 切换到文字形式
				roomLV.setVisibility(View.VISIBLE);
				layerGv.setVisibility(View.GONE);
				switchBtn.setBackgroundResource(R.drawable.soso_sekuai_btn);
				bianJi.setVisibility(View.VISIBLE);
				floorImg.setVisibility(View.VISIBLE);
				floorLayout.setOnClickListener(this);
				notifiViewForList(roomListForLinear);
				MyApplication.getInstance().setRoomManagerForm(1);
			} else if (MyApplication.getInstance().getRoomManagerForm() == 1) {// 切换到图形列表形式
				roomLV.setVisibility(View.GONE);
				layerGv.setVisibility(View.VISIBLE);
				notifyAdapter();
				MyApplication.getInstance().setRoomManagerForm(0);
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.GONE);
				floorLayout.setOnClickListener(null);
			}
		} else if (v == addRoomBtn) {//添加房源
			MyApplication.getInstance().setAddRoomFlag(1);
			Intent intent = new Intent(this, ReleaseNewRoomActivity.class);
			intent.putExtra("activityFlag", 1);
			intent.putExtra("buildmc", buildmc);
			intent.putExtra("buildid", buildid);
			startActivity(intent);
		} else if (v == roomListLayout) {//审核通过的房源列表
			shenHeEd();
		} else if (v == daiShenHeLayout) {//待审核的房源列表
			if (MyApplication.getInstance().getRoomManagerForm() == 1) {
				roomLV.setVisibility(View.VISIBLE);
				layerGv.setVisibility(View.GONE);
			}
			floorImg.setVisibility(View.GONE);
			floorLayout.setOnClickListener(null);
			roomListLayout.setClickable(true);
			daiShenHeLayout.setClickable(false);
			bianji_state = 0;// 非编辑状态
			bianJi.setText("编辑");
			MyApplication.getInstance(this).setRoom_state_for_examine(2);// 设置当前列表为待审核的房源列表
			MyApplication.getInstance(this).setRoom_list_state(1);// 设置当前房源列表为非编辑状态
			switchBtn.setVisibility(View.GONE);
			bianJi.setVisibility(View.VISIBLE);
			daiShenHeText.setTextColor(Color.WHITE);
			roomListText.setTextColor(Color.rgb(102, 102, 102));
			daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao);
			roomListImg.setImageResource(R.drawable.soso_house_sanjiao1);
			roomListLayout
					.setBackgroundResource(R.drawable.soso_email_title_bg2);
			daiShenHeLayout
					.setBackgroundResource(R.drawable.soso_email_title_bg1);
			 getDataForDaiShenHe();// 加载待审核的集合数据
			notifiViewForList(roomListForLinear);

		} else if (v == bianJi) {
			int room_state = MyApplication.getInstance(this)
					.getRoom_state_for_examine();
			if (bianji_state == 0) {
				// 设置文字列表形式为编辑状态，这一行代码必须写在notifiViewForList();之前,否则编辑需要的按钮不会出来。
				MyApplication.getInstance(this).setRoom_list_state(2);
				if (room_state == 1) {
					notifiViewForList(roomListForLinear);// 刷新文字列表，显示编辑需要的按钮
				} else {
					notifiViewForList(roomListForLinear);// 刷新待审核的房源的文字列表，显示编辑需要的按钮
				}
				bianji_state = 1;
				bianJi.setText("完成");
			} else if (bianji_state == 1) {
				// 设置文字列表形式为编辑状态，这一行代码必须写在notifiViewForList();之前,否则编辑需要的按钮不会出来。
				MyApplication.getInstance(this).setRoom_list_state(1);
				if (room_state == 1) {
					notifiViewForList(roomListForLinear);// 刷新文字列表，显示编辑需要的按钮
				} else {
					notifiViewForList(roomListForLinear);// 刷新待审核的房源的文字列表，显示编辑需要的按钮
				}
				bianji_state = 0;
				bianJi.setText("编辑");
			}
		} else if (v == layerLayout) {//点击回到楼层列表
			floorFlag = false;
			notifyAdapter();
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
//			listAdapter.notifyDataSetChanged();
			if(dialogDisplay){
				notifiViewForList(roomListForLinear);
			}
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
//			listAdapter.notifyDataSetChanged();
			if(dialogDisplay){
				notifiViewForList(roomListForLinear);
			}
			dismiss();
		}else if(v == floorLayout){//点击回到楼层列表
			MyApplication.getInstance().setRoomManagerForm(0);
			switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
			bianJi.setVisibility(View.GONE);
			notifyAdapter();
			floorFlag = false;
		}else if(v == noDataImg){//没有数据时点击图片刷新
			new Thread(runnable).start();
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			backFun();
		}
		return true;
	}

	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		switchBtn = (Button) findViewById(R.id.switchoverBtn);
		addRoomBtn = (Button) findViewById(R.id.addRoomBtn);
		roomListLayout = (RelativeLayout) findViewById(R.id.roomListLayout);
		daiShenHeLayout = (RelativeLayout) findViewById(R.id.daishenheLayout);
		roomListText = (TextView) findViewById(R.id.roomListText);
		daiShenHeText = (TextView) findViewById(R.id.daishenheText);
		roomListImg = (ImageView) findViewById(R.id.roomListImg);
		daiShenHeImg = (ImageView) findViewById(R.id.daiShenHeImg);
		addressText = (TextView) findViewById(R.id.address);
		roomLV = (XListView) findViewById(R.id.roomListView);
		bianJi = (Button) findViewById(R.id.bianjiBtn);
		layerGv = (GridView) findViewById(R.id.layerGridview);
		layerLayout = (LinearLayout) findViewById(R.id.layerLayout);
		layerNumber = (TextView) findViewById(R.id.layerNumber);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
		floorLayout = (LinearLayout) findViewById(R.id.floor_layout);
		floorImg = (ImageView) findViewById(R.id.floorImg);
	}

	@Override
	public void initView() {
		roomLV.setPullLoadEnable(false);
		roomLV.setPullRefreshEnable(true);
		roomLV.setXListViewListener(this);
		backBtn.setOnClickListener(this);
		switchBtn.setOnClickListener(this);
		addRoomBtn.setOnClickListener(this);
		roomListLayout.setOnClickListener(this);
		daiShenHeLayout.setOnClickListener(this);
		bianJi.setOnClickListener(this);
		layerGv.setOnItemClickListener(this);
		layerLayout.setOnClickListener(this);
	}

	@Override
	public void initData() {
	}
	
	
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ContentValues values = new ContentValues();
		values.put("room_state_for_examine", MyApplication.getInstance()
				.getRoom_state_for_examine());
		values.put("roommanagerform", MyApplication.getInstance().getRoomManagerForm());
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
	}

	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onResume()
	 */
	@Override
	protected void onResume() {
		//0代表从楼盘列表进入房源管理界面   1代表从添加房源成功进入房源管理的待审核列表
		buildid = MyApplication.getInstance().getBuildid();
		addressText.setText(getaddressText(buildid));
		if(MyApplication.getInstance().getRoomManagerFlag() ==  1){
		if (MyApplication.getInstance().getRoomManagerForm() == 1) {
			roomLV.setVisibility(View.VISIBLE);
			layerGv.setVisibility(View.GONE);
		}
		roomListLayout.setClickable(true);
		daiShenHeLayout.setClickable(false);
		bianji_state = 0;// 非编辑状态
		bianJi.setText("编辑");
		MyApplication.getInstance(this).setRoom_state_for_examine(2);// 设置当前列表为待审核的房源列表
		MyApplication.getInstance(this).setRoom_list_state(1);// 设置当前房源列表为非编辑状态
		switchBtn.setVisibility(View.GONE);
		bianJi.setVisibility(View.VISIBLE);
		daiShenHeText.setTextColor(Color.WHITE);
		roomListText.setTextColor(Color.rgb(102, 102, 102));
		daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao);
		roomListImg.setImageResource(R.drawable.soso_house_sanjiao1);
		roomListLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg2);
		daiShenHeLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg1);
		 getDataForDaiShenHe();// 加载待审核的集合数据
		notifiViewForList(roomListForLinear);
		MyApplication.getInstance().setRoomManagerFlag(0);
		dialogDisplay = true;
		}else if(MyApplication.getInstance().getRoomManagerFlag() ==  2){
			
		}else if(MyApplication.getInstance().getRoomManagerFlag() ==  3){
			 shenHeEd();
		}
		
		if(MyApplication.getInstance().getRoom_state_for_examine() == 1){//审核通过的房源列表
			floorLayout.setOnClickListener(this);
			if(MyApplication.getInstance().getRoomManagerForm() == 1){
				getRoomListFromLayer("");// 查询出所有的房源数据
				notifiViewForList(roomListForLinear);
			}else if(MyApplication.getInstance().getRoomManagerForm() == 0){
				roomLV.setVisibility(View.GONE);
				layerGv.setVisibility(View.VISIBLE);
				notifyAdapter();
				MyApplication.getInstance().setRoomManagerForm(0);
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.GONE);
				floorLayout.setOnClickListener(null);
			}
		}else if(MyApplication.getInstance().getRoom_state_for_examine() == 2){//待审核的房源列表
			floorImg.setVisibility(View.GONE);
		}
		super.onResume();
	}
	/**
	 * 切换到审核通过的房源列表
	 * @author 刘星星
	 * @createDate 2013/4/2
	 */
	public void shenHeEd(){
		roomListLayout.setClickable(false);
		daiShenHeLayout.setClickable(true);
		switchBtn.setVisibility(View.VISIBLE);
		MyApplication.getInstance(this).setRoom_state_for_examine(1);// 设置当前列表为审核通过的房源列表
		MyApplication.getInstance(this).setRoom_list_state(1);// 设置当前房源列表为非编辑状态
		roomListText.setTextColor(Color.WHITE);
		daiShenHeText.setTextColor(Color.rgb(102, 102, 102));
		roomListImg.setImageResource(R.drawable.soso_house_sanjiao);
		daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao1);
		roomListLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg1);
		daiShenHeLayout
				.setBackgroundResource(R.drawable.soso_email_title_bg2);
		bianji_state = 0;// 非编辑状态
		bianJi.setText("编辑");
		if (MyApplication.getInstance().getRoomManagerForm() == 1) {// 切换到文字形式
			getRoomListFromLayer("");// 查询出所有的房源数据
			switchBtn.setBackgroundResource(R.drawable.soso_sekuai_btn);
			bianJi.setVisibility(View.VISIBLE);
			floorImg.setVisibility(View.VISIBLE);
			floorLayout.setOnClickListener(this);
			notifiViewForList(roomListForLinear);
		} else if (MyApplication.getInstance().getRoomManagerForm() == 0) {// 切换到图形列表形式
			if(floorFlag){//开关视图
				floorFlag = true;
				getRoomListFromLayer(currentLayer);
				layerNumber.setText(currentLayer);
				notifiView();
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.VISIBLE);
				floorLayout.setOnClickListener(this);
			}else{
				floorFlag = false;
				notifyAdapter();
				switchBtn.setBackgroundResource(R.drawable.soso_list_btn);
				bianJi.setVisibility(View.GONE);
				floorImg.setVisibility(View.GONE);
				floorLayout.setOnClickListener(null);
			}
			
		}
	}
	public String getaddressText(String buildid) {
		StringBuffer address = new StringBuffer("");
		if (buildid != null && !buildid.equals("")) {
			String sql = "select areamc,districtmc,buildmc from soso_buildinfo where buildid='"
					+ buildid + "'";
			List<Map<String, Object>> selectresult = DBHelper.getInstance(this)
					.selectRow(sql, null);
			if (selectresult != null && selectresult.size() > 0) {
				address.delete(0, address.length());
				address.append(selectresult.get(selectresult.size() - 1).get(
						"areamc"));
				address.append(selectresult.get(selectresult.size() - 1).get(
						"districtmc"));
				address.append(selectresult.get(selectresult.size() - 1).get(
						"buildmc"));
				buildmc = selectresult.get(selectresult.size() - 1).get(
						"buildmc").toString();
			}
		}
		return address.toString();
	}

	/**
	 * 对楼层数据进行处理，把相同的排除
	 * 
	 * @param oldList
	 *            所有房源所在的楼层集合
	 * @return 返回一个新的无重复的楼层数据集合
	 * @author 刘星星
	 * @createDate 2013/2/3
	 */
	private ArrayList<String> getLayerListData(ArrayList<String> oldList) {
		ArrayList<String> newList = new ArrayList<String>();
		for (int i = 0; i < oldList.size(); i++) {
			String oldStr = oldList.get(i);
			boolean boo = false;
			for (int j = 0; j < newList.size(); j++) {
				String newLayer = newList.get(j);
				if (newLayer.equals(oldStr)) {
					boo = true;
					break;
				}
			}
			if (!boo) {
				newList.add(oldStr);
			}
		}
		for (int a = 0; a < newList.size() - 1; a++) {// 这里是将获取到的楼层进行排序。。。。。。
														// 用的是简单的冒泡排序方法
			for (int b = 0; b < newList.size() - a - 1; b++) {
				String temp1 = newList.get(b);
				String temp2 = newList.get(b + 1);
				if (Integer.parseInt(temp2) < Integer.parseInt(temp1)) {
					newList.remove(b);
					newList.add(b, temp2);
					newList.remove(b + 1);
					newList.add(b + 1, temp1);
				}
			}
		}
		return newList;
	}

	/**
	 * 获取楼盘对应的所有楼层
	 * 
	 * @author 刘星星
	 * @createDate 2013/2/3
	 */
	public void getFloorsListData() {
		List<Map<String, Object>> selectresult = null;
		String sql = "select floors from soso_officeinfo where buildid = '"
					+ buildid
					+ "'and createuserid='"
					+ MyApplication.getInstance(this).getSosouserinfo(this)
							.getUserID() + "' and ischecked <> 0 order by cast(floors as Integer)";
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			for (int i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("floors") != null
						&& !selectresult.get(i).get("floors").toString()
								.equals("")) {
					try {
						Integer.parseInt(selectresult.get(i).get("floors")
								.toString());
						layerList.add(selectresult.get(i).get("floors")
								.toString());
					} catch (Exception e) {
					}
				}
				// else{
				// layerList.add("");
				// }
			}
		}
		layerList = getLayerListData(layerList);
	}

	@Override
	public void notifiView() {// 图形形式的房源列表
		roomLV.setVisibility(View.GONE);
		layerGv.setVisibility(View.VISIBLE);
		layerLayout.setVisibility(View.VISIBLE);

		roomadapter = new RoomManagerGridViewAdapter(this, roomList);
		layerGv.setNumColumns(3);
		layerGv.setAdapter(roomadapter);
		layerGv.setCacheColorHint(0);
		layerGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(roomList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
		
	}

	/**
	 * 刷新listView列表数据
	 * 
	 * 作者：Ring 创建于：2013-1-31
	 */
	public void notifyAdapter() {
		roomList.clear();
		getRoomListFromLayer("");// 查询出所有的房源数据
		getFloorsListData();
		roomLV.setVisibility(View.GONE);
		layerGv.setVisibility(View.VISIBLE);
		layerLayout.setVisibility(View.GONE);
		layerGv.setNumColumns(4);
		layerGv.setAdapter(new RoomLayerAdapter(this, layerList));
		layerGv.setCacheColorHint(0);
		layerGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(layerList.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
		floorImg.setVisibility(View.GONE);
		floorLayout.setOnClickListener(null);
		/*
		 * if(roomadapter!=null){ roomadapter.notifyDataSetChanged(); }
		 */
	}

	/**
	 * 房源管理的文字列表形式
	 * 
	 * @createDate 2013/1/23
	 * @author 刘星星
	 */
	private void notifiViewForList(ArrayList<HashMap<String, Object>> list) {
		if(MyApplication.getInstance().getRoom_state_for_examine() == 1){//审核通过的房源列表
			noDataText.setText("您暂无任何房源数据，请点击右上角的加号，赶紧发布您的房源吧");
		}else{
			noDataText.setText("您暂无待审核房源数据!");
		}
		roomLV.setVisibility(View.VISIBLE);
		layerGv.setVisibility(View.GONE);
		roomLV.setAdapter(new RoomManagerForListAdapter(this, list));// 加载适配器
		roomLV.setCacheColorHint(0);
		roomLV.setDivider(getResources().getDrawable(R.drawable.soso_zc_line));
		roomLV.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(MyApplication.getInstance().getRoom_list_state() == 1){//非编辑状态下
			roomLV.setOnItemClickListener(this);
		}else{//编辑状态下
			roomLV.setOnItemClickListener(null);
		}
		if(list.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}

//	/**
//	 * @author 刘星星
//	 * @createDate 2013/1/23 获取文字形式列表的数据,这里的数据就是从本地的集合roomList中获取
//	 */
//	private void getDataForLinearList() {
//		roomListForLinear.clear();
//		for (int i = 0; i < roomList.size(); i++) {
//			HashMap<String, Object> map = roomList.get(i);// 获取最大结合中的每一个子集
//			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) map
//					.get("list");// 获取每一个子集中的list对象
//			for (int j = 0; j < list.size(); j++) {
//				HashMap<String, Object> map1 = list.get(j);// 再获取子集中的list中的子集
//				map1.put("layer", map.get("layer"));
//				roomListForLinear.add(map1);// 将取得的最小单元加入文字形式的集合中
//			}
//		}
//	}

	/**
	 * 获取待审核的房源列表数据
	 * 
	 * @createDate 2013、1、24
	 * @author 刘星星
	 */
	private void getDataForDaiShenHe() {
		roomListForDaiShenHe.clear();
		getRoomListFromLayer("");
		roomListForDaiShenHe = roomListForLinear;
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
			if (NetworkCheck.IsHaveInternet(RoomManagerActivity.this)) {
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
				if (tag == 1) {
					if (MyApplication.getInstance().getRoom_state_for_examine() == 1) {//审核通过
						shenHeEd();
					} else if (MyApplication.getInstance().getRoom_state_for_examine() == 2) {//待审核
						bianji_state = 0;// 非编辑状态
						bianJi.setText("编辑");
						MyApplication.getInstance().setRoom_state_for_examine(2);// 设置当前列表为待审核的房源列表
						MyApplication.getInstance().setRoom_list_state(1);// 设置当前房源列表为非编辑状态
						switchBtn.setVisibility(View.GONE);
						bianJi.setVisibility(View.VISIBLE);
						daiShenHeText.setTextColor(Color.WHITE);
						roomListText.setTextColor(Color.rgb(102, 102, 102));
						daiShenHeImg.setImageResource(R.drawable.soso_house_sanjiao);
						roomListImg.setImageResource(R.drawable.soso_house_sanjiao1);
						roomListLayout
								.setBackgroundResource(R.drawable.soso_email_title_bg2);
						daiShenHeLayout
								.setBackgroundResource(R.drawable.soso_email_title_bg1);
						getDataForDaiShenHe();// 加载待审核的集合数据
						notifiViewForList(roomListForLinear);
					} else {
						notifyAdapter();
					}
				} else if (tag == 2) {
					getDataForDaiShenHe();// 加载待审核的集合数据
					notifiViewForList(roomListForLinear);
					MyApplication.getInstance().setRoomManagerFlag(0);
				}
				handler.sendEmptyMessage(7);
				roomLV.stopRefresh();
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
					if(MyApplication.getInstance().getRoom_state_for_examine() == 1){//审核通过的房源列表
						errormsg = "您暂无任何房源数据，请点击右上角的加号，赶紧发布您的房源吧";
					}else if(MyApplication.getInstance().getRoom_state_for_examine() == 2){//待审核的房源列表
						errormsg = "您暂无待审核房源数据!";
					}
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
//				noDataText.setText(errormsg);
				roomLV.stopRefresh();
				// Toast.makeText(RoomManagerActivity.this, errormsg,
				// Toast.LENGTH_SHORT).show();
				break;
			case 4:// 没有网络时给用户提示
				roomLV.stopRefresh();
				MessageBox.CreateAlertDialog(RoomManagerActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(RoomManagerActivity.this);
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
			case 7:// 是否设置过显示图片
				if (MyApplication.getInstance().getDisplayRoomPhoto() == 0) {
					if (tag1 == 0) {
						displayRoomImgDialog();
						tag1 = 1;
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
	public boolean getUseroffices() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		// params 请求的参数列表
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name=''",
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
		String whereclause = "";
		whereclause = "CreateUserID="
				+ MyApplication.getInstance(this).getSosouserinfo(this)
						.getUserID() + " and BuildID=" + buildid;
		params.add(new BasicNameValuePair("whereClause", whereclause));
		params.add(new BasicNameValuePair("orderBy", "Floors"));
		params.add(new BasicNameValuePair("pageIndex", "0"));
		params.add(new BasicNameValuePair("pageLength", "10000"));
		uploaddata = new SoSoUploadData(this, "OfficeGetPaged.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication.getInstance(this).getSosouserinfo(this)
				.getUserID());
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
	private void dealReponse(String userid) {
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name=''");
			Type listType = new TypeToken<LinkedList<SoSoOfficeInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoOfficeInfo> sosoofficeinfos = null;
			SoSoOfficeInfo sosoofficeinfo = null;
			ContentValues values = new ContentValues();
			try {
				JSONObject json = new JSONObject(reponse);
				String officedatas = json.getString("data");
				sosoofficeinfos = gson.fromJson(officedatas, listType);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (sosoofficeinfos != null && sosoofficeinfos.size() > 0) {
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
						values.put("officeid", sosoofficeinfo.getOfficeID());
						values.put("createuserid",
								sosoofficeinfo.getCreateUserID());
						values.put("areaup", sosoofficeinfo.getAreaUp());
						values.put("areadown", sosoofficeinfo.getAreaDown());
						values.put("address", sosoofficeinfo.getAddress());
						values.put("telephone", sosoofficeinfo.getTelePhone());
						values.put("storey", sosoofficeinfo.getStorey());
						values.put("floors", sosoofficeinfo.getFloors());
						values.put("priceup", sosoofficeinfo.getPriceUp());
						values.put("pricedown", sosoofficeinfo.getPriceDown());
						values.put("officemc", sosoofficeinfo.getOfficeMC());
						values.put("wymanagementfees",
								sosoofficeinfo.getWYManagmentFees());
						values.put("officetype", sosoofficeinfo.getOfficeType());
						values.put("keywords", "");
						values.put("fycx", sosoofficeinfo.getFYCX());
						values.put("zcyh", sosoofficeinfo.getZCYH());
						values.put("tsyh", sosoofficeinfo.getTSYH());
						values.put("fyjj", sosoofficeinfo.getFYJJ());
						values.put("updatedate", "");
						values.put("buildid", sosoofficeinfo.getBuildID());
						values.put("buildmc", sosoofficeinfo.getBuildMC());
						values.put("officestate",
								sosoofficeinfo.getOfficeState());
						values.put("officestatus", "");
						values.put("isprice", "");
						values.put("offadddate", "");
						values.put("roomrate", sosoofficeinfo.getRoomRate());
						values.put("nextalertdate", "");
						values.put("ispushed", sosoofficeinfo.getIsPushed());
						values.put("pushstate", sosoofficeinfo.getPushState());
						values.put("ischecked", sosoofficeinfo.getIsChecked());
						values.put("isrent", sosoofficeinfo.getIsRent());
						values.put("imageid", sosoofficeinfo.getShowImageID());

						if (DBHelper
								.getInstance(RoomManagerActivity.this)
								.selectRow(
										"select * from soso_officeinfo where officeid = '"
												+ sosoofficeinfo.getOfficeID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(RoomManagerActivity.this)
									.insert("soso_officeinfo", values);
						} else {
							DBHelper.getInstance(RoomManagerActivity.this)
									.update("soso_officeinfo",
											values,
											"officeid = ?",
											new String[] { sosoofficeinfo
													.getOfficeID() });
						}
						values.clear();
					}
				}
				if (sosoofficeinfos != null) {
					sosoofficeinfos.clear();
					sosoofficeinfos = null;
				}
				if(values!=null){
					values.clear();
					values = null;
				}
			}
		}
	}

	/**
	 * 根据楼层号获取楼层的房源数据
	 * 
	 * @author 刘星星
	 * @createDate 2013/2/3
	 * @param layer
	 *            楼层号
	 * 该方法Ring修改于 2013/2/19，添加参数ischeck是否待审核 
	 */
	public void getRoomListFromLayer(String layer) {
		String name= "";
		String sdpath="";
		File file1;//缩略图文件
		List<Map<String, Object>> selectresult = null;
		HashMap<String, Object> map = null;
		String sql = null;
		if (layer.equals("")) {
			roomListForLinear.clear();
			if(MyApplication.getInstance().getRoom_state_for_examine()!=1){
				sql = "select * from soso_officeinfo where buildid = '"
						+ buildid
						+ "' and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked=0 order by cast(officeid as Integer) desc";
			}else{
				sql = "select * from soso_officeinfo where buildid = '"
						+ buildid
						+ "' and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked <> 0 order by cast(floors as Integer)";
			}
		} else {
			roomList.clear();
			if(MyApplication.getInstance().getRoom_state_for_examine()!=1){
				sql = "select * from soso_officeinfo where floors = '"
						+ layer
						+ "' and buildid = '"
						+ buildid
						+ "'and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked = 0  order by cast(officeid as Integer) desc";
			}else{
				sql = "select * from soso_officeinfo where floors = '"
						+ layer
						+ "' and buildid = '"
						+ buildid
						+ "'and createuserid='"
						+ MyApplication.getInstance(this).getSosouserinfo(this)
								.getUserID() + "' and ischecked <> 0  order by cast(officeid as Integer) desc";
			}
			
		}
		selectresult = DBHelper.getInstance(this).selectRow(sql, null);
		if (selectresult != null) {
			for (int i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("officeid") != null
						&&!selectresult.get(i).get("floors").toString().equals("")) {// //
					try {
						// "officeid text,"+//房源id
						Integer.parseInt(selectresult.get(i).get("floors")
								.toString());
					} catch (Exception e) {
						continue;
					}
					map = new HashMap<String, Object>();
					if (selectresult.get(i).get("offadddate") != null) {// "offadddate text,"+//添加时间
						map.put("offadddate",
								selectresult.get(i).get("offadddate")
										.toString());
					} else {
						map.put("offadddate", "");
					}
					if (selectresult.get(i).get("fyjj") != null) {// "fyjj text,"+//房源简介
						map.put("fyjj", selectresult.get(i).get("fyjj")
								.toString());
					} else {
						map.put("fyjj", "");
					}
					if (selectresult.get(i).get("roomrate") != null) {// "fyjj text,"+//房源简介
						map.put("roomrate", selectresult.get(i).get("roomrate")
								.toString());
					} else {
						map.put("roomrate", "");
					}
					if (selectresult.get(i).get("pushstate") != null) {// "areadown text,"+//面积下线
						map.put("pushstate", selectresult.get(i).get("pushstate")
								.toString());
					} else {
						map.put("pushstate", 1);
					}
					if (selectresult.get(i).get("areadown") != null) {// "areadown text,"+//面积下线
						map.put("areadown", selectresult.get(i).get("areadown")
								.toString());
					} else {
						map.put("areadown", "");
					}
					if (selectresult.get(i).get("floors") != null) {// "floors text,"+//楼层
						map.put("floors", selectresult.get(i).get("floors")
								.toString());
					} else {
						map.put("floors", "");
					}
					if (selectresult.get(i).get("ispushed") != null) {//0未推送，1已推送
						map.put("ispushed", selectresult.get(i).get("ispushed")
								.toString());
					} else {
						map.put("ispushed", "");
					}
					if (selectresult.get(i).get("fycx") != null) {// "fycx integer,"+//房源朝向
						map.put("fycx", selectresult.get(i).get("fycx")
								.toString());
					} else {
						map.put("fycx", "");
					}
					if (selectresult.get(i).get("isprice") != null) {// "isprice integer,"+//0：不可以议价，1可以议价
						map.put("isprice", selectresult.get(i).get("isprice")
								.toString());
					} else {
						map.put("isprice", "");
					}
					if (selectresult.get(i).get("zcyh") != null) {// "zcyh text,"+//政策优惠
						map.put("zcyh", selectresult.get(i).get("zcyh")
								.toString());
					} else {
						map.put("zcyh", "");
					}
					if (selectresult.get(i).get("pricedown") != null) {// "pricedown text,"+//单价下限
						map.put("pricedown",
								selectresult.get(i).get("pricedown").toString());
					} else {
						map.put("pricedown", "");
					}
					if (selectresult.get(i).get("keywords") != null) {// "keywords text,"+//房源关键字
						map.put("keywords", selectresult.get(i).get("keywords")
								.toString());
					} else {
						map.put("keywords", "");
					}
					if (selectresult.get(i).get("isrent") != null) {// "officestate integer,"+//房源状态
																			// 1：审核通过/未租，2：已租3：私信提醒中4：房源导入5：待删除
						map.put("officestate",
								selectresult.get(i).get("isrent")
										.toString());
					} else {
						map.put("officestate", 0);
					}
					if (selectresult.get(i).get("priceup") != null) {// "priceup text,"+//单价上限
						map.put("priceup", selectresult.get(i).get("priceup")
								.toString());
					} else {
						map.put("priceup", "");
					}
					if (selectresult.get(i).get("wymanagementfees") != null) {// "wymanagementfees text,"+//物业管理费
						map.put("wymanagementfees",
								selectresult.get(i).get("wymanagementfees")
										.toString());
					} else {
						map.put("wymanagementfees", "");
					}

					if (selectresult.get(i).get("officestatus") != null) {// "officestatus integer,"+//0：可整租1：可分割2：可合并
						map.put("officestatus",
								selectresult.get(i).get("officestatus")
										.toString());
					} else {
						map.put("officestatus", "");
					}
					map.put("officeid", selectresult.get(i).get("officeid")
							.toString());
					if (selectresult.get(i).get("areaup") != null) {// "areaup text,"+//面积上限
						map.put("areaup", selectresult.get(i).get("areaup")
								.toString());
					} else {
						map.put("areaup", "");
					}

					if (selectresult.get(i).get("nextalertdate") != null) {// "nextalertdate text"+//下次提醒时间
						map.put("nextalertdate",
								selectresult.get(i).get("nextalertdate")
										.toString());
					} else {
						map.put("nextalertdate", "");
					}

					if (selectresult.get(i).get("tsyh") != null) {// "tsyh text,"+//特殊优惠
						map.put("tsyh", selectresult.get(i).get("tsyh")
								.toString());
					} else {
						map.put("tsyh", "");
					}

					if (selectresult.get(i).get("officetype") != null) {// "officetype integer,"+//房源类型
																		// 0纯写字楼，1商住楼2，酒店式公寓，3园区，4商务中心
						map.put("officetype",
								selectresult.get(i).get("officetype")
										.toString());
					} else {
						map.put("officetype", i + 1);
					}

					if (selectresult.get(i).get("officemc") != null) {// "officemc text,"+//房源名称
						map.put("officemc", selectresult.get(i).get("officemc")
								.toString());
					} else {
						map.put("officemc", "");
					}

					if (selectresult.get(i).get("address") != null) {// "address text,"+//地址
						map.put("address", selectresult.get(i).get("address")
								.toString());
					} else {
						map.put("address", "");
					}

					if (selectresult.get(i).get("storey") != null) {// "storey text,"+//层高
						map.put("storey", selectresult.get(i).get("storey")
								.toString());
					} else {
						map.put("storey", "");
					}

					if (selectresult.get(i).get("createuserid") != null) {// "createuserid text,"+//创建人id
						map.put("createuserid",
								selectresult.get(i).get("createuserid")
										.toString());
					} else {
						map.put("createuserid", "");
					}

					if (selectresult.get(i).get("updatedate") != null) {// "updatedate text,"+//更新时间
						map.put("updatedate",
								selectresult.get(i).get("updatedate")
										.toString());
					} else {
						map.put("updatedate", "");
					}

					if (selectresult.get(i).get("buildid") != null) {// "buildid text,"+//所属楼盘id
						map.put("buildid", selectresult.get(i).get("buildid")
								.toString());
					} else {
						map.put("buildid", "");
					}

					if (selectresult.get(i).get("telephone") != null) {// "telephone text,"+//联系电话
						map.put("telephone",
								selectresult.get(i).get("telephone").toString());
					} else {
						map.put("telephone", "");
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
					
					
					if (layer.equals("")) {
						roomListForLinear.add(map);
					} else {
						roomList.add(map);
					}
				}
			}
		}
	}
	private String currentLayer = "";//当前楼层
	private boolean floorFlag = false;//用来判断是否为楼层列表中的开关示图层
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == layerGv) {
			String layer = layerList.get(arg2);
			floorFlag = true;
			currentLayer = layer;
			getRoomListFromLayer(layer);
			layerNumber.setText(layer);
			notifiView();
			floorImg.setVisibility(View.VISIBLE);
			floorLayout.setOnClickListener(this);
		} else if (arg0 == roomLV) {
			MyApplication.getInstance().setOfficeid(roomListForLinear.get(arg2-1).get("officeid").toString());
			Intent intent = new Intent(this, DetailRoomInfoActivity.class);
			startActivity(intent);
		}
	}

	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		String updatedate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		updatedate = StringUtils.formatCalendar1(sdf.format(new Date()),
				MyApplication.error_value);
		roomLV.setRefreshTime(updatedate);
		// if (tag == 1||tag == 2) {
		new Thread(runnable).start();
		// }

	}

	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onLoadMore()
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

//	/**
//	 * 获取待审核的房源信息
//	 * 
//	 * 作者：Ring 创建于：2013-2-19
//	 */
//	public void getOfficeGetByState() {
//		// params 请求的参数列表
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
//				this).getAPPID()));
//		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
//				this).getAPPKEY()));
//		String whereclause = "";
//		whereclause = "CreateUserID=+"
//				+ MyApplication.getInstance(this).getSosouserinfo(this)
//						.getUserID() + " and BuildID=" + buildid;
//		params.add(new BasicNameValuePair("whereClause", whereclause));
//		params.add(new BasicNameValuePair("orderBy", "Floors"));
//		params.add(new BasicNameValuePair("pageIndex", "0"));
//		params.add(new BasicNameValuePair("pageLength", "100"));
//		uploaddata = new SoSoUploadData(this, "OfficeGetPaged.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		dealReponse(MyApplication.getInstance(this).getSosouserinfo(this)
//				.getUserID());
//		params.clear();
//		params = null;
//	}
}
