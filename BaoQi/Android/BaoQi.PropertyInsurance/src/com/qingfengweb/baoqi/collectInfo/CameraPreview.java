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

import com.qingfengweb.baoqi.electroncollection.ElectrolCarXian;
import com.qingfengweb.baoqi.electroncollection.ElectronCaiChan;
import com.qingfengweb.baoqi.propertyInsurance.R;






// ----------------------------------------------------------------------

public class CameraPreview extends Activity implements SurfaceHolder.Callback,
		OnClickListener {

//	private SurfaceView mSurfaceView;
//	private SurfaceHolder mSurfaceHolder;
//	private boolean bifPrieview = false;
//	private Camera mCamera;
    FinalValues finalValues;
	public  int ScrrenWidth;
	public  int ScrrenHeight;
	public  int screenCameraWidth;
	public  int screenCameraHeight;
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
			 backFun();
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
	 }
	 @Override
	protected void onResume() {
		super.onResume();

	}
	 
	 public final Handler mHandler = new Handler() {
		    @Override
		    public void handleMessage(Message message) {
		      switch (message.what) {
		        case R.id.save:
		        	byte buf[]=(byte[]) message.obj;
//		        	String len = (String) message.obj;
		        	
		        	Intent intent = new Intent(CameraPreview.this,PictureInfo.class);
		        	intent.putExtra("pic", buf);
		        	intent.putExtra("btnvalue", getIntent().getIntExtra("btnvalue", 0));
		        	intent.putExtra("cameraPreviewdizhi", getIntent().getStringExtra("commDiZhi"));
		        	//setResult(20,intent);   
		        	startActivityForResult(intent, 20);
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
	public void backFun(){
		Intent intent = null;
		switch(getIntent().getIntExtra("btnvalue",0)){
		 case 1:
			 intent = new Intent(this,CommInfoActivity.class);
			 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 2:
			 intent = new Intent(this,CommInfoActivity.class);
			 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 3:intent = new Intent(this,CommInfoActivity.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 4:intent = new Intent(this,CommInfoActivity.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 5:intent = new Intent(this,CommInfoActivity.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 6:intent = new Intent(this,CommInfoActivity.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 7:intent = new Intent(this,CollCarInfo.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 8:intent = new Intent(this,CollCarInfo.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 9:intent = new Intent(this,CollCarInfo.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 10:intent = new Intent(this,CollCarInfo.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 11:intent = new Intent(this,CollCarInfo.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 12:intent = new Intent(this,CollCarInfo.class);
		 intent.putExtra("btnvalue",getIntent().getIntExtra("btnvalue",0));
			 break;
		 case 81:intent = new Intent(this,ElectrolCarXian.class);
			 break;
		 case 82:intent = new Intent(this,ElectrolCarXian.class);
			 break;
		 case 83:intent = new Intent(this,ElectrolCarXian.class);
			 break;
		 case 84:intent = new Intent(this,ElectronCaiChan.class);
			 break;
		 }
		startActivity(intent);
		finish();
	}
	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			backFun();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
