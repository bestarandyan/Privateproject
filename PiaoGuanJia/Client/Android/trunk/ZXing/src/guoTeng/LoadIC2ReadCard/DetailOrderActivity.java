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
 * @author 刘星星
 * @createDate 2013/3/28
 * 订单详情类
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
	private TextView checkTicketText;//点击验证票券
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
//		progressDialog.setMessage("正在验证，请稍候。。。");
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
				stateTv.setText("没有记录。。");
				stateTv.setTextColor(Color.RED);
				stateTv.setTextSize(18);
				stateImage.setVisibility(View.GONE);
				amount1.setVisibility(View.GONE);
				checkTicketText.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
//				AlertDialog.Builder alert=new AlertDialog.Builder(DetailOrderActivity.this);
//		      	alert.setMessage("该订单号没有有效订单！！！");
//		      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
  private void successGet(String tickets){//读卡成功后根据得到的身份证进行查询
  	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
      if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
//      	dialogWronFun("网络连接失败。。。",LoadIC2ReadCard.this);
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
		
		stateTv.setText("正在查询订单，请稍候...");
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
		stateTv.setText("订单有效");
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
//		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
//		// 没有这一句则效果不能出来，但并不会影响背景
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
	 * PopupWindow消失
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
			 ab.setTitle("提示：");
			 ab.setMessage("是否确定验证？");
			 ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(DetailOrderActivity.this,DetailCheckingActivity.class);
					intent.putExtra("displayContents", getIntent().getStringExtra("checkData").toString());
				    startActivity(intent);
				    finish();
				}
			});
			 ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
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
