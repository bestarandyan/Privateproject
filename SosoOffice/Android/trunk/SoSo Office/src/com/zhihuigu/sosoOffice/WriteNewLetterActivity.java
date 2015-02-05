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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.LinkManGvAdapter;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoContactInfo;
import com.zhihuigu.sosoOffice.model.SoSoUserInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class WriteNewLetterActivity extends Activity implements OnClickListener {
	// btn1 ���ذ�ť btn2 ���Ͱ�ť btn3 ȡ����ť
	private Button backBtn;
	private Button sendBtn;// ���Ͱ�ť
	private Button search/*, search1*/;// �ֱ��Ӧ���ҷ����˺ͳ�����
	// shoujianren�ռ��� chaosong���� theme���� content����
	private EditText /*shoujianren,*/ chaosong, theme, content;
	private AlertDialog alertDialog;// �Զ���ĵ�����������ʾǩ��������ѡ��
	private int priorCount = 0;
	private TextView topTitle;
	/*
	 * guoqing params
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public ArrayList<HashMap<String, String>> imagePathes = new ArrayList<HashMap<String, String>>();
	public String imagepath = "";
	public ArrayList<File> uploadfiles = new ArrayList<File>();
	/* ������ʶ����gallery��activity */
	public static final int PHOTO_PICKED_WITH_DATA = 3021;
	private ArrayList<HashMap<String, Object>> list1 = new ArrayList<HashMap<String, Object>>();

	private SoSoUploadData uploaddata;// �������������
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private ProgressDialog progressdialog = null;
	private String WhoUserID="";//�ż�������
	private String shoujianstr = "";//�ż�id
//	private GridView btnGv;
	
	private LinearLayout btnLayout;//����ϵ�˰�ť����󲼾�
	private RelativeLayout deleteLayout;//ɾ����ϵ�˵Ĳ���
	private TextView nameText/*,emailText*/;//��ʾ��ϵ�˵��������ʼ���ַ
	private Button deleteBtn,cancleBtn;//ɾ����ϵ��   ȡ��ɾ������
	private ArrayList<HashMap<String,String>> linkManList;//��ϵ�˼���
	private ArrayList<Button> btnList;//��ϵ�˰�ť����
	private int currentBtnFlag = 0;//��ǵ�ǰ��ϵ�˵�λ��
	private int tag = 0;//0ת����ֱ�ӷ�վ���ţ� 1�ظ�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.a_writenewletter);
		findView();
		ViewFun();
		initData();
	}
	/**
	 * ��ʼ������
	 */
	
	private void initData() {
		linkManList = new ArrayList<HashMap<String,String>>();
		btnList = new ArrayList<Button>();
		if(getIntent().getBundleExtra("bundle")!=null){
			Bundle bundle = getIntent().getBundleExtra("bundle");
//			shoujianren.setText(bundle.getString("shoujianren"));
			shoujianstr = bundle.getString("shoujianrenid");
			tag = bundle.getInt("tag", 0);
			if(bundle.getString("shoujianren")!=null
					&&!bundle.getString("shoujianren").equals("")
					&&bundle.getString("shoujianrenid")!=null
							&&!bundle.getString("shoujianrenid").equals("")){
				HashMap<String,String> map = new HashMap<String, String>();
				if(bundle.getString("shoujianrenid").equals(MyApplication.getInstance(this).getSosouserinfo(this).getUserID()))
					return;
				map.put("name", bundle.getString("shoujianren"));
				map.put("contactuserid", bundle.getString("shoujianrenid"));
				linkManList.add(map);
				LinearLayout mLayout = new LinearLayout(this);
				mLayout.setOrientation(LinearLayout.HORIZONTAL);
				Button btn = new Button(this);
				btn.setBackgroundResource(R.drawable.soso_linkman_select_btn);
				btn.setText(bundle.getString("shoujianren"));
				btn.setTag(bundle.getString("shoujianrenid"));
				btn.setId(btnList.size());
				btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Button btn = (Button) v;
						deleteLayout.setVisibility(View.VISIBLE);
						nameText.setText(btn.getText().toString());
//						emailText.setText(btn.getTag().toString());
						currentBtnFlag = btn.getId();
						for(int i=0;i<btnList.size();i++){//������İ�ťһ����ȡ�������ʽ
							Button btn1 = btnList.get(i);
							if(btn.getId() == i){
								btn1.setBackgroundResource(R.drawable.soso_lianxiren_btn);
							}else{
								btn1.setBackgroundResource(R.drawable.soso_linkman_select_btn);
							}
//							LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)), 
//									Integer.parseInt(getResources().getString(R.string.receiver_btn_height)));
//							param.setMargins(10, 0, 0, 5);
//							btn1.setLayoutParams(param);
							btn1.setPadding(8, 2, 8, 2);
						}
					}
				});
//				btn.setMaxWidth(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)));
				btn.setSingleLine(true);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)), 
						Integer.parseInt(getResources().getString(R.string.receiver_btn_height)));
				btn.setPadding(8, 2, 8, 2);
				if(btnLayout.getChildCount() > 0 && ((ViewGroup) btnLayout.getChildAt(btnLayout.getChildCount()-1)).getChildCount()<2){
					param.setMargins(10, 0, 0, 5);
					btn.setLayoutParams(param);
					((ViewGroup) btnLayout.getChildAt(btnLayout.getChildCount()-1)).addView(btn);
				}else{
					btn.setLayoutParams(param);
					mLayout.addView(btn);
					btnLayout.addView(mLayout);
				}
				btnList.add(btn);//��ÿһ����ϵ�����ڵİ�ť��ӵ���ť������
			}
			if(tag ==1){
				theme.setText(bundle.getString("theme"));
			}else {
				theme.setText(bundle.getString("theme"));
			}
			content.setText(bundle.getString("content"));
			WhoUserID = bundle.getString("WhoUserID");
		}
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		/*shoujianren = (EditText) findViewById(R.id.et1);
		shoujianren.setOnKeyListener(new View.OnKeyListener() {//���ı�������¼�����������̵�ɾ����
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_DEL){
					if(shoujianren.getText().length()==0){
						deleteLayout.setVisibility(View.VISIBLE);
						nameText.setText(linkManList.get(linkManList.size()-1).get("name").toString());
						emailText.setText(linkManList.get(linkManList.size()-1).get("contactuserid").toString());
						currentBtnFlag = linkManList.size()-1;
						for(int i=0;i<btnList.size();i++){
							Button btn1 = btnList.get(i);
							if(i == btnList.size()-1){
								btn1.setBackgroundResource(R.drawable.soso_lianxiren_btn);
							}else{
								btn1.setBackgroundResource(R.drawable.soso_linkman_select_btn);
							}
							LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 50);
							param.setMargins(10, 0, 0, 5);
							btn1.setLayoutParams(param);
							btn1.setPadding(8, 2, 8, 2);
						}
					}
				}
				return false;
			}
		});*/
		chaosong = (EditText) findViewById(R.id.et2);
		chaosong.setVisibility(View.GONE);
		theme = (EditText) findViewById(R.id.et3);
		content = (EditText) findViewById(R.id.et4);
		search = (Button) findViewById(R.id.search);
//		search1 = (Button) findViewById(R.id.search1);
//		search1.setVisibility(View.GONE);
		topTitle = (TextView) findViewById(R.id.topTitle);
		sendBtn = (Button) findViewById(R.id.sendBtn);
//		btnGv = (GridView) findViewById(R.id.btnGv);
		btnLayout = (LinearLayout) findViewById(R.id.btnLayout);
		nameText = (TextView) findViewById(R.id.nameText);
//		emailText = (TextView) findViewById(R.id.emailText);
		deleteBtn = (Button) findViewById(R.id.deleteBtn);
		cancleBtn = (Button) findViewById(R.id.cancleBtn);
		deleteLayout  = (RelativeLayout) findViewById(R.id.cancleLayout);
		int titleFlag = getIntent().getIntExtra("titleFlag",0);
		if(titleFlag == 1){
			topTitle.setText("�ظ�");
		}else if(titleFlag == 2){
			topTitle.setText("ת��");
		}
	}
	private void ViewFun() {
		backBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		search.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
//		search1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backBtn) {//���ذ�ť
			CommonUtils.hideSoftKeyboard(this);
			setResult(RESULT_OK);
			finish();
		} else if (v == search) {//������ϵ��
//			new Thread(runnable1).start();
			Intent intent = new Intent(this,LinkmanActivity.class);
			intent.putExtra("activityFlag", 1);
			startActivityForResult(intent, 0);
		}/* else if (v == search1) {
			showDialog();
		} */else if (v == sendBtn) {//����
			CommonUtils.hideSoftKeyboard(this);
			if(textValidate()){
				new Thread(runnable).start();
			}
		}else if(v == deleteBtn){//ɾ���Ѿ�ѡ�õ���ϵ��
			btnList.remove(currentBtnFlag);//�Ƴ���ť�����е���Ӧ��ť
			linkManList.remove(currentBtnFlag);//�Ƴ���ϵ�˼����еĶ�Ӧ����
			for(int a=0;a<btnLayout.getChildCount();a++){//�Ƴ����а�ť
				((ViewGroup) btnLayout.getChildAt(a)).removeAllViews();
			}
			btnLayout.removeAllViews();
			for(int i=0;i<btnList.size();i++){//�ٽ���ť�����еİ�ťȫ�����س���
				LinearLayout mLayout = new LinearLayout(this);
				mLayout.setOrientation(LinearLayout.HORIZONTAL);
				Button btn = btnList.get(i);
//				btn.setPadding(8, 2, 8, 2);
				btn.setText(btnList.get(i).getText().toString());
				btn.setTag(linkManList.get(i).get("contactuserid").toString());
				btn.setId(i);
				
				btn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Button btn = (Button) v;
						deleteLayout.setVisibility(View.VISIBLE);
						nameText.setText(btn.getText().toString());
//						emailText.setText(btn.getTag().toString());
						currentBtnFlag = btn.getId();
						
						for(int i=0;i<btnList.size();i++){
							Button btn1 = btnList.get(i);
							if(btn.getId() == i){
								btn1.setBackgroundResource(R.drawable.soso_lianxiren_btn);
							}else{
								btn1.setBackgroundResource(R.drawable.soso_linkman_select_btn);
							}
//							LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)),
//									Integer.parseInt(getResources().getString(R.string.receiver_btn_height)));
//							param.setMargins(10, 0, 0, 5);
//							btn1.setLayoutParams(param);
							btn1.setPadding(8, 2, 8, 2);
						}
					}
				});
//				btn.setMaxWidth(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)));
//				mLayout.setGravity(Gravity.CENTER_VERTICAL);
				btn.setSingleLine(true);
				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)), 
						Integer.parseInt(getResources().getString(R.string.receiver_btn_height)));
				btn.setPadding(8, 2, 8, 2);
				if(btnLayout.getChildCount() > 0 && ((ViewGroup) btnLayout.getChildAt(btnLayout.getChildCount()-1)).getChildCount()<2){
					param.setMargins(10, 0, 0, 5);
					btn.setLayoutParams(param);
					((ViewGroup) btnLayout.getChildAt(btnLayout.getChildCount()-1)).addView(btn);
				}else{
					btn.setLayoutParams(param);
					mLayout.addView(btn);
					btnLayout.addView(mLayout);
				}
			}
			deleteLayout.setVisibility(View.GONE);
		}else if(v == cancleBtn){//ȡ��ɾ��
			deleteLayout.setVisibility(View.GONE);
			for(int i=0;i<btnList.size();i++){// �����а�ť��ʧȥ����
				Button btn1 = btnList.get(i);
			    btn1.setBackgroundResource(R.drawable.soso_linkman_select_btn);
//			    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 50);
//				param.setMargins(10, 0, 0, 5);
//				btn1.setLayoutParams(param);
				btn1.setPadding(8, 2, 8, 2);
			}
		}
	}

	/**
	 * �����ݿ��л�ȡ�ռ����б�
	 */
	public void getList1() {
		list1.clear();
		HashMap<String, Object> map = null;
		int i;
		for (i = 0; i < 10; i++) {
			map = new HashMap<String, Object>();
			map.put("name", "����");
			map.put("username", "mail@qingfengweb.com");
			list1.add(map);
		}
	}

	/**
	 * �����ݿ��л�ȡ�ռ����б�
	 */
	public void getList2() {
		if (list1 == null) {
			list1 = new ArrayList<HashMap<String, Object>>();
		}
		list1.clear();
		HashMap<String, Object> map = null;
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
						&& selectresult.get(i).get("username") != null) {
					map = new HashMap<String, Object>();
					map.put("name", selectresult.get(i).get("username").toString());
					map.put("username", selectresult.get(i).get("contactuserid").toString());
					list1.add(map);
				}
			}
			selectresult.clear();
			selectresult = null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == 0){
				
				if(data == null){
					return;
				}
				ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) data.getSerializableExtra("list");
			/*	StringBuffer linker = new StringBuffer();
				StringBuffer linker1 = new StringBuffer();*/
				/*if(!shoujianren.getText().toString().equals("")){
					linker.append(shoujianren.getText().toString()+",");
					linker1.append(shoujianstr+",");
				}*/
				if(list!=null&&list.size()>=0){
					int i;
					for(i=0;i<list.size();i++){
						boolean newlinkman = false;
						for(int j=0;j<linkManList.size();j++){
							if(list.get(i).get("contactuserid").toString().equals(linkManList.get(j).get("contactuserid"))){
								newlinkman = true;
								break;
							}
						}
						if(!newlinkman){//�����ϵ��û����ӹ�
							linkManList.add(list.get(i));
							LinearLayout mLayout = new LinearLayout(this);
							mLayout.setOrientation(LinearLayout.HORIZONTAL);
							Button btn = new Button(this);
							btn.setBackgroundResource(R.drawable.soso_linkman_select_btn);
							btn.setText(list.get(i).get("name").toString());
							btn.setTag(list.get(i).get("contactuserid").toString());
							btn.setId(btnList.size());
							btn.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Button btn = (Button) v;
									deleteLayout.setVisibility(View.VISIBLE);
									nameText.setText(btn.getText().toString());
//									emailText.setText(btn.getTag().toString());
									currentBtnFlag = btn.getId();
									for(int i=0;i<btnList.size();i++){//������İ�ťһ����ȡ�������ʽ
										Button btn1 = btnList.get(i);
										if(btn.getId() == i){
											btn1.setBackgroundResource(R.drawable.soso_lianxiren_btn);
										}else{
											btn1.setBackgroundResource(R.drawable.soso_linkman_select_btn);
										}
//										LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)), 
//												Integer.parseInt(getResources().getString(R.string.receiver_btn_height)));
//										param.setMargins(10, 0, 0, 5);
//										btn1.setLayoutParams(param);
										btn1.setPadding(8, 2, 8, 2);
									}
								}
							});
//							btn.setMaxWidth(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)));
//							mLayout.setGravity(Gravity.CENTER_VERTICAL);
							btn.setSingleLine(true);
							LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(getResources().getString(R.string.receiver_btn_width)), 
									Integer.parseInt(getResources().getString(R.string.receiver_btn_height)));
							btn.setPadding(8, 2, 8, 2);
							if(btnLayout.getChildCount() > 0 && ((ViewGroup) btnLayout.getChildAt(btnLayout.getChildCount()-1)).getChildCount()<2){
								param.setMargins(10, 0, 0, 5);
								btn.setLayoutParams(param);
								((ViewGroup) btnLayout.getChildAt(btnLayout.getChildCount()-1)).addView(btn);
							}else{
								btn.setLayoutParams(param);
								mLayout.addView(btn);
								btnLayout.addView(mLayout);
							}
							btnList.add(btn);//��ÿһ����ϵ�����ڵİ�ť��ӵ���ť������
						}
						/*if(!linker.toString().contains(list.get(i).get("name"))){
							linker.append(list.get(i).get("name")+",");
							linker1.append(list.get(i).get("contactuserid")+",");
						}*/
					}
				/*	if(linker.length()>0){
						shoujianren.setText(linker.substring(0, linker.length()-1));
						shoujianstr = linker1.substring(0, linker1.length()-1);
					}*/
					
				}
//				notifyLinkMan();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return true;
	}

	/**
	 * �����ż� true,ɾ���ɹ���false ɾ��ʧ��
	 */
	public boolean sendLetter() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("SendUserID", MyApplication
				.getInstance(this).getSosouserinfo(this).getUserID()));
		params.add(new BasicNameValuePair("ReceiveUserIdList", shoujianstr));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = StringUtils.formatCalendar1(sdf.format(new Date()),MyApplication.error_value);
		params.add(new BasicNameValuePair("SendDate", time));
		params.add(new BasicNameValuePair("LetterState", "0"));
		if(WhoUserID==null||WhoUserID.equals("")){
			WhoUserID = MyApplication
					.getInstance(this).getSosouserinfo(this).getUserID();
		}
		params.add(new BasicNameValuePair("WhoUserID", WhoUserID));
		params.add(new BasicNameValuePair("Title", theme.getText().toString().replace("FWD:", "").replace("RE:", "")));
		params.add(new BasicNameValuePair("Content", content.getText().toString()));

		uploaddata = new SoSoUploadData(this, "LetterAddList.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		params.clear();
		params = null;
		dealReponse1(time,shoujianstr);
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	
	/***
	 * author by Ring �����������Ӧֵ
	 */

	public void dealReponse1(String time,String str) {
		if (StringUtils.CheckReponse(reponse)) {
			ContentValues values = new ContentValues();
			Gson gson = new Gson();// ����Gson����
			lettleInfo lettleinfo = null;
			try {
				lettleinfo = gson.fromJson(reponse, lettleInfo.class);// ����json����
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (lettleinfo != null && lettleinfo.getLetterID() != null) {
				values.put("letterid", lettleinfo.getLetterID());
				values.put("senduserid", MyApplication
						.getInstance(this).getSosouserinfo(this).getUserID());
				values.put("receiveuserid", str);
				values.put("senddate", time);
				values.put("letterstate", 1);
				values.put("whouserid",MyApplication
						.getInstance(this).getSosouserinfo(this).getUserID());
				values.put("title", theme.getText().toString());
				values.put("content", content.getText().toString());
				DBHelper.getInstance(WriteNewLetterActivity.this).insert(
						"soso_letterinfo", values);
			}
			if(values!=null){
				values.clear();
				values = null;
			}
		}
	}
	
	public class lettleInfo{
		private String LetterID;

		public String getLetterID() {
			return LetterID;
		}

		public void setLetterID(String letterID) {
			LetterID = letterID;
		}
		
		
	}

	/**
	 * ��ѯ��ϵ�� true,��ѯ�ɹ���false ��ѯʧ��
	 */
	public boolean selectContact() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("UserID", MyApplication
				.getInstance(this).getSosouserinfo(this).getUserID()));
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
	 * author by Ring ����ɾ���ż���������Ӧֵ
	 */

	public void dealReponse() {
		if (StringUtils.CheckReponse(reponse)) {
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
					values.put("userid", sosocontactinfo.getUserID());
					values.put("username", sosocontactinfo.getUserName());
					values.put("realname", sosocontactinfo.getRealName());
					values.put("headimage", sosocontactinfo.getHeadImage());
					values.put("dictid", sosocontactinfo.getDictID());
					values.put("adddate", sosocontactinfo.getAdddate());
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
	 * author by Ring ע��ǰ���ύ��Ϣ������֤ return true ��֤�ɹ���false ��֤ʧ��
	 */
	public boolean textValidate() {
		shoujianstr = getcontactIds();
		if (shoujianstr==null||shoujianstr.trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.shoujianren_null),
					WriteNewLetterActivity.this);
			return false;
		} else if (theme.getText().toString().replace("FWD:", "").replace("RE:", "").trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.title_null),
					WriteNewLetterActivity.this);
			return false;
		} else if (content.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.content_null),
					WriteNewLetterActivity.this);
			return false;
		}
		return true;
	}
	
	public String getcontactIds(){
		StringBuffer contactidsbuffer = new StringBuffer("");
		if(linkManList!=null&&linkManList.size()>0){
			int i ;
			for(i=0;i<linkManList.size();i++){
				if(linkManList.get(i).get("name")!=null
						&&!linkManList.get(i).get("name").toString().equals("")
						&&linkManList.get(i).get("contactuserid")!=null
						&&!linkManList.get(i).get("contactuserid").toString().equals("")
						&&!linkManList.get(i).get("contactuserid").toString().equals(MyApplication.getInstance(this).getSosouserinfo(this).getUserID())){
					contactidsbuffer.append(linkManList.get(i).get("contactuserid").toString()+",");
				}
			}
		}
		String contactids  = "";
		if(contactidsbuffer!=null&&contactidsbuffer.length()>1){
			contactids=contactidsbuffer.subSequence(0, contactidsbuffer.length()-1).toString();
		}
		return contactids;
	}

	/**
	 * author by Ring ����ɾ���ż���ʱ����
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(WriteNewLetterActivity.this)) {
				handler.sendEmptyMessage(5);
				boolean b = sendLetter();
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// ��ע�������ת��ע��ɹ�����
				} else {
					handler.sendEmptyMessage(3);// ע��ʧ�ܸ��û���ʾ
				}
			} else {
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}

			click_limit = true;
		}
	};
	/**
	 * author by Ring ����ҳ����ת����
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ���ͳɹ��������Ի�����ʾ�û�
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.sendlettlesuccess),
						WriteNewLetterActivity.this,true);
				break;
			case 2:
//				showDialog();
				break;
			case 3:// ע��ʧ�ܸ��û���ʾ
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(
						ErrorType.SoSoTimeOut.getValue())) {
					message = getResources().getString(
							R.string.progress_timeout);
				} else {
					message = getResources().getString(
							R.string.sendlettlefailure);
				}
				MessageBox.CreateAlertDialog(message,
						WriteNewLetterActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(WriteNewLetterActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(WriteNewLetterActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
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
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};

}
