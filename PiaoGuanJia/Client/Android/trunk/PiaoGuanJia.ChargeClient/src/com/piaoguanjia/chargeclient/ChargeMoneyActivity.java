/**
 * 
 */
package com.piaoguanjia.chargeclient;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * @author 刘星星
 * @CreateDate 2013、5、6
 * 充值界面
 */
public class ChargeMoneyActivity extends Activity implements OnClickListener,OnTouchListener{
	private Button chargeBtn1,chargeBtn2;//账户充值   专用账户充值
	private ImageView backBtn;//返回按钮
	private EditText userNameEt;//用户名
	private EditText chargeMoney;//充值金额
	private ImageView getPhotoIv;//获取照片
	private ImageView photoIv;//照片
	private Button preViewPhoto,deletePhoto;//预览照片   删除照片
	private EditText remarks;//备注
	private Button submitBtn,cancleBtn;//提交按钮 取消按钮
	private EditText chargeProduce;//充值产品
	private RelativeLayout photoLayout;///图片布局
	private LinearLayout produceLayout;//产品布局
	private LinearLayout parent = null;
	private Button shareBtn1, shareBtn2,dialogCancleBtn;// 
	private PopupWindow selectPopupWindow = null;
	private final String IMAGE_TYPE = "image/*";
	public int chazhi = 0;//底部弹出框的位置
	private boolean photo_tag = false;// 是否已经获取了证件图片
	private String fileName = "";
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//路径   
	public static String SAVE_PATH_IN_SDCARD = "/chargeMoney"; //图片及其他数据保存文件夹   
	private File sdcardTempFile = null;
	private String path = "";//图片的最终路径
	private Bitmap bm = null;//最终得到的缩略图
	private int chargeType = 1;//充值类型 1为普通用户充值  2为专用账户充值
	public ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_chargemoney);
		findView();
		initView();
	}
	@Override
	protected void onResume() {
		path = MyApplication.getInstance().getPhtotPath();
		if(bm == null && !path.equals("")){
			bm = CommonUtils.getDrawable(path);
			photoLayout.setVisibility(View.VISIBLE);
			photoIv.setImageBitmap(bm);
		}
		chargeType = MyApplication.getInstance().getCurrentChargeType();
		if(chargeType == 2){
			chargeBtn2.setBackgroundResource(R.drawable.chargebg);
			chargeBtn2.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			chargeBtn1.setBackgroundColor(Color.TRANSPARENT);
			chargeBtn1.setTextColor(Color.WHITE);
			produceLayout.setVisibility(View.VISIBLE);
		}
		super.onResume();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(bm!=null){
			photoLayout.setVisibility(View.VISIBLE);
			photoIv.setImageBitmap(bm);
		}
		super.onConfigurationChanged(newConfig);
	}
	private void findView(){
		chargeBtn1 = (Button) findViewById(R.id.button1);
		chargeBtn2 = (Button) findViewById(R.id.button2);
		backBtn = (ImageView) findViewById(R.id.backBtn);
		userNameEt = (EditText) findViewById(R.id.userName);
		chargeMoney = (EditText) findViewById(R.id.chargeMoney);
		getPhotoIv = (ImageView) findViewById(R.id.imageView1);
		photoIv = (ImageView) findViewById(R.id.photoIv);
		preViewPhoto = (Button) findViewById(R.id.previewPhotoBtn);
		deletePhoto = (Button) findViewById(R.id.deletePhotoBtn);
		remarks = (EditText) findViewById(R.id.remarksEt);
		chargeProduce = (EditText) findViewById(R.id.chargeProduce);
		submitBtn = (Button) findViewById(R.id.chargeBtn1);
		cancleBtn = (Button) findViewById(R.id.chargeBtn2);
		photoLayout = (RelativeLayout) findViewById(R.id.photoLayout);
		produceLayout = (LinearLayout) findViewById(R.id.chargeProduceLayout);
		produceLayout.setVisibility(View.GONE);
	}
	private void initView(){
		chargeBtn1.setOnClickListener(this);
		chargeBtn2.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		getPhotoIv.setOnClickListener(this);
		preViewPhoto.setOnClickListener(this);
		deletePhoto.setOnClickListener(this);
		submitBtn.setOnTouchListener(this);
		cancleBtn.setOnTouchListener(this);
		preViewPhoto.setOnTouchListener(this);
		deletePhoto.setOnTouchListener(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		if(v == chargeBtn1){//账户充值
			chargeBtn1.setBackgroundResource(R.drawable.chargebg);
			chargeBtn1.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			chargeBtn2.setBackgroundColor(Color.TRANSPARENT);
			chargeBtn2.setTextColor(Color.WHITE);
			produceLayout.setVisibility(View.GONE);
			chargeType = 1;
			MyApplication.getInstance().setCurrentChargeType(chargeType);
		}else if(v == chargeBtn2){//专用账户充值
			chargeBtn2.setBackgroundResource(R.drawable.chargebg);
			chargeBtn2.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			chargeBtn1.setBackgroundColor(Color.TRANSPARENT);
			chargeBtn1.setTextColor(Color.WHITE);
			produceLayout.setVisibility(View.VISIBLE);
			chargeType = 2;
			MyApplication.getInstance().setCurrentChargeType(chargeType);
		}else if(v == backBtn){//返回按钮
			finish();
		}else if(v == getPhotoIv){//获取照片
			CommonUtils.hideSoftKeyboard(this);
			initPopuWindow();
			popupWindwShowing();
		}else if(v == shareBtn1){//拍照获取图片
			takePhoto();//
		}else if(v == shareBtn2){//导入图片
			getAlbumPhoto();
		}else if(v == dialogCancleBtn){//取消获取图片
			
		}
		
	}
	
	/**
	 * 删除照片时的提示函数 
	 * @author 刘星星
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("提示")
					.setMessage("确定删除照片？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									File file = new File(path);
									if(file.exists()){
										file.delete();
									}
									path = "";
									bm.recycle();
									photoIv.setImageBitmap(null);
									photoLayout.setVisibility(View.GONE);
								}
							}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  callDailog.show();
	}
	/**
	 * 通过调用自带照相机获取图片
	 * @author 刘星星
	 */
	private void takePhoto(){
			dismiss();
			Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			 fileName = CommonUtils.getFileName();
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if(!file.exists()){
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
			path = sdcardTempFile.getAbsolutePath();
			MyApplication.getInstance().setPhtotPath(path);
			if (CommonUtils.isHasSdcard()) {
				openCamera.putExtra("output", Uri.fromFile(sdcardTempFile));
				openCamera.putExtra("return-data", true);
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
		fileName = CommonUtils.getFileName();
		File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
		if(!file.exists()){
			file.mkdirs();
		}
		sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD, fileName);
		path = sdcardTempFile.getAbsolutePath();
		MyApplication.getInstance().setPhtotPath(path);
		if (CommonUtils.isHasSdcard()) {
			getAlbum.putExtra("output", Uri.fromFile(sdcardTempFile));
			getAlbum.putExtra("return-data", true);
		}
		startActivityForResult(getAlbum, 2);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 0)
			return;
		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量
			return;
		}
		if (requestCode == 1) {// 拍照返回
			fileName = CommonUtils.getFileName();// 给图片起新的名字
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if (!file.exists()) {
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD,
					fileName);
			if (CommonUtils.isHasSdcard()) {// 存储卡可用
				path = MyApplication.getInstance().getPhtotPath();
				Bitmap bitmap = CommonUtils.getDrawable(path, MyApplication.getInstance().getScreenW(), 
						MyApplication.getInstance().getScreenH());
				bitmap = CommonUtils.RotateImg(path,bitmap);//倒立的图片旋转90°
				boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
				if (copy) {
					File oldFile = new File(path);
					oldFile.delete();
					path = sdcardTempFile.getAbsolutePath();
					MyApplication.getInstance().setPhtotPath(path);
				}
				bm = CommonUtils.getDrawable(path);
			} else { // 存储卡不可用直接返回缩略图
				Toast.makeText(this, "没有SD卡", 3000).show();
			}
			if(bm!=null){
				photoLayout.setVisibility(View.VISIBLE);
				photoIv.setImageBitmap(bm);
			}
			
		}else if (requestCode == 2) {// 选择图片返回
			String imgPath = "";
			Uri originalUri = data.getData(); // 获得图片的uri
			String[] proj = { MediaStore.Images.Media.DATA };
			// 好像是android多媒体数据库的封装接口，具体的看Android文档
			Cursor cursor = managedQuery(originalUri, proj, null, null, null);
			// 按我个人理解 这个是获得用户选择的图片的索引值
			if (cursor == null) {
				imgPath = data.getDataString();
			} else {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
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
				File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
				if (!file.exists()) {
					file.mkdirs();
				}
				sdcardTempFile = new File(SDCARD_ROOT_PATH
						+ SAVE_PATH_IN_SDCARD, fileName);
//				path = sdcardTempFile.getAbsolutePath();
//				FileCopyUtil.copyfile(fromFile, sdcardTempFile, true);
				Bitmap bitmap = CommonUtils.getDrawable(fromFile.getAbsolutePath(), MyApplication.getInstance().getScreenW(), 
						MyApplication.getInstance().getScreenH());
				bitmap = CommonUtils.RotateImg(fromFile.getAbsolutePath(),bitmap);//倒立的图片旋转90°
				if(bitmap!=null){
					boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
					if (copy) {
						/*File oldFile = new File(path);
						oldFile.delete();*/
						path = sdcardTempFile.getAbsolutePath();
						MyApplication.getInstance().setPhtotPath(path);
					}
				}else{
					Toast.makeText(this, "图片不存在", 3000).show();
				}
			}
			bm = CommonUtils.getDrawable(path);
			if (bm != null) {// 图片存在
				photoLayout.setVisibility(View.VISIBLE);
				photoIv.setImageBitmap(bm);
			} else {// 图片不存在
				Toast.makeText(this, "图片不存在", 3000).show();
			}
		} 
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {
		// PopupWindow浮动下拉框布局
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.dialog_share, null);
		shareBtn1 = (Button) loginwindow.findViewById(R.id.eshareBtn1);
		shareBtn2 = (Button) loginwindow.findViewById(R.id.eshareBtn2);
		shareBtn1.setText("拍照获取");
		shareBtn2.setText("从手机导入");
		dialogCancleBtn = (Button) loginwindow.findViewById(R.id.eshareCancleBtn);
		shareBtn1.setOnClickListener(this);
		shareBtn2.setOnClickListener(this);
		dialogCancleBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		selectPopupWindow = new PopupWindow(loginwindow, parent.getWidth(),
				LayoutParams.WRAP_CONTENT, true);
		
		selectPopupWindow.setAnimationStyle(R.style.AnimationActivity);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.dialog_border));
		selectPopupWindow.setOutsideTouchable(true);
//		selectPopupWindow.setOnDismissListener(new OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				dismiss();
//			}
//		});
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
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, -(parent.getHeight() - chazhi));  
//		selectPopupWindow
//				.showAsDropDown(parent, 0, -(parent.getHeight() - chazhi));
		selectPopupWindow.update();  
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Button tv = (Button) v;
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			tv.setTextColor(Color.BLACK);
			tv.setBackgroundResource(R.drawable.cz_charge_button);
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			tv.setTextColor(Color.WHITE);
			tv.setBackgroundResource(R.drawable.cz_charge_button1);
			if(v == submitBtn){//确认充值
				if(userNameEt.getText().toString().trim().length()<=0){
					dialogWronFun("请输入要充值的用户名！", this);
					return false;
				}
				if(chargeType == 2 && chargeProduce.getText().toString().trim().length()<=0){
					dialogWronFun("请输入要充值产品的编号！", this);
					return false;
				}
				String m = chargeMoney.getText().toString().trim();
				if(m.length()<=0){
					dialogWronFun("请输入充值金额！", this);
					return false;
				}else if(!Constant.isNumber(m)){
					dialogWronFun("请输入有效的金额值！", this);
					return false;
				}
					createProgress();
					progress.setMessage("正在充值，请稍候！");
					new Thread(chargeRunnable).start();
			}else if(v == cancleBtn){//取消充值
				finish();
			}else if(v == preViewPhoto){//预览照片
				Intent intent = new Intent(this,ImageSwitcher.class);
				intent.putExtra("path", path);
				startActivity(intent);
			}else if(v == deletePhoto){//删除图片
				showExitDialog();//提示用户是否删除
			}
		}
		return false;
	}
	private void createProgress(){
    	if(progress!=null){
    		progress.cancel();
    	}
    	progress = new ProgressDialog(this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.setCanceledOnTouchOutside(false);
    	progress.setCancelable(false);
    	progress.show();
    }
	Runnable chargeRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = "";
			if(bm==null){
				msg = chargeMoney();
			}else{
				msg = chargeMoneyHaveImg();
			}
			System.out.println("访问充值接口返回-----------------"+msg);
			if(msg == null){
				handler.sendEmptyMessage(5);
			}else{
				int r = Integer.parseInt(msg);
				if(r > 0){
					handler.sendEmptyMessage(0);
				}else if(r == -8){//要充值的用户不存在
					handler.sendEmptyMessage(1);
				}else if(r == -10){//充值金额非法
					handler.sendEmptyMessage(2);
				}else if(r == -14){//要充值的产品编号不存在
					handler.sendEmptyMessage(3);
				}else if(r == -24){//没有添加充值的权限
					handler.sendEmptyMessage(4);
				}else{//其他充值不成功的原因
					handler.sendEmptyMessage(5);
				}
			}
			
		}
	};
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				progress.dismiss();
				dialogWronFun("充值成功，您可以继续充值!",ChargeMoneyActivity.this);
				userNameEt.setText("");
				chargeMoney.setText("");
				chargeProduce.setText("");
				remarks.setText("");
				path = "";
				MyApplication.getInstance().setPhtotPath("");
				if(bm!=null && !bm.isRecycled()){
					bm.recycle();
				}
				photoIv.setImageBitmap(null);
				photoLayout.setVisibility(View.GONE);
				break;
			case 1://要充值的用户不存在
				progress.dismiss();
				dialogWronFun("要充值的用户不存在！",ChargeMoneyActivity.this);
				break;
			case 2://充值金额非法
				progress.dismiss();
				dialogWronFun("请填写有效充值金额！",ChargeMoneyActivity.this);
				break;
			case 3://要充值的产品编号不存在
				progress.dismiss();
				dialogWronFun("要充值的产品编号不存在！",ChargeMoneyActivity.this);
				break;
			case 4://没有添加充值的权限
				progress.dismiss();
				dialogWronFun("您没有添加充值的权限！",ChargeMoneyActivity.this);
				break;
			case 5://其他充值不成功的原因
				progress.dismiss();
				dialogWronFun("充值失败,请重试！",ChargeMoneyActivity.this);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
      	alert.show();
      }
	
	/**
	 * 有图片上传
	 * @return
	 */
	public String chargeMoneyHaveImg() {
		String reponse = "";
		String username = MyApplication.getInstance().getUsername();
		String password = MyApplication.getInstance().getPassword();
		String chargeUser = userNameEt.getText().toString().trim();
		String money = chargeMoney.getText().toString().trim();
		String produceid = chargeProduce.getText().toString().trim();
		String urlStr = Constant.CONNECT+Constant.ADDCHARGE_INTEGERFACE;
		Map<String, String> params = new HashMap<String, String>();
		Map<String, File> filesmap = new HashMap<String, File>();
		params.put("appid", Constant.APPID);
		params.put("appkey", Constant.APPKEY);
		params.put("action", "add");
		params.put("username", username);
		params.put("password", password);
		params.put("chargeUsername", chargeUser);
		params.put("amount", money);
		params.put("accountType", String.valueOf(chargeType));
		params.put("objectid", produceid);
		params.put("remark", remarks.getText().toString().trim());
//		params.put(new BasicNameValuePair("file", null));
		String headimage = "";
		if (path != null && !path.equals("")) {
			File file = new File(path);
			if (file.exists() && file.isFile()) {
				headimage = file.getName();
				filesmap.put(file.getName(), file);
			}
		}
		params.put("file", headimage);
		System.out.println("请求服务器地址:"+Constant.CONNECT);
		System.out.println("请求服务器的参数为："+params);
		try {
			UploadFile uploadfile = new UploadFile();
			uploadfile.post(urlStr, params, filesmap);
			 reponse = uploadfile.getReponse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("服务器的返回值为：" + reponse);
		return reponse;
	}
	/**
	 * 确认充值功能。
	 * 没有图片的上传
	 */
	private String chargeMoney(){
		String msg ="";
		String username = MyApplication.getInstance().getUsername();
		String password = MyApplication.getInstance().getPassword();
		String chargeUser = userNameEt.getText().toString().trim();
		String money = chargeMoney.getText().toString().trim();
		String produceid = chargeProduce.getText().toString().trim();
		try {
			String urlStr = Constant.CONNECT+Constant.ADDCHARGE_INTEGERFACE;
			HttpPost request = new HttpPost(urlStr);
			// 如果传递参数个数比较多的话，我们可以对传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("appid", Constant.APPID));
			params.add(new BasicNameValuePair("appkey", Constant.APPKEY));
			params.add(new BasicNameValuePair("action", "add"));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("chargeUsername", chargeUser));
			params.add(new BasicNameValuePair("amount", money));
			params.add(new BasicNameValuePair("accountType", String.valueOf(chargeType)));
			params.add(new BasicNameValuePair("objectid", produceid));
			params.add(new BasicNameValuePair("remark", remarks.getText().toString().trim()));
			params.add(new BasicNameValuePair("file", null));
		    request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		    HttpClient client = new DefaultHttpClient();
		    // 请求超时
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            // 读取超时
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000    );
			HttpResponse response = client.execute(request);
			msg = EntityUtils.toString(response.getEntity());
			}catch (Exception e) {
				//e.printStackTrace();
			}
		return msg;
	}
}
