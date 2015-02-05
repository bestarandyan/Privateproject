/**
 * 
 */
package com.piaoguanjia.chargeclient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/5/7
 * 审核界面
 *
 */
public class ExamineChargeActivity extends Activity implements OnClickListener,OnTouchListener{
	private TextView idTv,userName,chargeType,chargeAccount,chargeProduce,chargeMoney,countCharge;
	private TextView accountMoney,chargeDate,examineDate,chargeState,reasonTextView,chargeManagerTv,examineManagerTv;
	private EditText remarks;
	private Button chargeDetail,chargeImg;
	private ImageView backBtn;
	private Button chargePass,chargeRefuse;
	private RelativeLayout produceLayout;
	private ScrollView detailLayout;
	private LinearLayout photoLayout;
	private String chargeIdString = null;//充值id
	private boolean historyFlag = false;//历史充值标记
	ProgressDialog progress;
	public Bundle bundle = null;
	public RelativeLayout examineLayout;
	private LinearLayout reasonLayout;
	public Bitmap imgBitmap;
	public ZoomImageView photoImageView;
	public TextView photoTextView;
	public String responseMsg = "";//获取凭证图片失败时的返回值
	private ProjectDBHelper dbHelper;
	private SQLiteDatabase dbDatabase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_examinecharge);
		findView();
		initData();
	}
	@Override
	protected void onDestroy() {
		dbDatabase.close();
		super.onDestroy();
	}
	private void initData(){
		dbHelper = new ProjectDBHelper(this);
		dbDatabase = dbHelper.getWritableDatabase();
		dbDatabase.execSQL(Constant.IMAGESQL_STRING);
		chargeIdString = getIntent().getStringExtra("chargeId").toString().trim();
		historyFlag = getIntent().getBooleanExtra("historyFlag", false);
		if(historyFlag){
			chargePass.setVisibility(View.GONE);
			chargeRefuse.setVisibility(View.GONE);
		}
		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
        	dialogWronFun("未检测到网络，请检查您的网络连接！",ExamineChargeActivity.this);
        	Cursor cursor = dbDatabase.query(Constant.CHARGE_RECORD_TABLE_NAME, 
        			null, "chargeid=?", new String[]{chargeIdString}, null, null, null);
        	if(cursor!=null){
        		cursor.moveToFirst();
        		idTv.setText(cursor.getString(cursor.getColumnIndex("chargeid")));
				userName.setText(cursor.getString(cursor.getColumnIndex("username")));
				chargeType.setText(cursor.getString(cursor.getColumnIndex("chargeTypeDistription")));
				chargeAccount.setText(cursor.getString(cursor.getColumnIndex("accountTypeDis")));
				if (cursor.getString(cursor.getColumnIndex("objectName"))!=null && cursor.getString(cursor.getColumnIndex("objectName")).trim().length()>0) {
					produceLayout.setVisibility(View.VISIBLE);
					chargeProduce.setText(cursor.getString(cursor.getColumnIndex("objectName")));
				}else{
					produceLayout.setVisibility(View.GONE);
				}
				chargeMoney.setText(cursor.getString(cursor.getColumnIndex("amount")));
				countCharge.setText(cursor.getString(cursor.getColumnIndex("totalAmount")));
				accountMoney.setText(cursor.getString(cursor.getColumnIndex("balance")));
				chargeDate.setText(cursor.getString(cursor.getColumnIndex("createTime")));
				if(cursor.getString(cursor.getColumnIndex("auditTime"))!=null && cursor.getString(cursor.getColumnIndex("auditTime")).trim().length()>0){
					examineLayout.setVisibility(View.VISIBLE);
					examineDate.setText(cursor.getString(cursor.getColumnIndex("auditTime")));
				}else{
					examineLayout.setVisibility(View.GONE);
				}
				
				String operaString = cursor.getString(cursor.getColumnIndex("operator"));
				if(operaString!=null && operaString.trim().length()>0){
					chargeManagerTv.setVisibility(View.VISIBLE);
					operaString = "["+operaString+"]";
					chargeManagerTv.setText(operaString);
				}else{
					chargeManagerTv.setVisibility(View.GONE);
				}
				String auditString = cursor.getString(cursor.getColumnIndex("auditor"));
				if(auditString!=null && auditString.trim().length()>0){
					examineManagerTv.setVisibility(View.VISIBLE);
					auditString = "["+auditString+"]";
					examineManagerTv.setText(auditString);
				}else{
					examineManagerTv.setVisibility(View.GONE);
				}
				String statusString = cursor.getString(cursor.getColumnIndex("status"));
				if(statusString!=null && statusString.equals("0")){
					statusString = "充值中";
				}else if(statusString.equals("1")){
					statusString = "审核成功";
				}else if(statusString.equals("2")){
					statusString = "审核不通过";
				}
				remarks.setText(cursor.getString(cursor.getColumnIndex("remark")));
				chargeState.setText(statusString);
				if(cursor.getString(cursor.getColumnIndex("reason"))!=null && cursor.getString(cursor.getColumnIndex("reason")).trim().length()>0){
					reasonLayout.setVisibility(View.VISIBLE);
					reasonTextView.setText(cursor.getString(cursor.getColumnIndex("reason")));
				}else{
					reasonLayout.setVisibility(View.GONE);
				}
				cursor.close();
        	}
        }else {
        	createProgress();
    		progress.setMessage("正在加载数据，请稍候！");
    		new Thread(getDataRunnable).start();
		}
		
	}
	Runnable getDataRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msgString = getDataFromNet(chargeIdString);
			if(msgString.length()<=3){
				handler.sendEmptyMessage(1);//获取数据失败
			}else{
				bundle = jsonExamineToBundle(msgString);
				handler.sendEmptyMessage(2);//获取数据成功
			}
		}
	};
	/**
	 * 获取图片线程
	 */
	Runnable getImageRunnable = new Runnable() {
		
		@Override
		public void run() {
			String imgsString = null;
			if(bundle!=null){
				imgsString = bundle.getString("image");
			}
			if(imgsString ==null){
				handler.sendEmptyMessage(6);
			}else {
				if(imgsString.equals("1")){
					handler.sendEmptyMessage(4);
				}else if(imgsString.equals("-27")){
					handler.sendEmptyMessage(5);
				}else {
					imgBitmap = getImageStream();
					if(imgBitmap==null){
						if (responseMsg.equals("-28")) {//下载失败
							handler.sendEmptyMessage(6);
						}else{//其他原因没有下载到图片
							handler.sendEmptyMessage(7);
						}
					}else {//图片下载成功
						File file = new File(Environment.getExternalStorageDirectory()+"/piaoguanjiaCharge/",CommonUtils.getFileName());
						boolean isSuccess = CommonUtils.OutPutImage(file, imgBitmap);
						if(isSuccess){
							ContentValues values = new ContentValues();
							values.put("chargeid", chargeIdString);
							values.put("imageurl", file.getAbsolutePath());
							dbDatabase.insert(Constant.IMAGE_TABNAME_STRING, null, values);
						}
						handler.sendEmptyMessage(3);
					}
				}
			}
			
		}
	};
	Runnable refuseRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = examineCharge(chargeIdString,"0",refuseEt.getText().toString().trim());
			if(msg.equals("1")){//成功
				handler.sendEmptyMessage(8);//
			}else if(msg.equals("-25")){//没有权限
				handler.sendEmptyMessage(9);
			}else {//审核失败
				handler.sendEmptyMessage(10);
			}
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){//弹出框消失后隐藏键盘
				CommonUtils.hideSoftKeyboard(ExamineChargeActivity.this);
			}else if (msg.what == 1) {//获取数据失败
				progress.dismiss();
				dialogWronFun("获取数据失败!",ExamineChargeActivity.this);
			}else if (msg.what == 2) {//获取数据成功
				ContentValues values = new ContentValues();
				values.put("chargeid", bundle.getString("chargeid"));
				values.put("username", bundle.getString("username"));
				values.put("chargeTypeDistription", bundle.getString("chargeTypeDistription"));
				values.put("accountTypeDis", bundle.getString("accountTypeDis"));
				values.put("objectName", bundle.getString("objectName"));
				values.put("amount", bundle.getString("amount"));
				values.put("totalAmount", bundle.getString("totalAmount"));
				values.put("balance", bundle.getString("balance"));
				values.put("createTime", bundle.getString("createTime"));
				values.put("auditTime", bundle.getString("auditTime"));
				values.put("status", bundle.getString("status"));
				values.put("remark", bundle.getString("remark"));
				values.put("reason", bundle.getString("reason"));
				values.put("image", bundle.getString("image"));
				values.put("operator", bundle.getString("operator"));
				values.put("auditor", bundle.getString("auditor"));
				dbDatabase.update(Constant.CHARGE_RECORD_TABLE_NAME, values, "chargeid=?", new String[]{bundle.getString("chargeid")});
				idTv.setText(bundle.getString("chargeid"));
				userName.setText(bundle.getString("username"));
				chargeType.setText(bundle.getString("chargeTypeDistription"));
				chargeAccount.setText(bundle.getString("accountTypeDis"));
				if (bundle.getString("objectName").trim().length()>0) {
					produceLayout.setVisibility(View.VISIBLE);
					chargeProduce.setText(bundle.getString("objectName"));
				}else{
					produceLayout.setVisibility(View.GONE);
				}
				String operaString = bundle.getString("operator");
				if(operaString!=null && operaString.trim().length()>0){
					chargeManagerTv.setVisibility(View.VISIBLE);
					operaString = "["+operaString+"]";
					chargeManagerTv.setText(operaString);
				}else{
					chargeManagerTv.setVisibility(View.GONE);
				}
				String auditString = bundle.getString("auditor");
				if(auditString!=null && auditString.trim().length()>0){
					examineManagerTv.setVisibility(View.VISIBLE);
					auditString = "["+auditString+"]";
					examineManagerTv.setText(auditString);
				}else{
					examineManagerTv.setVisibility(View.GONE);
				}
				chargeMoney.setText(bundle.getString("amount"));
				countCharge.setText(bundle.getString("totalAmount"));
				accountMoney.setText(bundle.getString("balance"));
				chargeDate.setText(bundle.getString("createTime"));
				if(bundle.getString("auditTime").trim().length()>0){
					examineLayout.setVisibility(View.VISIBLE);
					examineDate.setText(bundle.getString("auditTime"));
				}else{
					examineLayout.setVisibility(View.GONE);
				}
				
				String statusString = bundle.getString("status");
				if(statusString.equals("0")){
					statusString = "充值中";
				}else if(statusString.equals("1")){
					statusString = "审核成功";
				}else if(statusString.equals("2")){
					statusString = "审核不通过";
				}
				remarks.setText(bundle.getString("remark"));
				chargeState.setText(statusString);
				if(bundle.getString("reason").trim().length()>0){
					reasonLayout.setVisibility(View.VISIBLE);
					reasonTextView.setText(bundle.getString("reason"));
				}else{
					reasonLayout.setVisibility(View.GONE);
				}
				
				progress.dismiss();
			}else if (msg.what == 3) {//下载图片成功
				progress.dismiss();
				if (imgBitmap!=null) {
					photoTextView.setVisibility(View.GONE);
					photoImageView.setVisibility(View.VISIBLE);
					photoImageView.setImage(imgBitmap);
				}else{
					photoTextView.setVisibility(View.VISIBLE);
					photoImageView.setVisibility(View.GONE);
				}
			}else if(msg.what == 4){//无图片
				progress.dismiss();
				photoTextView.setVisibility(View.VISIBLE);
				dialogWronFun("该充值无凭证！", ExamineChargeActivity.this);
			}else if(msg.what == 5){//已删除
				progress.dismiss();
				photoTextView.setVisibility(View.VISIBLE);
				dialogWronFun("未上传凭证!", ExamineChargeActivity.this);
			}else if(msg.what == 6){//下载失败
				progress.dismiss();
				photoTextView.setVisibility(View.VISIBLE);
				dialogWronFun("下载凭证失败！", ExamineChargeActivity.this);
			}else if(msg.what == 7){//字段错误
				progress.dismiss();
				photoTextView.setVisibility(View.VISIBLE);
				dialogWronFun("下载凭证失败！", ExamineChargeActivity.this);
			}else if(msg.what == 8){//审核成功
				if(progress!=null){
					progress.dismiss();
				}
				dialogPassWronFun("操作成功！");
			}else if(msg.what == 9){//没有权限
				if(progress!=null){
					progress.dismiss();
				}
				dialogWronFun("您没有权限审核！", ExamineChargeActivity.this);
			}else if(msg.what == 10){//审核失败
				if(progress!=null){
					progress.dismiss();
				}
				dialogWronFun("审核失败！", ExamineChargeActivity.this);
			}
			super.handleMessage(msg);
		}
	};
	public void dialogPassWronFun(CharSequence str){
      	AlertDialog.Builder alert=new AlertDialog.Builder(this);
      	alert.setMessage(str);
      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
					return;
				}
			});
      	alert.show();
      }
	
	public void dialogExaminePassWronFun(CharSequence str){
      	AlertDialog.Builder alert=new AlertDialog.Builder(this);
      	alert.setMessage(str);
      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					createProgress();
					progress.setMessage("正在审核，请稍候！");
					new Thread(examinePassRunnable).start();
					return;
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					
					return;
				}
			});
      	alert.show();
      }
	public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					new Thread(runnable).start();
					return;
				}
			});
      	alert.show();
      }
	/**
	 * 获取凭证图片
	 */
	private Bitmap getImageStream(){
		byte[] imgByte = null;
		Bitmap bitmap = null;
		String urlString = Constant.CONNECT+Constant.ADDCHARGE_INTEGERFACE;
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", Constant.APPID));
		params.add(new BasicNameValuePair("appkey", Constant.APPKEY));
		params.add(new BasicNameValuePair("action", "image"));
		params.add(new BasicNameValuePair("username", MyApplication.getInstance().getUsername()));
		params.add(new BasicNameValuePair("password", MyApplication.getInstance().getPassword()));
		params.add(new BasicNameValuePair("chargeid", chargeIdString));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			HttpResponse response = client.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
				bitmap = BitmapFactory.decodeStream(httpEntity.getContent());
			} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	private String getDataFromNet(String chargeid){
		String msgString = null;
		String urlString = Constant.CONNECT+Constant.ADDCHARGE_INTEGERFACE;
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", Constant.APPID));
		params.add(new BasicNameValuePair("appkey", Constant.APPKEY));
		params.add(new BasicNameValuePair("action", "detail"));
		params.add(new BasicNameValuePair("username", MyApplication.getInstance().getUsername()));
		params.add(new BasicNameValuePair("password", MyApplication.getInstance().getPassword()));
		params.add(new BasicNameValuePair("chargeid", chargeid));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			HttpResponse response = client.execute(httpPost);
			msgString = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgString;
	}
	
	public Bundle jsonExamineToBundle(String msg){
		Bundle bundle = new Bundle();
		Gson gson=new Gson();  
		ExamineDetailBean bean=gson.fromJson(msg, ExamineDetailBean.class); 
	    bundle.putString("chargeid", bean.getChargeid());
	    bundle.putString("username", bean.getUsername());
	    bundle.putString("chargeTypeDistription", bean.getChargeTypeDistription());
	    bundle.putString("accountTypeDis", bean.getAccountTypeDis());
	    bundle.putString("amount", bean.getAmount());
	    bundle.putString("totalAmount", bean.getTotalAmount());
	    bundle.putString("balance", bean.getBalance());
	    bundle.putString("createTime", bean.getCreateTime());
	    bundle.putString("auditTime", bean.getAuditTime());
	    bundle.putString("operator", bean.getOperator());
	    bundle.putString("auditor", bean.getAuditor());
	    bundle.putString("status", bean.getStatus());
	    bundle.putString("remark", bean.getRemark());
	    bundle.putString("reason", bean.getReason());
	    bundle.putString("objectName", bean.getObjectName());
	    bundle.putString("image", bean.getImage());
		return bundle;
	}
	private void findView(){
		detailLayout  = (ScrollView) findViewById(R.id.chargeDetialLayout);
		photoLayout = (LinearLayout) findViewById(R.id.PhotoLayout);
		idTv = (TextView) findViewById(R.id.idTv);
		userName = (TextView) findViewById(R.id.usernameTv);
		chargeType = (TextView) findViewById(R.id.chargeTypeTv);
		chargeAccount = (TextView) findViewById(R.id.chargeAccountTv);
		chargeProduce = (TextView) findViewById(R.id.chargePrduceTv);
		chargeMoney = (TextView) findViewById(R.id.chargeMoneyTv);
		countCharge = (TextView) findViewById(R.id.chargeCountTv);
		accountMoney = (TextView) findViewById(R.id.accountBalanceTv);
		reasonLayout = (LinearLayout) findViewById(R.id.reasonLayout);
		reasonTextView = (TextView) findViewById(R.id.reasonTv);
		examineLayout = (RelativeLayout) findViewById(R.id.examineLayout);
		chargeDate = (TextView) findViewById(R.id.chargeDateTv);
		examineDate = (TextView) findViewById(R.id.examineDateTv);
		photoTextView = (TextView) findViewById(R.id.photoTextView);
		chargeState = (TextView) findViewById(R.id.chargeStateTv);
		remarks = (EditText) findViewById(R.id.remarksEt);
		chargeDetail = (Button) findViewById(R.id.button1);
		chargeImg = (Button) findViewById(R.id.button2);
		backBtn = (ImageView) findViewById(R.id.backBtn);
		chargePass = (Button) findViewById(R.id.chargepass);
		chargeRefuse = (Button) findViewById(R.id.chargerefuse);
		produceLayout = (RelativeLayout) findViewById(R.id.produceLayout);
		photoImageView = (ZoomImageView) findViewById(R.id.imageViewPhoto);
		chargeManagerTv = (TextView) findViewById(R.id.chargeManagerTv);
		examineManagerTv = (TextView) findViewById(R.id.examineManagerTv);
		backBtn.setOnClickListener(this);
		chargeDetail.setOnClickListener(this);
		chargeImg.setOnClickListener(this);
		chargePass.setOnTouchListener(this);
		chargeRefuse.setOnTouchListener(this);
	}
	private void createProgress(){
    	if(progress!=null){
    		progress.cancel();
    	}
    	progress = new ProgressDialog(this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.setCanceledOnTouchOutside(false);
    	progress.setCancelable(false);
    	CommonUtils.hideSoftKeyboard(this);
    	progress.show();
    }
	@Override
	protected void onResume() {
		System.out.println("-------------------------onresume--------");
		super.onResume();
	}
	@Override
	protected void onRestart() {
		System.out.println("-------------------------onRestart--------");
		super.onRestart();
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v == chargeDetail){
			chargeDetail.setBackgroundResource(R.drawable.chargebg);
			chargeDetail.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			chargeImg.setBackgroundColor(Color.TRANSPARENT);
			chargeImg.setTextColor(Color.WHITE);
			detailLayout.setVisibility(View.VISIBLE);
			photoLayout.setVisibility(View.GONE);
			if(historyFlag){
				chargePass.setVisibility(View.GONE);
				chargeRefuse.setVisibility(View.GONE);
			}else{
				chargePass.setVisibility(View.VISIBLE);
				chargeRefuse.setVisibility(View.VISIBLE);
			}
		}else if(v == chargeImg){
			chargeImg.setBackgroundResource(R.drawable.chargebg);
			chargeImg.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			chargeDetail.setBackgroundColor(Color.TRANSPARENT);
			chargeDetail.setTextColor(Color.WHITE);
			detailLayout.setVisibility(View.GONE);
			chargePass.setVisibility(View.GONE);
			chargeRefuse.setVisibility(View.GONE);
			photoLayout.setVisibility(View.VISIBLE);
			
			File imageFile = null;
			String imageString = null;
			Cursor cursor = dbDatabase.query(Constant.IMAGE_TABNAME_STRING, null, "chargeid=?", 
					new String[]{chargeIdString}, null, null, null);
			if(cursor!=null){
				while (cursor.moveToNext()) {
					imageString = cursor.getString(cursor.getColumnIndex("imageurl"));	
				}
				if(imageString!=null){
					imageFile = new File(imageString);
				}
			}
			if(imageFile!=null && imageFile.exists()){
				imgBitmap = BitmapFactory.decodeFile(imageString);
			}
			if (imgBitmap!=null) {
				photoTextView.setVisibility(View.GONE);
				photoImageView.setVisibility(View.VISIBLE);
				photoImageView.setImage(imgBitmap);
			}else{
				ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {	
		        	dialogWronFun("未检测到网络，请检查您的网络连接！",ExamineChargeActivity.this);
		        }else {
		        	createProgress();
					progress.setMessage("正在下载凭证图片，请稍候！");
					new Thread(getImageRunnable).start();
				}
				
			}
		}
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
			if(v == chargePass){//审核通过
				dialogExaminePassWronFun("确定要通过审核吗？");
			}else if(v == chargeRefuse){//拒绝审核
				showDialogBusiness();
			}else if(v == submitBtnRefuse){//确认拒绝
				
				if(refuseEt.getText().toString().trim().length()<=0){
					dialogWronFun("请填写拒绝原因！", this);
				}else {
					new Thread(runnable).start();
					ad.dismiss();
					createProgress();
					progress.setMessage("正在提交请求！");
					new Thread(refuseRunnable).start();
				}
			}else if(v == cancleRefuse){//取消拒绝
				new Thread(runnable).start();
				ad.dismiss();
				
			}
		}
		return false;
	}
	Runnable examinePassRunnable = new Runnable() {
		
		@Override
		public void run() {
			String msg = examineCharge(chargeIdString,"1","");
			if(msg.equals("1")){//成功
				handler.sendEmptyMessage(8);//
			}else if(msg.equals("-25")){//没有权限
				handler.sendEmptyMessage(9);
			}else {//审核失败
				handler.sendEmptyMessage(10);
			}
		}
	};
	/**
	 * 向服务器发送审核通过的请求
	 * @param chargeId 充值编号
	 * @param ispass 是否通过  1代表通过  0代表不通过
	 * @param reason 不通过的原因
	 */
	private String examineCharge(String chargeid,String isPass,String reason){
		String msgString = null;
		String urlString = Constant.CONNECT+Constant.ADDCHARGE_INTEGERFACE;
		HttpPost httpPost = new HttpPost(urlString);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", Constant.APPID));
		params.add(new BasicNameValuePair("appkey", Constant.APPKEY));
		params.add(new BasicNameValuePair("action", "audit"));
		params.add(new BasicNameValuePair("username", MyApplication.getInstance().getUsername()));
		params.add(new BasicNameValuePair("password", MyApplication.getInstance().getPassword()));
		params.add(new BasicNameValuePair("chargeid", chargeid));
		params.add(new BasicNameValuePair("isPass", isPass));
		params.add(new BasicNameValuePair("reson", reason));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			HttpResponse response = client.execute(httpPost);
			msgString = EntityUtils.toString(response.getEntity());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgString;
	}
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendEmptyMessage(0);
		}
	};
	
	/**
	 * 点击商圈控件时弹出对话框
	 * @author 刘星星
	 * @createDate 2013//3/22
	 */
	AlertDialog ad;
	EditText refuseEt;
	Button submitBtnRefuse,cancleRefuse;
	public void showDialogBusiness(){
		ad = new AlertDialog.Builder(this).create();
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_examine_refuse, null);
		LayoutParams params = new LayoutParams((int) (MyApplication.getInstance().getScreenW()*0.95),
				LayoutParams.WRAP_CONTENT);
		refuseEt = (EditText) view.findViewById(R.id.editText1);
		submitBtnRefuse = (Button) view.findViewById(R.id.Btn1);
		cancleRefuse = (Button) view.findViewById(R.id.Btn2);
		submitBtnRefuse.setOnTouchListener(this);
		cancleRefuse.setOnTouchListener(this);
//		ad.addContentView(view, params);
		ad.setView(view);
		ad.show();
	}
}
