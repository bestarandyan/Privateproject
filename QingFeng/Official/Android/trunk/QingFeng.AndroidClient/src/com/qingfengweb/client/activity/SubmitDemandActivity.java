/**
 * 
 */
package com.qingfengweb.client.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnKeyListener;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.FileUpLoad.FileHandler;
import com.qingfengweb.FileUpLoad.HttpPostUploadFile;
import com.qingfengweb.FileUpLoad.ProgressBarCallback;
import com.qingfengweb.android.R;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.JsonData;
import com.qingfengweb.client.data.MyApplication;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.util.RegularExpression;

/**
 * @author ������
 * @createDate 2013/7/26
 * �ύ������
 *
 */
public class SubmitDemandActivity extends Activity implements OnClickListener,OnCheckedChangeListener{
	LinearLayout addLayout;
	LinearLayout jianLiLayout;
	HorizontalScrollView scrollView;
	EditText nameEt,phoneOneEt,phoneTwoEt,emailOneEt,emailTwoEt,qqEt,detailDemandEt,budgetEt,timeEt;
	Bundle currentFileBundle = null;
	ArrayList<Bundle> fileList = new ArrayList<Bundle>();
	private ImageButton backBtn;
	DBHelper dbHelper = null;
	CheckBox check1,check2,check3,check4,check5,check6,check7;
	Button sumbitBtn;
	public String currentFileId = "";//��ǰ�ļ��ڷ������ı��
	public String currentFileName = "";//��ǰ�ļ�������
	public String currentFilePath = "";//��ǰ�����ϴ����ļ�·��
	public byte[] currentFileByte = null;//��ǰ�����ϴ����ļ��ַ�����
	public int currentUploadFile = 0;//��ǵ�ǰ�����ϴ����ļ���fileList�е��±�
//	public SetProBroadcast setProBroadcast = null;
	public static final String setProAction =  "com.qingfengweb.setProAction";
	public int utilPro = 0;//����ǰ������ÿһ��Ӧ�������Ľ���
	public int upNumber = 0;//��ʾ��ǰ�ļ��ܹ�Ҫ�ϴ��Ĵ���
	ProgressBar currentBar = null;//��ǰ׼���߶��Ľ�����
	TextView currentProtv = null;//��ǰ��������ʾ�ٷֱȵĿؼ�
	public int countSuccess = 0;//�ɹ��ϴ��ļ�������
	public boolean uploadOver = false;//����Ƿ������ļ��ϴ���ɡ�
	public boolean uploading = false;//��������ϴ�����
	public String typeStr = "";//�������� �ö��ŷֿ�
	public String fileIdStr = "";//�ϴ��ɹ����ļ���id ���id֮���ö��ŷֿ�
	public ArrayList<HashMap<String,String>> fileIdList = new ArrayList<HashMap<String,String>>();//�ϴ��ɹ����ļ�id���� ������ϵ���Ҫ������  ��ɾ���ϴ��ɹ��˵��ļ���ʱ�����
	public Bundle infoBundle = new Bundle();//��Ϣ����
	public boolean clickSubmitBtn = false;//�ж��Ƿ������ύ��ť
	public  static boolean isStopInfoUpload = false;//����Ƿ�ֹͣ��Ϣ�ϴ�
	public  static boolean isStopFileUpload = false;//����Ƿ�ֹͣ�ļ��ϴ�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_submitdemand);
		findview();
		dbHelper = DBHelper.getInstance(this);
	}
	@Override
	protected void onDestroy() {
//		this.unregisterReceiver(setProBroadcast);
		super.onDestroy();
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
	@SuppressLint("ShowToast")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=2){
			return;
		}
		if(requestCode == 1){
			new Thread(runnable1).start();
//			((Button)findViewById(R.id.button_openfile)).setText(data.getBundleExtra("file").get("path").toString());
			currentFileBundle = data.getBundleExtra("file");
			File file = new File(currentFileBundle.getString("path"));
			if(!file.exists()){
				return ;
			}
			if(file.length() > 10*1024*1000){
				showDialog("�ļ���С���ܳ���10M�����С�ļ����߰��ļ���ֳɶ����", "֪���ˣ�", false, false);
				return ;
			}
			currentFileName = file.getName();
			if(fileList.size()>0){//�жϸ��ļ��Ƿ��Ѿ���ӡ�
				for (int  i = 0;i < fileList.size();i++) {
					if(fileList.get(i).getString("path").equals(currentFileBundle.getString("path"))){
						Toast.makeText(SubmitDemandActivity.this, "���ļ�����ӣ���ѡ�������ļ���", 7000).show();
						return ;
					}
				}
				fileList.add(currentFileBundle);
				jianLiLayout.addView(getFileView());
				if(uploadOver){
					uploadOver = false;
					new Thread(uploadFileStart).start();
				}
			}else{
				currentUploadFile = 0;
				fileList.add(currentFileBundle);
				jianLiLayout.addView(getFileView());
				uploadOver = false;
				new Thread(uploadFileStart).start();
			}
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	Runnable uploadFileStart = new Runnable() {
		
		@Override
		public void run() {
			currentFilePath = fileList.get(currentUploadFile).getString("path").toString();
			uploadFile(currentFilePath);
		}
	};
	public byte[] fileToByte(String filePath){
		byte[] fileByte = null;
		try {
			java.io.RandomAccessFile ras = new java.io.RandomAccessFile(filePath,"r");
	        int total = (int)ras.length();
	        System.out.println(total);
	        fileByte = new byte[total];
	        int p=0;
	        while (total-p>0){
	            p+=ras.read(fileByte,p,total-p);
	        }
	        ras.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileByte;
    }
	 /**
     * �ϴ�ͼƬ��������
     * 
     * @param imageFile ����·��
     */
	public int uploadMax = 256*1000;//һ���ϴ��ļ������ֵ  ��λ���ֽ�
	public float currentUploadPro = 0.0f;//��ǰ�ϴ�����
	public int currentPiece = 1;//��ǰ�Ŀ���
    public void uploadFile(String filePath) {
        try {
        	if(currentFileByte!=null){
        		currentFileByte = null;
        		System.gc();
        	}
            currentFileByte = fileToByte(filePath);//���ļ�ת���ֽ�����
            currentUploadPro = 0;
            currentPiece = 1;
            uploading = true;
            if(currentFileByte.length<=uploadMax){//���ļ���СС��256KBʱ ֱ��һ�����ϴ�
            	uploadFirst(currentFileByte,filePath,"",1);
            }else{//����ļ��Ĵ�С���ڵ���256KB ��ֿ��ϴ�  ÿһ��Ĵ�СΪ256�ˣ�
            	uploadFirst(currentFileByte,filePath,"",1);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * �ϴ��ļ��ĵ�һ��
     * @param fileByte
     * @param filePath
     * @param fileId
     * @param currentPiece
     */
    private void uploadFirst(byte[] fileByte,String filePath,String fileId,int currentPiece){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
        	String fileName = new File(currentFilePath).getName().toString().trim();
    		for(int i=0;i<fileList.size();i++){
    			if(fileName.equals(fileList.get(i).getString("name"))){
    				currentBar = (ProgressBar) jianLiLayout.getChildAt(i).findViewById(R.id.progressBar);
    				currentProtv = (TextView) jianLiLayout.getChildAt(i).findViewById(R.id.currentProText);
    			}
    		}
    		handler.sendEmptyMessage(5);//���߽��������ϴ���
    		upNumber = fileByte.length/uploadMax+(fileByte.length%uploadMax>0?1:0);
    		utilPro = 100/upNumber;
	    	Map<String, String> params = new HashMap<String, String>();
	        params.put("appid", AccessServer.APPID);
	        params.put("appkey", AccessServer.APPKEY);
	        params.put("action", AccessServer.SUBMIT_JOBFILE_ACTION);
	        params.put("type", getTypeOfFile(fileName));
	        System.out.println("Ŀǰ�ϴ����ļ�������Ϊ==========="+getTypeOfFile(fileName));
	        params.put("id", fileId);
	        params.put("filesize", fileByte.length+"");
	        params.put("start", "0");
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        if(upNumber>1){//����1���ʱ��
	        		params.put("length", (uploadMax)+"");
	        		System.out.println("��ǰ�ϴ���=================="+currentPiece);
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
        		handler.sendEmptyMessage(2);

    	}catch(Exception e){
    		System.out.println("�ϴ���һ��ʱ���������ˣ����������ļ��ϴ���һ���ļ������γ������ԭ��Ϊ��"+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
    /**
     * �ϴ��ļ������в��ֲ�������һ��
     * @param fileByte
     * @param filePath
     * @param fileId
     * @param currentPiece
     */
    private void uploadPiece(byte[] fileByte,String filePath,String fileId,int currentPiece){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
        	String fileName = new File(currentFilePath).getName().toString().trim();
    		for(int i=0;i<fileList.size();i++){
    			if(fileName.equals(fileList.get(i).getString("name"))){
    				currentBar = (ProgressBar) jianLiLayout.getChildAt(i).findViewById(R.id.progressBar);
    				currentProtv = (TextView) jianLiLayout.getChildAt(i).findViewById(R.id.currentProText);
    			}
    		}
	    	Map<String, String> params = new HashMap<String, String>();
	        params.put("appid", AccessServer.APPID);
	        params.put("appkey", AccessServer.APPKEY);
	        params.put("action", AccessServer.SUBMIT_JOBFILE_ACTION);
	        params.put("type", getTypeOfFile(fileName));
	        params.put("id", fileId);
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
               
        		handler.sendEmptyMessage(3);

    	}catch(Exception e){
    		System.out.println("�ϴ���"+currentPiece+"��ʱ���������ˣ����������ļ��ϴ���һ���ļ������γ������ԭ��Ϊ��"+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				scrollView.smoothScrollTo(10000,0);
			}else if(msg.what == 1){
				Toast.makeText(SubmitDemandActivity.this, msg.obj.toString()+"�ύ�ɹ���", 3000).show();
			}else if(msg.what == 2){//��һ���ύ�ɹ�
				currentBar.setProgress(currentBar.getProgress()+utilPro);
				currentBar.getProgressDrawable().setAlpha(80);
				currentProtv.setText(currentBar.getProgress()+"%");
				System.out.println(currentPiece+"��ǰ�������ĸ߶�ֵ��"+currentBar.getProgress());
				currentProtv.setTextSize(20);
				if(upNumber == 1){//�ļ���С����������ϴ���
					if(currentBar.getProgress() == 100){
						currentPiece = 1;
						currentUploadFile++;
						if(currentUploadFile<fileList.size()){//��������ϴ����ļ�ֻ��Ҫ�ϴ�һ�����ϴ�һ�κ�Ϳ�ʼ�ϴ���һ���ļ�
							new Thread(uploadFileStart).start();
						}else{
							uploadOver = true;
							uploading =  false;
						}
						HashMap<String,String> map = new HashMap<String, String>();
						map.put("currentFileId", currentFileId);
						map.put("currentFileName", currentFileName);
						if(!fileIdList.contains(map)){
							fileIdList.add(map);
						}
					}
				}else if(upNumber>1 && !isStopFileUpload){//��Ҫ����ϴ����ļ��������￪ʼ�ϴ������Ƭ��
					for(int i=2;i<=upNumber;i++){
						new Thread(upLoadPiece).start();
					}
				}
			}else if(msg.what ==3 ){//�ϴ��ɹ���������Ƭ�� �����������ļ�
				currentBar.setProgress(currentBar.getProgress()+utilPro);
				currentBar.getProgressDrawable().setAlpha(80);
				currentProtv.setText(currentBar.getProgress()+"%");
				currentProtv.setTextSize(20);
				System.out.println(currentPiece+"��ǰ�������ĸ߶�ֵ��"+currentBar.getProgress());
//				Intent intent = new Intent();
//				intent.setAction("com.qingfengweb.setProAction");
//				context.sendBroadcast(intent);
				System.out.println("��ǰ��--------------"+currentPiece);
				if(currentBar.getProgress() == 100 && currentPiece == upNumber){//һ���ļ��ϴ��ɹ�
					HashMap<String,String> map = new HashMap<String, String>();
					map.put("currentFileId", currentFileId);
					map.put("currentFileName", currentFileName);
					if(!fileIdList.contains(map)){
						fileIdList.add(map);
					}
					if(currentFileByte!=null){
		        		currentFileByte = null;
		        		System.gc();
		        	}
					currentPiece = 1;
					currentUploadFile++;
					if(!isStopFileUpload){//�ж��Ƿ���Ҫֹͣ�ϴ�
						if(currentUploadFile<fileList.size()){//�����ϴ���һ���ļ�
							new Thread(uploadFileStart).start();
						}else{
							uploadOver = true;
							uploading =  false;
							if(clickSubmitBtn){//���ļ��ϴ�����֮���ж��Ƿ������ύ��ť���������� ��ֱ���ϴ���Ϣ
								infoBundle = getInfo();
								new Thread(submitInfoRunnable).start();
							}
						}
					}
				}
			}else if(msg.what == 4){//���ĳһ���ļ��ϴ�ʧ�� �����������һ���ļ����ϴ�
				if(currentFileByte!=null){
	        		currentFileByte = null;
	        		System.gc();
	        	}
				currentProtv.setText("�ϴ�ʧ��");
	    		currentProtv.setTextColor(Color.RED);
	    		currentProtv.setTextSize(12);
				currentPiece = 1;
				currentUploadFile++;
				if(currentUploadFile<fileList.size()){
					new Thread(uploadFileStart).start();
				}else{
					uploadOver = true;
					uploading =  false;
				}
			}else if(msg.what == 5){//�����û������ϴ���
				currentProtv.setText("�ϴ���...");
				currentProtv.setTextSize(12);
			}else if(msg.what == 6){//��Ϣ���ļ����ϴ��ɹ���
				dialog.dismiss();
				showDialog("�����ύ�ɹ�����˾�ᾡ��������ϵ��", "֪���ˣ�", true, false);
			}else if(msg.what == 7){//���ӷ�����ʧ��
				
			}else if(msg.what == 8){//�ύʧ�ܡ�
				
			}
			
			super.handleMessage(msg);
		}
		
	};
	Runnable upLoadPiece = new Runnable() {
		
		@Override
		public void run() {
			currentPiece ++;
			System.out.println("��ǰ��0000000000000000000000=="+currentPiece);
			uploadPiece(currentFileByte,currentFilePath,currentFileId,currentPiece);
			
		}
	};
	
	Runnable runnable1 = new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(200);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		/**
		 * �ύ��Ϣ���߳�
		 */
		Runnable submitInfoRunnable = new Runnable() {
			
			@Override
			public void run() {
				String msg = AccessServer.submitDevelopDemand(infoBundle);
				System.out.println("�ύ������Ϣʱ���ص�����Ϊ��"+msg);
				String[] msgArray = msg.split(",");
				if(msgArray!=null && msgArray[0].equals("0")){//�ύ�ɹ�
					handler.sendEmptyMessage(6);
				}else if(msg.equals("404")){
					handler.sendEmptyMessage(7);//���ӷ�����ʧ��
				}else{
					handler.sendEmptyMessage(8);//�ϴ�ʧ��
				}
				
			}
		};
	/**
	 * �����ļ����ƻ�ȡ�ļ�����
	 * @param name
	 * @return
	 */
	private String  getTypeOfFile(String name){
		String type ="1";
		name = name.toLowerCase();
		if(name.contains(".png") || name.contains(".jpg")){
			type = "1";
		}else if(name.contains(".doc") || name.contains(".docx") || name.contains(".xls") ||
				name.contains(".xlsx") || name.contains(".ppt") || name.contains(".pptx") 
				|| name.contains(".pdf")){// doc,docx,xls,xlsx,ppt,pptx,pdf 
			type = "2";
		}else{
			type = "4";
		}
		return type;
	}
	/**
	 * �����ڻ�ȡ��Ƭ����֮���ù�������λ����ͣ���������棬�����û�������һЩ��������ȥ�ֶ�����������ȥ��ȡ�����ͼƬ
	 * ��ȷ���˹�����֮���������ܻ�ȡ����
	 * 
	 * @createDate 2013/1/22
	 * @author ������
	 * @param focus
	 *            true�����ܻ�ȡ���㣬false����ʧȥ����
	 */
	private void clearEditTextFocus() {
		nameEt.clearFocus();
		phoneOneEt.clearFocus();
		phoneTwoEt.clearFocus();
		emailOneEt.clearFocus();
		emailTwoEt.clearFocus();
		qqEt.clearFocus();
		detailDemandEt.clearFocus();
		budgetEt.clearFocus();
		timeEt.clearFocus();
	}
	private View getFileView(){
		final View  view = LayoutInflater.from(this).inflate(R.layout.view_jianli, null);
		ImageButton cancleImgBtn = (ImageButton) view.findViewById(R.id.cancleImgBtn);
		TextView fileNameTv = (TextView) view.findViewById(R.id.fileNameTv);
		ImageView fileIv = (ImageView) view.findViewById(R.id.jianLiImage);
		fileIv.setImageResource(currentFileBundle.getInt("imageId"));
		fileNameTv.setText(currentFileBundle.getString("name").toString());
		TextView proTextView = (TextView) view.findViewById(R.id.currentProText);
		proTextView.setText("�ȴ��ϴ�...");
		proTextView.setTextSize(12);
		view.setTag(currentFileBundle);
		cancleImgBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jianLiLayout.removeView(view);
				Bundle bundle = (Bundle) view.getTag();
				String fileName = bundle.getString("name");
				fileList.remove(bundle);
				currentUploadFile--;
				for(int i=0;i<fileIdList.size();i++){
					if(fileName.equals(fileIdList.get(i).get("currentFileName"))){
						fileIdList.remove(i);
						return ;
					}
				}
			}
		});
		return view;
	}
	@Override
	public void onClick(View v) {
		if(v == addLayout){//��������ļ�
			if(fileList!=null && fileList.size()>=10){
				Toast.makeText(SubmitDemandActivity.this, "��ѡ����ļ������Ѵ����ޣ�", 7000).show();
			}else{
				clearEditTextFocus();
				FileListViewActivity.suffix = ".doc;.docx;.pdf;.xls;.xlsx;.ppt;.pptx;.jpg;.png;.rar;.zip;.gz;.psd;.ai";
				Intent intent = new Intent(this,FileListViewActivity.class);
				startActivityForResult(intent, 1);
			}
			
		}else if(v == backBtn){//���ذ�ť
			isStopInfoUpload = true;
			isStopFileUpload = true;
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v == sumbitBtn){//�ύ����ť
			String nameStr = nameEt.getText().toString().trim();
			String phoneOneStr = phoneOneEt.getText().toString().trim();
			String phoneTwoStr = phoneTwoEt.getText().toString().trim();
			String emailOneStr = emailOneEt.getText().toString().trim();
			String emailTwoStr = emailTwoEt.getText().toString().trim();
			String qqStr = qqEt.getText().toString().trim();
			String detailStr = detailDemandEt.getText().toString().trim();
			String budgetStr = budgetEt.getText().toString().trim();
			String timeStr = timeEt.getText().toString().trim();
			System.out.println(typeStr);
			if(typeStr.length() == 0 || typeStr.equals(",")){
				showDialog("������ѡ��һ���������ͣ�","ȥѡ���������ͣ�", false, false);
			}else if(nameStr.length() == 0){
				showDialog("��ô�ƺ�����","ȥ��д�ƺ���", false, false);
			}else if(phoneOneStr.length() == 0 && phoneTwoStr.length() == 0){
				showDialog("�����ٸ�������һ����ϵ�绰���������ǿ�����ϵ������","ȥ��д��ϵ�绰��", false, false);
			}else if(emailOneStr.length() == 0 && emailTwoStr.length() == 0){
				showDialog("�����ٸ�������һ�����������ַ���������ǵĹ�ͨ��","ȥ��д���������ַ��", false, false);
			}else if((emailOneStr.length()>0 && !RegularExpression.isEmail(emailOneStr)) ||
					(emailTwoStr.length()>0 && !RegularExpression.isEmail(emailTwoStr))){
				showDialog("����д��Ч�ĵ������䣡","ȥ�޸������ַ��",false,false);
			}else if(detailStr.length() == 0){
				showDialog("����д�������飬�������˽���������","ȥ��д�������飡", false, false);
			}else if(fileIdList == null || fileIdList.size()==0){
				showDialog("��������󸽼���","֪����",false,false);
			}else{
				clickSubmitBtn = true;
				showDialog("�����ύ�������Ժ�", "", false, true);
				if(uploadOver && fileIdList!=null && fileIdList.size()>0){//�ж��ļ��ϴ��Ƿ��Ѿ����
					isStopInfoUpload = false;
					isStopFileUpload = false;
					infoBundle = getInfo();
					new Thread(submitInfoRunnable).start();
				}else if(isStopFileUpload){
					System.out.println("ֹͣ���ϴ�����������ʾ�����ϴ�");
					isStopInfoUpload = false;
					isStopFileUpload = false;
					for(int i=currentPiece;i<=upNumber;i++){
						new Thread(upLoadPiece).start();
					}
				}
			}
		}
	}
	
	private Bundle getInfo(){
		Bundle bundle = new Bundle();
		String nameStr = nameEt.getText().toString().trim();
		String phoneOneStr = phoneOneEt.getText().toString().trim();
		String phoneTwoStr = phoneTwoEt.getText().toString().trim();
		String emailOneStr = emailOneEt.getText().toString().trim();
		String emailTwoStr = emailTwoEt.getText().toString().trim();
		String qqStr = qqEt.getText().toString().trim();
		String detailStr = detailDemandEt.getText().toString().trim();
		String budgetStr = budgetEt.getText().toString().trim();
		String timeStr = timeEt.getText().toString().trim();
		bundle.putString("typeStr", typeStr);
		bundle.putString("nameStr", nameStr);
		String phoneStr = "";
		if(phoneOneStr.length() == 0){
			phoneStr = phoneTwoStr;
		}else if(phoneTwoStr.length() == 0){
			phoneStr = phoneOneStr;
		}else{
			phoneStr = phoneOneStr+";"+phoneTwoStr;
		}
		bundle.putString("phoneStr", phoneStr);
		
		String emailStr = "";
		if(emailOneStr.length() == 0){
			emailStr = emailTwoStr;
		}else if(phoneTwoStr.length() == 0){
			emailStr = emailOneStr;
		}else{
			emailStr = emailOneStr+";"+emailTwoStr;
		}
		bundle.putString("emailStr", emailStr);
		bundle.putString("qqStr", qqStr);
		bundle.putString("detailStr", detailStr);
		bundle.putString("budgetStr", budgetStr);
		bundle.putString("timeStr", timeStr);
		for(int i=0;i<fileIdList.size();i++){
			fileIdStr += (fileIdList.get(i).get("currentFileId").toString()+",");
		}
		fileIdStr = fileIdStr.substring(0, fileIdStr.length()-1);
		bundle.putString("fileIdStr", fileIdStr);
		return bundle;
	}
//	public ArrayList<String> checkList = new ArrayList<String>();//�����洢�������͵�����ֵ
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String type = buttonView.getTag().toString();
		if(isChecked){//����ö�ѡ��ť��ѡ�ж���û�б���ӹ� �򽫸����ͼ����ַ�����
			if(!typeStr.contains(type)){
				typeStr+=(typeStr.length()>0?(","+type):type);
			}
		}else{//���ȡ��ѡ����ɾ���ַ����еĸ�����
			if(typeStr.indexOf(type)==0){
				typeStr = typeStr.replace(typeStr.contains(type+",")?(type+","):type, "");
			}else{
				typeStr = typeStr.replace(","+type, "");
			}
			
		}
	}
	/**
	 * �ֶ���֤�ĵ�����
	 * @param msg
	 */
	private void showDialog(String msg,String btnStr,final boolean finishActivity,final boolean setView){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("��ʾ!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		if(setView){
			View view = LayoutInflater.from(this).inflate(R.layout.customprogressdialog,null);
			alerBuilder.setView(view);
		}else{
			alerBuilder.setMessage(msg);
			alerBuilder.setPositiveButton(btnStr, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				if(finishActivity){
					SubmitDemandActivity.this.dialog.dismiss();
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
					isStopInfoUpload = true;
					isStopFileUpload = false;
					
					dialog.dismiss();
				return ;
				}
			});
			alerBuilder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog1, int which) {
					dialog.dismiss();
					showDialog("�����ϴ�","",false,true);
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

	private void findview(){
		addLayout = (LinearLayout) findViewById(R.id.addJianLiLayout);
		jianLiLayout = (LinearLayout) findViewById(R.id.JianLiLayout);
		addLayout.setOnClickListener(this);
		scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);
		nameEt = (EditText) findViewById(R.id.nameEt);
		phoneOneEt = (EditText) findViewById(R.id.phoneoneEt);
		phoneTwoEt = (EditText) findViewById(R.id.phonetwoEt);
		emailOneEt = (EditText) findViewById(R.id.emailoneEt);
		emailTwoEt = (EditText) findViewById(R.id.emailtwoEt);
		qqEt = (EditText) findViewById(R.id.qqEt);
		detailDemandEt = (EditText) findViewById(R.id.detailDemandEt);
		budgetEt = (EditText) findViewById(R.id.moneyEt);
		timeEt = (EditText) findViewById(R.id.timeEt);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		check1 = (CheckBox) findViewById(R.id.check1);
		check2 = (CheckBox) findViewById(R.id.check2);
		check3 = (CheckBox) findViewById(R.id.check3);
		check4 = (CheckBox) findViewById(R.id.check4);
		check5 = (CheckBox) findViewById(R.id.check5);
		check6 = (CheckBox) findViewById(R.id.check6);
		check7 = (CheckBox) findViewById(R.id.check7);
		check1.setOnCheckedChangeListener(this);
		check2.setOnCheckedChangeListener(this);
		check3.setOnCheckedChangeListener(this);
		check4.setOnCheckedChangeListener(this);
		check5.setOnCheckedChangeListener(this);
		check6.setOnCheckedChangeListener(this);
		check7.setOnCheckedChangeListener(this);
		check1.setTag("1");
		check2.setTag("2");
		check3.setTag("3");
		check4.setTag("4");
		check5.setTag("5");
		check6.setTag("6");
		check7.setTag("7");
		sumbitBtn = (Button) findViewById(R.id.sumbitBtn);
		sumbitBtn.setOnClickListener(this);
	}
}
