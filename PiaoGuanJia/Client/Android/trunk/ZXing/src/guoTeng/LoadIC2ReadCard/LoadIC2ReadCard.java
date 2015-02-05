package guoTeng.LoadIC2ReadCard;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.client.android.R;
import com.qingfengweb.piaoguanjia.content.InterfaceContent;
import com.qingfengweb.piaoguanjia.db.ProjectDBHelper;

//�ظ� Android_Tutor��SkyGray˵�Ķԣ����̲�û�������˳���
//������onCreate�ж�savedInstanceState�Ƿ����NULL�Ϳ���֪���ǲ���re-initialized�ˣ�
//������onBackPressed����System.exit(0)�����˳����̡�һ��׾���������ҡ�
public class LoadIC2ReadCard extends Activity implements OnClickListener{
	private Button chaxun;//���ݶ�����Ż������֤�����ѯ
	private EditText ed1;
	public static  Bitmap bitmap1 = null;
	private TextView state;
	private GetTicket getTicket;
	private Button getTicketBtn;
	private ProjectDBHelper dbHelper;
	private SQLiteDatabase database;
	public Map<String,String> map = null;
	private ImageView pic;
	private int getTicketBtn_flag = 0;//0������֤���֤  1�������֤��֤��  2����ȡƱ�� 
	private Button backBtn;
	private int active_flag = 0;//�ж��Ǵ�����ת�����ҳ���
	private ProgressDialog progress;
	private LinearLayout inputLinear ;
	private static final int stateTextSize = 25;
	private TextView detailText;
	byte[] wltdata;// baseinfo and photo
	byte[] licdata;// lic info
	HashMap<String, String> nations = new HashMap<String, String>();
	private RelativeLayout testRelative;
	private TextView phoneNumber;
	private String dingdanhao = null;
	private String tickets = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    	super.onCreate(savedInstanceState);
        	setContentView(R.layout.a_readcard); 
        findView();
        initData();
        initView();
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
    	}else if(v == getTicketBtn){
    		getTicketsFun();
    	}
    }
 
    private void initView(){
    	if(active_flag == 1){//������֤
    		getTicketBtn.setVisibility(View.GONE);
    		testRelative.setVisibility(View.GONE);
    	}else if(active_flag == 3){//�ҵĶ���
    		inputLinear.setVisibility(View.GONE);
    		getTicketBtn.setVisibility(View.GONE);
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
    		v.setText(bundle.getString("validatetime"));
    		phoneNumber.setText(bundle.getString("phonenumber"));
    		detailText.setText("["+bundle.getString("scenicname")+"]");
    	}
    	backBtn.setOnClickListener(this);
    	getTicketBtn.setOnClickListener(this);
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
    	inputLinear = (LinearLayout) findViewById(R.id.inputLinear);
    	state = (TextView)findViewById(R.id.view9);       
        // shoudong = (Button) findViewById(R.id.btn1);
         chaxun = (Button) findViewById(R.id.btn2);
         chaxun.setOnClickListener(new ChaxunListener());
         ed1 = (EditText) findViewById(R.id.ed1);
         getTicketBtn = (Button) findViewById(R.id.duka);
         pic = (ImageView)findViewById(R.id.imageView1);
         backBtn = (Button) findViewById(R.id.back);
         detailText = (TextView) findViewById(R.id.detailText);
         testRelative = (RelativeLayout) findViewById(R.id.testLinear);
         phoneNumber = (TextView) findViewById(R.id.phoneNumber);
    }
   
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
				   getTicketBtn.setVisibility(View.VISIBLE);
				   getTicketBtn.setClickable(true);
				   getTicketBtn.setEnabled(true);
				   backBtn.setClickable(false);
				   backBtn.setEnabled(false);
				   chaxun.setClickable(false);
				   chaxun.setEnabled(false);
				break;
			case 1:
				successGet(tickets);
				break;
			case 3:
				if (mTimer != null){   
					   if (mTimerTask != null){  
						   mTimerTask.cancel();  //��ԭ����Ӷ������Ƴ�     
						   }       
					   mTimerTask = new myTimerTask(); // �½�һ������       
					   mTimer.schedule(mTimerTask, 3000);   
					   }
				   getTicketBtn.setClickable(true);
				   getTicketBtn.setEnabled(true);
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
				state.setText("��  ��  Ʊ  ȯ  ��  ֤  ϵ  ͳ");
				state.setTextSize(25);
				if(progress.isShowing()){
					progress.dismiss();
				}
				break;      
		
			}       
			super.handleMessage(msg);
		}
    };
   private  void getTicketsFun(){
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
		   state.setText("ȡƱ�ɹ�����ӭ���Ĺ��٣�ף����Ŀ��ġ�����");
		   MediaPlayer mp =MediaPlayer.create(this, R.raw.bobaoyuan);
		   mp.start();
		   AlertDialog.Builder ab = new AlertDialog.Builder(this);
		   ab.setTitle("��ʾ��").setIcon(android.R.drawable.ic_dialog_dialer)
		   .setMessage("��֤�ɹ�����");
		   ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				getTicketBtn.setVisibility(View.GONE);
				ed1.setText("");
				
				return;
			}
		});
		   ab.show();
		   state.setTextSize(stateTextSize);
		   if (mTimer != null){   
			   if (mTimerTask != null){  
				   mTimerTask.cancel();  //��ԭ����Ӷ������Ƴ�     
				   }       
			   mTimerTask = new myTimerTask(); // �½�һ������       
			   mTimer.schedule(mTimerTask, 3000);   
			   }
			   backBtn.setClickable(true);
			   backBtn.setEnabled(true);
			   chaxun.setClickable(true);
			   chaxun.setEnabled(true);
			   chaxun.setTextColor(Color.WHITE);
			   getTicketBtn_flag = 0;
   }
   public Timer mTimer = new Timer();//��ʱ��
   public myTimerTask  mTimerTask;
   public class myTimerTask extends TimerTask{  
       public void run() {  
       Message message = new Message();      
       message.what = 5;      
       myHandler.sendMessage(message);    
    }  
 };
   
    private void successGet(String tickets){//�����ɹ�����ݵõ������֤���в�ѯ
    	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
        	dialogWronFun("��������ʧ�ܡ�����",LoadIC2ReadCard.this);
        }else{
		if(tickets!=null && !tickets.equals("") && tickets.length()>0){
			map = new HashMap<String,String>();
			map.clear();
			map = getTicket.jsonGetTicket(tickets);
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
			state.setText("��Ʊ�ɹ�����ȡƱ...");
			state.setTextSize(stateTextSize);
			//getTicketBtn.setBackgroundResource(R.drawable.get);
			Message message = new Message();
			message.what = 0;
			myHandler.sendMessage(message);
		}else{
			MediaPlayer mp =MediaPlayer.create(this, R.raw.noticket);
			mp.start();
			dialogWronFun("û�и����֤�򶩵�������Ӧ�Ķ���������",LoadIC2ReadCard.this);
			ed1.setText("");
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
			v.setTextColor(Color.RED);
			v.setText("0");
			phoneNumber.setText("");
			detailText.setText("");
			state.setText("��  ��  Ʊ  ȯ  ��  ֤  ϵ  ͳ");
			state.setTextSize(stateTextSize);
			Message message = new Message();
			message.what = 3;
			myHandler.sendMessage(message);
		}
        }
    }
    class ChaxunListener implements OnClickListener{

		public void onClick(View v) {
			 dingdanhao = ed1.getText().toString();
			if(dingdanhao.equals("") || dingdanhao.length()==0 || dingdanhao == null){
				dialogWronFun("�����붩���Ż������֤�����ٽ��в�ѯ����", LoadIC2ReadCard.this);
			}else{
				createProgress();
				progress.setMessage("���ڲ�ѯ���Ժ򡣡���");
				progress.show();
				Thread thread = new Thread(runnable);
				thread.start();
			}
			
		}
    	
    }
    Runnable runnable = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			 tickets = getTicket.getTicket(dingdanhao,InterfaceContent.TICKET_GET_ACTION);
			Message msg = new Message();
			msg.what = 1;
			myHandler.sendMessage(msg);
		}
    	
    };
    public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			}).create().show();
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
		}
		return super.onKeyDown(keyCode, event);
	}      
}

