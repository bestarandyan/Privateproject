package com.qingfengweb.piaoguanjia.orderSystem;

import com.qingfengweb.piaoguanjia.orderSystem.util.CommonUtils;
import com.qingfengweb.piaoguanjia.orderSystem.util.JsonUtils;
import com.qingfengweb.piaoguanjia.orderSystem.util.MessageBox;
import com.qingfengweb.piaoguanjia.orderSystem.view.CustomProgressDialog;

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
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;

public class BaseActivity extends Activity implements OnClickListener,
		Runnable, OnTouchListener {

	public CustomProgressDialog progressdialog = null;
	public boolean clicked = false;
	public static final int PROGRESSSTART_HANDLER = 0x1314;
	public static final int PROGRESSEND_HANDLER = 0x1315;
	public static final int NONETWORK_HANDLER = 0x1316;
	public static final int RESPONSE_HANDLER = 0x1317;
	public static final String KEY_RESPONSE = "key_response";
	public static String PROGRESSMSG = "正在加载中...";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.activitylist.add(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
	                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
				MessageBox.promptDialog("请检查网络是否连接", BaseActivity.this);
				break;
			case PROGRESSSTART_HANDLER:// 打开进度条
				progressdialog = CustomProgressDialog
						.createDialog(BaseActivity.this);
				progressdialog.setMessage(PROGRESSMSG);
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
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
				errormsg = b.getString(KEY_RESPONSE);
				if(errormsg!=null&&!errormsg.equals("")){
					if(errormsg.equals("-404")){
						MessageBox.promptDialog("服务器404错误", BaseActivity.this);
					}else if(errormsg.equals("-1000")){
						MessageBox.promptDialog("服务器请求超时！", BaseActivity.this);
					}else{
						FailureBean o = JsonUtils.jsonObject(FailureBean.class, errormsg);
						if(o!=null&&o.getFailure()!=null&&!o.getFailure().equals("")){
							MessageBox.promptDialog(o.getFailure(), BaseActivity.this);
						}
					}
					
				}
				break;
			}
		}
	};
	
	/**
	 * 检查返回值
	 * @author QingFeng
	 *
	 */

	public boolean checkResponse(String response){
		int state = 0;
		try {
			state = Integer.parseInt(response);
		} catch (Exception e) {
		}
		if(response.contains("failure")
				||state<0){
			Message msg = new Message();
			msg.what = RESPONSE_HANDLER;
			Bundle b = new Bundle();
			b.putString(KEY_RESPONSE, response);
			b.putBoolean("islist", false);
			msg.setData(b);
			handler.sendMessage(msg);
			return false;
		}else if(response.contains("success")){
			return true;
		}
		return true;
	}
	
	public class FailureBean{
		private String failure;
		private String success;
		
		public String getSuccess() {
			return success;
		}

		public void setSuccess(String success) {
			this.success = success;
		}

		public String getFailure() {
			return failure;
		}

		public void setFailure(String failure) {
			this.failure = failure;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		CommonUtils.hideSoftKeyboard(BaseActivity.this);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 按下
			break;
		case MotionEvent.ACTION_MOVE:
			// 移动
			break;
		case MotionEvent.ACTION_UP:
			// 抬起
			break;
		}
		return true;
	}
	
	public void startIntent(Intent i){
		startActivity(i);
	}
}
