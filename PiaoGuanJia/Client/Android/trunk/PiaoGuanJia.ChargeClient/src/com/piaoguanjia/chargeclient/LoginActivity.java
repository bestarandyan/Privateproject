/**
 * 
 */
package com.piaoguanjia.chargeclient;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author ������
 * @createDate 2013/5/6
 * ��½����
 *
 */
public class LoginActivity extends Activity implements OnClickListener{
	private  EditText userNameEt;//�û���
	private EditText passwordEt;//����
	private Button loginBtn;//��½��ť
	private String userName = "";
	private String password = "";
	public ProgressDialog progress;
	String msg = "e";
	private Connect connect;
	public Bundle loginBundle = null;//��½�ɹ��󷵻ص����ݼ���
	public ProjectDBHelper dbHelper;
	SQLiteDatabase loginDB;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_login);
		userNameEt = (EditText) findViewById(R.id.userNameEt);
		passwordEt = (EditText) findViewById(R.id.passwordEt);
		loginBtn = (Button) findViewById(R.id.loadBtn);
		loginBtn.setOnClickListener(this);
		connect = new Connect(this);
		dbHelper = new ProjectDBHelper(LoginActivity.this);
		loginDB = dbHelper.getWritableDatabase();
		Cursor c = loginDB.query(Constant.USER_TABLE_NAME, null,
				null, null, null, null, null);
		int a = c.getCount();
		while(c.moveToNext()){
			userName = c.getString(c.getColumnIndex("username"));
			password = c.getString(c.getColumnIndex("password"));
		}
		loginDB.close();
		userNameEt.setText(userName);
		passwordEt.setText(password);
		userNameEt.addTextChangedListener(new watcher());
	}
	
	@Override
	public void onClick(View v) {
		if(v == loginBtn){
			login();
//			Intent intent = new Intent(this,MainActivity.class);
//			startActivity(intent);
//			finish();
		}
	}
	class watcher implements TextWatcher{
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(!userNameEt.getText().toString().equals(userName)){
				passwordEt.setText("");
			}
		}
	};
	private void login(){
		userName = userNameEt.getText().toString().trim();
		password = passwordEt.getText().toString().trim();
		if(userName == null || userName.equals("") || userName.length()==0){
			dialogWronFun("�û�������Ϊ�գ�",LoginActivity.this);
		}else if(password == null || password.equals("") || password.length()==0){
			dialogWronFun("���벻��Ϊ�գ�",LoginActivity.this);
		}else{
			ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
            	dialogWronFun("��������ʧ�ܣ������������硣",LoginActivity.this);
            }else{
            	createProgress();
				progress.setMessage("��½�У����Ժ�");
				progress.show();
				new Thread(runnable).start();
            }
		}
	}
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static final String sha1(String s) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("sha-1");
			return toHexString(md.digest(s.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * �ֽ�����ת�ַ���
	 * 
	 * @param bytes
	 *                �ֽ�����
	 * @return ���ر�ת�����ַ���
	 */
	public static final String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10)
				buffer.append("0");
			buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buffer.toString();
	}
	/**
	 * ���û���Ϣ�������ݿ�
	 */
	public void updateUser(){
//		userName=userNameEt.getText().toString();
//	    password=passwordEt.getText().toString();
		MyApplication.getInstance().setUsername(userName);
		MyApplication.getInstance().setPassword(password);
		MyApplication.getInstance().setHasAddPerm(loginBundle.getString("hasAddPerm"));
		MyApplication.getInstance().setHasAuditPerm(loginBundle.getString("hasAuditPerm"));
    	loginDB=dbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("username", userName);
		values.put("userid", loginBundle.getString("userid"));
		values.put("hasAddPerm", loginBundle.getString("hasAddPerm"));
		values.put("hasAuditPerm", loginBundle.getString("hasAuditPerm"));
		values.put("password", passwordEt.getText().toString());
		loginDB.delete(Constant.USER_TABLE_NAME, null, null);
		loginDB.insert(Constant.USER_TABLE_NAME, null, values);
		loginDB.close();
	} 
	Runnable runnable = new Runnable(){
		public void run() {
			 password = sha1(password);
			 msg =connect.loadFun(userName,password);
			 if(msg == null || msg.equals("")){
				 handler.sendEmptyMessage(2);
			 }else{
			 if(msg.equals("0")){
				 handler.sendEmptyMessage(2);
			 }else if(msg.equals("-1")){
				 handler.sendEmptyMessage(2);
			 }else if(msg.equals("-2")){
				 handler.sendEmptyMessage(2);
			 }else if(msg.equals("-3")){
				 handler.sendEmptyMessage(2);
			 }else if(msg.equals("-4")){
				 handler.sendEmptyMessage(5);
			 }else if(msg.equals("-5")){
				 handler.sendEmptyMessage(6);
			 }else if(msg.equals("-6")){
				 handler.sendEmptyMessage(4);
			 }else{
				 loginBundle = connect.jsonLoginToBundle(msg);
				 Message message = new Message();
				 message.what = 0;
				 handler.sendMessage(message);
			 }
			 }
		}
		
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
					updateUser();
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
					progress.dismiss();
				break;
			case 1:
				progress.dismiss();
				dialogWronFun("�˺Ż�������󡣡���",LoginActivity.this);
				break;
			case 2:
				progress.dismiss();
				dialogWronFun("���ʷ�����ʧ�ܡ�����",LoginActivity.this);
				break;
			case 4:
				progress.dismiss();
				dialogWronFun("�û�����������󡣡���",LoginActivity.this);
				break;
			case 5:
				progress.dismiss();
				dialogWronFun("�˺Ų���Ϊ�ա�����",LoginActivity.this);
				break;
			case 6:
				progress.dismiss();
				dialogWronFun("���벻��Ϊ�ա�����",LoginActivity.this);
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	private void createProgress(){
    	if(progress!=null){
    		progress.cancel();
    	}
    	progress = new ProgressDialog(this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.setCanceledOnTouchOutside(false);
    	progress.setCancelable(false);
    }
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showExitDialog();
			
		}
		return true;
	}
	/**
	 * �˳�ϵͳʱ����ʾ���� @author ������
	 */
	public void showExitDialog(){
		AlertDialog.Builder callDailog = new AlertDialog.Builder(this);
		  callDailog.setIcon(android.R.drawable.ic_dialog_alert)
		  			.setTitle("��ʾ")
					.setMessage("��ȷ��Ҫ�˳�ϵͳ��")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									android.os.Process.killProcess(android.os.Process.myPid());
									finish();
									System.exit(0);
								}
							}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									return ;
								}
							});
		  callDailog.show();
	}
}
