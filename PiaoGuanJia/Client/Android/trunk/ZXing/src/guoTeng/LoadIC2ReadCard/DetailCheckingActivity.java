/**
 * 
 */
package guoTeng.LoadIC2ReadCard;

import java.util.Calendar;
import java.util.HashMap;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;
import com.qingfengweb.piaoguanjia.content.InterfaceContent;
import com.qingfengweb.piaoguanjia.db.ProjectDBHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author ������
 * @createDate 2013/3/25
 * ����������
 *
 */
public class DetailCheckingActivity extends Activity implements OnClickListener{
	private Button backBtn;//���ذ�ť
	private ImageView photoIV;//��Ƭ
	private TextView nameText;//��������
	private TextView dateText;//��֤����
	private TextView timeText;//��֤ʱ��
	private TextView stateText;//��֤״̬
	private ImageView stateImage;//��֤״̬ͼ��
	private TextView typeText;//��Ʊ����
	private TextView amountText;//��Ʊ����
	private TextView createTime;//�µ�ʱ��
	private TextView reserveText;//Ԥ��ʱ��
	private TextView indentText;//������
	private TextView unserNameText;//Ԥ��������
	private TextView phoneText;//Ԥ���˵绰
	private TextView idText;//Ԥ�������֤
	private TextView sourceText;//��Դ
	private TextView requirementText;//��֤����
	private Bundle bundle = null;//��֤�󴫵ݹ���������
	private SQLiteDatabase database;
	private ProjectDBHelper dbHelper;
	private GetTicket getTicket;
//	private ProgressDialog progressDialog;
	private String tickets = "";
	private String displayContents="";
	LinearLayout userLayout;
	LinearLayout requiremengLayout;
	
	private LockLayer lockLayer = null;
	View lock = null;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.a_detailchecking);
	    lock = View.inflate(this, R.layout.a_detailchecking, null);
	      lockLayer = new LockLayer(this);  
	      lockLayer.setLockView(lock);
		findView();//�ҵ��ؼ�
		displayContents = getIntent().getStringExtra("displayContents");
		bundle = getIntent().getBundleExtra("bundle");
		if(bundle!=null){
			setViewContext();
			requiremengLayout.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);
		}else{
//			progressDialog = new ProgressDialog(this);
//			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//			progressDialog.setMessage("������֤�����Ժ򡣡���");
			nameText.setText("������֤�����Ժ�...");
			getTicket = new GetTicket(DetailCheckingActivity.this);
//			progressDialog.show();
			Thread thread = new Thread(runnable);
			thread.start();
		}
		
	}
	@Override
	protected void onResume() {
		lockLayer.lock();
		super.onResume();
	}
	@Override
	protected void onPause() {
		lockLayer.unlock();
		super.onPause();
	}

	Runnable runnable = new Runnable(){

		public void run() {
			// TODO Auto-generated method stub
			tickets = getTicket.getTicket(displayContents.toString(),InterfaceContent.TICKET_GET_ACTION);
			Message msg = new Message();
			msg.what = 1;
			myHandler.sendMessage(msg);
		}
  	
  };
  Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case 0:
				initData();//��ʼ������
//				progressDialog.dismiss();
				progressBar.setVisibility(View.GONE);
				break;
			case 1:
				successGet(tickets);
				break;
			case 2:
//				AlertDialog.Builder alert=new AlertDialog.Builder(CaptureActivity.this);
////		      	alert.setMessage("�ö�����û����Ч����������");
//		      	View v = LayoutInflater.from(CaptureActivity.this).inflate(R.layout.dialog_alert, null);
//		      	v.setAnimation(mAnimationRight);
//		      	alert.setView(v);
//		      	alert.create().show();
//		      	resetStatusView();
//		        if (handler != null) {
//		          handler.sendEmptyMessage(R.id.restart_preview);
//		        }
				stateText.setText("û�м�¼..");
				stateText.setTextSize(14);
				stateText.setTextColor(Color.RED);
				stateImage.setVisibility(View.GONE);
				userLayout.setVisibility(View.GONE);
				nameText.setVisibility(View.GONE);
						
				setTimeText();
				progressBar.setVisibility(View.GONE);
				break;
			}       
			super.handleMessage(msg);
		}
};
private void successGet(String tickets){//�����ɹ�����ݵõ������֤���в�ѯ
	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
//    	dialogWronFun("��������ʧ�ܡ�����",LoadIC2ReadCard.this);
    }else{
		if(tickets!=null && !tickets.equals("") && tickets.length()>0){
			bundle = new Bundle();
			bundle.clear();
			bundle = getTicket.jsonGetTicketToBundle(tickets);
			Message message = new Message();
			message.what = 0;
			myHandler.sendMessage(message);
		}else{
			MediaPlayer mp =MediaPlayer.create(this, R.raw.noticket);
			mp.start();
			Message message = new Message();
			message.what = 2;
			myHandler.sendMessage(message);
			
		}
    }
}
	private void findView(){
		backBtn  = (Button) lock.findViewById(R.id.backBtn);
		photoIV = (ImageView) lock.findViewById(R.id.photoImageView);
		nameText = (TextView) lock.findViewById(R.id.nameText);
		dateText = (TextView) lock.findViewById(R.id.dateText);
		timeText = (TextView) lock.findViewById(R.id.timeText);
		stateText = (TextView) lock.findViewById(R.id.stateText);
		typeText = (TextView) lock.findViewById(R.id.typeText);
		amountText = (TextView) lock.findViewById(R.id.amountText);
		reserveText = (TextView) lock.findViewById(R.id.reserveText);
		indentText = (TextView) lock.findViewById(R.id.indentText);
		unserNameText = (TextView) lock.findViewById(R.id.userNameText);
		phoneText = (TextView) lock.findViewById(R.id.phoneText);
		idText = (TextView) lock.findViewById(R.id.idText);
		sourceText = (TextView) lock.findViewById(R.id.sourseText);
		stateImage = (ImageView) lock.findViewById(R.id.stateImage);
		createTime = (TextView) lock.findViewById(R.id.createTimeText);
		requirementText = (TextView) lock.findViewById(R.id.requirementText);
		userLayout = (LinearLayout) lock.findViewById(R.id.userLayout);
		requiremengLayout = (LinearLayout) lock.findViewById(R.id.requirementLayout);
		progressBar = (ProgressBar) lock.findViewById(R.id.progressBar);
		backBtn.setOnClickListener(this);
		
	}
	private void setTimeText(){
		Calendar calendar  = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		String munite = String.valueOf(calendar.get(Calendar.MINUTE));
		String second = String.valueOf(calendar.get(Calendar.SECOND));
		String data = year+"-"+month+"-"+day;
		String time = hour+":"+munite+":"+second;
		
		dateText.setText(data);
		timeText.setText(time);
		requirementText.setText(displayContents);
	}
	private void initData(){
		MediaPlayer mp =MediaPlayer.create(this, R.raw.bobaoyuan);
		   mp.start();
		dbHelper  = new ProjectDBHelper(DetailCheckingActivity.this);
//		bundle = this.getIntent().getBundleExtra("checkDataBundle");
//		bundle.putString("orderid", user.getOrderid());
//	    bundle.putString("ordertime", user.getOrdertime().substring(0, 10));
//	    bundle.putString("username", user.getUsername());
//	    bundle.putString("tickettype", user.getTickettype());
//	    bundle.putString("ordercount", user.getOrdercount());
//	    bundle.putString("createtime", user.getCreatetime());
//	    bundle.putString("phonenumber", user.getPhonenumber());
//	    bundle.putString("scenicname", user.getScenicname());
//	    bundle.putString("idnumber", user.getIdnumber());
		setViewContext();
		database = dbHelper.getWritableDatabase();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("orderid", bundle.getString("orderid"));
		map.put("ordertime", bundle.getString("ordertime"));
		map.put("username", bundle.getString("username"));
		map.put("tickettype", bundle.getString("tickettype"));
		map.put("ordercount", bundle.getString("ordercount"));
		map.put("createtime", bundle.getString("createtime"));
		map.put("idnumber", bundle.getString("idnumber"));
		map.put("phonenumber", bundle.getString("phonenumber"));
		map.put("scenicname", bundle.getString("scenicname"));
		map.put("validatetime", bundle.getString("validatetime"));
		getTicket.insertTickets(map, database);
		database.close();
	}
	private void setViewContext(){
		dateText.setText(bundle.getString("validatetime"));
		timeText.setText("");
		requirementText.setText(displayContents);
		nameText.setText(bundle.getString("scenicname"));
//		stateText.setText(bundle.getString(""));
		typeText.setText(bundle.getString("tickettype"));
		amountText.setText(bundle.getString("ordercount"));
		createTime.setText(bundle.getString("createtime"));
		reserveText.setText(bundle.getString("ordertime"));
		indentText.setText(bundle.getString("orderid"));
		unserNameText.setText(bundle.getString("username"));
		phoneText.setText(bundle.getString("phonenumber"));
		idText.setText(bundle.getString("idnumber"));
		sourceText.setText("Ʊ�ܼ�");
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return false;
	}
}
