package guoTeng.LoadIC2ReadCard;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.zxing.client.android.R;
import com.qingfengweb.piaoguanjia.content.DbContent;
import com.qingfengweb.piaoguanjia.db.ProjectDBHelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExitProjectActivity extends Activity implements OnClickListener{
	private LockLayer lockLayer = null;
	View lock = null;
	private Button backBtn;
	private EditText exitEt;
	private TextView exitTv;
	private TextView errorTv;
	public ProjectDBHelper dbHelper;
	SQLiteDatabase loginDB;
	private LinearLayout btnExitLayout;
	private RelativeLayout inputExitLayout;
	private Button exitBtn;
	private Button cancleBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);      
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	    lock = View.inflate(this, R.layout.a_exit, null);
        lockLayer = new LockLayer(this);
        lockLayer.setLockView(lock);
        lockLayer.lock();
        dbHelper = new ProjectDBHelper(ExitProjectActivity.this);
        ExitApplication.getInstance().addActivity(ExitProjectActivity.this);
        backBtn = (Button) lock.findViewById(R.id.backBtn);
        exitEt = (EditText) lock.findViewById(R.id.editView);
        exitTv = (TextView) lock.findViewById(R.id.exitTextView);
        errorTv = (TextView) lock.findViewById(R.id.errorTv);
        btnExitLayout = (LinearLayout) lock.findViewById(R.id.btnExitLayout);
        inputExitLayout = (RelativeLayout) lock.findViewById(R.id.inputEditLayout);
        exitBtn = (Button) lock.findViewById(R.id.exitBtn);
        cancleBtn = (Button) lock.findViewById(R.id.calcleBtn);
        if(isNetworkConnected(this)){
        	btnExitLayout.setVisibility(View.GONE);
        	inputExitLayout.setVisibility(View.VISIBLE);
        }else{
        	btnExitLayout.setVisibility(View.VISIBLE);
        	inputExitLayout.setVisibility(View.GONE);
        }
        exitTv.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if(v == exitTv){
			String userExitPassword = "";
			loginDB = dbHelper.getReadableDatabase();
			Cursor c = loginDB.query(DbContent.USER_TABLE_NAME, new String[]{"exitpassword"}, null, null, null, null, null);
			while(c.moveToNext()){
				userExitPassword = c.getString(c.getColumnIndex("exitpassword"));
			}
			
			if(sha1(exitEt.getText().toString()).equals(userExitPassword)){
				ExitApplication.getInstance().onTerminate();
				android.os.Process.killProcess(android.os.Process.myPid());  
				System.gc();  
				System.exit(0); 
			}else{
				errorTv.setVisibility(View.VISIBLE);
			}
			loginDB.close();
			c.close();
		}else if(v == backBtn){
			finish();
		}else if(v == exitBtn){
			ExitApplication.getInstance().onTerminate();
			android.os.Process.killProcess(android.os.Process.myPid());  
			System.gc();  
			System.exit(0); 
		}else if(v == cancleBtn){
			finish();
		}
		
	}
	/**
	 * 判断网络连接
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	         if (mNetworkInfo != null) {  
	             return mNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
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
	 * 字节数组转字符串
	 * 
	 * @param bytes
	 *                字节数组
	 * @return 返回被转换的字符串
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
	@Override
	protected void onDestroy() {
		lockLayer.unlock();
		super.onDestroy();
	}
	
	
	
}
