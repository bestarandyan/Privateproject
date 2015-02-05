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
	private LinearLayout linearGetImage;//��ȡ��Ƭ��
	public boolean initWedget_tag = true;
	private Button shareBtn1, shareBtn2,shareBtn3, cancleBtn;// �����ܵĶ�Ӧ�ĸ���ť
	private LinearLayout parent = null;
	private PopupWindow selectPopupWindow = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
	private final String TAG = "RegisterThirdActivity";
	private boolean photo_tag = false;// �Ƿ��Ѿ���ȡ��֤��ͼƬ
	public String path = "";// ��Ƭ·��
	public String fileName = "";//ͼƬ����
	private LinearLayout bottomLinear;//
	private int  userType = Constant.TYPE_CLIENT;//�û�����
	
//	private SoSoUploadData uploaddata;//�ϴ���Ϣ����
	private PerfectIOthernfo perfectotherinfo;//�޸�ҵ�����н���Ϣ
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//·��   
	public static String SAVE_PATH_IN_SDCARD = ""; //ͼƬ���������ݱ����ļ���   
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;//�������ղ����ı�־  
	
	public int chazhi = 0;//�ײ��������λ��
	
	public File sdcardTempFile;
	
	private LinearLayout userNameCheckLayout,companyNameCheckLayout,phoneCheckLayout;
	private TextView userNameCheck,compamyCheck,phoneCheck;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_registerthird);
		findView();//�ؼ���ʼ��
		initView();//
		initData();//��ʼ������
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
			Message msg = new Message();
			Bundle b = new Bundle();
//			private LinearLayout userNameCheckLayout,companyNameCheckLayout,phoneCheckLayout;
//			private EditText nameEt,companyNameEt,companyAddressEt,companyPhoneEt;
			if (v == nameEt
					&& nameEt.getText().toString().trim().equals("")) {
				b.clear();
				b.putBoolean("isshow", true);
				b.putString("msg","������������ʵ������");
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
				b.putString("msg","�����빫˾����");
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
				b.putString("msg","������������ϵ��ʽ��");
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
	 * ��ʼ��ҳ������
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
	private void initView(){//Ϊ�ؼ�����ʼֵ
		userType = getIntent().getIntExtra("userType", Constant.TYPE_PROPRIETOR);
		if(userType == Constant.TYPE_PROPRIETOR){
			nameText.setText("��˾����");
			addressText.setText("��˾��ַ");
		}else if(userType == Constant.TYPE_AGENCY){
			nameText.setText("�н�����");
			addressText.setText("�н��ַ");
			companyNameEt.setHint("�������н�����");
			companyAddressEt.setHint("�������н��ַ");
			photoTextView.setText("�ϴ�����֤ͼƬ");
			companyPhoneEt.setHint("�������н���������");
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) { // �˴��� RESULT_OK ��ϵͳ�Զ����һ������
			Log.e(TAG, "ActivityResult resultCode error");
			return;
		}
		Bitmap bm = null;
		// ���ĳ������ContentProvider���ṩ���� ����ͨ��ContentResolver�ӿ�
		ContentResolver resolver = getContentResolver();
		// �˴��������жϽ��յ�Activity�ǲ�������Ҫ���Ǹ�
		if (requestCode == IMAGE_CODE) {//ѡ��ͼƬ����
				String imgPath = "";
				Uri originalUri = data.getData(); // ���ͼƬ��uri
//				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // �Եõ�bitmapͼƬ
				// ���￪ʼ�ĵڶ����֣���ȡͼƬ��·����
				String[] proj = { MediaStore.Images.Media.DATA };
				// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
				Cursor cursor = managedQuery(originalUri, proj, null, null,	null);
//				InputStream inputStream = resolver.openInputStream(originalUri);
				
				// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
				if(cursor==null){
					imgPath = data.getDataString();
				}else{
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
					cursor.moveToFirst();
					// ����������ֵ��ȡͼƬ·��www.2cto.com
					imgPath = cursor.getString(column_index);
				}
				File fromFile = new File(imgPath);
				// ���ļ����Ƶ��Զ����ļ�
				if (fromFile != null && fromFile.exists()) {
					fileName = CommonUtils.getFileName();// ��ͼƬ���µ�����
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
						bitmap = CommonUtils.RotateImg(fromFile.getAbsolutePath(),bitmap);//������ͼƬ��ת90��
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
				bm = CommonUtils.getDrawable(path,photoView);
				if(bm!=null){
					photo_tag = true;
					bottomLinear.setVisibility(View.GONE);
					photoView.setImageBitmap(bm);
				}else{
					Toast.makeText(this, "ͼƬ������", 3000).show();
				}
				
		}else if(requestCode == REQUEST_CODE_TAKE_PICTURE){//���շ���   
			fileName = CommonUtils.getFileName();// ��ͼƬ���µ�����
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if (!file.exists()) {
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD,
					fileName);
            if(CommonUtils.isHasSdcard()){//�洢������   
            	path = SDCARD_ROOT_PATH+SAVE_PATH_IN_SDCARD+MyApplication.getInstance(this).getFileName();
            	Bitmap bitmap = CommonUtils.getDrawable(path, 500,500);
            	bitmap = CommonUtils.RotateImg(path,bitmap);//������ͼƬ��ת90��
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
            }else{  //�洢��������ֱ�ӷ�������ͼ    
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
			CommonUtils.hideSoftKeyboard(this);//���ؿ��ܵ������˵������
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
				handler.sendEmptyMessage(7);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	/**
	 * û����onCreate�����е���initWedget()��������onWindowFocusChanged�����е��ã�
	 * ����ΪinitWedget()����Ҫ��ȡPopupWindow���������������������ȣ���onCreate���������޷���ȡ���ÿ�ȵ�
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
	 * ��ʼ��PopupWindow
	 */
	private void initPopuWindow() {
		// PopupWindow���������򲼾�
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
		// ��һ����Ϊ��ʵ�ֵ���PopupWindow�󣬵������Ļ�������ּ�Back��ʱPopupWindow����ʧ��
		// û����һ����Ч�����ܳ�������������Ӱ�챳��
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
	 * ��ʾPopupWindow����
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// ��selectPopupWindow��Ϊparent����������ʾ����ָ��selectPopupWindow��Y����������ƫ��3pix��
		// ����Ϊ�˷�ֹ���������ı���֮�������϶��Ӱ���������
		// ���Ƿ�������϶����������϶�Ĵ�С�����ܻ���ݻ��͡�Androidϵͳ�汾��ͬ����ɣ���̫�����
		
		selectPopupWindow
				.showAsDropDown(parent, 0, -(parent.getHeight() - chazhi));
	}

	/**
	 * PopupWindow��ʧ
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
	 * author by Ring �ύǰ���ύ��Ϣ������֤ return true ��֤�ɹ���false ��֤ʧ��
	 */
	public boolean textValidate() {
		//��֤���ĸ��ı���nameEt,companyNameEt,companyPhoneEt;
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
			MessageBox.CreateAlertDialog("����Ϊ��",
					RegisterThirdActivity.this);
			return false;
		}
		
		return true;
	}
	
//	/***
//	 * author by Ring
//	 * �ϴ���������
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
					handler.sendEmptyMessage(1);// ��ת���ɹ�����
				} else {
					handler.sendEmptyMessage(3);// �ύʧ�ܸ��û���ʾ
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
			case 1:// �ϴ����ݳɹ�
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
			case 3:// �ϴ�����ʧ��
				String errormsg = "";
				if(reponse.equals(ErrorType.SoSoTimeOut.getValue())){
					errormsg = getResources().getString(R.string.progress_timeout);
				}else{
					errormsg = getResources().getString(R.string.dialog_message_submit_error);
				}
				MessageBox.CreateAlertDialog(errormsg,
						RegisterThirdActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(RegisterThirdActivity.this);
				break;
			case 5:// �򿪽�����
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
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:
				popupWindwShowing();
				break;
//				private LinearLayout userNameCheckLayout,companyNameCheckLayout,phoneCheckLayout;
//				private TextView userNameCheck,compamyCheck,phoneCheck;
			case 8:// ��ʵ��������
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
			case 9:// ��˾���Ʋ���
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
			case 10:// �绰���벼��
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
//	 * �����û���Ϣ
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
//			reponse = "��ʼֵ";
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
//	 * ���û���Ϣ�޸ĳɹ���������ݱ��浽�������ݿ�
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
