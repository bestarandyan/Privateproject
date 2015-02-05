/**
 * 
 */
package com.chinaLife.claimAssistant;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.thread.sc_UploadData3;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import com.sqlcrypt.database.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 刘星星 武国庆
 * @createDate 2013/8/27
 * 修改密码界面
 *
 */
public class sc_AmendPasswordActivity extends Activity implements OnClickListener{
	private TextView explainTv;
	public sc_UploadData3 uploaddata = null;
	public boolean click_limit = true;//时间准备点
	public String reponse = ""; //服务器反映值
	public ProgressDialog progressdialog = null;
	private EditText passwordEt1,passwordEt2,passwordEt3;
	private Button passwordBtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sc_a_amendpassword);
		passwordEt1 = (EditText) findViewById(R.id.passwordEt1);
		passwordEt2 = (EditText) findViewById(R.id.passwordEt2);
		passwordEt3 = (EditText) findViewById(R.id.passwordEt3);
		findViewById(R.id.backBtn).setOnClickListener(this);
		findViewById(R.id.telBtn).setOnClickListener(this);
		explainTv = (TextView) findViewById(R.id.explainTv);
		passwordBtn = (Button)findViewById(R.id.passwordBtn);
		passwordBtn.setOnClickListener(this);
	}
	private void appendContent(){
	//  根据资源ID获得资源图像的Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sc_telephone);
        //  根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(this, bitmap);
        //  创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        SpannableString spannableString = new SpannableString("tou");
        //  用ImageSpan对象替换face
        spannableString.setSpan(imageSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //  将随机获得的图像追加到EditText控件的最后
        explainTv.append(spannableString);
	}
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.backBtn){
			finish();
		}else if(v.getId() == R.id.telBtn){
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:"+"15988888888"));
			startActivity(intent);
		}else if(v.getId() == R.id.passwordBtn){
			if(yanzheng()){
				new Thread(runnableQuest).start();
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	
	
	/**
	 * 请求的线程
	 */
	public Runnable runnableQuest = new Runnable() {
		
		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (sc_NetworkCheck.IsHaveInternet(sc_AmendPasswordActivity.this)) {
				handlerQuest.sendEmptyMessage(5);
				boolean b = setPassword();
				if(b){
					handlerQuest.sendEmptyMessage(0);
				}else{
					handlerQuest.sendEmptyMessage(1);
				}
				handlerQuest.sendEmptyMessage(6);
			} else {
				handlerQuest.sendEmptyMessage(4);// 没有网络时给用户提示

			}
			click_limit = true;
		}
	};
	
	
	
	public boolean yanzheng() {
		if (passwordEt1.getText().toString().replace(" ", "").equals("")) {
			tiXing("旧密码不能为空！");
			return false;
		}else if (passwordEt2.getText().toString().replace(" ", "").equals("")) {
			tiXing("新密码不能为空！");
			return false;
		}else if (!passwordEt3.getText().toString().replace(" ", "")
				.equals(passwordEt2.getText().toString().replace(" ", ""))) {
			tiXing("两次密码不一致！");
			return false;
		}
		return true;
	}
	
	public void tiXing(String msg) {
		AlertDialog.Builder callDailog = new AlertDialog.Builder(
				sc_AmendPasswordActivity.this);
		callDailog.setIcon(android.R.drawable.ic_dialog_info).setTitle("提示")
				.setMessage(msg).setPositiveButton("我知道了", null).show();
	}
	/**
	 * 根据手机号车牌号查询案件列表
	 * return 1 密码校验失败，2，密码未设置  3，密码已设置未传入  0成功
	 */
	public boolean setPassword() {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", sc_MyApplication.APPID));
		params.add(new BasicNameValuePair("appkey", sc_MyApplication.APPKEY));
		params.add(new BasicNameValuePair("action", "updatePassword"));
		params.add(new BasicNameValuePair("phoneNumber", sc_MyApplication.getInstance().getPhonenumber()));
		params.add(new BasicNameValuePair("plateNumber", sc_MyApplication.getInstance().getPlatenumber()));
		params.add(new BasicNameValuePair("password", passwordEt2.getText()
				.toString().replace(" ", "")));
		params.add(new BasicNameValuePair("oldPassword", passwordEt1.getText()
				.toString().replace(" ", "")));
		params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo.getIMEI(this)));
		uploaddata = new sc_UploadData3(sc_MyApplication.URL + "case", params);
		uploaddata.Post();
		reponse = uploaddata.getResponse();
		dealmsg();
		params.clear();
		params = null;
		if (reponse.contains("0")) {
			return true;
		}
		return false;
	}
	/***
	 * 处理服务器返回值
	 * @param reponse2
	 */
	private void dealmsg() {		
	}

	/**
	 * UI处理
	 */
	
	public Handler handlerQuest = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 密码，车牌号，手机号都验证成功，进入案件列表界面
				sc_MyApplication.getInstance().setPassword(
						passwordEt2.getText().toString().replace(" ", ""));
				ContentValues values = new ContentValues();
				values.put("password", passwordEt1.getText().toString()
						.replace(" ", ""));
				sc_DBHelper.getInstance().update(
						"userinfo",
						values,
						"plate_number = ? and contact_mobile_number = ?",
						new String[] {
								sc_MyApplication.getInstance().getPlatenumber(),
								sc_MyApplication.getInstance().getPhonenumber() });
				finish();
				break;
			case 1:
				if (reponse.equals("-35")) {
					tiXing("旧密码有误，请重新输入，也可点击下方电话联系客服修改！");
				} else {
					tiXing("修改失败，请稍后重试，也可点击下方电话联系客服修改！");
				}
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(sc_AmendPasswordActivity.this);
				progressdialog.setMessage("系统处理中，请稍等~~~");
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (uploaddata != null) {
							uploaddata.overResponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// 无网络
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		}
		
	};
}
