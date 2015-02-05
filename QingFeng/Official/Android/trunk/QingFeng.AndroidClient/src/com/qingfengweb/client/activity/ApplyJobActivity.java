/**
 * 
 */
package com.qingfengweb.client.activity;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.qingfengweb.FileUpLoad.FileHandler;
import com.qingfengweb.FileUpLoad.HttpPostUploadFile;
import com.qingfengweb.android.R;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.util.CustomProgressDialog;
import com.qingfengweb.util.RegularExpression;

/**
 * @author ������
 * @createDate 2013/7/25
 * ����ְλ��
 *
 */
public class ApplyJobActivity extends Activity implements OnClickListener{
	private ImageButton backBtn;//���ذ�ť
	private EditText nameEt,ageEt,phoneEt,emailEt,explainEt;
	private TextView genderTv;//�Ա�ѡ��ؼ�
	private LinearLayout addLayout;//��Ӹ�������  ��������
	private Button submitBtn; //�ύְλ���밴ť
	private ImageView fileImageView;
	private TextView fileTv,fileNameTv;
	ImageButton cancleFile;
	private PopupWindow selectPopupWindow = null;
	private String filePath = "";//�����ļ���·����
	File jianLifile = null;
	public DBHelper dbHelper;
	public int uploadMax = 256*1000;//һ���ϴ��ļ������ֵ  ��λ���ֽ�
	public float currentUploadPro = 0.0f;//��ǰ�ϴ�����
	public boolean clickSubmitBtn = false;//�ж��Ƿ������ύ��ť
	public String currentFileId = "";//��ǰ�ļ��ڷ������ı��
	ProgressBar progressBar = null;
	TextView currentProText = null;
	ProgressDialog pDialog = null;
	private CustomProgressDialog progressDialog = null;
	public  static boolean isStopInfoUpload = false;//����Ƿ�ֹͣ��Ϣ�ϴ�
	public  static boolean isStopFileUpload = false;//����Ƿ�ֹͣ�ļ��ϴ�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_applyjob);
		findview();
		dbHelper = DBHelper.getInstance(this);
	}
	
	
	@Override
	public void onClick(View v) {
		if(v == backBtn){//���ؼ���ť�¼�����
			isStopInfoUpload = true;
			isStopFileUpload = true;
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v == genderTv){//ѡ���Ա�
			if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
				dismiss();//���ص��Ѿ���ʾ�Ŀؼ�
			}else{
				showPopupWindow();
			}
		}else if(v.getId() == R.id.man){//ѡ������
			dismiss();
			genderTv.setText("��");
		}else if(v.getId() == R.id.woman){//ѡ��Ů��
			dismiss();
			genderTv.setText("Ů");
		}else if(v == addLayout){//���������Ӹ���
			FileListViewActivity.suffix = ".doc;.docx;.pdf;";
			Intent intent = new Intent(this,FileListViewActivity.class);
			startActivityForResult(intent, 1);
		}else if(v == cancleFile){//ɾ���ļ�
			fileImageView.setImageResource(R.drawable.qf_fujian);
			fileTv.setVisibility(View.VISIBLE);
			fileNameTv.setText("");
			cancleFile.setVisibility(View.INVISIBLE);
			filePath = "";//�����ļ���·����
			jianLifile = null;
			isStopFileUpload = true;
			progressBar.setProgress(0);
			currentProText.setVisibility(View.GONE);
		}else if(v == submitBtn){//�ύ����
			if(nameEt.getText().toString().trim().length() == 0){
				showDialog("����д����������",false,false);
			}else if(ageEt.getText().toString().trim().length() == 0){
				showDialog("����д�������䣡",false,false);
			}else if(Integer.parseInt(ageEt.getText().toString().trim())<18 || Integer.parseInt(ageEt.getText().toString().trim()) >80 ){
				showDialog("��������18-80֮�䣡",false,false);
			}else if(phoneEt.getText().toString().trim().length() == 0){
				showDialog("����д�����ֻ��Ż������ţ�",false,false);
			}else if(emailEt.getText().toString().trim().length() == 0){
				showDialog("����д���ĵ������䣡",false,false);
			}else if(!RegularExpression.isEmail(emailEt.getText().toString().trim())){
				showDialog("����д��Ч�ĵ������䣡",false,false);
			}else if(jianLifile ==null || filePath.equals("")){
				showDialog("����Ӽ�����",false,false);
			}else if(!jianLifile.exists()){
				showDialog("����ӵļ����ļ������ڣ�������ѡ��",false,false);
			}else{
//				showProgressDidlaog();
//				startProgressDialog();
				showDialog("�����ϴ�",false,true);
				clickSubmitBtn = true;
				isStopInfoUpload = false;
				isStopFileUpload = false;
				if(currentUploadPro == 1){//��������ļ��Ѿ��ϴ���� �ſ����߳��ϴ�������Ϣ
					new Thread(applyJobInfoRunnable).start();
				}
			}
			
		}
		
	}
	/**
	 * �ϴ��ļ���һ��
	 */
	Runnable applyJobFileRunnable = new Runnable() {
		
		@Override
		public void run() {
			uploadFirst();
		}
	};
	/**
	 * �ϴ��ļ��ڶ��κ͵ڶ����Ժ�����ж�
	 */
	Runnable upLoadPiece = new Runnable() {
		
		@Override
		public void run() {
			currentPiece ++;
			System.out.println("��ǰ��0000000000000000000000=="+currentPiece);
			uploadPiece(currentPiece);
			
		}
	};
	int currentPiece = 1;//��ǰ�����ϴ����ļ���
	int upNumber = 0;//�ܹ�Ҫ�ϴ��Ŀ���
	int utilPro = 0;//ÿһ���ϴ��ɹ��������Ҫ�����ĵ�λ���ȡ�
	byte[] fileByte = null;//��ǰ�ϴ��ļ�ת����Ϊ�ֽ�����
	  /**
     * �ϴ��ļ��ĵ�һ��
     */
    private void uploadFirst(){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
        	String fileName = new File(filePath).getName().toString().trim();
    		handler.sendEmptyMessage(5);//���߽��������ϴ���
    		upNumber = fileByte.length/uploadMax+(fileByte.length%uploadMax>0?1:0);
    		utilPro = 100/upNumber;
	    	Map<String, String> params = new HashMap<String, String>();
	        params.put("appid", AccessServer.APPID);
	        params.put("appkey", AccessServer.APPKEY);
	        params.put("action", AccessServer.SUBMIT_JOBFILE_ACTION);
	        params.put("type", "2");
	        params.put("id", "");
	        params.put("filesize", fileByte.length+"");
	        params.put("start", "0");
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        if(upNumber>1){//����1���ʱ��
	        		params.put("length", (uploadMax)+"");
	        		System.out.println("��ǰ�ϴ������ʼλ��=================="+0);
	        		System.out.println("��ǰ�ϴ����ݳ���=================="+uploadMax);
	        		//��ʼ����
	    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, 0, uploadMax,"file");
	    	        System.out.println("���η������ķ���ֵ=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        }else{//ֻ��һ���ʱ��
	        	params.put("length", (fileByte.length)+"");
        		//��ʼ�ϴ�
    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, 0, fileByte.length,"file");
    	        System.out.println(msgStr);
	        }
	        	//��������
	            ArrayList<String> reList = JsonData.jsonFileUploadData(msgStr, dbHelper.open());
	            currentUploadPro = Float.parseFloat(reList.get(1));//���õ�ǰ����
	            currentFileId = reList.get(0);
        		handler.sendEmptyMessage(0);
    	}catch(Exception e){
    		System.out.println("�ϴ���һ��ʱ���������ˣ����������ļ��ϴ���һ���ļ������γ������ԭ��Ϊ��"+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
    boolean uploading = false;//����Ƿ������ϴ�
    /**
     * �ϴ��ļ������в��ֲ�������һ��
     * @param fileByte
     * @param filePath
     * @param fileId
     * @param currentPiece
     */
    private void uploadPiece(int currentPiece){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
	    	Map<String, String> params = new HashMap<String, String>();
	        params.put("appid", AccessServer.APPID);
	        params.put("appkey", AccessServer.APPKEY);
	        params.put("action", AccessServer.SUBMIT_JOBFILE_ACTION);
	        params.put("type", "2");
	        params.put("id", currentFileId);
	        params.put("filesize", fileByte.length+"");
	        System.out.println("�ļ��ܳ���=================="+fileByte.length);
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        if(upNumber>1){//����1���ʱ��
	        	if(currentPiece < upNumber){
	        		params.put("start", ""+(currentPiece-1)*uploadMax);
	        		params.put("length", (uploadMax)+"");
	        		System.out.println("��ǰ�ϴ���=================="+currentPiece);
	        		System.out.println("��ǰ�ϴ������ʼλ��=================="+(currentPiece-1)*uploadMax);
	        		System.out.println("��ǰ�ϴ����ݳ���=================="+uploadMax);
	        		//��ʼ����
	    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, (currentPiece-1)*uploadMax, uploadMax*currentPiece,"file");
	    	        System.out.println("���η������ķ���ֵ=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        	}else if(currentPiece == upNumber){
	        		params.put("start", ""+(currentPiece-1)*uploadMax);
	        		params.put("length", (fileByte.length-(uploadMax*(currentPiece-1)))+"");
	        		System.out.println("��ǰ�ϴ���=================="+currentPiece);
	        		System.out.println("��ǰ�ϴ������ʼλ��=================="+(currentPiece-1)*uploadMax);
	        		System.out.println("��ǰ�ϴ����ݳ���=================="+(fileByte.length-(uploadMax*(currentPiece-1))));
	        		//��ʼ����
	    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, (currentPiece-1)*uploadMax, fileByte.length,"file");
	    	        System.out.println("���η������ķ���ֵ=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        	}
	        }else{//ֻ��һ���ʱ��
	        	params.put("start", "0");
	        	params.put("length", (fileByte.length)+"");
        		//��ʼ����
    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, 0, fileByte.length,"file");
    	        System.out.println(msgStr);
	        }
	        	//��������
	            ArrayList<String> reList = JsonData.jsonFileUploadData(msgStr, dbHelper.open());
	            currentUploadPro = Float.parseFloat(reList.get(1));//���õ�ǰ����
	            currentFileId = reList.get(0);
        		handler.sendEmptyMessage(0);

    	}catch(Exception e){
    		System.out.println("�ϴ���"+currentPiece+"��ʱ���������ˣ����������ļ��ϴ���һ���ļ������γ������ԭ��Ϊ��"+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
    Runnable applyJobInfoRunnable = new Runnable() {
		
		@Override
		public void run() {
			Bundle bundle = new Bundle();
			bundle.putString("jobid", getIntent().getStringExtra("jobid"));
			bundle.putString("name", nameEt.getText().toString().trim());
			bundle.putString("gender", genderTv.getText().toString().trim().equals("��")?"1":"2");
			bundle.putString("age", ageEt.getText().toString().trim());
			bundle.putString("phone_number", phoneEt.getText().toString().trim());
			bundle.putString("email", emailEt.getText().toString().trim());
			bundle.putString("resume", currentFileId);
			bundle.putString("message", explainEt.getText().toString().trim());
			String msgStr = AccessServer.submitJobAsk(bundle);
			System.out.println(msgStr);
			String[] msgArray = msgStr.split(",");
			if(msgArray[0].equals("0")){//�ύ�ɹ�
				handler.sendEmptyMessage(3);
			}else if(msgArray[0].equals("404")){//���ʷ�����ʧ��
				handler.sendEmptyMessage(2);
			}else{//�ύʧ��
				 handler.sendEmptyMessage(1);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
//				Toast.makeText(ApplyJobActivity.this, ""+currentUploadPro, 1000).show();
				progressBar.setProgress(progressBar.getProgress()+utilPro);
				currentProText.setText(progressBar.getProgress()+"%");
				currentProText.setTextSize(20);
				currentProText.setTextColor(Color.BLACK);
				if(currentPiece == 1 && !isStopFileUpload){//����һ���ϴ�����֮�����ϴ�����Ķ�
					for(int i=2;i<=upNumber;i++){
						new Thread(upLoadPiece).start();
					}
				}
				if(progressBar.getProgress() == 100 && currentPiece == upNumber){
					if(clickSubmitBtn && !isStopInfoUpload){//��������ļ��ϴ�����   ����û�е�����ύ��ť������вſ�ʼ�ϴ���Ϣ
						new Thread(applyJobInfoRunnable).start();
					}
				}
			}else if(msg.what == 1){//���ʷ���������
				Toast.makeText(getApplicationContext(), "������", 3000).show();
//				 stopProgressDialog();
			}else if(msg.what == 2){//���ʷ�����ʧ��
				Toast.makeText(getApplicationContext(), "404", 3000).show();
//				stopProgressDialog();
			}else if(msg.what == 3){//������Ϣ�ͼ����ļ��ύ��
//				stopProgressDialog();
				if(!isStopInfoUpload){
					dialog.dismiss();
					showDialog("ְλ�����ύ�ɹ�����˾�ᾡ����ϵ����",true,false);
				}
				
			}else if(msg.what == 4){
				currentProText.setText("�ϴ�ʧ��");
				currentProText.setTextSize(12);
				currentProText.setTextColor(Color.RED);
			}else if(msg.what == 5){
				currentProText.setText("�����ϴ�...");
				currentProText.setTextSize(12);
				currentProText.setTextColor(Color.BLACK);
			}
			super.handleMessage(msg);
		}
		
	};
	private void showProgressDidlaog(){
		pDialog = new ProgressDialog(this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("����Ϊ���ύְλ���룬���Ժ�");
		pDialog.setCanceledOnTouchOutside(false);
		pDialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					
				}
				return false;
			}
		});
		pDialog.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=2){
			return;
		}
		if(requestCode == 1){
//			((Button)findViewById(R.id.button_openfile)).setText(data.getBundleExtra("file").get("path").toString());
			filePath = data.getBundleExtra("file").get("path").toString();
		    jianLifile =  new File(filePath);
			if(jianLifile.exists()){//�ж��ļ��Ƿ����
				fileByte = FileHandler.fileToByte(filePath);//���ļ�ת���ֽ�����
	        	if(fileByte.length>1024*10*1000){
	        		showDialog("�ļ���С���ܳ���10M�����С�ļ����߰��ļ���ֳɶ����", false, false);
	        		return;
	        	}
	        	progressBar.setProgress(0);
				fileImageView.setImageResource((Integer) data.getBundleExtra("file").getInt("imageId"));
				fileTv.setVisibility(View.GONE);
				fileNameTv.setText(data.getBundleExtra("file").getString("name"));
				cancleFile.setVisibility(View.VISIBLE);
				currentProText.setVisibility(View.VISIBLE);
				isStopInfoUpload = false;
				isStopFileUpload = false;
				currentPiece = 1;
				new Thread(applyJobFileRunnable).start();
			}
			
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void findview(){
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.getProgressDrawable().setAlpha(80);
		currentProText = (TextView) findViewById(R.id.currentProText);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		nameEt = (EditText) findViewById(R.id.nameEt);
		ageEt = (EditText) findViewById(R.id.ageEt);
		phoneEt = (EditText) findViewById(R.id.phoneEt);
		emailEt = (EditText) findViewById(R.id.emailEt);
		explainEt = (EditText) findViewById(R.id.explainEt);
		genderTv = (TextView) findViewById(R.id.genderTv);
		addLayout = (LinearLayout) findViewById(R.id.addJianLiLayout);
		fileImageView = (ImageView) findViewById(R.id.fileImage);
		fileTv = (TextView) findViewById(R.id.fileText);
		fileNameTv = (TextView) findViewById(R.id.fileName);
		submitBtn = (Button) findViewById(R.id.sumbitBtn);
		submitBtn.setOnClickListener(this);
		cancleFile = (ImageButton) findViewById(R.id.cancleFile);
		genderTv.setOnClickListener(this);
		addLayout.setOnClickListener(this);
		cancleFile.setOnClickListener(this);
		initPopuWindow();//��ʼ��������
	}
	/**
	 * ��ʼ��PopupWindow
	 */
	private void initPopuWindow() {
		View v = LayoutInflater.from(this).inflate(R.layout.gender_select, null);
		int pwidth = com.qingfengweb.client.data.MyApplication.getInstant().getScreenW()/2-23;
		v.findViewById(R.id.man).setOnClickListener(this);
		v.findViewById(R.id.woman).setOnClickListener(this);
		selectPopupWindow = new PopupWindow(v, pwidth, LayoutParams.WRAP_CONTENT,true);//�����true����Ҫ��Ϊtrueʱ���ԭ���Ŀؼ����ڲŻ���ʧ
		// ��һ����Ϊ��ʵ�ֵ���PopupWindow�󣬵������Ļ�������ּ�Back��ʱPopupWindow����ʧ��
		// û����һ����Ч�����ܳ�������������Ӱ�챳��
		// ���������������ޣ���������ԭ�򣬻������֡�֪����ָ��һ��
		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.gender_border));
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				dismiss();
			}
		});
	}
	/**
	 * PopupWindow��ʧ
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	public void showPopupWindow(){
		if(selectPopupWindow.isShowing()){
			dismiss();
		}else{
			selectPopupWindow.showAsDropDown(genderTv, 0, 0);
		}
		
	}
	/**
	 * �ֶ���֤�ĵ�����
	 * @param msg
	 */
	private void showDialog(String msg,final boolean finishActivity,final boolean setView){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("��ʾ!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		if(setView){
			View view = LayoutInflater.from(this).inflate(R.layout.customprogressdialog,null);
			alerBuilder.setView(view);
		}else{
			alerBuilder.setMessage(msg);
			alerBuilder.setPositiveButton("֪���ˣ�", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				if(finishActivity){
					isStopInfoUpload = true;
					isStopFileUpload = true;
					finish();
					overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
				}
				return ;
				}
			});
		}
		
		alerBuilder.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					if(setView){
						dialog.dismiss();
						showDialog("�˲�������ְͣλ���룬ȷ����ͣ��");
					}else{
						return true;
					}
				}
				return false;
			}
		});
	    dialog = alerBuilder.create();
		dialog.show();
	}
	Dialog dialog = null;
	/**
	 * �ֶ���֤�ĵ�����
	 * @param msg
	 */
	private void showDialog(String msg){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("��ʾ!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
			alerBuilder.setMessage(msg);
			alerBuilder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog1, int which) {
					dialog.dismiss();
					isStopInfoUpload = true;
					isStopFileUpload = false;
				return ;
				}
			});
			alerBuilder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog1, int which) {
					dialog.dismiss();
					showDialog("�����ϴ�",false,true);
				return ;
				}
			});
		alerBuilder.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK){
					return true;
				}
				return false;
			}
		});
	    dialog = alerBuilder.create();
		dialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			isStopInfoUpload = true;
			isStopFileUpload = true;
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}
		return true;
	}
//	private void startProgressDialog() {
//		if (progressDialog == null) {
//			progressDialog = CustomProgressDialog.createDialog(this);
//			progressDialog.setMessage("�����ύְλ���룬���Ժ�");
//		}
//		progressDialog.setCanceledOnTouchOutside(false);
//		progressDialog.setOnKeyListener(new OnKeyListener() {
//
//			public boolean onKey(DialogInterface dialog, int keyCode,
//					KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					return true;
//				}
//				return false;
//			}
//		});
//		progressDialog.show();
//	}
//
//	private void stopProgressDialog() {
//		if (progressDialog != null) {
//			progressDialog.dismiss();
//			progressDialog = null;
//		}
//
//	}
}
