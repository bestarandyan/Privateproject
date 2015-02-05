package com.boluomi.children.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.boluomi.children.R;
import com.boluomi.children.data.MyApplication;
import com.boluomi.children.data.RequestServerFromHttp;
import com.boluomi.children.database.DBHelper;
import com.boluomi.children.network.NetworkCheck;
import com.boluomi.children.network.UploadData;
import com.boluomi.children.util.MessageBox;

public class FeedbackForPingdaiActivity extends BaseActivity implements
		OnRatingBarChangeListener {
	private Button backBtn;
	private Button affirmSubmitBtn;// 确认提交按钮
	private EditText et;// 备注
	private ImageView image;
	private int type = 0;// 标记当前意见反馈类型为平台类还是服务类 1代表平台类 2代表服务类
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private ProgressDialog progressdialog;
	private RatingBar ratingBar1, ratingBar2, ratingBar3, ratingBar4;
	private TextView text1, text2, text3, text4;// 用来描述意见反馈的评分对象
	private int value1 = 0;// 评分1
	private int value2 = 0;// 评分2
	private int value3 = 0;// 评分3
	private int value4 = 0;// 评分4
	private String staffid = "";// 摄影师或化妆师id
	private TextView title;
	private LinearLayout text4Linear;// 服务类特有的点评服务项

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_feedbackforpingtai);
		findView();
		initData();
	}

	private void initData() {
		type = getIntent().getIntExtra("type", 1);
//		MyApplication.getInstant().setPosition(type);
		if (type == 1) {
			title.setText("平台类意见反馈");
			text1.setText("操作界面");
			text2.setText("使用功能");
			text3.setText("下载速率");
			text4Linear.setVisibility(View.GONE);
		} else {
			int textWidth = Integer
					.parseInt(getString(R.string.feddback_text_width));
			title.setText("服务类意见反馈");
			text1.setWidth(textWidth);
			text1.setGravity(Gravity.RIGHT);
			text2.setWidth(textWidth);
			text2.setGravity(Gravity.RIGHT);
			text3.setWidth(textWidth);
			text3.setGravity(Gravity.RIGHT);
			text4.setWidth(textWidth);
			text4.setGravity(Gravity.RIGHT);
			text1.setText("积分服务");
			text2.setText("查件定制服务");
			text3.setText("百宝箱服务");
			text4Linear.setVisibility(View.VISIBLE);
		}
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		affirmSubmitBtn = (Button) findViewById(R.id.affirmSubmitBtn);
		et = (EditText) findViewById(R.id.et);
		affirmSubmitBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		image = (ImageView) findViewById(R.id.image);
		ratingBar1 = (RatingBar) findViewById(R.id.room_ratingbar1);
		ratingBar2 = (RatingBar) findViewById(R.id.room_ratingbar2);
		ratingBar3 = (RatingBar) findViewById(R.id.room_ratingbar3);
		ratingBar4 = (RatingBar) findViewById(R.id.room_ratingbar4);
		ratingBar1.setOnRatingBarChangeListener(this);
		ratingBar2.setOnRatingBarChangeListener(this);
		ratingBar3.setOnRatingBarChangeListener(this);
		ratingBar4.setOnRatingBarChangeListener(this);
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		text4 = (TextView) findViewById(R.id.text4);
		text4Linear = (LinearLayout) findViewById(R.id.text4Linear);

	}

	@Override
	public void onClick(View v) {
		if (v == backBtn) {
			finish();
		} else if (v == affirmSubmitBtn) {
			if (textValidate()) {
				new Thread(submitFeedbackRunnable).start();
			}
		}
		super.onClick(v);
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		if (ratingBar == ratingBar1) {
			value1 = (int) rating;
		} else if (ratingBar == ratingBar2) {
			value2 = (int) rating;
		} else if (ratingBar == ratingBar3) {
			value3 = (int) rating;
		} else if (ratingBar == ratingBar4) {
			value4 = (int) rating;
		}
	}

	

	/**
	 * author by Ring 提交前对提交信息进行验证 return true 验证成功，false 验证失败
	 */
	public boolean textValidate() {
		String msg = "";
		if (value1 == 0) {
			if (type == 1) {
				msg = getResources().getString(R.string.help_value1_null);
			} else {
				msg = getResources().getString(R.string.help_value11_null);
			}
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), msg,
					FeedbackForPingdaiActivity.this);
			return false;
		} else if (value2 == 0) {
			if (type == 1) {
				msg = getResources().getString(R.string.help_value2_null);
			} else {
				msg = getResources().getString(R.string.help_value21_null);
			}
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), msg,
					FeedbackForPingdaiActivity.this);
			return false;
		} else if (value3 == 0) {
			if (type == 1) {
				msg = getResources().getString(R.string.help_value3_null);
			} else {
				msg = getResources().getString(R.string.help_value31_null);
			}
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), msg,
					FeedbackForPingdaiActivity.this);
			return false;
		} else if (ratingBar4.isShown() && value4 == 0) {
			msg = getResources().getString(R.string.help_value41_null);
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.prompt), msg,
					FeedbackForPingdaiActivity.this);
			return false;
		} /*
		 * else if (getCommentState()) { if(type ==1){ msg = getResources()
		 * .getString(R.string.value1_null); }else{ msg = getResources()
		 * .getString(R.string.value1_null); } MessageBox.CreateAlertDialog(
		 * getResources().getString(R.string.prompt), msg,
		 * FeedbackForPingdaiActivity.this); return false; }
		 */
		return true;
	}


	/**
	 *@author 刘星星
	 *提交反馈
	 */
	public Runnable submitFeedbackRunnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(FeedbackForPingdaiActivity.this)) {
				handler.sendEmptyMessage(5);
				String msgStr = RequestServerFromHttp.submitFeedback(type+"", getPoint(), et.getText().toString().trim());
				handler.sendEmptyMessage(6);
				if (msgStr.startsWith("0")) {
					handler.sendEmptyMessage(1);// 跳转到成功界面
				}else if(msgStr.equals("404")){
					
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
			case 1:// 跳转到成功界面
				i.setClass(FeedbackForPingdaiActivity.this,
						FeedbackSubmitSuccessActivity.class);
				FeedbackForPingdaiActivity.this.startActivity(i);
				FeedbackForPingdaiActivity.this.finish();
				break;
			case 3:// 提交失败给用户提示
				String errormsg = "";
					errormsg = getResources().getString(R.string.person_error);
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt), errormsg,
						FeedbackForPingdaiActivity.this);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(
						getResources().getString(R.string.prompt),
						getResources().getString(R.string.person_error_net),
						FeedbackForPingdaiActivity.this);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(
						FeedbackForPingdaiActivity.this);
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
	
	
	/***
	 * 拼接评分字段按前后顺序排列
	 * return string
	 */
	public String getPoint(){
		String points = "";
		if(type == 1){
			points = value1+","+value2+","+value3;
		}else{
			points = value1+","+value2+","+value3+","+value4;
		}
		return points;
	}
	
}
