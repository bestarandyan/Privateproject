/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoConfigurationInfo;
import com.zhihuigu.sosoOffice.model.SoSoCountInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.GetBuilds;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * @author ������
 * @createDate 2013/1/8
 * ��ǩ�࣬���ƾӵ׵�TabHost
 *
 */
public class MainTabActivity extends TabActivity{
	public static TabHost mTabHost;
	public static TabWidget mTabWidget;
	private int userType = 0;
	TimerTask timerTask = null;
	Timer timer = null;
	private int number = 0;
	
	
	private SoSoUploadData uploaddata;
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private ProgressDialog progressdialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.a_tabmain);
		initView();
		roleId = MyApplication.getInstance().getRoleid();
		System.out.println(roleId+"�û�����");
//		startTimer();
//		if(getIntent().getIntExtra("tag", 0)==1){
//			new Thread(runnable1).start();
//		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onResume()
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		
		if(roleId != MyApplication.getInstance().getRoleid()){
			initView();
		}
		runnable_tag = false;
		updateCount();
		
		new Thread(runnable1).start();
		super.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onPause()
	 */
	@Override
	protected void onPause() {
		roleId = MyApplication.getInstance().getRoleid();
		runnable_tag = true;
		click_limit = true;
		if (uploaddata != null) {
			uploaddata.overReponse();
		}
		super.onPause();
	}
	
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
	 * ����������Ϣ
	 *
	 * ���ߣ�Ring
	 * �����ڣ�2013-2-28
	 */
	public void updateCount(){
		if(MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper.getInstance(this).selectRow(
				"select * from soso_countinfo where userid = '"
						+ MyApplication.getInstance().getSosouserinfo(this).getUserID() + "'", null);
		if (selectresult != null) {
			int count = 0;
			try {
				count = Integer.parseInt(selectresult
						.get(selectresult.size() - 1).get("lettercount").toString());
				MyApplication.getInstance().setLettercount(count);
			} catch (Exception e) {
			}
			try {
				count = Integer.parseInt(selectresult
						.get(selectresult.size() - 1).get("pushcount").toString());
				MyApplication.getInstance().setPushcount(count);
			} catch (Exception e) {
			}
			try {
				count = Integer.parseInt(selectresult
						.get(selectresult.size() - 1).get("rcount").toString());
				MyApplication.getInstance().setRcount(count);
			} catch (Exception e) {
			}
		}
		handler.sendEmptyMessage(0);
	}
	
	/***
	 * ��ȡ�׽��������
	 */

	public void getCount() {
		if(MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
		String updatedate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = StringUtils.formatCalendar1(sdf.format(new Date()),MyApplication.error_value);
		updatedate = StringUtils.formatDate1(time,3);
		List<Map<String, Object>> selectresult = null;
		selectresult = DBHelper
				.getInstance(this)
				.selectRow(
						"select updatedate from soso_countinfo where userid = '"
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
		params.add(new BasicNameValuePair("LastTimeLogin", updatedate));
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance().getSosouserinfo(this).getUserID()));
		uploaddata = new SoSoUploadData(this, "SystemCountSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse3(updatedate,MyApplication.getInstance().getSosouserinfo(this).getUserID());
		params.clear();
		params = null;
	}
	
	/***
	 * author by Ring ����ȡ���������ݱ��浽�������ݿ�
	 */
	private void dealReponse3(String updatedate,String userid) {
		if (StringUtils.CheckReponse(reponse)) {
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			List<Map<String, Object>> selectresult = null;
			SoSoCountInfo soSoCountInfo= null;
			try {
				soSoCountInfo = gson.fromJson(reponse, SoSoCountInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(soSoCountInfo!=null){
				values.put("userid", userid);
				values.put("lettercount", soSoCountInfo.getLetterCount());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = StringUtils.formatCalendar1(sdf.format(new Date()),MyApplication.error_value);
				values.put("updatedate", time);
				selectresult = DBHelper.getInstance(this).selectRow(
						"select * from soso_countinfo where userid = '"
								+ userid + "'", null);
				int pushcount1 = 0;
				try {
					pushcount1 = Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("pushcount1")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				int pushcount = 0;
				try {
					pushcount = Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("pushcount")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(soSoCountInfo.getPushCount()!=0){
					if(soSoCountInfo.getPushCount()!=pushcount1){
						values.put("pushcount", soSoCountInfo.getPushCount()+pushcount);
						values.put("pushcount1", soSoCountInfo.getPushCount());
					}
				}
				
				int rcount = 0;
				try {
					rcount = Integer.parseInt(selectresult
							.get(selectresult.size() - 1).get("rcount1")
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(soSoCountInfo.getRequirementCount()!=0){
					if(soSoCountInfo.getRequirementCount()!=rcount){
						values.put("rcount", soSoCountInfo.getRequirementCount());
						values.put("rcount1", soSoCountInfo.getRequirementCount());
					}
				}
				if (selectresult != null) {
					if (selectresult.size() <= 0) {
						DBHelper.getInstance(this).insert(
								"soso_countinfo", values);
					} else {
						DBHelper.getInstance(this)
								.update("soso_countinfo",
										values,
										"userid = ?",
										new String[] {userid});
					}
					selectresult.clear();
					selectresult = null;
				}else{
					DBHelper.getInstance(this).insert(
							"soso_countinfo", values);
				}
			}
			if(values!=null){
				values.clear();
				values=null;
			}
		}
	}
	
	/**
	 * author by Ring �����ʱ����
	 */
	public Runnable runnable1 = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(MainTabActivity.this)){
				if(!runnable_tag){
					getCount();
					getConfigration();
					handler2.sendEmptyMessage(1);// 
				}
//				handler2.sendEmptyMessage(5);// ����������
//				if(MyApplication.getInstance()
//						.getCityid()!=null
//						&&!MyApplication.getInstance()
//						.getCityid().equals("")){
//					getbuilds = new GetBuilds(MainTabActivity.this);
//					getbuilds.getBuildsBycity(MyApplication.getInstance()
//							.getCityid());
//				}
//				handler2.sendEmptyMessage(6);// �رս�����
//				handler2.sendEmptyMessage(1);// ��¼�ɹ���
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
			} else {
				handler2.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}
			click_limit = true;
		}
	};
	
//	/**
//	 * author by Ring ��ȡϵͳ����
//	 */
	public void getConfigration() {
		if(MyApplication.getInstance().getSosouserinfo()==null
				||MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			return;
		}
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		uploaddata = new SoSoUploadData(this, "GetSystemSetting.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse1();
		params.clear();
		params = null;
	}

	/**
	 * author by Ring ��������Ӧ����ķ�������ȡ���ã�
	 */
	public void dealReponse1() {
		if (StringUtils.CheckReponse(reponse)) {
			Type listType = new TypeToken<LinkedList<SoSoConfigurationInfo>>() {
			}.getType();
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			LinkedList<SoSoConfigurationInfo> settingsinfos = null;
			SoSoConfigurationInfo settingsinfo = null;
			settingsinfos = gson.fromJson(reponse, listType);
			if (settingsinfos != null) {
				for (Iterator<SoSoConfigurationInfo> iterator = settingsinfos.iterator(); iterator
						.hasNext();) {
					settingsinfo = (SoSoConfigurationInfo) iterator.next();
					if (settingsinfo!=null&&settingsinfo.getName() != null
							&& settingsinfo.getValue() != null) {
						List<Map<String, Object>> selectresult = DBHelper
								.getInstance(this).selectRow(
										"select name from soso_configurationinfo where name='"
												+ settingsinfo.getName() + "' and userid = '"+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'",
										null);
//						"userid text,"+//�û�id
//						"name text,"+//������keyֵ
//						"value text,"+//�����е�valueֵ
//						"updatedate text"+//�������õ�ʱ��
						values.put("name", settingsinfo.getName());//�����еļ�ֵ��
						values.put("value", settingsinfo.getValue());//�����е�valueֵ�������������ʱ��
						values.put("userid", MyApplication.getInstance(this).getSosouserinfo(this).getUserID());
						if (selectresult != null && selectresult.size() > 0) {
							DBHelper.getInstance(this).update("soso_configurationinfo",
									values, "name = ? and userid = ? ",
									new String[] { settingsinfo.getName(),MyApplication.getInstance(this).getSosouserinfo(this).getUserID()});
						} else {
							if(settingsinfo.getName().equals("TMETRO_TIME")
									||settingsinfo.getName().equals("THELPDOCUMENT_TIME")){
								values.put("updatedate", "1900-01-01 00:00:00");
							}
							DBHelper.getInstance(this).insert("soso_configurationinfo",
									values);
						}
						if (settingsinfo.getName()!=null
								&&settingsinfo.getValue()!=null
								&&settingsinfo.getName().equals("SERVER_TIME")){
							long error_value = 0;
							java.util.Date date1;
							try {
								date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(settingsinfo.getValue());
								long l1 = date1.getTime();
								long l2 = 0;
								java.util.Date d2 = new Date();
								l2 = d2.getTime();
								error_value = l1-l2;
								MyApplication.error_value = error_value;
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					values.clear();
				}
				values = null;
				DBHelper.getInstance(this).close();
				settingsinfos.clear();
				settingsinfo = null;
				settingsinfos = null;
			}
		}
	}

	/**
	 * author by Ring ����ҳ����ת����
	 */
	public Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ����������Ϣ
					updateCount();
				break;
			case 2:// �ӵ�¼������ת��ע�����
				break;
			case 3:// ��¼ʧ�ܸ��û���ʾ
				String errormsg = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					errormsg = getResources().getString(
							R.string.progress_timeout);
				} else {
					errormsg = getResources().getString(
							R.string.login_error_check);
				}
				MessageBox.CreateAlertDialog(errormsg, MainTabActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(MainTabActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(MainTabActivity.this);
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
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};
//	private void startTimer(){
//		timer = new Timer();
//		timerTask = new TimerTask() {
//			@Override
//			public void run() {
//				number ++;
//				handler.sendEmptyMessage(0);
//			}
//		};
//		timer.schedule(timerTask, 1000, 2000);
//	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch(MyApplication.getInstance(MainTabActivity.this).getRoleid()){
         	case Constant.TYPE_AGENCY:
         		setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
    			setButtonNumber(mTabWidget.getChildAt(2),MyApplication.getInstance().getPushcount());
         		break;
         	case Constant.TYPE_CLIENT:
         		setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
//    			setButtonNumber(mTabWidget.getChildAt(2),number);
         		break;
         	case Constant.TYPE_PROPRIETOR:
         		setButtonNumber(mTabWidget.getChildAt(1),MyApplication.getInstance().getLettercount());
//    			setButtonNumber(mTabWidget.getChildAt(2),MyApplication.getInstance().getPushcount());
    			setButtonNumber(mTabWidget.getChildAt(3),MyApplication.getInstance().getRcount());
         		break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	
	/** ��ʼ��
	 * @author ������
	 * @createDate 2013/1/8
	 */
	
	private int tabindex = 0;
	private int roleId = 0;
	private void initView(){
		if(mTabHost!=null && mTabHost.getChildCount() > 0){
			mTabHost.clearAllTabs();
//			mTabHost.getTabWidget().removeAllViews();
			mTabHost = this.getTabHost();
		}else{
			mTabHost = this.getTabHost();
			
		}
		   /* ȥ����ǩ�·��İ��� */
     mTabHost.setPadding(mTabHost.getPaddingLeft(),mTabHost.getPaddingTop(), 
     		mTabHost.getPaddingRight(),mTabHost.getPaddingBottom() );
//     MyApplication.getInstance(this).setRoleid(Constant.TYPE_PROPRIETOR);
     userType = MyApplication.getInstance(this).getRoleid();
    
     switch(userType){//���ݲ�ͬ���û����ͼ��ز�ͬ�Ĳ���
     case Constant.TYPE_PROPRIETOR://ҵ��
     	addTabHost(MainFirstTab.class,"mainActivity",
     			getBottomView(R.drawable.soso_bottom_ico1_on,0,"��ҳ",Color.WHITE));
     	addTabHost(StationInLetterActivity.class,"StationInLetterActivity",
     			getBottomView(R.drawable.soso_bottom_ico2,0,"վ����",Color.rgb(165,168,175)));
     	addTabHost(RecommendManagerForOwner.class,"RecommendManagerForOwner",
     			getBottomView(R.drawable.soso_bottom_ico3,0,"����",Color.rgb(165,168,175)));
     	addTabHost(ClientDemandActivity.class,"ClientDemandActivity",
     			getBottomView(R.drawable.soso_bottom_ico5,0,"�ͻ�����",Color.rgb(165,168,175)));
     	addTabHost(OtherSoftwareInfoActivity.class,"OtherSoftwareInfoActivity",
     			getBottomView(R.drawable.soso_bottom_ico7,0,"����",Color.rgb(165,168,175)));
     	break;
     case Constant.TYPE_AGENCY://�н�
     	addTabHost(MainFirstTab.class,"mainActivity",
     			getBottomView(R.drawable.soso_bottom_ico1_on,0,"��ҳ",Color.WHITE));
     	addTabHost(StationInLetterActivity.class,"StationInLetterActivity",
     			getBottomView(R.drawable.soso_bottom_ico2,0,"վ����",Color.rgb(165,168,175)));
     	addTabHost(RecommendManagerForAgency.class,"RecommendManagerForAgency",
     			getBottomView(R.drawable.soso_bottom_ico3,0,"����",Color.rgb(165,168,175)));
     	addTabHost(CollectManagerActivity.class,"CollectManagerActivity",
     			getBottomView(R.drawable.soso_bottom_ico6,0,"�ղ�",Color.rgb(165,168,175)));
     	addTabHost(OtherSoftwareInfoActivity.class,"OtherSoftwareInfoActivity",
     			getBottomView(R.drawable.soso_bottom_ico7,0,"����",Color.rgb(165,168,175)));
     	break;
     case Constant.TYPE_CLIENT://�ͻ�
     	addTabHost(MainFirstTab.class,"mainActivity",
     			getBottomView(R.drawable.soso_bottom_ico1_on,0,"��ҳ",Color.WHITE));
     	addTabHost(StationInLetterActivity.class,"StationInLetterActivity",
     			getBottomView(R.drawable.soso_bottom_ico2,0,"վ����",Color.rgb(165,168,175)));
     	addTabHost(AccurateSearchActivity.class,"AccurateSearchActivity",
     			getBottomView(R.drawable.soso_bottom_ico4,0,"����",Color.rgb(165,168,175)));
     	addTabHost(CollectManagerActivity.class,"CollectManagerActivity",
     			getBottomView(R.drawable.soso_bottom_ico6,0,"�ղ�",Color.rgb(165,168,175)));
     	addTabHost(OtherSoftwareInfoActivity.class,"OtherSoftwareInfoActivity",
     			getBottomView(R.drawable.soso_bottom_ico7,0,"����",Color.rgb(165,168,175)));
     	break;
     }
     mTabHost.setCurrentTab(tabindex);
     /* ��Tab��ǩ�Ķ��� */
     mTabWidget = mTabHost.getTabWidget();
     mTabHost.setOnTabChangedListener(new OnTabChangeListener()
     {
         @Override
         public void onTabChanged(String tabId)
         {
        	 if(!tabId.equals("AccurateSearchActivity")
        			 &&!tabId.equals("mainActivity")
        			 &&!tabId.equals("OtherSoftwareInfoActivity")){
					if((MyApplication.getInstance().getSosouserinfo(MainTabActivity.this)==null
							||MyApplication.getInstance().getSosouserinfo(MainTabActivity.this).getUserID()==null)){
						mTabHost.setCurrentTab(tabindex);
						return;
					}
				}
        	 if(tabId.equals("mainActivity")){//���ƾ�ȷ�����еĹؼ��ʿؼ�
        		 MyApplication.getInstance().setSearchbundle(null);
        		 MyApplication.getInstance().setCurrentKeyt("");
        	 }
        	 tabindex = mTabHost.getCurrentTab();
        	 String str = tabId;
        	 String str2 = str;
        	 CommonUtils.hideSoftKeyboard(MainTabActivity.this);
             for (int i = 0; i < mTabWidget.getChildCount(); i++){
                 View view = mTabWidget.getChildAt(i);
                 if (mTabHost.getCurrentTab() == i){
                 	switch(MyApplication.getInstance(MainTabActivity.this).getRoleid()){
                 	case Constant.TYPE_AGENCY:
                 		setBottomImage(view,Constant.agency_on[i],Color.WHITE);
                 		break;
                 	case Constant.TYPE_CLIENT:
                 		setBottomImage(view,Constant.client_on[i],Color.WHITE);
                 		break;
                 	case Constant.TYPE_PROPRIETOR:
                 		setBottomImage(view,Constant.proprietor_on[i],Color.WHITE);
                 		break;
                 	}
                 	
                 }else{
                 	switch(MyApplication.getInstance(MainTabActivity.this).getRoleid()){
                 	case Constant.TYPE_AGENCY:
                 		setBottomImage(view,Constant.agency[i],Color.rgb(165,168,175));
                 		break;
                 	case Constant.TYPE_CLIENT:
                 		setBottomImage(view,Constant.client[i],Color.rgb(165,168,175));
                 		break;
                 	case Constant.TYPE_PROPRIETOR:
                 		setBottomImage(view,Constant.proprietor[i],Color.rgb(165,168,175));
                 		break;
                 	}
                 	
                 }
             }
         }
     });
     
     ImageView homeBtn = (ImageView) mTabHost.getChildAt(0).findViewById(R.id.bottomImg);
     if(homeBtn!=null){
	     homeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mTabHost.getCurrentTab() == 0){
					Intent intent = new Intent(MainTabActivity.this, MainActivity.class).
				              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//��һ��Activityת����һ��View
					Window w = MainFirstTab.group.getLocalActivityManager().startActivity("MainActivity",intent);
				    View view = w.getDecorView();
				    //��View��Ӵ�ActivityGroup��
				    MainFirstTab.group.setContentView(view);
				}else{
					mTabHost.setCurrentTab(0);
				}
			}
		});
     }
	}
/**
 * ���һ��TabHost��
 * @param activity ��һ�����Ӧ��Activity
 * @param tabName ��һ�������
 * @param view ��һ���Ӧ�ĵײ��ؼ�
 * @author ������
 * @createDate 2013/1/8
 */
	private void addTabHost(Class cls,String tabName,View view){
		 	Intent layoutintent = new Intent();
	        layoutintent.setClass(this, cls);
	        TabHost.TabSpec layoutspec = mTabHost.newTabSpec(tabName);
	        layoutspec.setIndicator(view);
	        layoutspec.setContent(layoutintent);
	        mTabHost.addTab(layoutspec);
	}
	/**
	 * �����ӵײ���
	 * @param imageId ͼƬ��id
	 * @param number Ҫ��ʾ������
	 * @param text ��ǩ����
	 * @return һ����ǩ
	 * @author ������
	 * @createDate 2013/1/8
	 */
	private View getBottomView(int imageId,int number,String text,int color){
		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_tab_bottom, null);
		ImageView image = (ImageView) linearLayout.findViewById(R.id.bottomImg);
		Button  bottomBtn = (Button) linearLayout.findViewById(R.id.bottomCircle);
		TextView bottomText = (TextView) linearLayout.findViewById(R.id.bottomText);
		image.setImageResource(imageId);
		bottomBtn.setClickable(false);
		bottomBtn.setEnabled(false);
		if(number>0){
			bottomBtn.setVisibility(View.VISIBLE);
			bottomBtn.setText(String.valueOf(number));
		}else{
			bottomBtn.setVisibility(View.GONE);
		}
		bottomText.setTextColor(color);
		bottomText.setText(text);
        return linearLayout;
	}
	
	
	
	
	/**
	 * �����ǩ��ʱ�ı�ͼƬ�Լ��ı�����ʽ
	 * @param parent ��ǩ��
	 * @param id ͼƬid
	 * @param color �ı���ɫ
	 * @author ������
	 * @createDate 2013/1/8
	 */
	private void setBottomImage(View parent,int id ,int color){
		ImageView image = (ImageView) parent.findViewById(R.id.bottomImg);
		image.setImageResource(id);
		TextView textView = (TextView) parent.findViewById(R.id.bottomText);
		textView.setTextColor(color);
	}
	/**
	 * ���ײ�����Ϣ������ť����ֵ
	 * @param parent �ײ���
	 * @param number ֵ
	 * @author ������
	 * @createDate 2013/1/8
	 */
	public static void setButtonNumber(View parent,int number){
		Button btn = (Button) parent.findViewById(R.id.bottomCircle);
		if(MyApplication.getInstance().getSosouserinfo()==null||
				MyApplication.getInstance().getSosouserinfo().getUserID()==null){
			btn.setVisibility(View.GONE);
		}else{
			if(number>0){
				btn.setVisibility(View.VISIBLE);
				btn.setText(String.valueOf(number));
			}else{
				btn.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
