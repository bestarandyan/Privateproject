package com.zhihuigu.sosoOffice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.utils.AsyncImageLoader;
import com.zhihuigu.sosoOffice.utils.AsyncImageLoader.ImageCallback;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.FileTools;
import com.zhihuigu.sosoOffice.utils.MD5;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.PerfectInfo;
import com.zhihuigu.sosoOffice.utils.StringUtils;

public class PersonalDataSetActivity extends BaseActivity{
	private RelativeLayout headLayout;//ͷ�񲼾�
	private RelativeLayout genderLayout;//�Ա𲼾�
	private RelativeLayout birthdayLayout;//���ղ���
	private RelativeLayout emailLayout;//���䲼��
	private RelativeLayout phoneLayout;//�绰����
	private RelativeLayout detailLayout;//���鲼��
	private RelativeLayout nameLayout;//��ʵ��������
	private Button backBtn;//���ؼ�
	private ImageView headImage;
	
	public int chazhi = 0;//�ײ��������λ��
	private PopupWindow selectPopupWindow = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // �����IMAGE_CODE���Լ����ⶨ���
	public final static int REQUEST_CODE_TAKE_PICTURE = 12;//�������ղ����ı�־  
	public final static int REQUEST_CODE_INFO_SET = 1;//�����ı���Ϣ�ķ���ֵ���
	public static final int PHOTORESOULT = 3;
	private final String TAG = "PersonalDataSetActivity";
	private boolean photo_tag = false;// �Ƿ��Ѿ���ȡ��֤��ͼƬ
//	public boolean initWedget_tag = true;
	public String path = "";// ��Ƭ·��
	public String oldPath = "";//��������֮ǰ��·��
	public String fileName = "";//ͼƬ����
	private Button shareBtn1, shareBtn2,shareBtn3, cancleBtn;// �����ܵĶ�Ӧ�ĸ���ť
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//·��   
	public static String SAVE_PATH_IN_SDCARD = ""; //ͼƬ���������ݱ����ļ���   
	private LinearLayout parent = null;
	
	private int crop = 150;
	private File sdcardTempFile = null;
	private int dialog_type = 0;//0�����ȡͼƬ   1����ѡ���Ա�
	private TextView name,gender,birthday,email,phone;
	
	private PerfectInfo perfectinfo;//�޸��û�����ķ����������
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private ProgressDialog progressdialog;
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	
	private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	private int headimagestate = 0;//0��ͷ����sd�����ڣ�1ͷ����sd��������
	private Calendar c=Calendar.getInstance();//���ڵ�ʵ����
	
	
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID  = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_personaldata);
		findView();
		initData();
	}
	/***
	 * author by Ring
	 * ��ʼ��ҳ������
	 */
	
	private void initData(){
		initPersonInfo();//��ʼ��������Ϣ
		SAVE_PATH_IN_SDCARD = "/"+getResources().getString(R.string.root_directory)+"/"+
				getResources().getString(R.string.head_image)+"/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(birthday.getText().toString());
			c.setTime(d);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}
	
	
	
	/***
	 * author by Ring
	 * ��ʼ���������ϵ�����
	 */
	
	
	public void initPersonInfo(){
		
		if(MyApplication.getInstance(this).getSosouserinfo(this)!=null){
			List<Map<String, Object>> selectresult = DBHelper
					.getInstance(this)
					.selectRow("select soso_userimagesd from soso_userinfo where soso_userid = '"
					+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'", null);
			String user_realname = MyApplication.getInstance(this).getSosouserinfo(this).getRealName();
			String user_sex = MyApplication.getInstance(this).getSosouserinfo(this).getSex()==1?"��":"Ů";
			String user_birthday = MyApplication.getInstance(this).getSosouserinfo(this).getBirthday();
			String user_email = MyApplication.getInstance(this).getSosouserinfo(this).getEmail();
			String user_phone = MyApplication.getInstance(this).getSosouserinfo(this).getTelephone();
			String user_headimag=MyApplication.getInstance(this).getSosouserinfo(this).getHeadImage();
			String user_headimagesd=null;
			try {
				user_headimagesd = selectresult.get(selectresult.size() - 1)
						.get("soso_userimagesd").toString();
			} catch (Exception e) {
				user_headimagesd="";
			}
//			String user_headimageupdate =selectresult.get(selectresult.size()-1).get("soso_userimageupdate").toString();
			String user_headimageupdate ="0";
			boolean b = true;//�ж��Ƿ���Ҫ�첽ȥ����ͼƬ
			path="";
			if(user_headimageupdate.equals("0")){
				if(user_headimagesd!=null&&!user_headimagesd.equals("")){
					File file = new File(user_headimagesd);
					if(file.exists()&&file.isFile()){
						Bitmap bm = CommonUtils.getDrawable(user_headimagesd,headImage);
						if(bm!=null){
							path = file.getAbsolutePath();
							headImage.setImageBitmap(bm);
							photo_tag = true;
							b= false;
						}
					}
				}
			}
			if(b){
				File file = null;
				String name = MD5.getMD5(user_headimag)
						+ ".jpg";
				file = new File(FileTools.SDPATH + "/"
						+ getResources().getString(R.string.root_directory)
						+ "/"
						+ getResources().getString(R.string.head_image)
						+ "/" + name);
				String sql = "update soso_userinfo set soso_userimagesd='"+file.getAbsolutePath()+"' where soso_userid='"
						+MyApplication.getInstance(this).getSosouserinfo(this).getUserID()+"'";
				if (file.exists() && file.isFile()) {
					Bitmap bm = CommonUtils.getDrawable(file.getAbsolutePath(),headImage);
					if(bm!=null){
						headImage.setImageBitmap(bm);
						photo_tag = true;
						path=file.getAbsolutePath();
						b= false;
					}
				} else {
					file = FileTools.getFile(
							getResources().getString(R.string.root_directory),
							getResources().getString(R.string.head_image),
							name);
					path=file.getAbsolutePath();
					Drawable cachedImage = asyncImageLoader
							.loadDrawable(user_headimag,headImage, new ImageCallback() {
								@Override
								public void imageLoaded(Drawable imageDrawable,
										ImageView imageView, String imageUrl) {
									 if (imageView !=
									 null) {
										 if(imageDrawable!=null){
											 headimagestate=0;
											 imageView.setImageDrawable(imageDrawable);
											 photo_tag = true;
										 }else{
											 headimagestate=1;
											 path="";
											 imageView.setImageResource(R.drawable.soso_gray_logo);
										 }
										 
									 }
								}
									},file,PersonalDataSetActivity.this,sql);
					if (cachedImage == null) {
						headimagestate=1;
						headImage.setImageResource(R.drawable.soso_gray_logo);
					} else {
						headimagestate=0;
						headImage.setImageDrawable(cachedImage);
					}
				}
			}
			
			name.setText(user_realname);
			gender.setText(user_sex);
			try {
				birthday.setText(StringUtils.MyformatDate("yyyy-MM-dd",
						"yyyy-MM-dd HH:mm:ss", user_birthday));
			} catch (Exception e) {
				birthday.setText(user_birthday);
			}
			email.setText(user_email);
			phone.setText(user_phone);
		}
		
	}
	
	
	private void findView(){
		headLayout = (RelativeLayout) findViewById(R.id.headLayout);
		genderLayout = (RelativeLayout) findViewById(R.id.genderLayout);
		birthdayLayout = (RelativeLayout) findViewById(R.id.birthdayLayout);
		emailLayout = (RelativeLayout) findViewById(R.id.emailLayout);
		phoneLayout = (RelativeLayout) findViewById(R.id.phoneLayout);
		detailLayout = (RelativeLayout) findViewById(R.id.detailLayout);
		nameLayout = (RelativeLayout) findViewById(R.id.nameLayout);
		backBtn = (Button) findViewById(R.id.backBtn);
		headImage = (ImageView) findViewById(R.id.headImage);
		name = (TextView) findViewById(R.id.name);
		gender = (TextView) findViewById(R.id.gender);
		birthday = (TextView) findViewById(R.id.birthday);
		email = (TextView) findViewById(R.id.email);
		phone = (TextView) findViewById(R.id.phone);
		headLayout.setOnClickListener(this);
		genderLayout.setOnClickListener(this);
		birthdayLayout.setOnClickListener(this);
		emailLayout.setOnClickListener(this);
		phoneLayout.setOnClickListener(this);
		detailLayout.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		nameLayout.setOnClickListener(this);
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == DATE_DIALOG_ID){
			Dialog dialog = new DatePickerDialog(this, mDatePickDialog, mYear,mMonth,mDay);
			dialog.setCanceledOnTouchOutside(false);
//			DatePicker dp = ((DatePickerDialog) dialog).getDatePicker();
//            dp.setMinDate(now.getTime().getTime());
			return dialog;
		}
			return null;
	}
	DatePickerDialog.OnDateSetListener mDatePickDialog=new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
//			view.setMinDate(c.getTime().getTime());
//			view.setMaxDate(c.getTime().getTime());
				mYear=year;
				mMonth=monthOfYear;
				mDay=dayOfMonth;	
				Calendar currentCalendar = Calendar.getInstance();
				int currentYear = currentCalendar.get(Calendar.YEAR);
				if(currentYear < mYear){
					mYear = currentYear;
				}
				c.set(Calendar. YEAR , mYear);
				c.set(Calendar. MONTH , mMonth);
				c.set(Calendar. DAY_OF_MONTH , mDay);
				updateDate();
				new Thread(runnable).start();
			}
	};
	
	/**
	 * �������ڿؼ���ʱ��
	 */
	
	private void updateDate(){
		SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
		birthday .setText( df .format( c .getTime()));
		}
	@Override
	public void onClick(View v) {
		if(v == headLayout){
			dialog_type = 0;
//			BitmapDrawable bitmapDrawable = (BitmapDrawable)headImage.getDrawable();   
//			if(bitmapDrawable!=null && bitmapDrawable.getBitmap()!=null&&headimagestate==0){
//				photo_tag = true;
//			}else{
//				photo_tag = false;
//			}
			initPopuWindow();
			showBottomDialog();
		}else if(v == nameLayout){//��ʵ����
			Intent intent = new Intent(this,InfoSetActivity.class);
			intent.putExtra("type", 1);
			intent.putExtra("value", name.getText().toString());
			startActivityForResult(intent, REQUEST_CODE_INFO_SET);
		}else if(v == genderLayout){//�Ա�
			dialog_type = 1;
			photo_tag = false;
			showBottomDialog();
		}else if(v == birthdayLayout){//����
			showDialog(DATE_DIALOG_ID);
		}else if(v == emailLayout){//�����ʼ�
			Intent intent = new Intent(this,InfoSetActivity.class);
			intent.putExtra("type", 4);
			intent.putExtra("value", email.getText().toString());
			startActivityForResult(intent, REQUEST_CODE_INFO_SET);
		}else if(v == phoneLayout){//�ֻ�����
			Intent intent = new Intent(this,InfoSetActivity.class);
			intent.putExtra("type", 5);
			intent.putExtra("value", phone.getText().toString());
			startActivityForResult(intent, REQUEST_CODE_INFO_SET);
		}else if(v == detailLayout){//�鿴����
			Intent intent = new Intent(this,DetailPersonalDataActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("path",(path.equals(""))?"":path);
			bundle.putString("name", name.getText().toString());
			bundle.putString("gender", gender.getText().toString());
			bundle.putString("birthday", birthday.getText().toString());
			bundle.putString("email", email.getText().toString());
			bundle.putString("phone", phone.getText().toString());
			intent.putExtra("personalInfo",bundle);
			startActivity(intent);
		}else if(v == backBtn){
			setResult(1);
			finish();
		}else if(v == shareBtn1){
			dismiss();
			if(dialog_type == 0){
				Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				 fileName = CommonUtils.getFileName();
				 MyApplication.getInstance(this).setFileName(fileName);
				File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
				if(!file.exists()){
					file.mkdirs();
				}
				sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
				if(CommonUtils.isHasSdcard()){
					/*openCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDCARD_ROOT_PATH+  
	                        SAVE_PATH_IN_SDCARD,fileName)));  */
					openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
				/*	openCamera.putExtra("crop", "true");
					openCamera.putExtra("aspectX", 1);// �ü������
					openCamera.putExtra("aspectY", 1);
					openCamera.putExtra("outputX", crop);// ���ͼƬ��С
					openCamera.putExtra("outputY", crop);*/
					openCamera.putExtra("return-data", true);
				}
				startActivityForResult(openCamera, REQUEST_CODE_TAKE_PICTURE);  
			}else if(dialog_type == 1 ){
				gender.setText("��");
				new Thread(runnable).start();//�޸ĵ����û���Ϣ��
			}
		}else if(v == shareBtn2){
			dismiss();
			if(dialog_type == 0){
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			fileName = CommonUtils.getFileName();
			 MyApplication.getInstance(this).setFileName(fileName);
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if(!file.exists()){
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
			getAlbum.putExtra("output", Uri.fromFile(sdcardTempFile));
			getAlbum.putExtra("crop", "true");
			getAlbum.putExtra("aspectX", 1);// �ü������
			getAlbum.putExtra("aspectY", 1);
			getAlbum.putExtra("outputX", crop);// ���ͼƬ��С
			getAlbum.putExtra("outputY", crop);
			startActivityForResult(getAlbum, IMAGE_CODE);
		}else if(dialog_type == 1 ){
			gender.setText("Ů");
			new Thread(runnable).start();//�޸ĵ����û���Ϣ��
		}
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
	
	private void showBottomDialog(){
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
		if(dialog_type == 0){//��ȡͼƬ
			shareBtn1.setText("���ջ�ȡ");
			shareBtn2.setText("���ֻ�����");
		}else if(dialog_type == 1){//ѡ���Ա�
			shareBtn1.setText("��");
			shareBtn2.setText("Ů");
			shareBtn3.setVisibility(View.GONE);
		}
		popupWindwShowing();
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
		// �˴��������жϽ��յ�Activity�ǲ�������Ҫ���Ǹ�
		if (requestCode == IMAGE_CODE) {//ѡ��ͼƬ����
				path = sdcardTempFile.getAbsolutePath();
				bm = CommonUtils.getDrawable(path,headImage);
				if(bm !=null){
					photo_tag = true;
					headImage.setImageBitmap(bm);
					new Thread(runnable).start();//�޸ĵ����û���Ϣ��
				}else{
					Toast.makeText(this, "ͼƬ������", 3000).show();
				}
				
		}else if(requestCode == REQUEST_CODE_TAKE_PICTURE){//���շ���   
            if(CommonUtils.isHasSdcard()){//�洢������   
            	 bm = CommonUtils.getDrawable(sdcardTempFile.getAbsolutePath(),null);
            	 bm = CommonUtils.RotateImg(sdcardTempFile.getAbsolutePath(),bm);//������ͼƬ��ת90��
 				if(sdcardTempFile.exists()){
 					sdcardTempFile.delete();
 				}
 				fileName = CommonUtils.getFileName();
 				 MyApplication.getInstance(this).setFileName(fileName);
 				File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
 				if(!file.exists()){
 					file.mkdirs();
 				}
 				sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
 				path = sdcardTempFile.getAbsolutePath();
 				CommonUtils.OutPutImage(sdcardTempFile,bm);
            	startPhotoZoom(Uri.fromFile(sdcardTempFile));
//            	new Thread(runnable).start();//�޸ĵ����û���Ϣ��
            	photo_tag = true;
            }else{  //�洢��������ֱ�ӷ�������ͼ    
	            Bundle extras = data.getExtras();   
	            bm = (Bitmap) extras.get("data");  
	            photo_tag = true;
				headImage.setImageBitmap(bm);
            }         
        }else if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				bm = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				BitmapDrawable bitmapDrawable = (BitmapDrawable)headImage.getDrawable();   
		        //���ͼƬ��δ���գ���ǿ�ƻ��ո�ͼƬ    
		        if(bitmapDrawable !=null && !bitmapDrawable.getBitmap().isRecycled()){   
		            bitmapDrawable.getBitmap().recycle();   
		        }   
		        headImage.setImageBitmap(bm);
		        if(sdcardTempFile.exists()){
 					sdcardTempFile.delete();
 				}
 				fileName = CommonUtils.getFileName();
 				 MyApplication.getInstance(this).setFileName(fileName);
 				File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
 				if(!file.exists()){
 					file.mkdirs();
 				}
 				sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
 				path = sdcardTempFile.getAbsolutePath();
 				CommonUtils.OutPutImage(sdcardTempFile,bm);
				
				new Thread(runnable).start();//�޸ĵ����û���Ϣ��
			}
			}else if(requestCode == REQUEST_CODE_INFO_SET){//�����û���Ϣ
        	int type = data.getIntExtra("type", 1);
        	if(type == 1){
        		name.setText(data.getStringExtra("value"));
        		new Thread(runnable).start();//�޸ĵ����û���Ϣ��
        	}else if(type == 4){
        		String str = data.getStringExtra("value");
        		if(str.length()>16){
        			str = str.substring(0, 16)+"...";
        		}
        		email.setText(str);
        		new Thread(runnable).start();//�޸ĵ����û���Ϣ��
        	}else if(type == 5){
        		phone.setText(data.getStringExtra("value"));
        		new Thread(runnable).start();//�޸ĵ����û���Ϣ��
        	}
        }
	}
	
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_TYPE);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);

	}
	/**
	 * û����onCreate�����е���initWedget()��������onWindowFocusChanged�����е��ã�
	 * ����ΪinitWedget()����Ҫ��ȡPopupWindow���������������������ȣ���onCreate���������޷���ȡ���ÿ�ȵ�
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
//		while (initWedget_tag) {
//			findView();
//			initPopuWindow();
//			initWedget_tag = false;
//		}

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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(1);
			finish();
		}
		return true;
	}
	
	
	/**
	 * author by Ring �����ʱ����
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				runnable_tag = true;
				click_limit = true;
				if (perfectinfo != null) {
					perfectinfo.overReponse();
					perfectinfo = null;
				}
			}
			if (NetworkCheck.IsHaveInternet(PersonalDataSetActivity.this)) {
				boolean b = false;
//				handler.sendEmptyMessage(5);
				if(!runnable_tag){
					perfectinfo = new PerfectInfo(PersonalDataSetActivity.this);
					b = perfectinfo.perfectUserInfo(path,name.getText().toString(),gender.getText().toString().equals("��")?"1":"2",birthday.getText().toString(),
							"",email.getText().toString(),phone.getText().toString(),"");
				}
//				handler.sendEmptyMessage(6);
				if(runnable_tag){
					runnable_tag= false;
					click_limit = true;
					return;
				}
				if (b) {
//					handler.sendEmptyMessage(1);// ��ת���ɹ�����
				} else {
//					handler.sendEmptyMessage(3);// �ύʧ�ܸ��û���ʾ
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
			case 1:// �ϴ����ݳɹ�
			 break;
			case 3:// �ϴ�����ʧ��
				String errormsg = "";
				if(reponse.equals(ErrorType.SoSoTimeOut.getValue())){
					errormsg = getResources().getString(R.string.progress_timeout);
				}else{
					errormsg = getResources().getString(R.string.dialog_message_submit_error);
				}
				MessageBox.CreateAlertDialog(errormsg,
						PersonalDataSetActivity.this);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(PersonalDataSetActivity.this);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(PersonalDataSetActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_submit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						click_limit = true;
						if (perfectinfo != null) {
							perfectinfo.overReponse();
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
