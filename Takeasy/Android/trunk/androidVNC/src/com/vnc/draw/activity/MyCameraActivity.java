package com.vnc.draw.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.vnc.draw.tools.CompassImage;

import android.androidVNC.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PixelXorXfermode;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;


public class MyCameraActivity extends Activity implements
		SurfaceHolder.Callback, OnClickListener {
	private SurfaceView cameraview = null;
	private SurfaceHolder surfaceHolder = null;
	private Camera mCamera;
	private ImageButton btn;
	public Bitmap bmp = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.a_camera);
		initView();
		initData();
	}
/**
 * 初始化控件
 */
	private void initView() {
		btn = (ImageButton) findViewById(R.id.btn);
		btn.setOnClickListener(this);
		btn.setVisibility(View.VISIBLE);
	}
	/**
	 * 获取最合适的相机参数
	 * @param sizes
	 * @param w
	 * @param h
	 * @return
	 */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	      final double ASPECT_TOLERANCE = 0.05;
	      double targetRatio = (double) w / h;
	      if (sizes == null) return null;

	      Size optimalSize = null;
	      double minDiff = Double.MAX_VALUE;

	      int targetHeight = h;

	      // Try to find an size match aspect ratio and size
	      for (Size size : sizes) {
	          double ratio = (double) size.width / size.height;
	          if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	          if (Math.abs(size.height - targetHeight) < minDiff) {
	              optimalSize = size;
	              minDiff = Math.abs(size.height - targetHeight);
	          }
	      }

	      // Cannot find the one match the aspect ratio, ignore the requirement
	      if (optimalSize == null) {
	          minDiff = Double.MAX_VALUE;
	          for (Size size : sizes) {
	              if (Math.abs(size.height - targetHeight) < minDiff) {
	                  optimalSize = size;
	                  minDiff = Math.abs(size.height - targetHeight);
	              }
	          }
	      }
	      return optimalSize;
	  }
	public void notifyLayout1() {
		cameraview = (SurfaceView) this.findViewById(R.id.surface_camera);
		surfaceHolder = cameraview.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	/**
	 * 程序数据初始化
	 */
	public void initData() {
		notifyLayout1();
		if(!isEnoughMem()){
			Toast.makeText(this, "手机储存卡已满。", 10000).show();
		}
	}

	public void onClick(View v) {
		if (v == btn) {
			if(!isEnoughMem()){
				Toast.makeText(this, "手机储存卡已满。", 10000).show();
				return;
			}else{
				btn.setClickable(false);
				try{
					mCamera.takePicture(mShutterListener, null, new TakePictureCallback());
				}catch(Exception e){
					if(mCamera!=null){
						mCamera.release();
						mCamera = null;
						System.gc();
					}
				
					if(bmp!=null){
						bmp.recycle();
						bmp = null;
						System.gc();
					}
					finish();
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*if(event.getAction() == MotionEvent.ACTION_DOWN){
			mCamera.autoFocus(mAutoFocusCallBack);
		}*/
		if(mCamera != null){
			try{
			mCamera.cancelAutoFocus(); //release the previous auto-focus
			mCamera.autoFocus(new Camera.AutoFocusCallback(){
				public void onAutoFocus(boolean success, Camera camera) {
					Log.d("HOME", "isAutofoucs " + Boolean.toString(success));					
				}
			} );
			}catch(Exception e){
				
			}
		}
		return super.onTouchEvent(event);
	}
	
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(mCamera != null){
			try{
			mCamera.cancelAutoFocus(); //release the previous auto-focus
			mCamera.autoFocus(new Camera.AutoFocusCallback(){
				public void onAutoFocus(boolean success, Camera camera) {
					Log.d("HOME", "isAutofoucs " + Boolean.toString(success));					
				}
			} );
			}catch(Exception e){
				
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		int a = width;
		int b = a+height;
		try {
			Camera.Parameters parameters = mCamera.getParameters();
		    List<Size> sizes = parameters.getSupportedPreviewSizes();
		    Size optimalSize = getOptimalPreviewSize(sizes,800, 600);
		    parameters.setPreviewSize(optimalSize.width, optimalSize.height);
		    parameters.setPictureFormat(PixelFormat.JPEG);
//		    parameters.setPictureSize(800, 600);
		    mCamera.setParameters(parameters);
		    try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    mCamera.startPreview();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "相机初始化失败。。", 5000).show();
			if(mCamera!=null){
				mCamera.release();
				mCamera = null;
				System.gc();
			}
		
			if(bmp!=null){
				bmp.recycle();
				bmp = null;
				System.gc();
			}
			finish();
		}
		
	}
	/**
	 * 判断手机存储卡是否内存足够
	 * @return
	 */
	 private boolean isEnoughMem() {
         File path = android.os.Environment.getExternalStorageDirectory();  // Get the path /data, this is internal storage path.
         StatFs stat = new StatFs(path.getPath());
         long blockSize = stat.getBlockSize();
         long availableBlocks = stat.getAvailableBlocks();
         long memSize = availableBlocks* blockSize;  // free size, unit is byte.
         if (memSize <1024*1024*10) { //If phone available memory is less than 10M , kill activity,it will avoid force when phone low memory.
                 return false;
         }
         return true;
 } 

	public void surfaceCreated(SurfaceHolder holder) {
		try{
			if (mCamera == null) {
				mCamera = mCamera.open();  
			}else{
				mCamera.release();
				mCamera = mCamera.open();  
			}
		}catch(Exception e){
			
			Toast.makeText(getApplicationContext(), "打开照相机失败。。", 5000).show();
			if(mCamera!=null){
				mCamera.release();
				mCamera = null;
				System.gc();
			}
			
			if(bmp!=null){
				bmp.recycle();
				bmp = null;
				System.gc();
			}
			finish();
		}  
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(mCamera!=null){
			mCamera.release();
			mCamera = null;
			System.gc();
		}

		if(bmp!=null){
			bmp.recycle();
			bmp = null;
			System.gc();
		}
//		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mCamera!=null){
			mCamera.release();
			mCamera = null;
			System.gc();
		}

		if(bmp!=null){
			bmp.recycle();
			bmp = null;
			System.gc();
		}
		
		super.onDestroy();
	}
	
	private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback() {
		public void onShutter() {
			int a = 0;
			int b = a;
		}
	};

	/**
	 * 处理照片被拍摄之后的事件
	 */
	private final class TakePictureCallback implements PictureCallback {

		public void onPictureTaken(byte[] data, Camera camera) {
			btn.setVisibility(View.GONE);
			MyApplication.getInstance().setDataByte(null);
			MyApplication.getInstance().setDataByte(data);
			MyApplication.getInstance().setPicId(SendToServiceToDraw.getId());
			/*if(mCamera!=null){
				mCamera.stopPreview();
				
//				mCamera.release();
				mCamera = null;
			}*/
			mCamera.stopPreview();
			Intent i = new Intent();
			MyCameraActivity.this.setResult(20, i);
			MyCameraActivity.this.finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
				mCamera.release();
				mCamera = null;
				if(bmp!=null){
					bmp.recycle();
					bmp = null;
					System.gc();
				}
				Intent i = new Intent();
				MyCameraActivity.this.setResult(RESULT_CANCELED, i);
				MyCameraActivity.this.finish();
		}
		return true;
	}


}
