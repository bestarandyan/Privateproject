/**
 * 图片预览类，可以放大缩小图片，如果有多找照片可以切换查看
 */
package com.qingfengweb.id.biluomiV2;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.util.CommonUtils;
import com.qingfengweb.view.ZoomImageView;


/**
 * @author 刘星星
 * @CreateDate 2012年1月6日
 *
 */
public class ImagePreViewActivity extends BaseActivity{

	private int mIndex;

	private int mItemwidth;
	private int mItemHerght;

	private ArrayList<String> pathes;

//	private ProgressBar mProgressBar;

	/** Image zoom view */
	private ImageView mZoomView;

	/** Zoom state */
	/** On touch listener for zoom view */
	private Bitmap zoomBitmap;
//	private ZoomControls zcontrols;

	public int getmIndex() {
		return mIndex;
	}
	private Button backBtn;


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
//		MyApplication.getInstance().setWidthPixels(mItemwidth);
//		MyApplication.getInstance().setHeightPixels(mItemHerght);
		setContentView(R.layout.a_imagepreview);
		mZoomView = (ImageView) findViewById(R.id.zoomview);
//		zcontrols = (ZoomControls) findViewById(R.id.zoomcontrol);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		mZoomView.setZcontrols(zcontrols);
//		HashMap<String, Object> map = (HashMap<String, Object>) getIntent()
//				.getSerializableExtra("map");
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//				Integer.parseInt(map.get("pixelswidth")
//						.toString()), Integer.parseInt(map
//						.get("pixelsheight").toString()));
//		mZoomView.setLayoutParams(params);
//		boolean b = true;
		String imgUrl = getIntent().getStringExtra("url");
		File file = new File(imgUrl);
		if (file.exists() && file.isFile()) {
			zoomBitmap = getDrawable(file.getAbsolutePath());
			if (zoomBitmap != null) {
				mZoomView.setImageBitmap(zoomBitmap);
//				mZoomView.setBackgroudImage(zoomBitmap);
			}
		}
	}


	 public void onLongPress(MotionEvent event) {
//		 zcontrols.show();

     }
	private Bitmap getDrawable(String path) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int mWidth = options.outWidth;
			int mHeight = options.outHeight;
			int s = 1;
			while ((mWidth / s > mItemwidth * 2 * 1) || (mHeight / s > mItemHerght * 2 * 1)) {
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
				int neww = ft >= fs ? mItemwidth * 1 : (int) (mItemHerght * 1 * ft);
				int newh = ft >= fs ? (int) (mItemwidth * 1 / ft) : mItemHerght * 1;
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
