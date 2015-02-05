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
 * @author 刘星星
 * @createDate 2013/1/17 添加新房源类
 * 
 */
public class ReleaseNewRoomActivity extends BaseActivity implements
		Activity_interface, OnItemClickListener {
	private Gallery gallery;//房源图片查看控件
	private Button backBtn;
	private ArrayList<HashMap<String, Object>> roomImgList;
	public int chazhi = 0;// 底部弹出框的位置
	private PopupWindow selectPopupWindow = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// 设置拍照操作的标志
	public final static int REQUEST_CODE_INFO_SET = 1;// 设置文本信息的返回值标记
	public static final int PHOTORESOULT = 3;
	private final String TAG = "PersonalDataSetActivity";
	public boolean initWedget_tag = true;
	public String path = "";// 照片路径
	public String fileName = "";// 图片名字
	private Button takePictureBtn, getAlbumBtn, cancleBtn;// 拍照获取图片 从手机内存导入图片
															// 取消按钮
	public static final String SDCARD_ROOT_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();// 路径
	public static String SAVE_PATH_IN_SDCARD = ""; // 图片及其他数据保存文件夹
	private LinearLayout parent = null;
	private File sdcardTempFile = null;
	private ScrollView scrollView;// 滚动条，声明它是为了在拍摄回来后能停留在拍照的地方
	private int scrollH = 0;// 记录滚动条的位置

	private ArrayList<HashMap<String, String>> loupanList = new ArrayList<HashMap<String, String>>();// 所属楼盘集合
	private ListView dialogListView;
	TextView noText;//无数据时显示
	private TextView dialogTitle;
	private Button dialogBackBtn;
	private Button dialogCloseBtn;
	private ImageView dialogJianTou;

	private int data_type = 0;// 0代表地区 1代表商圈 2代表楼盘 3代表房源类型

	// private EditText roomNameEt;//房源名称
	private Button loupanSpinner;// 所属楼盘
	private EditText phoneEt;// 联系方式
	private Button typeRoomSpinner;// 房源类型
	private EditText loucengEt;// 楼层
	private EditText roomNumberEt;// 房间号
	private EditText roomMoneyEt;// 房间金额
	private EditText managerMoneyEt;// 物业管理费
	private EditText acreageEt;// 面积
//	private EditText ownAcreageEt;// 得房率
	private EditText roomIntroEt;// 房源简介
	private Button saveAndSendBtn;// 保存并发布房源的按钮
	private Button helpBtn;// 帮助说明
	private TextView title;

	private String cityid = "";// 城市编号
	private String areaid = "";// 区域编号
	private String districtid = "";// 商圈编号
	private String buildid = "";// 楼盘编号
	private String officetype = "";// 房源类型

	private SoSoUploadData uploaddata;// 上传数据的对象
	private GetBuilds getbuilds;
	private SoSoUploadFile sosouploadfile = null;// 上传文件的对象
	private String reponse = "";// 从服务器获取响应值

	private String officeid = "";
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private int intenttag = 1;// 表示该界面是修改还是添加，1添加，2审核过的编辑 3待审核的编辑
	private StringBuffer deleteimages = new StringBuffer("");//拼接的删除图片的id;
	private int activityFlag = 0;//0代表从房源列表中跳进来  1代表从房源管理中跳转过来
	// private ArrayList<HashMap<String, Object>> list;// 房源图片列表
	private boolean editFlag = true;
	private int galleryFirstImgSize = 300;//表示房源图片的第一张用来获取图片的图片尺寸
	private int gallerySize = 150;//房源图片的控件尺寸
	private int btn_tag = 0;//0任何不点击，1点击楼盘btn2点击房源类型btn
	
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
	 * author by Ring 文本焦点监听事件
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
				b.putString("msg","请选择楼层");
				msg.setData(b);
				msg.what = 15;
				handler.sendMessage(msg);
			}else if(v == loucengEt
					&& !loucengEt.getText().toString().trim().equals("")){
				int louceng = 0;
				try {
					louceng = Integer.parseInt(loucengEt.getText().toString());
				} catch (Exception e) {
					// 请填入楼层号
					b.clear();
					b.putString("msg","楼层输入有误！");
					b.putBoolean("isshow", true);
					msg.setData(b);
					msg.what = 15;
					handler.sendMessage(msg);
				}
				if (louceng < 0 || louceng > 200) {
					b.clear();
					b.putString("msg","楼层只能在0~200层以内");
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
				b.putString("msg","请选择房间号");
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
				b.putString("msg","请输入电话号码");
				msg.setData(b);
				msg.what = 17;
				handler.sendMessage(msg);
			} else if (v == phoneEt&&!StringUtils
					.isCellphone(phoneEt.getText().toString().trim())) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","手机号码格式错误！");
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
				b.putString("msg","请输入房租金额！");
				msg.setData(b);
				msg.what = 18;
				handler.sendMessage(msg);
			}else if(v == roomMoneyEt
					&& !roomMoneyEt.getText().toString().trim().equals("")){
				Double louceng = 0.0;
				try {
					louceng = Double.parseDouble(roomMoneyEt.getText().toString());
				} catch (Exception e) {
					// 请填入楼层号
					b.clear();
					b.putString("msg","房租金额输入有误！");
					b.putBoolean("isshow", true);
					msg.setData(b);
					msg.what = 18;
					handler.sendMessage(msg);
				}
				if (louceng < 0 || louceng > 20) {
					b.clear();
					b.putString("msg","房租金额只能在0~20以内");
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
				b.putString("msg","请输入物业费！");
				msg.setData(b);
				msg.what = 19;
				handler.sendMessage(msg);
			}else if(v == managerMoneyEt
					&& !managerMoneyEt.getText().toString().trim().equals("")){
				Double louceng = 0.0;
				try {
					louceng = Double.parseDouble(managerMoneyEt.getText().toString());
				} catch (Exception e) {
					// 请填入楼层号
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","物业费输入有误！");
					msg.setData(b);
					msg.what = 19;
					handler.sendMessage(msg);
				}
				if (louceng < 0 || louceng > 100) {
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","物业费只能在0~100之内");
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
				b.putString("msg","请输入房源面积！");
				msg.setData(b);
				msg.what = 20;
				handler.sendMessage(msg);
			}else if (v == acreageEt) {
				hintAcreageLayout.setVisibility(View.GONE);
			}
		}
	};

	/**
	 * 获取从房源列表传递过来的数据
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/23
	 * @param intent
	 *            传递过来的桥梁 信使
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
		title.setText("修改房源信息");
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
		if (v == backBtn) {// 返回按钮
			CommonUtils.hideSoftKeyboard(this);
			if(activityFlag == 1){//0代表从房源列表中跳进来  1代表从房源管理中跳转过来
				finish();
			}else if(activityFlag == 0){
				MyApplication.getInstance().setRoomListBack(true);
				finish();
			}
		} else if (v == takePictureBtn) {// 拍照获取图片
			dismiss();
			clearEditTextFocus();// 去掉所有文本框的焦点，确保获取图片返回之后滚动条能保持在能看到照片的地方
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
		} else if (v == getAlbumBtn) {// 从手机获取图片
			dismiss();
			clearEditTextFocus();// 去掉所有文本框的焦点，确保获取图片返回之后滚动条能保持在能看到照片的地方
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_CODE);
		} else if (v == cancleBtn) {// 取消获取图片按钮
			dismiss();
		} else if (v == typeRoomSpinner) {// 选择房源类型
			CommonUtils.hideSoftKeyboard(this);
			btn_tag = 2;
			popupWindwShowing(3);
		} else if (v == loupanSpinner) {// 选择所属楼盘
			CommonUtils.hideSoftKeyboard(this);
			btn_tag = 1;
			popupWindwShowing(2);
		} else if (v == helpBtn) {// 帮助明
			CommonUtils.hideSoftKeyboard(this);
			Intent intent = new Intent(this,EmployHelpActivity.class);
			startActivity(intent);
		} else if (v == saveAndSendBtn) {// 保存并发布
			if (textValidate()) {
				new Thread(runnable).start();
			}
		} else if (v == dialogBackBtn) {// 城市选择弹出框的返回按钮
			if (data_type == 1) {// 地区数据
				initDiQuList1(cityid);
			} else if (data_type == 2) {// 商圈数据
				initShangQuanList1(areaid);
			}
		} else if (v == dialogCloseBtn) {// 楼盘选择弹出框的关闭按钮
			Message msg = new Message();
			Bundle b = new Bundle();
			if (btn_tag == 1
					&& loupanSpinner.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","请选择楼盘");
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
				b.putString("msg","请选择楼盘类型");
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
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}
		Bitmap bm = null;
		if (requestCode == IMAGE_CODE) {// 选择图片返回
			String imgPath = "";
			Uri originalUri = data.getData(); // 获得图片的uri
			String[] proj = { MediaStore.Images.Media.DATA };
			// 好像是android多媒体数据库的封装接口，具体的看Android文档
			Cursor cursor = managedQuery(originalUri, proj, null, null, null);
			// 按我个人理解 这个是获得用户选择的图片的索引值
			if (cursor == null) {
				imgPath = data.getDataString();
			} else {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径www.2cto.com
				imgPath = cursor.getString(column_index);
			}

			File fromFile = new File(imgPath);
//			if(fromFile.getParentFile().getName() .contains("thumbnail")){
//				Toast.makeText(this, "不能选择缩略图！", 3000).show();
//				return;
//			}
			// 将文件复制到自定义文件
			if (fromFile != null && fromFile.exists()) {
				fileName = CommonUtils.getFileName();// 给图片起新的名字
				File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
				if (!file.exists()) {
					file.mkdirs();
				}
				sdcardTempFile = new File(SDCARD_ROOT_PATH
						+ SAVE_PATH_IN_SDCARD, fileName);
//				path = sdcardTempFile.getAbsolutePath();
//				FileCopyUtil.copyfile(fromFile, sdcardTempFile, true);
				Bitmap bitmap = CommonUtils.getDrawable(fromFile.getAbsolutePath(), 640, 480);
				bitmap = CommonUtils.RotateImg(fromFile.getAbsolutePath(),bitmap);//倒立的图片旋转90°
				if(bitmap!=null){
					boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
					if (copy) {
						/*File oldFile = new File(path);
						oldFile.delete();*/
						path = sdcardTempFile.getAbsolutePath();
					}
				}else{
					Toast.makeText(this, "图片不存在", 3000).show();
				}
			}
			bm = CommonUtils.getDrawable(path);
			if (bm != null) {// 图片存在
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("path", path);
				map.put("img", bm);
				roomImgList.add(map);
				notifiView();// 图片处理完毕，刷新布局
			} else {// 图片不存在
				Toast.makeText(this, "图片不存在", 3000).show();
			}
		} else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {// 拍照返回
			fileName = CommonUtils.getFileName();// 给图片起新的名字
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if (!file.exists()) {
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD,
					fileName);
			if (CommonUtils.isHasSdcard()) {// 存储卡可用
				// path = sdcardTempFile.getAbsolutePath();
				
				Bitmap bitmap = CommonUtils.getDrawable(path, 640, 480);
				bitmap = CommonUtils.RotateImg(path,bitmap);//倒立的图片旋转90°
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
				roomImgList.add(map);// 将图片加入列表
			} else { // 存储卡不可用直接返回缩略图
				Toast.makeText(this, "没有SD卡", 3000).show();
				// Bundle extras = data.getExtras();
				// bm = (Bitmap) extras.get("data");
				// roomImgList.add(bm);
				// boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bm);
				// if (copy) {
				// path = sdcardTempFile.getAbsolutePath();
				// }
			}
			notifiView();// 图片处理完毕，刷新布局
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(activityFlag == 1){//0代表从房源列表中跳进来  1代表从房源管理中跳转过来
				finish();
			}else if(activityFlag == 0){
				MyApplication.getInstance().setRoomListBack(true);
				finish();
			}
		}
		return false;
	}

	/**
	 * 用于在获取照片回来之后，让滚动条的位置能停留在最下面，这样用户体验会好一些，不用再去手动滚到最下面去获取更多的图片
	 * 当确定了滚动条之后，再让其能获取焦点
	 * 
	 * @createDate 2013/1/22
	 * @author 刘星星
	 * @param focus
	 *            true代表能获取焦点，false代表失去焦点
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
		Intent intent = getIntent();// 接受其他类传递过来的数据
		if (intent.getStringExtra("officeid") != null) {
			officeid = getIntent().getStringExtra("officeid");
			if(MyApplication.getInstance().getRoom_state_for_examine()==1){//1代码审核通过的房源  2代表待审核房源
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
			title.setText("发布新房源");
		}else{
			title.setText("修改房源信息");
		}
	}
	private void initEdit(){
		if(intenttag == 1 || intenttag == 3){
			
		}else if(intenttag == 2){
			loupanSpinner.setClickable(false);// 所属楼盘
			loupanSpinner.setTextColor(Color.GRAY);
			loupanSpinner.setBackgroundResource(R.drawable.soso_input_hui);
			typeRoomSpinner.setClickable(false);// 房源类型
			typeRoomSpinner.setTextColor(Color.GRAY);
			typeRoomSpinner.setBackgroundResource(R.drawable.soso_input_hui);
			loucengEt.setClickable(false);// 楼层
			loucengEt.setTextColor(Color.GRAY);
			loucengEt.setFocusable(false);
			loucengEt.setBackgroundResource(R.drawable.soso_input_hui);
			roomNumberEt.setFocusable(false);// 房间号
			roomNumberEt.setTextColor(Color.GRAY);
			roomNumberEt.setBackgroundResource(R.drawable.soso_input_hui);
			acreageEt.setTextColor(Color.GRAY);
			acreageEt.setFocusable(false);
			acreageEt.setClickable(false);
			acreageEt.setBackgroundResource(R.drawable.soso_input_hui);
//			ownAcreageEt.setFocusable(false);// 得房率
//			roomIntroEt.setFocusable(false);// 房源简介
//			saveAndSendBtn.setClickable(false);// 保存并发布房源的按钮
//			helpBtn.setClickable(false);
		}
	}
	/**
	 * 初始化地区数据
	 * 
	 * @createDate 2013/1/22
	 * @author 刘星星
	 */
	public void initDiQuList() {
		// loupanList.clear();
		// loupanList.add("徐汇区");
		// loupanList.add("浦东新区");
		// loupanList.add("普陀区");
		// loupanList.add("静安区");
		// loupanList.add("闵行区");
		// loupanList.add("长宁区");
		// loupanList.add("虹口区");
		// loupanList.add("黄浦区");
		// loupanList.add("杨浦区");
		// loupanList.add("卢湾区");
		// notifyLoupanSpinner();
		// dialogTitle.setText("所属地区");
		// dialogBackBtn.setVisibility(View.GONE);
		// data_type = 0;
	}

	/**
	 * 初始化地区数据
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
		// loupanList.add("徐汇区");
		// loupanList.add("浦东新区");
		// loupanList.add("普陀区");
		// loupanList.add("静安区");
		// loupanList.add("闵行区");
		// loupanList.add("长宁区");
		// loupanList.add("虹口区");
		// loupanList.add("黄浦区");
		// loupanList.add("杨浦区");
		// loupanList.add("卢湾区");
		notifyLoupanSpinner(true);
		dialogTitle.setText("所属地区");
		dialogBackBtn.setVisibility(View.GONE);
		data_type = 0;
	}

	/**
	 * 初始化地区数据
	 * 
	 * @createDate 2013/1/22
	 * @author 刘星星
	 */
	public void initShangQuanList() {
		// loupanList.clear();
		// loupanList.add("漕河泾");
		// loupanList.add("衡山路/肇嘉浜");
		// loupanList.add("龙华");
		// loupanList.add("田林");
		// loupanList.add("万体馆");
		// loupanList.add("徐家汇");
		// loupanList.add("陆家嘴");
		notifyLoupanSpinner(true);
		dialogTitle.setText("所属商圈");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 1;
	}

	/**
	 * 初始化商圈数据
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
		// loupanList.add("漕河泾");
		// loupanList.add("衡山路/肇嘉浜");
		// loupanList.add("龙华");
		// loupanList.add("田林");
		// loupanList.add("万体馆");
		// loupanList.add("徐家汇");
		// loupanList.add("陆家嘴");
		notifyLoupanSpinner(true);
		dialogTitle.setText("所属商圈");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 1;
	}

	/**
	 * 初始化地区数据
	 * 
	 * @createDate 2013/1/22
	 * @author 刘星星
	 */
	public void initLouPanList() {
		loupanList.clear();
		// loupanList.add("长城大厦");
		// loupanList.add("房地大厦");
		// loupanList.add("金茂大厦");
		// loupanList.add("金铭大厦");
		// loupanList.add("清水湾");
		// loupanList.add("曹杨家园");
		// loupanList.add("陆家嘴家园");
		notifyLoupanSpinner(false);
		dialogTitle.setText("所属楼盘");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 2;
	}

	/**
	 * 初始化楼盘数据
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

		// loupanList.add("长城大厦");
		// loupanList.add("房地大厦");
		// loupanList.add("金茂大厦");
		// loupanList.add("金铭大厦");
		// loupanList.add("清水湾");
		// loupanList.add("曹杨家园");
		// loupanList.add("陆家嘴家园");
		notifyLoupanSpinner(false);
		dialogTitle.setText("所属楼盘");
		dialogBackBtn.setVisibility(View.VISIBLE);
		data_type = 2;
		
	}

	/**
	 * 初始化房源类型
	 * 
	 * @author 刘星星
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
		dialogTitle.setText("房源类型");
		dialogBackBtn.setVisibility(View.GONE);
		data_type = 3;

	}

	/**
	 * 刷新楼盘列表
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/22
	 * @param display 是否显示箭头图片
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
	 * author by Ring 处理耗时操作
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
				handler.sendEmptyMessage(5);// 开启进度条
				getbuilds = new GetBuilds(ReleaseNewRoomActivity.this);
				getbuilds.getBuildsBydistrictid(districtid);
				handler.sendEmptyMessage(6);// 关闭进度条
				handler.sendEmptyMessage(10);// 登录成功后
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
	 * 刷新房源类型列表
	 * 
	 * @author 刘星星
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
	 * @author 刘星星
	 * @createDate 2013.1.22
	 * 
	 */
	class dialogListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Message msg = new Message();
			Bundle b = new Bundle();
			if (data_type == 0) {// 0代表地区
				areaid = loupanList.get(arg2).get("id").toString();
				initShangQuanList1(areaid);
			} else if (data_type == 1) {// 1代表商圈 
				districtid = loupanList.get(arg2).get("id").toString();
				new Thread(runnable1).start();
			} else if (data_type == 2) {//2代表楼盘 
				buildid = loupanList.get(arg2).get("id").toString();
				loupanSpinner.setText(loupanList.get(arg2).get("name")
						.toString());
				if (btn_tag == 1
						&& loupanSpinner.getText().toString().trim().equals("")) {
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","请选择楼盘");
					msg.setData(b);
					msg.what = 13;
					handler.sendMessage(msg);
				} else if (btn_tag == 1) {
					hintLoupanLayout.setVisibility(View.GONE);
				}
				dismiss();
			} else if (data_type == 3) {//3代表房源类型
				officetype = loupanList.get(arg2).get("id").toString();
				typeRoomSpinner.setText(loupanList.get(arg2).get("name")
						.toString());
				if (btn_tag == 2
						&& typeRoomSpinner.getText().toString().trim().equals("")) {
					b.clear();
					b.putBoolean("isshow", true);
					b.putString("msg","请选择楼盘类型");
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
	 * 显示PopupWindow窗口
	 * 
	 * @author 刘星星
	 * @param type
	 *            1代表底部的获取照片 2代表获取楼盘的窗口 3代表获取房源类型的窗口
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
		if (type == 1) {// 底部的获取照片的弹出框
			initPopuWindow();
		} else if (type == 2) {// 获取楼盘的弹出框
			initDialogWindow();
			initDiQuList1(cityid);// 初始化楼盘数据
		} else if (type == 3) {// 获取房源类型的弹出框
			initDialogWindow();
			initTypeRoomList();// 初始化房源类型数据
		}

	}

	/**
	 * 初始化楼盘窗口。
	 * 
	 * @author 刘星星
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
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
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
	 * @author 刘星星
	 * @createDate 2013/1/23 初始化获取图片的PopupWindow窗口
	 */
	private void initPopuWindow() {
		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.dialog_share, null);
		takePictureBtn = (Button) loginwindow.findViewById(R.id.eshareBtn1);
		getAlbumBtn = (Button) loginwindow.findViewById(R.id.eshareBtn2);
		cancleBtn = (Button) loginwindow.findViewById(R.id.eshareCancleBtn);
		takePictureBtn.setText("拍照");
		getAlbumBtn.setText("用户相册");
		cancleBtn.setText("取消");
		takePictureBtn.setOnClickListener(this);
		getAlbumBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		selectPopupWindow = new PopupWindow(loginwindow, parent.getWidth(),
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
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
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	/**
	 * 
	 * @author 刘星星
	 * @createDate 2013/1/23 获取图片的适配器类
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
								dialog.setTitle("提示！");
								dialog.setIcon(android.R.drawable.ic_dialog_alert);
								dialog.setMessage("确定要删除该图片吗？");
								dialog.setPositiveButton("确定",
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
													if (file.exists()) {// 删除图片
														deleteFile = file
																.delete();
													}
													if (deleteFile) {// 将列表中的对应的数据清除
														roomImgList
																.remove(position);
														notifiView();
													} else {// 删除不成功
														Toast.makeText(
																ReleaseNewRoomActivity.this,
																"图片删除失败！", 3000)
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
								dialog.setNegativeButton("取消",
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
	 * 显示弹出框的线程
	 * 
	 * @author 刘星星
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
	 * 图片控件的点击事件
	 * 
	 * @createDate 2013.1.23
	 * @author 刘星星
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 0) {// 控件中的第一张图片，用来获取图片。
			if(intenttag == 1 || intenttag == 3){//只有在发布房源的时候才可用
				if (roomImgList.size() == Constant.GALLERY_IMAGE_MAX_IMAGE_NUMBER) {// 设置最大图片数量为10张
					Toast.makeText(ReleaseNewRoomActivity.this, "最多只能拍摄上传10张房源图片！",
							3000).show();
				} else {// 弹出对话框选择获取图片方式
					CommonUtils.hideSoftKeyboard(this);// 隐藏软键盘
					arg1.requestFocus();
					// 这里采用线程的方式是因为在隐藏软键盘的同时也把对话框给隐藏了，因此采用线程的方式
					// 等软键盘消失之后再弹出对话框
					new Thread(runnableForShowDialog).start();
				}
			}else{
				Toast.makeText(ReleaseNewRoomActivity.this, "房源已审核通过，不能再修改！",
						3000).show();
			}
		} else {// 除了第一张以外的图片，点击可以进入预览页
			if (roomImgList.get(arg2).get("path") != null) {
				File file = new File(roomImgList.get(arg2).get("path")
						.toString());
				if (file.exists()) {// 判断图片是否存在
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
				} else {// 图片不存在 则删除列表中的图片 并刷新控件
					Toast.makeText(ReleaseNewRoomActivity.this, "图片不存在！", 5000)
							.show();
					roomImgList.remove(arg2);
					notifiView();
				}
			} else if (roomImgList.get(arg2).get("file") != null) {
				File file = (File) roomImgList.get(arg2).get("file");
				if (file.exists()) {// 判断图片是否存在
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
				} else {// 图片不存在 则删除列表中的图片 并刷新控件
					Toast.makeText(ReleaseNewRoomActivity.this, "图片不存在！", 5000)
							.show();
//					roomImgList.remove(arg2);
//					notifiView();
				}
			} else {
				// 图片不存在 则删除列表中的图片 并刷新控件
				Toast.makeText(ReleaseNewRoomActivity.this, "图片不存在！", 5000)
						.show();
				roomImgList.remove(arg2);
				notifiView();
			}
		}
	}
	
	/**
	 * author by Ring 处理耗时操作
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
				handler.sendEmptyMessage(5);// 开启进度条
				if (intenttag == 2||intenttag == 3) {
					getOfficeDetail();
					getofficeimages();
					handler.sendEmptyMessage(6);// 关闭进度条
					handler.sendEmptyMessage(12);// 初始化数据
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
			if (NetworkCheck.IsHaveInternet(ReleaseNewRoomActivity.this)) {
				handler.sendEmptyMessage(5);// 开启进度条
				if (intenttag == 1) {
					boolean b = false;// 添加房源信息是否成功的标识
					boolean b1 = false;// 添加房源图片是否成功标识
					b = addOfficeInfo();
					if (b) {
						b1 = addOfficePictrues();
					}
					handler.sendEmptyMessage(6);// 关闭进度条
					if (b) {
						if (b1) {
							handler.sendEmptyMessage(7);// 添加房源成功
						} else {
							handler.sendEmptyMessage(8);// 添加房源信息成功，添加房源图片失败
						}

					} else {
						handler.sendEmptyMessage(9);// 添加房源失败
					}
				} else if (intenttag == 2||intenttag == 3){
					boolean b = false;// 添加房源信息是否成功的标识
					boolean b1 = false;// 添加房源图片是否成功标识
					boolean b2 = false;//删除图片是否成功标识
					b = addOfficeInfo();
					if (b) {
						b1 = addOfficePictrues();
					}
//					if (b) {
						b2 = deleteOfficePictrues();
//					}
					handler.sendEmptyMessage(6);// 关闭进度条
					if (b) {
						if (b1) {
							handler.sendEmptyMessage(7);// 添加房源成功
						} else {
							handler.sendEmptyMessage(8);// 添加房源信息成功，添加房源图片失败
						}

					} else {
						handler.sendEmptyMessage(9);// 添加房源失败
					}
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
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			Bundle b = null;
			switch (msg.what) {
			case 1://刷新楼盘列表
				initLouPanList1(districtid);
				break;
			case 2:// 从登录界面跳转到注册界面
				break;
			case 3:// 登录失败给用户提示
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
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(ReleaseNewRoomActivity.this);
				break;
			case 5:// 打开进度条
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
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// 添加房源成功
				String promptmsg = "";
				if(intenttag==1){
					promptmsg = "恭喜您，添加房源成功！";
				}else{
					promptmsg = "修改房源成功！";
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseNewRoomActivity.this);
				builder.setMessage(promptmsg)
						.setTitle(getResources().getString(R.string.prompt))
						.setCancelable(false)
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
								//0代表从楼盘列表中进入添加房源 1代表从房源管理界面进入添加房源界面
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
			case 8:// 添加房源信息成功，添加房源图片失败
				MessageBox.CreateAlertDialog("部分房源图片未上传成功，请继续点击发布房源按钮！",
						ReleaseNewRoomActivity.this);
				break;
			case 9:// 添加房源失败
				String promptmsg1 = "";
				if(intenttag==1){
					promptmsg1 = "添加房源失败，请您稍后重试！";
				}else{
					promptmsg1 = "修改房源失败，请您稍后重试！";
				}
				MessageBox.CreateAlertDialog(promptmsg1,
						ReleaseNewRoomActivity.this);
				break;
			case 10://
				initLouPanList1(districtid);
				break;
			case 11:// 弹出对话框选择获取图片的方式
				scrollH = scrollView.getScrollY();
				popupWindwShowing(1);
				break;
			case 12:// 初始化数据
				getIntentData();// 获取数据
				notifiView();
				break;
//				LinearLayout hintLoupanLayout,hintTypeLayout,hintLouCengLayout,hintRoomNumberLayout,hintPhoneLayout,
//				hintMoneyLayout,hintWuyeLayout,hintAcreageLayout;
//				TextView hintLoupanTv,hintTypeTv,hintLouCengTv,hintRoomNumberTv,hintPhoneTv,
//hintMoneyTv,hintWuyeTv,hintAcreageTv;
			case 13:// 楼盘布局
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
			case 14:// 房源类型布局
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
			case 15:// 楼层布局
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
			case 16:// 房间号
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
			case 17:// 电话号码
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
			case 18:// 房租金额
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
			case 19:// 房间号
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
			case 20:// 面积
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
	 * 添加房源信息
	 * 
	 * @author Ring
	 * @since 2013-01-25 15:14
	 * @return true 发布成功，false 发布失败
	 */
	public boolean addOfficeInfo() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				this).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				this).getAPPKEY()));
		if(intenttag==1){//添加
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
					.toString()));// 房源名称
			params.add(new BasicNameValuePair("officeType", officetype));
			params.add(new BasicNameValuePair("WYManagmentFees", managerMoneyEt
					.getText().toString()));
			params.add(new BasicNameValuePair("RoomRate", ""));
			params.add(new BasicNameValuePair("Floors", loucengEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("FYJJ", roomIntroEt.getText()
					.toString()));
			uploaddata = new SoSoUploadData(this, "OfficeAdd.aspx", params);
		}else if(intenttag ==2){//审核过的编辑
			params.add(new BasicNameValuePair("OfficeID", officeid));
			params.add(new BasicNameValuePair("AreaUp", acreageEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("TelePhone", phoneEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("PriceUp", roomMoneyEt.getText()
					.toString()));
			uploaddata = new SoSoUploadData(this, "OfficeUpdate.aspx", params);
		}else if(intenttag ==3){//待审核的编辑
			params.add(new BasicNameValuePair("OfficeID", officeid));
			params.add(new BasicNameValuePair("AreaUp", acreageEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("TelePhone", phoneEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("PriceUp", roomMoneyEt.getText()
					.toString()));
			params.add(new BasicNameValuePair("BuildID", buildid));
			params.add(new BasicNameValuePair("OfficeMC", roomNumberEt.getText()
					.toString()));// 房源名称
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
	 * 提交信息前验证信息是否完整和合法
	 * 
	 * @author Ring
	 * @since 2013-01-25 15:17
	 * @return true 验证通过 false 验证失败
	 */

	public boolean textValidate() {
		/*
		 * if (roomNameEt.getText().toString().trim().equals("")) { //房源名称不能为空
		 * MessageBox.CreateAlertDialog(
		 * getResources().getString(R.string.roomName_null),
		 * ReleaseNewRoomActivity.this); return false; } else
		 */if (phoneEt.getText().toString().trim().equals("")) {
			// 手机号码不能为空
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.officephone_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (!StringUtils
				.isCellphone(phoneEt.getText().toString().trim())) {
			// 判断手机号码是否正确
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.phone_error),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (loupanSpinner.getText().toString().trim().equals("")) {
			// 请选择所属楼盘
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.loupan_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (typeRoomSpinner.getText().toString().trim().equals("")) {
			// 请选择房源类型
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.typeRoom_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (loucengEt.getText().toString().trim().equals("")) {
			// 请填入楼层号
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.louceng_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (roomNumberEt.getText().toString().trim().equals("")) {
			// 请填入房间号
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.roomNumber_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (roomMoneyEt.getText().toString().trim().equals("")) {
			// 请填入房租金额
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.roomMoney_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (managerMoneyEt.getText().toString().trim().equals("")) {
			// 请填入物业管理费
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.managerMoney_null),
					ReleaseNewRoomActivity.this);
			return false;
		} else if (acreageEt.getText().toString().trim().equals("")) {
			// 请填入房屋面积
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
				// 请填入楼层号
				MessageBox.CreateAlertDialog("楼层号输入有误",
						ReleaseNewRoomActivity.this);
				return false;
			}
			if (louceng <= 0 || louceng > 200) {
				MessageBox.CreateAlertDialog("楼层只能在1~200之内",
						ReleaseNewRoomActivity.this);
				return false;
			}
		}
		if (!managerMoneyEt.getText().toString().trim().equals("")) {
			Double louceng = 0.0;
			try {
				louceng = Double.parseDouble(managerMoneyEt.getText().toString());
			} catch (Exception e) {
				// 请填入楼层号
				MessageBox.CreateAlertDialog("物业费输入有误",
						ReleaseNewRoomActivity.this);
				return false;
			}
			if (louceng < 0 || louceng > 100) {
				MessageBox.CreateAlertDialog("物业费只能在0~100之内",
						ReleaseNewRoomActivity.this);
				return false;
			}
		}
		
		if (!roomMoneyEt.getText().toString().trim().equals("")) {
			Double louceng = 0.0;
			try {
				louceng = Double.parseDouble(roomMoneyEt.getText().toString());
			} catch (Exception e) {
				// 请填入楼层号
				MessageBox.CreateAlertDialog("房租金额输入有误",
						ReleaseNewRoomActivity.this);
				return false;
			}
			if (louceng < 0 || louceng > 20) {
				MessageBox.CreateAlertDialog("房租金额只能在0~20之内",
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
			if(intenttag!=1){//当页面是编辑功能时数据库只做更新
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
			Gson gson = new Gson();// 创建Gson对象
			SoSoOfficeInfo sosoofficeinfo = null;
			try {
				sosoofficeinfo = gson.fromJson(reponse, SoSoOfficeInfo.class);// 解析json对象
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
	 * 添加房源图片(单张上传)
	 * 
	 * @author Ring
	 * @since 2013-01-26
	 */

	public boolean addOfficePictrues() {
		File file = null;// 上传的文件
		boolean b = false;// 文件是否存在，是否可以上传
		boolean b1 = true;// 上传文件中是否有出现异常和超时的情况
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
					System.out.println("请求服务器地址:"
							+ MyApplication.getInstance(this).getURL()
							+ "ImageAdd.aspx");
					System.out.println("请求服务器的参数为：" + params);
					try {
						sosouploadfile = new SoSoUploadFile();
						sosouploadfile.post(MyApplication.getInstance(this)
								.getURL() + "ImageAdd.aspx", params, filesmap);
						reponse = sosouploadfile.getReponse();
					} catch (IOException e) {
						reponse = "初始值";
						e.printStackTrace();
					}
					System.out.println("服务器的返回值为：" + reponse);
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
	 * 删除图片
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
	 * 删除房源图片
	 * 
	 * 作者：Ring 创建于：2013-2-2
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
	 * 房源图片添加
	 * 
	 * 作者：Ring 创建于：2013-2-2
	 */
	public void dealReponse1(String xgid, int imagetype, File file) {
		if (StringUtils.CheckReponse(reponse)) {
			// "_id integer primary key autoincrement,"+//自动增长_id
			// "imageid text,"+//图片id
			// "xgid text,"+//关联id，此处是房源id
			// "datuurl text,"+//大图网络位置
			// "datusd text,"+//大图sd卡位置
			// "xiaotuurl text,"+//缩略图网络位置
			// "xiaotusd text,"+//缩略图sd卡位置
			// "imagetype text"+//图片类型
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
	 * 获取房源详情信息
	 * 
	 * 作者：Ring 创建于：2013-2-19
	 */
	public void getOfficeDetail() {
		// params 请求的参数列表
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
	 * 处理服务器响应值，将返回的房源对象信息保存下来
	 * 
	 * 创建于：2013-2-19
	 * 
	 */
	private void dealReponse(String userid) {
		if (StringUtils.CheckReponse(reponse)) {
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			SoSoOfficeInfo sosoofficeinfo = null;
			try {
				sosoofficeinfo = gson.fromJson(reponse, SoSoOfficeInfo.class);// 解析json对象
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
	 * 获取房源图片id
	 * 
	 * 作者：Ring 创建于：2013-2-20
	 */
	public void getofficeimages() {
		// params 请求的参数列表
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
	 * 处理服务器响应值，将返回的房源对象信息保存下来
	 * 
	 * 创建于：2013-2-19
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
