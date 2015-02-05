/**
 * 
 */
package com.qingfengweb.weddingideas.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengweb.weddingideas.R;
import com.qingfengweb.weddingideas.data.ConstantValues;
import com.qingfengweb.weddingideas.data.MyApplication;
import com.qingfengweb.weddingideas.filedownload.FileUtils;
import com.qingfengweb.weddingideas.imagehandle.PicHandler;

/**
 * @author 刘星星
 *
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.ECLAIR) public class DetailYangZhaoActivity extends BaseActivity implements View.OnTouchListener{
	LinearLayout bottomLayout;
	ViewPager viewPager;
	public ArrayList<View> pageViews;
	public TextView titleTv,contentTv;
	Bitmap bitmap =null;
	Map<String,Object> photoMap = null;
	ArrayList<String> imgUrlArray = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailyangzhao);
		initView();
	}
	private void initView(){
		findViewById(R.id.backBtn).setOnClickListener(this);
		bottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
		bottomLayout.getBackground().setAlpha(200);
		findViewById(R.id.tellBtn).setOnClickListener(this);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		titleTv = (TextView) findViewById(R.id.titleTv);
		contentTv = (TextView) findViewById(R.id.contentTv);
		pageViews = new ArrayList<View>();  
		getData();
		notifyPager();
		if(photoMap.get("s_name")!=null && !photoMap.get("s_name").equals("null")){
			String name = photoMap.get("s_name").toString();
			titleTv.setText(name);
		}else{
			titleTv.setText("");
		}
		if(photoMap.get("p_desc")!=null && !photoMap.get("p_desc").equals("null")){
			String description = photoMap.get("p_desc").toString();
			contentTv.setText(description);
		}else{
			contentTv.setText("");
		}
	}
	private void notifyPager(){
		viewPager.setAdapter(new pagerAdapter());  
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());  
	}
	@SuppressWarnings("unchecked")
	private void getData(){
		photoMap = (Map<String, Object>) getIntent().getSerializableExtra("yzMap");
		String photoUrl = "";
		if(photoMap.get("photo_one")!=null && !photoMap.get("photo_one").equals("null")){
			photoUrl = photoMap.get("photo_one").toString();
			pageViews.add(getView());
			imgUrlArray.add(photoUrl);
		}
		if(photoMap.get("photo_two")!=null && !photoMap.get("photo_two").equals("null")){
			photoUrl = photoMap.get("photo_two").toString();
			pageViews.add(getView());
			imgUrlArray.add(photoUrl);
		}
		if(photoMap.get("photo_thr")!=null && !photoMap.get("photo_thr").equals("null")){
			photoUrl = photoMap.get("photo_thr").toString();
			pageViews.add(getView());
			imgUrlArray.add(photoUrl);
		}
		if(photoMap.get("photo_for")!=null && !photoMap.get("photo_for").equals("null")){
			photoUrl = photoMap.get("photo_for").toString();
			pageViews.add(getView());
			imgUrlArray.add(photoUrl);
		}
		if(photoMap.get("photo_fiv")!=null && !photoMap.get("photo_fiv").equals("null")){
			photoUrl = photoMap.get("photo_fiv").toString();
			pageViews.add(getView());
			imgUrlArray.add(photoUrl);
		}
	new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<pageViews.size();i++){
					String imgUrl = "";
		        	imgUrl= imgUrlArray.get(i);
		    		imgUrl = "http://img.weddingideas.cn"+imgUrl;
		    		String[] fileStrings = imgUrl.trim().split("/");
		    		String  nameString = fileStrings[fileStrings.length-1];
		    		 File file = new File(ConstantValues.sdcardUrl+ConstantValues.yangzhaoImgUrl+nameString);
		    		 if(file.exists()){
		    			 try {
		    					BitmapFactory.Options opts = new BitmapFactory.Options();
		    					opts.inJustDecodeBounds = true;
		    					opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//		    					BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		    					opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,700 * 700);
		    					opts.inJustDecodeBounds = false;
		    					// 如果图片还未回收，先强制回收该图片
		    					bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
		    					if (bitmap != null) {
		    						int width = bitmap.getWidth();
		    						int height = bitmap.getHeight();
		    						int newWidth = MyApplication.getInstance().getScreenW()*2/3;
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
		    				 msg.what =0;
						     msg.arg1 = i;
						     handler.sendMessage(msg);
		    				 System.gc();
		    			 }else{
		    				 loadImageFromUrl(imgUrl, ConstantValues.yangzhaoImgUrl,i);
		    			 }
		    		 }else{
		    			 loadImageFromUrl(imgUrl, ConstantValues.yangzhaoImgUrl,i);
		    		 }
				
				}
	}
	}).start();
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
							int newWidth = MyApplication.getInstance().getScreenW()*2/3;
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
							msg.what =0;
							msg.arg1 = position;
							handler.sendMessage(msg);
						}
		//				System.out.println("图片存储========================"+(b?"成功":"失败"));
					}
				} catch (IOException e) {
					
				}catch(OutOfMemoryError e){
					System.out.println("在下载图片的时候内存溢出了+++++++++++++++++++++++++++++++++++++++++++++");
					Message msg = new Message();
					msg.what =1;
					msg.arg1 = position;
					handler.sendMessage(msg);
				}
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				((ImageView)pageViews.get(msg.arg1)).setImageBitmap(bitmap);
				bitmap = null;
				System.gc();
			}else if(msg.what == 1){
				((ImageView)pageViews.get(msg.arg1)).setImageResource(R.drawable.img_default);
				bitmap = null;
				System.gc();
			}
			super.handleMessage(msg);
		}
		
	};
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
	private View getView(){
//		View view = LayoutInflater.from(this).inflate(R.layout.page_detail, null);
//		ImageView imageView = (ImageView) view.findViewById(R.id.pageImageView);
		ImageView imageView = new ImageView(this);
		imageView.setAdjustViewBounds(true);
		imageView.setImageResource(R.drawable.img_default);
		return imageView;
	}
	@Override
	protected void onDestroy() {
		for(int i=0;i<pageViews.size();i++){
			if (pageViews.get(i) != null) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) ((ImageView)pageViews.get(i)).getDrawable();
				if (bitmapDrawable != null && bitmapDrawable.getBitmap()!=null && !bitmapDrawable.getBitmap().isRecycled()) {
					bitmapDrawable.getBitmap().recycle();
				}
			}
		}
		if(bitmap!=null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
		pageViews.clear();
		photoMap.clear();
		if(viewPager!=null){
			viewPager.clearDisappearingChildren();
			viewPager.clearAnimation();
			viewPager.destroyDrawingCache();
			viewPager = null;
		}
		System.gc();
		super.onDestroy();
	}
	 // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
//        	if(arg0 == 0){
//        		titleTv.setText("教堂门口");
//        		contentTv.setText("我们一起守护爱情的大门，让彼此永远生活在幸福与浪漫的爱情里");
//        	}else if(arg0 == 1){
//        		titleTv.setText("教堂大厅");
//        		contentTv.setText("这一副画将我们的幸福永远定格在这一瞬间。");
//        	}else if(arg0 == 2){
//        		titleTv.setText("蓝天白玉下的海滩游轮");
//        		contentTv.setText("蓝天白云映照着碧蓝的大海，我们驾着游轮从亮白的沙滩驶向美好的未来...");
//        	}else if(arg0 == 3){
//        		titleTv.setText("嫩绿的草坪");
//        		contentTv.setText("古色古香的建州、如画的风景，象征着我们的爱情永远青春...");
//        	}else if(arg0 == 4){
//        		titleTv.setText("郊外溪水边");
//        		contentTv.setText("溪水清澈、源远流长；我们的爱情会如溪水般清澈单纯、长长久久，并最终流入婚姻的大海。。。。。");
//        	}
        }  
    }  
    
    
    
    public class pagerAdapter extends PagerAdapter{
    	@Override
    	public int getCount() {
    		// TODO Auto-generated method stub
    		return pageViews.size();
    	}

    	@Override
    	public boolean isViewFromObject(View arg0, Object arg1) {
    		// TODO Auto-generated method stub
    		return arg0 == arg1;  
    	}
    	@Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  

        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(pageViews.get(arg1));  
        }  

        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
//        	ImageView imageView = (ImageView) pageViews.get(arg1);
            ((ViewPager) arg0).addView(pageViews.get(arg1));  
            return pageViews.get(arg1);  
        }  

        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  

        }  

        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  

        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  

        }  

        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  

        }  
    }

	@Override
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return true;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		return true;
	}
}
