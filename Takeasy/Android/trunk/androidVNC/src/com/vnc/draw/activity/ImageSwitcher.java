/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.vnc.draw.activity;
import java.util.ArrayList;

import android.androidVNC.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
public class ImageSwitcher extends Activity implements OnClickListener{

	private int mIndex;

	private int mItemwidth;
	private int mItemHerght;

	private ArrayList<String> pathes;

//	private ProgressBar mProgressBar;

	/** Image zoom view */
	private ZoomImageView mZoomView;
	private Button frontBtn,nextBtn,backBtn;
	/** Zoom state */
	/** On touch listener for zoom view */
	private Bitmap zoomBitmap;
	private ZoomControls zcontrols;

	public int getmIndex() {
		return mIndex;
	}



	private boolean isViewIntent() {
		String action = getIntent().getAction();
		return Intent.ACTION_VIEW.equals(action);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(ImageSwitcher.this);
		Intent intent = getIntent();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mItemwidth = dm.widthPixels;
		mItemHerght = dm.heightPixels;
		// mInflater = LayoutInflater.from(this);
		if (!isViewIntent()) {
			pathes = intent.getStringArrayListExtra("pathes");
			mIndex = intent.getIntExtra("index", 0);
		} else {
			pathes = new ArrayList<String>();
			pathes.add(intent.getData().getPath());
			mIndex = 0;
		}  

		setContentView(R.layout.myhorizontalview);
		mZoomView = (ZoomImageView) findViewById(R.id.zoomview);
		zcontrols = (ZoomControls) findViewById(R.id.zoomcontrol);
		frontBtn = (Button) findViewById(R.id.picFront);
		nextBtn = (Button) findViewById(R.id.picNext);
		backBtn = (Button) findViewById(R.id.back);
		backBtn.setOnClickListener(this);
		frontBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
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
	
		zoomBitmap = getDrawable(mIndex,1);
		mZoomView.setImage(zoomBitmap);
		if(mIndex == 0){
			frontBtn.setTextColor(Color.GRAY);
			frontBtn.setEnabled(false);
			frontBtn.setClickable(false);
			if(pathes.size() == 1){
				nextBtn.setEnabled(false);
				nextBtn.setClickable(false);
				nextBtn.setTextColor(Color.GRAY);
			}else{
				nextBtn.setEnabled(true);
				nextBtn.setClickable(true);
				nextBtn.setTextColor(Color.rgb(20,155,245));
			}
		}else if(mIndex == pathes.size()-1){
			frontBtn.setTextColor(Color.rgb(20,155,245));
			nextBtn.setTextColor(Color.GRAY);
			nextBtn.setEnabled(false);
			nextBtn.setClickable(false);
			frontBtn.setEnabled(true);
			frontBtn.setClickable(true);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == frontBtn){
			if(mIndex > 0){
				mIndex -=1;
				zoomBitmap = getDrawable(mIndex,1);
				mZoomView.setImage(zoomBitmap);
				if(mIndex == 0){
					frontBtn.setTextColor(Color.GRAY);
					frontBtn.setEnabled(false);
					frontBtn.setClickable(false);
					nextBtn.setEnabled(true);
					nextBtn.setClickable(true);
					nextBtn.setTextColor(Color.rgb(20,155,245));
					Toast.makeText(this, "已处于最先位置。。",5000).show();
				}else{
					frontBtn.setTextColor(Color.rgb(20,155,245));
					nextBtn.setTextColor(Color.rgb(20,155,245));
					frontBtn.setEnabled(true);
					frontBtn.setClickable(true);
					nextBtn.setEnabled(true);
					nextBtn.setClickable(true);
				}
			}
		}else if(v == nextBtn){
			if(mIndex < pathes.size()-1){
				mIndex +=1;
				zoomBitmap = getDrawable(mIndex,1);
				mZoomView.setImage(zoomBitmap);
				if(mIndex == pathes.size()-1){
					frontBtn.setTextColor(Color.rgb(20,155,245));
					nextBtn.setTextColor(Color.GRAY);
					nextBtn.setEnabled(false);
					nextBtn.setClickable(false);
					frontBtn.setEnabled(true);
					frontBtn.setClickable(true);
					Toast.makeText(this, "已处于末尾位置。。",5000).show();
				}else{
					nextBtn.setEnabled(true);
					nextBtn.setClickable(true);
					frontBtn.setEnabled(true);
					frontBtn.setClickable(true);
					frontBtn.setTextColor(Color.rgb(20,155,245));
					nextBtn.setTextColor(Color.rgb(20,155,245));
				}
			}
		}else if(v == backBtn){
			finish();
		}
	}
	 public void onLongPress(MotionEvent event) {
		 zcontrols.show();

     }
	private Bitmap getDrawable(int index, int zoom) {
		if (index >= 0 && index < pathes.size()) {
			String path = pathes.get(index);

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
			options.inPreferredConfig = Config.RGB_565;
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
		}
		return null;
	}



	
	
}
