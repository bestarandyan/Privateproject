package com.zhihuigu.sosoOffice;

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
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.LinkmanListAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoContactInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.ChineseToEnglish;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.ListDispose;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;
/**
 * 联系人类
 * @author 刘星星
 * 
 *
 */
public class LinkmanActivity extends BaseActivity implements Activity_interface,OnItemClickListener{
	private Button backBtn,completeBtn;
	private EditText searchLinkmanEt;
	private ListView linkmanListView;
	private ListView zimuListView;
	private TextView agencyText,kehuText,yezhuText;
	private ImageView agencyIv,kehuIv,yezhuIv;
	private ArrayList<Map<String, Object>> linkmanList = new ArrayList<Map<String, Object>>();// 联系人集合
	
	
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private ProgressDialog progressdialog = null;

	private ArrayList<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
	private LinkmanListAdapter adapter;
	
	private int activityFlag = 0;//标记是从哪个类跳转过来的  0代表主页  1代表写信  2代表需求
	private boolean refreshFlag = false;//用来判断是否为搜索而刷新出来的列表
	
	private int type = 0;
	
	private LinearLayout daibiaoLinear;
	private LinearLayout noinfoText;
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	private TextView titleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_linkman);
		activityFlag = getIntent().getIntExtra("activityFlag", 0);
		findView();
		initView();

		initData();
		notifiViewZimu();
	}
	@Override
	public void onClick(View v) {
		if(backBtn == v){
			CommonUtils.hideSoftKeyboard(this);
			if(activityFlag == 0){//从主页跳过来的
				//后退跳转到第一个Activity界面
				Intent intent = new Intent(this, MainActivity.class).
			              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainTabActivity.mTabHost.setCurrentTab(0);
				Window w = MainFirstTab.group.getLocalActivityManager()
						.startActivity("MainActivity",intent);
			    View view = w.getDecorView();
			    MainFirstTab.group.setContentView(view);
			}else if(activityFlag == 1){//从写信跳过来的
				setResult(RESULT_OK);
				finish();
			}else if(activityFlag ==2){
				MyApplication.getInstance().setLinkManBack(false);
				finish();
			}
		}else if(v == completeBtn){//完成按钮
//			StringBuffer shou = new StringBuffer("");// 收件人选项字符
//			StringBuffer shou1 = new StringBuffer("");// 收件人选项字符id
//			if(shou.length()>0){
//				intent.putExtra("linkman", shou.toString().substring(0, shou.toString().length()-1));
//				intent.putExtra("linkmanid", shou1.toString().substring(0, shou1.toString().length()-1));
//			}
			CommonUtils.hideSoftKeyboard(this);
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();// 联系人集合
			HashMap<String, String> map =null;
			for(int i=0;i<adapter.messageList.size();i++){
//				CheckBox checketBox = (CheckBox) linkmanListView.getChildAt(i).findViewById(R.id.checkBox);
				if(adapter.messageList.get(i).isChecked){
					map =  new HashMap<String, String>();
					if(refreshFlag){//搜索过的
						map.put("name", searchList.get(i).get("name").toString());
						map.put("contactuserid", searchList.get(i).get("contactuserid").toString());
					}else{//所有数据
						map.put("name", linkmanList.get(i).get("name").toString());
						map.put("contactuserid", linkmanList.get(i).get("contactuserid").toString());
					}
					list.add(map);
				}
			}
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
			setResult(RESULT_OK,intent);
			finish();
		}else if(v == noDataImg){//没有数据时点击图片刷新
			new Thread(runnable).start();
		}
		super.onClick(v);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		//后退跳转到第一个Activity界面
		Intent intent = new Intent(this, MainActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		MainTabActivity.mTabHost.setCurrentTab(0);
		Window w = MainFirstTab.group.getLocalActivityManager()
				.startActivity("MainActivity",intent);
	    View view = w.getDecorView();
	    MainFirstTab.group.setContentView(view);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			CommonUtils.hideSoftKeyboard(this);
			if(activityFlag == 0){//从主页跳过来的
				//后退跳转到第一个Activity界面
				Intent intent = new Intent(this, MainActivity.class).
			              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainTabActivity.mTabHost.setCurrentTab(0);
				Window w = MainFirstTab.group.getLocalActivityManager()
						.startActivity("MainActivity",intent);
			    View view = w.getDecorView();
			    MainFirstTab.group.setContentView(view);
			}else if(activityFlag == 1){//从写信跳过来的
				setResult(RESULT_OK);
				finish();
			}else if(activityFlag ==2){
				MyApplication.getInstance().setLinkManBack(false);
				finish();
			}
		}
		return true;
	}
	@Override
	public void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		searchLinkmanEt = (EditText) findViewById(R.id.linkmanEt);
		linkmanListView = (ListView) findViewById(R.id.linkmanList);
		zimuListView = (ListView) findViewById(R.id.zimuList);
		completeBtn = (Button) findViewById(R.id.completeBtn);
		agencyText = (TextView) findViewById(R.id.agencyText);
		kehuText = (TextView) findViewById(R.id.kehuText);
		yezhuText = (TextView) findViewById(R.id.yezhuText);
		agencyIv =  (ImageView) findViewById(R.id.agencyIv);
		kehuIv =  (ImageView) findViewById(R.id.kehuIv);
		yezhuIv =  (ImageView) findViewById(R.id.yezhuIv);
		daibiaoLinear = (LinearLayout) findViewById(R.id.daibiaoLinear);
		noinfoText = (LinearLayout) findViewById(R.id.noDataLayout);
		titleText = (TextView) findViewById(R.id.titleText);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		String message="";
		if(MyApplication.getInstance().getRoleid()==UserType.UserTypeOwner.getValue()){
			message = "您暂时还没有任何联系人，查看潜在客户，可添加为您的联系人";
		}else{
			message = "您暂时还没有任何联系人，查看房源，可以添加业主为您的联系人，快去搜索适合您的房源吧";
		}
		noDataText.setText(message);
		noDataImg.setOnClickListener(this);
	}

	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
		searchLinkmanEt.addTextChangedListener(new EditTextListener());
		completeBtn.setOnClickListener(this);
		if(activityFlag == 0 || activityFlag == 2){
			completeBtn.setVisibility(View.GONE);
		}
		if(activityFlag == 2){//从需求中来的
			titleText.setText(MyApplication.getInstance().getLinkManTitle());
			searchLinkmanEt.setHint("搜索");
		}
//		linkmanListView.setOnItemClickListener(this);
		 type = MyApplication.getInstance().getRoleid();
		switch(type){
		case Constant.TYPE_AGENCY:
		yezhuIv.setVisibility(View.VISIBLE);
		yezhuText.setVisibility(View.VISIBLE);
		break;
		case Constant.TYPE_CLIENT:
			yezhuIv.setVisibility(View.VISIBLE);
			yezhuText.setVisibility(View.VISIBLE);
			break;
		case Constant.TYPE_PROPRIETOR:
			agencyIv.setVisibility(View.VISIBLE);
			agencyText.setVisibility(View.VISIBLE);
			kehuIv.setVisibility(View.VISIBLE);
			kehuText.setVisibility(View.VISIBLE);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		if(activityFlag == 2){
			if (linkmanList == null) {
				linkmanList = new ArrayList<Map<String, Object>>();
			}
			linkmanList.clear();
			if(getIntent().getSerializableExtra("list")!=null){
				linkmanList = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("list");
			}
			notifiView();
			return ;
		}
		getContractList();
		notifiView();
		new Thread(runnable).start();
//		linkmanList.clear();
//		for(int i=0;i<5;i++){
//			HashMap< String, Object> map = new HashMap<String, Object>();
//			map.put("name", "刘星星");
//			map.put("headImageId", R.drawable.soso_zhongjei);
//			map.put("remark", "他是我的朋友");
//			linkmanList.add(map);
//		}
//		for(int i=0;i<5;i++){
//			HashMap< String, Object> map = new HashMap<String, Object>();
//			map.put("name", "安迪");
//			map.put("headImageId", R.drawable.soso_kehu);
//			map.put("remark", "他是我的同事");
//			linkmanList.add(map);
//		}
//		for(int i=0;i<5;i++){
//			HashMap< String, Object> map = new HashMap<String, Object>();
//			map.put("name", "谢国良");
//			map.put("headImageId", R.drawable.soso_zhongjei);
//			map.put("remark", "他是我的领导");
//			linkmanList.add(map);
//		}
	}
	
	
	/**
	 * 从数据库中获取联系人列表
	 */
	public void getContractList() {
		if (linkmanList == null) {
			linkmanList = new ArrayList<Map<String, Object>>();
		}
		linkmanList.clear();
		Map<String, Object> map = null;
		List<Map<String, Object>> selectresult = DBHelper.getInstance(this)
				.selectRow(
						"select * from sosocontactinfo where userid='"
								+ MyApplication.getInstance(this)
										.getSosouserinfo(this).getUserID() + "'",
						null);
		int i;
		if (selectresult != null && selectresult.size() > 0) {
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("contactuserid") != null
					&&selectresult.get(i).get("contactid") != null
						&& selectresult.get(i).get("username") != null
						&&!selectresult.get(i).get("contactuserid").toString().equals(MyApplication.getInstance(this)
										.getSosouserinfo(this).getUserID())) {
					int roleid=0;
					try {
						roleid = Integer.parseInt(selectresult.get(i)
								.get("contactroleid").toString());
					} catch (Exception e) {
					}
					if(MyApplication.getInstance().getRoleid()==UserType.UserTypeOwner.getValue()){
						if(roleid!=UserType.UserTypeCustomer.getValue()
								&&roleid!=UserType.UserTypeIntermediary.getValue()){
							continue;
						}
					}else if(MyApplication.getInstance().getRoleid()==UserType.UserTypeCustomer.getValue()){
						if(roleid!=UserType.UserTypeOwner.getValue()){
							continue;
						}
					}else if(MyApplication.getInstance().getRoleid()==UserType.UserTypeIntermediary.getValue()){
						if(roleid!=UserType.UserTypeOwner.getValue()){
							continue;
						}
					}
					map = new HashMap<String, Object>();
					if(roleid==UserType.UserTypeIntermediary.getValue()){//中介类型
						map.put("headImageId", R.drawable.soso_zhongjei);
					} else if(roleid==UserType.UserTypeCustomer.getValue()){
						map.put("headImageId", R.drawable.soso_kehu);
					} else if(roleid==UserType.UserTypeOwner.getValue()){
						map.put("headImageId", R.drawable.soso_yezhu);
					} else{
						map.put("headImageId", R.drawable.soso_yezhu);
					}
					map.put("contactuserid", selectresult.get(i).get("contactuserid").toString());
					if(selectresult.get(i).get("birthday")!=null){
						map.put("birthday", selectresult.get(i).get("birthday").toString());
					}else{
						map.put("birthday", "");
					}
					if(selectresult.get(i).get("company")!=null){
						map.put("company", selectresult.get(i).get("company").toString());
					}else{
						map.put("company", "");
					}
					if(selectresult.get(i).get("sex")!=null){
						map.put("sex", selectresult.get(i).get("sex").toString());
					}else{
						map.put("sex", "1");
					}
					if(selectresult.get(i).get("realname")!=null
							&&!selectresult.get(i).get("realname").toString().trim().equals("")){
						map.put("realname", selectresult.get(i).get("realname").toString());
//						map.put("name", selectresult.get(i).get("realname").toString());
					}else{
						map.put("realname", "");
//						map.put("name", selectresult.get(i).get("username").toString());
						
					}
					map.put("contactid", selectresult.get(i).get("contactid").toString());
					map.put("username", selectresult.get(i).get("username").toString());
					map.put("name", selectresult.get(i).get("username").toString());
					map.put("remark", "");
					linkmanList.add(map);
				}
			}
			selectresult.clear();
			selectresult = null;
		}
	}
	/**
	 * 刷新列表
	 *
	 * 作者：Ring
	 * 创建于：2013-3-12
	 */
	public void notifyAdapter(){
		getContractList();
		notifiView();
	}
	
	@Override
	public void notifiView() {
		linkmanList = ListDispose.sortList(linkmanList,"name");//将集合按拼音排序
		adapter = new LinkmanListAdapter(this, 	linkmanList, activityFlag);
		linkmanListView.setAdapter(adapter);
		linkmanListView.setCacheColorHint(0);
		linkmanListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		if(linkmanList.size()==0){
			daibiaoLinear.setVisibility(View.GONE);
			noinfoText.setVisibility(View.VISIBLE);
			zimuListView.setVisibility(View.GONE);
		}else{
			daibiaoLinear.setVisibility(View.VISIBLE);
			noinfoText.setVisibility(View.GONE);
			zimuListView.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 字母控件的点击事件监听
	 * @author 刘星星
	 * @createDate 2013/1/15
	 *
	 */
	class ZimuListItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			TextView zimuTv = (TextView) arg1;
			String s = zimuTv.getText().toString().toLowerCase();
			int localPosition = binSearch(linkmanList, s); // 接收返回值
			if (localPosition != -1) {
				linkmanListView.setSelection(localPosition); // 让List指向对应位置的Item
				linkmanListView.setSelectionFromTop(localPosition, localPosition);
				// list.setSelectionAfterHeaderView();
			}
		}
		
	}
	/**
	 * 字母控件的触屏事件监听
	 * @author 刘星星
	 * @createDate 2013/1/15
	 *
	 */
	class ZimuListTouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				zimuListView.setBackgroundResource(R.drawable.zimulist_background);
				zimuListView.getBackground().setAlpha(180);
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				zimuListView.setBackgroundColor(Color.TRANSPARENT);
			}
			int  itemY = v.getHeight()/26;
			int y = (int) (event.getY()/itemY);
			String str = Constant.py[(y>0)?(y<26?(y-1):25):0];
			int localPosition = binSearch(linkmanList, str); // 接收返回值
			if (localPosition != -1) {
				linkmanListView.setSelection(localPosition); // 让List指向对应位置的Item
				linkmanListView.setSelectionFromTop(localPosition, localPosition);
				// list.setSelectionAfterHeaderView();
			}
			return false;
		}
		
	}
	/**
	 * 将选中的py与stringArr的首字符进行匹配并返回对应字符串在数组中的位置
	 * @param list
	 * @param s
	 * @return
	 */
		public static int binSearch(ArrayList<Map<String, Object>> list, String s) {
			for (int i = 0; i < list.size(); i++) {
				String name = ChineseToEnglish.toEnglish(list.get(i).get("name").toString()	.charAt(0));
				if (s.equalsIgnoreCase("" + name.charAt(0))) { // 不区分大小写
					return i;
				}
			}
			return -1;
		}
		/**
		 * 搜索输入框的输入事件监听，在输入框的文字发生改变后，根据文本框中的值进行相应的搜索
		 * @author 刘星星
		 * @createDate  2013/1/15
		 *
		 */
		class EditTextListener implements TextWatcher{
			@Override
			public void afterTextChanged(Editable arg0) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {}
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				searchList.clear();
				for (int i = 0; i < linkmanList.size(); i++) {
					String str = linkmanList.get(i).get("name").toString();// 表示列表里面的一个人名
					String inputStr = s.toString();// 表示输入的人名
					StringBuffer returnstr = new StringBuffer();//表示列表中的人名转化成拼音再转化成大写后的字符串
					StringBuffer returnistr = new StringBuffer();//表示输入的人名转化成拼音再转化成大写后的字符串
					if (inputStr != null && inputStr != "" && inputStr.length() > 0) {//判断输入是否为空
						if (inputStr.charAt(0) >= 0x4e00 && inputStr.charAt(0) <= 0x9fa5) {// 判断输入的是不是汉字
							if (str.substring(0,inputStr.length() > str.length() ? str.length(): inputStr.length()).equals(inputStr)) {// 如果是汉字则直接跟列表里面的人名的字一个一个对比
								searchList.add(linkmanList.get(i));
							}
						} else {// 如果不是中文，则先把输入的内容和列表里面的内容转化成为拼音再进行比对
							for (int j = 0; j < str.length(); j++) {// 把列表里面的人名转化成为拼音
								returnstr.append(ChineseToEnglish.toEnglish(str	.charAt(j)).toUpperCase());
							}
							for (int k = 0; k < inputStr.length(); k++) {// 把输入的内容转化成拼音
								returnistr.append(ChineseToEnglish.toEnglish(inputStr.charAt(k)).toUpperCase());
							}
							//用最短的字符串去进行匹配
							int StrLen = (returnstr.toString().length() > returnistr	.toString().length()) ? (returnistr.toString().length()) : returnstr.toString().length();
							if (returnstr.toString().substring(0, StrLen).equals(returnistr.toString().substring(0,StrLen))) {
								searchList.add(linkmanList.get(i));
							}
						}
						refreshFlag = true;//证明是通过搜索表现出来的列表
					} else {//当文本框的内容为空时显示所有数据
						searchList.add(linkmanList.get(i));
						refreshFlag = false;//不是搜索的
					}
				}
				Message msg = new Message();
				msg.what = 0;
				RefreshListHandler.sendMessage(msg);//通过托管机制刷新布局，更快捷
			}
			
		}
		/**
		 * 托管机制，用于处理控件刷新
		 * @author 刘星星
		 * @createDate 2013/1/15
		 */
		Handler RefreshListHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0) {
//					searchLinkmanEt.clearFocus();
					refreshList();
				}
			}

		};
		/**
		 * 根据输入的条件查询出联系人后，刷新列表。只显示符合条件的联系人
		 * @author 刘星星
		 * @createDate 2013/1/15
		 */
		private void refreshList() {// 搜索客户
			adapter = new LinkmanListAdapter(this, 	searchList, activityFlag);
			linkmanListView.setAdapter(adapter);// 将数据适配器与Activity进行绑定
			linkmanListView.setCacheColorHint(0);
			linkmanListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			if(searchList.size()==0){
				daibiaoLinear.setVisibility(View.GONE);
				noinfoText.setVisibility(View.VISIBLE);
				zimuListView.setVisibility(View.GONE);
			}else{
				daibiaoLinear.setVisibility(View.VISIBLE);
				noinfoText.setVisibility(View.GONE);
				zimuListView.setVisibility(View.VISIBLE);
			}
		}
	/**
	 * @author 刘星星
	 * @createDate 2013/1/15
	 * 加载字母数据
	 */
	public void notifiViewZimu(){
//		intent.putExtra("haveMenu", true);
		boolean haveMenu = getIntent().getBooleanExtra("haveMenu", false);
		ArrayAdapter<String> adapter = null;
		if(haveMenu){//从主页点击联系人进来的，所以下面会有菜单
			adapter = new ArrayAdapter<String>(this,
					R.layout.linkman_item_zimu, Constant.py);
		}else{//从其他地方进来的  没有菜单
			adapter = new ArrayAdapter<String>(this,
					R.layout.item_zimu, Constant.py);
		}
		zimuListView.setAdapter(adapter);
		zimuListView.setDivider(null);
		zimuListView.setOnItemClickListener(new ZimuListItemClickListener());
		zimuListView.setOnTouchListener(new ZimuListTouchListener());
		zimuListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
	
	
	/**
	 * 查询联系人 true,查询成功，false 查询失败
	 */
	public boolean selectContact() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)==null
				||MyApplication.getInstance(this).getSosouserinfo(this).getUserID()==null){
			return false;
		}
		
		// params 请求的参数列表
		if(DBHelper
				.getInstance(this)
				.selectRow(
						"select * from soso_configurationinfo where name='TUSERCONACT_TIME' and updatedate = value and value<>'' and userid = '"
				+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
						null).size()>0){
			return true;
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_configurationinfo where name='TUSERCONACT_TIME' and userid = '"
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
		params.add(new BasicNameValuePair("RoleID", MyApplication
				.getInstance(this).getRoleid()+""));
		uploaddata = new SoSoUploadData(this, "UserContactSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse();
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * author by Ring 处理删除信件请求后的响应值
	 */

	public void dealReponse() {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TUSERCONACT_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate = value where name='TUSERCONACT_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoContactInfo>>() {
			}.getType();
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			LinkedList<SoSoContactInfo> sosocontactinfos = null;
			SoSoContactInfo sosocontactinfo = null;
			try {
				sosocontactinfos = gson.fromJson(reponse, listType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Map<String, Object>> selectresult = null;
			if (sosocontactinfos != null && sosocontactinfos.size() > 0) {
				for (Iterator<SoSoContactInfo> iterator = sosocontactinfos
						.iterator(); iterator.hasNext();) {
					sosocontactinfo = (SoSoContactInfo) iterator.next();
					if (sosocontactinfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("sosocontactinfo",
								"contactid = ?", new String[] { sosocontactinfo
								.getContactID() });
						continue;
					}
					values.put("contactid", sosocontactinfo.getContactID());
					values.put("contactuserid",
							sosocontactinfo.getContactUserID());
					values.put("contactroleid",
							sosocontactinfo.getRoleID());
					values.put("userid", sosocontactinfo.getUserID());
					values.put("username", sosocontactinfo.getUserName());
					values.put("realname", sosocontactinfo.getRealName());
					values.put("headimage", sosocontactinfo.getHeadImage());
					values.put("dictid", sosocontactinfo.getDictID());
					values.put("adddate", sosocontactinfo.getAdddate());
					values.put("sex", sosocontactinfo.getSex());
					values.put("birthday", sosocontactinfo.getBirthday());
					values.put("company", sosocontactinfo.getCompany());
					if (sosocontactinfo.getIsUsed() == 1) {
						DBHelper.getInstance(this)
								.delete("sosocontactinfo",
										"contactid = ?",
										new String[] { sosocontactinfo
												.getContactID() });
						continue;
					}
					selectresult = DBHelper.getInstance(this).selectRow(
							"select * from sosocontactinfo where contactid = '"
									+ sosocontactinfo.getContactID() + "'",
							null);
					if (selectresult != null) {
						if (selectresult.size() <= 0) {
							DBHelper.getInstance(this).insert(
									"sosocontactinfo", values);
						} else {
							DBHelper.getInstance(this).update(
									"sosocontactinfo",
									values,
									"contactid = ?",
									new String[] { sosocontactinfo
											.getContactID() });
						}
						selectresult.clear();
						selectresult = null;
					}
					values.clear();
				}
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			if (sosocontactinfos != null) {
				sosocontactinfos.clear();
				sosocontactinfos = null;
			}
			DBHelper.getInstance(this).close();
		}
	}
	

	/**
	 * author by Ring 处理删除信件耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			boolean bool = false;
			if(activityFlag == 0){
				bool = NetworkCheck.IsHaveInternet(LinkmanActivity.this.getParent());
			}else if(activityFlag == 1 || activityFlag == 2){
				bool = NetworkCheck.IsHaveInternet(LinkmanActivity.this);
			}
			if (bool) {
				handler.sendEmptyMessage(5);
				boolean b = selectContact();
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// 从注册界面跳转到注册成功界面
				} else {
//					handler.sendEmptyMessage(3);// 注册失败给用户提示
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

			click_limit = true;
		}
	};

	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 发送成功，跳出对话框提示用户
				notifyAdapter();
//				MessageBox.CreateAlertDialog(
//						getResources().getString(R.string.sendlettlesuccess),
//						LinkmanActivity.this.getParent());
				break;
			case 2:// 从注册界面跳转到登录界面
				if(activityFlag == 0){
					i.setClass(LinkmanActivity.this.getParent(), LoginActivity.class);
				}else if(activityFlag == 1 || activityFlag == 2){
					i.setClass(LinkmanActivity.this, LoginActivity.class);
				}
				LinkmanActivity.this.startActivity(i);
				LinkmanActivity.this.finish();
				break;
			case 3:// 联系人失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				}else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoNoData.getValue())) {
					if(MyApplication.getInstance().getRoleid()==UserType.UserTypeOwner.getValue()){
						message = "您暂时还没有任何联系人，查看潜在客户，可添加为您的联系人";
					}else{
						message = "您暂时还没有任何联系人，查看房源，可以添加业主为您的联系人，快去搜索适合您的房源吧";
					}
					
				} else {
					message = getResources().getString(
							R.string.deletelettleerror);
				}
				if(activityFlag == 0){
				MessageBox.CreateAlertDialog(message,
						LinkmanActivity.this.getParent());
				}else if(activityFlag == 1  || activityFlag == 2){
					MessageBox.CreateAlertDialog(message,
							LinkmanActivity.this);
				}
				noDataText.setText(message);
				break;
			case 4:// 没有网络时给用户提示
				if(activityFlag == 0){
					MessageBox.CreateAlertDialog(LinkmanActivity.this.getParent());
				}else if(activityFlag ==1   || activityFlag == 2){
					MessageBox.CreateAlertDialog(LinkmanActivity.this);
				}
				break;
			case 5:// 打开进度条
				if(activityFlag == 0){
					progressdialog = new ProgressDialog(LinkmanActivity.this.getParent());
				}else if(activityFlag == 1  || activityFlag == 2){
					progressdialog = new ProgressDialog(LinkmanActivity.this);
				}
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_loading));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
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
				if(linkmanList.size()==0){
					daibiaoLinear.setVisibility(View.GONE);
					noinfoText.setVisibility(View.VISIBLE);
					zimuListView.setVisibility(View.GONE);
				}else{
					daibiaoLinear.setVisibility(View.VISIBLE);
					noinfoText.setVisibility(View.GONE);
					zimuListView.setVisibility(View.VISIBLE);
				}
				break;
			}
		};
	};
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
		if(checkBox.isChecked()){
			checkBox.setChecked(false);
		}else{
			checkBox.setChecked(true);
		}
		
	}
}
