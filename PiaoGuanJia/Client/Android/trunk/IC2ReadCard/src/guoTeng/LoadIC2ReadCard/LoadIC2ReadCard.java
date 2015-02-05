package guoTeng.LoadIC2ReadCard;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.ivsign.android.IDCReader.IDCReaderSDK;
import com.qingfengweb.db.ProjectDBHelper;
import com.serial.apps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//回复 Android_Tutor：SkyGray说的对，进程并没有真正退出。
//可以在onCreate判断savedInstanceState是否等于NULL就可以知道是不是re-initialized了，
//或者在onBackPressed调用System.exit(0)真正退出进程。一点拙见，请轻砸。
public class LoadIC2ReadCard extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	public final static String GT_READCARD_MESSAGE = "GT_READCARD_MESSAGE";  
	private String ReadCardMsg[] = {"", "开始连接设备...","设备连接成功...","校验设备...","找卡成功...","选卡成功...","读卡成功..."};	
	//private Handler mHandler=null;
	//public byte[] bBmp = new byte[38862];
	private Bitmap bm;//图片资源Bitmap
	//private Button shoudong;
	private Button chaxun;//根据订单编号或者身份证号码查询
	private EditText ed1;
	public static  Bitmap bitmap1 = null;
	private static final String TAG = "MyNewLog";
	private TextView state;
	private GetTicket getTicket;
	private Button /*connectLanYan,*/duka;
	private ProjectDBHelper dbHelper;
	private SQLiteDatabase database;
	private String name = null;
	private String IdNo = null;
	public Map<String,String> map = null;
	private ImageView pic;
	private int getTicketBtn_flag = 0;//0代表验证身份证  1代表身份证验证中  2代表取票。 
	private Button backBtn;
	private int active_flag = 0;//判断是从哪跳转到这个页面的
	private ProgressDialog progress;
	private RelativeLayout inputLinear ;
	private static final int stateTextSize = 15;
	private TextView detailText;
	byte[] wltdata;// baseinfo and photo
	byte[] licdata;// lic info
	HashMap<String, String> nations = new HashMap<String, String>();
	private RelativeLayout testRelative;
	private TextView phoneNumber;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	super.onCreate(savedInstanceState);
        	setContentView(R.layout.l_indentinfo); 
        ExitApplication.getInstance().addActivity(LoadIC2ReadCard.this);
        findView();
        initData();
        initView();
        mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
    }
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	if(v == backBtn){
    		if(active_flag == 3){
				Intent intent = new Intent(LoadIC2ReadCard.this,HistoryTicket.class);
				startActivity(intent);
				this.finish();
			}else{
				Intent intent = new Intent(LoadIC2ReadCard.this,MainActivity.class);
				startActivity(intent);
				this.finish();
			}
    	}else if(v == duka){
    		dukaFun();
    	}
    }
    private Handler mHandler = new Handler();
    public void disableHomeKey()
	   {
	   this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	   }

	   Runnable mDisableHomeKeyRunnable = new Runnable() {

		   public void run() {
		   disableHomeKey();
		   }
	};
    private void initView(){
    	if(active_flag == 1){//身份证验证
    		//inputLinear.setVisibility(View.GONE);
    		//duka.setBackgroundResource(R.drawable.read);
    		duka.setText("读取");
    		testRelative.setVisibility(View.GONE);
    	}else if(active_flag == 2){//手输验证
    		//inputLinear.setVisibility(View.VISIBLE);
    		//duka.setBackgroundResource(R.drawable.get);
    		duka.setText("取票");
    		duka.setVisibility(View.GONE);
    		testRelative.setVisibility(View.GONE);
    	}else if(active_flag == 3){//我的订单
    		inputLinear.setVisibility(View.GONE);
    		duka.setVisibility(View.GONE);
    		testRelative.setVisibility(View.VISIBLE);
    		Bundle bundle=getIntent().getBundleExtra("bundle");
    		TextView v = (TextView)findViewById(R.id.textView1);
    		v.setText(bundle.getString("username"));
    		v = (TextView)findViewById(R.id.textView2);
    		v.setText(bundle.getString("idnumber"));
    		v = (TextView)findViewById(R.id.textView3);
    		v.setText(bundle.getString("orderid"));
    		v = (TextView)findViewById(R.id.textView4);
    		v.setText(bundle.getString("ordertime"));
    		v = (TextView)findViewById(R.id.textView5);
    		v.setText(bundle.getString("tickettype"));
    		v = (TextView)findViewById(R.id.textView6);
    		v.setText(bundle.getString("ordercount"));
    		v = (TextView) findViewById(R.id.successTime);
    		v.setText(bundle.getString("testingTime"));
    		phoneNumber.setText(bundle.getString("phonenumber"));
    		detailText.setText("["+bundle.getString("scenicname")+"]");
    	}
    	backBtn.setOnClickListener(this);
    	duka.setOnClickListener(this);
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
    private void initData(){
    	active_flag = getIntent().getIntExtra("activity_flag", 0);
        dbHelper  = new ProjectDBHelper(LoadIC2ReadCard.this);
        getTicket = new GetTicket(LoadIC2ReadCard.this);
    }
    private void findView(){
    	inputLinear = (RelativeLayout) findViewById(R.id.inputLinear);
    	state = (TextView)findViewById(R.id.view9);       
        // shoudong = (Button) findViewById(R.id.btn1);
         chaxun = (Button) findViewById(R.id.btn2);
         chaxun.setOnClickListener(new ChaxunListener());
         ed1 = (EditText) findViewById(R.id.ed1);
         duka = (Button) findViewById(R.id.duka);
         pic = (ImageView)findViewById(R.id.imageView1);
         backBtn = (Button) findViewById(R.id.back);
         detailText = (TextView) findViewById(R.id.detailText);
         testRelative = (RelativeLayout) findViewById(R.id.testLinear);
         phoneNumber = (TextView) findViewById(R.id.phoneNumber);
    }
    Runnable myRunnable = new Runnable(){
		public void run() {
			wltdata = new byte[1300];
			licdata = new byte[16];
			if (true) {
				int ret1;
				int ret;
				if (0 == IDCReaderSDK.wltInit("/data/wltlib")) {
					ret1 = apps.Serialread(wltdata, licdata);
					if (ret1 == 1) {   
						ret = IDCReaderSDK.wltGetBMP(wltdata, licdata);
						if (ret == 1) {
						//	showToast("check sam datan ok");
							Message msg  = new Message();
							msg.what = 8;
							myHandler.sendMessage(msg);
						//	initdata(wltdata);
						} /*else{
							//showToast("check sam datan err");
					} else {
						showToast("read data err ret1=" + ret1);
						System.out.printf("\r\read data err ret1=%d\r\n",
								ret1);
					}*/
				} else {
					Message msg  = new Message();
					msg.what = 9;
					myHandler.sendMessage(msg);
				}
				}
			}
				}
		    };
   
    Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
				if(progress.isShowing()){
					progress.dismiss();
				}
				   getTicketBtn_flag = 2;   
				   duka.setVisibility(View.VISIBLE);
				   duka.setClickable(true);
				   duka.setEnabled(true);
				   backBtn.setClickable(false);
				   backBtn.setEnabled(false);
				   chaxun.setClickable(false);
				   chaxun.setEnabled(false);
				break;
			case 1:
				
				String dingdanhao = ed1.getText().toString();
				if(dingdanhao.equals("") || dingdanhao.length()==0 || dingdanhao == null){
					 progress.dismiss();
					dialogWronFun("请输入订单号或者身份证号码再进行查询。。", LoadIC2ReadCard.this);
				}else{
				   getTicketBtn_flag = 2;
				   successGet(dingdanhao,2);
				   pic.setBackgroundResource(R.drawable.photo);
				   ed1.setText("");
				}
				
				break;
			case 3:
				if (mTimer != null){   
					   if (mTimerTask != null){  
						   mTimerTask.cancel();  //将原任务从队列中移除     
						   }       
					   mTimerTask = new myTimerTask(); // 新建一个任务       
					   mTimer.schedule(mTimerTask, 3000);   
					   }
				   duka.setClickable(true);
				   duka.setEnabled(true);
				   backBtn.setClickable(true);
				   backBtn.setEnabled(true);
				   chaxun.setClickable(true);
				   chaxun.setEnabled(true);
				   getTicketBtn_flag = 0;
				   if(progress.isShowing()){
						progress.dismiss();
					}
				break;
				
			case 4:
				if (mTimer != null){   
					   if (mTimerTask != null){  
						   mTimerTask.cancel();  //将原任务从队列中移除     
						   }       
					   mTimerTask = new myTimerTask(); // 新建一个任务       
					   mTimer.schedule(mTimerTask, 3000);   
					   }
				   duka.setClickable(true);
				   duka.setEnabled(true);
				   backBtn.setClickable(true);
				   backBtn.setEnabled(true);
				   chaxun.setClickable(true);
				   chaxun.setEnabled(true);
				   getTicketBtn_flag = 0; 
				   if(progress.isShowing()){
						progress.dismiss();
					}
				break;
			case 5: 
				state.setText("电  子  票  券  验  证  系  统");
				state.setTextSize(25);
				if(progress.isShowing()){
					progress.dismiss();
				}
				break;      
			case 6: 
				   progress.dismiss();
				   getTicketBtn_flag = 0;
				   duka.setClickable(true);
				   duka.setEnabled(true);
				   backBtn.setEnabled(true);
				   backBtn.setClickable(true);
				   chaxun.setClickable(true);
				   chaxun.setEnabled(true);
				   if(progress.isShowing()){
						progress.dismiss();
					}
				break;  
			case 7:
				break;
			case 8:
				setIdnumAndName(wltdata);
				successGet(IdNo,1);
				break;
			case 9:
				if(progress.isShowing()){
					progress.dismiss();
				}
				Toast.makeText(LoadIC2ReadCard.this, "读卡失败，请重新读取。。。。", 5000).show();
				   duka.setVisibility(View.VISIBLE);
				   duka.setClickable(true);
				   duka.setEnabled(true);
				   backBtn.setClickable(true);
				   backBtn.setEnabled(true);
				   chaxun.setClickable(true);
				   chaxun.setEnabled(true);
				   getTicketBtn_flag = 0;
				break;
			}       
			super.handleMessage(msg);
		}
    };
   private  void dukaFun(){
	   //读卡或者取卡函数
	   if(getTicketBtn_flag == 0){//读卡
		   getTicketBtn_flag = 1;
	   duka.setClickable(false);
	   duka.setEnabled(false);
	   backBtn.setEnabled(false);
	   backBtn.setClickable(false);
	   chaxun.setClickable(false);
	   chaxun.setEnabled(false);
	   createProgress();
	   progress.setMessage("正在读卡请稍候。。。");
	   progress.show();
	   Thread thread = new Thread(myRunnable);
	   thread.start();
	   }else if(getTicketBtn_flag == 2){//取票
		   pic.setBackgroundResource(R.drawable.photo);
		   TextView v = (TextView)findViewById(R.id.textView1);
			v.setText("");
			v = (TextView)findViewById(R.id.textView2);
			v.setText("");
			v = (TextView)findViewById(R.id.textView3);
			v.setText("");
			v = (TextView)findViewById(R.id.textView4);
			v.setText("");
			v = (TextView)findViewById(R.id.textView5);
			v.setText("");
			v = (TextView)findViewById(R.id.textView6);
			v.setText("");
			phoneNumber.setText("");
			detailText.setText("");
		   state.setText("取票成功，欢迎您的光临，祝您玩的开心。。。");
		   MediaPlayer mp =MediaPlayer.create(this, R.raw.bobaoyuan);
		   mp.start();
		   AlertDialog.Builder ab = new AlertDialog.Builder(this);
		   ab.setTitle("提示：").setIcon(android.R.drawable.ic_dialog_dialer)
		   .setMessage("验证成功！！");
		   ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				return;
			}
		});
		   ab.show();
		   //duka.setBackgroundResource(R.drawable.read);
		   duka.setText("读取");
		   state.setTextSize(stateTextSize);
		   if (mTimer != null){   
			   if (mTimerTask != null){  
				   mTimerTask.cancel();  //将原任务从队列中移除     
				   }       
			   mTimerTask = new myTimerTask(); // 新建一个任务       
			   mTimer.schedule(mTimerTask, 3000);   
			   }
		   if(active_flag == 1){
			   duka.setVisibility(View.VISIBLE);
			   duka.setClickable(true);
			   duka.setEnabled(true);
			   backBtn.setClickable(true);
			   backBtn.setEnabled(true);
			   chaxun.setClickable(true);
			   chaxun.setEnabled(true);
			   chaxun.setTextColor(Color.WHITE);
			   getTicketBtn_flag = 0;
		   }else if(active_flag == 2){
			   duka.setVisibility(View.GONE);
			   duka.setClickable(true);
			   duka.setEnabled(true);
			   backBtn.setClickable(true);
			   backBtn.setEnabled(true);
			   chaxun.setClickable(true);
			   chaxun.setEnabled(true);
			   chaxun.setTextColor(Color.WHITE);
			   getTicketBtn_flag = 0;
		   }
		   
		   
	   }
	    
   }
   public Timer mTimer = new Timer();//定时器
   public myTimerTask  mTimerTask;
   public class myTimerTask extends TimerTask{  
       public void run() {  
       Message message = new Message();      
       message.what = 5;      
       myHandler.sendMessage(message);    
    }  
 };
    private void setIdnumAndName(byte[] data){
    	if (data != null && data.length >= 256) {
			String str;
			try {
				Bitmap bm = BitmapFactory.decodeFile("/data/wltlib/zp.bmp");
				BitmapDrawable bd = new BitmapDrawable(bm);
				pic.setBackgroundDrawable(bd);
				TextView v = (TextView)findViewById(R.id.textView1);
				str = new String(data, 14, 30, "UnicodeLittleUnmarked");
				v.setText(str.trim());
				name = str.trim();
				str = new String(data, 136, 36, "UnicodeLittleUnmarked");
				v = (TextView)findViewById(R.id.textView2);
				v.setText(str.trim());
				IdNo = str.trim();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    private void successGet(String idNo,int select_type){//读卡成功后根据得到的身份证进行查询
    	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
        	dialogWronFun("网络连接失败。。。",LoadIC2ReadCard.this);
        }else{
    	String tickets = getTicket.getTicket(idNo);
		if(tickets!=null && !tickets.equals("") && tickets.length()>0){
		map = new HashMap<String,String>();
		map.clear();
		map = getTicket.jsonGetTicket(tickets);
		//map.put("idnumber", idNo);
		/*Bitmap bm = BitmapFactory.decodeFile("/data/wltlib/zp.bmp");
		BitmapDrawable bd = new BitmapDrawable(bm);
		pic.setBackgroundDrawable(bd);*/
		TextView v = (TextView)findViewById(R.id.textView1);
		v.setText(map.get("username"));
		v = (TextView)findViewById(R.id.textView2);
		v.setText(map.get("idnumber"));
		v = (TextView)findViewById(R.id.textView3);
		v.setText(map.get("orderid"));
		v = (TextView)findViewById(R.id.textView4);
		v.setText(map.get("ordertime"));
		v = (TextView)findViewById(R.id.textView5);
		v.setText(map.get("tickettype"));
		v = (TextView)findViewById(R.id.textView6);
		v.setTextColor(Color.GREEN);
		v.setText(map.get("ordercount"));
		phoneNumber.setText(map.get("phonenumber"));
		detailText.setText("["+map.get("scenicname")+"]");
		database = dbHelper.getWritableDatabase();
		getTicket.insertTickets(map, database);
		database.close();
		state.setText("验票成功，请取票...");
		state.setTextSize(stateTextSize);
		//duka.setBackgroundResource(R.drawable.get);
		duka.setText("取票");
		Message message = new Message();
		message.what = 0;
		myHandler.sendMessage(message);
		}else{
			MediaPlayer mp =MediaPlayer.create(this, R.raw.noticket);
			mp.start();
			dialogWronFun("没有该身份证或订单号所对应的订单。。。",LoadIC2ReadCard.this);
			TextView v = (TextView)findViewById(R.id.textView1);
			if(select_type == 1){
				v.setText(name);
				v = (TextView)findViewById(R.id.textView2);
				v.setText(idNo);
			}else{
				v.setText("");
				v = (TextView)findViewById(R.id.textView2);
				v.setText("");
			}
			v = (TextView)findViewById(R.id.textView3);
			v.setText("");
			v = (TextView)findViewById(R.id.textView4);
			v.setText("");
			v = (TextView)findViewById(R.id.textView5);
			v.setText("");
			v = (TextView)findViewById(R.id.textView6);
			v.setTextColor(Color.RED);
			v.setText("0");
			phoneNumber.setText("");
			detailText.setText("");
			state.setText("电  子  票  券  验  证  系  统");
			state.setTextSize(stateTextSize);
			Message message = new Message();
			message.what = 3;
			myHandler.sendMessage(message);
		}
        }
    }
    class ChaxunListener implements OnClickListener{

		public void onClick(View v) {
			createProgress();
			progress.setMessage("正在查询请稍候。。。");
			progress.show();
			Thread thread = new Thread(runnable);
			thread.start();
		}
    	
    }
    Runnable runnable = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = 1;
			myHandler.sendMessage(msg);
		}
    	
    };
    public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			}).create().show();
      }
	@Override
	protected void onResume() {
		mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
		Log.e(TAG, "onResume");
		super.onResume();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			if(active_flag == 3){
				Intent intent = new Intent(LoadIC2ReadCard.this,HistoryTicket.class);
				startActivity(intent);
				this.finish();
			}else{
				Intent intent = new Intent(LoadIC2ReadCard.this,MainActivity.class);
				startActivity(intent);
				this.finish();
			}
			break;
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

