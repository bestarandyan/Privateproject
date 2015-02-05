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
 * @author 刘星星
 * @createDate 2013/7/25
 * 申请职位类
 *
 */
public class ApplyJobActivity extends Activity implements OnClickListener{
	private ImageButton backBtn;//返回按钮
	private EditText nameEt,ageEt,phoneEt,emailEt,explainEt;
	private TextView genderTv;//性别选择控件
	private LinearLayout addLayout;//添加附件布局  简历布局
	private Button submitBtn; //提交职位申请按钮
	private ImageView fileImageView;
	private TextView fileTv,fileNameTv;
	ImageButton cancleFile;
	private PopupWindow selectPopupWindow = null;
	private String filePath = "";//简历文件的路劲；
	File jianLifile = null;
	public DBHelper dbHelper;
	public int uploadMax = 256*1000;//一次上传文件的最大值  单位：字节
	public float currentUploadPro = 0.0f;//当前上传进度
	public boolean clickSubmitBtn = false;//判断是否点击了提交按钮
	public String currentFileId = "";//当前文件在服务器的编号
	ProgressBar progressBar = null;
	TextView currentProText = null;
	ProgressDialog pDialog = null;
	private CustomProgressDialog progressDialog = null;
	public  static boolean isStopInfoUpload = false;//标记是否停止信息上传
	public  static boolean isStopFileUpload = false;//标记是否停止文件上传
	
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
		if(v == backBtn){//返回键按钮事件监听
			isStopInfoUpload = true;
			isStopFileUpload = true;
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
		}else if(v == genderTv){//选择性别
			if(selectPopupWindow!=null && selectPopupWindow.isShowing()){
				dismiss();//隐藏掉已经显示的控件
			}else{
				showPopupWindow();
			}
		}else if(v.getId() == R.id.man){//选择男人
			dismiss();
			genderTv.setText("男");
		}else if(v.getId() == R.id.woman){//选择女人
			dismiss();
			genderTv.setText("女");
		}else if(v == addLayout){//点击这里添加附件
			FileListViewActivity.suffix = ".doc;.docx;.pdf;";
			Intent intent = new Intent(this,FileListViewActivity.class);
			startActivityForResult(intent, 1);
		}else if(v == cancleFile){//删除文件
			fileImageView.setImageResource(R.drawable.qf_fujian);
			fileTv.setVisibility(View.VISIBLE);
			fileNameTv.setText("");
			cancleFile.setVisibility(View.INVISIBLE);
			filePath = "";//简历文件的路劲；
			jianLifile = null;
			isStopFileUpload = true;
			progressBar.setProgress(0);
			currentProText.setVisibility(View.GONE);
		}else if(v == submitBtn){//提交简历
			if(nameEt.getText().toString().trim().length() == 0){
				showDialog("请填写您的姓名！",false,false);
			}else if(ageEt.getText().toString().trim().length() == 0){
				showDialog("请填写您的年龄！",false,false);
			}else if(Integer.parseInt(ageEt.getText().toString().trim())<18 || Integer.parseInt(ageEt.getText().toString().trim()) >80 ){
				showDialog("年龄须在18-80之间！",false,false);
			}else if(phoneEt.getText().toString().trim().length() == 0){
				showDialog("请填写您的手机号或座机号！",false,false);
			}else if(emailEt.getText().toString().trim().length() == 0){
				showDialog("请填写您的电子邮箱！",false,false);
			}else if(!RegularExpression.isEmail(emailEt.getText().toString().trim())){
				showDialog("请填写有效的电子邮箱！",false,false);
			}else if(jianLifile ==null || filePath.equals("")){
				showDialog("请添加简历！",false,false);
			}else if(!jianLifile.exists()){
				showDialog("您添加的简历文件不存在，请重新选择！",false,false);
			}else{
//				showProgressDidlaog();
//				startProgressDialog();
				showDialog("正在上传",false,true);
				clickSubmitBtn = true;
				isStopInfoUpload = false;
				isStopFileUpload = false;
				if(currentUploadPro == 1){//如果简历文件已经上传完毕 才开启线程上传其他信息
					new Thread(applyJobInfoRunnable).start();
				}
			}
			
		}
		
	}
	/**
	 * 上传文件第一段
	 */
	Runnable applyJobFileRunnable = new Runnable() {
		
		@Override
		public void run() {
			uploadFirst();
		}
	};
	/**
	 * 上传文件第二段和第二段以后的所有段
	 */
	Runnable upLoadPiece = new Runnable() {
		
		@Override
		public void run() {
			currentPiece ++;
			System.out.println("当前块0000000000000000000000=="+currentPiece);
			uploadPiece(currentPiece);
			
		}
	};
	int currentPiece = 1;//当前正在上传的文件段
	int upNumber = 0;//总共要上传的块数
	int utilPro = 0;//每一次上传成功后进度条要上升的单位进度。
	byte[] fileByte = null;//当前上传文件转化成为字节数组
	  /**
     * 上传文件的第一段
     */
    private void uploadFirst(){
    	try{
    		File file = new File(filePath);
        	if(!file.exists())
        		return;
        	uploading = true;
        	String fileName = new File(filePath).getName().toString().trim();
    		handler.sendEmptyMessage(5);//告诉界面正在上传。
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
	        if(upNumber>1){//大于1块的时候
	        		params.put("length", (uploadMax)+"");
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
        		handler.sendEmptyMessage(0);
    	}catch(Exception e){
    		System.out.println("上传第一块时出现问题了，会跳过该文件上传下一个文件，本次出问题的原因为："+e.getMessage());
    		handler.sendEmptyMessage(4);
    		e.printStackTrace();
    	}
    }
    boolean uploading = false;//标记是否正在上传
    /**
     * 上传文件的所有部分不包括第一段
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
        		handler.sendEmptyMessage(0);

    	}catch(Exception e){
    		System.out.println("上传第"+currentPiece+"块时出现问题了，会跳过该文件上传下一个文件，本次出问题的原因为："+e.getMessage());
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
			bundle.putString("gender", genderTv.getText().toString().trim().equals("男")?"1":"2");
			bundle.putString("age", ageEt.getText().toString().trim());
			bundle.putString("phone_number", phoneEt.getText().toString().trim());
			bundle.putString("email", emailEt.getText().toString().trim());
			bundle.putString("resume", currentFileId);
			bundle.putString("message", explainEt.getText().toString().trim());
			String msgStr = AccessServer.submitJobAsk(bundle);
			System.out.println(msgStr);
			String[] msgArray = msgStr.split(",");
			if(msgArray[0].equals("0")){//提交成功
				handler.sendEmptyMessage(3);
			}else if(msgArray[0].equals("404")){//访问服务器失败
				handler.sendEmptyMessage(2);
			}else{//提交失败
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
				if(currentPiece == 1 && !isStopFileUpload){//当第一段上传完了之后，再上传后面的段
					for(int i=2;i<=upNumber;i++){
						new Thread(upLoadPiece).start();
					}
				}
				if(progressBar.getProgress() == 100 && currentPiece == upNumber){
					if(clickSubmitBtn && !isStopInfoUpload){//如果简历文件上传完了   看有没有点击过提交按钮，如果有才开始上传信息
						new Thread(applyJobInfoRunnable).start();
					}
				}
			}else if(msg.what == 1){//访问服务器出错
				Toast.makeText(getApplicationContext(), "无数据", 3000).show();
//				 stopProgressDialog();
			}else if(msg.what == 2){//访问服务器失败
				Toast.makeText(getApplicationContext(), "404", 3000).show();
//				stopProgressDialog();
			}else if(msg.what == 3){//简历信息和简历文件提交成
//				stopProgressDialog();
				if(!isStopInfoUpload){
					dialog.dismiss();
					showDialog("职位申请提交成功，我司会尽快联系您！",true,false);
				}
				
			}else if(msg.what == 4){
				currentProText.setText("上传失败");
				currentProText.setTextSize(12);
				currentProText.setTextColor(Color.RED);
			}else if(msg.what == 5){
				currentProText.setText("正在上传...");
				currentProText.setTextSize(12);
				currentProText.setTextColor(Color.BLACK);
			}
			super.handleMessage(msg);
		}
		
	};
	private void showProgressDidlaog(){
		pDialog = new ProgressDialog(this);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("正在为您提交职位申请，请稍候！");
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
			if(jianLifile.exists()){//判断文件是否存在
				fileByte = FileHandler.fileToByte(filePath);//将文件转成字节数组
	        	if(fileByte.length>1024*10*1000){
	        		showDialog("文件大小不能超过10M，请减小文件或者把文件拆分成多个！", false, false);
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
		initPopuWindow();//初始化弹出框
	}
	/**
	 * 初始化PopupWindow
	 */
	private void initPopuWindow() {
		View v = LayoutInflater.from(this).inflate(R.layout.gender_select, null);
		int pwidth = com.qingfengweb.client.data.MyApplication.getInstant().getScreenW()/2-23;
		v.findViewById(R.id.man).setOnClickListener(this);
		v.findViewById(R.id.woman).setOnClickListener(this);
		selectPopupWindow = new PopupWindow(v, pwidth, LayoutParams.WRAP_CONTENT,true);//这里的true很重要，为true时点击原来的控件窗口才会消失
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		// 本人能力极其有限，不明白其原因，还望高手、知情者指点一下
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
	 * PopupWindow消失
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
	 * 字段验证的弹出框
	 * @param msg
	 */
	private void showDialog(String msg,final boolean finishActivity,final boolean setView){
		AlertDialog.Builder alerBuilder = new AlertDialog.Builder(this);
		alerBuilder.setTitle("提示!");
		alerBuilder.setIcon(android.R.drawable.ic_dialog_alert);
		if(setView){
			View view = LayoutInflater.from(this).inflate(R.layout.customprogressdialog,null);
			alerBuilder.setView(view);
		}else{
			alerBuilder.setMessage(msg);
			alerBuilder.setPositiveButton("知道了！", new DialogInterface.OnClickListener() {
				
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
					dialog.dismiss();
					isStopInfoUpload = true;
					isStopFileUpload = false;
				return ;
				}
			});
			alerBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog1, int which) {
					dialog.dismiss();
					showDialog("正在上传",false,true);
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
//			progressDialog.setMessage("正在提交职位申请，请稍候！");
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
