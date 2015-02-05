/**
 * 
 */
package com.boluomi.children.activity;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.boluomi.children.R;
import com.boluomi.children.data.ConstantsValues;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.util.CommonUtils;
import com.qingfengweb.imagehandle.PicHandler;


/**
 * @author 刘星星
 * @createDate 2013/11/12
 *
 */
public class TakePhotoActivity extends BaseActivity{
	private String fileName = "";
	private File sdcardTempFile = null;
	private String path = "";//图片的最终路径
	private final String IMAGE_TYPE = "image/*";
	public String currentFilePath = "";//当前正在上传的文件路径
	private ImageView photoImageView;
	private int photoType = 1;
	private ImageButton  backBtn;
	private Button trueBtn,cancleBtn;
	private EditText sayEt;
	public static boolean isGetPhoto = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_takephoto);
		findView();
		photoType = getIntent().getIntExtra("photoType", 1);
		if(photoType == 1){
			if(!isGetPhoto){
				takePhoto();
				isGetPhoto = true;
			}
		}else{
			getAlbumPhoto();
		}
	}
	
	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(sayEt.getWindowToken(), 0); //强制隐藏键盘
		if(v == backBtn){
			setResult(1);
			finish();
		}else if(v == trueBtn){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("imgUrl", currentFilePath);
			map.put("photoname", sayEt.getText().toString());
			Intent intent = new Intent();
			intent.putExtra("imgInfo", (Serializable)map);
			setResult(1, intent);
			finish();
		}else if(v == cancleBtn){
			setResult(1);
			finish();
		}else if(v == photoImageView){
		}
		super.onClick(v);
	}
	private void findView(){
		photoImageView = (ImageView) findViewById(R.id.imageView);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		trueBtn = (Button) findViewById(R.id.trueBtn);
		cancleBtn = (Button) findViewById(R.id.cancleBtn);
		sayEt = (EditText) findViewById(R.id.msgEt);
		backBtn.setOnClickListener(this);
		trueBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);
		photoImageView.setOnClickListener(this);
	}
	@Override
	protected void onDestroy() {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) photoImageView.getDrawable();
		if (bitmapDrawable != null&& !bitmapDrawable.getBitmap().isRecycled()) {
			bitmapDrawable.getBitmap().recycle();
		}
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(1);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			sayEt.requestFocus();
			CommonUtils.showSoftKey(sayEt);
			super.handleMessage(msg);
		}
		
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 0){
			setResult(1);
			finish();
		}
		if(requestCode == 1){
			if(path!=null && !path.equals("")){//拍照返回处理
//				Bitmap bitmap = PicHandler.getDrawable(path, 720, 1280);
				Bitmap bitmap = PicHandler.getDrawable(path, photoImageView);
//				Bitmap bitmap = BitmapFactory.decodeFile(path);
				if(bitmap!=null){
					boolean saveImg = PicHandler.OutPutImage(new File(path), bitmap);
					if(saveImg){//压缩并保存成功后  删除原有图片
						currentFilePath = path;
					}else{
						return;
					}
					photoImageView.setImageBitmap(bitmap);
					handler.sendEmptyMessageDelayed(0, 500);
				}
				
			}
		} else if (requestCode == 2) {// 选择图片返回
			if(data == null){
				return;
			}
			String imgPath = "";
			Uri originalUri = data.getData(); // 获得图片的uri
			String[] proj = { MediaStore.Images.Media.DATA };
			// 好像是android多媒体数据库的封装接口，具体的看Android文档
			Cursor cursor = managedQuery(originalUri, proj, null, null, null);
			// 按我个人理解 这个是获得用户选择的图片的索引值
			if (cursor == null) {
				imgPath = data.getDataString();
			} else {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// 将光标移至开头 ，这个很重要，不小心很容易引起越界
				cursor.moveToFirst();
				// 最后根据索引值获取图片路径www.2cto.com
				imgPath = cursor.getString(column_index);
			}

			File fromFile = new File(imgPath);
//			if(fromFile.getParentFile().getName() .contains("thumbnail")){
//				Toast.makeText(this, "不能选择缩略图！", 3000).show();
//				return;
//			}
			// 将文件复制到自定义文件
			if (fromFile != null && fromFile.exists()) {
				fileName = CommonUtils.getFileName();// 给图片起新的名字
				File file = new File(ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MYALBUM_IMG_URL);
				if (!file.exists()) {
					file.mkdirs();
				}
				sdcardTempFile = new File(ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MYALBUM_IMG_URL, fileName);
//				path = sdcardTempFile.getAbsolutePath();
//				FileCopyUtil.copyfile(fromFile, sdcardTempFile, true);
//				Bitmap bitmap = PicHandler.getDrawable(fromFile.getAbsolutePath(), 1680, 1980);
				Bitmap bitmap = BitmapFactory.decodeFile(fromFile.getAbsolutePath());
				if(bitmap!=null){
					boolean copy = PicHandler.OutPutImage(sdcardTempFile, bitmap);
					if (copy) {
						currentFilePath = sdcardTempFile.getAbsolutePath();
						photoImageView.setImageBitmap(bitmap);
						handler.sendEmptyMessageDelayed(0, 500);
					}
				}else{
					Toast.makeText(this, "图片不存在", 3000).show();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 从相册获取图片
	 * @author 刘星星
	 */
	private void getAlbumPhoto(){
//		dismiss();
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, 2);
	}
	/**
	 * 通过调用自带照相机获取图片
	 * @author 刘星星
	 */
	private void takePhoto() {
//		dismiss();
		Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		 fileName = CommonUtils.getFileName();
		File file = new File(ConstantsValues.SDCARD_ROOT_PATH + ConstantsValues.MYALBUM_IMG_URL);
		if(!file.exists()){
			file.mkdirs();
		}
		sdcardTempFile = new File(ConstantsValues.SDCARD_ROOT_PATH + ConstantsValues.MYALBUM_IMG_URL, fileName);
		path = sdcardTempFile.getAbsolutePath();
		MyApplication.getInstant().setCurrentGrowupImgPath(path);
		if (CommonUtils.isHasSdcard()) {
			//图片输出大小
			openCamera.putExtra("outputX", MyApplication.getInstant().getWidthPixels());
			openCamera.putExtra("outputY", MyApplication.getInstant().getHeightPixels());
			openCamera.putExtra("scale", true);
			openCamera.putExtra("return-data", true);
//			openCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
			openCamera.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
//			openCamera.putExtra("return-data", true);
		}
		startActivityForResult(openCamera, 1);  
	}
}
