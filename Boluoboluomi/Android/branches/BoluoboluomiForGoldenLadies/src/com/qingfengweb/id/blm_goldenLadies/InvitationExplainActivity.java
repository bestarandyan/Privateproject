package com.qingfengweb.id.blm_goldenLadies;

import com.qingfengweb.id.blm_goldenLadies.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class InvitationExplainActivity extends BaseActivity{
	private Button backBtn,myInviationBtn;
	private WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_invitationexplain);
		backBtn = (Button) findViewById(R.id.backBtn);
		myInviationBtn = (Button) findViewById(R.id.helpBtn);
		backBtn.setOnClickListener(this);
		myInviationBtn.setOnClickListener(this);
		wv = (WebView) findViewById(R.id.wv);
		wv.setBackgroundColor(0);
		wv.getSettings().setDefaultTextEncodingName("utf-8");
		wv.loadDataWithBaseURL("", "<span style='color:green;margin-left:20px'>电子请帖，" +
				"是用电脑处理软件，设计出非常漂亮、很具个性的请帖，功能和传统请帖相近，内容也包括地点、时间、用途、主角等。" +
				"设计完成后，通过电子邮件，即时通讯工具等网络通道，告知受邀人活动信息的一种请柬。目前这种请帖非常流行，因为" +
				"它环保、方便、实惠，符合现在提倡的低碳生活。<br><span style='margin-left:20px'>金夫人摄影于2012年1月1日全新推出属于您两的个性电子请帖。</span></span>",
				"text/html", "utf-8", "");
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v == myInviationBtn){
			Intent i = new Intent(this, InvitationTemplateActivity.class);
			startActivity(i);
		}
		super.onClick(v);
	}
}
