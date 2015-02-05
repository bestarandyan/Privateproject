package com.qingfengweb.id.biluomiV2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.filedownload.CallbackImpl;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.filedownload.ImageLoadFromUrlOrId;
import com.qingfengweb.util.CommonUtils;
import com.qingfengweb.model.SystemUpdateInfo;
import com.qingfengweb.model.UserPhotoInfo;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.util.MessageBox;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AlbumMainActivity extends BaseActivity implements OnClickListener{
	private ImageView imageBtn;// 相框图片
	private Button backBtn;// 顶部返回按钮
	private TextView userName;// 用户号
	private Bitmap bitmap;// 我的相册主页图片封面
	private ProgressDialog progressdialog;
	private boolean runnable_tag = true;
	private ImageButton openMenuBtn;
	private PopupWindow selectPopupWindow = null;
	RelativeLayout parent;
	
	private final String IMAGE_TYPE = "image/*";
	private String fileName = "";
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
	private File sdcardTempFile = null;
	private String path = "";//图片的最终路径
	private DBHelper db = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_album);
		findview();
		initData();// 初始化数据
		
	}
	
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v == imageBtn){//相册图片
			MyApplication.getInstant().setAlbumid("");
			Intent intent = new Intent(AlbumMainActivity.this,
					PhotoListActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(v == openMenuBtn){//打开获取图片菜单
			popupWindwShowing();
		}else if(v.getId() == R.id.closeWindowBtn){//关闭菜单按钮
			dismiss();
		}else if(v.getId() == R.id.paizhaoLayout){//拍照
			takePhoto();
		}else if(v.getId() == R.id.selectPhotoLayout){//从手机中选择
			getAlbumPhoto();
		}
		super.onClick(v);
	}
	
	/**
	 * 通过调用自带照相机获取图片
	 * @author 刘星星
	 */
	private void takePhoto() {
		dismiss();
		Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileName = CommonUtils.getFileName();
		File file = new File(SDCARD_ROOT_PATH + ConstantsValues.MYALBUM_IMG_URL);
		if (!file.exists()) {
			file.mkdirs();
		}
		sdcardTempFile = new File(SDCARD_ROOT_PATH + ConstantsValues.MYALBUM_IMG_URL,
				fileName);
		path = sdcardTempFile.getAbsolutePath();
		// MyApplication.getInstance().setPhtotPath(path);
		if (CommonUtils.isHasSdcard()) {
			openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
			openCamera.putExtra("return-data", true);
		}
		startActivityForResult(openCamera, 1);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			Intent intent = new Intent(this,PhotoListActivity.class);
			intent.putExtra("isFromAlbumMain", true);
			intent.putExtra("albumPath", path);
			startActivity(intent);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 从相册获取图片
	 * @author 刘星星
	 */
	private void getAlbumPhoto(){
		dismiss();
		Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbum.setType(IMAGE_TYPE);
		startActivityForResult(getAlbum, 2);
	}
	private void findview(){
		imageBtn = (ImageView) findViewById(R.id.imageBtn);
		backBtn = (Button) findViewById(R.id.backBtn);
		userName = (TextView) findViewById(R.id.userName);
		userName.setText(UserBeanInfo.getInstant().getUserName());
		openMenuBtn = (ImageButton) findViewById(R.id.openMenuBtn);
		backBtn.setOnClickListener(this);
		imageBtn.setOnClickListener(this);
		openMenuBtn.setOnClickListener(this);
		parent = (RelativeLayout) findViewById(R.id.parent);
		initPopuWindow();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {
		// PopupWindow浮动下拉框布局
		View layout = (View) this.getLayoutInflater().inflate(
				R.layout.menu_myalbum, null);
		LinearLayout bottomLayout = (LinearLayout) layout.findViewById(R.id.bottomLayout);
		layout.findViewById(R.id.paizhaoLayout).setOnClickListener(this);
		layout.findViewById(R.id.selectPhotoLayout).setOnClickListener(this);
		layout.findViewById(R.id.closeWindowBtn).setOnClickListener(this);
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (MyApplication.getInstant().getHeightPixels()*0.3));
		bottomLayout.setLayoutParams(param);
		selectPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(false);
	}
	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	/**
	 * 显示PopupWindow窗口
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// 将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
		// 这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
		// （是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
//		selectPopupWindow.showAsDropDown(parent, 0,
//				-(parent.getHeight() - chazhi));
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
		selectPopupWindow.update();  
	}

	/***
	 * author Ring 初始化数据
	 * 
	 */
	public void initData() {
		db = DBHelper.getInstance(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	
	
	@Override
	protected void onStop() {
//		if(downloaddata!=null){
//			downloaddata.overReponse();
//		}
		super.onStop();
	}
	@Override
	protected void onResume() {
		new Thread(systemUpdateRunnable).start();
		super.onResume();
	}
	/**
	 * 系统更新机制线程
	 */
	Runnable systemUpdateRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
					+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.ServerTime.getValue();
			List<Map<String,Object>> systemList = db.selectRow(sqlTime, null);
			String systemTimeStr = "";
			String localTimeStr = "";//历史更新数据时间
			if(systemList!=null && systemList.size()>0){
				Map<String,Object> map = systemList.get(0);
				systemTimeStr = map.get("updatetime").toString();
				localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
			}
			System.out.println("本次更新时间======"+systemTimeStr);
			if(UserBeanInfo.getInstant().getCurrentStoreId()==null || UserBeanInfo.getInstant().getCurrentStoreId().equals("")){//如果没有门店ID则不进行对服务器的访问
				return;
			}
				if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					String msgStr = RequestServerFromHttp.systemUpdate(UserBeanInfo.getInstant().getCurrentStoreId(), localTimeStr);//请求服务器获取更新内容
					if(msgStr.equals("404")){//访问服务器失败
						System.out.println("本次系统更新接口访问服务器失败");
					}else if(JsonData.isNoData(msgStr)){//参数错误或者无数据
						System.out.println("本次系统更新接口返回无数据");
					}else{//请求成功并且有有效数据
						JsonData.jsonUpdateTimeData(msgStr, db.open());//解析数据并将数据保存到数据库
						ContentValues values = new ContentValues();
						values.put("localtime", systemTimeStr);
						db.update(SystemUpdateInfo.TableName, values, "type=? and userid=? and storeid=?", 
								new String[]{UpdateType.ServerTime.getValue(),UserBeanInfo.getInstant().getUserid(),UserBeanInfo.getInstant().getCurrentStoreId()});
//						String sql = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"'";
//						systemList = dbHelper.selectRow(sql, null);
//						System.out.println(systemList.size()+"");
//						handler.sendEmptyMessage(0);//处理UI
					}
				}
			
			handler.sendEmptyMessage(0);
		}
	};
	/***
	 * author by Ring 处理界面跳转，从加载页跳转到主界面
	 * 
	 */

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if(!runnable_tag)
				return;
			switch (msg.what) {
			case 0:
				new Thread(getPhotoRunnable).start();
				break;
			case 1:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.error_net),
						AlbumMainActivity.this);
				break;
			case 2:// 打开进度条
				progressdialog = new ProgressDialog(AlbumMainActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_loading));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = false;
						if(keyCode == KeyEvent.KEYCODE_BACK){
							dialog.dismiss();
							if(progressdialog!=null){
								progressdialog.dismiss();
							}
						}
//						if (downloaddata != null) {
//							downloaddata.overReponse();
//						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 3:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 4:// 初始化界面
				initData();
				break;
			case 5://获取第一张图片
				Map<String,Object> map = (Map<String, Object>) msg.obj;
				if(map.get("imgurl")!=null && !map.get("imgurl").toString().equals("")){
					String fileUrl = map.get("imgurl").toString();
					if(fileUrl!=null && !fileUrl.equals("")){
						if(new File(fileUrl).exists()){
							Bitmap bitmap = null;
							int w = (int) (MyApplication.getInstant().getWidthPixels()*0.9);
							bitmap = CommonUtils.getDrawable(fileUrl, w);
							imageBtn.setImageBitmap(bitmap);
						}else if(map.get("imageid")!=null){
							final String imgId =  map.get("imageid").toString();
							if(imgId!=null && !imgId.equals("")){
								new Thread(new Runnable() {
									public void run() {
										downImage(imgId,ImageType.UserPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
												(MyApplication.getInstant().getWidthPixels()*0.9)+"",(MyApplication.getInstant().getWidthPixels()*0.9)+"");
									}
								}).start();
							}
						}
					}
				}else if(map.get("imageid")!=null && !map.get("imageid").toString().equals("")){
					final String imgId =  map.get("imageid").toString();
					if(imgId!=null && !imgId.equals("")){
						new Thread(new Runnable() {
							public void run() {
								downImage(imgId,ImageType.UserPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
										(MyApplication.getInstant().getWidthPixels()*0.9)+"",(MyApplication.getInstant().getWidthPixels()*0.9)+"");
							}
						}).start();
					}
				}
				handler.sendEmptyMessage(3);
				break;
			case 6:
				handler.sendEmptyMessage(3);
				break;
			case 7:
				imageBtn.setImageBitmap(bitmap);
				break;
			case 8:
				imageBtn.setImageBitmap(null);
				break;
			}
		};
	};

	/***
	 * @author 刘星星
	 * @createDate 2013、9、18
	 * 获取照片id线程
	 */
	private Runnable getPhotoRunnable = new Runnable() {

		@Override
		public void run() {
			String sql = "select *from "+UserPhotoInfo.TableName+" where username = '"+UserBeanInfo.getInstant().getUserName()+"' and isUpload='1' order by iscover desc limit 1";
			List<Map<String,Object>> oldPhotoList = db.selectRow(sql, null);
			if(oldPhotoList.size()>0){//数据库如果有该用户的照片存在。
				if(oldPhotoList.get(0).get("imgurl")!=null && !oldPhotoList.get(0).get("imgurl").equals("")){
					String firstPhoto = oldPhotoList.get(0).get("imgurl").toString();//得到该用户的第一张照片用户显示在相册的封面上
					Message msg = new Message();
					msg.obj = oldPhotoList.get(0);
					msg.what = 5;
					handler.sendMessage(msg);
				}else if(oldPhotoList.get(0).get("imageid")!=null && !oldPhotoList.get(0).get("imageid").equals("")){
					String firstPhoto = oldPhotoList.get(0).get("imageid").toString();//得到该用户的第一张照片用户显示在相册的封面上
					Message msg = new Message();
					msg.obj = oldPhotoList.get(0);
					msg.what = 5;
					handler.sendMessage(msg);
				}
			}
				//检查系统更新机制表  看该用户的相册是否有更新
				String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
						+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.UserAlbum.getValue();
				List<Map<String,Object>> systemList = db.selectRow(sqlTime, null);
				String systemTimeStr = "";//最新更新时间
				String localTimeStr = "";//历史更新数据时间
				if(systemList!=null && systemList.size()>0){
					Map<String,Object> map = systemList.get(0);
					systemTimeStr = map.get("updatetime").toString();
					localTimeStr = (map.get("localtime") == null)?"":map.get("localtime").toString();
					if(!systemTimeStr.equals(localTimeStr) || (systemTimeStr.equals("") && localTimeStr.equals(""))){//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
						getPhoto(systemTimeStr,localTimeStr);//如果系统最新更新时间和历史更新时间不相同 则代表照片有更新  则向服务器获取更新的数据
					}
				}
		}
	};
	/**
	 * 访问服务器获取照片的ID 
	 * @param updateTime 最新更新时间
	 * @param localTime 上一次更新的时间
	 */
	private void getPhoto(String updateTime,String localTime){
		if (NetworkCheck.IsHaveInternet(AlbumMainActivity.this)) {//判断网络状态
			String sql = "select *from "+UserPhotoInfo.TableName+" where username = '"+UserBeanInfo.getInstant().getUserName()+"' and isUpload='1' order by iscover desc limit 1";
			handler.sendEmptyMessage(2);//开启进度条
			String msgStr = RequestServerFromHttp.getUserPhoto(UserBeanInfo.getInstant().getUserName(), UserBeanInfo.getInstant().getPassword(), localTime);
			if(msgStr.equals("404")){
				
			}else if(JsonData.isNoData(msgStr)  ){
				
			}else if(msgStr.startsWith("[") && msgStr.length()>3){
				JsonData.jsonUserPhotos(msgStr, db.open(),UserBeanInfo.getInstant().getUserName());
			}else{
				handler.sendEmptyMessage(6);
			}
			
			List<Map<String,Object>> list = db.selectRow(sql, null);
			//此时对该用户的照片id已经更新完毕，把更新机制中的历史时间更新成为最新更新时间
			ContentValues values = new ContentValues();
			values.put("localtime", updateTime);
			db.update(SystemUpdateInfo.TableName, values, "storeid=? and type =? and userid=?", 
					new String[]{UserBeanInfo.getInstant().getCurrentStoreId(),UpdateType.UserAlbum.getValue(),UserBeanInfo.getInstant().getUserid()});
			
			if(list!=null && list.size()>0 && list.get(0).get("imgurl")!=null && !list.get(0).get("imgurl").equals("")){
				String firstPhoto = list.get(0).get("imgurl").toString();//得到该用户的第一张照片用户显示在相册的封面上
				Message msg = new Message();
				msg.obj = list.get(0);
				msg.what = 5;
				handler.sendMessage(msg);
			}else if(list!=null && list.size()>0 && list.get(0).get("imageid")!=null && !list.get(0).get("imageid").equals("")){
				String firstPhoto = list.get(0).get("imageid").toString();//得到该用户的第一张照片用户显示在相册的封面上
				Message msg = new Message();
				msg.obj = list.get(0);
				msg.what = 5;
				handler.sendMessage(msg);
			}else if(list==null || list.size() == 0){
				handler.sendEmptyMessage(8);
			}
		}else{//没有网络
			
		}
	}
	
	/**
	 * 图片下载
	 * @param context 
	 * @param imageView 装载图片的控件
	 * @param imgid 图片id
	 * @param image_type 图片类型 
	 * @param download_type 下载类型 大图或者缩略图
	 * @param width 想要的宽度
	 * @param height 想要的图片的高度
	 * @param isBackground 是否为背景图片
	 * @param imgUrl 图片的本地保存路劲
	 */
	public void downImage(String imgid,String image_type,String download_type,String width,String height){
		//如果图片在本地不存在，则根据id准备去服务器下载。
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("appid", RequestServerFromHttp.APPID));
		list.add(new BasicNameValuePair("appkey", RequestServerFromHttp.APPKEY));
		list.add(new BasicNameValuePair("action", RequestServerFromHttp.ACTION_DOWNLOAD));
		list.add(new BasicNameValuePair("imageid", imgid));
		list.add(new BasicNameValuePair("image_type",image_type));
		list.add(new BasicNameValuePair("download_type", download_type));
		list.add(new BasicNameValuePair("width", width));
		list.add(new BasicNameValuePair("height", height));
		try{
		HttpPost httpPost = new HttpPost(RequestServerFromHttp.INTERFACE_SYSTEM);
		HttpEntity httpEntity = new UrlEncodedFormEntity(list,HTTP.UTF_8);
		httpPost.setEntity(httpEntity);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity entity = httpResponse.getEntity();
		InputStream isInputStream = entity.getContent();
		try{
			bitmap = BitmapFactory.decodeStream(isInputStream);
		}catch(OutOfMemoryError error){
			System.out.println("内存溢出");
		}
			if(bitmap!=null){
				handler.sendEmptyMessage(7);
				File file = new File(ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MyALBUM_IMG_URL_THUMB+imgid+".png");
				boolean b = OutPutImage(file,bitmap);
				System.out.println("图片存储========================"+(b?"成功":"失败"));
				if(b){
					ContentValues values = new ContentValues();
					values.put("imgurl", file.getAbsolutePath());
					values.put("isUpload", "1");
					db.update(UserPhotoInfo.TableName, values, "imageid=? and username=?", new String[]{imgid,UserBeanInfo.getInstant().getUserName()});
				}
			}
			isInputStream.close();
//		}
	} catch (IOException e) {

	}
		
	}
	
	
	
	public boolean OutPutImage(File file, Bitmap bitmap) {
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
}
