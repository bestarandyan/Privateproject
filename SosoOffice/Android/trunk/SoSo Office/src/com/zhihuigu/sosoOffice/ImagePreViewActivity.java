/**
 * 图片预览类，可以放大缩小图片，如果有多找照片可以切换查看
 */
package com.zhihuigu.sosoOffice;

import java.io.File;
import java.util.HashMap;

import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.utils.BitmapCache;
import com.zhihuigu.sosoOffice.utils.CommonUtils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * @author 刘星星
 * @CreateDate 2012年1月6日
 *
 */
public class ImagePreViewActivity extends BaseActivity{
	private ImageView imageView;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	int lastX = 0, lastY = 0;  
	private float oldDist;
	private Button backBtn;
	private Bitmap bm = null;
	private RelativeLayout topLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_imagepreview);
		imageView = (ImageView) findViewById(R.id.imageView);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) getIntent()
				.getSerializableExtra("map");
		if(map!=null){
			boolean b = true;
			File file = (File) map.get("file");
//			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(Integer.parseInt(map.get("pixelswidth").toString())
//					,Integer.parseInt(map.get("pixelsheight").toString()));
//			imageView.setLayoutParams(param);
			if(file.exists()&&file.isFile()){
				Bitmap bitmap = BitmapCache.getInstance().getBitmap(file, this);
				if(bitmap!=null){
					imageView.setImageBitmap(bitmap);
					b=false;
				}
			}
			if(b){
//				imageView.setImageResource(R.drawable.soso_gray_logo);
				MyApplication.getInstance(this).getImageDownloaderId().download((File) map.get("file"), 
						map.get("sql").toString(),
						map.get("id").toString(), 
						map.get("pixelswidth").toString(),
						map.get("pixelsheight").toString(),
						map.get("request_name").toString(),
						imageView,"0", "0",map.get("pixelswidth").toString(),
						map.get("pixelsheight").toString());
			}
		}else{
//			imageView.setMinimumWidth(MyApplication.getInstance(ImagePreViewActivity.this).getScreenWidth());
//			imageView.setMinimumHeight(MyApplication.getInstance(ImagePreViewActivity.this).getScreenHeight());
			bm = CommonUtils.getDrawable(getIntent().getStringExtra("path"), null);
			imageView.setImageBitmap(bm);	
		}
		backBtn = (Button) findViewById(R.id.backBtn);
		topLayout = (RelativeLayout) findViewById(R.id.topLinear);
		backBtn.setOnClickListener(this);
		imageView.setMinimumWidth(MyApplication.getInstance(ImagePreViewActivity.this).getScreenWidth());
		imageView.setMinimumHeight(MyApplication.getInstance(ImagePreViewActivity.this).getScreenHeight()-topLayout.getHeight());
		Thread thread = new Thread(runnable);
		thread.start();
	}
	@Override
	protected void onDestroy() {
		if(bm!=null && !bm.isRecycled()){
			bm.recycle();
		}
		super.onDestroy();
	}
	 Runnable runnable =  new Runnable() {
		public void run() {
			try {
				Thread.sleep(100);
				handler.sendEmptyMessage(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			int x=0,y=0;
			if(bm!=null){
				if(bm.getWidth() >= MyApplication.getInstance(ImagePreViewActivity.this).getScreenWidth()){
					x = 0;
				}else{
					x = (MyApplication.getInstance(ImagePreViewActivity.this).getScreenWidth()-bm.getWidth())/2;
				}
				if(bm.getHeight() >= MyApplication.getInstance(ImagePreViewActivity.this).getScreenHeight()-topLayout.getHeight()){
					y=0;
				}else{
					y = (MyApplication.getInstance(ImagePreViewActivity.this).getScreenHeight()-topLayout.getHeight()-bm.getHeight())/2;
				}
			}
			
			savedMatrix.set(matrix); // 把原始 Matrix对象保存起来
			matrix.set(savedMatrix);
			matrix.postTranslate(x,y);
			imageView.invalidate();//重绘    
			imageView.setImageMatrix(matrix);
			super.handleMessage(msg);
		}
		
	};
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix); // 把原始 Matrix对象保存起来
			start.set(event.getX(), event.getY()); // 设置x,y坐标
			mode = DRAG;
			lastX = (int) event.getRawX();   
	        lastY = (int) event.getRawY(); 
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event); // 求出手指两点的中点
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x,event.getY() - start.y);
				imageView.invalidate();//重绘    
		           break;   
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
					matrix.mapRect(rectf);
				}
				}
			break;
		}
		imageView.setImageMatrix(matrix);
		return true;
	}
	RectF rectf = new RectF();
	// 求两点距离
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}
		// 求两点间中点
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			setResult(6);
			finish();
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(6);
			finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	}
