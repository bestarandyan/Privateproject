/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.adapter.HuoDongListAdapter;
import com.qingfengweb.weddingideas.adapter.TaoXiListAdapter;
import com.qingfengweb.weddingideas.adapter.YangZhaoListAdapter;
import com.qingfengweb.weddingideas.beans.HuoDongBean;
import com.qingfengweb.weddingideas.beans.TaoXiBean;
import com.qingfengweb.weddingideas.beans.YangZhaoBean;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.DBHelper;
import com.qingfengweb.weddingideas.data.JsonData;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.data.RequestServerFromHttp;
import com.qingfengweb.weddingideas.filedownload.FileUtils;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;

/**
 * @author 刘星星
 * 掌上商铺页面
 *
 */
@SuppressLint("HandlerLeak") @TargetApi(Build.VERSION_CODES.HONEYCOMB) public class WeddingStoreActivity extends BaseActivity implements OnItemClickListener{
	RelativeLayout parentLayout;
//	ListView listView;
	LinearLayout yzListView,txListView,hdListView;
	LinearLayout rightLayout1,rightLayout2,rightLayout3,rightLayout4;
	ImageView img1,img2,img3,img4;
	TextView tv1,tv2,tv3,tv4;
	List<Map<String,Object>> yzList = null;//样照数据
	List<Map<String,Object>> txList = null;//套系数据
	List<Map<String,Object>> hdList = null;//活动数据
	public int currentListType = 0;//0代表为样照列表  1代表为套系列表   2代表活动列表
	Bitmap bitmap = null;
	String storeid = "";
	String madeid = "";
	DBHelper dbHelper = null;
	YangZhaoListAdapter adapteryz =null;//样照适配器
	TaoXiListAdapter adaptertx  =null;//套系适配器
	HuoDongListAdapter adapterhd  = null;//活动适配器
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_weddingstores);
		initView();
		initData();
	}
	private void initView(){
		parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
		parentLayout.setBackgroundColor(Color.parseColor(LoadingActivity.templateList.get(0).get("store_bgcolor")));
//		parentLayout.setOnTouchListener(this);
//		listView = (ListView) findViewById(R.id.listView);
		yzListView = (LinearLayout) findViewById(R.id.yzListView);
		txListView = (LinearLayout) findViewById(R.id.txListView);
		hdListView = (LinearLayout) findViewById(R.id.hdListView);
//		listView.setOnTouchListener(this);
//		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
//		listView.setOnItemClickListener(this);
//		listView.setDivider(new ColorDrawable(Color.parseColor(LoadingActivity.templateList.get(0).get("store_bgcolor"))));
//		listView.setDividerHeight(10);
		findViewById(R.id.backBtn).setOnClickListener(this);
		rightLayout1 = (LinearLayout)findViewById(R.id.rightLayout1);
		rightLayout2 = (LinearLayout)findViewById(R.id.rightLayout2);
		rightLayout3 = (LinearLayout)findViewById(R.id.rightLayout3);
		rightLayout4 = (LinearLayout)findViewById(R.id.rightLayout4);
		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);
		img4 = (ImageView) findViewById(R.id.img4);
		tv1 = (TextView) findViewById(R.id.text1);
		tv2 = (TextView) findViewById(R.id.text2);
		tv3 = (TextView) findViewById(R.id.text3);
		tv4 = (TextView) findViewById(R.id.text4);
		rightLayout1.setOnClickListener(this);
		rightLayout2.setOnClickListener(this);
		rightLayout3.setOnClickListener(this);
		rightLayout4.setOnClickListener(this);
		TextView titleTv = (TextView) findViewById(R.id.topView);
		titleTv.setText(LoadingActivity.configList.get(0).get("store_name"));
	}
	public void initData(){
		dbHelper = DBHelper.getInstance(this);
		storeid = LoadingActivity.configList.get(0).get("storeid");
		madeid = LoadingActivity.configList.get(0).get("madeid");
		yzList = new ArrayList<Map<String,Object>>();
		txList = new ArrayList<Map<String,Object>>();
		hdList = new ArrayList<Map<String,Object>>();
		new Thread(getYZRunnable).start();//获取样照数据
	}
	
	/**
	 * 刷新样照布局
	 */
	public void notifyYZAdapter(){
		adapteryz  = new YangZhaoListAdapter(this, yzList);
//		listView.setAdapter(adapteryz);
	}
	
	/**
	 * 刷新套系布局
	 */
	public void notifyTXAdapter(){
		adaptertx  = new TaoXiListAdapter(this, txList);
//		listView.setAdapter(adaptertx);
	}
	
	
	/**
	 * 刷新活动布局
	 */
	public void notifyHDAdapter(){
		adapterhd  = new HuoDongListAdapter(this, hdList);
//		listView.setAdapter(adapterhd);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.rightLayout1){//样照
			if(currentListType == 0){
				return;
			}
			currentListType = 0;
			rightLayout1.setBackgroundResource(R.drawable.btn_shop_menu_selected);
			rightLayout2.setBackgroundResource(R.drawable.shangpu_menu_bg);
			rightLayout3.setBackgroundResource(R.drawable.shangpu_menu_bg);
			rightLayout4.setBackgroundResource(R.drawable.shangpu_menu_bg);
			tv1.setTextColor(Color.WHITE);
			tv2.setTextColor(Color.parseColor("#B3B3B3"));
			tv3.setTextColor(Color.parseColor("#B3B3B3"));
			tv4.setTextColor(Color.parseColor("#B3B3B3"));
			img1.setImageResource(R.drawable.shangpu_menu_ico1);
			img2.setImageResource(R.drawable.shangpu_menu_ico02);
			img3.setImageResource(R.drawable.shangpu_menu_ico03);
			img4.setImageResource(R.drawable.shangpu_menu_ico04);
//			if(txList!=null && txList.size()>0){
//				for(int i=0;i<txList.size();i++){
//					if((Bitmap)txList.get(i).get("bitmap")!=null && !((Bitmap)txList.get(i).get("bitmap")).isRecycled()){
//						((Bitmap)txList.get(i).get("bitmap")).recycle();
//					}
//				}
//				txList.clear();
//			}
//			if(hdList!=null && hdList.size()>0){
//				for(int i=0;i<hdList.size();i++){
//					if((Bitmap)hdList.get(i).get("bitmap")!=null && !((Bitmap)hdList.get(i).get("bitmap")).isRecycled()){
//						((Bitmap)hdList.get(i).get("bitmap")).recycle();
//					}
//				}
//				hdList.clear();
//			}
//			if(adaptertx!=null && adaptertx.bitmap!=null && !adaptertx.bitmap.isRecycled()){
//				adaptertx.bitmap.recycle();
//			}
//			if(adapterhd!=null && adapterhd.bitmap!=null && !adapterhd.bitmap.isRecycled()){
//				adapterhd.bitmap.recycle();
//			}
			if(yzList==null || yzList.size() == 0){
				new Thread(getYZRunnable).start();//获取样照数据
			}
				yzListView.setVisibility(View.VISIBLE);
				txListView.setVisibility(View.GONE);
				hdListView.setVisibility(View.GONE);
			
		}else if(v.getId() == R.id.rightLayout2){//套系
			if(currentListType == 1){
				return;
			}
			currentListType = 1;
			rightLayout2.setBackgroundResource(R.drawable.btn_shop_menu_selected);
			rightLayout1.setBackgroundResource(R.drawable.shangpu_menu_bg);
			rightLayout3.setBackgroundResource(R.drawable.shangpu_menu_bg);
			rightLayout4.setBackgroundResource(R.drawable.shangpu_menu_bg);
			tv2.setTextColor(Color.WHITE);
			tv1.setTextColor(Color.parseColor("#B3B3B3"));
			tv3.setTextColor(Color.parseColor("#B3B3B3"));
			tv4.setTextColor(Color.parseColor("#B3B3B3"));
			img1.setImageResource(R.drawable.shangpu_menu_ico01);
			img2.setImageResource(R.drawable.shangpu_menu_ico2);
			img3.setImageResource(R.drawable.shangpu_menu_ico03);
			img4.setImageResource(R.drawable.shangpu_menu_ico04);
//			if(yzList!=null && yzList.size()>0){
//				for(int i=0;i<yzList.size();i++){
//					if((Bitmap)yzList.get(i).get("bitmap")!=null && !((Bitmap)yzList.get(i).get("bitmap")).isRecycled()){
//						((Bitmap)yzList.get(i).get("bitmap")).recycle();
//					}
//				}
//				yzList.clear();
//			}
//			if(hdList!=null && hdList.size()>0){
//				for(int i=0;i<hdList.size();i++){
//					if((Bitmap)hdList.get(i).get("bitmap")!=null && !((Bitmap)hdList.get(i).get("bitmap")).isRecycled()){
//						((Bitmap)hdList.get(i).get("bitmap")).recycle();
//					}
//				}
//				hdList.clear();
//			}
//			if(adapteryz!=null && adapteryz.bitmap!=null && !adapteryz.bitmap.isRecycled()){
//				adapteryz.bitmap.recycle();
//			}
//			if(adapterhd!=null && adapterhd.bitmap!=null && !adapterhd.bitmap.isRecycled()){
//				adapterhd.bitmap.recycle();
//			}
			if(txList==null || txList.size()==0){
				new Thread(getTXRunnable).start();
			}
				yzListView.setVisibility(View.GONE);
				txListView.setVisibility(View.VISIBLE);
				hdListView.setVisibility(View.GONE);
		}else if(v.getId() == R.id.rightLayout3){//活动
			if(currentListType == 2){
				return;
			}
			currentListType = 2;
//			if(hdList==null || hdList.size()==0){
//					Map<String,Object> map = new HashMap<String, Object>();
//					map.put("imageid", R.drawable.activity_thumb);
//					bitmap = PicHandler.getDrawable(getResources(), R.drawable.activity_thumb, (int) (MyApplication.getInstance().getScreenW()*0.8), MyApplication.getInstance().getScreenH());
//					map.put("bitmap", bitmap);
//					hdList.add(map);
//			}
//			if(yzList!=null && yzList.size()>0){
//				for(int i=0;i<yzList.size();i++){
//					if((Bitmap)yzList.get(i).get("bitmap")!=null && !((Bitmap)yzList.get(i).get("bitmap")).isRecycled()){
//						((Bitmap)yzList.get(i).get("bitmap")).recycle();
//					}
//				}
//				yzList.clear();
//			}
//			if(txList!=null && txList.size()>0){
//				for(int i=0;i<txList.size();i++){
//					if((Bitmap)txList.get(i).get("bitmap")!=null && !((Bitmap)txList.get(i).get("bitmap")).isRecycled()){
//						((Bitmap)txList.get(i).get("bitmap")).recycle();
//					}
//				}
//				txList.clear();
//			}
//			if(adapteryz!=null && adapteryz.bitmap!=null && !adapteryz.bitmap.isRecycled()){
//				adapteryz.bitmap.recycle();
//			}
//			if(adaptertx!=null && adaptertx.bitmap!=null && !adaptertx.bitmap.isRecycled()){
//				adaptertx.bitmap.recycle();
//			}
			if(hdList==null || hdList.size()==0){
				new Thread(getHDRunnable).start();
			}
			yzListView.setVisibility(View.GONE);
			txListView.setVisibility(View.GONE);
			hdListView.setVisibility(View.VISIBLE);
			rightLayout3.setBackgroundResource(R.drawable.btn_shop_menu_selected);
			rightLayout2.setBackgroundResource(R.drawable.shangpu_menu_bg);
			rightLayout1.setBackgroundResource(R.drawable.shangpu_menu_bg);
			rightLayout4.setBackgroundResource(R.drawable.shangpu_menu_bg);
			tv3.setTextColor(Color.WHITE);
			tv2.setTextColor(Color.parseColor("#B3B3B3"));
			tv1.setTextColor(Color.parseColor("#B3B3B3"));
			tv4.setTextColor(Color.parseColor("#B3B3B3"));
			img1.setImageResource(R.drawable.shangpu_menu_ico01);
			img2.setImageResource(R.drawable.shangpu_menu_ico02);
			img3.setImageResource(R.drawable.shangpu_menu_ico3);
			img4.setImageResource(R.drawable.shangpu_menu_ico04);
		}else if(v.getId() == R.id.rightLayout4){//咨询
			showCallDialog(LoadingActivity.photoOne);
		}else if(v.getId() == R.id.backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		super.onClick(v);
	}
	/**
	 * 获取样照数据
	 */
	Runnable getYZRunnable = new Runnable() {
		@Override
		public void run() {
			handlerYZData();
			String  msgStr = RequestServerFromHttp.getyangzhaoData(storeid, "");
			if(msgStr.startsWith("[")){
				JsonData.jsonYZData(msgStr, dbHelper.open(),storeid);
				handlerYZData();
			}
		}
	};
	private void handlerYZData(){
		String sql = "select * from "+YangZhaoBean.tbName +" where storeid='"+storeid+"'";
		yzList = dbHelper.selectRow(sql, null);
		if(yzList!=null && yzList.size()>0){
			handler.sendEmptyMessage(0);
		}
	}
	/**
	 * 套系数据
	 */
	Runnable getTXRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+TaoXiBean.tbName+" where storeid='"+storeid+"'";
			txList = dbHelper.selectRow(sql, null);
			if(txList!=null && txList.size()>0){
				handler.sendEmptyMessage(1);
			}
			String  msgStr = RequestServerFromHttp.gettaoxiData(storeid,"");
			if(msgStr.startsWith("[")){
				JsonData.jsonTaoXiData(msgStr, dbHelper.open(),storeid);
				txList = dbHelper.selectRow(sql, null);
				if(txList!=null && txList.size()>0){
					handler.sendEmptyMessage(1);
				}
			}
		}
	};
	/**
	 * 获取活动数据
	 */
	Runnable getHDRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sql = "select *from "+HuoDongBean.tbName +" where storeid='"+storeid+"'";
			hdList = dbHelper.selectRow(sql, null);
			if(hdList!=null && hdList.size()>0){
				handler.sendEmptyMessage(2);
			}
			String  msgStr = RequestServerFromHttp.getactiveData(storeid, "");
			if(msgStr.startsWith("[")){
				JsonData.jsonHuoDongData(msgStr, dbHelper.open(),storeid);
				hdList = dbHelper.selectRow(sql, null);
				if(hdList!=null && hdList.size()>0){
					handler.sendEmptyMessage(2);
				}
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//刷新样照布局
//				notifyYZAdapter();
				yzListView.removeAllViews();
				for(int i=0;i<yzList.size();i++){
					yzListView.addView(addYzView(i));
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for(int i=0;i<yzList.size();i++){
							String imgUrl = "";
							Map<String,Object> map = yzList.get(i);
							if(map.get("c_photo")!=null){
								imgUrl = map.get("c_photo").toString();
								downLoadBitmap(imgUrl,ConstantValues.yangzhaoImgUrl,i);
							}
						}
			}
			}).start();
			}else if(msg.what == 1){//刷新套系布局
//				notifyTXAdapter();
				txListView.removeAllViews();
				for(int i=0;i<txList.size();i++){
					txListView.addView(addTxView(i));
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for(int i=0;i<txList.size();i++){
							String imgUrl = "";
							Map<String,Object> map = txList.get(i);
							if(map.get("photo_c")!=null){
								imgUrl = map.get("photo_c").toString();
								downLoadBitmap(imgUrl,ConstantValues.taoxiImgUrl,i);
							}
						}
			}
			}).start();
			}else if(msg.what == 2){//刷新活动数据
//				notifyHDAdapter();
				hdListView.removeAllViews();
				for(int i=0;i<hdList.size();i++){
					hdListView.addView(addHdView(i));
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for(int i=0;i<hdList.size();i++){
							String imgUrl = "";
							Map<String,Object> map = hdList.get(i);
							if(map.get("pic_active")!=null){
								imgUrl = map.get("pic_active").toString();
								downLoadBitmap(imgUrl,ConstantValues.huodongImgUrl,i);
							}
						}
			}
			}).start();
			}else if(msg.what == 3){//设置样照列表图片
				try{
				if(currentListType == 0){
					((ImageView)(yzListView.getChildAt(msg.arg1).findViewById(R.id.imageView))).setImageBitmap((Bitmap) msg.obj);
				}else if(currentListType == 1){
					((ImageView)(txListView.getChildAt(msg.arg1).findViewById(R.id.imageView))).setImageBitmap((Bitmap) msg.obj);
				}else if(currentListType == 2){
					((ImageView)(hdListView.getChildAt(msg.arg1).findViewById(R.id.imageView))).setImageBitmap((Bitmap) msg.obj);
				}
				}catch(NullPointerException p){
					System.out.println("内存溢出了");
				}
			}else if(msg.what == 4){//内存溢出
//				if(currentListType == 0){
//					((ImageView)(yzListView.getChildAt(msg.arg1).findViewById(R.id.imageView))).setImageResource(R.drawable.img_default);
//				}else if(currentListType == 1){
//					((ImageView)(txListView.getChildAt(msg.arg1).findViewById(R.id.imageView))).setImageResource(R.drawable.img_default);
//				}else if(currentListType == 2){
//					((ImageView)(hdListView.getChildAt(msg.arg1).findViewById(R.id.imageView))).setImageResource(R.drawable.img_default);
//				}
			}
			super.handleMessage(msg);
		}
		
	};
	private void downLoadBitmap(String imgUrl,String sdRoot,int index){
		Bitmap bitmap = null;
		if(imgUrl!=null && imgUrl.length()>0){
    		imgUrl = "http://img.weddingideas.cn"+imgUrl;
    		String[] fileStrings = imgUrl.trim().split("/");
    		String  nameString = fileStrings[fileStrings.length-1];
    		 File file = new File(ConstantValues.sdcardUrl+sdRoot+nameString);
    		 if(file.exists()){
    			 try {
    					BitmapFactory.Options opts = new BitmapFactory.Options();
    					opts.inJustDecodeBounds = true;
    					opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
    					opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,700 * 700);
    					opts.inJustDecodeBounds = false;
    					// 如果图片还未回收，先强制回收该图片
    					bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
    					if (bitmap != null) {
    						int width = bitmap.getWidth();
    						int height = bitmap.getHeight();
    						int newWidth = MyApplication.getInstance().getScreenW()-50;
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
    					System.out.println("在本地文件存在的情况下内存溢出了--------------------------------------------");
    					e.printStackTrace();
    				} 
    			 if(bitmap!=null){
    				 Message msg = new Message();
    				 msg.what =3;
				     msg.arg1 = index;
				     msg.obj = bitmap;
				     handler.sendMessage(msg);
    				 System.gc();
    			 }else{
    				 loadImageFromUrl(imgUrl, sdRoot,index);
    			 }
    		 }else{
    			 loadImageFromUrl(imgUrl, sdRoot,index);
    		 }
			}
	}
	/**
	 * 通过网络地址下载图片
	 * @param imageUrl
	 * @param toSD
	 * @param context
	 * @param defId
	 * @return
	 */
	private  void loadImageFromUrl(final String imageUrl,final String toSD,final int position){
		Bitmap bitmap = null;
				URL myFileUrl = null;
				String nameString = "";
				try {
					myFileUrl = new URL(imageUrl);
					System.out.println(myFileUrl.getFile());
					nameString = myFileUrl.getFile().substring(1);
					String[] fileStrings = imageUrl.trim().split("/");
					nameString = fileStrings[fileStrings.length-1];
					HttpURLConnection connection = (HttpURLConnection) myFileUrl.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream isInputStream = connection.getInputStream();
					int length = connection.getContentLength();
					if (length!=-1) {
						byte[] imgData = new byte[length];
						byte[] temp = new byte[512];
						int readLen = 0;
						int destPos = 0;
						while ((readLen = isInputStream.read(temp))>0) {
							System.arraycopy(temp, 0, imgData, destPos, readLen);
							destPos += readLen;
						}
						BitmapFactory.Options opts = new BitmapFactory.Options();
						opts.inJustDecodeBounds = true;
						opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//						BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
						opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,700 * 700);
						opts.inJustDecodeBounds = false;
						bitmap =BitmapFactory.decodeByteArray(imgData, 0, imgData.length, opts);
						if (bitmap != null) {
							int width = bitmap.getWidth();
							int height = bitmap.getHeight();
							int newWidth = MyApplication.getInstance().getScreenW()-50;
							// 计算缩放比例
							float scaleWidth = ((float) newWidth) / width;
							// 取得想要缩放的matrix参数
							Matrix matrix = new Matrix();
							matrix.postScale(scaleWidth, scaleWidth);
							// 得到新的图片
							bitmap= Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, true);
						}
						File file = new File(FileUtils.SDPATH+toSD+nameString);
						if(bitmap!=null){
							boolean b = OutPutImage(file,bitmap);
							Message msg = new Message();
							msg.what =3;
							msg.arg1 = position;
							msg.obj = bitmap;
							handler.sendMessage(msg);
						}
		//				System.out.println("图片存储========================"+(b?"成功":"失败"));
					}
				} catch (IOException e) {
					
				}catch(OutOfMemoryError e){
					System.out.println("在下载图片的时候内存溢出了+++++++++++++++++++++++++++++++++++++++++++++");
					Message msg = new Message();
					msg.what =4;
					msg.arg1 = position;
					handler.sendMessage(msg);
				}
	}
	/**
	 * 将图片保存到本地ＳＤ�?
	 * 
	 * @param file
	 *            �?���?
	 * @return
	 * @author 刘星�?
	 */
	public static boolean OutPutImage(File file, Bitmap bitmap) {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(file.getAbsolutePath());
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			if(bos!=null){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				fos.flush();
				return true;
			}else{
				return false;
			}
		} catch (IOException e) {
			return false;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	ImageView iv = null;
	/**
	 * 添加样照布局
	 * @param index
	 * @return
	 */
	private View addYzView(final int index){
		View v = LayoutInflater.from(this).inflate(R.layout.item_yangzhao, null);
		iv = (ImageView) v.findViewById(R.id.imageView);
		iv.setImageResource(R.drawable.img_default);
		iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeddingStoreActivity.this,DetailYangZhaoActivity.class);
				Map<String,Object> photoMap = new HashMap<String, Object>();
				photoMap.put("s_name", yzList.get(index).get("s_name"));
				photoMap.put("p_desc", yzList.get(index).get("p_desc"));
				photoMap.put("photo_one", yzList.get(index).get("photo_one"));
				photoMap.put("photo_two", yzList.get(index).get("photo_two"));
				photoMap.put("photo_thr", yzList.get(index).get("photo_thr"));
				photoMap.put("photo_for", yzList.get(index).get("photo_for"));
				photoMap.put("photo_fiv", yzList.get(index).get("photo_fiv"));
				intent.putExtra("yzMap", (Serializable)photoMap);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}
		});
		return v;
	}
	/**
	 * 添加套系布局
	 * @param index
	 * @return
	 */
	private View addTxView(final int index){
		View v = LayoutInflater.from(this).inflate(R.layout.item_taoxi, null);
		iv = (ImageView) v.findViewById(R.id.imageView);
		TextView nametv = (TextView) v.findViewById(R.id.name);
		TextView pricetv = (TextView) v.findViewById(R.id.price);
		String name = "";
		String price_cut = "";
		if(txList.get(index).get("s_name")!=null && !txList.get(index).get("s_name").equals("null")){
			name = txList.get(index).get("s_name").toString();
		}
		if(txList.get(index).get("price_cut")!=null && !txList.get(index).get("price_cut").equals("null")){
			price_cut = txList.get(index).get("price_cut").toString();
			if(price_cut!=null && price_cut.length()>0){
				price_cut = "￥"+price_cut+"元";
			}
		}
		nametv.setText(name);
		pricetv.setText(price_cut);
		iv.setImageResource(R.drawable.img_default);
		iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeddingStoreActivity.this,DetailTaoXiActivity.class);
//				intent.putExtra("map", (Serializable) txList.get(arg2));
//				intent.putExtra("position", arg2);
				intent.putExtra("id", txList.get(index).get("id").toString());
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}
		});
		return v;
	}
	/**
	 * 添加活动布局
	 * @param index
	 * @return
	 */
	private View addHdView(final int index){
		View v = LayoutInflater.from(this).inflate(R.layout.item_yangzhao, null);
		iv = (ImageView) v.findViewById(R.id.imageView);
		iv.setImageResource(R.drawable.img_default);
		iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WeddingStoreActivity.this,DetailHuoDongActivity.class);
				intent.putExtra("pic_active", hdList.get(index).get("pic_active").toString());
				startActivity(intent);
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}
		});
		return v;
	}
	@Override
	protected void onDestroy() {
		if(bitmap!=null){
			bitmap.recycle();
			bitmap = null;
		}
		if(yzList!=null && yzList.size()>0){
			for(int i=0;i<yzList.size();i++){
				if((Bitmap)yzList.get(i).get("bitmap")!=null && !((Bitmap)yzList.get(i).get("bitmap")).isRecycled()){
					((Bitmap)yzList.get(i).get("bitmap")).recycle();
				}
			}
			yzList.clear();
		}
		if(txList!=null && txList.size()>0){
			for(int i=0;i<txList.size();i++){
				if((Bitmap)txList.get(i).get("bitmap")!=null && !((Bitmap)txList.get(i).get("bitmap")).isRecycled()){
					((Bitmap)txList.get(i).get("bitmap")).recycle();
				}
			}
			txList.clear();
		}
		if(hdList!=null && hdList.size()>0){
			for(int i=0;i<hdList.size();i++){
				if((Bitmap)hdList.get(i).get("bitmap")!=null && !((Bitmap)hdList.get(i).get("bitmap")).isRecycled()){
					((Bitmap)hdList.get(i).get("bitmap")).recycle();
				}
			}
			hdList.clear();
		}
		
		for(int i=0;i<yzListView.getChildCount();i++){
			if (yzListView.getChildAt(i) != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView)yzListView.getChildAt(i).findViewById(R.id.imageView)).getDrawable();
				if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
		}
		yzListView.removeAllViews();
		for(int i=0;i<txListView.getChildCount();i++){
			if (txListView.getChildAt(i) != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView)txListView.getChildAt(i).findViewById(R.id.imageView)).getDrawable();
				if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
		}
		txListView.removeAllViews();
		for(int i=0;i<hdListView.getChildCount();i++){
			if (hdListView.getChildAt(i) != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView)hdListView.getChildAt(i).findViewById(R.id.imageView)).getDrawable();
				if (bitmapDrawable != null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
		}
		hdListView.removeAllViews();
		System.gc();
		super.onDestroy();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return false;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		if(currentListType == 0){//样照
			Intent intent = new Intent(this,DetailYangZhaoActivity.class);
			Map<String,Object> photoMap = new HashMap<String, Object>();
			photoMap.put("s_name", yzList.get(arg2).get("s_name"));
			photoMap.put("p_desc", yzList.get(arg2).get("p_desc"));
			photoMap.put("photo_one", yzList.get(arg2).get("photo_one"));
			photoMap.put("photo_two", yzList.get(arg2).get("photo_two"));
			photoMap.put("photo_thr", yzList.get(arg2).get("photo_thr"));
			photoMap.put("photo_for", yzList.get(arg2).get("photo_for"));
			photoMap.put("photo_fiv", yzList.get(arg2).get("photo_fiv"));
			intent.putExtra("yzMap", (Serializable)photoMap);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(currentListType == 1){//套系
			Intent intent = new Intent(this,DetailTaoXiActivity.class);
//			intent.putExtra("map", (Serializable) txList.get(arg2));
//			intent.putExtra("position", arg2);
			intent.putExtra("id", txList.get(arg2).get("id").toString());
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(currentListType == 2){//活动
			Intent intent = new Intent(this,DetailHuoDongActivity.class);
			intent.putExtra("pic_active", hdList.get(arg2).get("pic_active").toString());
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
		
	}
}
