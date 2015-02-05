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
 * @author 刘星星
 * @createDate 2013/7/26
 * 提交需求类
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
	public String currentFileId = "";//当前文件在服务器的编号
	public String currentFileName = "";//当前文件的名称
	public String currentFilePath = "";//当前正在上传的文件路径
	public byte[] currentFileByte = null;//当前正在上传的文件字符数组
	public int currentUploadFile = 0;//标记当前正在上传的文件在fileList中的下标
//	public SetProBroadcast setProBroadcast = null;
	public static final String setProAction =  "com.qingfengweb.setProAction";
	public int utilPro = 0;//代表当前进度条每一次应该提升的进度
	public int upNumber = 0;//表示当前文件总共要上传的次数
	ProgressBar currentBar = null;//当前准备走动的进度条
	TextView currentProtv = null;//当前进度条显示百分比的控件
	public int countSuccess = 0;//成功上传文件个数。
	public boolean uploadOver = false;//标记是否所有文件上传完成。
	public boolean uploading = false;//标记正在上传当中
	public String typeStr = "";//需求类型 用逗号分开
	public String fileIdStr = "";//上传成功的文件的id 多个id之间用逗号分开
	public ArrayList<HashMap<String,String>> fileIdList = new ArrayList<HashMap<String,String>>();//上传成功的文件id集合 这儿集合的主要作用是  在删除上传成功了的文件的时候会用
	public Bundle infoBundle = new Bundle();//信息集合
	public boolean clickSubmitBtn = false;//判断是否点击了提交按钮
	public  static boolean isStopInfoUpload = false;//标记是否停止信息上传
	public  static boolean isStopFileUpload = false;//标记是否停止文件上传
	
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
				showDialog("文件大小不能超过10M，请减小文件或者把文件拆分成多个！", "知道了！", false, false);
				return ;
			}
			currentFileName = file.getName();
			if(fileList.size()>0){//判断该文件是否已经添加。
				for (int  i = 0;i < fileList.size();i++) {
					if(fileList.get(i).getString("path").equals(currentFileBundle.getString("path"))){
						Toast.makeText(SubmitDemandActivity.this, "该文件已添加，请选择其他文件！", 7000).show();
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
     * 上传图片到服务器
     * 
     * @param imageFile 包含路径
     */
	public int uploadMax = 256*1000;//一次上传文件的最大值  单位：字节
	public float currentUploadPro = 0.0f;//当前上传进度
	public int currentPiece = 1;//当前的块数
    public void uploadFile(String filePath) {
        try {
        	if(currentFileByte!=null){
        		currentFileByte = null;
        		System.gc();
        	}
            currentFileByte = fileToByte(filePath);//将文件转成字节数组
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
    		handler.sendEmptyMessage(5);//告诉界面正在上传。
    		upNumber = fileByte.length/uploadMax+(fileByte.length%uploadMax>0?1:0);
    		utilPro = 100/upNumber;
	    	Map<String, String> params = new HashMap<String, String>();
	        params.put("appid", AccessServer.APPID);
	        params.put("appkey", AccessServer.APPKEY);
	        params.put("action", AccessServer.SUBMIT_JOBFILE_ACTION);
	        params.put("type", getTypeOfFile(fileName));
	        System.out.println("目前上传的文件的类型为==========="+getTypeOfFile(fileName));
	        params.put("id", fileId);
	        params.put("filesize", fileByte.length+"");
	        params.put("start", "0");
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        if(upNumber>1){//大于1块的时候
	        		params.put("length", (uploadMax)+"");
	        		System.out.println("当前上传块=================="+currentPiece);
	        		System.out.println("当前上传块的起始位置=================="+0);
	        		System.out.println("当前上传数据长度=================="+uploadMax);
	        		//开始下载
	    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, 0, uploadMax,"file");
	    	        System.out.println("本次服务器的返回值=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        }else{//只有一块的时候
	        	params.put("length", (fileByte.length)+"");
        		//开始上传
    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, 0, fileByte.length,"file");
    	        System.out.println(msgStr);
	        }
	        	//解析数据
	            ArrayList<String> reList = JsonData.jsonFileUploadData(msgStr, dbHelper.open());
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
	        System.out.println("文件总长度=================="+fileByte.length);
	        Map<String, File> files  = new HashMap<String, File>();
	        files.put(file.getName(), file);
	        String msgStr =  "";
	        if(upNumber>1){//大于1块的时候
	        	if(currentPiece < upNumber){
	        		params.put("start", ""+(currentPiece-1)*uploadMax);
	        		params.put("length", (uploadMax)+"");
	        		System.out.println("当前上传块=================="+currentPiece);
	        		System.out.println("当前上传块的起始位置=================="+(currentPiece-1)*uploadMax);
	        		System.out.println("当前上传数据长度=================="+uploadMax);
	        		//开始下载
	    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, (currentPiece-1)*uploadMax, uploadMax*currentPiece,"file");
	    	        System.out.println("本次服务器的返回值=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        	}else if(currentPiece == upNumber){
	        		params.put("start", ""+(currentPiece-1)*uploadMax);
	        		params.put("length", (fileByte.length-(uploadMax*(currentPiece-1)))+"");
	        		System.out.println("当前上传块=================="+currentPiece);
	        		System.out.println("当前上传块的起始位置=================="+(currentPiece-1)*uploadMax);
	        		System.out.println("当前上传数据长度=================="+(fileByte.length-(uploadMax*(currentPiece-1))));
	        		//开始下载
	    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, (currentPiece-1)*uploadMax, fileByte.length,"file");
	    	        System.out.println("本次服务器的返回值=================="+msgStr);
	    	        System.out.println("------------------------------------------------");
	        	}
	        }else{//只有一块的时候
	        	params.put("start", "0");
	        	params.put("length", (fileByte.length)+"");
        		//开始下载
    	        msgStr =  HttpPostUploadFile.post(this,AccessServer.FILE_UPLOAD_INTERFACE, params, files, 0, fileByte.length,"file");
    	        System.out.println(msgStr);
	        }
	        	//解析数据
	            ArrayList<String> reList = JsonData.jsonFileUploadData(msgStr, dbHelper.open());
	            currentUploadPro = Float.parseFloat(reList.get(1));//设置当前进度
	            currentFileId = reList.get(0);
               
        		handler.sendEmptyMessage(3);

    	}catch(Exception e){
    		System.out.println("上传第"+currentPiece+"块时出现问题了，会跳过该文件上传下一个文件，本次出问题的原因为："+e.getMessage());
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
				Toast.makeText(SubmitDemandActivity.this, msg.obj.toString()+"提交成功！", 3000).show();
			}else if(msg.what == 2){//第一段提交成功
				currentBar.setProgress(currentBar.getProgress()+utilPro);
				currentBar.getProgressDrawable().setAlpha(80);
				currentProtv.setText(currentBar.getProgress()+"%");
				System.out.println(currentPiece+"当前进度条的高度值："+currentBar.getProgress());
				currentProtv.setTextSize(20);
				if(upNumber == 1){//文件大小不大于最大上传量
					if(currentBar.getProgress() == 100){
						currentPiece = 1;
						currentUploadFile++;
						if(currentUploadFile<fileList.size()){//如果正在上传的文件只需要上传一次则上传一次后就开始上传下一个文件
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
				}else if(upNumber>1 && !isStopFileUpload){//需要多次上传的文件，从这里开始上传后面的片段
					for(int i=2;i<=upNumber;i++){
						new Thread(upLoadPiece).start();
					}
				}
			}else if(msg.what ==3 ){//上传成功，可能是片段 可能是整个文件
				currentBar.setProgress(currentBar.getProgress()+utilPro);
				currentBar.getProgressDrawable().setAlpha(80);
				currentProtv.setText(currentBar.getProgress()+"%");
				currentProtv.setTextSize(20);
				System.out.println(currentPiece+"当前进度条的高度值："+currentBar.getProgress());
//				Intent intent = new Intent();
//				intent.setAction("com.qingfengweb.setProAction");
//				context.sendBroadcast(intent);
				System.out.println("当前块--------------"+currentPiece);
				if(currentBar.getProgress() == 100 && currentPiece == upNumber){//一个文件上传成功
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
					if(!isStopFileUpload){//判断是否需要停止上传
						if(currentUploadFile<fileList.size()){//接着上传下一个文件
							new Thread(uploadFileStart).start();
						}else{
							uploadOver = true;
							uploading =  false;
							if(clickSubmitBtn){//当文件上传完了之后，判断是否点击过提交按钮，如果点击过 这直接上传信息
								infoBundle = getInfo();
								new Thread(submitInfoRunnable).start();
							}
						}
					}
				}
			}else if(msg.what == 4){//如果某一个文件上传失败 则继续进入下一个文件的上传
				if(currentFileByte!=null){
	        		currentFileByte = null;
	        		System.gc();
	        	}
				currentProtv.setText("上传失败");
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
			}else if(msg.what == 5){//告诉用户正在上传，
				currentProtv.setText("上传中...");
				currentProtv.setTextSize(12);
			}else if(msg.what == 6){//信息和文件都上传成功。
				dialog.dismiss();
				showDialog("需求提交成功，我司会尽快与您联系！", "知道了！", true, false);
			}else if(msg.what == 7){//连接服务器失败
				
			}else if(msg.what == 8){//提交失败。
				
			}
			
			super.handleMessage(msg);
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
		 * 提交信息的线程
		 */
		Runnable submitInfoRunnable = new Runnable() {
			
			@Override
			public void run() {
				String msg = AccessServer.submitDevelopDemand(infoBundle);
				System.out.println("提交需求信息时返回的数据为："+msg);
				String[] msgArray = msg.split(",");
				if(msgArray!=null && msgArray[0].equals("0")){//提交成功
					handler.sendEmptyMessage(6);
				}else if(msg.equals("404")){
					handler.sendEmptyMessage(7);//连接服务器失败
				}else{
					handler.sendEmptyMessage(8);//上传失败
				}
				
			}
		};
	/**
	 * 根据文件名称获取文件类型
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
	 * 用于在获取照片回来之后，让滚动条的位置能停留在最下面，这样用户体验会好一些，不用再去手动滚到最下面去获取更多的图片
	 * 当确定了滚动条之后，再让其能获取焦点
	 * 
	 * @createDate 2013/1/22
	 * @author 刘星星
	 * @param focus
	 *            true代表能获取焦点，false代表失去焦点
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
		proTextView.setText("等待上传...");
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
		if(v == addLayout){//添加需求文件
			if(fileList!=null && fileList.size()>=10){
				Toast.makeText(SubmitDemandActivity.this, "您选择的文件数量已达上限！", 7000).show();
			}else{
				clearEditTextFocus();
				FileListViewActivity.suffix = ".doc;.docx;.pdf;.xls;.xlsx;.ppt;.pptx;.jpg;.png;.rar;.zip;.gz;.psd;.ai";
				Intent intent = new Intent(this,FileListViewActivity.class);
				startActivityForResult(intent, 1);
			}
			
		}else if(v == backBtn){//返回按钮
			isStopInfoUpload = true;
			isStopFileUpload = true;
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v == sumbitBtn){//提交需求按钮
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
				showDialog("请至少选择一种需求类型！","去选择需求类型！", false, false);
			}else if(nameStr.length() == 0){
				showDialog("怎么称呼您？","去填写称呼！", false, false);
			}else if(phoneOneStr.length() == 0 && phoneTwoStr.length() == 0){
				showDialog("请至少告诉我们一个联系电话，好让我们可以联系到您！","去填写联系电话！", false, false);
			}else if(emailOneStr.length() == 0 && emailTwoStr.length() == 0){
				showDialog("请至少告诉我们一个电子邮箱地址，方便我们的沟通！","去填写电子邮箱地址！", false, false);
			}else if((emailOneStr.length()>0 && !RegularExpression.isEmail(emailOneStr)) ||
					(emailTwoStr.length()>0 && !RegularExpression.isEmail(emailTwoStr))){
				showDialog("请填写有效的电子邮箱！","去修改邮箱地址！",false,false);
			}else if(detailStr.length() == 0){
				showDialog("请填写需求详情，让我们了解您的需求！","去填写需求详情！", false, false);
			}else if(fileIdList == null || fileIdList.size()==0){
				showDialog("请添加需求附件！","知道了",false,false);
			}else{
				clickSubmitBtn = true;
				showDialog("正在提交需求，请稍候", "", false, true);
				if(uploadOver && fileIdList!=null && fileIdList.size()>0){//判断文件上传是否已经完毕
					isStopInfoUpload = false;
					isStopFileUpload = false;
					infoBundle = getInfo();
					new Thread(submitInfoRunnable).start();
				}else if(isStopFileUpload){
					System.out.println("停止过上传，点击这里表示接着上传");
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
//	public ArrayList<String> checkList = new ArrayList<String>();//用来存储需求类型的类型值
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String type = buttonView.getTag().toString();
		if(isChecked){//如果该多选按钮被选中而且没有被添加过 则将该类型加入字符串中
			if(!typeStr.contains(type)){
				typeStr+=(typeStr.length()>0?(","+type):type);
			}
		}else{//如果取消选中则删除字符串中的该类型
			if(typeStr.indexOf(type)==0){
				typeStr = typeStr.replace(typeStr.contains(type+",")?(type+","):type, "");
			}else{
				typeStr = typeStr.replace(","+type, "");
			}
			
		}
	}
	/**
	 * 字段验证的弹出框
	 * @param msg
	 */
	private void showDialog(String msg,String btnStr,final boolean finishActivity,final boolean setView){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示!");
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
						showDialog("此操作会暂停职位申请，确定暂停吗？");
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
	 * 字段验证的弹出框
	 * @param msg
	 */
	private void showDialog(String msg){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
			alerBuilder.setMessage(msg);
			alerBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog1, int which) {
					isStopInfoUpload = true;
					isStopFileUpload = false;
					
					dialog.dismiss();
				return ;
				}
			});
			alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog1, int which) {
					dialog.dismiss();
					showDialog("正在上传","",false,true);
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
