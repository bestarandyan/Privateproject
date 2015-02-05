/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.qingfengweb.weddingideas.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher.ViewFactory;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.customview.Rotate3dAnimation;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;
import com.qingfengweb.weddingideas.share.ConstantsValuesShare;
import com.qingfengweb.weddingideas.share.EmailShare;
import com.qingfengweb.weddingideas.share.SMSShare;
import com.qingfengweb.weddingideas.share.WechatShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * @Filename: ImageSwitcher.java
 * @Author: 刘星星
 * @CreateDate: 2014-1-3
 * @Description: 相册类，包括自动播放和手动切换照片。并实现分享功能
 * @Others: comments
 * @ModifyHistory:
 */
@SuppressLint("HandlerLeak") @TargetApi(Build.VERSION_CODES.FROYO) public class ImageSwitcher extends BaseActivity implements ViewFactory,OnClickListener,OnTouchListener{
	private int mIndex = 0;
	public boolean isPlay = false;//判断是否自动播放
//	private List<Map<String,Object>> photoListInfo = null;
	private ImageButton /*frontBtn, nextBtn, backBtn, */shareBtn, playBtn;// 上一张 下一张 返回按钮
																	// 分享按钮
																	// 自动播放按钮
	private LinearLayout bottomLinear;// 底部布局
	private Timer timer = null;
	public boolean initWedget_tag = true;
	private RelativeLayout parent = null;
	private boolean autoFLag = false;// 用来判断当前状态是否为自动播放状态
	private android.widget.ImageSwitcher imageSwitcher = null;
	private Bitmap bitmap = null;
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
	private boolean goon = true;
	private boolean goon1 = true;
	int downX = 0;
	int upX = 0;
	Tencent tencent;
	private int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
	
	private int mExtarFlag = 0x00;
	private RelativeLayout topLayout;
	/**
	 * 此类mIndex是一个重要的变量，根据此变量可以找到当前图片在集合中所保存的数据
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_imageswitcher);
		findView();
		mIndex = 0;
		initData();
	}
	private void findView() {
//		flipper = (UGallery) findViewById(R.id.vf);
		imageSwitcher = (android.widget.ImageSwitcher) findViewById(R.id.imageswitch);
		topLayout = (RelativeLayout) findViewById(R.id.topLayout);
//		frontBtn = (ImageButton) findViewById(R.id.picFront);
//		nextBtn = (ImageButton) findViewById(R.id.picNext);
//		backBtn = (ImageButton) findViewById(R.id.backBtn);
		shareBtn = (ImageButton) findViewById(R.id.picShare);
		playBtn = (ImageButton) findViewById(R.id.autoPlay);
//		currentPhoto = (TextView) findViewById(R.id.currentPhoto);
//		topLinear = (RelativeLayout) findViewById(R.id.top_linear);
//		bottomLinear = (LinearLayout) findViewById(R.id.bottomLinear);
		parent = (RelativeLayout) findViewById(R.id.parent);
//		backBtn.setOnClickListener(this);
//		frontBtn.setOnClickListener(this);
//		nextBtn.setOnClickListener(this);
		playBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
//		setBtn = (Button) findViewById(R.id.picSet);
//		setBtn.setOnClickListener(this);
//		openMenuBtn = (ImageButton) findViewById(R.id.openMenuBtn);
//		openMenuBtn.setOnClickListener(this);
		imageSwitcher.setOnTouchListener(this);
		findViewById(R.id.backBtn).setOnClickListener(this);
	}
	
	
	@Override
	protected void onDestroy() {
		imageSwitcher.destroyDrawingCache();
		imageSwitcher.removeAllViews();
//		if(photoListInfo!=null){
//			photoListInfo.clear();
//		}
		if(bitmap !=null){
//			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
		super.onDestroy();
	}
	/**
	 * 数据初始化，根据mIndex 来判断当前显示的照片
	 */
	private void initData() {
		tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, ImageSwitcher.this);
		mIndex = getIntent().getIntExtra("index", 0);
		isPlay = getIntent().getBooleanExtra("autoPlay", false);
		imageSwitcher.removeAllViews();
		imageSwitcher.setFactory(this);
		new Thread(getListData).start();
		
	}
	Runnable getListData = new Runnable() {
		
		@Override
		public void run() {
//			if(photoListInfo == null || photoListInfo.size() == 0){
//				photoListInfo = new ArrayList<Map<String,Object>>();
//				getPhotoListData(photoListInfo);
//			}
			if(MainVideoActivity.photoListInfo!=null && MainVideoActivity.photoListInfo.size()>0){
				handler.sendEmptyMessage(3);
			}
		}
	};
	private Bitmap getBitmap(String string){
//		ImageView imageView = new ImageView(this);
//		imageView.setAdjustViewBounds(true);
		try {
			InputStream is = this.getAssets().open(string);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
			BitmapFactory.decodeStream(is, null, opts);
			opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,800 * 800);
			opts.inJustDecodeBounds = false;
			// 如果图片还未回收，先强制回收该图片
			bitmap = BitmapFactory.decodeStream(is, null, opts);
			if (bitmap != null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				int newWidth = MyApplication.getInstance().getScreenW() - 20;
				// 计算缩放比例
				float scaleWidth = ((float) newWidth) / width;
				// 取得想要缩放的matrix参数
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleWidth);
				// 得到新的图片
				bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
			}
//			if(bitmap!=null){
//				imageView.setImageBitmap(bitmap);
//			}else{
//				return null;
//			}
			is.close();
			System.gc();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return bitmap;
	}
	private void getPhotoListData(List<Map<String,Object>> photoList){
		String path = "photos";
        try {
            String str[] = this.getAssets().list(path);
            if (str.length > 0) {//如果是目录
                for (String string : str) {
                    bitmap =  getBitmap(path + "/" + string);
                    if(bitmap!=null){
                    	 Map<String,Object> map = new HashMap<String, Object>();
                 		map.put("imageid", "");
                 		map.put("bitmap", bitmap);
                 		photoList.add(map);
                    }
                }
            } else {//如果是文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
	 * PopupWindow消失
	 */
	public void dismiss1() {
		selectPopupWindow.dismiss();
	}
	
	public void onClick(View v) {
//		Intent i = new Intent();
//		File file = null;
//		try{
//			file = (File) photoListInfo.get(mIndex).get("file");
//		}catch(Exception e){
//			return;
//		}
		/*if (v == frontBtn && click_limit && pathes != null
				&& pathes.size() >= 2) {
			goFrontPic();
		} else if (v == nextBtn && click_limit && pathes != null
				&& pathes.size() >= 2) {
			goNextPic(false);
		} else if (v == backBtn) {
			
		} else */if (v == playBtn) {//播放
			MainVideoActivity.mp.start();
//			topLinear.setVisibility(View.GONE);
//			bottomLinear.setVisibility(View.GONE);
			playBtn.setVisibility(View.GONE);
			shareBtn.setVisibility(View.GONE);
			timer = new Timer();
			myTimerTask = new MyTimerTask();
			timer.schedule(myTimerTask, 0, 4000);
			autoFLag = true;
			topLayout.setVisibility(View.GONE);
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
		}else if(v.getId() == R.id.backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v.getId() == R.id.sharelayout1){//微博分享
			Intent intent = new Intent(this,ShareActivity.class);
			if(new File(ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png").exists()){
				  intent.putExtra("filePath", ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png");
	            }
			intent.putExtra("msgStr", MainVideoActivity.shareText);
			intent.putExtra("type", ConstantsValuesShare.SINA_TYPE);
			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout2){//短信分享
			SMSShare shareSDK = new SMSShare(this);
			shareSDK.startSMSShare(MainVideoActivity.shareText, "");
		}else if(v.getId() == R.id.sharelayout3){//QQ分享
			final Bundle params = new Bundle();
        	params.putString(Tencent.SHARE_TO_QQ_TITLE, LoadingActivity.configList.get(0).get("share_title"));
            params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, MainVideoActivity.shareUrl);
            params.putString(Tencent.SHARE_TO_QQ_SUMMARY, MainVideoActivity.shareText);
            if(new File(ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png").exists()){
            	params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, ConstantValues.sdcardUrl+ConstantValues.weddingIdeasIMgUrl+"shareimg.png");
            }
//        params.putString(shareType == Tencent.SHARE_TO_QQ_TYPE_IMAGE ? Tencent.SHARE_TO_QQ_IMAGE_LOCAL_URL 
//        		: Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrl.getText().toString());
        params.putString(Tencent.SHARE_TO_QQ_APP_NAME, "weddingideas");
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);
        params.putInt(Tencent.SHARE_TO_QQ_EXT_INT, mExtarFlag);
       /* if (shareType == Tencent.SHARE_TO_QQ_TYPE_AUDIO) {
            params.putString(Tencent.SHARE_TO_QQ_AUDIO_URL, mEditTextAudioUrl.getText().toString());
        }*/
        doShareToQQ(params);
//			Intent intent = new Intent(this,ShareActivity.class);
//			intent.putExtra("filePath", "");
//			intent.putExtra("msgStr", "伴随着春天脚步的临近，张三丰&杨采妮即将走入婚姻的殿堂。幸福即将起航，我们在此诚挚的邀请您共同见证！");
//			intent.putExtra("type", com.qingfengweb.share.ConstantsValues.TENCENT_TYPE);
//			startActivity(intent);
		}else if(v.getId() == R.id.sharelayout4){//邮件分享
			EmailShare emailShare = new EmailShare(this);
			emailShare.startSendToEmailIntent(MainVideoActivity.shareText, "");
		}else if(v.getId() == R.id.sharelayout5){//二维码分享
			Intent intent = new Intent(this,ErweimaShareActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(v.getId() == R.id.sharelayout6){//微信
			showWechatShareDialog();
//			showMoreShareDialog();
//			Intent intent = new Intent(Intent.ACTION_SEND);  
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
////            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile((File) pathes.get(mIndex).get("file")));  //传输图片或者文件 采用流的方式  
//            intent.putExtra(Intent.EXTRA_TEXT, "分享分享微博");   //附带的说明信息  
//            intent.putExtra(Intent.EXTRA_SUBJECT, "标题");  
//            intent.setType("image/*");   //分享图片  
//            startActivity(Intent.createChooser(intent,"分享")); 
		}else if(v.getId() == R.id.closeWindowBtn1){//关闭对话框按钮
			selectPopupWindow.dismiss();
		}else if(v.getId() == R.id.shareToGoodFriend){//分享到好友
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize(null,null)){
					wechatShare.shareMSG(LoadingActivity.configList.get(0).get("share_title"), MainVideoActivity.shareText, R.drawable.img_share_photo, MainVideoActivity.shareUrl, 1);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
		}else if(v.getId() == R.id.shareToFriends){//分享到朋友网
			wechatDialog.dismiss();
			WechatShare wechatShare = new WechatShare(this);
			if(wechatShare.isAuthorize()){
				if(wechatShare.isAuthorize()){
					wechatShare.shareMSG(LoadingActivity.configList.get(0).get("share_title"), MainVideoActivity.shareText, R.drawable.img_share_photo, MainVideoActivity.shareUrl, 2);
				}
			}else{
				WechatShare.showAlert(this,"未检测到微信客户端，请先安装","提示：");
			}
			
		}else if(v.getId() == R.id.cancle){
			wechatDialog.dismiss();
		}
	}
	 /**
     * 用异步方式启动分享
     * @param params
     */
    private void doShareToQQ(final Bundle params) {
        final Tencent tencent = Tencent.createInstance(ConstantsValuesShare.TENCENT_ID, ImageSwitcher.this);
        
        final Activity activity = ImageSwitcher.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                tencent.shareToQQ(activity, params,null /*new IUiListener() {

                    @Override
                    public void onComplete(JSONObject response) {
                        Util.toastMessage(activity, "onComplete: " + response.toString());
                    }

                    @Override
                    public void onError(UiError e) {
                        Util.toastMessage(activity, "onComplete: " + e.errorMessage, "e");
                    }

                    @Override
                    public void onCancel() {
//                    	if(shareType != Tencent.SHARE_TO_QQ_TYPE_IMAGE){
//                    		Util.toastMessage(activity, "onCancel: ");
//                    	}
                    }

                }*/);
            }
        }).start();
    }
	  /**
     * 用异步方式启动分享
     * @param params
     */
    public void doShareToQzone(final Bundle params) {
        final Activity activity = ImageSwitcher.this;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
            	tencent.shareToQzone(activity, params, new IUiListener() {

                    @Override
                    public void onCancel() {
//                        Util.toastMessage(activity, "onCancel: ");
                    }

                    @Override
                    public void onComplete(JSONObject response) {
//                        Util.toastMessage(activity, "onComplete: " + response.toString());
                    }

                    @Override
                    public void onError(UiError e) {
//                        Util.toastMessage(activity, "onComplete: " + e.errorMessage, "e");
                    }

                });
            }
        }).start();
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (autoFLag) {
				stopPlay();
				
			}else{
				finish();
				overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
			}
		}
		return false;
	}
	/**
	 * 停止播放
	 * @return
	 */
	private boolean stopPlay(){
				if(MainVideoActivity.mp!=null && MainVideoActivity.mp.isPlaying()){
					MainVideoActivity.mp.pause();
				}
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
		//	topLinear.setVisibility(View.VISIBLE);
			playBtn.setVisibility(View.VISIBLE);
			shareBtn.setVisibility(View.VISIBLE);
			topLayout.setVisibility(View.VISIBLE);
		//	currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size()
		//			+ "张）");
			autoFLag = false;
			return false;
	}
	
	
	@Override
	public View makeView() {
		 ImageView imageView = new ImageView(this);  
	        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
	        imageView.setLayoutParams(new android.widget.ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
	                        LayoutParams.MATCH_PARENT));
	        if(mIndex>=MainVideoActivity.photoListInfo.size()){
	        	imageView.setImageResource(R.drawable.img_default);
	        }else{
	        	imageView.setImageBitmap((Bitmap) MainVideoActivity.photoListInfo.get(mIndex).get("bitmap"));
	        }
//	        if(photoListInfo!=null && photoListInfo.size()>0){
//	        	 bitmap = (Bitmap) photoListInfo.get(mIndex).get("bitmap");
//	 		    imageView.setImageBitmap(bitmap);
//	        }else{
//	        	String path = "photos";
//	                String str[];
//					try {
//						str = this.getAssets().list(path);
//						if (str.length > 0) {//如果是目录
//							  bitmap =  getBitmap(path + "/" + str[mIndex]);
//							  imageView.setImageBitmap(bitmap);
//		                }
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//	                
//	        }
	        return imageView;  
	}
	int animFlag = 0;//动画方案
	/**
	 * 自动播放
	 */
	@SuppressWarnings("deprecation")
	public void autoPlay() {
		if (mIndex < MainVideoActivity.photoListInfo.size() - 1) {
			mIndex += 1;
		} else{
//			if ( MainVideoActivity.typeUser.equals("1")) {//只有在用户为主人的情况下才进入婚嫁日志
//				stopPlay();
//				if(MainVideoActivity.mp!=null && MainVideoActivity.mp.isPlaying()){
//					MainVideoActivity.mp.pause();
//				}
//				topLayout.setVisibility(View.VISIBLE);
//				Intent intent = new Intent(this,MarryLogActivity.class);
//				startActivity(intent);
//				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
//				return;
//			}else if(MainVideoActivity.typeUser.equals("0")){//客人
				mIndex = 0;
				if(animFlag < ConstantValues.ANIMRESIN.length-1){
					animFlag++;
				}else{
					animFlag = 0;
//				}
			} 
		}
		topLayout.setVisibility(View.GONE);
		boolean b = true;
		Bitmap bit =(Bitmap) MainVideoActivity.photoListInfo.get(mIndex).get("bitmap");
			if(bit!=null){
				b=false;
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
				bottomLinear.setVisibility(View.VISIBLE);
			}
			return ;
		}
		Drawable drawable = new BitmapDrawable(bit);
		int count = new Random().nextInt(ConstantValues.ANIMRESIN.length);
		if(count < ConstantValues.ANIMRESIN.length){
					int residin = ConstantValues.ANIMRESIN[animFlag];
					int residout = ConstantValues.ANIMRESOUT[animFlag];
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
			final float centerX = MyApplication.getInstance().getScreenW() / 2.0f;
			final float centerY = MyApplication.getInstance().getScreenW() / 2.0f;
			
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
			}else if(msg.what == 3){
				if(isPlay){
					MainVideoActivity.mp.start();
					playBtn.setVisibility(View.GONE);
					shareBtn.setVisibility(View.GONE);
					timer = new Timer();
					myTimerTask = new MyTimerTask();
					timer.schedule(myTimerTask, 0, 4000);
					autoFLag = true;
				}
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(autoFLag){
			stopPlay();
			topLayout.setVisibility(View.VISIBLE);
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX(); // 取得按下时的坐标
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            upX = (int) event.getX(); // 取得松开时的坐标
            // 从左拖到右，即看前一张
            if (upX - downX > 80) {
               goFrontPic();
            } else if (downX - upX > 80) { // 从右拖到左，即看后一张
            	goNextPic(false);
            }
            return true;
        }
        return false;
	}
	
	/**
	 * 查看上一张图片
	 */

	@SuppressWarnings("deprecation")
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
//		currentPhoto.setText("第" + (mIndex + 1) + "张（共" + pathes.size() + "张）");
		Bitmap bit = null;
		bit = (Bitmap)MainVideoActivity.photoListInfo.get(mIndex).get("bitmap");
		Drawable drawable = new BitmapDrawable(bit);
		imageSwitcher.setImageDrawable(drawable);
//		if (file.exists() && file.isFile()) {
//			bit = BitmapCache.getInstance().getBitmap(file, ImageSwitcher.this);
//			if (bit != null) {
//				currentBitmap = bit;
//				b = false;
//				Drawable drawable = new BitmapDrawable(bit);
//				imageSwitcher.setImageDrawable(drawable);
//			}
//		}else{
//			imageSwitcher.setImageResource(R.drawable.photolist_defimg);
//			if(imgLocalUrl.equals(ConstantsValues.BEAUTYPHOTOS_IMG_URL)){//则为下载美图相册列表图片
//				   RequestServerFromHttp.downImage(ImageSwitcher.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.beautyPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
//							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
//       		}else if(imgLocalUrl.equals(ConstantsValues.MYALBUM_IMG_URL)){
//       			RequestServerFromHttp.downImage(ImageSwitcher.this,(ImageView) imageSwitcher.getCurrentView(),photoListInfo.get(mIndex).get("imageid").toString(),ImageType.UserPhotos.getValue(),ImgDownType.BigBitmap.getValue(),
//							MyApplication.getInstant().getWidthPixels()+"","0",false,imgLocalUrl,R.drawable.photolist_defimg);
//       		}
//			}
//		
	}

	/**
	 * 查看下一张图片
	 */

	@SuppressWarnings("deprecation")
	public void goNextPic(boolean isDeletePic) {
		if(mIndex > MainVideoActivity.photoListInfo.size() - 1)
			return;
		if(!isDeletePic){
			/*if (mIndex == MainVideoActivity.photoListInfo.size() - 1 && MainVideoActivity.typeUser.equals("1")) {//只有在用户为主人的情况下才进入婚嫁日志
				if(MainVideoActivity.mp!=null && MainVideoActivity.mp.isPlaying()){
					MainVideoActivity.mp.pause();
				}
				Intent intent = new Intent(this,MarryLogActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
				return;
			}else */if(mIndex == MainVideoActivity.photoListInfo.size() - 1/* && MainVideoActivity.typeUser.equals("0")*/){
				return;
			} else {
				mIndex++;
			}
		}else{
			if (mIndex > MainVideoActivity.photoListInfo.size() - 1) {
				return;
			} 
		}
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
				ImageSwitcher.this, R.anim.slide_in_right));
		imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
				ImageSwitcher.this, R.anim.slide_out_left));
		Bitmap bit = null;
		bit = (Bitmap)MainVideoActivity.photoListInfo.get(mIndex).get("bitmap");
		Drawable drawable = new BitmapDrawable(bit);
		imageSwitcher.setImageDrawable(drawable);
	}
	
	
	/**
	 * 查看下一张图片
	 */

	@SuppressWarnings("deprecation")
	public void rotatePic(int angle) {
		Bitmap bit = null;
		bit = (Bitmap)MainVideoActivity.photoListInfo.get(mIndex).get("bitmap");
		Drawable drawable = new BitmapDrawable(bit);
		imageSwitcher.clearAnimation();
		imageSwitcher.setImageDrawable(drawable);
	}
}
