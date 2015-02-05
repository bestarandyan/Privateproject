/**
 * 
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhihuigu.sosoOffice.Adapter.LoupanListAdapter;
import com.zhihuigu.sosoOffice.Interface.Activity_interface;
import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.OfficeType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.SoSoBuildInfo;
import com.zhihuigu.sosoOffice.model.SoSoImageInfo;
import com.zhihuigu.sosoOffice.model.SoSoOfficeInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.thread.SoSoUploadFile;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.GetBuilds;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderId;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;
import com.zhihuigu.sosoOffice.utils.ZoneUtil;

/**
 * @author ������
 * @createDate 2013/1/17 ����·�Դ��
 * 
 */
public class ReleaseNewRoomActivity extends BaseActivity implements
		Activity_interface, OnItemClickListener {
	private Gallery gallery;//��ԴͼƬ�鿴�ؼ�
	private Button backBtn;
	private ArrayList<HashMap<String, Object>> roomImgList;
	public int chazhi = 0;// �ײ��������λ��
	private PopupWindow selectPopupWindow = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// �������ղ����ı�־
	public final static int REQUEST_CODE_INFO_SET = 1;// �����ı���Ϣ�ķ���ֵ���
	public static final int PHOTORESOULT = 3;
	private final String TAG = "PersonalDataSetActivity";
	public boolean initWedget_tag = true;
	public String path = "";// ��Ƭ·��
	public String fileName = "";// ͼƬ����
	private Button takePictureBtn, getAlbumBtn, cancleBtn;// ���ջ�ȡͼƬ ���ֻ��ڴ浼��ͼƬ
															// ȡ����ť
	public static final String SDCARD_ROOT_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();// ·��
	public static String SAVE_PATH_IN_SDCARD = ""; // ͼƬ���������ݱ����ļ���
	private LinearLayout parent = null;
	private File sdcardTempFile = null;
	private ScrollView scrollView;// ����������������Ϊ���������������ͣ�������յĵط�
	private int scrollH = 0;// ��¼��������λ��

	private ArrayList<HashMap<String, String>> loupanList = new ArrayList<HashMap<String, String>>();// ����¥�̼���
	private ListView dialogListView;
	TextView noText;//������ʱ��ʾ
	private TextView dialogTitle;
	private Button dialogBackBtn;
	private Button dialogCloseBtn;
	private ImageView dialogJianTou;

	private int data_type = 0;// 0������� 1������Ȧ 2����¥�� 3����Դ����

	// private EditText roomNameEt;//��Դ����
	private Button loupanSpinner;// ����¥��
	private EditText phoneEt;// ��ϵ��ʽ
	private Button typeRoomSpinner;// ��Դ����
	private EditText loucengEt;// ¥��
	private EditText roomNumberEt;// �����
	private EditText roomMoneyEt;// ������
	private EditText managerMoneyEt;// ��ҵ�����
	private EditText acreageEt;// ���
//	private EditText ownAcreageEt;// �÷���
	private EditText roomIntroEt;// ��Դ���
	private Button saveAndSendBtn;// ���沢������Դ�İ�ť
	private Button helpBtn;// ����˵��
	private TextView title;

	private String cityid = "";// ���б��
	private String areaid = "";// ������
	private String districtid = "";// ��Ȧ���
	private String buildid = "";// ¥�̱��
	private String officetype = "";// ��Դ����

	private SoSoUploadData uploaddata;// �ϴ����ݵĶ���
	private GetBuilds getbuilds;
	private SoSoUploadFile sosouploadfile = null;// �ϴ��ļ��Ķ���
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ

	private String officeid = "";
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private ProgressDialog progressdialog;
	private int intenttag = 1;// ��ʾ�ý������޸Ļ�����ӣ�1��ӣ�2��˹��ı༭ 3����˵ı༭
	private StringBuffer deleteimages = new StringBuffer("");//ƴ�ӵ�ɾ��ͼƬ��id;
	private int activityFlag = 0;//0����ӷ�Դ�б���������  1����ӷ�Դ��������ת����
	// private ArrayList<HashMap<String, Object>> list;// ��ԴͼƬ�б�
	private boolean editFlag = true;
	private int galleryFirstImgSize = 300;//��ʾ��ԴͼƬ�ĵ�һ��������ȡͼƬ��ͼƬ�ߴ�
	private int gallerySize = 150;//��ԴͼƬ�Ŀؼ��ߴ�
	private int btn_tag = 0;//0�κβ������1���¥��btn2�����Դ����btn
	
	private LinearLayout hintLoupanLayout,hintTypeLayout,hintLouCengLayout,hintRoomNumberLayout,hintPhoneLayout,
								hintMoneyLayout,hintWuyeLayout,hintAcreageLayout;
	private TextView hintLoupanTv,hintTypeTv,hintLouCengTv,hintRoomNumberTv,hintPhoneTv,
	hintMoneyTv,hintWuyeTv,hintAcreageTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_releasenewroom);
		findView();
		initView();
		initData();
		activityFlag = getIntent().getIntExtra("activityFlag", 0);
		editFlag = getIntent().getBooleanExtra("editFlag", true);
	}
	
	/***
	 * author by Ring �ı���������¼�
	 */
	private OnFocusChangeListener textfocuschange = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				return;
			}
//			LinearLayout hintLoupanLayout,hintTypeLayout,hintLouCengLayout,hintRoomNumberLayout,hintPhoneLayout,
//			hintMoneyLayout,hintWuyeLayout,hintAcreageLayout;
			Message msg = new Message();
			Bundle b = new Bundle();
			
			if (v == loucengEt
					&& loucengEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","��ѡ��¥��");
				msg.setData(b);
				msg.what = 15;
				handler.sendMessage(msg);
			}else if(v == loucengEt
					&& !loucengEt.getText().toString().trim().equals("")){
				int louceng = 0;
				try {
					louceng = Integer.parseInt(loucengEt.getText().toString());
				} catch (Exception e) {
					// ������¥���
					b.clear();
					b.putString("msg","¥����������");
					b.putBoolean("isshow", true);
					msg.setData(b);
					msg.what = 15;
					handler.sendMessage(msg);
				}
				if (louceng < 0 || louceng > 200) {
					b.clear();
					b.putString("msg","¥��ֻ����0~200������");
					b.putBoolean("isshow", true);
					msg.setData(b);
					msg.what = 15;
					handler.sendMessage(msg);
				}else{
					hintLouCengLayout.setVisibility(View.GONE);
				}
			} else if (v == loucengEt) {
				hintLouCengLayout.setVisibility(View.GONE);
			}
			
			if (v == roomNumberEt
					&& roomNumberEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","��ѡ�񷿼��");
				msg.setData(b);
				msg.what = 16;
				handler.sendMessage(msg);
			} else if (v == roomNumberEt) {
				hintRoomNumberLayout.setVisibility(View.GONE);
			}
			if (v == phoneEt
					&& phoneEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","������绰����");
				msg.setData(b);
				msg.what = 17;
				handler.sendMessage(msg);
			} else if (v == phoneEt&&!StringUtils
					.isCellphone(phoneEt.getText().toString().trim())) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","�ֻ������ʽ����");
				msg.setData(b);
				msg.what = 17;
				handler.sendMessage(msg);
			} else if (v == phoneEt) {
				hintPhoneLayout.setVisibility(View.GONE);
			}
			if (v == roomMoneyEt
					&& roomMoneyEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","�����뷿���");
				msg.setData(b);
				msg.what = 18;
				handler.sendMessage(msg);
			}else if(v == roomMoneyEt
					&& !roomMoneyEt.getText().toString().trim().equals("")){
				Double louceng = 0.0;
				try {
					louceng = Double.parseDouble(roomMoneyEt.getText().toString());
				} catch (Exception e) {
					// ������¥���
					b.clear();
					b.putString("msg","��������������");
					b.putBoolean("isshow", true);
					msg.setData(b);
					msg.what = 18;
					handler.sendMessage(msg);
				}
				if (louceng < 0 || louceng > 20) {
					b.clear();
					b.putString("msg","������ֻ����0~20����");
					b.putBoolean("isshow", true);
					msg.setData(b);
					msg.what = 18;
					handler.sendMessage(msg);
				}else{
					hintMoneyLayout.setVisibility(View.GONE);
				}
			} else if (v == roomMoneyEt) {
				hintMoneyLayout.setVisibility(View.GONE);
			}
			if (v == managerMoneyEt
					&& managerMoneyEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","��������ҵ�ѣ�");
				msg.setData(b);
				msg.what = 19;
				handler.sendMessage(msg);
			}else if(v == managerMoneyEt
					&& !managerMoneyEt.getText().toString().trim().equals("")){
				Double louceng = 0.0;
				try {
					louceng = Double.parseDouble(managerMoneyEt.getText().toString());
				} catch (Exception e) {
					// ������¥���
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","��ҵ����������");
					msg.setData(b);
					msg.what = 19;
					handler.sendMessage(msg);
				}
				if (louceng < 0 || louceng > 100) {
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","��ҵ��ֻ����0~100֮��");
					msg.setData(b);
					msg.what = 19;
					handler.sendMessage(msg);
				}else{
					hintWuyeLayout.setVisibility(View.GONE);
				}
			} else if (v == managerMoneyEt) {
				hintWuyeLayout.setVisibility(View.GONE);
			}
			if (v == acreageEt
					&& acreageEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","�����뷿Դ�����");
				msg.setData(b);
				msg.what = 20;
				handler.sendMessage(msg);
			}else if (v == acreageEt) {
				hintAcreageLayout.setVisibility(View.GONE);
			}
		}
	};

	/**
	 * ��ȡ�ӷ�Դ�б��ݹ���������
	 * 
	 * @author ������
	 * @createDate 2013/1/23
	 * @param intent
	 *            ���ݹ��������� ��ʹ
	 */
	private void getIntentData() {
		List<Map<String, Object>> selectresult = DBHelper.getInstance(
				ReleaseNewRoomActivity.this).selectRow(
				"select * from soso_imageinfo where xgid = '" + officeid + "'",
				null);
		if (roomImgList == null) {
			roomImgList = new ArrayList<HashMap<String, Object>>();
		}
		roomImgList.clear();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.soso_house_paijiao);
		bitmap = CommonUtils.scaleImg(bitmap, galleryFirstImgSize, galleryFirstImgSize);
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("path", "");
		map1.put("img", bitmap);
		roomImgList.add(0, map1);
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, Object> map = null;
			String name = "";
			File file = null;
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("imageid") != null
						&& !selectresult.get(i).get("imageid").toString()
								.equals("")) {
					if(selectresult.get(i).get("imageid")!=null){
						name = MD5.getMD5(selectresult.get(i).get("imageid")
								.toString()
								+ ".jpg")
								+ ".jpg";
					}
					if (selectresult.get(i).get("thumb_sdpath") != null) {
						file = new File(selectresult.get(i).get("thumb_sdpath")
								.toString());
						if (!(file.exists() && file.isFile())) {
							file = FileTools
									.getFile(getResources().getString(
													R.string.root_directory),
													getResources().getString(
															R.string.room_image), name);
						}
					}else{
						file = FileTools
								.getFile(
										getResources().getString(
												R.string.root_directory),
												getResources().getString(
														R.string.room_image), name);
					}
					map = new HashMap<String, Object>();
					if(selectresult.get(i).get("imageid")!=null){
						map.put("id", selectresult.get(i).get("imageid")
								.toString());
					}else{
						map.put("id", "");
					}
					map.put("file", file);
					map.put("pixelswidth", (MyApplication.getInstance(this).getScreenWidth()));
					map.put("pixelsheight", (MyApplication.getInstance(this).getScreenHeight() / 3));
					map.put("sql", "update soso_imageinfo set datusd='"+file.getAbsolutePath()+"' where imageid='"+selectresult.get(i).get("imageid")
							.toString()+"'");
					map.put("request_name", "ImageFileDownload.aspx");
					roomImgList.add(map);
				}
			}
		}

		List<Map<String, Object>> selectresult1 = DBHelper.getInstance(
				ReleaseNewRoomActivity.this).selectRow(
				"select * from soso_officeinfo where officeid = '" + officeid
						+ "'", null);
		if (selectresult1 != null && selectresult1.size() > 0) {
			if (selectresult1.get(selectresult1.size() - 1).get("priceup") != null) {
				roomMoneyEt.setText(selectresult1.get(selectresult1.size() - 1)
						.get("priceup").toString());
			} else {
				roomMoneyEt.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("buildmc") != null
					&&selectresult1.get(selectresult1.size() - 1).get("buildid") != null) {
				loupanSpinner.setText(selectresult1
						.get(selectresult1.size() - 1).get("buildmc")
						.toString());
				buildid = selectresult1.get(selectresult1.size() - 1).get("buildid").toString();
			} else {
				loupanSpinner.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get(
					"wymanagementfees") != null) {
				managerMoneyEt.setText(selectresult1
						.get(selectresult1.size() - 1).get("wymanagementfees")
						.toString());
			} else {
				managerMoneyEt.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("officetype") != null) {
				for (OfficeType accidentType : OfficeType.values()) {
					try {
						int officetype = Integer.parseInt(selectresult1
								.get(selectresult1.size() - 1)
								.get("officetype").toString());
						if (accidentType.getValue() == officetype) {
							typeRoomSpinner.setText(accidentType.getName());
							this.officetype = accidentType.getValue()+"";
							break;
						}
					} catch (Exception e) {
					}
				}
			} else {
				typeRoomSpinner.setText("");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("areaup") != null) {
				acreageEt.setText(selectresult1.get(selectresult1.size() - 1)
						.get("areaup").toString());
			} else {
				acreageEt.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("floors") != null) {
				loucengEt.setText(selectresult1.get(selectresult1.size() - 1)
						.get("floors").toString());
			} else {
				loucengEt.setText("0");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("officemc") != null) {
				roomNumberEt.setText(selectresult1
						.get(selectresult1.size() - 1).get("officemc")
						.toString());
			} else {
				roomNumberEt.setText("");
			}
			/*if (selectresult1.get(selectresult1.size() - 1).get("roomrate") != null) {
				ownAcreageEt.setText(selectresult1
						.get(selectresult1.size() - 1).get("roomrate")
						.toString());
			} else {
				ownAcreageEt.setText("");
			}*/
			if (selectresult1.get(selectresult1.size() - 1).get("telephone") != null) {
				phoneEt.setText(selectresult1.get(selectresult1.size() - 1)
						.get("telephone").toString());
			} else {
				phoneEt.setText("");
			}
			if (selectresult1.get(selectresult1.size() - 1).get("fyjj") != null) {
				roomIntroEt.setText(selectresult1.get(selectresult1.size() - 1)
						.get("fyjj").toString());
			} else {
				roomIntroEt.setText("");
			}

		}
		// loupanSpinner.setText(bundle.getString("loupan"));
		// // phoneEt.setText(bundle.getString("phone"));
		// roomMoneyEt.setText(bundle.getString("money"));
		title.setText("�޸ķ�Դ��Ϣ");
	}

	@Override
	public void findView() {
		gallery = (Gallery) findViewById(R.id.gallery);
		backBtn = (Button) findViewById(R.id.backBtn);
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		// roomNameEt = (EditText) findViewById(R.id.RoomName);
		loucengEt = (EditText) findViewById(R.id.loucengEt);
		loucengEt.setOnFocusChangeListener(textfocuschange);
		roomNumberEt = (EditText) findViewById(R.id.roomNumberEt);
		roomNumberEt.setOnFocusChangeListener(textfocuschange);
		roomMoneyEt = (EditText) findViewById(R.id.fangzuEt);
		roomMoneyEt.setOnFocusChangeListener(textfocuschange);
		managerMoneyEt = (EditText) findViewById(R.id.wuyeEt);
		managerMoneyEt.setOnFocusChangeListener(textfocuschange);
		acreageEt = (EditText) findViewById(R.id.acreageEt);
		acreageEt.setOnFocusChangeListener(textfocuschange);
//		ownAcreageEt = (EditText) findViewById(R.id.defanglvEt);
		roomIntroEt = (EditText) findViewById(R.id.jianjieEt);
		loupanSpinner = (Button) findViewById(R.id.loupanSpinner);
		loupanSpinner.setOnFocusChangeListener(textfocuschange);
		typeRoomSpinner = (Button) findViewById(R.id.typeSpinner);
		typeRoomSpinner.setOnFocusChangeListener(textfocuschange);
		saveAndSendBtn = (Button) findViewById(R.id.saveAndSend);
		helpBtn = (Button) findViewById(R.id.searchLoupanBtn);
		title = (TextView) findViewById(R.id.title);
		phoneEt = (EditText) findViewById(R.id.phone);
		phoneEt.setOnFocusChangeListener(textfocuschange);
		hintLoupanLayout = (LinearLayout) findViewById(R.id.loupanCheckLayout);
		hintTypeLayout = (LinearLayout) findViewById(R.id.typeCheckLayout);
		hintLouCengLayout = (LinearLayout) findViewById(R.id.loucengCheckLayout);
		hintRoomNumberLayout = (LinearLayout) findViewById(R.id.roomNumberCheckLayout);
		hintPhoneLayout = (LinearLayout) findViewById(R.id.phoneCheckLayout);
		hintMoneyLayout = (LinearLayout) findViewById(R.id.moneyCheckLayout);
		hintWuyeLayout = (LinearLayout) findViewById(R.id.wuyeCheckLayout);
		hintAcreageLayout = (LinearLayout) findViewById(R.id.acreageCheckLayout);
		hintLoupanTv = (TextView) findViewById(R.id.loupanCheck);
		hintTypeTv = (TextView) findViewById(R.id.typeCheck);
		hintLouCengTv = (TextView) findViewById(R.id.loucengCheck);
		hintRoomNumberTv = (TextView) findViewById(R.id.roomNumberCheck);
		hintPhoneTv = (TextView) findViewById(R.id.phoneCheck);
		hintMoneyTv = (TextView) findViewById(R.id.moneyCheck);
		hintWuyeTv = (TextView) findViewById(R.id.wuyeCheck);
		hintAcreageTv = (TextView) findViewById(R.id.acreageCheck);
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {// ���ذ�ť
			CommonUtils.hideSoftKeyboard(this);
			if(activityFlag == 1){//0����ӷ�Դ�б���������  1����ӷ�Դ��������ת����
				finish();
			}else if(activityFlag == 0){
				MyApplication.getInstance().setRoomListBack(true);
				finish();
			}
		} else if (v == takePictureBtn) {// ���ջ�ȡͼƬ
			dismiss();
			clearEditTextFocus();// ȥ�������ı���Ľ��㣬ȷ����ȡͼƬ����֮��������ܱ������ܿ�����Ƭ�ĵط�
			Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			fileName = CommonUtils.getFileName();
			MyApplication.getInstance(this).setFileName(fileName);
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if (!file.exists()) {
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD,
					fileName);
			path = sdcardTempFile.getAbsolutePath();
			if (CommonUtils.isHasSdcard()) {
				openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
				openCamera.putExtra("return-data", true);
			}
			startActivityForResult(openCamera, REQUEST_CODE_TAKE_PICTURE);
		} else if (v == getAlbumBtn) {// ���ֻ���ȡͼƬ
			dismiss();
			clearEditTextFocus();// ȥ�������ı���Ľ��㣬ȷ����ȡͼƬ����֮��������ܱ������ܿ�����Ƭ�ĵط�
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_CODE);
		} else if (v == cancleBtn) {// ȡ����ȡͼƬ��ť
			dismiss();
		} else if (v == typeRoomSpinner) {// ѡ��Դ����
			CommonUtils.hideSoftKeyboard(this);
			btn_tag = 2;
			popupWindwShowing(3);
		} else if (v == loupanSpinner) {// ѡ������¥��
			CommonUtils.hideSoftKeyboard(this);
			btn_tag = 1;
			popupWindwShowing(2);
		} else if (v == helpBtn) {// ������
			CommonUtils.hideSoftKeyboard(this);
			Intent intent = new Intent(this,EmployHelpActivity.class);
			startActivity(intent);
		} else if (v == saveAndSendBtn) {// ���沢����
			if (textValidate()) {
				new Thread(runnable).start();
			}
		} else if (v == dialogBackBtn) {// ����ѡ�񵯳���ķ��ذ�ť
			if (data_type == 1) {// ��������
				initDiQuList1(cityid);
			} else if (data_type == 2) {// ��Ȧ����
				initShangQuanList1(areaid);
			}
		} else if (v == dialogCloseBtn) {// ¥��ѡ�񵯳���Ĺرհ�ť
			Message msg = new Message();
			Bundle b = new Bundle();
			if (btn_tag == 1
					&& loupanSpinner.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","��ѡ��¥��");
				msg.setData(b);
				msg.what = 13;
				handler.sendMessage(msg);
			} else if (btn_tag == 1) {
				hintLoupanLayout.setVisibility(View.GONE);
			}
			if (btn_tag == 2
					&& typeRoomSpinner.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","��ѡ��¥������");
				msg.setData(b);
				msg.what = 14;
				handler.sendMessage(msg);
			} else if (btn_tag == 2) {
				hintTypeLayout.setVisibility(View.GONE);
			}
			dismiss();
		}
		super.onClick(v);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0)
			return;
		if (resultCode != RESULT_OK) { // �˴��� RESULT_OK ��ϵͳ�Զ����һ������
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}
		Bitmap bm = null;
		if (requestCode == IMAGE_CODE) {// ѡ��ͼƬ����
			String imgPath = "";
			Uri originalUri = data.getData(); // ���ͼƬ��uri
			String[] proj = { MediaStore.Images.Media.DATA };
			// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
			Cursor cursor = managedQuery(originalUri, proj, null, null, null);
			// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
			if (cursor == null) {
				imgPath = data.getDataString();
			} else {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
				cursor.moveToFirst();
				// ����������ֵ��ȡͼƬ·��www.2cto.com
				imgPath = cursor.getString(column_index);
			}

			File fromFile = new File(imgPath);
//			if(fromFile.getParentFile().getName() .contains("thumbnail")){
//				Toast.makeText(this, "����ѡ������ͼ��", 3000).show();
//				return;
//			}
			// ���ļ����Ƶ��Զ����ļ�
			if (fromFile != null && fromFile.exists()) {
				fileName = CommonUtils.getFileName();// ��ͼƬ���µ�����
				File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
				if (!file.exists()) {
					file.mkdirs();
				}
				sdcardTempFile = new File(SDCARD_ROOT_PATH
						+ SAVE_PATH_IN_SDCARD, fileName);
//				path = sdcardTempFile.getAbsolutePath();
//				FileCopyUtil.copyfile(fromFile, sdcardTempFile, true);
				Bitmap bitmap = CommonUtils.getDrawable(fromFile.getAbsolutePath(), 640, 480);
				bitmap = CommonUtils.RotateImg(fromFile.getAbsolutePath(),bitmap);//������ͼƬ��ת90��
				if(bitmap!=null){
					boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
					if (copy) {
						/*File oldFile = new File(path);
						oldFile.delete();*/
						path = sdcardTempFile.getAbsolutePath();
					}
				}else{
					Toast.makeText(this, "ͼƬ������", 3000).show();
				}
			}
			bm = CommonUtils.getDrawable(path);
			if (bm != null) {// ͼƬ����
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("path", path);
				map.put("img", bm);
				roomImgList.add(map);
				notifiView();// ͼƬ������ϣ�ˢ�²���
			} else {// ͼƬ������
				Toast.makeText(this, "ͼƬ������", 3000).show();
			}
		} else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {// ���շ���
			fileName = CommonUtils.getFileName();// ��ͼƬ���µ�����
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if (!file.exists()) {
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD,
					fileName);
			if (CommonUtils.isHasSdcard()) {// �洢������
				// path = sdcardTempFile.getAbsolutePath();
				
				Bitmap bitmap = CommonUtils.getDrawable(path, 640, 480);
				bitmap = CommonUtils.RotateImg(path,bitmap);//������ͼƬ��ת90��
				boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
				if (copy) {
					File oldFile = new File(path);
					oldFile.delete();
					path = sdcardTempFile.getAbsolutePath();
				}
				bm = CommonUtils.getDrawable(path);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("path", path);
				map.put("img", bm);
				roomImgList.add(map);// ��ͼƬ�����б�
			} else { // �洢��������ֱ�ӷ�������ͼ
				Toast.makeText(this, "û��SD��", 3000).show();
				// Bundle extras = data.getExtras();
				// bm = (Bitmap) extras.get("data");
				// roomImgList.add(bm);
				// boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bm);
				// if (copy) {
				// path = sdcardTempFile.getAbsolutePath();
				// }
			}
			notifiView();// ͼƬ������ϣ�ˢ�²���
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(activityFlag == 1){//0����ӷ�Դ�б���������  1����ӷ�Դ��������ת����
				finish();
			}else if(activityFlag == 0){
				MyApplication.getInstance().setRoomListBack(true);
				finish();
			}
		}
		return false;
	}

	/**
	 * �����ڻ�ȡ��Ƭ����֮���ù�������λ����ͣ���������棬�����û�������һЩ��������ȥ�ֶ�����������ȥ��ȡ�����ͼƬ
	 * ��ȷ���˹�����֮���������ܻ�ȡ����
	 * 
	 * @createDate 2013/1/22
	 * @author ������
	 * @param focus
	 *            true�����ܻ�ȡ���㣬false����ʧȥ����
	 */
	private void clearEditTextFocus() {
		// roomNameEt.clearFocus();
		loupanSpinner.clearFocus();
		typeRoomSpinner.clearFocus();
		loucengEt.clearFocus();
		roomNumberEt.clearFocus();
		roomMoneyEt.clearFocus();
		managerMoneyEt.clearFocus();
		acreageEt.clearFocus();
//		ownAcreageEt.clearFocus();
		roomIntroEt.clearFocus();
	}

	@Override
	public void initView() {
		backBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);
		saveAndSendBtn.setOnClickListener(this);
		loupanSpinner.setOnClickListener(this);
		typeRoomSpinner.setOnClickListener(this);
	}

	@Override
	public void initData() {
		galleryFirstImgSize = Integer.parseInt(getString(R.string.gallery_first_img));
		gallerySize = Integer.parseInt(getString(R.string.gallery_size));
		SAVE_PATH_IN_SDCARD = "/"
				+ getResources().getString(R.string.root_directory) + "/"
				+ getResources().getString(R.string.room_image) + "/";
		roomImgList = new ArrayList<HashMap<String, Object>>();
		cityid = MyApplication.getInstance().getCityid();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.soso_house_paijiao);
		bitmap = CommonUtils.scaleImg(bitmap, galleryFirstImgSize, galleryFirstImgSize);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("path", "");
		map.put("img", bitmap);
		roomImgList.add(0, map);
		Intent intent = getIntent();// ���������ഫ�ݹ���������
		if (intent.getStringExtra("officeid") != null) {
			officeid = getIntent().getStringExtra("officeid");
			if(MyApplication.getInstance().getRoom_state_for_examine()==1){//1�������ͨ���ķ�Դ  2�������˷�Դ
				intenttag=2;
			}else{
				intenttag=3;
			}
			new Thread(runnable2).start();
		} else {
			intenttag=1;
			notifiView();
		}
		initEdit();
		if(getIntent().getStringExtra("buildid")!=null
				&&!getIntent().getStringExtra("buildid").toString().equals("")
				&&getIntent().getStringExtra("buildmc")!=null
				&&!getIntent().getStringExtra("buildmc").toString().equals("")){
			buildid = getIntent().getStringExtra("buildid").toString();
			loupanSpinner.setText(getIntent().getStringExtra("buildmc").toString());
		}
		if(intenttag==1){
			title.setText("�����·�Դ");
		}else{
			title.setText("�޸ķ�Դ��Ϣ");
		}
	}
	private void initEdit(){
		if(intenttag == 1 || intenttag == 3){
			
		}else if(intenttag == 2){
			loupanSpinner.setClickable(false);// ����¥��
			loupanSpinner.setTextColor(Color.GRAY);
			loupanSpinner.setBackgroundResource(R.drawable.soso_input_hui);
			typeRoomSpinner.setClickable(false);// ��Դ����
			typeRoomSpinner.setTextColor(Color.GRAY);
			typeRoomSpinner.setBackgroundResource(R.drawable.soso_input_hui);
			loucengEt.setClickable(false);// ¥��
			loucengEt.setTextColor(Color.GRAY);
			loucengEt.setFocusable(false);
			loucengEt.setBackgroundResource(R.drawable.soso_input_hui);
			roomNumberEt.setFocusable(false);// �����
			roomNumberEt.setTextColor(Color.GRAY);
			roomNumberEt.setBackgroundResource(R.drawable.soso_input_hui);
			acreageEt.setTextColor(Color.GRAY);
			acreageEt.setFocusable(false);
			acreageEt.setClickable(false);
			acreageEt.setBackgroundResource(R.drawable.soso_input_hui);
//			ownAcreageEt.setFocusable(false);// �÷���
//			roomIntroEt.setFocusable(false);// ��Դ���
//			saveAndSendBtn.setClickable(false);// ���沢������Դ�İ�ť
//			helpBtn.setClickable(false);
		}
	}
	/**
	 * ��ʼ����������
	 * 
	 * @createDate 2013/1/22
	 * @author ������
	 */
	public void initDiQuList() {
		// loupanList.clear();
		// loupanList.add("�����");
		// loupanList.add("�ֶ�����");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("�����");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("¬����");
		// notifyLoupanSpinner();
		// dialogTitle.setText("��������");
		// dialogBackBtn.setVisibility(View.GONE);
		// data_type = 0;
	}

	/**
	 * ��ʼ����������
	 * 
	 * @createDate 2013/1/24
	 * @author Ring
	 */
	public void initDiQuList1(String cityid) {
		if (loupanList == null) {
			loupanList = new ArrayList<HashMap<String, String>>();
		}
		loupanList.clear();
		String sql = "select distinct region.id,region.name,region.type,region.parentid,region.pinyin " +
				"from district left join region on region.id = district.regionid where region.type=3 " +
				"and region.parentid='" + cityid
				+ "'";
		List<Map<String, Object>> selectresult = null;
		selectresult = ZoneUtil.getregion(this, sql);
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, String> map = null;
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("id") != null
						&& selectresult.get(i).get("name") != null) {
					map = new HashMap<String, String>();
					map.put("id", selectresult.get(i).get("id").toString());
					map.put("name", selectresult.get(i).get("name").toString());
					loupanList.add(map);
				}
			}
		}
		// loupanList.add("�����");
		// loupanList.add("�ֶ�����");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("�����");
		// loupanList.add("������");
		// loupanList.add("������");
		// loupanList.add("¬����");
		notifyLoupanSpinner(true);
		dialogTitle.setText("��������");
		dialogBackBtn.setVisibility(View.GONE);
		data_type = 0;
	}

	/**
	 * ��ʼ����������
	 * 
	 * @createDate 2013/1/22
	 * @author ������
	 */
	public void initShangQuanList() {
		// loupanList.clear();
		// loupanList.add("�����");
		// loupanList.add("��ɽ·/�ؼ��");
		// loupanList.add("����");
		// loupanList.add("����");
		// loupanList.add("�����");
		// loupanList.add("��һ�");
		// loupanList.add("½����");
		notifyLoupanSpinner(true);
		dialogTitle.setText("������Ȧ");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 1;
	}

	/**
	 * ��ʼ����Ȧ����
	 * 
	 * @createDate 2013/1/24
	 * @author Ring
	 */
	public void initShangQuanList1(String areaid) {
		if (loupanList == null) {
			loupanList = new ArrayList<HashMap<String, String>>();
		}
		loupanList.clear();
		String sql = "select * from district where regionid='" + areaid + "'";
		List<Map<String, Object>> selectresult = null;
		selectresult = ZoneUtil.getregion(this, sql);
		if (selectresult != null && selectresult.size() > 0) {
			int i;
			HashMap<String, String> map = null;
			for (i = 0; i < selectresult.size(); i++) {
				if (selectresult.get(i).get("id") != null
						&& selectresult.get(i).get("name") != null) {
					map = new HashMap<String, String>();
					map.put("id", selectresult.get(i).get("id").toString());
					map.put("name", selectresult.get(i).get("name").toString());
					loupanList.add(map);
				}
			}
		}
		// loupanList.add("�����");
		// loupanList.add("��ɽ·/�ؼ��");
		// loupanList.add("����");
		// loupanList.add("����");
		// loupanList.add("�����");
		// loupanList.add("��һ�");
		// loupanList.add("½����");
		notifyLoupanSpinner(true);
		dialogTitle.setText("������Ȧ");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 1;
	}

	/**
	 * ��ʼ����������
	 * 
	 * @createDate 2013/1/22
	 * @author ������
	 */
	public void initLouPanList() {
		loupanList.clear();
		// loupanList.add("���Ǵ���");
		// loupanList.add("���ش���");
		// loupanList.add("��ï����");
		// loupanList.add("��������");
		// loupanList.add("��ˮ��");
		// loupanList.add("�����԰");
		// loupanList.add("½�����԰");
		notifyLoupanSpinner(false);
		dialogTitle.setText("����¥��");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 2;
	}

	/**
	 * ��ʼ��¥������
	 * 
	 * @createDate 2013/1/24
	 * @author Ring
	 */
	public void initLouPanList1(String districtid) {
		loupanList.clear();
		SoSoBuildInfo sosoBuildInfo = new SoSoBuildInfo();
		HashMap<String, String> map = null;
		String sql = "select * from soso_buildinfo where districtid='"
				+ districtid + "' group by buildid";
		LinkedList<SoSoBuildInfo> sosoBuildInfos = sosoBuildInfo
				.SelectBuildInfo(this, sql);
		if (sosoBuildInfos != null) {
			for (Iterator<SoSoBuildInfo> iterator = sosoBuildInfos.iterator(); iterator
					.hasNext();) {
				sosoBuildInfo = (SoSoBuildInfo) iterator.next();
				if (sosoBuildInfo.getBuildID() != null
						&& !sosoBuildInfo.getBuildID().equals("")
						&& sosoBuildInfo.getBuildMC() != null
						&& !sosoBuildInfo.getBuildMC().equals("")) {
					map = new HashMap<String, String>();
					map.put("id", sosoBuildInfo.getBuildID());
					map.put("name", sosoBuildInfo.getBuildMC());
					loupanList.add(map);
				}
			}
		}

		// loupanList.add("���Ǵ���");
		// loupanList.add("���ش���");
		// loupanList.add("��ï����");
		// loupanList.add("��������");
		// loupanList.add("��ˮ��");
		// loupanList.add("�����԰");
		// loupanList.add("½�����԰");
		notifyLoupanSpinner(false);
		dialogTitle.setText("����¥��");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 2;
		
	}

	/**
	 * ��ʼ����Դ����
	 * 
	 * @author ������
	 * @createDate 2013.1.22
	 */
	public void initTypeRoomList() {
		if (loupanList == null) {
			loupanList = new ArrayList<HashMap<String, String>>();
		}
		loupanList.clear();
		HashMap<String, String> map = null;
		for (OfficeType accidentType : OfficeType.values()) {
			map = new HashMap<String, String>();
			map.put("id", accidentType.getValue() + "");
			map.put("name", accidentType.getName());
			loupanList.add(map);
		}
		notifyTypeRoomSpinner();
		dialogTitle.setText("��Դ����");
		dialogBackBtn.setVisibility(View.GONE);
		data_type = 3;

	}

	/**
	 * ˢ��¥���б�
	 * 
	 * @author ������
	 * @createDate 2013/1/22
	 * @param display �Ƿ���ʾ��ͷͼƬ
	 */
	public void notifyLoupanSpinner(boolean display) {
		LoupanListAdapter adapter = new LoupanListAdapter(this, loupanList,display);
		dialogListView.setAdapter(adapter);
		dialogListView
				.setOnItemClickListener(new dialogListItemClickListener());
		if(loupanList.size() == 0){
			noText.setVisibility(View.VISIBLE);
		}else{
			noText.setVisibility(View.GONE);
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
			if (NetworkCheck.IsHaveInternet(ReleaseNewRoomActivity.this)) {
				handler.sendEmptyMessage(5);// ����������
				getbuilds = new GetBuilds(ReleaseNewRoomActivity.this);
				getbuilds.getBuildsBydistrictid(districtid);
				handler.sendEmptyMessage(6);// �رս�����
				handler.sendEmptyMessage(10);// ��¼�ɹ���
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
			} else {
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}
			click_limit = true;
		}
	};

	/**
	 * ˢ�·�Դ�����б�
	 * 
	 * @author ������
	 * @createDate 2013/1/22
	 */
	public void notifyTypeRoomSpinner() {
		LoupanListAdapter adapter = new LoupanListAdapter(this, loupanList,false);
		dialogListView.setAdapter(adapter);
		dialogListView
				.setOnItemClickListener(new dialogListItemClickListener());
	}

	@Override
	public void notifiView() {
		gallery.setAdapter(new GalleryAdapter());
//		if (roomImgList.size() > 0) {
//			gallery.setSelection(roomImgList.size() - 1, true);
//		}
		gallery.setOnItemClickListener(this);
	}

	/**
	 * 
	 * @author ������
	 * @createDate 2013.1.22
	 * 
	 */
	class dialogListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Message msg = new Message();
			Bundle b = new Bundle();
			if (data_type == 0) {// 0�������
				areaid = loupanList.get(arg2).get("id").toString();
				initShangQuanList1(areaid);
			} else if (data_type == 1) {// 1������Ȧ 
				districtid = loupanList.get(arg2).get("id").toString();
				new Thread(runnable1).start();
			} else if (data_type == 2) {//2����¥�� 
				buildid = loupanList.get(arg2).get("id").toString();
				loupanSpinner.setText(loupanList.get(arg2).get("name")
						.toString());
				if (btn_tag == 1
						&& loupanSpinner.getText().toString().trim().equals("")) {
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","��ѡ��¥��");
					msg.setData(b);
					msg.what = 13;
					handler.sendMessage(msg);
				} else if (btn_tag == 1) {
					hintLoupanLayout.setVisibility(View.GONE);
				}
				dismiss();
			} else if (data_type == 3) {//3����Դ����
				officetype = loupanList.get(arg2).get("id").toString();
				typeRoomSpinner.setText(loupanList.get(arg2).get("name")
						.toString());
				if (btn_tag == 2
						&& typeRoomSpinner.getText().toString().trim().equals("")) {
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","��ѡ��¥������");
					msg.setData(b);
					msg.what = 14;
					handler.sendMessage(msg);
				} else if (btn_tag == 2) {
					hintTypeLayout.setVisibility(View.GONE);
				}
				dismiss();
			}
			
		}

	}

	/**
	 * ��ʾPopupWindow����
	 * 
	 * @author ������
	 * @param type
	 *            1����ײ��Ļ�ȡ��Ƭ 2�����ȡ¥�̵Ĵ��� 3�����ȡ��Դ���͵Ĵ���
	 */
	public void popupWindwShowing(int type) {
		int height = MyApplication.getInstance(this).getScreenHeight();
		if (height == 1280) {
			chazhi = 800;
		} else if (height == 800) {
			chazhi = 440;
		} else if (height == 960) {
			chazhi = 600;
		} else if (height == 854) {
			chazhi = 492;
		} else if (height == 480) {
			chazhi = 240;
		}
		if (type == 1) {// �ײ��Ļ�ȡ��Ƭ�ĵ�����
			initPopuWindow();
		} else if (type == 2) {// ��ȡ¥�̵ĵ�����
			initDialogWindow();
			initDiQuList1(cityid);// ��ʼ��¥������
		} else if (type == 3) {// ��ȡ��Դ���͵ĵ�����
			initDialogWindow();
			initTypeRoomList();// ��ʼ����Դ��������
		}

	}

	/**
	 * ��ʼ��¥�̴��ڡ�
	 * 
	 * @author ������
	 * @createDate 2013.1.22
	 * 
	 */
	private void initDialogWindow() {
		int width = 0;
		int height = 0;
		LinearLayout loupanwindow = (LinearLayout) this.getLayoutInflater()
				.inflate(R.layout.dialog_select_loupan, null);
		loupanwindow.getBackground().setAlpha(220);
		dialogListView = (ListView) loupanwindow.findViewById(R.id.listView);
		dialogTitle = (TextView) loupanwindow.findViewById(R.id.textView);
		dialogCloseBtn = (Button) loupanwindow.findViewById(R.id.closeBtn);
		dialogBackBtn = (Button) loupanwindow.findViewById(R.id.dialogBackBtn);
		noText = (TextView) loupanwindow.findViewById(R.id.noText);
		dialogCloseBtn.setOnClickListener(this);
		dialogBackBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		width = (int) (parent.getWidth() * 0.8f);
		height = width + 100;
		selectPopupWindow = new PopupWindow(loupanwindow, width, height, true);
		// ��һ����Ϊ��ʵ�ֵ���PopupWindow�󣬵������Ļ�������ּ�Back��ʱPopupWindow����ʧ��
		// û����һ����Ч�����ܳ�������������Ӱ�챳��
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.getBackground().setAlpha(220);
		selectPopupWindow.setOutsideTouchable(false);
		selectPopupWindow.showAsDropDown(parent,
				(parent.getWidth() - width) / 2,
				-(parent.getHeight() - (parent.getHeight() - height) / 2));
		
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				dismiss();
			}
		});
	}

	/**
	 * @author ������
	 * @createDate 2013/1/23 ��ʼ����ȡͼƬ��PopupWindow����
	 */
	private void initPopuWindow() {
		// PopupWindow���������򲼾�
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.dialog_share, null);
		takePictureBtn = (Button) loginwindow.findViewById(R.id.eshareBtn1);
		getAlbumBtn = (Button) loginwindow.findViewById(R.id.eshareBtn2);
		cancleBtn = (Button) loginwindow.findViewById(R.id.eshareCancleBtn);
		takePictureBtn.setText("����");
		getAlbumBtn.setText("�û����");
		cancleBtn.setText("ȡ��");
		takePictureBtn.setOnClickListener(this);
		getAlbumBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		selectPopupWindow = new PopupWindow(loginwindow, parent.getWidth(),
				LayoutParams.WRAP_CONTENT, true);
		// ��һ����Ϊ��ʵ�ֵ���PopupWindow�󣬵������Ļ�������ּ�Back��ʱPopupWindow����ʧ��
		// û����һ����Ч�����ܳ�������������Ӱ�챳��
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(false);
		
		selectPopupWindow.showAsDropDown(parent, 0,
				-(parent.getHeight() - chazhi));
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				dismiss();
			}
		});
	}

	/**
	 * PopupWindow��ʧ
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	/**
	 * 
	 * @author ������
	 * @createDate 2013/1/23 ��ȡͼƬ����������
	 * 
	 */
	class GalleryAdapter extends BaseAdapter {
//		private ImageDownloaderId imageDownloader = null;

		public GalleryAdapter() {
//			imageDownloader = new ImageDownloaderId(ReleaseNewRoomActivity.this,
//					10);
		}

		public int getCount() {
			return roomImgList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ReleaseNewRoomActivity.this)
						.inflate(R.layout.item_gallery, null);
				holder.galleryImage = (ImageView) convertView
						.findViewById(R.id.galleryImage);
				holder.galleryCancleImage = (ImageView) convertView
						.findViewById(R.id.galleryCancleImage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			convertView.setLayoutParams(new Gallery.LayoutParams(gallerySize, gallerySize));
			holder.setContent(position);
			return convertView;
		}

		class ViewHolder {
			ImageView galleryImage;
			ImageView galleryCancleImage;

			public void setContent(final int position) {
				if(position!=0){
					galleryImage.setBackgroundResource(R.drawable.room_list_img_bg);
				}
				if (roomImgList.get(position).get("img") != null) {
					Bitmap bm = (Bitmap) roomImgList.get(position).get("img");
					if(position == 0){
						galleryImage.setBackgroundResource(R.drawable.soso_house_paijiao);
					}else{
						galleryImage.setImageBitmap(bm);
					}
//					galleryImage.setBackgroundDrawable(new BitmapDrawable(bm));
					
				} else {
					boolean b = true;
					File file = (File) roomImgList.get(position).get("file");
					if(file.exists()&&file.isFile()){
						Bitmap bitmap = BitmapCache.getInstance().getBitmap(file, ReleaseNewRoomActivity.this);
						if(bitmap!=null){
//							bitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth()-bitmap.getHeight()/3)/2, 
//									bitmap.getHeight()/3, bitmap.getHeight()/3, bitmap.getHeight()/3);
							bitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth()-gallerySize)/2, 
									(bitmap.getHeight()-gallerySize)/2, gallerySize, gallerySize);
							galleryImage.setImageBitmap(bitmap);
							b=false;
						}
					}
					if(b){
						galleryImage.setImageResource(R.drawable.soso_gray_logo);
						MyApplication.getInstance(ReleaseNewRoomActivity.this).getImageDownloaderId().download((File) roomImgList.get(position).get("file"), roomImgList
								.get(position).get("sql").toString(),
								roomImgList.get(position).get("id").toString(), roomImgList.get(position)
										.get("pixelswidth").toString(),
										roomImgList.get(position).get("pixelsheight").toString(),
										roomImgList.get(position).get("request_name").toString(),
										galleryImage,(Integer.parseInt(roomImgList.get(position).get("pixelswidth").toString())/2-gallerySize/2)+""
										, (Integer.parseInt(roomImgList.get(position).get("pixelsheight").toString())/2-gallerySize/2)+"",""+gallerySize,""+gallerySize);
					}
				}
				if(intenttag ==2){
					galleryCancleImage.setVisibility(View.GONE);
				}else{
					if (position == 0) {
						galleryCancleImage.setVisibility(View.GONE);
					} else {
						galleryCancleImage.setVisibility(View.VISIBLE);
					}
				}
				
				galleryCancleImage
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								AlertDialog.Builder dialog = new AlertDialog.Builder(
										ReleaseNewRoomActivity.this);
								dialog.setTitle("��ʾ��");
								dialog.setIcon(android.R.drawable.ic_dialog_alert);
								dialog.setMessage("ȷ��Ҫɾ����ͼƬ��");
								dialog.setPositiveButton("ȷ��",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												if (roomImgList.get(position)
														.get("path") != null) {
													boolean deleteFile = false;
													File file = new File(
															roomImgList
																	.get(position)
																	.get("path")
																	.toString());
													if (file.exists()) {// ɾ��ͼƬ
														deleteFile = file
																.delete();
													}
													if (deleteFile) {// ���б��еĶ�Ӧ���������
														roomImgList
																.remove(position);
														notifiView();
													} else {// ɾ�����ɹ�
														Toast.makeText(
																ReleaseNewRoomActivity.this,
																"ͼƬɾ��ʧ�ܣ�", 3000)
																.show();
													}
												}else if(roomImgList.get(position)
														.get("file") != null){
													deleteimages.append(roomImgList.get(position)
															.get("id").toString()+",");
													roomImgList
													.remove(position);
													notifiView();
												}

											}
										});
								dialog.setNegativeButton("ȡ��",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												return;
											}
										});
								dialog.create().show();
							}
						});
				System.gc();
			}
		}
	}

	/**
	 * ��ʾ��������߳�
	 * 
	 * @author ������
	 * @createDate 2013/2/19
	 */
	Runnable runnableForShowDialog = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(200);
				handler.sendEmptyMessage(11);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	/**
	 * ͼƬ�ؼ��ĵ���¼�
	 * 
	 * @createDate 2013.1.23
	 * @author ������
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 0) {// �ؼ��еĵ�һ��ͼƬ��������ȡͼƬ��
			if(intenttag == 1 || intenttag == 3){//ֻ���ڷ�����Դ��ʱ��ſ���
				if (roomImgList.size() == Constant.GALLERY_IMAGE_MAX_IMAGE_NUMBER) {// �������ͼƬ����Ϊ10��
					Toast.makeText(ReleaseNewRoomActivity.this, "���ֻ�������ϴ�10�ŷ�ԴͼƬ��",
							3000).show();
				} else {// �����Ի���ѡ���ȡͼƬ��ʽ
					CommonUtils.hideSoftKeyboard(this);// ���������
					arg1.requestFocus();
					// ��������̵߳ķ�ʽ����Ϊ����������̵�ͬʱҲ�ѶԻ���������ˣ���˲����̵߳ķ�ʽ
					// ���������ʧ֮���ٵ����Ի���
					new Thread(runnableForShowDialog).start();
				}
			}else{
				Toast.makeText(ReleaseNewRoomActivity.this, "��Դ�����ͨ�����������޸ģ�",
						3000).show();
			}
		} else {// ���˵�һ�������ͼƬ��������Խ���Ԥ��ҳ
			if (roomImgList.get(arg2).get("path") != null) {
				File file = new File(roomImgList.get(arg2).get("path")
						.toString());
				if (file.exists()) {// �ж�ͼƬ�Ƿ����
//					Intent intent = new Intent(ReleaseNewRoomActivity.this,
//							ImagePreViewActivity.class);
//					intent.putExtra("path", file.getAbsolutePath());
//					startActivityForResult(intent, 6);
					
					ArrayList<String> al = new ArrayList<String>();
					al.clear();
					al.add(file.getAbsolutePath());
					Intent it = new Intent(ReleaseNewRoomActivity.this,
							ImageSwitcher.class);
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					it.putStringArrayListExtra("pathes", al);
					it.putExtra("index", 0);
					startActivity(it);
				} else {// ͼƬ������ ��ɾ���б��е�ͼƬ ��ˢ�¿ؼ�
					Toast.makeText(ReleaseNewRoomActivity.this, "ͼƬ�����ڣ�", 5000)
							.show();
					roomImgList.remove(arg2);
					notifiView();
				}
			} else if (roomImgList.get(arg2).get("file") != null) {
				File file = (File) roomImgList.get(arg2).get("file");
				if (file.exists()) {// �ж�ͼƬ�Ƿ����
//					Intent intent = new Intent(ReleaseNewRoomActivity.this,
//							ImagePreViewActivity.class);
//					intent.putExtra("path", file.getAbsolutePath());
//					startActivityForResult(intent, 6);
					
					ArrayList<String> al = new ArrayList<String>();
					al.clear();
					al.add(file.getAbsolutePath());
					Intent it = new Intent(ReleaseNewRoomActivity.this,
							ImageSwitcher.class);
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					it.putStringArrayListExtra("pathes", al);
					it.putExtra("index", 0);
					startActivity(it);
				} else {// ͼƬ������ ��ɾ���б��е�ͼƬ ��ˢ�¿ؼ�
					Toast.makeText(ReleaseNewRoomActivity.this, "ͼƬ�����ڣ�", 5000)
							.show();
//					roomImgList.remove(arg2);
//					notifiView();
				}
			} else {
				// ͼƬ������ ��ɾ���б��е�ͼƬ ��ˢ�¿ؼ�
				Toast.makeText(ReleaseNewRoomActivity.this, "ͼƬ�����ڣ�", 5000)
						.show();
				roomImgList.remove(arg2);
				notifiView();
			}
		}
	}
	
	/**
	 * author by Ring �����ʱ����
	 */
	public Runnable runnable2= new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(ReleaseNewRoomActivity.this)) {
				handler.sendEmptyMessage(5);// ����������
				if (intenttag == 2||intenttag == 3) {
					getOfficeDetail();
					getofficeimages();
					handler.sendEmptyMessage(6);// �رս�����
					handler.sendEmptyMessage(12);// ��ʼ������
				} 
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
				}
			} else {
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}
			click_limit = true;
		}
	};

	/**
	 * author by Ring �����ʱ����
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(ReleaseNewRoomActivity.this)) {
				handler.sendEmptyMessage(5);// ����������
				if (intenttag == 1) {
					boolean b = false;// ��ӷ�Դ��Ϣ�Ƿ�ɹ��ı�ʶ
					boolean b1 = false;// ��ӷ�ԴͼƬ�Ƿ�ɹ���ʶ
					b = addOfficeInfo();
					if (b) {
						b1 = addOfficePictrues();
					}
					handler.sendEmptyMessage(6);// �رս�����
					if (b) {
						if (b1) {
							handler.sendEmptyMessage(7);// ��ӷ�Դ�ɹ�
						} else {
							handler.sendEmptyMessage(8);// ��ӷ�Դ��Ϣ�ɹ�����ӷ�ԴͼƬʧ��
						}

					} else {
						handler.sendEmptyMessage(9);// ��ӷ�Դʧ��
					}
				} else if (intenttag == 2||intenttag == 3){
					boolean b = false;// ��ӷ�Դ��Ϣ�Ƿ�ɹ��ı�ʶ
					boolean b1 = false;// ��ӷ�ԴͼƬ�Ƿ�ɹ���ʶ
					boolean b2 = false;//ɾ��ͼƬ�Ƿ�ɹ���ʶ
					b = addOfficeInfo();
					if (b) {
						b1 = addOfficePictrues();
					}
//					if (b) {
						b2 = deleteOfficePictrues();
//					}
					handler.sendEmptyMessage(6);// �رս�����
					if (b) {
						if (b1) {
							handler.sendEmptyMessage(7);// ��ӷ�Դ�ɹ�
						} else {
							handler.sendEmptyMessage(8);// ��ӷ�Դ��Ϣ�ɹ�����ӷ�ԴͼƬʧ��
						}

					} else {
						handler.sendEmptyMessage(9);// ��ӷ�Դʧ��
					}
				}
				if (runnable_tag) {
					runnable_tag = false;
					click_limit = true;
					return;
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
			Bundle b = null;
			switch (msg.what) {
			case 1://ˢ��¥���б�
				initLouPanList1(districtid);
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
				MessageBox.CreateAlertDialog(errormsg,
						ReleaseNewRoomActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(ReleaseNewRoomActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(ReleaseNewRoomActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
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
			case 7:// ��ӷ�Դ�ɹ�
				String promptmsg = "";
				if(intenttag==1){
					promptmsg = "��ϲ������ӷ�Դ�ɹ���";
				}else{
					promptmsg = "�޸ķ�Դ�ɹ���";
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseNewRoomActivity.this);
				builder.setMessage(promptmsg)
						.setTitle(getResources().getString(R.string.prompt))
						.setCancelable(false)
						.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								/*if(activityFlag == 1){
									finish();
								}else if(activityFlag == 0){
									MyApplication.getInstance().setRoomListBack(true);
									finish();
								}*/
								CommonUtils.hideSoftKeyboard(ReleaseNewRoomActivity.this);
								MyApplication.getInstance().setBuildid(buildid);
								//0�����¥���б��н�����ӷ�Դ 1����ӷ�Դ������������ӷ�Դ����
								if(MyApplication.getInstance().getAddRoomFlag() == 1){
									if(intenttag !=2){
										MyApplication.getInstance().setRoomManagerFlag(1);
									}else{
										MyApplication.getInstance().setRoomManagerFlag(3);
									}
								}else{
									MyApplication.getInstance().setRoomManagerFlag(1);
									Intent intent = new Intent(ReleaseNewRoomActivity.this,RoomManagerActivity.class);
									intent.putExtra("tag", 2);
									MyApplication.getInstance().setRoomListBack(true);
									startActivity(intent);
								}
								finish();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				break;
			case 8:// ��ӷ�Դ��Ϣ�ɹ�����ӷ�ԴͼƬʧ��
				MessageBox.CreateAlertDialog("���ַ�ԴͼƬδ�ϴ��ɹ�����������������Դ��ť��",
						ReleaseNewRoomActivity.this);
				break;
			case 9:// ��ӷ�Դʧ��
				String promptmsg1 = "";
				if(intenttag==1){
					promptmsg1 = "��ӷ�Դʧ�ܣ������Ժ����ԣ�";
				}else{
					promptmsg1 = "�޸ķ�Դʧ�ܣ������Ժ����ԣ�";
				}
				MessageBox.CreateAlertDialog(promptmsg1,
						ReleaseNewRoomActivity.this);
				break;
			case 10://
				initLouPanList1(districtid);
				break;
			case 11:// �����Ի���ѡ���ȡͼƬ�ķ�ʽ
				scrollH = scrollView.getScrollY();
				popupWindwShowing(1);
				break;
			case 12:// ��ʼ������
				getIntentData();// ��ȡ����
				notifiView();
				break;
//				LinearLayout hintLoupanLayout,hintTypeLayout,hintLouCengLayout,hintRoomNumberLayout,hintPhoneLayout,
//				hintMoneyLayout,hintWuyeLayout,hintAcreageLayout;
//				TextView hintLoupanTv,hintTypeTv,hintLouCengTv,hintRoomNumberTv,hintPhoneTv,
//hintMoneyTv,hintWuyeTv,hintAcreageTv;
			case 13:// ¥�̲���
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintLoupanLayout.setVisibility(View.VISIBLE);
						hintLoupanTv.setText(b.getString("msg"));
					}
				} else {
					hintLoupanLayout.setVisibility(View.GONE);
				}
				break;
			case 14:// ��Դ���Ͳ���
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintTypeLayout.setVisibility(View.VISIBLE);
						hintTypeTv.setText(b.getString("msg"));
					}
				} else {
					hintTypeLayout.setVisibility(View.GONE);
				}
				break;
			case 15:// ¥�㲼��
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintLouCengLayout.setVisibility(View.VISIBLE);
						hintLouCengTv.setText(b.getString("msg"));
					}
				} else {
					hintLouCengLayout.setVisibility(View.GONE);
				}
				break;
			case 16:// �����
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintRoomNumberLayout.setVisibility(View.VISIBLE);
						hintRoomNumberTv.setText(b.getString("msg"));
					}
				} else {
					hintRoomNumberLayout.setVisibility(View.GONE);
				}
				break;
			case 17:// �绰����
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintPhoneLayout.setVisibility(View.VISIBLE);
						hintPhoneTv.setText(b.getString("msg"));
					}
				} else {
					hintPhoneLayout.setVisibility(View.GONE);
				}
				break;
			case 18:// ������
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintMoneyLayout.setVisibility(View.VISIBLE);
						hintMoneyTv.setText(b.getString("msg"));
					}
				} else {
					hintMoneyLayout.setVisibility(View.GONE);
				}
				break;
			case 19:// �����
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintWuyeLayout.setVisibility(View.VISIBLE);
						hintWuyeTv.setText(b.getString("msg"));
					}
				} else {
					hintWuyeLayout.setVisibility(View.GONE);
				}
				break;
			case 20:// ���
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						hintAcreageLayout.setVisibility(View.VISIBLE);
						hintAcreageTv.setText(b.getString("msg"));
					}
				} else {
					hintAcreageLayout.setVisibility(View.GONE);
				}
				break;
			}
		};
	};

	/**
	 * ��ӷ�Դ��Ϣ
	 * 
	 * @author Ring
	 * @since 2013-01-25 15:14
	 * @return true �����ɹ���false ����ʧ��
	 */
	public boolean addOfficeInfo() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		if(intenttag==1){//���
			params.add(new BasicNameValuePair("CreateUserID", MyApplication
					.getInstance(this).getSosouserinfo(this).getUserID()));
			params.add(new BasicNameValuePair("AreaUp", acreageEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("TelePhone", phoneEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("PriceUp", roomMoneyEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("BuildID", buildid));
			params.add(new BasicNameValuePair("OfficeMC", roomNumberEt.getText()
					.toString()));// ��Դ����
			params.add(new BasicNameValuePair("officeType", officetype));
			params.add(new BasicNameValuePair("WYManagmentFees", managerMoneyEt
					.getText().toString()));
			params.add(new BasicNameValuePair("RoomRate", ""));
			params.add(new BasicNameValuePair("Floors", loucengEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("FYJJ", roomIntroEt.getText()
					.toString()));
			uploaddata = new SoSoUploadData(this, "OfficeAdd.aspx", params);
		}else if(intenttag ==2){//��˹��ı༭
			params.add(new BasicNameValuePair("OfficeID", officeid));
			params.add(new BasicNameValuePair("AreaUp", acreageEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("TelePhone", phoneEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("PriceUp", roomMoneyEt.getText()
					.toString()));
			uploaddata = new SoSoUploadData(this, "OfficeUpdate.aspx", params);
		}else if(intenttag ==3){//����˵ı༭
			params.add(new BasicNameValuePair("OfficeID", officeid));
			params.add(new BasicNameValuePair("AreaUp", acreageEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("TelePhone", phoneEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("PriceUp", roomMoneyEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("BuildID", buildid));
			params.add(new BasicNameValuePair("OfficeMC", roomNumberEt.getText()
					.toString()));// ��Դ����
			params.add(new BasicNameValuePair("officeType", officetype));
			params.add(new BasicNameValuePair("WYManagmentFees", managerMoneyEt
					.getText().toString()));
			params.add(new BasicNameValuePair("RoomRate", ""));
			params.add(new BasicNameValuePair("Floors", loucengEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("FYJJ", roomIntroEt.getText()
					.toString()));
			uploaddata = new SoSoUploadData(this, "OfficeUpdateAll.aspx", params);
		}
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

	/**
	 * �ύ��Ϣǰ��֤��Ϣ�Ƿ������ͺϷ�
	 * 
	 * @author Ring
	 * @since 2013-01-25 15:17
	 * @return true ��֤ͨ�� false ��֤ʧ��
	 */

	public boolean textValidate() {
		/*
		 * if (roomNameEt.getText().toString().trim().equals("")) { //��Դ���Ʋ���Ϊ��
		 * MessageBox.CreateAlertDialog(
		 * getResources().getString(R.string.roomName_null),
		 * ReleaseNewRoomActivity.this); return false; } else
		 */if (phoneEt.getText().toString().trim().equals("")) {
			// �ֻ����벻��Ϊ��
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.officephone_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (!StringUtils
				.isCellphone(phoneEt.getText().toString().trim())) {
			// �ж��ֻ������Ƿ���ȷ
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.phone_error),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (loupanSpinner.getText().toString().trim().equals("")) {
			// ��ѡ������¥��
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.loupan_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (typeRoomSpinner.getText().toString().trim().equals("")) {
			// ��ѡ��Դ����
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.typeRoom_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (loucengEt.getText().toString().trim().equals("")) {
			// ������¥���
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.louceng_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (roomNumberEt.getText().toString().trim().equals("")) {
			// �����뷿���
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.roomNumber_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (roomMoneyEt.getText().toString().trim().equals("")) {
			// �����뷿����
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.roomMoney_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (managerMoneyEt.getText().toString().trim().equals("")) {
			// ��������ҵ�����
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.managerMoney_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (acreageEt.getText().toString().trim().equals("")) {
			// �����뷿�����
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.acreage_null),
					ReleaseNewRoomActivity.this);
			return false;
		}

		if (!loucengEt.getText().toString().trim().equals("")) {
			int louceng = 0;
			try {
				louceng = Integer.parseInt(loucengEt.getText().toString());
			} catch (Exception e) {
				// ������¥���
				MessageBox.CreateAlertDialog("¥�����������",
						ReleaseNewRoomActivity.this);
				return false;
			}
			if (louceng <= 0 || louceng > 200) {
				MessageBox.CreateAlertDialog("¥��ֻ����1~200֮��",
						ReleaseNewRoomActivity.this);
				return false;
			}
		}
		if (!managerMoneyEt.getText().toString().trim().equals("")) {
			Double louceng = 0.0;
			try {
				louceng = Double.parseDouble(managerMoneyEt.getText().toString());
			} catch (Exception e) {
				// ������¥���
				MessageBox.CreateAlertDialog("��ҵ����������",
						ReleaseNewRoomActivity.this);
				return false;
			}
			if (louceng < 0 || louceng > 100) {
				MessageBox.CreateAlertDialog("��ҵ��ֻ����0~100֮��",
						ReleaseNewRoomActivity.this);
				return false;
			}
		}
		
		if (!roomMoneyEt.getText().toString().trim().equals("")) {
			Double louceng = 0.0;
			try {
				louceng = Double.parseDouble(roomMoneyEt.getText().toString());
			} catch (Exception e) {
				// ������¥���
				MessageBox.CreateAlertDialog("��������������",
						ReleaseNewRoomActivity.this);
				return false;
			}
			if (louceng < 0 || louceng > 20) {
				MessageBox.CreateAlertDialog("������ֻ����0~20֮��",
						ReleaseNewRoomActivity.this);
				return false;
			}
		}
		return true;
	}

	/**
	 * @author Ring
	 * @since 2013-01-25 15:13
	 */
	public void dealReponse() {
		if (StringUtils.CheckReponse(reponse)) {
			ContentValues values = new ContentValues();
			if(intenttag!=1){//��ҳ���Ǳ༭����ʱ���ݿ�ֻ������
//				values.put("officeid", sosoofficeinfo.getOfficeID());
//				values.put("createuserid", MyApplication.getInstance(this)
//						.getSosouserinfo(this).getUserID());
				values.put("areaup", acreageEt.getText().toString());
//				values.put("areadown", sosoofficeinfo.getAreaDown());
//				values.put("address", sosoofficeinfo.getAddress());
				values.put("telephone", phoneEt.getText().toString());
//				values.put("storey", sosoofficeinfo.getStorey());
				values.put("floors", loucengEt.getText().toString());
				values.put("priceup", roomMoneyEt.getText().toString());
//				values.put("pricedown", sosoofficeinfo.getPriceDown());
				values.put("officemc", roomNumberEt.getText().toString());
				values.put("wymanagementfees", managerMoneyEt.getText()
						.toString());
				values.put("officetype", officetype);
				values.put("keywords", "");
//				values.put("fycx", sosoofficeinfo.getFYCX());
//				values.put("zcyh", sosoofficeinfo.getZCYH());
//				values.put("tsyh", sosoofficeinfo.getTSYH());
				values.put("fyjj", roomIntroEt.getText().toString());
				values.put("updatedate", "");
				values.put("buildid", buildid);
				values.put("officestatus", "");
				values.put("isprice", "");
				DBHelper.getInstance(ReleaseNewRoomActivity.this).update(
						"soso_officeinfo", values, "officeid = ?",
						new String[] {officeid});
				if (values != null) {
					values.clear();
					values = null;
				}
				DBHelper.getInstance(this).close();
				return;
			}
			Gson gson = new Gson();// ����Gson����
			SoSoOfficeInfo sosoofficeinfo = null;
			try {
				sosoofficeinfo = gson.fromJson(reponse, SoSoOfficeInfo.class);// ����json����
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sosoofficeinfo != null && sosoofficeinfo.getOfficeID() != null) {
				officeid = sosoofficeinfo.getOfficeID();
				values.put("officeid", sosoofficeinfo.getOfficeID());
				values.put("createuserid", MyApplication.getInstance(this)
						.getSosouserinfo(this).getUserID());
				values.put("areaup", acreageEt.getText().toString());
				values.put("areadown", sosoofficeinfo.getAreaDown());
				values.put("address", sosoofficeinfo.getAddress());
				values.put("telephone", phoneEt.getText().toString());
				values.put("storey", sosoofficeinfo.getStorey());
				values.put("floors", loucengEt.getText().toString());
				values.put("priceup", roomMoneyEt.getText().toString());
				values.put("pricedown", "0");
				values.put("officemc", roomNumberEt.getText().toString());
				values.put("wymanagementfees", managerMoneyEt.getText()
						.toString());
				values.put("officetype", officetype);
				values.put("keywords", "");
				values.put("fycx", sosoofficeinfo.getFYCX());
				values.put("zcyh", sosoofficeinfo.getZCYH());
				values.put("tsyh", sosoofficeinfo.getTSYH());
				values.put("fyjj", roomIntroEt.getText().toString());
				values.put("updatedate", "");
				values.put("buildid", buildid);
				values.put("officestate", "0");
				values.put("officestatus", "");
				values.put("isprice", "");
				values.put("ischecked", "0");
				values.put("isrent", "0");
				values.put("ispushed", "0");
				values.put("offadddate", "");
				values.put("nextalertdate", "");

				if (DBHelper
						.getInstance(ReleaseNewRoomActivity.this)
						.selectRow(
								"select * from soso_officeinfo where officeid = '"
										+ sosoofficeinfo.getOfficeID() + "'",
								null).size() <= 0) {
					DBHelper.getInstance(ReleaseNewRoomActivity.this).insert(
							"soso_officeinfo", values);
				} else {
					DBHelper.getInstance(ReleaseNewRoomActivity.this).update(
							"soso_officeinfo", values, "officeid = ?",
							new String[] { sosoofficeinfo.getOfficeID() });
				}
				values.clear();
				
				values.put("buildid", buildid);
				values.put("buildmc", loupanSpinner.getText().toString());
				values.put("buildtype", "");
				values.put("address", "");
				values.put("userid", MyApplication.getInstance().getSosouserinfo(this).getUserID());
//				selectresult = DBHelper.getInstance(context).selectRow(
//						"select * from soso_builduserinfo where buildid = '"
//								+ soSoBuildUserInfo.getBuildID() + "' and userid = '"+soSoBuildUserInfo.getUserID()+"'", null);
				boolean b = false;
				if (DBHelper.getInstance(this).selectRow(
						"select * from soso_builduserinfo where buildid = '"
						+ buildid + "' and userid = '"+MyApplication.getInstance().getSosouserinfo(this).getUserID()+"'", null) != null) {
					if (DBHelper.getInstance(this).selectRow(
							"select * from soso_builduserinfo where buildid = '"
							+ buildid + "' and userid = '"+MyApplication.getInstance().getSosouserinfo(this).getUserID()+"'", null).size() <= 0) {
						if (DBHelper.getInstance(this).insert("soso_builduserinfo",
								values)>0) {
							b =true ;
						}
					} else {
						b=DBHelper.getInstance(this)
								.update("soso_builduserinfo",
										values,
										"buildid = ? and userid =?",
										new String[] { buildid,MyApplication.getInstance().getSosouserinfo(this).getUserID() });
					}
//					selectresult.clear();
//					selectresult = null;
				}

			}
			if (values != null) {
				values.clear();
				values = null;
			}
			DBHelper.getInstance(this).close();
		}
	}

	/**
	 * ��ӷ�ԴͼƬ(�����ϴ�)
	 * 
	 * @author Ring
	 * @since 2013-01-26
	 */

	public boolean addOfficePictrues() {
		File file = null;// �ϴ����ļ�
		boolean b = false;// �ļ��Ƿ���ڣ��Ƿ�����ϴ�
		boolean b1 = true;// �ϴ��ļ����Ƿ��г����쳣�ͳ�ʱ�����
		if (roomImgList != null && roomImgList.size() > 0) {
			int i;
			for (i = 0; i < roomImgList.size(); i++) {
				if (roomImgList.get(i).get("path") != null) {
					file = new File(roomImgList.get(i).get("path").toString());
					if (file != null && file.exists() && file.isFile()) {
						b = true;
					}
				}
				if (b) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("appid", MyApplication.getInstance(this)
							.getAPPID());
					params.put("appkey", MyApplication.getInstance(this)
							.getAPPKEY());
					params.put("XgID", officeid);
					params.put("ImageType", "4");
					params.put("File", file.getName());
					Map<String, File> filesmap = new HashMap<String, File>();
					filesmap.put(file.getName(), file);
					System.out.println("�����������ַ:"
							+ MyApplication.getInstance(this).getURL()
							+ "ImageAdd.aspx");
					System.out.println("����������Ĳ���Ϊ��" + params);
					try {
						sosouploadfile = new SoSoUploadFile();
						sosouploadfile.post(MyApplication.getInstance(this)
								.getURL() + "ImageAdd.aspx", params, filesmap);
						reponse = sosouploadfile.getReponse();
					} catch (IOException e) {
						reponse = "��ʼֵ";
						e.printStackTrace();
					}
					System.out.println("�������ķ���ֵΪ��" + reponse);
					dealReponse1(officeid, 4, file);
					if (!StringUtils.CheckReponse(reponse)) {
						b1 = false;
					}
				}
			}
		}
		return b1;
	}
	
	/**
	 * ɾ��ͼƬ
	 * 
	 * @author Ring
	 * @since 2013-01-26
	 */

	public boolean deleteOfficePictrues() {
		boolean b = false;
		String str ="";
		try {
			str = deleteimages.substring(0, deleteimages.length() - 1);
			b= true;
		} catch (Exception e) {
		}
		if (b) {
			List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("appid", MyApplication
					.getInstance(this).getAPPID()));
			params.add(new BasicNameValuePair("appkey", MyApplication
					.getInstance(this).getAPPKEY()));
			params.add(new BasicNameValuePair("ImageIDList", str));
			uploaddata = new SoSoUploadData(this, "ImageDeleteList.aspx",
					params);
			uploaddata.Post();
			reponse = uploaddata.getReponse();
			dealReponse2(deleteimages
					.substring(0, deleteimages.length() - 1));
			params.clear();
			params = null;
			if (StringUtils.CheckReponse(reponse)) {
				return true;
			} else {
				return false;
			}

		}
		return b;
	}
	
	/**
	 * ɾ����ԴͼƬ
	 * 
	 * ���ߣ�Ring �����ڣ�2013-2-2
	 */
	public void dealReponse2(String imageids) {
		if (StringUtils.CheckReponse(reponse)) {
			String[] ids = imageids.split(",");
			int i;
			for(i=0;i<ids.length;i++){
				DBHelper.getInstance(this).delete("soso_imageinfo", "imageid = ?", new String[] {ids[i]});
			}
		}
	}

	/**
	 * ��ԴͼƬ���
	 * 
	 * ���ߣ�Ring �����ڣ�2013-2-2
	 */
	public void dealReponse1(String xgid, int imagetype, File file) {
		if (StringUtils.CheckReponse(reponse)) {
			// "_id integer primary key autoincrement,"+//�Զ�����_id
			// "imageid text,"+//ͼƬid
			// "xgid text,"+//����id���˴��Ƿ�Դid
			// "datuurl text,"+//��ͼ����λ��
			// "datusd text,"+//��ͼsd��λ��
			// "xiaotuurl text,"+//����ͼ����λ��
			// "xiaotusd text,"+//����ͼsd��λ��
			// "imagetype text"+//ͼƬ����
			ContentValues values = new ContentValues();
			SoSoImageInfo soSoImageInfo = null;
			Gson gson = new Gson();
			soSoImageInfo = gson.fromJson(reponse, SoSoImageInfo.class);
			values.put("imageid", soSoImageInfo.getImageID());
			values.put("xgid", xgid);
			values.put("datuurl", "");
			values.put("datusd", file.getAbsolutePath());
			values.put("xiaotuurl", "");
			values.put("xiaotusd", "");
			values.put("imagetype", imagetype);
			DBHelper.getInstance(this).execSql("update soso_officeinfo set imageid = '"+soSoImageInfo.getImageID()+"',thumb_sdpath='"+file.getAbsolutePath()+"' where officeid = '"+
					xgid+"'");
			if (DBHelper
					.getInstance(ReleaseNewRoomActivity.this)
					.selectRow(
							"select * from soso_imageinfo where imageid = '"
									+ soSoImageInfo.getImageID() + "'", null)
					.size() <= 0) {
				DBHelper.getInstance(ReleaseNewRoomActivity.this).insert(
						"soso_imageinfo", values);
			} else {
				DBHelper.getInstance(ReleaseNewRoomActivity.this).update(
						"soso_imageinfo", values, "imageid = ?",
						new String[] { soSoImageInfo.getImageID() });
			}

			if (values != null) {
				values.clear();
				values = null;
			}
			DBHelper.getInstance(this).close();
		}
	}

	/**
	 * ��ȡ��Դ������Ϣ
	 * 
	 * ���ߣ�Ring �����ڣ�2013-2-19
	 */
	public void getOfficeDetail() {
		// params ����Ĳ����б�
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("OfficeID", officeid));
		uploaddata = new SoSoUploadData(this, "OfficeSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(MyApplication.getInstance(this).getSosouserinfo(this)
				.getUserID());
		params.clear();
		params = null;
	}

	/**
	 * �����������Ӧֵ�������صķ�Դ������Ϣ��������
	 * 
	 * �����ڣ�2013-2-19
	 * 
	 */
	private void dealReponse(String userid) {
		if (StringUtils.CheckReponse(reponse)) {
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			SoSoOfficeInfo sosoofficeinfo = null;
			try {
				sosoofficeinfo = gson.fromJson(reponse, SoSoOfficeInfo.class);// ����json����
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (sosoofficeinfo != null && sosoofficeinfo.getOfficeID() != null) {
				values.put("officeid", sosoofficeinfo.getOfficeID());
				values.put("createuserid", sosoofficeinfo.getCreateUserID());
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
				values.put("officestate", sosoofficeinfo.getOfficeState());
				values.put("officestatus", "");
				values.put("isprice", "");
				values.put("offadddate", "");
				values.put("roomrate", sosoofficeinfo.getRoomRate());
				values.put("nextalertdate", "");

				if (DBHelper
						.getInstance(ReleaseNewRoomActivity.this)
						.selectRow(
								"select * from soso_officeinfo where officeid = '"
										+ sosoofficeinfo.getOfficeID() + "'",
								null).size() <= 0) {
					DBHelper.getInstance(ReleaseNewRoomActivity.this).insert(
							"soso_officeinfo", values);
				} else {
					DBHelper.getInstance(ReleaseNewRoomActivity.this).update(
							"soso_officeinfo", values, "officeid = ?",
							new String[] { sosoofficeinfo.getOfficeID() });
				}
			}
		}
	}


	/**
	 * ��ȡ��ԴͼƬid
	 * 
	 * ���ߣ�Ring �����ڣ�2013-2-20
	 */
	public void getofficeimages() {
		// params ����Ĳ����б�
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		params.add(new BasicNameValuePair("XgID", officeid));
		params.add(new BasicNameValuePair("ImageType", ""));
		uploaddata = new SoSoUploadData(this, "ImageSelect.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse1();
		params.clear();
		params = null;
	}

	/**
	 * �����������Ӧֵ�������صķ�Դ������Ϣ��������
	 * 
	 * �����ڣ�2013-2-19
	 * 
	 */
	private void dealReponse1() {
		if (StringUtils.CheckReponse(reponse)) {
			Type listType = new TypeToken<LinkedList<SoSoImageInfo>>() {
			}.getType();
			Gson gson = new Gson();
			LinkedList<SoSoImageInfo> soSoImageInfos = null;
			ContentValues values = new ContentValues();
			SoSoImageInfo soSoImageInfo = null;
			soSoImageInfos = gson.fromJson(reponse, listType);
			if (soSoImageInfos != null && soSoImageInfos.size() > 0) {
				for (Iterator<SoSoImageInfo> iterator = soSoImageInfos
						.iterator(); iterator.hasNext();) {
					soSoImageInfo = (SoSoImageInfo) iterator.next();
					if (soSoImageInfo.getIsUsed() == 1) {
						DBHelper.getInstance(this).delete("soso_imageinfo",
								"imageid = ?", new String[] { soSoImageInfo
								.getImageID() });
						continue;
					}
					if (soSoImageInfo != null
							&& soSoImageInfo.getImageID() != null) {
						values.put("imageid", soSoImageInfo.getImageID());
						values.put("xgid", soSoImageInfo.getXgID());
						values.put("datuurl",
								"http://114.141.181.215:99/uploadedFiles/office/300@300/"
										+ soSoImageInfo.getUrl());
						values.put("xiaotuurl",
								"http://114.141.181.215:99/uploadedFiles/office/70@60/"
										+ soSoImageInfo.getUrl());
						values.put("xiaotusd", "");
						values.put("imagetype", "0");
						if (DBHelper
								.getInstance(ReleaseNewRoomActivity.this)
								.selectRow(
										"select * from soso_imageinfo where imageid = '"
												+ soSoImageInfo.getImageID()
												+ "'", null).size() <= 0) {
							DBHelper.getInstance(ReleaseNewRoomActivity.this)
									.insert("soso_imageinfo", values);
						} else {
							DBHelper.getInstance(ReleaseNewRoomActivity.this)
									.update("soso_imageinfo",
											values,
											"imageid = ?",
											new String[] { soSoImageInfo
													.getImageID() });
						}
					}
				}
				if (soSoImageInfos != null) {
					soSoImageInfos.clear();
					soSoImageInfos = null;
				}

				if (values != null) {
					values.clear();
					values = null;
				}
				DBHelper.getInstance(this).close();
			}
		}
	}
}
