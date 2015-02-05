/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.beans.TaoXiBean;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.DBHelper;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.filedownload.CallbackImpl;
import com.qingfengweb.weddingideas.filedownload.ImageLoadFromUrlOrId;
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
import android.widget.TextView;

/**
 * @author 刘星星
 *
 */
@TargetApi(Build.VERSION_CODES.ECLAIR) public class DetailTaoXiActivity extends BaseActivity{
	Map<String,Object>  map = null;
	TextView descripTv;
	Bitmap bitmap= null;
	DBHelper dbHelper = null;
	ImageView imageView;
	CallbackImpl callbackImpl = null;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_aboutus);
		initView();
		initData();
	}
	private void initView(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.tellBtn).setOnClickListener(this);
		findViewById(R.id.telLayout).setOnClickListener(this);
		descripTv = (TextView) findViewById(R.id.descripTv);
	}
	@SuppressWarnings("unchecked")
	private void initData(){
		dbHelper = DBHelper.getInstance(this);
		String id= "";
		id = getIntent().getStringExtra("id");
		map = new HashMap<String, Object>();
		String sql = "select *from "+TaoXiBean.tbName+" where id='"+id+"'";
		map = dbHelper.selectRow(sql, null).get(0);
		if(map!=null){
			String descrip = map.get("set_desc").toString();
			String name = map.get("s_name").toString();
			String price = "￥"+map.get("price_cut").toString()+"元";
			String tag = "标签："+map.get("c_photo").toString();
			((TextView)findViewById(R.id.name)).setText(name);
			((TextView)findViewById(R.id.topView)).setText(name);
			((TextView)findViewById(R.id.price)).setText(price);
			((TextView)findViewById(R.id.tagTv)).setText(tag);
//			bitmap= PicHandler.getDrawable(getResources(), Integer.parseInt(map.get("imageid").toString()), MyApplication.getInstance().getScreenW(), (int) (MyApplication.getInstance().getScreenH()*0.4));
			imageView = ((ImageView)findViewById(R.id.taoxiImg));
			String photo_c = "";
			if(map.get("photo_c")!=null && !map.get("photo_c").equals("null")){
				photo_c = map.get("photo_c").toString();
			}
			if(photo_c!=null && !photo_c.equals("")){
				photo_c = "http://img.weddingideas.cn"+photo_c;
				String[] fileStrings = photo_c.trim().split("/");
				String  nameString = fileStrings[fileStrings.length-1];
				 File file = new File(ConstantValues.sdcardUrl+ConstantValues.taoxiImgUrl+nameString);
				 if(file.exists()){
					 BitmapFactory.Options opts = new BitmapFactory.Options();
						try {
							opts.inJustDecodeBounds = true;
							opts.inPreferredConfig = Bitmap.Config.RGB_565;
							opts.inSampleSize = PicHandler.computeSampleSize(opts, -1, 1000 * 1000);
							opts.inJustDecodeBounds = false;
							bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
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
						} catch (OutOfMemoryError e) {
							
						}
					 if(bitmap!=null){
						 imageView.setImageBitmap(bitmap);
					 }else{
						    callbackImpl = new CallbackImpl(this,imageView);
							ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
							imageLoad.loadImageFromUrl(this, R.drawable.img_default, photo_c, ConstantValues.taoxiImgUrl, callbackImpl, false, 480);
					 }
				 }else{
					 	callbackImpl = new CallbackImpl(this,imageView);
						ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
						imageLoad.loadImageFromUrl(this, R.drawable.img_default, photo_c, ConstantValues.taoxiImgUrl, callbackImpl, false, 480);
				 }
			}
			descripTv.setText(descrip);
		}
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v.getId() == R.id.tellBtn){//拨打电话咨询
			showCallDialog(LoadingActivity.photoOne);
		}else if(v.getId() == R.id.telLayout){//拨打电话咨询
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
		if(bitmap!=null){
			bitmap.recycle();
			bitmap = null;
		}
		if(map!=null){
			map.clear();
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
