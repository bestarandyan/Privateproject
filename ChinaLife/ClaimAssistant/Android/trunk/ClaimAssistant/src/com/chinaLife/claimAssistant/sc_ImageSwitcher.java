/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.chinaLife.claimAssistant;

import java.io.File;
import java.util.ArrayList;

import com.chinaLife.claimAssistant.tools.sc_EncryptPhoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
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
public class sc_ImageSwitcher extends Activity {

	private int mIndex;

	private int mItemwidth;
	private int mItemHerght;

	private ArrayList<String> pathes;

//	private ProgressBar mProgressBar;

	/** Image zoom view */
	private sc_ZoomImageView mZoomView;

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
		super.onCreate(savedInstanceState);
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

		setContentView(R.layout.sc_myhorizontalview);
		mZoomView = (sc_ZoomImageView) findViewById(R.id.zoomview);
		zcontrols = (ZoomControls) findViewById(R.id.zoomcontrol);
		
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
		if(zoomBitmap == null){
			Toast.makeText(this, "尊敬的客户:您刚拍摄的照片有缺损，无法预览，请尝试重拍。。", 6000).show();
		}else{
			mZoomView.setImage(zoomBitmap);
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
			if(bm==null){
				bm = sc_EncryptPhoto.decode(new File(path));
			}
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
