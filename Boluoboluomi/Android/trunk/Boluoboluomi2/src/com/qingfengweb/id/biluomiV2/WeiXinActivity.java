package com.qingfengweb.id.biluomiV2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.qingfengweb.data.MyApplication;
import com.qingfengweb.data.RequestServerFromHttp;
import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.network.NetworkCheck;
import com.qingfengweb.network.UploadData;
import com.qingfengweb.util.MessageBox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class WeiXinActivity extends BaseActivity {
	private Button backBtn, submitBtn;
	private EditText et;
	private WebView wv;
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_weixin);
		backBtn = (Button) findViewById(R.id.backBtn);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		et = (EditText) findViewById(R.id.weixinhao);
		wv = (WebView) findViewById(R.id.textView);
		backBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		setWVContent();
	}

	private void setWVContent() {
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		wv.setBackgroundColor(0);
		wv.getSettings().setDefaultFontSize(19);
		wv.loadDataWithBaseURL(
				"",
				"添加金夫人摄影公众微信平台成功后,请填写所添加平台微信号,经系统确认后，系统将于24小时内赠送<span style='color:red;'> 100 </span>积分。",
				"text/html", "utf-8", "");
	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {//返回键
			finish();
		} else if (v == submitBtn) {//确认提交
			if(textValidate()){
				new Thread(submitWechatRunnable).start();
			}
		}
		super.onClick(v);
	}
	/**
	 * author by Ring 登录前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		if (et.getText().toString().trim().equals("")) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), getResources()
							.getString(R.string.wechat_null),
					WeiXinActivity.this);
			return false;
		}
		return true;
	}

	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable submitWechatRunnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(WeiXinActivity.this)) {
				handler.sendEmptyMessage(5);//打开进度条
				String msg = RequestServerFromHttp.submitWeiXin(et.getText().toString().trim());
				handler.sendEmptyMessage(6);//关闭进度条
				if (msg.startsWith("0")) {
					handler.sendEmptyMessage(1);// 跳转到成功界面
				}else if(msg.equals("404")){//访问服务器失败
					
				} else {
					handler.sendEmptyMessage(3);// 提交失败给用户提示
				}
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}
			click_limit = true;
		}
	};
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1://跳转到微信成功界面
				i.setClass(WeiXinActivity.this, WeiXinSuccessActivity.class);
				WeiXinActivity.this.startActivity(i);
				WeiXinActivity.this.finish();
				break;
//			case 2:// 从登录界面跳转到注册界面
//				i.setClass(WeiXinActivity.this, RegisterActivity.class);
//				WeiXinActivity.this.startActivity(i);
//				WeiXinActivity.this.finish();
//				break;
			case 3:// 提交失败给用户提示
				String errormsg = "";
					errormsg = getResources().getString(R.string.wechat_error);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						errormsg,
						WeiXinActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.wechat_error_net),
						WeiXinActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(WeiXinActivity.this);
				progressdialog.setMessage(getResources().getString(
						R.string.progress_message_sumbit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};

}
