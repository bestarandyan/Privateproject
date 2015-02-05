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

package com.vnc.draw.activity;

import android.androidVNC.R;
import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.IOException;

// ----------------------------------------------------------------------

public class CameraPreview extends Activity implements SurfaceHolder.Callback,
		OnClickListener {
	public static int ScrrenWidth;
	public static int ScrrenHeight;
	private Button getPic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.camera);
		CameraManager.init(getApplication());
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		ScrrenWidth = dm.widthPixels;
		ScrrenHeight=dm.heightPixels;
		getPic = (Button) findViewById(R.id.getPic);
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
	    SurfaceHolder surfaceHolder = surfaceView.getHolder();
	    surfaceHolder.addCallback(this);
	    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		getPic.setOnClickListener(this);
		surfaceView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				CameraManager.get().requestAutoFocus();
				return true;
			}
		});
	}

	public void onClick(View v) {
	 if (v == getPic) {
			 CameraManager.get().requestPreviewFrame(mHandler, R.id.save);
		}
	}
	@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
			return super.onKeyDown(keyCode, event);
		}
	 @Override
	  protected void onPause() {
	    super.onPause();
	    CameraManager.get().stopPreview();
	    CameraManager.get().closeDriver();
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
		        	MyApplication.getInstance().setDataByte(buf);
		        	setResult(20,getIntent());   
		        	finish();   
		        	break;
		      }
		    }
		  };

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("digilinx-Camera surfaceChanged", "width="+width+"height="+height+"Format="+format);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		 try {
			CameraManager.get().openDriver(holder);
			 CameraManager.get().startPreview();
			 CameraManager.get().requestAutoFocus();
		} catch (IOException e) {
			 throw new RuntimeException(e);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {

	}
}
