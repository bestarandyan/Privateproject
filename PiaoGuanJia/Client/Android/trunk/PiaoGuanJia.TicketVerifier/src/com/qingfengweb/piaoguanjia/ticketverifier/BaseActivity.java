package com.qingfengweb.piaoguanjia.ticketverifier;




import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;

public class BaseActivity extends Activity implements OnClickListener, Runnable {

	public ProgressDialog progressdialog = null;
	public boolean clicked = false;
	public static final int PROGRESSSTART_HANDLER = 0x1314;
	public static final int PROGRESSEND_HANDLER = 0x1315;
	public static final int NONETWORK_HANDLER = 0x1316;
	public static final int RESPONSE_HANDLER = 0x1317;
	public static final String KEY_RESPONSE = "key_response";
	public static String PROGRESSMSG = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	protected void onPause() {
		/**
		 * 隐藏键盘的函数
		 */
		try {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
							getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
		}
		super.onPause();
	}

	@Override
	public void run() {
	}
	
	/**
	 * 上传数据时验证数据
	 */
	public boolean validate(){
		return false;
	}

	/**
	 * 页面逻辑处理
	 */
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NONETWORK_HANDLER:// 没有网络时给用户提示
//				CreateAlertDialog("请检查网络是否连接！", BaseActivity.this);
				break;
			case PROGRESSSTART_HANDLER:// 打开进度条
				progressdialog = new ProgressDialog(BaseActivity.this);
				progressdialog.setMessage(PROGRESSMSG);
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						// UploadData uploaddata =
						// RequestServerFromHttp.requestmap
						// .get(RequestServerFromHttp.ACTION_VALIDATECERTIFICATE);
						// if (uploaddata != null) {
						// uploaddata.overReponse();
						// }
						return false;
					}
				});
				progressdialog.show();
				break;
			case PROGRESSEND_HANDLER:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case RESPONSE_HANDLER:
				String errormsg = "";
				Bundle b = msg.getData();
				String response = b.getString(KEY_RESPONSE);
				if (response.equals("-7")) {
					errormsg = "用户名或者密码不正确！";
				} else if (response.equals("-37")) {
					errormsg = "没有验证权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了客户端登录，如有问题，请联系您的服务专员！";
				} else if (response.equals("-12")) {
					errormsg = "订单编号不存在";
				} else if (response.equals("-11")) {
					errormsg = "订单编号为空，或者格式不正确";
				} else if (response.equals("-13")) {
					errormsg = "退出密码为空";
				} else if (response.equals("-14")) {
					errormsg = "退出密码不正确";
				}else if (response.equals("-404")) {
					errormsg = "服务器处于维护状态，请稍后重试！";
				}else if (response.equals("-1000")) {
					errormsg = "请求超时，请稍后重试！";
				} else {
					errormsg = "请求失败，错误编号为"+response;
				}
//				MessageBox.CreateAlertDialog(errormsg, BaseActivity.this);
				break;
			}
		}
	};
	
	public OnTouchListener mainviewOntouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Animation clickAnimation = AnimationUtils.loadAnimation(BaseActivity.this,
					R.anim.anim_button);
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.startAnimation(clickAnimation);
			}
			return false;
		}
	};
	
	
	public OnTouchListener btnviewOntouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundResource(R.drawable.btn_corner_roundtmp);
			}else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundResource(R.drawable.btn_corner_round);
			}
			return false;
		}
	};
	
	public OnTouchListener btnbackOntouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundColor(Color.parseColor("#666666"));
			}else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackground(null);
			}
			return false;
		}
	};
}
