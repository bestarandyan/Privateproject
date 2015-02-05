/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.piaoguanjia.chargeclient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ZoomControls;

/**
* @Filename: ImageSwitcher.java
* @Author: wanghb
* @Email: wanghb@foryouge.com.cn
* @CreateDate: 2011-7-15
* @Description: description of the new class
* @Others: comments
* @ModifyHistory:
*/
public class ImageSwitcher extends Activity {

	private int mIndex;
	private String path = "";
	private int mItemwidth;
	private int mItemHerght;
//	private ProgressBar mProgressBar;

	/** Image zoom view */
	private ZoomImageView mZoomView;

	/** Zoom state */
	/** On touch listener for zoom view */
	private Bitmap zoomBitmap;
	private ZoomControls zcontrols;
	private ImageView backBtn;
	private ProgressBar progressBar;
	private HashMap<String, Object> map = null;
	public int getmIndex() {
		return mIndex;
	}



	private boolean isViewIntent() {
		String action = getIntent().getAction();
		return Intent.ACTION_VIEW.equals(action);
	}
	
	
	

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myhorizontalview);
		findView();
		initView();
		 path = getIntent().getStringExtra("path");
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			if(bitmap!=null){
				mZoomView.setImage(bitmap);
//				zoomBitmap = getDrawable(1);
//				if(zoomBitmap == null){
//					Toast.makeText(this, "对不起，此照片有缺损无法预览！", 6000).show();
//				}else{
//					mZoomView.setImage(zoomBitmap);
//				}
			}
	}
	private void findView(){
		mZoomView = (ZoomImageView) findViewById(R.id.zoomview);
		zcontrols = (ZoomControls) findViewById(R.id.zoomcontrol);
		backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
    private void initView(){
    	mZoomView.setZcontrols(zcontrols);
		zcontrols.setOnZoomInClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						mZoomView.zoomImage(1.5f, 0, 0);
					}
				});
		zcontrols.setOnZoomOutClickListener(new View.OnClickListener() {
			
	
			public void onClick(View v) {
				mZoomView.zoomImage(0.8f, 0, 0);
			}
		});
    }
    
	 public void onLongPress(MotionEvent event) {
		 zcontrols.show();

     }
	private Bitmap getDrawable(int zoom) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int mWidth = options.outWidth;
			int mHeight = options.outHeight;
			int s = 1;
			while ((mWidth / s > mItemwidth * 2 * zoom) || (mHeight / s > mItemHerght * 2 * zoom)) {
				s *= 2;
			}
			options = new BitmapFactory.Options();
			options.inPreferredConfig = Config.ARGB_8888;
			options.inSampleSize = s;
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			
			if (bm != null) {
				int h = bm.getHeight();
				int w = bm.getWidth();
				float ft = (float) ((float) w / (float) h);
				float fs = (float) ((float) mItemwidth / (float) mItemHerght);
				int neww = ft >= fs ? mItemwidth * zoom : (int) (mItemHerght * zoom * ft);
				int newh = ft >= fs ? (int) (mItemwidth * zoom / ft) : mItemHerght * zoom;
				float scaleWidth = ((float) neww) / w;
				float scaleHeight = ((float) newh) / h;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				bm = Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
				return bm;
			}
		return null;
	}
	
}
