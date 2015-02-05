package com.zhihuigu.sosoOffice;


import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.StationInLetterAdapter;
import com.zhihuigu.sosoOffice.View.XListView;
import com.zhihuigu.sosoOffice.View.XListView.IXListViewListener;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoLetterInInfo;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.GetInbox;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;
/**
 * 
 * @author 刘星星
 * @createDate 2013/1/11
 *站内信
 */
public class StationInLetterActivity  extends BaseActivity implements OnClickListener,OnItemClickListener,IXListViewListener{

	private Button backBtn, writeNewBtn;
	private RelativeLayout inboxLayout,sendboxLayout,managerLayout;
	private XListView listView;
	private ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private TextView inboxText,sendboxText,managerText;
	/*
	 * guoqing params
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private DBHelper database = null;
	private ProgressDialog progressdialog = null;
	private SoSoUploadData uploaddata;
	private String reponse = "";// 从服务器获取响应值
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private StationInLetterAdapter stationinletteradapter = null;//数据列表的适配器
	private ProgressBar progressbar = null;
	private int tag = 1;//1获取收件箱数据，2获取发件箱收据
	private LinearLayout noDataLayout;//没有数据时显示的布局
	private ImageView noDataImg;//没有数据时的图片点击刷新
	private TextView noDataText;//没有数据时的提示文字
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyApplication.getInstance().setLettertype(1);
		setContentView(R.layout.a_stationinletter);
		findView();
		ViewFun();
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}

	}
	
	@Override
	protected void onResume() {
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			super.onResume();
			return;
		}
		initData();
		new Thread(runnable).start();
		if(MyApplication.getInstance().isStationInLetterBackBtnVisibility()){
			backBtn.setVisibility(View.VISIBLE);
		}else{
			backBtn.setVisibility(View.GONE);
		}
		switch(MyApplication.getInstance().getRoleid()){
     	case Constant.TYPE_AGENCY:
     		MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(1),
     				MyApplication.getInstance().getLettercount());
//			setButtonNumber(mTabWidget.getChildAt(2),MyApplication.getInstance().getPushcount());
     		break;
     	case Constant.TYPE_CLIENT:
     		MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(1),
     				MyApplication.getInstance().getLettercount());
////			setButtonNumber(mTabWidget.getChildAt(2),number);
     		break;
     	case Constant.TYPE_PROPRIETOR:
//     		setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
//     		MainTabActivity.setButtonNumber(MainTabActivity.mTabWidget.getChildAt(2),
//     				MyApplication.getInstance().getPushcount());
//     		MainTabActivity.mTabWidget.setVisibility(View.GONE);
//			setButtonNumber(mTabWidget.getChildAt(3),MyApplication.getInstance().getRcount());
     		break;
		}
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		//后退跳转到第一个Activity界面
		Intent intent = new Intent(this, MainActivity.class).
	              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		Window w = MainFirstTab.group.getLocalActivityManager()
				.startActivity("MainActivity",intent);
	    View view = w.getDecorView();
	    MainFirstTab.group.setContentView(view);
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
	
		switch(MyApplication.getInstance(this).getLettertype()){
		case 1:
			inboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			sendboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			managerLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			inboxText.setTextColor(Color.WHITE);
			sendboxText.setTextColor(Color.BLACK);
			managerText.setTextColor(Color.BLACK);
			break;
		case 2:
			inboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			sendboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			managerLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			inboxText.setTextColor(Color.BLACK);
			sendboxText.setTextColor(Color.WHITE);
			managerText.setTextColor(Color.BLACK);
			break;
		case 3:
			inboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			sendboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			managerLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			inboxText.setTextColor(Color.BLACK);
			sendboxText.setTextColor(Color.BLACK);
			managerText.setTextColor(Color.WHITE);
			break;
		case 4:
			break;
		}
		
		notifyAdapter();
		
	}

	/**
	 * 更新listview列表数据
	 */

	public void notifyAdapter() {
		getlistdata1(MyApplication.getInstance(this).getLettertype());
		stationinletteradapter = new StationInLetterAdapter(this, list);
		listView.setAdapter(stationinletteradapter);
		if(list.size() == 0){
			noDataLayout.setVisibility(View.VISIBLE);
		}else{
			noDataLayout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 更新listview列表数据
	 */

	public void notifyAdapter1() {
		if(stationinletteradapter!=null){
			getlistdata1(MyApplication.getInstance(this).getLettertype());
			stationinletteradapter.notifyDataSetChanged();
			if(list.size() == 0){
				noDataLayout.setVisibility(View.VISIBLE);
			}else{
				noDataLayout.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 获取tab列表数据
	 */

	public void getlistdata() {
		list.clear();
		for(int i=0;i<20;i++){
			HashMap<String, String> map = new HashMap<String,String>();
			map.put("content", "我这个房子是非常好的啊，赶紧跟我联系，要不然我就给别人了。。");
			map.put("name", "清水湾小区房");
			map.put("time", "12月20号");
			if(i%2 == 0){
				map.put("status", "0");
			}else{
				map.put("status", "1");
			}
			list.add(map);
		}
		
	}
	
	
	/**
	 * author By Ring
	 * 获取tab列表数据
	 * @param type 1,收件箱2发件箱3管理员信件
	 */

	public void getlistdata1(int type) {
		if(list==null){
			list = new ArrayList<Map<String,String>>();
		}
		if(MyApplication.getInstance(this).getSosouserinfo(this)!=null){
		list.clear();
		List<Map<String, Object>> selectresult = null;
		String sql = "";
		if(MyApplication.getInstance(this).getLettertype()==1){
			sql="select * from soso_letterininfo where receiveuserid = '"
							+ MyApplication.getInstance(this).getSosouserinfo(this).getUserID() + "' order by senddate desc";
		}else if(MyApplication.getInstance(this).getLettertype()==2){
			sql="select * from soso_letteroutinfo where senduserid = '"
					+ MyApplication.getInstance(this).getSosouserinfo(this).getUserID() + "' order by senddate desc";
		}
		selectresult = DBHelper.getInstance(this).selectRow(sql,null);
		if(selectresult!=null){
			for(int i=0;i<selectresult.size();i++){
				if(selectresult.get(i).get("letterid")!=null
						&&selectresult.get(i).get("title")!=null
						&&selectresult.get(i).get("content")!=null
						&&selectresult.get(i).get("senddate")!=null
						&&selectresult.get(i).get("letterstate")!=null){
					HashMap<String, String> map = new HashMap<String,String>();
					String content = selectresult.get(i).get("title").toString();
					if(content.length()>20){
						content = content.substring(0, 20)+"...";
					}
					map.put("title", content);
					map.put("content", selectresult.get(i).get("content").toString());
					map.put("senduserid", selectresult.get(i).get("senduserid").toString());
					if(selectresult.get(i).get("sendusername")!=null){
						map.put("sendusername", selectresult.get(i).get("sendusername").toString());
					}else{
						map.put("sendusername", "");
					}
					map.put("receiveuserid", selectresult.get(i).get("receiveuserid").toString());
					if(selectresult.get(i).get("receiveusername")!=null){
						map.put("receiveusername", selectresult.get(i).get("receiveusername").toString());
					}else{
						map.put("receiveusername", "");
					}
					if(MyApplication.getInstance(this).getLettertype()==1){
						if(selectresult.get(i).get("sendusername")!=null){
							map.put("name", selectresult.get(i).get("sendusername").toString());
							map.put("id", selectresult.get(i).get("senduserid").toString());
						}else{
							map.put("name", "");
							map.put("id", selectresult.get(i).get("senduserid").toString());
						}
					}else if(MyApplication.getInstance(this).getLettertype()==2){
						if(selectresult.get(i).get("receiveusername")!=null){
							map.put("name", selectresult.get(i).get("receiveusername").toString());
							map.put("id", selectresult.get(i).get("receiveuserid").toString());
						}else{
							map.put("name", "");
							map.put("id", selectresult.get(i).get("receiveuserid").toString());
						}
					}
					map.put("time", selectresult.get(i).get("senddate").toString());
					int state = 0;
					try {
						state = Integer.parseInt(selectresult.get(i)
								.get("letterstate").toString());
					} catch (Exception e) {
					}
					if(type==2){
						map.put("status", "1");
					}else{
						map.put("status", state+"");
					}
					map.put("letterid", selectresult.get(i).get("letterid").toString());
					list.add(map);
				}
			}
		}
		}
		
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		writeNewBtn = (Button) findViewById(R.id.writeNewLetter);
		inboxLayout = (RelativeLayout) findViewById(R.id.shoujianxiangLayout);
		sendboxLayout = (RelativeLayout) findViewById(R.id.fajianxiangLayout);
		managerLayout = (RelativeLayout) findViewById(R.id.guanliyuanLayout);
		listView = (XListView) findViewById(R.id.letterList);
		inboxText = (TextView) findViewById(R.id.inboxText);
		sendboxText = (TextView) findViewById(R.id.sendBoxText);
		managerText = (TextView) findViewById(R.id.managerText);
		progressbar = (ProgressBar) findViewById(R.id.progressBar);
		progressbar.setVisibility(View.GONE);
		noDataLayout = (LinearLayout) findViewById(R.id.noDataLayout);
		noDataImg = (ImageView) findViewById(R.id.noDataImg);
		noDataText = (TextView) findViewById(R.id.noDataMsg);
		noDataImg.setOnClickListener(this);
		String errormsg = "";
		if(tag==1){//收件箱
			errormsg = "您还未收到任何站内信哦，若存在网络问题，可点击图片刷新！";
		}else if(tag==2){//发件箱
			errormsg = "您还未给任何好友发站内信，发站内信给好友，请点击右上角发件按钮，若存在网络问题，可点击图片刷新！";
		}
		noDataText.setText(errormsg);
	}

	private void ViewFun() {
		backBtn.setOnClickListener(this);
		writeNewBtn.setOnClickListener(this);
		inboxLayout.setOnClickListener(this);
		sendboxLayout.setOnClickListener(this);
		managerLayout.setOnClickListener(this);
		listView.setCacheColorHint(0);
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
//		listView.setOnItemClickListener(this);

	}
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backBtn) {
			CommonUtils.hideSoftKeyboard(this);
			// TODO Auto-generated method stub
//			super.onBackPressed();
			//后退跳转到第一个Activity界面
			MyApplication.getInstance().setStationInLetterBackBtnVisibility(false);
			Intent intent = new Intent(this, MainActivity.class).
		              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			MainTabActivity.mTabHost.setCurrentTab(0);
			Window w = MainFirstTab.group.getLocalActivityManager()
					.startActivity("MainActivity",intent);
		    View view = w.getDecorView();
		    MainFirstTab.group.setContentView(view);
		} else if (v == writeNewBtn) {//写新信件
			Intent i = new Intent(this, WriteNewLetterActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("type", 2);//写信
			bundle.putString("messageid", "");
			i.putExtra("bundle", bundle);
			startActivityForResult(i, 0);
		} else if (v == inboxLayout) {//收件箱
			MyApplication.getInstance(this).setLettertype(1);
			inboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			sendboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			managerLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			inboxText.setTextColor(Color.WHITE);
			sendboxText.setTextColor(Color.BLACK);
			managerText.setTextColor(Color.BLACK);
			tag = 1;
			noDataText.setText("您还未收到任何站内信哦，若存在网络问题，可点击图片刷新！");
			list.clear();
			notifyAdapter1();
			new Thread(runnable).start();
		} else if (v == sendboxLayout) {//发件箱
			MyApplication.getInstance(this).setLettertype(2);
			inboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			sendboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			managerLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			inboxText.setTextColor(Color.BLACK);
			sendboxText.setTextColor(Color.WHITE);
			managerText.setTextColor(Color.BLACK);
			tag =2;
			noDataText.setText("您还未给任何好友发站内信，发站内信给好友，请点击右上角发件按钮，若存在网络问题，可点击图片刷新！");
			list.clear();
			notifyAdapter1();
			new Thread(runnable).start();
		} else if (v == managerLayout) {//
			MyApplication.getInstance(this).setLettertype(3);
			inboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			sendboxLayout.setBackgroundResource(R.drawable.soso_email_title_bg2);
			managerLayout.setBackgroundResource(R.drawable.soso_email_title_bg1);
			inboxText.setTextColor(Color.BLACK);
			sendboxText.setTextColor(Color.BLACK);
			managerText.setTextColor(Color.WHITE);
			notifyAdapter1();
//			updataSend();
		} else if(v == noDataImg){//没有数据时点击图片刷新
			new Thread(runnable).start();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(backBtn.getVisibility() == View.VISIBLE){
				MyApplication.getInstance().setStationInLetterBackBtnVisibility(false);
				Intent intent = new Intent(this, MainActivity.class).
			              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				MainTabActivity.mTabHost.setCurrentTab(0);
				Window w = MainFirstTab.group.getLocalActivityManager()
						.startActivity("MainActivity",intent);
			    View view = w.getDecorView();
			    MainFirstTab.group.setContentView(view);
			}else{
				 showExitDialog();
			}
			
		}
		return true;
	}
	
	
	
	/**
	 * author by Ring 向服务器提交登录信息 return true登录成功，false 登录失败
	 */
	public boolean getSendLettle() {
		if(MyApplication.getInstance(this).getSosouserinfo(this)!=null
				&&MyApplication.getInstance(this).getSosouserinfo(this).getUserID()!=null){
			if(DBHelper
					.getInstance(this)
					.selectRow(
							"select * from soso_configurationinfo where name='TLETTERS_TIME' and updatedate1 = value and value<>'' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
							null).size()>0){
				return true;
			}
			String updatedate = "";
			List<Map<String, Object>> selectresult = null;
			selectresult = DBHelper
					.getInstance(this)
					.selectRow(
							"select updatedate1 from soso_configurationinfo where name='TLETTERS_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
							null);
			if (selectresult != null && selectresult.size() > 0) {
				if (selectresult.get(selectresult.size() - 1) != null
						&& selectresult.get(selectresult.size() - 1).get("updatedate1") != null) {
					updatedate = (selectresult.get(selectresult.size() - 1).get(
							"updatedate1").toString());
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
			params.add(new BasicNameValuePair("SendUserID", MyApplication.getInstance(this).getSosouserinfo(this).getUserID()));
			uploaddata = new SoSoUploadData(this, "LetterOutSelect.aspx", params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse();
			if(params!=null){
				params.clear();
				params =null;
			}
			if (StringUtils.CheckReponse(reponse)) {
				return true;
			}
		}
		return false;
	}

	

	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(StationInLetterActivity.this)) {
				handler.sendEmptyMessage(5);// 没有网络时给用户提示
				boolean b =false;
				if(tag==1){//收件箱
					GetInbox getInbox = new GetInbox();
					b=getInbox.getMyInbox(StationInLetterActivity.this);
					reponse = getInbox.getReponse();
				}else if(tag==2){//发件箱
					b =getSendLettle();
				}
				handler.sendEmptyMessage(6);// 没有网络时给用户提示
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
				if(b){
					handler.sendEmptyMessage(1);
				}else{
					handler.sendEmptyMessage(3);
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
			case 1:// 
				notifyAdapter1();
				listView.stopRefresh();
				break;
			case 2:// 
				break;
			case 3:// 
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoNoData.getValue())) {
					if(tag==1){//收件箱
						errormsg = "您还未收到任何站内信哦，若存在网络问题，可点击图片刷新！";
					}else if(tag==2){//发件箱
						errormsg = "您还未给任何好友发站内信，发站内信给好友，请点击右上角发件按钮，若存在网络问题，可点击图片刷新！";
					}
				} else {
					errormsg = getResources().getString(
							R.string.toast_message_failure);
				}
				noDataText.setText(errormsg);
				listView.stopRefresh();
//				Toast.makeText(StationInLetterActivity.this, errormsg, Toast.LENGTH_SHORT).show();
				notifyAdapter1();
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(StationInLetterActivity.this);
				listView.stopRefresh();
				break;
			case 5:// 打开进度条
				progressbar.setVisibility(View.VISIBLE);
				listView.setEnabled(false);
//				progressdialog = new ProgressDialog(getParent());
//				progressdialog.setMessage(getResources().getString(
//						R.string.progress_message_login));
//				progressdialog.setCanceledOnTouchOutside(false);
//				progressdialog.setOnKeyListener(new OnKeyListener() {
//
//					@Override
//					public boolean onKey(DialogInterface dialog, int keyCode,
//							KeyEvent event) {
//						runnable_tag = true;
//						if (uploaddata != null) {
//							uploaddata.overReponse();
//						}
//						return false;
//					}
//				});
//				progressdialog.show();
				break;
			case 6:// 关闭进度条
				progressbar.setVisibility(View.GONE);
				listView.setEnabled(true);
//				if (progressdialog != null && progressdialog.isShowing()) {
//					progressdialog.dismiss();
//				}
				break;
			}
		};
	};

	/***
	 * author by Ring 处理提交登录信息的服务器响应值
	 */

	public void dealReponse() {
		if (StringUtils.getErrorState(reponse).equals(
				ErrorType.SoSoNoData.getValue())) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate1 = value where name='TLETTERS_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
		}
		if (StringUtils.CheckReponse(reponse)) {
			DBHelper.getInstance(this).execSql("update soso_configurationinfo set updatedate1 = value where name='TLETTERS_TIME' and userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'");
			Type listType = new TypeToken<LinkedList<SoSoLetterInInfo>>() {
			}.getType();
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			LinkedList<SoSoLetterInInfo> sosoletterinfos = null;
			SoSoLetterInInfo sosoletterinfo = null;
			sosoletterinfos = gson.fromJson(reponse, listType);
			List<Map<String, Object>> selectresult = null;
			if (sosoletterinfos != null && sosoletterinfos.size() > 0) {
				for (Iterator<SoSoLetterInInfo> iterator = sosoletterinfos.iterator(); iterator
						.hasNext();) {
					sosoletterinfo = (SoSoLetterInInfo) iterator.next();
					if (sosoletterinfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_letteroutinfo",
								"letterid = ?", new String[] { sosoletterinfo.getLetterId() });
						continue;
					}
					values.put("letterid", sosoletterinfo.getLetterId());
					values.put("senduserid", sosoletterinfo.getSendUserId());
					values.put("receiveuserid", sosoletterinfo.getReceiveUserid());
					if(!sosoletterinfo.getSendUserName().equals("")){
						values.put("sendusername", sosoletterinfo.getSendUserName());
					}else{
						values.put("sendusername", MyApplication.getInstance(this).getSosouserinfo(this).getUserName());
					}
					if(!sosoletterinfo.getReceiveUserName().equals("")){
						values.put("receiveusername", sosoletterinfo.getReceiveUserName());
					}
					try {
						values.put("senddate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sosoletterinfo.getSendDate())));
					} catch (ParseException e) {
						values.put("senddate", sosoletterinfo.getSendDate());
					}
					values.put("letterstate", sosoletterinfo.getLetterState());
					values.put("whouserid", sosoletterinfo.getWhoUserID());
					values.put("title", sosoletterinfo.getTitle());
					values.put("content", sosoletterinfo.getContent());
					if (sosoletterinfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_letteroutinfo",
								"letterid = ?",
								new String[] { sosoletterinfo.getLetterId() });
						continue;
					}
					selectresult = DBHelper.getInstance(this).selectRow(
							"select * from soso_letteroutinfo where letterid = '"
									+ sosoletterinfo.getLetterId() + "'", null);
					if (selectresult != null) {
						if (selectresult.size() <= 0) {
							DBHelper.getInstance(this).insert("soso_letteroutinfo",
									values);
						} else {
							DBHelper.getInstance(this).update("soso_letteroutinfo",
									values, "letterid = ?",
									new String[] { sosoletterinfo.getLetterId() });
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
			if (sosoletterinfos != null) {
				sosoletterinfos.clear();
				sosoletterinfos = null;
			}
			DBHelper.getInstance(this).close();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(this,DetailLetterActivity.class);
//		MyApplication.getInstance().setLetterlist(list);
		intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) list);
		intent.putExtra("index", arg2);
		startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.View.XListView.IXListViewListener#onRefresh()
	 */
	@Override
	public void onRefresh() {
		String updatetime = "";
		if(tag==1){//收件箱
			updatetime="updatedate";
		}else{//发件箱
			updatetime="updatedate1";
		}
		String updatedate = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select "+updatetime+" from soso_configurationinfo where name='TLETTERS_TIME' and userid = '"
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
