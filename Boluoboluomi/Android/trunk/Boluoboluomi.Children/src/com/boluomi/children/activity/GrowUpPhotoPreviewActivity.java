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
 * @Author: liuxingxing
 * @Email: xingxing@qingfengweb.com
 * @CreateDate: 2012-11-16
 * @Description: 相册类，包括自动播放和手动切换照片。并实现分享功能
 * @Others: comments
 * @ModifyHistory:
 */
public class GrowUpPhotoPreviewActivity extends BaseActivity implements ViewFactory,OnClickListener,OnTouchListener{
	private int mIndex = 0;
	private int mItemwidth;
	private int mItemHerght;
	private ArrayList<HashMap<String, Object>> pathes;
	private List<Map<String,Object>> photoListInfo = null;
	private Button backBtn;// 上一张 下一张 返回按钮
																	// 分享按钮
																	// 自动播放按钮
	private Timer timer = null;
	private Dialog alert = null;
	public boolean initWedget_tag = true;
	private boolean flag = true;
	private GestureDetector detector;// 控件的触屏事件监听
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
	ImageButton shareBtn;
	LinearLayout parent;
	TextView photoName,titleTv;
	
	/**
	 * 此类mIndex是一个重要的变量，根据此变量可以找到当前图片在集合中所保存的数据
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_growupphotopreview);
		findView();
		mIndex = 0;
		initData();
	}
	private void findView() {
//		flipper = (UGallery) findViewById(R.id.vf);
		imageSwitcher = (android.widget.ImageSwitcher) findViewById(R.id.imageswitch);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		imageSwitcher.setOnTouchListener(this);
		shareBtn = (ImageButton) findViewById(R.id.shareBtn);
		shareBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		photoName = (TextView) findViewById(R.id.photoName);
		titleTv = (TextView) findViewById(R.id.title);
		titleTv.setText(getIntent().getStringExtra("growupName"));
	}
	/**
	 * 数据初始化，根据mIndex 来判断当前显示的照片
	 */
	@SuppressWarnings("unchecked")
	private void initData() {
		photoListInfo = (List<Map<String, Object>>) getIntent().getSerializableExtra("photoList");
		imgLocalUrl = getIntent().getStringExtra("localSDUrl");
		mIndex = getIntent().getIntExtra("index", 0);
		System.out.println("传过来的mIndex ===== "+mIndex);
		imageSwitcher.removeAllViews();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if(pathes == null){
			pathes = new ArrayList<HashMap<String,Object>>();
		}else{
			pathes.clear();
		}
		if(photoListInfo==null || photoListInfo.size()==0){
			photoListInfo = new ArrayList<Map<String,Object>>();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("imgpath",  ConstantsValues.SDCARD_ROOT_PATH+"/boluomiversion2/myalbum/665.png");
			photoListInfo.add(map);
			Map<String,Object> map1 = new HashMap<String,Object>();
			map1.put("imgpath",  ConstantsValues.SDCARD_ROOT_PATH+"/boluomiversion2/myalbum/675.png");
			photoListInfo.add(map1);
			Map<String,Object> map2 = new HashMap<String,Object>();
			map2.put("imgpath",  ConstantsValues.SDCARD_ROOT_PATH+"/boluomiversion2/myalbum/676.png");
			photoListInfo.add(map2);
		}
		HashMap<String, Object> map = null;
		for(int i = 0;i<photoListInfo.size();i++){
			map = new HashMap<String, Object>();
//			 String imageid = photoListInfo.get(i).get("imageid").toString();
			 String imgPath = photoListInfo.get(i).get("imgpath")==null?"":photoListInfo.get(i).get("imgpath").toString();
		     String fileUrl = "";
		     if(imgPath!=null && imgPath.length()>0){
		    	 fileUrl = imgPath;
		     }
		     File file = new File(fileUrl);
			 map.put("file", file);
			 pathes.add(map);
		}
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

	MediaPlayer mp = null;
	public void onClick(View v) {
		Intent i = new Intent();
		File file = null;
		try{
			file = (File) pathes.get(mIndex).get("file");
		}catch(Exception e){
			return;
		}
		if (v == backBtn) {
			finish();
//			if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
//    			finish();
//    		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
//    			Intent intent = new Intent(this,PhotoListActivity.class);
//				startActivity(intent);
//				finish();
//    		}
		}else if(v == shareBtn){//分享按钮
			showShareDialog(parent);
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
		}else if(v.getId() == R.id.closeWindowBtn1){
			dismiss();
		}
	}
	/**
	 * 删除图片的线程
	 */
	Runnable deleteImgRunnable = new Runnable() {
		
		@Override
		public void run() {
			if(photoListInfo.get(mIndex).get("id") ==null || photoListInfo.get(mIndex).get("id").toString().length() == 0){
				File currentFile = (File) pathes.get(mIndex).get("file");
				DBHelper dbHelper = DBHelper.getInstance(GrowUpPhotoPreviewActivity.this);
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
					DBHelper dbHelper = DBHelper.getInstance(GrowUpPhotoPreviewActivity.this);
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
					DBHelper dbHelper = DBHelper.getInstance(GrowUpPhotoPreviewActivity.this);
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
				finish();
				/*if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
        			finish();
        		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
        			Intent intent = new Intent(this,PhotoListActivity.class);
    				startActivity(intent);
    				finish();
        		}*/
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
				progressdialog = new ProgressDialog(GrowUpPhotoPreviewActivity.this);
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
		 Map<String,Object> map = photoListInfo.get(mIndex);
		 String remark = map.get("photoname").toString();
		 photoName.setText(remark);
		 ImageView imageView = new ImageView(this);  
	        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        imageView.setLayoutParams(new android.widget.ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
	                        LayoutParams.MATCH_PARENT));
	        Bitmap bit = null;
	        
	        String imageid = "";
	        String fileUrl = ConstantsValues.SDCARD_ROOT_PATH+imgLocalUrl+imageid+".png";
	        if(imageid.length() == 0 &&  photoListInfo.get(mIndex).get("imgpath")!=null){
	        	fileUrl = map.get("imgpath").toString();
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
		        			RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,imageView,imageid,ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
									MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
		        		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
		        			RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,imageView,imageid,ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
									MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
		        		}
		        		
		        	}	
	        	}else{//如果上传表中没有对应的图片的数据，则直接下载该图
	        		System.out.println("去服务器下载图片");
	        		imageView.setImageResource(R.drawable.photolist_defimg);
	        		if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
	        			RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,imageView,imageid,ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
								MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
	        		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
	        			RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,imageView,imageid,ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
								MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
	        		}
	        	}	
	        }
			
	        return imageView;  
	}
	
	
	/**
	 * 更新UI界面
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
			}else if(msg.what == 1){
				goNextPic(true);
			}
			
			super.handleMessage(msg);
		}

	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
				GrowUpPhotoPreviewActivity.this, android.R.anim.slide_in_left));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				GrowUpPhotoPreviewActivity.this, android.R.anim.slide_out_right));
		File file = (File) pathes.get(mIndex).get("file");
		boolean b = true;
		Bitmap bit = null;
		if (file.exists() && file.isFile()) {
			bit = BitmapCache.getInstance().getBitmap(file, GrowUpPhotoPreviewActivity.this);
			if (bit != null) {
				currentBitmap = bit;
				b = false;
				Drawable drawable = new BitmapDrawable(bit);
				imageSwitcher.setImageDrawable(drawable);
			}
		}else{
			imageSwitcher.setImageResource(R.drawable.photolist_defimg);
			if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
				   RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
       		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
       			RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
       		}
			}
		 Map<String,Object> map = photoListInfo.get(mIndex);
		 String remark = map.get("photoname").toString();
		 photoName.setText(remark);
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
				GrowUpPhotoPreviewActivity.this, R.anim.slide_in_right));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				GrowUpPhotoPreviewActivity.this, R.anim.slide_out_left));
		File file = (File) pathes.get(mIndex).get("file");
		boolean b = true;
		Bitmap bit = null;
		if (file.exists() && file.isFile()) {
			bit = BitmapCache.getInstance().getBitmap(file, GrowUpPhotoPreviewActivity.this);
			if (bit != null) {
				currentBitmap = bit;
				b = false;
				Drawable drawable = new BitmapDrawable(bit);
				imageSwitcher.setImageDrawable(drawable);
			}
		}else{
			imageSwitcher.setImageResource(R.drawable.photolist_defimg);
			if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
				   RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
    		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
    			RequestServerFromHttp.downImage(GrowUpPhotoPreviewActivity.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
    		}
		}
		 Map<String,Object> map = photoListInfo.get(mIndex);
		 String remark = map.get("photoname").toString();
		 photoName.setText(remark);
	}
	
	
	/**
	 * 查看下一张图片
	 */

	public void rotatePic(int angle) {
		File file = (File) pathes.get(mIndex).get("file");
		boolean b = true;
		Bitmap bit = null;
		if (file.exists() && file.isFile()) {
			bit = BitmapCache.getInstance().getBitmap(file, GrowUpPhotoPreviewActivity.this);
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
