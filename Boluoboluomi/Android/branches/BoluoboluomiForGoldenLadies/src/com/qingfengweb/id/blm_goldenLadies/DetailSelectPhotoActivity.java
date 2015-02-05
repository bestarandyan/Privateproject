package com.qingfengweb.id.blm_goldenLadies;

import com.qingfengweb.id.blm_goldenLadies.R;
import com.qingfengweb.data.UserBeanInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailSelectPhotoActivity extends BaseActivity {
	private Button backBtn;
	private LinearLayout centerLinear;
	private ImageView centerImage;
	private WebView bottomText;
	private boolean flag = false;// 用来标记是否查询到了照片
	private String username = "13888888888";// 用来装载会员名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailselectphoto);
		flag = getIntent().getBooleanExtra("flag", false);
		username = UserBeanInfo.getInstant().getUserName();
		findView();
	}

	private void findView() {
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		centerLinear = (LinearLayout) findViewById(R.id.centerlinear1);
		centerImage = (ImageView) findViewById(R.id.imageState);
		bottomText = (WebView) findViewById(R.id.bottomText);
		setBottomText();
	}

	/**
	 * 设置图片以及文字
	 */
	private void setBottomText() {
		bottomText.getSettings().setDefaultTextEncodingName("utf-8");
		bottomText.setBackgroundColor(0);
		bottomText.getSettings().setDefaultFontSize(19);
		if (flag) {
			centerImage.setBackgroundResource(R.drawable.image_border);
			centerImage.setImageResource(R.drawable.chajian_photo);
			bottomText.loadDataWithBaseURL("", "亲爱的<span style='color:green'>"
					+ username + "</span>会员，您的产品已全部到店，请您即时到店领取。", "text/html",
					"utf-8", "");
		} else {
			centerImage.setBackgroundResource(R.drawable.image_border);
			centerImage.setImageResource(R.drawable.logo);
			bottomText.loadDataWithBaseURL("",
					"<span style='margin-left:40px';>亲爱的<span style='color:green'>"
							+ username + "</span>会员,您的产品还在制作当中,请耐心等待,产品到件后，系统将"
							+ "会进行推送信息提示,请留意推送消息及短信,感谢您对本公司的支持。</span>",
					"text/html", "utf-8", "");
		}
	}

	@Override
	public void onClick(View v) {
		if (backBtn == v) {
			Intent i = new Intent(this, SelectPhotoActivity.class);
			startActivity(i);
			finish();
		}
		super.onClick(v);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(this, SelectPhotoActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
