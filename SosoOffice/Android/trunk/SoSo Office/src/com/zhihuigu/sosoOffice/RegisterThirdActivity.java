package com.zhihuigu.sosoOffice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.zhihuigu.sosoOffice.constant.Constant;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.constant.OfficeType;
import com.zhihuigu.sosoOffice.constant.UserType;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.thread.SoSoUploadFile;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.PerfectIOthernfo;
import com.zhihuigu.sosoOffice.utils.PerfectInfo;
import com.zhihuigu.sosoOffice.utils.StringUtils;

public class RegisterThirdActivity extends BaseActivity{
	private Button backBtn,submitBtn;
	private EditText nameEt,companyNameEt,companyAddressEt,companyPhoneEt;
	private TextView nameText,addressText;//
	private TextView photoTextView;
	private ImageView imageView,photoView;
	private LinearLayout linearGetImage;//获取照片层
	public boolean initWedget_tag = true;
	private Button shareBtn1, shareBtn2,shareBtn3, cancleBtn;// 分享功能的对应四个按钮
	private LinearLayout parent = null;
	private PopupWindow selectPopupWindow = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private final String TAG = "RegisterThirdActivity";
	private boolean photo_tag = false;// 是否已经获取了证件图片
	public String path = "";// 照片路径
	public String fileName = "";//图片名字
	private LinearLayout bottomLinear;//
	private int  userType = Constant.TYPE_CLIENT;//用户类型
	
//	private SoSoUploadData uploaddata;//上传信息对象
	private PerfectIOthernfo perfectotherinfo;//修改业主或中介信息
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
	public static String SAVE_PATH_IN_SDCARD = ""; //图片及其他数据保存文件夹   
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;//设置拍照操作的标志  
	
	public int chazhi = 0;//底部弹出框的位置
	
	public File sdcardTempFile;
	
	private LinearLayout userNameCheckLayout,companyNameCheckLayout,phoneCheckLayout;
	private TextView userNameCheck,compamyCheck,phoneCheck;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_registerthird);
		findView();//控件初始化
		initView();//
		initData();//初始化数据
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
			Message msg = new Message();
			Bundle b = new Bundle();
//			private LinearLayout userNameCheckLayout,companyNameCheckLayout,phoneCheckLayout;
//			private EditText nameEt,companyNameEt,companyAddressEt,companyPhoneEt;
			if (v == nameEt
					&& nameEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","请输入您的真实姓名！");
				msg.setData(b);
				msg.what = 8;
				handler.sendMessage(msg);
			}else if (v == nameEt) {
				userNameCheckLayout.setVisibility(View.GONE);
			}
			
			if (v == companyNameEt
					&& companyNameEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","请输入公司名称");
				msg.setData(b);
				msg.what = 9;
				handler.sendMessage(msg);
			}else if (v == companyNameEt) {
				companyNameCheckLayout.setVisibility(View.GONE);
			}
			
			if (v == companyPhoneEt
					&& companyPhoneEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","请输入您的联系方式！");
				msg.setData(b);
				msg.what = 10;
				handler.sendMessage(msg);
			}else if (v == companyPhoneEt) {
				phoneCheckLayout.setVisibility(View.GONE);
			}
		}
	};
	
	/***
	 * author by Ring
	 * 初始化页面数据
	 */
	
	private void initData(){
		SAVE_PATH_IN_SDCARD = "/"+getResources().getString(R.string.root_directory)+"/"+
				getResources().getString(R.string.room_image)+"/";
	}
	
	
	private void findView(){
		backBtn = (Button) findViewById(R.id.backBtn);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		nameEt = (EditText) findViewById(R.id.NameEt);
		companyNameEt = (EditText) findViewById(R.id.companyNameEt);
		companyAddressEt  = (EditText) findViewById(R.id.companyAddressEt);
		companyPhoneEt = (EditText) findViewById(R.id.companyphoneEt);
		backBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageView);
		photoView = (ImageView) findViewById(R.id.photoView);
		linearGetImage = (LinearLayout) findViewById(R.id.linearGetImage);
		linearGetImage.setOnClickListener(this);
		bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
		nameText = (TextView) findViewById(R.id.nameText);
		addressText = (TextView) findViewById(R.id.addressText);
		photoTextView = (TextView) findViewById(R.id.photoTextView);
		userNameCheckLayout = (LinearLayout) findViewById(R.id.userNameCheckLayout);
		companyNameCheckLayout = (LinearLayout) findViewById(R.id.companyNameCheckLayout);
		phoneCheckLayout = (LinearLayout) findViewById(R.id.phoneCheckLayout);
		userNameCheck = (TextView) findViewById(R.id.userNameCheck);
		compamyCheck = (TextView) findViewById(R.id.companyNameCheck);
		phoneCheck = (TextView) findViewById(R.id.phoneCheck);
		nameEt.setOnFocusChangeListener(textfocuschange);
		companyNameEt.setOnFocusChangeListener(textfocuschange);
		companyPhoneEt.setOnFocusChangeListener(textfocuschange);
		
	}
	private void initView(){//为控件赋初始值
		userType = getIntent().getIntExtra("userType", Constant.TYPE_PROPRIETOR);
		if(userType == Constant.TYPE_PROPRIETOR){
			nameText.setText("公司名称");
			addressText.setText("公司地址");
		}else if(userType == Constant.TYPE_AGENCY){
			nameText.setText("中介名称");
			addressText.setText("中介地址");
			companyNameEt.setHint("请输入中介名称");
			companyAddressEt.setHint("请输入中介地址");
			photoTextView.setText("上传工作证图片");
			companyPhoneEt.setHint("请输入中介座机号码");
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}
		Bitmap bm = null;
		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();
		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {//选择图片返回
				String imgPath = "";
				Uri originalUri = data.getData(); // 获得图片的uri
//				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
				// 这里开始的第二部分，获取图片的路径：
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null,	null);
//				InputStream inputStream = resolver.openInputStream(originalUri);
				
				// 按我个人理解 这个是获得用户选择的图片的索引值
				if(cursor==null){
					imgPath = data.getDataString();
				}else{
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// 将光标移至开头 ，这个很重要，不小心很容易引起越界
					cursor.moveToFirst();
					// 最后根据索引值获取图片路径www.2cto.com
					imgPath = cursor.getString(column_index);
				}
				File fromFile = new File(imgPath);
				// 将文件复制到自定义文件
				if (fromFile != null && fromFile.exists()) {
					fileName = CommonUtils.getFileName();// 给图片起新的名字
					File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
					if (!file.exists()) {
						file.mkdirs();
					}
					sdcardTempFile = new File(SDCARD_ROOT_PATH
							+ SAVE_PATH_IN_SDCARD, fileName);
//					path = sdcardTempFile.getAbsolutePath();
//					FileCopyUtil.copyfile(fromFile, sdcardTempFile, true);
					
					Bitmap bitmap = CommonUtils.getDrawable(fromFile.getAbsolutePath(), MyApplication
							.getInstance(this).getScreenWidth(), MyApplication
							.getInstance(this).getScreenHeight());
					if(bitmap!=null){
						bitmap = CommonUtils.RotateImg(fromFile.getAbsolutePath(),bitmap);//倒立的图片旋转90°
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
				bm = CommonUtils.getDrawable(path,photoView);
				if(bm!=null){
					photo_tag = true;
					bottomLinear.setVisibility(View.GONE);
					photoView.setImageBitmap(bm);
				}else{
					Toast.makeText(this, "图片不存在", 3000).show();
				}
				
		}else if(requestCode == REQUEST_CODE_TAKE_PICTURE){//拍照返回   
			fileName = CommonUtils.getFileName();// 给图片起新的名字
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if (!file.exists()) {
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD,
					fileName);
            if(CommonUtils.isHasSdcard()){//存储卡可用   
            	path = SDCARD_ROOT_PATH+SAVE_PATH_IN_SDCARD+MyApplication.getInstance(this).getFileName();
            	Bitmap bitmap = CommonUtils.getDrawable(path, 500,500);
            	bitmap = CommonUtils.RotateImg(path,bitmap);//倒立的图片旋转90°
				boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
				if (copy) {
					File oldFile = new File(path);
					oldFile.delete();
					path = sdcardTempFile.getAbsolutePath();
				}
            	
                bm = CommonUtils.getDrawable(path,photoView);
                photo_tag = true;
				bottomLinear.setVisibility(View.GONE);
				photoView.setImageBitmap(bm);
            }else{  //存储卡不可用直接返回缩略图    
	            Bundle extras = data.getExtras();   
	            bm = (Bitmap) extras.get("data");  
	            photo_tag = true;
				bottomLinear.setVisibility(View.GONE);
				photoView.setImageBitmap(bm);
            }         
        }  
	}
	
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
		}else if(v == submitBtn){
			if(textValidate()){
				new Thread(runnable).start();
			}
		}else if(v == linearGetImage){
			int height = MyApplication.getInstance(this).getScreenHeight();
			if(photo_tag){
				shareBtn3.setVisibility(View.VISIBLE);
				if(height == 1280){
					chazhi = 670;
				}else if(height == 800){
					chazhi = 340;
				}else if(height == 960){
					chazhi = 500;
				}else if(height == 854){
					chazhi = 400;
				}else if(height == 480){
					chazhi = 175;
				}
			}else{
				shareBtn3.setVisibility(View.GONE);
				if(height == 1280){
					chazhi = 800;
				}else if(height == 800){
					chazhi = 440;
				}else if(height == 960){
					chazhi = 600;
				}else if(height == 854){
					chazhi = 492;
				}else if(height == 480){
					chazhi = 240;
				}
			}
			CommonUtils.hideSoftKeyboard(this);//隐藏可能弹出来了的软键盘
			new Thread(runnableForShowDialog).start();
			
		}else if(v == shareBtn1){
			dismiss();
			Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 fileName = CommonUtils.getFileName();
			 MyApplication.getInstance(this).setFileName(fileName);
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if(!file.exists()){
				file.mkdirs();
			}
			if(CommonUtils.isHasSdcard()){
				openCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDCARD_ROOT_PATH+  
                        SAVE_PATH_IN_SDCARD,fileName)));  
			}
			startActivityForResult(openCamera, REQUEST_CODE_TAKE_PICTURE);  
		}else if(v == shareBtn2){
			dismiss();
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			startActivityForResult(getAlbum, IMAGE_CODE);
		}else if(v == shareBtn3){
			dismiss();
//			Intent intent = new Intent(this,ImagePreViewActivity.class);
//			intent.putExtra("path", path);
//			startActivityForResult(intent, 6);
			ArrayList<String> al = new ArrayList<String>();
			al.clear();
			al.add(path);
			Intent it = new Intent(this,	ImageSwitcher.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putStringArrayListExtra("pathes", al);
			it.putExtra("index", 0);
			startActivity(it);
		}else if(v == cancleBtn){
			dismiss();
		}
		super.onClick(v);
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
				handler.sendEmptyMessage(7);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (initWedget_tag) {
			findView();
			initPopuWindow();
			initWedget_tag = false;
		}

	}
	
	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {
		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.dialog_share, null);
		shareBtn1 = (Button) loginwindow.findViewById(R.id.eshareBtn1);
		shareBtn2 = (Button) loginwindow.findViewById(R.id.eshareBtn2);
		shareBtn3 = (Button) loginwindow.findViewById(R.id.eshareBtn3);
		cancleBtn = (Button) loginwindow.findViewById(R.id.eshareCancleBtn);
		shareBtn1.setOnClickListener(this);
		shareBtn2.setOnClickListener(this);
		shareBtn3.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		selectPopupWindow = new PopupWindow(loginwindow, parent.getWidth(),
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
		
		selectPopupWindow
				.showAsDropDown(parent, 0, -(parent.getHeight() - chazhi));
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		return false;
	}
	
	
	/**
	 * author by Ring 提交前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		//验证这四个文本框nameEt,companyNameEt,companyPhoneEt;
		if (nameEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.realname_null),
					RegisterThirdActivity.this);
			return false;
		} else if (companyNameEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.companyname_null),
					RegisterThirdActivity.this);
			return false;
		} else if (companyPhoneEt.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.telephone_null),
					RegisterThirdActivity.this);
			return false;
		}else if(path!=null&&path.equals("")){
			File file = new File(path);
			if(!file.exists()||
					!file.isFile()){
				if(MyApplication.getInstance().getRoleid() == (int)(UserType.UserTypeIntermediary.getValue())){
					MessageBox.CreateAlertDialog(
							getResources().getString(R.string.pictrue1_null),
							RegisterThirdActivity.this);
				}else if(MyApplication.getInstance().getRoleid() == (int)(UserType.UserTypeOwner.getValue())){
					MessageBox.CreateAlertDialog(
							getResources().getString(R.string.pictrue2_null),
							RegisterThirdActivity.this);
				}
				return false;
			}
		}
		if(MyApplication.getInstance(this).getSosouserinfo()==null){
			MessageBox.CreateAlertDialog("对象为空",
					RegisterThirdActivity.this);
			return false;
		}
		
		return true;
	}
	
//	/***
//	 * author by Ring
//	 * 上传邀请数据
//	 */
//	
//	public boolean uploadMyData(){
//		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
//				this).getAPPID()));
//		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
//				this).getAPPKEY()));
//		params.add(new BasicNameValuePair("action", "add_invitation"));
//		params.add(new BasicNameValuePair("username", MyApplication
//				.getInstance(this).getSosouserinfo().getUserName()));
//		params.add(new BasicNameValuePair("password", MyApplication
//				.getInstance(this).getSosouserinfo().getPassWord()));
//		uploaddata = new SoSoUploadData(this, "invitation.aspx", params);
//		uploaddata.Post();
//		reponse = uploaddata.getReponse();
//		params.clear();
//		params = null;
//		if (reponse.equals("0")) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
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
			if (NetworkCheck.IsHaveInternet(RegisterThirdActivity.this)) {
				boolean b = false;
				handler.sendEmptyMessage(5);
				if(!runnable_tag){
					perfectotherinfo = new PerfectIOthernfo(RegisterThirdActivity.this);
					b = perfectotherinfo.perfectUserInfo(path,companyNameEt.getText().toString(),companyAddressEt.getText().toString(),companyPhoneEt.getText().toString(),nameEt.getText().toString());
				}
				handler.sendEmptyMessage(6);
				if(runnable_tag){
					runnable_tag= false;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(1);// 跳转到成功界面
				} else {
					handler.sendEmptyMessage(3);// 提交失败给用户提示
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
			case 1:// 上传数据成功
				i.putExtra("tag", 1);
				if(ApplicationSettingInfo.initSetting(RegisterThirdActivity.this,MyApplication.getInstance(RegisterThirdActivity.this)
						.getSosouserinfo().getUserID())){
					i.setClass(RegisterThirdActivity.this, CityListActivity.class);
					RegisterThirdActivity.this.startActivity(i);
					RegisterThirdActivity.this.finish();
				}else{
					if(MyApplication.getInstance().getNotlogin()==1){
						MyApplication.getInstance().setNotlogin(0);
						RegisterThirdActivity.this.finish();
					}else{
						i.setClass(RegisterThirdActivity.this, MainTabActivity.class);
						RegisterThirdActivity.this.startActivity(i);
						RegisterThirdActivity.this.finish();
					}
					
					
				}
			 break;
			case 3:// 上传数据失败
				String errormsg = "";
				if(reponse.equals(ErrorType.SoSoTimeOut.getValue())){
					errormsg = getResources().getString(R.string.progress_timeout);
				}else{
					errormsg = getResources().getString(R.string.dialog_message_submit_error);
				}
				MessageBox.CreateAlertDialog(errormsg,
						RegisterThirdActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(RegisterThirdActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(RegisterThirdActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						click_limit = true;
						if (perfectotherinfo != null) {
							perfectotherinfo.overReponse();
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
			case 7:
				popupWindwShowing();
				break;
//				private LinearLayout userNameCheckLayout,companyNameCheckLayout,phoneCheckLayout;
//				private TextView userNameCheck,compamyCheck,phoneCheck;
			case 8:// 真实姓名布局
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						userNameCheckLayout.setVisibility(View.VISIBLE);
						userNameCheck.setText(b.getString("msg"));
					}
				} else {
					userNameCheckLayout.setVisibility(View.GONE);
				}
				break;
			case 9:// 公司名称布局
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						companyNameCheckLayout.setVisibility(View.VISIBLE);
						compamyCheck.setText(b.getString("msg"));
					}
				} else {
					companyNameCheckLayout.setVisibility(View.GONE);
				}
				break;
			case 10:// 电话号码布局
				b = msg.getData();
				if (b.containsKey("isshow") && b.getBoolean("isshow")) {
					if (b.containsKey("msg")) {
						phoneCheckLayout.setVisibility(View.VISIBLE);
						phoneCheck.setText(b.getString("msg"));
					}
				} else {
					phoneCheckLayout.setVisibility(View.GONE);
				}
				break;
			}
		};
	};
	
//	/***
//	 * author by Ring
//	 * 完善用户信息
//	 */
//	public boolean uploadFile(){
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("appid",  MyApplication.getInstance(
//				this).getAPPID());
//		params.put("appkey", MyApplication.getInstance(
//				this).getAPPKEY());
//		params.put("UserID", MyApplication
//				.getInstance(this).getSosouserinfo().getUserID());
//		params.put("RealName", nameEt.getText().toString());
//		params.put("HeadImage", "");
//		params.put("Sex", MyApplication
//				.getInstance(this).getSosouserinfo().getSex()+"");
//		params.put("Birthday", MyApplication
//				.getInstance(this).getSosouserinfo().getBirthday());
//		params.put("Region", MyApplication
//				.getInstance(this).getSosouserinfo().getRegion());
//		params.put("Email", MyApplication
//				.getInstance(this).getSosouserinfo().getEmail());
//		params.put("Telephone", MyApplication
//				.getInstance(this).getSosouserinfo().getTelephone());
//		params.put("RoleID", userType+"");
//		params.put("ZJMC", companyNameEt.getText().toString());
//		params.put("ZJAddress", companyAddressEt.getText().toString());
//		params.put("ZJTele", companyPhoneEt.getText().toString());
//		params.put("JobsImage", new File(path).getName());
//		Map<String, File> filesmap = new HashMap<String, File>();
//		filesmap.put(new File(path).getName(), new File(path));
//		try {
//			uploadfile = new SoSoUploadFile();
//			uploadfile.post(MyApplication.getInstance(this).getURL()+"UpdateUser.aspx", params, filesmap);
//			reponse = uploadfile.getReponse();
//		} catch (IOException e) {
//			reponse = "初始值";
//			e.printStackTrace();
//		}
//		System.out.println(params+"---"+reponse);
//		dealReponse();
//		if (StringUtils.CheckReponse(reponse)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
//	
//	/***
//	 * author by Ring 
//	 * 当用户信息修改成功后将相关数据保存到本地数据库
//	 * 
//	 */
//	
//	public void dealReponse(){
//		if(StringUtils.CheckReponse(reponse)
//				&&MyApplication.getInstance(this).getSosouserinfo()!=null
//				&&MyApplication.getInstance(this).getSosouserinfo().getUserID()!=null){
//			ContentValues values = new ContentValues();
//			values.put("soso_realname", nameEt.getText().toString());
//			values.put("soso_ownerphone", companyPhoneEt.getText().toString());
//			values.put("soso_legalperson", companyNameEt.getText().toString());
//			values.put("soso_owneraddress", companyAddressEt.getText().toString());
//			values.put("soso_businesslicensesd", path);
//			values.put("soso_roleid", userType);
//			DBHelper.getInstance(RegisterThirdActivity.this).update("soso_userinfo",
//					values, "soso_userid = ?",
//					new String[] { MyApplication.getInstance(this).getSosouserinfo().getUserID() });
//		}
//	}
}
