/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * @author 刘星星
 * @createDate 2013、12、28
 * 活动详情
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR) public class DetailHuoDongActivity extends BaseActivity{
	ImageView imageView;
	Bitmap bitmap =null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailhuodong);
		initView();
	}
	private void initView(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.tellBtn).setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.imageView);
		String pic_active = getIntent().getStringExtra("pic_active");
		pic_active = pic_active.split("/")[pic_active.split("/").length-1];//得到名字
		pic_active = ConstantValues.sdcardUrl+ConstantValues.huodongImgUrl+pic_active;
//		bitmap = PicHandler.getDrawable(pic_active, MyApplication.getInstance().getScreenW(), MyApplication.getInstance().getScreenH());
		 try {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
				BitmapFactory.decodeFile(pic_active, opts);
				opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,1000 * 1000);
				opts.inJustDecodeBounds = false;
				// 如果图片还未回收，先强制回收该图片
				bitmap = BitmapFactory.decodeFile(pic_active, opts);
				if (bitmap != null) {
					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					int newWidth = MyApplication.getInstance().getScreenW();
					// 计算缩放比例
					float scaleWidth = ((float) newWidth) / width;
					// 取得想要缩放的matrix参数
					Matrix matrix = new Matrix();
					matrix.postScale(scaleWidth, scaleWidth);
					// 得到新的图片
					bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
				}
				System.gc();
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 if(bitmap!=null){
			 imageView.setImageBitmap(bitmap);
			 bitmap = null;
			 System.gc();
		 }
		
	}
	@TargetApi(Build.VERSION_CODES.ECLAIR) @Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v.getId() == R.id.tellBtn){
			showCallDialog(LoadingActivity.photoOne);
		}
		super.onClick(v);
	}
	
	@Override
	protected void onDestroy() {
		if(imageView!=null){
			BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
			if (bitmapDrawable != null && bitmapDrawable.getBitmap()!=null && !bitmapDrawable.getBitmap().isRecycled()) {
				bitmapDrawable.getBitmap().recycle();
			}
		}
		if(bitmap!=null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
		System.gc();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return true;
	}
}
