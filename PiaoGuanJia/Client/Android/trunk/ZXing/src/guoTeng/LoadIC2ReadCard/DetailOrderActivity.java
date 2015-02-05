/**
 * 
 */
package guoTeng.LoadIC2ReadCard;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;
import com.qingfengweb.piaoguanjia.content.InterfaceContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author ������
 * @createDate 2013/3/28
 * ����������
 */
public class DetailOrderActivity extends Activity implements OnClickListener{
	private Button backBtn;
	private TextView orderName;
	private TextView amount1,amount2;
	private TextView typeOrder;
	private TextView numberOrder;
	private TextView userName;
	private TextView userPhone;
	private TextView userId;
	private TextView orderTime;
	private TextView checkTicketText;//�����֤Ʊȯ
	private Bundle bundle = null;
//	private ProgressDialog progressDialog;
	private String tickets = "";
	private GetTicket getTicket;
	private TextView stateTv;
	private ImageView stateImage;
	private TextView checkRequirementText;
	private LockLayer lockLayer = null;
	private Handler mHandler = new Handler();
	View lock = null;
	private PopupWindow selectPopupWindow = null;
	private LinearLayout parent = null;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.a_detailorder);
		lock = View.inflate(this, R.layout.a_detailorder, null);
      lockLayer = new LockLayer(this);  
      lockLayer.setLockView(lock);
      ExitApplication.getInstance().addActivity(DetailOrderActivity.this);
		findView();
		checkRequirementText.setText(getIntent().getStringExtra("checkData").toString());
//		progressDialog = new ProgressDialog(this);
//		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		progressDialog.setMessage("������֤�����Ժ򡣡���");
//		progressDialog.show();
		getTicket = new GetTicket(this);
		Thread thread = new Thread(runnable);
		thread.start();
//		initData();
//		initView();
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
			tickets = getTicket.getTicket(getIntent().getStringExtra("checkData").toString(),
					InterfaceContent.TICKET_SELECT_ACTION);
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
//				Intent intent = new Intent(InputSelectTicketActivity.this,DetailCheckingActivity.class);
//				intent.putExtra("checkDataBundle", bundle);
//			    startActivity(intent);
//				MediaPlayer mp =MediaPlayer.create(DetailOrderActivity.this, R.raw.bobaoyuan);
//				mp.start();
				initView();
				progressBar.setVisibility(View.GONE);
//				progressDialog.dismiss();
				break;
			case 1:
				successGet(tickets);
				break;
			case 2:
//				progressDialog.dismiss();
				MediaPlayer mp =MediaPlayer.create(DetailOrderActivity.this, R.raw.noticket);
				mp.start();
				stateTv.setText("û�м�¼����");
				stateTv.setTextColor(Color.RED);
				stateTv.setTextSize(18);
				stateImage.setVisibility(View.GONE);
				amount1.setVisibility(View.GONE);
				checkTicketText.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
//				AlertDialog.Builder alert=new AlertDialog.Builder(DetailOrderActivity.this);
//		      	alert.setMessage("�ö�����û����Ч����������");
//		      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
//		      			
//						public void onClick(DialogInterface dialog, int which) {
//							return;
//						}
//					}).create().show();
//				
				break;
			}       
			super.handleMessage(msg);
		}
  };
  private void successGet(String tickets){//�����ɹ�����ݵõ������֤���в�ѯ
  	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
      if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
//      	dialogWronFun("��������ʧ�ܡ�����",LoadIC2ReadCard.this);
      }else{
		if(tickets!=null && !tickets.equals("") && tickets.length()>0){
			bundle = new Bundle();
			bundle.clear();
			bundle = getTicket.jsonGetTicketToBundle(tickets);
			Message message = new Message();
			message.what = 0;
			myHandler.sendMessage(message);
		}else{
			Message message = new Message();
			message.what = 2;
			myHandler.sendMessage(message);
			
		}
      }
  }
	private void findView(){
		backBtn = (Button) lock.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		orderName = (TextView) lock.findViewById(R.id.orderName);
		amount1 = (TextView) lock.findViewById(R.id.amount1);
		amount2  = (TextView) lock.findViewById(R.id.amountTv);
		typeOrder = (TextView) lock.findViewById(R.id.typeTv);
		numberOrder = (TextView) lock.findViewById(R.id.orderTv);
		userName = (TextView) lock.findViewById(R.id.nameTv);
		userPhone = (TextView) lock.findViewById(R.id.phoneTv);
		userId = (TextView) lock.findViewById(R.id.idTv);
		orderTime = (TextView) lock.findViewById(R.id.dateTv);
		checkTicketText = (TextView) lock.findViewById(R.id.checkTicket);
		stateTv = (TextView) lock.findViewById(R.id.stateTv);
		stateImage = (ImageView) lock.findViewById(R.id.stateImage);
		checkRequirementText = (TextView) lock.findViewById(R.id.checkRequirement);
		progressBar = (ProgressBar) lock.findViewById(R.id.progressBar);
		
		stateTv.setText("���ڲ�ѯ���������Ժ�...");
//		stateTv.setTextColor(Color.RED);
		stateTv.setTextSize(18);
		stateImage.setVisibility(View.GONE);
		amount1.setVisibility(View.GONE);
		checkTicketText.setVisibility(View.GONE);
	}
	private void initData(){
		bundle = getIntent().getBundleExtra("bundle");

	}
	private void initView(){
		stateImage.setVisibility(View.VISIBLE);
		amount1.setVisibility(View.VISIBLE);
		checkTicketText.setVisibility(View.VISIBLE);
		stateTv.setText("������Ч");
		checkTicketText.setOnClickListener(this);
		orderName.setText(bundle.getString("scenicname"));
		amount1.setText(bundle.getString("ordercount"));
		amount2.setText(bundle.getString("ordercount"));
		typeOrder.setText(bundle.getString("tickettype"));
		numberOrder.setText(bundle.getString("orderid"));
		userName.setText(bundle.getString("username"));
		userPhone.setText(bundle.getString("phonenumber"));
		userId.setText(bundle.getString("idnumber"));
		orderTime.setText(bundle.getString("ordertime"));
	}
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
            return super.dispatchKeyEvent(event);
		}
		switch(event.getKeyCode()){
		case KeyEvent.KEYCODE_ENTER:
			if(checkTicketText.getVisibility() == View.VISIBLE){
				Intent intent = new Intent(this,DetailCheckingActivity.class);
				intent.putExtra("displayContents", getIntent().getStringExtra("checkData").toString());
			    startActivity(intent);
			    finish();
			}
			return true;
		case 120:
			if(checkTicketText.getVisibility() == View.VISIBLE){
				Intent intent = new Intent(this,DetailCheckingActivity.class);
				intent.putExtra("displayContents", getIntent().getStringExtra("checkData").toString());
			    startActivity(intent);
			    finish();
			}
			return true;
		case KeyEvent.KEYCODE_BACK:
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
//	private void initDialogWindow(){
//		int width = 0;
//		int height = 0;
//		LinearLayout loupanwindow = (LinearLayout) LayoutInflater.from(lockLayer.mActivty).inflate(R.layout.dialog_select_loupan, null);
//		loupanwindow.getBackground().setAlpha(220);
//		parent = (LinearLayout) lock.findViewById(R.id.parent);
//		width = (int)(parent.getWidth()*0.8f);
//		height = width + 100;
//		selectPopupWindow = new PopupWindow(loupanwindow, width,	height, true);
//		// ��һ����Ϊ��ʵ�ֵ���PopupWindow�󣬵������Ļ�������ּ�Back��ʱPopupWindow����ʧ��
//		// û����һ����Ч�����ܳ�������������Ӱ�챳��
//		selectPopupWindow.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.dialog_border));
//		selectPopupWindow.getBackground().setAlpha(220);
//		selectPopupWindow.setOutsideTouchable(true);
//		selectPopupWindow.showAsDropDown(parent, (parent.getWidth()-width)/2, -(parent.getHeight()-(parent.getHeight()-height)/2));
//		selectPopupWindow.setOnDismissListener(new OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				dismiss();
//			}
//		});
//	}
	/**
	 * PopupWindow��ʧ
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}
	Handler handlerDialog = new Handler(){

		@Override
		public void handleMessage(Message msg) {
//			initDialogWindow();
			super.handleMessage(msg);
		}
	
	};
	Runnable runnableDialog = new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(200);
				handlerDialog.sendEmptyMessage(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
	
	@Override
	protected void onDestroy() {
//		lockLayer.unlock();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		if(v == backBtn){
			if(getIntent().getBooleanExtra("backCamera", false)){
				Intent i = new Intent();
				i.setClass(DetailOrderActivity.this,CaptureActivity.class);
				startActivity(i);
			}
			finish();
		}else if(v == checkTicketText){
			Intent intent = new Intent(DetailOrderActivity.this,CheckBooleanActivity.class);
			intent.putExtra("displayContents", getIntent().getStringExtra("checkData").toString());
		    startActivity(intent);
//		    finish();
			/*AlertDialog.Builder ab = new AlertDialog.Builder(this);
			 ab.setIcon(android.R.drawable.ic_dialog_alert);
			 ab.setTitle("��ʾ��");
			 ab.setMessage("�Ƿ�ȷ����֤��");
			 ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(DetailOrderActivity.this,DetailCheckingActivity.class);
					intent.putExtra("displayContents", getIntent().getStringExtra("checkData").toString());
				    startActivity(intent);
				    finish();
				}
			});
			 ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				});
			 ab.show();*/
//			new Thread(runnableDialog).start();
		}
	}
}
