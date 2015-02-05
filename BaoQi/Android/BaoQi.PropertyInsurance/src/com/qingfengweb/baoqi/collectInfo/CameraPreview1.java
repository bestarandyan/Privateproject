/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qingfengweb.baoqi.collectInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

import com.qingfengweb.baoqi.propertyInsurance.R;




// ----------------------------------------------------------------------

public class CameraPreview1 extends Activity implements SurfaceHolder.Callback,
		OnClickListener {

//	private SurfaceView mSurfaceView;
//	private SurfaceHolder mSurfaceHolder;
//	private boolean bifPrieview = false;
//	private Camera mCamera;
    FinalValues finalValues;
	public static int ScrrenWidth;
	public static int ScrrenHeight;
	public static int screenCameraWidth;
	public static int screenCameraHeight;
    public FrameLayout.LayoutParams params;
	private Button backPic;
	private Button getPic;
	
	//private SaveThread mSaveThread = null;
	private static int PicPhotoCount = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    
		
		setContentView(R.layout.camera);
		CameraManager.init(getApplication());
		finalValues=new FinalValues();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ScrrenWidth = dm.widthPixels;
		ScrrenHeight=dm.heightPixels;
		screenCameraWidth=finalValues.SCREEN_CAMERA_WIDTH;
		screenCameraHeight=finalValues.SCREEN_CAMERA_HEIGHT;

//		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		backPic = (Button) findViewById(R.id.backPic);
		getPic = (Button) findViewById(R.id.getPic);

//		mSurfaceHolder = mSurfaceView.getHolder();
//		mSurfaceHolder.addCallback(CameraPreview.this);
//		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		/*Intent intent=getIntent();
		int i=intent.getIntExtra("screen_flag", 0);
		
		if(i==1){
			 params = new FrameLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		     params.setMargins(0, 0,0, 0);
		}else if(i==2){
			
			params = new FrameLayout.LayoutParams(
					screenCameraWidth, screenCameraHeight);
		     params.setMargins(ScrrenWidth/2-screenCameraWidth/2, ScrrenHeight/2-screenCameraHeight/2,0, 0);
		}*/
		
	

		
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
	    SurfaceHolder surfaceHolder = surfaceView.getHolder();
	    surfaceHolder.addCallback(this);
	    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	  //  surfaceView.setLayoutParams(params);
		backPic.setOnClickListener(this);
		getPic.setOnClickListener(this);
		PicPhotoCount=0;

	}

	public void onClick(View v) {

		 if (v == backPic) {
			 CameraManager.get().stopPreview();
			 CameraManager.get().closeDriver();
			 Intent intent = new Intent(this,CollCarInfo.class);
				startActivity(intent);
				finish();
			}
		 else if (v == getPic) {
			 
			if(PicPhotoCount==0){
				PicPhotoCount++;
			 CameraManager.get().requestPreviewFrame(mHandler, R.id.save);
			}
			
		}

	}
	 @Override
	  protected void onPause() {
	    super.onPause();
	   // CameraManager.get().stopPreview();
//	    if (mSaveThread != null) {
//	        Message quit = Message.obtain(mSaveThread.mHandler, R.id.quit);
//	        quit.sendToTarget();
//	        try {
//	          mSaveThread.join();
//	        } catch (InterruptedException e) {
//	        }
//	        mSaveThread = null;
//	      }
	   // CameraManager.get().closeDriver();

	 }
	 @Override
	protected void onResume() {
		super.onResume();
//		if (mSaveThread == null ) {
//		      mSaveThread = new SaveThread(this);
//		      mSaveThread.start();
//		 }
	}
	 
	 public final Handler mHandler = new Handler() {
		    @Override
		    public void handleMessage(Message message) {
		      switch (message.what) {
		        case R.id.save:
		        	byte buf[]=(byte[]) message.obj;
//		        	String len = (String) message.obj;
		        	
		        	
		        	getIntent().putExtra("pic", buf);
		        	getIntent().putExtra("btnvalue", getIntent().getIntExtra("btnvalue", 0));
		        	setResult(20,getIntent());   
		        	finish();  
		        	break;
		      }
		    }
		  };

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		
//		CameraManager.get().DriverSurfaceChanged(holder,format, width, height);
		
		
		Log.d("digilinx-Camera surfaceChanged", "width="+width+"height="+height+"Format="+format);

	}

	public void surfaceCreated(SurfaceHolder holder) {
//		try {
//			initCamera(holder);
//		} catch (IOException e) {
//			 throw new RuntimeException(e);
//		}
		
		 try {
			 CameraManager.get().openDriver(holder);
			 CameraManager.get().startPreview();
			 
		} catch (IOException e) {
			 throw new RuntimeException(e);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 CameraManager.get().stopPreview();
		 CameraManager.get().closeDriver();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(this,CollCarInfo.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
//	void initCamera(SurfaceHolder holder)throws IOException  {
//
//		if (!bifPrieview) {
//			if(mCamera==null){
//			mCamera = Camera.open();
//			}
//			if(mCamera==null)
//			{
//				 throw new IOException();
//			}
//		}
//		if (mCamera != null && !bifPrieview) {
//			Camera.Parameters p = mCamera.getParameters();
//			p.setPictureFormat(PixelFormat.JPEG);
//			p.setPictureSize(ScrrenWidth, ScrrenHeight);
//			mCamera.setParameters(p);
//			try {
//				mCamera.setPreviewDisplay(holder);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			mCamera.startPreview();
//			bifPrieview = true;
//		}
//
//	}

//	private void takePicture() {
//		mCamera.takePicture(shuuterCallback, rawCallback, jpegCallback);
//	}

//	private void resetCamera() {
//		if (mCamera != null && bifPrieview) {
//			mCamera.stopPreview();
//			mCamera = null;
//			bifPrieview = false;
//		}
//	}

//	private ShutterCallback shuuterCallback = new ShutterCallback() {
//
//		public void onShutter() {
//
//		}
//
//	};
//	private PictureCallback jpegCallback = new PictureCallback() {
//
//		public void onPictureTaken(byte[] data, Camera camera) {
//			getIntent().putExtra("pic", data);
//			setResult(20, getIntent());
//			finish();
//		}
//
//	};
//	private PictureCallback rawCallback = new PictureCallback() {
//
//		public void onPictureTaken(byte[] data, Camera camera) {
//
//		}
//
//	};

//	public void stopPreview() {
//		if(mCamera!=null&&bifPrieview){
//		mCamera.stopPreview();
//		bifPrieview=false;
//		}
//	}
//
//	public void closeDriver() {
//		if (mCamera != null) {
//			mCamera.release();
//			mCamera = null;
//		}
//
//	}

	// private void displayFrameworkBugMessageAndExit() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setTitle(getString(R.string.app_name));
	// builder.setMessage(getString(R.string.msg_camera_framework_bug));
	// builder.setPositiveButton("确定", new FinishListener(this));
	// builder.setOnCancelListener(new FinishListener(this));
	// builder.show();
	//	    

	// public void onError(int error, Camera camera) {
	// /**
	// * 媒体服务器死亡。在这种情况下，应用程序必须释放Camera对象和实例化一个新的。
	// * */
	// if(error==android.hardware.Camera.CAMERA_ERROR_SERVER_DIED)
	// {
	// Log.d(TAG, "CAMERA_ERROR_SERVER_DIED");
	// camera.release();
	// camera = null;
	//			
	//			
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setTitle(getString(R.string.app_name));
	// builder.setMessage("CAMERA_ERROR_SERVER_DIED");
	// builder.setPositiveButton("确定", new FinishListener(this));
	// builder.setOnCancelListener(new FinishListener(this));
	// builder.show();
	// }
	// /**
	// * 未指定的错误camerar
	// * */
	// else if(error==android.hardware.Camera.CAMERA_ERROR_UNKNOWN)
	// {
	// Log.d(TAG, "CAMERA_ERROR_UNKNOWN");
	// camera.release();
	// camera = null;
	//			
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setTitle(getString(R.string.app_name));
	// builder.setMessage("CAMERA_ERROR_UNKNOWN");
	// builder.setPositiveButton("确定", new FinishListener(this));
	// builder.setOnCancelListener(new FinishListener(this));
	// builder.show();
	// }
	//		
	// }

}
