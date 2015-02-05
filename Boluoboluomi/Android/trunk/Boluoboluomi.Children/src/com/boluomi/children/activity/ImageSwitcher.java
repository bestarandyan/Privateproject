/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.boluomi.children.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.ImageType;
import com.boluomi.children.data.ImgDownType;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.model.UpLoadFileInfo;
import com.boluomi.children.model.UserPhotoInfo;
import com.boluomi.children.util.BitmapCache;
import com.boluomi.children.util.CommonUtils;
import com.boluomi.children.util.MessageBox;
import com.boluomi.children.view.Rotate3dAnimation;
import com.qingfengweb.share.EmailShare;
import com.qingfengweb.share.SMSShare;

/**
 * @Filename: ImageSwitcher.java
 * @Author: liuxingxing and wuguoqing
 * @Email: xingxing@qingfengweb.com or guoqing@qingfengweb.com
 * @CreateDate: 2012-11-16
 * @Description: 相册类，包括自动播放和手动切换照片。并实现分享功能
 * @Others: comments
 * @ModifyHistory:
 */
public class ImageSwitcher extends BaseActivity implements ViewFactory,OnClickListener,OnTouchListener{
	private int mIndex = 0;
	private int mItemwidth;
	private int mItemHerght;
	private ArrayList<HashMap<String, Object>> pathes;
	private List<Map<String,Object>> photoListInfo = null;
	private Button frontBtn, nextBtn, backBtn, shareBtn, playBtn;// 上一张 下一张 返回按钮
																	// 分享按钮
																	// 自动播放按钮
	private Button setBtn;//设置按钮
	private TextView currentPhoto;// 顶部中间显示当前照片下标以及照片总张数。
	private RelativeLayout topLinear;// 顶部布局
	private LinearLayout bottomLinear;// 底部布局
	private Timer timer = null;
	private Dialog alert = null;
	public boolean initWedget_tag = true;
	private boolean flag = true;
	private RelativeLayout parent = null;
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;//分享弹出框或者是设置弹出框
	private GestureDetector detector;// 控件的触屏事件监听
//	private UGallery flipper;// 滑屏控件
	private boolean autoFLag = false;// 用来判断当前状态是否为自动播放状态
	private android.widget.ImageSwitcher imageSwitcher = null;
	private ProgressDialog progressdialog;
	private Bitmap bitmap = null;
	private boolean click_limit = true;
//	private ImageButton openMenuBtn;
	private final String IMAGE_TYPE = "image/*";
	private String fileName = "";
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
//	public static String SAVE_PATH_IN_SDCARD = "/chargeMoney"; //图片及其他数据保存文件夹   
	private String path = "";//图片的最终路径
	private File sdcardTempFile = null;
	private boolean goon = true;
	private boolean goon1 = true;
	int downX = 0;
	int upX = 0;
	private Bitmap currentBitmap = null;//当前图片
	private String imgLocalUrl = "";//图片在本地sd卡的路径
	/**
	 * 此类mIndex是一个重要的变量，根据此变量可以找到当前图片在集合中所保存的数据
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myhorizontalview);
		findView();
		mIndex = 0;
		initData();
	}
	private void findView() {
//		flipper = (UGallery) findViewById(R.id.vf);
		imageSwitcher = (android.widget.ImageSwitcher) findViewById(R.id.imageswitch);
		frontBtn = (Button) findViewById(R.id.picFront);
		nextBtn = (Button) findViewById(R.id.picNext);
		backBtn = (Button) findViewById(R.id.backBtn);
		shareBtn = (Button) findViewById(R.id.picShare);
		playBtn = (Button) findViewById(R.id.autoPlay);
		currentPhoto = (TextView) findViewById(R.id.currentPhoto);
		topLinear = (RelativeLayout) findViewById(R.id.top_linear);
		bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
		parent = (RelativeLayout) findViewById(R.id.parent);
		backBtn.setOnClickListener(this);
		frontBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		setBtn = (Button) findViewById(R.id.picSet);
		setBtn.setOnClickListener(this);
//		openMenuBtn = (ImageButton) findViewById(R.id.openMenuBtn);
//		openMenuBtn.setOnClickListener(this);
		imageSwitcher.setOnTouchListener(this);
	}
	/**
	 * 数据初始化，根据mIndex 来判断当前显示的照片
	 */
	private void initData() {
		photoListInfo = (List<Map<String, Object>>) getIntent().getSerializableExtra("photoList");
		imgLocalUrl = getIntent().getStringExtra("localSDUrl");
		//以下这一段是整理数据，把原来列表中实际上不用的四张图片从列表中删除。
		//这里考虑到前面四张合成一张，所以列表中的第一张图片不删除，因为在照片列表中的第一张图片和集合中的不用在界面上显示出来的四张图片是相同的，所以可以加以利用这个条件，来实现让用户看起来像是所有照片连一起的。
//		if(photoListInfo.size()>1)
//			photoListInfo.remove(1);
//		if(photoListInfo.size()>3)
//			photoListInfo.remove(3);
//		if(photoListInfo.size()>3)
//			photoListInfo.remove(3);
		mIndex = getIntent().getIntExtra("index", 0);
//		//下面这一段是对索引进行处理 因为上面一段已经把列表中的数据改变了，所以传递过来的索引值也要改变。
//		 if(mIndex == 0){
//			mIndex = 0;
//		}else if(mIndex == 2 || mIndex == 3){
//			mIndex = mIndex-1;
//		}else{
//			mIndex = mIndex-3;
//		}
		System.out.println("传过来的mIndex ===== "+mIndex);
		imageSwitcher.removeAllViews();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if(pathes == null){
			pathes = new ArrayList<HashMap<String,Object>>();
		}else{
			pathes.clear();
		}
		HashMap<String, Object> map = null;
		for(int i = 0;i<photoListInfo.size();i++){
			map = new HashMap<String, Object>();
			 String imageid = photoListInfo.get(i).get("imageid").toString();
			 String imgPath = photoListInfo.get(i).get("imgpath")==null?"":photoListInfo.get(i).get("imgpath").toString();
		     String fileUrl = ConstantsValues.SDCARD_ROOT_PATH+imgLocalUrl+imageid+".png";
		     if(imgPath!=null && imgPath.length()>0){
		    	 fileUrl = imgPath;
		     }
		     File file = new File(fileUrl);
			 map.put("file", file);
			 pathes.add(map);
		}
//		pathes = MyApplication.getInstance(this).getPathes();
		
//		PageAdapter pageAdapter = new PageAdapter(this, pathes);
//		flipper.setAdapter(pageAdapter);
//		flipper.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int position, long id) {
//				mIndex=position;
//				currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size()
//						+ "张）");
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size()
//						+ "张）");
//			}
//		});
//		flipper.setSelection(mIndex);
		currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size()
				+ "张）");
		imageSwitcher.setFactory(this);
	}

	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (initWedget_tag) {
			findView();
			initWedget_tag = false;
		}
	}

	/**
	 * 初始设置弹出框
	 */
	private void showSetupDialog() {
		// PopupWindow浮动下拉框布局
		View bottomLayout = (View) this.getLayoutInflater().inflate(
				R.layout.dialog_setup, null);
		bottomLayout.findViewById(R.id.setupLayout1).setOnClickListener(this);
		bottomLayout.findViewById(R.id.setupLayout2).setOnClickListener(this);
		bottomLayout.findViewById(R.id.setupLayout3).setOnClickListener(this);
		bottomLayout.findViewById(R.id.setupLayout4).setOnClickListener(this);
		bottomLayout.findViewById(R.id.setupLayout5).setOnClickListener(this);
		bottomLayout.findViewById(R.id.setupLayout6).setOnClickListener(this);
		bottomLayout.findViewById(R.id.closeWindowBtn).setOnClickListener(this);
		if(photoListInfo.get(mIndex).get("type") == null || photoListInfo.get(mIndex).get("type").equals("1")){//美图欣赏图片  或者 店里拍摄的照片
			bottomLayout.findViewById(R.id.setupLayout5).setVisibility(View.GONE);
			bottomLayout.findViewById(R.id.line5).setVisibility(View.GONE);
		}else{//用户自己上传的照片
			bottomLayout.findViewById(R.id.setupLayout5).setVisibility(View.VISIBLE);
			bottomLayout.findViewById(R.id.line5).setVisibility(View.VISIBLE);
		}
		selectPopupWindow = new PopupWindow(bottomLayout, parent.getWidth(),
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
		selectPopupWindow.update();  
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss1() {
		flag = true;
		selectPopupWindow.dismiss();
	}
//	MediaPlayer mp = null;
	public void onClick(View v) {
		Intent i = new Intent();
		File file = null;
		try{
			file = (File) pathes.get(mIndex).get("file");
		}catch(Exception e){
			return;
		}
		if (v == frontBtn && click_limit && pathes != null
				&& pathes.size() >= 2) {
			goFrontPic();
		} else if (v == nextBtn && click_limit && pathes != null
				&& pathes.size() >= 2) {
			goNextPic(false);
		} else if (v == backBtn) {
			if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
    			finish();
    		}
		} else if (v == playBtn) {//播放
//			mp =MediaPlayer.create(this, R.raw.music);
//			mp.setLooping(true);
//			mp.start();
			topLinear.setVisibility(View.GONE);
			bottomLinear.setVisibility(View.GONE);
			timer = new Timer();
			myTimerTask = new MyTimerTask();
			timer.schedule(myTimerTask, 0, 4000);
			autoFLag = true;
//			imageSwitcher.setVisibility(View.VISIBLE);
//			flipper.setVisibility(View.INVISIBLE);
		} else if (v == shareBtn) {//分享功能
			showShareDialog(parent);
//			if (flag) {
//				// 显示PopupWindow窗口
//				flag = false;
//				popupWindwShowing();
//			} else {
//				flag = true;
//				dismiss();
//			}
		}else if(v.getId() == R.id.sharelayout1){//微博分享
			Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("filePath", ((File)pathes.get(mIndex).get("file")).getAbsolutePath());
			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare("菠萝菠萝蜜", ((File)pathes.get(mIndex).get("file")).getAbsolutePath());
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("filePath", ((File)pathes.get(mIndex).get("file")).getAbsolutePath());
			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.TENCENT_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout4){//邮件分享
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent("", ((File)pathes.get(mIndex).get("file")).getAbsolutePath());
		}else if(v.getId() == R.id.sharelayout5){//二维码分享
			
		}else if(v.getId() == R.id.sharelayout6){//更多分享
//			showMoreShareDialog();
			Intent intent = new Intent(Intent.ACTION_SEND);  
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile((File) pathes.get(mIndex).get("file")));  //传输图片或者文件 采用流的方式  
            intent.putExtra(Intent.EXTRA_TEXT, "分享分享微博");   //附带的说明信息  
            intent.putExtra(Intent.EXTRA_SUBJECT, "标题");  
            intent.setType("image/*");   //分享图片  
            startActivity(Intent.createChooser(intent,"分享")); 
		}else if(v.getId() == R.id.closeWindowBtn){
			dismiss1();
		}else if(v.getId() == R.id.closeWindowBtn1){
			dismiss();
		}else if(v.getId() == R.id.setupLayout1){//向左旋转
			angle -=90;
			rotatePic(angle);
		}else if(v.getId() == R.id.setupLayout2){//向右旋转
			angle +=90;
			rotatePic(angle);
		}else if(v.getId() == R.id.setupLayout3){//设置为壁纸
			dismiss1();
			setWallBitmap();
		}else if(v.getId() == R.id.setupLayout4){//设置为头像
			
		}else if(v.getId() == R.id.setupLayout5){//删除
			dismiss1();
			new Thread(deleteImgRunnable).start();
		}else if(v.getId() == R.id.setupLayout6){//详细信息
			
		}else if(v.getId() == R.id.picSet){//弹出设置框
			showSetupDialog();
		}/*else if(v == openMenuBtn){//打开获取图片菜单
			showGetPicDialog();
		}else if(v.getId() == R.id.paizhaoLayout){//拍照
			takePhoto();
		}else if(v.getId() == R.id.selectPhotoLayout){//从手机中选择
			getAlbumPhoto();
		}*/
	}
	/**
	 * 设置墙纸。
	 */
	private void setWallBitmap(){
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
		try {
			wallpaperManager.setBitmap(currentBitmap);
			System.out.println("设置墙纸成功。。。。。。。。");
		} catch (IOException e) {
			System.out.println("设置墙纸失败。。。。。。。。");
			e.printStackTrace();
		}
	}
	private int angle = 0;
//	/**
//	 * 通过调用自带照相机获取图片
//	 * @author 刘星星
//	 */
//	private void takePhoto(){
//			dismiss1();
//			Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			 fileName = CommonUtils.getFileName();
//			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
//			if(!file.exists()){
//				file.mkdirs();
//			}
//			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
//			path = sdcardTempFile.getAbsolutePath();
////			MyApplication.getInstance().setPhtotPath(path);
//			if (CommonUtils.isHasSdcard()) {
//				openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
//				openCamera.putExtra("return-data", true);
//			}
//			startActivityForResult(openCamera, 1);  
//	}
//	/**
//	 * 从相册获取图片
//	 * @author 刘星星
//	 */
//	private void getAlbumPhoto(){
//		dismiss1();
//		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//		getAlbum.setType(IMAGE_TYPE);
//		startActivityForResult(getAlbum, 2);
//	}
	/**
	 * 删除图片的线程
	 */
	Runnable deleteImgRunnable = new Runnable() {
		
		@Override
		public void run() {
			if(photoListInfo.get(mIndex).get("id") ==null || photoListInfo.get(mIndex).get("id").toString().length() == 0){
				File currentFile = (File) pathes.get(mIndex).get("file");
				DBHelper dbHelper = DBHelper.getInstance(ImageSwitcher.this);
				dbHelper.delete(UserPhotoInfo.TableName, "name=?", new String[]{currentFile.getName()});
				dbHelper.delete(UpLoadFileInfo.TableName,"name=?",new String[]{currentFile.getName()});
				if(currentFile.exists()){
					currentFile.delete();
				}
				pathes.remove(pathes.get(mIndex));
				photoListInfo.remove(mIndex);
				if(pathes.size()==0){//如果图片全部删除完
					handler.sendEmptyMessage(2);
				}else{
					if(mIndex>=pathes.size()-1){
						mIndex = pathes.size()-1;
					}
					handler.sendEmptyMessage(1);
				}
			}else{
				String photoId = photoListInfo.get(mIndex).get("id").toString();
				String msgStr = RequestServerFromHttp.deletePhoto(photoId);
				System.out.println("删除图片的返回值为："+msgStr);
				if(msgStr.startsWith("0")){//删除成功
					DBHelper dbHelper = DBHelper.getInstance(ImageSwitcher.this);
					dbHelper.delete(UserPhotoInfo.TableName, "id=?", new String[]{photoId});
					File currentFile = (File) pathes.get(mIndex).get("file");
					if(currentFile.exists()){
						currentFile.delete();
					}
					pathes.remove(pathes.get(mIndex));
					photoListInfo.remove(mIndex);
					if(pathes.size()==0){//如果图片全部删除完
						handler.sendEmptyMessage(2);
					}else{
						if(mIndex>=pathes.size()-1){
							mIndex = pathes.size()-1;
						}
						handler.sendEmptyMessage(1);
					}
					
				}else if(msgStr.equals("404")){//访问服务器失败
					
				}else if(msgStr.startsWith("-213")){//图片不存在
					DBHelper dbHelper = DBHelper.getInstance(ImageSwitcher.this);
					dbHelper.delete(UserPhotoInfo.TableName, "id=?", new String[]{photoId});
					File currentFile = (File) pathes.get(mIndex).get("file");
					if(currentFile.exists()){
						currentFile.delete();
					}
					pathes.remove(pathes.get(mIndex));
					photoListInfo.remove(mIndex);
					if(mIndex>=pathes.size()-1){
						mIndex = pathes.size()-1;
					}
					handler.sendEmptyMessage(1);
				}else{//删除失败
					
				}
			}
			
		}
	};
	/**
	 * 初始化PopupWindow
	 */
	private void showGetPicDialog() {
		// PopupWindow浮动下拉框布局
		View layout = (View) this.getLayoutInflater().inflate(
				R.layout.menu_myalbum, null);
		LinearLayout bottomLayout = (LinearLayout) layout.findViewById(R.id.bottomLayout);
		layout.findViewById(R.id.paizhaoLayout).setOnClickListener(this);
		layout.findViewById(R.id.selectPhotoLayout).setOnClickListener(this);
		layout.findViewById(R.id.closeWindowBtn).setOnClickListener(this);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (MyApplication.getInstant().getHeightPixels()*0.3));
		bottomLayout.setLayoutParams(param);
		selectPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(false);
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
		selectPopupWindow.update();  
	}
//	/***
//	 * 发送邮件
//	 */
//
//	private void startSendToEmailIntent() {
//		String contentDetails = "";
//
//		Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
//		intent.setType("text/plain");
//		intent.setType("image/*"); // 分享图片信息类型
//		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
//				intent, 0);
//
//		if (!resInfo.isEmpty()) {
//			List<Intent> targetedShareIntents = new ArrayList<Intent>();
//			for (ResolveInfo info : resInfo) {
//				Intent targeted = new Intent(Intent.ACTION_SEND);
//				targeted.setType("image/*"); // 分享图片信息类型
//				targeted.setType("text/plain");
//				targeted.putExtra(Intent.EXTRA_STREAM,
//						Uri.fromFile((File) pathes.get(mIndex).get("file")));
//				targeted.putExtra(Intent.EXTRA_TEXT, "");
//				targeted.putExtra(Intent.EXTRA_SUBJECT, "主题");
//				targeted.putExtra(Intent.EXTRA_EMAIL, new String[] {
//						"simple1@163.com", "simple2@163.com" });
//				ActivityInfo activityInfo = info.activityInfo;
//				if (activityInfo.packageName.contains("bluetooth")
//						|| activityInfo.name.contains("bluetooth")) {
//					continue; // 过滤蓝牙应用
//				}
//				if (activityInfo.packageName.contains("com.my.activity")
//						|| activityInfo.name.contains("com.my.activity")) {
//					continue; // 过滤自己的应用包
//				}
//				if (activityInfo.packageName.contains("gm")
//						|| activityInfo.name.contains("mail")) {
//					targeted.putExtra(Intent.EXTRA_TEXT, contentDetails);
//				} else if (activityInfo.packageName.contains("zxing")) {
//					continue; // 过滤自己的应用包
//				} else {
//					continue; // 过滤自己的应用包
//				}
//				targeted.setPackage(activityInfo.packageName);
//				targetedShareIntents.add(targeted);
//			}
//			// 分享框标题
//			if (targetedShareIntents != null && targetedShareIntents.size() > 0) {
//				Intent chooserIntent = Intent.createChooser(
//						targetedShareIntents.remove(0), "选择程序分享");
//				if (chooserIntent == null) {
//					return;
//				}
//				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
//						targetedShareIntents.toArray(new Parcelable[] {}));
//				try {
//					startActivity(chooserIntent);
//				} catch (android.content.ActivityNotFoundException ex) {
//					Intent i2 = new Intent(Intent.ACTION_VIEW,
//							Uri.parse("mailto:" + "simple@163.com"));
//					startActivity(i2);
//				}
//			} else {
//				Intent i3 = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"
//						+ "simple@163.com"));
//				startActivity(i3);
//			}
//
//		}
//	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (autoFLag) {
//				if(mp!=null){
//					mp.stop();
//				}
				if(!goon1){
					return false;
				}
				try {
					if (imageSwitcher.getAnimation() != null) {
						imageSwitcher.getAnimation().cancel();
					}
					imageSwitcher.getOutAnimation().cancel();
					imageSwitcher.getInAnimation().cancel();
					imageSwitcher.clearAnimation();

				} catch (Exception e) {
					e.printStackTrace();
				}
				goon = true;
				if (timer != null && myTimerTask != null) {
					timer.cancel();
					myTimerTask.cancel();
				}
				topLinear.setVisibility(View.VISIBLE);
				bottomLinear.setVisibility(View.VISIBLE);
				currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size()
						+ "张）");
				autoFLag = false;
			}else{
				if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
        			finish();
        		}
				
			}
		}
		return false;
	}

	/**
	 * author by Ring 处理页耗时动作
	 */
	public Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 打开进度条
				progressdialog = new ProgressDialog(ImageSwitcher.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_loading));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub
						return true;
					}
				});
				progressdialog.show();
//				flipper.setVisibility(View.GONE);
				break;
			case 2:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				
				break;
			case 3:// 加载页面
				initData();
				break;
			case 4://
				break;
			case 5://
				break;
			case 6:
				break;
			}
		};
	};

	
	
	@Override
	public View makeView() {
		 ImageView imageView = new ImageView(this);  
	        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        imageView.setLayoutParams(new android.widget.ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
	                        LayoutParams.MATCH_PARENT));
	        Bitmap bit = null;
	        
	        String imageid = photoListInfo.get(mIndex).get("imageid").toString();
	        String fileUrl = ConstantsValues.SDCARD_ROOT_PATH+imgLocalUrl+imageid+".png";
	        if(imageid.length() == 0 &&  photoListInfo.get(mIndex).get("imgpath")!=null){
	        	fileUrl = photoListInfo.get(mIndex).get("imgpath").toString();
	        }
	        System.out.println("mIndex === "+mIndex);
	        System.out.println("本图片的imageid ===="+imageid);
	        File file = new File(fileUrl);
	        if(file.exists()){//如果对应的imageId在本地存在图片  则直接使用本地图片
	        	System.out.println("此imageid对应的图片在本地存在");
	        	 bit = BitmapCache.getInstance().getBitmap(file, this);
	        	 Drawable drawable = new BitmapDrawable(bit);
	 			 imageView.setImageDrawable(drawable);
	        }else{//如果对应的imageId在本地没有图片
	        	DBHelper dbHelper = DBHelper.getInstance(this);
	        	String sql = "select localpath from "+UpLoadFileInfo.TableName+" where imageid='"+imageid+"' and progress='1'";
	        	List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
	        	if(list!=null && list.size()>0 && list.get(0)!=null){//查找上传表中的上传文件路径，  如果此路径在本地对应的图片还存在，则直接使用本地图片
	        		System.out.println("图片的本地路径为"+list.get(0).get("localpath").toString());
		        	File localFile = new File(list.get(0).get("localpath").toString());
		        	if(localFile.exists()){//存在
		        		System.out.println("此文件是本地上传的，而且本地还存在该图片");
		        		bit = BitmapCache.getInstance().getBitmap(localFile, this);
		        		Drawable drawable = new BitmapDrawable(bit);
		    			imageView.setImageDrawable(drawable);
		        	}else{//不存在的时候  去网上下载，先给控件放一张初始图片
		        		System.out.println("去服务器下载图片");
		        		imageView.setImageResource(R.drawable.photolist_defimg);
		        		if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
		        			RequestServerFromHttp.downImage(ImageSwitcher.this,imageView,imageid,ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
									MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
		        		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
		        			RequestServerFromHttp.downImage(ImageSwitcher.this,imageView,imageid,ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
									MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
		        		}
		        		
		        	}	
	        	}else{//如果上传表中没有对应的图片的数据，则直接下载该图
	        		System.out.println("去服务器下载图片");
	        		imageView.setImageResource(R.drawable.photolist_defimg);
	        		if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
	        			RequestServerFromHttp.downImage(ImageSwitcher.this,imageView,imageid,ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
								MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
	        		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
	        			RequestServerFromHttp.downImage(ImageSwitcher.this,imageView,imageid,ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
								MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
	        		}
	        	}	
	        }
			
	        return imageView;  
	}
	
	/**
	 * 自动播放
	 */
	@SuppressWarnings("deprecation")
	public void autoPlay() {
		if (mIndex < pathes.size() - 1) {
			mIndex += 1;

		} else {
			mIndex = 0;
		}
		currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size() + "张）");
		File file = (File) pathes.get(mIndex).get("file");
		boolean b = true;
		Bitmap bit = null;
		if(file.exists()&&file.isFile()){
			bit = BitmapCache.getInstance().getBitmap(file, this);
			if(bit!=null){
				currentBitmap = bit;
				b=false;
			}
		}
		if(b){
			if (autoFLag) {
				try{
					if(imageSwitcher.getAnimation()!=null){
						imageSwitcher.getAnimation().cancel();
					}
					imageSwitcher.getOutAnimation().cancel();
					imageSwitcher.getInAnimation().cancel();
					imageSwitcher.clearAnimation();
					
				}catch(Exception e){
					
				}
				autoFLag = false;
				goon = true;
				if (timer != null && myTimerTask != null) {
					timer.cancel();
					myTimerTask.cancel();
				}
				topLinear.setVisibility(View.VISIBLE);
				bottomLinear.setVisibility(View.VISIBLE);
				currentPhoto.setText("第" + (mIndex + 1) + "张（共"
						+ pathes.size() + "张）");
//				flipper.setVisibility(View.VISIBLE);
//				flipper.setSelection(mIndex);
//				imageSwitcher.setVisibility(View.GONE);
			}
			
			MessageBox.CreateAlertDialog("提示", "后面的图片还未下载哦，请先下载吧~", this);
			
			return ;
		}
		Drawable drawable = new BitmapDrawable(bit);
		int count = new Random().nextInt(ConstantsValues.ANIMRESIN.length+2);
		final float centerX = bit.getWidth() / 2.0f;
		final float centerY = bit.getHeight() / 2.0f;
		if(count == ConstantsValues.ANIMRESIN.length){
			Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90,
					centerX, centerY, 310.0f, true,true);
			rotation.setDuration(1000);
			rotation.setFillAfter(true);
			rotation.setFillEnabled(true);
			rotation.setFillBefore(false);
			rotation.setAnimationListener(new DisplayNextView(imageSwitcher.getCurrentView(),imageSwitcher.getNextView(),true,310.0f));
			rotation.setInterpolator(new AccelerateInterpolator());
			imageSwitcher.setOutAnimation(rotation);
			AlphaAnimation alphamin = new AlphaAnimation(0, 0);
			imageSwitcher.setInAnimation(alphamin);
			imageSwitcher.setImageDrawable(drawable);
		}else if(count == ConstantsValues.ANIMRESIN.length+1){
			Rotate3dAnimation rotation = new Rotate3dAnimation(0, 90,
					centerX, centerY, 340.0f, true,false);
			rotation.setDuration(1000);
			rotation.setFillAfter(true);
			rotation.setFillEnabled(true);
			rotation.setFillBefore(false);
			rotation.setAnimationListener(new DisplayNextView(imageSwitcher.getCurrentView(),imageSwitcher.getNextView(),false,340.0f));
			rotation.setInterpolator(new AccelerateInterpolator());
			imageSwitcher.setOutAnimation(rotation);
			AlphaAnimation alphamin = new AlphaAnimation(0, 0);
			imageSwitcher.setInAnimation(alphamin);
			imageSwitcher.setImageDrawable(drawable);
		}else if(count < ConstantsValues.ANIMRESIN.length){
			int residin = ConstantsValues.ANIMRESIN[new Random().nextInt(ConstantsValues.ANIMRESIN.length)];
			int residout = ConstantsValues.ANIMRESOUT[new Random().nextInt(ConstantsValues.ANIMRESOUT.length)];
			imageSwitcher.setInAnimation(this, residin);
			imageSwitcher.setOutAnimation(this, residout);
			imageSwitcher.setImageDrawable(drawable);
			goon = true;
		}
	}
	
	/**
	 * 动画执行过程监听器
	 * 
	 * @author Jin Bin Bin
	 * 
	 */
	public class DisplayNextView implements Animation.AnimationListener {
		View mView1;
		View mView2;
		float mcenterZ;
		boolean mflag;

		public DisplayNextView(View view1,View view2,boolean flag,float centerZ) {
			this.mView1 = view1;
			this.mView2 = view2;
			this.mcenterZ = centerZ;
			this.mflag = flag;
		}

		public void onAnimationStart(Animation animation) {
			goon1 = false;
			mView2.setVisibility(View.GONE);
			mView1.setVisibility(View.VISIBLE);
		}

		public void onAnimationEnd(Animation animation) {
			mView1.setVisibility(View.GONE);
			mView2.setVisibility(View.GONE);
			mView2.post(new SwapViews(mView1,mView2, 270, 360,mflag,mcenterZ));
		}

		public void onAnimationRepeat(Animation animation) {

		}
	}

	/**
	 * 执行异步动画线程类
	 * 
	 * @author Jin Bin Bin
	 * 
	 */

	private final class SwapViews implements Runnable, AnimationListener {
		View mView2;
		float mStart;
		float mEnd;
		boolean mflag;
		float mcenterZ;

		public SwapViews(View view1,View view2, float start, float end,boolean flag,float centerZ) {
			mView2 = view2;
			mStart = start;
			mEnd = end;
			mflag = flag;
			mcenterZ = centerZ;
		}

		public void run() {
			final float centerX = MyApplication.getInstant().getWidthPixels() / 2.0f;
			final float centerY = MyApplication.getInstant().getHeightPixels() / 2.0f;
			
			Rotate3dAnimation rotation = new Rotate3dAnimation(mStart, mEnd,
					centerX, centerY, mcenterZ, false,mflag);

			rotation.setDuration(1000);
			rotation.setFillAfter(true);
			rotation.setFillEnabled(true);
			rotation.setAnimationListener(this);
			AlphaAnimation alphamin = new AlphaAnimation(0, 0);
			imageSwitcher.setOutAnimation(alphamin);
			imageSwitcher.startAnimation(rotation);

		}

		public void onAnimationStart(Animation animation) {
			goon1 = true;
			if(!autoFLag){
				animation.cancel();
			}
			mView2.setVisibility(View.VISIBLE);
		}

		public void onAnimationEnd(Animation animation) {
			goon = true;
		}

		public void onAnimationRepeat(Animation animation) {

		}
	}
	/**
	 * 自动播放的计时器
	 */
	MyTimerTask myTimerTask = null;

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			if(goon){
				goon = false;
				autoShowPic();
			}
		}
	};
	
	/**
	 * 自动播放图片
	 */
	private void autoShowPic() {
		handler.sendEmptyMessage(0);
	}
	
	/**
	 * 更新UI界面
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				autoPlay();
			}else if(msg.what == 1){
				goNextPic(true);
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(autoFLag){
			return true;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX(); // 取得按下时的坐标
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            upX = (int) event.getX(); // 取得松开时的坐标
            // 从左拖到右，即看前一张
            if (upX - downX > 100) {
               goFrontPic();
            } else if (downX - upX > 100) { // 从右拖到左，即看后一张
            	goNextPic(false);
            }
            return true;
        }
        return false;
	}
	
	/**
	 * 查看上一张图片
	 */

	public void goFrontPic() {
		if (mIndex <= 0) {
			return;
		} else {
			mIndex--;
		}
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				ImageSwitcher.this, android.R.anim.slide_in_left));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				ImageSwitcher.this, android.R.anim.slide_out_right));
		currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size() + "张）");
		File file = (File) pathes.get(mIndex).get("file");
		boolean b = true;
		Bitmap bit = null;
		if (file.exists() && file.isFile()) {
			bit = BitmapCache.getInstance().getBitmap(file, ImageSwitcher.this);
			if (bit != null) {
				currentBitmap = bit;
				b = false;
				Drawable drawable = new BitmapDrawable(bit);
				imageSwitcher.setImageDrawable(drawable);
			}
		}else{
			imageSwitcher.setImageResource(R.drawable.photolist_defimg);
			if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
				   RequestServerFromHttp.downImage(ImageSwitcher.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
       		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
       			RequestServerFromHttp.downImage(ImageSwitcher.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
       		}
			}
		
	}

	/**
	 * 查看下一张图片
	 */

	public void goNextPic(boolean isDeletePic) {
		if(!isDeletePic){
			if (mIndex >= pathes.size() - 1) {
				return;
			} else {
				mIndex++;
			}
		}else{
			if (mIndex > pathes.size() - 1) {
				return;
			} 
		}
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				ImageSwitcher.this, R.anim.slide_in_right));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				ImageSwitcher.this, R.anim.slide_out_left));
		currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size() + "张）");
		File file = (File) pathes.get(mIndex).get("file");
		boolean b = true;
		Bitmap bit = null;
		if (file.exists() && file.isFile()) {
			bit = BitmapCache.getInstance().getBitmap(file, ImageSwitcher.this);
			if (bit != null) {
				currentBitmap = bit;
				b = false;
				Drawable drawable = new BitmapDrawable(bit);
				imageSwitcher.setImageDrawable(drawable);
			}
		}else{
			imageSwitcher.setImageResource(R.drawable.photolist_defimg);
			if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
				   RequestServerFromHttp.downImage(ImageSwitcher.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
    		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
    			RequestServerFromHttp.downImage(ImageSwitcher.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
    		}
		}
		
	}
	
	
	/**
	 * 查看下一张图片
	 */

	public void rotatePic(int angle) {
		File file = (File) pathes.get(mIndex).get("file");
		boolean b = true;
		Bitmap bit = null;
		if (file.exists() && file.isFile()) {
			bit = BitmapCache.getInstance().getBitmap(file, ImageSwitcher.this);
			if (bit != null) {
				b = false;
			}
		}
		// Getting width & height of the given image.  
		int w = bit.getWidth();  
		int h = bit.getHeight();  
		// Setting post rotate to 90  
		Matrix mtx = new Matrix();  
		mtx.postRotate(angle);  
		// Rotating Bitmap  
		Bitmap rotatedBMP = Bitmap.createBitmap(bit, 0, 0, w, h, mtx, true);  
		Drawable drawable = new BitmapDrawable(rotatedBMP);
		imageSwitcher.clearAnimation();
		imageSwitcher.setImageDrawable(drawable);
	}
}
