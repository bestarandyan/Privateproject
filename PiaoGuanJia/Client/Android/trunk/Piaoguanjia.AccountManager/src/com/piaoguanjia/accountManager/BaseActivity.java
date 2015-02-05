package com.piaoguanjia.accountManager;

import com.piaoguanjia.accountManager.util.CommonUtils;
import com.piaoguanjia.accountManager.util.MessageBox;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;

public class BaseActivity extends Activity implements OnClickListener, Runnable,OnTouchListener{

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
		CommonUtils.hideSoftKeyboard(this);
		super.onPause();
	}

	@Override
	public void run() {
	}

	/**
	 * 上传数据时验证数据
	 */
	public boolean validate() {
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
				MessageBox.CreateAlertDialog("请检查网络是否连接！", BaseActivity.this);
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
				} else if (response.equals("-1")) {
					errormsg = "操作失败！";
				} else if (response.equals("-9")) {
					errormsg = "充值对象用户名不存在";
				} else if (response.equals("-12")) {
					errormsg = "产品编号不能为空";
				} else if (response.equals("-13")) {
					errormsg = "产品编号不存在";
				} else if (response.equals("-18")) {
					errormsg = "担保人不存在";
				} else if (response.equals("-28")) {
					errormsg = "当前状态已经无法进行此操作（代表额度已经被人审核了）";
				} else if (response.equals("-34")) {
					errormsg = "添加理由不能为空";
				} else if (response.equals("-35")) {
					errormsg = "额度编号为空，或者格式不正确";
				} else if (response.equals("-36")) {
					errormsg = "额度编号不存在";
				} else if (response.equals("-37")) {
					errormsg = "没有权限";
				} else if (response.equals("-38")) {
					errormsg = "您被限制了充值客户端登录，如有问题，请联系票管家！";
				} else if (response.equals("-39")) {
					errormsg = "余额不足，无法扣款";
				} else if (response.equals("-41")) {
					errormsg = "分销商已经拥有这个产品的专用账户，无法再次添加！";
				} else if (response.equals("-42")) {
					errormsg = "信用额度为空，或者格式不正确";
				} else if (response.equals("-43")) {
					errormsg = "分销商已经拥有该账户的信用额度，无法再次添加";
				} else if (response.equals("-44")) {
					errormsg = "分销商不存在该产品的专用账户";
				} else if (response.equals("-45")) {
					errormsg = "充值金额小于专用账户的充值限制";
				} else if (response.equals("-46")) {
					errormsg = "直冲限制格式不正确";
				} else if (response.equals("-404")) {
					errormsg = "系统正在维护，请稍后重试";
				} else if (response.equals("-1000")) {
					errormsg = "请求超时，请稍后重试！";
				} else {
					errormsg = "请求失败，错误编号为" + response;
				}
				MessageBox.CreateAlertDialog(errormsg, BaseActivity.this);
				break;
			}
		}
	};
	public OnTouchListener mainviewOntouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			CommonUtils.hideSoftKeyboard((Activity)v.getContext());
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
			CommonUtils.hideSoftKeyboard((Activity)v.getContext());
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundColor(Color.parseColor("#666666"));
			}else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundColor(Color.parseColor("#3ABFDE"));
			}
			return false;
		}
	};
	
	public OnTouchListener btn1viewOntouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			CommonUtils.hideSoftKeyboard((Activity)v.getContext());
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				v.setBackgroundColor(Color.parseColor("#666666"));
			}else if (event.getAction() == MotionEvent.ACTION_UP) {
				v.setBackgroundColor(Color.parseColor("#3ABFDE"));
			}
			return false;
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		CommonUtils.hideSoftKeyboard(this);
		return false;
	}
}
