package com.chinaLife.claimAssistant.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.activity.Sc_CaseOfOnlyOneActivity.MyLocationListener;
import com.chinaLife.claimAssistant.bean.sc_LegendInfo;
import com.chinaLife.claimAssistant.content.sc_Contentvalues;
import com.chinaLife.claimAssistant.thread.sc_PicHandler;
import com.chinaLife.claimAssistant.tools.sc_BitmapCache;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;

public class Sc_MyCameraActivity extends Activity implements
		SurfaceHolder.Callback, OnClickListener {
	private RelativeLayout layout1 = null;
	private RelativeLayout layout2 = null;
	private SurfaceView cameraview = null;
	private SurfaceHolder surfaceHolder = null;
	private Camera mcamera = null;
	private ImageButton btn = null;
	/** Image zoom view */
	private Sc_ZoomImageView mZoomView = null;      
	/** Zoom state */
	/** On touch listener for zoom view */
	private Bitmap zoomBitmap = null;
	//private ZoomControls zcontrols;
	private byte[] datas = null;
	private Button keepphoto = null;
	private Button retake = null;
	private Button zoomOut,zoomIn;
	private ImageView legendimage = null;
	private TextView legendtext = null;
	public Bitmap bmp = null;
	private Drawable drawable = null;
	public Bitmap menban = null;
	private final static float TARGET_HEAP_UTILIZATION = 0.75f; 
	sc_PicHandler ph = null;
	private ProgressDialog progress = null;
	
	private boolean isTouch = true;
	private boolean isTake = true;
	public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.sc_a_camera);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=format.format(new Date());
		if(Sc_MyApplication.getInstance().getNowTime() == null){
			Sc_MyApplication.getInstance().setNowTime(nowTime);
			getLocation();
			System.gc();
		}else{
			try {
				java.util.Date begin=format.parse(Sc_MyApplication.getInstance().getNowTime());
				java.util.Date end = format.parse(nowTime);  
				long l = (end.getTime()-begin.getTime())/1000/60;//得到时间差转成秒
				if(l>3 || Sc_MyApplication.getInstance().getAddress().length()<2){
					Sc_MyApplication.getInstance().setNowTime(nowTime);
					getLocation();
					System.gc();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
		initView();
		initData();
		initViewFun();
		
	}
	private void getLocation(){
		mLocationClient = new LocationClient(getApplicationContext());  //声明LocationClient类   
		mLocationClient.registerLocationListener( myListener );   	//注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())	{
			mLocationClient.requestLocation();
		}else 	{
			Log.d("LocSDK3", "locClient is null or not started");
		}
	}
	public class MyLocationListener implements BDLocationListener {	
		@Override	
		public void onReceiveLocation(BDLocation location) {
			if (location == null)		
				return ;	
			Sc_MyApplication.getInstance().setLatitude((float) location.getLatitude());
			Sc_MyApplication.getInstance().setLongitude((float) location.getLongitude());
			if(location.getAddrStr()!=null){
				Sc_MyApplication.getInstance().setAddress(location.getAddrStr());
			}
			System.out.println("获取到的当前地址为：================"+location.getAddrStr());
			}
		public void onReceivePoi(BDLocation poiLocation) {	
				if (poiLocation == null){		
					return ;		
					}		
				}
			}
	private void showProgress(){
		if(progress!=null)
		progress.cancel();
		progress = null;
		progress = new ProgressDialog(this);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setMessage("图片处理中，请稍候。。");
		progress.show();
	}
	private void initViewFun() {
		// btn.setOnClickListener(this);
		zoomOut.setOnClickListener(this);
		zoomIn.setOnClickListener(this);
	}

	private void initView() {
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		zoomOut = (Button) findViewById(R.id.zoomOut);
		legendimage = (ImageView) findViewById(R.id.legendimage);
		legendtext = (TextView) findViewById(R.id.legendtext);
		zoomIn = (Button) findViewById(R.id.zoomIn);
		// btn = (ImageButton) findViewById(R.id.btn);
	}
	
	public void notifyLayout1() {
		cameraview = (SurfaceView) this.findViewById(R.id.surface_camera);
		surfaceHolder = cameraview.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		cameraview.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(isTouch){
					isTouch = false;
					isTake = false;
				}else {
					return false;
				}
				if(mcamera != null){
					try{
						mcamera.cancelAutoFocus(); //release the previous auto-focus
						mcamera.autoFocus(new Camera.AutoFocusCallback(){
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							Log.d("HOME", "isAutofoucs " + Boolean.toString(success));	
							isTouch = true;
							isTake = true;
						}
					} );
					}catch(final Exception e){
						isTouch = true;
						isTake = true;
						new Thread(new Runnable() {
							@Override
							public void run() {
								sc_LogUtil.sendLog(2, "拍照界面，对焦失败："+e.getMessage());
							}
						}).start();
//						if(MyApplication.opLogger!=null){
//							MyApplication.opLogger.error("对焦失败",e);
//						}
					}
				}else{
					isTouch = true;
					isTake = true;
				}
				return false;
			}
		});
		btn = (ImageButton) findViewById(R.id.btn);
		legendimage.destroyDrawingCache();
		legendtext.setText(sc_LegendInfo.getMasktext(Sc_MyApplication.getInstance().getLegendid()+""));
		if(sc_LegendInfo.getMaskImage(Sc_MyApplication.getInstance().getLegendid()+"")!=0){
			try{
				BitmapDrawable bitmapDrawable = (BitmapDrawable) legendimage.getDrawable();
				if(bitmapDrawable!=null && !bitmapDrawable.getBitmap().isRecycled()){
					bitmapDrawable.getBitmap().recycle();
				}
				/*if(legendimage!=null)
					legendimage.destroyDrawingCache();*/
				menban = getDrawable(sc_LegendInfo.getMaskImage(Sc_MyApplication.getInstance().getLegendid()+""));
				legendimage.setImageBitmap(menban);
				/*if(drawable!=null){
					drawable.clearColorFilter();
					drawable = null;
				}
				  Drawable drawable1 = getResources().getDrawable(Contentvalues.Legend1Image[MyApplication.getInstance().getLegendid()-1]);
		          drawable = drawable1;
		          Drawable drawable2 = drawable;
		          legendimage.setImageDrawable(drawable2);*/
		          
//				legendimage.setImageResource(Contentvalues.Legend1Image[MyApplication.getInstance().getLegendid()-1]);

				
				//				legendimage.setImageBitmap(BitmapCache.getInstance().getBitmap(Contentvalues.Legend1Image[MyApplication.getInstance().getLegendid()-1], MyCameraActivity.this));
			}catch(final OutOfMemoryError e){
				BitmapDrawable bitmapDrawable = (BitmapDrawable) legendimage.getDrawable();
				if(bitmapDrawable!=null && !bitmapDrawable.getBitmap().isRecycled()){
					bitmapDrawable.getBitmap().recycle();
				}
				/*if(legendimage!=null)
					legendimage.destroyDrawingCache();*/
				legendimage.setImageBitmap(sc_BitmapCache.getInstance().getBitmap(sc_LegendInfo.getMaskImage(Sc_MyApplication.getInstance().getLegendid()+""), Sc_MyCameraActivity.this,null));
				new Thread(new Runnable() {
					@Override
					public void run() {
						sc_LogUtil.sendLog(2, "拍照界面出现内存溢出："+e.getMessage());
					}
				}).start();
//				if(MyApplication.opLogger!=null){
//					MyApplication.opLogger.error("拍照出错",e);
//				}
				
			}
		}else{
			legendimage.setVisibility(View.GONE);
		}
		btn.setOnClickListener(this);
	}
	/**
	 * 用来处理拍摄得到的照片 包括压缩和打水印
	 * 
	 * @author  刘星星
	 */
	public Runnable lxxRunnable = new Runnable() {
		@Override
		public void run() {
			/*if(bitmap!=null){
				bitmap.recycle();
				bitmap = null;
			}*/
//			MyApplication.getInstance().setLegendid(legendid);
			ph = new sc_PicHandler(datas);
			ph.handlePic();
			lxxHandler.sendEmptyMessage(0);
//			MyApplication.switch_tag = true;
		}
	};
	public Handler lxxHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if(mcamera!=null){
					mcamera.release();
					mcamera = null;
				}
				if(zoomBitmap!=null){
					zoomBitmap.recycle();
					zoomBitmap = null;
				}
			
				if(bmp!=null){
					bmp.recycle();
					bmp = null;
				}
				datas = null;
				System.gc();
				/*if(menban!=null){
					menban.recycle();
				}*/
				progress.dismiss();
				
				Intent i = new Intent();
//				MyApplication.getInstance().setDataByte(datas);
				Sc_MyCameraActivity.this.setResult(RESULT_OK, i);
				Sc_MyCameraActivity.this.finish();
			}
			super.handleMessage(msg);
		}

	};
	/**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */  
    public  Bitmap readBitMap(Context context, int resId){  
        BitmapFactory.Options opt = new BitmapFactory.Options();  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
        opt.inPurgeable = true;  
        opt.inInputShareable = true;  
          //获取资源图片  
       InputStream is = context.getResources().openRawResource(resId);  
      return BitmapFactory.decodeStream(is,null,opt);  
   }

//	private int success_flag = 0;//用来判断图片处理是否成功
	public void notifyLayout2() {
		mZoomView = (Sc_ZoomImageView) findViewById(R.id.zoomview);
		keepphoto = (Button) findViewById(R.id.keepphoto);
		retake = (Button) findViewById(R.id.retake);
		keepphoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showProgress();
				Thread thread = new Thread(lxxRunnable);
				thread.start();
			}
		});

		retake.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout2.setVisibility(View.GONE);
				if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
					zoomBitmap.recycle();
					zoomBitmap = null;
					System.gc();
				}
				if(bmp!=null){
					bmp.recycle();
					bmp = null;
					System.gc();
				}
				layout1.setVisibility(View.VISIBLE);
				notifyLayout1();
				//mZoomView.setImage(null);
				mcamera.startPreview();
				isTouch = true;
			}
		});
		if(zoomBitmap!=null && !zoomBitmap.isRecycled()){
			zoomBitmap.recycle();
			zoomBitmap = null;
			System.gc();
		}
		if(mZoomView.getDrawingCache()!=null && !mZoomView.getDrawingCache().isRecycled()){
			mZoomView.getDrawingCache().recycle();
		}
		if(mZoomView!=null)
			mZoomView.destroyDrawingCache();
			zoomBitmap = getDrawable1(datas, 1);
//			zoomBitmap = null;
			if(zoomBitmap!=null){//如果生成预览图成功则显示预览图
//				success_flag = 0;
				mZoomView.setImage(zoomBitmap);
				
//				MyApplication.getInstance().setDataByte(datas);
			}else{
				//Toast.makeText(this, "抱歉，图片处理失败，请重新拍摄。。。", 5000).show();
				/*success_flag = -1;
				if(camera!=null){
					camera.stopPreview();
					camera.release();
					camera = null;
				}*/
				/*Intent i = new Intent(this,MyCameraActivity.class);
				startActivity(i);
				finish();*/
				/*layout2.setVisibility(View.GONE);
				if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
					zoomBitmap.recycle();
					zoomBitmap = null;
					System.gc();
				}
				if(bmp!=null){
					bmp.recycle();
					bmp = null;
					System.gc();
				}
				layout1.setVisibility(View.VISIBLE);
				notifyLayout1();
				//mZoomView.setImage(null);
				camera.startPreview();*/
				//如果生成预览图失败，则直接返回列表，图片的数据还在
				try{
				zoomBitmap = BitmapFactory.decodeByteArray(datas, 0, datas.length);
//				success_flag = 0;
				mZoomView.setImage(zoomBitmap);
//				MyApplication.getInstance().setDataByte(datas);
				}catch(OutOfMemoryError e){
//					success_flag = -1;
					if(mcamera!=null){
						mcamera.release();
						mcamera = null;
					}
					if(zoomBitmap!=null){
						zoomBitmap.recycle();
						zoomBitmap = null;
						
					}
					if(bmp!=null){
						bmp.recycle();
						bmp = null;
					}
					
					System.gc();
					Intent i = new Intent();
//					MyApplication.getInstance().setDataByte(datas);
					Sc_MyCameraActivity.this.setResult(RESULT_OK, i);
					Sc_MyCameraActivity.this.finish();
				}
				if(zoomBitmap!=null){
					mZoomView.setImage(zoomBitmap);
				}
				
			}
	}

	/**
	 * 程序数据初始化
	 */
	public void initData() {
		Sc_MyApplication.switch_tag = false;
		layout2.setVisibility(View.GONE);
		layout1.setVisibility(View.VISIBLE);
		notifyLayout1();
		if(!isEnoughMem()/* && !readSystem()*/){
			Toast.makeText(this, "存储空间已满，请插入或更换SD卡", 10000).show();
		}/*else if(!isEnoughMem() && readSystem()){
			MyApplication.getInstance().setURL_PIC(MyApplication.rootUrl);
			MyApplication.getInstance().setURL_PIC(MyApplication.rootUrl1);
		}*/
		// cameraview = (SurfaceView) this.findViewById(R.id.surface_camera);
		// surfaceHolder = cameraview.getHolder();
		// surfaceHolder.addCallback(this);
		// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	@Override
	public void onClick(View v) {
		if (v == btn) {
			if(!isEnoughMem()){
				Toast.makeText(this, "存储空间已满，请插入或更换SD卡", 10000).show();
				return;
			}else{
				//camera.autoFocus(mAutoFocusCallBack);
				if(isTake){
					btn.setClickable(false);
					isTouch = false;
					mcamera.takePicture(mShutterListener, null, new TakePictureCallback());
				}
				//mcamera.takePicture(null, null, new TakePictureCallback());
			}
		}else if(v == zoomOut){
			mZoomView.zoomImage(1.5f, 0, 0);
		}else if(v == zoomIn){
			mZoomView.zoomImage(0.8f, 0, 0);
		}
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		try {
			
//				parameters.setPictureFormat (PixelFormat.JPEG);
//			    parameters.setPictureSize(480, 800);
	//	    TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//		    Build.MODEL //手机型号
			if(mcamera!=null){
		    String str = Build.MODEL;
//		    Toast.makeText(MyCameraActivity.this,str, 5000).show();
		    Camera.Parameters parameters = mcamera.getParameters();
	    	parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
		    if(str.equals("HTC A9188") || str.equals("ZTE-T U960s") || str.equals("HTC G10")){
			    List<Size> sizes = parameters.getSupportedPreviewSizes();
			    Size optimalSize = getOptimalPreviewSize(sizes, width, height);
			    parameters.setPreviewSize(optimalSize.width, optimalSize.height);
		    }
		    	mcamera.setParameters(parameters);
				mcamera.setPreviewDisplay(holder);
		        mcamera.startPreview();
//		        if(mcamera != null){
//					try{
//						mcamera.cancelAutoFocus(); //release the previous auto-focus
//						mcamera.autoFocus(new Camera.AutoFocusCallback(){
//						@Override
//						public void onAutoFocus(boolean success, Camera camera) {
//							Log.d("HOME", "isAutofoucs " + Boolean.toString(success));					
//						}
//					} );
//					}catch(Exception e){
//						if(MyApplication.opLogger!=null){
//							MyApplication.opLogger.error("对焦失败",e);
//						}
//					}
//				}
			}else{

				if(mcamera!=null){
					mcamera.release();
					mcamera = null;
				}
				if(zoomBitmap!=null){
					zoomBitmap.recycle();
					zoomBitmap = null;
					
				}
				if(bmp!=null){
					bmp.recycle();
					bmp = null;
				}
				
				System.gc();
				/*if(menban!=null){
					menban.recycle();
				}*/
				Intent i = new Intent();
//				MyApplication.getInstance().setDataByte(datas);
				Sc_MyCameraActivity.this.setResult(RESULT_OK, i);
				Sc_MyCameraActivity.this.finish();
			
			}
		} catch (final Exception e) {
//			if(MyApplication.opLogger!=null){
//				MyApplication.opLogger.error("相机初始化失败。。",e);
//			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "拍照界面相机初始化失败："+e.getMessage());
				}
			}).start();
			Toast.makeText(getApplicationContext(), "相机初始化失败。。", 5000).show();
			if(mcamera!=null){
				mcamera.release();
				mcamera = null;
			}
			if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
				zoomBitmap.recycle();
				zoomBitmap = null;
			}
			if(bmp!=null){
				bmp.recycle();
				bmp = null;
			}
			System.gc();
			/*if(menban!=null){
				menban.recycle();
			}*/
			Intent i = new Intent();
//			MyApplication.getInstance().setDataByte(datas);
			Sc_MyCameraActivity.this.setResult(RESULT_OK, i);
			Sc_MyCameraActivity.this.finish();
		}
		
	}
	/**
	 * 判断手机存储卡是否内存足够
	 * @return
	 */
	 private boolean isEnoughMem() {
         File path = android.os.Environment.getExternalStorageDirectory();  // Get the path /data, this is internal storage path.
//		 File path = Environment.getRootDirectory();
		 StatFs stat = new StatFs(path.getPath());
         long blockSize = stat.getBlockSize();
         long availableBlocks = stat.getAvailableBlocks();
         long memSize = availableBlocks* blockSize;  // free size, unit is byte.
         if (memSize <1024*1024*5) { //If phone available memory is less than 5M , kill activity,it will avoid force when phone low memory.
                 return false;
         }
         return true;
 } 
	 /**
	  * 获取手机内部空间大小
	  */
	 private boolean readSystem() {
		 File root = Environment.getRootDirectory();
		 StatFs sf = new StatFs(root.getPath());
		 long blockSize = sf.getBlockSize();
		 long blockCount = sf.getBlockCount();
		 long availCount = sf.getAvailableBlocks();
//		 Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
//		 Log.d("", "可用的block数目：:"+ availCount+",可用大小:"+ availCount*blockSize/1024+"KB");
		 long a = availCount*blockSize/1024/1024;
		 if(a<10){
			 return false;
		 }
         return true;
		}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try{
//			if (mcamera == null) {
				mcamera = Camera.open();  
		/*	}else{
				mcamera.release();
				mcamera = Camera.open();  
			}*/
		}catch(final Exception e){
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "拍照界面出现异常："+e.getMessage());
				}
			}).start();
//			if(MyApplication.opLogger!=null){
//				MyApplication.opLogger.error("拍照界面异常",e);
//			}
			
			Toast.makeText(getApplicationContext(), "打开照相机失败。。", 5000).show();
			if(mcamera!=null){
				mcamera.release();
				mcamera = null;
			}
			if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
				zoomBitmap.recycle();
				zoomBitmap = null;      
			}
			if(bmp!=null){
				bmp.recycle();
				bmp = null;
			}
			System.gc();
			/*if(menban!=null){
				menban.recycle();
			}*/
			Intent i = new Intent();
//			MyApplication.getInstance().setDataByte(datas);
			Sc_MyCameraActivity.this.setResult(RESULT_OK, i);
			Sc_MyCameraActivity.this.finish();
		}  
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(mcamera!=null){
			mcamera.release();
			mcamera = null;
		}

		if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
			zoomBitmap.recycle();
			zoomBitmap = null;
		}
		if(bmp!=null){
			bmp.recycle();
			bmp = null;
		}
		if(surfaceHolder!=null){
			surfaceHolder.removeCallback(this);
			surfaceHolder = null;
			
		}
		System.gc();
		finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mcamera!=null){
			mcamera.release();
			mcamera = null;
		}

		if(zoomBitmap!=null && !zoomBitmap.isRecycled() ){
			zoomBitmap.recycle();
			zoomBitmap = null;
		}
		if(bmp!=null){
			bmp.recycle();
			bmp = null;
		}
		if(drawable!=null){
			drawable.clearColorFilter();
			drawable.setCallback(null);
			drawable = null;
		}
		if(menban!=null && !menban.isRecycled() ){
			menban.recycle();
			menban = null;
		}
		if(surfaceHolder!=null){
			surfaceHolder.removeCallback(this);
			surfaceHolder = null;
			
		}
		System.gc();
		if(mLocationClient!=null){
			mLocationClient.stop();
		}
		
		super.onDestroy();
	}
	private Camera.ShutterCallback mShutterListener = new Camera.ShutterCallback() {
		public void onShutter() {
			int a = 0;
			int b = a;
		}
	};

	/**
	 * 处理照片被拍摄之后的事件
	 */
	private final class TakePictureCallback implements PictureCallback {

		public void onPictureTaken(byte[] data, Camera camera) {
			layout1.setVisibility(View.GONE);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
			layout2.setVisibility(View.VISIBLE);
			datas = null;
			datas = data;
//			MyApplication.getInstance().setDataByte(data);
			if(datas!=null){
				notifyLayout2();
//				if(success_flag == 0){
				if(mcamera!=null){
					mcamera.stopPreview();
				}
					
//				}
			}else{
				if(mcamera!=null){
					mcamera.stopPreview();
					mcamera.release();
					mcamera = null;
				}
				finish();
			}
			System.gc();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Sc_MyApplication.switch_tag = true;
			if (layout1.getVisibility() == View.VISIBLE) {
				mcamera.release();
				mcamera = null;
				if(zoomBitmap!=null){
					zoomBitmap.recycle();
					zoomBitmap = null;
				}
				if(bmp!=null){
					bmp.recycle();
					bmp = null;
				}
				/*if(menban!=null){
					menban.recycle();
				}*/
				if(surfaceHolder!=null){
					surfaceHolder.removeCallback(this);
					surfaceHolder = null;
					
				}
				System.gc();
				Intent i = new Intent();
				Sc_MyCameraActivity.this.setResult(RESULT_CANCELED, i);
				Sc_MyCameraActivity.this.finish();
				
			} else {
				layout2.setVisibility(View.GONE);
				layout1.setVisibility(View.VISIBLE);
				notifyLayout1();
				mcamera.startPreview();
			}

		}
		return true;
	}

	private Bitmap getDrawable1(byte[] data, int zoom) {
		try {
			sc_PicHandler ph = new sc_PicHandler();
			BitmapFactory.Options opts =  new  BitmapFactory.Options();
	        opts.inJustDecodeBounds =  true ;
	        if(data!=null){
	        	BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	        }else{
	        	return null;
	        }
	         opts.inSampleSize = ph.computeSampleSize(opts, -1 , 600 * 600 ); 
	         opts.inJustDecodeBounds =  false ;
	        	 if(bmp != null){
	        		 bmp.recycle();
	        		 bmp = null;
	        		 System.gc();
	        	 }
//	              bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	        	 try{
	        	  bmp = sc_BitmapCache.getInstance().getBitmap(data, Sc_MyCameraActivity.this,opts);
//	        	  bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	        	  if(bmp!=null)
	        	  bmp = ph.scaleImg(bmp,bmp.getWidth(),bmp.getHeight());
	        	 }catch(OutOfMemoryError e){
	        		 if(bmp != null){
		        		 bmp.recycle();
		        		 bmp = null;
		        		 System.gc();
		        	 }
	        		 return null;
	        	 }
	        	 
		} catch (final OutOfMemoryError e) {
			if(bmp != null){
       		 bmp.recycle();
       		 bmp = null;
       		 System.gc();
       	 }
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "拍照界面照片处理内存溢出："+e.getMessage());
				}
			}).start();
//			if(MyApplication.opLogger!=null){
//				MyApplication.opLogger.error("拍照界面异常",e);
//			}
			return null;
			
		}
		
			return bmp;
	}
	/*
	 * 得到门板图片
	 */
	private Bitmap getDrawable(int id) {
		BitmapFactory.Options opts =  new  BitmapFactory.Options();
		try {
			InputStream is = getResources().openRawResource(id);  
			sc_PicHandler ph = new sc_PicHandler();
	        opts.inJustDecodeBounds =  true ;
	        opts.inPreferredConfig = Bitmap.Config.RGB_565;   
	        BitmapFactory.decodeStream(is,null,opts);
	         opts.inSampleSize = ph.computeSampleSize(opts, -1 , 640 * 480 ); 
	         opts.inJustDecodeBounds =  false ;
	        	 if(bmp != null){
	        		 bmp.recycle();
	        		 bmp = null;
	        		 System.gc();
	        	 }
	              bmp = BitmapFactory.decodeStream(is,null,opts);
//	        	  bmp = BitmapCache.getInstance().getBitmap(id, MyCameraActivity.this,opts);
	              bmp = ph.scaleImg(bmp,640,480);
	              is.close();
		} catch (OutOfMemoryError e) {
			bmp = sc_BitmapCache.getInstance().getBitmap(id, Sc_MyCameraActivity.this,opts);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			return bmp;
	}
	/*
	 * 取得最适合的照相机预览尺寸大小
	 */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	      final double ASPECT_TOLERANCE = 0.05;
	      double targetRatio = (double) w / h;
	      if (sizes == null) return null;

	      Size optimalSize = null;
	      double minDiff = Double.MAX_VALUE;

	      int targetHeight = h;

	      // Try to find an size match aspect ratio and size
	      for (Size size : sizes) {
	          double ratio = (double) size.width / size.height;
	          if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
	          if (Math.abs(size.height - targetHeight) < minDiff) {
	              optimalSize = size;
	              minDiff = Math.abs(size.height - targetHeight);
	          }
	      }

	      // Cannot find the one match the aspect ratio, ignore the requirement
	      if (optimalSize == null) {
	          minDiff = Double.MAX_VALUE;
	          for (Size size : sizes) {
	              if (Math.abs(size.height - targetHeight) < minDiff) {
	                  optimalSize = size;
	                  minDiff = Math.abs(size.height - targetHeight);
	              }
	          }
	      }
	      return optimalSize;
	  }
}
