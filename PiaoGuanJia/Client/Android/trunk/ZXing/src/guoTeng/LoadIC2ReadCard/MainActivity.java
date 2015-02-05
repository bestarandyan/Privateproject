package guoTeng.LoadIC2ReadCard;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.ScanManagerActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity  extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	//选项图片列表
//	private GridView gridView;
	private Handler mHandler = new Handler();
	private Button checkBtn1,checkBtn2,checkBtn3;
	private LockLayer lockLayer = null;
	private	View lock = null;
	private Button exitBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);      
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);      
//        setContentView(R.layout.a_main);
        lock = View.inflate(this, R.layout.a_main, null);
        lockLayer = new LockLayer(this);
        lockLayer.setLockView(lock);
        lockLayer.lock();
        checkBtn1 = (Button) lock.findViewById(R.id.checkBtn1);
        checkBtn2 = (Button) lock.findViewById(R.id.checkBtn2);
        checkBtn3 = (Button) lock.findViewById(R.id.checkBtn3);
        exitBtn = (Button) lock.findViewById(R.id.exitBTn);
        checkBtn1.setOnClickListener(this);
        checkBtn2.setOnClickListener(this);
        checkBtn3.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        ExitApplication.getInstance().addActivity(MainActivity.this);
        checkBtn1.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					checkBtn1.setBackgroundResource(R.drawable.piao_yz_btn_01);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					checkBtn1.setBackgroundResource(R.drawable.piao_yz_btn1);
				}
				return false;
			}
		});
		checkBtn2.setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_DOWN){
							checkBtn2.setBackgroundResource(R.drawable.piao_yz_btn_02);
						}else if(event.getAction() == MotionEvent.ACTION_UP){
							checkBtn2.setBackgroundResource(R.drawable.piao_yz_btn2);
						}
						return false;
					}
				});
		checkBtn3.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					checkBtn3.setBackgroundResource(R.drawable.piao_yz_btn_03);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					checkBtn3.setBackgroundResource(R.drawable.piao_yz_btn3);
				}
				return false;
			}
		});
//        InitView();
    }
    
    
    @Override
	protected void onDestroy() {
    	lockLayer.unlock();
		super.onDestroy();
	}


	public void disableHomeKey(){
	   this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	   }

	Runnable mDisableHomeKeyRunnable = new Runnable() {
		   public void run() {
		   disableHomeKey();
		   }
	};
		   
    @Override
	protected void onResume() {
    	mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
		super.onResume();
	}
	/**
     * 初始化布局文件
     */
//    public void InitView(){
//    	gridView = (GridView)this.lock.findViewById(R.id.gridview);
//    	gridView.setAdapter(new ImageAdapter(this));	
//        gridView.setOnItemClickListener(gridlistener);
//    }
    /**
     * GridView的点击事件
     */
    private OnItemClickListener gridlistener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent();
			switch (arg2) {
			case 0:
				intent.setClass(MainActivity.this, InputSelectTicketActivity.class);
				intent.putExtra("activity_flag", 1);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
				break;
			case 1:
				intent.setClass(MainActivity.this,CaptureActivity.class);
				startActivityForResult(intent, 0);
				break;
			case 2:
				intent.setClass(MainActivity.this, HistoryTicket.class);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			AlertDialog.Builder alert=new AlertDialog.Builder(this);
	      	alert.setMessage("确定要退出应用吗？");
	      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
	      			
					public void onClick(DialogInterface dialog, int which) {
						android.os.Process.killProcess(android.os.Process.myPid());  
						System.gc();  
						System.exit(0);  
					}
				});
	      	alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	      			
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
	      	alert.show();
			
			break;
		}
		return false;
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		if(v == checkBtn1){//开始验证   二维码验证
			intent.setClass(MainActivity.this,CaptureActivity.class);
			MainActivity.this.startActivity(intent);
		}else if(v == checkBtn2){//手输验证
			intent.setClass(MainActivity.this, InputSelectTicketActivity.class);
			intent.putExtra("activity_flag", 1);
			MainActivity.this.startActivity(intent);
		}else if(v == checkBtn3){//验证记录
			intent.setClass(MainActivity.this, HistoryTicket.class);
			MainActivity.this.startActivity(intent);
		}else if(v == exitBtn){
			intent.setClass(MainActivity.this, ExitProjectActivity.class);
			MainActivity.this.startActivity(intent);
		}
	}
   
}
