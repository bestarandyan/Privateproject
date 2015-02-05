package guoTeng.LoadIC2ReadCard;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.R;
import com.qingfengweb.piaoguanjia.content.DbContent;
import com.qingfengweb.piaoguanjia.db.ProjectDBHelper;
import com.qingfengweb.piaoguanjia.db.UserBean;
public class Login extends Activity implements OnClickListener{
private Button drop_username,drop_password,login_btn;
private LinearLayout loadingLayout;
private ProgressBar loadingPro;
private TextView loadingTv;
private EditText username,password;
public ProjectDBHelper dbHelper;
SQLiteDatabase loginDB;
private String name = null;
private String pass = null;
private CheckBox checkBox;
public GetTicket getTicket;
//public ProgressDialog progress;
public String un = null;
private String  pw = null;
String msg = "e";
private LockLayer lockLayer = null;
	View lock = null;
	private Button exitBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);      
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	    lock = View.inflate(this, R.layout.a_login, null);
        lockLayer = new LockLayer(this);
        lockLayer.setLockView(lock);
        lockLayer.lock();
        ExitApplication.getInstance().addActivity(Login.this);
//		setContentView(R.layout.a_login);
		findView();
		initViewFun();
	}
	
	@Override
	protected void onDestroy() {
		lockLayer.unlock();
		super.onDestroy();
	}

	public void loadFun(){
		 un = username.getText().toString();
		 pw = password.getText().toString();
		if(un.equals("") || un == null || un.length()==0){
//			dialogWronFun("用户名不能为空。。。",Login.this);
			loadingLayout.setVisibility(View.VISIBLE);
			loadingTv.setText("用户名不能为空");
			loadingPro.setVisibility(View.GONE);
		}else if(pw.equals("") || pw == null || pw.length()==0){
//			dialogWronFun("密码不能为空。。。",Login.this);
			loadingLayout.setVisibility(View.VISIBLE);
			loadingTv.setText("密码不能为空");
			loadingPro.setVisibility(View.GONE);
		}else{
			ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
//            	dialogWronFun("网络连接失败。。。",Login.this);
            	loadingLayout.setVisibility(View.VISIBLE);
    			loadingTv.setText("网络连接失败。。。");
    			loadingPro.setVisibility(View.GONE);
            }else{
            	loadingLayout.setVisibility(View.VISIBLE);
    			loadingTv.setText("loading ....");
    			loadingPro.setVisibility(View.VISIBLE);
//            	createProgress();
//				progress.setMessage("正在验证您的信息，请稍候。。。");
//				progress.show();
//            	lockLayer.showProgress("正在验证您的信息，请稍候。。。");
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
		checkBox = (CheckBox)lock.findViewById(R.id.checkedpassword);
		drop_username = (Button) lock.findViewById(R.id.login_drop_username);
		drop_password = (Button) lock.findViewById(R.id.login_drop_password);
		username = (EditText) lock.findViewById(R.id.username);
		password = (EditText) lock.findViewById(R.id.password);
		login_btn = (Button) lock.findViewById(R.id.login);
		loadingTv = (TextView) lock.findViewById(R.id.loadingTv);
		loadingLayout = (LinearLayout) lock.findViewById(R.id.loadingLayout);
		loadingPro = (ProgressBar) lock.findViewById(R.id.progressBar);
		dbHelper = new ProjectDBHelper(Login.this);
		exitBtn = (Button) lock.findViewById(R.id.exitBTn);
	}
//	private void createProgress(){
//    	if(progress!=null){
//    		progress.cancel();
//    	}
//    	progress = new ProgressDialog(this);
//    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//    	progress.setCanceledOnTouchOutside(false);
//    	progress.setCancelable(false);
//    }
	public void initViewFun(){
		login_btn.setOnClickListener(this);
		drop_password.setOnClickListener(this);
		drop_username.setOnClickListener(this);
		exitBtn.setOnClickListener(this);
	}
	public void onClick(View v) {
		if(v == login_btn){
			
			loadFun();
		}else if(v == drop_password){
			password.setText("");
		}else if(v == drop_username){
			username.setText("");
			password.setText("");
		}else if(v == exitBtn){
			Intent intent = new Intent();
			intent.setClass(Login.this, ExitProjectActivity.class);
			Login.this.startActivity(intent);
		}
	}      
	
	
	public Thread loginThread = null;
	public Thread timeThread = null;
	Bundle bundle = null;
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
			 if(msg.equals("-1")){
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
				 bundle = getTicket.jsonLoginToBundle(msg);
				 String exitpassword = bundle.getString("exitpassword");
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
					isTrue = false;
					loginThread.interrupt();
					loginThread=null;
					timeThread.interrupt();
					timeThread = null;
//					progress.dismiss();
					progress_show_flag = 0;
					UserBean.getInstence().setUsername(un);
					UserBean.getInstence().setPassword(pw);
					updateUser();//更新数据库的用户
					Intent intent = new Intent(Login.this,MainActivity.class);
					startActivity(intent);
					Login.this.finish();
//					progress.dismiss();
				break;
			case 1:
//				progress.dismiss();
//				dialogWronFun("账号或密码错误。。。",Login.this);
				loadingTv.setText("账号或密码错误");
				loadingPro.setVisibility(View.GONE);
				break;
			case 2:
//				progress.dismiss();
//				dialogWronFun("访问服务器失败。。。",Login.this);
				loadingTv.setText("访问服务器失败");
				loadingPro.setVisibility(View.GONE);
				break;
			case 3:
				if(progress_show_flag == 1){
					isTrue = false;
					loginThread.interrupt();
					loginThread=null;
					timeThread.interrupt();
					timeThread = null;
//					progress.dismiss();
					progress_show_flag = 0;
//					Toast.makeText(Login.this, "连接超时。。", 3000).show();
					loadingTv.setText("用户不存在");
					loadingPro.setVisibility(View.GONE);
				}
				break;
			case 4:
				if(progress_show_flag == 1){
					isTrue = false;
					loginThread.interrupt();
					loginThread=null;
					timeThread.interrupt();
					timeThread = null;
//					progress.dismiss();
					progress_show_flag = 0;
				}
//				Toast.makeText(Login.this, "用户名或密码错误", 3000).show();
				loadingTv.setText("密码错误");
				loadingPro.setVisibility(View.GONE);
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
		values.put("userid", bundle.getString("userid"));
		values.put("exitpassword", bundle.getString("exitpassword"));
		MyApplication.getInstance().setCurrentExitPassword(bundle.getString("exitpassword"));
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
//	 public boolean dispatchKeyEvent(KeyEvent event) {
//			if (event.getAction() == KeyEvent.ACTION_UP) {
//	            return super.dispatchKeyEvent(event);
//			}
//			switch(event.getKeyCode()){
//			case KeyEvent.KEYCODE_ENTER:
//				loadFun();
//				return true;
//			case KeyEvent.KEYCODE_BACK:
//				 AlertDialog.Builder ab = new AlertDialog.Builder(this);
//				 ab.setIcon(android.R.drawable.ic_dialog_alert);
//				 ab.setTitle("提示：");
//				 ab.setMessage("确定要退出程序吗？");
//				 ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						finish();
//						android.os.Process.killProcess(android.os.Process.myPid());  
//						System.gc();  
//						System.exit(0);  
//					}
//				});
//				 ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//						
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							return;
//						}
//					});
//				 ab.show();
//				return true;
//			case 120:
//				loadFun();
//				return true;
//			}
//			return super.dispatchKeyEvent(event);
//		}
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
		 switch (keyCode) {   
	        case KeyEvent.KEYCODE_BACK:
//	        	Toast.makeText(this, "asdfadsf", 3000).show();
	        return true;   
	        case KeyEvent.KEYCODE_HOME:   
	            return true;   

	    }   

			return super.onKeyDown(keyCode, event);
		}
//	@Override
//	public void onAttachedToWindow() {
//		this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);   
//		super.onAttachedToWindow();
//	}
	 
	 
//	 public void onAttachedToWindow()  
//	 {    
//	        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);       
//	        super.onAttachedToWindow();    
//	 }   

	
	
}
