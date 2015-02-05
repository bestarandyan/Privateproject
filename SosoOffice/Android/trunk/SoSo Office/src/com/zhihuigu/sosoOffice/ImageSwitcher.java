/*****************Copyright (C), 2010-2015, FORYOU Tech. Co., Ltd.********************/
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.View.ZoomImageView;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.ImageDownloaderSync;

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
import android.widget.Button;
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
public class ImageSwitcher extends BaseActivity {

	private int mIndex;

	private int mItemwidth;
	private int mItemHerght;

	private ArrayList<String> pathes;

//	private ProgressBar mProgressBar;

	/** Image zoom view */
	private ZoomImageView mZoomView;

	/** Zoom state */
	/** On touch listener for zoom view */
	private Bitmap zoomBitmap;
	private ZoomControls zcontrols;
	private Button backBtn;
	private ProgressBar progressBar;
	private ImageDownloaderSync imagedownloadersync = null;
	private HashMap<String, Object> map = null;
	public int getmIndex() {
		return mIndex;
	}



	private boolean isViewIntent() {
		String action = getIntent().getAction();
		return Intent.ACTION_VIEW.equals(action);
	}
	
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0://下载图片成功
				zoomBitmap = getDrawable(mIndex,1);
				if(zoomBitmap == null){
					Toast.makeText(ImageSwitcher.this, "尊敬的客户，您刚拍摄的照片有缺损，无法预览，请尝试重拍。。", 6000).show();
				}else{
					mZoomView.setImage(zoomBitmap);
				}
				progressBar.setVisibility(View.GONE);
				break;
			case 1://下载图片失败
				progressBar.setVisibility(View.GONE);
				AlertDialog.Builder callDailog = new AlertDialog.Builder(ImageSwitcher.this);
				  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
				  			.setTitle("提示")
							.setMessage("下载图片失败，是否重试！")
							.setPositiveButton("重试",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int which) {
											if(imagedownloadersync==null){
												imagedownloadersync = new ImageDownloaderSync(ImageSwitcher.this, handler);
											}
											imagedownloadersync.postData((File) map.get("file"), 
													map.get("sql").toString(),
													map.get("id").toString(), 
													map.get("pixelswidth").toString(),
													map.get("pixelsheight").toString(),
													map.get("request_name").toString());
										}
									}).setNegativeButton("取消", null);
				  if(map!=null){
					  callDailog.show();
				  }
				break;
			}
		};
	};
	
	
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if(imagedownloadersync==null){
				imagedownloadersync = new ImageDownloaderSync(ImageSwitcher.this, handler);
			}
			imagedownloadersync.postData((File) map.get("file"), 
					map.get("sql").toString(),
					map.get("id").toString(), 
					map.get("pixelswidth").toString(),
					map.get("pixelsheight").toString(),
					map.get("request_name").toString());
		}
	};

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mItemwidth = dm.widthPixels;
		mItemHerght = dm.heightPixels;
		// mInflater = LayoutInflater.from(this);
		setContentView(R.layout.myhorizontalview);
		findView();
		initView();
		
		map = (HashMap<String, Object>) getIntent()
				.getSerializableExtra("map");
		if(map!=null){
			boolean b = true;
			File file = (File) map.get("file");
			if(pathes==null){
				pathes= new ArrayList<String>();
			}
			pathes.clear();
			pathes.add(file.getAbsolutePath());
			mIndex = 0;
			if(file.exists()&&file.isFile()){
				Bitmap bitmap = BitmapCache.getInstance().getBitmap(file, this);
				if(bitmap!=null){
					b=false;
					zoomBitmap = getDrawable(mIndex,1);
					if(zoomBitmap == null){
						Toast.makeText(this, "对不起，此照片有缺损无法预览！", 6000).show();
					}else{
						mZoomView.setImage(zoomBitmap);
					}
				}
			}
			if(b){
				zoomBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.soso_gray_logo);
				mZoomView.setImage(zoomBitmap);
				progressBar.setVisibility(View.VISIBLE);
				new Thread(runnable).start();
			}
		}else{
			if (!isViewIntent()) {
				pathes = intent.getStringArrayListExtra("pathes");
				mIndex = intent.getIntExtra("index", 0);
			} else {
				pathes = new ArrayList<String>();
				pathes.add(intent.getData().getPath());
				mIndex = 0;
			}
			zoomBitmap = getDrawable(mIndex,1);
			if(zoomBitmap == null){
				Toast.makeText(this, "对不起，此照片有缺损无法预览！", 6000).show();
			}else{
				mZoomView.setImage(zoomBitmap);
			}
		}
		
	}
	private void findView(){
		mZoomView = (ZoomImageView) findViewById(R.id.zoomview);
		zcontrols = (ZoomControls) findViewById(R.id.zoomcontrol);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
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
    
    @Override
	public void onClick(View v) {
		if(v == backBtn){
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
		}
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.zhihuigu.sosoOffice.BaseActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if(imagedownloadersync!=null){
			imagedownloadersync.overReponse();
		}
		super.onDestroy();
	}
	
}
