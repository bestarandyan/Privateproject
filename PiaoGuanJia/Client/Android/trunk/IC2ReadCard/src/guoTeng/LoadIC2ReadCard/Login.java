package guoTeng.LoadIC2ReadCard;

import org.apache.commons.logging.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.qingfengweb.content.DbContent;
import com.qingfengweb.db.ProjectDBHelper;
import com.qingfengweb.db.UserBean;

public class Login extends Activity implements OnClickListener{
private Button drop_username,drop_password,login_btn;
private EditText username,password;
public ProjectDBHelper dbHelper;
SQLiteDatabase loginDB;
private String name = null;
private String pass = null;
private CheckBox checkBox;
public GetTicket getTicket;
public ProgressDialog progress;
public String un = null;
private String  pw = null;
String msg = "e";
private Button cancleUser;
private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);      
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
		ExitApplication.getInstance().addActivity(Login.this);
		findView();
		initViewFun();
		UnlockListener();
	}
	public void loadFun(){
		 un = username.getText().toString();
		 pw = password.getText().toString();
		if(un.equals("") || un == null || un.length()==0){
			dialogWronFun("用户名不能为空。。。",Login.this);
		}else if(pw.equals("") || pw == null || pw.length()==0){
			dialogWronFun("密码不能为空。。。",Login.this);
		}else{
			ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
            	dialogWronFun("网络连接失败。。。",Login.this);
            }else{
            	createProgress();
				progress.setMessage("正在验证您的信息，请稍候。。。");
				progress.show();
				progress_show_flag = 1;
				if(loginThread==null){
					isTrue = true;
				if(timeThread == null){
            		timeThread = new Thread(timeRunnable);
	                timeThread.start();
        		}
				loginThread = new Thread(runnable);
				loginThread.start();
				}
            }
		}
	
	}
	public void findView(){
		
		getTicket = new GetTicket(Login.this);
		checkBox = (CheckBox) findViewById(R.id.checkedpassword);
		drop_username = (Button) findViewById(R.id.login_drop_username);
		drop_password = (Button) findViewById(R.id.login_drop_password);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login_btn = (Button) findViewById(R.id.login);
		dbHelper = new ProjectDBHelper(Login.this);
		cancleUser = (Button) findViewById(R.id.cancleUser);
	}
	private void createProgress(){
    	if(progress!=null){
    		progress.cancel();
    	}
    	progress = new ProgressDialog(this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.setCanceledOnTouchOutside(false);
    	progress.setCancelable(false);
    }
	public void initViewFun(){
		login_btn.setOnClickListener(this);
		drop_password.setOnClickListener(this);
		drop_username.setOnClickListener(this);
		cancleUser.setOnClickListener(this);
	}
	public void onClick(View v) {
		if(v == login_btn){
			loadFun();
		}else if(v == drop_password){
			password.setText("");
		}else if(v == drop_username){
			username.setText("");
		}else if(v == cancleUser){
			showCancleDialog();
		}
	}      
	public void showCancleDialog(){
		// TODO Auto-generated method stub
		final EditText et = new EditText(Login.this);
		et.setTransformationMethod(PasswordTransformationMethod.getInstance());
		AlertDialog.Builder ab = new AlertDialog.Builder(Login.this);
		ab.setTitle("请输入注销用户的权限命令：");
		ab.setIcon(android.R.drawable.ic_dialog_alert);
		ab.setView(et);
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(et.getText().toString().equals("123")){
					while(true){
						ExitApplication.getInstance().onTerminate(); 
						finish();
						android.os.Process.killProcess(android.os.Process.myPid());  
						System.gc();  
						System.exit(0);  
					}
				}else{
					Toast.makeText(Login.this,"此命令不容许通过。。。", 5000).show();
					return;
				}
				
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		});
		ab.show();
	
	}
	public void disableHomeKey()
	   {
	   this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	   }

	public  Runnable mDisableHomeKeyRunnable = new Runnable() {

		   public void run() {
		   disableHomeKey();
		   }
	};
	public Thread loginThread = null;
	public Thread timeThread = null;
	private int progress_show_flag = 0;
	public boolean isTrue = true;
	public Runnable timeRunnable = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(30000);
				Message msg = new Message();
				msg.what = 3;
				handler.sendMessage(msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	public void UnlockListener(){//监听亮屏  黑屏   和解锁
        final IntentFilter filter = new IntentFilter();    
        filter.addAction(Intent.ACTION_SCREEN_OFF);    
        filter.addAction(Intent.ACTION_SCREEN_ON);   
        filter.addAction(Intent.ACTION_USER_PRESENT);   
        registerReceiver(mBatInfoReceiver, filter);  
	}
	public final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {    
        @Override    
        public void onReceive(final Context context, final Intent intent) {  
            String action = intent.getAction();    
           if(Intent.ACTION_SCREEN_ON.equals(action))  
           {    
                
           }else if(Intent.ACTION_SCREEN_OFF.equals(action))  
           {    
               
           }else if(Intent.ACTION_USER_PRESENT.equals(action))  
           {  
        	   mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
           }  
             
        }    
    };    
	@Override
	protected void onDestroy() {
 	   unregisterReceiver(mBatInfoReceiver);
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = 4;
		handler.sendMessage(msg);
		super.onStop();
	}
	Runnable runnable = new Runnable(){
		public void run() {
			// TODO Auto-generated method stub
			//while(isTrue){
			 msg =getTicket.loadFun(un,pw);
			 if(msg == null){
				 Message message = new Message();
				 message.what = 2;
				 handler.sendMessage(message);
			 }else{
			 if(msg.equals("-1") || msg.startsWith("<")){
				 Message message = new Message();
				 message.what = 2;
				 handler.sendMessage(message);
			 }else if(msg.equals("-2")){
				 Message message = new Message();
				 message.what = 2;
				 handler.sendMessage(message);
			 }else if(msg.equals("-3")){
				 Message message = new Message();
				 message.what = 2;
				 handler.sendMessage(message);
			 }else if(msg.equals("-4")){
				 Message message = new Message();
				 message.what = 2;
				 handler.sendMessage(message);
			 }else if(msg.equals("-11")){
				 Message message = new Message();
				 message.what = 3;
				 handler.sendMessage(message);
			 }else if(msg.equals("-12")){
				 Message message = new Message();
				 message.what = 4;
				 handler.sendMessage(message);
			 }else if(msg.equals("-13")){
				 Message message = new Message();
				 message.what = 1;
				 handler.sendMessage(message);
			 }else{
				 Message message = new Message();
				 message.what = 0;
				 handler.sendMessage(message);
			 }
			 }
				
			//}
		}
		
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
					progress.dismiss();
					UserBean.getInstence().setUsername(un);
					UserBean.getInstence().setPassword(pw);
					updateUser();
					Intent intent = new Intent(Login.this,MainActivity.class);
					startActivity(intent);
					Login.this.finish();
				break;
			case 1:
				progress.dismiss();
				dialogWronFun("账号或密码错误。。。",Login.this);
				break;
			case 2:
				progress.dismiss();
				dialogWronFun("访问服务器失败。。。",Login.this);
				break;
			case 3:
				if(progress_show_flag == 1){
					isTrue = false;
					loginThread.interrupt();
					loginThread=null;
					timeThread.interrupt();
					timeThread = null;
					progress.dismiss();
					progress_show_flag = 0;
					Toast.makeText(Login.this, "连接超时。。", 3000).show();
				}
				break;
			case 4:
				if(progress_show_flag == 1){
					isTrue = false;
					loginThread.interrupt();
					loginThread=null;
					timeThread.interrupt();
					timeThread = null;
					progress.dismiss();
					progress_show_flag = 0;
				}
				break;
			case 5:
				Toast.makeText(Login.this, "用户名或密码错误！", 3000).show();
				break;
			/*case 3:
				progress.dismiss();
				dialogWronFun("账号不能为空。。。",Login.this);
				break;
			case 4:
				progress.dismiss();
				dialogWronFun("密码不能为空。。。",Login.this);
				break;*/
			}
			super.handleMessage(msg);
		}
		
	};
	public void updateUser(){
		name=username.getText().toString();
	    pass=password.getText().toString();
    	loginDB=dbHelper.getWritableDatabase();
	    //----------为登陆页面创建的表，保存最后一次输入并提交成功的登陆名称--------start
		ContentValues values=new ContentValues();
		values.put("username", name);
		if(checkBox.isChecked()){
			values.put("password", pass);
		}else{
			values.put("password", "");
		}
		loginDB.delete(DbContent.USER_TABLE_NAME, null, null);
		loginDB.insert(DbContent.USER_TABLE_NAME, null, values);
		loginDB.close();
	} 
	@Override
	protected void onResume() {
		mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
		if(password.getText().toString().equals("")){
			SQLiteDatabase selDB=dbHelper.getWritableDatabase();
			Cursor c=selDB.query(DbContent.USER_TABLE_NAME,null, null, null, null, null, null);
			String lastName=null;
			String lastPass = null;
			while(c.moveToNext()){
				lastName=c.getString(c.getColumnIndex("username"));
				lastPass=c.getString(c.getColumnIndex("password"));
			}
			c.close();
			selDB.close();
			username.setText(lastName);
			password.setText(lastPass);
		}
		super.onResume();
	}
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
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			switch(keyCode){
			case KeyEvent.KEYCODE_BACK:return true;
			case KeyEvent.KEYCODE_MENU:return true;
			case KeyEvent.KEYCODE_CALL:return true;
			case KeyEvent.KEYCODE_SYM: return true;
			case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
			case KeyEvent.KEYCODE_VOLUME_UP: return true;
			case KeyEvent.KEYCODE_STAR: return true;
			case KeyEvent.KEYCODE_CAMERA: return true;
			case KeyEvent.KEYCODE_POWER: return true;
			}
			return super.onKeyDown(keyCode, event);
		}

	
	
}
