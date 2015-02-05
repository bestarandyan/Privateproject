package com.qingfengweb.id.biluomiV2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.FileUpLoad.HttpPostUploadFile;
import com.qingfengweb.adapter.GridViewAdapter;
import com.qingfengweb.data.ConstantsValues;
import com.qingfengweb.data.ImageType;
import com.qingfengweb.data.ImgDownType;
import com.qingfengweb.data.JsonData;
import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.data.UpdateType;
import com.qingfengweb.data.UserBeanInfo;
import com.qingfengweb.database.DBHelper;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.model.ImgLocalData;
import com.qingfengweb.model.SystemUpdateInfo;
import com.qingfengweb.model.UpLoadFileInfo;
import com.qingfengweb.model.UserPhotoInfo;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.util.CommonUtils;
import com.qingfengweb.util.FileTools;
import com.qingfengweb.util.MessageBox;

@TargetApi(Build.VERSION_CODES.FROYO)
public class PhotoListActivity extends BaseActivity implements OnClickListener{
	private Button backBtn;// 返回按钮
	private GridView topGv,bottomGv;// 图片网格控件
	private ArrayList<Map<String, Object>> list1 = new ArrayList<Map<String,Object>>();//大图
	private TextView title;
	private List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();//缩略图
	private ImageView daTuImg;
	
	private ImageButton openMenuBtn;
	private PopupWindow selectPopupWindow = null;
	LinearLayout parent;
	
	private final String IMAGE_TYPE = "image/*";
	private String fileName = "";
	private File sdcardTempFile = null;
	private String path = "";//图片的最终路径
	private DBHelper dbHelper;
	GridViewAdapter adapter = null;//
	int daW = 0;
	public String currentImageId = "";//当前图片id
	public String currentFileId = "";//当前文件在服务器的编号
	public String currentFileName = "";//当前文件的名称
	public String currentFilePath = "";//当前正在上传的文件路径
	public byte[] currentFileByte = null;//当前正在上传的文件字符数组
	public int currentUploadFile = 0;//标记当前正在上传的文件在fileList中的下标  //这里的currentUploadFile下标为localListData中的下标
	public int countSuccess = 0;//成功上传文件个数。
	public boolean uploadOver = false;//标记是否所有文件上传完成。
	public boolean uploading = false;//标记正在上传当中
//	public int typeGv = 0;//上传的图片所属控件  0代表属于第一张大图  1代表数据第一个gridview 2代表数据第二个gridview
	public int utilPro = 0;//代表当前进度条每一次应该提升的进度
	public int upNumber = 0;//表示当前文件总共要上传的次数
	ProgressBar currentBar = null;//当前准备走动的进度条
	TextView currentProtv = null;//当前进度条显示百分比的控件
	List<Map<String,Object>> localListData = null;//表示本地的照片集合
//	ArrayList<Bundle> fileList = new ArrayList<Bundle>();
	RelativeLayout firstImgLayout = null;
	ScrollView photoScrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_photolist);
		findView();
		initData();
		new Thread(systemUpdateRunnable).start();
	}
	private void initData(){
		daW = (MyApplication.getInstant().getWidthPixels())/2-10;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(daW,daW);
		params.setMargins(5, 5, 0, 0);
		firstImgLayout.setLayoutParams(params);
		firstImgLayout.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(daW,daW);
		daTuImg.setLayoutParams(params1);
		dbHelper = DBHelper.getInstance(this);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String sql = "select *from "+UserPhotoInfo.TableName+" where username = '"+UserBeanInfo.getInstant().getUserName()+"' and isUpload='1'";
				localListData = dbHelper.selectRow(sql, null);
				
				sql = "select *from "+UserPhotoInfo.TableName+" where username = '"+UserBeanInfo.getInstant().getUserName()+"' and isUpload='0'";
				List<Map<String,Object>> list0 = dbHelper.selectRow(sql, null);
				if(list0.size()>0){//如果没有
					currentUploadFile = localListData.size();
				}
				if(list0!=null && list0.size()>0){
					currentFileId = "";
//					boolean isHaveEffectiveData = false;//标记是否有有效的数据
					for(int i=0;i<list0.size();i++){
						currentFilePath = list0.get(i).get("imgurl").toString();
						localListData.add(list0.get(i));
//						currentBar = (ProgressBar) findViewById(R.id.progressBar);
//		        		currentProtv = (TextView) findViewById(R.id.currentProText);
//		        		currentBar.setVisibility(View.GONE);
//		        		currentProtv.setVisibility(View.GONE);
//						handlerGvData(true);
//						isHaveEffectiveData = true;
					}
//					if(isHaveEffectiveData){//如果有有效的需要上传的图片
//						//这里的currentUploadFile下标为localListData中的下标
//						if(list1.size()>0){//已经上传成功的图片张数大于1的时候
//							currentFilePath = localListData.get(list1.size()+list2.size()+1).get("imgurl").toString();
//							currentUploadFile = list1.size()+list2.size()+1;
//							currentUploadPro = Float.parseFloat(localListData.get(currentUploadFile).get("progress").toString());
//						}else if(firstImgLayout.getVisibility() == View.GONE){//已经上传成功的图片张数等于0的时候
//							currentFilePath = localListData.get(0).get("imgurl").toString();
//							currentUploadFile = 0;
//							currentUploadPro = Float.parseFloat(localListData.get(currentUploadFile).get("progress").toString());
//						}else if(list1.size()==0 && firstImgLayout.getVisibility() == View.VISIBLE){//已经上传成功的图片张数等于1的时候
//							currentFilePath = localListData.get(1).get("imgurl").toString();
//							currentUploadFile = 1;
//							currentUploadPro = Float.parseFloat(localListData.get(currentUploadFile).get("progress").toString());
//						}
//						Message msg = new Message();
//						msg.what = 17;
//						handler.sendMessage(msg);
//					}else{
//						handler.sendEmptyMessage(12);
//					}
				}
				handler.sendEmptyMessage(18);
				
			}
		}).start();
		
		
	}
	String firstPhoto = "";/*localListData.get(0).get("imageid").toString();*/
	/**
	 * @author 刘星星
	 * 处理两个gridview中的数据
	 */
	private void handlerGvData(boolean isUpload,int startPosition){
		if(localListData.size() == 0){
			firstImgLayout.setVisibility(View.GONE);
		}else{
			firstImgLayout.setVisibility(View.VISIBLE);
			//处理第一张图片
			
			if(firstPhoto.equals("")){
				if(localListData.get(0).get("imgurl")!=null && !localListData.get(0).get("imgurl").equals("")){
					firstPhoto = localListData.get(0).get("imgurl").toString();
				}else if(localListData.get(0).get("imageid")!=null){
					firstPhoto = localListData.get(0).get("imageid").toString();
				}
			}
			if(new File(firstPhoto).exists()){//如果该图片存在 则直接显示该图片
				int w = MyApplication.getInstant().getWidthPixels()/2;
				scaleBitmap(firstPhoto, w);
				cutBitmap(w);
				daTuImg.setImageBitmap(bitmap);
			}else{//如果不存在 则去服务器下载
				if(firstPhoto!=null && !firstPhoto.equals("")){
//					RequestServerFromHttp.downImage(PhotoListActivity.this,daTuImg,firstPhoto,ImageType.UserPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
//							MyApplication.getInstant().getWidthPixels()+"","0",false,ConstantsValues.MyALBUM_IMG_URL_THUMB,R.drawable.photolist_defimg);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							downImage(daTuImg,firstPhoto,ImageType.UserPhotos.getValue(),ImgDownType.ThumbBitmap.getValue(),
									MyApplication.getInstant().getWidthPixels()/2+"",MyApplication.getInstant().getWidthPixels()/2+"");
						}
					}).start();
					
					
				}
			}
			list1.clear();
			list2.clear();
			if(localListData.size()>1){//大于5则代表会有三个部分
				for(int i=1;i<localListData.size();i++){
					Map<String, Object> map = localListData.get(i);
					if(i<5){
						list1.add(map);
					}else{
						list2.add(map);
					}
				}
			}
			if(list1.size()>0){
				notifyAdapter(topGv, list1,2,daW,isUpload,startPosition);
			}
			if(list2.size()>0){
				notifyAdapter(bottomGv, list2,4,daW,isUpload,startPosition);
			}
			
		}
		photoScrollView.scrollTo(0, photoScrollView.getHeight());
	}
	/**
	 * 给gv控件加载适配器
	 */
	private void notifyAdapter(GridView gv,List<Map<String, Object>> list,int numColumns,int daW,boolean isUpload,int uploadStartLocation) {
		int h = list.size()/numColumns+(list.size()%numColumns>0?1:0);//在垂直方向上有多少行
		int util = (MyApplication.getInstant().getWidthPixels()-32)/4;//单位高
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(numColumns==4?LayoutParams.MATCH_PARENT:(MyApplication.getInstant().getWidthPixels()/2-5),
				h*(util+5));
		if(numColumns==4){
			params.setMargins(5, 5, 5, 20);
		}else if(numColumns==2){
			params.setMargins(5, 5, 5, 0);
		}else{
			params.setMargins(5, 0, 0, 0);
		}
		gv.setLayoutParams(params);
		adapter = new GridViewAdapter(this, list,daW,0);
		gv.setAdapter(adapter);
		gv.setCacheColorHint(0);
		if(isUpload && NetworkCheck.IsHaveInternet(PhotoListActivity.this)){//开始上传图片
			uploadOver = false;
			currentUploadFile = uploadStartLocation;
			new Thread(uploadFileStart).start();
		}
		
	}

	/**
	 * 初始化控件函数
	 */
	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		topGv = (GridView) findViewById(R.id.topGv);
		topGv.setOnItemClickListener(new gvItemListener());
		topGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		bottomGv = (GridView) findViewById(R.id.bottomgv);
		bottomGv.setOnItemClickListener(new gvItemListener());
		bottomGv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		title = (TextView) findViewById(R.id.title);
		firstImgLayout = (RelativeLayout) findViewById(R.id.firstImgLayout);
		daTuImg = (ImageView) findViewById(R.id.datuImg);
		daTuImg.setOnClickListener(this);
		openMenuBtn = (ImageButton) findViewById(R.id.openMenuBtn);
		backBtn.setOnClickListener(this);
		openMenuBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		photoScrollView = (ScrollView) findViewById(R.id.photoScrollView);
		initPopuWindow();
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v == openMenuBtn){//打开获取图片菜单
//			if (NetworkCheck.IsHaveInternet(PhotoListActivity.this)) {//检查是否有网络连接
				popupWindwShowing();
//			}else{
//				MessageBox.CreateAlertDialog(
//						getResources().getString(R.string.prompt),
//						getResources().getString(R.string.person_error_net),
//						PhotoListActivity.this);
//			}
		}else if(v.getId() == R.id.closeWindowBtn){//关闭菜单按钮
			dismiss();
		}else if(v.getId() == R.id.paizhaoLayout){//拍照
				takePhoto();
		}else if(v.getId() == R.id.selectPhotoLayout){//从手机中选择
				getAlbumPhoto();
		}else if(v == daTuImg){//第一张图片的点击事件
			if(localListData.size()>0){
				Intent intent = new Intent(PhotoListActivity.this,ImageSwitcher.class);
				intent.putExtra("photoList", (Serializable)localListData);
				intent.putExtra("index", 0);
				intent.putExtra("localSDUrl", ConstantsValues.MYALBUM_IMG_URL);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
			}else{
				Toast.makeText(this, "赞无您的相片，您可以点击右上角的照相机按钮获取图片后，上传到服务器！", 3000).show();
			}
			
		}
		super.onClick(v);
	}
	Bitmap bitmap= null;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bitmap = null;
		if(requestCode == 1){
			if(path!=null && !path.equals("")){//拍照返回处理
				if(localListData.size() == 0){//这是相册的第一张图片
					daTuImg.setImageResource(R.drawable.photolist_defimg);
				}
				photoScrollView.scrollTo(0, photoScrollView.getHeight());
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						ContentValues contentValues = new ContentValues();
						contentValues.put("imgurl", path);
						contentValues.put("isUpload", "0");
						contentValues.put("state", "1");
						contentValues.put("name", sdcardTempFile.getName());
						contentValues.put("createtime", CommonUtils.getCurrentTime());
						contentValues.put("username", UserBeanInfo.getInstant().getUserName());
						long number = dbHelper.insert(UserPhotoInfo.TableName, contentValues);
						scaleBitmap(path,MyApplication.getInstant().getWidthPixels());
						RotateImg(path);
						if(bitmap!=null){
							boolean saveImg = OutPutImage(sdcardTempFile, bitmap);
							if(saveImg){//压缩并保存成功后  删除原有图片
								currentFilePath = sdcardTempFile.getAbsolutePath();
							}else{
								currentFilePath = path;
							}
//							int w = MyApplication.getInstant().getWidthPixels()/2;
//							bitmap = CommonUtils.getDrawable(currentFilePath, w);
							scaleBitmap(path,MyApplication.getInstant().getWidthPixels()/2);
							cutBitmap(MyApplication.getInstant().getWidthPixels()/2);
							if(localListData.size() == 0){//这是相册的第一张图片
								handler.sendEmptyMessage(16);
							}else{//除第一张大图以外的两个gridview控件的图片的处理
								handler.sendEmptyMessage(17);
							}
						}
						
					}
				}).start();
				
			}
		} else if (requestCode == 2) {// 选择图片返回
			String imgPath = "";
			if(data==null){
				return;
			}
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

			final File fromFile = new File(imgPath);
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
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						FileTools.copyFile(fromFile.getAbsolutePath(), sdcardTempFile.getAbsolutePath());
						ContentValues contentValues = new ContentValues();
						contentValues.put("imgurl", sdcardTempFile.getAbsolutePath());
						contentValues.put("isUpload", "0");
						contentValues.put("state", "1");
						contentValues.put("name", sdcardTempFile.getName());
						contentValues.put("createtime", CommonUtils.getCurrentTime());
						contentValues.put("username", UserBeanInfo.getInstant().getUserName());
						dbHelper.insert(UserPhotoInfo.TableName, contentValues);
						scaleBitmap(sdcardTempFile.getAbsolutePath(),MyApplication.getInstant().getWidthPixels());
						if(bitmap!=null){
							boolean copy = OutPutImage(sdcardTempFile, bitmap);
							if (copy) {
								currentFilePath = sdcardTempFile.getAbsolutePath();
//								int w = MyApplication.getInstant().getWidthPixels();
//								bitmap = CommonUtils.getDrawable(currentFilePath, w);
								scaleBitmap(currentFilePath,MyApplication.getInstant().getWidthPixels()/2);
								cutBitmap(MyApplication.getInstant().getWidthPixels()/2);
								if(localListData.size() == 0){//这是相册的第一张图片
									System.out.println("这次是第一张图片的显示");
									handler.sendEmptyMessage(13);
								}else{//除第一张大图以外的两个gridview控件的图片的处理
									handler.sendEmptyMessage(14);
								}
							}
						}else{
							handler.sendEmptyMessage(15);
						}
					}
				}).start();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void scaleBitmap(String path,int w){
		File file = new File(path);
		if(file.exists()){
			try {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
				BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
				opts.inSampleSize = PicHandler.computeSampleSize(opts, -1,1000 * 1000);
				opts.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
				if (bitmap != null) {
					int width = bitmap.getWidth();
					int height = bitmap.getHeight();
					int newWidth = w;
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
		}
	}
	
	/**
	 * 将倒掉的图片旋转90°
	 * @author 刘星星
	 * @param fileName 文件路径
	 * @param oldBitmap 位图
	 * @return 新的正立的图片
	 */
public void RotateImg(String fileUrl){
	ExifInterface exifInterface = null;
	try {
		exifInterface = new ExifInterface(fileUrl);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	int tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	int degree = 0;
	if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
		degree = 90;
	} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
			degree = 180;
	} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
				degree = 270;
	}
	if (degree != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degree, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
			try {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				} catch (OutOfMemoryError ex) {
				}
	}
}
	/**
	 * 压缩并剪切图片，图片可能不会完全显示出来
	 * @param bm 
	 * @param newWidth
	 * @param newHeight
	 * @return
	 * @author 刘星星
	 * @createDate 2013/2/20
	 */
		public void cutBitmap(int newWidth) {
			// 图片源
			// Bitmap bm = BitmapFactory.decodeStream(getResources()
			// .openRawResource(id));
			// 获得图片的宽高
			// Bitmap newbm = null;
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 设置想要的大小
			float newWidth1 = newWidth;
			float newHeight1 = newWidth;
			// 计算缩放比例
			if (height > width) {
				float scaleHeight = ((float) newHeight1) / height;
				newWidth1 = ((float) (width * (float) newHeight1) / height);
				float scaleWidth = ((float) newWidth1) / width;
				// 取得想要缩放的matrix参数
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				// 得到新的图片
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, width,
						matrix, true);
			} else {
				float scaleWidth = ((float) newWidth1) / width;
				newHeight1 = ((float) (height * (float) newWidth1) / width);
				float scaleHeight = ((float) newHeight1) / height;
				// 取得想要缩放的matrix参数
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				// 得到新的图片

				bitmap = Bitmap.createBitmap(bitmap, 0, 0, height, height,
						matrix, true);
			}

		}
	/**
	 * 文件开始下载
	 */
	Runnable uploadFileStart = new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				uploadFile(currentFilePath);
			}
		};
		
		Runnable upLoadPiece = new Runnable() {
			
			@Override
			public void run() {
				currentPiece ++;
				System.out.println("当前块0000000000000000000000=="+currentPiece);
				uploadPiece(currentFileByte,currentFilePath,currentFileId,currentPiece);
				
			}
		};
		
	 /**
     * 上传图片到服务器
     * 
     * @param imageFile 包含路径
     */
	public int uploadMax = 10*1024;//一次上传文件的最大值  单位：字节
	public float currentUploadPro = 0.0f;//当前上传进度
	public int currentPiece = 1;//当前的块数
//	public int continueUploadStartLocation = 0;//续传的起始位置
    public void uploadFile(String filePath) {
    	ConnectivityManager connectMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info = connectMgr.getActiveNetworkInfo();
    	if(info!=null){
    		if(info.getType() == ConnectivityManager.TYPE_WIFI){//wifi网络
    			uploadMax = 50*1024;//一次上传文件的最大值  单位：字节
    		}else if(info.getType() == ConnectivityManager.TYPE_MOBILE){//手机网络
    			if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0//电信3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A//电信3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B){//电信3G
    				uploadMax = 30*1024;//一次上传文件的最大值  单位：字节
    			}else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA){//电信3G
    				uploadMax = 10*1024;//一次上传文件的最大值  单位：字节
    			}
    		}
    	}
    	System.out.println("目前每块上传的长度为："+uploadMax);
        try {
        	if(currentFileByte!=null){
        		currentFileByte = null;
        		System.gc();
        	}
            currentFileByte = CommonUtils.fileToByte(filePath);//将文件转成字节数组
            currentUploadPro = 0;
            currentPiece = 1;
            uploading = true;
            if(currentFileByte.length<=uploadMax){//当文件大小小于256KB时 直接一次性上传
            	uploadFirst(currentFileByte,filePath,"",1);
            }else{//如果文件的大小大于等于256KB 则分块上传  每一块的大小为256ＫＢ
            	uploadFirst(currentFileByte,filePath,"",1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 上传文件的第一段
     * @param fileByte
     * @param filePath
     * @param fileId
     * @param currentPiece
     * @param typeGv 列表控件类型
     */
    private void uploadFirst(byte[] fileByte,String filePath,String fileId,int currentPiece){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
        	String fileName = new File(filePath).getName().toString().trim();
//        	File listFile = null;
//    		for(int i=0;i<list2.size();i++){
//    			Object imgPath = list2.get(i).get("imgurl");
//    			if(imgPath!=null){
//    				listFile = new File(imgPath.toString());
//        			if(fileName.equals(listFile.getName())){
//        				System.out.println("下面的控件为："+bottomGv.getChildAt(i));
//        	if(typeGv == 0){
//        		System.out.println("这是第一张图片正在上传");
//        		currentBar = (ProgressBar) findViewById(R.id.progressBar);
//        		currentProtv = (TextView) findViewById(R.id.currentProText);
//        	}else if(typeGv == 1){
//        		currentBar = (ProgressBar) topGv.getChildAt(list1.size()-1).findViewById(R.id.progressBar);
//				currentProtv = (TextView) topGv.getChildAt(list1.size()-1).findViewById(R.id.currentProText);
//        	}else if(typeGv == 2){
//        		currentBar = (ProgressBar) bottomGv.getChildAt(list2.size()-1).findViewById(R.id.progressBar);
//				currentProtv = (TextView) bottomGv.getChildAt(list2.size()-1).findViewById(R.id.currentProText);
//        	}
        	
        	if(currentUploadFile==0){//当续传的文件为第一张大图
				currentBar = (ProgressBar)findViewById(R.id.progressBar);
				currentProtv = (TextView)findViewById(R.id.currentProText);
			}else if(currentUploadFile>0 && currentUploadFile<5){//当续传的文件为第一个gridview中的图片
	        	currentBar = (ProgressBar) topGv.getChildAt(currentUploadFile-1).findViewById(R.id.progressBar);
				currentProtv = (TextView) topGv.getChildAt(currentUploadFile-1).findViewById(R.id.currentProText);
	        }else if(currentUploadFile >= 5){//当续传的文件为第二个gridview中的图片
	        	currentBar = (ProgressBar) bottomGv.getChildAt(currentUploadFile-5).findViewById(R.id.progressBar);
				currentProtv = (TextView) bottomGv.getChildAt(currentUploadFile-5).findViewById(R.id.currentProText);
	        }
        				
//        			}
//    			}
//    			
//    		}
    		handler.sendEmptyMessage(5);//告诉界面正在上传。
    		upNumber = fileByte.length/uploadMax+(fileByte.length%uploadMax>0?1:0);
    		utilPro = 100/upNumber;
	    	Map<String, String> params = new HashMap<String, String>();
	        params.put("appid", RequestServerFromHttp.APPID);
	        params.put("appkey", RequestServerFromHttp.APPKEY);
	        params.put("action", RequestServerFromHttp.ACTION_UPLOAD_IMG);
	        params.put("username", UserBeanInfo.getInstant().getUserName());
	        params.put("password", UserBeanInfo.getInstant().getPassword());
	        params.put("name", fileName);
	        params.put("uploadid", fileId);
	        params.put("size", fileByte.length+"");
	        params.put("start", "0");
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        String endLocation = "";
	        if(upNumber>1){//大于1块的时候
	        	endLocation = uploadMax+"";
	        	params.put("length", (uploadMax)+"");
	        	System.out.println("当前上传块=================="+currentPiece);
	        	System.out.println("当前上传块的起始位置=================="+0);
	        	System.out.println("当前上传数据长度=================="+uploadMax);
	        	//开始下载
	    	    msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, 0, uploadMax,"photo");
	    	    System.out.println("本次服务器的返回值=================="+msgStr);
	    	    System.out.println("------------------------------------------------");
	        }else{//只有一块的时候
	        	endLocation = (fileByte.length)+"";
	        	params.put("length", (fileByte.length)+"");
        		//开始上传
    	        msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, 0, fileByte.length,"photo");
    	        System.out.println(msgStr);
	        }
	        	//解析数据
	            ArrayList<String> reList = JsonData.jsonImageUploadData(msgStr, dbHelper.open(),currentFilePath,endLocation,fileByte.length+"");
	            currentUploadPro = Float.parseFloat(reList.get(1));//设置当前进度
	            currentFileId = reList.get(0);
        		handler.sendEmptyMessage(2);

    	}catch(Exception e){
    		System.out.println("上传第一块时出现问题了，会跳过该文件上传下一个文件，本次出问题的原因为："+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
    /**
     * 上传文件的所有部分不包括第一段
     * @param fileByte
     * @param filePath
     * @param fileId 文件编号
     * @param currentPiece
     */
    private void uploadPiece(byte[] fileByte,String filePath,String fileId,int currentPiece){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
        	String fileName = new File(currentFilePath).getName().toString().trim();
//        	File listFile = null;
//    		for(int i=0;i<list2.size();i++){
//    			Object imgPath = list2.get(i).get("imgurl");
//    			if(imgPath!=null){
//    				listFile = new File(imgPath.toString());
//        			if(fileName.equals(listFile.getName())){
//        				System.out.println("下面的控件为："+bottomGv.getChildAt(i));
//        	if(localListData.size() == 1){
//        		System.out.println("这是第一张图片正在上传");
//        		currentBar = (ProgressBar) findViewById(R.id.progressBar);
//        		currentProtv = (TextView) findViewById(R.id.currentProText);
//        	}else{
//        		currentBar = (ProgressBar) bottomGv.getChildAt(list2.size()-1).findViewById(R.id.progressBar);
//				currentProtv = (TextView) bottomGv.getChildAt(list2.size()-1).findViewById(R.id.currentProText);
//        	}
//        			}
//    			}
//    			
//    		}
	    	Map<String, String> params = new HashMap<String, String>();
	    	params.put("appid", RequestServerFromHttp.APPID);
	        params.put("appkey", RequestServerFromHttp.APPKEY);
	        params.put("action", RequestServerFromHttp.ACTION_UPLOAD_IMG);
	        params.put("username", UserBeanInfo.getInstant().getUserName());
	        params.put("password", UserBeanInfo.getInstant().getPassword());
	        params.put("name", fileName);
	        params.put("uploadid", fileId);
	        params.put("size", fileByte.length+"");
	        System.out.println("文件总长度=================="+fileByte.length);
	        System.out.println("上传id====:"+fileId);
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        String startLocation = "0";//起始位置
	        String endLocation = "0";//结束位置， 这个变量不是用来传递给服务器的，是用来记录上传成功的位置，用于做续传的起点位置
	        if(upNumber>1){//大于1块的时候
	        	if(currentPiece < upNumber){
	        		startLocation = ""+(currentPiece-1)*uploadMax;
	        		endLocation = currentPiece*uploadMax+"";
	        		params.put("start", startLocation);
	        		params.put("length", (uploadMax)+"");
	        		System.out.println("当前上传块=================="+currentPiece);
	        		System.out.println("当前上传块的起始位置=================="+(currentPiece-1)*uploadMax);
	        		System.out.println("当前上传数据长度=================="+uploadMax);
	        		//开始下载
	    	        msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, (currentPiece-1)*uploadMax, uploadMax*currentPiece,"photo");
	    	        System.out.println("本次服务器的返回值=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        	}else if(currentPiece == upNumber){
	        		startLocation = ""+(currentPiece-1)*uploadMax;
	        		endLocation = fileByte.length+"";
	        		params.put("start", startLocation);
	        		params.put("length", (fileByte.length-(uploadMax*(currentPiece-1)))+"");
	        		System.out.println("当前上传块=================="+currentPiece);
	        		System.out.println("当前上传块的起始位置=================="+(currentPiece-1)*uploadMax);
	        		System.out.println("当前上传数据长度=================="+(fileByte.length-(uploadMax*(currentPiece-1))));
	        		//开始下载
	    	        msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, (currentPiece-1)*uploadMax, fileByte.length,"photo");
	    	        System.out.println("本次服务器的返回值=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        	}
	        }else{//只有一块的时候
	        	params.put("start", startLocation);
	        	params.put("length", (fileByte.length)+"");
        		//开始下载
    	        msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, 0, fileByte.length,"photo");
    	        System.out.println(msgStr);
	        }
	        	//解析数据
	            ArrayList<String> reList = JsonData.jsonImageUploadData(msgStr, dbHelper.open(),currentFilePath,endLocation,fileByte.length+"");
	            currentUploadPro = Float.parseFloat(reList.get(1));//设置当前进度
	            currentFileId = reList.get(0);
	            currentImageId = reList.get(2);
        		handler.sendEmptyMessage(3);

    	}catch(Exception e){
    		System.out.println("上传第"+currentPiece+"块时出现问题了，会跳过该文件上传下一个文件，本次出问题的原因为："+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
    
    /**
     * 上传文件的所有部分不包括第一段
     * @param fileByte
     * @param filePath
     * @param fileId 文件编号
     * @param currentPiece
     */
    private void continueUploadPiece(byte[] fileByte,String filePath,String fileId,int currentPiece){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
        	String fileName = new File(currentFilePath).getName().toString().trim();
	    	Map<String, String> params = new HashMap<String, String>();
	    	params.put("appid", RequestServerFromHttp.APPID);
	        params.put("appkey", RequestServerFromHttp.APPKEY);
	        params.put("action", RequestServerFromHttp.ACTION_UPLOAD_IMG);
	        params.put("username", UserBeanInfo.getInstant().getUserName());
	        params.put("password", UserBeanInfo.getInstant().getPassword());
	        params.put("name", fileName);
	        params.put("uploadid", fileId);
	        params.put("size", fileByte.length+"");
	        System.out.println("文件总长度=================="+fileByte.length);
	        System.out.println("上传id====:"+fileId);
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        String startLocation = "0";//起始位置
	        String endLocation = "0";//结束位置， 这个变量不是用来传递给服务器的，是用来记录上传成功的位置，用于做续传的起点位置
	    /*    if(upNumber>1){//大于1块的时候
	        	if(currentPiece < upNumber){*/
//	        		startLocation = ""+(currentPiece-1)*uploadMax;
//	        		endLocation = currentPiece*uploadMax+"";
	        		params.put("start", "0");
	        		params.put("length", (fileByte.length)+"");
//	        		System.out.println("当前上传块=================="+currentPiece);
//	        		System.out.println("当前上传块的起始位置=================="+continueUploadStartLocation);
//	        		System.out.println("当前上传数据长度=================="+(fileByte.length-continueUploadStartLocation));
	        		//开始下载
	    	        msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, 0, fileByte.length,"photo");
	    	        System.out.println("本次续传图片访问服务器的返回值为=================="+msgStr);
	    	        System.out.println("-------------------续传完毕-----------------------------");
	        /*	}else if(currentPiece == upNumber){
	        		startLocation = ""+(currentPiece-1)*uploadMax;
	        		endLocation = fileByte.length+"";
	        		params.put("start", startLocation);
	        		params.put("length", (fileByte.length-(uploadMax*(currentPiece-1)))+"");
	        		System.out.println("当前上传块=================="+currentPiece);
	        		System.out.println("当前上传块的起始位置=================="+(currentPiece-1)*uploadMax);
	        		System.out.println("当前上传数据长度=================="+(fileByte.length-(uploadMax*(currentPiece-1))));
	        		//开始下载
	    	        msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, (currentPiece-1)*uploadMax, fileByte.length,"photo");
	    	        System.out.println("本次服务器的返回值=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        	}
	        }else{//只有一块的时候
	        	params.put("start", startLocation);
	        	params.put("length", (fileByte.length)+"");
        		//开始下载
    	        msgStr =  HttpPostUploadFile.post(this,RequestServerFromHttp.INTERFACE_USER, params, files, 0, fileByte.length,"photo");
    	        System.out.println(msgStr);
	        }*/
	        	//解析数据
	            ArrayList<String> reList = JsonData.jsonImageUploadData(msgStr, dbHelper.open(),currentFilePath,endLocation,fileByte.length+"");
	            currentUploadPro = Float.parseFloat(reList.get(1));//设置当前进度
	            currentFileId = reList.get(0);
	            currentImageId = reList.get(2);
        		handler.sendEmptyMessage(3);

    	}catch(Exception e){
    		System.out.println("上传第"+currentPiece+"块时出现问题了，会跳过该文件上传下一个文件，本次出问题的原因为："+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
    /**
     * UI处理
     */
    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				notifyAdapter(bottomGv, list2,4,daW,false,0);
			}else if(msg.what == 1){
				initData();//这个函数为界面制作专用函数
			}else if(msg.what == 2){//第一段提交成功
				currentBar.setProgress(0);
				currentBar.setProgress(currentBar.getProgress()+utilPro);
				currentBar.getProgressDrawable().setAlpha(120);
				currentProtv.setVisibility(View.VISIBLE);
				currentProtv.setText(currentBar.getProgress()+"%");
				System.out.println(currentPiece+"当前进度条的高度值："+currentBar.getProgress());
//				currentProtv.setTextSize(20);
				if(upNumber == 1){//文件大小不大于最大上传量
					if(currentBar.getProgress() == 100){
						ContentValues values = new ContentValues();
						values.put("isUpload", "1");
						values.put("imageid", currentImageId+"");
						dbHelper.update(UserPhotoInfo.TableName, values, "imgUrl=?", new String[]{currentFilePath});
						currentPiece = 1;
						currentUploadFile++;
						if(currentUploadFile<localListData.size()){//如果正在上传的文件只需要上传一次则上传一次后就开始上传下一个文件
							currentFilePath = localListData.get(currentUploadFile).get("imgurl").toString();
							new Thread(uploadFileStart).start();
						}else{
							uploadOver = true;
							uploading =  false;
						}
//						HashMap<String,String> map = new HashMap<String, String>();
//						map.put("currentFileId", currentFileId);
//						map.put("currentFileName", currentFileName);
//						if(!fileIdList.contains(map)){
//							fileIdList.add(map);
//						}
					}
				}else if(upNumber>1){//需要多次上传的文件，从这里开始上传后面的片段
					for(int i=2;i<=upNumber;i++){
						new Thread(upLoadPiece).start();
					}
				}
			}else if(msg.what ==3 ){//上传成功，可能是片段 可能是整个文件
				currentBar.setProgress(currentBar.getProgress()+utilPro);
				currentBar.getProgressDrawable().setAlpha(80);
				currentProtv.setText(currentBar.getProgress()+"%");
//				currentProtv.setTextSize(20);
				System.out.println(currentPiece+"当前进度条的高度值："+currentBar.getProgress());
//				Intent intent = new Intent();
//				intent.setAction("com.qingfengweb.setProAction");
//				context.sendBroadcast(intent);
				System.out.println("上传总块数xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx--------------"+upNumber);
				if(currentPiece == upNumber){
					currentBar.setProgress(100);
					currentBar.getProgressDrawable().setAlpha(80);
					currentProtv.setText("100%");
//					currentProtv.setTextSize(20);
					localListData.get(localListData.size()-1).put("imageid", currentImageId);
					
				}
				if(currentBar.getProgress() == 100/* && currentPiece == upNumber*/){//一个文件上传成功
//					HashMap<String,String> map = new HashMap<String, String>();
//					map.put("currentFileId", currentFileId);
//					map.put("currentFileName", currentFileName);
//					if(!fileIdList.contains(map)){
//						fileIdList.add(map);
//					}
					ContentValues values = new ContentValues();
					values.put("isUpload", "1");
					values.put("imageid", currentImageId+"");
					dbHelper.update(UserPhotoInfo.TableName, values, "imgurl=?", new String[]{currentFilePath});
					if(currentFileByte!=null){
		        		currentFileByte = null;
		        		System.gc();
		        	}
					currentPiece = 1;
					currentUploadFile++;
//					if(!isStopFileUpload){//判断是否需要停止上传
						if(currentUploadFile<localListData.size()){//接着上传下一个文件
							currentFilePath = localListData.get(currentUploadFile).get("imgurl").toString();
							new Thread(uploadFileStart).start();
						}else{
							uploadOver = true;
							uploading =  false;
//							if(clickSubmitBtn){//当文件上传完了之后，判断是否点击过提交按钮，如果点击过 这直接上传信息
//								infoBundle = getInfo();
//								new Thread(submitInfoRunnable).start();
//							}
						}
//					}
				}
			}else if(msg.what == 4){//如果某一个文件上传失败 则继续进入下一个文件的上传
				if(currentFileByte!=null){
	        		currentFileByte = null;
	        		System.gc();
	        	}
//				currentProtv.setText("上传失败");
//	    		currentProtv.setTextColor(Color.RED);
//	    		currentProtv.setTextSize(12);
				currentPiece = 1;
				currentUploadFile++;
//				if(currentUploadFile<fileList.size()){
//					new Thread(uploadFileStart).start();
//				}else{
//					uploadOver = true;
//					uploading =  false;
//				}
			}else if(msg.what == 5){//告诉用户正在上传，
//				currentProtv.setText("上传中...");
//				currentProtv.setTextSize(12);
			}else if(msg.what == 6){//信息和文件都上传成功。
//				dialog.dismiss();
//				showDialog("需求提交成功，我司会尽快与您联系！", "知道了！", true, false);
			}else if(msg.what == 7){//连接服务器失败
				
			}else if(msg.what == 8){//提交失败。
				
			}else if(msg.what == 9){//更新相册
				new Thread(getPhotoRunnable).start();
				
			}else if(msg.what == 10){//续传
//				notifyAdapter(bottomGv, list2,4,daW,false,currentUploadFile);
				handlerGvData(false,currentUploadFile);
				handler.sendEmptyMessage(12);//
				new Thread(findViewRunnable).start();
			}else if(msg.what == 11){
				for(int i=currentUploadFile;i<localListData.size();i++){
					String proStr = localListData.get(i).get("progress").toString();
					int progress = proStr.equals("1")?1:Integer.parseInt((proStr+"").substring(2, 4));
					if(currentUploadFile==0){//当续传的文件为第一张大图
						ProgressBar p = ((ProgressBar)findViewById(R.id.progressBar));
						TextView t = ((TextView)findViewById(R.id.currentProText));
						p.setProgress(progress);
						p.getProgressDrawable().setAlpha(120);
						p.setVisibility(View.VISIBLE);
						t.setVisibility(View.VISIBLE);
						t.setText(progress+"%");
					}else if(currentUploadFile>0 && currentUploadFile<5){//当续传的文件为第一个gridview中的图片
						ProgressBar p = ((ProgressBar) topGv.getChildAt(i-1).findViewById(R.id.progressBar));
						p.setProgress(progress);
						p.getProgressDrawable().setAlpha(120);
						p.setVisibility(View.VISIBLE);
						TextView t = ((TextView) topGv.getChildAt(i-1).findViewById(R.id.currentProText));
						t.setVisibility(View.VISIBLE);
						t.setText(progress+"%");
			        }else if(currentUploadFile >= 5){//当续传的文件为第二个gridview中的图片
			        	ProgressBar p = ((ProgressBar) bottomGv.getChildAt(i-5).findViewById(R.id.progressBar));
			        	p.setProgress(progress);
			        	p.getProgressDrawable().setAlpha(120);
			        	p.setVisibility(View.VISIBLE);
			        	TextView t = ((TextView) bottomGv.getChildAt(i-5).findViewById(R.id.currentProText));
			        	t.setText(progress+"%");
			        	t.setVisibility(View.VISIBLE);
			        }
				}
//				currentBar.getProgressDrawable().setAlpha(120);
//				currentProtv.setVisibility(View.VISIBLE);
//				currentBar.setProgress(progress);
//				currentProtv.setText(progress+"%");
//				new Thread(continueUploadRunnable).start();
				new Thread(uploadFileStart).start();
			}else if(msg.what == 12){//从相册封面进来的
				boolean flag = getIntent().getBooleanExtra("isFromAlbumMain", false);
				if(flag){
					String path = getIntent().getStringExtra("path");
				}
				
			}else if(msg.what == 13){//选择完照片后设置列表照片
				firstImgLayout.setVisibility(View.VISIBLE);
				currentBar = (ProgressBar) findViewById(R.id.progressBar);
        		currentProtv = (TextView) findViewById(R.id.currentProText);
				daTuImg.setImageBitmap(bitmap);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bitmap", bitmap);
				map.put("imgurl", currentFilePath);
				map.put("imageid", "");
				localListData.add(map);
				new Thread(uploadFileStart).start();
			}else if(msg.what == 14){//除第一张大图以外的两个gridview控件的图片的处理
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bitmap", bitmap);
				map.put("imgurl", currentFilePath);
				map.put("imageid", "");
				localListData.add(map);
				handlerGvData(true,localListData.size()-1);
			}else if(msg.what == 15){//选择了本地不存在的图片
				Toast.makeText(PhotoListActivity.this, "图片不存在", 3000).show();
			}else if(msg.what == 16){//拍照返回
				System.out.println("这次是第一张图片的显示");
				firstImgLayout.setVisibility(View.VISIBLE);
				currentBar = (ProgressBar) findViewById(R.id.progressBar);
        		currentProtv = (TextView) findViewById(R.id.currentProText);
				daTuImg.setImageBitmap(bitmap);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bitmap", bitmap);
				map.put("imgurl", currentFilePath);
				map.put("imageid", "");
				localListData.add(map);
				if (NetworkCheck.IsHaveInternet(PhotoListActivity.this)) {//检查是否有网络连接
					new Thread(uploadFileStart).start();
				}
			}else if(msg.what == 17){
				currentBar = (ProgressBar) findViewById(R.id.progressBar);
        		currentProtv = (TextView) findViewById(R.id.currentProText);
        		currentBar.setVisibility(View.GONE);
        		currentProtv.setVisibility(View.GONE);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("bitmap", bitmap);
				map.put("imgurl", currentFilePath);
				map.put("imageid", "");
				localListData.add(map);
				handlerGvData(true,localListData.size()-1);
			}else if(msg.what == 18){
				handlerGvData(true,currentUploadFile);//处理gridview中的数据
			}else if(msg.what == 19){
				((ImageView)msg.obj).setImageBitmap(bitmap);
			}
			super.handleMessage(msg);
		}
    	
    };
    /**
     * 续传线程
     */
    Runnable continueUploadRunnable = new Runnable() {
		@Override
		public void run() {
			continueUploadFile(currentFilePath);
		}
	};
	public void continueUploadFile(String filePath) {
    	ConnectivityManager connectMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info = connectMgr.getActiveNetworkInfo();
    	if(info!=null){
    		if(info.getType() == ConnectivityManager.TYPE_WIFI){//wifi网络
    			uploadMax = 50*1024;//一次上传文件的最大值  单位：字节
    		}else if(info.getType() == ConnectivityManager.TYPE_MOBILE){//手机网络
    			if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0//电信3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A//电信3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_B){//电信3G
    				uploadMax = 30*1024;//一次上传文件的最大值  单位：字节
    			}else if(info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE//联通3G
    			   || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA){//电信3G
    				uploadMax = 10*1024;//一次上传文件的最大值  单位：字节
    			}
    		}
    	}
    	System.out.println("目前每块上传的长度为："+uploadMax);
        try {
        	if(currentFileByte!=null){
        		currentFileByte = null;
        		System.gc();
        	}
            currentFileByte = CommonUtils.fileToByte(filePath);//将文件转成字节数组
            currentUploadPro = 0;
            currentPiece = 1;
            uploading = true;
            if(currentFileByte.length<=uploadMax){//当文件大小小于256KB时 直接一次性上传
            	continueUploadPiece(currentFileByte,filePath,currentFileId,1);
            }else{//如果文件的大小大于等于256KB 则分块上传  每一块的大小为256ＫＢ
            	continueUploadPiece(currentFileByte,filePath,currentFileId,1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Runnable findViewRunnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(2000);
				//这里的currentUploadFile下标为localListData中的下标
				if(currentUploadFile==0){//当续传的文件为第一张大图
					currentBar = (ProgressBar)findViewById(R.id.progressBar);
					currentProtv = (TextView)findViewById(R.id.currentProText);
				}else if(currentUploadFile>0 && currentUploadFile<5){//当续传的文件为第一个gridview中的图片
		        	currentBar = (ProgressBar) topGv.getChildAt(currentUploadFile-1).findViewById(R.id.progressBar);
					currentProtv = (TextView) topGv.getChildAt(currentUploadFile-1).findViewById(R.id.currentProText);
		        }else if(currentUploadFile >= 5){//当续传的文件为第二个gridview中的图片
		        	currentBar = (ProgressBar) bottomGv.getChildAt(currentUploadFile-5).findViewById(R.id.progressBar);
					currentProtv = (TextView) bottomGv.getChildAt(currentUploadFile-5).findViewById(R.id.currentProText);
		        }
				
				handler.sendEmptyMessage(11);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	/**
	 * 通过调用自带照相机获取图片
	 * @author 刘星星
	 */
	private void takePhoto(){
			dismiss();
//			Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			final Intent openCamera = new Intent();
			final Intent intent_camera = getPackageManager()
			.getLaunchIntentForPackage("com.android.camera");
			if (intent_camera != null) {
				openCamera.setPackage("com.android.camera");
			}
			openCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			fileName = CommonUtils.getFileName();
			File file = new File(ConstantsValues.SDCARD_ROOT_PATH + ConstantsValues.MYALBUM_IMG_URL);
			if(!file.exists()){
				file.mkdirs();
			}
			sdcardTempFile = new File(ConstantsValues.SDCARD_ROOT_PATH + ConstantsValues.MYALBUM_IMG_URL, fileName);
			path = sdcardTempFile.getAbsolutePath();
//			MyApplication.getInstance().setPhtotPath(path);
			if (CommonUtils.isHasSdcard()) {
				//图片输出大小
				openCamera.putExtra("outputX", 600);
				openCamera.putExtra("outputY", 300);
				openCamera.putExtra("scale", true);
				openCamera.putExtra("return-data", true);
//				openCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(sdcardTempFile));
				openCamera.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
				openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
//				openCamera.putExtra("return-data", true);
			}
			startActivityForResult(openCamera, 1);  
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
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
		selectPopupWindow.update();  
	}


	/**
	 * 网格布局控件监听器
	 * 
	 * @author qingfeng
	 * 
	 */
	class gvItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(PhotoListActivity.this,ImageSwitcher.class);
			intent.putExtra("photoList", (Serializable)localListData);
			intent.putExtra("localSDUrl", ConstantsValues.MYALBUM_IMG_URL);
			if(arg0 == topGv){
				intent.putExtra("index", arg2+1);
			}else if(arg0 == bottomGv){
				intent.putExtra("index", arg2+5);
			}
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}
	}
	
	/**
	 * 系统更新机制线程
	 */
	Runnable systemUpdateRunnable = new Runnable() {
		
		@Override
		public void run() {
			String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
					+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.ServerTime.getValue();
			List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
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
						JsonData.jsonUpdateTimeData(msgStr, dbHelper.open());//解析数据并将数据保存到数据库
						ContentValues values = new ContentValues();
						values.put("localtime", systemTimeStr);
						dbHelper.update(SystemUpdateInfo.TableName, values, "type=?", new String[]{UpdateType.ServerTime.getValue()});
//						String sql = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"'";
//						systemList = dbHelper.selectRow(sql, null);
//						System.out.println(systemList.size()+"");
//						handler.sendEmptyMessage(0);//处理UI
					}
				}
			
			handler.sendEmptyMessage(9);
		}
	};
	/***
	 * @author 刘星星
	 * @createDate 2013、9、18
	 * 获取照片id线程
	 */
	private Runnable getPhotoRunnable = new Runnable() {

		@Override
		public void run() {
				//检查系统更新机制表  看该用户的相册是否有更新
				String sqlTime = "select *from "+SystemUpdateInfo.TableName+" where storeid='"+UserBeanInfo.getInstant().getCurrentStoreId()
						+"' and userid = '"+UserBeanInfo.getInstant().getUserid()+"' and type = " + UpdateType.UserAlbum.getValue();
				List<Map<String,Object>> systemList = dbHelper.selectRow(sqlTime, null);
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
//				checkLocalUploadNoFinishImg();//检查是否有未上传成功的图片
		}
	};
	/**
	 * 访问服务器获取照片的ID 
	 * @param updateTime 最新更新时间
	 * @param localTime 上一次更新的时间
	 */
	private void getPhoto(String updateTime,String localTime){
		if (NetworkCheck.IsHaveInternet(PhotoListActivity.this)) {//判断网络状态
			String msgStr = RequestServerFromHttp.getUserPhoto(UserBeanInfo.getInstant().getUserName(), UserBeanInfo.getInstant().getPassword(), localTime);
			if(msgStr.equals("404")){
				
			}else if(JsonData.isNoData(msgStr)){
				
			}else if(msgStr.startsWith("[") && msgStr.length()>3){
				String sql = "select *from "+UserPhotoInfo.TableName+" where username="+UserBeanInfo.getInstant().getUserName();
				JsonData.jsonUserPhotos(msgStr, dbHelper.open(),UserBeanInfo.getInstant().getUserName());
				handler.sendEmptyMessage(1);
			}
		}else{//没有网络
			
		}
	}
	/**
	 * 检查本地上传未完成的图片
	 */
	private void checkLocalUploadNoFinishImg(){
		String sql = "select *from "+UpLoadFileInfo.TableName+ " where progress != '1'";
		List<Map<String,Object>> list = dbHelper.selectRow(sql, null);
		System.out.println(list.size()+"");
		boolean isHaveEffectiveData = false;//标记是否有有效的数据
//		continueUploadStartLocation = 0;
		currentFileId = "";
		for(int i=0;i<list.size();i++){
			File file = new File(list.get(i).get("localpath").toString());	
			if(file!=null && file.exists()){
				int w = MyApplication.getInstant().getWidthPixels()/2;
				Bitmap bitmap = CommonUtils.getDrawable(list.get(i).get("localpath").toString(), w);
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("bitmap", bitmap);
				map.put("imgurl", list.get(i).get("localpath"));
				map.put("imageid", list.get(i).get("imageid"));
				map.put("progress", list.get(i).get("progress"));
				localListData.add(map);
				isHaveEffectiveData = true;
				if(currentFileId.equals("")){
//					continueUploadStartLocation = Integer.parseInt(list.get(i).get("successlocation").toString());
					currentFileId = list.get(i).get("uploadid").toString();
				}
			}else{//如果本地路径对应的文件不存在，则删除掉数据库中的该条数据
				dbHelper.delete(UpLoadFileInfo.TableName, "localpath=?", new String[]{list.get(i).get("localpath").toString()});
			}
		}
		if(isHaveEffectiveData){//如果有有效的需要上传的图片
			//这里的currentUploadFile下标为localListData中的下标
			if(list1.size()>0){//已经上传成功的图片张数大于1的时候
				currentFilePath = localListData.get(list1.size()+list2.size()+1).get("imgurl").toString();
				currentUploadFile = list1.size()+list2.size()+1;
				currentUploadPro = Float.parseFloat(localListData.get(currentUploadFile).get("progress").toString());
			}else if(firstImgLayout.getVisibility() == View.GONE){//已经上传成功的图片张数等于0的时候
				currentFilePath = localListData.get(0).get("imgurl").toString();
				currentUploadFile = 0;
				currentUploadPro = Float.parseFloat(localListData.get(currentUploadFile).get("progress").toString());
			}else if(list1.size()==0 && firstImgLayout.getVisibility() == View.VISIBLE){//已经上传成功的图片张数等于1的时候
				currentFilePath = localListData.get(1).get("imgurl").toString();
				currentUploadFile = 1;
				currentUploadPro = Float.parseFloat(localListData.get(currentUploadFile).get("progress").toString());
			}
			Message msg = new Message();
			msg.what = 10;
			handler.sendMessage(msg);
		}else{
			handler.sendEmptyMessage(12);
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
	public void downImage(ImageView imageView,String imgid,String image_type,String download_type,String width,String height){
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
				Message msg = new Message();
				msg.obj = imageView;
				msg.what = 19;
				handler.sendMessage(msg);
				File file = new File(ConstantsValues.SDCARD_ROOT_PATH+ConstantsValues.MyALBUM_IMG_URL_THUMB+imgid+".png");
				boolean b = OutPutImage(file,bitmap);
				System.out.println("图片存储========================"+(b?"成功":"失败"));
				if(b){
					ContentValues values = new ContentValues();
					values.put("imgurl", file.getAbsolutePath());
					values.put("isUpload", "1");
					dbHelper.update(UserPhotoInfo.TableName, values, "imageid=? and username=?", new String[]{imgid,UserBeanInfo.getInstant().getUserName()});
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
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			fos.flush();
			return true;
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
