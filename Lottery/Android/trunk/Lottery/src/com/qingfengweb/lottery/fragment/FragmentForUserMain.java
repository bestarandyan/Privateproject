package com.qingfengweb.lottery.fragment;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.FileUpLoad.HttpPostUploadFile;
import com.qingfengweb.imagehandle.PicHandler;
import com.qingfengweb.lottery.R;
import com.qingfengweb.lottery.activity.AmendMyInfoActivity;
import com.qingfengweb.lottery.activity.AmendPasswordActivity;
import com.qingfengweb.lottery.activity.ChargeHistoryActivity;
import com.qingfengweb.lottery.activity.ChargeMoneyActivity;
import com.qingfengweb.lottery.activity.KuaiSanActivity;
import com.qingfengweb.lottery.activity.LoginActivity;
import com.qingfengweb.lottery.activity.MainActivity;
import com.qingfengweb.lottery.activity.RegisterActivity;
import com.qingfengweb.lottery.bean.UserBean;
import com.qingfengweb.lottery.data.DBHelper;
import com.qingfengweb.lottery.data.JsonData;
import com.qingfengweb.lottery.data.MyApplication;
import com.qingfengweb.lottery.data.RequestServerFromHttp;
import com.qingfengweb.lottery.util.DeviceTool;
import com.qingfengweb.lottery.util.NetworkCheck;
@SuppressLint("HandlerLeak")
public class FragmentForUserMain  extends Fragment implements OnClickListener{
	private static final String ARG_TEXT = "com.qingfengweb.lottery.fragment";
	 View view = null;
	 RelativeLayout userLayout;
	 DBHelper dbHelper = null;
	 private TextView userNameTv;
	 private PopupWindow selectPopupWindow = null;
	 private RelativeLayout parent;
	 ImageView headImage;
	 TextView registerTv,forgetTv,cancleLoginTv;
	 ImageView userJianTou;//箭头
	 private boolean photo_tag = false;// 是否已经获取了证件图片
//		public boolean initWedget_tag = true;
		public String path = "";// 照片路径
		public String oldPath = "";//保存拍照之前的路径
		public String fileName = "";//图片名字
		private Button shareBtn1, shareBtn2,shareBtn3, cancleBtn;// 分享功能的对应四个按钮
		public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
		public static String SAVE_PATH_IN_SDCARD = "/656cai/"; //图片及其他数据保存文件夹   
		private File sdcardTempFile = null;
		private final String IMAGE_TYPE = "image/*";
		private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
		public final static int REQUEST_CODE_TAKE_PICTURE = 12;//设置拍照操作的标志  
		public final static int REQUEST_CODE_INFO_SET = 1;//设置文本信息的返回值标记
		public static final int PHOTORESOULT = 3;
		private int crop = 200;
		public TextView versionTv;
	 public static FragmentForUserMain newInstance(String text) {
		FragmentForUserMain f = new FragmentForUserMain();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        f.setArguments(args);

        return f;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = LayoutInflater.from(getActivity()).inflate(R.layout.f_user, null);
		findView();
		initData();
		if(NetworkCheck.IsHaveInternet(getActivity())){
			new Thread(loginRunnable).start();
		}
	}
	/**
	 * 登陆接口
	 */
Runnable loginRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.loginUser(MyApplication.getInstance().getCurrentUserName(), 
					MyApplication.getInstance().getCurrentPassword());
			System.out.println("登陆用户返回值--------------------------"+msg);
			if(msg.startsWith("{") && msg.length()>3){//登陆成功
				JsonData.jsonRegisterUserData(msg, dbHelper.open());
			}else if(msg.equals("404")){//访问接口失败
			}else if(msg.equals("-5")){//用户不存在
			}else if(msg.equals("-901")){//系统错误
			}else if(msg.equals("-12")){//密码输入有误
			}
		}
	};
	
	/**
	 * 查询余额
	 */
	Runnable getMoneyRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = RequestServerFromHttp.queryMoney();
			if(msg.startsWith("{") && msg.length()>3){//查询成功
				JsonData.jsonBalanceData(msg, dbHelper.open(),handler);
				handler.sendEmptyMessage(3);
			}else if(msg.equals("-102") || msg.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}else{
				
			}
		}
	};
	private void findView(){
		userLayout = (RelativeLayout) view.findViewById(R.id.loginLayout);
		userLayout.setOnClickListener(this);
		registerTv = (TextView) view.findViewById(R.id.registerTv);
		forgetTv = (TextView) view.findViewById(R.id.forgetTv);
		cancleLoginTv = (TextView) view.findViewById(R.id.cancleLoginTv);
		userNameTv = (TextView) view.findViewById(R.id.userNameTv);
		registerTv.setOnClickListener(this);
		forgetTv.setOnClickListener(this);
		cancleLoginTv.setOnClickListener(this);
		view.findViewById(R.id.myDingdan).setOnClickListener(this);
		view.findViewById(R.id.amendpassword).setOnClickListener(this);
		view.findViewById(R.id.zhanghuchongzhi).setOnClickListener(this);
		view.findViewById(R.id.historychongzhi).setOnClickListener(this);
		parent = (RelativeLayout) view.findViewById(R.id.parent);
		headImage = (ImageView) view.findViewById(R.id.userHead);
		headImage.setOnClickListener(this);
		view.findViewById(R.id.amendziliao).setOnClickListener(this);
		userJianTou = (ImageView) view.findViewById(R.id.userJianTou);
		versionTv = (TextView) view.findViewById(R.id.versionTv);
		try {
			String  version = DeviceTool.getVersionName(getActivity());
			if(version!=null && version.length()>0){
				versionTv.setText("版本号:V"+version);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initData(){
		dbHelper = DBHelper.getInstance(getActivity());
		String sql = "select * from "+UserBean.tbName +" where islastuser=1";
		if(MainActivity.userList!=null){
			MainActivity.userList.clear();
		}
		MainActivity.userList = dbHelper.selectRow(sql, null);
		if(MainActivity.userList!=null && MainActivity.userList.size()>0){
			MainActivity.userMap = MainActivity.userList.get(0);
		}else{
			MainActivity.userMap = null;
		}
		if(MainActivity.userMap!=null){//有已经登陆过的用户
			userNameTv.setText(MainActivity.userMap.get("username").toString());
			MyApplication.getInstance().setCurrentToken(MainActivity.userMap.get("token").toString());
			MyApplication.getInstance().setCurrentUserName(MainActivity.userMap.get("username").toString());
			MyApplication.getInstance().setCurrentPassword(MainActivity.userMap.get("password").toString());
			if(MainActivity.userMap.get("headimg_localurl")!=null && MainActivity.userMap.get("headimg_localurl").toString().length()>0){
				String imgLocalUrl = MainActivity.userMap.get("headimg_localurl").toString().trim();
				sdcardTempFile = new File(imgLocalUrl);
				if(sdcardTempFile!=null && sdcardTempFile.exists()){
					bm = BitmapFactory.decodeFile(imgLocalUrl);
					if(bm!=null){
						headImage.setImageBitmap(bm);
					}else{
						headImage.setImageBitmap(null);
					}
				}
			}else{
				headImage.setImageBitmap(null);
			}
			forgetTv.setVisibility(View.GONE);
			registerTv.setVisibility(View.GONE);
			cancleLoginTv.setVisibility(View.VISIBLE);
			userNameTv.setClickable(true);
			userJianTou.setVisibility(View.GONE);
			userJianTou.setClickable(false);
		}else{//没有登陆过的用户
			MyApplication.getInstance().setCurrentToken("");
			MyApplication.getInstance().setCurrentUserName("");
			MyApplication.getInstance().setCurrentPassword("");
			forgetTv.setVisibility(View.GONE);
			registerTv.setVisibility(View.VISIBLE);
			cancleLoginTv.setVisibility(View.GONE);
			userNameTv.setText("登陆账号");
			headImage.setImageBitmap(null);
			userNameTv.setClickable(false);
			userJianTou.setVisibility(View.VISIBLE);
			userJianTou.setClickable(true);
		}
	}
	@Override
	public void onResume() {
		System.out.println("userMain----------------------onResume");
		initData();
//		if(sdcardTempFile!=null){
//			sdcardTempFile = MyApplication.getInstance().getCurrentHeadImgFile();
//			Bitmap bitmap = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
//			if(bitmap!=null){
//				headImage.setImageBitmap(bitmap);
//			}
//		}
		
		super.onResume();
	}
	/**
	 * 检查是否登陆过
	 */
	public boolean checkLogin(){
		String username = MyApplication.getInstance().getCurrentUserName();
		String token = MyApplication.getInstance().getCurrentToken();
		if(username==null || username.equals("") || token == null || token.equals("")){//表示没有登陆
			return false;
		}else{
			return true;
		}
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
		// PopupWindow浮动下拉框布局
				View layout = (View) this.getActivity().getLayoutInflater().inflate(R.layout.menu_myalbum, null);
				layout.findViewById(R.id.btn1).setOnClickListener(this);
				layout.findViewById(R.id.btn2).setOnClickListener(this);
				Button btn3 = (Button) layout.findViewById(R.id.btn3);
				btn3.setOnClickListener(this);
				if(sdcardTempFile!=null && sdcardTempFile.exists()){
					btn3.setVisibility(View.VISIBLE);
				}else{
					btn3.setVisibility(View.GONE);
				}
				layout.findViewById(R.id.cancleBtn).setOnClickListener(this);
				selectPopupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT, true);
				// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
				// 没有这一句则效果不能出来，但并不会影响背景
				selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.dialog_border));
				selectPopupWindow.setOutsideTouchable(false);
				selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
				selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, 0);  
				selectPopupWindow.update();  
	}
	@Override
	public void onClick(View v) {
		if(v == userLayout){//用户登陆入口
			Intent intent = new Intent(this.getActivity(),LoginActivity.class);
			this.getActivity().startActivity(intent);
		}else if(v.getId() == R.id.myDingdan){//我的订单
			if(checkLogin()){
				Message message = new Message();
				message.arg1 = 2;
				message.what = 1;
				SampleListFragment.handler.sendMessage(message);
			}else{
				Intent intent = new Intent(this.getActivity(),LoginActivity.class);
				intent.putExtra("activity", "FragmentForMyLottery");
				startActivity(intent);
			}
		}else if(v.getId() == R.id.amendpassword){//修改密码
			if(checkLogin()){
				Intent intent = new Intent(this.getActivity(),AmendPasswordActivity.class);
				this.getActivity().startActivity(intent);
			}else{
				Intent intent = new Intent(this.getActivity(),LoginActivity.class);
				intent.putExtra("activity", "AmendPasswordActivity");
				startActivity(intent);
			}
		}else if(v.getId() == R.id.zhanghuchongzhi){//账户充值
			if(checkLogin()){
				Intent intent = new Intent(this.getActivity(),ChargeMoneyActivity.class);
				this.getActivity().startActivity(intent);
			}else{
				Intent intent = new Intent(this.getActivity(),LoginActivity.class);
				intent.putExtra("activity", "ChargeMoneyActivity");
				startActivity(intent);
			}
		}else if(v.getId() == R.id.historychongzhi){//充值历史
			if(checkLogin()){
				Intent intent = new Intent(this.getActivity(),ChargeHistoryActivity.class);
				this.getActivity().startActivity(intent);
			}else{
				Intent intent = new Intent(this.getActivity(),LoginActivity.class);
				intent.putExtra("activity", "ChargeHistoryActivity");
				startActivity(intent);
			}
		} else if(v == headImage){
//			popupWindwShowing();
		}else if(v.getId() == R.id.btn1){
				dismiss();
				Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				 fileName = PicHandler.getFileName();
				File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
				if(!file.exists()){
					file.mkdirs();
				}
				sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
//				MyApplication.getInstance().setCurrentHeadImgFile(sdcardTempFile);
				if(PicHandler.isHasSdcard()){
					/*openCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDCARD_ROOT_PATH+  
	                        SAVE_PATH_IN_SDCARD,fileName)));  */
					openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
				/*	openCamera.putExtra("crop", "true");
					openCamera.putExtra("aspectX", 1);// 裁剪框比例
					openCamera.putExtra("aspectY", 1);
					openCamera.putExtra("outputX", crop);// 输出图片大小
					openCamera.putExtra("outputY", crop);*/
					openCamera.putExtra("return-data", true);
				startActivityForResult(openCamera, REQUEST_CODE_TAKE_PICTURE);  
				}
		}else if(v.getId() == R.id.btn2){
			dismiss();
			Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
			getAlbum.setType(IMAGE_TYPE);
			fileName = PicHandler.getFileName();
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if(!file.exists()){
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
			getAlbum.putExtra("output", Uri.fromFile(sdcardTempFile));
			getAlbum.putExtra("crop", "true");
			getAlbum.putExtra("aspectX", 1);// 裁剪框比例
			getAlbum.putExtra("aspectY", 1);
			getAlbum.putExtra("outputX", crop);// 输出图片大小
			getAlbum.putExtra("outputY", crop);
			startActivityForResult(getAlbum, IMAGE_CODE);
		}else if(v.getId() == R.id.btn3){//图片预览
			dismiss();
			showDialog();
		}else if(v.getId() == R.id.cancleBtn){//取消获取图片按钮
			dismiss();
		}else if(v.getId() == R.id.amendziliao){//修改资料
			if(checkLogin()){
				Intent intent = new Intent(this.getActivity(),AmendMyInfoActivity.class);
				this.getActivity().startActivity(intent);
			}else{
				Intent intent = new Intent(this.getActivity(),LoginActivity.class);
				intent.putExtra("activity", "AmendMyInfoActivity");
				startActivity(intent);
			}
		}else if(v == forgetTv){//忘记密码
			
		}else if(v == registerTv){//注册用户
			Intent intent = new Intent(this.getActivity(),RegisterActivity.class);
			this.getActivity().startActivity(intent);
		}else if(v == cancleLoginTv){//注销登陆
			AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
			alert.setTitle("提示!");
			alert.setMessage("确定要退出登陆吗？");
			alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ContentValues values = new ContentValues();
					values.put("islastuser", "0");
					boolean isSuccess = dbHelper.update(UserBean.tbName, values, "username=?", new String[]{MyApplication.getInstance().getCurrentUserName()});
					if(isSuccess){
						Intent intent = new Intent(FragmentForUserMain.this.getActivity(),LoginActivity.class);
						intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
						startActivity(intent);
					}
				}
			});
			alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			Dialog dialog = alert.create();
			dialog.show();
			
		}
	}
	private void showDialog(){
		Dialog dialog = new Dialog(this.getActivity(),R.style.FullHeightDialog);
		View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.dialog_image, null);
		LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(bm.getWidth(), bm.getHeight());
		view.setLayoutParams(param1);
		ImageView image = (ImageView) view.findViewById(R.id.imageView);
		image.setImageBitmap(BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath()));
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = 300; // 宽度
        lp.height = 300; // 高度
        lp.alpha = 1.0f; // 透明度
        lp.dimAmount=0.91f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogWindow.setAttributes(lp);
		dialog.show();
	}
	private Bitmap bm = null;
	/**
	 * 上传头像
	 */
	public void upLoadHeadImg(){
		byte[] currentFileByte = PicHandler.fileToByte(sdcardTempFile.getAbsolutePath());//将文件转成字节数组
		Map<String, String> params = new HashMap<String, String>();
        params.put("appkey", RequestServerFromHttp.APPKEY);
        params.put("token", MyApplication.getInstance().getCurrentToken());
        params.put("name", sdcardTempFile.getName());
//        params.put("filename", sdcardTempFile.getName());
//        params.put("file", sdcardTempFile.getName());
        Map<String, File> files  = new HashMap<String, File>();
        files.put(sdcardTempFile.getName(), sdcardTempFile);
        try {
			String msgStr =  HttpPostUploadFile.post(this.getActivity(),RequestServerFromHttp.INTERFACE_UPLOAD_HEADIMG, params, files, 0, currentFileByte.length,"file");
			if(msgStr.equals("-102") || msgStr.equals("-101") ){//token超时
				handler.sendEmptyMessage(102);
			}
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        uploadFile(RequestServerFromHttp.INTERFACE_UPLOAD_HEADIMG);
//		String msg = HttpPostUploadFile.uploadImg(RequestServerFromHttp.INTERFACE_UPLOAD_HEADIMG, sdcardTempFile.getAbsolutePath());
	}
	Handler handler  = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 102){//token超时 或者错误
				Toast.makeText(FragmentForUserMain.this.getActivity(), getString(R.string.again_login_str), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(FragmentForUserMain.this.getActivity(),LoginActivity.class);
				intent.putExtra("username", MyApplication.getInstance().getCurrentUserName());
				startActivity(intent);
			}
			super.handleMessage(msg);
		}
		
	};
	Runnable uploadHeadImgRunnable = new Runnable() {
		
		@Override
		public void run() {
			upLoadHeadImg();
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0)
			return;
		if (resultCode != this.getActivity().RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			Log.e("", "ActivityResult resultCode error");
			return;
		}
		if(bm!=null){
			bm.recycle();
		}
		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {//选择图片返回
//				path = sdcardTempFile.getAbsolutePath();
//				bm = BitmapFactory.decodeFile(path);
				if(sdcardTempFile.exists()){
					photo_tag = true;
//					headImage.setImageBitmap(bm);
//					new Thread(runnable).start();//修改单个用户信息。
					updateUserHeadImgLocalUrl();
				}else{
					Toast.makeText(this.getActivity(), "图片不存在", 3000).show();
				}
				
		}else if(requestCode == REQUEST_CODE_TAKE_PICTURE){//拍照返回   
            if(PicHandler.isHasSdcard()){//存储卡可用   
//            	sdcardTempFile = MyApplication.getInstance().getCurrentHeadImgFile();
//            	bm = PicHandler.getDrawable(sdcardTempFile.getAbsolutePath(),null);
// 				PicHandler.OutPutImage(sdcardTempFile,bm);
            	startPhotoZoom(Uri.fromFile(sdcardTempFile));
//            	new Thread(runnable).start();//修改单个用户信息。
            	photo_tag = true;
            }else{  //存储卡不可用直接返回缩略图    
	            Bundle extras = data.getExtras();   
	            bm = (Bitmap) extras.get("data");  
	            photo_tag = true;
				headImage.setImageBitmap(bm);
            }         
        }else if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				bm = extras.getParcelable("data");
				if(sdcardTempFile.exists()){
					sdcardTempFile.delete();
				}
				PicHandler.OutPutImage(sdcardTempFile,bm);
//				bm = PicHandler.scaleImg(bm, 300, 300);
		        headImage.setImageBitmap(bm);
//		        sdcardTempFile = MyApplication.getInstance().getCurrentHeadImgFile();
// 				MyApplication.getInstance().setCurrentHeadImgFile(sdcardTempFile);
 				updateUserHeadImgLocalUrl();
 				if(sdcardTempFile!=null && sdcardTempFile.exists()){
 					new Thread(uploadHeadImgRunnable).start();
 				}
			}
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 更新用户表中用户头像的本地路径
	 */
	public void updateUserHeadImgLocalUrl(){
		ContentValues values = new ContentValues();
		values.put("headimg_localurl", sdcardTempFile.getAbsolutePath());
		MainActivity.userMap.put("headimg_localurl", sdcardTempFile.getAbsolutePath());
		dbHelper.update(UserBean.tbName, values, "username=?", new String[]{MyApplication.getInstance().getCurrentUserName()});
	}
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_TYPE);
		intent.putExtra("output", uri);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", crop);
		intent.putExtra("outputY", crop);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}
	
	  /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
	  private void uploadFile(String uploadUrl){
	    String end = "\r\n";
	    String twoHyphens = "--";
	    String boundary = "******";
	    try
	    {
	      URL url = new URL(uploadUrl);
	      HttpURLConnection httpURLConnection = (HttpURLConnection) url
	          .openConnection();
	      // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
	      // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
	      httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
	      // 允许输入输出流
	      httpURLConnection.setDoInput(true);
	      httpURLConnection.setDoOutput(true);
	      httpURLConnection.setUseCaches(false);
	      // 使用POST方法
	      httpURLConnection.setRequestMethod("POST");
	      httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
	      httpURLConnection.setRequestProperty("Charset", "UTF-8");
	      httpURLConnection.setRequestProperty("Content-Type",
	          "multipart/form-data;boundary=" + boundary);

	      DataOutputStream dos = new DataOutputStream(
	          httpURLConnection.getOutputStream());
	      dos.writeBytes(twoHyphens + boundary + end);
	      dos.writeBytes("Content-Disposition: form-data; appkey=\""+RequestServerFromHttp.APPKEY+"\"; token=\""
	          + MyApplication.getInstance().getCurrentToken()
	          + "\";file=\""+sdcardTempFile.getName()+"\""
	          + end);
	      dos.writeBytes(end);
	      FileInputStream fis = new FileInputStream(sdcardTempFile);
	      byte[] buffer = new byte[8192]; // 8k
	      int count = 0;
	      // 读取文件
	      while ((count = fis.read(buffer)) != -1)
	      {
	        dos.write(buffer, 0, count);
	      }
	      fis.close();

	      dos.writeBytes(end);
	      dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
	      dos.flush();

	      InputStream is = httpURLConnection.getInputStream();
	      InputStreamReader isr = new InputStreamReader(is, "utf-8");
	      BufferedReader br = new BufferedReader(isr);
	      String result = br.readLine();

	      Toast.makeText(FragmentForUserMain.this.getActivity(), result, Toast.LENGTH_LONG).show();
	      dos.close();
	      is.close();

	    } catch (Exception e){
	      e.printStackTrace();
	    }
	  }
}
