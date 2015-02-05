/**
 * 
 */
package guoTeng.LoadIC2ReadCard;

import com.google.zxing.client.android.R;
import com.qingfengweb.piaoguanjia.content.InterfaceContent;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 刘星星
 * @createDate 2013/3/26
 * 手输查询类
 *
 */
public class InputSelectTicketActivity extends Activity implements OnClickListener{
	private Button backBtn;//
	private TextView nameText;//
	private EditText et;//
	private TextView selectView;//
	private String tickets = "";
	private GetTicket getTicket;
	public Bundle bundle = null;
	private LockLayer lockLayer = null;
	private	View lock = null;
	RelativeLayout selectLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.a_inputselectticket);
	    lock = View.inflate(this, R.layout.a_inputselectticket, null);
        lockLayer = new LockLayer(this);
        lockLayer.setLockView(lock);
        lockLayer.lock();
	    backBtn = (Button) lock.findViewById(R.id.backBtn);
	    nameText = (TextView) lock.findViewById(R.id.nameText);
	    et = (EditText) lock.findViewById(R.id.editView);
	    selectView = (TextView) lock.findViewById(R.id.selectTextView);
	    selectView.setOnClickListener(this);
	    backBtn.setOnClickListener(this);
	    getTicket = new GetTicket(this);
	    selectLayout = (RelativeLayout) lock.findViewById(R.id.selectLayout);
	    selectView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					selectView.setBackgroundColor(Color.rgb(9,78,141));
					selectLayout.setBackgroundResource(R.drawable.input_keydown_bg);
				}else if(event.getAction() == MotionEvent.ACTION_UP){
					selectView.setBackgroundColor(Color.rgb(9,108,227));
					selectLayout.setBackgroundResource(R.drawable.input_bg);
				}
				return false;
			}
		});
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
            return super.dispatchKeyEvent(event);
		}
		switch(event.getKeyCode()){
		case KeyEvent.KEYCODE_ENTER:
			hideSoftKeyboard(this);
			if(et.getText().toString().length() <= 0){
				Toast.makeText(this, "请输入查询条件。。。", 3000).show();
			}else{
				Intent intent = new Intent(InputSelectTicketActivity.this,DetailOrderActivity.class);
				intent.putExtra("checkData", et.getText().toString().trim());
			    startActivity(intent);
			    et.setText("");
			}
			return true;
		case 120:
			hideSoftKeyboard(this);
			if(et.getText().toString().length() <= 0){
				Toast.makeText(this, "请输入查询条件。。。", 3000).show();
			}else{
				Intent intent = new Intent(InputSelectTicketActivity.this,DetailOrderActivity.class);
				intent.putExtra("checkData", et.getText().toString().trim());
			    startActivity(intent);
			    et.setText("");
			}
			return true;
		case KeyEvent.KEYCODE_BACK:
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	};
//	Runnable runnable = new Runnable(){
//
//		public void run() {
//			// TODO Auto-generated method stub
//			tickets = getTicket.getTicket(et.getText().toString(),InterfaceContent.TICKET_SELECT_ACTION);
//			Message msg = new Message();
//			msg.what = 1;
//			myHandler.sendMessage(msg);
//		}
//  	
//  };
  @Override
	protected void onDestroy() {
		lockLayer.unlock();
		super.onDestroy();
	}
//  Handler myHandler = new Handler(){
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			switch(msg.what){
//			case 0:
//				et.setText("");
//				Intent intent = new Intent(InputSelectTicketActivity.this,DetailCheckingActivity.class);
//				intent.putExtra("checkDataBundle", bundle);
//			    startActivity(intent);
//				break;
//			case 1:
//				successGet(tickets);
//				break;
//			case 2:
//				AlertDialog.Builder alert=new AlertDialog.Builder(InputSelectTicketActivity.this);
//		      	alert.setMessage("该订单号没有有效订单！！！");
//		      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//		      			
//						public void onClick(DialogInterface dialog, int which) {
//							return;
//						}
//					}).create().show();
//				
//				break;
//			}       
//			super.handleMessage(msg);
//		}
//  };
//  private void successGet(String tickets){//读卡成功后根据得到的身份证进行查询
//  	ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//      if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
////      	dialogWronFun("网络连接失败。。。",LoadIC2ReadCard.this);
//      }else{
//		if(tickets!=null && !tickets.equals("") && tickets.length()>0){
//			bundle = new Bundle();
//			bundle.clear();
//			bundle = getTicket.jsonGetTicketToBundle(tickets);
//			Message message = new Message();
//			message.what = 0;
//			myHandler.sendMessage(message);
//		}else{
//			MediaPlayer mp =MediaPlayer.create(this, R.raw.noticket);
//			mp.start();
//			Message message = new Message();
//			message.what = 2;
//			myHandler.sendMessage(message);
//			
//		}
//      }
//  }
	@Override
	public void onClick(View v) {
		if(v == selectView){
			hideSoftKeyboard(this);
			if(et.getText().toString().length() <= 0){
				Toast.makeText(this, "请输入查询条件。。。", 3000).show();
			}else{
				Intent intent = new Intent(InputSelectTicketActivity.this,DetailOrderActivity.class);
				intent.putExtra("checkData", et.getText().toString().trim());
			    startActivity(intent);
			    et.setText("");
			}
			
		}else if(v == backBtn){
			finish();
		}
	}
	
	/**
	 * 隐藏键盘的函数
	 * 
	 * @author 刘星星
	 * @createDate 2013/2/19
	 * @param activity
	 */
	public static void hideSoftKeyboard(Activity activity) {
		try {
			((InputMethodManager) activity
					.getSystemService(activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(activity.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
	}
}








































