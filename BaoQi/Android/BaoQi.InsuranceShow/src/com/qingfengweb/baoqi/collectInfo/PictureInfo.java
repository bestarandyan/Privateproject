package com.qingfengweb.baoqi.collectInfo;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import com.qingfengweb.baoqi.insuranceShow.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

public class PictureInfo extends Activity implements  AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener {
private Gallery gallery;
GalleryAdapter gallerAdapter;	
Vector imgVec;
Button bt1,bt2,bt3,bt4;
private File mCurrentPhotoFile;//照相机拍照得到的图片 
private ArrayList<String> imagePathes;
FinalValues finalValues=new FinalValues();
private Intent intent;
private File fileValue = null ;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.picture);
	gallery = (Gallery) findViewById(R.id.gallery);
	imgVec = new Vector();
	imagePathes = new ArrayList<String>();
	 MyApplication.getInstance().setImagePathes(imagePathes);
	bt1=(Button) findViewById(R.id.paishe);
	bt2=(Button) findViewById(R.id.picture_back);
	bt3=(Button) findViewById(R.id.picture_delete);
	bt4=(Button) findViewById(R.id.picture_yulan);
	 intent=getIntent();
	 int in=intent.getIntExtra("btnvalue", 0);
	 switch(in){
	 case 1:
		 fileValue = finalValues.PHOTO_COLLECT_SUOPEI_DIR;
		 break;
	 case 2:
		 fileValue = finalValues.PHOTO_COLLECT_JIASHI_DIR;
		 break;
	 case 3:
		 fileValue = finalValues.PHOTO_COLLECT_PEIKZHJIAN_DIR;
		 break;
	 case 4:
		 fileValue = finalValues.PHOTO_COLLECT_SURVEY_DIR;
		 break;
	 case 5:
		 fileValue = finalValues.PHOTO_COLLECT_BAODANZHENGBEN_DIR;
		 break;
	 case 6:
		 fileValue = finalValues.PHOTO_COLLECT_SHIGUCHULI_DIR;
		 break;
	 case 7:
		 fileValue = finalValues.PHOTO_COLLECT_FAYUANZHENGMING_DIR;
		 break;
	 case 8:
		 fileValue = finalValues.PHOTO_COLLECT_CHESUN_DIR;
		 break;
	 case 9:
		 fileValue = finalValues.PHOTO_COLLECT_CAICHANSUNSHI_DIR;
		 break;
	 case 10:
		 fileValue = finalValues.PHOTO_COLLECT_PERSONSUNSHI_DIR;
		 break;
	 case 11:
		 fileValue = finalValues.PHOTO_COLLECT_CHELIANGDAOQIANG_DIR;
		 break;
	 case 12:
		 fileValue = finalValues.PHOTO_COLLECT_QITA_DIR;
		 break;
	 }
	byte bb[] = intent.getByteArrayExtra("pic");
	if (bb != null) {
		Bitmap bm = BitmapFactory.decodeByteArray(bb, 0, bb.length);
		imgVec.add(bm);
		
		gallerAdapter = new GalleryAdapter(this,imgVec);
		gallery.setAdapter(gallerAdapter);
		
		gallery.setOnItemSelectedListener(this);
		gallery.setOnItemClickListener(this);
		gallery.setSelection(imgVec.size()/2,true);
		commResultActivityFun(fileValue,bm);
		intent.getExtras().remove("pic");
	}
	System.gc();
	bt1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(PictureInfo.this, CameraPreview1.class);
			startActivityForResult(i, 1);
			
		}
	});
	bt2.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(PictureInfo.this,CollCarInfo.class);
			startActivity(intent);
			finish();
			
		}
	});
	bt3.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if(imgVec!=null){
				if(imgVec.size()>0)
				{
					imgVec.remove(ImgSelectIndex);
					gallerAdapter = new GalleryAdapter(PictureInfo.this,imgVec);
					gallery.setOnItemSelectedListener(PictureInfo.this);
					
					gallery.setAdapter(gallerAdapter);
					gallery.setSelection(imgVec.size()/2,true);
					mCurrentPhotoFile = new File(imagePathes.get(ImgSelectIndex));
					mCurrentPhotoFile.delete();
					imagePathes.remove(ImgSelectIndex);
					
				}else {
					
					dialogWronFun("没有要删除的照片数据!",PictureInfo.this);
					return;
				}
			}
		}
	});
	bt4.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent it = new Intent(PictureInfo.this, ImageSwitcher.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			it.putStringArrayListExtra("pathes", MyApplication.getInstance().getImagePathes());
			it.putExtra("index", ImgSelectIndex);
			startActivity(it);
		}
	});
}
public void dialogWronFun(CharSequence str,Context context){
  	LayoutInflater inflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
	    View view=inflater.inflate(R.layout.worningdialog, null);
	    TextView tv=(TextView) view.findViewById(R.id.worningTv);
	    tv.setText(str);
  	AlertDialog.Builder alert=new AlertDialog.Builder(context);
  	alert.setView(view);
  	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
  			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		}).create().show();
  	
  }
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	setTitle( requestCode + "=" + resultCode);
//	if(resultCode ==21){
//		
//		String res=data.getExtras().getString("pic");
//		
//		setTitle( requestCode + "=" + resultCode+"="+res);
//	}
//	else
	if (resultCode == 20) {

		byte bb[] = data.getByteArrayExtra("pic");
		if (bb != null) {
			Bitmap bm = BitmapFactory.decodeByteArray(bb, 0, bb.length);
			imgVec.add(bm);
			commResultActivityFun(fileValue,bm);
//			setTitle(requestCode + "=" + resultCode + ":"
//					+ bb.length / 1024+":"+imgVec.size());
			gallerAdapter = new GalleryAdapter(this,imgVec);
			gallery.setOnItemSelectedListener(this);
			
			gallery.setAdapter(gallerAdapter);
			gallery.setOnItemClickListener(this);
			gallery.setSelection(imgVec.size()/2,true);
			data.getExtras().remove("pic");
		}
		System.gc();
	}
	super.onActivityResult(requestCode, resultCode, data);

}
private void commResultActivityFun(File dir,Bitmap bm){
	
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {      
			dir.mkdirs();// 创建照片的存储目录  
            mCurrentPhotoFile = new File(dir, getPhotoFileName());// 给新照的照片文件命名  
            Thread thread = new Thread(new CanvasPhotoActivity(bm, mCurrentPhotoFile,PictureInfo.this,getIntent().getStringExtra("cameraPreviewdizhi"),PictureInfo.this));
            thread.start();//启动线程  为获得的图片添加水印并把文件存储到本地目录
            imagePathes.add(mCurrentPhotoFile.getAbsolutePath());
		
		
		
			} else {       
				//dialogWronFun(getString(R.string.worn_sd).toString(),MainControl.this);
				
				return;
				}
	
	
}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	
	switch(keyCode){
	case KeyEvent.KEYCODE_BACK:
		Intent intent = new Intent(this,CollCarInfo.class);
		startActivity(intent);
		finish();
		break;
	}
	return super.onKeyDown(keyCode, event);
}
/** 
 * 用当前时间给取得的图片命名 
 *  
 */  
 private String getPhotoFileName() {  
     Date date = new Date(System.currentTimeMillis());  
     SimpleDateFormat dateFormat = new SimpleDateFormat(  
             "'IMG'_yyyyMMdd_HHmmss");  
     return dateFormat.format(date) + ".jpg";  
 } 
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	private int ImgSelectIndex = 0;
	private int ImgClickIndex = 0;
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		ImgSelectIndex = arg2;
	}
	
	/**
	 * 照片类型选中
	 * */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		ImgClickIndex=arg2;
		if(ImgClickIndex==ImgSelectIndex){
					Intent it = new Intent(PictureInfo.this, ImageSwitcher.class);
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
					it.putStringArrayListExtra("pathes", MyApplication.getInstance().getImagePathes());
					it.putExtra("index", ImgSelectIndex);
					startActivity(it);
	
	}
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	float x_temp01 = 0.0f;
	float y_temp01 = 0.0f;

	float x_temp02 = 0.0f;
	float y_temp02 = 0.0f;

	public boolean onTouchEvent(MotionEvent event) {
		// 获得当前坐标
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			x_temp01 = x;
			y_temp01 = y;
		}
			break;
		case MotionEvent.ACTION_UP: {
			x_temp02 = x;
			y_temp02 = y;

			if (x_temp01 != 0 && y_temp01 != 0)//
			{
				// 比较x_temp01和x_temp02
				// x_temp01>x_temp02 //向左
				// x_temp01==x_temp02 //竖直方向或者没动
				// x_temp01<x_temp02 //向右

				if (x_temp01 > x_temp02 + 8)// 向左
				{
					// 移动了x_temp01-x_temp02
					// setTitle("左移");

				}

				if (x_temp01 + 8 < x_temp02)// 向右
				{
					// 移动了x_temp02-x_temp01
					// setTitle("右移");
				//	finish();
					
					
				}
			}
		}
			break;
		case MotionEvent.ACTION_MOVE: 
		{

		}
			break;

		}
		return super.onTouchEvent(event);
	}
	
}
