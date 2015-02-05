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
 * @author ������
 * @CreateDate 2013��5��6
 * ��ֵ����
 */
public class ChargeMoneyActivity extends Activity implements OnClickListener,OnTouchListener{
	private Button chargeBtn1,chargeBtn2;//�˻���ֵ   ר���˻���ֵ
	private ImageView backBtn;//���ذ�ť
	private EditText userNameEt;//�û���
	private EditText chargeMoney;//��ֵ���
	private ImageView getPhotoIv;//��ȡ��Ƭ
	private ImageView photoIv;//��Ƭ
	private Button preViewPhoto,deletePhoto;//Ԥ����Ƭ   ɾ����Ƭ
	private EditText remarks;//��ע
	private Button submitBtn,cancleBtn;//�ύ��ť ȡ����ť
	private EditText chargeProduce;//��ֵ��Ʒ
	private RelativeLayout photoLayout;///ͼƬ����
	private LinearLayout produceLayout;//��Ʒ����
	private LinearLayout parent = null;
	private Button shareBtn1, shareBtn2,dialogCancleBtn;// 
	private PopupWindow selectPopupWindow = null;
	private final String IMAGE_TYPE = "image/*";
	public int chazhi = 0;//�ײ��������λ��
	private boolean photo_tag = false;// �Ƿ��Ѿ���ȡ��֤��ͼƬ
	private String fileName = "";
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//·��   
	public static String SAVE_PATH_IN_SDCARD = "/chargeMoney"; //ͼƬ���������ݱ����ļ���   
	private File sdcardTempFile = null;
	private String path = "";//ͼƬ������·��
	private Bitmap bm = null;//���յõ�������ͼ
	private int chargeType = 1;//��ֵ���� 1Ϊ��ͨ�û���ֵ  2Ϊר���˻���ֵ
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
		if(v == chargeBtn1){//�˻���ֵ
			chargeBtn1.setBackgroundResource(R.drawable.chargebg);
			chargeBtn1.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			chargeBtn2.setBackgroundColor(Color.TRANSPARENT);
			chargeBtn2.setTextColor(Color.WHITE);
			produceLayout.setVisibility(View.GONE);
			chargeType = 1;
			MyApplication.getInstance().setCurrentChargeType(chargeType);
		}else if(v == chargeBtn2){//ר���˻���ֵ
			chargeBtn2.setBackgroundResource(R.drawable.chargebg);
			chargeBtn2.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			chargeBtn1.setBackgroundColor(Color.TRANSPARENT);
			chargeBtn1.setTextColor(Color.WHITE);
			produceLayout.setVisibility(View.VISIBLE);
			chargeType = 2;
			MyApplication.getInstance().setCurrentChargeType(chargeType);
		}else if(v == backBtn){//���ذ�ť
			finish();
		}else if(v == getPhotoIv){//��ȡ��Ƭ
			CommonUtils.hideSoftKeyboard(this);
			initPopuWindow();
			popupWindwShowing();
		}else if(v == shareBtn1){//���ջ�ȡͼƬ
			takePhoto();//
		}else if(v == shareBtn2){//����ͼƬ
			getAlbumPhoto();
		}else if(v == dialogCancleBtn){//ȡ����ȡͼƬ
			
		}
		
	}
	
	/**
	 * ɾ����Ƭʱ����ʾ���� 
	 * @author ������
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("��ʾ")
					.setMessage("ȷ��ɾ����Ƭ��")
					.setPositiveButton("ȷ��",
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
							}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  callDailog.show();
	}
	/**
	 * ͨ�������Դ��������ȡͼƬ
	 * @author ������
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
	 * ������ȡͼƬ
	 * @author ������
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
		if (resultCode != RESULT_OK) { // �˴��� RESULT_OK ��ϵͳ�Զ����һ������
			return;
		}
		if (requestCode == 1) {// ���շ���
			fileName = CommonUtils.getFileName();// ��ͼƬ���µ�����
			File file = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD);
			if (!file.exists()) {
				file.mkdirs();
			}
			sdcardTempFile = new File(SDCARD_ROOT_PATH + SAVE_PATH_IN_SDCARD,
					fileName);
			if (CommonUtils.isHasSdcard()) {// �洢������
				path = MyApplication.getInstance().getPhtotPath();
				Bitmap bitmap = CommonUtils.getDrawable(path, MyApplication.getInstance().getScreenW(), 
						MyApplication.getInstance().getScreenH());
				bitmap = CommonUtils.RotateImg(path,bitmap);//������ͼƬ��ת90��
				boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
				if (copy) {
					File oldFile = new File(path);
					oldFile.delete();
					path = sdcardTempFile.getAbsolutePath();
					MyApplication.getInstance().setPhtotPath(path);
				}
				bm = CommonUtils.getDrawable(path);
			} else { // �洢��������ֱ�ӷ�������ͼ
				Toast.makeText(this, "û��SD��", 3000).show();
			}
			if(bm!=null){
				photoLayout.setVisibility(View.VISIBLE);
				photoIv.setImageBitmap(bm);
			}
			
		}else if (requestCode == 2) {// ѡ��ͼƬ����
			String imgPath = "";
			Uri originalUri = data.getData(); // ���ͼƬ��uri
			String[] proj = { MediaStore.Images.Media.DATA };
			// ������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
			Cursor cursor = managedQuery(originalUri, proj, null, null, null);
			// ���Ҹ������ ����ǻ���û�ѡ���ͼƬ������ֵ
			if (cursor == null) {
				imgPath = data.getDataString();
			} else {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// �����������ͷ ���������Ҫ����С�ĺ���������Խ��
				cursor.moveToFirst();
				// ����������ֵ��ȡͼƬ·��www.2cto.com
				imgPath = cursor.getString(column_index);
			}

			File fromFile = new File(imgPath);
//			if(fromFile.getParentFile().getName() .contains("thumbnail")){
//				Toast.makeText(this, "����ѡ������ͼ��", 3000).show();
//				return;
//			}
			// ���ļ����Ƶ��Զ����ļ�
			if (fromFile != null && fromFile.exists()) {
				fileName = CommonUtils.getFileName();// ��ͼƬ���µ�����
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
				bitmap = CommonUtils.RotateImg(fromFile.getAbsolutePath(),bitmap);//������ͼƬ��ת90��
				if(bitmap!=null){
					boolean copy = CommonUtils.OutPutImage(sdcardTempFile, bitmap);
					if (copy) {
						/*File oldFile = new File(path);
						oldFile.delete();*/
						path = sdcardTempFile.getAbsolutePath();
						MyApplication.getInstance().setPhtotPath(path);
					}
				}else{
					Toast.makeText(this, "ͼƬ������", 3000).show();
				}
			}
			bm = CommonUtils.getDrawable(path);
			if (bm != null) {// ͼƬ����
				photoLayout.setVisibility(View.VISIBLE);
				photoIv.setImageBitmap(bm);
			} else {// ͼƬ������
				Toast.makeText(this, "ͼƬ������", 3000).show();
			}
		} 
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * ��ʼ��PopupWindow
	 */
	private void initPopuWindow() {
		// PopupWindow���������򲼾�
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.dialog_share, null);
		shareBtn1 = (Button) loginwindow.findViewById(R.id.eshareBtn1);
		shareBtn2 = (Button) loginwindow.findViewById(R.id.eshareBtn2);
		shareBtn1.setText("���ջ�ȡ");
		shareBtn2.setText("���ֻ�����");
		dialogCancleBtn = (Button) loginwindow.findViewById(R.id.eshareCancleBtn);
		shareBtn1.setOnClickListener(this);
		shareBtn2.setOnClickListener(this);
		dialogCancleBtn.setOnClickListener(this);
		parent = (LinearLayout) findViewById(R.id.parent);
		selectPopupWindow = new PopupWindow(loginwindow, parent.getWidth(),
				LayoutParams.WRAP_CONTENT, true);
		
		selectPopupWindow.setAnimationStyle(R.style.AnimationActivity);
		// ��һ����Ϊ��ʵ�ֵ���PopupWindow�󣬵������Ļ�������ּ�Back��ʱPopupWindow����ʧ��
		// û����һ����Ч�����ܳ�������������Ӱ�챳��
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
	 * ��ʾPopupWindow����
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// ��selectPopupWindow��Ϊparent����������ʾ����ָ��selectPopupWindow��Y����������ƫ��3pix��
		// ����Ϊ�˷�ֹ���������ı���֮�������϶��Ӱ���������
		// ���Ƿ�������϶����������϶�Ĵ�С�����ܻ���ݻ��͡�Androidϵͳ�汾��ͬ����ɣ���̫�����
		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
		selectPopupWindow.showAtLocation(parent, Gravity.BOTTOM| Gravity.BOTTOM, 0, -(parent.getHeight() - chazhi));  
//		selectPopupWindow
//				.showAsDropDown(parent, 0, -(parent.getHeight() - chazhi));
		selectPopupWindow.update();  
	}

	/**
	 * PopupWindow��ʧ
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
			if(v == submitBtn){//ȷ�ϳ�ֵ
				if(userNameEt.getText().toString().trim().length()<=0){
					dialogWronFun("������Ҫ��ֵ���û�����", this);
					return false;
				}
				if(chargeType == 2 && chargeProduce.getText().toString().trim().length()<=0){
					dialogWronFun("������Ҫ��ֵ��Ʒ�ı�ţ�", this);
					return false;
				}
				String m = chargeMoney.getText().toString().trim();
				if(m.length()<=0){
					dialogWronFun("�������ֵ��", this);
					return false;
				}else if(!Constant.isNumber(m)){
					dialogWronFun("��������Ч�Ľ��ֵ��", this);
					return false;
				}
					createProgress();
					progress.setMessage("���ڳ�ֵ�����Ժ�");
					new Thread(chargeRunnable).start();
			}else if(v == cancleBtn){//ȡ����ֵ
				finish();
			}else if(v == preViewPhoto){//Ԥ����Ƭ
				Intent intent = new Intent(this,ImageSwitcher.class);
				intent.putExtra("path", path);
				startActivity(intent);
			}else if(v == deletePhoto){//ɾ��ͼƬ
				showExitDialog();//��ʾ�û��Ƿ�ɾ��
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
			System.out.println("���ʳ�ֵ�ӿڷ���-----------------"+msg);
			if(msg == null){
				handler.sendEmptyMessage(5);
			}else{
				int r = Integer.parseInt(msg);
				if(r > 0){
					handler.sendEmptyMessage(0);
				}else if(r == -8){//Ҫ��ֵ���û�������
					handler.sendEmptyMessage(1);
				}else if(r == -10){//��ֵ���Ƿ�
					handler.sendEmptyMessage(2);
				}else if(r == -14){//Ҫ��ֵ�Ĳ�Ʒ��Ų�����
					handler.sendEmptyMessage(3);
				}else if(r == -24){//û����ӳ�ֵ��Ȩ��
					handler.sendEmptyMessage(4);
				}else{//������ֵ���ɹ���ԭ��
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
				dialogWronFun("��ֵ�ɹ��������Լ�����ֵ!",ChargeMoneyActivity.this);
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
			case 1://Ҫ��ֵ���û�������
				progress.dismiss();
				dialogWronFun("Ҫ��ֵ���û������ڣ�",ChargeMoneyActivity.this);
				break;
			case 2://��ֵ���Ƿ�
				progress.dismiss();
				dialogWronFun("����д��Ч��ֵ��",ChargeMoneyActivity.this);
				break;
			case 3://Ҫ��ֵ�Ĳ�Ʒ��Ų�����
				progress.dismiss();
				dialogWronFun("Ҫ��ֵ�Ĳ�Ʒ��Ų����ڣ�",ChargeMoneyActivity.this);
				break;
			case 4://û����ӳ�ֵ��Ȩ��
				progress.dismiss();
				dialogWronFun("��û����ӳ�ֵ��Ȩ�ޣ�",ChargeMoneyActivity.this);
				break;
			case 5://������ֵ���ɹ���ԭ��
				progress.dismiss();
				dialogWronFun("��ֵʧ��,�����ԣ�",ChargeMoneyActivity.this);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
      	alert.show();
      }
	
	/**
	 * ��ͼƬ�ϴ�
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
		System.out.println("�����������ַ:"+Constant.CONNECT);
		System.out.println("����������Ĳ���Ϊ��"+params);
		try {
			UploadFile uploadfile = new UploadFile();
			uploadfile.post(urlStr, params, filesmap);
			 reponse = uploadfile.getReponse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("�������ķ���ֵΪ��" + reponse);
		return reponse;
	}
	/**
	 * ȷ�ϳ�ֵ���ܡ�
	 * û��ͼƬ���ϴ�
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
			// ������ݲ��������Ƚ϶�Ļ������ǿ��ԶԴ��ݵĲ������з�װ
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
		    // ����ʱ
            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            // ��ȡ��ʱ
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000    );
			HttpResponse response = client.execute(request);
			msg = EntityUtils.toString(response.getEntity());
			}catch (Exception e) {
				//e.printStackTrace();
			}
		return msg;
	}
}
