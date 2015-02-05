package com.piaoguanjia.accountManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.piaoguanjia.accountManager.SpecialAccountActivity.productinfo;
import com.piaoguanjia.accountManager.request.HandleData;
import com.piaoguanjia.accountManager.request.RequestServerFromHttp;
import com.piaoguanjia.accountManager.util.CommonUtils;
import com.piaoguanjia.accountManager.util.MD5;
import com.piaoguanjia.accountManager.util.MessageBox;
import com.piaoguanjia.accountManager.util.NetworkCheck;
import com.piaoguanjia.accountManager.view.SelectPicPopupWindow;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

public class RechargeActivity extends MainFrameActivity implements
		OnCheckedChangeListener {

	public int usertype = 1;// 1 总帐户，2专用账户
	public int rechargetype = 1;// 有凭证，2无凭证

	public static final int ID_IMAGEVIEW = 0x7f44763;// 凭证图片id
	public static final int ID_SCROLLVIEW1 = 0x7f44764;// 滚动视图1
	public static final int ID_SCROLLVIEW2 = 0x7f44765;// 滚动视图2
	private LinearLayout btn_linear1, btn_linear2, btn_linear3, btn_linear4,
			btn_linear6;// 支付宝，银行账号，现金，拍照
	public RadioGroup rg2;// 按钮组
	public ImageView imageview_gou1, imageview_gou2, imageview_gou3,
			imageview_gou4, imageview_gou5, image_head;

	public EditText tv_username, tv_produceid, tv_money, tv_remark,
			tv_warrantor;// 要充值的用户，产品编号,充值金额,备注

	public Button btn_scan, btn_delete;// 预览，删除按钮

	// 自定义的弹出框类
	public SelectPicPopupWindow menuWindow;

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	public final static int REQUEST_CODE_INFO_SET = 1;// 设置文本信息的返回值标记
	public static final int PHOTORESOULT = 3;
	private final String TAG = "PersonalDataSetActivity";
	// public boolean initWedget_tag = true;
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;// 设置拍照操作的标志
	public final static int EDITINFO = 13;// 进入编辑界面回来时的标识
	private File headFile;// 头像文件
	private File tempfile;// 头像临时文件
	public TextView productname;
	
	public int headimagewidth;
	public int headimageheight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 添加第一个滚动视图
		ScrollView scrollView1 = getScrollView();
		scrollView1.setId(ID_SCROLLVIEW1);
		addCenterView(scrollView1);
		rl1.setBackgroundColor(Color.parseColor("#3ABFDE"));
		setBottomEnable(false);
		initView("充值", "总帐户", "专用账户", "", "", true);
	}

	/**
	 * 
	 * @param 详情列表
	 * @return
	 */

	public ScrollView getScrollView() {
		ScrollView scrollView = new ScrollView(this);
		View view = LayoutInflater.from(this).inflate(R.layout.listitem3, null);
		view.setOnTouchListener(this);
		rg2 = (RadioGroup) view.findViewById(R.id.rg2);
		rg2.setOnCheckedChangeListener(this);
		btn_linear1 = (LinearLayout) view.findViewById(R.id.btn_linear1);
		btn_linear2 = (LinearLayout) view.findViewById(R.id.btn_linear2);
		btn_linear3 = (LinearLayout) view.findViewById(R.id.btn_linear3);
		btn_linear4 = (LinearLayout) view.findViewById(R.id.btn_linear4);
		btn_linear6 = (LinearLayout) view.findViewById(R.id.btn_linear6);
		imageview_gou1 = (ImageView) view.findViewById(R.id.imageview_gou1);
		imageview_gou2 = (ImageView) view.findViewById(R.id.imageview_gou2);
		imageview_gou3 = (ImageView) view.findViewById(R.id.imageview_gou3);
		imageview_gou4 = (ImageView) view.findViewById(R.id.imageview_gou4);
		imageview_gou5 = (ImageView) view.findViewById(R.id.imageview_gou5);
		image_head = (ImageView) view.findViewById(R.id.image_head);
		headimagewidth = image_head.getWidth();
		headimageheight = image_head.getHeight();
		// File file = new File(Environment.getExternalStorageDirectory()
		// + "/paiguanjia/temp");
		// if (!file.exists()) {
		// file.mkdirs();
		// }
		// tempfile = new File(file, "head.png");
		// Bitmap bm = CommonUtils.getDrawable(tempfile.getAbsolutePath(),
		// image_head);
		// if (bm != null) {
		// image_head.setImageBitmap(bm);
		// view.findViewById(R.id.tv_takepicture).setVisibility(View.VISIBLE);
		// } else {
		view.findViewById(R.id.tv_takepicture).setVisibility(View.GONE);
		// }
		btn_scan = (Button) view.findViewById(R.id.btn_scan);
		btn_delete = (Button) view.findViewById(R.id.btn_delete);
		btn_linear1.setOnClickListener(this);
		btn_linear2.setOnClickListener(this);
		btn_linear3.setOnClickListener(this);
		btn_linear4.setOnClickListener(this);
		btn_linear6.setOnClickListener(this);
		btn_scan.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		tv_username = (EditText) view.findViewById(R.id.tv_username);
		productname = (TextView) view.findViewById(R.id.productname);
		tv_produceid = (EditText) view.findViewById(R.id.tv_produceid);
		tv_produceid.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					new Thread(seleprorunnable).start();
			}
		});
		tv_money = (EditText) view.findViewById(R.id.tv_money);
		tv_money.addTextChangedListener(new TextWatcher()
		  {
		      public void afterTextChanged(Editable edt)
		      {
		          String temp = edt.toString();
		          int posDot = temp.indexOf(".");
		          if (posDot <= 0) return;
		          if (temp.length() - posDot - 1 > 2)
		          {
		              edt.delete(posDot + 3, posDot + 4);
		          }
		      }

		      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		      public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
		  });
		tv_remark = (EditText) view.findViewById(R.id.tv_remark);
		tv_warrantor = (EditText) view.findViewById(R.id.tv_warrantor);
		view.findViewById(R.id.btn_submit).setOnClickListener(this);
		view.findViewById(R.id.btn_submit).setBackgroundColor(Color.parseColor("#3ABFDE"));
		view.findViewById(R.id.btn_submit).setOnTouchListener(btnviewOntouch);
		scrollView.addView(view);
		return scrollView;
	}

	boolean b = true;// 判断有木有照片

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.rb3) {
			findViewById(R.id.btn_linear4).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_warrantor).setVisibility(View.GONE);
			if (b) {
				findViewById(R.id.tv_takepicture).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.tv_takepicture).setVisibility(View.GONE);
			}
			// findViewById(R.id.tv_takepicture).setVisibility(View.VISIBLE);
		} else if (checkedId == R.id.rb4) {
			b = findViewById(R.id.tv_takepicture).isShown();
			findViewById(R.id.btn_linear4).setVisibility(View.GONE);
			findViewById(R.id.tv_takepicture).setVisibility(View.GONE);
			findViewById(R.id.tv_warrantor).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btn_linear1) {
			imageview_gou1.setVisibility(View.VISIBLE);
			imageview_gou2.setVisibility(View.INVISIBLE);
			imageview_gou3.setVisibility(View.INVISIBLE);
		} else if (v == btn_linear2) {
			imageview_gou2.setVisibility(View.VISIBLE);
			imageview_gou1.setVisibility(View.INVISIBLE);
			imageview_gou3.setVisibility(View.INVISIBLE);
		} else if (v == btn_linear3) {
			imageview_gou3.setVisibility(View.VISIBLE);
			imageview_gou1.setVisibility(View.INVISIBLE);
			imageview_gou2.setVisibility(View.INVISIBLE);
		} else if (v == btn_linear4) {
			CommonUtils.hideSoftKeyboard(this);
			// 实例化SelectPicPopupWindow
			menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
			// 显示窗口
			menuWindow.showAtLocation(findViewById(ID_SCROLLVIEW1),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

		} else if (v == btn_linear6) {
			if (imageview_gou4.isShown()) {
				imageview_gou4.setVisibility(View.INVISIBLE);
				imageview_gou5.setVisibility(View.VISIBLE);
			} else {
				imageview_gou4.setVisibility(View.VISIBLE);
				imageview_gou5.setVisibility(View.INVISIBLE);
			}
		} else if (v.getId() == R.id.btn_submit) {
			if (validate()) {
				new Thread(this).start();
			}
		} else if (v.getId() == R.id.btn_scan) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/paiguanjia/temp");
			if (!file.exists()) {
				file.mkdirs();
			}
			tempfile = new File(file, "head.png");
			if (tempfile != null && tempfile.exists()) {
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(tempfile), "image/*");
				startActivity(intent);
			}
		} else if (v.getId() == R.id.btn_delete) {
			ShowConnectDialog("确定要删除该凭证么？");
		} else if (v.getId() == R.id.dialog_btn1) {
			((Dialog) (v.getTag())).dismiss();
		} else if (v.getId() == R.id.dialog_btn2) {
			((Dialog) (v.getTag())).dismiss();
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/paiguanjia/temp");
			if (!file.exists()) {
				file.mkdirs();
			}
			tempfile = new File(file, "head.png");
			try {
				tempfile.delete();
			} catch (Exception e) {
			}
			image_head.setImageBitmap(null);
			findViewById(R.id.tv_takepicture).setVisibility(View.GONE);
		} else if (v.getId() == R.id.rl1) {
			productname.setVisibility(View.GONE);
			findViewById(R.id.tv_produceid).setVisibility(View.GONE);
		} else if (v.getId() == R.id.rl2) {
//			productname.setVisibility(View.VISIBLE);
			findViewById(R.id.tv_produceid).setVisibility(View.VISIBLE);
			((EditText)findViewById(R.id.tv_produceid)).setText("");
		}
		super.onClick(v);
	}

	public void ShowConnectDialog(String msg) {
		LinearLayout loginLayout1 = (LinearLayout) getLayoutInflater().inflate(
				R.layout.dialog_layout, null);
		Button dialog_btn1 = (Button) loginLayout1
				.findViewById(R.id.dialog_btn1);
		Button dialog_btn2 = (Button) loginLayout1
				.findViewById(R.id.dialog_btn2);
		TextView tv_content = (TextView) loginLayout1
				.findViewById(R.id.tv_content);
		EditText et_content = (EditText) loginLayout1
				.findViewById(R.id.et_content);
		dialog_btn1.setText("取消");
		dialog_btn2.setText("确定");
		dialog_btn1.setOnClickListener(this);
		dialog_btn2.setOnClickListener(this);
		tv_content.setText(msg);
		et_content.setVisibility(View.GONE);
		Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(loginLayout1);
		dialog.show();
		dialog_btn1.setTag(dialog);
		dialog_btn2.setTag(dialog);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:// 拍照
				takePhotoes(1);
				break;
			case R.id.btn_pick_photo:// 导入
				takePhotoes(2);
				break;
			default:
				break;
			}

		}

	};

	@Override
	public boolean validate() {
		int accountType = tv_produceid.isShown() ? 2 : 1;// 1-总账户，2专用账户
		String chargeUsername = tv_username.getText().toString();
		Double amount = 0.0;
		boolean b_amount = false;
		try {
			amount = Double.parseDouble(tv_money.getText().toString());
			b_amount = true;
		} catch (Exception e) {
		}
		String productid = tv_produceid.getText().toString();
		int isCertificate = ((RadioButton) findViewById(R.id.rb3)).isChecked() ? 1
				: 0;// 1有，0没有
		String remark = tv_remark.getText().toString();
		String warrantor = tv_warrantor.getText().toString();
		int balanceSource = 0;
		if (imageview_gou1.isShown()) {
			balanceSource = 1;
		} else if (imageview_gou2.isShown()) {
			balanceSource = 2;
		} else if (imageview_gou3.isShown()) {
			balanceSource = 3;
		}
		int isReceipt = 0;
		if (imageview_gou4.isShown()) {
			isReceipt = 1;
		} else if (imageview_gou5.isShown()) {
			isReceipt = 0;
		}
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/paiguanjia/temp");
		if (!file.exists()) {
			file.mkdirs();
		}
		tempfile = new File(file, "head.png");
		if (chargeUsername.equals("")) {
			MessageBox.CreateAlertDialog("充值用户不能为空！", RechargeActivity.this);
			return false;
		} else if (!b_amount) {
			MessageBox.CreateAlertDialog("充值金额输入有误！", RechargeActivity.this);
			return false;
		} else if (accountType == 2 && productid.equals("")) {
			MessageBox.CreateAlertDialog("产品编号不能为空！", RechargeActivity.this);
			return false;
		} else if (amount < 0 && remark.trim().equals("")) {
			MessageBox.CreateAlertDialog("备注不能为空！", RechargeActivity.this);
			return false;
		} else if (isCertificate == 0 && warrantor.equals("")) {
			MessageBox.CreateAlertDialog("担保人用户名不能为空！", RechargeActivity.this);
			return false;
		} else if (isCertificate == 0 && remark.trim().equals("")) {
			MessageBox.CreateAlertDialog("备注不能为空！", RechargeActivity.this);
			return false;
		} else if (isCertificate == 1
				&& (!tempfile.exists() || !tempfile.isFile())) {
			MessageBox.CreateAlertDialog("请上传充值凭证！", RechargeActivity.this);
			return false;
		} else if (amount < 0 && isReceipt == 1) {
			MessageBox.CreateAlertDialog("充值金额小于零不能开具发票！",
					RechargeActivity.this);
			return false;
		} else if (balanceSource == 0) {
			MessageBox.CreateAlertDialog("请选择资金来源！", RechargeActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * 页面逻辑处理
	 */
	public Handler runnablehandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RESPONSE_HANDLER:
				linearInclude.removeAllViews();
				ScrollView scrollView1 = getScrollView();
				scrollView1.setId(ID_SCROLLVIEW1);
				addCenterView(scrollView1);
				MessageBox.CreateAlertDialog("添加充值成功！", RechargeActivity.this);
				break;
			case 0x11:
				Bundle b = msg.getData();
				String response = b.getString(KEY_RESPONSE);
				productname.setText("产品名称："+response);
				productname.setVisibility(View.VISIBLE);
				break;
			case 0x12:
				productname.setVisibility(View.GONE);
				break;
			}
		}
	};

	/**
	 * 数据逻辑处理
	 */
	@Override
	public void run() {
		if (!clicked) {
			clicked = true;
		} else {
			return;
		}
		if (NetworkCheck.IsHaveInternet(this)) {
			PROGRESSMSG = "正在添加充值，请稍等...";
			handler.sendEmptyMessage(PROGRESSSTART_HANDLER);// 开启进度条
			int accountType = tv_produceid.isShown() ? 2 : 1;// 1-总账户，2专用账户
			String chargeUsername = tv_username.getText().toString();
			Double amount = 0.0;
			try {
				amount = Double.parseDouble(tv_money.getText().toString());
			} catch (Exception e) {
			}
			String productid = tv_produceid.getText().toString();
			int isCertificate = ((RadioButton) findViewById(R.id.rb3))
					.isChecked() ? 1 : 0;// 1有，0没有
			String remark = tv_remark.getText().toString();
			String warrantor = tv_warrantor.getText().toString();
			int balanceSource = 0;
			if (imageview_gou1.isShown()) {
				balanceSource = 1;
			} else if (imageview_gou2.isShown()) {
				balanceSource = 2;
			} else if (imageview_gou3.isShown()) {
				balanceSource = 3;
			}
			int isReceipt = 0;
			if (imageview_gou4.isShown()) {
				isReceipt = 1;
			} else if (imageview_gou5.isShown()) {
				isReceipt = 0;
			}
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/paiguanjia/temp");
			if (!file.exists()) {
				file.mkdirs();
			}
			tempfile = new File(file, "head.png");

			String response = RequestServerFromHttp.addCharge(chargeUsername,
					amount, accountType, productid, isCertificate, remark,
					warrantor, tempfile, balanceSource, isReceipt);// 添加充值
			HandleData.handleChargeInfo(response);
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			msg.setData(b);
			int chargeid = 0;
			try {
				chargeid = Integer.parseInt(response);
			} catch (Exception e) {
			}
			if (chargeid > 0) {
				if (isCertificate == 1) {
					try {
						File file1 = new File(
								Environment.getExternalStorageDirectory()
										+ "/paiguanjia/image");
						if (!file1.exists()) {
							file1.mkdirs();
						}
						File imagefile = new File(file1, MD5.getMD5(chargeid
								+ "")
								+ ".png");
						tempfile.renameTo(imagefile);
						tempfile.delete();
					} catch (Exception e) {
					}
				}
				runnablehandler.sendMessage(msg);
			} else {
				handler.sendMessage(msg);
			}
			handler.sendEmptyMessage(PROGRESSEND_HANDLER);
		} else {
			handler.sendEmptyMessage(NONETWORK_HANDLER);
		}
		clicked = false;
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
		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {// 选择图片返回
			if (tempfile == null) {
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/paiguanjia/temp");
				if (!file.exists()) {
					file.mkdirs();
				}
				tempfile = new File(file, "temp.png");
			}
			bm = CommonUtils
					.getDrawable(tempfile.getAbsolutePath(), image_head);
			if (bm != null) {
				image_head.setImageBitmap(bm);
				findViewById(R.id.tv_takepicture).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.tv_takepicture).setVisibility(View.GONE);
				Toast.makeText(this, "图片不存在", 3000).show();
			}
		} else if (requestCode == REQUEST_CODE_TAKE_PICTURE) {// 拍照返回
			if (CommonUtils.isHasSdcard()) {// 存储卡可用
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/paiguanjia/temp");
				if (!file.exists()) {
					file.mkdirs();
				}
				tempfile = new File(file, "head.png");

				bm = CommonUtils.getDrawable(tempfile.getAbsolutePath(), null);
				bm = CommonUtils.RotateImg(tempfile.getAbsolutePath(), bm);// 倒立的图片旋转90°
				CommonUtils.OutPutImage(tempfile, bm);
				// startPhotoZoom(Uri.fromFile(tempfile));
				image_head.setImageBitmap(bm);
				findViewById(R.id.tv_takepicture).setVisibility(View.VISIBLE);
			} else { // 存储卡不可用直接返回缩略图
				Bundle extras = data.getExtras();
				bm = (Bitmap) extras.get("data");
				image_head.setImageBitmap(bm);
				findViewById(R.id.tv_takepicture).setVisibility(View.GONE);
			}
		} else if (requestCode == PHOTORESOULT) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/paiguanjia/temp");
			if (!file.exists()) {
				file.mkdirs();
			}
			tempfile = new File(file, "head.png");
			Bundle extras = data.getExtras();
			if (extras != null) {
				bm = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				image_head.setImageBitmap(bm);
				findViewById(R.id.tv_takepicture).setVisibility(View.VISIBLE);
				CommonUtils.OutPutImage(tempfile, bm);
			}
		}
	}

	/**
	 * type ==1 拍照，type == 2导出
	 * 
	 * @param type
	 */
	public void takePhotoes(int type) {
		if (type == 1) {
			Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/paiguanjia/temp");
			if (!file.exists()) {
				file.mkdirs();
			}
			tempfile = new File(file, "head.png");
			if (CommonUtils.isHasSdcard()) {
				openCamera.putExtra("output", Uri.fromFile(tempfile));
				openCamera.putExtra("return-data", true);
			}
			startActivityForResult(openCamera, REQUEST_CODE_TAKE_PICTURE);
		} else {
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/paiguanjia/temp");
			if (!file.exists()) {
				file.mkdirs();
			}
			tempfile = new File(file, "head.png");
			getAlbum.putExtra("output", Uri.fromFile(tempfile));
			getAlbum.putExtra("crop", "true");
//			getAlbum.putExtra("aspectX", 1);// 裁剪框比例
//			getAlbum.putExtra("aspectY", 1);
//			getAlbum.putExtra("outputX", 200);// 输出图片大小
//			getAlbum.putExtra("outputY", 200);
			startActivityForResult(getAlbum, IMAGE_CODE);
		}
	}
	
	public Runnable seleprorunnable = new Runnable() {

		@Override
		public void run() {
			String response = RequestServerFromHttp.productname(tv_produceid
					.getText().toString());
			Gson gson = new Gson();// 创建Gson对象
			productinfo bean = null;
			try {
				bean = gson.fromJson(response, productinfo.class);// 解析json对象
			} catch (Exception e) {
			}
			if (bean == null || bean.getProductName() == null
					|| bean.getProductName().equals("")) {
				runnablehandler.sendEmptyMessage(0x12);
				return;
			}
			Message msg = new Message();
			msg.what = 0x11;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, bean.getProductName());
			msg.setData(b);
			runnablehandler.sendMessage(msg);
		}
	};
}
