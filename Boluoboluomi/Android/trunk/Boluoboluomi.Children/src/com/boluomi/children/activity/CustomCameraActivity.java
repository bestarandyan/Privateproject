package com.boluomi.children.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.util.CommonUtils;
import com.boluomi.children.view.ZoomImageView;
import com.qingfengweb.imagehandle.PicHandler;

@SuppressLint("ShowToast")
public class CustomCameraActivity extends Activity implements
		SurfaceHolder.Callback, OnClickListener,OnTouchListener {
	private RelativeLayout cameraLayout = null;//照相机预览视图
	private RelativeLayout imgLayout = null;//图片预览视图
	private SurfaceView cameraview = null;
	private SurfaceHolder surfaceHolder = null;
	private Camera mcamera = null;
	private ImageButton btn = null;
	private ZoomImageView mZoomView = null;      
	private Bitmap zoomBitmap = null;
	private byte[] datas = null;
	private Button keepphotoBtn = null;
	private Button retakePhotoBtn = null;
	private Button zoomOut,zoomIn;
	private Drawable drawable = null;
	public Bitmap menban = null;
	PicHandler ph = null;
	private ProgressDialog progress = null;
	private boolean isTouch = true;
	private boolean isTake = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.a_camera);
		initView();
		initData();
		
	}
	/**
	 * 打开耗时处理的进度条
	 */
	private void showProgress(){
		if(progress!=null)
		progress.cancel();
		progress = null;
		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage("图片处理中，请稍候。。");
		progress.show();
	}
	/**
	 * 初始化控件
	 */
	private void initView() {
		cameraLayout = (RelativeLayout) findViewById(R.id.layout1);
		imgLayout = (RelativeLayout) findViewById(R.id.layout2);
		zoomOut = (Button) findViewById(R.id.zoomOut);
		zoomIn = (Button) findViewById(R.id.zoomIn);
		zoomOut.setOnClickListener(this);
		zoomIn.setOnClickListener(this);
	}
	
	@SuppressWarnings("deprecation")
	public void initCameraView(){
		cameraview = (SurfaceView) this.findViewById(R.id.surface_camera);
		surfaceHolder = cameraview.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		cameraview.setOnTouchListener(this);
		btn = (ImageButton) findViewById(R.id.btn);
		btn.setOnClickListener(this);
	}
	/**
	 * 用来处理拍摄得到的照片 包括压缩和打水印
	 * 
	 * @author  刘星星
	 */
	public Runnable saveImgRunnable = new Runnable() {
		@Override
		public void run() {
			String fileUrl = ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MYALBUM_IMG_URL;
			String fileName = PicHandler.getFileName();
			PicHandler.byteToFile(datas, fileUrl, fileName);
//			Bitmap bitmap = BitmapFactory.decodeFile(fileUrl+fileName);
//			RotateImg(file.getAbsolutePath(), bitmap);
//			getOriention();
			Message msg = new Message();
			msg.obj = fileUrl+fileName;
			msg.what = 0 ;
			handler.sendMessage(msg);
		}
	};
	@SuppressWarnings("deprecation")
	private void getOriention(){

		  Display display = ((WindowManager) this.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		  int t = display.getOrientation();
		   if(t == Surface.ROTATION_0  ){
		       Log.i("TAG", "ROTATION_0");
		   } else if(t ==Surface.ROTATION_90 ){
		       Log.i("TAG", "ROTATION_90");
		   }else if(t ==Surface.ROTATION_180 ){
		       Log.i("TAG", "ROTATION_180");
		   }else if(t ==Surface.ROTATION_270 ){
		       Log.i("TAG", "ROTATION_270");
		   }

	}
	// 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度  
	public  int getPreviewDegree(Activity activity) {  
	    // 获得手机的方向  
	    int rotation = activity.getWindowManager().getDefaultDisplay()  
	            .getRotation();  
	    int degree = 0;  
	    // 根据手机的方向计算相机预览画面应该选择的角度  
	    switch (rotation) {  
	    case Surface.ROTATION_0:  
	        degree = 90;  
	        Toast.makeText(CustomCameraActivity.this, "0°", 1000).show();
	        break;  
	    case Surface.ROTATION_90:  
	        degree = 0;  
	        Toast.makeText(CustomCameraActivity.this, "90°", 1000).show();
	        break;  
	    case Surface.ROTATION_180:  
	        degree = 270;  
	        Toast.makeText(CustomCameraActivity.this, "180°", 1000).show();
	        break;  
	    case Surface.ROTATION_270:  
	        degree = 180;  
	        Toast.makeText(CustomCameraActivity.this, "270°", 1000).show();
	        break;  
	    }  
	    return degree;  
	}  
	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if(mcamera!=null){
					mcamera.release();
					mcamera = null;
				}
				if(zoomBitmap!=null){
					zoomBitmap.recycle();
					zoomBitmap = null;
				}
				datas = null;
				System.gc();
//				progress.dismiss();
				Intent i = new Intent();
				i.putExtra("imgUrl",msg.obj.toString());
				CustomCameraActivity.this.setResult(RESULT_OK, i);
				CustomCameraActivity.this.finish();
			}
			super.handleMessage(msg);
		}

	};
    /**
     * 初始化拍照完成之后的界面
     */
	public void initImgLayout() {
//		mZoomView = (ZoomImageView) findViewById(R.id.zoomview);
//		keepphotoBtn = (Button) findViewById(R.id.keepphoto);
//		retakePhotoBtn = (Button) findViewById(R.id.retake);
//		keepphotoBtn.setOnClickListener(this);
//		retakePhotoBtn.setOnClickListener(this);
		if(zoomBitmap!=null && !zoomBitmap.isRecycled()){
			zoomBitmap.recycle();
			zoomBitmap = null;
			System.gc();
		}
//		if(mZoomView.getDrawingCache()!=null && !mZoomView.getDrawingCache().isRecycled()){
//			mZoomView.getDrawingCache().recycle();
//		}
//		if(mZoomView!=null)
//			mZoomView.destroyDrawingCache();
//			zoomBitmap = PicHandler.getDrawable(datas, mZoomView);
			if(datas!=null && datas.length>0){//如果生成预览图成功则显示预览图
//				mZoomView.setImage(zoomBitmap);
//				showProgress();
				Thread thread = new Thread(saveImgRunnable);
				thread.start();
			}else{
				
			}
	}

	/**
	 * 程序数据初始化
	 */
	public void initData() {
		imgLayout.setVisibility(View.GONE);
		cameraLayout.setVisibility(View.VISIBLE);
		initCameraView();
		if(!CommonUtils.isEnoughMem()){
			Toast.makeText(this, "存储空间已满，请插入或更换SD卡", 10000).show();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btn) {//拍照
			if(!CommonUtils.isEnoughMem()){
				Toast.makeText(this, "存储空间已满，请插入或更换SD卡", 10000).show();
				return;
			}else{
				//camera.autoFocus(mAutoFocusCallBack);
				if(isTake){
					btn.setClickable(false);
					isTouch = false;
					mcamera.takePicture(mShutterListener, null, new TakePictureCallback());
				}
				//mcamera.takePicture(null, null, new TakePictureCallback());
			}
		}else if(v == zoomOut){
			mZoomView.zoomImage(1.5f, 0, 0);
		}else if(v == zoomIn){
			mZoomView.zoomImage(0.8f, 0, 0);
		}else if(v == keepphotoBtn){//保存照片
			showProgress();
			Thread thread = new Thread(saveImgRunnable);
			thread.start();
		}else if(v == retakePhotoBtn){//重新拍摄照片
			imgLayout.setVisibility(View.GONE);
			if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
				zoomBitmap.recycle();
				zoomBitmap = null;
				System.gc();
			}
			cameraLayout.setVisibility(View.VISIBLE);
			initCameraView();
			//mZoomView.setImage(null);
			mcamera.startPreview();
			isTouch = true;
		
		}
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			
	//	    TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//		    Build.MODEL //手机型号
			if(mcamera!=null){
			    String str = Build.MODEL;
			    Camera.Parameters parameters = mcamera.getParameters();
//			    parameters.setPictureFormat (PixelFormat.RGBX_8888);
//			    parameters.setPictureSize(600, 800);
		    	parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
			    if(str.equals("HTC A9188") || str.equals("ZTE-T U960s") || str.equals("HTC G10")){
				    List<Size> sizes = parameters.getSupportedPreviewSizes();
				    Size optimalSize = CommonUtils.getOptimalPreviewSize(sizes, width, height);
				    parameters.setPreviewSize(optimalSize.width, optimalSize.height);
			    }
		    	mcamera.setParameters(parameters);
//				mcamera.setPreviewDisplay(holder);
//		        mcamera.startPreview();
			}else{

				if(mcamera!=null){
					mcamera.release();
					mcamera = null;
				}
				if(zoomBitmap!=null){
					zoomBitmap.recycle();
					zoomBitmap = null;
					
				}
				System.gc();
				Intent i = new Intent();
				CustomCameraActivity.this.setResult(RESULT_OK, i);
				CustomCameraActivity.this.finish();
			
			}
		} catch (final Exception e) {
			Toast.makeText(getApplicationContext(), "相机初始化失败。。", 5000).show();
			if(mcamera!=null){
				mcamera.release();
				mcamera = null;
			}
			if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
				zoomBitmap.recycle();
				zoomBitmap = null;
			}
			System.gc();
			Intent i = new Intent();
			CustomCameraActivity.this.setResult(RESULT_OK, i);
			CustomCameraActivity.this.finish();
		}
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try{
			if (mcamera == null) {
				mcamera = Camera.open();  
			}else{
				mcamera.release();
				mcamera = Camera.open();  
			}
			mcamera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象  
			mcamera.setDisplayOrientation(getPreviewDegree(CustomCameraActivity.this));  
			mcamera.startPreview(); // 开始预览  
		}catch(final Exception e){
			Toast.makeText(this, "拍照异常", 3000).show();
		}  
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(mcamera!=null){
			mcamera.release();
			mcamera = null;
		}

		if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
			zoomBitmap.recycle();
			zoomBitmap = null;
		}
		if(surfaceHolder!=null){
			surfaceHolder.removeCallback(this);
			surfaceHolder = null;
		}
		System.gc();
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mcamera!=null){
			mcamera.release();
			mcamera = null;
		}

		if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
			zoomBitmap.recycle();
			zoomBitmap = null;
		}
		if(drawable!=null){
			drawable.clearColorFilter();
			drawable.setCallback(null);
			drawable = null;
		}
		if(menban!=null && !menban.isRecycled() ){
			menban.recycle();
			menban = null;
		}
		if(surfaceHolder!=null){
			surfaceHolder.removeCallback(this);
			surfaceHolder = null;
			
		}
		System.gc();
		super.onDestroy();
	}
	private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback() {
		public void onShutter() {
			
		}
	};

	/**
	 * 处理照片被拍摄之后的事件
	 */
	private final class TakePictureCallback implements PictureCallback {

		public void onPictureTaken(byte[] data, Camera camera) {
//			cameraLayout.setVisibility(View.GONE);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
//			imgLayout.setVisibility(View.VISIBLE);
			datas = null;
			datas = data;
			if(datas!=null){
				initImgLayout();//获取图片成功，显示图片
				if(mcamera!=null){
					mcamera.stopPreview();
				}
			}else{
				if(mcamera!=null){
					mcamera.stopPreview();
					mcamera.release();
					mcamera = null;
				}
				finish();
			}
			System.gc();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			if (cameraLayout.getVisibility() == View.VISIBLE) {
				mcamera.release();
				mcamera = null;
				if(zoomBitmap!=null){
					zoomBitmap.recycle();
					zoomBitmap = null;
				}
				if(surfaceHolder!=null){
					surfaceHolder.removeCallback(this);
					surfaceHolder = null;
				}
				System.gc();
				CustomCameraActivity.this.setResult(RESULT_CANCELED, null);
				CustomCameraActivity.this.finish();
				
			} else {
				imgLayout.setVisibility(View.GONE);
				cameraLayout.setVisibility(View.VISIBLE);
				initCameraView();
				mcamera.startPreview();
			}

		}
		return true;
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(isTouch){
			isTouch = false;
			isTake = false;
		}else {
			return false;
		}
		if(mcamera != null){
			try{
				mcamera.cancelAutoFocus(); //release the previous auto-focus
				mcamera.autoFocus(new Camera.AutoFocusCallback(){
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					Log.d("HOME", "isAutofoucs " + Boolean.toString(success));	
					isTouch = true;
					isTake = true;
				}
			} );
			}catch(final Exception e){
				isTouch = true;
				isTake = true;
			}
		}else{
			isTouch = true;
			isTake = true;
		}
		return false;
	}
}
